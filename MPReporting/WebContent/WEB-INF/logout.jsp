<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<scrip>
<script type="text/javascript">
function a_onClick() {
    var retVal = confirm("Esta seguro que desea salir...");
   if( retVal == true ){
	   
	   <%session.removeAttribute("user");%>
	      window.location.reload();
   }
}
</script>

</scrip>

</head>
<body>

</body>
</html>