<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>
<%@ page import="java.util.HashMap" %>
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
<script type="text/javascript" src="../../js/funciones.js"></script>
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
 	window.open(path , "blank" , "width=950,height=520,scrollbars=YES"); 
 	}
   $(document).ready(function(){
	   $('#tblDatos').tablesorter({
			locale: 'en',
			headers: { 
				2: { sorter:'commaDigit'},
				3: { sorter:'shortDate' } ,
				4: { sorter:'commaDigit'},
                5: { sorter:'shortDate' } ,
                6: { sorter:'commaDigit'},
                7: { sorter:'commaDigit'},
                8: { sorter:'shortDate' } ,
                9: { sorter:'commaDigit'},
                10: { sorter:'shortDate' } ,
                11: { sorter:'commaDigit' } ,
                12: { sorter:'commaDigit'},
                13: { sorter:'shortDate' } ,
                14: { sorter:'commaDigit'},
                15: { sorter:'shortDate' } ,
            	16: { sorter:'commaDigit' }
           }
		});
		
	   $(".toolTip").mouseenter(function(e){
		    var id = $(this).attr("id");
		    //alert($(this).attr("id"));
		   $("#msg"+id).css("left", e.pageX);
		   $("#msg"+id).css("top", e.pageY);
		   $("#msg"+id).css("display", "block");
		});

		$(".toolTip").mouseleave(function(e){
		    var id = $(this).attr("id");
		   $("#msg"+id).css("display", "none");
		});
		
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
   function eliminaSimulacion(idProd){
	 //Elimina simulacion
		$.ajax({
			type : "POST",
			url : "eliminaSimulacion.jsp",
			data : "id_prod="+idProd,
			success: function(data){
				location.reload(true);
				alert("Eliminacion de simulacion correcta...");
				
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
//String fun = "guardaFiltros('"+id_portlet+"','"+cmp_id+"','"+id_Ifrm+"');";
String a_compras = "";
a_compras = inv.getTableAnalisisComprasSim("1",id_user, id_modulo, "1", "9", mes);

HashMap hm = new HashMap();
hm = inv.getDatosPorProd(id_prod, 4);
HashMap sim = new HashMap();
sim = inv.getDatosPorProdSim("1", id_user, id_modulo, "1", "9",id_prod);

String m_diasP1 = (String) sim.get("diasP1");
String m_diasP2 = (String) sim.get("diasP2");
String m_diasP3 = (String) sim.get("diasP3");

String m_cajasP1 = (String) sim.get("cajasP1");
String m_cajasP2 = (String) sim.get("cajasP2");
String m_cajasP3 = (String) sim.get("cajasP3");


if(m_diasP1 == null || m_diasP1.equals("null")){
	m_diasP1 = "0";
}
if(m_diasP2 == null || m_diasP2.equals("null")){
	m_diasP2 = "0";
}
if(m_diasP3 == null || m_diasP3.equals("null")){
	m_diasP3 = "0";
}

if(m_cajasP1 == null || m_cajasP1.equals("null")){
	m_cajasP1 = "0";
}
if(m_cajasP2 == null || m_cajasP2.equals("null")){
	m_cajasP2 = "0";
}
if(m_cajasP3 == null || m_cajasP3.equals("null")){
	m_cajasP3 = "0";
}

%>
</br>
<div id="box">
<h3>Producto: <%=hm.get("producto") %> </h3>
<table class=tablesorter border=1 >
	<thead>
		<tr>
		<th colspan="3" align="center">Pedido 1</th>
		<th colspan="3" align="center">Pedido 2</th>
		<th colspan="3" align="center">Pedido 3</th>
		</tr>
		<tr>
			<th>Introducir</th>
			<th>Compras</th>
			<th>Introducir</th>
			<th>Introducir</th>
			<th>Compras</th>
			<th>Introducir</th>
			<th>Introducir</th>
			<th>Compras</th>
			<th>Introducir</th>
		</tr>
		<tr>
			<th>DIAS</th>
			<th>CAJAS</th>
			<th>CAJAS</th>
			<th>DIAS</th>
			<th>CAJAS</th>
			<th>CAJAS</th>
			<th>DIAS</th>
			<th>CAJAS</th>
			<th>CAJAS</th>
		</tr>
	</thead>
	<tr>
			<th> <input type="text" id="in_diasP1" size="10" value=<%=m_diasP1 %>><br></th>
			<th><%=hm.get("cajasP1") %></th>
			<th> <input type="text" id="in_cjsP1" size="10" value=<%=m_cajasP1 %>><br></th>
			
			<th> <input type="text" id="in_diasP2" size="10" value=<%=m_diasP2 %>><br></th>
			<th><%=hm.get("cajasP2") %></th>
			<th> <input type="text" id="in_cjsP2" size="10" value=<%=m_cajasP2 %>><br></th>
			
			<th> <input type="text" id="in_diasP3" size="10" value=<%=m_diasP3 %>><br></th>
			<th><%=hm.get("cajasP3") %></th>
			<th> <input type="text" id="in_cjsP3" size="10" value=<%=m_cajasP3 %>><br></th>
		</tr>
</table>
<button onclick="insertaDatosProd(<%=id_prod %>,9);" id="simProd">Simular</button>
</div>
</br>
</br>
</br>
<div id="box">
<button onclick="abrePop('popup_ventas.jsp?id_portlet=9&cmp_id=id_marca&id_menu=1&id_Ifr=g_sComp');" value="showMenu" id="menuPortelt_5"><span>Marca</span></button>
<button onclick="abrePop('popup_ventas.jsp?id_portlet=9&cmp_id=id_producto&id_menu=2&id_Ifr=g_sComp');" value="showMenu" id="menuPortelt_5"><span>Producto</span></button>
# Meses:
<select class=meses id=aComp>
	<option value=1>1</option>
	<option value=2>2</option>
	<option value=3>3</option>
	<option value=4 selected="selected">4</option>
	<option value=5>5</option>
	<option value=6>6</option>
	<option value=7>7</option>
	<option value=8>8</option>
	<option value=9>9</option>
	<option value=10>10</option>
	<option value=11>11</option>
	<option value=12>12</option>
</select>
	<h3>Análisis De Compras (Días)</h3>
<%=a_compras %>

<form action="excel.jsp" method="POST" onsubmit="return cadenaTabla()">
	<input type="hidden" name="tabla" value="" id="tabla" /> 
	<input type="image" src="../../img/xls.jpg" width="25" height="25" />
</form>

</div>
</body>
</html>