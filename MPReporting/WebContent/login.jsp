<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/login-box.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div style="padding: 100px 0 0 250px;">
	<div id="login-box">
		<H2>Login</H2>
		Introduzca sus datos.
		<br />
		<br />
		<div id="login-box-name" style="margin-top:20px;">Usuario:</div>
		<form name="loginForm" action="valida_login.jsp" method ="POST">
		<div id="login-box-field" style="margin-top:20px;">
			<input name="user" class="form-login" title="Username" value="" size="30" maxlength="2048" />
		</div>
		<div id="login-box-name">Password:</div>
		<div id="login-box-field">
			<input name="password" type="password" class="form-login" title="Password" value="" size="30" maxlength="2048" />
		</div>
		</form>
		<br/>
		<br/>
		<br/>
		<br/>
		<a href="javascript: document.loginForm.submit();"><img src="img/login-btn.png" width="103" height="42" style="margin-left:90px;" /></a>
	</div>
</div>
</body>
</html>