-- Index Apache Access Log
-- Author: Paul Codding
-- hadoop fs -mkdir -p /user/paul/data/apache/access
-- pig -f index_logs.pig -p solrUrl=http://bimota.hortonworks.local:8983/solr/access_logs
set solr.collection 'access_logs';

register /home/paul/pig/hadoop-lws-job-1.2.0-0-0.jar;
register /home/paul/pig/datafu-1.2.0.jar;
register /usr/lib/pig/lib/piggybank.jar;

DEFINE EXTRACT org.apache.pig.piggybank.evaluation.string.RegexExtractAll;
DEFINE CustomFormatToISO org.apache.pig.piggybank.evaluation.datetime.convert.CustomFormatToISO();
define MD5 datafu.pig.hash.MD5();

RAW_LOGS = LOAD '/user/paul/data/apache/access' USING TextLoader as (line:chararray);

ACCESS_LOGS = FOREACH RAW_LOGS GENERATE MD5(line) as (id:chararray), 
    FLATTEN( 
      EXTRACT(line, '^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] "(.+?)" (\\S+) (\\S+) "([^"]*)" "([^"]*)"')
    ) 
    as (
      remoteAddr:    chararray, 
      remoteLogname: chararray, 
      user:          chararray, 
      time:          chararray, 
      request:       chararray, 
      status:        int, 
      bytes_string:  chararray, 
      referrer:      chararray, 
      browser:       chararray
  );
ACCESS_LOGS_CLEAN = FILTER ACCESS_LOGS by time is not null;
ACCESS_LOGS_MIN = FOREACH ACCESS_LOGS_CLEAN GENERATE id, 'remoteAddr', remoteAddr, 'remoteLogname', remoteLogname, 'time', CustomFormatToISO(time, 'dd/MMM/yyyy:HH:mm:ss Z') as time, 'request', request, 'status', status, 'bytes_string', bytes_string, 'referrer', referrer, 'browser', browser;
--DUMP ACCESS_LOGS_MIN;
STORE ACCESS_LOGS_MIN into 'http://bimota.hortonworks.local:8983/solr/' using com.lucidworks.hadoop.pig.SolrStoreFunc();