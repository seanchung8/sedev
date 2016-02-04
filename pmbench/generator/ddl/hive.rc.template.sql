create database if not exists ${DB};
use ${DB};

drop table ${TABLE};
create table ${TABLE} 
STORED AS RCFILE
AS 
  SELECT * FROM ${SOURCE}.${TABLE}
;