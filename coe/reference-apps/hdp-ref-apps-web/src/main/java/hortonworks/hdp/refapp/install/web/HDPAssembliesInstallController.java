package hortonworks.hdp.refapp.install.web;

import hortonworks.hdp.apputil.registry.DeploymentMode;
import hortonworks.hdp.apputil.registry.HDPServiceRegistry;
import hortonworks.hdp.apputil.registry.ServiceRegistryParams;
import hortonworks.hdp.apputil.storm.StormTopologyParams;
import hortonworks.hdp.refapp.config.registry.HDPRefAppServiceRegistryConfig;
import hortonworks.hdp.refapp.ecm.registry.ECMBeanRefresher;
import hortonworks.hdp.refapp.falcon.management.FalconDeploymentParams;
import hortonworks.hdp.refapp.falcon.management.service.FalconService;
import hortonworks.hdp.refapp.install.AssembliesParam;
import hortonworks.hdp.refapp.registry.CustomHDPServiceRegistryParams;
import hortonworks.hdp.refapp.registry.CustomRegistryKeys;
import hortonworks.hdp.refapp.storm.management.service.StormService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HDPAssembliesInstallController {
	
	private static final String DEFAULT_STORM_PUBLISHER_URL = "http://centralregion09.cloud.hortonworks.com:56232/ws/v1/slider/publisher";
	private static final String DEFAULT_HBASE_PUBLISHER_URL = "http://centralregion09.cloud.hortonworks.com:34112/ws/v1/slider/publisher";
	
	
	private static final Logger LOG = Logger.getLogger(HDPAssembliesInstallController.class);
	
	@Autowired
	private StormService stormService;
	
	@Autowired
	private FalconService falconService;
	
	@Autowired
	private HDPServiceRegistry registry;
	
	@Autowired
	ApplicationContext context;
	
	
	@RequestMapping(value="install/step1", method=RequestMethod.GET)
	public String step1Render(Model model) {
		String view = assembleyRefAppSelectionView(model);
		model.addAttribute("action", "step1");
		return  view;	
	}	
	
	@RequestMapping(value="install/step1", method=RequestMethod.POST)
	public String step1Request(AssembliesParam param) {
		selectAssembly(param);
		return "redirect:/install/step2";	
	}	
	
	@RequestMapping(value="install/step2", method=RequestMethod.GET)
	public String step2Render(Model model) {
		
		String view = serviceRegistryView(model);
		model.addAttribute("action", "step2");
		return view;
	}
	
	@RequestMapping(value="install/step2", method=RequestMethod.POST)
	public String step2Request(CustomHDPServiceRegistryParams registryParams, HttpSession session) throws Exception {
		
		populateServiceRegistryw(registryParams, session);
		return "redirect:/install/step3";
	}	
	
	
	@RequestMapping(value="install/step3", method=RequestMethod.GET)
	public String step3Render(Model model) {
		String view = truckingTopologyView(model);
		model.addAttribute("action", "step3");
		return view;
	}
	
	@RequestMapping(value="install/step3", method=RequestMethod.POST)
	public String step3Request(StormTopologyParams topologyParams, HttpSession session) {
		configureAndDeployTruckingTopology(topologyParams, session);
		return "redirect:/install/step4";
	}	
	
	
	@RequestMapping(value="install/step4", method=RequestMethod.GET)
	public String step4Render(Model model) {
		String view = dataPipelinesView(model); 
		model.addAttribute("action", "step4");
		return view;
	}
	
	@RequestMapping(value="install/step4", method=RequestMethod.POST)
	public String step4REquest(FalconDeploymentParams deploymentParams, HttpSession session) {
		configureAndDeployDataPipeline(deploymentParams, session);
		return "redirect:/dashboard";
	}

	
	@RequestMapping(value="install/assemblyselection", method=RequestMethod.GET)
	public String assembleyRefAppSelectionView(Model model) {
		AssembliesParam param = new AssembliesParam();
		model.addAttribute("assembliesParam", param);
		model.addAttribute("action", "assemblyselection");
		return "install/assembly-selection";
			
	}
		
	@RequestMapping(value="install/assemblyselection", method=RequestMethod.POST)
	public String selectAssembly(AssembliesParam param) {
		return "redirect:/dashboard";
			
	}	
	
	@RequestMapping(value="install/serviceregistry", method=RequestMethod.GET)
	public String serviceRegistryView(Model model) {
		ServiceRegistryParams params = createDefaultRegistryParams();
		model.addAttribute("registryParams", params);
		model.addAttribute("action", "serviceregistry");
		return "install/service-registry";
			
	}
	
	@RequestMapping(value="install/serviceregistry", method=RequestMethod.POST)
	public String populateServiceRegistryw(CustomHDPServiceRegistryParams registryParams, HttpSession session) throws Exception {
		LOG.info("Populating Service Registry");
		LOG.info("Ambari Server URL[" + registryParams.getAmbariUrl() + ", cluster name["+registryParams.getClusterName()+" ]");
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String configFileName = auth.getName() + "-" + HDPRefAppServiceRegistryConfig.CONFIG_FILE_NAME;
			
			HDPServiceRegistry registry = getServiceRegistry(session);
			Map<String, String> customParamsMap = constructCustomParamsMap(registryParams);
			registry.populate(registryParams, customParamsMap, configFileName);
			registry.writeToPropertiesFile();
			
			
			ECMBeanRefresher refresher = new ECMBeanRefresher(registry, context);
			refresher.refreshBeans();			
		} catch (Exception e) {
			LOG.error("Error populating registry:"+e.getMessage(), e);
		}
		return "redirect:/dashboard"; 
	}


		
		
	private Map<String, String> constructCustomParamsMap(
			CustomHDPServiceRegistryParams registryParams) {
		Map<String, String> customParamsMap = new HashMap<String, String>();
		customParamsMap.put(CustomRegistryKeys.ACTIVEMQ_CONNECTION_URL, registryParams.getActiveMQConnectionUrl());
		customParamsMap.put(CustomRegistryKeys.ACTIVEMQ_ADMIN_URL, registryParams.getActiveMQConsoleUrl());
		customParamsMap.put(CustomRegistryKeys.ACTIVEMQ_HOST, registryParams.getActiveMQHost());
		customParamsMap.put(CustomRegistryKeys.SOLR_BANANA_DASH_COMPLETED_URL, registryParams.getBananaDashboardCompletedUrl());
		customParamsMap.put(CustomRegistryKeys.SOLR_BANANA_DASH_EMPTY_URL, registryParams.getBananaDashboardEmptyUrl());
		//customParamsMap.put(CustomRegistryKeys., registryParams.getCsAmbariViewUrl());
		customParamsMap.put(CustomRegistryKeys.SOLR_DELETE_TRUCK_INDEX_URL, registryParams.getDeleteTruckIndexUrl());
		//customParamsMap.put(CustomRegistryKeys., registryParams.getHueHCatalogUrl());
		customParamsMap.put(CustomRegistryKeys.HUE_SERVER_URL, registryParams.getHueServerUrl());
		customParamsMap.put(CustomRegistryKeys.MAIL_SMTP_HOST, registryParams.getMailSmtpHost());
		customParamsMap.put(CustomRegistryKeys.RANGER_SERVER_URL, registryParams.getRangerServerUrl());
		customParamsMap.put(CustomRegistryKeys.SOLR_SERVER_URL, registryParams.getSolrServerUrl());
		customParamsMap.put(CustomRegistryKeys.SOLR_INDEX_PIG_JOB_HUE_URL, registryParams.getSolrIndexPigJobHueUrl());
		
		
		return customParamsMap;
		
	}

	@RequestMapping(value="install/stormtopology", method=RequestMethod.GET)
	public String truckingTopologyView(Model model) {
		LOG.info("inside render topology view");
		StormTopologyParams topologyParams = new StormTopologyParams();
		topologyParams.setUpload(false);
		topologyParams.setUploadedTopologyJarLocation("/mnt/hadoop/storm/nimbus/staging/stormjar-ac1fe301-3883-41a2-9962-8e258539e50b.jar");
		model.addAttribute("topologyParams", topologyParams);
		model.addAttribute("action", "stormtopology");
		return "install/topology";
	}
	
	@RequestMapping(value="install/stormtopology", method=RequestMethod.POST)
	public String configureAndDeployTruckingTopology(StormTopologyParams topologyParams, HttpSession session) {
		LOG.info("inside configure and deploy trucking topology");
		try {
			this.stormService.deployStormTopology(topologyParams);
		} catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
		return "redirect:/dashboard";
	}
	
	@RequestMapping(value="install/datapipelines", method=RequestMethod.GET)
	public String dataPipelinesView(Model model) {
		LOG.info("inside render data pipelines view");
		FalconDeploymentParams params = new FalconDeploymentParams();
		params.setScheduleIndexingDataPipeline(true);
		params.setSchedulePhoenixUpdateDataPipeline(false);
		model.addAttribute("dataPipelineParams", params);
		model.addAttribute("action", "datapipelines");
		return "install/data-pipelines";
	}
	
	@RequestMapping(value="install/datapipelines", method=RequestMethod.POST)
	public String configureAndDeployDataPipeline(FalconDeploymentParams deploymentParams, HttpSession session) {
		LOG.info("inside request for configureAndDeployDataPipeline ");
		try {
			this.falconService.deployTruckingDataPipelines(deploymentParams);
		} catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
		return "redirect:/dashboard";
	}	
		
	
	private CustomHDPServiceRegistryParams createDefaultRegistryParams() {
		CustomHDPServiceRegistryParams params = new CustomHDPServiceRegistryParams();
		params.setActiveMQConnectionUrl("tcp://george-activemq01.cloud.hortonworks.com:61616");
		params.setActiveMQHost("george-activemq01.cloud.hortonworks.com");
		params.setAmbariUrl("http://centralregion01.cloud.hortonworks.com:8080");
		params.setClusterName("centralregioncluster");
		params.setHbaseDeploymentMode(DeploymentMode.SLIDER);
		params.setStormDeploymentMode(DeploymentMode.SLIDER);
		params.setHbaseSliderPublisherUrl(DEFAULT_HBASE_PUBLISHER_URL);
		params.setHueServerUrl("http://centralregion10.cloud.hortonworks.com:8000");
		params.setStormSliderPublisherUrl(DEFAULT_STORM_PUBLISHER_URL);
		params.setMailSmtpHost("hadoopsummit-stormapp.secloud.hortonworks.com");
		//params.setSolrServerUrl("http://george-search01.cloud.hortonworks.com:8983/solr");
		params.setSolrServerUrl("http://vett-search01.cloud.hortonworks.com:8983/solr");
		params.setActiveMQConsoleUrl("http://george-activemq01.cloud.hortonworks.com:8161/admin/topics.jsp");
		params.setRangerServerUrl("http://george-security01.cloud.hortonworks.com:6080/");
		params.setBananaDashboardCompletedUrl("http://george-search01.cloud.hortonworks.com:8983/banana/#/dashboard/solr/Truck%20Events%20Dashboard%20V4");
		return params;
	}
	
	private HDPServiceRegistry getServiceRegistry(HttpSession session) {
		//HDPServiceRegistry registry  = (HDPServiceRegistry) session.getAttribute("HDP.REGISTRY");
		return registry;
	}	

}
