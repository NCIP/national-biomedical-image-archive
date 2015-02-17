<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<authz:authorize ifAllGranted="ROLE_USER">
  <c:redirect url="index.jsp"/>
</authz:authorize>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
  <title>NBIA API</title>
  <link type="text/css" rel="stylesheet" href="<c:url value="/style.css"/>"/>
</head>

<body>

  <h1>NBIA API</h1>

  <div id="content">
    <c:if test="${!empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
      <div class="error">
		<h2>Woops!</h2>

      	<p>Your login attempt was not successful. (<%= ((AuthenticationException) session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>)</p>
      </div>
    </c:if>
    <c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>

    <authz:authorize ifNotGranted="ROLE_USER">
      <h2>Login</h2>

      <p>If you have a account with NBIA to access private collections, go ahead and log in. Otherwise, you can use public user "nbia_guest" to access public data.  nbia_guest's password is "changeme".</p>
      <form action="<c:url value="/login.do"/>" method="post">
        <p><label>Username: <input type='text' name='j_username' value="nbia_guest"></label></p>
        <p><label>Password: <input type='text' name='j_password' value="changeme"></label></p>
        
        <p><input name="login" value="Login" type="submit"></p>
      </form>
    </authz:authorize>
  </div>
</body>
</html>
