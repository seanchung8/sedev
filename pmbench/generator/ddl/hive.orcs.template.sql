create database if not exists ${DB};
use ${DB};

drop table ${TABLE};

create table ${TABLE} 
like ${SOURCE}.${TABLE};

alter table ${TABLE}
set fileformat ORC;

alter table ${TABLE}
set tblproperties ("orc.compress"="SNAPPY");


INSERT OVERWRITE TABLE ${TABLE}
  SELECT * FROM ${SOURCE}.${TABLE}
;
