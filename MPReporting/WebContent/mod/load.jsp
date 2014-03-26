<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.sf.SourceFile" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
SourceFile sf = new SourceFile();
String file = request.getParameter("file");
String delFile = request.getParameter("delFile");
String fileMod = request.getParameter("fileMod");
String delFileMod = request.getParameter("delFileMod");
if(file != null){
	sf.runCommand(file);	
}
if(delFile != null){
	sf.eliminaArchivo(delFile);
}
if(fileMod != null){	
	sf.runCommandMod(fileMod);	
}
if(delFileMod != null){	
	sf.eliminaArchivoMod(delFileMod);
}
//System.out.println("Ajax"+file);
%>
</body>
</html>