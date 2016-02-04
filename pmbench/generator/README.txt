Based on Stinger-Benchmark from Hortonworks Github + Stinger Demo.

tpcds generation code for all the tables.

generates it for various scale factors.

You need gcc, yacc, flex installed on the machine to compile the dsgen program

./tpcds-setup.sh <SCALE> <HDFSDIR>

or 

./tpcds-setup.sh 2 /user/hive/external/
