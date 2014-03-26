<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.auribox.reporting.tools.Tools" %>
<%@ page import="java.util.HashMap" %>
<%@include file="utils/FusionCharts/Code/JSP/Includes/FusionCharts.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="JavaScript" src="utils/FusionCharts/JSClass/FusionCharts.js"></script>
</head>
<body>
<%
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
Tools pptos = new Tools();
String user = request.getParameter("user");
String medida = request.getParameter("medida");
String mesIni = request.getParameter("mesIni");
String mesFin = request.getParameter("mesFin");
%>
<%
String indicador = "";
String var = "";
	var = pptos.getChartPpts("1", id_user, "1", "3", "medida", "2014", "2014",medida, "id", "asc",id_modulo, true);
	indicador = createChartHTML("utils/FusionCharts/Charts/MSColumnLine3D.swf", "", var, "FactorySum", 320, 200, false);
%>
<div id="portlet" align="center" style="height: 200px; width: 320;">
	<%=indicador %>
</div>
</body>
</html>