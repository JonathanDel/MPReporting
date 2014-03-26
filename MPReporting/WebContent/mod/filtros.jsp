<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<%@ page import="java.util.HashMap" %>
<%

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" src="../js/jquery-ui/js/jquery-1.7.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="../css/general.css" media="screen" />
<script src="../js/general.js"></script>
<link rel="stylesheet" type="text/css" href="../css/dragbox.css" media="screen" />	
<script type="text/javascript" src="../js/dragbox.js"></script>
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
function setMesIni(mes_1){
	
	$("#mesIni option[value='"+mes_1+"']").attr("selected",true);
	//$("#mesFin option[value='"+mes_1+"']").attr("disabled","disabled");
}
function setMesFin(mes_2){
	if(mes_2 != null){
		$("#mesFin option[value='"+mes_2+"']").attr("selected",true);
		//$("#mesIni option[value='"+mes_2+"']").attr("disabled","disabled");
	}else{
		$("#mesFin option[value='"+$("#mesIni").val()+"']").attr("disabled","disabled");
	}
	
}

$(document).ready(function(){
	
	//setMesIni("<=mes_1%>");
	//setMesFin("<=mes_2%>");
	
	
	$(".mainBox").mouseenter(function(e){
	       var id = $(this).attr("id");
	       //alert($(this).attr("id"));
	      $("#msg"+id).css("left", e.pageX);
	      $("#msg"+id).css("top", e.pageY);
	      $("#msg"+id).css("display", "block");
	   });
	   
	   $(".mainBox").mouseleave(function(e){
	       var id = $(this).attr("id");
	      $("#msg"+id).css("display", "none");
	   });
	   
	$(".medida_").click(function(){//Evento click del boton paraq consultar segun los parametros selecconados
		//alert("ConsultA");
		var medida= $(".medida:checked").val();
		//alert("medida" + medida);
		//Medidas Para Combinacion
		var cmb_medidas  = "";
		var cont;
		cont = 0;
	    $('.cmb_medidas').each(function(){
	    	//alert($(this).attr("checked"));
	    	if($(this).is(":checked")) {
	    		if(cont == 0){
	    			cmb_medidas += $(this).val();
	    			//alert("igual a 0");
	    		}else{
	    			cmb_medidas += "," + $(this).val();
	    			//alert("diferente a 0");
	    		}
	    		cont ++;
	    	}
	    	
	    });
	    
	    //Obtener rango de meses para filtros
	    var mesIni = $("#mesIni").val();
	    var mesFin = $("#mesFin").val();
	    //alert ("Mes Ini: "+mesIni + " Mes Fin: "+ mesFin);
	    //alert (cmb_medidas);
		var e=window.parent.document.getElementsByTagName("iframe"); 
		//alert(e.length);
		 /*Armo un arreglo con todos los div's*/
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			if (e[i].className == 'pptos'){
				/*  tu acción */
				var srcFinal = "";
				var idF = "#"+e[i].id;
				//alert (idF);
		    	var src = window.parent.$(idF).attr("src");//+"?medida="+med;
		    	var valSrc = src.indexOf('?');
		    	var st = 0;
		    	//if(idF == "#iframe_idDiv0"){
			    	if(valSrc != -1){
			    		//var srcNV = remplace_url_parm(src,"medida",med);
			    		var param_name = "medida";
			    		var param_cmb = "cmb_medidas";
			    		var param_mes_ini = "mesIni";
			    		var param_mes_fin = "mesFin";
			    		var new_value = medida;
			    		var cmb_value  = cmb_medidas;
			    		var val_mes_ini = mesIni;
			    		var val_mes_fin = mesFin;
			    		var base = src.substr(0, src.indexOf('?'));
					    var query = src.substr(src.indexOf('?')+1, src.length);
					    var a_query = query.split('&');
					    for(var x=0; x < a_query.length; x++){
					        var name = a_query[x].split('=')[0];
					        //var value = a_query[x].split('=')[1];
					        if (name == param_name){
					        	a_query[x] = param_name+'='+new_value;
					        	st = 1;
					        }
					        if (name == param_cmb){
					        	a_query[x] = param_cmb+'='+cmb_value;
					        	st = 1;
					        }
					        if (name == param_mes_ini){
					        	a_query[x] = param_mes_ini+'='+val_mes_ini;
					        	st = 1;
					        }
					        if (name == param_mes_fin){
					        	a_query[x] = param_mes_fin+'='+val_mes_fin;
					        	st = 1;
					        }
					    }
					    var srcNV = base + '?' + a_query.join('&');
			    		// alert(cmb_value);
			    		if(st == 0){
			    			srcFinal = srcNV + "&medida=" + new_value + "&cmb_medidas=" + cmb_value + "&mesIni=" + val_mes_ini + "&mesFin=" + val_mes_fin;	
			    		}else{
			    			srcFinal = srcNV;
			    		}
			    		
			    	}else{
			    		srcFinal = src + "?medida=" + medida + "&cmb_medidas=" + cmb_value + "&mesIni=" + val_mes_ini + "&mesFin=" + val_mes_fin;
			    	}
			    	//alert("srcFinal "+srcFinal);
			    	
			    	window.parent.document.getElementById(e[i].id).src = srcFinal;
		    	//}
		    	//else{
		    		//alert("srcFinal "+src);
		    		//window.parent.document.getElementById(e[i].id).src = src;
		    	//}
	    	}//if para todos los iframe de la clase
		}
	});	
});
</script>
<title>Insert title here</title>
</head>

<body>
<%
Tools pptos = new Tools();
String id_customer = request.getParameter("id_customer");
String id_dashboard = request.getParameter("id_dashboard");
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String chart_id = request.getParameter("chart_id");

String eliminar  = request.getParameter("delFil");


String medida = request.getParameter("medida");
String cmb_medidas = request.getParameter("cmb_medidas");
String mesIni = request.getParameter("mesIni");
String mesFin = request.getParameter("mesFin");
boolean reload = false;
if(id_user == null || id_user == "" || id_user.equals("null")){
	%>
	<script language="javascript">
	parent.document.location = parent.document.location;
	</script>
	<%
}
if(medida != null && medida != "" && cmb_medidas != null && cmb_medidas != ""){
	pptos.insertaMedidasGpo(id_user, "1", id_modulo, medida, cmb_medidas);
	reload = true;
}
if(mesIni != null && mesIni != "" && mesFin != null && mesFin != "" && !mesIni.equals("undefined") && !mesFin.equals("undefined")){
	pptos.insertaRangoMes("1", id_user, "1", "1", id_modulo, "1", mesIni, mesFin);
	reload = true;
}

if(reload){
	%>
	<script type="text/javascript">
	var e=window.parent.document.getElementsByTagName("iframe"); 
	/* Armo un arreglo con todos los div's*/
	for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
		if (e[i].className == 'pptosP'){
			/*  tu acción */
			var idF = "#"+e[i].id;
			//var idFnv = "#iframe_idDiv"+nFr;
	    	src = window.parent.$(idF).attr("src");
	    		window.parent.document.getElementById(e[i].id).src = src;
  		}
	}
	</script>
	<%

}
if(eliminar != null && eliminar != "" && id_user != null && id_user != ""){
	pptos.borraFiltro(eliminar, id_user, "1", id_modulo);
	%>
	<script type="text/javascript">
	var e=window.parent.document.getElementsByTagName("iframe"); 
	/* Armo un arreglo con todos los div's*/
	for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
		if (e[i].className == 'pptosP'){
			/*  tu acción */
			var idF = "#"+e[i].id;
			//var idFnv = "#iframe_idDiv"+nFr;
	    	src = window.parent.$(idF).attr("src");
	    		window.parent.document.getElementById(e[i].id).src = src;
  		}
	}
	</script>
	<%
	//System.out.println("Filtros: Eliminar "+eliminar);
}
%>
<div align="right"><input type="button" align="left" value="Consultar" class="medida_" id="consulta"></div>
<strong>Medidas:</strong>
<%=pptos.getGpoMed(id_user, id_modulo, "1")%>
&nbsp;&nbsp;&nbsp;&nbsp;
<strong>Combinacion:</strong>
<%=pptos.getCmbMed(id_user, id_modulo, "1")%>
&nbsp;&nbsp;&nbsp;&nbsp;
<strong>Periodo:</strong>
De
<select name="" id="mesIni">	
	<option value="0">--</option>
	<option value="1">Ene</option>
	<option value="2">Feb</option>
	<option value="3">Mar</option>
	<option value="4">Abr</option>
	<option value="5">May</option>
	<option value="6">Jun</option>
	<option value="7">Jul</option>
	<option value="8">Ago</option>
	<option value="9">Sep</option>
	<option value="10">Oct</option>
	<option value="11">Nov</option>
	<option value="12">Dic</option>
</select>

<select name="" id="mesFin">
	<option value="0">--</option>
	<option value="1">Ene</option>
	<option value="2">Feb</option>
	<option value="3">Mar</option>
	<option value="4">Abr</option>
	<option value="5">May</option>
	<option value="6">Jun</option>
	<option value="7">Jul</option>
	<option value="8">Ago</option>
	<option value="9">Sep</option>
	<option value="10">Oct</option>
	<option value="11">Nov</option>
	<option value="12">Dic</option>
</select>
De 2014
&nbsp;&nbsp;&nbsp;&nbsp;
<%
//(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_chart)
HashMap setMedida = pptos.obtieneRangoMes("1", id_user,id_modulo,"1","1");
String mes_1 = (String) setMedida.get("mes_1");
String mes_2 = (String) setMedida.get("mes_2");
%>
<script type="text/javascript">
$("#mesIni option[value='"+<%=mes_1%>+"']").attr("selected",true);
$("#mesFin option[value='"+<%=mes_2%>+"']").attr("selected",true);
</script>
<br><strong>Filtros:</strong><br>
<%=pptos.agregaFiltro("1", id_user, id_modulo,"1")%>
</body>
</html>