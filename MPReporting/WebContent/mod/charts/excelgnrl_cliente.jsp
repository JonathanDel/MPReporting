<%@ page import="com.auribox.reporting.tools.Tools" %>
<%@ page import="java.util.HashMap" %>
<%@include file="utils/FusionCharts/Code/JSP/Includes/FusionCharts.jsp" %>

<%
Tools pptos = new Tools();
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String medida = request.getParameter("medida");
String ctacve = request.getParameter("valFil");
String mesFin = request.getParameter("mesFin");
String id = request.getParameter("id");
String valor = request.getParameter("valor");
String parametro = request.getParameter("parametro");
String chart_id = request.getParameter("chart_id");
String id_dashboard = "1";
String id_customer = "1";
String nombre = request.getParameter("nombre");
String nomValue = request.getParameter("nomVal");
if(valor!=null && valor!="" && parametro != null && parametro != "" && chart_id != null && nombre != null && nomValue != "" &&
id_user != null && id_user != "" && id_dashboard != null && id_dashboard != "" && id_modulo != null && id_modulo != ""){
	pptos.insertaParametrosGnrl(id_customer, id_user, id_dashboard, id_modulo, chart_id,
			valor, parametro, nombre, nomValue);
}
%>
<%
String idF = "";
String var = ""; 
if(chart_id.equals("10")){
	idF = "5";
}else if(chart_id.equals("5")){
	idF = "9";
}
	var = pptos.getTablePptosGnrl("1", id_user, id_modulo, "1", idF, "medida", "2013", "2013",medida, "id", "asc", true,false,false); 
%>
<div id="box">
	<h3><%=nomValue %></h3>
<%=var %>
</div>