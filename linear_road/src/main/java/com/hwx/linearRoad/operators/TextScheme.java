package com.hwx.linearRoad.operators;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TextScheme implements Scheme {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TextScheme.class);
	
	public final String[] FIELDS = new String[]{"Type", "Time", "Vid", "Spd", "XWay", "Lane", "Dir", "Seg", "Pos", "QID", "Sint", "Send", "DOW", "TOD", "DAY", "Tin"};

    public List<Object> deserialize(byte[] bytes) {
    	Values vals = new Values();
    	
    	
    	try {    		
    		String[] temp = new String(bytes, "UTF-8").split(",");
    		
	    	for(int i=0;i<FIELDS.length-1;i++){
	    		vals.add(Integer.parseInt(temp[i]));
	    	}
	    	//add a current timestap in millis as field "Tin"
	    	vals.add(System.currentTimeMillis());
	    	
    	}catch(UnsupportedEncodingException e){
    		logger.error("Parsing Error - Skipping Message");
			logger.error(e.getMessage());
    	}
    	
    	return vals;
    }

    
    public Fields getOutputFields() {
        return new Fields(FIELDS);
    }

}
