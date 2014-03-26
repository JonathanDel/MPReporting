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
<script language="JavaScript" src="utils/FusionWidgets/JSClass/FusionCharts.js"></script>
</head>
<body>
 <%
Tools pptos = new Tools();
 String user = (String)session.getAttribute("user");
 String id_modulo = (String) session.getAttribute("mod");
 String medida = request.getParameter("medida");
 String mesIni = request.getParameter("mesIni");
 String mesFin = request.getParameter("mesFin");
%>
<%
String indicador = "";
String var = "";
	var = pptos.getChartCumpGlobal("1", user, id_modulo, "1", "1", medida, "2014","2014", "1", "2");
	String[] arrayDatos = var.split("_");
%>
 <div id="cc1" align="center">FusionGadgets</div>
   
   
   <script type="text/javascript">
	var myChart = new FusionCharts("utils/FusionWidgets/Charts/VBullet.swf", "myChartId", "100", "300", "0", "0");
	myChart.setDataXML("<%=arrayDatos[0]%>");
	myChart.render("cc1");
   </script>
  <strong><font color=#3399CC>Porcentaje: <%=arrayDatos[1] %> %</font></strong>
</body>
</html>