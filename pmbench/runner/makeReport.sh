#!/bin/bash

RUN=$1
APP=$2
REPORT="runs/$RUN/report.csv"

if [ -z $RUN ]; then
	echo "Usage: makeReport.sh RUN APP"
	exit 1
fi

if [[ "$APP" == "stinger3" ]]; then
   echo "" > "$REPORT";
   for f in `ls runs/$RUN/*.results`
   do
	grep -e "query[0-9]*\.sql" -e "Time taken" $f | while read -r row ; do
		row=$(echo "$row" | tr -d '\n')
		if [[ "$row" =~ "query" ]]; 
			then 
				echo -e -n "\n$row\t" >> "$REPORT";
			else 	echo -n"$row" | awk '{print $3}' | tr -d '\n' >> "$REPORT";
		fi
	done
   done

elif [[ "$APP" == "hive11" ]]; then

echo "query	time(s)	rows" > "$REPORT"
for f in `ls runs/$RUN/*.results`
do
	query=`basename "$f"`
	rows=`tail -1 "$f" | grep -e "Time taken " | awk '{print $6}'`
	sec=`tail -1 "$f" | grep -e "Time taken" | awk '{print $3}'`
	echo "$query	$sec	$rows" >> "$REPORT"
done


 for d in `ls -d runs/$RUN/*/`
        do
                query=`basename "$d"`
                rows=""
                secs=""
                for f in `ls "$d*.results"`
                do
                        row=`tail -1 "$f" | grep -e "Time taken" | awk '{print $6}'`
                        sec=`tail -1 "$f" | grep -e "Time taken" | awk '{print $3}' | tr -d 's' `
                        rows="$rows,$row"
                        secs="$secs,$sec"
                done
        rows=`echo "$rows" | sed s/^,//`
        secs=`echo "$secs" | sed s/^,//`
                echo "$query    $secs   $rows" >> "$REPORT"
        done


elif [ "$APP" == "impala11" ]; then

	echo "query	time	rows" > "$REPORT"
	for f in `ls runs/$RUN/*.results`
	do
		query=`basename "$f" `
		rows=`tail -1 "$f" | grep -e "Returned " | awk '{print $2}'`
		sec=`tail -1 "$f" | grep -e "Returned" | awk '{print $5}' | tr -d 's' `
		echo "$query	$sec	$rows" >> "$REPORT"
	done
	
	for d in `ls -d runs/$RUN/*/`
	do
		query=`basename "$d"`
		rows=""
		secs=""
		for f in `ls "$d*.results"`
		do
			row=`tail -1 "$f" | grep -e "Returned " | awk '{print $2}'`
			sec=`tail -1 "$f" | grep -e "Returned" | awk '{print $5}' | tr -d 's' `
			rows="$rows,$row"
			secs="$secs,$sec"
		done
        rows=`echo "$rows" | sed s/^,//`
        secs=`echo "$secs" | sed s/^,//`
		echo "$query	$secs	$rows" >> "$REPORT"
	done
fi
