<%@ page language="java" contentType="text/html; charset=ISO-8859-1"

    pageEncoding="ISO-8859-1"%>

<%

    response.setContentType ("application/vnd.ms-excel"); //Tipo de fichero.

response.setHeader ("Content-Disposition", "attachment;filename=\"report.xls\"");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Insert title here</title>

</head>

<body>

<TABLE border="1">

<thead>
	<tr>
		<th class="header">Categoria</th>
		<th class="header">Facturacion</th>
		<th class="header">% Contribucion Facturacion </th>
		</tr>
</thead>
	<tbody>
		<tr>
			<td><a href="#" onclick="abrePop('popup.jsp?id_user=3&amp;id_modulo=2&amp;id_dashboard=1&amp;id_chart=6&amp;parametro=id_sop_core&amp;val_param=2&amp;val_org=CORE&amp;nom_org=Categoria');"><strong>+</strong></a>  <a href="parametros.jsp?valor=2&amp;parametro=id_sop_core&amp;chartID=6&amp;nombre=Categoria&amp;nomValue=CORE&amp;id_user=3&amp;id_dashboard=1&amp;popup=false" '="" target="hidden">CORE</a><a></a></td>
			<td class="tablrWidget_headerCell" align="right" valign="top">19,016,141</td>
			<td class="tablrWidget_headerCell" align="right" valign="top">50.6 %</td>
		</tr>
		<tr>
			<td><a href="#" onclick="abrePop('popup.jsp?id_user=3&amp;id_modulo=2&amp;id_dashboard=1&amp;id_chart=6&amp;parametro=id_sop_core&amp;val_param=3&amp;val_org=NO_CORE&amp;nom_org=Categoria');"><strong>+</strong></a>  <a href="parametros.jsp?valor=3&amp;parametro=id_sop_core&amp;chartID=6&amp;nombre=Categoria&amp;nomValue=NO_CORE&amp;id_user=3&amp;id_dashboard=1&amp;popup=false" '="" target="hidden">NO_CORE</a><a></a></td>
			<td class="tablrWidget_headerCell" align="right" valign="top">18,556,596</td>
			<td class="tablrWidget_headerCell" align="right" valign="top">49.4 %</td>
		</tr>
		<tr>
			<td><font color="red"><strong>Total</strong></font></td>
			<td class="tablrWidget_headerCell" align="right" valign="top">37,572,737</td>
			<td class="tablrWidget_headerCell" align="right" valign="top">100.0 %</td>
		</tr>
</tbody>
</TABLE>

</body>

</html>
