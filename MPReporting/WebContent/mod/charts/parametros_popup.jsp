<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="Expires" content="-1"> 
<meta http-equiv="last modified" content="-1">
<title>Insert title here</title>
</head>
<body>
<%
System.out.println("Parametros");
Tools pptos = new Tools();
String valor = request.getParameter("valor");
String parametro = request.getParameter("parametro");
String chart_id=  request.getParameter("chartID");
String nombre = request.getParameter("nombre");
String nomValue = request.getParameter("nomValue");
String eliminar = request.getParameter("eliminar");
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String id_dashboard = request.getParameter("id_dashboard");
String id_customer = "1";
if(id_user == null || id_user == "" || id_user.equals("null")){
	%>
	<script language="javascript">
	parent.document.location = parent.document.location;
	</script>
	<%
}

if(id_customer != "" && id_customer != null && id_user != "" && id_user != null && id_modulo != "" && id_modulo != null
&& id_dashboard != "" && id_dashboard != null && chart_id != "" && chart_id != null && eliminar != "true" && eliminar != null){
	System.out.println("Eliminar..."+eliminar);
	pptos.borraFiltroPopup(id_customer, id_user, id_modulo, id_dashboard, chart_id);
	%>
	<script type="text/javascript">
	var srcFinal = "";
	var src = new String(parent.document.location);
	var valSrc = src.indexOf('?');
	var st = 0;
    	if(valSrc != -1){
    		var param_fil = "delFil";    	
    		var new_value = "false";
    		var base = src.substr(0, src.indexOf('?'));
		    var query = src.substr(src.indexOf('?')+1, src.length);
		    var a_query = query.split('&');
		    for(var x=0; x < a_query.length; x++){
		        var name = a_query[x].split('=')[0];
		        if (name == param_fil){
		        	a_query[x] = param_fil+'='+new_value;
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
    	}
    	parent.document.location = srcFinal;
	</script>
	<%
}
if(valor!=null && valor!="" && parametro != null && parametro != "" && chart_id != null && nombre != null && nomValue != "" &&
id_user != null && id_user != "" && id_dashboard != null && id_dashboard != "" && id_modulo != null && id_modulo != ""){
	pptos.insertaParametroPopup(id_customer,id_user, id_dashboard, id_modulo, chart_id, parametro, valor, nomValue); 

%>
	<script type="text/javascript">
	
	
	var srcFinal = "";
	var src = new String(parent.document.location);
	var valSrc = src.indexOf('?');
	var st = 0;
    	if(valSrc != -1){
    		var param_fil = "delFil";    	
    		var new_value = "false";
    		var base = src.substr(0, src.indexOf('?'));
		    var query = src.substr(src.indexOf('?')+1, src.length);
		    var a_query = query.split('&');
		    for(var x=0; x < a_query.length; x++){
		        var name = a_query[x].split('=')[0];
		        if (name == param_fil){
		        	a_query[x] = param_fil+'='+new_value;
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
    	}
    	parent.document.location = srcFinal;
	</script>
	<%} %>

</body>

</html>