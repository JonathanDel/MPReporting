<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<!-- <script type="text/javascript" src="../../js/jquery.tablesorter/jquery-latest.js"></script> -->
<!-- <script type="text/javascript" src="../../js/jquery.tablesorter/jquery.tablesorter-update.js"></script> -->
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
	   var left = (screen.width/2)-(300/2);
	   var top = (screen.height/2)-(180/2);
	   window.open(path , "ajuste" , "width=300,height=180,scrollbars=NO,toolbar=NO,resize=NO,top="+top+", left="+left); 
 	}
   
   function hideRow(id){
		//alert(id);
		$(".mrc_"+id).hide();
	}
   function showRow(id){
		//alert(id);
		$(".mrc_"+id).show();
	}
   function listaProdLugares(id, fechaIni,fechaFin){
		var e = parent.document.getElementById('prod_list');
			    	
		var srcFinal = "";
		var base = "";
		var id_prod_term = id;
		var src =  e.getAttribute('src');
		//alert("src--"+src);
		
		var valSrc = src.indexOf('?');
    	if(valSrc != -1){
			base = src.substr(0, src.indexOf('?'));
			srcFinal = base + "?id_prod_term=" + id_prod_term +"&fechaIni=" + fechaIni + "&fechaFin=" + fechaFin;
    	}else{
    		srcFinal = src + "?id_prod_term=" + id_prod_term +"&fechaIni=" + fechaIni + "&fechaFin=" + fechaFin;
    	}
		//alert("src2 "+srcFinal);
		window.parent.document.getElementById(e.id).src = srcFinal;
		parent.mostrarProductosMPO();
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

String fechaIni = request.getParameter("fechaIni");
String fechaFin = request.getParameter("fechaFin");
String idEspacios = request.getParameter("idEspacios");

if(id_user == null || id_user.equals("null") || 
	id_modulo == null || id_modulo.equals("null")){
	response.sendRedirect("login.jsp?error=s");
}
String id_customer = "1";
//Variables que contendran la tabla en htlm
System.out.println("mes..."+mes);
String prod_no_desplaza = "";
prod_no_desplaza = inv.getTableListaProdMPO("1",id_user, id_modulo, "1", "3", fechaIni, fechaFin, idEspacios); 

%>
<%=prod_no_desplaza %>

</body>
</html>