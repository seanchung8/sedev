package com.hortonworks.streaming.impl.topologies;

import org.apache.log4j.Logger;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy.Units;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.apache.storm.hdfs.common.rotation.MoveFileAction;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.hortonworks.streaming.impl.bolts.DebugBolt;
import com.hortonworks.streaming.impl.bolts.NetflowEnrichmentBolt;
import com.hortonworks.streaming.impl.bolts.IPDataHBaseBolt;
import com.hortonworks.streaming.impl.bolts.TruckEventRuleBolt;
import com.hortonworks.streaming.impl.bolts.WebSocketBolt;
import com.hortonworks.streaming.impl.bolts.hdfs.FileTimeRotationPolicy;
import com.hortonworks.streaming.impl.bolts.hive.HiveTablePartitionAction;
import com.hortonworks.streaming.impl.bolts.solr.SolrIndexingBolt;

import com.hortonworks.streaming.impl.kafka.NetflowScheme;
import com.hortonworks.streaming.impl.kafka.IPDataScheme;

public class BellStormKafkaTopology extends BaseEventTopology {
	
	private static final Logger LOG = Logger.getLogger(BellStormKafkaTopology.class);
	
	
	public BellStormKafkaTopology(String configFileLocation) throws Exception {
		
		super(configFileLocation);			
	}

	private void buildAndSubmit() throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		
		// Kafka input sources
		configureNetflowKafkaSpout(builder);
		configureIPDataKafkaSpout(builder);

		// Debug bolt to send all data to so it sinks somewhere while
		// I make this application
		// configureDebugBolt(builder);

		// Send IPData to HBase for storage
		configureIPDataHBaseBolt(builder);

		// Enrich Netflow data with things stored in HBase
		configureNetflowEnrichmentBolt(builder);

		/* Set up HDFSBOlt to send every truck event to HDFS */
		// configureHDFSBolt(builder);
		
		/* configure Solr indexing bolt */
		configureSolrIndexingBolt(builder);
		
		/* Setup Monitoring Bolt to track number of alerts per truck driver */
		// configureMonitoringBolt(builder);
		
		/* Setup WebSocket Bolt for alerts and notifications */
		// configureWebSocketBolt(builder);
		
		
		/* This conf is for Storm and it needs be configured with things like the following:
		 * 	Zookeeper server, nimbus server, ports, etc... All of this configuration will be picked up
		 * in the ~/.storm/storm.yaml file that will be located on each storm node.
		 */
		Config conf = new Config();
		conf.setDebug(false);

		// This is scary crap... this will ack packets immediately from Kafka.
		conf.setNumAckers(0);

		/* Set the number of workers that will be spun up for this topology. 
		 * Each worker represents a JVM where executor thread will be spawned from */
		Integer topologyWorkers = Integer.valueOf(topologyConfig.getProperty("storm.topology.workers"));
		conf.put(Config.TOPOLOGY_WORKERS, topologyWorkers);
		
		try {
			StormSubmitter.submitTopology("bell-storm", conf, builder.createTopology());	
		} catch (Exception e) {
			LOG.error("Error submiting Topology", e);
		}
			
	}

	// This bolt is used for development purposes to validate the data coming from Kafka
	public void configureDebugBolt(TopologyBuilder builder) {
		DebugBolt netFlowDebugBolt = new DebugBolt(topologyConfig);
		builder.setBolt("netflow_debug_bolt", netFlowDebugBolt, 4).shuffleGrouping("netflowKafkaSpout");
		DebugBolt ip_dataDebugBolt = new DebugBolt(topologyConfig);
		builder.setBolt("ip_data_debug_bolt", ip_dataDebugBolt, 4).shuffleGrouping("ip_dataKafkaSpout");
	}

	private void configureSolrIndexingBolt(TopologyBuilder builder) {
		int solrBoltCount = Integer.valueOf(topologyConfig.getProperty("solr.bolt.thread.count"));
		SolrIndexingBolt solrBolt = new SolrIndexingBolt(topologyConfig);
		builder.setBolt("solr_indexer_bolt", solrBolt, solrBoltCount).shuffleGrouping("netflow_enrichment_bolt");
	}

	public void configureWebSocketBolt(TopologyBuilder builder) {
		boolean configureWebSocketBolt = Boolean.valueOf(topologyConfig.getProperty("notification.topic")).booleanValue();
		if(configureWebSocketBolt) {
			WebSocketBolt webSocketBolt = new WebSocketBolt(topologyConfig);
			builder.setBolt("web_sockets_bolt", webSocketBolt, 4).shuffleGrouping("hbase_bolt");
		}
	}

	public void configureIPDataHBaseBolt(TopologyBuilder builder) {
		IPDataHBaseBolt hbaseBolt = new IPDataHBaseBolt(topologyConfig);
		builder.setBolt("ip_data_hbase_bolt", hbaseBolt, 2).shuffleGrouping("ip_dataKafkaSpout");
	}

	public void configureNetflowEnrichmentBolt(TopologyBuilder builder) {
		int netflow_enrichmentBoltCount = Integer.valueOf(topologyConfig.getProperty("netflow_enrichment.bolt.thread.count"));
		NetflowEnrichmentBolt netflowEnrichmentBolt = new NetflowEnrichmentBolt(topologyConfig);
		builder.setBolt("netflow_enrichment_bolt", netflowEnrichmentBolt, netflow_enrichmentBoltCount).shuffleGrouping("netflowKafkaSpout");
	}

	/**
	 * Send truckEvents from same driver to the same bolt instances to maintain accuracy of eventCount per truck/driver 
	 * @param builder
	 */
	public void configureMonitoringBolt(TopologyBuilder builder) {
		int boltCount = Integer.valueOf(topologyConfig.getProperty("bolt.thread.count"));
		builder.setBolt("monitoring_bolt", 
						new TruckEventRuleBolt(topologyConfig), boltCount)
						.fieldsGrouping("kafkaSpout", new Fields("driverId"));
	}

	public void configureHDFSBolt(TopologyBuilder builder) {
		// Use pipe as record boundary
		
		String rootPath = topologyConfig.getProperty("hdfs.path");
		String prefix = topologyConfig.getProperty("hdfs.file.prefix");
		String fsUrl = topologyConfig.getProperty("hdfs.url");
		String sourceMetastoreUrl = topologyConfig.getProperty("hive.metastore.url");
		String hiveStagingTableName = topologyConfig.getProperty("hive.staging.table.name");
		String databaseName = topologyConfig.getProperty("hive.database.name");
		Float rotationTimeInMinutes = Float.valueOf(topologyConfig.getProperty("hdfs.file.rotation.time.minutes"));
		
		RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter(",");

		//Synchronize data buffer with the filesystem every 1000 tuples
		SyncPolicy syncPolicy = new CountSyncPolicy(1000);

		// Rotate data files when they reach five MB
		FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5.0f, Units.MB);
		
		//Rotate every X minutes
		//FileTimeRotationPolicy rotationPolicy = new FileTimeRotationPolicy(rotationTimeInMinutes, FileTimeRotationPolicy.Units.MINUTES);
		
		//Hive Partition Action
		HiveTablePartitionAction hivePartitionAction = new HiveTablePartitionAction(sourceMetastoreUrl, hiveStagingTableName, databaseName, fsUrl);
		
		//MoveFileAction moveFileAction = new MoveFileAction().toDestination(rootPath + "/working");

		FileNameFormat fileNameFormat = new DefaultFileNameFormat()
				.withPath(rootPath + "/staging")
				.withPrefix(prefix);

		// Instantiate the HdfsBolt
		HdfsBolt hdfsBolt = new HdfsBolt()
				 .withFsUrl(fsUrl)
		         .withFileNameFormat(fileNameFormat)
		         .withRecordFormat(format)
		         .withRotationPolicy(rotationPolicy)
		         .withSyncPolicy(syncPolicy)
		         .addRotationAction(hivePartitionAction);
		
		int hdfsBoltCount = Integer.valueOf(topologyConfig.getProperty("hdfsbolt.thread.count"));
		builder.setBolt("hdfs_bolt", hdfsBolt, hdfsBoltCount).shuffleGrouping("netflow_enrichment_bolt");
	}

	public int configureNetflowKafkaSpout(TopologyBuilder builder) {
		KafkaSpout kafkaSpout = constructNetflowKafkaSpout();
		
		int spoutCount = Integer.valueOf(topologyConfig.getProperty("netflow.spout.thread.count"));
		int boltCount = Integer.valueOf(topologyConfig.getProperty("netflow.bolt.thread.count"));
		
		builder.setSpout("netflowKafkaSpout", kafkaSpout, spoutCount);
		return boltCount;
	}

	public int configureIPDataKafkaSpout(TopologyBuilder builder) {
		KafkaSpout kafkaSpout = constructIPDataKafkaSpout();
		
		int spoutCount = Integer.valueOf(topologyConfig.getProperty("ip_data.spout.thread.count"));
		int boltCount = Integer.valueOf(topologyConfig.getProperty("ip_data.bolt.thread.count"));
		
		builder.setSpout("ip_dataKafkaSpout", kafkaSpout, spoutCount);
		return boltCount;
	}

	/**
	 * Construct the KafkaSpout which comes from the jar storm-kafka-0.8-plus
	 * @return
	 */
	private KafkaSpout constructNetflowKafkaSpout() {
		KafkaSpout kafkaSpout = new KafkaSpout(constructNetflowKafkaSpoutConf());
		return kafkaSpout;
	}

	/**
	 * Construct the KafkaSpout which comes from the jar storm-kafka-0.8-plus
	 * @return
	 */
	private KafkaSpout constructIPDataKafkaSpout() {
		KafkaSpout kafkaSpout = new KafkaSpout(constructIPDataKafkaSpoutConf());
		return kafkaSpout;
	}

	/**
	 * Construct 
	 * @return
	 */
	private SpoutConfig constructNetflowKafkaSpoutConf() {
		BrokerHosts hosts = new ZkHosts(topologyConfig.getProperty("kafka.netflow.zookeeper.host.port"));
		String topic = topologyConfig.getProperty("kafka.netflow.topic");
		String zkRoot = topologyConfig.getProperty("kafka.netflow.zkRoot");
		String consumerGroupId = topologyConfig.getProperty("kafka.netflow.consumer.group.id");
		
		SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);
		spoutConfig.scheme = new SchemeAsMultiScheme(new NetflowScheme());
		return spoutConfig;
	}

	/**
	 * Construct 
	 * @return
	 */
	private SpoutConfig constructIPDataKafkaSpoutConf() {
		BrokerHosts hosts = new ZkHosts(topologyConfig.getProperty("kafka.ip_data.zookeeper.host.port"));
		String topic = topologyConfig.getProperty("kafka.ip_data.topic");
		String zkRoot = topologyConfig.getProperty("kafka.ip_data.zkRoot");
		String consumerGroupId = topologyConfig.getProperty("kafka.ip_data.consumer.group.id");
		
		SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);
		spoutConfig.scheme = new SchemeAsMultiScheme(new IPDataScheme());
		return spoutConfig;
	}
	
	public static void main(String[] args) throws Exception {
		String configFileLocation = args[0];
		BellStormKafkaTopology truckTopology = new BellStormKafkaTopology(configFileLocation);
		truckTopology.buildAndSubmit();
		
	}	

}



