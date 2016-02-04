# EHI Project

This project processes EDIFACT data simulated by the stream-simulator, and processed using Storm.  Storm uses HBase to catalog rates, and detects rate changes.  Those rate changes are sent to MySQL and to Elasticsearch.  MySQL is used to simulate a staging table that is loaded into a production system to improve price optimization, and Elasticsearch is used to analyze and visualize the changes.

## MYSQL
Run this script in the terminal while logged into mysql shell as root.

    CREATE USER 'ehi'@'%' identified by 'ehi';
    CREATE USER 'ehi'@'localhost' identified by 'ehi';
    CREATE DATABASE rates;
    USE rates; 
    create table rates (LOR varchar(100), Rental_Location varchar(200), Arrival_Date varchar(200), Upload_Timestamp varchar(200), 
    Total_Rate float, Prev_Total_Rate float, Total_Rate_Change varchar(50), Car_Class varchar(100), Brand varchar(100), 
    Rate_Code varchar(100));
    DELIMITER //
    CREATE PROCEDURE InsertRates 
    (
    	IN _LOR varchar(100), IN _rental_Location VARCHAR(200), IN _arrival_Date varchar(200), IN _upload_Timestamp varchar(200),
     	IN _total_Rate FLOAT, _prev_Total_Rate FLOAT, _car_Class varchar(100), _brand varchar(100), _Rate_Code varchar(100)
    )
    BEGIN 
    INSERT INTO rates 
    	(LOR, Rental_Location, Arrival_Date, Upload_Timestamp, Total_Rate, Prev_Total_Rate, Total_Rate_Change, Car_Class, Brand, Rate_Code) 
    VALUES 
    	(_LOR, _rental_Location, _arrival_Date, _upload_Timestamp, _total_Rate, _prev_Total_Rate, 
    	(CASE WHEN _total_Rate - _prev_Total_Rate < 0 THEN 'DOWN' WHEN _total_Rate - _prev_Total_Rate > 0 THEN 'UP' ELSE NULL END),
    	_car_Class, _brand, _rate_Code); 
    END;
    //
    GRANT ALL ON rates.* TO 'ehi'@'%';
    GRANT ALL ON rates.* TO 'ehi'@'localhost';
    GRANT SELECT ON mysql.proc TO 'ehi'@'%';
    GRANT SELECT ON mysql.proc TO 'ehi'@'localhost';
    flush privileges;

## HBase
    
    hbase shell
    hbase> create 'rates', 'rate'

Row key needs is a composite key "rentalservice + rate type + location" CF will be rate and there will be the following quals:

- "ECAR"
- "CCAR"
- "ICAR"
- "SCAR"
- "FCAR"
- "PCAR"
- "LCAR"

# Elasticsearch

## Elasticsearch

    rpm -ivh https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.7.noarch.rpm

## Kibana

    wget https://download.elasticsearch.org/kibana/kibana/kibana-3.0.0milestone4.zip
    unzip kibana-3.0.0milestone4.zip -d /var/www/html/kibana

# Apache Solr

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

    $ wget https://github.com/LucidWorks/banana/archive/banana-1.1.tar.gz
    
Untar Solr and Banana

    $ unzip solr-4.7.2.zip
    $ tar -zxvf /opt/solr/banana-1.2.tar.gz
    $ cd banana-banana-1.2/
    $ vi src/config.js
    $ ant
    $ cp -r solr-4.5.0/kibana-int solr-4.7.2/example/solr/
    $ cp build/banana*.war solr-4.7.2/example/solr/webapps
    $ cd solr-4.7.2/example
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
 
Repeat a similar process to add the EHI core once you've created the directory and put in the starting solrconfig.xml, and schema.xml (found in ./solr/schema.xml of this project).

    $ mkdir /opt/solr/solr-4.7.2/example/solr/ehi
    $ cp -ra /opt/solr/solr-4.7.2/example/collection1/conf /opt/solr/solr-4.7.2/example/solr/ehi
    $ cp $THIS_PROJECT_PATH/solr/schema.xml /opt/solr/solr-4.7.2/example/solr/ehi/conf

Now from the Solr Admin UI:

 * Click on *Core Admin* from the left-side menu
 * Click on the *Add Core* button
 * Fill in the form with the following data

<table style="border: 1px solid black">
    <tr><td><strong>name</strong></td><td>ehi</td></tr>
    <tr><td><strong>instanceDir</strong></td><td>/opt/solr/solr-4.7.2/example/solr/ehi</td></tr>
    <tr><td><strong>dataDir</strong></td><td>data</td></tr>
    <tr><td><strong>config</strong></td><td>solrconfig.xml</td></tr>
    <tr><td><strong>schema</strong></td><td>schema.xml</td></tr>
</table>

 * Click on the *Add Core* button
 
Once this has been done, you can move on to the next step.

# Pre-Req's

You need the following projects to get this working:

1. Active MQ
2. Stream-Simulator
3. MySQL
4. storm-jms

## Configuring

All configuration for HBase, ActiveMQ, MySQL etc. is located in the `multilang/config.properties` file.

## Building

You need to have maven installed and git.  From there you can use Eclipse to get the project checked out from Git and built with Maven.  One pre-req project that needs to be built before the project will build is storm-jms:

    git clone https://github.com/pcodding/storm-jms.git
    mvn clean install
  
Update the configuration to tell the ehi topology where to talk to ActiveMQ, Elasticsearch, and Solr by editing the src/main/resources/config.properties file.  Once this bas been done you can build the project by cd'ing to the directory in which you've checked out this project.

To build this project, there are two modes that Storm can be started in local, and cluster.  We use local mode for testing in your development JVM, and cluster to deploy to YARN.  Switching between the profiles can be done through using `-DdeployTo=local`, or `-DdeployTo=cluster`.  To build for your local environment, do this:

    mvn -DdeployTo=local clean package
    
To build a package to deploy to YARN, do this:

    mvn -DdeployTo=cluster clean package
    
**If you don't want to have to specify the deployTo profile every time, just edit you ~/.m2/settings.xml and set the default profile:**

    $ cat ~/.m2/settings.xml 
    <settings>
      <activeProfiles>
      	<activeProfile>local</activeProfile>
      </activeProfiles>
    </settings>

Once you've done that, a simple mvn clean package will run without having to specify the profile.

## Running

1.) Make sure ActiveMQ is started

2.) Run the stream simulator with the following params

    cd stream-simulator
    ./run.sh 1 -1 com.hortonworks.streaming.impl.domain.rental.RequestInitiator com.hortonworks.streaming.impl.collectors.JmsEventCollector

3.) Start the standalone storm topology

    cd ehi
    ./run.sh
    
    
## Cleaning up
### Deleting Indexes
#### ES

    curl -XDELETE 'http://es.host.com:9200/ehi'

#### Solr

    curl http://solr.host.com:8983/solr/ehi/update -H "Content-Type: text/xml" --data-binary '<delete><query>*:*</query></delete>'