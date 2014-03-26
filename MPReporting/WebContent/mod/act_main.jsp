<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">
	//Sec_cod_param
	//alert("ALert");
	var src_ex = window.parent.$("#filtros").attr("src");//window.parent.$("iframeHidden").attr("src");
	alert(src_ex);
	window.parent.document.getElementById("filtros").src = src_ex;
	
	var e=window.parent.document.getElementsByTagName("iframe"); 
	/* Armo un arreglo con todos los div's*/
	alert(e.length);
	for(var i=0;i<e.length;i++){  /* Recorro todo los div's */
		if (e[i].className == 'pptosP'){
			/*  tu acción */
			var idF = "#"+e[i].id;
			//var idFnv = "#iframe_idDiv"+nFr;
	    	src = window.parent.$(idF).attr("src");
	    	//alert(idF+"--"+src);
	    	//if(idF != idFnv){
	    		var base = src.substr(0, src.indexOf('?'));
	    		base = base + "?modulo=2";
	    		window.parent.document.getElementById(e[i].id).src = base;
	    		//alert(base);
	    //	}
  		}
	}
	
	</script>
</body>
</html>