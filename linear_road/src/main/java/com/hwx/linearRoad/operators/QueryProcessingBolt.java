package com.hwx.linearRoad.operators;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.hwx.linearRoad.hbase.TollHistoryDAO;
import com.hwx.linearRoad.hbase.TollHistoryDAO.DailyExpendature;
import com.hwx.linearRoad.hbase.VehicleDAO;
import com.hwx.linearRoad.operators.beans.VehicleStat;
import com.hwx.linearRoad.utils.StormUtils;


public class QueryProcessingBolt implements IRichBolt {
	
	/**
	 * QueryProcessingBolt - Feeds off Stream from RouterBolt
	 * 
	 * Implements logic for query command (Type 2)
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(QueryProcessingBolt.class);
	private OutputCollector collector;
	
	private final Properties props;
	private Connection conn;
	private QueryProcessingOp op;
	

	public QueryProcessingBolt(Properties props) {
		this.props = props;

	}

	@Override
	public void cleanup() {
		if(conn != null && !conn.isClosed())
			try {
				conn.close();
			} catch (IOException e) {
				//do nothing
			}
	}

	@Override
	public void execute(Tuple input) {
		/** 
		 * First, ensure that the input tuple is a Query command, one of these types:
		 * 
		 * Type 2: Account Balance
		 * 	   Tuple (Type = 2, Time, VID, QID)
		 * 
		 * Type 3: Daily Expenditure
		 *     Type (Type = 3, Time, VID, XWay, QID, Day)
		 **/ 
		int type = input.getIntegerByField("Type");
		long enumTime = -1;

		
		switch (type) {
			case 2:
				VehicleStat stat = new VehicleStat(input.getIntegerByField("Vid"));
				collector.emit(input, new Values(op.processCurrentBalance(
						input.getIntegerByField("Time").intValue(), 
						input.getLongByField("Tin").longValue(), 
						input.getIntegerByField("QID").intValue(), 
						stat)));
				break;
				
			case 3:
				collector.emit(input, new Values(
					op.processTollHistory(
							input.getIntegerByField("Time").intValue(), 
							input.getIntegerByField("Vid").intValue(),
							input.getIntegerByField("XWay").intValue(),
							input.getIntegerByField("QID").intValue(), 
							input.getIntegerByField("DAY").intValue(), 
							input.getLongByField("Tin").longValue())
				));
				break;
				

		}
		
		collector.ack(input);

	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector coll) {
		this.collector = coll;
		
		try{
			conn = StormUtils.createHBaseConnection(props);
		}catch(IOException e){
			logger.error("Exception instantiating HBase connection");
			e.printStackTrace();
			System.exit(1);
		}
		
		this.op = new QueryProcessingOp(conn);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//just declare one output field of the formatted tuple that will be written out
		declarer.declare(new Fields("output"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	
	
	
}
