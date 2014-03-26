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
			$("table").tablesorter({ 
		        headers: { 2: { sorter:'digit' } , 
	                   3: { sorter:'digit' } ,
	                   4: { sorter:'digit' }
	                 } 
	    });
		}
	);
</script>
<link rel="stylesheet" type="text/css" href="../../js/jquery.tablesorter/themes/blue/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css">
<script>
var StyleFile = "theme" + document.cookie.charAt(6) + ".css";
   document.writeln('<link rel="stylesheet" type="text/css" href="css_charts/theme1.css">');

</script>
</head>
<body>
<%
Tools pptos = new Tools();
//String id_user = request.getParameter("id_user");
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String id_customer = "1";
String id_dashboard = request.getParameter("id_dashboard");
//String id_modulo = request.getParameter("id_modulo");
String id_chart = request.getParameter("id_chart");
String parametro = request.getParameter("parametro");
String val_param = request.getParameter("val_param");
String nom_org = request.getParameter("nom_org");
String val_org = request.getParameter("val_org");
String del_fil = request.getParameter("delFil");

if(del_fil.equals("true")){
	// Elimina filtros antes de iniciar..
	pptos.eliminaFiltroPopup(id_customer, id_user, id_dashboard, id_modulo);
}

if(val_param != null && val_param != "" && parametro != null && parametro != ""){
	pptos.insertaFiltroPopup(id_customer,id_user, id_dashboard, id_modulo, id_chart, parametro, val_param, nom_org); 
	
}

%>

<%
String clientes = "";
String productos = "";
System.out.println("Modulo Para Popup: "+id_modulo);
clientes = pptos.getTablePptos("1",id_user, id_modulo, "1", "5", "medida", "2014", "2014","", "id", "asc",false, true);
productos = pptos.getTablePptos("1",id_user, id_modulo, "1", "9", "medida", "2014", "2014","", "id", "asc",false, true); 
//pptos.getFiltroPopup("1", id_user, "2" ,"1", id_portlet) 
%>
<div id="rightnow">
    <h3 class="reallynow">
        <span><%=nom_org %></span>
        <span> - </span>
        <span><%=val_org %></span>
        <br />
    </h3>
</div>
<br />
<div id="box">
	<h3>Clientes <%=pptos.agregaFiltroPopup(id_customer, id_user, id_modulo, id_dashboard, "5") %></h3>
	<%=clientes %>
	<a href="excel_cliente.jsp" target="_blank"><img src="../../img/xls.jpg" width="25" height="25"/></a>
</div>
<div id="box">
	<h3>Productos  <%=pptos.agregaFiltroPopup(id_customer, id_user, id_modulo, id_dashboard, "9") %></h3>
	<%=productos %>
	<a href="excel_producto.jsp" target="_blank"><img src="../../img/xls.jpg" width="25" height="25"/></a>
</div>
<iframe id="iframeHidden" name="hidden" src="mod/charts/parametros_popup.jsp?id_user=1" style="width:0px; height:0px; border: 0px"  class="pptos"></iframe>
</body>
</html>