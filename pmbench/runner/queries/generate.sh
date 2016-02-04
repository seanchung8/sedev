#!/bin/sh

set -x

INIT=""
if [ X"$1" != "X" ]; then
	INIT="-i $1"
fi
DATABASE=orc
if [ X"$2" != "X" ]; then
	DATABASE="$2"
fi
if [ X"$HIVE" -eq "X" ]; then
	HIVE=hive
fi

RUNID=$$
mkdir $RUNID
for f in *.sql; do
	$HIVE $INIT --database $DATABASE -f $f > $RUNID/$f.results 2>&1;
done
