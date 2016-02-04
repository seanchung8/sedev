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

<h3 class="box-header bold"> Reset Storm Demo </h3>


 
<div class="box non-collapsible ">
	<div data-id="r_form">
	

	<form:form action="reset" method="post" commandName="demoParams" class="form-horizontal">
	
	
	<fieldset class="configProp">
		<p class="formHeader">
			HBase/Phoenix Reset
		</p>
		<div class="" data-fields="truncateHbaseTables">
			<div class="control-group field-name">      
				<label class="control-label" for="c21_name">Truncate HBase/Phoenix Table</label>      
					<div class="controls">       
						<span data-editor="">
							<form:checkbox path="truncateHbaseTables" maxlength="100" size="100" />
							
						</span>        
						<div class="help-inline" data-error=""></div>   
					</div>    
			</div>
		</div>
		
	</fieldset>
	
	</div>
		
		<div class="form-actions form-asset">
			<button type="submit" data-id="save" class="btn btn-primary">
				Reset Storm Demo
			</button>
		</div>
	</div> 	
	
</form:form>

 
 
 
 </div>
 </section>
</body>
</html>


		 




 