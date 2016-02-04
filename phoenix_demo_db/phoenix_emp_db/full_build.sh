read -p "Enter HBase Master FQDN: " hbasemaster
basedir=$(pwd)
sqlline=$(find / -name sqlline.py)

echo $hbasemaster
echo $basedir
echo $sqlline

$sqlline $hbasemaster:2181:/hbase-unsecure /$basedir/drop_employees_tables.sql
$sqlline $hbasemaster:2181:/hbase-unsecure /$basedir/create_salted_employees_tables.sql
$basedir/load_data.sh $hbasemaster
