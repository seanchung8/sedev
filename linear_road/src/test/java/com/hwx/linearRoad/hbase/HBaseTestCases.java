package com.hwx.linearRoad.hbase;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;

import com.hwx.linearRoad.AbstractTestCase;
import com.hwx.linearRoad.hbase.TollHistoryDAO.DailyExpendature;
import com.hwx.linearRoad.operators.beans.VehicleStat;
import com.hwx.linearRoad.utils.StormUtils;
import com.hwx.linearRoad.utils.TableUtil;

public class HBaseTestCases extends AbstractTestCase {
	
	private static final Logger logger = Logger.getLogger(HBaseTestCases.class);

	//tested and working
	@Ignore
	public void testHBaseConnection() throws Exception{
		
		Connection conn = getHBaseConnection();
		//will drop and recreate "VehicleStatsTable"
		TableUtil.createTable(true, conn, VehicleDAO.getTableName(), VehicleDAO.getColFamilies());
		
		//make a test query to the DB just to verify that you can connect
		VehicleStat stat = new VehicleStat(10);
		VehicleDAO dao = new VehicleDAO(conn);
		dao.getVehicleStat(stat);
		
		conn.close();
	}
	
	
	//tested and working
	@Ignore
	public void testHistoryFileLoader() throws Exception{
		
		String configFile = getResourcePath(HBaseTestCases.class)+"/../config.properties";
		String historyFile = getResourcePath("tollHistory100k.dat");
		String recordLimit = "10000";
		
		HistoryFileLoader loader = new HistoryFileLoader(new String[]{configFile, historyFile, recordLimit});
		logger.info(String.format("%d messages were loaded into the Toll History table", loader.getRecordInsertedCount()));		
	}
	
	//tested and working
	@Ignore
	public void testTollHistoryGet() throws Exception{
		
		Connection conn = getHBaseConnection();
		TollHistoryDAO history = new TollHistoryDAO(conn);
		DailyExpendature q = history.new DailyExpendature(1, 1, 0);
		history.getDailyExpedature(q);
		Assert.assertEquals(43, q.getToll());
		
	}
	
	
	
	
	
	
	private Connection getHBaseConnection() throws Exception{
		String configPath = getResourcePath(HBaseTestCases.class)+"/../config.properties";
		Properties props = new Properties();
		props.load(new FileInputStream(configPath));
		return StormUtils.createHBaseConnection(props);
	}

}
