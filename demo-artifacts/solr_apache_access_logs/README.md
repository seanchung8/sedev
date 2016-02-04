# Apache Access Logs Demo

## Solr

Create the solr user and add a password

    # adduser solr
    # passwd solr

Create a location for the installation

    # mkdir /opt/solr
    # chown solr:solr /opt/solr

### Download and Install

SU to the solr user

    # su solr
    $ cd /opt/solr

Download Solr from http://lucene.apache.org/solr/downloads.html

    $ wget http://archive.apache.org/dist/lucene/solr/4.7.2/solr-4.7.2.zip
    
Download Banana from Github

    $ wget https://github.com/LucidWorks/banana/archive/banana-1.2.tar.gz
    
Untar Solr and Banana

    $ unzip solr-4.7.2.zip
    $ tar -zxvf /opt/solr/banana-1.2.tar.gz
    $ cd banana-banana-1.2/

Edit the Banana Config and update the `solr: "http://..."` property with your Solr Host.

    $ vi src/config.js
    
Build Banana

    $ mkdir build
    $ ant

Deploy Banana    

    $ cp -r solr-4.5.0/kibana-int ../solr-4.7.2/example/solr/
    $ cp build/banana*.war ../solr-4.7.2/example/webapps/banana.war
    $ cd ../solr-4.7.2/example

Create a new context file here `/opt/solr/solr-4.7.2/example/contexts/banana-context.xml` and add the following contents:

    <?xml version="1.0"?>
    <!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "http://www.eclipse.org/jetty/configure.dtd">
    <Configure class="org.eclipse.jetty.webapp.WebAppContext">
      <Set name="contextPath"><SystemProperty name="hostContext" default="/banana"/></Set>
      <Set name="war"><SystemProperty name="jetty.home"/>/webapps/banana.war</Set>
      <Set name="tempDirectory"><Property name="jetty.home" default="."/>/banana-webapp</Set>
    </Configure>

Start Solr: 
    $ java -jar start.jar

Solr should now be deployed and started, validate that by navigating to http://your.host.com:8983/solr.  The next step is to focus on creating the "kibana-int" core, after which we'll ingest some data into.  This core is used to save stored Banana dashboards and is optional, but recommended.

 * Click on *Core Admin* from the left-side menu
 * Click on the *Add Core* button
 * Fill in the form with the following data

<table style="border: 1px solid black">
    <tr><td><strong>name</strong></td><td>kibana-int</td></tr>
    <tr><td><strong>instanceDir</strong></td><td>/opt/solr/solr-4.7.2/example/solr/kibana-int</td></tr>
    <tr><td><strong>dataDir</strong></td><td>data</td></tr>
    <tr><td><strong>config</strong></td><td>solrconfig.xml</td></tr>
    <tr><td><strong>schema</strong></td><td>schema.xml</td></tr>
</table>

 * Click on the *Add Core* button

Now, add the Apache Access Logs core once you've created the directory and put in the starting solrconfig.xml, and schema.xml (found in ./solr/schema.xml of this project).

    $ mkdir /opt/solr/solr-4.7.2/example/solr/access_logs
    $ cp -ra /opt/solr/solr-4.7.2/example/solr/collection1/conf /opt/solr/solr-4.7.2/example/solr/access_logs/conf
    $ cp $THIS_PROJECT_PATH/solr/schema.xml /opt/solr/solr-4.7.2/example/solr/access_logs/conf
    $ cd /opt/solr/solr-4.7.2/example
    $ java -jar start.jar

Now from the Solr Admin UI:

 * Click on *Core Admin* from the left-side menu
 * Click on the *Add Core* button
 * Fill in the form with the following data

<table style="border: 1px solid black">
    <tr><td><strong>name</strong></td><td>access_logs</td></tr>
    <tr><td><strong>instanceDir</strong></td><td>/opt/solr/solr-4.7.2/example/solr/access_logs</td></tr>
    <tr><td><strong>dataDir</strong></td><td>data</td></tr>
    <tr><td><strong>config</strong></td><td>solrconfig.xml</td></tr>
    <tr><td><strong>schema</strong></td><td>schema.xml</td></tr>
</table>

 * Click on the *Add Core* button
 
Once this has been done, you can move on to the next step, which is loading data.

# Data

  * You can get the compressed data from here: https://hortonworks.app.box.com/files/0/f/2163570912/1/f_18694371314
  * Decompress the file and put it in HDFS under a directory owned by the user that will execute the following pig script (e.g., /user/solr/data/apache/access)
  
# Pig

Use this Pig Script to Load the data:

    -- Index Apache Access Log
    -- Author: Paul Codding
    -- hadoop fs -mkdir -p /user/paul/data/apache/access
    -- pig -f index_logs.pig
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
          status:        chararray, 
          bytes_string:  chararray, 
          referrer:      chararray, 
          browser:       chararray
      );
    ACCESS_LOGS_CLEAN = FILTER ACCESS_LOGS by time is not null;
    ACCESS_LOGS_MIN = FOREACH ACCESS_LOGS_CLEAN GENERATE id, 'remoteAddr', remoteAddr, 'remoteLogname', remoteLogname, 'time', CustomFormatToISO(time, 'dd/MMM/yyyy:HH:mm:ss Z') as time, 'request', request, 'status', status, 'bytes_string', bytes_string, 'referrer', referrer, 'browser', browser;
    --DUMP ACCESS_LOGS_MIN;
    STORE ACCESS_LOGS_MIN into 'http://your.host.local:8983/solr/' using com.lucidworks.hadoop.pig.SolrStoreFunc();

## Cleanup

Use this URL to delete all documents within the Index

    http://your.host.local:8983/solr/access_logs/update?stream.body=%3Cdelete%3E%3Cquery%3Eid:*%3C/query%3E%3C/delete%3E&commit=true
    
## Banana

Open the Banana interface at http://your.host.local:8983/banana 

Modify the line near the end of the end of the `banana/AccessLogs.json` so it points to your local instance of Solr.

Import the `banana/AccessLogs.json` file using "Load -> Local File -> Choose File".
