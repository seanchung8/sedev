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

<h3 class="box-header bold"> Step 1 - Select a Reference Application </h3>


 
<div class="box non-collapsible ">
	<div data-id="r_form">
	

	<form:form action="${action}" method="post" commandName="assembliesParam" class="form-horizontal">
	
	<fieldset class="configProp">
		<p class="formHeader">
			Reference Applications
		</p>
		<div class="" data-fields="assembly">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Select an Ref App</label>      
					<div class="controls">       
						<span data-editor="">
							<form:select path="assembly">
	            				<form:option value="-" label="None"/>
	            				<form:option value="streaming-sensor" label="Trucking Streaming Sensor App"/>
	            				<form:option value="ecm" label="Enterprise Content Management App"/>
	            			</form:select>
							
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>
		</div>
		
	</fieldset>
	
	</div>
		
		<div class="form-actions form-asset">
			<button type="submit" data-id="save" class="btn btn-primary">
				Save
			</button>
		</div>
	</div> 	
	
</form:form>

 
 
 
 </div>
 </section>
</body>
</html>


		 