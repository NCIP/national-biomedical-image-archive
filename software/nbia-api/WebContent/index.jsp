<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
  <title>NBIA API</title>
  <link type="text/css" rel="stylesheet" href="<c:url value="/style.css"/>"/>

  <authz:authorize ifAllGranted="ROLE_USER">
  <h1>Authentication Successful!</h1>

  </authz:authorize>
</head>
<body>

  <h1>NBIA API</h1>

  <div id="content">
    <h2>Home</h2>

    <p>If you have a account with NBIA to access private collections, go ahead and log in. Otherwise, you can use public user "nbia_guest" to access public data.  nbia_guest's password is "changeme".</p>

    <authz:authorize ifNotGranted="ROLE_USER">
      <h2>Login</h2>
      <form action="<c:url value="/login.do"/>" method="post">
        <p><label>Username: <input type='text' name='j_username' value="nbia_guest"></label></p>
        <p><label>Password: <input type='text' name='j_password' value="changeme"></label></p>
        
        <p><input name="login" value="Login" type="submit"></p>
      </form>
    </authz:authorize>
    <authz:authorize ifAllGranted="ROLE_USER">
      <!--div style="text-align: center"><form action="<c:url value="/logout.do"/>"><input type="submit" value="Logout"></form></div>
      
      <h2>Your Photos</h2>

      <p>
        <script type='text/javascript' src='photos?callback=pictureDisplay&format=json'></script>
      </p-->
    </authz:authorize>
  </div>
</body>
</html>
