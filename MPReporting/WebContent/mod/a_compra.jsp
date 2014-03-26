<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
/*Funcion que abre pupup */
function abrePop(path){
	  //alert(path);
	window.open(path , "ventana1" , "width=630,height=320,scrollbars=NO"); 
	}
</script>
</head>
<body>
<%
ToolsInv inv = new ToolsInv();
//Obtener ususario y modulo
String id_user = (String) session.getAttribute("user");
String id_modulo = "2";//(String) session.getAttribute("mod");
if(id_user == null || id_user.equals("null") || 
	id_modulo == null || id_modulo.equals("null")){
	response.sendRedirect("login.jsp?error=s");
}
String id_customer = "1";
//Variables que contendran la tabla en htlm
//String a_ventas = "";
//a_ventas = inv.getTableAnalisisVentas("1",id_user, id_modulo, "1", "7", "medida", "2011", "2012","", "id", "asc",false, true);

%>
<!-- <button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=1&cmp_id=id_marca');" value="showMenu" id="menuPortelt_5"><span>Marca</span></button>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=2&cmp_id=id_producto');" value="showMenu" id="menuPortelt_5"><span>Producto</span></button> -->
<div id="rightnow">
    <h3 class="reallynow">
        <span>Analisis de Compra</span>
        
        <br />
    </h3>
</div>
</br>
<div id="box">
	<h3>Analisis de Compra</h3>
	<iframe style="width:940px" height="400px" frameborder="0"  src="mod/charts/ic_analisis_compras.jsp" class="pptosP" id="categoria"></iframe>
</div>
</body>
</html>