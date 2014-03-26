<%@page import="com.auribox.html.LimpiaHTML"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="com.auribox.reporting.tools.Tools" %>
<%@include file="utils/FusionCharts/Code/JSP/Includes/FusionCharts.jsp" %>
<%
    response.setContentType("application/vnd.ms-excel;charset=UTF-8"); //Tipo de fichero.
    Tools pptos = new Tools();
    String id_user = (String) session.getAttribute("user");
    String id_modulo = (String) session.getAttribute("mod");
    String medida = request.getParameter("medida");
    String mesIni = request.getParameter("mesIni");
    String mesFin = request.getParameter("mesFin");
    String char_id = request.getParameter("char_id");
    String var = "";
    
    if (char_id.equals("1")) {              
        response.setHeader ("Content-Disposition", "attachment;filename=\"mensual.xls\"");
        var = pptos.getTablePptos("1", id_user, id_modulo, "1", "1", "medida", "2013", "2013", medida, "id", "asc", true, true);
        LimpiaHTML limpiaTabla = new LimpiaHTML(var);
        String tablaExcel = limpiaTabla.limpiaTablaHTML();
        out.println(tablaExcel);
    } else if (char_id.equals("6")) {
        response.setHeader ("Content-Disposition", "attachment;filename=\"categoria.xls\"");
        var = pptos.getTablePptos("1", id_user,id_modulo, "1", "6", "medida", "2013", "2013",medida, "id", "asc", true,true);
        LimpiaHTML limpiaTabla = new LimpiaHTML(var);
        String tablaExcel = limpiaTabla.limpiaTablaHTML();
        out.println(tablaExcel);
    }
    if (char_id.equals("7")) {
        response.setHeader ("Content-Disposition", "attachment;filename=\"departamento.xls\"");
        var = pptos.getTablePptos("1", id_user, id_modulo, "1", "7", "medida", "2013", "2013",medida, "id", "asc", true,true);
        LimpiaHTML limpiaTabla = new LimpiaHTML(var);
        String tablaExcel = limpiaTabla.limpiaTablaHTML();
        out.println(tablaExcel);
    }
    if (char_id.equals("8")) {
        response.setHeader ("Content-Disposition", "attachment;filename=\"marca.xls\"");
        var = pptos.getTablePptos("1", id_user,id_modulo, "1", "8", "medida", "2013", "2013",medida, "id", "asc", true,true);
        LimpiaHTML limpiaTabla = new LimpiaHTML(var);
        String tablaExcel = limpiaTabla.limpiaTablaHTML();
        out.println(tablaExcel);
    }
%>
