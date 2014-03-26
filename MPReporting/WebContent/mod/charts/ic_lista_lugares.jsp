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
<link href="../../jquery-ui/css/redmond/jquery-ui-1.10.3.custom.css" rel="stylesheet">
<script src="../../jquery-ui/js/jquery-1.9.1.js"></script>
<script src="../../jquery-ui/js/jquery-ui-1.10.3.custom.js"></script>
<style type="text/css">
   .tip{
      background-color: #ccc;
      padding: 10px;
      display: none;
      position: absolute;
      z-index:4000;      
   }
</style>
<script type="text/javascript">

function listaEntradasLugares(idEspacios, id_prodTerm, fechaIni, fechaFin){
	var e = parent.document.getElementsByTagName('iframe');
		
	for(var i=0;i<e.length;i++){
		if (e[i].className == 'listES'){
				/*  tu acción */
			var srcFinal = "";
			var idF = "#"+e[i].id;
		    
			var src = window.parent.$(idF).attr("src");
		    var valSrc = src.indexOf('?');
			
			if(valSrc != -1){
			
				var base = src.substr(0, src.indexOf('?'));
				srcFinal = base + "?id_prod_term=" + id_prodTerm+"&id_espacios="+ idEspacios+"&fechaIni=" + fechaIni+ "&fechaFin=" + fechaFin;	
			    
			}else{
				srcFinal = src + "?id_prod_term=" + id_prodTerm+"&id_espacios="+ idEspacios+"&fechaIni=" + fechaIni+ "&fechaFin=" + fechaFin;
			}
			//alert("src "+srcFinal);
			window.parent.document.getElementById(e[i].id).src = srcFinal;
		}
	}
	parent.mostrarEntradasSalidas();	
}

function listaEntradasLugaresInsumComp(idEspacios, id_compInsum, fechaIni, fechaFin){
	var e = parent.document.getElementsByTagName('iframe');
		
	for(var i=0;i<e.length;i++){
		if (e[i].className == 'listES_IC'){
				/*  tu acción */
			var srcFinal = "";
			var idF = "#"+e[i].id;
		    
			var src = window.parent.$(idF).attr("src");
		    var valSrc = src.indexOf('?');
			
			if(valSrc != -1){
			
				var base = src.substr(0, src.indexOf('?'));
				srcFinal = base + "?id_comp_insum=" + id_compInsum +"&id_espacios="+ idEspacios+"&fechaIni=" + fechaIni + "&fechaFin=" + fechaFin;	
			    
			}else{
				srcFinal = src + "?id_comp_insum=" + id_compInsum +"&id_espacios="+ idEspacios+"&fechaIni=" + fechaIni + "&fechaFin=" + fechaFin;
			}
			//alert("src "+srcFinal);
			window.parent.document.getElementById(e[i].id).src = srcFinal;
		}
	}
	parent.mostrarEntradasSalidasInsumComp();	
}


</script>
<link rel="stylesheet" type="text/css" href="../../js/jquery.tablesorter/themes/blue/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css" />
<script>
   var StyleFile = "theme" + document.cookie.charAt(6) + ".css";
   document.writeln('<link rel="stylesheet" type="text/css" href="css_charts/theme1.css">');

   /*Funcion que abre pupup */
   function abrePop(path){
	   var left = (screen.width/2)-(450/2);
	   var top = (screen.height/2)-(280/2);
	   window.open(path , "ajuste" , "width=450,height=280,scrollbars=NO,toolbar=NO,resize=NO,top="+top+", left="+left); 
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
String id_prod_term = request.getParameter("id_prod_term");

String fechaIni = request.getParameter("fechaIni");
String fechaFin = request.getParameter("fechaFin");
String idEspacios = request.getParameter("idEspacios");

System.out.println("ID_PROD_TERM "+id_prod_term);
if(id_user == null || id_user.equals("null") || 
	id_modulo == null || id_modulo.equals("null")){
	response.sendRedirect("login.jsp?error=s");
}
String id_customer = "1";
//Variables que contendran la tabla en htlm
System.out.println("<<<......>>>>Fecha Ini... "+fechaIni);
System.out.println("<<<......>>>>Fecha Fin... "+fechaFin);
String prod_no_desplaza = "";
if(id_prod_term != null ){
	prod_no_desplaza = inv.getTableListaComponentesPorLugar("1",id_user, id_modulo, "1", "3", id_prod_term, fechaIni, fechaFin, "");
}
%>
<%=prod_no_desplaza %>

</body>
</html>