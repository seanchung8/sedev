package com.hortonworks.streaming.impl.kafka;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class IPDataScheme implements Scheme{

	private static final long serialVersionUID = -2990121166902741545L;

	private static final Logger LOG = Logger.getLogger(IPDataScheme.class);
	
	@Override
	public List<Object> deserialize(byte[] bytes) {

		String event = null;

		try {
			event = new String(bytes, "UTF-8");
			String[] pieces = event.split(",");

			if(pieces.length != 12) {
				LOG.error("Bad IP Info: " + event);
				//throw new RuntimeException("Bad number of fields: " + pieces.length);
				return new Values(null,null,null,null,null,null,null,null,null,null,null,null);
			}

			String ip_data_IP = String.valueOf(pieces[0]); // ex. 174.92.163.240
			String ip_data_Source = String.valueOf(pieces[1]); // ex. DSL
			String ip_data_Name = String.valueOf(pieces[2]); // ex albm4000
			String ip_data_Device = String.valueOf(pieces[3]);// ex bas1-stetherese38
			String ip_data_Interface = String.valueOf(pieces[4]); // ex GigabitEthernet 3/0/5.1360041:136-41
			String ip_data_Continent = String.valueOf(pieces[5]); // ex NA
			String ip_data_Country = String.valueOf(pieces[6]);// ex Canada
			String ip_data_State = String.valueOf(pieces[7]); // ex Quebec
			String ip_data_City = String.valueOf(pieces[8]); // ex Laval
			Float  ip_data_Lat = Float.valueOf(pieces[9]); // ex 45.6167
			Float  ip_data_Long = Float.valueOf(pieces[10]); // ex -73.75
			Float  ip_data_Timestamp = Float.valueOf(pieces[11]); // 1411352680.56
			
			return new Values(ip_data_IP, ip_data_Source, ip_data_Name, 
				ip_data_Device,ip_data_Interface,ip_data_Continent,ip_data_Country,ip_data_State, 
				ip_data_City,ip_data_Lat,ip_data_Long,ip_data_Timestamp);
		} catch (Exception e) {
			LOG.error("Bad IP Info: " + event);
			//throw new RuntimeException(e);
			return new Values(null,null,null,null,null,null,null,null,null,null,null,null);
		}
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("ip_data_IP", "ip_data_Source", "ip_data_Name", 
				"ip_data_Device", "ip_data_Interface", "ip_data_Continent", "ip_data_Country", "ip_data_State", 
				"ip_data_City","ip_data_Lat", "ip_data_Long", "ip_data_Timestamp");
	}

}