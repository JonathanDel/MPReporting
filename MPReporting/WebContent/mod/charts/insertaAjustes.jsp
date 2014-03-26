<%@page import="java.lang.reflect.Array"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
ToolsInv inv = new ToolsInv();

String id_customer = "1";//(String) session.getAttribute("id_cust");
String id_modulo = (String) session.getAttribute("mod");
String id_user = (String)session.getAttribute("user");//"1";//(String)session.gerAtributte("id_uder");
String id_dashboard = "1"; //(String)session.getAttribute("id_dashboard");

String id_espacio = request.getParameter("id_espacio");
String id_prod_term = request.getParameter("id_prod_term");
String itemcode_prod_term = request.getParameter("itemcode_prod_term");
String id_producto = request.getParameter("id_producto");
String itemcode = request.getParameter("itemcode");
String tipo_prod = request.getParameter("tipo_prod");

String fecha_ajuste = request.getParameter("fecha");
String cantidad = request.getParameter("cantidad");
String num_doc = request.getParameter("num_doc");
String comentario = request.getParameter("coment");

Date ahora = new Date();
SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat frt = new SimpleDateFormat("dd/MM/yyyy");
String fecha = formateador.format(ahora);
Date fechaajuste = null;
fechaajuste = frt.parse(fecha_ajuste);
String fechaaj = formateador.format(fechaajuste);
System.out.println("usr: "+id_user+" dash: "+id_dashboard+" id_espacio: "+id_espacio+" id_prod_term: "+id_prod_term+" id_producto: "+id_producto+
		" itemcode: "+itemcode+" fecha: "+fecha_ajuste+" cantidad: "+cantidad+" num_doc: "+num_doc+" comentario: "+comentario);
if(id_user != null ){ 
	inv.insertaDatosAjuste(id_customer, id_user, id_modulo, id_dashboard, "1", id_espacio, id_prod_term, itemcode_prod_term, itemcode, id_producto, tipo_prod,fecha, fechaaj, cantidad, num_doc, comentario);
}


%>
</body>
</html>