#!/bin/bash

function check_existing_instances
{
	local INSTANCE_NAMES="$1"
	# Check that no instance name is already in use
	# !! This requires that the 'jenkins' user has read access to /etc/nova and write access to /var/log/nova

	ALL_INSTANCES=`nova-manage vm list | awk '{print $1}' | tail -n+2`
	#log "Search returned: $ALL_INSTANCES"	
	
	#This is horribly inefficient !
	for name in $INSTANCE_NAMES 
	do
		for existing in $ALL_INSTANCES; do
			if [ "$name" == "$existing" ] ; then
			log "Error: an instance by the name '$name' already exists."	
			exit 1
			fi
		done
	done
}

function resume_instances 
{
	local BUILD_NUMBER="$1"
	INSTANCES=$(cluster_db_get_instance_names $BUILD_NUMBER)
	for instance in $INSTANCES; do
		log "nova resume $instance"	
		OUTPUT=$(nova resume $instance)
		log2 "$OUTPUT"
	done
}

function check_existing_build
{
	local BUILD_RETURNED
	BUILD_RETURNED=$( cluster_db_get_build $1 )
	if [ "$BUILD_RETURNED" == "" ]; then
		log "Error: build $1 not found"
		exit 1
	fi
}

function generate_instance_names
{
	local BASE_NAME=$1
	local NUMBER=$2
	INSTANCE_NAMES=""
	
	if [ "$NAMING_SCHEME" == "hadoop" ]; then
		for i in $(eval echo "{1..$NUMBER}")
                do
                        if [ $i == 1 ]; then INSTANCE_NAMES="$CLUSTER"; 
			else
			    dn=$((i-1))
                            INSTANCE_NAMES="$INSTANCE_NAMES $CLUSTER-dn$dn"
        		fi
		done

	elif [ "$NAMING_SCHEME" == "natural" ]; then
              for i in $(eval echo "{1..$NUMBER}")
                do
                        if [ $i == 1 ] && [ $NUMBER == 1 ]; then INSTANCE_NAMES="$CLUSTER";
                        else
                            dn=$(printf %02d $i)
                            INSTANCE_NAMES="$INSTANCE_NAMES $CLUSTER$dn"
                        fi
                done
	else
		for i in $(eval echo "{1..$NUMBER}")
		do
        		if [ $i == 1 ]; then i=""; fi
        		INSTANCE_NAMES="$INSTANCE_NAMES $CLUSTER$i"
		done
	fi
	#export $INSTANCE_NAMES

}

function cluster_db_add
{
	local BUILD=$1
	local INSTANCE_NAME=$2
	local EXPIRATION=$3
	local BUILD_USER=$4
	log2 "mysql $MYSQL_OPTS -e \"INSERT INTO instances VALUES($BUILD,'$INSTANCE_NAME','$EXPIRATION','$BUILD_USER')\""
	mysql $MYSQL_OPTS -e "INSERT INTO instances VALUES($BUILD,'$INSTANCE_NAME','$EXPIRATION','$BUILD_USER')"
}

function cluster_db_get_build
{
	local BUILD="$1"
	local OUTPUT=`mysql $MYSQL_OPTS -B -e "select buildnb from instances where buildnb=$BUILD" `
	echo "$OUTPUT" | tail -n+2
}

function cluster_db_get_instance_names
{
	local BUILD=$1
	local BUILD_INSTANCES=`mysql $MYSQL_OPTS -B -e "select instance_name from instances where buildnb=$BUILD" `
	BUILD_INSTANCES=`echo "$BUILD_INSTANCES" | tail -n+2`
	echo $BUILD_INSTANCES
}

function cluster_db_get_expired_instance_names
{
	EXPIRATION_DATE="$1"
	local INSTANCES=`mysql $MYSQL_OPTS -B -e "select instance_name from instances where expiration < '$EXPIRATION_DATE'" `
        INSTANCES=`echo "$INSTANCES" | tail -n+2`
        echo $INSTANCES
}
function cluster_db_get_expiration
{
        local BUILD=$1
        local EXPIRATION=`mysql $MYSQL_OPTS -B -e "select expiration from instances where buildnb=$BUILD" `
        EXPIRATION=`echo "$EXPIRATION" | tail -1`
        echo "$EXPIRATION"
}

function cluster_db_list_instances
{
	mysql $MYSQL_OPTS -e "select * from instances"
}

function cluster_db_extend
{
	local BUILD=$1
	local EXPIRATION=$2
	log2 "mysql $MYSQL_OPTS -e \"UPDATE instances SET expiration='$EXPIRATION' WHERE buildnb=$BUILD\""
	mysql $MYSQL_OPTS -e "UPDATE instances SET expiration='$EXPIRATION' WHERE buildnb=$BUILD"
}

function cluster_db_delete
{
	local INSTANCE_NAME=$1
	log2 "mysql $MYSQL_OPTS -e \"DELETE FROM instances WHERE instance_name='$INSTANCE_NAME'\""
	mysql $MYSQL_OPTS -e "DELETE FROM instances WHERE instance_name='$INSTANCE_NAME'"
}

# 5 minute timeout
function get_instances_ip_address
{
       INSTANCE_NAMES="$1"
        TIMEOUT=`date -d "5 minutes" '+%s'`
        IPS=""
#echo "entering _ip_address function for $INSTANCE_NAMES" 1>&2
        for INSTANCE_NAME in $INSTANCE_NAMES
        do
#echo "Checking instance: $INSTANCE_NAME" 1>&2
                while [[ `date '+%s'` -le "$TIMEOUT" ]]; do
                        OUTPUT=`nova show "$INSTANCE_NAME"`
#echo "$OUTPUT" 1>&2
                        STATUS=`echo "$OUTPUT" | grep ' status ' | awk '{print $4}' | tr -d ' '`
#echo "|$STATUS|" 1>&2
                        if [ "$STATUS" == "ACTIVE" ]; then
                                IP=`echo "$OUTPUT" | grep ' vmnet network ' | awk -F '|' '{print $3}' | tr -d ' '`
                                IPS="$IPS $IP"
#echo "STATUS is ACTIVE. IP is $IP" 1>&2
                                break
                        fi
			sleep 1
                done
        done
        echo "$IPS"
}

# Wait up to 2mn for instances to be ready
# Return a warning if it fails by the timeout
function wait_until_ips_up
{
	IPS="$1"
	TIMEOUT=`date -d "2 minutes" '+%s'`
	for ip in $IPS
	do
		while [[ `date '+%s'` -le "$TIMEOUT" ]]; do
		
		ping -c 1 "$ip" > /dev/null
		if [[ $? -eq 0 ]]; then
			break
		fi
		sleep 1
		done	
	done

	if [[ `date '+%s'` -gt "$TIMEOUT" ]]
	then
		echo "timeout"
	else
		echo "up"
	fi
}

#Must be run as root; this is only for reference
function cluster_db_init
{
	mysql  << EOF

create database if not exists oslaunch;
create table if not exists oslaunch.instances(
	buildnb	bigint not null,
	instance_name	varchar(255),
	expiration	timestamp,
	build_user	varchar(255)
);

CREATE USER 'jenkins'@'localhost'
IDENTIFIED BY 'jenkins';
GRANT ALL PRIVILEGES ON oslaunch . * TO 'jenkins'@'localhost';
FLUSH PRIVILEGES;

EOF
}

#Requires LOG_FILE
function log
{
	echo "$1"
	echo `date` $BUILD_NUMBER "$1" >> "$LOG_FILE"
}

function log2
{
	echo `date` $BUILD_NUMBER "$1" >> "$LOG_FILE"
}

