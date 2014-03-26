<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
<%
String id_espacio = request.getParameter("id_espacio");
String id_prod_term = request.getParameter("id_prod_term");
String itemcode_prod_term = request.getParameter("itemcode_prod_term");
String id_producto = request.getParameter("id_prod");
String itemcode = request.getParameter("itemcode");
String tipo_prod = request.getParameter("tipo_prod");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<!-- <script type="text/javascript" src="../../js/jquery.tablesorter/jquery-latest.js"></script> -->
<!-- <script type="text/javascript" src="../../js/jquery.tablesorter/jquery.tablesorter-update.js"></script> -->

<!-- <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" /> -->
<!--   <script src="http://code.jquery.com/jquery-1.9.1.js"></script> -->
<!--   <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script> -->
<!--   <link rel="stylesheet" href="/resources/demos/style.css" /> -->

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
$(document).ready(function(){
	
	$.datepicker.regional['es'] =
	  {
		  prevText: 'Previo',
		  nextText: 'Próximo',
		  monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
		  'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
		  monthNamesShort: ['Ene','Feb','Mar','Abr','May','Jun',
		  'Jul','Ago','Sep','Oct','Nov','Dic'],
		  dayNames: ['Domingo','Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'],
		  dayNamesShort: ['Dom','Lun','Mar','Mie','Jue','Vie','Sáb'],
		  dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sa'],
		  dateFormat: 'dd/mm/yy', firstDay: 0,
		};
	 $.datepicker.setDefaults($.datepicker.regional['es']);
	 $( "#fecha" ).datepicker();
	 
	$("#aceptar").click(function(){
	    //alert('clicked!');
	    var fecha = $("#fecha").val();
	    var cantidad = $("#cantidad").val();
	    var num_doc = $("#num_doc").val();
	    var coment = $("#comentario").val();
	    var id_espacio = $("#id_espacio").val();
	    $.ajax({
			type : "POST",
			url : "insertaAjustes.jsp",
			data : "fecha="+fecha+"&cantidad="+cantidad+"&num_doc="+num_doc+"&coment="+coment+"&id_espacio="+id_espacio+"&tipo_prod="+<%=tipo_prod%>
	    			+"&itemcode_prod_term=<%=itemcode_prod_term%>+&id_prod_term="+<%=id_prod_term%>+"&id_producto="+<%=id_producto%>+"&itemcode=<%=itemcode%>",
			success: function(data){
				
				window.opener.parent.document.getElementById("prod_mpo").contentWindow.location.reload();
				window.opener.parent.document.getElementById("prod_list").contentWindow.location.reload();
				//window.opener.parent.mostrarEntradasSalidas();
				close();
			}
		});
	});
	
					
});


</script>
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

if(id_user == null || id_user.equals("null") || 
	id_modulo == null || id_modulo.equals("null")){
	response.sendRedirect("login.jsp?error=s");
}

%>
<div id="box">
<h3>Ajuste:</h3>
<input type="hidden" value=<%=id_espacio%> id="id_espacio">
<table class=tablesorter border=1 >
	
	<tr>
		<th>Fecha: </th>
		<th><input type="text" id="fecha"></th>
	</tr>
	<tr>
		<th>Cantidad: </th>
		<th><input type="text" id="cantidad"></th>
	</tr>
	<tr>
		<th># De Documento: </th>
		<th><input type="text" id="num_doc"></th>
	</tr>
	<tr>
		<th>Comentario: </th>
		<th><input type="text" id="comentario"></th>
	</tr>
	<tr><th colspan="2" valign="middle"><input type="button" value="Aceptar" id="aceptar"></th></tr>
</table>

</div>


</body>
</html>