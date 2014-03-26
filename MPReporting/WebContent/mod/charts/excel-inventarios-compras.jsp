<%@page import="com.auribox.html.LimpiaHTML"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools"%>
<%@ page import="com.auribox.reporting.tools.ToolsInv"%>
<%@include file="utils/FusionCharts/Code/JSP/Includes/FusionCharts.jsp"%>
<%
	response.setContentType("application/vnd.ms-excel;charset=UTF-8"); //Tipo de fichero.
	response.setHeader("Content-Disposition",
			"attachment;filename=\"principales_indicadores.xls\"");
	String char_id = request.getParameter("char_id");
	String ver = request.getParameter("ver");
	String act = request.getParameter("act");
	ToolsInv inv = new ToolsInv();
	//Obtener ususario y modulo
	String id_user = (String) session.getAttribute("user");
	String id_modulo = (String) session.getAttribute("mod");
	String mes = request.getParameter("mes");

	String inDiasP1 = (String) request.getParameter("diasP1");
	String inDiasP2 = (String) request.getParameter("diasP1");
	String inDiasP3 = (String) request.getParameter("diasP2");
	String inCajasP1 = (String) request.getParameter("cajasP1");
	String inCajasP2 = (String) request.getParameter("cajasP1");
	String inCajasP3 = (String) request.getParameter("cajasP2");

	if (id_user == null || id_user.equals("null") || id_modulo == null
			|| id_modulo.equals("null")) {
		response.sendRedirect("login.jsp?error=s");
	}
	String id_customer = "1";
	//Variables que contendran la tabla en htlm
	String a_compras = "";
	System.out.println("mes..." + mes);
	String a_ventas = "";

	if (char_id.equals("4")) {
		a_ventas = inv.getTablePrinIndicadores("1", id_user, id_modulo,
				"1", "4", mes);
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_ventas);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	}
	 else if (char_id.equals("1")) {
			a_ventas = inv.getTableAnalisisVentas("1", id_user, id_modulo,
					"1", "1", mes, ver, act);
			LimpiaHTML limpiaTabla = new LimpiaHTML(a_ventas);
			String tablaExcel = limpiaTabla.limpiaTablaHTML();
			out.println(tablaExcel);
	}else if (char_id.equals("2")) {
		a_ventas = inv.getTableAnalisisInvent("1", id_user, id_modulo,
				"1", "2", mes, ver, act);
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_ventas);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	} else if (char_id.equals("5")) {

		a_compras = inv.getTableAnalisisCompras("1", id_user,
				id_modulo, "1", "5", mes);
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_compras);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	} else if (char_id.equals("6")) {
		a_compras = inv.getTableAnalisisComprasCajas("1", id_user,
				id_modulo, "1", "6", mes);
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_compras);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	}else if(char_id.equals("3")){
		String prod_no_desplaza = "";
		prod_no_desplaza = inv.getTableProdNoDesplaza("1",id_user, id_modulo, "1", "3", mes); 
		LimpiaHTML limpiaTabla = new LimpiaHTML(prod_no_desplaza);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	}else if(char_id.equals("7")){		
		a_compras = inv.getTableAnalisisCompras("1",id_user, id_modulo, "1", "7", mes);
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_compras);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	}else if(char_id.equals("8")){
		a_compras = inv.getTableAnalisisComprasCajas("1",id_user, id_modulo, "1", "8", mes);
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_compras);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	}else if(char_id.equals("9")){
		a_compras = inv.getTableAnalisisComprasSim("1",id_user, id_modulo, "1", "9", mes);
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_compras);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	}else if(char_id.equals("10")){
		a_compras = inv.getTableAnalisisComprasCajasSim("1",id_user, id_modulo, "1", "10", mes); 
		LimpiaHTML limpiaTabla = new LimpiaHTML(a_compras);
		String tablaExcel = limpiaTabla.limpiaTablaHTML();
		out.println(tablaExcel);
	}
%>
