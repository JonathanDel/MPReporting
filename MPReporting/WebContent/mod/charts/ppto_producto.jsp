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
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery-latest.js"></script>
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(document).ready(function()
		{
			$("#tblDatos").tablesorter({ 
		        headers: { 2: { sorter:'digit' } , 
	                   3: { sorter:'digit' } ,
	                   4: { sorter:'digit' }
	                 } 
	    });
		}
	);
</script>
<link rel="stylesheet" type="text/css" href="../../js/jquery.tablesorter/themes/blue/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css" />
<script>
   var StyleFile = "theme" + document.cookie.charAt(6) + ".css";
   document.writeln('<link rel="stylesheet" type="text/css" href="css_charts/theme1.css">');

function abrePop(path, width, height){
	window.open(path, "ventana1" , "width=950,height=520,scrollbars=NO") 
}
</script>
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
	var = pptos.getTablePptos("1",id_user, id_modulo, "1", "9", "medida", "2014", "2014",medida, "id", "asc", true, true);
%>
	<%=var %>
</body>
</html>