#!/bin/bash

#set -x
#set -e

#HDP
PLATFORM=HDP
HIVE_WAREHOUSE="/apps/hive/warehouse"
HIVE_LIB="/usr/lib/hive/lib"

#CDH
#PLATFORM=CDH
#HIVE_WAREHOUSE="/user/hive/warehouse"
#HIVE_LIB="/opt/cloudera/parcels/CDH/lib/hive/lib"


function check_not_root(){
	if [[ $EUID -eq 0 ]]; then
		echo "This feature should not be run as root. Please choose a valid HDFS user."
		exit 1
	fi
}

function generate(){
	check_not_root
	# Generate data in all formats
	cd generator

	for SCALE in 2 200 500 
	do
		./tpcds-setup.sh $SCALE text 	$HIVE_WAREHOUSE/tpcds
		./tpcds-setup.sh $SCALE rc 	$HIVE_WAREHOUSE/tpcds
		./tpcds-setup.sh $SCALE orc 	$HIVE_WAREHOUSE/tpcds
		./tpcds-setup.sh $SCALE orcs    $HIVE_WAREHOUSE/tpcds
		./tpcds-setup.sh $SCALE parquet $HIVE_WAREHOUSE/tpcds
	done
	cd ..
}

function validate(){
	check_not_root
	# Validate queries on small data set
	cd runner
#		./validate.sh tpcds_text_2 hive11 list/hive_only.csv
#		./validate.sh tpcds_text_2 impala11 list/impala_only.csv
		./validate.sh tpcds_orc_2 stinger3 list/hive13_only.csv
	cd ..
}

function runbench(){
	check_not_root
	# Run on validated queries
	cd runner
	for SCALE in 2 200 500
	do
		if [ "$PLATFORM" == "HDP" ]; then

		./benchmark.sh list/pm50.csv         tpcds_text_$SCALE      hive12    pmrun_hive12_text_$SCALE
		./benchmark.sh list/pm50.csv         tpcds_rc_$SCALE        hive12    pmrun_hive12_rc_$SCALE
		./benchmark.sh list/pm50.csv         tpcds_orc_$SCALE       hive12    pmrun_hive12_orc_$SCALE
		./benchmark.sh list/pm50.csv         tpcds_orcs_$SCALE      hive12    pmrun_hive12_orcs_$SCALE
		./benchmark.sh list/pm50.csv	     tpcds_parquet_$SCALE   hive12    pmrun_hive12_parquet_$SCALE

                ./benchmark.sh list/pm50.csv         tpcds_text_$SCALE      stinger3  pmrun_stinger3_text_$SCALE
                ./benchmark.sh list/pm50.csv         tpcds_rc_$SCALE        stinger3  pmrun_stinger3_rc_$SCALE
                ./benchmark.sh list/pm50.csv         tpcds_orc_$SCALE       stinger3  pmrun_stinger3_orc_$SCALE
                ./benchmark.sh list/pm50.csv         tpcds_orcs_$SCALE      stinger3  pmrun_stinger3_orcs_$SCALE
                ./benchmark.sh list/pm50.csv         tpcds_parquet_$SCALE   stinger3  pmrun_stinger3_parquet_$SCALE 
 
#		./benchmark.sh list/impala_only.csv	tpcds_rc_$SCALE 	hive11 pmrun_hive11_rc_$SCALE 		
#		./benchmark.sh list/impala_only.csv	tpcds_parquet_$SCALE hive11 pmrun_hive11_parquet_$SCALE 	
		elif [ "$PLATFORM" == "CDH" ]; then
		./benchmark.sh list/impala_only.csv	tpcds_parquet_$SCALE impala11 pmrun_impala11_parquet_$SCALE 	
		fi
	done
	cd ..
}

function runbench2(){
	check_not_root
	# Run on validated queries
	cd runner
	for SCALE in 1000
	do
		if [ "$PLATFORM" == "HDP" ]; then
		./benchmark.sh list/impala_only.csv	tpcds_orc_$SCALE 	hive11 pmrun_hive11_orc_${SCALE}_rerun6
		elif [ "$PLATFORM" == "CDH" ]; then
		./benchmark.sh list/impala_only.csv	tpcds_parquet_$SCALE impala11 pmrun_impala11_parquet_$SCALE 	
		fi
	done
	cd ..
}

function mkreport(){
	# Compute report
	cd runner
	for SCALE in 2 200 500
	for SYSTEM in hive12 stinger3
	do
		if [ "$PLATFORM" == "HDP" ]; then
		./makeReport.sh pmrun_${SYSTEM}_text_$SCALE     $SYSTEM
		./makeReport.sh pmrun_${SYSTEM}_rc_$SCALE       $SYSTEM
		./makeReport.sh pmrun_${SYSTEM}_orc_$SCALE	$SYSTEM
		./makeReport.sh pmrun_${SYSTEM}_orcs_$SCALE     $SYSTEM
		./makeReport.sh pmrun_${SYSTEM}_parquet_$SCALE	$SYSTEM
		elif [ "$PLATFORM" == "CDH" ]; then
		./makeReport.sh pmrun_impala11_parquet_$SCALE	impala11
		fi
	done
	cd ..
}

function usage(){
cat << EOF
Usage: pmbench <command>
   generate
   validate
   run
   report
   setup (as root)
   
On non-HDP distributions, set HIVE_LIB and HIVE_WAREHOUSE appropriately.
EOF
}

case $1 in
	
	all)
		generate
		runbench
		mkreport
	;;

	generate)
		generate
	;;

	validate)
		validate
	;;

	run)
		runbench
	;;

	report)
		mkreport
	;;

	setup)
		CURRENTPWD="$PWD"
		
		if [[ $EUID -ne 0 ]]; then
		   echo "Setup must be run as root" 1>&2
		   exit 1
		fi
		
		#installs parquet format for hive on HDP
		cd $HIVE_LIB
curl -O "http://search.maven.org/remotecontent?filepath=com/twitter/parquet-hadoop/1.2.2/parquet-hadoop-1.2.2.jar"
curl -O "http://search.maven.org/remotecontent?filepath=com/twitter/parquet-format/1.0.0-t2/parquet-format-1.0.0-t2.jar"
curl -O "http://search.maven.org/remotecontent?filepath=com/twitter/parquet-encoding/1.2.2/parquet-encoding-1.2.2.jar"
curl -O "http://search.maven.org/remotecontent?filepath=com/twitter/parquet-column/1.2.2/parquet-column-1.2.2.jar"
curl -O "http://search.maven.org/remotecontent?filepath=com/twitter/parquet-common/1.2.2/parquet-common-1.2.2.jar"
curl -O "http://search.maven.org/remotecontent?filepath=com/twitter/parquet-hive/1.2.2/parquet-hive-1.2.2.jar"
echo "Please restart the Hive Metastore to enable the parquet format."
		cd "$CURRENTPWD"
		
	;;
	*)
		usage
		exit
;;
esac
