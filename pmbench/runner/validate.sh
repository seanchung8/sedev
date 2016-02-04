#!/bin/bash

set -x
#set -e -- WARNING: if set, the script will stop when encountering an error, such as a hive failure.

QUERIES=`ls queries/*.sql`
DATABASE="$1"
APP="$2"
VALIDQUERIES="$3"
LOG="$3.log"

if [ "$APP" == "impala11" ]; then
		#Impala must be forced to re-read the metastore
		impala-shell -q "quit;" -r
fi


#Exit if the file already exists
[ -f "$VALIDQUERIES" ] && exit

mkdir -p "list"
echo -n "" > "$VALIDQUERIES"
echo -n "" >  "$LOG"

for query in $QUERIES
do
	STATUS=UNKNOWN
	queryname=$(basename "$query")
	sql=`cat "$query"`
	sql="use $DATABASE; explain $sql"

	#Set 'output' as needed
	case "$APP" in
		hive11)
			output=`hive -e "$sql" 2> /dev/null`
			echo "$queryname" >> "$LOG"
			echo "$output" >> "$LOG"
			if result=`echo "$output" | head -1 | grep FAILED`; 
				then STATUS=FAILED; 
				else STATUS=SUCCESS;
				fi
		;;
		impala11)
			output=`impala-shell -q "$sql" 2>&1`
			echo "$queryname" >> "$LOG"
			echo "$output" >> "$LOG"
			#if result=`echo "$output" | grep -e "Syntax error" -e "ERROR: "`; then STATUS=FAILED; fi
			if result=`echo "$output" | grep -e "Query finished, fetching results"`; 
				then STATUS=SUCCESS; 
				else STATUS=FAILED
			fi
		;;
		stinger3)
                        output=`/opt/hive/bin/hive  -hiveconf hive.optimize.tez=true -hiveconf hive.input.format=org.apache.hadoop.hive.ql.io.HiveInputFormat  -e "$sql" 2> /dev/null`
                        echo "$queryname" >> "$LOG"
                        echo "$output" >> "$LOG"
                        if result=`echo "$output" | head -1 | grep FAILED`;
                                then STATUS=FAILED;
                                else STATUS=SUCCESS;
                                fi
		;;
		*)
			exit 1
		;;
	esac

	if [[ "$STATUS" == "SUCCESS" ]]; then
		echo "$queryname is valid"
		echo "$queryname" >> "$VALIDQUERIES"
	else
		echo "$queryname failed"
	fi
done
