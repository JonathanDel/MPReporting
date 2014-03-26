<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<%@ page import="java.util.HashMap" %>
<%@ page import= "java.net.URLEncoder" %>
<%@include file="utils/FusionCharts/Code/JSP/Includes/FusionCharts.jsp" %>
<%
response.setContentType ("application/vnd.ms-excel"); //Tipo de fichero.
response.setHeader ("Content-Disposition", "attachment;filename=\"clientes.xls\"");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>

<%
Tools pptos = new Tools();
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String medida = request.getParameter("medida");
String mesIni = request.getParameter("mesIni");
String mesFin = request.getParameter("mesFin");
%>
<%
String var = ""; 
	var = pptos.getTablePptos("1", id_user,id_modulo, "1", "5", "medida", "2013", "2013",medida, "id", "asc", true,true);
	//String val_encode =  URLEncoder.encode(var, "ISO-8859-1");
%>
<%=var %>

</body>
</html> 