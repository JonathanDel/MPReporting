<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<%@ page import="java.util.HashMap" %>
<%@ page import= "java.net.URLEncoder" %>
<%@include file="utils/FusionCharts/Code/JSP/Includes/FusionCharts.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery-latest.js"></script>
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery.tablesorter-update.js"></script>
<script type="text/javascript" src="../../js/funciones.js"></script>
<script type="text/javascript">
$(document).ready(function()
		{
			$("#tblDatos").tablesorter();
		}
	);
function exportTableHTMLtoExcel(file_name){
	
	   var tbl = $("#tblDatos").eq(0).clone().html();
	   alert(tbl);
	   var table = encodeURIComponent(tbl);
	   alert(table);
	   var path = "export_excel.jsp?file_name="+file_name+"&table="+table;
	   window.open(path , "ventana1" , "width=100,height=50,scrollbars=YES");
}
</script>
<link rel="stylesheet" type="text/css" href="../../js/jquery.tablesorter/themes/blue/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css" />
<script>
   var StyleFile = "theme" + document.cookie.charAt(6) + ".css";
   document.writeln('<link rel="stylesheet" type="text/css" href="css_charts/theme1.css">');

   /*Funcion que abre pupup */
   function abrePop(path){
 	window.open(path , "ventana1" , "width=950,height=520,scrollbars=YES"); 
 	}
   
   
</script>
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
	var = pptos.getTablePptos("1", id_user,id_modulo, "1", "6", "medida", "2014", "2014",medida, "id", "asc", true, true);
	String val_encode =  URLEncoder.encode(var, "ISO-8859-1");
%>
<%=var %>
<form action="excel.jsp" method="POST" onsubmit="return cadenaTabla()">
	<input type="hidden" name="tabla" value="" id="tabla" /> 
	<input type="image" src="../../img/xls.jpg" width="25" height="25" />
</form>
</body>
</html> 