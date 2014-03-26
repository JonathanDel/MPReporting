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
String popup = request.getParameter("popup");
String id_user = (String) session.getAttribute("user");
String id_modulo = (String) session.getAttribute("mod");
String id_dashboard = request.getParameter("id_dashboard");
String id_customer = "1";
//String id_modulo= "2";
if(id_user == null || id_user == "" || id_user.equals("null")){
	%>
	<script language="javascript">
	parent.document.location = parent.document.location;
	</script>
	<%
}
if(valor!=null && valor!="" && parametro != null && parametro != "" && chart_id != null && nombre != null && nomValue != "" &&
id_user != null && id_user != "" && id_dashboard != null && id_dashboard != "" && id_modulo != null && id_modulo != ""){
	pptos.insertaParametros(id_customer, id_user, id_dashboard, id_modulo, chart_id,
			valor, parametro, nombre, nomValue);
	System.out.println("Popup: "+popup);
	if(popup.equals("true")){
		System.out.println("Popup_True: "+popup);
	%>
		<script type="text/javascript">
		var src_ex = parent.window.opener.parent.document.getElementById("filtros").src;
		parent.window.opener.parent.document.getElementById("filtros").src = src_ex;
		var e = parent.window.opener.parent.document.getElementsByTagName("iframe");
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			if (e[i].className == 'pptosP'){
				/*  tu acción */
				var idF = e[i].id;
		    	src = parent.window.opener.parent.document.getElementById(idF).src;
		    		var base = src.substr(0, src.indexOf('?'));
		    		base = base + "?modulo=2";
		    		parent.window.opener.parent.document.getElementById(e[i].id).src = base;
	  		}
		}
		</script>
	<%
	}else{
		System.out.println("Popup_False: "+popup);
	%>
		<script type="text/javascript">
		var src_ex = window.parent.$("#filtros").attr("src");
		window.parent.document.getElementById("filtros").src = src_ex;
		var e=window.parent.document.getElementsByTagName("iframe"); 
		/* Armo un arreglo con todos los div's*/
		for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
			if (e[i].className == 'pptosP'){
				/*  tu acción */
				var idF = "#"+e[i].id;
		    	src = window.parent.$(idF).attr("src");
		    		var base = src.substr(0, src.indexOf('?'));
		    		base = base + "?modulo=2";
		    		window.parent.document.getElementById(e[i].id).src = base;
	  		}
		}
		</script>
	<%
	}
}
%>
</body>

</html>