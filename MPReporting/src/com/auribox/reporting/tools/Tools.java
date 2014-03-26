package com.auribox.reporting.tools;

import java.awt.geom.CubicCurve2D;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.db.DataSource;
import com.db.dao.Presupuestos;
import com.mysql.jdbc.SQLError;

public class Tools {
	
	JdbcTemplate jdbcTemplate;
	JdbcTemplate jdbcTemplateAdmin;
	Presupuestos pptos = new Presupuestos();
	public Tools(){
		DataSource ds = new DataSource();
		jdbcTemplate = ds.getDataSource();
		jdbcTemplateAdmin = ds.getDataSourceAdmin();
	}
	
	public static String getModuleFile(String module, String submodule, String id_user){
		String filemodule="";
		if( module==null || module.equals("main")){
			filemodule="mod/main.jsp";
		}
		else if(module!=null && module.equals("pptos" )){ //Modulo Presupuestos y Submodulos
			if(submodule==null){
				filemodule="mod/pptos.jsp";
			}
			else if(submodule.equals("ppto_org")){
				filemodule="mod/upload_file.jsp";
			}
			else if(submodule.equals("ppto_mod")){
				filemodule="mod/upload_file_mod.jsp";
			}
			else if(submodule.equals("ppto_cmp")){
				filemodule="mod/pptos.jsp";
			}
			else if(submodule.equals("fact_ttl")){
				filemodule="mod/fact.jsp";
			}
		}else if(module!=null && module.equals("invent" )){ //Nuevo Modulo de
			if(submodule==null){
				filemodule="mod/p_compra.jsp";
			}
			else if(submodule.equals("p_compra")){
				filemodule="mod/p_compra.jsp";
			}/*
			else if(submodule.equals("a_venta")){
				filemodule="mod/a_venta.jsp";
			}*/
			else if(submodule.equals("p_insum")){
				filemodule="mod/p_insumos.jsp";
			}
			else if(submodule.equals("p_no_dspz")){
				filemodule="mod/prod_no_desplaza.jsp";
			}
			else if(submodule.equals("p_sim")){
				filemodule="mod/p_simulacion.jsp";
			}
		}
		return filemodule;
	}
	
	public static String getMenuSubmodule(String module, String submodule, String id_user){
		String submodules = ""; 
		if(module != null){
			if(module.equals("pptos")){
				submodules = getSubModulesPptos(id_user);
			}else if(module.equals("invent")){
				submodules = getSubModulesInvent(id_user);
			}
		}
		return submodules;
	}
	public static String getMenuSubmoduleCtaCve(String module, String submodule, String id_user){
		String submodules = ""; 
		if(module != null){
			if(module.equals("pptos")){
				submodules = getSubModulesPptosCtaCve(id_user);
			}else if(module.equals("invent")){
				submodules = getSubModulesInvent(id_user);
			}
		}
		return submodules;
	}
	public static String getMenuRigthSubmodule(String module, String submodule, String id_user){
		String submodules = ""; 
		if(module != null){
			if(module.equals("pptos")){
				submodules = getSubModulesRigthPptos(id_user);
			}
		}
		return submodules;
	}
	public static String getMenuRigthSubmoduleGlobales(String module, String submodule, String id_user){
		String submodules = ""; 
		if(module != null){
			if(module.equals("pptos")){
				submodules = getSubModulesRigthPptosGlobales(id_user);
			}
			/*if(module.equals("invent")){
				submodules = getSubModulesRigthInvent(id_user);
			}*/
		}
		return submodules;
	}
	public static String getSubModulesPptos(String id_user){
		String submodules = 
				"<div id=top-panel>" +
					"<div id=panel>" +
						"<ul>" +
							"<li><a href=?mod=pptos&idmod=2&submod=ppto_cmp class=report>Presupuesto Comparado</a></li>"+
							"<li><a href=?mod=pptos&idmod=3&submod=fact_ttl class=report>Facturacion Total</a></li>"+
							"<li><a href=?mod=pptos&submod=ppto_org class=report>Ppto Original</a></li>"+
							"<li><a href=?mod=pptos&submod=ppto_mod class=report>Ppto Modificado</a></li>"+
						"</ul>"+
				"</div>";
		
		return submodules;
		
	}
	public static String getSubModulesPptosCtaCve(String id_user){
		String submodules = 
				"<div id=top-panel>" +
					"<div id=panel>" +
						"<ul>" +
							"<li><a href=?mod=pptos&idmod=2&submod=ppto_cmp class=report>Presupuesto Comparado</a></li>"+
							"<li><a href=?mod=pptos&idmod=3&submod=fact_ttl class=report>Facturacion Total</a></li>"+
						"</ul>"+
				"</div>";
		
		return submodules;
		
	}
	public static String getSubModulesInvent(String id_user){
		String submodules = 
				"<div id=top-panel>" +
					"<div id=panel>" +
						"<ul>" +
							"<li><a href=?mod=invent&submod=p_compra class=report>Prevision De Compras</a></li>"+
							/*"<li><a href=?mod=invent&submod=a_venta class=report>Analisis De Ventas</a></li>"+
							"<li><a href=?mod=invent&submod=a_compra class=report>Analisis De Compras</a></li>"+*/ 
							"<li><a href=?mod=invent&submod=p_no_dspz class=report>Productos De Bajo Desplazamiento</a></li>"+
							"<li><a href=?mod=invent&submod=p_sim class=report>Simulación</a></li>"+
							"<li><a href=?mod=invent&submod=p_insum class=report>Insumos</a></li>"+
						"</ul>"+
				"</div>";
		
		return submodules;
		
	}
	public static String getSubModulesRigthPptos(String id_user){
		
		String submodules = 
				"<ul>"+
					"<li><h3><a href=?mod=pptos class=house>Presupuestos</a></h3>"+
						"<ul>" +
							"<li><a href=?mod=pptos&idmod=2&submod=ppto_cmp class=report>Presupuesto Comparado</a></li>"+
							"<li><a href=?mod=pptos&idmod=3&submod=fact_ttl class=report>Facturacion Total</a></li>"+
							"<li><a href=?mod=pptos&submod=ppto_org class=report>Ppto Original</a></li>"+
							"<li><a href=?mod=pptos&submod=ppto_mod class=report>Ppto Modificado</a></li>"+
						"</ul>"+
					"</li>"+
					"<li>"+
					"<h3>"+
						"<a href=# class=manage>% Cump. Global Ptto Org</a></h3>"+
						"<iframe src='mod/charts/cump_global_ptto_org.jsp?modulo=2' style='width:170px; height:350px;' class='pptosP' id='cmp_global_ptto' frameborder=0 scrolling=no></iframe>"+
					"<br>"+
					"<h3>"+
						"<a href=# class=manage>% Cump. Global Ptto Mod</a></h3>"+
						"<iframe src='mod/charts/cump_global_ptto_mod.jsp?modulo=2' style='width:170px; height:350px;' class='pptosP' id='cmp_global_mod' frameborder=0 scrolling=no></iframe>"+
					"<li>"+
				"</ul>";
						
		return submodules;
		
	}
	public static String getSubModulesRigthInvent(String id_user){
		
		String submodules = 
				"<ul>"+
					"<li><h3><a href=?mod=invent class=house>Inventarios</a></h3>"+
						"<ul>" +
							"<li><a href=?mod=invent&submod=p_compra class=report>Prevision de Compra</a></li>"+
							"<li><a href=?mod=invent&submod=a_venta class=report>Analisis de Venta</a></li>"+
							"<li><a href=?mod=invent&submod=a_compra class=report>Analisis de Compra</a></li>"+
						"</ul>"+
					"</li>"+
				"</ul>";
						
		return submodules;
		
	}
	public static String getSubModulesRigthPptosGlobales(String id_user){
		
		String submodules = 
				"<ul>"+
						"<li><a href=?mod=pptos&idmod=2&submod=ppto_cmp class=report>Presupuesto Comparado</a></li>"+
						"<li><a href=?mod=pptos&idmod=3&submod=fact_ttl class=report>Facturacion Total</a></li>"+
					"<li>"+
					"<h3>"+
						"<a href=# class=manage>% Cump. Global Ptto Org</a></h3>"+
						"<iframe src='mod/charts/cump_global_ptto_org.jsp?modulo=2' style='width:170px; height:350px;' class='pptosP' id='cmp_global_ptto' frameborder=0 scrolling=no></iframe>"+
					"<br>"+
					"<h3>"+
						"<a href=# class=manage>% Cump. Global Ptto Mod</a></h3>"+
						"<iframe src='mod/charts/cump_global_ptto_mod.jsp?modulo=2' style='width:170px; height:350px;' class='pptosP' id='cmp_global_mod' frameborder=0 scrolling=no></iframe>"+
					"<li>"+
				"</ul>";
						
		return submodules;
		
	}
	public String getChartPpts(
			String cust_id,
			String id_user,
			String id_dashboard,
			String chart_id,
			String medida,
			String anioIni,
			String anioFin,
			String gpoMed,
			String orden,
			String ordenMenu,
			String id_modulo,
			boolean filtros){
		String xml = "";
		HashMap<String, String> dtPag = new HashMap<String, String>();
		//Configuracion del portlet
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id, id_modulo);// Configuracon de portlet por modulo...
		String caption = (String) hm.get("caption");
		String xAxisName = (String) hm.get("xAxisName");
		String yAxisName = (String) hm.get("yAxisName");
		String numberPrefix = (String) hm.get("numberPrefix");
		String tbl_dim = (String) hm.get("tbl_dim");
		String cmp_filtro = (String) hm.get("cmp_id");
		String tipo_orden = "";
		/*if(ordenMenu == null){
			////System.out.println(hm.get("tipo_orden"));
			tipo_orden = (String) hm.get("tipo_orden");
		}else{
			tipo_orden = ordenMenu;
		}*/
		System.out.println("año -- > ---- > ------------> "+anioIni+ "  "+anioFin);
		HashMap head = pptos.getDataHead(chart_id, cust_id, anioIni, anioFin,
				tipo_orden, orden, id_user, id_dashboard, id_modulo, filtros); 
		HashMap body = pptos.getDataBody(chart_id, cust_id, anioIni, anioFin, id_user, id_dashboard, gpoMed, id_modulo, filtros);
		SortedSet<Integer> sort = new TreeSet<Integer>(head.keySet());
		Iterator it = sort.iterator();

		ArrayList<String> dimId = new ArrayList<String>();
		ArrayList<String> dimIDV = new ArrayList<String>();
		// Paginacion
		// Obener numero de paginas
		int cont = 0;
		//int contPag = 1;
		//int datosXPag = 10;
		//if(chart_id == "1"){
			//datosXPag = 12;
		//}
		//int tamTotalDatos = head.size();
		//int totalPag = (int) Math.floor((tamTotalDatos - 1) / datosXPag) + 1;
		//this.setNumTotalPag(totalPag);
		//String numPag = "1";
		////System.out.println("Tamano: " + totalPag);
		xml += "<chart "+//caption='" + caption +// "' xAxisName='" + xAxisName
				//+ "' yAxisName='" + yAxisName + 
				" numberPrefix='' showValues='' formatNumberScale='' sFormatNumberScale='' bgColor='FFFFFF' bgAlpha='0' showBorder='0'  palette='2'>";
		xml += "<categories>";

		while (it.hasNext()) {
			Object clave = it.next();
			Object valor = head.get(clave);
			//if (cont == datosXPag) {
				//contPag++;
				//cont = 0;
			//}
			//System.out.println(valor);
			String dimNombre = pptos.getDimName(chart_id, cust_id,(String) valor, id_modulo);// Se cambio (String) valor por cve
			//System.out.println(dimNombre);
			dimId.add((String) valor);
			//System.out.println("put: "+"medida_" + contPag + "_" + (String) valor + "_");
			dtPag.put("medida_" + (String) valor + "_",dimNombre);
			////System.out.println("medida_" + contPag + "_" + (String) valor + "_"+ "," + dimNombre);

			cont++;
		}

		// /////////////////////////
		SortedSet<String> sortP = new TreeSet<String>(dtPag.keySet());
		Iterator itr = sortP.iterator();
		HashMap<String, String> nomLabel = new HashMap<String, String>();
		String nom = null;
		//System.out.println("Tamanioo: "+dimId.size());
		for (int i = 0; i < dimId.size(); i++) {
			//System.out.println("medida_" + numPag + "_" + dimId.get(i) + "_");
			if (dtPag.get("medida_"+ dimId.get(i) + "_") != null) {
				////System.out.println("medida_"+numPag+"_"+dimId.get(i)+"_");
				nom = (String) dtPag.get("medida_" + dimId.get(i) + "_");
				//System.out.println(dimId.get(i)+","+ nom);
				dimIDV.add(dimId.get(i));
				nomLabel.put(dimId.get(i), nom);
				//System.out.println(nom);
				xml += "<category label='" + nom + "'  />";
			}// else{ nom="0"; }
		}
		xml += "</categories>";
		// /////////////////////////
		HashMap prmt = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		//String parametros = "";
		//String filtro;
		String anio = (String)prmt.get("anio");
		if(!hm.isEmpty()){
			if(anio != null){
				anioIni = anio;
			 	anioFin = anio;
			}
			
		}
		HashMap nomMed = new HashMap();
		//nomMed = getNomMed(id_user,_id_dashboard); 
		String med = "";
		String med_2 = "";
		String med_3 = "";
		String medida_2_dataset = "";
		String medida_3_dataset = "";
		HashMap nom_gpo_med = pptos.getNomMed(id_user, id_dashboard, gpoMed);//*Modulo
		//boolean vrf_med_2 = pptos.verificaMedida_2(id_user, id_dashboard);
		String cmp_1 = "";
		String cmp_2 = "";
		String cmp_3 = "";
		String cmp_4 = "";
		for (int j = Integer.parseInt(anioFin); j <= Integer.parseInt(anioFin); j++) {
			
			//Obtener nombres de las medidas para generar datasets
			HashMap grupo_med = pptos.getGrupoMed(id_user, id_modulo, id_dashboard, gpoMed);
			Iterator iter = grupo_med.entrySet().iterator();
			Map.Entry e;
			int contDS = 0;
			String num_cmp_med = " ";
			while (iter.hasNext()) {
				e = (Map.Entry)iter.next();
				num_cmp_med = (String)e.getValue(); 
				//System.out.println("Cont DataSet: "+ contDS);
				//System.out.println("CPM_1: "+cmp_1+" CMP_2: "+cmp_2+"CMP_3: "+cmp_3);
				if(contDS == 0 && !num_cmp_med.equals("medida7") && !num_cmp_med.equals("medida8")){
					cmp_1 = num_cmp_med;
					xml += "<dataset seriesName='"+nom_gpo_med.get(cmp_1)+"' color=''>";
					//System.out.println("XML: "+ xml);
				}
				if(contDS == 1 && !num_cmp_med.equals("medida7") &&!num_cmp_med.equals("medida8")){
					cmp_2 = num_cmp_med;
					medida_2_dataset += "<dataset seriesName='"+nom_gpo_med.get(cmp_2)+"'  >";
					//System.out.println("XML2"+medida_2_dataset);
				}
				if(contDS == 2 && !num_cmp_med.equals("medida7") && !num_cmp_med.equals("medida8")){
					cmp_3 = num_cmp_med;
					medida_3_dataset += "<dataset seriesName='"+nom_gpo_med.get(cmp_3)+"' renderAs='Line' >";
					//System.out.println("XML3"+medida_3_dataset);
				}
				if(!num_cmp_med.equals("medida7") && !num_cmp_med.equals("medida8")){
					contDS ++;
				}
			}
			
			for (int i = 0; i < dimIDV.size(); i++) {
				
				if (body.get(cmp_1+"_" + j + "_" + dimIDV.get(i)) != null) {
					med = (String) body.get(cmp_1+"_" + j + "_" + dimIDV.get(i));
				} else {
					med = "0";
				}
				if (body.get(cmp_2+"_" + j + "_" + dimIDV.get(i)) != null) {
					med_2 = (String) body.get(cmp_2+"_" + j + "_" + dimIDV.get(i));
				} else {
					med_2 = "0";
				}
				if (body.get(cmp_3+"_" + j + "_" + dimIDV.get(i)) != null) {
					med_3 = (String) body.get(cmp_3+"_" + j + "_" + dimIDV.get(i));
				} else {
					med_3 = "0";
				}
				String nombre = nomLabel.get(dimIDV.get(i));
				xml += "<set  value='" + med + "' link='F-hidden-parametros.jsp?valor="
				+dimIDV.get(i)+"%26parametro="+cmp_filtro+"%26chartID="+chart_id+"%26nombre="+xAxisName
				+"%26nomValue="+nombre+"%26id_user="+id_user+"%26id_dashboard="+id_dashboard+"%26popup=false' color=''/>";
				if(medida_2_dataset != ""){
					medida_2_dataset += "<set  value='" + med_2 + "' />";
				}
				if(medida_3_dataset != ""){
					medida_3_dataset += "<set  value='" + med_3 + "' />";
				}
				
			}
			if(medida_2_dataset != ""){
				//medida_2_dataset += "<set  value='" + med_2 + "' />";
				medida_2_dataset += "</dataset>";
				//System.out.println();
			}
			if(medida_3_dataset != ""){
				//medida_3_dataset += "<set  value='" + med_3 + "' />";
				medida_3_dataset += "</dataset>";
			}
				//medida_2_dataset += "</dataset>";
				//medida_3_dataset += "</dataset>";
			xml += "</dataset>";
		
		}
		//Medida 2;
		//if(vrf_med_2){
		if(medida_2_dataset != ""){
			//medida_2_dataset += "<set  value='" + med_2 + "' />";
			xml += medida_2_dataset;
			//System.out.println();
		}
		if(medida_3_dataset != ""){
			//medida_3_dataset += "<set  value='" + med_3 + "' />";
			//medida_3_dataset += "</dataset>";
			xml += medida_3_dataset;
		}
			//xml += medida_2_dataset;
			//xml += medida_3_dataset;
		//}		

		xml += "</chart>";
		System.out.println(xml);
		return xml;
	}
	public String getChartCumpGlobal(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String medida,
			String anioIni,
			String anioFin,
			String gpoMed,
			String cmp_ppto
			){
		String xml = "";
		String limite = "";//getLimiteGlobal(cmp);
		double limite_final = 0;
		String cumpl_global = "";//StringCumplimientoGlobal();
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean sts_fact =getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_prefix = getStatusGpo(id_user, id_modulo, id_dashboard);
		HashMap medidas = getDatosCtaCve(cust_id, id_user, chart_id, id_dashboard, id_modulo);
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id, id_modulo);
		Iterator it = medidas.entrySet().iterator();
		Map.Entry e;
		String valor = "";
		String id_val = "";
		String cmp_fact ="";
		String cmp_target ="";
		String prefix = "";
		if(cmp_ppto.equals("2")){
			if(sts_po && sts_fact){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					//System.out.println("ID-- "+id_val +"-- cppto "+cmp_ppto+"___"+id_val.equals(cmp_ppto));
					if(id_val.equals("1")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp_trgt "+ cmp_fact);
					}
					if(id_val.equals(cmp_ppto)){
						cmp_fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ cmp_fact);
					}
					//System.out.println(id_val+" -- "+valor+" nom_cmp "+ cmp_fact);
				}
				//System.out.println("Ant Cmp "+cmp_fact);
				limite = getLimiteGlobal(cmp_fact, cust_id, id_user, id_modulo, id_dashboard, chart_id, anioIni);
				cumpl_global = getCumpGlobal(cmp_target, cust_id, id_user, id_modulo, id_dashboard, chart_id, anioIni);
			}else{
				limite = "0";
				cumpl_global  = "0";
			}
		}
		if(cmp_ppto.equals("3")){
			if(sts_pm && sts_fact){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					//System.out.println("ID-- "+id_val +"-- cppto "+cmp_ppto+"___"+id_val.equals(cmp_ppto));
					if(id_val.equals("1")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp_trgt "+ cmp_fact);
					}
					if(id_val.equals(cmp_ppto)){
						cmp_fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ cmp_fact);
					}
					//System.out.println(id_val+" -- "+valor+" nom_cmp "+ cmp_fact);
				}
				//System.out.println("Ant Cmp "+cmp_fact);
				limite = getLimiteGlobal(cmp_fact, cust_id, id_user, id_modulo, id_dashboard, chart_id, anioIni);
				cumpl_global = getCumpGlobal(cmp_target,cust_id, id_user, id_modulo, id_dashboard, chart_id, anioIni);
			}else{
				limite = "0";
				cumpl_global  = "0";
			}
		}
		if(limite != null && limite != ""){
			limite_final = (Double.parseDouble(limite)+ Double.parseDouble(limite) * (.20));
		}else{
			limite_final = 0;
			//(String) Collections.max(max);
		}
		if(sts_prefix){
			prefix = "$";
		}
		if(cumpl_global == null || cumpl_global == ""){
			cumpl_global = "0";
		}
		/*if(limite == null || limite == ""){
			limite = "0";
		}*/
		/*xml = "<chart upperLimit='"+limite+"' lowerLimit='0' numberSuffix='"+prefix+"'>"+
				"<value>"+cumpl_global+"</value>"+
				"</chart>";*/
		xml = "<chart palette='3' animation='1' lowerLimit='0' upperLimit='"+limite_final+"' showShadow='1' caption=''  colorRangeFillRatio='0,10,80,10' showColorRangeBorder='0' roundRadius='0' numberPrefix='"+prefix+"' numberSuffix='K' showValue='1' showTickMarks='0' showTickValues='0'>"+
			    "<colorRange >"+
		        	"<color minValue='0' maxValue='3' code='3399CC' />"+
		        	//"<color minValue='30' maxValue='50' />" +
		        	//"<color minValue='50' maxValue='70' />"+ 
		        	//"<color minValue='70' maxValue='100' />"+ 
		        "</colorRange>"+
		    "<value >"+cumpl_global+"</value>"+
		    "<target >"+limite+"</target>"+
		"</chart>";
		
		double porcentaje = 0;
		//System.out.println(cumpl_global +" - "+limite);
		if(limite != "0" && limite!= null && limite != ""){
			porcentaje = Math.rint((Double.parseDouble(cumpl_global) /Double.parseDouble(limite) * 100));
		}
		xml += "_" + porcentaje;
		//lSystem.out.println("xml-global  "+xml );
		return xml;
	}
	
	public String getTablePptos(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String medida,
			String anioIni,
			String anioFin,
			String gpoMed,
			String orden,
			String ordenMenu,
			boolean popup,
			boolean filtros) throws UnsupportedEncodingException {
		////System.out.println("Tabla");
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		String cmp_filtro = (String) hm.get("cmp_id");
		HashMap<String, String> dtNom = new HashMap<String, String>();/////---nom
		HashMap<String, String> dtMedPag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMed_2Pag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMed_3Pag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMed_4Pag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMedTot = new HashMap<String, String>();///----total_med
		HashMap<String, String> dtMed_2Tot = new HashMap<String, String>();///----total_med
		HashMap<String, String> dtMed_3Tot = new HashMap<String, String>();///----total_med
		HashMap<String, String> dtMed_4Tot = new HashMap<String, String>();///----total_med
		String xAxisName = (String) hm.get("xAxisName");
		String tipo_orden = "";
		String html = "";
		if(ordenMenu == null){
			tipo_orden = (String) hm.get("tipo_orden");
		}else{
			tipo_orden = ordenMenu;
		}
		//String cmp_medida = "";
		boolean org_param = false;
		HashMap head = pptos.getDataHead(chart_id, cust_id, anioIni, anioFin,
				tipo_orden, orden, id_user, id_dashboard, id_modulo, filtros); 
		HashMap body = pptos.getDataBody(chart_id, cust_id, anioIni, anioFin, id_user, id_dashboard, gpoMed, id_modulo, filtros);
		if(chart_id.equals("5") || chart_id.equals("9")){
			org_param = true;
		}
		SortedSet<Integer> sort = new TreeSet<Integer>(head.keySet());
		Iterator it = sort.iterator();
		HashMap prmt = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		String anio = (String)prmt.get("anio");
		System.out.println("Año -----> "+anio);
		System.out.println("AñoIni: "+anioIni + "AñoFin: "+anioFin);
		if(!hm.isEmpty()){
			if(anio != null){
				anioIni = anio;
			 	anioFin = anio;
			}
			
		}
		System.out.println("AñoIni: "+anioIni + "AñoFin: "+anioFin);
		String link_popup = "";
		
		ArrayList<String> dimId = new ArrayList<String>();////-----------
		int cont = 0;
		HashMap nom_gpo_med = pptos.getNomMedTable(id_user, id_dashboard, gpoMed);
		
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		html += "<tr><th>" + xAxisName + "</th>";
		String cmp_med_1 = "";
		String cmp_med_2 = "";
		String cmp_med_3 = "";
		String cmp_med_4 = "";
		boolean chk_med_1 = false;
		boolean chk_med_2 = false;
		boolean chk_med_3 = false;
		boolean chk_fact = false;
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);
		boolean cmb = getStatusCmb(id_user, id_modulo, id_dashboard);
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		String cc = getPerCC(id_user);
		
		HashMap grupo_med = pptos.getGrupoMed(id_user, id_modulo, id_dashboard, gpoMed);
		Iterator iter = grupo_med.entrySet().iterator();
		Map.Entry e_med;
		int contDS = 1;
		HashMap ord_med = new HashMap();
		while (iter.hasNext()) {
			e_med = (Map.Entry)iter.next();
			ord_med.put("med_"+contDS, e_med.getValue());			
			contDS ++;
		}
		
		//Condiciones para generar el orden de las columnas en las tablas
		if(sts_fact && sts_po && sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_3")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>"; 
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Presupuestos</th>";
			html += "<th>Diferencia Fact-Original</th>";
			html += "<th>Diferencia Fact-Modificado</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Pttos</td>";
			html += "<th>% Cubrimiento Original-Fact</th>";
			html += "<th>% Cubrimiento Modificado-Fact</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Original </th>";
			html += "<th>% Contribucion Ptto Modificado</th>";
			html += "<th>% Contribucion Facturacion</th>";
		}
		if(sts_fact && sts_po && !sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Ptto Original</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Original-Fact</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Original </th>";
			html += "<th>% Contribucion Facturacion</th>";	
			//Tendencia
			html += "<th>Tendencia P.O. Ptto Original</th>";
		}
		if(sts_fact && sts_pm && !sts_po){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Ptto Modificado</th>";;
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Modificado-Fact</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Modificado</th>";
			html += "<th>% Contribucion Facturacion</th>";
			//Tendencia
			html += "<th>Tendencia P.M. Ptto Modificado</td>";
		}
		if(sts_po && sts_pm && !sts_fact){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Pttos</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Pttos</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Original </th>";
			html += "<th>% Contribucion Ptto Modificado</th>";
			//%MU
			if(cc == "" || cc == null && gpo){
				html += "<th align='center' valign='top'> MU Original </th>";
				html += "<th align='center' valign='top'> MU Modificado</th>";
			}
		}
		if(sts_fact && !sts_po && !sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>% Contribucion Facturacion </th>";
		}
		if(!sts_fact && sts_po && !sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>% Contribucion Ptto Original </th>";
		}
		if(!sts_fact && !sts_po && sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>% Contribucion Ptto Modificado </th>";
		}
		html += "</tr>";
		html += "</thead>";
		html +="<tbody >";
		//Array-----------Array-----------Array-----------Array-----------Array-----------Array-----------
		ArrayList<String> dimClaves = new ArrayList<String>();
		ArrayList<String> dimCvPag = new ArrayList<String>();
		//Array-----------Array-----------Array-----------Array-----------Array-----------Array-----------
		while (it.hasNext()) {
			Object clave = it.next();
			Object valor = head.get(clave);
			//System.out.println("Valor "+valor);
			dimId.add((String) valor);
			dimClaves.add((String)valor);//-------Nomm
			dtNom.put((String)valor, (String) valor);//------Nomm
			cont++;
			String med_1 = null;
			String med_2 = null;
			String med_3 = null;
			String med_4 = null;
			for (int j = Integer.parseInt(anioFin); j <= Integer
					.parseInt(anioFin); j++) {

				if (body.get(ord_med.get("med_1")+"_" + j + "_" + (String) valor) != null) {
					//System.out.println("Med_1 "+ord_med.get("med_1")+"_" + j + "_"	+ (String) valor);
					med_1 = (String) body.get(ord_med.get("med_1")+"_" + j + "_"	+ (String) valor);
				} else {
					med_1 = "0";
				}
				//System.out.println("Med_1: ");
				dtMedPag.put((String) valor+"_"+j, med_1);
				dtMedTot.put((String) valor+"_"+j, med_1);
				
				if (body.get(ord_med.get("med_2")+"_" + j + "_" + (String) valor) != null) {
					med_2 = (String) body.get(ord_med.get("med_2")+"_" + j + "_"	+ (String) valor);
				} else {
					med_2 = "0";
				}
				dtMed_2Pag.put((String) valor+"_"+j, med_2);
				dtMed_2Tot.put((String) valor+"_"+j, med_2);
				
				if (body.get(ord_med.get("med_3")+"_" + j + "_" + (String) valor) != null) {
					med_3 = (String) body.get(ord_med.get("med_3")+"_" + j + "_"	+ (String) valor);
				} else {
					med_3 = "0";
				}
				dtMed_3Pag.put((String) valor+"_"+j, med_3);
				dtMed_3Tot.put((String) valor+"_"+j, med_3);
				//System.out.println("BODY MED 4 " + cmp_med_4+"_" + j + "_" + (String) valor); Agregar la caurta medida
				
				if (body.get(ord_med.get("med_4")+"_" + j + "_" + (String) valor) != null) {
					med_4 = (String) body.get(ord_med.get("med_4")+"_" + j + "_"	+ (String) valor);
				} else {
					med_4 = "0";
				}
				dtMed_4Pag.put((String) valor+"_"+j, med_4);
				dtMed_4Tot.put((String) valor+"_"+j, med_4);
			}
		}
		for(int a=0; a < dimId.size(); a++){
			if(dtNom.get(dimId.get(a)) != null){
				dimCvPag.add(dtNom.get(dimId.get(a)));
			}
		}
		
		String val = null;
		String medd_1 = null;
		String clv = null;
		String clv_tot = null;
		String val_2 = null;
		String val_3 = null;
		
		String medd_2 = null;
		String medd_3 = "";
		String medd_4 = "";
		String clv_2 = null;
		String clv_3 = null;
		double sumTotal = 0;
		double sumTotal_2 = 0;
		double sumTotal_3 = 0;
		double sumTotal_4 = 0;
		int contsum = 0;
		double perc_dif_0 = 0;
		double perc_dif_1 = 0;
		double perc_dif_2 = 0;
		double perc_contrib_1 = 0;
		double perc_contrib_2 = 0;
		double perc_contrib_3 = 0;
		double totalPorc1 = 0;
		double totalPorc2 = 0;
		double totalPorc3 = 0;
		double total_perc_contrib_fact = 0;
		double total_perc_contrib_org = 0;
		double total_perc_contrib_mod = 0;
		double total_perc_dif_0 = 0;
		double total_perc_dif_1 = 0;
		double total_perc_dif_2 = 0;
		//Totales para % de Contribucion
		double total1 = 0;
		double total2 = 0;
		double total3 = 0;
		double total4 = 0;
		//Obtiene totales para el porcentaje de contribucion
		for(int a=0; a < dimCvPag.size(); a++){
			clv_tot = dtNom.get(dimCvPag.get(a));
			//System.out.println("Cve total "+clv_tot);
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				String medd1 = dtMedTot.get(clv_tot+"_"+y);
				String medd2 = dtMed_2Tot.get(clv_tot+"_"+y);
				String medd3 = dtMed_3Tot.get(clv_tot+"_"+y);
				String medd4 = dtMed_4Tot.get(clv_tot+"_"+y);
				total1 += Double.parseDouble(medd1);
				total2 += Double.parseDouble(medd2);
				total3 += Double.parseDouble(medd3);
				total4 += Double.parseDouble(medd4);
				
			}
		}
		//System.out.println((total1+"--"+total2+"--"+total3));
		HashMap<String, String> sumaTotal = new HashMap<String, String>();
		HashMap<String, String> sumaTotal_2 = new HashMap<String, String>();
		HashMap<String, String> sumaTotal_3 = new HashMap<String, String>();
		HashMap<String, String> sumaTotal_4 = new HashMap<String, String>();
		double val_med_1 = 0;
		double val_med_2 = 0;
		double val_med_3 = 0;
		double val_med_4 = 0;
		double dif_pptos =0;
		double dif_fact_org = 0;
		double dif_fact_mod = 0;
		double perc_cub_ptto = 0;
		double perc_cub_org_fact = 0;
		double perc_cub_mod_fact = 0;
		double perc_cont_org = 0;
		double perc_cont_mod = 0;
		double perc_cont_fact = 0;
		double total_dif_pptos = 0;
		double total_dif_fact_org = 0;
		double total_dif_fact_mod = 0;
		double total_perc_cub_ppto = 0;
		double total_perc_cub_org_fact = 0;
		double total_perc_cub_org_mod = 0;
		double total_perc_cont_1 = 0;
		double total_perc_cont_2 = 0;
		double total_perc_cont_3 = 0;
		
		double prb_fact_ttl = 0; //Prueba total facturado
		double prb_pptoO_ttl = 0; //Prueba total facturado
		double prb_pptoM_ttl = 0; //Prueba total facturado
		String fnt_md1 = "";
		String fnt_md2 = "";
		String fnt_md3 = "";
		String fnt_md4 = "";
		String fnt_dif1 = "";
		String fnt_dif2 = "";
		String fnt_dif3 = "";
		for(int a=0; a < dimCvPag.size(); a++){
			
			
			clv = dtNom.get(dimCvPag.get(a));
			val = pptos.getDimNameTable(chart_id, cust_id,(String) dtNom.get(dimCvPag.get(a)), id_modulo);
			String val_encode =  URLEncoder.encode(val, "ISO-8859-1");
			String ruta = "popup.jsp?id_user="+id_user+"&id_modulo="+id_modulo+"&id_dashboard="+id_dashboard+"&id_chart="+
			chart_id+"&parametro="+cmp_filtro+"&val_param="+clv+"&val_org="+val_encode+"&nom_org="+xAxisName+"&delFil=true";
			
			if(popup){
				link_popup = "<a href='#' onclick=abrePop('"+ruta+"');><strong>+</strong></a>";
			}else{
				link_popup = "<a href='parametros_popup.jsp?valor="
								+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
								+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"' target='hidden'>+<a>";
			}
			
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				
				medd_1 = dtMedPag.get(clv+"_"+y);
				medd_2 = dtMed_2Pag.get(clv+"_"+y);
				medd_3 = dtMed_3Pag.get(clv+"_"+y);
				medd_4 = dtMed_4Pag.get(clv+"_"+y);
				if(medd_3 == null){
					medd_3 = "0";
				}
				perc_dif_0 = 0;
				perc_dif_1 = 0;
				perc_dif_2 = 0;
				perc_contrib_1 = 0;
				perc_contrib_2 = 0;
				perc_contrib_3 = 0;
				
				fnt_md1 = "";
				fnt_md2 = "";
				fnt_md3 = "";
				fnt_dif1 = "";
				fnt_dif2 = "";
				fnt_dif3 = "";
				//total_2 = Double.parseDouble(medd_2); //Suma Total
				sumTotal = sumTotal  + (int)Double.parseDouble(medd_1);
				
				sumTotal_2 = sumTotal  + (int)Double.parseDouble(medd_2);
				sumTotal_3 = sumTotal  + (int)Double.parseDouble(medd_3);
				sumTotal_4 = sumTotal  + (int)Double.parseDouble(medd_4);
			
				/*++++++++++++++++*/
				sumaTotal.put(contsum+"_"+Integer.toString(y), medd_1);
				sumaTotal_2.put(contsum+"_"+Integer.toString(y), medd_2);
				sumaTotal_3.put(contsum+"_"+Integer.toString(y), medd_3);
				sumaTotal_4.put(contsum+"_"+Integer.toString(y), medd_4);
				
				DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
				simbolo.setDecimalSeparator('.');
				simbolo.setGroupingSeparator(',');
				DecimalFormat format_med = new DecimalFormat("###,###.####",simbolo);
				//Valores de las columnas d ela tabla
				
				String _perc_contrib_1 = "";
				String _perc_contrib_2 = "";
				String _perc_contrib_3 = "";
				
				String frmt_1 = "";
				String frmt_2 = "";
				String frmt_3 = "";
				String frmt_4 = "";
				String frmt_dif_pptos = "";
				String frmt_dif_fact_org = "";
				String frmt_dif_fact_mod = "";
				
				String _perc_cub_ptto = "";
				String _perc_cub_org_fact = "";
				String _perc_cub_mod_fact = "";
				
				
				
				if(sts_fact && sts_po && sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					
					val_med_1 = Double.parseDouble(medd_1);
					prb_fact_ttl += Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
				
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
				
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					
					val_med_3 = Double.parseDouble(medd_3);
					if(val_med_3 < 0){
						fnt_md3 = "<font color=red>";
					}
					
					if(val_med_3 != 0){
						frmt_3 = format_med.format(val_med_3);
					}
				
					if(val_med_1 > 0 || val_med_1 < 0 || val_med_2 > 0 || val_med_2 < 0 || val_med_3 > 0 || val_med_3 < 0 ){
						//System.out.println("-------"+a);
						html += "<tr>";
						
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
						//System.out.println( medd_1 + "-"+ medd_2+"-"+medd_3);
					//}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md3+frmt_3+ "</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md3+frmt_1+ "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					dif_pptos = val_med_3 - val_med_2;
					//System.out.println("dif pptos "+dif_pptos);
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
				
					if(dif_pptos != 0){
						frmt_dif_pptos = format_med.format(dif_pptos);
					}
					
					dif_fact_org = val_med_1 - val_med_2;
					//System.out.println("dif_fact_org "+dif_fact_org);
					if(dif_fact_org < 0){
						fnt_dif2 = "<font color=red>";
					}
					total_dif_fact_org += dif_fact_org;
					
					if(dif_fact_org != 0){
						frmt_dif_fact_org= format_med.format(dif_fact_org);
					}
					dif_fact_mod = val_med_1 - val_med_3;
					//System.out.println("dif_fact_mod "+dif_fact_mod);
					if(dif_fact_mod < 0){
						fnt_dif3 = "<font color=red>";
					}
					total_dif_fact_mod += dif_fact_mod;
					
					if(dif_fact_mod != 0){
						frmt_dif_fact_mod = format_med.format(dif_fact_mod);
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif2+frmt_dif_fact_org+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif3+frmt_dif_fact_mod+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					//System.out.println(val_med_3 / val_med_2);
					
					if(val_med_2 > 0){
						perc_cub_ptto = ((val_med_3 / val_med_2) * 100);
						
					}else{
						perc_cub_ptto = 0;
					}
					
					BigDecimal bd_perc_cub_ptto = new BigDecimal(perc_cub_ptto);
					if(perc_cub_ptto != 0){
						BigDecimal bigDecimalRedondeado = bd_perc_cub_ptto.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_ptto = String.valueOf(bigDecimalRedondeado) +" %";
					}
					total_perc_cub_ppto += perc_cub_ptto;
					if(val_med_2 > 0){
						perc_cub_org_fact= ((val_med_1 / val_med_2) * 100);
						
					}else{
						perc_cub_org_fact= 0;
					}
					 
					BigDecimal bd_perc_cub_org_fact = new BigDecimal(perc_cub_org_fact);
					if(perc_cub_org_fact != 0){
						BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_org_fact = String.valueOf(rd_perc_cub_org_fact+" %");
					}
					total_perc_cub_org_fact += perc_cub_org_fact;
					if(val_med_3 > 0){
						perc_cub_mod_fact = ((val_med_1 / val_med_3) * 100);
					}else{
						perc_cub_mod_fact = 0;
					}
					BigDecimal bd_perc_cub_mod_fact = new BigDecimal(perc_cub_mod_fact);
					if(perc_cub_mod_fact != 0){
						BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_mod_fact = String.valueOf(rd_perc_cub_mod_fact+" %");
					}
					total_perc_cub_org_mod += perc_cub_mod_fact;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_ptto+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_org_fact+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_mod_fact+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					
					if(total1 > 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
						totalPorc1 += perc_contrib_1;
						System.out.println("% 1 --> "+val_med_1+"/"+total1);
					}
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 + " %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					if(total2 >0){
						perc_contrib_2 = ((val_med_2 /total2) * 100);
						totalPorc2 += perc_contrib_2;
						//System.out.println("% 2 --> "+val_med_2+"/"+total2);
					}
					BigDecimal bd_perc_contrib_2 = new BigDecimal(perc_contrib_2);
					if(perc_contrib_2 != 0){
						BigDecimal rd_perc_contrib_2 = bd_perc_contrib_2.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_2 = rd_perc_contrib_2 + " %";
					}
					total_perc_cont_2 += Double.parseDouble(bd_perc_contrib_2.toString());
					if(total3 > 0){
						perc_contrib_3 = ((val_med_3 /total3) * 100);
						totalPorc3 += perc_contrib_3;
						//System.out.println(val + " "+ perc_contrib_1 );
					}
					BigDecimal bd_perc_contrib_3 = new BigDecimal(perc_contrib_3);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_3 = bd_perc_contrib_3.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_3 = rd_perc_contrib_3 + " %";
					}
					total_perc_cont_3 += Double.parseDouble(bd_perc_contrib_3.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_2+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_3+"</td>";
					}
					
					html += "</tr>";

				}
				if(sts_fact && sts_po && !sts_pm ){
					System.out.println("<<<< "+ medd_1 + " _ " + medd_2);
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					_perc_cub_org_fact = "";
					//Ppto Orig //Ppto Modif //Facturacion 
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						 frmt_1 = format_med.format(val_med_1);
					}
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
					//String frmt_2 = "";
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					if(val_med_1 > 0 || val_med_1 < 0 || val_med_2 > 0 || val_med_2 < 0){
						html += "<tr>";
						//if(!medd_1.equals("0.0") && !medd_2.equals("0.0") ){
						//System.out.println(">>>> "+ medd_1 + " _ " + medd_2);
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
						
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+ "</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+ "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					
					dif_pptos = val_med_1 - val_med_2;
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
					//String frmt_dif_pptos = "";
					if(dif_pptos != 0){
						frmt_dif_pptos= format_med.format(dif_pptos);
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					perc_cub_org_fact = 0;
					if(val_med_2 != 0){
						perc_cub_org_fact= ((val_med_1 / val_med_2) * 100);
					}
					//String _perc_cub_org_fact = "";
					BigDecimal bd_perc_cub_org_fact = new BigDecimal(perc_cub_org_fact);
					if(perc_cub_org_fact != 0){
						BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_org_fact = rd_perc_cub_org_fact + " %";
					}
					total_perc_cub_org_fact += perc_cub_org_fact;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_org_fact+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					
					if(total2 > 0){
						perc_contrib_1 = ((val_med_2 /total2) * 100);
						totalPorc1 += perc_contrib_1;
							
					}
					//total_perc_cont_1 += perc_contrib_1;
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 + " %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					if(total1 > 0){
						perc_contrib_2 = ((val_med_1 /total1) * 100);
						totalPorc2 += perc_contrib_2;
						System.out.println(" 1: "+val + " --> "+ perc_contrib_1 +"--> "+totalPorc1  );
					}
					//total_perc_cont_2 += perc_contrib_2;
					BigDecimal bd_perc_contrib_2 = new BigDecimal(perc_contrib_2);
					//String _perc_contrib_2 = "";
					if(perc_contrib_2 != 0){
						BigDecimal rd_perc_contrib_2 = bd_perc_contrib_2.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_2 = rd_perc_contrib_2 + " %";
					}
					total_perc_cont_2 += Double.parseDouble(bd_perc_contrib_2.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_2+"</td>";	
					//Tendencia
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
					}
					html += "</tr>";
				}
				if(sts_fact && sts_pm && !sts_po){
					prb_fact_ttl += Double.parseDouble(medd_1);
					//Ppto Orig //Ppto Modif //Facturacion
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
					
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					if(val_med_1 > 0 || val_med_2 > 0 || val_med_1 < 0 || val_med_2 < 0 ){
						html += "<tr>";
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					dif_pptos = val_med_1 - val_med_2;
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
					//String frmt_dif_pptos = "";
					if(dif_pptos != 0){
						frmt_dif_pptos= format_med.format(dif_pptos);
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";;
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					perc_cub_mod_fact = 0;
					BigDecimal bd_perc_cub_mod_fact = null;
					if(val_med_2 != 0){
						perc_cub_mod_fact= ((val_med_1 / val_med_2) * 100);
						bd_perc_cub_mod_fact = new BigDecimal(perc_cub_mod_fact);
					}
					//String _perc_cub_mod_fact = "";
					if(perc_cub_mod_fact != 0){
						BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_mod_fact = rd_perc_cub_mod_fact + " %";
					}
					total_perc_cub_org_mod += perc_cub_mod_fact;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_mod_fact+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					BigDecimal bd_perc_contrib_1 = null;
					BigDecimal bd_perc_contrib_2 = null;
					if(total2 != 0){
						perc_contrib_2 = ((val_med_2 /total2) * 100);
						totalPorc2 += (val_med_2 / total2) * 100;
						bd_perc_contrib_2 = new BigDecimal(perc_contrib_2);
							
					}
					//String _perc_contrib_1 = ""; 
					
					total_perc_cont_1 += perc_contrib_1;
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
						System.out.println(" Cont_1 <----> "+val_med_1 +"/"+total1);
						totalPorc1 += ((val_med_1 / total1) * 100);
						bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					}
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 + " %";
					}
					//String _perc_contrib_2 = ""; 
					if(perc_contrib_2 != 0){
						BigDecimal rd_perc_contrib_2 = bd_perc_contrib_2.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_2 = rd_perc_contrib_2 + " %";
					}
					
					
					total_perc_cont_2 += perc_contrib_2;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_2+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					//Tendencia
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
					}
					html += "</tr>";
					
				}
				if(!sts_fact && sts_po && sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					//System.out.println("<<<< "+medd_1+" _ "+medd_2);
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
					//String frmt_2 = ""; 
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					
					//Medidas MU original y modificado...
					val_med_3 = Double.parseDouble(medd_3);
					if(val_med_3 < 0){
						fnt_md3 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_3 != 0){
						frmt_3 = format_med.format(val_med_3);
					}
					
					val_med_4 = Double.parseDouble(medd_4);
					if(val_med_4 < 0){
						fnt_md4 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_4 != 0){
						frmt_4 = format_med.format(val_med_4);
					}
					if(val_med_1 > 0 || val_med_2 > 0 || val_med_1 < 0 || val_med_2 < 0 ){
						html += "<tr>";
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					dif_pptos = val_med_2 - val_med_1;
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
					//String frmt_dif_pptos= "";
					if(dif_pptos != 0){
						frmt_dif_pptos = format_med.format(dif_pptos);
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					perc_cub_ptto = 0;
					if(val_med_1 != 0){
						perc_cub_ptto= ((val_med_2 / val_med_1) * 100);
					}
					BigDecimal bd_perc_cub_ptto = new BigDecimal(perc_cub_ptto);
					//String _perc_cub_ptto = "";
					if(perc_cub_ptto != 0){
						BigDecimal rd_perc_cub_ptto = bd_perc_cub_ptto.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_ptto = String.valueOf(rd_perc_cub_ptto) +" %";
					}
					total_perc_cub_ppto += perc_cub_ptto;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_ptto+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
						totalPorc1 += perc_contrib_1;
						
					}
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					//String _perc_contrib_1 ="";
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 +" %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					if(total2 != 0){
						
						perc_contrib_2 = ((val_med_2 /total2) * 100);
						totalPorc2 += perc_contrib_2;
						
					}
					BigDecimal bd_perc_contrib_2 = new BigDecimal(perc_contrib_2);
					//String _perc_contrib_2 ="";
					if(perc_contrib_2 != 0){
						BigDecimal rd_perc_contrib_2 = bd_perc_contrib_2.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_2 = rd_perc_contrib_2 + " %";
					}
					total_perc_cont_2 += Double.parseDouble(bd_perc_contrib_2.toString());
					html += "<td align='right' valign='top'>"+_perc_contrib_1+"</td>";
					html += "<td align='right' valign='top'>"+_perc_contrib_2+"</td>";
					//%MU
					if(cc == "" || cc == null && gpo){
						html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_3+"</td>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_4+"</td>";
					}
					}
					html += "</tr>";
				}
				if(sts_fact && !sts_po && !sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					if(val_med_1 > 0 || val_med_1 < 0){
						html += "<tr>";
						//if(!medd_1.equals("0.0")){
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+ "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
						totalPorc1 += perc_contrib_1;
						
					}
					//String _perc_contrib_1 = "";
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 +" %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					}
					html += "</tr>";
				}
				if(!sts_fact && sts_po && !sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion 
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					if(val_med_1 > 0 || val_med_1 < 0){
						html += "<tr>";
						//if(!medd_1.equals("0.0")){
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1 + "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
						totalPorc1 += perc_contrib_1;
						
					}
					//String _perc_contrib_1 = "";
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 +" %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
				}
					html += "</tr>";
				}
				if(!sts_fact && !sts_po && sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					if(val_med_1 > 0 || val_med_1 < 0){
						//if(!medd_1.equals("0.0")){
						html += "<tr>";
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1 + "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
						totalPorc1 += perc_contrib_1;
						
					}
					//String _perc_contrib_1 = "";
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 + " %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					}
					html += "<tr>";
				}
				//html += "</tr>";		
			}
			contsum++;
			////System.out.println("Total: " + sumTotal);
			//html += html_med_2;
			
		}
		int total = 0;
		int total_2 = 0;
		int total_3 = 0;
		int total_4 = 0;
		int contS = 0;
		int contS_2 = 0;
		int contS_3 = 0;
		int contS_4 = 0;
		////System.out.println("ContS = "+ contS);
		//Iterator itt = sumaTotal.entrySet().iterator();
		html += "<tr><td><font color=red><strong>Total</strong></td>";
		for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
			
			if(contS != 0){
				total = 0;
				total_2 = 0;
				contS = 0;
				contS_2 = 0;
				total_3 = 0;
				contS_3 = 0;
				total_4 = 0;
				contS_4 = 0;
			}
			
			Iterator itt = sumaTotal.entrySet().iterator();
			while (itt.hasNext()) {
			
				Map.Entry e = (Map.Entry)itt.next();
				String cmp = contS+"_"+Integer.toString(y);
				String cant = (String)sumaTotal.get(cmp);
				
				if(cant != null){
					double ct = Double.parseDouble(cant);
					int sum = (int) ct;
					total = total + sum;
					
				}
				contS ++;	
			}
			
			//Total para la medida_2
			Iterator itt_2 = sumaTotal_2.entrySet().iterator();
			while (itt_2.hasNext()) {
			
				Map.Entry e_2 = (Map.Entry)itt_2.next();
				String cmp_2 = contS_2+"_"+Integer.toString(y);
				String cant_2 = (String)sumaTotal_2.get(cmp_2);
				
				if(cant_2 != null){
					double ct_2 = Double.parseDouble(cant_2);
					int sum_2 = (int) ct_2;
					total_2 = total_2 + sum_2;
					
				}
				contS_2 ++;	
			}
			
			//Total para la medida_3
			Iterator itt_3 = sumaTotal_3.entrySet().iterator();
			while (itt_3.hasNext()) {
			
				Map.Entry e_3 = (Map.Entry)itt_3.next();
				String cmp_3 = contS_3+"_"+Integer.toString(y);
				String cant_3 = (String)sumaTotal_3.get(cmp_3);
				
				if(cant_3 != null){
					double ct_3 = Double.parseDouble(cant_3);
					int sum_3 = (int) ct_3;
					total_3 = total_3 + sum_3;
				}
				contS_3 ++;	
			}
			
			//Total para la medida_4
			Iterator itt_4 = sumaTotal_4.entrySet().iterator();
			while (itt_4.hasNext()) {
			
				Map.Entry e_4 = (Map.Entry)itt_4.next();
				String cmp_4 = contS_4+"_"+Integer.toString(y);
				String cant_4 = (String)sumaTotal_4.get(cmp_4);
				
				if(cant_4 != null){
					double ct_4 = Double.parseDouble(cant_4);
					int sum_4 = (int) ct_4;
					total_4 = total_4 + sum_4;
					
				}
				contS_4 ++;	
			}
			
			DecimalFormatSymbols simb = new DecimalFormatSymbols();
			simb.setDecimalSeparator('.');
			simb.setGroupingSeparator(',');
			DecimalFormat format_med = new DecimalFormat("###,###.####",simb);
			
			String sumaTot = format_med.format(total);
			String sumaTot_2 = format_med.format(total_2);
			String sumaTot_3 = format_med.format(total_3);
			String sumaTot_4 = format_med.format(total_4);
			
			//Totales para las columnas
			if(sts_fact && sts_po && sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				String frmt_2 = format_med.format(total2);
				String frmt_3 = format_med.format(total3);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_3+ "</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+ "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				if(total_dif_fact_org  < 0){
					fnt_dif2 = "<font color=red>";
				}
				if(total_dif_fact_mod  < 0){
					fnt_dif3 = "<font color=red>";
				}
				String frmt_dif_pptos= format_med.format(total_dif_pptos);
				String frmt_dif_fact_org= format_med.format(total_dif_fact_org);
				String frmt_dif_fact_mod = format_med.format(total_dif_fact_mod);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif2+frmt_dif_fact_org+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif3+frmt_dif_fact_mod+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//System.out.println(val_med_3 / val_med_2);
				BigDecimal bd_perc_cub_pttos= new BigDecimal(total_perc_cub_ppto);
				BigDecimal rd_perc_cub_pttos = bd_perc_cub_pttos.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_mod_fact = new BigDecimal(total_perc_cub_org_mod);
				BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_DOWN);
				
				String ttl_perc_cub_pttos = "";
				String ttl_perc_cub_org_fact = "";
				String ttl_perc_cub_mod_fact = "";
				if(total_perc_cub_ppto != 0.0){
					ttl_perc_cub_pttos = rd_perc_cub_pttos +" %";
				}
				if(total_perc_cub_org_fact != 0.0){
					ttl_perc_cub_org_fact = rd_perc_cub_org_fact + " %";
				}
				if(total_perc_cub_org_mod != 0.0){
					ttl_perc_cub_mod_fact = rd_perc_cub_mod_fact +" %";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(totalPorc1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(totalPorc3);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_3 = new BigDecimal(totalPorc3);
				BigDecimal rd_total_perc_cont_3 = bd_total_perc_cont_3.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_3+" %</td>";
			}
			if(sts_fact && sts_po && !sts_pm ){
				//Ppto Orig //Ppto Modif //Facturacion 
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				String frmt_2 = format_med.format(total2);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+ "</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+ "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				String frmt_dif_fact_org= format_med.format(total_dif_pptos);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_fact_org+" </td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//perc_cub_org_fact= Math.rint((val_med_1 / val_med_2) * 100);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				//perc_contrib_1 = Math.rint((val_med_2 /total2) * 100);
				//perc_contrib_2 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(totalPorc1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(totalPorc2);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";	
				//Tendencia
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			}
			if(sts_fact && sts_pm && !sts_po){
				//Ppto Orig //Ppto Modif //Facturacion
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				//val_med_2 = Double.parseDouble(medd_2);
				String frmt_2 = format_med.format(total_2);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				//dif_pptos = val_med_1 - val_med_2;
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				
				String frmt_dif_fact_org= format_med.format(total_dif_pptos);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_fact_org+"</td>";;
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//perc_cub_mod_fact= Math.rint((val_med_1 / val_med_2) * 100);
				BigDecimal bd_perc_cub_pttos= new BigDecimal(total_perc_cub_ppto);
				BigDecimal rd_perc_cub_pttos = bd_perc_cub_pttos.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_mod_fact = new BigDecimal(total_perc_cub_org_mod);
				BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				//perc_contrib_1 = Math.rint((val_med_2 /total2) * 100);
				//perc_contrib_2 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(totalPorc1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(totalPorc2);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";
				//Tendencia
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			}
			if(!sts_fact && sts_po && sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				//val_med_2 = Double.parseDouble(medd_2);
				String frmt_2 = format_med.format(total2);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				//dif_pptos = val_med_1 - val_med_2;
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				
				String frmt_dif_pptos= format_med.format(total_dif_pptos );
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//perc_cub_ptto= Math.rint((val_med_1 / val_med_2) * 100);
				BigDecimal bd_perc_cub_pttos= new BigDecimal(total_perc_cub_ppto);
				BigDecimal rd_perc_cub_pttos = bd_perc_cub_pttos.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_mod_fact = new BigDecimal(total_perc_cub_org_mod);
				BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				//perc_contrib_1 = Math.rint((val_med_2 /total2) * 100);
				//perc_contrib_2 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(totalPorc1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(totalPorc2);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";
				//%MU
				if(cc == "" || cc == null && gpo){
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+sumaTot_3+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+sumaTot_4+"</td>";
				}
			}
			if(sts_fact && !sts_po && !sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+ "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
				//perc_contrib_1 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(totalPorc1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
			}
			if(!sts_fact && sts_po && !sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion 
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1 + "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
				//perc_contrib_1 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(totalPorc1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
			}
			if(!sts_fact && !sts_po && sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion 
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1 + "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
				//perc_contrib_1 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(totalPorc1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
			}
		}
		html += "</tr>";
		html += "</tbody>";
		html += "</table>";
		System.out.println("Tabla: "+html);
		return html;
	}
	public String getTablePptosGnrl(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String medida,
			String anioIni,
			String anioFin,
			String gpoMed,
			String orden,
			String ordenMenu,
			boolean popup,
			boolean filtros,
			boolean excel) throws UnsupportedEncodingException {
		////System.out.println("Tabla");
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		String cmp_filtro = (String) hm.get("cmp_id");
		HashMap<String, String> dtNom = new HashMap<String, String>();/////---nom
		HashMap<String, String> dtMedPag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMed_2Pag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMed_3Pag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMed_4Pag = new HashMap<String, String>();///----med
		HashMap<String, String> dtMedTot = new HashMap<String, String>();///----total_med
		HashMap<String, String> dtMed_2Tot = new HashMap<String, String>();///----total_med
		HashMap<String, String> dtMed_3Tot = new HashMap<String, String>();///----total_med
		HashMap<String, String> dtMed_4Tot = new HashMap<String, String>();///----total_med
		String xAxisName = (String) hm.get("xAxisName");
		String tipo_orden = "";
		String html = "";
		if(ordenMenu == null){
			tipo_orden = (String) hm.get("tipo_orden");
		}else{
			tipo_orden = ordenMenu;
		}
		//String cmp_medida = "";
		boolean org_param = false;
		HashMap head = pptos.getDataHead(chart_id, cust_id, anioIni, anioFin,
				tipo_orden, orden, id_user, id_dashboard, id_modulo, filtros); 
		HashMap body = pptos.getDataBody(chart_id, cust_id, anioIni, anioFin, id_user, id_dashboard, gpoMed, id_modulo, filtros);
		if(chart_id.equals("5") || chart_id.equals("9")){
			org_param = true;
		}
		SortedSet<Integer> sort = new TreeSet<Integer>(head.keySet());
		Iterator it = sort.iterator();
		HashMap prmt = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		String anio = (String)prmt.get("anio");
		if(!hm.isEmpty()){
			if(anio != null){
				anioIni = anio;
			 	anioFin = anio;
			}
			
		}
		String link_popup = "";
		
		ArrayList<String> dimId = new ArrayList<String>();////-----------
		int cont = 0;
		HashMap nom_gpo_med = pptos.getNomMedTable(id_user, id_dashboard, gpoMed);
		
		String img = ""; 
		if(!excel){
			img = "<img src='../../img/close.gif'/>";
		}
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		html += "<tr><th>" + xAxisName + "</th>";
		String cmp_med_1 = "";
		String cmp_med_2 = "";
		String cmp_med_3 = "";
		String cmp_med_4 = "";
		boolean chk_med_1 = false;
		boolean chk_med_2 = false;
		boolean chk_med_3 = false;
		boolean chk_fact = false;
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);
		boolean cmb = getStatusCmb(id_user, id_modulo, id_dashboard);
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		String cc = getPerCC(id_user);
		
		HashMap grupo_med = pptos.getGrupoMed(id_user, id_modulo, id_dashboard, gpoMed);
		Iterator iter = grupo_med.entrySet().iterator();
		Map.Entry e_med;
		int contDS = 1;
		HashMap ord_med = new HashMap();
		while (iter.hasNext()) {
			e_med = (Map.Entry)iter.next();
			ord_med.put("med_"+contDS, e_med.getValue());			
			contDS ++;
		}
		
		//Condiciones para generar el orden de las columnas en las tablas
		if(sts_fact && sts_po && sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_3")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>"; 
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Presupuestos</th>";
			html += "<th>Diferencia Fact-Original</th>";
			html += "<th>Diferencia Fact-Modificado</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Pttos</td>";
			html += "<th>% Cubrimiento Original-Fact</th>";
			html += "<th>% Cubrimiento Modificado-Fact</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Original </th>";
			html += "<th>% Contribucion Ptto Modificado</th>";
			html += "<th>% Contribucion Facturacion</th>";
		}
		if(sts_fact && sts_po && !sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Ptto Original</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Original-Fact</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Original </th>";
			html += "<th>% Contribucion Facturacion</th>";	
			//Tendencia
			html += "<th>Tendencia P.O. Ptto Original</th>";
		}
		if(sts_fact && sts_pm && !sts_po){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Ptto Modificado</th>";;
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Modificado-Fact</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Modificado</th>";
			html += "<th>% Contribucion Facturacion</th>";
			//Tendencia
			html += "<th>Tendencia P.M. Ptto Modificado</td>";
		}
		if(sts_po && sts_pm && !sts_fact){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_2")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>Diferencia Pttos</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
			html += "<th>% Cubrimiento Pttos</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
			html += "<th>% Contribucion Ptto Original </th>";
			html += "<th>% Contribucion Ptto Modificado</th>";
			//%MU
			if(cc == "" || cc == null && gpo){
				html += "<th align='center' valign='top'> MU Original </th>";
				html += "<th align='center' valign='top'> MU Modificado</th>";
			}
		}
		if(sts_fact && !sts_po && !sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>% Contribucion Facturacion </th>";
		}
		if(!sts_fact && sts_po && !sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>% Contribucion Ptto Original </th>";
		}
		if(!sts_fact && !sts_po && sts_pm){
			//Ppto Orig //Ppto Modif //Facturacion 
			html += "<th>"+nom_gpo_med.get((String)ord_med.get("med_1")) + "</th>";
			//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
			html += "<th>% Contribucion Ptto Modificado </th>";
		}
		html += "</tr>";
		html += "</thead>";
		html +="<tbody >";
		//Array-----------Array-----------Array-----------Array-----------Array-----------Array-----------
		ArrayList<String> dimClaves = new ArrayList<String>();
		ArrayList<String> dimCvPag = new ArrayList<String>();
		//Array-----------Array-----------Array-----------Array-----------Array-----------Array-----------
		while (it.hasNext()) {
			Object clave = it.next();
			Object valor = head.get(clave);
			//System.out.println("Valor "+valor);
			dimId.add((String) valor);
			dimClaves.add((String)valor);//-------Nomm
			dtNom.put((String)valor, (String) valor);//------Nomm
			cont++;
			String med_1 = null;
			String med_2 = null;
			String med_3 = null;
			String med_4 = null;
			for (int j = Integer.parseInt(anioFin); j <= Integer
					.parseInt(anioFin); j++) {

				if (body.get(ord_med.get("med_1")+"_" + j + "_" + (String) valor) != null) {
					//System.out.println("Med_1 "+ord_med.get("med_1")+"_" + j + "_"	+ (String) valor);
					med_1 = (String) body.get(ord_med.get("med_1")+"_" + j + "_"	+ (String) valor);
				} else {
					med_1 = "0";
				}
				//System.out.println("Med_1: ");
				dtMedPag.put((String) valor+"_"+j, med_1);
				dtMedTot.put((String) valor+"_"+j, med_1);
				
				if (body.get(ord_med.get("med_2")+"_" + j + "_" + (String) valor) != null) {
					med_2 = (String) body.get(ord_med.get("med_2")+"_" + j + "_"	+ (String) valor);
				} else {
					med_2 = "0";
				}
				dtMed_2Pag.put((String) valor+"_"+j, med_2);
				dtMed_2Tot.put((String) valor+"_"+j, med_2);
				
				if (body.get(ord_med.get("med_3")+"_" + j + "_" + (String) valor) != null) {
					med_3 = (String) body.get(ord_med.get("med_3")+"_" + j + "_"	+ (String) valor);
				} else {
					med_3 = "0";
				}
				dtMed_3Pag.put((String) valor+"_"+j, med_3);
				dtMed_3Tot.put((String) valor+"_"+j, med_3);
				//System.out.println("BODY MED 4 " + cmp_med_4+"_" + j + "_" + (String) valor); Agregar la caurta medida
				
				if (body.get(ord_med.get("med_4")+"_" + j + "_" + (String) valor) != null) {
					med_4 = (String) body.get(ord_med.get("med_4")+"_" + j + "_"	+ (String) valor);
				} else {
					med_4 = "0";
				}
				dtMed_4Pag.put((String) valor+"_"+j, med_4);
				dtMed_4Tot.put((String) valor+"_"+j, med_4);
			}
		}
		for(int a=0; a < dimId.size(); a++){
			if(dtNom.get(dimId.get(a)) != null){
				dimCvPag.add(dtNom.get(dimId.get(a)));
			}
		}
		
		String val = null;
		String medd_1 = null;
		String clv = null;
		String clv_tot = null;
		String val_2 = null;
		String val_3 = null;
		
		String medd_2 = null;
		String medd_3 = "";
		String medd_4 = "";
		String clv_2 = null;
		String clv_3 = null;
		double sumTotal = 0;
		double sumTotal_2 = 0;
		double sumTotal_3 = 0;
		double sumTotal_4 = 0;
		int contsum = 0;
		double perc_dif_0 = 0;
		double perc_dif_1 = 0;
		double perc_dif_2 = 0;
		double perc_contrib_1 = 0;
		double perc_contrib_2 = 0;
		double perc_contrib_3 = 0;
		double total_perc_contrib_fact = 0;
		double total_perc_contrib_org = 0;
		double total_perc_contrib_mod = 0;
		double total_perc_dif_0 = 0;
		double total_perc_dif_1 = 0;
		double total_perc_dif_2 = 0;
		//Totales para % de Contribucion
		double total1 = 0;
		double total2 = 0;
		double total3 = 0;
		double total4 = 0;
		//Obtiene totales para el porcentaje de contribucion
		for(int a=0; a < dimCvPag.size(); a++){
			clv_tot = dtNom.get(dimCvPag.get(a));
			//System.out.println("Cve total "+clv_tot);
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				String medd1 = dtMedTot.get(clv_tot+"_"+y);
				String medd2 = dtMed_2Tot.get(clv_tot+"_"+y);
				String medd3 = dtMed_3Tot.get(clv_tot+"_"+y);
				String medd4 = dtMed_4Tot.get(clv_tot+"_"+y);
				total1 += Double.parseDouble(medd1);
				total2 += Double.parseDouble(medd2);
				total3 += Double.parseDouble(medd3);
				total4 += Double.parseDouble(medd4);
				
			}
		}
		//System.out.println((total1+"--"+total2+"--"+total3));
		HashMap<String, String> sumaTotal = new HashMap<String, String>();
		HashMap<String, String> sumaTotal_2 = new HashMap<String, String>();
		HashMap<String, String> sumaTotal_3 = new HashMap<String, String>();
		HashMap<String, String> sumaTotal_4 = new HashMap<String, String>();
		double val_med_1 = 0;
		double val_med_2 = 0;
		double val_med_3 = 0;
		double val_med_4 = 0;
		double dif_pptos =0;
		double dif_fact_org = 0;
		double dif_fact_mod = 0;
		double perc_cub_ptto = 0;
		double perc_cub_org_fact = 0;
		double perc_cub_mod_fact = 0;
		double perc_cont_org = 0;
		double perc_cont_mod = 0;
		double perc_cont_fact = 0;
		double total_dif_pptos = 0;
		double total_dif_fact_org = 0;
		double total_dif_fact_mod = 0;
		double total_perc_cub_ppto = 0;
		double total_perc_cub_org_fact = 0;
		double total_perc_cub_org_mod = 0;
		double total_perc_cont_1 = 0;
		double total_perc_cont_2 = 0;
		double total_perc_cont_3 = 0;
		
		double prb_fact_ttl = 0; //Prueba total facturado
		double prb_pptoO_ttl = 0; //Prueba total facturado
		double prb_pptoM_ttl = 0; //Prueba total facturado
		String fnt_md1 = "";
		String fnt_md2 = "";
		String fnt_md3 = "";
		String fnt_md4 = "";
		String fnt_dif1 = "";
		String fnt_dif2 = "";
		String fnt_dif3 = "";
		for(int a=0; a < dimCvPag.size(); a++){
			
			
				
			
			clv = dtNom.get(dimCvPag.get(a));
			
			val = pptos.getDimNameTable(chart_id, cust_id,(String) dtNom.get(dimCvPag.get(a)), id_modulo);
			String val_encode =  URLEncoder.encode(val, "ISO-8859-1");
			String ruta = "popup.jsp?id_user="+id_user+"&id_modulo=2&id_dashboard="+id_dashboard+"&id_chart="+
			chart_id+"&parametro="+cmp_filtro+"&val_param="+clv+"&val_org="+val_encode+"&nom_org="+xAxisName+"&delFil=true";
			
			if(popup){
				link_popup = "  <a href='#' onclick=creaSubGrid('"+clv+"','"+cmp_filtro+"','"+chart_id+"','"+val_encode+"','"+val_encode+"');><strong>+</strong></a>";
			}else{
				link_popup = "<a href='parametros_popup.jsp?valor="
								+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
								+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"' target='hidden'>+<a>";
			}
			
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				
				medd_1 = dtMedPag.get(clv+"_"+y);
				medd_2 = dtMed_2Pag.get(clv+"_"+y);
				medd_3 = dtMed_3Pag.get(clv+"_"+y);
				medd_4 = dtMed_4Pag.get(clv+"_"+y);
				if(medd_3 == null){
					medd_3 = "0";
				}
				perc_dif_0 = 0;
				perc_dif_1 = 0;
				perc_dif_2 = 0;
				perc_contrib_1 = 0;
				perc_contrib_2 = 0;
				perc_contrib_3 = 0;
				fnt_md1 = "";
				fnt_md2 = "";
				fnt_md3 = "";
				fnt_dif1 = "";
				fnt_dif2 = "";
				fnt_dif3 = "";
				//total_2 = Double.parseDouble(medd_2); //Suma Total
				sumTotal = sumTotal  + (int)Double.parseDouble(medd_1);
				
				sumTotal_2 = sumTotal  + (int)Double.parseDouble(medd_2);
				sumTotal_3 = sumTotal  + (int)Double.parseDouble(medd_3);
				sumTotal_4 = sumTotal  + (int)Double.parseDouble(medd_4);
			
				/*++++++++++++++++*/
				sumaTotal.put(contsum+"_"+Integer.toString(y), medd_1);
				sumaTotal_2.put(contsum+"_"+Integer.toString(y), medd_2);
				sumaTotal_3.put(contsum+"_"+Integer.toString(y), medd_3);
				sumaTotal_4.put(contsum+"_"+Integer.toString(y), medd_4);
				
				DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
				simbolo.setDecimalSeparator('.');
				simbolo.setGroupingSeparator(',');
				DecimalFormat format_med = new DecimalFormat("###,###.####",simbolo);
				//Valores de las columnas d ela tabla
				
				String _perc_contrib_1 = "";
				String _perc_contrib_2 = "";
				String _perc_contrib_3 = "";
				
				String frmt_1 = "";
				String frmt_2 = "";
				String frmt_3 = "";
				String frmt_4 = "";
				String frmt_dif_pptos = "";
				String frmt_dif_fact_org = "";
				String frmt_dif_fact_mod = "";
				
				String _perc_cub_ptto = "";
				String _perc_cub_org_fact = "";
				String _perc_cub_mod_fact = "";
				
				
				
				if(sts_fact && sts_po && sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					
					val_med_1 = Double.parseDouble(medd_1);
					prb_fact_ttl += Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
				
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
				
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					
					val_med_3 = Double.parseDouble(medd_3);
					if(val_med_3 < 0){
						fnt_md3 = "<font color=red>";
					}
					
					if(val_med_3 != 0){
						frmt_3 = format_med.format(val_med_3);
					}
				
					if(val_med_1 > 0 || val_med_1 < 0 || val_med_2 > 0 || val_med_2 < 0 || val_med_3 > 0 || val_med_3 < 0 ){
						//System.out.println("-------"+a);
						html += "<tr>";
						
						html += "<td>"+link_popup+val+"</td>";////////Nombre
						//System.out.println( medd_1 + "-"+ medd_2+"-"+medd_3);
					//}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md3+frmt_3+ "</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md3+frmt_1+ "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					dif_pptos = val_med_3 - val_med_2;
					//System.out.println("dif pptos "+dif_pptos);
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
				
					if(dif_pptos != 0){
						frmt_dif_pptos = format_med.format(dif_pptos);
					}
					
					dif_fact_org = val_med_1 - val_med_2;
					//System.out.println("dif_fact_org "+dif_fact_org);
					if(dif_fact_org < 0){
						fnt_dif2 = "<font color=red>";
					}
					total_dif_fact_org += dif_fact_org;
					
					if(dif_fact_org != 0){
						frmt_dif_fact_org= format_med.format(dif_fact_org);
					}
					dif_fact_mod = val_med_1 - val_med_3;
					//System.out.println("dif_fact_mod "+dif_fact_mod);
					if(dif_fact_mod < 0){
						fnt_dif3 = "<font color=red>";
					}
					total_dif_fact_mod += dif_fact_mod;
					
					if(dif_fact_mod != 0){
						frmt_dif_fact_mod = format_med.format(dif_fact_mod);
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif2+frmt_dif_fact_org+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif3+frmt_dif_fact_mod+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					//System.out.println(val_med_3 / val_med_2);
					
					if(val_med_2 > 0){
						perc_cub_ptto = ((val_med_3 / val_med_2) * 100);
					}else{
						perc_cub_ptto = 0;
					}
					
					BigDecimal bd_perc_cub_ptto = new BigDecimal(perc_cub_ptto);
					if(perc_cub_ptto != 0){
						BigDecimal bigDecimalRedondeado = bd_perc_cub_ptto.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_ptto = String.valueOf(bigDecimalRedondeado) +" %";
					}
					total_perc_cub_ppto += perc_cub_ptto;
					if(val_med_2 > 0){
						perc_cub_org_fact= ((val_med_1 / val_med_2) * 100);
					}else{
						perc_cub_org_fact= 0;
					}
					 
					BigDecimal bd_perc_cub_org_fact = new BigDecimal(perc_cub_org_fact);
					if(perc_cub_org_fact != 0){
						BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_org_fact = String.valueOf(rd_perc_cub_org_fact+" %");
					}
					total_perc_cub_org_fact += perc_cub_org_fact;
					if(val_med_3 > 0){
						perc_cub_mod_fact = ((val_med_1 / val_med_3) * 100);
					}else{
						perc_cub_mod_fact = 0;
					}
					BigDecimal bd_perc_cub_mod_fact = new BigDecimal(perc_cub_mod_fact);
					if(perc_cub_mod_fact != 0){
						BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_mod_fact = String.valueOf(rd_perc_cub_mod_fact+" %");
					}
					total_perc_cub_org_mod += perc_cub_mod_fact;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_ptto+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_org_fact+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_mod_fact+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					
					if(total1 > 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
					}
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 + " %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					if(total2 >0){
						perc_contrib_2 = ((val_med_2 /total2) * 100);
					}
					BigDecimal bd_perc_contrib_2 = new BigDecimal(perc_contrib_2);
					if(perc_contrib_2 != 0){
						BigDecimal rd_perc_contrib_2 = bd_perc_contrib_2.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_2 = rd_perc_contrib_2 + " %";
					}
					total_perc_cont_2 += Double.parseDouble(bd_perc_contrib_2.toString());
					if(total3 > 0){
						perc_contrib_3 = ((val_med_3 /total3) * 100);
					}
					BigDecimal bd_perc_contrib_3 = new BigDecimal(perc_contrib_3);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_3 = bd_perc_contrib_3.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_3 = rd_perc_contrib_3 + " %";
					}
					total_perc_cont_3 += Double.parseDouble(bd_perc_contrib_3.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_2+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_3+"</td>";
					
						html += "<tr style='display: none' id=row_"+clv+"><td colspan=13><a href='javascript:hideRow("+clv+")'>"+img+"</a><div id=box"+clv+"></div></tr>";
					
					}
					
					html += "</tr>";
					

				}
				if(sts_fact && sts_po && !sts_pm ){
					//System.out.println("<<<< "+ medd_1 + " _ " + medd_2);
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					_perc_cub_org_fact = "";
					//Ppto Orig //Ppto Modif //Facturacion 
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						 frmt_1 = format_med.format(val_med_1);
					}
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
					//String frmt_2 = "";
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					if(val_med_1 > 0 || val_med_1 < 0 || val_med_2 > 0 || val_med_2 < 0){
						html += "<tr>";
						//if(!medd_1.equals("0.0") && !medd_2.equals("0.0") ){
						//System.out.println(">>>> "+ medd_1 + " _ " + medd_2);
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
						
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+ "</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+ "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					
					dif_pptos = val_med_1 - val_med_2;
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
					//String frmt_dif_pptos = "";
					if(dif_pptos != 0){
						frmt_dif_pptos= format_med.format(dif_pptos);
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					perc_cub_org_fact = 0;
					if(val_med_2 != 0){
						perc_cub_org_fact= ((val_med_1 / val_med_2) * 100);
					}
					//String _perc_cub_org_fact = "";
					BigDecimal bd_perc_cub_org_fact = new BigDecimal(perc_cub_org_fact);
					if(perc_cub_org_fact != 0){
						BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_org_fact = rd_perc_cub_org_fact + " %";
					}
					total_perc_cub_org_fact += perc_cub_org_fact;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_org_fact+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					
					if(total2 > 0){
						perc_contrib_1 = ((val_med_2 /total2) * 100);
					}
					//total_perc_cont_1 += perc_contrib_1;
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 + " %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					if(total1 > 0){
						perc_contrib_2 = ((val_med_1 /total1) * 100);
					}
					//total_perc_cont_2 += perc_contrib_2;
					BigDecimal bd_perc_contrib_2 = new BigDecimal(perc_contrib_2);
					//String _perc_contrib_2 = "";
					if(perc_contrib_2 != 0){
						BigDecimal rd_perc_contrib_2 = bd_perc_contrib_2.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_2 = rd_perc_contrib_2 + " %";
					}
					total_perc_cont_2 += Double.parseDouble(bd_perc_contrib_2.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_2+"</td>";	
					//Tendencia
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
					
						html += "<tr style='display: none' id=row_"+clv+"><td colspan=13><a href='javascript:hideRow("+clv+")'>"+img+"</a><div id=box"+clv+"></div></tr>";
					
					}
					html += "</tr>";
				}
				if(sts_fact && sts_pm && !sts_po){
					//System.out.println("<<<< "+medd_1+" _ "+medd_2);
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					//Ppto Orig //Ppto Modif //Facturacion
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
					//String frmt_2 = "";
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					if(val_med_1 > 0 || val_med_2 > 0 || val_med_1 < 0 || val_med_2 < 0 ){
						html += "<tr>";
						//System.out.println(">>>> "+val_med_1+" _ "+val_med_2);
						//if(!medd_1.equals("0.0") || !medd_2.equals("0.0") ){
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					dif_pptos = val_med_1 - val_med_2;
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
					//String frmt_dif_pptos = "";
					if(dif_pptos != 0){
						frmt_dif_pptos= format_med.format(dif_pptos);
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";;
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					perc_cub_mod_fact = 0;
					if(val_med_2 != 0){
						perc_cub_mod_fact= Math.rint((val_med_1 / val_med_2) * 100);
					}
					//String _perc_cub_mod_fact = "";
					if(perc_cub_mod_fact != 0){
						_perc_cub_mod_fact = String.valueOf(perc_cub_mod_fact) + " %";
					}
					total_perc_cub_org_mod += perc_cub_mod_fact;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_mod_fact+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					if(total2 != 0){
						perc_contrib_1 = Math.rint((val_med_2 /total2) * 100);
					}
					//String _perc_contrib_1 = ""; 
					if(perc_contrib_1 != 0){
						_perc_contrib_1 = perc_contrib_1 + " %";
					}
					total_perc_cont_1 += perc_contrib_1;
					if(total1 != 0){
						perc_contrib_2 = Math.rint((val_med_1 /total1) * 100);
					}
					//String _perc_contrib_2 = ""; 
					if(perc_contrib_2 != 0){
						_perc_contrib_2 = perc_contrib_2 + " %";
					}
					total_perc_cont_2 += perc_contrib_2;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_2+"</td>";
					//Tendencia
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
					
						html += "<tr style='display: none' id=row_"+clv+"><td colspan=13><a href='javascript:hideRow("+clv+")'>"+img+"</a><div id=box"+clv+"></div></tr>";
					
					}
					html += "</tr>";
				}
				if(!sts_fact && sts_po && sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					//System.out.println("<<<< "+medd_1+" _ "+medd_2);
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					val_med_2 = Double.parseDouble(medd_2);
					if(val_med_2 < 0){
						fnt_md2 = "<font color=red>";
					}
					//String frmt_2 = ""; 
					if(val_med_2 != 0){
						frmt_2 = format_med.format(val_med_2);
					}
					
					//Medidas MU original y modificado...
					val_med_3 = Double.parseDouble(medd_3);
					if(val_med_3 < 0){
						fnt_md3 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_3 != 0){
						frmt_3 = format_med.format(val_med_3);
					}
					
					val_med_4 = Double.parseDouble(medd_4);
					if(val_med_4 < 0){
						fnt_md4 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_4 != 0){
						frmt_4 = format_med.format(val_med_4);
					}
					if(val_med_1 > 0 || val_med_2 > 0 || val_med_1 < 0 || val_med_2 < 0 ){
						html += "<tr>";
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md2+frmt_2+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
					dif_pptos = val_med_2 - val_med_1;
					if(dif_pptos < 0){
						fnt_dif1 = "<font color=red>";
					}
					total_dif_pptos += dif_pptos;
					//String frmt_dif_pptos= "";
					if(dif_pptos != 0){
						frmt_dif_pptos = format_med.format(dif_pptos);
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
					perc_cub_ptto = 0;
					if(val_med_1 != 0){
						perc_cub_ptto= ((val_med_2 / val_med_1) * 100);
					}
					BigDecimal bd_perc_cub_ptto = new BigDecimal(perc_cub_ptto);
					//String _perc_cub_ptto = "";
					if(perc_cub_ptto != 0){
						BigDecimal rd_perc_cub_ptto = bd_perc_cub_ptto.setScale(1, RoundingMode.HALF_UP);
						_perc_cub_ptto = String.valueOf(rd_perc_cub_ptto) +" %";
					}
					total_perc_cub_ppto += perc_cub_ptto;
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_cub_ptto+"</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
					}
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					//String _perc_contrib_1 ="";
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 +" %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					if(total2 != 0){
						
						perc_contrib_2 = ((val_med_2 /total2) * 100);
					}
					BigDecimal bd_perc_contrib_2 = new BigDecimal(perc_contrib_2);
					//String _perc_contrib_2 ="";
					if(perc_contrib_2 != 0){
						BigDecimal rd_perc_contrib_2 = bd_perc_contrib_2.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_2 = rd_perc_contrib_2 + " %";
					}
					total_perc_cont_2 += Double.parseDouble(bd_perc_contrib_2.toString());
					html += "<td align='right' valign='top'>"+_perc_contrib_1+"</td>";
					html += "<td align='right' valign='top'>"+_perc_contrib_2+"</td>";
					//%MU
					if(cc == "" || cc == null && gpo){
						html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_3+"</td>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_4+"</td>";
					}
					
						html += "<tr style='display: none' id=row_"+clv+"><td colspan=13><a href='javascript:hideRow("+clv+")'>"+img+"</a><div id=box"+clv+"></div></tr>";
					
					}
					html += "</tr>";
				}
				if(sts_fact && !sts_po && !sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					if(val_med_1 > 0 || val_med_1 < 0){
						html += "<tr>";
						//if(!medd_1.equals("0.0")){
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1+ "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
					}
					//String _perc_contrib_1 = "";
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 +" %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					
						html += "<tr style='display: none' id=row_"+clv+"><td colspan=13><a href='javascript:hideRow("+clv+")'>"+img+"></a><div id=box"+clv+"></div></tr>";
					
					}
					html += "</tr>";
				}
				if(!sts_fact && sts_po && !sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion 
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					if(val_med_1 > 0 || val_med_1 < 0){
						html += "<tr>";
						//if(!medd_1.equals("0.0")){
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1 + "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
					}
					//String _perc_contrib_1 = "";
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 +" %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					if(!excel){
						html += "<tr style='display: none' id=row_"+clv+"><td colspan=13><a href='javascript:hideRow("+clv+")'>"+img+"</a><div id=box"+clv+"></div></tr>";
					}
				}
					html += "</tr>";
				}
				if(!sts_fact && !sts_po && sts_pm){
					//Ppto Orig //Ppto Modif //Facturacion
					
					prb_fact_ttl += Double.parseDouble(medd_1);
					val_med_1 = Double.parseDouble(medd_1);
					if(val_med_1 < 0){
						fnt_md1 = "<font color=red>";
					}
					//String frmt_1 = "";
					if(val_med_1 != 0){
						frmt_1 = format_med.format(val_med_1);
					}
					if(val_med_1 > 0 || val_med_1 < 0){
						//if(!medd_1.equals("0.0")){
						html += "<tr>";
						html += "<td>"+link_popup+
								"  <a href='parametros.jsp?valor="
							+clv+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName
							+"&nomValue="+val+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup="+org_param+"'' target='hidden'>"+val+"<a></td>";////////Nombre
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_md1+frmt_1 + "</td>";
					//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
					if(total1 != 0){
						perc_contrib_1 = ((val_med_1 /total1) * 100);
					}
					//String _perc_contrib_1 = "";
					BigDecimal bd_perc_contrib_1 = new BigDecimal(perc_contrib_1);
					if(perc_contrib_1 != 0){
						BigDecimal rd_perc_contrib_1 = bd_perc_contrib_1.setScale(1, RoundingMode.HALF_UP);
						_perc_contrib_1 = rd_perc_contrib_1 + " %";
					}
					total_perc_cont_1 += Double.parseDouble(bd_perc_contrib_1.toString());
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+_perc_contrib_1+"</td>";
					
						html += "<tr style='display: none' id=row_"+clv+"><td colspan=13><a href='javascript:hideRow("+clv+")'>"+img+"</a><div id=box"+clv+"></div></tr>";
					
					}
					html += "<tr>";
				}
				//html += "</tr>";		
			}
			contsum++;
			////System.out.println("Total: " + sumTotal);
			//html += html_med_2;
			
		}
		int total = 0;
		int total_2 = 0;
		int total_3 = 0;
		int total_4 = 0;
		int contS = 0;
		int contS_2 = 0;
		int contS_3 = 0;
		int contS_4 = 0;
		////System.out.println("ContS = "+ contS);
		//Iterator itt = sumaTotal.entrySet().iterator();
		html += "<tr><td><font color=red><strong>Total</strong></td>";
		for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
			
			if(contS != 0){
				total = 0;
				total_2 = 0;
				contS = 0;
				contS_2 = 0;
				total_3 = 0;
				contS_3 = 0;
				total_4 = 0;
				contS_4 = 0;
			}
			
			Iterator itt = sumaTotal.entrySet().iterator();
			while (itt.hasNext()) {
			
				Map.Entry e = (Map.Entry)itt.next();
				String cmp = contS+"_"+Integer.toString(y);
				String cant = (String)sumaTotal.get(cmp);
				
				if(cant != null){
					double ct = Double.parseDouble(cant);
					int sum = (int) ct;
					total = total + sum;
					
				}
				contS ++;	
			}
			
			//Total para la medida_2
			Iterator itt_2 = sumaTotal_2.entrySet().iterator();
			while (itt_2.hasNext()) {
			
				Map.Entry e_2 = (Map.Entry)itt_2.next();
				String cmp_2 = contS_2+"_"+Integer.toString(y);
				String cant_2 = (String)sumaTotal_2.get(cmp_2);
				
				if(cant_2 != null){
					double ct_2 = Double.parseDouble(cant_2);
					int sum_2 = (int) ct_2;
					total_2 = total_2 + sum_2;
					
				}
				contS_2 ++;	
			}
			
			//Total para la medida_3
			Iterator itt_3 = sumaTotal_3.entrySet().iterator();
			while (itt_3.hasNext()) {
			
				Map.Entry e_3 = (Map.Entry)itt_3.next();
				String cmp_3 = contS_3+"_"+Integer.toString(y);
				String cant_3 = (String)sumaTotal_3.get(cmp_3);
				
				if(cant_3 != null){
					double ct_3 = Double.parseDouble(cant_3);
					int sum_3 = (int) ct_3;
					total_3 = total_3 + sum_3;
				}
				contS_3 ++;	
			}
			
			//Total para la medida_4
			Iterator itt_4 = sumaTotal_4.entrySet().iterator();
			while (itt_4.hasNext()) {
			
				Map.Entry e_4 = (Map.Entry)itt_4.next();
				String cmp_4 = contS_4+"_"+Integer.toString(y);
				String cant_4 = (String)sumaTotal_4.get(cmp_4);
				
				if(cant_4 != null){
					double ct_4 = Double.parseDouble(cant_4);
					int sum_4 = (int) ct_4;
					total_4 = total_4 + sum_4;
					
				}
				contS_4 ++;	
			}
			
			DecimalFormatSymbols simb = new DecimalFormatSymbols();
			simb.setDecimalSeparator('.');
			simb.setGroupingSeparator(',');
			DecimalFormat format_med = new DecimalFormat("###,###.####",simb);
			
			String sumaTot = format_med.format(total);
			String sumaTot_2 = format_med.format(total_2);
			String sumaTot_3 = format_med.format(total_3);
			String sumaTot_4 = format_med.format(total_4);
			
			//Totales para las columnas
			if(sts_fact && sts_po && sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				String frmt_2 = format_med.format(total2);
				String frmt_3 = format_med.format(total3);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_3+ "</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+ "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				if(total_dif_fact_org  < 0){
					fnt_dif2 = "<font color=red>";
				}
				if(total_dif_fact_mod  < 0){
					fnt_dif3 = "<font color=red>";
				}
				String frmt_dif_pptos= format_med.format(total_dif_pptos);
				String frmt_dif_fact_org= format_med.format(total_dif_fact_org);
				String frmt_dif_fact_mod = format_med.format(total_dif_fact_mod);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif2+frmt_dif_fact_org+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif3+frmt_dif_fact_mod+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//System.out.println(val_med_3 / val_med_2);
				BigDecimal bd_perc_cub_pttos= new BigDecimal(total_perc_cub_ppto);
				BigDecimal rd_perc_cub_pttos = bd_perc_cub_pttos.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_mod_fact = new BigDecimal(total_perc_cub_org_mod);
				BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_DOWN);
				
				String ttl_perc_cub_pttos = "";
				String ttl_perc_cub_org_fact = "";
				String ttl_perc_cub_mod_fact = "";
				if(total_perc_cub_ppto != 0.0){
					ttl_perc_cub_pttos = rd_perc_cub_pttos +" %";
				}
				if(total_perc_cub_org_fact != 0.0){
					ttl_perc_cub_org_fact = rd_perc_cub_org_fact + " %";
				}
				if(total_perc_cub_org_mod != 0.0){
					ttl_perc_cub_mod_fact = rd_perc_cub_mod_fact +" %";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+ttl_perc_cub_pttos+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+ttl_perc_cub_org_fact+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+ttl_perc_cub_mod_fact+" %</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(total_perc_cont_1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(total_perc_cont_2);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_3 = new BigDecimal(total_perc_cont_3);
				BigDecimal rd_total_perc_cont_3 = bd_total_perc_cont_3.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_3+" %</td>";
			}
			if(sts_fact && sts_po && !sts_pm ){
				//Ppto Orig //Ppto Modif //Facturacion 
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				String frmt_2 = format_med.format(total2);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+ "</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+ "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				String frmt_dif_fact_org= format_med.format(total_dif_pptos);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_fact_org+" </td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//perc_cub_org_fact= Math.rint((val_med_1 / val_med_2) * 100);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_perc_cub_org_fact+" %</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				//perc_contrib_1 = Math.rint((val_med_2 /total2) * 100);
				//perc_contrib_2 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(total_perc_cont_1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(total_perc_cont_2);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";	
				//Tendencia
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			}
			if(sts_fact && sts_pm && !sts_po){
				//Ppto Orig //Ppto Modif //Facturacion
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				//val_med_2 = Double.parseDouble(medd_2);
				String frmt_2 = format_med.format(total_2);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				//dif_pptos = val_med_1 - val_med_2;
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				
				String frmt_dif_fact_org= format_med.format(total_dif_pptos);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_fact_org+"</td>";;
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//perc_cub_mod_fact= Math.rint((val_med_1 / val_med_2) * 100);
				BigDecimal bd_perc_cub_pttos= new BigDecimal(total_perc_cub_ppto);
				BigDecimal rd_perc_cub_pttos = bd_perc_cub_pttos.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_mod_fact = new BigDecimal(total_perc_cub_org_mod);
				BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_perc_cub_mod_fact+" %</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				//perc_contrib_1 = Math.rint((val_med_2 /total2) * 100);
				//perc_contrib_2 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(total_perc_cont_1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(total_perc_cont_2);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";
				//Tendencia
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			}
			if(!sts_fact && sts_po && sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				//val_med_2 = Double.parseDouble(medd_2);
				String frmt_2 = format_med.format(total2);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+"</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_2+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Diferencia
				//dif_pptos = val_med_1 - val_med_2;
				if(total_dif_pptos  < 0){
					fnt_dif1 = "<font color=red>";
				}
				
				String frmt_dif_pptos= format_med.format(total_dif_pptos );
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fnt_dif1+frmt_dif_pptos+"</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Cubrimiento
				//perc_cub_ptto= Math.rint((val_med_1 / val_med_2) * 100);
				BigDecimal bd_perc_cub_pttos= new BigDecimal(total_perc_cub_ppto);
				BigDecimal rd_perc_cub_pttos = bd_perc_cub_pttos.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_org_fact = new BigDecimal(total_perc_cub_org_fact);
				BigDecimal rd_perc_cub_org_fact = bd_perc_cub_org_fact.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_perc_cub_mod_fact = new BigDecimal(total_perc_cub_org_mod);
				BigDecimal rd_perc_cub_mod_fact = bd_perc_cub_mod_fact.setScale(1, RoundingMode.HALF_DOWN);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_perc_cub_pttos+" %</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> % Contribucion
				//perc_contrib_1 = Math.rint((val_med_2 /total2) * 100);
				//perc_contrib_2 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(total_perc_cont_1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				BigDecimal bd_total_perc_cont_2 = new BigDecimal(total_perc_cont_2);
				BigDecimal rd_total_perc_cont_2 = bd_total_perc_cont_2.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_2+" %</td>";
				//%MU
				if(cc == "" || cc == null && gpo){
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+sumaTot_3+"</td>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+sumaTot_4+"</td>";
				}
			}
			if(sts_fact && !sts_po && !sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1+ "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
				//perc_contrib_1 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(total_perc_cont_1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
			}
			if(!sts_fact && sts_po && !sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion 
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1 + "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
				//perc_contrib_1 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(total_perc_cont_1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
			}
			if(!sts_fact && !sts_po && sts_pm){
				//Ppto Orig //Ppto Modif //Facturacion 
				//val_med_1 = Double.parseDouble(medd_1);
				//String frmt_1 = format_med.format(total1);
				String frmt_1 = format_med.format(prb_fact_ttl);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt_1 + "</td>";
				//Ppto Orig //Ppto Modif //Facturacion --> Contribucion
				//perc_contrib_1 = Math.rint((val_med_1 /total1) * 100);
				BigDecimal bd_total_perc_cont_1 = new BigDecimal(total_perc_cont_1);
				BigDecimal rd_total_perc_cont_1 = bd_total_perc_cont_1.setScale(1, RoundingMode.HALF_DOWN);
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_total_perc_cont_1+" %</td>";
			}
		}
		html += "</tr>";
		html += "</tbody>";
		html += "</table>";
		System.out.println("Tabla: "+html);
		return html;
	}
	public String getLinkCtaCve(
			String cust_id,
			String id_user,
			String id_dashboard,
			String chart_id,
			String id_modulo,
			String ppto,
			String anioIni,
			String anioFin,
			String gpoMed,
			String cta_cve){
		String xml = "";
		HashMap<String, String> dtPag = new HashMap<String, String>();
		//Configuracion del portlet
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id, id_modulo);
		String xAxisName = (String) hm.get("xAxisName");
		String cmp_filtro = (String) hm.get("cmp_id");
		String name = getNameCC(cta_cve);
		//System.out.println("FACTTTT "+hm.get("medida4"));
		/*String caption = (String) hm.get("caption");
		String xAxisName = (String) hm.get("xAxisName");
		String yAxisName = (String) hm.get("yAxisName");
		String numberPrefix = (String) hm.get("numberPrefix");
		String tbl_dim = (String) hm.get("tbl_dim");
		String cmp_filtro = (String) hm.get("cmp_id");
		String tipo_orden = "";*/
		//Obtener total por cta_cve
		
		
		//Obtener target
		
		
		//Obtener limite superior
		
		//HashMap med_ctacve = getDatosCtaCve(id_user, id_dashboard, id_modulo);
		
		//Proceso obtener medidas para los ctacve
		String fact = "";
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		//String target = "";
		String total_fact = "";
		String target = "";
		double limite = 0;
		
		HashMap medidas = getDatosCtaCve(cust_id, id_user, chart_id, id_dashboard, id_modulo);
		Iterator it = medidas.entrySet().iterator();
		Map.Entry e;
		String valor = "";
		String id_val = "";
		String cmp_fact ="";
		String cmp_target ="";
		if(sts_fact){
			if(sts_po && !sts_pm && ppto.equals("2")){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}
					/*if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}*/
				}
				//total_fact = getTotalCtaCveFact(cta_cve, fact, id_user, id_dashboard);
				//target = getTargetCtacve(cta_cve,cmp_target, id_user, id_dashboard);
			}
			if(sts_pm && !sts_po && ppto.equals("3")){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					/*if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}*/
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				//total_fact = getTotalCtaCveFact(cta_cve, fact, id_user, id_dashboard);
				//target = getTargetCtacve(cta_cve,cmp_target, id_user, id_dashboard);
			}
			if(sts_fact && sts_po && sts_pm){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				//total_fact = getTotalCtaCveFact(cta_cve, fact, id_user, id_dashboard);
				//target = getTargetCtacve(cta_cve,cmp_target, id_user, id_dashboard);
			}
			
		}
		
		if(target != null && target != ""){
			limite = (Double.parseDouble(target)+ Double.parseDouble(target) * (.20));
		}else{
			limite= 0;
			//(String) Collections.max(max);
		}
		xml = "parametros.jsp?valor="+cta_cve+"&parametro="+cmp_filtro+"&chartID="+chart_id+"&nombre="+xAxisName+"&nomValue="+name+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&popup=false";	
		
		return xml;
	}
	public String getChartCtaCve(
			String cust_id,
			String id_user,
			String id_dashboard,
			String chart_id,
			String id_modulo,
			String ppto,
			String anioIni,
			String anioFin,
			String gpoMed,
			String cta_cve){
		String xml = "";
		HashMap<String, String> dtPag = new HashMap<String, String>();
		//Configuracion del portlet
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id, id_modulo);
		String xAxisName = (String) hm.get("xAxisName");
		String cmp_filtro = (String) hm.get("cmp_id");
		String cmp_idfk = (String) hm.get("cmp_idfk");
		String name = getNameCC(cta_cve);
		
		String fact = "";
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_prefix = getStatusGpo(id_user, id_modulo, id_dashboard);
		//String target = "";
		String total_fact = "";
		String target = "";
		String prefix = "";
		double limite = 0;
		
		HashMap medidas = getDatosCtaCve(cust_id, id_user, chart_id, id_dashboard, id_modulo);
		Iterator it = medidas.entrySet().iterator();
		Map.Entry e;
		String valor = "";
		String id_val = "";
		String cmp_fact ="";
		String cmp_target ="";
		System.out.println("Pppto --- "+ppto);
		if(sts_fact){
			if(sts_po && !sts_pm && ppto.equals("2")){
				System.out.println("Fact-Org");
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					//System.out.println(id_val+" -- "+valor);
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						System.out.println("2 -- "+valor+ " nom_cmp "+cmp_target);
					}
					/*if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}*/
				}
				System.out.println("Fact 1 Cmp "+fact);
				System.out.println("Target 1 Cmp "+cmp_target);
				total_fact = getTotalCtaCveFact(cmp_idfk, cta_cve, fact,cust_id, id_user,id_modulo, id_dashboard, chart_id, anioIni);
				target = getTargetCtacve(cmp_idfk, cta_cve, cmp_target,cust_id, id_user,id_modulo, id_dashboard, chart_id, anioIni);
			}
			if(sts_pm && !sts_po && ppto.equals("3")){
				System.out.println("Fact-Mod");
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					//System.out.println(id_val+" -- "+valor);
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					/*if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}*/
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				System.out.println("Fact 2 "+fact);
				total_fact = getTotalCtaCveFact(cmp_idfk, cta_cve, fact, cust_id, id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTargetCtacve(cmp_idfk, cta_cve, cmp_target,cust_id, id_user,id_modulo, id_dashboard, chart_id, anioIni);
			}
			if(sts_fact && sts_po && sts_pm){
				System.out.println("Fact-Org-Mod");
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					//System.out.println(id_val+" -- "+valor);
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				System.out.println("Fact 3 "+fact);
				total_fact = getTotalCtaCveFact(cmp_idfk, cta_cve, fact, cust_id, id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTargetCtacve(cmp_idfk, cta_cve, cmp_target,cust_id, id_user,id_modulo, id_dashboard, chart_id, anioIni);
			}
		
		}
		
		if(target != null && target != ""){
			limite = (Double.parseDouble(target)+ Double.parseDouble(target) * (.20));
		}else{
			limite= 0;
			//(String) Collections.max(max);
		}
		if(sts_prefix){
			prefix = "$";
		}
		if(target == null || target == ""){
			target = "0";
		}
		if(total_fact == null || total_fact == ""){
			total_fact = "0";
		}
		DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
		simbolo.setDecimalSeparator('.');
		simbolo.setGroupingSeparator(',');
		DecimalFormat format_med = new DecimalFormat("###,###.####",simbolo);
		//String sql ="SELECT round(SUM(importe_pre),0) importe_pre, round(SUM(importe_fact),0) importe_fact FROM fact_presupuestos WHERE id_sop_ctaclave='"+cta_cve+"'";
		//SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		//if(rs.next()){
		//System.out.println("Limitee "+limite+" Total_Fact: "+total_fact+" Target: "+target);	
		xml = "<chart palette='3' animation='1' lowerLimit='0' upperLimit='"+limite+"' showShadow='1' caption='"+name+"'  colorRangeFillRatio='0,10,80,10' showColorRangeBorder='0' " +
				"roundRadius='0' numberPrefix='"+prefix+"' numberSuffix='K' showValue='1' showTickMarks='0' showTickValues='0'>"+
			    "<colorRange >"+
		        	"<color minValue='0' maxValue='"+limite+"' code='3399CC' />"+
		        	//"<color minValue='30' maxValue='50' />" +
		        	//"<color minValue='50' maxValue='70' />"+ 
		        	//"<color minValue='70' maxValue='100' />"+ 
		        "</colorRange>"+
		        //"<set  value='"+total_fact+"' link='F-hidden-parametros.jsp'/>" +
		    "<value link='F-hidden-parametros.jsp'>"+total_fact+"</value>"+
		    "<target link='F-hidden-parametros.jsp'>"+target+"</target>"+
		"</chart>";
		//}
		
		xml += "_" +  prefix + format_med.format(Double.parseDouble(target));
		System.out.println(xml);
		return xml;
	}
	
	public String getTotalCtaCve(
			String cust_id,
			String id_user,
			String id_dashboard,
			String chart_id,
			String id_modulo,
			String ppto,
			String anioIni,
			String anioFin,
			String gpoMed,
			String cta_cve){
		String total = "";
		HashMap<String, String> dtPag = new HashMap<String, String>();
		//Configuracion del portlet
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id, id_modulo);
		String xAxisName = (String) hm.get("xAxisName");
		String cmp_filtro = (String) hm.get("cmp_id");
		String cmp_idfk = (String) hm.get("cmp_idfk");
		//Proceso obtener medidas para los ctacve
		String fact = "";
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		//String target = "";
		String total_fact = "0";
		String target = "0";
		double limite = 0;
		
		HashMap medidas = getDatosCtaCve(cust_id, id_user, chart_id, id_dashboard, id_modulo);
		Iterator it = medidas.entrySet().iterator();
		Map.Entry e;
		String valor = "";
		String id_val = "";
		String cmp_fact ="";
		String cmp_target ="";
		if(sts_fact){
			if(sts_po && !sts_pm && ppto.equals("2")){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}
					/*if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}*/
				}
				total_fact = getSumTotalCtaCve(cmp_idfk, cta_cve, fact, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTotalPttoCtacve(cmp_idfk, cta_cve,cmp_target, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				System.out.println("fact: "+ total_fact+" target: "+target+" "+ppto);
			}
			if(sts_pm && !sts_po && ppto.equals("3")){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					/*if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}*/
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				total_fact = getSumTotalCtaCve(cmp_idfk, cta_cve, fact, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTotalPttoCtacve(cmp_idfk, cta_cve,cmp_target, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				System.out.println("fact: "+ total_fact+" target: "+target+" "+ppto);
			}
			if(sts_fact && sts_po && sts_pm){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				total_fact = getSumTotalCtaCve(cmp_idfk, cta_cve, fact, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTotalPttoCtacve(cmp_idfk, cta_cve,cmp_target, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				System.out.println("fact: "+ total_fact+" target: "+target);
			}
		}
		System.out.println("Total Fact -- "+total_fact+" Total Targ -- "+target);
		if(total_fact.isEmpty()){
			System.out.println("iff total");
			total_fact = "0";
		}
		System.out.println("target "+target);
		if(target == "" || target == null){
			System.out.println("iff torget");
			target = "0";
		}
		double frmt_fact = Double.parseDouble(total_fact);
		//System.out.println("Target Total: "+cta_cve+"-"+target);
		double frmt_ppto = Double.parseDouble(target);
		DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
		simbolo.setDecimalSeparator('.');
		simbolo.setGroupingSeparator(',');
		DecimalFormat format_med = new DecimalFormat("###,###.####",simbolo);
		//System.out.println("Fact "+format_med.format(frmt_fact) + " Ptto "+format_med.format(frmt_ppto));
		total = "<strong><font size=2 color=#3399CC>Facturado: "+ format_med.format(frmt_fact)+" Presupuesto: "+ format_med.format(frmt_ppto)+"</font></strong>";
		return total;
	}
	
	//Chart que obtiene los totoales presupuestados
	public String getTotalChartCtaCve(
			String cust_id,
			String id_user,
			String id_dashboard,
			String chart_id,
			String id_modulo,
			String ppto,
			String anioIni,
			String anioFin,
			String gpoMed,
			String cta_cve){
		String total = "";
		HashMap<String, String> dtPag = new HashMap<String, String>();
		//Configuracion del portlet
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id, id_modulo);
		String xAxisName = (String) hm.get("xAxisName");
		String cmp_filtro = (String) hm.get("cmp_id");
		String cmp_idfk = (String) hm.get("cmp_idfk");
		//Proceso obtener medidas para los ctacve
		String fact = "";
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_prefix = getStatusGpo(id_user, id_modulo, id_dashboard);

		//String target = "";
		String total_fact = "";
		String target = "";
		String prefix = "";
		double limite = 0;
		
		HashMap medidas = getDatosCtaCve(cust_id, id_user, chart_id, id_dashboard, id_modulo);
		Iterator it = medidas.entrySet().iterator();
		Map.Entry e;
		String valor = "";
		String id_val = "";
		String cmp_fact ="";
		String cmp_target ="";
		if(sts_fact){
			if(sts_po && !sts_pm && ppto.equals("2")){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}
					/*if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}*/
				}
				total_fact = getSumTotalCtaCve(cmp_idfk, cta_cve, fact, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTotalPttoCtacve(cmp_idfk, cta_cve,cmp_target, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
			}
			if(sts_pm && !sts_po && ppto.equals("3")){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					/*if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}*/
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				total_fact = getSumTotalCtaCve(cmp_idfk, cta_cve, fact, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTotalPttoCtacve(cmp_idfk, cta_cve,cmp_target, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
			}
			if(sts_fact && sts_po && sts_pm){
				while (it.hasNext()) {
					e = (Map.Entry)it.next();
					id_val = (String) e.getKey();
					valor = (String) e.getValue();
					if(id_val.equals("1")){
						fact = (String)hm.get(valor);
						//System.out.println("1 -- "+valor+" nom_cmp "+ fact);
					}
					if(id_val.equals("2") && ppto.equals("2")){ //2: Ppto org --- 3  Pppto mod
						cmp_target = (String)hm.get(valor);
						//System.out.println("2--"+valor+ "nom_cmp "+cmp_target);
					}
					if(id_val.equals("3") && ppto.equals("3")){
						cmp_target = (String)hm.get(valor);
						//System.out.println("3--"+valor+ "nom_cmp "+cmp_target);
					}
				}
				total_fact = getSumTotalCtaCve(cmp_idfk, cta_cve, fact, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
				target = getTotalPttoCtacve(cmp_idfk, cta_cve,cmp_target, cust_id , id_user, id_modulo, id_dashboard, chart_id, anioIni);
			}
		}
		//double frmt_fact = Double.parseDouble(total_fact);
		//double frmt_ppto = Double.parseDouble(target);
		//DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
		//simbolo.setDecimalSeparator('.');
		//simbolo.setGroupingSeparator(',');
		//DecimalFormat format_med = new DecimalFormat("###,###.####",simbolo);
		//System.out.println("Fact "+format_med.format(frmt_fact) + " Ptto "+format_med.format(frmt_ppto));
		//total = "Facturado: "+ format_med.format(frmt_fact)+" Presupuesto: "+ format_med.format(frmt_ppto);
		if(target != null && target != ""){
			limite = (Double.parseDouble(target)+ Double.parseDouble(target) * (.20));
		}else{
			limite= 0;
			//(String) Collections.max(max);
		}
		if(sts_prefix){
			prefix = "$";
		}
		total = "<chart palette='3' animation='1' lowerLimit='0' upperLimit='"+limite+"' showShadow='1' caption='Total: "+
				"'  colorRangeFillRatio='0,10,80,10' showColorRangeBorder='0' roundRadius='0' numberPrefix='"+prefix+
				"' numberSuffix='K' showValue='1' showTickMarks='0' showTickValues='0'>"+
			    "<colorRange >"+
		        	"<color minValue='0' maxValue='30' code='3399CC' />"+
		        	//"<color minValue='30' maxValue='50' />" +
		        	//"<color minValue='50' maxValue='70' />"+ 
		        	//"<color minValue='70' maxValue='100' />"+ 
		        "</colorRange>"+
		        //"<set  value='"+total_fact+"' link='F-hidden-parametros.jsp'/>" +
		    "<value link='F-hidden-parametros.jsp'>"+total_fact+"</value>"+
		    "<target link='F-hidden-parametros.jsp'>"+target+"</target>"+
		"</chart>";
		return total;
	}
	
	//Obtiene el total facturado para mostrar la liena de avance en el cta cve
	public String getTotalCtaCveFact(String cmp_idfk, String ctacve, String cmp, String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String anio){
		System.out.println("Cmp----"+cmp);
		String total_ctacve = "";
		String filtro = "";
		String fil_cmb = "";
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);
		HashMap parametros = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		HashMap hm = pptos.getDataChartConfig(id_portlet, id_customer,id_modulo);
		String tbl_fact = (String) hm.get("tbl_fact");
		String elemento=(String)hm.get("elemento");
		int cont = 0;
		Iterator it = parametros.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
			if(cont == 0){
				////System.out.println(cmp_idfk+" -- "+e.getKey());
				if(!cmp_idfk.equals(e.getKey())){
					filtro += "AND "+ e.getKey()+"='"+e.getValue()+"' ";
				}
			}
			else{
				//if(!cmp_idfk.equals(e.getKey())){
					//filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					if(filtro==""){
						filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
					}else{
						filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
					}
				//}
				
				
			}
			cont++;
		}
		//if(filtro == ""){
			filtro += "AND "+ tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ;// rango de aï¿½os se modificara para uno solo
		/*}else{
			filtro += "AND "+tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ;// rango de aï¿½os se modificara para uno solo
		}*/
			
		/*if(filtro != null && filtro != ""){
			filtro = "AND " + filtro;
		}*/
		// Obtner rango de meses 
		String rango_meses = pptos.obtieneRangoMes(id_customer, id_user, id_modulo, id_dashboard, id_portlet);
		//System.out.println("Rango Mes "+rango_meses);
		if(rango_meses !=  null && rango_meses != ""){
			//System.out.println("Condicion Filtro...");
			filtro += "AND "+rango_meses;
		}
		//Filtrs segun combinacion
				if(sts_fact && sts_po && !sts_pm && gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".importe_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".importe_prem != 0";
				}
				if(sts_fact && sts_po && !sts_pm && !gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".cant_cajas_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && !gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".cant_cajas_prem != 0";
				}
				filtro = filtro + fil_cmb;
				
		String sql = "SELECT ROUND(SUM("+cmp+"),0) as total FROM "+tbl_fact+" WHERE id_sop_ctaclave='"+ctacve+"' "+ filtro;
		System.out.println("Total_Fact "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			total_ctacve = rs.getString("total");
		}
		return total_ctacve;
	}
	
	public String getSumTotalCtaCve(String cmp_idfk, String ctacve, String cmp, String id_customer, String id_user, String id_modulo, String id_dashboard, String id_chart, String anio){
		String fil_cmb = "";
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);
		String total_ctacve = "0";
		String filtro = "";
		HashMap parametros = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		HashMap hm = pptos.getDataChartConfig(id_chart, id_customer,id_modulo);
		String tbl_fact = (String) hm.get("tbl_fact");
		String elemento=(String)hm.get("elemento");
		int cont = 0;
		Iterator it = parametros.entrySet().iterator();
		while(it.hasNext()){
			
			Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
			if(cont == 0){
				//System.out.println("Cont "+cont);
				//System.out.println(cmp_idfk+" -- "+e.getKey()+" "+!cmp_idfk.equals(e.getKey()));
				if(!cmp_idfk.equals(e.getKey())){
					filtro += e.getKey()+"='"+e.getValue()+"' ";
				}
				//System.out.println("Filtro 0: "+filtro);
			}
			else{
				if(!cmp_idfk.equals(e.getKey())){
					//filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					if(filtro==""){
						filtro += e.getKey()+"='"+e.getValue()+"' ";
					}else{
						filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
					}
				}
				
				
			}
			cont++;
		}
		if(filtro == "" || filtro == null){
			filtro += tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		}else{
			filtro += "AND "+tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		}
		
		//if(filtro != null && filtro != ""){
			filtro = "WHERE " + filtro;
		//}
		String rango_meses = pptos.obtieneRangoMes(id_customer, id_user, id_modulo, id_dashboard, id_chart);
		//System.out.println("Rango Mes "+rango_meses);
		if(filtro != null && filtro != ""){
			if(rango_meses !=  null && rango_meses != ""){
				//System.out.println("Condicion Filtro...");
				filtro += "AND "+rango_meses;
			}
		}else{
			if(rango_meses !=  null && rango_meses != ""){
				filtro += "WHERE "+rango_meses;
			}
		}
		
		//Filtrs segun combinacion
				if(sts_fact && sts_po && !sts_pm && gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".importe_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".importe_prem != 0";
				}
				if(sts_fact && sts_po && !sts_pm && !gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".cant_cajas_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && !gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".cant_cajas_prem != 0";
				}
				if(filtro == null || filtro ==""){
					if(fil_cmb != null && fil_cmb != ""){
						filtro = " WHERE "+fil_cmb;
					}
				}else{
					if(fil_cmb != null && fil_cmb != ""){
						filtro += " AND " +fil_cmb;
					}
				}
				
		String sql = "SELECT ROUND(SUM("+cmp+"),0) as total FROM "+tbl_fact+" "+ filtro;
		System.out.println("Sql SumTolalCtaCve "+sql );
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			total_ctacve = rs.getString("total");
		}
		if(total_ctacve == "" || total_ctacve == null){
			total_ctacve = "0";
		}
		System.out.println("Total_Fact "+sql+"--"+total_ctacve);
		//if(total_ctacve == null || total_ctacve == ""){
			//total_ctacve = "0";
		//}
		return total_ctacve;
	}
	//Obtiene el target ke es el punto acum,plir en cuanto alo facturado para cada cta cve
	public String getTargetCtacve(String cmp_idfk, String ctacve, String cmp, String id_cust, String id_user, String id_modulo, String id_dashboard, String chart_id, String anio){
		String target = "";
		String filtro = "";
		String fil_cmb = "";
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);
		HashMap parametros = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		HashMap hm = pptos.getDataChartConfig(chart_id, id_cust,id_modulo);
		String tbl_fact = (String) hm.get("tbl_fact");
		String elemento=(String)hm.get("elemento");
		int cont = 0;
		Iterator it = parametros.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
			if(cont == 0){
				////System.out.println(cmp_idfk+" -- "+e.getKey());
				if(!cmp_idfk.equals(e.getKey())){
					filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
				}
				System.out.println("Filtro Cont 0 "+filtro);
			}
			else{
				//if(!cmp_idfk.equals(e.getKey())){
					//filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					if(filtro==""){
						filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
						System.out.println("Filtro Cont 0 Else 1 "+filtro);
					}else{
						filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
						System.out.println("Filtro Cont 0 Else 2 "+filtro);
					}
				//}
				
				
			}
			cont++;
		}
		System.out.println("Filtro Param "+filtro);
		//if(filtro == ""){
			filtro += "AND " +tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		/*}else{
			filtro += "AND "+tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		}
		/*if(filtro != null && filtro != ""){
			filtro = "AND " + filtro;
		}*/
		System.out.println("Filtro Anio "+filtro);
		String rango_meses = pptos.obtieneRangoMes(id_cust, id_user, id_modulo, id_dashboard, chart_id);
		System.out.println("Rango Mes "+rango_meses);
		if(rango_meses !=  null && rango_meses != ""){
			System.out.println("Condicion Filtro...");
			filtro += "AND "+rango_meses;
		}
		System.out.println("Filtro Mes "+filtro);
		//Filtrs segun combinacion
				if(sts_fact && sts_po && !sts_pm && gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".importe_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".importe_prem != 0";
				}
				if(sts_fact && sts_po && !sts_pm && !gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".cant_cajas_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && !gpo && id_modulo.equals("2")){
					fil_cmb = "  AND "+tbl_fact+".cant_cajas_prem != 0";
				}
				filtro = filtro + fil_cmb;
				
		String sql = "SELECT ROUND(SUM("+cmp+"),0) as total FROM "+tbl_fact+" WHERE id_sop_ctaclave='"+ctacve+"' "+ filtro;
		//System.out.println("SQL _ Targt "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			target = rs.getString("total");
		}
		return target;
	}
	
	//Total presupuestado
	public String getTotalPttoCtacve(String cmp_idfk, String ctacve, String cmp, String id_customer,  String id_user, String id_modulo,  String id_dashboard, String id_chart, String anio){
		String target = "0";
		String filtro = "";
		String fil_cmb = "";
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);
		HashMap parametros = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		HashMap hm = pptos.getDataChartConfig(id_chart, id_customer,id_modulo);
		String tbl_fact = (String) hm.get("tbl_fact");
		String elemento=(String)hm.get("elemento");
		int cont = 0;
		Iterator it = parametros.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
			if(cont == 0){
				////System.out.println(cmp_idfk+" -- "+e.getKey());
				if(!cmp_idfk.equals(e.getKey())){
					filtro += e.getKey()+"='"+e.getValue()+"' ";
				}
			}
			else{
				//if(!cmp_idfk.equals(e.getKey())){
					//filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					if(filtro==""){
						filtro += e.getKey()+"='"+e.getValue()+"' ";
					}else{
						filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
					}
				//}
				
				
			}
			cont++;
		}
		if(filtro == "" || filtro == null){
			filtro += tbl_fact+"."+elemento+" >= '"+anio+"' "+
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		}else{
			filtro += "AND "+tbl_fact+"."+elemento+" >= '"+anio+"' "+
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		}
		//if(filtro != null && filtro != ""){
			//filtro = "WHERE " + filtro;
		//}
		String rango_meses = pptos.obtieneRangoMes(id_customer, id_user, id_modulo, id_dashboard, id_chart);
		//System.out.println("Rango Mes "+rango_meses);
		if(filtro != null && filtro != ""){
			if(rango_meses !=  null && rango_meses != ""){
				//System.out.println("Condicion Filtro...");
				filtro += "AND "+rango_meses;
			}
		}else{
			if(rango_meses !=  null && rango_meses != ""){
				filtro += "WHERE "+rango_meses;
			}
		}
		//Filtrs segun combinacion
				if(sts_fact && sts_po && !sts_pm && gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".importe_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".importe_prem != 0";
				}
				if(sts_fact && sts_po && !sts_pm && !gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".cant_cajas_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && !gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".cant_cajas_prem != 0";
				}
				if(filtro == null || filtro ==""){
					if(fil_cmb != null && fil_cmb != ""){
						filtro = " WHERE "+ fil_cmb;
					}
				}else{
					if(fil_cmb != null && fil_cmb != ""){
						filtro += " AND "+fil_cmb;
					}
				}
				//filtro = filtro + fil_cmb;
		String sql = "SELECT ROUND(SUM("+cmp+"),0) as total FROM "+tbl_fact+" WHERE "+ filtro;
		
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			target = rs.getString("total");
		}
		
		System.out.println("SQL _ Targt "+sql+"--"+target);
		return target;
	}
	
	//Obtinene el limite para todos los cta cve con la sumatoria de todos los cunta calve
	public HashMap getLimiteCtacve(String cmp){
		HashMap ctacve = new HashMap();
		String limite = "";
		String sql = "SELECT  id_sop_ctaclave cve, ROUND(SUM("+cmp+"),0) as total FROM fact_presupuestos GROUP BY cve";
		//System.out.println("Limite CtaCve: "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			//limite = rs.getString("total");
			ctacve.put(rs.getString("cve"), rs.getString("total"));
		}
		return ctacve;
	}
	public void  insertaMedida(String id_user, String id_dashboard, String medida){
		String sql = "SELECT * FROM ppto_medida WHERE medida='"+medida+"' AND id_user='"+id_user+"' AND id_dashboard="+id_dashboard;
		//System.out.println("Medida" + id_user + sql);;
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(!rs.next()){
			try {
				jdbcTemplateAdmin.update(
				        "INSERT INTO ppto_medida (id_user, id_dashboard, medida) VALUES (?,?,?)", 
				        new Object[] {id_user, id_dashboard, medida});
				//result=1;
			} catch (DataAccessException e) {
				e.printStackTrace();
				//result=0;
			}
		}else{
			try {
				jdbcTemplateAdmin.update(
						"UPDATE ppto_medida SET medida='"+medida+"' WHERE medida= '"+medida+"' AND id_user='"+id_user+"' AND id_dashboard="+id_dashboard);
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}
		
	}
	
	public void  insertaGpoMedida(String id_user, String id_dashboard, String medida){
		String sql = "SELECT * FROM ppto_medida WHERE medida='gpo_medida' AND id_user='"+id_user+"' AND id_dashboard="+id_dashboard;
		if(medida ==null || medida == ""){
			medida = "cantidad";
		}
		//System.out.println("Medida" + id_user + sql);;
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(!rs.next()){
			try {
				jdbcTemplateAdmin.update(
				        "INSERT INTO ppto_medida (id_user, id_dashboard,medida, valor) VALUES (?,?,?,?)", 
				        new Object[] {id_user, id_dashboard,"gpo_medida", medida});
				//result=1;
			} catch (DataAccessException e) {
				e.printStackTrace();
				//result=0;
			}
		}else{
			try {
				jdbcTemplateAdmin.update(
						"UPDATE ppto_medida SET valor='"+medida+"' WHERE medida= 'gpo_medida' AND id_user='"+id_user+"' AND id_dashboard="+id_dashboard);
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}
		
	}
	
	public void  insertaMedidasGpo(//customer
			String id_user, 
			String id_dashboard, 
			String id_modulo,
			String gpo_medida,
			String comb_medidas
			){
		
		if(gpo_medida != null && gpo_medida != "" && comb_medidas != null && comb_medidas != ""){
			String sql_del = "DELETE FROM ppto_medidas WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			String sql_del_gpo = "DELETE FROM ppto_status_gpo_med WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			String sql_del_cmb = "DELETE FROM ppto_status_cmb WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			try{ 
				jdbcTemplateAdmin.update(sql_del);
				jdbcTemplateAdmin.update(sql_del_gpo);
				jdbcTemplateAdmin.update(sql_del_cmb);
				String[] arrayMedidas = comb_medidas.split(",");
				String arr_gpo[] = {"Importe","Cantidad"};
				String arr_cmb[] = {"Facturacion","Ppto Org","Ppto Mod"};
				for(int x = 0; x < arr_gpo.length; x++){
					jdbcTemplateAdmin.update("INSERT INTO ppto_status_gpo_med(id_user,id_dashboard, id_modulo, id_status_gpo, desc_status_gpo, status_gpo) VALUES(?,?,?,?,?,?)",
							new Object[]{id_user, id_dashboard, id_modulo,x+1 , arr_gpo[x], "0"});
				}
				for(int y = 0; y < arr_cmb.length; y++){
					jdbcTemplateAdmin.update("INSERT INTO ppto_status_cmb(id_user,id_dashboard, id_modulo, id_status_cmb, desc_status_cmb, status_cmb) VALUES(?,?,?,?,?,?)",
							new Object[]{id_user, id_dashboard, id_modulo,y+1 , arr_cmb[y], "0"});
				}
				try{
					jdbcTemplateAdmin.update("UPDATE ppto_status_cmb SET status_cmb ='0' WHERE id_status_cmb='"+gpo_medida+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'");
					//Coloca en 0 los gpos de medidas
					jdbcTemplateAdmin.update("UPDATE ppto_status_gpo_med SET status_gpo ='0' WHERE id_status_gpo='"+gpo_medida+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'");
					//Coloca en 1 el gpo de medidad elegido
					jdbcTemplateAdmin.update("UPDATE ppto_status_gpo_med SET status_gpo ='1' WHERE id_status_gpo='"+gpo_medida+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'");
				}catch(DataAccessException e){
					e.printStackTrace();
				}
				// En este momento tenemos un array en el que cada elemento es un color.
				for (int i = 0; i < arrayMedidas.length; i++) {
					if(String.valueOf(arrayMedidas[i]) != null && String.valueOf(arrayMedidas[i]) != ""){
						String med_val = "SELECT num_med FROM ppto_nom_medidas WHERE nom_comb='"+arrayMedidas[i]+"' AND grupo_medida='"+gpo_medida+"'";
						try{
							jdbcTemplateAdmin.update("INSERT INTO ppto_medidas(id_user,id_dashboard, id_modulo, id_gpo, id_cmb) VALUES(?,?,?,?,?)",
									new Object[]{id_user, id_dashboard, id_modulo,gpo_medida , arrayMedidas[i]});
							
							jdbcTemplateAdmin.update(
									"UPDATE ppto_status_cmb SET status_cmb ='1' WHERE id_status_cmb= '"+arrayMedidas[i]+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'");
						}catch(DataAccessException e){
							e.printStackTrace();
						}
					}
				}
			}catch(DataAccessException e){
				e.printStackTrace();
			}
			if(gpo_medida.equals("1")){
				try{
					jdbcTemplateAdmin.update("INSERT INTO ppto_medidas(id_user,id_dashboard, id_modulo, id_gpo, id_cmb) VALUES(?,?,?,?,?)",
							new Object[]{id_user, id_dashboard, id_modulo,gpo_medida , "4"});
					jdbcTemplateAdmin.update("INSERT INTO ppto_medidas(id_user,id_dashboard, id_modulo, id_gpo, id_cmb) VALUES(?,?,?,?,?)",
							new Object[]{id_user, id_dashboard, id_modulo,gpo_medida , "5"});					
				}catch(DataAccessException e){
					e.printStackTrace();
				}
			}
		}else{
			String sql_bsq = "SELECT * FROM ppto_medidas WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			SqlRowSet rs_bsq = jdbcTemplateAdmin.queryForRowSet(sql_bsq);
			if(!rs_bsq.next()){
				String sql = "SELECT num_med, nom_comb FROM ppto_nom_medidas WHERE grupo_medida='"+gpo_medida+"'"; 
				
				if(gpo_medida == null || gpo_medida == ""){
					gpo_medida = "cantidad";
				}
				
				SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
				while(rs.next()){
					try{
						jdbcTemplateAdmin.update("INSERT INTO ppto_medidas(id_user,id_dashboard, id_modulo, id_gpo, id_cmb) VALUES(?,?,?,?,?)",
								new Object[]{id_user, id_dashboard, id_modulo, rs.getString("num_med") , rs.getString("nom_comb"), gpo_medida});
					}catch(DataAccessException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void setPermisos(String id_user, String id_dashboard, String id_modulo){
		try{
			String sql1 = "UPDATE ppto_status_gpo_med SET status_gpo ='0' WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			String sql2 = "UPDATE ppto_status_gpo_med SET status_gpo ='1' WHERE id_status_gpo='2' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			String sql3 = "UPDATE ppto_medidas SET id_gpo ='2' WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			System.out.println("SQL2 "+sql3);
			//System.out.println("SQL2 "+sql2);
			//Coloca en 0 los gpos de medidas
			jdbcTemplateAdmin.update(sql1);
			//Coloca en 1 el gpo de medidad elegido
			jdbcTemplateAdmin.update(sql2);
			//Actualiza las medidas del usuario para ke solo pueda  Cajas y no Importe
			jdbcTemplateAdmin.update(sql3);
		}catch(DataAccessException e){
			e.printStackTrace();
		}
	}
	public String getNombreMedida(
			String id_user,
			String id_dashboard,
			String id_modulo,
			String num_medida
			){
		String medida = "";
		String sql = //"SELECT valor FROM ppto_medidas WHERE id_user='"+id_user+
				//"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"' AND medida='"+num_medida+"'";
				"SELECT desc_cmb FROM ppto_cmb_med where id_cmb='"+num_medida+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			medida = rs.getString("desc_cmb");
		}
		//System.out.println("SQL NOmbreMedida: "+sql);
		return medida;
	}
	
	public String getGpoMed(String id_user, String id_modulo, String id_dashboard){
		String gpo_med = "";
		String status;
		String id;
		boolean sts_imprt = stsImporte(id_user);
		String sql = "SELECT id_status_gpo, desc_status_gpo, status_gpo FROM ppto_status_gpo_med WHERE " +
				"id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' " +
						" ORDER BY id_status_gpo ASC";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			id = rs.getString("id_status_gpo");
			status = rs.getString("status_gpo");
			if(sts_imprt){//Sin permiso
				if(status.equals("1") && !id.equals("1")){
					gpo_med += "<input type='radio' name='gpo_med' class='medida' value="+id+" id='rdb_"+id+"' checked='checked'/> "+rs.getString("desc_status_gpo")+" ";
				}
			}else{
				if(status.equals("1")){
					gpo_med += "<input type='radio' name='gpo_med' class='medida' value="+id+" id='rdb_"+id+"' checked='checked'/> "+rs.getString("desc_status_gpo")+" ";
				}else{
					gpo_med += "<input type='radio' name='gpo_med' class='medida' value="+id+" id='rdb_"+id+"' /> "+rs.getString("desc_status_gpo")+" ";
				}
			}
			
		}
		return gpo_med;
	}
	
	public String getCmbMed(String id_user, String id_modulo, String id_dashboard){
		String cmb_med = "";
		String status;
		String id;
		String sql = "SELECT id_status_cmb, desc_status_cmb, status_cmb FROM ppto_status_cmb WHERE "+
				"id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' " +
						" ORDER BY id_status_cmb ASC";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			id = rs.getString("id_status_cmb");
			status = rs.getString("status_cmb");
			//System.out.println("Cmb - Id "+id + "--"+status);
			if(status.equals("1")){
				//cmb_med += "<input type='radio' name='gpo_med' class='medida' value="+id+" id="+id+" checked='checked'/> "+rs.getString("desc_status_gpo")+" ";
				cmb_med += "<input type='checkbox' value="+id+" id='chk_"+id+"' checked='checked' class='cmb_medidas'>"+rs.getString("desc_status_cmb")+" ";
			}else{
				//cmb_med += "<input type='radio' name='gpo_med' class='medida' value="+id+" id="+id+" /> "+rs.getString("desc_status_cmb")+" ";
				cmb_med += "<input type='checkbox' value="+id+" id='chk_"+id+"' class='cmb_medidas'>"+rs.getString("desc_status_cmb")+" ";
			}
		}
		//System.out.println("cmb "+cmb_med);
		return cmb_med;
	}
	
	public boolean getStatusGpo(String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT id_status_gpo FROM ppto_status_gpo_med WHERE status_gpo = '1' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		//System.out.println("StsGpo "+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			id = rs.getString("id_status_gpo");
			if(id.equals("1")){
				status = true;
			}
		}
		return status;
	}
	public boolean getStatusGpo_G(String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT id_status_gpo FROM ppto_status_gpo_med WHERE status_gpo = '1' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			id = rs.getString("id_status_gpo");
				status = true;
		}
		return status;
	}
	
	public boolean getStatusCmb(String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT id_status_cmb FROM ppto_status_cmb WHERE status_cmb = '1' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		//System.out.println("STS CMB "+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			id = rs.getString("id_status_cmb");
			if(id.equals("1")){
				status = true;
			}
		}
		return status;
	}
	public boolean getStatusCmb_G(String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT id_status_cmb FROM ppto_status_cmb WHERE status_cmb = '1' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		//System.out.println("STS CMB "+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			//id = rs.getString("id_status_cmb");
			//if(id.equals("1")){
				status = true;
			//}
		}
		return status;
	}
	public boolean getStatusFact(String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT id_status_cmb FROM ppto_status_cmb WHERE status_cmb = '1' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			id = rs.getString("id_status_cmb");
			//System.out.println("ID-ORG"+id);
			if(id.equals("1")){
				status = true;
			}
		}
		return status;
	}
	
	public boolean getStatusPptoOrg(String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT id_status_cmb FROM ppto_status_cmb WHERE status_cmb = '1' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			id = rs.getString("id_status_cmb");
			//System.out.println("ID-ORG"+id);
			if(id.equals("2")){
				status = true;
			}
		}
		return status;
	}
	
	public boolean getStatusPptoMod(String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT id_status_cmb FROM ppto_status_cmb WHERE status_cmb = '1' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			id = rs.getString("id_status_cmb");
			//System.out.println("ID-MOD"+id);
			if(id.equals("3")){
				status = true;
			}
		}
		return status;
	}
	
	//Verifica que el usuario por modulo tenga su cuenta clve asignada
	public boolean getExistCC(String id_customer, String id_user, String id_modulo, String id_dashboard){
		boolean status =  false;
		String id;
		String sql = "SELECT * FROM ppto_parametros WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
				status = true;
		}
		return status;
	}
	public HashMap  getDatosCtaCve(String cust_id, String id_user, String chart_id, String id_dashboard, String id_modulo){
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id, id_modulo);
		
		HashMap datos =  new HashMap();
		boolean gpomed = getStatusGpo(id_user, id_modulo, id_dashboard);
		boolean cmbmed = getStatusCmb(id_user, id_modulo, id_dashboard);
		String id;
		String val;
		int contmed = 1;
		//Grupo de Medida
		/*String sql = "SELECT ppto_cmps_med.val_cmp val, ppto_medidas.id_cmb id FROM ppto_cmps_med, ppto_medidas "+
					" WHERE ppto_cmps_med.id_gpo=ppto_medidas.id_gpo AND ppto_cmps_med.id_cmb = ppto_medidas.id_cmb " +
					" ORDER BY ppto_medidas.id_cmb ASC";*/
		String sql = "SELECT ppto_medidas.id_cmb id ,ppto_cmps_med.val_cmp val" +
				" FROM ppto_cmps_med, ppto_medidas  WHERE ppto_cmps_med.id_gpo=ppto_medidas.id_gpo AND" +
				" ppto_cmps_med.id_cmb = ppto_medidas.id_cmb AND ppto_medidas.id_user='"+id_user+"'" +
				" AND ppto_medidas.id_modulo='"+id_modulo+"' AND ppto_medidas.id_dashboard='"+id_dashboard+"' ORDER BY ppto_medidas.id_cmb ASC";
		
		System.out.println("Sql Dtos Cta Cve "+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		
		while(rs.next()){
			id = rs.getString("id");
			val = rs.getString("val");
			//System.out.println("ID "+id+" VAL "+val);
			if(!id.equals("4")){
				//Importe y Facturaqdo
				//System.out.println("GpoMed "+gpomed+" CmbMed "+cmbmed);
				//if(gpomed && cmbmed){
					System.out.println("Medidas Cta Cve: ID "+id+" VAL "+ val);
					datos.put(id, val);
			//	}
				//Cantidfad y facturado
				//if(!gpomed && cmbmed){
					//System.out.println("Medidasa Cta Cve: "+id+ val);
					//datos.put(id, val);
				//}
			}
		}
		
		return datos;
	}
	
	public void insertaParametros(String id_customer, String id_user, String id_dashboard, String id_modulo, String id_chart,
			String valor, String parametro, String nombre, String nomValue){
		
		String sql = "SELECT * FROM ppto_parametros WHERE parametro='"+parametro+"' " +
				"AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"'AND id_modulo='"+id_modulo+"'";
		//SQL para datos generales
		String sql_gnrl = "SELECT * FROM ppto_parametros_gnrl WHERE parametro='"+parametro+"' " +
				"AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"'AND id_modulo='"+id_modulo+"'";
		String nombreValor = decodeNombre(nomValue);
		
		//System.out.println("SQL inserta filtro"+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(!rs.next()){
			try {
				jdbcTemplateAdmin.update(
						"INSERT INTO ppto_parametros (id_customer, id_user, id_modulo, id_dashboard, parametro, value, nombre, nomValue) VALUES (?,?,?,?,?,?,?,?)", 
				        new Object[] {id_customer, id_user, id_modulo, id_dashboard, parametro, valor, nombre, nombreValor});
				//System.out.println("Inserta Parametro.........");
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}else{
			try {
				jdbcTemplateAdmin.update(
						//"update parametros set(chart,value) values (?,?) where chart="+chart, 
				        //new Object[] {chart,valor});
						"UPDATE ppto_parametros SET parametro='"+parametro+"',value='"+valor+"',nombre='"+nombre+"',nomValue='"+nombreValor+"' " +
								"WHERE parametro= '"+parametro+"' AND id_user='"+id_user+"' AND id_dashboard="+id_dashboard+" AND id_customer='"+id_customer+"' " +
										" AND id_modulo='"+id_modulo+"'");
				System.out.println("Actualiza Parametro.........");
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}
		if(id_chart.equals("1")){
			//Elimina Rango de Meses
			String sqlDelRango = "DELETE FROM ppto_filtros_portlets WHERE id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+id_chart+"'";
			jdbcTemplateAdmin.update(sqlDelRango);
			//System.out.println("Elimina Rango Meses "+sqlDelRango);
		}
		if(id_chart.equals("1") || id_chart.equals("2") || id_chart.equals("3")){
			SqlRowSet rs_gnrl = jdbcTemplateAdmin.queryForRowSet(sql_gnrl);
			if(!rs_gnrl.next()){
				try {
					jdbcTemplateAdmin.update(
							"INSERT INTO ppto_parametros_gnrl (id_customer, id_user, id_modulo, id_dashboard, parametro, value, nombre, nomValue) VALUES (?,?,?,?,?,?,?,?)", 
					        new Object[] {id_customer, id_user, id_modulo, id_dashboard, parametro, valor, nombre, nombreValor});
					//System.out.println("Inserta Parametro.........");
					
				} catch (DataAccessException e) {
					e.printStackTrace();
					
				}
			}else{
				try {
					jdbcTemplateAdmin.update(
							//"update parametros set(chart,value) values (?,?) where chart="+chart, 
					        //new Object[] {chart,valor});
							"UPDATE ppto_parametros_gnrl SET parametro='"+parametro+"',value='"+valor+"',nombre='"+nombre+"',nomValue='"+nombreValor+"' " +
									"WHERE parametro= '"+parametro+"' AND id_user='"+id_user+"' AND id_dashboard="+id_dashboard+" AND id_customer='"+id_customer+"' " +
											" AND id_modulo='"+id_modulo+"'");
					System.out.println("Actualiza Parametro.........");
				} catch (DataAccessException e) {
					e.printStackTrace();
					
				}
			}
			if(id_chart.equals("1")){
				//Elimina Rango de Meses
				String sqlDelRango = "DELETE FROM ppto_filtros_portlets_gnrl WHERE id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+id_chart+"'";
				jdbcTemplateAdmin.update(sqlDelRango);
				//System.out.println("Elimina Rango Meses "+sqlDelRango);
			}
		}
	}
	
	//_Inserta parametros generales para popup
	public void insertaParametrosGnrl(String id_customer, String id_user, String id_dashboard, String id_modulo, String id_chart,
			String valor, String parametro, String nombre, String nomValue){
		
		String sql = "SELECT * FROM ppto_parametros_gnrl WHERE parametro='"+parametro+"' " +
				"AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"'AND id_modulo='"+id_modulo+"'";
		String nombreValor = decodeNombre(nomValue);
		
		//System.out.println("SQL inserta filtro"+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(!rs.next()){
			try {
				jdbcTemplateAdmin.update(
						"INSERT INTO ppto_parametros_gnrl (id_customer, id_user, id_modulo, id_dashboard, parametro, value, nombre, nomValue) VALUES (?,?,?,?,?,?,?,?)", 
				        new Object[] {id_customer, id_user, id_modulo, id_dashboard, parametro, valor, nombre, nombreValor});
				//System.out.println("Inserta Parametro.........");
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}else{
			try {
				jdbcTemplateAdmin.update(
						//"update parametros set(chart,value) values (?,?) where chart="+chart, 
				        //new Object[] {chart,valor});
						"UPDATE ppto_parametros_gnrl SET parametro='"+parametro+"',value='"+valor+"',nombre='"+nombre+"',nomValue='"+nombreValor+"' " +
								"WHERE parametro= '"+parametro+"' AND id_user='"+id_user+"' AND id_dashboard="+id_dashboard+" AND id_customer='"+id_customer+"' " +
										" AND id_modulo='"+id_modulo+"'");
				System.out.println("Actualiza Parametro.........");
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}
		if(id_chart.equals("1")){
			//Elimina Rango de Meses
			String sqlDelRango = "DELETE FROM ppto_filtros_portlets_gnrl WHERE id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+id_chart+"'";
			jdbcTemplateAdmin.update(sqlDelRango);
			//System.out.println("Elimina Rango Meses "+sqlDelRango);
		}
	}
	private String decodeNombre(String nombre){
		String impNombre ="";
		
		impNombre = nombre.replace("Ã¡", "ï¿½").replace("Ã©", "ï¿½").replace("Ã­", "ï¿½").replace("Ã³", "ï¿½").replace("Ãº", "ï¿½").replace("Ã±", "ï¿½");
		//ï¿½ => Ã¡
		//ï¿½ => Ã©
		//ï¿½ => Ã­
		//ï¿½ => Ã³
		//ï¿½ => Ãº
		//ï¿½ => Ã±
		//System.out.println("Decode"+impNombre);
		return impNombre;
	}
	public String agregaFiltro(String id_customer, String id_user, String id_modulo,String id_dashboard){
		String codigoPortlet = "";
		String blur = "";
		String id = "";
		String width = "";
		String height = "";
		String sql = "SELECT * FROM ppto_parametros WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard="+id_dashboard;
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			//System.out.println(rs.getString("nomValue"));
			codigoPortlet += getHeadFiltro(rs.getString("parametro"),rs.getString("nombre"),"divFiltro","1", rs.getString("parametro"), id_user, id_dashboard);
			String nombre=rs.getString("nomValue");
			String[] arrayNom = nombre.split(" ");
			String nom = "";
			nom = arrayNom[0];
			codigoPortlet += nom;//rs.getString("nomValue");	
		    codigoPortlet += getFooterPortlet();
		    codigoPortlet += "<div id=msg"+rs.getString("parametro")+" style='display: none;' class=tip >"+nombre+"</div>";
		}
		
		return codigoPortlet;
	}
	public String agregaFiltroPopup(String id_customer, String id_user, String id_modulo,String id_dashboard, String id_portlet){
		String codigoPortlet = "";
		String filtro = "";
		String id = "";
		String width = "";
		String height = "";
		String sql = "SELECT * FROM ppto_filtros_portlets WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard="+id_dashboard+" AND id_portlet='"+id_portlet+"'";
		System.out.println("Filtro Popup "+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			//filtro = getNombreFiltroPopup();
			
			codigoPortlet += "- Filtro: "+rs.getString("nombre")+" <a href='parametros_popup.jsp?chartID="+id_portlet+"&id_customer="+id_customer
								+"&id_user="+id_user+"&id_dashboard="+id_dashboard+"&eliminar=true' target='hidden'><img src='../../img/close.gif'><a>";
		}
		
		return codigoPortlet;
	}
	public String getHeadFiltro(String id, String title, String style, String close, String idClose, String id_user, String id_dashboard){
		String cc = getPerCC(id_user);
		System.out.println("CErrar Cuenta Calve PAra Administrado "+cc+"-"); 
		if(id.equals("id_sop_ctaclave") && cc != null && !cc.isEmpty()){
			close = "0";
		}
		
		String str = "<div class='mainBox' dragableBox='false' id='"+id+"'> <div class='"+style+"'><div class='shadow'> <div class='content'><div class='title_bar'>"+title;
		if( close == "1" ){
			str += "<a href=?delFil="+id+"&id_user="+id_user+"&id_dashboard="+id_dashboard+" class=delFil ><img src='../img/close.gif' align='right'></a>";
		}
		str += "</div>";
		return str;
	}
	public String getFooterPortlet(){
		String str = "</div> </div> </div> </div>";
		return str;
	}
	public int borraFiltro(String filtro, String id_user, String id_dashboard, String id_modulo){
		//System.out.println("Dashboard "+id_dashboard);
		int result;
		//System.out.println("Filtro "+id_user);
		try {
			jdbcTemplateAdmin.update("DELETE FROM ppto_parametros WHERE parametro='"+filtro+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"'");
			if(filtro.equals("mes") || filtro.equals("dim_tiempo_semestre") || filtro.equals("dim_tiempo_trimestre")){
				jdbcTemplateAdmin.update("DELETE FROM ppto_parametros_gnrl WHERE parametro='"+filtro+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"'");
			}
			result=1;
		} catch (DataAccessException e) {
			e.printStackTrace();
			result=0;
		}
		//System.out.println("borro filtro");
		return result;
	}
	public int borraFiltroPopup(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet){
		//System.out.println("Dashboard "+id_dashboard);
		int result;
		//System.out.println("Filtro "+id_user);
		try {
			jdbcTemplateAdmin.update(
			        "DELETE FROM ppto_filtros_portlets WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"' AND id_portlet='"+id_portlet+"'");
			result=1;
		} catch (DataAccessException e) {
			e.printStackTrace();
			result=0;
		}
		//System.out.println("borro filtro");
		return result;
	}
	public String getLimiteGlobal(String cmp, String id_customer, String id_user, String id_modulo, String id_dashboard, String id_chart, String anio){
		String limite = "";
		String fil_cmb = "";
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);
		String filtro = "";
		HashMap parametros = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		HashMap hm = pptos.getDataChartConfig(id_chart, id_customer,id_modulo);
		String tbl_fact = (String) hm.get("tbl_fact");
		String elemento=(String)hm.get("elemento");
		int cont = 0;
		Iterator it = parametros.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
			if(cont == 0){
				////System.out.println(cmp_idfk+" -- "+e.getKey());
				//if(!cmp_idfk.equals(e.getKey())){
					filtro += e.getKey()+"='"+e.getValue()+"' ";
				//}
			}
			else{
				//if(!cmp_idfk.equals(e.getKey())){
					//filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					if(filtro==""){
						filtro += e.getKey()+"='"+e.getValue()+"' ";
					}else{
						filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
					}
				//}
				
				
			}
			cont++;
		}
		if(filtro == ""){
			filtro += tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		}else{
			filtro += "AND "+tbl_fact+"."+elemento+" >= '"+anio+"' " +
					"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
		}
		if(filtro != null && filtro != ""){
			filtro = "WHERE " + filtro;
		}
		String rango_meses = pptos.obtieneRangoMes(id_customer, id_user, id_modulo, id_dashboard, id_chart);
		//System.out.println("Rango Mes "+rango_meses);
		if(filtro != null && filtro != ""){
			if(rango_meses !=  null && rango_meses != ""){
				//System.out.println("Condicion Filtro...");
				filtro += "AND "+rango_meses;
			}
		}else{
			if(rango_meses !=  null && rango_meses != ""){
				filtro += "WHERE "+rango_meses;
			}
		}
		//Filtrs segun combinacion
		if(sts_fact && sts_po && !sts_pm && gpo && id_modulo.equals("2")){
			fil_cmb = tbl_fact+".importe_pre != 0";
		}
		if(sts_fact && sts_pm && !sts_po && gpo && id_modulo.equals("2")){
			fil_cmb = tbl_fact+".importe_prem != 0";
		}
		if(sts_fact && sts_po && !sts_pm && !gpo && id_modulo.equals("2")){
			fil_cmb = tbl_fact+".cant_cajas_pre != 0";
		}
		if(sts_fact && sts_pm && !sts_po && !gpo && id_modulo.equals("2")){
			fil_cmb = tbl_fact+".cant_cajas_prem != 0";
		}
		if(filtro == null || filtro == ""){
			if(fil_cmb != null && fil_cmb != ""){
				System.out.println("Filtro = "+filtro);
				filtro = " WHERE "+ fil_cmb;
			}
		}else{
			if(fil_cmb != null && fil_cmb != ""){
				filtro += " AND " + fil_cmb;
			}
		}
		
		
		String sql = "SELECT ROUND(SUM("+cmp+"),0) total from "+tbl_fact+" " + filtro;
		System.out.println("Sql LimteGloblal "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			limite = rs.getString("total");
		}
		return limite;
	}
	public String getCumpGlobal(String cmp, String id_customer, String id_user, String id_modulo, String id_dashboard, String id_chart, String anio){
		String limite = "";
		String filtro = "";
		String fil_cmb = "";
		boolean sts_fact = getStatusFact(id_user, id_modulo, id_dashboard);
		boolean sts_po = getStatusPptoOrg(id_user, id_modulo, id_dashboard);
		boolean sts_pm = getStatusPptoMod(id_user, id_modulo, id_dashboard);
		boolean gpo = getStatusGpo(id_user, id_modulo, id_dashboard);	
		HashMap parametros = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		HashMap hm = pptos.getDataChartConfig(id_chart, id_customer,id_modulo);
		String tbl_fact = (String) hm.get("tbl_fact");
		String elemento=(String)hm.get("elemento");
				int cont = 0;
				Iterator it = parametros.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
					if(cont == 0){
						////System.out.println(cmp_idfk+" -- "+e.getKey());
						//if(!cmp_idfk.equals(e.getKey())){
							filtro += e.getKey()+"='"+e.getValue()+"' ";
						//}
					}
					else{
						//if(!cmp_idfk.equals(e.getKey())){
							//filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
							if(filtro==""){
								filtro += e.getKey()+"='"+e.getValue()+"' ";
							}else{
								filtro += "AND "+e.getKey()+"='"+e.getValue()+"' ";
							}
						//}
						
						
					}
					cont++;
				}
				if(filtro == ""){
					filtro += tbl_fact+"."+elemento+" >= '"+anio+"' "+
							"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
				}else{
					filtro += "AND "+tbl_fact+"."+elemento+" >= '"+anio+"' "+
							"AND "+tbl_fact+"."+elemento+" <= '"+anio+"' " ; //rango de aï¿½os se modificara para uno solo
				}
				if(filtro != null && filtro != ""){
					filtro = "WHERE " + filtro;
				}
				String rango_meses = pptos.obtieneRangoMes(id_customer, id_user, id_modulo, id_dashboard, id_chart);
				//System.out.println("Rango Mes "+rango_meses);
				if(filtro != null && filtro != ""){
					if(rango_meses !=  null && rango_meses != ""){
						//System.out.println("Condicion Filtro...");
						filtro += "AND "+rango_meses;
					}
				}else{
					if(rango_meses !=  null && rango_meses != ""){
						filtro += "WHERE "+rango_meses;
					}
				}
				//System.out.println("filtro "+filtro);
				//Filtrs segun combinacion
				if(sts_fact && sts_po && !sts_pm && gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".importe_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".importe_prem != 0";
				}
				if(sts_fact && sts_po && !sts_pm && !gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".cant_cajas_pre != 0";
				}
				if(sts_fact && sts_pm && !sts_po && !gpo && id_modulo.equals("2")){
					fil_cmb = tbl_fact+".cant_cajas_prem != 0";
				}
				if(filtro == null || filtro == ""){
					if(fil_cmb != null && fil_cmb != ""){
						filtro = " WHERE "+ fil_cmb;
					}
				}else{
					if(fil_cmb != null && fil_cmb != ""){
						filtro += " AND " + fil_cmb; 
					}
				}
		String sql = "SELECT ROUND(SUM("+cmp+"),0) total from "+tbl_fact+" "+ filtro;
		//System.out.println("Sql "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			limite = rs.getString("total");
		}
		return limite;
	}
	public int insertaRangoMes(String id_customer, String id_user, String id_dashboard, String id_portlet, String id_modulo, String id_chart, 
			String mesIni, String mesFin){
		
		//System.out.println("Mes Ini:  ----- "+ mesIni +" Mes Fin ------: "+ mesFin);
		int rtn = 0;		
		String sql = "DELETE FROM ppto_filtros_portlets WHERE id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+id_portlet+"'";
		String sqlCmpID = "SELECT chartcfg_cmp_id FROM ppto_chartcfg WHERE chart_id='"+id_portlet+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sqlCmpID);
		String nom_cmpid = "";
		while(rs.next()){
			nom_cmpid = rs.getString("chartcfg_cmp_id");
		}
		//Eliminar MEs Filtro Por Chart
		
		
		try {
			if(mesIni != "" && mesIni != null && mesIni != "" && mesIni != null){
				if(mesIni.equals("0") || mesFin.equals("0")){
					jdbcTemplateAdmin.update(sql);
					/*try {
						jdbcTemplateAdmin.update(
						        "DELETE FROM ppto_parametros WHERE parametro='"+nom_cmpid+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"'");
						System.out.println("Borra filtro por mes i rango....");
					} catch (DataAccessException e) {
						e.printStackTrace();
					}*/
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		//System.out.println("insertaMEnuFiltro "+ id_portlet);
		if(mesIni != null && !mesIni.equals("0") && mesIni != "" &&
				mesFin != null && !mesFin.equals("0") && mesFin != ""){
				
			//elimina filtro mes
			jdbcTemplateAdmin.update(
			        "DELETE FROM ppto_parametros WHERE parametro='"+nom_cmpid+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"'");
			//System.out.println("Borra Filtro Mes....");
			jdbcTemplateAdmin.update(sql);
				try {
					
					jdbcTemplateAdmin.update("INSERT INTO ppto_filtros_portlets (id_customer, id_user, id_modulo, id_dashboard, id_portlet, parametro, value) VALUES(?,?,?,?,?,?,?)", 
					        new Object[] {id_customer, id_user, id_modulo, id_dashboard, id_portlet, nom_cmpid, mesIni});
					//System.out.println("Inzeta MEs Inicial...");
					
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
		//}
		//if(mesFin != null && !mesFin.equals("0") && mesFin != ""){
			try {
				//jdbcTemplateAdmin.update(sql);
				jdbcTemplateAdmin.update("INSERT INTO ppto_filtros_portlets (id_customer, id_user, id_modulo, id_dashboard, id_portlet, parametro, value) VALUES(?,?,?,?,?,?,?)", 
				        new Object[] {id_customer, id_user, id_modulo, id_dashboard, id_portlet, nom_cmpid, mesFin});
				//System.out.println("InsertaMes Final.....");
				
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
		/*try {
			jdbcTemplateAdmin.update("INSERT INTO filtros_portlets (id_user, id_dashboard, id_portlet, id_filtro) VALUES(?,?,?,?)", 
			        new Object[] {id_user, id_dashboard, id_portlet,id_filtro});
			result=1;
		} catch (DataAccessException e) {
			e.printStackTrace();
			result=0;
		}*/
		return rtn;
	}
	
	public void insertMenuFiltroPopup(String id_user, String id_modulo, String id_dashboard, String id_portlet, String elementos){
		int result;
		String sql = "DELETE FROM pptos_filtros_portlets WHERE id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+id_portlet+"'";
		String sqlCmpID = "SELECT chartcfg_cmp_id FROM pptos_chartcfg WHERE chart_id='"+id_portlet+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sqlCmpID);
		String nom_cmpid = "";
		while(rs.next()){
			nom_cmpid = rs.getString("chartcfg_cmp_id");
		}
		try {
			jdbcTemplateAdmin.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		//System.out.println("insertaMEnuFiltro "+ id_portlet);
		if(!elementos.isEmpty()){
			String[] arrayFiltros = elementos.split(",");
			//System.out.println("length "+arrayFiltros.length);
			for(int x = 0; x < arrayFiltros.length; x++){
				if(arrayFiltros[x] != null || arrayFiltros[x] != "" ){
					//System.out.println("Filtros - " + id_user+ "--"+ id_dashboard +"--"+ id_portlet+"--"+arrayFiltros[x]+"--"+nom_cmpid);
					try {
						jdbcTemplateAdmin.update("INSERT INTO pptos_filtros_portlets (id_user, id_dashboard, id_portlet, nom_cmp_id, id_filtro) VALUES(?,?,?,?,?)", 
						        new Object[] {id_user, id_dashboard, id_portlet, nom_cmpid, arrayFiltros[x]});
						result=1;
					} catch (DataAccessException e) {
						e.printStackTrace();
						result=0;
					}
				}
			}
		}
		/*try {
			jdbcTemplateAdmin.update("INSERT INTO filtros_portlets (id_user, id_dashboard, id_portlet, id_filtro) VALUES(?,?,?,?)", 
			        new Object[] {id_user, id_dashboard, id_portlet,id_filtro});
			result=1;
		} catch (DataAccessException e) {
			e.printStackTrace();
			result=0;
		}*/
	}
	
	public void insertaFiltroPopup(String id_customer, String id_user, String id_dashboard, String id_modulo, String id_chart, 
			String parametro, String value, String nom_val){
		
		String sql_del = "DELETE FROM ppto_filtros_portlets WHERE"+ 
				" id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"' AND id_modulo='"+id_modulo+"' AND id_portlet!='5'" +
						" AND id_portlet != '9'  AND id_portlet != '1'";
		
		String sql = "SELECT * FROM ppto_filtros_portlets WHERE"+ 
				" id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"'AND id_modulo='"+id_modulo+"' AND id_portlet!='1' " +
						"AND parametro='"+parametro+"'";
		
				try {
					jdbcTemplateAdmin.update(sql_del);
					
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
		
		//System.out.println("SQL"+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(!rs.next()){
			try {
				//System.out.println("InsertaFiltroPopup "+id_customer +" "+id_user+" "+id_dashboard+" "+id_modulo+" "+id_chart+" "+parametro+" "+value);
				jdbcTemplateAdmin.update(
						"INSERT INTO ppto_filtros_portlets (id_customer, id_user, id_modulo, id_dashboard, id_portlet, parametro, value, nombre) VALUES (?,?,?,?,?,?,?,?)", 
				        new Object[] {id_customer, id_user, id_modulo,id_dashboard, id_chart, parametro, value, nom_val});
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
			//System.out.println("InsertaFiltroPopup "+id_customer +" "+id_user+" "+id_dashboard+" "+id_modulo+" "+id_chart+" "+parametro+" "+value);
		}else{
			try {
				jdbcTemplateAdmin.update(
						//"update parametros set(chart,value) values (?,?) where chart="+chart, 
				        //new Object[] {chart,valor});
						"UPDATE ppto_filtros_portlets SET parametro='"+parametro+"',value='"+value+"', nombre='"+nom_val+"' " +
								"WHERE id_user='"+id_user+"' AND id_dashboard="+id_dashboard+" AND id_customer='"+id_customer+"' " +
										"  AND id_modulo='"+id_modulo+"' AND id_portlet != '1' AND parametro='"+parametro+"'");
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}
	}
	public void eliminaFiltroPopup(String id_customer, String id_user, String id_dashboard, String id_modulo){
		
		String sql_del = "DELETE FROM ppto_filtros_portlets WHERE"+ 
				" id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"' AND id_modulo='"+id_modulo+"'" +
						" AND id_portlet != '1'";
		System.out.println("SQL DELETE "+sql_del);
		try {
			jdbcTemplateAdmin.update(sql_del);		
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	//Elimina los filtros creados anteriormente para el popup general
	public void eliminaFiltroPopupGnrl(String id_customer, String id_user, String id_dashboard, String id_modulo){
		System.out.println("Elimina Filtrosssssssssssssss");
		String sql_del = "DELETE FROM ppto_parametros_gnrl WHERE"+ 
				" id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"' AND id_modulo='"+id_modulo+"'" +
						" AND parametro != 'mes' AND parametro != 'dim_tiempo_semestre' AND parametro != 'dim_tiempo_trimestre'";
		System.out.println("SQL-DEL*Fil-Gnrl "+sql_del);
		try {
			jdbcTemplateAdmin.update(sql_del);
			System.out.println("Exec..........");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	public void insertaParametroPopup(String id_customer, String id_user, String id_dashboard, String id_modulo, String id_chart, 
			String parametro, String value, String nom_val){
		
		String sql = "SELECT * FROM ppto_filtros_portlets WHERE"+ 
				" id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_customer='"+id_customer+"'AND id_modulo='"+id_modulo+"' AND id_portlet!='1' " +
						"AND parametro='"+parametro+"'";
		
		
		///System.out.println("SQL"+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(!rs.next()){
			try {
				System.out.println("InsertaFiltroPopup "+id_customer +" "+id_user+" "+id_dashboard+" "+id_modulo+" "+id_chart+" "+parametro+" "+value);
				jdbcTemplateAdmin.update(
						"INSERT INTO ppto_filtros_portlets (id_customer, id_user, id_modulo, id_dashboard, id_portlet, parametro, value, nombre) VALUES (?,?,?,?,?,?,?,?)", 
				        new Object[] {id_customer, id_user, id_modulo,id_dashboard, id_chart, parametro, value, nom_val});
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
			//System.out.println("InsertaFiltroPopup "+id_customer +" "+id_user+" "+id_dashboard+" "+id_modulo+" "+id_chart+" "+parametro+" "+value);
		}else{
			try {
				jdbcTemplateAdmin.update(
						//"update parametros set(chart,value) values (?,?) where chart="+chart, 
				        //new Object[] {chart,valor});
						"UPDATE ppto_filtros_portlets SET parametro='"+parametro+"',value='"+value+"', nombre='"+nom_val+"' " +
								"WHERE id_user='"+id_user+"' AND id_dashboard="+id_dashboard+" AND id_customer='"+id_customer+"' " +
										"  AND id_modulo='"+id_modulo+"' AND id_portlet != '1' AND parametro='"+parametro+"'");
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				
			}
		}
	}
	//Obtiene Reango de Meses A Establecer
	public HashMap obtieneRangoMes(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_chart){
		HashMap medida = new HashMap();
		
		String sql = "SELECT parametro, value FROM ppto_filtros_portlets WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+
				"' AND id_customer='"+id_customer+"' AND id_modulo='"+id_modulo+"' AND id_portlet='"+id_chart+"' ORDER BY value ASC";
		System.out.println("SQL Mesessssssssssssssssss______"+sql);;
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		int cont = 1;
		while(rs.next()){
			//medida = rs.getString("medidaValor");
			System.out.println(rs.getString("parametro")+"_"+cont+"-__-"+ rs.getString("value"));
			medida.put(rs.getString("parametro")+"_"+cont, rs.getString("value"));
			//System.out.println(cont);
			cont++;
		}
		//System.out.println("Medida -- "+medida);
		return  medida;
	}
	
	public String getLogin(String user, String password){
		String login = null;
		String sql = "SELECT * FROM users WHERE user='"+user+"' AND password='"+password+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		
		if(rs.next()){
			login = rs.getString("id_user");
		}
		return login;
	}
	
	public boolean stsImporte(String id_user){
		boolean login = false;
		
		String per_imprt = "";
		String sql = "SELECT per_importe FROM users WHERE id_user='"+id_user+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		
		if(rs.next()){
			per_imprt = rs.getString("per_importe");
			if(per_imprt != null ){
				if(!per_imprt.equals("1")){
					login = true;
				}
			}
			
		}
		return login;
	}
	public boolean insertaFiltroPorUsuario(String id_customer, String id_user, String id_modulo, String id_dashboard){
		boolean insert = false;
		String sql_del = "DELETE FROM ppto_parametros WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
		jdbcTemplateAdmin.execute(sql_del);
		//Seleccionar el cuenta clave para cada usuario
		String sql_obtn = " SELECT val_ctacve FROM users WHERE id_user ='"+id_user+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql_obtn);
		String cta_cve = "";
		
		if(rs.next()){
			cta_cve = rs.getString("val_ctacve");
			if(cta_cve != null && cta_cve != ""){
				String sql_ctacve = "SELECT ctaclave FROM soporte_ctaclave WHERE id_sop_ctaclave='"+cta_cve+"'";
				SqlRowSet rs_cv = jdbcTemplate.queryForRowSet(sql_ctacve);
				if(rs_cv.next()){
					jdbcTemplateAdmin.update(
							"INSERT INTO ppto_parametros (id_customer, id_user, id_modulo, id_dashboard, parametro, value, nombre, nomValue) VALUES (?,?,?,?,?,?,?,?)", 
					        new Object[] {id_customer, id_user, id_modulo, id_dashboard, "id_sop_ctaclave", cta_cve, "Cuenta Clave", rs_cv.getString("ctaclave")});
				}
			}
		}
		return insert;
	}
	public boolean getExistCtaCve(String id_customer, String id_user, String id_modulo, String id_dashboard){
		boolean exist = false;
		String sql = "SELECT * FROM ppto_parametros WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
	
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			exist = true;
		}
		return exist;
	}
	public String getPerCC(String id_user){
		String cc = "";
		String sql = "SELECT val_ctacve FROM users WHERE id_user='"+id_user+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			cc = rs.getString("val_ctacve");
		}
		//System.out.println("CCCCCCCCCCC: "+cc);
		return cc;
	}
	/*public String getPerCC(String id_user){
		String cc = "";
		String sql = "SELECT val_ctacve FROM users WHERE id_user='"+id_user+"'";
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		if(rs.next()){
			cc = rs.getString("val_ctacve");
		}
		//System.out.println("CCCCCCCCCCC: "+cc);
		return cc;
	}*/
	public String getNameCC(String cta_cve){
		String name= "";
		String sql = "SELECT ctaclave FROM soporte_ctaclave WHERE id_sop_ctaclave='"+cta_cve+"'";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		if(rs.next()){
			name = rs.getString("ctaclave");
		}
		return name;
	}
}
