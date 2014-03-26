<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.math.RoundingMode"%>
<%

String mes = request.getParameter("mes");
int ms = 4;
if(mes != null && !mes.isEmpty() && !mes.equals("null")){
	ms= Integer.parseInt(mes);
}
%>
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
 	window.open(path , "_blank" , "width=550,height=400,scrollbars=YES"); 
 	}
   $(document).ready(function(){
	   $("#aComp option[value='"+<%=ms %>+"']").attr("selected",true);

		
		$(".meses").change(function() {//Evento click del boton paraq consultar segun los parametros selecconados
			var mes = $(this).val();
			//var x = "#g_"; 
			//var idIfr =  window.opener.location.href;//document.URL;//'#g_'+$(this).attr("id");
			//alert("ID. "+ idIfr);
		    
		    //Obtener URL 
			var e= document.getElementsByTagName("iframe"); 
			
		    /*Armo un arreglo con todos los div's*/
			//for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					//var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = document.URL;//$(idF).attr("src");//+"?medida="+med;
			    	//alert("src 1 "+src);
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	//if(idF == idIfr){
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
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		//alert("Fin "+srcFinal);
				    	}else{
				    		srcFinal = src + "?mes=" + mes;
				    	}
				    	//alert("src "+srcFinal);
				    	document.location.href = srcFinal;
				    	//window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	//}
			//}
		});
	});
   
  function insertaDatosProd(id_prod,id_portlet){
	   
	   var in_diasP1 = $("#in_diasP1").val();
	   var in_diasP2 = $("#in_diasP2").val();
	   var in_diasP3 = $("#in_diasP3").val();
	   
	   var in_cjsP1 = $("#in_cjsP1").val();
	   var in_cjsP2 = $("#in_cjsP2").val();
	   var in_cjsP3 = $("#in_cjsP3").val();
	   //alert(in_diasP1+" -"+in_diasP2+" - "+in_diasP3+" - "+in_cjsP1+" - "+in_cjsP2+" - "+in_cjsP3);
	   
			//Envio de datos para ser introducidos ala BD
			$.ajax({
				type : "POST",
				url : "datosSimulacion.jsp",
				data : "id_prod="+id_prod+"&id_portlet="+id_portlet+"&diasP1="+in_diasP1+"&diasP2="+in_diasP2+"&diasP3="+in_diasP3+
						"&cajasP1="+in_cjsP1+"&cajasP2="+in_cjsP2+"&cajasP3="+in_cjsP3,
				success: function(data){
					location.reload(true);
				}
			});
	}
</script>
</head>
<body>
<%
ToolsInv inv = new ToolsInv();
//Obtener ususario y modulo
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String id_chart = request.getParameter("id_chart");
String id_prod = request.getParameter("id_prod");


String inDiasP1 = (String)request.getParameter("diasP1");
String inDiasP2 = (String)request.getParameter("diasP1");
String inDiasP3 = (String)request.getParameter("diasP2");
String inCajasP1 = (String)request.getParameter("cajasP1");
String inCajasP2 = (String)request.getParameter("cajasP1");
String inCajasP3 = (String)request.getParameter("cajasP2");

if(id_user == null || id_user.equals("null") || 
	id_modulo == null || id_modulo.equals("null")){
	response.sendRedirect("login.jsp?error=s");
}
String id_customer = "1";
//Variables que contendran la tabla en htlm
//String a_compras = "";
//a_compras = inv.getTableAnalisisComprasCajasSim("1",id_user, id_modulo, "1", "10", mes);

String detalle_producto = "";//new HashMap();
detalle_producto = inv.getDetalleProd("1", id_user, id_modulo, "1", id_chart, id_prod, mes);

%>
<%=detalle_producto %>
</body>
</html>