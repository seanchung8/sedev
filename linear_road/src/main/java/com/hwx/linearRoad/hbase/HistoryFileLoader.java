package com.hwx.linearRoad.hbase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;

import com.hwx.linearRoad.hbase.TollHistoryDAO.DailyExpendature;
import com.hwx.linearRoad.utils.StormUtils;
import com.hwx.linearRoad.utils.TableUtil;

public class HistoryFileLoader {
	
	private static final Logger logger = Logger.getLogger(HistoryFileLoader.class);
	private final Properties topologyConfig;
	private int recordCounter = 0;
	private int recordLimit;

	public static void main(String[] args) throws Exception{
		new HistoryFileLoader(args);

	}
	
	
	public HistoryFileLoader(String[] args) throws Exception{
		if(args.length<2){
			usage();
		}
		
		if(args.length==3){
			recordLimit = Integer.parseInt(args[2]);
		}else{
			recordLimit = Integer.MAX_VALUE;
		}
		
		
		topologyConfig = new Properties();
		try {
			topologyConfig.load(new FileInputStream(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		Connection conn = StormUtils.createHBaseConnection(topologyConfig);
		TollHistoryDAO loader = new TollHistoryDAO(conn);
		confirmTable(conn, loader);
		
		ArrayList<DailyExpendature> inserts = new ArrayList<DailyExpendature>();
		String temp[];
		
		BufferedReader br = new BufferedReader(new FileReader(args[1]));
		for(String line; (line = br.readLine()) != null && recordCounter<recordLimit; ) {
			
			temp = line.split(",");
			inserts.add(loader.new DailyExpendature(
					Integer.parseInt(temp[0]), //VID
					Integer.parseInt(temp[1]), //day
					Integer.parseInt(temp[2]), //Xway
					Integer.parseInt(temp[3]))); //toll
			
			
			if(++recordCounter%1000==0){
				logger.info(String.format("Loaded %d rows", recordCounter));
				loader.putDailyExpendature(inserts);
				inserts.clear();
			}
			
		}
		//flush whatever is left over at end of file... 
		if(inserts.size()>0)
			loader.putDailyExpendature(inserts);
		logger.info(String.format("Loaded %d rows", recordCounter));
		br.close();
		conn.close();
	}
	
	
	
	/*
	 * Confirms the table exists and truncates it before adding the new data, otherwise create the table
	 */
	public void confirmTable(Connection conn, TollHistoryDAO loader) throws Exception{
		HTableDescriptor t = TableUtil.verifyTable(conn, loader.getTableName());
		if(t!=null){
			//truncate the table
			TableUtil.truncateTable(conn, loader.getTableName(), true);
		}else{
			TableUtil.createTable(conn, loader.getTableName(), loader.getColumnFamilies());
		}
	}
	
	
	
	
	public int getRecordInsertedCount(){
		return this.recordCounter;
	}
	
	
	
	
	private void usage(){
		logger.error("HistoryFileLoader requires two arguments to run:");
		logger.error("\t Path to a configuration file for HBase");
		logger.error("\t Path to a Toll History file that is to be loaded");
		logger.error("\t (optional) the max number of records to be loaded");
		System.exit(-1);
	}
	
	
	
	

}
