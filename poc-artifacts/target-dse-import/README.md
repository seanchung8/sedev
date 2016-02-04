# Sample Table

## Creation

    CREATE KEYSPACE esv WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
  
    CREATE TABLE users (name varchar,
    language varchar,
    schmotes int,
    votes int,
    PRIMARY KEY (name))
    WITH COMPACT STORAGE;

    insert into users (name,language,schmotes,votes) values ('test','english',1,0);
    select * from users;
    
# Building

    mvn clean package
    
# Submission

    [paul@bimota ~]$ hadoop jar ./target-dse-import-1.0-SNAPSHOT.jar com.hortonworks.target.dse.ingest.SampleFlow /user/paul/tmp
    