<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>HDP Ref Arch Login Page</title>

 		 <link type="text/css" href="${contextPath}/assets/lib/bootstrap/css/bootstrap.css" rel="stylesheet">
  		<link  type="text/css" href="${contextPath}/assets/common/login.css" rel="stylesheet">		
       	
    </head>


<body>

  <div >

    <form:form class="form-signin" method="post" action="login">
      
      <input type="text" class="input-block-level" placeholder="User name" name="username" value="northcentral">
      <input type="password" class="input-block-level" placeholder="Password" name="password" value="northcentral">
      <button class="btn btn-primary" type="submit">Sign in</button>
      <hr>
      <p class="text-info"><small>
        Log in with <strong>regional demo credentials</strong> 

    </form:form>
  </div>



</body>
</html>
