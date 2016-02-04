<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<!doctype html>

<html class="no-js">
	<head>
    <meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate, max-age=0">
    <link href="${contextPath}/assets/lib/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${contextPath}/assets/common/stormdemo.css" rel="stylesheet">
	 <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.2/leaflet.css" />
	</head>
		
<body>
			
<section id="r_content" class="row-fluid" style="min-height: 450px;">

<div>

<h3 class="box-header bold"> Real-Time Truck Driver Monitoring Application </h3>


 
<div class="box non-collapsible ">

 <div id="map"> </div>


 
  <table class="table table-striped">
          <thead>
            <tr>
              <th>Driver Name</th>
               <th>Route Name</th>
              <th>TruckId</th>               
              <th>DriverId</th>
			  <th>RouteId</th>
              <th class="number">Timestamp</th>
              <th class="number">Longitude</th>
              <th class="number">Latitude</th>
              <th class="number">Last Infraction</th>
              <th>Total Infractions</th>
              <th></th>
            </tr>
          </thead>
          <tbody data-bind="foreach: driverMontior().rows">
            <tr data-bind="css: rowClass">
              <td data-bind="text: driverName"></td>
               <td data-bind="text: routeName"></td>
               <td data-bind="text: truckId"></td>
              <td data-bind="text: driverId"></td>
              <td data-bind="text: routeId"></td>
              <td data-bind="text: timeStampString"></td>
              <td data-bind="text: longitude"></td>
              <td data-bind="text: latitude"></td>
              <td data-bind="text: infractionEvent"></td>
              <td data-bind="text: numberOfInfractions" class="number"></td>
             </tr>
          </tbody>
      
          <tbody></tbody>
        </table>
        <div class="alert alert-warning">
          <h5>Notifications</h5>
          <ul data-bind="foreach: notifications">
            <li data-bind="text: notification"></li>
          </ul>
        </div>

    <!-- 3rd party -->
    <script src="${contextPath}/assets/lib/jquery/jquery.js"></script>
 <!--    <script src="${contextPath}/assets/lib/bootstrap/js/bootstrap.js"></script>  -->
    <script src="${contextPath}/assets/lib/knockout/knockout.js"></script>
    <script src="${contextPath}/assets/lib/sockjs/sockjs.js"></script>
    <script src="${contextPath}/assets/lib/stomp/dist/stomp.js"></script>
    <script src="http://cdn.leafletjs.com/leaflet-0.7.2/leaflet.js"></script>

    <!-- application -->
    <script src="${contextPath}/assets/js/monitor/truck.js"></script>
    <script type="text/javascript">
      


      (function() {

        
        $.getJSON("<c:url value="/storm/truckdemo/driverEvents.json"/>",
      		  function(data){
      		    console.log(data.driverEventsResponse);
      		    
      	        var socket = new SockJS('${contextPath}/monitor');
      	        console.log("about to create stomp client");
      	        var stompClient = Stomp.over(socket);
      	        console.log("created stomp client");
      	       
      	      
		        var appModel = new ApplicationModel(stompClient, data.driverEventsResponse, L);
		        console.log("model created");
		        ko.applyBindings(appModel);
		        console.log("applyBindings done");
		
		        appModel.connect();
		        console.log("connected........");
		        appModel.pushNotification("No Alerts Yet....");             
      		  });        
        

      })();    

    
    </script>


</div>

</div>


 </section>
</body>
</html>


		 


