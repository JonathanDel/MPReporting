<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
Tools pptos = new Tools();
String user = request.getParameter("user");
String password = request.getParameter("password");
String login = "";
boolean exist_ctacve = false;
if(user != null && user != "" && password != null && password != ""){
	login = pptos.getLogin(user, password);
	if(login != null && login != ""){
		session.setAttribute("user", login);
		//Checa si el usuario ya existe
		exist_ctacve = pptos.getExistCtaCve("1",login,"2","1");
		if(!exist_ctacve){
			//insertar filtro dependiendo del cuenta clave si eske no existe ya...
			pptos.insertaFiltroPorUsuario("1",login,"2","1");
		}
		response.sendRedirect("index.jsp?mod=pptos&idmod=2");
	}else{
		response.sendRedirect("login.jsp?error=s");
	}
}else{
	response.sendRedirect("login.jsp?error=s");
}
%>
</body>
</html>