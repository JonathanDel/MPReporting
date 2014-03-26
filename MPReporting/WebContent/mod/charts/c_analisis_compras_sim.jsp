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
<script type="text/javascript">
$(document).ready(function()
		{
			$("#tblDatos").tablesorter();
			
			$(".mes").mouseenter(function(e){
			    var id = $(this).attr("id");
			    //alert($(this).attr("id"));
			   $("#msg"+id).css("left", e.pageX);
			   $("#msg"+id).css("top", e.pageY);
			   $("#msg"+id).css("display", "block");
			});

			$(".mes").mouseleave(function(e){
			    var id = $(this).attr("id");
			   $("#msg"+id).css("display", "none");
			});
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
ToolsInv inv = new ToolsInv();
//Obtener ususario y modulo
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String mes = request.getParameter("mes");
if(id_user == null || id_user.equals("null") || 
	id_modulo == null || id_modulo.equals("null")){
	response.sendRedirect("login.jsp?error=s");
}
String id_customer = "1";
//Variables que contendran la tabla en htlm
String a_compras = "";
a_compras = inv.getTableAnalisisCompras("1",id_user, id_modulo, "1", "5", mes);

%>
<%=a_compras %>

</body>
</html>