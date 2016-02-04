package com.hwx.linearRoad.operators;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;

import com.hwx.linearRoad.hbase.TollHistoryDAO;
import com.hwx.linearRoad.hbase.TollHistoryDAO.DailyExpendature;
import com.hwx.linearRoad.hbase.VehicleDAO;
import com.hwx.linearRoad.operators.beans.VehicleStat;

public class QueryProcessingOp {
	
	private static final Logger logger = Logger.getLogger(QueryProcessingOp.class);
	
	private final VehicleDAO vehicleDAO;
	private final TollHistoryDAO historyDAO;

	public QueryProcessingOp(Connection conn) {
		this.vehicleDAO = new VehicleDAO(conn);
		this.historyDAO = new TollHistoryDAO(conn);
	}
	
	
	
	public String processCurrentBalance(int Time, long Tin, int QID, VehicleStat stat){
		vehicleDAO.getVehicleStat(stat);
		float emit = getEmit(Time, Tin);
		return String.format("2,%d,%4.10f,%d,%d,%d", Time, emit, stat.getTime(), QID, stat.getRunningTotal());

	}
	
	 //Input (Time, VID, XWay, QID, Day)
	public String processTollHistory(int Time, int VID, int XWay, int QID, int Day, long Tin){
		DailyExpendature d = historyDAO.new DailyExpendature(VID, Day, XWay);
		historyDAO.getDailyExpedature(d);
		float emit = getEmit(Time, Tin);
		//output: Type 3, Time, Emit, QID, Bal  
		return String.format("3,%d,%4.10f,%d,%d", Time, emit, QID, d.getToll());
	}
	
	private float getEmit(int Time, long Tin){
		return ((System.currentTimeMillis() - Tin)/1000f)+Time;
	}

}
