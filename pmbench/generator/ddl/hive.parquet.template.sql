create database if not exists ${DB};
use ${DB};

set HIVE_DIR=/opt/cloudera/parcels/CDH/lib/hive/lib;
add jar  
${hiveconf:HIVE_DIR}/parquet-column-1.2.2.jar 
${hiveconf:HIVE_DIR}/parquet-common-1.2.2.jar 
${hiveconf:HIVE_DIR}/parquet-encoding-1.2.2.jar 
${hiveconf:HIVE_DIR}/parquet-format-1.0.0-t2.jar  
${hiveconf:HIVE_DIR}/parquet-hadoop-1.2.2.jar  
${hiveconf:HIVE_DIR}/parquet-hive-1.2.2.jar;

drop table ${TABLE};
create table ${TABLE} 
ROW FORMAT SERDE 'parquet.hive.serde.ParquetHiveSerDe'
STORED AS INPUTFORMAT 'parquet.hive.DeprecatedParquetInputFormat'
      OUTPUTFORMAT 'parquet.hive.DeprecatedParquetOutputFormat'
AS 
  SELECT * FROM ${SOURCE}.${TABLE}
;