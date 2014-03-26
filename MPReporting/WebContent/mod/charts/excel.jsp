<%-- 
    Document   : excel
    Created on : 2/12/2013, 10:35:31 PM
    Author     : Jonathan
--%>

<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.File"%>
<%
        response.setContentType("application/vnd.ms-excel;charset=ISO-8859-1"); //Tipo de fichero.
        response.setHeader("Content-Disposition", "attachment;filename=\"Reporte.xls\"");

        String tabla = request.getParameter("tabla");
       
        out.println(new String(tabla.getBytes(),"ISO-8859-1"));

    

%>
