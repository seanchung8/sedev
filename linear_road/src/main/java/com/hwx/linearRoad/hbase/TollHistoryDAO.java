package com.hwx.linearRoad.hbase;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

public class TollHistoryDAO {
	
	private static final Logger logger = Logger.getLogger(TollHistoryDAO.class);
	private final Connection conn;
	private static final String tollHistoryTable = "TollHistoryTable";
	private static final String[] colFamName = new String[]{"cf1"};
	private final byte[] columnFamily = Bytes.toBytes("cf1");
	

	public TollHistoryDAO(Connection conn) {
		this.conn = conn;
	}
	
	public void getDailyExpedature(DailyExpendature query){
		
		//The row is indexed by the VID, query.day is the timestamp value and we want the toll value under the column identifier query.Xway
		byte[] colName = Bytes.toBytes(query.Xway);

		try {
			Get g = new Get(Bytes.toBytes(query.getVID())).addColumn(columnFamily, colName).setTimeStamp(query.day);
			Table t = conn.getTable(TableName.valueOf(tollHistoryTable));
			Result r = t.get(g);
			if(!r.isEmpty()){
				query.Toll = Bytes.toInt(r.getValue(columnFamily, colName));
			}
			t.close();
		} catch (IOException e) {
			logger.error(String.format("Error retrieving record VID %d from HBase table %s", query.getVID(), tollHistoryTable));
			logger.error(e.getStackTrace());
		}
	}
	
	protected void putDailyExpendature(List<DailyExpendature> inserts){
		
		//This is a bulk loader that assumes it is given a list of DailyExpendature records for a given VID
		//get the IVD rowkey from the first record
		Put p = new Put(Bytes.toBytes(inserts.get(0).VID));
		p.setDurability(Durability.SKIP_WAL);
		
		for (DailyExpendature d : inserts){
			p.addColumn(columnFamily, Bytes.toBytes(d.Xway), d.day, Bytes.toBytes(d.Toll));
		}
		
		try{
			Table t = conn.getTable(TableName.valueOf(tollHistoryTable));
			t.put(p);
			t.close();
		}catch(IOException e){
			logger.error(String.format("Unable to update %s with VID %d, %d records were not inserted", tollHistoryTable, inserts.get(0).VID, inserts.size()));
			logger.error(e.getStackTrace());
			return;
		}
		
	}
	
	public String getTableName(){
		return tollHistoryTable;
	}
	
	public String[] getColumnFamilies(){
		return colFamName;
	}
	
	public class DailyExpendature{
		
		private int VID;
		private int day;
		private int Xway;
		private int Toll = -1;
		
		/**
		 * Public constructor for external class to create a Daily Expendature object to pass to the getDailyExpendature
		 * If the return Toll value is -1, then no mathing record in HBase was found.
		 * @param VID - The Vehicle ID
		 * @param day - The day
		 * @param Xway - The ExpressWay
		 */
		public DailyExpendature(int VID, int day, int Xway){
			this(VID, day, Xway, -1);	
		}
		
		/**
		 * Protected constructor used by the History Data Loader
		 * 
		 * @param VID
		 * @param day
		 * @param Xway
		 * @param Toll
		 */
		protected DailyExpendature(int VID, int day, int Xway, int Toll){
			this.VID = VID;
			this.day = day;
			this.Xway = Xway;
			this.Toll = Toll;
		}

		public int getVID() {
			return VID;
		}

		public void setVID(int vID) {
			VID = vID;
		}

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public int getXway() {
			return Xway;
		}


		public int getToll() {
			return Toll;
		}

		
		
	}

}
