<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>HDP Data Lake</title>
		<meta name="description" content="">
		<meta name="viewport" content="width=device-width">

		<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
		<link rel="shortcut icon" href="${contextPath}/assets/horton/images/favicon.ico">
		<link rel="stylesheet" href="${contextPath}/assets/horton/styles/adminflare-font.css">

		<link href="${contextPath}/assets/horton/styles/1.3.0/green/bootstrap.css" media="all" rel="stylesheet" type="text/css" id="bootstrap-css">
		<link href="${contextPath}/assets/horton/styles/1.3.0/green/adminflare.min.css" media="all" rel="stylesheet" type="text/css" id="adminflare-css">

		<link href="${contextPath}/assets/horton/libs/other/backgrid/backgrid.css" media="all" rel="stylesheet" type="text/css" >
		<link href="${contextPath}/assets/horton/libs/bower/backgrid-paginator/css/backgrid-paginator.css" media="all" rel="stylesheet" type="text/css" >
		<link href="${contextPath}/assets/horton/libs/bower/backgrid-filter/css/backgrid-filter.css" media="all" rel="stylesheet" type="text/css" >
		<link href="${contextPath}/assets/horton/libs/bower/select2/select2.css" media="all" rel="stylesheet" type="text/css" >
		<link href="${contextPath}/assets/horton/libs/bower/pines-notify/css/jquery.pnotify.default.css" rel="stylesheet">
		<!--link href="libs/bower/Tag-Handler/css/jquery.taghandler.css" rel="stylesheet"-->
		<link href="${contextPath}/assets/horton/libs/bower/tag-it/css/jquery.tagit.css" rel="stylesheet">
		<link href="${contextPath}/assets/horton/libs/other/datepicker/css/datepicker.css" media="all" rel="stylesheet" type="text/css" >
		<!--
		<script src="themejs/1.3.0/adminflare.min.js" type="text/javascript"></script>
		<script src="themejs/1.3.0/bootstrap.min.js" type="text/javascript"></script> -->
		<link href="${contextPath}/assets/horton/libs/other/jquery-ui/css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
		<link href="${contextPath}/assets/horton/libs/other/visualsearch/css/reset.css" rel="stylesheet">
		<link href="${contextPath}/assets/horton/libs/other/visualsearch/css/workspace.css" rel="stylesheet">
		<link href="${contextPath}/assets/horton/libs/other/visualsearch/css/icons.css" rel="stylesheet">   
		<link href="${contextPath}/assets/horton/styles/default.css" media="all" rel="stylesheet" type="text/css" >
		<!--link href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" media="all" rel="stylesheet" type="text/css" -->
		<decorator:head/>
	</head>
	
	<body class="fluid-layout"/>
		<!-- Main navigation bar
		================================================== -->
		<header class="navbar navbar-fixed-top" id="main-navbar">
			<div class="navbar-inner">
				<div class="container">
					<a class="logo page-logo" href="#"><img alt="Af_logo" src="${contextPath}/assets/horton/images/hortonworks_logo_w.png"></a>
					<a class="btn nav-button collapsed" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-reorder"></span> </a>
					<div id="r_topNav">

					</div>
					<div id="r_topProfileBar">

					<div>
						<div class="nav-collapse collapse">
							<ul class="nav pull-right">
								<li class="dropdown">
									<a href="${contextPath}/logout" class="dropdown-toggle usermenu _allowNav" data-toggle="dropdown">
									 <img alt="Avatar" src="${contextPath}/assets/horton/images/avatar.png"> 
										<span>Logout</span> </a>
				

								</li>
							</ul>
							<ul class="nav pull-right">
								<li class="dropdown">
									<a href="${contextPath}/dashboard" class="dropdown-toggle usermenu _allowNav" data-toggle="dropdown">
									 <img alt="Avatar" src="${contextPath}/assets/horton/images/avatar.png"> 
										<span>Dashboard</span> </a>
				

								</li>
							</ul>
							<ul class="nav pull-right">
								<li class="dropdown">
									<a href="${contextPath}/install/step1" class="dropdown-toggle usermenu _allowNav" data-toggle="dropdown">
									 <img alt="Avatar" src="${contextPath}/assets/horton/images/avatar.png"> 
										<span>Ref Apps Install</span> </a>
				

								</li>
							</ul>							
							

						</div>
					</div>




					</div>
				</div>
			</div>
		</header>
		<!-- / Main navigation bar -->
		
		<!-- Page content
		================================================== -->


		<section class="container">	
	
			<section class="container">
				<decorator:body/>
	
			</section>	
		</section>
	
	</body>
</html>