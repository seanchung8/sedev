package hortonworks.hdp.refapp.registry;

import hortonworks.hdp.apputil.registry.ServiceRegistryParams;

public class CustomHDPServiceRegistryParams extends ServiceRegistryParams {

	private String activeMQHost;
	private String activeMQConnectionUrl;
	private String activeMQConsoleUrl;
	
	private String mailSmtpHost;
	
	private String hueServerUrl;
	private String rangerServerUrl;
	private String csAmbariViewUrl;
	
	private String hueHCatalogUrl;
	
	
	private String solrServerUrl;
	private String bananaDashboardCompletedUrl;
	private String bananaDashboardEmptyUrl;
	private String deleteTruckIndexUrl;
	private String solrIndexPigJobHueUrl;
	
	public String getActiveMQHost() {
		return activeMQHost;
	}
	public void setActiveMQHost(String activeMQHost) {
		this.activeMQHost = activeMQHost;
	}
	public String getActiveMQConnectionUrl() {
		return activeMQConnectionUrl;
	}
	public void setActiveMQConnectionUrl(String activeMQConnectionUrl) {
		this.activeMQConnectionUrl = activeMQConnectionUrl;
	}
	public String getSolrServerUrl() {
		return solrServerUrl;
	}
	public void setSolrServerUrl(String solrServerUrl) {
		this.solrServerUrl = solrServerUrl;
	}
	public String getMailSmtpHost() {
		return mailSmtpHost;
	}
	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}
	public String getHueServerUrl() {
		return hueServerUrl;
	}
	public void setHueServerUrl(String hueServerUrl) {
		this.hueServerUrl = hueServerUrl;
	}
	public String getRangerServerUrl() {
		return rangerServerUrl;
	}
	public void setRangerServerUrl(String rangerServerUrl) {
		this.rangerServerUrl = rangerServerUrl;
	}
	public String getCsAmbariViewUrl() {
		return csAmbariViewUrl;
	}
	public void setCsAmbariViewUrl(String csAmbariViewUrl) {
		this.csAmbariViewUrl = csAmbariViewUrl;
	}
	public String getHueHCatalogUrl() {
		return hueHCatalogUrl;
	}
	public void setHueHCatalogUrl(String hueHCatalogUrl) {
		this.hueHCatalogUrl = hueHCatalogUrl;
	}

	public String getActiveMQConsoleUrl() {
		return activeMQConsoleUrl;
	}
	public void setActiveMQConsoleUrl(String activeMQConsoleUrl) {
		this.activeMQConsoleUrl = activeMQConsoleUrl;
	}
	public String getBananaDashboardCompletedUrl() {
		return bananaDashboardCompletedUrl;
	}
	public void setBananaDashboardCompletedUrl(String bananaDashboardCompletedUrl) {
		this.bananaDashboardCompletedUrl = bananaDashboardCompletedUrl;
	}
	public String getBananaDashboardEmptyUrl() {
		return bananaDashboardEmptyUrl;
	}
	public void setBananaDashboardEmptyUrl(String bananaDashboardEmptyUrl) {
		this.bananaDashboardEmptyUrl = bananaDashboardEmptyUrl;
	}
	public String getDeleteTruckIndexUrl() {
		return deleteTruckIndexUrl;
	}
	public void setDeleteTruckIndexUrl(String deleteTruckIndexUrl) {
		this.deleteTruckIndexUrl = deleteTruckIndexUrl;
	}
	public String getSolrIndexPigJobHueUrl() {
		return solrIndexPigJobHueUrl;
	}
	public void setSolrIndexPigJobHueUrl(String solrIndexPigJobHueUrl) {
		this.solrIndexPigJobHueUrl = solrIndexPigJobHueUrl;
	}
	
	
	
}
