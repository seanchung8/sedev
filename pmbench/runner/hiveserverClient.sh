#!/bin/bash

#set -x
#set -e
#./hiveserverClient.sh hive13 localhost 10001 tpcds_bin_partitioned_orc_10 hive hive list/simple.csv 5 20 600

SYSTEM="$1"
HOST="$2"
PORT="$3"
DATABASE="$4"
USERNAME="$5"
PASSWORD="$6"
QUERY_LIST="$7"
THREADS="$8"
MAXQ="$9"
TIMEOUT="${10}"

#RUNDIR="runs/$RUN"
mkdir -p report

CLASSPATH="hive-jdbc-libs-0.12/*"
CLASSPATH=".:$PWD/HiveServerClient/target/HiveServerClient-1.0-SNAPSHOT-jar-with-dependencies.jar:$CLASSPATH"
java -cp $CLASSPATH com.hortonworks.bench.HiveServerClient "$SYSTEM" "$HOST" "$PORT" "$DATABASE" "$USERNAME" "$PASSWORD" "$QUERY_LIST"  "$THREADS" "$MAXQ" "$TIMEOUT"


