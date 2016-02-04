INSERT INTO  TABLE ${falcon_output_database}.${falcon_output_table} PARTITION(date='${falcon_output_dated_partition_value_date}') SELECT
driverid,
  truckid,
  eventtime,
  eventtype,
  longitude,
  latitude,
  eventkey,
  correlationid,
  drivername,
  routeid,
  routename
 FROM ${falcon_input_database}.${falcon_input_table} WHERE ${falcon_input_filter};