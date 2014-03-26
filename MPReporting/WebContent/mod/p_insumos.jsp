<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="jquery-ui/css/redmond/jquery-ui-1.10.3.custom.css" rel="stylesheet">
<script src="jquery-ui/js/jquery-1.9.1.js"></script>
<script src="jquery-ui/js/jquery-ui-1.10.3.custom.js"></script>
<script>
/*Funciones Para Mostrar Tabs*/
function mostrarProductosMPO(){
	$('#tabs').tabs( "option", "active", 1 );
}
function mostrarEntradasSalidas(){
	$('#tabs').tabs( "option", "active", 2 );
}
function mostrarEntradasSalidasInsumComp(){
	$('#tabs').tabs( "option", "active", 3 );
}
/*Funcion que abre pupup */
function abrePop(path){
	  //alert(path);
	window.open(path , "ventana1" , "width=630,height=360,scrollbars=NO"); 
	}
$(document).ready(function(){
	
	$("#tabs").tabs();
	
	//Array para dar formato en español
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
	 $( "#datepicker" ).datepicker();
	 $( "#inicio" ).datepicker();
	 $( "#fin" ).datepicker();
	    
	$("#filtro").click(function(){//Evento click del boton paraq consultar segun los parametros selecconados
		var fechaIni = $("#inicio").val();
		var fechaFin = $("#fin").val();
		var idEspacios = $('#espacios option:selected').val();
		
		var elementos = "";// = new Array();
		var cont = 0;
		$("#espacios option:selected").each(function(){
	  		if(cont == 0){
	  			elementos += $(this).val();
	  		}else{
	  			elementos += ','+$(this).val();
	  		}
	  		cont = cont + 1;
		});
		
		//alert("Parametros: "+fechaIni+" _ "+fechaFin+" _ "+elementos);
		var e= document.getElementsByTagName("iframe"); 
		
	    /*Armo un arreglo con todos los div's*/
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			
				/*  tu acción */
				var base = "";
				var srcFinal = "";
				var idF = "#"+e[i].id;
		    	var src = $(idF).attr("src");
		    	var valSrc = src.indexOf('?');
		    	
			    	if(valSrc != -1){
			    		base = src.substr(0, src.indexOf('?'));
			    		
					    if(e[i].id == "prod_mpo"){
					    	srcFinal = base + "?fechaIni=" + fechaIni + "&fechaFin=" + fechaFin + "&idEspacios=" + elementos;
					    }else{
					    	srcFinal = base;
					    }
			    	}else{
			    		if(e[i].id == "prod_mpo"){
					    	srcFinal = src + "?fechaIni=" + fechaIni + "&fechaFin=" + fechaFin + "&idEspacios=" + elementos;
					    }else{
					    	srcFinal = src;
					    }
			    		
			    	}
			    	if(e[i].id == "prod_mpo"){
			    		window.parent.document.getElementById(e[i].id).src = srcFinal;
			    		$('#tabs').tabs( "option", "active", 0 );
			    	}else{
			    		if(base == "" || base == null){
			    			window.parent.document.getElementById(e[i].id).src = srcFinal;
			    		}else{
			    			window.parent.document.getElementById(e[i].id).src = base;
			    		}
			    	}
			    			    	
		}

	});
	
});

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
String espacios = inv.getFiltroEspacios();
%>
<br><strong>Periodo:</strong>
Inicio: <input type="text" id="inicio" size = "10" />
Fin: <input type="text" id="fin" size = "10"/>
<strong>Espacios:</strong>
<%=espacios %>
<input type="button" align="left" value="Consultar" class="fil" id="filtro">

<div id="rightnow">
    <h3 class="reallynow">
        <span>Insumos</span>
        
        <br />
    </h3>
</div>
</br>
<div id="tabs">
	<ul>
		<li><a href="#tabs-1">Productos MPO</a></li>
		<li><a href="#tabs-2">Componentes en Cada  Lugar</a></li>
		<li><a href="#tabs-3">Entradas - Salidas por Lugar</a></li>
		<li><a href="#tabs-4">Entradas - Salidas por Componente o Insumo</a></li>
	</ul>
	
	<div id="tabs-1">
		<div id="box">
			<h3>Productos MPO</h3>
			<iframe style="width:1050px" height="400px" frameborder="0"  src="mod/charts/ic_productos_mpo.jsp" class="prod" id="prod_mpo" name="prod_mpo"></iframe>
		</div>
	</div>
	
	<div id="tabs-2">
		<div id="box">
			<h3>Componentes En Cada Lugar</h3>
			<iframe style="width:1050px" height="400px" frameborder="0"  src="mod/charts/ic_lista_lugares.jsp" class="list" id="prod_list" name="prod_list"></iframe>
		</div>
	</div>
	
	<div id="tabs-3">
		<table border="0" cellspacing="0" cellpadding="0" style="width:730px">
		 <tr>
		    <td valign="top">
		    <div id="box" style="width:525px">
			    <h3>Entradas Por Lugar</h3>
		        <iframe  style="width:525px" height="350px"  frameborder="0" src="mod/charts/ic_lista_entradas.jsp" class="listES" id="entradas_list" name="entradas_list"></iframe>
			</div>
		    </td>
		    <td valign="top">
		    <div id="box" style="width:525px">
			    <h3>Salidas Por Lugar</h3>
		     	<iframe  style="width:525px" height="350px"  frameborder="0" src="mod/charts/ic_lista_salidas.jsp" class="listES" id="salidas_list" name="salidas_list"></iframe>
			</div>
		    </td>
		  </tr>
		</table>
	</div>
	
	<div id="tabs-4">
		<table border="0" cellspacing="0" cellpadding="0" style="width:730px">
		 <tr>
		    <td valign="top">
		    <div id="box" style="width:525px">
			    <h3>Entradas Por Componente / Insumo</h3>
		        <iframe  style="width:525px" height="350px"  frameborder="0" src="mod/charts/ic_lista_entradas_ci.jsp" class="listES_IC" id="entradas_list_ic" name="entradas_list_ic"></iframe>
			</div>
		    </td>
		    <td valign="top">
		    <div id="box" style="width:525px">
			    <h3>Salidas Por Componente / Insumo</h3>
 		     	<iframe  style="width:525px" height="350px"  frameborder="0" src="mod/charts/ic_lista_salidas_ci.jsp" class="listES_IC" id="salidas_list_ic" name="salidas_list_ic"></iframe> 
			</div>
		    </td>
		  </tr>
		</table>
	</div>
</div>

</body>
</html>