package hortonworks.hdp.refapp.storm.management.service;

import hortonworks.hdp.apputil.storm.StormTopologyParams;
import hortonworks.hdp.refapp.BaseTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



public class StormServiceTest extends BaseTest{


	private static final String TOPOLOGY_NAME = "truck-event-processor";
	
	
	@Autowired
	private StormService stormDeploymentService;
	

	
	@Test
	public void testDeployServiceUpload() throws Exception{
		
		StormTopologyParams topologyParams = new StormTopologyParams();
		topologyParams.setUpload(true);	
		topologyParams.setTopologyName(TOPOLOGY_NAME);
		
		stormDeploymentService.deployStormTopology(topologyParams);
	}
	
	
	
	@Test
	public void testKillStormTopology() throws Exception {
		stormDeploymentService.killStormTopology();
	}
	

	
//	@Test
//	public void testDeployServiceNoUpload() throws Exception{
//		
//		StormTopologyParams topologyParams = new StormTopologyParams();
//		topologyParams.setUpload(false);	
//		topologyParams.setUploadedTopologyJarLocation("/mnt/hadoop/storm/nimbus/staging/stormjar-ac1fe301-3883-41a2-9962-8e258539e50b.jar");
//		stormDeploymentService.deployStormTopology(topologyParams);
//	}	

}
