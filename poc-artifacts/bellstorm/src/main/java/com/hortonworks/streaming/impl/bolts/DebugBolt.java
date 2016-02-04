package com.hortonworks.streaming.impl.bolts;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class DebugBolt implements IRichBolt {

	private static final long serialVersionUID = -5319490672681173657L;
	private static final Logger LOG = Logger.getLogger(DebugBolt.class);
	
	private OutputCollector collector;
	private Properties config;

	
	public DebugBolt(Properties config) {
		this.config = config;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		collector.ack(input);
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

}
