package com.hortonworks.bench;

import com.hortonworks.bench.Query;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.File;
import java.nio.charset.Charset;
   
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class QueryQueue{
   
    private static final Logger LOG = LoggerFactory.getLogger(HiveServerRunner.class); 
    private int maxqueries;
    List<Query> queries = new ArrayList<Query>();
    int current = -1;


    static String readFile(String path, Charset encoding) 
      throws IOException 
    {
        return new Scanner(new File( path )).useDelimiter("\\Z").next();
//java 7 only
//      byte[] encoded = File.readAllBytes(Paths.get(path));
//      return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }
    
    private void loadFromDisk( String queryList )
        throws IOException
    {
        
        BufferedReader br = new BufferedReader(new FileReader(queryList));  
        String path = null;  
        int i = 0;
        while ((path = br.readLine()) != null)  
        {  
	    i++;
	    try {
            Query query = new Query( 
                Integer.toString(i), 
                readFile( path, Charset.defaultCharset() )
            );
	    queries.add ( query );
	    } catch( FileNotFoundException e ) {
		LOG.warn("File not found:" + e.getMessage()) ;
		}
        } 
	if (i == 0 ) LOG.warn( "QueryQueue.loadFromDisk: no queries loaded");
    }

    public QueryQueue(
        String queryList, 
        int maxqueries 
        )
        throws IOException
    {
        this.maxqueries = maxqueries;
        loadFromDisk(queryList);
    }
    
    public synchronized Query next(){
        LOG.debug( "current = " + current + " maxqueries=" + maxqueries + " queries.size()=" + queries.size() );
	if ( current >= maxqueries -1 )
            return null;
        current++;
        if( current >= queries.size() )
            return null;
        else
            return queries.get(current);
    }
}
