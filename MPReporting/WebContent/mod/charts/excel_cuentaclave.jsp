<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<%@ page import="java.util.HashMap" %>
<%@include file="utils/FusionCharts/Code/JSP/Includes/FusionCharts.jsp" %>
<%
response.setContentType ("application/vnd.ms-excel"); //Tipo de fichero.
response.setHeader ("Content-Disposition", "attachment;filename=\"cuenta_clave.xls\"");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery-latest.js"></script>
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery.tablesorter.js"></script>

</head>
<body>
<%
Tools pptos = new Tools();
//String id_user = request.getParameter("id_user");
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String id_customer = "1";
String id_dashboard = "1";
//String id_modulo = request.getParameter("id_modulo");
String id_chart = request.getParameter("id_chart");
String parametro = request.getParameter("parametro");
String val_param = request.getParameter("val_param");
String nom_org = request.getParameter("nom_org");
String val_org = request.getParameter("val_org");
String del_fil = request.getParameter("delFil");

if(del_fil.equals("true")){
	// Elimina filtros antes de iniciar..
	System.out.println("Eliminar filtros de popup gener");
	pptos.eliminaFiltroPopupGnrl(id_customer, id_user, id_dashboard, id_modulo);
}

//obtiene el cuenta clave ke pertenece a los usuarios
String cc = pptos.getPerCC(id_user);
if(cc != "" && cc != null){
	//insertamos el CC en caso de que no sea admin para aplicar filtro
	System.out.println("CCCCCC");
	pptos.insertaParametrosGnrl(id_customer, id_user, id_dashboard, id_modulo, "10",
						cc, "id_sop_ctaclave", "---", "---");
}
String cta_cve = "";
String productos = "";

cta_cve = pptos.getTablePptosGnrl("1",id_user, id_modulo, "1", "10", "medida", "2013", "2013","", "id", "asc",true, false, true); 
//pptos.getFiltroPopup("1", id_user, "2" ,"1", id_portlet) 
%>
<div id="box">
	<h3>Cuenta Clave</h3>
	<%=cta_cve %>
	
</div>

</body>
</html>