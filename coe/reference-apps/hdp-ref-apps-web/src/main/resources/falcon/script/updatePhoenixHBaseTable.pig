A = LOAD '${falcon_input_database}.${falcon_input_table}' USING org.apache.hcatalog.pig.HCatLoader();
B = filter A by ${falcon_input_filter};
C = FOREACH B GENERATE eventkey, driverid, truckid, eventtime, latitude, longitude, eventtype, drivername, routeid, routename;
STORE C into 'hbase://TRUCK.EVENTS_2' using org.apache.phoenix.pig.PhoenixHBaseStorage('vett-cluster01.cloud.hortonworks.com:2181:/hbase-unsecure','-batchSize 5000');