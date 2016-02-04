package hortonworks.hdp.refapp.falcon.management.service;

import hortonworks.hdp.refapp.BaseTest;
import hortonworks.hdp.refapp.falcon.management.FalconDeploymentParams;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



public class FalconServiceTest extends BaseTest{


	@Autowired
	private FalconService falconDeploymentService;


	
	@Test
	public void testDeployTruckingDataPipelineWithAllProcessesExceptPhoenixUpdate() throws Exception{
		FalconDeploymentParams deploymentParams = new FalconDeploymentParams();
		deploymentParams.setScheduleIndexingDataPipeline(true);
		deploymentParams.setSchedulePhoenixUpdateDataPipeline(false);
		
		falconDeploymentService.deployTruckingDataPipelines(deploymentParams);
	}	
	
	@Test
	public void testTearDownTruckingDataPipelines() throws Exception{
		falconDeploymentService.tearDownTruckingDataPipelines();
	}
	
		

}
