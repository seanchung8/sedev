package hortonworks.hdp.refapp.dashboard.web;

import hortonworks.hdp.apputil.registry.HDPServiceRegistry;
import hortonworks.hdp.refapp.config.registry.HDPRefAppServiceRegistryConfig;
import hortonworks.hdp.refapp.dashboard.DashboardConfiguration;
import hortonworks.hdp.refapp.ecm.registry.ECMBeanRefresher;
import hortonworks.hdp.refapp.registry.CustomRegistryKeys;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

	private static final Logger LOG = Logger.getLogger(DashboardController.class);
	
	@Autowired
	HDPServiceRegistry registry;
	
	@Autowired
	ApplicationContext context;
	
	@RequestMapping("/dashboard")
	public String renderLoginPage(Model model, HttpSession session) throws Exception {
		LOG.info("inside render dashboard...");
		LOG.info("Registry on renderLoginPage is: " + registry.getRegistry());
		HDPServiceRegistry userRegistry  = (HDPServiceRegistry) session.getAttribute("HDP.REGISTRY");
		if(userRegistry != null) {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String configFileName = auth.getName() + "-" + HDPRefAppServiceRegistryConfig.CONFIG_FILE_NAME;
			
			registry.populate(null, userRegistry.getRegistry(), configFileName);
			
			ECMBeanRefresher refresher = new ECMBeanRefresher(registry, context);
			refresher.refreshBeans();
		}
		model.addAttribute("dashboardConfig", constructDashboardConfig(registry));
		return "dashboard/dashboard";
	}

	private DashboardConfiguration constructDashboardConfig(HDPServiceRegistry registry) {
		DashboardConfiguration config = new DashboardConfiguration();
		
		/* Operations and Management */
		config.setAmbariServerUrl(registry.getAmbariServerUrl());
		// need to revisit this one
		config.setAmbariExperimentalUrl(null);
		String hueServerUrl = registry.getCustomValue(CustomRegistryKeys.HUE_SERVER_URL);
		config.setHueServerUrl( hueServerUrl);
		
		/* Security and Multi-Teanancy */
		config.setRangerServerUrl(registry.getCustomValue(CustomRegistryKeys.RANGER_SERVER_URL));
		config.setCSAmbariViewUrl(null);
		String resourceManagerUIUrl = registry.getResourceManagerUIURL();
		config.setResourceManagerUIUrl(constructResourceManagerRunningAppsUrl(resourceManagerUIUrl));
		config.setCSUtilizationUrl(constructCSUtilziationUrl(resourceManagerUIUrl));
		
		
		/* Data Pipelines, Metadata and Lineage */
		String falconServerUrl = registry.getFalconServerUrl();
		config.setFalconSeverUrl(falconServerUrl);
		config.setHueHCatalogUrl(constructHueHCatalogUrl(hueServerUrl));
		config.setFalconLineageUrl(constructFalconLineageUrl(falconServerUrl));
		config.setFalconTitanLineageRestUrl(constructFalconTitanLineageRestUrl(falconServerUrl));
		
		/* Stream Processing */
		config.setStormUIServerUrl(registry.getStormUIUrl());
		config.setActiveMQConsoleUrl(registry.getCustomValue(CustomRegistryKeys.ACTIVEMQ_ADMIN_URL));;
		
		/* Search Application */
		config.setSolrServerUrl(registry.getCustomValue(CustomRegistryKeys.SOLR_SERVER_URL));
		config.setBananaDashboardCompletedUrl(registry.getCustomValue(CustomRegistryKeys.SOLR_BANANA_DASH_COMPLETED_URL));;
		config.setBananaDashboardEmptyUrl(registry.getCustomValue(CustomRegistryKeys.SOLR_BANANA_DASH_EMPTY_URL));
		config.setDeleteTruckIndexUrl(registry.getCustomValue(CustomRegistryKeys.SOLR_DELETE_TRUCK_INDEX_URL));
		config.setSolrIndexPigJobHueUrl(registry.getCustomValue(CustomRegistryKeys.SOLR_INDEX_PIG_JOB_HUE_URL));
		
		/* Interactive Query Processing App */
		
		
		return config;
	}

	private String constructResourceManagerRunningAppsUrl(
			String resourceManagerUIUrl) {
		if(StringUtils.isEmpty(resourceManagerUIUrl)) {
			return null;
		}
		return resourceManagerUIUrl + "/cluster/apps/RUNNING";
	}

	private String constructFalconTitanLineageRestUrl(String falconServerUrl) {
		if(StringUtils.isEmpty(falconServerUrl))
			return null;
		return falconServerUrl + "api/graphs/lineage/vertices/all?user.name=dashboard";
	}

	private String constructCSUtilziationUrl(String resourceManagerUrl) {
		if(StringUtils.isEmpty(resourceManagerUrl))
			return null;
		return resourceManagerUrl + "/cluster/scheduler";
	}


	private String constructFalconLineageUrl(String falconServerUrl) {
		if(StringUtils.isEmpty(falconServerUrl)) 
			return null;
		return falconServerUrl + "/entity.html?type=process&id=solr-index-process";
	}

	private String constructHueHCatalogUrl(String hueServerUrl) {
		if(StringUtils.isEmpty(hueServerUrl))
			return null;
		return hueServerUrl + "/hcatalog";
	}
	
	
	private String getUrl(String url) {
		if(StringUtils.isEmpty(url))
			return "#";
		return url;
	}
	
}
