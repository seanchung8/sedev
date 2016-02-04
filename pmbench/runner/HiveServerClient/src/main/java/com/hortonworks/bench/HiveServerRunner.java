package com.hortonworks.bench;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

import java.lang.RuntimeException;
import java.lang.ClassNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hortonworks.bench.Query;
import com.hortonworks.bench.QueryQueue;


import java.util.Date;
import java.util.Calendar;

public class HiveServerRunner implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(HiveServerRunner.class);
    private Connection con;
    private String threadName;
    private QueryQueue queryQueue;
    private int timeout;
    
    protected ServerConfiguration serverConfiguration = null;
    
    public HiveServerRunner( 
        String threadName,
        ServerConfiguration serverConfiguration,
        QueryQueue queryQueue,
        int timeout
        ) 
    {
        this.threadName = threadName;
        this.serverConfiguration = serverConfiguration;
        this.queryQueue = queryQueue;
        this.timeout = timeout;
    }
    
    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        this.con = DriverManager.getConnection(
            "jdbc:hive2://" +  
            serverConfiguration.host + ":" +
            serverConfiguration.port + "/" +
            serverConfiguration.database    
        , 
        serverConfiguration.username, 
        serverConfiguration.password
        );
    }

    public void disconnect() throws SQLException {
        con.close();
    }

    public void run() {
    try {
        LOG.info( threadName + " connecting");
        connect();
        LOG.info( threadName + "connected");
        Query query;

	Statement stmt = con.createStatement();
	stmt.execute( "use " + serverConfiguration.database );

        while( (query = queryQueue.next()) != null ) {
        
            //Statement stmt = con.createStatement();
            //stmt.setQueryTimeout(timeout); // not supported by hive
            String sql = query.sql;
            LOG.info("Running: " + sql);
        
	    String firstRow = "";
    	    // get timestamp
            Date ts_start = new Date();
            Date ts_end = null;
            try{
                ResultSet rs = stmt.executeQuery(sql);
                ts_end = new Date();
                if (rs.next()) {
                    firstRow = rs.getString(1);
                }
                rs.close();
            }
            catch( SQLTimeoutException  e){
                //Simply log to INFO. Timeout results in ts_e being null.
		throw new RuntimeException( e.getMessage(), e );
            }
	    catch( SQLException e) {
		LOG.warn( threadName + ": Query failed. Reaseon:" + e.getMessage()  );
	    }
	  if ( ts_end == null ) ts_end = new Date();
	  System.out.println( threadName + "\t" + query.name + "\t" + ts_start.getTime() + "\t" + ts_end.getTime() + "\t" + firstRow );
        }
        disconnect();
    } catch (Exception e){
        throw new RuntimeException( e.getMessage(), e );
    }
    }
    
    public static class ServerConfiguration{
            public String host = null;
            public String port = null;
            public String database = "default";
            public String username = null;
            public String password = null;
            
            public ServerConfiguration(
                String host,
                String port,
                String database,
                String username,
                String password
            ){ 
               this.host=      host;
               this.port=      port;
               this.database=  database;
               this.username=  username;
               this.password=  password;             
                
            }
            
    }
    
}

