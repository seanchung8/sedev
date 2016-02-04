package com.hwx.linearRoad.storm;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.TimedRotationPolicy;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.hwx.linearRoad.operators.PositionReportBolt;
import com.hwx.linearRoad.operators.QueryProcessingBolt;
import com.hwx.linearRoad.operators.RouterBolt;
import com.hwx.linearRoad.operators.TextScheme;
import com.hwx.linearRoad.utils.StormUtils;


public class LinearRoadTopology {
	
	private static final Logger Log = Logger.getLogger(LinearRoadTopology.class);
	private static final String thisName = LinearRoadTopology.class.getSimpleName();
	private final Properties topologyConfig;

	
	public LinearRoadTopology(String... args) throws Exception{
		if (args.length<1){
			usage();
		}
		topologyConfig = new Properties();
		try {
			topologyConfig.load(new FileInputStream(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	
	
	public static void main(String[] args) throws Exception{
		LinearRoadTopology topo = new LinearRoadTopology(args);
		topo.submit(topo.compose());
	}
	
	
	
	protected TopologyBuilder compose() throws Exception{
		
		//Connection conn = StormUtils.createHBaseConnection(topologyConfig);
		
		TopologyBuilder builder = new TopologyBuilder();
		

		
		IRichSpout kafkaSpout = StormUtils.getKafkaSpout(topologyConfig, new TextScheme());
		builder.setSpout("spout", kafkaSpout, 1);
		
		//add a single routerBolt to the graph to direct the appropriate tuples to their specific output stream
		IRichBolt routerBolt = new RouterBolt(topologyConfig, StormUtils.getOutputfields(kafkaSpout));
		builder.setBolt("router", routerBolt, 1).localOrShuffleGrouping("spout");
		
		
		//add the positonReport bolt to the graph and link it to the router bolt positionOutputStream partitioning by Xway and Dir
		int positionReportParallelism = Integer.parseInt(StormUtils.getExpectedProperty(topologyConfig, "storm.positionReportParallelism"));
		IRichBolt positionReportBolt = new PositionReportBolt(topologyConfig);
		builder.setBolt("notification", positionReportBolt, positionReportParallelism).fieldsGrouping("router", "positionReportOutputStream", new Fields("XWay", "Dir"));
		
		//add the HDFS writer to the positionReportBolt
		IRichBolt notifyWriter = getHDFSWriter(StormUtils.getExpectedProperty(topologyConfig, "hdfs.fs.notificationsDir"));
		builder.setBolt(notifyWriter.toString(), notifyWriter, positionReportParallelism).localOrShuffleGrouping("notification");
		
		//Will add and link a single QueryProcessingBolt to the routerBolt "queriesOutput" output stream
		IRichBolt queryBolt = new QueryProcessingBolt(topologyConfig);
		builder.setBolt("query", queryBolt, 1).localOrShuffleGrouping("router", "queriesOutput");
		
		//add the HDFS writer to the queryProcessingBolt
		IRichBolt queryWriter = getHDFSWriter(StormUtils.getExpectedProperty(topologyConfig, "hdfs.fs.queriesDir"));
		builder.setBolt(queryWriter.toString(), queryWriter).localOrShuffleGrouping("query");
		
		
		return builder;
		
	}

	
	/**
	 * Callable by Junit for testing
	 * @param builder
	 * @return
	 */
	protected LocalCluster testSubmit(TopologyBuilder builder){
		Config conf = new Config();
//		conf.registerSerialization(PositionReportOp.TollNotification.class);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(thisName, conf, builder.createTopology());
		return cluster;
	}

	protected void submit(TopologyBuilder builder){
//		conf.registerSerialization(PositionReportOp.TollNotification.class);
//		conf.setDebug(true);

		try {
			StormSubmitter.submitTopology(thisName, topologyConfig, builder.createTopology());
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}
	
	
	private IRichBolt getHDFSWriter(String outputDir){

		//Synchronize data buffer with the filesystem every 5000 tuples
		SyncPolicy syncPolicy = new CountSyncPolicy(5000);
		
		RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter("");
		
		// Rotate data files when they reach five MB
		FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(2.0f, FileSizeRotationPolicy.Units.GB);
//		FileRotationPolicy rotationPolicy = new TimedRotationPolicy(1.0f, TimedRotationPolicy.TimeUnit.MINUTES);

		// Use default, Storm-generated file names
		FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath(outputDir);

		// Instantiate the HdfsBolt
		HdfsBolt bolt = new HdfsBolt()
				.withFsUrl(StormUtils.getExpectedProperty(topologyConfig, "hdfs.fs.defaultFS"))
		        .withFileNameFormat(fileNameFormat)
		        .withRotationPolicy(rotationPolicy)
		        .withRecordFormat(format)
		        .withSyncPolicy(syncPolicy);
		
		return bolt;
	}
	
	
	private void usage(){
		Log.error("You must provide a path to a properties file when running LinearRoadTopology to corrently configure this topology.");
		System.exit(-1);
	}

}
