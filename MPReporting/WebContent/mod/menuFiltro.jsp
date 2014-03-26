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
String module = (String) session.getAttribute("mod");
String id_user = (String)session.getAttribute("user");//"1";//(String)session.gerAtributte("id_uder");
String id_dashboard = "1"; //(String)session.getAttribute("id_dashboard");
String id_portlet = request.getParameter("id_portlet");
String elementos = request.getParameter("elementos");
String cmp_id = request.getParameter("cmp_id");

//String id_modulo = (String) session.getAttribute("id_modulo");
System.out.println("CmpId -- "+cmp_id);
if(id_user != null && id_dashboard != null && id_portlet != null){
	inv.insertMenuFiltro(id_customer, id_user, module, id_dashboard, id_portlet, elementos, cmp_id); 
}


%>
</body>
</html>