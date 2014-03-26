<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery-latest.js"></script>
<script type="text/javascript" src="../../js/jquery.tablesorter/jquery.tablesorter-update.js"></script>
<style type="text/css">
   .tip{
      background-color: #ccc;
      padding: 10px;
      display: none;
      position: absolute;
      z-index:4000;      
   }
</style>

<link rel="stylesheet" type="text/css" href="../../js/jquery.tablesorter/themes/blue/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css" />
<script>
   var StyleFile = "theme" + document.cookie.charAt(6) + ".css";
   document.writeln('<link rel="stylesheet" type="text/css" href="css_charts/theme1.css">');

   /*Funcion que abre pupup */
   function abrePop(path){
	   window.open(path , "detalle" , "width=750,height=215,scrollbars=NO,toolbar=NO,resize=NO"); 
 	}
   
   function hideRow(id){
		//alert(id);
		$(".mrc_"+id).hide();
	}
   function showRow(id){
		//alert(id);
		$(".mrc_"+id).show();
	}
</script>
</head>
<body>
<%
ToolsInv inv = new ToolsInv();
//Obtener ususario y modulo
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String mes = request.getParameter("mes");
String id_espacios = request.getParameter("id_espacios");
String id_prod_term = request.getParameter("id_prod_term");

String fechaIni = request.getParameter("fechaIni");
String fechaFin = request.getParameter("fechaFin");
//String idEspacios = request.getParameter("idEspacios");

System.out.println("ID_PROD_TERM "+id_prod_term);
if(id_user == null || id_user.equals("null") || 
	id_modulo == null || id_modulo.equals("null")){
	response.sendRedirect("login.jsp?error=s");
}
String id_customer = "1";
//Variables que contendran la tabla en htlm
System.out.println("Lista entradas por lugar..."+ id_prod_term);
String prod_no_desplaza = "";
if(id_prod_term != null && id_espacios != null ){
	prod_no_desplaza = inv.getTableListaSalidasPorLugar("1",id_user, id_modulo, "1", "3", id_prod_term, fechaIni, fechaFin, id_espacios);
}
%>
<%=prod_no_desplaza %>

</body>
</html>