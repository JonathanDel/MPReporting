<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
String table = request.getParameter("table");
System.out.println("Param "+request.getParameter("table"));
String file_name = request.getParameter("file_name");
System.out.println("Tabla: "+table);
System.out.println("File Name : "+file_name);
response.setContentType ("application/vnd.ms-excel"); //Tipo de fichero.
response.setHeader ("Content-Disposition", "attachment;filename=\"report.xls\"");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Export Table to Excel</title>
</head>
<body>
<table border="1">
<%=table %>
</table>
</body>
</html>