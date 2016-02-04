package com.hwx.linearRoad.operators;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.hwx.linearRoad.operators.PositionReportOp.PositionReportRec;
import com.hwx.linearRoad.operators.PositionReportOp.SegStatKey;
import com.hwx.linearRoad.operators.PositionReportOp.SegmentStat;
import com.hwx.linearRoad.operators.beans.Notification;
import com.hwx.linearRoad.operators.beans.VehicleStat;
import com.hwx.linearRoad.utils.StormUtils;

public class PositionReportBolt implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PositionReportBolt.class);
	private final Properties props;
	private OutputCollector collector;

	private Connection conn;
	private PositionReportOp op;

	public PositionReportBolt(Properties props) {
		this.props = props;
		
		logger.info(String.format("Instantiating new PositionReportBolt: %s", this.toString()));
		
		
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
		//if the input tuple is a Position Report record - pass it to the Postion Report Operator for processing
		if(0 == input.getIntegerByField("Type")){

			PositionReportRec rec = op.new PositionReportRec(input.getIntegerByField("Time"),
					input.getIntegerByField("Vid"), 
					input.getIntegerByField("Spd"), 
					input.getIntegerByField("XWay"), 
					input.getIntegerByField("Lane"), 
					input.getIntegerByField("Dir"), 
					input.getIntegerByField("Seg"), 
					input.getIntegerByField("Pos"), 
					input.getLongByField("Tin"));
			
//			logger.info(String.format("Task: %s, Xway: %d, Dir: %d", this.toString(), input.getIntegerByField("XWay"), input.getIntegerByField("Dir")));

			op.process(rec);

			if(op.getTollNotification()!=null){
				collector.emit(input, new Values(op.getTollNotification().asString()));
			}
			
			if(op.getAccidentNotification()!=null){
				collector.emit(input, new Values(op.getAccidentNotification().asString()));
			}
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
		
		logger.info(String.format("Instantiating new PositionReportOp is null: %b", op == null));
		op = new PositionReportOp(conn);
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//this bolt adds no fields to the input tuple
		declarer.declare(new Fields("output"));

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
	public PositionReportOp getPositionReportOp(){
		return this.op;
	}
	
//private synchronized void foo(){
//		
//		if (vehicleCache==null){
//			vehicleCache = new ConcurrentHashMap<Integer, VehicleStat>(250);
//			logger.info("creating new vehicle cache!");
//		}
//		
//		if (segmentCache==null){
//			segmentCache = new ConcurrentHashMap<SegStatKey, SegmentStat>(250);
//			logger.info("creating new segment cache!");
//		}
//	}



}
