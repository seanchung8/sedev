#!/bin/bash

#export OS_USERNAME=wfb
#export OS_TENANT_NAME=Wellsfargo
#export OS_PASSWORD=wfbhorton
#export OS_AUTH_URL=http://192.168.16.1:35357/v2.0/

export OS_USERNAME=se
export OS_TENANT_NAME=secloud
export OS_PASSWORD=hadoop
export OS_AUTH_URL=http://192.168.16.1:35357/v2.0/

KEYPAIR=secloud
COMMAND="$1"
LOG_FILE=/var/log/jenkins/oslaunch.log
MYSQL_OPTS="-u jenkins --password=jenkins oslaunch"

source /home/secloud/scripts/oslib.sh

BUILD_USER="$2"
log "COMMAND=$1. Started by user $BUILD_USER."

function usage
{
        echo "Called by Jenkins"
	echo "Usage: oslaunch.sh <command>"
        echo "Command: startvminstances <ENV: BUILD_NUMBER CLUSTER NUMBER_OF_INSTANCES>"
        echo "Command: deletevminstances <ENV: BUILD_NUMBER>"
	echo "Command: extendvminstances <ENV: BUILD_NUMBER TIME_HOURS>"
	echo "Command: listvminstances"
	echo "Command: deleteexpiredinstances"
	exit 1
}

#test data
if [ "$2" == debug ]; then
IMAGE="Centos 6.4"
FLAVOR=m1.medium
CLUSTER=test
NUMBER_OF_INSTANCES=4
BUILD_NUMBER=-1
fi

case "$COMMAND" in

	"startvminstances") 
if [[ -z "$NUMBER_OF_INSTANCES" ]]; then NUMBER_OF_INSTANCES=1; fi 
log "****************************"
log "BUILD_NUMBER=$BUILD_NUMBER"
log "****************************" 
log "CLUSTER=$CLUSTER NUMBER_OF_INSTANCES=$NUMBER_OF_INSTANCES"
generate_instance_names $CLUSTER $NUMBER_OF_INSTANCES
log "Generated Instances Names: $INSTANCE_NAMES"

check_existing_instances "$INSTANCE_NAMES"

EXPIRATION=`date --date="$LIFETIME hours" '+%Y-%m-%d %H:%I:%S'`

echo "Launching VMs. They will expire on $EXPIRATION."
for INSTANCE_NAME in $INSTANCE_NAMES 
do
	echo "Launching $INSTANCE_NAME"
	log "nova boot --key-name=$KEYPAIR --image=\"$IMAGE\" --flavor=\"$FLAVOR\" \"$INSTANCE_NAME\""
	OUTPUT=$(nova boot --key-name=$KEYPAIR --image="$IMAGE" --flavor="$FLAVOR" "$INSTANCE_NAME" )
	log2 "$OUTPUT"
	cluster_db_add $BUILD_NUMBER "$INSTANCE_NAME" "$EXPIRATION" "$BUILD_USER"
done

echo "Spawning virtual machines and assigning network ..."
INSTANCE_IPS=$( get_instances_ip_address "$INSTANCE_NAMES" )
echo "done."

echo "$INSTANCE_IPS"

echo "Waiting for instances to boot up ..."
wait_until_ips_up "$INSTANCE_IPS"


echo ""
echo "-------  HOSTNAMES  -------"
for INSTANCE_NAME in $INSTANCE_NAMES
do
	echo "$INSTANCE_NAME.secloud.hortonworks.com"
done
echo "-------  IPs  -------------"
for IP in $INSTANCE_IPS
do
	echo "$IP"
done
echo "---------------------------"
echo ""
;;


	"deletevminstances")
log "deletevminstances: BUILD_NUMBER=$BUILD_NUMBER"

check_existing_build "$BUILD_NUMBER"

INSTANCE_NAMES=$( cluster_db_get_instance_names $BUILD_NUMBER )
for INSTANCE_NAME in $INSTANCE_NAMES
do
	log "nova delete \"$INSTANCE_NAME\""
	nova delete "$INSTANCE_NAME"
	cluster_db_delete "$INSTANCE_NAME"
done

;;

	"extendvminstances")
log "extendvminstances: BUILD_NUMBER=$BUILD_NUMBER"
check_existing_build "$BUILD_NUMBER"

EXPIRATION=$( cluster_db_get_expiration $BUILD_NUMBER )
log "Current expiration is $EXPIRATION"
if [ "$EXTEND_LIFETIME" == "unlimited" ]; then
EXTEND_LIFETIME=87600
fi
EXPIRATION=`date -d "$EXPIRATION $EXTEND_LIFETIME hours" '+%Y-%m-%d %H:%I:%S'`
cluster_db_extend $BUILD_NUMBER "$EXPIRATION"
log "Extended instances to $EXPIRATION"
;;

	"resumevminstances")
log "resumevminstances: BUILD_NUMBER=$BUILD_NUMBER"
check_existing_build "$BUILD_NUMBER"

EXPIRATION=$( cluster_db_get_expiration $BUILD_NUMBER )
log "Current expiration is $EXPIRATION"
#start from current time
EXPIRATION=`date -d "$EXTEND_LIFETIME hours" '+%Y-%m-%d %H:%I:%S'`
cluster_db_extend $BUILD_NUMBER "$EXPIRATION"
resume_instances $BUILD_NUMBER
#wait for instances to be resumed. Duplicate code from startvminstances needs cleanup
INSTANCE_NAMES=$(cluster_db_get_instance_names $BUILD_NUMBER)
INSTANCE_IPS=$(get_instances_ip_address $INSTANCE_NAMES)
log "Resuming and extending instances to $EXPIRATION"
wait_until_ips_up "$INSTANCE_IPS"
log "Complete"
;;

	"listvminstances")
cluster_db_list_instances
;;

	"deleteexpiredinstances")
# We first suspend the instance, and delete it 2 weeks later
EXPIRATION_DATE=`date -d "-2 hours" '+%Y-%m-%d %H:%M:%S'`
INSTANCES_TO_SUSPEND=$( cluster_db_get_expired_instance_names "$EXPIRATION_DATE" )
#note: duplicates deletevminstances
log "Instances to suspend: $INSTANCES_TO_SUSPEND"
for INSTANCE_NAME in $INSTANCES_TO_SUSPEND
do
        log "nova suspend \"$INSTANCE_NAME\""
        nova suspend "$INSTANCE_NAME"
        #cluster_db_delete "$INSTANCE_NAME"
done

# We first suspend the instance, and delete it 2 weeks later
EXPIRATION_DATE=`date -d "-2 weeks" '+%Y-%m-%d %H:%M:%S'`
INSTANCES_TO_DELETE=$( cluster_db_get_expired_instance_names "$EXPIRATION_DATE" )
#note: duplicates deletevminstances
log "Instances to delete: $INSTANCES_TO_DELETE"
for INSTANCE_NAME in $INSTANCES_TO_DELETE
do
        log "nova delete \"$INSTANCE_NAME\""
        nova delete "$INSTANCE_NAME"
        cluster_db_delete "$INSTANCE_NAME"
done


;;

	*) usage ;;
esac

