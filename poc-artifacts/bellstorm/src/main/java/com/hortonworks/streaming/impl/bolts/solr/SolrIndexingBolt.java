package com.hortonworks.streaming.impl.bolts.solr;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SolrIndexingBolt implements IRichBolt {

	private static final long serialVersionUID = -5319490672681173657L;
	private static final Logger LOG = Logger.getLogger(SolrIndexingBolt.class);
	
	private OutputCollector collector;
	private Properties config;

	private SolrServer server = null;	
	private String solrUrl;	

	
	public SolrIndexingBolt(Properties config) {
		this.config = config;

	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;

		this.solrUrl = config.getProperty("solr.server.url");


		server = new HttpSolrServer(solrUrl);
		(new Thread(new CommitThread(server))).start();		
	}

	@Override
	public void execute(Tuple input) {
		NetflowEvent event = constructNetflowEvent(input);
		index(event);
		collector.ack(input);
	}

	private void index(NetflowEvent netflowEvent) {
		try {
			UpdateResponse response = server.addBean(netflowEvent);
		} catch (IOException e) {
			LOG.error("Could not index document at starting time: " + netflowEvent.getnf_Start()
					+ " " + e.getMessage());
			e.printStackTrace();
		} catch (SolrServerException e) {
			LOG.error("Could not index document at starting time: " + netflowEvent.getnf_Start()
					+ " " + e.getMessage());
			e.printStackTrace();
		}
	}

	private NetflowEvent constructNetflowEvent(Tuple input) {


		Date nf_Start = null;
		Date nf_End = null;
		String source_geo = null;
		String dest_geo = null;

		SimpleDateFormat netflow_datetime_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		// Extract data from tuple stream
		String nf_Start_raw = input.getStringByField("nf_Start");
		String nf_End_raw = input.getStringByField("nf_End");

		try {
			nf_Start = netflow_datetime_format.parse(nf_Start_raw);
			nf_End = netflow_datetime_format.parse(nf_End_raw);
		} catch (Exception date_ex) {
			LOG.error("Error converting date: " + date_ex.getMessage());
			date_ex.printStackTrace();
		}

		Float nf_Duration = input.getFloatByField("nf_Duration");
		String nf_SrcAddr = input.getStringByField("nf_SrcAddr");
		String nf_DstAddr = input.getStringByField("nf_DstAddr");
		Long nf_SrdPort = input.getLongByField("nf_SrdPort");
		Long nf_DstPort = input.getLongByField("nf_DstPort");
		String nf_Proto = input.getStringByField("nf_Proto");
		String nf_flg = input.getStringByField("nf_flg");
		Long nf_fwd = input.getLongByField("nf_fwd");
		Long nf_stos = input.getLongByField("nf_stos");
		Long nf_ipkt = input.getLongByField("nf_ipkt");
		String nf_ibyt = input.getStringByField("nf_ibyt");
		String nf_opkt = input.getStringByField("nf_opkt");
		Long nf_obyt = input.getLongByField("nf_obyt");
		Long nf_inIF = input.getLongByField("nf_inIF");
		Long nf_outIF = input.getLongByField("nf_outIF");
		Long nf_sAS = input.getLongByField("nf_sAS");
		Long nf_dAS = input.getLongByField("nf_dAS");
		Long nf_smk = input.getLongByField("nf_smk");
		Long nf_dmk = input.getLongByField("nf_dmk");
		Long nf_dtos = input.getLongByField("nf_dtos");
		Long nf_dir = input.getLongByField("nf_dir");
		String nf_NextHop = input.getStringByField("nf_NextHop");
		String nf_BGPNextHop = input.getStringByField("nf_BGPNextHop");
		Long nf_svln = input.getLongByField("nf_svln");
		Long nf_dvln = input.getLongByField("nf_dvln");
		String nf_ismc = input.getStringByField("nf_ismc");
		String nf_odmc = input.getStringByField("nf_odmc");
		String nf_idmc = input.getStringByField("nf_idmc");
		String nf_osmc = input.getStringByField("nf_osmc");
		String nf_mpls1 = input.getStringByField("nf_mpls1");
		String nf_mpls2 = input.getStringByField("nf_mpls2");
		String nf_mpls3 = input.getStringByField("nf_mpls3");
		String nf_mpls4 = input.getStringByField("nf_mpls4");
		String nf_mpls5 = input.getStringByField("nf_mpls5");
		String nf_mpls6 = input.getStringByField("nf_mpls6");
		String nf_mpls7 = input.getStringByField("nf_mpls7");
		String nf_mpls8 = input.getStringByField("nf_mpls8");
		String nf_mpls9 = input.getStringByField("nf_mpls9");
		String nf_mpls10 = input.getStringByField("nf_mpls10");
		Float nf_cl = input.getFloatByField("nf_cl");
		Float nf_sl = input.getFloatByField("nf_sl");
		Float nf_al = input.getFloatByField("nf_al");
		String nf_RouterIP = input.getStringByField("nf_RouterIP");
		String nf_eng = input.getStringByField("nf_eng");
		Long nf_exid = input.getLongByField("nf_exid");
		String nf_tr = input.getStringByField("nf_tr");

		String source_ip_data_Source = input.getStringByField("source_ip_data_Source");
		String source_ip_data_Name = input.getStringByField("source_ip_data_Name");
		String source_ip_data_Device = input.getStringByField("source_ip_data_Device");
		String source_ip_data_Interface = input.getStringByField("source_ip_data_Interface");
		String source_ip_data_Continent = input.getStringByField("source_ip_data_Continent");
		String source_ip_data_Country = input.getStringByField("source_ip_data_Country");
		String source_ip_data_State = input.getStringByField("source_ip_data_State");
		String source_ip_data_City = input.getStringByField("source_ip_data_City");
		Float source_ip_data_Lat = input.getFloatByField("source_ip_data_Lat");
		Float source_ip_data_Long = input.getFloatByField("source_ip_data_Long");
		Float source_ip_data_Timestamp = input.getFloatByField("source_ip_data_Timestamp");

		String dest_ip_data_Source = input.getStringByField("dest_ip_data_Source");
		String dest_ip_data_Name = input.getStringByField("dest_ip_data_Name");
		String dest_ip_data_Device = input.getStringByField("dest_ip_data_Device");
		String dest_ip_data_Interface = input.getStringByField("dest_ip_data_Interface");
		String dest_ip_data_Continent = input.getStringByField("dest_ip_data_Continent");
		String dest_ip_data_Country = input.getStringByField("dest_ip_data_Country");
		String dest_ip_data_State = input.getStringByField("dest_ip_data_State");
		String dest_ip_data_City = input.getStringByField("dest_ip_data_City");
		Float dest_ip_data_Lat = input.getFloatByField("dest_ip_data_Lat");
		Float dest_ip_data_Long = input.getFloatByField("dest_ip_data_Long");
		Float dest_ip_data_Timestamp = input.getFloatByField("dest_ip_data_Timestamp");
                
        //create key field: id
		String id = "" + UUID.randomUUID();

		if(source_ip_data_Lat != null) {
			source_geo = "{ \"type\": \"Point\", \"coordinates\": [" + source_ip_data_Long + ", " + source_ip_data_Lat + "] }";
		}

		if(dest_ip_data_Lat != null) {
			dest_geo = "{ \"type\": \"Point\", \"coordinates\": [" + dest_ip_data_Long + ", " + dest_ip_data_Lat + "] }";
		}
		
		NetflowEvent event = new NetflowEvent(id, nf_Start, nf_End, nf_Duration, nf_SrcAddr, nf_DstAddr, nf_SrdPort, nf_DstPort, 
				nf_Proto, nf_flg, nf_fwd, nf_stos, nf_ipkt, nf_ibyt, nf_opkt, nf_obyt, nf_inIF, nf_outIF, nf_sAS, 
				nf_dAS, nf_smk, nf_dmk, nf_dtos, nf_dir, nf_NextHop, nf_BGPNextHop, nf_svln, nf_dvln, nf_ismc, 
				nf_odmc, nf_idmc, nf_osmc, nf_mpls1, nf_mpls2, nf_mpls3, nf_mpls4, nf_mpls5, nf_mpls6, nf_mpls7, 
				nf_mpls8, nf_mpls9, nf_mpls10, nf_cl, nf_sl, nf_al, nf_RouterIP, nf_eng, nf_exid, nf_tr,
				source_ip_data_Source,source_ip_data_Name,source_ip_data_Device, source_ip_data_Interface,
				source_ip_data_Continent,source_ip_data_Country,source_ip_data_State,
				source_ip_data_City,source_ip_data_Lat,source_ip_data_Long,source_ip_data_Timestamp,dest_ip_data_Source,
				dest_ip_data_Name,dest_ip_data_Device,dest_ip_data_Interface,dest_ip_data_Continent,dest_ip_data_Country,
				dest_ip_data_State,dest_ip_data_City,dest_ip_data_Lat,dest_ip_data_Long,dest_ip_data_Timestamp, source_geo, dest_geo);
		return event;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}	
	
	class CommitThread implements Runnable {
		SolrServer server;

		public CommitThread(SolrServer server) {
			this.server = server;
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(15000);
					server.commit();
					LOG.info("Committing Index");
				} catch (InterruptedException e) {
					LOG.error("Interrupted: " + e.getMessage());
					e.printStackTrace();
				} catch (SolrServerException e) {
					LOG.error("Error committing: " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					LOG.error("Error committing: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}	

}
