package com.sf;
import  java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SourceFile {

	public String getPathFile(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("path_file_pres");
		//System.out.println("Path " +path);
		return path;
	}
	
	public String getPathFileMod(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("path_file_pres_mod");
		//System.out.println("Path " +path);
		return path;
	}
	/* Variables Comando */
	public String getPathKitchen(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("variable_kitchen");
		//System.out.println("Path " +path);
		return path;
	}
	
	public String getPathEtlPres(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("v_path_etl_pres");
		//System.out.println("Path " +path);
		return path;
	}
	
	public String getPathEtlPresModif(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("v_path_etl_pres_modif");
		//System.out.println("Path " +path);
		return path;
	}
	
	public String getNameEtlPres(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("v_file_job_pres");
		//System.out.println("Path " +path);
		return path;
	}
	
	public String getNameEtlPresModif(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("v_file_job_pres_modif");
		//System.out.println("Path " +path);
		return path;
	}
	
	public String getPathLogPres(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("v_path_log_pres");
		//System.out.println("Path " +path);
		return path;
	}
	
	public String getPathLogPresModif(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String path = resourceBundle.getString("v_path_log_pres_modif");
		//System.out.println("Path " +path);
		return path;
	}
	/*************************/
	
	public String getSO(){
		String dataSourceFile = "filesourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String so = resourceBundle.getString("sistema_operativo");
		//System.out.println("Path " +path);
		return so;
	}
	public String listFilesDir(String ruta) throws IOException{
		String lista = "<table border=1>";
		String dir_bsqr = ruta;//getPathFile();
		File dir = new File(dir_bsqr);
		int cont = 0;
		String nom_file = "";
		String[] ficheros = dir.list();
		//System.out.println(ficheros.length);
		if (ficheros == null)
		  System.out.println("No hay archivos en el directorio especificado");
		else {
		  for (int x=0;x<ficheros.length;x++){
			  String sFichero = "fichero.txt";
			  //File fichero = new File(sFichero);
			  /*BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero));
			  bw.write(ficheros[x]);
			  System.out.println("------");*/
			  nom_file = ficheros[x].replaceAll("[\n\r]","");
			  //nom_file = nom_file.replace("\n", " ");
			 // System.out.println(nom_file+"--"+nom_file.length());
			  String fun=(String) "cargaPresupuesto('"+nom_file+"')";
			  String funDel=(String) "eliminaArchivo('"+nom_file+"')";
			  if(nom_file.endsWith(".xls") || nom_file.endsWith(".xlxs")){
				 // System.out.println(fun);
			  	lista += "<tr>" +
			  				"<td>" +
			  					"<strong>"+nom_file+"</strong>" +
			  				"</td>" +
			  				"<td>" +
			  					//"<input type=button onclick=cargaPresupuesto('"+ficheros[x]+"') value=Cargar>" +
			  					"<input type=button onClick="+fun+" value=Cargar>" +
			  					"<input type=button onClick="+funDel+" value=Eliminar>" +
			  				"</td>" +
			  				"</tr>";
				  //System.out.println(ficheros[x]);
			  }
		  }  
		}
		lista += "</table>";
		return lista;
	}
	public String listFilesDirMod(String ruta) throws IOException{
		String lista = "<table border=1>";
		String dir_bsqr = ruta;//getPathFile();
		File dir = new File(dir_bsqr);
		int cont = 0;
		String nom_file = "";
		String[] ficheros = dir.list();
		//System.out.println(ficheros.length);
		if (ficheros == null)
		  System.out.println("No hay archivos en el directorio especificado");
		else {
		  for (int x=0;x<ficheros.length;x++){
			 // String sFichero = "fichero.txt";
			  //File fichero = new File(sFichero);
			  /*BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero));
			  bw.write(ficheros[x]);
			  System.out.println("------");*/
			  nom_file = ficheros[x].replaceAll("[\n\r]","");
			  //nom_file = nom_file.replace("\n", " ");
			 // System.out.println(nom_file+"--"+nom_file.length());
			  String fun=(String) "cargaPresupuestoMod('"+nom_file+"')";
			  String funDel=(String) "eliminaArchivoMod('"+nom_file+"')";
			  if(nom_file.endsWith(".xls") || nom_file.endsWith(".xlxs")){
				 // System.out.println(fun);
			  	lista += "<tr>" +
			  				"<td>" +
			  					"<strong>"+nom_file+"</strong>" +
			  				"</td>" +
			  				"<td>" +
			  					//"<input type=button onclick=cargaPresupuesto('"+ficheros[x]+"') value=Cargar>" +
			  					"<input type=button onClick="+fun+" value=Cargar>" +
			  					"<input type=button onClick="+funDel+" value=Eliminar>" +
			  				"</td>" +
			  				"</tr>";
				  //System.out.println(ficheros[x]);
			  }
		  }  
		}
		lista += "</table>";
		return lista;
	}
	public void runCommand(String file) throws IOException{
		String variable_kitchen = getPathKitchen();
		String variable_path_etl = getPathEtlPres();
		String variable_file_job = getNameEtlPres();
		String variable_path_log = getPathLogPres();
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String resultado = formato.format(fecha);
		//SimpleDateFormat f = new SimpleDateFormat("H:mm");
		//String hora = f.format(fecha);
		//String fechaFinal = resultado;
		System.out.println("formato de la fecha:" + resultado);
		
		String command = variable_kitchen+"/Kitchen.bat /file:"+variable_path_etl+"/"+variable_file_job +" /level:Basic > "+ variable_path_log+"/log_org_"+resultado+".log";
		System.out.println(command);
		String so = getSO();
		if(so.equals("linux")){
			Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", ""});
			System.out.println("Prueba Para EjecutarComando "+ file);
			
			System.out.println("Linux");
		}else if(so.equals("windows")){
			System.out.println("Windows");
			//Runtime.getRuntime().exec("notepad");
			//Process p = Runtime.getRuntime().exec("C:/data-integration/Kitchen.bat /file:C:/Pentaho/presupuestos/load_fact_presupuestossf.kjb /level:Basic > C:/Pentaho/presupuestos/logs/log_org%FECHA%.log");
			Process p=null;
			try { 
		        String linea;
		        p = Runtime.getRuntime().exec(command);
		        BufferedReader input = new BufferedReader (new InputStreamReader (p.getInputStream())); 
		        while ((linea = input.readLine()) != null) { 
		             System.out.println(linea); 
		        } 
		        input.close(); 
		    }catch (Exception err) { 
		           err.printStackTrace(); 
		    }
						//variable_kitchen/Kitchen.bat /file:variable_path_etl/variable_file_job /level:Basic > variable_path_log/log_%FECHA%.log
			 boolean no_exit = true;
	            while(no_exit){
	                try {
	                    p.exitValue();
	                    no_exit = false;
	                }catch(IllegalThreadStateException exception){
	                    no_exit=true;
	                    //System.out.println("El programa aun no finaliza");
	                }
	            }
		}
		//Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
		//System.out.println("Prueba Para EjecutarComando "+ file);
	}
	public void runCommandMod(String file) throws IOException{
		//String command = "ls -la";
		String variable_kitchen = getPathKitchen();
		String variable_path_etl = getPathEtlPresModif();
		String variable_file_job = getNameEtlPresModif();
		String variable_path_log = getPathLogPresModif();
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String resultado = formato.format(fecha);
		System.out.println("formato de la fecha:" + resultado);
		String command = variable_kitchen+"/Kitchen.bat /file:"+variable_path_etl+"/"+variable_file_job +" /level:Basic > "+ variable_path_log+"/log_modif_"+resultado+".log";
		System.out.println(command);
		
		
		String so = getSO();
		if(so.equals("linux")){
			Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", ""});
			System.out.println("Prueba Para EjecutarComando "+ file);
			
			System.out.println("Linux");
		}else if(so.equals("windows")){
			System.out.println("Windows");
			//Runtime.getRuntime().exec("notepad");
			//Runtime.getRuntime().exec("C:/data-integration/Kitchen.bat /file:C:/Pentaho/presupuestosmodif/load_fact_presupuestosprueba.kjb /level:Basic > C:/Pentaho/presupuestosmodif/logs/log_modif%FECHA%.log");
			Process p=null;
			try { 
		        String linea;
		        p = Runtime.getRuntime().exec(command);
		        BufferedReader input = new BufferedReader (new InputStreamReader (p.getInputStream())); 
		        while ((linea = input.readLine()) != null) { 
		             System.out.println(linea); 
		         } 
		           input.close(); 
		    }catch (Exception err) { 
		           err.printStackTrace(); 
		    } 
			//variable_kitchen/Kitchen.bat /file:variable_path_etl/variable_file_job /level:Basic > variable_path_log/log_%FECHA%.log
			boolean no_exit = true;
			    while(no_exit){
			        try {
			            p.exitValue();
			            no_exit = false;
			        }catch(IllegalThreadStateException exception){
			            no_exit=true;
			            //System.out.println("El programa aun no finaliza");
			        }
			    }
		}
		//Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
		//System.out.println("Prueba Para EjecutarComando "+ file);
	}
	public void eliminaArchivo(String file){
		String prb = getPathFile()+"/"+file; 
		File fichero = new File(prb);
		System.out.println("Eli "+fichero);
		//fichero.delete();
		if (fichero.delete()){
			System.out.println("El Archivo ha sido borrado satisfactoriamente");
		}else{
			System.out.println("El Archivo no puede ser borrado");
		}
	}
	public void eliminaArchivoMod(String file){
		String prb = getPathFileMod()+"/"+file; 
		System.out.println("Mod "+prb);
		File fichero = new File(prb);
		System.out.println("Eli "+fichero);
		//fichero.delete();
		if (fichero.delete()){
			System.out.println("El Archivo ha sido borrado satisfactoriamente");
		}else{
			System.out.println("El Archivo no puede ser borrado");
		}
	}
}
