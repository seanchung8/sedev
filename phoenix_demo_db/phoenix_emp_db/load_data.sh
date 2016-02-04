hbasemaster=$1
basedir=$(pwd)
psql=$(find / -name psql.py)
$psql $hbasemaster:2181:/hbase-unsecure /$basedir/departments.csv
$psql $hbasemaster:2181:/hbase-unsecure /$basedir/dept_emp.csv
$psql $hbasemaster:2181:/hbase-unsecure /$basedir/dept_manager.csv
$psql $hbasemaster:2181:/hbase-unsecure /$basedir/employees.csv
$psql $hbasemaster:2181:/hbase-unsecure /$basedir/salaries.csv
$psql $hbasemaster:2181:/hbase-unsecure /$basedir/titles.csv
