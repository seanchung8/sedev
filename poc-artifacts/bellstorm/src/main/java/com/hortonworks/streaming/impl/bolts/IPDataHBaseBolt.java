package com.hortonworks.streaming.impl.bolts;

import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class IPDataHBaseBolt implements IRichBolt {

	private static final Logger LOG = Logger.getLogger(IPDataHBaseBolt.class);
	
	private static final String IP_DATA_TABLE = "ip_data_expanded";
	private static final String DATA_COLUMN_FAMILY_NAME = "data";	
	
	private OutputCollector collector;
	private HConnection connection;
	private HTableInterface ipDataTable;
	
	public IPDataHBaseBolt(Properties topologyConfig) {
		// Do nothing
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		
		this.collector = collector;
		try {
			this.connection = HConnectionManager.createConnection(constructConfiguration());
			this.ipDataTable = connection.getTable(IP_DATA_TABLE);	
		} catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}		
	}

	@Override
	public void execute(Tuple input) {

		// Extract data from tuple stream
		String ip_data_IP = input.getStringByField("ip_data_IP"); // ex. 174.92.163.240


		// This data is bad. We don't need to be here anymore. ACK and gtfo.
		if(ip_data_IP == null) {
			collector.ack(input);
			return;
		}

		String ip_data_Source = input.getStringByField("ip_data_Source"); // ex. DSL
		String ip_data_Name = input.getStringByField("ip_data_Name"); // ex albm4000
		String ip_data_Device = input.getStringByField("ip_data_Device"); // ex bas1-stetherese38
		String ip_data_Interface = input.getStringByField("ip_data_Interface"); // ex GigabitEthernet 3/0/5.1360041:136-41
		String ip_data_Continent = input.getStringByField("ip_data_Continent"); // ex NA
		String ip_data_Country = input.getStringByField("ip_data_Country"); // ex Canada
		String ip_data_State = input.getStringByField("ip_data_State"); // ex Quebec
		String ip_data_City = input.getStringByField("ip_data_City"); // ex Laval
		Float  ip_data_Lat = input.getFloatByField("ip_data_Lat"); // ex 45.6167
		Float  ip_data_Long = input.getFloatByField("ip_data_Long"); // ex -73.75
		Float  ip_data_Timestamp = input.getFloatByField("ip_data_Timestamp"); // 1411352680.56

		// Store the data in hbase
		try {
			Put put = constructRow(DATA_COLUMN_FAMILY_NAME, ip_data_IP, ip_data_Source, ip_data_Name, 
				ip_data_Device, ip_data_Interface, ip_data_Continent, ip_data_Country, ip_data_State, 
				ip_data_City,ip_data_Lat, ip_data_Long, ip_data_Timestamp);
			this.ipDataTable.put(put);
		}
		catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}	
		
		// Re-emit the tuples, just in case some downstream consumer wants them
		collector.emit(input, new Values(ip_data_IP, ip_data_Source, ip_data_Name, 
				ip_data_Device, ip_data_Interface, ip_data_Continent, ip_data_Country, ip_data_State, 
				ip_data_City,ip_data_Lat, ip_data_Long, ip_data_Timestamp));
		
		//acknowledge even if there is an error
		collector.ack(input);
	}
	
	/**
	 * We don't need to set any configuration because at deployment time, it should pick up all configuration from hbase-site.xml 
	 * as long as it in classpath. Note that we store hbase-site.xml in src/main/resources so it will be in the topology jar that gets deployed
	 * @return
	 */
	public static  Configuration constructConfiguration() {
		Configuration config = HBaseConfiguration.create();
		return config;
	}	

	private Put constructRow(String columnFamily, String ip_data_IP, String ip_data_Source, String ip_data_Name, 
		String ip_data_Device , String ip_data_Interface , String ip_data_Continent ,String ip_data_Country ,
		String ip_data_State , String ip_data_City , Float  ip_data_Lat ,Float  ip_data_Long , Float  ip_data_Timestamp) {
		
		String rowKey = ip_data_IP;
		Put put = new Put(Bytes.toBytes(rowKey));
		
		String nameCol = "ip_data_Source";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(nameCol), Bytes.toBytes(ip_data_Source));
		
		String dataCol = "ip_data_Name";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(dataCol), Bytes.toBytes(ip_data_Name));

		String deviceCol = "ip_data_Device";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(deviceCol), Bytes.toBytes(ip_data_Device));

		String interfaceCol = "ip_data_Interface";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(interfaceCol), Bytes.toBytes(ip_data_Interface));

		String continentCol = "ip_data_Continent";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(continentCol), Bytes.toBytes(ip_data_Continent));

		String countryCol = "ip_data_Country";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(countryCol), Bytes.toBytes(ip_data_Country));

		String stateCol = "ip_data_State";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(stateCol), Bytes.toBytes(ip_data_State));

		String cityCol = "ip_data_City";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(cityCol), Bytes.toBytes(ip_data_City));

		String latCol = "ip_data_Lat";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(latCol), Bytes.toBytes(ip_data_Lat));

		String longCol = "ip_data_Long";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(longCol), Bytes.toBytes(ip_data_Long));
		
		String tsCol = "ip_data_Timestamp";
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(tsCol), Bytes.toBytes(ip_data_Timestamp));
				
		return put;
	}

	
	@Override
	public void cleanup() {
		try {
			ipDataTable.close();
		} catch (Exception  e) {
			LOG.error("Error closing connections", e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("ip_data_IP", "ip_data_Source", "ip_data_Name", 
				"ip_data_Device", "ip_data_Interface", "ip_data_Continent", "ip_data_Country", "ip_data_State", 
				"ip_data_City","ip_data_Lat", "ip_data_Long", "ip_data_Timestamp"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
