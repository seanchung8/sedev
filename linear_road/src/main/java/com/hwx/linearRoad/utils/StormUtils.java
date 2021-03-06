package com.hwx.linearRoad.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.log4j.Logger;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import backtype.storm.spout.Scheme;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.IComponent;
import backtype.storm.topology.OutputFieldsGetter;

public class StormUtils {

	private static final Logger logger = Logger.getLogger(StormUtils.class);
	

	/**
	 * Returns the list of output fields (schema) for the given bolt or spout. Since spouts and bolts 
	 * can have declare multiple output schemes, users can specify the schema name.
	 * 
	 * @param component - The Spout or Bolt.
	 * @param schemaName - The name of the schema to which you are referring. Leaving this value blank or null will use the "default" schema. 
	 * @return - List of field names declared in the schema.
	 */
	public static List<String> getOutputfields(IComponent component, String schemaName){
		if(schemaName==null || schemaName.length()==0){
			schemaName = "default";
		}

		OutputFieldsGetter getter = new OutputFieldsGetter();
		component.declareOutputFields(getter);

		if (!getter.getFieldsDeclaration().containsKey(schemaName)){
			throw new FailedException(schemaName + " was not found in the fieldsDeclaration");
		}
		return new ArrayList<String>(getter.getFieldsDeclaration().get(schemaName).get_output_fields());
	}
	
	public static List<String> getOutputfields(IComponent component){
		return getOutputfields(component, "default");
	}
	

	/**
	 * Convenience method for instantiating a new Kafka spout
	 * @param zkHosts - comma separated list of ZK servers and their communication ports ZK1:2181,ZK2:2181,etc..
	 * @param topic - the Kafka topic name being read from
	 * @param zkRoot - 
	 * @param consumerGroupId - 
	 * @param schema - Schema containing the message parsing code
	 * @return - KafkaSpout
	 */
	public static KafkaSpout getKafkaSpout(Properties props, Scheme schema){
		
		String zkHosts = StormUtils.getExpectedProperty(props, "kafka.zookeeper.host.port");
		String topic = StormUtils.getExpectedProperty(props, "kafka.topic");
		String zkRoot = StormUtils.getExpectedProperty(props, "kafka.zkRoot");
		String consumerGroupId = StormUtils.getExpectedProperty(props, "kafka.consumer.group.id");
		
		
		BrokerHosts hosts = new ZkHosts(zkHosts);
		SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);
		spoutConfig.scheme = new SchemeAsMultiScheme(schema);
		
		if(props.containsKey("force.from.start")){
			spoutConfig.forceFromStart = Boolean.parseBoolean(props.getProperty("force.from.start"));
		}
		
		if(props.containsKey("start.offset.time")){
			spoutConfig.startOffsetTime = Long.parseLong(props.getProperty("start.offset.time"));
		}
		
		logger.info(String.format("force.from.start: %b", spoutConfig.forceFromStart));
		logger.info(String.format("start.offset.time: %d", spoutConfig.startOffsetTime));
		
		return new KafkaSpout(spoutConfig);
	}

	public static String getExpectedProperty(Properties props, String name){
		if(!props.containsKey(name)){
			logger.error(String.format("The expected property %s was not found in your configuration file. Please check the value and try again.", name));
		}
		return props.getProperty(name, null);
	}
	
	
	public static Connection createHBaseConnection(Properties props) throws IOException{
		return ConnectionFactory.createConnection(getHBaseConfiguration(props));
	}
	
	public static Configuration getHBaseConfiguration(Properties props) throws IOException{
		Configuration conf = HBaseConfiguration.create();
		conf.set("zookeeper.znode.parent", StormUtils.getExpectedProperty(props, "zookeeper.znode.parent"));
		conf.set("hbase.zookeeper.quorum", StormUtils.getExpectedProperty(props, "hbase.zookeeper.quorum"));
		conf.set("hbase.zookeeper.property.clientPort",StormUtils.getExpectedProperty(props, "hbase.zookeeper.property.clientPort"));
		conf.set("hbase.master", StormUtils.getExpectedProperty(props, "hbase.master"));
		return conf;
	}

}
