<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<%
Tools pptos = new Tools();
String id_modulo = "";
String module = request.getParameter("mod");
String submodule = request.getParameter("submod");
String id_user = (String)session.getAttribute("user");//"1";//(String)session.gerAtributte("id_uder");
String chk_id_mod = request.getParameter("idmod");//(String)session.getAttribute("mod");// Verifica que haya una sesion creada, sino es asi se creara

if(chk_id_mod == null || chk_id_mod.equals("null")){
	id_modulo = (String)session.getAttribute("mod");
}else{
	id_modulo = chk_id_mod;
}

//Estatus sobre el grupo y combinacion de medidas
boolean sts_cmb = pptos.getStatusCmb_G(id_user, id_modulo, "1");
boolean sts_gpo = pptos.getStatusGpo_G(id_user, id_modulo, "1");
boolean sts_iprt = pptos.getStatusGpo_G(id_user, id_modulo, "1");
//Verifica que el usuario tenga creado sus cuenta clave segun el modulo
boolean cta_cve = pptos.getExistCC("1",id_user, id_modulo, "1");
boolean per_imprt = false;
if(id_user == null || id_user ==("null") || id_user.isEmpty()
	|| id_modulo == null || id_modulo.equals("null") || id_modulo.isEmpty()){
	System.out.println("Redireccionar...");
	response.sendRedirect("login.jsp?error=s");
	return;
}
if(id_user != null && id_user != "" && !id_user.equals("null") && 
	id_modulo != null && id_modulo!="" && !id_modulo.equals("null")){
	if(!sts_cmb || !sts_gpo){
		pptos.insertaMedidasGpo(id_user, "1", id_modulo, "1", "1,2,3");
	}
	if(!cta_cve){
		pptos.insertaFiltroPorUsuario("1",id_user,id_modulo,"1");
	}
	//Checa Los Permisos Para el Usuario
	per_imprt = pptos.stsImporte(id_user);
	if(per_imprt){
		pptos.setPermisos(id_user, "1", id_modulo);
		System.out.println(" Sin Permiso Para Importe");
	}
			
	session.setAttribute("mod", id_modulo);
}
else{
	
	response.sendRedirect("login.jsp?error=s");
	System.out.println("Redirect");
	return;
}
String filemodule = Tools.getModuleFile(module, submodule, id_user);
String menusubmodule = "";
String menusubmodulerigth = "";
String cc = pptos.getPerCC(id_user);
if(cc == "" || cc == null){// Condicion para admines --> Muestra submodulos para carga de archivos...
	menusubmodule = Tools.getMenuSubmodule(module, submodule, id_user);
	menusubmodulerigth = Tools.getMenuRigthSubmodule(module, submodule, id_user);
}else{// Solo muestra cumplieentos globales para cuntas claves...
	menusubmodule = Tools.getMenuSubmoduleCtaCve(module, submodule, id_user);
	menusubmodulerigth = Tools.getMenuRigthSubmoduleGlobales(module, submodule, id_user);
}
if(id_modulo != null ){
	System.out.println("id_modulo_4 "+id_modulo);
	if(id_modulo.equals("4")){
		System.out.println("Modulo Inventarios: "+module);
		menusubmodule = Tools.getMenuSubmodule(module, submodule, id_user);
		menusubmodulerigth = Tools.getMenuRigthSubmoduleGlobales(module, submodule, id_user);
	}
}else{
	response.sendRedirect("login.jsp?error=s");
	System.out.println("Redirect 4");
	return;
}
String c_class = "content";
if(module.equals("invent")){
	c_class = "content_ex";
}
System.out.println("Clase Final: "+c_class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>BI-Reporting</title>
<!-- <link rel="stylesheet" type="text/css" href="css/theme.css" /> -->
<link rel="stylesheet" type="text/css" href="css/style.css" />

<link href="jquery-ui/css/redmond/jquery-ui-1.10.3.custom.css" rel="stylesheet">
<script src="jquery-ui/js/jquery-1.9.1.js"></script>
<script src="jquery-ui/js/jquery-ui-1.10.3.custom.js"></script>

<script>
   var StyleFile = "theme" + document.cookie.charAt(6) + ".css";
   document.writeln('<link rel="stylesheet" type="text/css" href="css/theme1.css">'); 
   /*Funcion que abre pupup */
   function abrePop(path){
 	  //alert(path);
 	window.open(path , "ventana1" , "width=1000,height=520,scrollbars=YES"); 
 	}
   
   
</script>
<style type="text/css">
#menu-fondo-fijo {
	width:100% ; height:40px ; position:fixed ; bottom:0 ;
	right:0 ; left:0 ; overflow:auto ; background-color:white ; text-align:left ;
	-moz-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border:1px solid #000000;
	transition:500ms;
}
#menu-fondo-fijo:hover{
	height:150px;
	border-shadow:0px -50px 5px #888;
	
}
</style>

<!--[if IE]>
<link rel="stylesheet" type="text/css" href="css/ie-sucks.css" />
<![endif]-->
</head>

<body>
<input type="hidden" name="hiddenElement"  id="hiddenElement" value="English">
<%
//String user = (String)session.getAttribute("user");

%>
	<div id="container">
        <div id="header">
            <h2>BI-Reporting: Productos Marcopolo</h2>
            <div id="topmenu">
                <ul>
                    <li><a href="#">Dashboards</a></li>
                    <li><a href="#">Ventas</a></li>
                    <li><a href="?mod=invent&idmod=4">Inventarios/Compras</a></li>
                    <!-- <li><a href="#">Inventarios/Compras</a></li>  -->
                    <li><a href="?mod=pptos&idmod=2">Presupuestos</a></li>
                    <!-- <li><a href="?mod=pptos&idmod=3">Pttos-Fact</a></li> -->
                    <li><a href="#">Estadísticas</a></li>
                    <li><a href="#">Configuración</a></li>
                    <li><a href="logout.jsp">Salir</a></li>
                </ul>
                 
            </div>
        </div>
      
      <div id="wrapper">
      
			<div id="panel">
      		<%=menusubmodule %>
      		</div>
      
        <div id=<%=c_class %>>
		<jsp:include page="<%=filemodule%>" />
        </div>
<div id="menu-der-fijo"><p style="horizontal-align:top">
<%
System.out.println("Modulo derecho: "+menusubmodulerigth);
if(!menusubmodulerigth.isEmpty()){ 
System.out.println("Modulo derecho:----- "+menusubmodulerigth);
%>
        <div id="sidebar">
<%} %>
        	<%=menusubmodulerigth %>
<%if(menusubmodulerigth != null || menusubmodulerigth != ""){ %>
        </div>
<%} %>
</div>
      </div>
        <div id="footer">
        <div id="credits">&nbsp;</div>
        <div id="styleswitcher">
        Desarrollado por <strong>Auribox Consulting</strong> <a href="http://www.auriboxconsulting.com">www.auriboxconsulting.com </a>
        </div><br />

        </div>
</div>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
<p></p>
</body>
</html>    