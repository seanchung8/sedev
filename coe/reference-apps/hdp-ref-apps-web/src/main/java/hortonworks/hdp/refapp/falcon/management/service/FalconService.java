package hortonworks.hdp.refapp.falcon.management.service;

import hortonworks.hdp.apputil.falcon.FalconUtils;
import hortonworks.hdp.apputil.registry.HDPServiceRegistry;
import hortonworks.hdp.refapp.falcon.management.FalconDeploymentParams;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FalconService {
	
	private static final Logger LOG = Logger.getLogger(FalconService.class);
	private static final String USER_SUBMITTING_JOB = "user.name=hdfs";

	
	private HDPServiceRegistry serviceRegistry;
	
	
	@Autowired
	public FalconService(HDPServiceRegistry registry) {
		this.serviceRegistry = registry;
	}
	
	
	public void deployTruckingDataPipelines(FalconDeploymentParams deploymentParams) throws Exception {
		
		FalconUtils falconUtils = new FalconUtils(serviceRegistry);
		try {
			LOG.info("Tearing down data piplines before deploying..");
			tearDownTruckingDataPipelines();
			LOG.info("Successfully tore down data piplines before deploying..");
		} catch (Exception e) {
			LOG.error("Error tearding down data pipelines. Continuing deploying..", e);
		}
		
		LOG.info("Starting deployment of data pipelines");
		/* Submit Cluster */
		String clusterFileName = "/falcon/entities/cluster/primary-cluster.xml";
		falconUtils.submitClusterEntity(clusterFileName);		
		
		/* Submit and Schedule Feeds */
		String rawDataFeedFile = "falcon/entities/feed/raw-data-feed.xml";
		falconUtils.submitAndScheduleFeedEntity(rawDataFeedFile);
		
		String orcDataFeed = "falcon/entities/feed/orc-data-feed.xml";
		falconUtils.submitAndScheduleFeedEntity(orcDataFeed);
		
		/* Submit and Schedule Processes */
		String orcConversionProcessFile = "falcon/entities/process/orc-conversion-process.xml";
		falconUtils.submitAndScheduleProcessEntity(orcConversionProcessFile);
		
		if(deploymentParams.isScheduleIndexingDataPipeline()) {
			String solrIndexProcessFile = "falcon/entities/process/solr-index-process.xml";
			falconUtils.submitAndScheduleProcessEntity(solrIndexProcessFile);			
		}
		
		if(deploymentParams.isSchedulePhoenixUpdateDataPipeline()) {
			String updatePhoenixProcessFile = "falcon/entities/process/update-phoenix-hbase-process.xml";
			falconUtils.submitAndScheduleProcessEntity(updatePhoenixProcessFile);				
		}
			
		
		LOG.info("Finished successful deployment of data pipelines");
		
	}
	
	public void tearDownTruckingDataPipelines() throws Exception {

		
		FalconUtils falconUtils = new FalconUtils(serviceRegistry);
		
		/* Delete Feeds */
//		String phoenixProcessName = "update-phoenix-hbase-process";
//		falconUtils.deleteProcessEntity(phoenixProcessName);
		
		String solrIndexProcess = "solr-index-process";
		falconUtils.deleteProcessEntity(solrIndexProcess);		
		
		String orcIndexProcess = "orc-conversion-process";
		falconUtils.deleteProcessEntity(orcIndexProcess);
		
		/* Delete Feeds */
		String rawDataFeedName = "truck-events-raw-data-feed";
		falconUtils.deleteFeedEntity(rawDataFeedName);
		
		String orcDataFeedName = "truck-events-orc-data-feed";
		falconUtils.deleteFeedEntity(orcDataFeedName);
		
		/* Delete Cluster */
		String clusterName = "george-cluster";
		falconUtils.deleteClusterEntity(clusterName);		
	}
	
	

}

