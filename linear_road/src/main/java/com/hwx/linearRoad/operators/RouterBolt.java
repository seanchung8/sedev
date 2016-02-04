package com.hwx.linearRoad.operators;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class RouterBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(RouterBolt.class);
	private final List<String> inputFields;
	
	private OutputCollector collector;
	
	public final String positionReportOutputStreamName = "positionReportOutputStream";
	public final String queryOutputStreamName = "queriesOutput";

	/**
	 * Responsible for routing Type 0 records (PostionReports) to the postionsReportOutput stream
	 * and Type 2 and 3 records (historic queries) to the queriesOutput stream
	 * @param props
	 * @param inputFields - List of the fields from the tuple that is coming in. This bolt does not modfiy the underlying input Tuple.
	 */
	public RouterBolt(Properties props, List<String> inputFields) {
		this.inputFields = inputFields;
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void execute(Tuple input) {

		int type = input.getIntegerByField("Type");
		if(0 == type){
			//0's go here...
			collector.emit(positionReportOutputStreamName, input, input.getValues());
		}else if(4 != type){
			//2's and 3's go here, 4's get thrown away...
			collector.emit(queryOutputStreamName, input, input.getValues());
		}
		collector.ack(input);

	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
		this.collector = collector;

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(positionReportOutputStreamName, new Fields(inputFields));
		declarer.declareStream(queryOutputStreamName, new Fields(inputFields));

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	
}
