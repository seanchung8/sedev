set solr.collection 'truck_event_logs';

DEFINE CustomFormatToISO org.apache.pig.piggybank.evaluation.datetime.convert.CustomFormatToISO();

A = LOAD '${falcon_input_database}.${falcon_input_table}' USING  org.apache.hive.hcatalog.pig.HCatLoader();
B = filter A by ${falcon_input_filter};
C = FOREACH B GENERATE  eventkey, 'driverid', driverid, 'truckid', truckid, 'time',  CustomFormatToISO(eventtime, 'yyyy-MM-dd HH:mm:ss.SSS') as time, 'eventtype', eventtype, 'longitude', longitude, 'latitude', latitude,  'correlationid', correlationid,'drivername', drivername,'routeid', routeid,'routename', routename;
--dump C;
STORE C into 'http://george-search01.cloud.hortonworks.com:8983/solr/' using com.lucidworks.hadoop.pig.SolrStoreFunc();
