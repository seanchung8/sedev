package com.hwx.linearRoad.operators;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.hwx.linearRoad.AbstractTestCase;
import com.hwx.linearRoad.operators.PositionReportOp.PositionReportRec;
import com.hwx.linearRoad.operators.beans.Notification;
import com.hwx.linearRoad.operators.beans.VehicleStat;
import com.hwx.linearRoad.utils.StormUtils;
import com.hwx.linearRoad.utils.TableUtil;

public class OperatorTestCase extends AbstractTestCase{
	
	private static final Logger logger = Logger.getLogger(OperatorTestCase.class);
	//the path to the config.properties file under src/test/resources/com/hwx/linearRoad
	
	@Test
	public void testCase() throws Exception{
		Connection conn = getHBaseConnection();
		TableUtil.truncateTable(conn, "VehicleStatsTable", true);
		String inputFile = getResourcePath("1.xway.final.dat");
		testCaseRunner(conn, inputFile);
		
		conn.close();
	}
	
	
	private Connection getHBaseConnection() throws Exception{
		String configPath = getResourcePath(OperatorTestCase.class)+"/../config.properties";
		Properties props = new Properties();
		props.load(new FileInputStream(configPath));
		return StormUtils.createHBaseConnection(props);
	}
	
	
	private void testCaseRunner(Connection conn, String inputFile) throws Exception{
		
		String configPath = getResourcePath(OperatorTestCase.class)+"/../config.properties";
		Properties props = new Properties();
		props.load(new FileInputStream(configPath));

		PositionReportBolt bolt = new PositionReportBolt(props);
		bolt.prepare(null, null, null);		
		PositionReportOp op = bolt.getPositionReportOp();
		QueryProcessingOp qop = new QueryProcessingOp(conn);
		
		//Time, VID, Spd, Xway, Lane, Dir, Seg, Pos, Tin
		PositionReportRec rec = op.new PositionReportRec(-1, -1, -1, -1, -1, -1, -1, -1, -1);
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
    		
		    for(String line; (line = br.readLine()) != null; ) {
		    	String[] tmp = line.split(",");
		    	
		    	switch(Integer.parseInt(tmp[0])){
		    	
		    	case 0:
		    		
		    		rec.Time=Integer.parseInt(tmp[1]);
		    		rec.VID=Integer.parseInt(tmp[2]);
		    		rec.Spd=Integer.parseInt(tmp[3]);
		    		rec.XWay=Integer.parseInt(tmp[4]);
		    		rec.Lane=Integer.parseInt(tmp[5]);
		    		rec.Dir=Integer.parseInt(tmp[6]);
		    		rec.Seg=Integer.parseInt(tmp[7]);
		    		rec.Pos=Integer.parseInt(tmp[8]);
		    		rec.Tin=System.currentTimeMillis();
		    		
		    		Notification n = op.process(rec);
		    		
		    		if(n!=null){
		    			logger.info(n.asString());
		    		}
		    		
		    		break;
		    		
		    	case 2:
		    		VehicleStat stat = new VehicleStat(Integer.parseInt(tmp[2]));
		    		logger.info(
		    				qop.processCurrentBalance(
		    						Integer.parseInt(tmp[1]),
		    						System.currentTimeMillis(),
		    						Integer.parseInt(tmp[9]),
		    						stat)
		    				);
		    		//output: type 2, time, emit, resultTime, QUI, balance
		    		break;
		    		
		    	case 3:
		    		
		    		logger.info(
		    				qop.processTollHistory(
				    				Integer.parseInt(tmp[1]), 
				    				Integer.parseInt(tmp[2]), 
				    				Integer.parseInt(tmp[4]), 
				    				Integer.parseInt(tmp[9]), 
				    				Integer.parseInt(tmp[14]), 
				    				System.currentTimeMillis())
				    		);
		    		
		    		break;
		    		
		    	}
		       
		    }
		    br.close();		    
		
	}

	

}
