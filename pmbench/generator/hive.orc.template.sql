set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;

create database if not exists ${TARGET};

drop table ${TARGET}.${TABLE};

create table ${TARGET}.${TABLE} like ${SOURCE}.${TABLE};

-- can't use ${TARGET}.${TABLE} syntax
use ${TARGET};
alter table ${TABLE} set fileformat ORC;

INSERT OVERWRITE TABLE ${TARGET}.${TABLE}
SELECT * FROM ${SOURCE}.${TABLE}
;
