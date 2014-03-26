<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="java.util.*"
	import="org.apache.commons.fileupload.*"
	import="org.apache.commons.fileupload.servlet.*"
	import="org.apache.commons.fileupload.disk.*" import="java.io.*"%>
<%@ page import="com.sf.SourceFile"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="JavaScript" src="js/jquery-ui/js/jquery-1.7.1.min.js"></script>
<script>
	function comprueba_extension(formulario, archivo) {
		//alert(formulario + " "+ archivo);
		extensiones_permitidas = new Array(".xls", ".xlxs");
		mierror = "";
		if (!archivo) {
			//Si no tengo archivo, es que no se ha seleccionado un archivo en el formulario 
			mierror = "No has seleccionado ningún archivo";
		} else {
			//recupero la extensión de este nombre de archivo 
			extension = (archivo.substring(archivo.lastIndexOf(".")))
					.toLowerCase();
			nombre = (archivo.substring(archivo.lastIndexOf("/"), archivo
					.lastIndexOf("."))).toLowerCase();
			nom = "presupuesto";
			//alert (nombre);
			//alert (nombre.indexOf(nom) != -1);
			//compruebo si la extensión está entre las permitidas 
			permitida = false;
			for ( var i = 0; i < extensiones_permitidas.length; i++) {
				if (extensiones_permitidas[i] == extension) {
					permitida = true;
					break;
				}
			}
			if (!permitida) {
				mierror = "Solo se Aceptan Archivos con Extension: "
						+ extensiones_permitidas.join();
			} else if (!(nombre.indexOf(nom) != -1)) {
				mierror = "El Nombre del Archivo Debe Incluir la Palabra : presupuesto";
			} else {
				formulario.submit();
				
				return 1;
			}
		}
		//si estoy aqui es que no se ha podido submitir 
		alert(mierror);
		return 0;
	}
	function cargaPresupuesto(archivo) {
		//var file = new String(archivo);
		//alert ("Carga: "+ archivo);
		$("#imgProgress").show();
		$.ajax({
			type : "POST",
			url : "mod/load.jsp",
			data : "file=" + archivo,
			success : function(data) {
				//$("#portlet").html(data);
				$("#imgProgress").hide();
				//window.opener.location.reload();
				window.location = 'index.jsp?mod=pptos&submod=ppto_org';
				alert("Carga Correcta...");
			}
		});
	}

	function eliminaArchivo(archivo) {
		//var file = new String(archivo);
		//alert ("Carga: "+ archivo);
		$.ajax({
			type : "POST",
			url : "mod/load.jsp",
			data : "delFile=" + archivo,
			success : function(data) {
				//$("#portlet").html(data);
				alert("Archivo eliminado...");
				//window.opener.location.reload();
				window.location = 'index.jsp?mod=pptos&submod=ppto_org';
			}
		});

	}
</script>
</head>
<%
	SourceFile sf = new SourceFile();
	String path = sf.getPathFile();
	System.out.println("Ruta: " + path);
%>
<body>
	<h3>CARGA DE DATOS PARA PRESUPUESTO ORIGINAL</h3>
	<!--  <div style="width:415px ; height:50px ;" align="center"> -->
	<form method=post action=# enctype="multipart/form-data">
		Archivo Presupuestos: <input type=file name="archivoupload"><br>
		<input type=button name="Submit" value="Enviar"
			onclick="comprueba_extension(this.form, this.form.archivoupload.value)"><br>
	</form>
	<!--  </div> -->
	<!-- codigo para subir el fichero al servidor-->
	<%
		//Ruta donde se guardara el fichero
		File destino = new File(path); //("C:/prueba_excel");
		if (destino.exists()) {
			ServletRequestContext src = new ServletRequestContext(request);
			System.out.println(src);
			if (ServletFileUpload.isMultipartContent(src)) {
				DiskFileItemFactory factory = new DiskFileItemFactory(
						(1024 * 1024), destino);
				//System.out.println("F "+factory);

				ServletFileUpload upload = new ServletFileUpload(factory);
				//System.out.println("U "+ upload);
				java.util.List lista = upload.parseRequest(src);
				File file = null;
				java.util.Iterator it = lista.iterator();

				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();
					String isFile = path + "/" + item.getName();
					System.out.println(isFile);

					File fichero = new File(isFile);
					if (fichero.exists()) {

						if (item.isFormField())
							out.println(item.getFieldName() + "<br>");
						else {
							file = new File(item.getName());
							//					System.out.println(file);

							out.println("Ya existe un archivo con el mismo nombre...");

							sf.listFilesDir(path);
						} // end if
					} else {
						file = new File(item.getName()); ///------
						item.write(new File(destino, file.getName()));
						//out.println("Fichero subido");
					}
				} // end while
			} // end if
		} else {
			out.println("El directorio no existe...");
		}
	%>
	<div id="imgProgress" style="display: none;">
		<img src="img/ajax-loader.gif" />
	</div>
	<%=sf.listFilesDir(path)%>

</body>
</html>