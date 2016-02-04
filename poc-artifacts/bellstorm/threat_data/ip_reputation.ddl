CREATE TABLE
ip_reputation_data (ip STRING, exploit_type STRING, score STRING, created STRING, modified STRING, count STRING, hashcode STRING)
STORED BY 'org.apache.hcatalog.hbase.HBaseHCatStorageHandler'
TBLPROPERTIES (
  'hbase.table.name' = 'ip_reputation_data',
  'hbase.columns.mapping' = 'd:exploit_type,d:score,d:created,d:modified,d:count,d:hashcode',
  'hcat.hbase.output.bulkMode' = 'true'
);