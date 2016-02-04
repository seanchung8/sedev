#!/bin/bash

function usage() {

cat <<EOF
hive2orc.sh <option>
	-s source_database
	-t target_database
EOF
}

while getopts ":s:t:" option; do
	case $option in
	s)
		SOURCE=${OPTARG}
		;;
	t)
		TARGET=${OPTARG}
		;;
	*)
		usage
		exit 1
		;;
	esac
done

if [[ -z $SOURCE || -z $TARGET ]]; then usage; exit 1; fi 

TABLES=`hive -e "show tables in $SOURCE" | sort`

#for table in $TABLES; do
#	echo "Processing table $table"
#	echo "hive -v -f hive.orc.template.sql -d TARGET=$TARGET -d SOURCE=$SOURCE -d TABLE=$table"
# 	hive -v -f hive.orc.template.sql -d TARGET=$TARGET -d SOURCE=$SOURCE -d TABLE=$table 
#done

# Process 5 tables at a time
HIVE="hive -v --hiveconf hive.execution.engine=tez"
echo $TABLES | tr ' ' '\n ' | xargs -P 5 -I table $HIVE -f hive.orc.template.sql -d TARGET=$TARGET -d SOURCE=$SOURCE -d TABLE=table
