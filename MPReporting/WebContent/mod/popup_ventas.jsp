<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui/js/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript">
//Busca dato en el select
$(document).ready(function(){
var dts = [];
var dts_src = new Array();
var cont = 0;
$("#filtros option").each(function(){
    var valor = $(this).val();
	var texto = $(this).text();
	//alert(valor+" - "+texto);
    if(cont==0){
        dts[texto] = valor;
        dts_src[cont] = texto;
    }else{
        dts[texto]=valor;
        dts_src[cont] = texto;
    }
    cont ++;    
});
//alert (dts_src);
$("input#autocomplete").autocomplete({ source: dts });  
$('input').autocomplete({
    search: function(event, ui) {
        $("#filtros option").each(function(){
        $(this).removeAttr('selected');
    });
    },
    source: dts_src 
}).data('autocomplete')._renderItem = function(ul, item) {

$("#filtros option").each(function(){
        var value = dts[item.value];
        
        if(value == $(this).val()){
            $(this).attr('selected', true);
        }
    });
};
});
/*Funcion que agrega filtros al segundo select y los elimina des primer select*/
function agregaFil(){
	  $("#filtros > :selected").each(function() {
			var valor = $(this).val();
			var texto = $(this).text();
			        
			$('#filtroMenu')
          .append($("<option></option>")
          .attr("value",valor)
          .text(texto));
			
			$("#filtros option[value="+valor+"]").remove();
      });
}
/*Funcion que elimina filtros del segundo select y los regresa al segundo selecet*/
function eliminaFil(){
	  $("#filtroMenu > :selected").each(function() {
			var valor = $(this).val();
			var texto = $(this).text();
			        
			$('#filtros')
          .append($("<option></option>")
          .attr("value",valor)
          .text(texto));
			
			$("#filtroMenu option[value="+valor+"]").remove();
      });
}
/*Funcion para agregar todas las opciones al segundo select*/
function agregaAllFil(){
	  $("#filtros option").each(function(){
		   //alert($(this).text()+'-'+ $(this).attr('value'))
		   var valor = $(this).val();
			var texto = $(this).text(); 
			
		   $('#filtroMenu')
         .append($("<option></option>")
         .attr("value",valor)
         .text(texto));
		   
		  // $("#mnuPortlet_"+variable+" option[value="+valor+"]").remove();
		});
	  $("#filtros option").remove();
}
/*Funcion para quitar todos los datos del segundo select*/
function eliminaAllFil(){
	  $("#filtroMenu option").each(function(){
		   //alert($(this).text()+'-'+ $(this).attr('value'))
		   var valor = $(this).val();
			var texto = $(this).text(); 
			
		   $('#filtros')
         .append($("<option></option>")
         .attr("value",valor)
         .text(texto));
		   
		  // $("#mnuPortlet_"+variable+" option[value="+valor+"]").remove();
		});
	  $("#filtroMenu option").remove();
}

/*Guardar filtros en la base de datos */
 function guardaFiltros(id_portlet,cmp_id, id_Ifrm){
	//var idF = "#"+id_Ifrm;
	//alert("Iframe: "+idF);
	 var elementos = "";// = new Array();
		var cont = 0;
		$("#filtroMenu option").each(function(){
	  		//alert($(this).text()+'-'+ $(this).attr('value'))
	  		//elementos[cont] = $(this).val();
	  		if(cont == 0){
	  			//alert(cont);
	  			elementos += $(this).val();
	  		}else{
	  			//alert(cont);
	  			elementos += ','+$(this).val();
	  		}
	  		
	   		//var valor = $(this).val();
			//var texto = $(this).text();
			cont = cont + 1;
			//alert (cont);
		});
		//alert (elementos);
		//Envio de datos para ser introducidos ala BD
		$.ajax({
			type : "POST",
			url : "menuFiltro.jsp",
			data : "elementos="+elementos+"&id_portlet="+id_portlet+"&cmp_id="+cmp_id,
			success: function(data){
				//alert("Enviado");
				//window.opener.location.reload();
				//var e=opener.document.getElementsByTagName("iframe");
				//alert("# de Iframes: "+e.length);
				//for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
					//var src = opener.document.getElementById(e[i].id).src;
					//alert(e[i].id);
					//alert(id_Ifrm);
					opener.document.getElementById(id_Ifrm).contentWindow.location.reload(true);
				//alert(src);
				//}
				//opener.document.location = opener.document.location; 
				window.close();
			}
		});
}
</script>
</head >
<body >
<%
ToolsInv inv = new ToolsInv();
String id_portlet = request.getParameter("id_portlet");
String id_menu = request.getParameter("id_menu");
String id_user = (String) session.getAttribute("user");
String id_dashboard = "1";//(String) session.getAttribute("id_dashboard");
String id_modulo = (String) session.getAttribute("mod");
String cmp_id = request.getParameter("cmp_id");
String id_Ifrm = request.getParameter("id_Ifr");
System.out.println("CMP _ ID "+cmp_id);
String fun = "guardaFiltros('"+id_portlet+"','"+cmp_id+"','"+id_Ifrm+"');";
%>
<div class="showMenu">
	<table>
		<tr>
			<td rowspan="4">
				Buscar: <input id="autocomplete"/><br><br>
				<select id="filtros" multiple="multiple" style="height: 250px; width: 275px;" >
					<%=inv.drawMenu("1", id_user, id_modulo, id_dashboard, id_portlet, id_menu, cmp_id) %>
				</select>
			</td>
			<td>
				<input type="button" value=">" style="width: 50px;" class="btnFil" onclick="agregaFil();"/>
			</td>
			<td rowspan=4>
				<br><br>
				<select id="filtroMenu" multiple="multiple" style="height:250px; width:275px;" >
					<%=inv.drawMenuFiltro("1", id_user, id_modulo, id_dashboard, id_portlet, id_menu, cmp_id)%>
									</select>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value=">>" style='width:50px' class=btnFil onclick="agregaAllFil();"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="<" style='width: 50px' onclick="eliminaFil();" />
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="<<" style='width:50px'  onclick="eliminaAllFil();"/>
			</td>
		</tr>
	</table>
	<input type="button" value="Aceptar" style="width:100px" align="right"  onclick=<%=fun %> />
</div>
</body>
</html>