<div id="rightnow">
    <h3 class="reallynow">
        <span>PRESUPUESTO COMPARADO</span>
        <a href="#" class="add" onClick="abrePop('mod/charts/popup_total.jsp?delFil=true', '600', '800')">General</a>
        <br />
    </h3>
</div>
<br />
<!-- Medidas y Opciones fijas -->
<div id="menu-fondo-fijo"><p style="vertical-align:top">
	<div id="box">
		<h3>Medidas - Filtros</h3>
		<iframe style="width:1150px" height="150px"  frameborder="0" scrolling="no" src="mod/filtros.jsp?modulo=2" class="pptos" id="filtros"></iframe>
	</div>
</div>
<!--  -->
<br />
<div id="box">
	<h3>Cumplimiento Mensual</h3>
	<iframe style="width:735px" height="200px"  frameborder="0" scrolling="no" src="mod/charts/ppto_mensual.jsp?modulo=2" class="pptosP" id="mensual"></iframe>
</div>
<br />
<div id="box">
    <h3>Cumplimiento Mensual (Tabla)</h3>
    <iframe style="width:735px" height="200px" frameborder="0"  src="mod/charts/ppto_tbl_mensual.jsp?modulo=2" class="pptosP" id="tbl_mensual"></iframe>
</div>
<br />
<table border="0" cellspacing="0" cellpadding="0" style="width:735px">
  <tr>
    <td valign="top">
    <div id="box" style="width:355px">
	    <h3>Cumplimiento semestral</h3>
        <iframe  style="width:320px" height="200px"  frameborder="0" scrolling="no" src="mod/charts/ppto_semestral.jsp?modulo=2" class="pptosP" id="semestral"></iframe>
	</div>
    </td>
    <td valign="top">
    <div id="box" style="width:355px">
	    <h3>Cumplimiento trimestral</h3>
     <iframe style="width:320px" height="200px" frameborder="0" scrolling="no" src="mod/charts/ppto_trimestral.jsp?modulo=2" class="pptosP" id="trimestral"></iframe>
	</div>
    </td>
  </tr>
</table>

<br />

<div id="box">
	<h3>Cuenta Clave Ppto Orig</h3>
	<iframe style="width:735px" height="200px"  frameborder="0"  src="mod/charts/ppto_ctaclave_org.jsp?modulo=2" class="pptosP" id="cta_cve_ppto_org"></iframe>
</div>
<br />
<div id="box">
	<h3>Cuenta Clave Ppto Mod</h3>
	<iframe style="width:735px" height="200px"  frameborder="0"  src="mod/charts/ppto_ctaclave_mod.jsp?modulo=2" class="pptosP" id="cta_cve_ppto_mod"></iframe>
</div> 
<br />
<!-- -->  
<br/>
<div id="box">
    <h3>Categoría</h3>
    <iframe style="width:735px" height="200px" frameborder="0"  src="mod/charts/ppto_categoria.jsp?modulo=2" class="pptosP" id="categoria"></iframe>
</div>
<br/>
 <div id="box" >
    <h3>Departamento</h3> 
 	<iframe style="width:735px" height="200px" frameborder="0" src="mod/charts/ppto_departamento.jsp?modulo=2" class="pptosP" id="departamento"></iframe>
</div>
<br/>
<div id="box" >
    <h3>Marca</h3>
	<iframe style="width:735px" height="200px" frameborder="0" src="mod/charts/ppto_marca.jsp?modulo=2" class="pptosP" id="marca"></iframe>
</div>
<br/>
<!-- Iframe para la inserccioen de los filtros por portlets i recarga de los demas iframes para ke tomen dichos filtros -->
<iframe id="iframeHidden" name="hidden" src="mod/charts/parametros.jsp?id_user=1" style="width:0px; height:0px; border: 0px"  class="pptos"></iframe>



