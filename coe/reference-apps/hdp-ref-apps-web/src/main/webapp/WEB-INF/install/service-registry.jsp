
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

<h3 class="box-header bold"> Step 2 - Configure HDP Service Endpoint Registry </h3>


 
<div class="box non-collapsible ">
	<div data-id="r_form">
	

	<form:form action="${action}" method="post" commandName="registryParams" class="form-horizontal">
	
	<fieldset class="configProp">
		<p class="formHeader">
			Discover Endpoints Via Ambari
		</p>
		<div class="" data-fields="ambariUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Ambari Server URL</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="ambariUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>
			
		<div class="" data-fields="clusterName">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Cluster Name</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="clusterName" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>
		
					
		
	</fieldset>
	
	
	<fieldset class="configProp">
		<p class="formHeader">
			Deployment Mode of Apps
		</p>
		
		<div class="" data-fields="hbaseSliderApp">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">HBase  Deployment Option?</label>      
					<div class="controls">       
						<span data-editor="">
							<form:select  path="hbaseDeploymentMode">
								<form:option value="STANDALONE"></form:option>
								<form:option value="SLIDER"></form:option>
							
							</form:select>
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>	
		
		<div class="" data-fields="hbaseSliderPublisherUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Slider HBase Publisher Url</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="hbaseSliderPublisherUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>			
		
		<div class="" data-fields="hbaseSliderPublisherUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Storm Deployment Option?</label>      
					<div class="controls">       
						<span data-editor="">
							<form:select  path="stormDeploymentMode">
								<form:option value="STANDALONE"></form:option>
								<form:option value="SLIDER"></form:option>
							
							</form:select>
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>		
			
		
		<div class="" data-fields="stormSliderPublisherUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Slider Storm Publisher Url</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="stormSliderPublisherUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>			
		
	</fieldset>	
	
	<fieldset class="configProp">
		<p class="formHeader">
			Configure Custom Endpoints
		</p>
		
		<div class="" data-fields="hueServerUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Hue Server Url</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="hueServerUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
		</div>	
		
		<div class="" data-fields="rangerServerUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Ranger Server URL</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="rangerServerUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
		</div>			
					
		<div class="" data-fields="activeMQConnectionUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">ActiveMQ Connection Url</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="activeMQConnectionUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>
		</div>
			
		<div class="" data-fields="activeMQHost">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">ActiveMQ Host</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="activeMQHost" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
		</div>
			
		<div class="" data-fields="activeMQConsoleUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">ActiveMQ Admin Url</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="activeMQConsoleUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
		</div>			
			
		
		
		
		<div class="" data-fields="solrServerUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">SOLR Server URL</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="solrServerUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
			
		<div class="" data-fields="bananaDashboardCompletedUrl">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Search UI - Banana - Completed Dashboard</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="bananaDashboardCompletedUrl" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
		</div>
			
		
						
		
	</fieldset>		
	
	</div>
		
		<div class="form-actions form-asset">
			<button type="submit" data-id="save" class="btn btn-primary">
				Create Endpoint Registry
			</button>
		</div>
	</div> 	
	
</form:form>

 
 
 
 </div>
 </section>
</body>
</html>

	            		

	            		
	            		
	            		
	            		
	           
