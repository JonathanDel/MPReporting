<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="../js/jquery.tablesorter/jquery-latest.js"></script>
<script type="text/javascript">
/*Funcion que abre pupup */
function abrePop(path){
	  //alert(path);
	window.open(path , "ventana1" , "width=630,height=360,scrollbars=NO"); 
	}
$(document).ready(function(){
	$("#_mes").change(function() {
		  alert("Mes. "+ $(this).val());
	});
	
	$("#mes").change(function() {//Evento click del boton paraq consultar segun los parametros selecconados
		var mes = $(this).val();
		//alert("Mes. "+ $(this).val());
	    
	    //Obtener URL 
		var e= document.getElementsByTagName("iframe"); 
		
	    /*Armo un arreglo con todos los div's*/
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			
				/*  tu acci�n */
				var srcFinal = "";
				var idF = "#"+e[i].id;
				//alert (idF);
		    	var src = $(idF).attr("src");//+"?medida="+med;
		    	var valSrc = src.indexOf('?');
		    	var st = 0;
		    	//if(idF == "#iframe_idDiv0"){
			    	if(valSrc != -1){
			    		//var srcNV = remplace_url_parm(src,"medida",med);
			    		var param_mes = "mes";
			    		var new_mes = mes;
			    		var base = src.substr(0, src.indexOf('?'));
					    var query = src.substr(src.indexOf('?')+1, src.length);
					    var a_query = query.split('&');
					    for(var x=0; x < a_query.length; x++){
					        var name = a_query[x].split('=')[0];
					        //var value = a_query[x].split('=')[1];
					        if (name == param_mes){
					        	a_query[x] = param_mes+'='+new_mes;
					        	st = 1;
					        }
					    }
					    var srcNV = base + '?' + a_query.join('&');
			    		// alert(cmb_value);
			    		if(st == 0){
			    			srcFinal = srcNV + "&mes=" + mes;	
			    		}else{
			    			srcFinal = srcNV;
			    		}
			    		
			    	}else{
			    		srcFinal = src + "?mes=" + mes;
			    	}
			    	//alert("srcFinal "+srcFinal);
			    	
			    	window.parent.document.getElementById(e[i].id).src = srcFinal;		    	
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

%>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=3&cmp_id=id_marca&id_menu=1&id_Ifr=p_noDespz');" value="showMenu" id="menuPortelt_1"><span>Marca</span></button>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=3&cmp_id=id_producto&id_menu=2&id_Ifr=p_noDespz');" value="showMenu" id="menuPortelt_2"><span>Producto</span></button>
<div id="rightnow">
    <h3 class="reallynow">
        <span>Productos De Bajo Desplazamiento</span>
        
        <br />
    </h3>
</div>
</br>
<div id="box">
	<h3>Productos De Bajo Desplazamiento</h3>
	<iframe style="width:1100px" height="400px" frameborder="0"  src="mod/charts/ic_prod_no_desplaza.jsp" class="pptosP" id="p_noDespz"></iframe>
</div>
</body>
</html>