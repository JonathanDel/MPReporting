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
<!--  <script language="JavaScript" src="utils/FusionCharts/JSClass/FusionCharts.js"></script> -->
<script language="JavaScript" src="../../js/jquery-ui/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){  
	var show = true;  
    $("#ok").click(function() {  
        if($("#show_num").is(':checked')) {  
            //alert("Está activado");
            shows = true;
        } else {  
            //alert("No está activado");
            shows = false;
        }  
    });  
  
});  
</script>
</head>

<body>
<%
Tools pptos = new Tools();
//String user = request.getParameter("id_user");//(String)session.getAttribute("user");
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String medida = request.getParameter("medida");
String mesIni = request.getParameter("mesIni");
String mesFin = request.getParameter("mesFin");
//if(medida != null){
	//pptos.insertaGpoMedida("1", "2", medida);
//}
%>
<%
String indicador = "";
String var = "";
	// getChartPpts("cust_id", "id_user", "id_dash", "id_portlet", "ord_medida", "2011", "2012", "medida", "asc","*","modulo"); 
	var = pptos.getChartPpts("1", id_user, "1", "1", "medida", "2014", "2014",medida, "id", "asc",id_modulo, true);
	indicador = createChartHTML("utils/FusionCharts/Charts/MSColumnLine3D.swf", "", var, "FactorySum", 700, 200, false);
%>

<div id="portlet" align="center" style="height: 300px; width: 850;">
	<%=indicador %>
</div>
</body>
</html>