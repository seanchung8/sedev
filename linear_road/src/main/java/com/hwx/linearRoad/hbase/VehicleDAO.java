package com.hwx.linearRoad.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.hwx.linearRoad.operators.beans.VehicleStat;

public class VehicleDAO {
		
	private static final Logger logger = Logger.getLogger(VehicleDAO.class);
	private final Connection conn;
	private static final String vehicleTableName = "VehicleStatsTable";
	private static final String[] colFamName = new String[]{"cf1"};
	private static final byte[] columnFamily = Bytes.toBytes("cf1");
	
	//fields
	private static final byte[] Lane = Bytes.toBytes("VID");
	private static final byte[] Pos = Bytes.toBytes("Pos");
	private static final byte[] Seg = Bytes.toBytes("Seg");
	private static final byte[] Time = Bytes.toBytes("Time");
	private static final byte[] Dir = Bytes.toBytes("Dir");
	private static final byte[] Xway = Bytes.toBytes("Xway");
	private static final byte[] Total = Bytes.toBytes("Total");

	public VehicleDAO(Connection conn) {
		this.conn = conn;
	}
	
	public void getVehicleStat(VehicleStat statObj){
		
		//make the call to HBase table using the VID value from the statObj
		Get g = new Get(Bytes.toBytes(statObj.getVID())).addFamily(columnFamily);
				
		
		try {
			Table t = conn.getTable(TableName.valueOf(vehicleTableName));
			Result r = t.get(g);
			if(!r.isEmpty()){
				statObj.setDir(Bytes.toInt(r.getValue(columnFamily, Dir)));
				statObj.setLane(Bytes.toInt(r.getValue(columnFamily, Lane)));
				statObj.setPos(Bytes.toInt(r.getValue(columnFamily, Pos)));
				statObj.setRunningTotal(Bytes.toInt(r.getValue(columnFamily, Total)));
				statObj.setSeg(Bytes.toInt(r.getValue(columnFamily, Seg)));
				statObj.setTime(Bytes.toInt(r.getValue(columnFamily, Time)));
				statObj.setXWay(Bytes.toInt(r.getValue(columnFamily, Xway)));
			}
			t.close();
		} catch (IOException e) {
			logger.error(String.format("Error retrieving record VID %d from HBase table %s", statObj.getVID(), vehicleTableName));
			logger.error(e.getStackTrace());
		}
	}
	
	public void putVehicleStat(VehicleStat statObj){
		//put the statObj record into the VehicleStatsTable
		
		//ensure the most recent record is near the top of the hfile...
		long ts = statObj.getTime();
		Put p = new Put(Bytes.toBytes(statObj.getVID()));
		p.setDurability(Durability.SKIP_WAL);

		p.addColumn(columnFamily, Lane, ts, Bytes.toBytes(statObj.getLane()));
		p.addColumn(columnFamily, Pos, ts, Bytes.toBytes(statObj.getPos()));
		p.addColumn(columnFamily, Seg, ts, Bytes.toBytes(statObj.getSeg()));
		p.addColumn(columnFamily, Time, ts, Bytes.toBytes(statObj.getTime()));
		p.addColumn(columnFamily, Dir, ts, Bytes.toBytes(statObj.getDir()));
		p.addColumn(columnFamily, Xway, ts, Bytes.toBytes(statObj.getXWay()));
		p.addColumn(columnFamily, Total, ts, Bytes.toBytes(statObj.getRunningTotal()));
		
		try{
			Table t = conn.getTable(TableName.valueOf(vehicleTableName));
			t.put(p);
			t.close();
		}catch(IOException e){
			logger.error(String.format("Unable to update %s with VID %d", vehicleTableName, statObj.getVID()));
			logger.error(e.getStackTrace());
			return;
		}
	}
	
	public static String getTableName(){
		return vehicleTableName;
	}
	
	public static String[] getColFamilies(){
		return colFamName;
	}
	
	

}
