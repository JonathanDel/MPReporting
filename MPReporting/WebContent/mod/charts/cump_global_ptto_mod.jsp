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
 String user = (String) session.getAttribute("user");
 String id_modulo = (String) session.getAttribute("mod");
 String medida = request.getParameter("medida");
 String mesIni = request.getParameter("mesIni");
 String mesFin = request.getParameter("mesFin");
%>
<%
String indicador = "";
String var = "";
//if(tipoInd.equals("G")){
	// getChartPpts("cust_id", "id_user", "id_dash", "id_modulo", "id_portlet", "medida", "2011", "2012", "medida", "asc","mesIni","mesF");
	var = pptos.getChartCumpGlobal("1", user, id_modulo, "1", "1", medida, "2013","2013", "1", "3");
	String[] arrayDatos = var.split("_");
	//indicador = createChartHTML("utils/FusionWidgets/Charts/Cylinder.swf", "", var, "FactorySum", 320, 200, false); 
//}
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