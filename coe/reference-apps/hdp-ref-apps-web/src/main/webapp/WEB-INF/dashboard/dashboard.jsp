<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<!doctype html>

<html class="no-js">
	<head>

	</head>
		
<body>
			
<section id="r_content" class="row-fluid" style="min-height: 450px;">

<div>

<h3 class="box-header bold"> HDP Reference Apps - Data Lake Dashboard </h3>

<div class="box non-collapsible ">



	<div class="span4">
		<div class="position-relative">
			<table class="table table-bordered table-striped"><!-- table-policy -->
				<thead>
					<tr>
						<th><img alt="*" src="images/folder2.png"> Operations and Management
							<a href="#!/asset/create/3" class="pull-right" title="Add"><i class="icon-plus"></i></a></th>
						</th>
					</tr>
					
				</thead>
				<tbody>
					<c:if test="${dashboardConfig.ambariServerUrl != null && dashboardConfig.ambariServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.ambariServerUrl}'/>"> Ambari UI</a>
						</div>
						</td>
					</tr>	
					</c:if>
						

					<c:if test="${dashboardConfig.hueServerUrl != null && dashboardConfig.hueServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.hueServerUrl}'/>">HUE UI</a>
							
						</div>
						</td>
					</tr>	
					</c:if>	
					
					<c:if test="${dashboardConfig.solrServerUrl != null && dashboardConfig.solrServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.solrServerUrl}'/>">Solr Admin Console</a>
							
						</div>
						</td>
					</tr>	
					</c:if>		
					
					<c:if test="${dashboardConfig.activeMQConsoleUrl != null && dashboardConfig.activeMQConsoleUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.activeMQConsoleUrl}'/>">ActiveMQ UI</a>
							
						</div>
						</td>
					</tr>	
					</c:if>																																	
					
					
				</tbody>
			</table>
		</div>
	</div>
	
	
	<div class="span4">
		<div class="position-relative">
			<table class="table table-bordered table-striped"><!-- table-policy -->
				<thead>
					<tr>
						<th><img alt="*" src="images/folder2.png"> MultiTenancy/Security
							<a href="#!/asset/create/3" class="pull-right" title="Add"><i class="icon-plus"></i></a></th>
						</th>
					</tr>
					
				</thead>
				<tbody>
				
					<c:if test="${dashboardConfig.rangerServerUrl != null && dashboardConfig.rangerServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.rangerServerUrl}'/>">HDP Security Portal</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					
					<c:if test="${dashboardConfig.csAmbariViewUrl != null && dashboardConfig.csAmbariViewUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.csAmbariViewUrl}'/>">Capacity Scheduler Management-Ambari View</a>
							
						</div>
						</td>
					</tr>
					</c:if>	
					
					<c:if test="${dashboardConfig.csUtilizationUrl != null && dashboardConfig.csUtilizationUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.csUtilizationUrl}'/>">Capacity Scheduler Utilization View</a>
							
						</div>
						</td>
					</tr>
					</c:if>		
					
					<c:if test="${dashboardConfig.resourceManagerUIUrl != null && dashboardConfig.resourceManagerUIUrl != ''}">
					<tr>
						<td>
						<div>
							<p><a href="<c:out value='${dashboardConfig.resourceManagerUIUrl}'/>">Resource Manager UI - Running Apps</a></p>
							
						</div>
						</td>
					</tr>										
					</c:if>
																								
					
					
				</tbody>
			</table>
		</div>
	</div>	
	

	
	<div class="span4">
		<div class="position-relative">
			<table class="table table-bordered table-striped"><!-- table-policy -->
				<thead>
					<tr>
						<th><img alt="*" src="images/folder2.png"> Data Pipelines, Metadata and Lineage
							<a href="#!/asset/create/3" class="pull-right" title="Add"><i class="icon-plus"></i></a></th>
						</th>
					</tr>
					
				</thead>
				<tbody>
				
					<c:if test="${dashboardConfig.falconServerUrl != null && dashboardConfig.falconServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.falconServerUrl}'/>?user.name=hdfs">Falcon Data Pipelines</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<c:if test="${dashboardConfig.hueHCatalogUrl != null && dashboardConfig.hueHCatalogUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.hueHCatalogUrl}'/>">HCatalog Metadata</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<c:if test="${dashboardConfig.falconLineageUrl != null && dashboardConfig.falconLineageUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.falconLineageUrl}'/>&user.name=hdfs">Falcon Lineage View</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<c:if test="${dashboardConfig.falconTitanLineageRestUrl != null && dashboardConfig.falconTitanLineageRestUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.falconTitanLineageRestUrl}'/>&user.name=hdfs">Falcon Titan Lineage Rest API </a>
							
						</div>
						</td>
					</tr>
					</c:if>					
					

				</tbody>
			</table>
		</div>
	</div>	

</div>

<div class="box non-collapsible">

<div class="span4">
		<div class="position-relative">
			<table class="table table-bordered table-striped"><!-- table-policy -->
				<thead>
					<tr>
						<th><img alt="*" src="images/folder2.png"> IOT Trucking App
							<a href="#!/asset/create/3" class="pull-right" title="Add"><i class="icon-plus"></i></a></th>
						</th>
					</tr>
					
				</thead>
				<tbody>
				
					<c:if test="${dashboardConfig.stormUIServerUrl != null && dashboardConfig.stormUIServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.stormUIServerUrl}'/>">Storm UI</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
						
					<!-- Need to change..right now a hack.. -->
					<c:if test="${dashboardConfig.ambariServerUrl != null && dashboardConfig.ambariServerUrl != ''}">		
					<tr>
						<td>
						<div>
							<a href="${contextPath}/storm/truckdemo/reset">Reset Storm Demo</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<!-- Need to change..right now a hack.. -->
					<c:if test="${dashboardConfig.ambariServerUrl != null && dashboardConfig.ambariServerUrl != ''}">				
					<tr>
						<td>
						<div>
							<a href="${contextPath}/storm/truckdemo/streaming">Truck Events Stream Generator</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<!-- Need to change..right now a hack.. -->
					<c:if test="${dashboardConfig.ambariServerUrl != null && dashboardConfig.ambariServerUrl != ''}">	
					<tr>
						<td>
						<div>
							<a href="${contextPath}/storm/truckdemo/truckmonitor"><strong>Real-Time</strong> Driver Monitoring Application</a>
							
						</div>
						</td>
					</tr>
					</c:if>
					
					<c:if test="${dashboardConfig.bananaDashboardCompletedUrl != null && dashboardConfig.bananaDashboardCompletedUrl != ''}">		
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.bananaDashboardCompletedUrl}'/>">Search UI - Banana - Completed Dashboard</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<c:if test="${dashboardConfig.bananaDashboardEmptyUrl != null && dashboardConfig.bananaDashboardEmptyUrl != ''}">		
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.bananaDashboardEmptyUrl}'/>">Search UI - Banana - Empty Dashboard</a>
							
						</div>
						</td>
					</tr>	
					</c:if>					
					
					<c:if test="${dashboardConfig.deleteTruckIndexUrl != null && dashboardConfig.deleteTruckIndexUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.deleteTruckIndexUrl}'/>">Delete Truck Index</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<c:if test="${dashboardConfig.solrIndexPigJobHueUrl != null && dashboardConfig.solrIndexPigJobHueUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="<c:out value='${dashboardConfig.solrIndexPigJobHueUrl}'/>">Solr Index Pig Job</a>
							
						</div>
						</td>
					</tr>	
					</c:if>												
					

					
														

				</tbody>
			</table>
		</div>
	</div>		
	
	
	
	<div class="span4">
		<div class="position-relative">
			<table class="table table-bordered table-striped"><!-- table-policy -->
				<thead>
					<tr>
						<th><img alt="*" src="images/folder2.png"> Data Science Analytics App
							<a href="#!/asset/create/3" class="pull-right" title="Add"><i class="icon-plus"></i></a></th>
						</th>
					</tr>
					
					
				</thead>
				<tbody>
				
					<c:if test="${dashboardConfig.stormUIServerUrl != null && dashboardConfig.stormUIServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="http://vett-datascience01.cloud.hortonworks.com:9990">IPython Notebook For Spark Examples</a>
							
						</div>
						</td>
					</tr>	
					</c:if>

					<c:if test="${dashboardConfig.stormUIServerUrl != null && dashboardConfig.stormUIServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="http://vett-cluster02.cloud.hortonworks.com:8088/proxy/application_1413286201911_0022/">Spark UI</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<c:if test="${dashboardConfig.stormUIServerUrl != null && dashboardConfig.stormUIServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="http://vett-datascience01.cloud.hortonworks.com:9991/notebooks/Airline%20Analysis%20%28Language%3DPython%2C%20ML%20Library%3DScikit-learn%29.ipynb">Airline Analysis (Engine=Python, Language=Python, ML Library=Scikit-learn)</a>
							
						</div>
						</td>
					</tr>	
					</c:if>
					
					<c:if test="${dashboardConfig.stormUIServerUrl != null && dashboardConfig.stormUIServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="http://vett-datascience01.cloud.hortonworks.com:9990/notebooks/Airline%20Analysis%20%28Engine%3DSpark%2C%20Language%3DScala%2C%20ML%20Library%3DSparkML%29.ipynb
">Airline Analysis (Engine=Spark, Language=Scala, ML Library=SparkML)</a>
							
						</div>
						</td>
					</tr>	
					</c:if>																			
					
					 	

				</tbody>
			</table>
		</div>
	</div>	
	
	<div class="span4">
		<div class="position-relative">
			<table class="table table-bordered table-striped"><!-- table-policy -->
				<thead>
					
					<tr>
						<th><img alt="*" src="images/folder2.png"> Document Store / Content Management App
							<a href="#!/asset/create/3" class="pull-right" title="Add"><i class="icon-plus"></i></a></th>
						</th>
					</tr>
					
					
				</thead>
				<tbody>
				
					<c:if test="${dashboardConfig.solrServerUrl != null && dashboardConfig.solrServerUrl != ''}">
					<tr>
						<td>
						<div>
							<a href="${contextPath}/ecm/index.html">Document Store App</a>
							
							
						</div>
						</td>
					</tr>	
					</c:if>				

					
														

				</tbody>
			</table>
		</div>
	</div>				

			
</div>




	

<!-- Break -->


</div>

</section>


</body>
</html>