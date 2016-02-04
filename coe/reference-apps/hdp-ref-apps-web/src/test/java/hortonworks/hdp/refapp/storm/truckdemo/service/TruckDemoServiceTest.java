package hortonworks.hdp.refapp.storm.truckdemo.service;

import hortonworks.hdp.refapp.BaseTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class TruckDemoServiceTest extends BaseTest {
	
	
	@Autowired
	TruckDemoService truckDemo;
	
	
	@Autowired
	ApplicationContext appContext;
	
	@Test
	public void getLatestEventsForAllDrivers() throws Exception {
		truckDemo.getLatestEventsForAllDrivers();
	}
	
//	@Test
//	public void getLatestEventsForAllDriversPhoenix() throws Exception {
//		truckDemo.getLatestEventsForAllDriversPhoenix();
//	}
	
	
//	@Test
//	public void truncateTables() throws Exception {
//		DemoResetParam param = new DemoResetParam();
//		param.setTruncateHbaseTables(true);
//		truckDemo.resetDemo(param);
//	}


	@Test
	public void testCreatingTruckingTables() throws Exception {
		truckDemo.createTablesForTruckingApp();
	}
}
