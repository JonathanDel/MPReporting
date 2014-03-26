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
	window.open(path , "ventana1" , "width=630,height=360,scrollbars=NO"); 
	}
$(document).ready(function(){
	$("#_mes").change(function() {
		 // alert("Mes. "+ $(this).val());
	});
	
	
	
	$("input[name='a_invent']").click(function(){
		//	Operaciones en la función.
		var ver = $("input[name='a_invent']:checked").val();
		var act = $("input[name='a_inventActivo']").prop("checked") && $("input[name='a_inventInactivo']").prop("checked")?
				"undefined":$("input[name='a_inventActivo']").prop("checked")?
						$("input[name='a_inventActivo']").val():$("input[name='a_inventInactivo']").prop("checked")?
								$("input[name='a_inventInactivo']").val():"undefined";
		var mes = $("#aInvent").val();
		//alert(ver+"---"+mes);
		var e= document.getElementsByTagName("iframe"); 
		var idIfr = "#g_aInvent";
	    /*Armo un arreglo con todos los div's*/
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			
				/*  tu acción */
				var srcFinal = "";
				var idF = "#"+e[i].id;
				//alert (idF);
		    	var src = $(idF).attr("src");//+"?medida="+med;
		    	var valSrc = src.indexOf('?');
		    	var st = 0;
		    	//alert(idF +'='+idIfr);
		    	if(idF == idIfr){
			    	if(valSrc != -1){
			    		//var srcNV = remplace_url_parm(src,"medida",med);
			    		var param_mes = "mes";
			    		var new_mes = mes;
			    		var param_ver = "ver";
			    		var new_ver = ver;
			    		var param_act = "act";
			    		var new_act = act;
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
					        if (name == param_ver){
					        	a_query[x] = param_ver+'='+new_ver;
					        	st = 1;
					        }
					        if (name == param_act){
					        	a_query[x] = param_act+'='+new_act;
					        	st = 1;
					        }
					    }
					    var srcNV = base + '?' + a_query.join('&');
			    		if(st == 0){
			    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
			    		}else{
			    			srcFinal = srcNV;
			    		}
			    		
			    	}else{
			    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
			    	}
			    	//alert("src "+srcFinal);
			    	window.parent.document.getElementById(e[i].id).src = srcFinal;
		    	}
		}
		
	});
	
	$("input[name='a_ventas']").click(function(){
		//	Operaciones en la función.
		var ver = $("input[name='a_ventas']:checked").val();
		var act = $("input[name='a_ventasActivo']").prop("checked") && $("input[name='a_ventasInactivo']").prop("checked")?
							"undefined":$("input[name='a_ventasActivo']").prop("checked")?
									$("input[name='a_ventasActivo']").val():$("input[name='a_ventasInactivo']").prop("checked")?
											$("input[name='a_ventasInactivo']").val():"undefined";
		console.log("Act= "+act);
		console.log("VentasActivo "+ $("input[name='a_ventasActivo']").prop("checked"));
		console.log("VentasInactivo "+ $("input[name='a_ventasInactivo']").prop("checked"));
		var mes = $("#aVentas").val();
		//alert(ver+"---"+mes);
		var e= document.getElementsByTagName("iframe"); 
		var idIfr = "#g_aVentas";
	    /*Armo un arreglo con todos los div's*/
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			
				/*  tu acción */
				var srcFinal = "";
				var idF = "#"+e[i].id;
				//alert (idF);
		    	var src = $(idF).attr("src");//+"?medida="+med;
		    	var valSrc = src.indexOf('?');
		    	var st = 0;
		    	//alert(idF +'='+idIfr);
		    	if(idF == idIfr){
			    	if(valSrc != -1){
			    		//var srcNV = remplace_url_parm(src,"medida",med);
			    		var param_mes = "mes";
			    		var new_mes = mes;
			    		var param_ver = "ver";
			    		var new_ver = ver;
			    		var param_act = "act";
			    		var new_act = act;
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
					        if (name == param_ver){
					        	a_query[x] = param_ver+'='+new_ver;
					        	st = 1;
					        }
					        if (name == param_act){
					        	a_query[x] = param_act+'='+new_act;
					        	st = 1;
					        }
					    }
					    var srcNV = base + '?' + a_query.join('&');
			    		if(st == 0){
			    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
			    		}else{
			    			srcFinal = srcNV;
			    		}
			    		
			    	}else{
			    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
			    	}
			    	//alert("src "+srcFinal);
			    	window.parent.document.getElementById(e[i].id).src = srcFinal;
		    	}
		}
		
	});
	
	$("input[name='a_ventasActivo']").click(function(){
		if($("input[name='a_ventasActivo']").prop("checked") && !$("input[name='a_ventasInactivo']").prop("checked")){
			//	Operaciones en la función.
			var ver = $("input[name='a_ventas']:checked").val();
			var act = $("input[name='a_ventasActivo']:checked").val();
			var mes = $("#aVentas").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aVentas";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if($("input[name='a_ventasInactivo']").prop("checked") && $("input[name='a_ventasActivo']").prop("checked")){
//			Operaciones en la función.
			var ver = $("input[name='a_ventas']:checked").val();
			
			var mes = $("#aVentas").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aVentas";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver ;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if(!$("input[name='a_ventasActivo']").prop("checked") && $("input[name='a_ventasInactivo']").prop("checked")){
			//	Operaciones en la función.
			var ver = $("input[name='a_ventas']:checked").val();
			var act = $("input[name='a_ventasInactivo']:checked").val();
			var mes = $("#aVentas").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aVentas";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}
	});
	
	$("input[name='a_ventasInactivo']").click(function(){
		
		if($("input[name='a_ventasInactivo']").prop("checked") && !$("input[name='a_ventasActivo']").prop("checked")){
		
		
			//	Operaciones en la función.
			var ver = $("input[name='a_ventas']:checked").val();
			var act = $("input[name='a_ventasInactivo']:checked").val();
			var mes = $("#aVentas").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aVentas";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if($("input[name='a_ventasInactivo']").prop("checked") && $("input[name='a_ventasActivo']").prop("checked")){
//			Operaciones en la función.
			var ver = $("input[name='a_ventas']:checked").val();
			
			var mes = $("#aVentas").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aVentas";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver ;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if(!$("input[name='a_ventasInactivo']").prop("checked")  && $("input[name='a_ventasActivo']").prop("checked")){
			//	Operaciones en la función.
			var ver = $("input[name='a_ventas']:checked").val();
			var act = $("input[name='a_ventasActivo']:checked").val();
			var mes = $("#aVentas").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aVentas";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}
		
	});
	
	$("input[name='a_inventActivo']").click(function(){
		
		if($("input[name='a_inventActivo']").prop("checked") && !$("input[name='a_inventInactivo']").prop("checked")){
			//	Operaciones en la función.		
			var ver = $("input[name='a_invent']:checked").val();
			var act = $("input[name='a_inventActivo']:checked").val();
			var mes = $("#aInvent").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aInvent";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if($("input[name='a_inventActivo']").prop("checked") && $("input[name='a_inventInactivo']").prop("checked")){
			//	Operaciones en la función.		
			var ver = $("input[name='a_invent']:checked").val();			
			var mes = $("#aInvent").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aInvent";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if(!$("input[name='a_inventActivo']").prop("checked")  && $("input[name='a_inventInactivo']").prop("checked")){
			//	Operaciones en la función.		
			var ver = $("input[name='a_invent']:checked").val();
			var act = $("input[name='a_inventInactivo']:checked").val();
			var mes = $("#aInvent").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aInvent";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}	
		}
		
	});
	
	$("input[name='a_inventInactivo']").click(function(){
		
		if($("input[name='a_inventInactivo']").prop("checked") && !$("input[name='a_inventActivo']").prop("checked")){
			//	Operaciones en la función.		
			var ver = $("input[name='a_invent']:checked").val();
			var act = $("input[name='a_inventInactivo']:checked").val();
			var mes = $("#aInvent").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aInvent";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if($("input[name='a_inventInactivo']").prop("checked") && $("input[name='a_inventActivo']").prop("checked")){
			//	Operaciones en la función.		
			var ver = $("input[name='a_invent']:checked").val();			
			var mes = $("#aInvent").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aInvent";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}
		}else if(!$("input[name='a_inventInactivo']").prop("checked")  && $("input[name='a_inventActivo']").prop("checked")){
			//	Operaciones en la función.		
			var ver = $("input[name='a_invent']:checked").val();
			var act = $("input[name='a_inventActivo']:checked").val();
			var mes = $("#aInvent").val();
			//alert(ver+"---"+mes);
			var e= document.getElementsByTagName("iframe"); 
			var idIfr = "#g_aInvent";
		    /*Armo un arreglo con todos los div's*/
			for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
				
					/*  tu acción */
					var srcFinal = "";
					var idF = "#"+e[i].id;
					//alert (idF);
			    	var src = $(idF).attr("src");//+"?medida="+med;
			    	var valSrc = src.indexOf('?');
			    	var st = 0;
			    	//alert(idF +'='+idIfr);
			    	if(idF == idIfr){
				    	if(valSrc != -1){
				    		//var srcNV = remplace_url_parm(src,"medida",med);
				    		var param_mes = "mes";
				    		var new_mes = mes;
				    		var param_ver = "ver";
				    		var new_ver = ver;
				    		var param_act = "act";
				    		var new_act = act;
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
						        if (name == param_ver){
						        	a_query[x] = param_ver+'='+new_ver;
						        	st = 1;
						        }
						        if (name == param_act){
						        	a_query[x] = param_act+'='+new_act;
						        	st = 1;
						        }
						    }
						    var srcNV = base + '?' + a_query.join('&');
				    		if(st == 0){
				    			srcFinal = srcNV + "&mes=" + mes + "&ver=" + ver + "&act=" + act;	
				    		}else{
				    			srcFinal = srcNV;
				    		}
				    		
				    	}else{
				    		srcFinal = src + "?mes=" + mes + "&ver=" + ver + "&act=" + act;
				    	}
				    	//alert("src "+srcFinal);
				    	window.parent.document.getElementById(e[i].id).src = srcFinal;
			    	}
			}	
		}
		
	});
	
	$(".meses").change(function() {//Evento click del boton paraq consultar segun los parametros selecconados
		var mes = $(this).val();
		//var x = "#g_"; 
		var idIfr = '#g_'+$(this).attr("id");
		//alert("ID. "+ idIfr);
	    
	    //Obtener URL 
		var e= document.getElementsByTagName("iframe"); 
		
	    /*Armo un arreglo con todos los div's*/
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			
				/*  tu acción */
				var srcFinal = "";
				var idF = "#"+e[i].id;
				//alert (idF);
		    	var src = $(idF).attr("src");//+"?medida="+med;
		    	var valSrc = src.indexOf('?');
		    	var st = 0;
		    	//alert(idF +'='+idIfr);
		    	if(idF == idIfr){
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
			    		
			    	}else{
			    		srcFinal = src + "?mes=" + mes;
			    	}
			    	//alert("src "+srcFinal);
			    	window.parent.document.getElementById(e[i].id).src = srcFinal;
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
//Variables que contendran la tabla en htlm
//String a_ventas = "";
//a_ventas = inv.getTableAnalisisVentas("1",id_user, id_modulo, "1", "7", "medida", "2011", "2012","", "id", "asc",false, true);

%>

<div id="rightnow">
    <h3 class="reallynow">
        <span>Previsión De Compra</span>        
        <br />
    </h3>
</div>
</br>
</br>

<div id="box">
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=4&cmp_id=id_marca&id_menu=1&id_Ifr=g_pIndicadores');" value="showMenu" id="menuPortelt_4"><span>Marca</span></button>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=4&cmp_id=id_producto&id_menu=2&id_Ifr=g_pIndicadores');" value="showMenu" id="menuPortelt_4"><span>Producto</span></button>
# Meses:
<select class=meses id=pIndicadores>
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
	<h3>Principales Indicadores</h3>
	<iframe style="width:1100px" height="400px" frameborder="0"  src="mod/charts/ic_princ_indicadores.jsp" class="pptosP" id="g_pIndicadores"></iframe>
</div>
</br>
</br>

<div id="box">
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=2&cmp_id=id_marca&id_menu=1&id_Ifr=g_aInvent');" value="showMenu" id="menuPortelt_1"><span>Marca</span></button>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=2&cmp_id=id_producto&id_menu=2&id_Ifr=g_aInvent');" value="showMenu" id="menuPortelt_5"><span>Producto</span></button>
<strong># Meses:</strong>
<select class=meses id=aInvent>
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
 <strong>Ver Datos Por: </strong>
<input type="radio" name="a_invent" id="rb_iProd" checked="checked" value="producto">Producto</input>
<input type="radio" name="a_invent" id="rb_iMarca" value="marca">Marca</input>
<strong>Mostrar Productos: </strong>
<input type="checkbox" name="a_inventActivo" id="rb_iProdA" value="1" checked>Activos</input>
<input type="checkbox" name="a_inventInactivo" id="rb_iProdI" value="0">Inactivos</input>

	<h3>Análisis De Inventario</h3>
	<iframe style="width:1100px" height="400px" frameborder="0"  src="mod/charts/ic_analisis_invent.jsp?mes=4&ver=producto&act=1" class="pptosP" id="g_aInvent"></iframe>
</div>
</br>
</br>

<div id="box">
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=5&cmp_id=id_marca&id_menu=1&id_Ifr=g_aComp');" value="showMenu" id="menuPortelt_5"><span>Marca</span></button>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=5&cmp_id=id_producto&id_menu=2&id_Ifr=g_aComp');" value="showMenu" id="menuPortelt_5"><span>Producto</span></button>
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
	<iframe style="width:1100px" height="400px" frameborder="0"  src="mod/charts/ic_analisis_compras.jsp" class="pptosP" id="g_aComp"></iframe>
</div>
</br>
</br>

<div id="box">
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=6&cmp_id=id_marca&id_menu=1&id_Ifr=g_aCompCjs');" value="showMenu" id="menuPortelt_6"><span>Marca</span></button>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=6&cmp_id=id_producto&id_menu=2&id_Ifr=g_aCompCjs');" value="showMenu" id="menuPortelt_6"><span>Producto</span></button>
# Meses:
<select class=meses id=aCompCjs>
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
	<h3>Análisis De Compras (Cajas)</h3>
	<iframe style="width:1100px" height="400px" frameborder="0"  src="mod/charts/ic_analisis_compras_cajas.jsp" class="pptosP" id="g_aCompCjs"></iframe>
</div>
</br>
</br>

<div id="box">
Filtros: 
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=1&cmp_id=id_marca&id_menu=1&id_Ifr=g_aVentas');" value="showMenu" id="menuPortelt_1"><span>Marca</span></button>
<button onclick="abrePop('mod/popup_ventas.jsp?id_portlet=1&cmp_id=id_producto&id_menu=2&id_Ifr=g_aVentas');" value="showMenu" id="menuPortelt_1"><span>Producto</span></button>
<strong>Meses a Mostrar:</strong>
<select class=meses id=aVentas>
	<option value=1>1</option>
	<option value=2>2</option>
	<option value=3 selected="selected">3</option>
	<option value=4>4</option>
	<option value=5>5</option>
	<option value=6>6</option>
	<option value=7>7</option>
	<option value=8>8</option>
	<option value=9>9</option>
	<option value=10>10</option>
	<option value=11>11</option>
	<option value=12>12</option>
</select>
 <strong>Ver Datos Por:</strong> 
<input type="radio" name="a_ventas" id="rb_vProd" checked="checked" value="producto">Producto</input>
<input type="radio" name="a_ventas" id="rb_vMarca" value="marca">Marca</input>
 <strong>Mostrar Productos:</strong>
<input type="checkbox" name="a_ventasActivo" id="rb_vProdA" value=1 checked>Activos</input>
<input type="checkbox" name="a_ventasInactivo" id="rb_vProdI" value=0>Inactivos</input>


	<h3>Análisis De Ventas</h3>
	<iframe style="width:1100px" height="400px" frameborder="0"  src="mod/charts/ic_analisis_ventas.jsp?mes=3&ver=producto&act=1" class="pptosP" id="g_aVentas"></iframe>
</div>

</body>
</html>