package hortonworks.hdp.refapp;

import hortonworks.hdp.apputil.registry.HDPServiceRegistry;
import hortonworks.hdp.apputil.registry.HDPServiceRegistryImpl;
import hortonworks.hdp.apputil.registry.RegistryKeys;
import hortonworks.hdp.refapp.config.registry.HDPRefAppServiceRegistryConfig;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(classes={TestConfig.class, HDPRefAppServiceRegistryConfig.class}) 
public class BaseTest extends AbstractJUnit4SpringContextTests {

	public static final String CONFIG_FILE_NAME = "trucking-streaming-hdp-service-config.properties";

	/**
	 * Default to relative path and local config if the property has not been set as part of -D setting
	 */
	@BeforeClass
	public static void setUpSystemRegistryConfigDirectoryLocation() {
		String serviceConfigDir = System.getProperty(RegistryKeys.SERVICE_REGISTRY_CONFIG_LOCATION_SYSTEM_PROP_KEY);
		
		if(StringUtils.isEmpty(serviceConfigDir)) {
			System.setProperty(RegistryKeys.SERVICE_REGISTRY_CONFIG_LOCATION_SYSTEM_PROP_KEY, "/config/local/registry");
			System.setProperty(RegistryKeys.SERVICE_REGISTRY_CONFIG_LOCATION_IS_ABSOLUTE_PROP_KEY, "false");
		}

	}	

	protected HDPServiceRegistry constructHDPServiceRegistry() throws Exception {
		// If System property is not set, then default to relative
		String serviceConfigDir = System.getProperty(RegistryKeys.SERVICE_REGISTRY_CONFIG_LOCATION_SYSTEM_PROP_KEY);
		boolean isAbsolute = true;
		if(StringUtils.isEmpty(serviceConfigDir)) {
			serviceConfigDir = "/config/dev/registry";
			isAbsolute = false;
		}
		
		HDPServiceRegistry serviceRegistry = new HDPServiceRegistryImpl(serviceConfigDir, CONFIG_FILE_NAME, isAbsolute);
		serviceRegistry.populate();
		return serviceRegistry;
	}		
	
//	private CustomHDPServiceRegistryParams createServiceRegistryParams() {
//		CustomHDPServiceRegistryParams params = new CustomHDPServiceRegistryParams();
//		params.setAmbariUrl("http://centralregion01.cloud.hortonworks.com:8080");
//		params.setClusterName("centralregioncluster");
//		
//		params.setHbaseDeploymentMode(DeploymentMode.SLIDER);
//		params.setHbaseSliderPublisherUrl(SLIDER_HBASE_PUBLISHER_URL);
//		params.setStormDeploymentMode(DeploymentMode.SLIDER);
//		params.setStormSliderPublisherUrl(SLIDER_STORM_PUBLISHER_URL);
//		
//		params.setActiveMQConnectionUrl("tcp://george-activemq01.cloud.hortonworks.com:61616");
//		params.setActiveMQHost("george-activemq01.cloud.hortonworks.com");
//		
//		params.setMailSmtpHost("hadoopsummit-stormapp.secloud.hortonworks.com");
//		params.setSolrServerUrl("http://george-search01.cloud.hortonworks.com:8983/solr");
//		
//
//		params.setHueServerUrl("http://centralregion10.cloud.hortonworks.com:8000");
//		params.setRangerServerUrl("http://george-security01.cloud.hortonworks.com:6080/");
//		params.setActiveMQConsoleUrl("http://george-activemq01.cloud.hortonworks.com:8161/admin/topics.jsp");
//		params.setBananaDashboardCompletedUrl("http://george-search01.cloud.hortonworks.com:8983/banana/#/dashboard/solr/Truck%20Events%20Dashboard%20V4");
//		params.setBananaDashboardEmptyUrl("http://george-search01.cloud.hortonworks.com:8983/banana/#/dashboard/solr/Demo%20Trucking%20Events%20Dashboard%20V3");
//		params.setDeleteTruckIndexUrl("http://george-search01.cloud.hortonworks.com:8983/solr/truck_event_logs/update?stream.body=%3Cdelete%3E%3Cquery%3Eid:*%3C/query%3E%3C/delete%3E&commit=true");
//		params.setSolrIndexPigJobHueUrl("http://stormapp01.cloud.hortonworks.com:8000/pig/3");
//		return params;
//	}	
	
	
		
}
