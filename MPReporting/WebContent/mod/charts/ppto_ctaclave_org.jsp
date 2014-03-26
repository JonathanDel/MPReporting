<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="JavaScript" src="utils/FusionWidgets/JSClass/FusionCharts.js"></script>
<style type="text/css">
.cc_chart{
width:600px;
height:60px;
float: center;
}
.link{
width:80px;
height:60px;
float: left;
margin: 0 auto; /* centramos el contenido */
text-align: center;
}

.cc_ttl{
width:60px;
height:60px;
float: left;
vertical-align: middle;
}



</style>
</head>
<body>
<%
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
Tools pptos = new Tools();
boolean sts_fact = pptos.getStatusFact(id_user,id_modulo, "1"); 
boolean sts_po = pptos.getStatusPptoOrg(id_user,id_modulo, "1");
String user = request.getParameter("user");
String medida = request.getParameter("medida");
String mesIni = request.getParameter("mesIni");
String mesFin = request.getParameter("mesFin");
boolean permisos = false; //Permisos para ver todos los ctacve(seperuser) o solo cvecve(usuario normal)
String cc = pptos.getPerCC(id_user);
if(cc == "" || cc == null){
	cc = "0";
}
String indicador = "";

String cta_cve_1 = "";
String[] arrayDatos_1 = {} ;
String lk_cta_cve_1 = "";
String cta_cve_1_1 = "";
String[] arrayDatos_1_1  = {};
String lk_cta_cve_1_1 = "";

String cta_cve_2 = "";
String[] arrayDatos_2 = {};
String lk_cta_cve_2 = "";
String cta_cve_2_1 = "";
String[] arrayDatos_2_1 = {};
String lk_cta_cve_2_1 = "";

String cta_cve_3 = "";
String[] arrayDatos_3 = {};
String lk_cta_cve_3 = "";
String cta_cve_3_1 = "";
String[] arrayDatos_3_1 = {};
String lk_cta_cve_3_1 = "";

String cta_cve_4 = "";
String[] arrayDatos_4 = {};
String lk_cta_cve_4 = "";
String cta_cve_4_1 = "";
String[] arrayDatos_4_1 = {};
String lk_cta_cve_4_1 = "";

String cta_cve_5 = "";
String[] arrayDatos_5 = {};
String lk_cta_cve_5 = "";
String cta_cve_5_1 = "";
String[] arrayDatos_5_1 = {};
String lk_cta_cve_5_1 = "";

String cta_cve_6 = "";
String[] arrayDatos_6 = {};
String lk_cta_cve_6 = "";
String cta_cve_6_1 = "";
String[] arrayDatos_6_1 = {};
String lk_cta_cve_6_1 = "";

String cta_cve_7 = "";
String[] arrayDatos_7 = {};
String lk_cta_cve_7 = "";
String cta_cve_7_1 = "";
String[] arrayDatos_7_1 = {};
String lk_cta_cve_7_1 = "";

String cta_cve_8 = "";
String[] arrayDatos_8 = {};
String lk_cta_cve_8 = "";
String cta_cve_8_1 = "";
String[] arrayDatos_8_1 = {};
String lk_cta_cve_8_1 = "";

String cta_cve_31 = "";
String[] arrayDatos_31 = {};
String lk_cta_cve_31 = "";

String cta_cve_15 = "";
String[] arrayDatos_15 = {};
String lk_cta_cve_15 = "";

String cta_cve_20 = "";
String[] arrayDatos_20 = {};
String lk_cta_cve_20 = "";

String cta_cve_21 = "";
String[] arrayDatos_21 = {};
String lk_cta_cve_21 = "";

String cta_cve_18 = "";
String[] arrayDatos_18 = {};
String lk_cta_cve_18 = "";

String cta_cve_19 = "";
String[] arrayDatos_19 = {};
String lk_cta_cve_19 = "";

String cta_cve_23 = "";
String[] arrayDatos_23 = {};
String lk_cta_cve_23 = "";

String cta_cve_22 = "";
String[] arrayDatos_22 = {};
String lk_cta_cve_22 = "";

String cta_cve_24 = "";
String[] arrayDatos_24 = {};
String lk_cta_cve_24 = "";

String cta_cve_25 = "";
String[] arrayDatos_25 = {};
String lk_cta_cve_25 = "";

String cta_cve_26 = "";
String[] arrayDatos_26 = {};
String lk_cta_cve_26 = "";

String cta_cve_28 = "";
String[] arrayDatos_28 = {};
String lk_cta_cve_28 = "";

String cta_cve_29 = "";
String[] arrayDatos_29 = {};
String lk_cta_cve_29 = "";

String cta_cve_30 = "";
String[] arrayDatos_30 = {};
String lk_cta_cve_30 = "";

String cta_cve_27 = "";
String[] arrayDatos_27 = {};
String lk_cta_cve_27 = "";

String cta_cve_16 = "";
String[] arrayDatos_16 = {};
String lk_cta_cve_16 = "";

if(sts_fact && sts_po){
	//pptos.getChartCtaCve("cust_id", "id_user", "id_dashboard", "chart_id", "id_modulo", "ppto org_2/mod_3", "2011", "2012",medida, "cta_cve");
	if(cc.equals("1") || cc.equals("0")){
		cta_cve_1 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "1");
		arrayDatos_1 = cta_cve_1.split("_");
		lk_cta_cve_1 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "1");
	}

	if(cc.equals("24") || cc.equals("0")){
		cta_cve_24 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "24");
		arrayDatos_24 = cta_cve_24.split("_");
		lk_cta_cve_24 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "24");
	}

	if(cc.equals("25") || cc.equals("0")){
		cta_cve_25 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "25");
		arrayDatos_25 = cta_cve_25.split("_");
		lk_cta_cve_25 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "25");
	}

	if(cc.equals("26") || cc.equals("0")){
		cta_cve_26 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "26");
		arrayDatos_26 = cta_cve_26.split("_");
		lk_cta_cve_26 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "26");
	}

	if(cc.equals("30") || cc.equals("0")){
		cta_cve_30 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "30");
		arrayDatos_30 = cta_cve_30.split("_");
		lk_cta_cve_30 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "30");
	}

	if(cc.equals("27") || cc.equals("0")){
		cta_cve_27 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "27");
		arrayDatos_27 = cta_cve_27.split("_");
		lk_cta_cve_27 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "27");
	}

	if(cc.equals("28") || cc.equals("0")){
		cta_cve_28 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "28");
		arrayDatos_28 = cta_cve_28.split("_");
		lk_cta_cve_28 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "28");
	}

	if(cc.equals("29") || cc.equals("0")){
		cta_cve_29 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "29");
		arrayDatos_29 = cta_cve_29.split("_");
		lk_cta_cve_29 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "29");
	}

	if(cc.equals("31") || cc.equals("0")){
		cta_cve_31 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "31");
		arrayDatos_31 = cta_cve_31.split("_");
		lk_cta_cve_31 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "31");
	}

	if(cc.equals("15") || cc.equals("0")){
		cta_cve_15 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "15");
		arrayDatos_15 = cta_cve_15.split("_");
		lk_cta_cve_15 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "15");
	}

	if(cc.equals("16") || cc.equals("0")){
		cta_cve_16 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "16");
		arrayDatos_16 = cta_cve_16.split("_");
		lk_cta_cve_16 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "16");
	}

	if(cc.equals("20") || cc.equals("0")){
		cta_cve_20 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "20");
		arrayDatos_20 = cta_cve_20.split("_");
		lk_cta_cve_20 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "20");
	}

	if(cc.equals("21") || cc.equals("0")){
		cta_cve_21 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "21");
		arrayDatos_21 = cta_cve_21.split("_");
		lk_cta_cve_21 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "21");
	}

	if(cc.equals("19") || cc.equals("0")){
		cta_cve_19 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "19");
		arrayDatos_19 = cta_cve_19.split("_");
		lk_cta_cve_19 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "19");
	}

	if(cc.equals("18") || cc.equals("0")){
		cta_cve_18 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "18");
		arrayDatos_18 = cta_cve_18.split("_");
		lk_cta_cve_18 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "18");
	}

	if(cc.equals("23") || cc.equals("0")){
		cta_cve_23 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "23");
		arrayDatos_23 = cta_cve_23.split("_");
		lk_cta_cve_23 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "23");
	}

	if(cc.equals("22") || cc.equals("0")){
		cta_cve_22 = pptos.getChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "22");
		arrayDatos_22 = cta_cve_22.split("_");
		lk_cta_cve_22 = pptos.getLinkCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "22");
	}

	String total = pptos.getTotalCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "8");

	String total_cta_cve = pptos.getTotalChartCtaCve("1", id_user, "1", "10", id_modulo, "2", "2014", "2014",medida, "8");
	String[] arrayDatos_T = total_cta_cve.split("_");
	//String[] arrayDatos_T = total_cta_cve.split(",");
	%>
	<!-- Creacion de la tabla que contiene los cuenta claves alineados... -->
	<table>
	  <%if(cc.equals("1") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_1 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc1" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_1[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("24") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_24 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc24" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_24[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("25") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_25 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc25" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_25[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("26") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_26 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc26" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_26[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("30") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_30 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc30" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_30[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("27") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_27 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc27" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_27[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   <tr>
	   
	   <%if(cc.equals("28") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_28 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc28" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_28[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("29") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_29 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc29" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_29[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("31") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_31 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc31" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_31[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("15") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_15 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc15" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_15[1] %></font></strong>
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("16") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_16 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc16" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_16[1] %></font></strong> 
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("20") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_20 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc20" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_20[1] %></font></strong> 
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("21") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_21 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc21" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_21[1] %></font></strong> 
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("18") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_18 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc18" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_18[1] %></font></strong> 
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("19") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_19 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc19" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_19[1] %></font></strong> 
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("23") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_23 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc23" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_23[1] %></font></strong> 
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("22") || cc.equals("0")){ %>
	   <tr>
		   <td valign="middle">
		   	<a href="<%=lk_cta_cve_22 %>" ><img src="../../img/filter.png" width="15" height="15" alt="filtro" /></a>
		   </td>
		   <td>
		   	<div id="cc22" class="cc_chart">FusionGadgets</div>
		   </td>
		   <td valign = "middle">
		   	<strong><font size=2 color=#3399CC><%=arrayDatos_22[1] %></font></strong> 
		   </td>
		</tr>
	   <%} %>
	   
	   <%if(cc.equals("0")){ %>
	   <tr>
	   	<td>
	   	</td>
	   	<td>
	   		<div id="cc" align="center">FusionGadgets</div>
	   	</td>
	   	<td>
	   	</td>
	   	</tr>
	   <%}%>
	  </table>
	  
	   <%if(cc.equals("1") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_1[0]%>");
		myChart.render("cc1");
	   </script>
		<%} %>
		
		<%if(cc.equals("24") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_24[0]%>");
		myChart.render("cc24");
	   </script>
		<%} %>
		
		<%if(cc.equals("25") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_25[0]%>");
		myChart.render("cc25");
	   </script>
		<%} %>
		
		<%if(cc.equals("26") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_26[0]%>");
		myChart.render("cc26");
	   </script>
		<%} %>
		
		<%if(cc.equals("30") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_30[0]%>");
		myChart.render("cc30");
	   </script>
		<%} %>
		
		<%if(cc.equals("27") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_27[0]%>");
		myChart.render("cc27");
	   </script>
		<%} %>
		
		<%if(cc.equals("28") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_28[0]%>");
		myChart.render("cc28");
	   </script>
		<%} %>
		
		<%if(cc.equals("29") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_29[0]%>");
		myChart.render("cc29");
	   </script>
		<%} %>
		
		<%if(cc.equals("31") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_31[0]%>");
		myChart.render("cc31");
	   </script>
		<%} %>
		
		
		<%if(cc.equals("15") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_15[0]%>");
		myChart.render("cc15");
	   </script>
		<%} %>
		
		<%if(cc.equals("16") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_16[0]%>");
		myChart.render("cc16");
	   </script>
		<%} %>
		
		<%if(cc.equals("20") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_20[0]%>");
		myChart.render("cc20");
	   </script>
		<%} %>
		
		<%if(cc.equals("21") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_21[0]%>");
		myChart.render("cc21");
	   </script>
		<%} %>
		
		<%if(cc.equals("18") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_18[0]%>");
		myChart.render("cc18");
	   </script>
		<%} %>
		
		<%if(cc.equals("19") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_19[0]%>");
		myChart.render("cc19");
	   </script>
		<%} %>
		
		<%if(cc.equals("23") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_23[0]%>");
		myChart.render("cc23");
	   </script>
		<%} %>
		
		<%if(cc.equals("22") || cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_22[0]%>");
		myChart.render("cc22");
	   </script>
		<%} %>
		
		
	   <%if(cc.equals("0")){ %>
	   <script type="text/javascript">
		var myChart = new FusionCharts("utils/FusionWidgets/Charts/HBullet.swf", "myChartId", "600", "62", "0", "0");
		myChart.setDataXML("<%=arrayDatos_T[0]%>");
		myChart.render("cc");
	   </script>
	   <%} %>
	   
	   <%if(cc.equals("8") || cc.equals("0")){ %>
	   <div align="right"><strong><%=total %></strong></div>
	   <%} %>
	   <%}else{ %>
	   <center><strong>Mensaje</strong></center>
	   <%} %>
	</body>
</html>