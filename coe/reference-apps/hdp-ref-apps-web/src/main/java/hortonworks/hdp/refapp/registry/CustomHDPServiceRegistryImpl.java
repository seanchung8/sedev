//package hortonworks.hdp.refapp.registry;
//
//import hortonworks.hdp.apputil.registry.HDPServiceRegistryImpl;
//
//public class CustomHDPServiceRegistryImpl extends HDPServiceRegistryImpl implements CustomHDPServiceRegistry {
//
//	public CustomHDPServiceRegistryImpl(String configFileLocation, String configFileName) {
//		super(configFileLocation, configFileName);
//	}
//
//	
//	public void populateCustom(CustomHDPServiceRegistryParams customParams) throws Exception {
//
//		super.populate(customParams);
//		
//		saveToRegistry(CustomRegistryKeys.ACTIVEMQ_HOST, customParams.getActiveMQHost());
//		saveToRegistry(CustomRegistryKeys.ACTIVEMQ_CONNECTION_URL, customParams.getActiveMQConnectionUrl() + "?wireFormat.maxInactivityDuration=0");
//		
//
//		saveToRegistry(CustomRegistryKeys.SOLR_SERVER_URL, customParams.getSolrServerUrl());
//		saveToRegistry(CustomRegistryKeys.MAIL_SMTP_HOST, customParams.getMailSmtpHost());
//		
//		saveToRegistry(CustomRegistryKeys.HUE_SERVER_URL, customParams.getHueServerUrl());
//		saveToRegistry(CustomRegistryKeys.RANGER_SERVER_URL, customParams.getRangerServerUrl());
//		saveToRegistry(CustomRegistryKeys.ACTIVEMQ_ADMIN_URL, customParams.getActiveMQConsoleUrl());
//		saveToRegistry(CustomRegistryKeys.SOLR_BANANA_DASH_COMPLETED_URL, customParams.getBananaDashboardCompletedUrl());
//		saveToRegistry(CustomRegistryKeys.SOLR_BANANA_DASH_EMPTY_URL, customParams.getBananaDashboardEmptyUrl());
//		saveToRegistry(CustomRegistryKeys.SOLR_DELETE_TRUCK_INDEX_URL, customParams.getDeleteTruckIndexUrl());
//		saveToRegistry(CustomRegistryKeys.SOLR_INDEX_PIG_JOB_HUE_URL, customParams.getSolrIndexPigJobHueUrl());		
//		
//	}
//
//	public String getActiveMQAdminConsoleUrl() {
//		return getValueForKey(CustomRegistryKeys.ACTIVEMQ_ADMIN_URL);
//	}
//	
//	
//	public String getSolrBananaDashboardCompletedUrl() {
//		return getValueForKey(CustomRegistryKeys.SOLR_BANANA_DASH_COMPLETED_URL);
//	}	
//	
//	public String getSolrBananaDashboardEmptyUrl() {
//		return getValueForKey(CustomRegistryKeys.SOLR_BANANA_DASH_EMPTY_URL);
//	}		
//	
//	public String getSolrDeleteTruckIndexUrl() {
//		return getValueForKey(CustomRegistryKeys.SOLR_DELETE_TRUCK_INDEX_URL);
//	}			
//	
//	public String getSolrIndexPigJobHueUrl() {
//		return getValueForKey(CustomRegistryKeys.SOLR_INDEX_PIG_JOB_HUE_URL);
//	}	
//	
//	public String getActiveMQHost() {
//		return getValueForKey(CustomRegistryKeys.ACTIVEMQ_HOST);
//	}
//	
//	public String getActiveMQConnectionUrl() {
//		return getValueForKey(CustomRegistryKeys.ACTIVEMQ_CONNECTION_URL);
//	}	
//	
//	public String getHueServerUrl() {
//		return getValueForKey(CustomRegistryKeys.HUE_SERVER_URL);
//	}	
//	
//	public String getRangerServerUrl() {
//		return getValueForKey(CustomRegistryKeys.RANGER_SERVER_URL);
//	}	
//	
//	public String getSolrServerUrl() {
//		return getValueForKey(CustomRegistryKeys.SOLR_SERVER_URL);
//	}
//	
//	public String getMailSMTPHost() {
//		return getValueForKey(CustomRegistryKeys.MAIL_SMTP_HOST);
//	}			
//	
//	
//}
