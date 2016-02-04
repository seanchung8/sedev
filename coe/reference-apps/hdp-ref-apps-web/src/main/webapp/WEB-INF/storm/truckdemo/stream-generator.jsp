
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

<h3 class="box-header bold"> Truck Sensor Data Stream Generaotr  </h3>


 
<div class="box non-collapsible ">
	<div data-id="r_form">
	

	<form:form action="streaming" method="post" commandName="streamConfig" class="form-horizontal">

	</fieldset>
	
	<fieldset class="configProp">
		<p class="formHeader">
			Stream Configuration
		</p>
		<div class="" data-fields="eventEmitterClassName">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Event Emitter Class</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="eventEmitterClassName" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>
			
		<div class="" data-fields="eventCollectorClassName">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Event Collector Class</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="eventCollectorClassName" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>
		
		<div class="" data-fields="numberOfEvents">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Number of Events Per Truck</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="numberOfEvents" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>
		
		<div class="" data-fields="delayBetweenEvents">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Delay Between Events (ms)</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="delayBetweenEvents" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>
		
		<div class="" data-fields="routeDirectory">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Route Directory</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="routeDirectory" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>
		
		<div class="" data-fields="centerCoordinatesLat">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Center LAT Coordinate</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="centerCoordinatesLat" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
				
		</div>
		
		<div class="" data-fields="centerCoordinatesLong">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Center LONG Coordinate</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="centerCoordinatesLong" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
			
		<div class="" data-fields="zoomLevel">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Map Zoom Level</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="zoomLevel" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>	
			
		<div class="" data-fields="truckSymbolSize">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Truck Symbol Size</label>      
					<div class="controls">       
						<span data-editor="">
							<form:input path="truckSymbolSize" maxlength="100" size="100" />
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>				
		
						
		
	</fieldset>		
	
	</div>
		
		<div class="form-actions form-asset">
			<button type="submit" data-id="save" class="btn btn-primary">
				Generate Truck Streams
			</button>
		</div>
	</div> 	
	
</form:form>

 
 
 
 </div>
 </section>
</body>
</html>

	            		
