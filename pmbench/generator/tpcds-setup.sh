#!/bin/bash

#set -x
#set -e

# (cd tpcds-gen; make)

SCALE=$1
FORMAT=$2
DIR=$3
LIST=`cat ddl/table_list.txt`
#BUCKETS=4
#SPLIT=16
#FILE_FORMATS="orc rc" 
#SERDES=( "org.apache.hadoop.hive.ql.io.orc.OrcSerde" "org.apache.hadoop.hive.serde2.columnar.LazyBinaryColumnarSerDe" )
# STORE_CLAUSES=( "orc" )
# FILE_FORMATS=( "orc" )
# SERDES=( "org.apache.hadoop.hive.ql.io.orc.OrcSerde" )

if [ -z "$HIVE_WAREHOUSE" ]; then
	HIVE_WAREHOUSE="/apps/hive/warehouse"
fi

function usage() {
	echo "Usage: tpcds-setup.sh SCALE FORMAT HDFS_DIR"
}

if [ "$SCALE" == "" ]; then
	usage
	exit 0
fi

case $FORMAT in

	text)
# Generate the raw text files
hadoop fs -ls ${DIR}/${SCALE} || (cd tpcds-gen; hadoop jar target/*.jar -d ${DIR}/${SCALE}/ -s ${SCALE})
hadoop fs -ls ${DIR}/${SCALE}

# Declare the tables in hcatalog
hive -f ddl/tpcds.hive.sql -d DB=tpcds_text_${SCALE} -d DIR=${DIR}/${SCALE}
;;
	rc|orc|orcs|parquet)

# Generate RCFILE and ORC flat schema
	hadoop fs -ls "$HIVE_WAREHOUSE/tpcds_${FORMAT}_${SCALE}.db" && continue
	i=0
    for t in ${LIST}
    do
	echo "Processing table $i - $t"
	# single threaded to avoid problems. Should really parallelize with a max=5 or similar
	hive -f ddl/hive.${FORMAT}.template.sql -d DB=tpcds_${FORMAT}_${SCALE} -d SOURCE=tpcds_text_${SCALE} -d TABLE=$t 
#	hive -i load.sql -f ddl/hive.${FORMAT}.template.sql -d DB=tpcds_bin_flat_${FORMAT}_${SCALE} -d SOURCE=tpcds_text_${SCALE} -d TABLE=$t &
#	hive -i load.sql -f ddl/bin_flat/${t}.sql -d DB=tpcds_bin_flat_${FILE_FORMATS[$i]}_${SCALE} -d SOURCE=tpcds_text_${SCALE} -d BUCKETS=${BUCKETS} -d FILE="${file}" -d SERDE=${SERDES[$i]} -d SPLIT=${SPLIT}
	i=$(( $i + 1 ))
    done

#i=0
#for file in "${STORE_CLAUSES[@]}"
#do

# generate the partitioned schema
#    for t in ${LIST}
#    do
#	hive -i load.sql -f ddl/bin_partitioned/${t}.sql -d DB=tpcds_bin_partitioned_${FILE_FORMATS[$i]}_${SCALE} -d SOURCE=tpcds_text_${SCALE} -d BUCKETS=${BUCKETS} -d FILE="${file}" -d SERDE=${SERDES[$i]} -d SPLIT=${SPLIT}
#    done

#    i=$((i+1))
#done

;;

	*)
	usage
	exit 0
	;;
	
esac
	
