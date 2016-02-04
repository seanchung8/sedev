package com.hortonworks.streaming.impl.bolts;

import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import java.util.ArrayList;
import java.util.List;

public class NetflowEnrichmentBolt implements IRichBolt {

	private static final Logger LOG = Logger.getLogger(NetflowEnrichmentBolt.class);
	
	private static final String IP_DATA_TABLE = "ip_data_expanded";
	private static final String DATA_COLUMN_FAMILY_NAME = "data";	
	
	private OutputCollector collector;
	private HConnection connection;
	private HTableInterface ipDataTable;
	
	public NetflowEnrichmentBolt(Properties topologyConfig) {
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

		// Extract necessary data from tuple stream
		String nf_Start = input.getStringByField("nf_Start");
                String nf_SrcAddr = input.getStringByField("nf_SrcAddr");
		String nf_DstAddr = input.getStringByField("nf_DstAddr");
                
        //Shallow copy the tuple
        List<Object> enrichedIPDataTuple = new ArrayList<Object>(input.getValues());

		// This data is bad. We don't need to be here anymore. ACK and gtfo.
		if(nf_Start == null) {
			collector.ack(input);
			return;
		}
                
        //new values to enrich tuple
		String source_ip_data_Source = null; 
		String source_ip_data_Name = null;
		String source_ip_data_Device = null;
		String source_ip_data_Interface = null;
		String source_ip_data_Continent = null;
		String source_ip_data_Country = null;
		String source_ip_data_State = null;
		String source_ip_data_City = null;
		Float  source_ip_data_Lat = null;
		Float  source_ip_data_Long = null;
		Float  source_ip_data_Timestamp = null;

		String dest_ip_data_Source = null; 
		String dest_ip_data_Name = null;
		String dest_ip_data_Device = null;
		String dest_ip_data_Interface = null;
		String dest_ip_data_Continent = null;
		String dest_ip_data_Country = null;
		String dest_ip_data_State = null;
		String dest_ip_data_City = null;
		Float  dest_ip_data_Lat = null;
		Float  dest_ip_data_Long = null;
		Float  dest_ip_data_Timestamp = null;

		// Pull HBase data for source ip
		try {
			byte[] ip_dataData = Bytes.toBytes(nf_SrcAddr);
			Get get = new Get(ip_dataData);
			Result result = ipDataTable.get(get);
			if(!result.isEmpty()) {
				byte[] b_data_Source = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Source"));
				byte[] b_data_Name = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Name"));
				byte[] b_data_Device = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Device"));
				byte[] b_data_Interface = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Interface"));
				byte[] b_data_Continent = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Continent"));
				byte[] b_data_Country = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Country"));
				byte[] b_data_State = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_State"));
				byte[] b_data_City = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_City"));
				byte[] b_data_Lat = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Lat"));
				byte[] b_data_Long = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Long"));
				byte[] b_data_Timestamp = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Timestamp"));

				source_ip_data_Source = Bytes.toString(b_data_Source); 
				source_ip_data_Name = Bytes.toString(b_data_Name); 
				source_ip_data_Device = Bytes.toString(b_data_Device); 
				source_ip_data_Interface = Bytes.toString(b_data_Interface); 
				source_ip_data_Continent = Bytes.toString(b_data_Continent); 
				source_ip_data_Country = Bytes.toString(b_data_Country); 
				source_ip_data_State = Bytes.toString(b_data_State); 
				source_ip_data_City = Bytes.toString(b_data_City); 
				source_ip_data_Lat = Bytes.toFloat(b_data_Lat); 
				source_ip_data_Long = Bytes.toFloat(b_data_Long); 
				source_ip_data_Timestamp = Bytes.toFloat(b_data_Timestamp);
			}
		} catch (Exception e) {
			LOG.error("Error getting ip_data source enrichment: ", e);
			throw new RuntimeException(e);
		}

		// Pull HBase data for destination ip
		try {
			byte[] ip_dataData = Bytes.toBytes(nf_DstAddr);
			Get get = new Get(ip_dataData);
			Result result = ipDataTable.get(get);
			if(!result.isEmpty()) {
				byte[] b_data_Source = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Source"));
				byte[] b_data_Name = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Name"));
				byte[] b_data_Device = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Device"));
				byte[] b_data_Interface = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Interface"));
				byte[] b_data_Continent = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Continent"));
				byte[] b_data_Country = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Country"));
				byte[] b_data_State = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_State"));
				byte[] b_data_City = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_City"));
				byte[] b_data_Lat = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Lat"));
				byte[] b_data_Long = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Long"));
				byte[] b_data_Timestamp = result.getValue(Bytes.toBytes(DATA_COLUMN_FAMILY_NAME), Bytes.toBytes("ip_data_Timestamp"));

				dest_ip_data_Source = Bytes.toString(b_data_Source); 
				dest_ip_data_Name = Bytes.toString(b_data_Name); 
				dest_ip_data_Device = Bytes.toString(b_data_Device); 
				dest_ip_data_Interface = Bytes.toString(b_data_Interface); 
				dest_ip_data_Continent = Bytes.toString(b_data_Continent); 
				dest_ip_data_Country = Bytes.toString(b_data_Country); 
				dest_ip_data_State = Bytes.toString(b_data_State); 
				dest_ip_data_City = Bytes.toString(b_data_City); 
				dest_ip_data_Lat = Bytes.toFloat(b_data_Lat); 
				dest_ip_data_Long = Bytes.toFloat(b_data_Long); 
				dest_ip_data_Timestamp = Bytes.toFloat(b_data_Timestamp);
			}
		} catch (Exception e) {
			LOG.error("Error getting ip_data destination enrichment: ", e);
			throw new RuntimeException(e);
		}

        //Add new enriched values to tuple
        enrichedIPDataTuple.add(source_ip_data_Source);
        enrichedIPDataTuple.add(source_ip_data_Name);
        enrichedIPDataTuple.add(source_ip_data_Device);
        enrichedIPDataTuple.add(source_ip_data_Interface);
        enrichedIPDataTuple.add(source_ip_data_Continent);
        enrichedIPDataTuple.add(source_ip_data_Country);
		enrichedIPDataTuple.add(source_ip_data_State);
		enrichedIPDataTuple.add(source_ip_data_City);
		enrichedIPDataTuple.add(source_ip_data_Lat);
		enrichedIPDataTuple.add(source_ip_data_Long);
		enrichedIPDataTuple.add(source_ip_data_Timestamp);

        enrichedIPDataTuple.add(dest_ip_data_Source);
        enrichedIPDataTuple.add(dest_ip_data_Name);
        enrichedIPDataTuple.add(dest_ip_data_Device);
        enrichedIPDataTuple.add(dest_ip_data_Interface);
        enrichedIPDataTuple.add(dest_ip_data_Continent);
        enrichedIPDataTuple.add(dest_ip_data_Country);
		enrichedIPDataTuple.add(dest_ip_data_State);
		enrichedIPDataTuple.add(dest_ip_data_City);
		enrichedIPDataTuple.add(dest_ip_data_Lat);
		enrichedIPDataTuple.add(dest_ip_data_Long);
		enrichedIPDataTuple.add(dest_ip_data_Timestamp);
                
		// Re-emit the tuples, the solr indexing and hdfs bolts will want this
        collector.emit(input, enrichedIPDataTuple);
                
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
		declarer.declare(new Fields("nf_Start", "nf_End", "nf_Duration", "nf_SrcAddr", "nf_DstAddr", "nf_SrdPort", "nf_DstPort", 
			"nf_Proto", "nf_flg", "nf_fwd", "nf_stos", "nf_ipkt", "nf_ibyt", "nf_opkt", "nf_obyt", "nf_inIF", "nf_outIF", 
			"nf_sAS", "nf_dAS", "nf_smk", "nf_dmk", "nf_dtos", "nf_dir", "nf_NextHop", "nf_BGPNextHop", "nf_svln", "nf_dvln", 
			"nf_ismc", "nf_odmc", "nf_idmc", "nf_osmc", "nf_mpls1", "nf_mpls2", "nf_mpls3", "nf_mpls4", "nf_mpls5", 
			"nf_mpls6", "nf_mpls7", "nf_mpls8", "nf_mpls9", "nf_mpls10", "nf_cl", "nf_sl", "nf_al", "nf_RouterIP", 
			"nf_eng", "nf_exid", "nf_tr","source_ip_data_Source","source_ip_data_Name","source_ip_data_Device",
			"source_ip_data_Interface","source_ip_data_Continent","source_ip_data_Country","source_ip_data_State",
			"source_ip_data_City","source_ip_data_Lat","source_ip_data_Long","source_ip_data_Timestamp","dest_ip_data_Source",
			"dest_ip_data_Name","dest_ip_data_Device","dest_ip_data_Interface","dest_ip_data_Continent","dest_ip_data_Country",
			"dest_ip_data_State","dest_ip_data_City","dest_ip_data_Lat","dest_ip_data_Long","dest_ip_data_Timestamp"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
