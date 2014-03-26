<%@page import="java.lang.reflect.Array"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
ToolsInv inv = new ToolsInv();

String id_customer = "1";//(String) session.getAttribute("id_cust");
String id_modulo = (String) session.getAttribute("mod");
String id_user = (String)session.getAttribute("user");//"1";//(String)session.gerAtributte("id_uder");
String id_dashboard = "1"; //(String)session.getAttribute("id_dashboard");

String id_producto = request.getParameter("id_prod");
String id_portlet = request.getParameter("id_portlet");

String diasP1 = request.getParameter("diasP1");
String diasP2 = request.getParameter("diasP2");
String diasP3 = request.getParameter("diasP3");

String cajasP1 = request.getParameter("cajasP1");
String cajasP2 = request.getParameter("cajasP2");
String cajasP3 = request.getParameter("cajasP3");
System.out.println("usr: "+id_user+" dash: "+id_dashboard+" portlet: "+id_portlet);
if(id_user != null && id_dashboard != null && id_portlet != null){
	System.out.println("usr: "+id_user+" dash: "+id_dashboard+" portlet: "+id_portlet);
	inv.insertaDatosSim(id_customer, id_user, id_modulo, id_dashboard, id_portlet, id_producto, diasP1, diasP2, diasP3, cajasP1, cajasP2, cajasP3);
}


%>
</body>
</html>