package com.auribox.reporting.tools;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.db.DataSource;
import com.db.dao.InvInsumos;
import com.db.dao.InvPrevComp;
import com.db.dao.Inventarios;
import com.db.dao.Presupuestos;
import com.db.dao.PresupuestosDTO;
import com.db.dao.InvDTO;
public class ToolsInv {
	
	JdbcTemplate jdbcTemplate;
	JdbcTemplate jdbcTemplateAdmin;
	
	Inventarios invent = new Inventarios();
	Presupuestos pptos = new Presupuestos();
	
	public ToolsInv(){
		DataSource ds = new DataSource();
		jdbcTemplate = ds.getDataSource();
		jdbcTemplateAdmin = ds.getDataSourceAdmin();
	}
	public String getTableInd(
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
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		//Descripcion de los datos
		HashMap<String, String> dtNom = new HashMap<String, String>();/////---nom
		//Medida correspondiente
		HashMap<String, String> dtMedPag = new HashMap<String, String>();///----med
		//Total de la medida
		HashMap<String, String> dtMedTot = new HashMap<String, String>();///----total_med
		
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
		HashMap body = pptos.getDataBody(chart_id, cust_id, anioIni, anioFin, 
				id_user, id_dashboard, gpoMed, id_modulo, filtros);
		
		SortedSet<Integer> sort = new TreeSet<Integer>(head.keySet());
		Iterator it = sort.iterator();
		HashMap prmt = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		String anio = (String)prmt.get("anio");
		
		//Lista de ID�s de los datos
		ArrayList<String> dimId = new ArrayList<String>();
		int cont = 0;
		HashMap nom_gpo_med = pptos.getNomMedTable(id_user, id_dashboard, gpoMed);
		
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		html += "<tr>";
		String cmp_med_1 = "";
		
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
		
		//Nombre De las Columnas PAra La Tabla(Grid) Previcion de Compras...
		html += "<th>Clave</th>";
		html += "<th>" + xAxisName + "</th>";
		html += "<th>Tiempo Limite</th>";
		html += "<th>Pedido 1</th>";
		html += "<th>Pedido 2</th>";
		html += "<th>Pedido3</th>";
		
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
		//Totales para % de Contribucion
		double total1 = 0;
		//Obtiene totales para el porcentaje de contribucion
		for(int a=0; a < dimCvPag.size(); a++){
			clv_tot = dtNom.get(dimCvPag.get(a));
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				String medd1 = dtMedTot.get(clv_tot+"_"+y);
				total1 += Double.parseDouble(medd1);
				
			}
		}
		
		HashMap<String, String> sumaTotal = new HashMap<String, String>();
		double val_med_1 = 0;for(int a=0; a < dimCvPag.size(); a++){
			
			
			clv = dtNom.get(dimCvPag.get(a));
			val = pptos.getDimNameTable(chart_id, cust_id,(String) dtNom.get(dimCvPag.get(a)), id_modulo);
			String val_encode =  URLEncoder.encode(val, "ISO-8859-1");
			String ruta = "popup.jsp?id_user="+id_user+"&id_modulo=2&id_dashboard="+id_dashboard+"&id_chart="+
			chart_id+"&parametro="+cmp_filtro+"&val_param="+clv+"&val_org="+val_encode+"&nom_org="+xAxisName+"&delFil=true";
			
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				
				medd_1 = dtMedPag.get(clv+"_"+y);
				
				sumaTotal.put("1_"+Integer.toString(y), medd_1);
				
				
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
				
				
				html += "<tr>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "</tr>";		
			}
			////System.out.println("Total: " + sumTotal);
			//html += html_med_2;
			
		}
		int total = 0;
		int contS = 0;
		
		html += "<tr><td><font color=red><strong>Total</strong></td>";
		for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
			
			if(contS != 0){
				total = 0;
				contS = 0;
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
					
			DecimalFormatSymbols simb = new DecimalFormatSymbols();
			simb.setDecimalSeparator('.');
			simb.setGroupingSeparator(',');
			DecimalFormat format_med = new DecimalFormat("###,###.####",simb);
			
			String sumaTot = format_med.format(total);
				
			//Totales para las columnas
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
		}
		html += "</tr>";
		html += "</tbody>";
		html += "</table>";
		System.out.println("Tabla: "+html);
		return html;
	}
	
	//Tabla Ventas
	public String getTableVentas(
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
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		//Descripcion de los datos
		HashMap<String, String> dtNom = new HashMap<String, String>();/////---nom
		//Medida correspondiente
		HashMap<String, String> dtMedPag = new HashMap<String, String>();///----med
		//Total de la medida
		HashMap<String, String> dtMedTot = new HashMap<String, String>();///----total_med
		
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
		HashMap body = pptos.getDataBody(chart_id, cust_id, anioIni, anioFin, 
				id_user, id_dashboard, gpoMed, id_modulo, filtros);
		
		SortedSet<Integer> sort = new TreeSet<Integer>(head.keySet());
		Iterator it = sort.iterator();
		HashMap prmt = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		String anio = (String)prmt.get("anio");
		
		//Lista de ID�s de los datos
		ArrayList<String> dimId = new ArrayList<String>();
		int cont = 0;
		HashMap nom_gpo_med = pptos.getNomMedTable(id_user, id_dashboard, gpoMed);
		
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		html += "<tr>";
		String cmp_med_1 = "";
		
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
		
		//Nombre De las Columnas PAra La Tabla(Grid) Previcion de Compras...
		html += "<th>Clave</th>";
		html += "<th>" + xAxisName + "</th>";
		html += "<th>Prom Dia</th>";
		html += "<th>Dias de Inventario (Dias)</th>";
		html += "<th>Dias de Inventario (Fecha)</th>";
		html += "<th>X Facturar Normal</th>";
		html += "<th>Disponible</th>";
		html += "<th>Tiempo Libre Para Mantener Stock</th>";
		html += "<th>Tiempo Libre Para Levantar Pedido</th>";
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
		//Totales para % de Contribucion
		double total1 = 0;
		//Obtiene totales para el porcentaje de contribucion
		for(int a=0; a < dimCvPag.size(); a++){
			clv_tot = dtNom.get(dimCvPag.get(a));
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				String medd1 = dtMedTot.get(clv_tot+"_"+y);
				total1 += Double.parseDouble(medd1);
				
			}
		}
		
		HashMap<String, String> sumaTotal = new HashMap<String, String>();
		double val_med_1 = 0;for(int a=0; a < dimCvPag.size(); a++){
			
			
			clv = dtNom.get(dimCvPag.get(a));
			val = pptos.getDimNameTable(chart_id, cust_id,(String) dtNom.get(dimCvPag.get(a)), id_modulo);
			String val_encode =  URLEncoder.encode(val, "ISO-8859-1");
			String ruta = "popup.jsp?id_user="+id_user+"&id_modulo=2&id_dashboard="+id_dashboard+"&id_chart="+
			chart_id+"&parametro="+cmp_filtro+"&val_param="+clv+"&val_org="+val_encode+"&nom_org="+xAxisName+"&delFil=true";
			
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				
				medd_1 = dtMedPag.get(clv+"_"+y);
				
				sumaTotal.put("1_"+Integer.toString(y), medd_1);
				
				
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
				
				
				html += "<tr>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "</tr>";		
			}
			////System.out.println("Total: " + sumTotal);
			//html += html_med_2;
			
		}
		int total = 0;
		int contS = 0;
		
		html += "<tr><td><font color=red><strong>Total</strong></td>";
		for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
			
			if(contS != 0){
				total = 0;
				contS = 0;
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
					
			DecimalFormatSymbols simb = new DecimalFormatSymbols();
			simb.setDecimalSeparator('.');
			simb.setGroupingSeparator(',');
			DecimalFormat format_med = new DecimalFormat("###,###.####",simb);
			
			String sumaTot = format_med.format(total);
				
			//Totales para las columnas
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
		}
		html += "</tr>";
		html += "</tbody>";
		html += "</table>";
		System.out.println("Tabla: "+html);
		return html;
	}
	
	//Tabla Pedidos
	public String getTablePedidos(
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
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		//Descripcion de los datos
		HashMap<String, String> dtNom = new HashMap<String, String>();/////---nom
		//Medida correspondiente
		HashMap<String, String> dtMedPag = new HashMap<String, String>();///----med
		//Total de la medida
		HashMap<String, String> dtMedTot = new HashMap<String, String>();///----total_med
		
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
		HashMap body = pptos.getDataBody(chart_id, cust_id, anioIni, anioFin, 
				id_user, id_dashboard, gpoMed, id_modulo, filtros);
		
		SortedSet<Integer> sort = new TreeSet<Integer>(head.keySet());
		Iterator it = sort.iterator();
		HashMap prmt = pptos.getDataParameters(id_user, id_dashboard, id_modulo);
		String anio = (String)prmt.get("anio");
		
		//Lista de ID�s de los datos
		ArrayList<String> dimId = new ArrayList<String>();
		int cont = 0;
		HashMap nom_gpo_med = pptos.getNomMedTable(id_user, id_dashboard, gpoMed);
		
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		html += "<tr>";
		String cmp_med_1 = "";
		
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
		
		//Nombre De las Columnas PAra La Tabla(Grid) Previcion de Compras...
		html += "<th>Clave</th>";
		html += "<th>" + xAxisName + "</th>";
		html += "<th>Pedido 1,2,3</th>";
		html += "<th>Dias Para Que Llegue (Dias)</th>";
		html += "<th>Dias Para Que Llegue (Fecha)</th>";
		html += "<th>Dias de Inventario al Llegar (DDI Dias)</th>";
		html += "<th>Dias de Inventario al Llegar (DDI Fecha)</th>";
		html += "<th>Continuidad Existencias al Minimo</th>";
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
		//Totales para % de Contribucion
		double total1 = 0;
		//Obtiene totales para el porcentaje de contribucion
		for(int a=0; a < dimCvPag.size(); a++){
			clv_tot = dtNom.get(dimCvPag.get(a));
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				String medd1 = dtMedTot.get(clv_tot+"_"+y);
				total1 += Double.parseDouble(medd1);
				
			}
		}
		
		HashMap<String, String> sumaTotal = new HashMap<String, String>();
		double val_med_1 = 0;for(int a=0; a < dimCvPag.size(); a++){
			
			
			clv = dtNom.get(dimCvPag.get(a));
			val = pptos.getDimNameTable(chart_id, cust_id,(String) dtNom.get(dimCvPag.get(a)), id_modulo);
			String val_encode =  URLEncoder.encode(val, "ISO-8859-1");
			String ruta = "popup.jsp?id_user="+id_user+"&id_modulo=2&id_dashboard="+id_dashboard+"&id_chart="+
			chart_id+"&parametro="+cmp_filtro+"&val_param="+clv+"&val_org="+val_encode+"&nom_org="+xAxisName+"&delFil=true";
			
			for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
				
				medd_1 = dtMedPag.get(clv+"_"+y);
				
				sumaTotal.put("1_"+Integer.toString(y), medd_1);
				
				
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
				
				
				html += "<tr>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
				html += "</tr>";		
			}
			////System.out.println("Total: " + sumTotal);
			//html += html_med_2;
			
		}
		int total = 0;
		int contS = 0;
		
		html += "<tr><td><font color=red><strong>Total</strong></td>";
		for(int y=Integer.parseInt(anioFin); y <= Integer.parseInt(anioFin); y++){
			
			if(contS != 0){
				total = 0;
				contS = 0;
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
					
			DecimalFormatSymbols simb = new DecimalFormatSymbols();
			simb.setDecimalSeparator('.');
			simb.setGroupingSeparator(',');
			DecimalFormat format_med = new DecimalFormat("###,###.####",simb);
			
			String sumaTot = format_med.format(total);
				
			//Totales para las columnas
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>";
		}
		html += "</tr>";
		html += "</tbody>";
		html += "</table>";
		System.out.println("Tabla: "+html);
		return html;
	}
	
	//Tabla analisis de ventas
	public String getTableProdNoDesplaza(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String mes) throws UnsupportedEncodingException {
		//Configuracion del portlet o grid
		//HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		//String cmp_filtro = (String) hm.get("cmp_id");
		String html = "";
		boolean org_param = false;
		
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr>";
		html += "<th></th>";
		html += "<th></th>";
		html += "<th align=center>Tendencia</th>";
		html += "<th align=center><strong>Total</strong></th>";
		html += "<th align=center>Promedio</th>";
		html += "<th align=center>Tendencia - Promedio</th>";
		html += "<th align=center>Promedio Diario</th>";
		html += "<th align=center>Disponible</th>";
		html += "<th align=center>Costo</th>";
		html += "<th align=center>D&iacute;as Inventarios Promedio</th>";
		html += "<th align=center>D&iacute;as Inventarios Tendencia</th>";
		html += "<th class='header headerSortUp' align=center>Desplazamiento</th>";
		html += "</tr>";
		
		html += "<tr>";
				
		//Nombre De las Columnas PAra La Tabla(Grid) Previcion de Compras...
		html += "<td></td>";
		html += "<td></td>";
		html += "<td colspan=3 align=center>CAJAS</td>";
		html += "<td></td>";
		html += "<td colspan=2 align=center>CAJAS</td>";
		html += "<td align=center>$</td>";
		html += "<td align=center>DIAS</th>";
		html += "<td align=center>DIAS</td>";
		html += "<td align=center></td>";
		html += "</thead>";
		html +="<tbody >";
		
		Double promedio = 0.0;
		Double promedio_diario = 0.0;
		Double dias_inventario = 0.0;
		Double costo = 0.0;
		List<InvDTO> row = invent.getDataProdNoDesplaza(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id);
		HashMap hm_semanas = invent.getDataSemanasProdNoDesplaza(cust_id, id_user, id_modulo, id_dashboard, 18, chart_id);
		
		for(int i = 0; i < row.size(); i ++){
			
			InvDTO dto = row.get(i);
			
			
			Double existencias = Double.parseDouble((String) dto.getTtl_cajas_ext());
			BigDecimal bd_existencias = new BigDecimal(existencias);
			BigDecimal rd_existencias  = bd_existencias.setScale(1, RoundingMode.HALF_UP);
			
			promedio = Double.parseDouble((String) dto.getTtl_cajas() ) / 18;
			BigDecimal bd_promedio = new BigDecimal(promedio);
			BigDecimal rd_promedio  = bd_promedio.setScale(1, RoundingMode.HALF_UP);
			
			promedio_diario = Double.parseDouble((String) dto.getTtl_cajas() ) / 126;
			BigDecimal bd_promedio_diario = new BigDecimal(promedio_diario);
			BigDecimal rd_promedio_diario  = bd_promedio_diario.setScale(1, RoundingMode.HALF_UP);
			
			
			
			
			Double tendencia = 0.0;
			Double tendencia_prom = 0.0;
			Double dias_invent_tendencia = 0.0;
			Double desplazamiento = 0.0;
			Double m = 0.0;
			Double b = 0.0;
			int numSem = 18;
			String sumCajas = "";
			Double sm_ttl_cajas = 0.0;
			Double sm_semanas = 0.0;
			Double sm_semanas_x_semanas = 0.0;
			Double sm_ttl_cajas_x_sem = 0.0;
			//Genera la TENDENCIA para cada producto
			for (int x = 1; x <= numSem; x ++){
				sm_semanas += x;
				sm_semanas_x_semanas += (double) (x * x);
				sumCajas = (String)hm_semanas.get(dto.getIdProducto()+"_"+x);
				//System.out.println("Cajas: "+sumCajas);
				if(!sumCajas.equals("null")){
					sm_ttl_cajas += Double.parseDouble(sumCajas);
					sm_ttl_cajas_x_sem += (x * Double.parseDouble(sumCajas));
				}
			}
			b = ((sm_ttl_cajas * sm_semanas_x_semanas)- (sm_semanas * sm_ttl_cajas_x_sem)) 
					/ ((numSem * sm_semanas_x_semanas) - (sm_semanas * sm_semanas));
			m = ((numSem * sm_ttl_cajas_x_sem)-(sm_semanas * sm_ttl_cajas))
					/ ((numSem * sm_semanas_x_semanas) - (sm_semanas * sm_semanas));
			
			Double total = sm_ttl_cajas;
			BigDecimal bd_total = new BigDecimal(total);
			BigDecimal rd_total  = bd_total.setScale(1, RoundingMode.HALF_UP);
			
			if(total != 0){
				dias_inventario = existencias / promedio_diario;
			}else{
				dias_inventario = 0.0;
			}
			BigDecimal bd_dias_inventario = new BigDecimal(dias_inventario);
			BigDecimal rd_dias_inventario  = bd_dias_inventario.setScale(1, RoundingMode.HALF_UP);
			
			tendencia = (m * 1) + b;
			BigDecimal bd_tendencia = new BigDecimal(tendencia);
			BigDecimal rd_tendencia  = bd_tendencia.setScale(1, RoundingMode.HALF_UP);
			if(sm_ttl_cajas != 0.0){
				
				tendencia_prom = (tendencia / promedio) ;///100;
				//System.out.println(tendencia+"/"+promedio+" = "+ tendencia_prom);
			}
			BigDecimal bd_tendencia_prom = new BigDecimal(tendencia_prom);
			BigDecimal rd_tendencia_prom  = bd_tendencia_prom.setScale(1, RoundingMode.HALF_UP);
			
			if(tendencia != 0.0){
				dias_invent_tendencia = existencias /(tendencia/7);
			}
			BigDecimal bd_dias_invent_tendencia = new BigDecimal(Math.abs(dias_invent_tendencia));
			BigDecimal rd_dias_invent_tendencia  = bd_dias_invent_tendencia.setScale(1, RoundingMode.HALF_UP);
			
			if(dias_inventario != 0.0){
				desplazamiento = dias_invent_tendencia / dias_inventario;
			}
			BigDecimal bd_desplazamiento = new BigDecimal(Math.abs(desplazamiento));
			BigDecimal rd_desplazamiento = bd_desplazamiento.setScale(1, RoundingMode.HALF_UP);

			Double pendiente_x_fact = 0.0 ;
			String pendXFact = "0.0";
			pendXFact = dto.getPendiente_x_fact();
			if(pendXFact != null && !pendXFact.equals("null")){
				pendiente_x_fact = Double.parseDouble(pendXFact); 
			}
			
			BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
			BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP);
			//System.out.println("Disponibles: "+existencias +" - "+pendiente_x_fact);
			Double disponible = existencias - pendiente_x_fact;
			BigDecimal bd_disponible = new BigDecimal(disponible);
			BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
			
			costo = Double.parseDouble((String) dto.getCosto() ) * disponible ;// * existencias;
			BigDecimal bd_costo = new BigDecimal(costo);
			BigDecimal rd_costo  = bd_costo.setScale(0, RoundingMode.HALF_UP);
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
			//System.out.println("Total Cajas X Semana: "+dto.getIdProducto()+": "+sm_semanas+" - "+sm_ttl_cajas+" - "+sm_semanas_x_semanas+" - "+sm_ttl_cajas_x_sem+" - "+ tendencia);
			//Link en producto, el cual abrira el simulador
			String nomProducto = "";
			nomProducto = "<a href='#' onclick=abrePop('"+"detalle_producto.jsp?id_prod="+dto.getIdProducto()+"&id_mar="+dto.getIdMarca()+"&id_chart="+chart_id+"');><strong> +</strong></a>";
			
			html += "<tr>";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Producto
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getProducto()+nomProducto+"</td>"; //Producto
			
			String color = "";
			Double numC = 0.0;
			if(!rd_tendencia.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_tendencia.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color+">"+rd_tendencia+"</td>"; //Tendencia
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Tendencia
			}
			
			if(!rd_total.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_total.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color+">"+frmt.format(rd_total)+"</td>"; //Total
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Total
			}
			
			if(!rd_promedio.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_promedio.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color+">"+frmt.format(rd_promedio)+"</td>"; //Promedio
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Promedio
			}
			
			//Semaforizacion
			String s_tendenciaProm = "";
			String c_amarilloClaro = "#FFFF00";
			String c_amarilloFuerte = "#D7DF01";
			String c_azulClaro = "#00FFFF";
			String c_naranjaObscuro = "#FF8000";
			
			String colorRojo =  "#FF0000";
			String colorNaranja = "#FE9A2E";
			String colorNaranjaSuave = "#F5BCA9";
			String colorVerdeClaro  = "#A9F5A9";
			String colorNaranjaClaro = "#F5D0A9";
			
			/*String amarilloClaro = "#FDFEB0";
			String amarilloObscuro = "#FAFC80";
			String verdeClaro  = "#C6EBC9";
			String verdeObscuro = "#77A77B";*/
			
			String rojoFuerte  = "#FF6161";
			String rojoMedio  ="#FFB8B7";
			String verdeObscuro = "#C6E6A2";
			
			String amarilloSuave = "#FFF0C9";
			String amarilloMedio = "#FFDC99";
			String amarilloFuerte = "#FFC799";

			
			BigDecimal bd_tendenciaProm = new BigDecimal(tendencia_prom);
			BigDecimal rd_tendenciaProm = bd_tendenciaProm.setScale(4, RoundingMode.HALF_UP);
			
			
			
			if(rd_tendenciaProm.floatValue() > 0.0085 && rd_tendenciaProm.floatValue() <= 0.0089){ //Amarillo Claro
				s_tendenciaProm = amarilloSuave;
			}
			if(rd_tendenciaProm.floatValue() > 0.0075 && rd_tendenciaProm.floatValue() <= 0.0085){ //Azul Claro
				s_tendenciaProm = amarilloMedio;
			}
			if(rd_tendenciaProm.floatValue() > 0.0000 && rd_tendenciaProm.floatValue() <= 0.0075 ){ //Naranja Obscuro
				s_tendenciaProm = amarilloFuerte;
			}
			//System.out.println("Color <-----> "+s_tendenciaProm + " -- "+rd_tendenciaProm +" -> "+rd_tendencia_prom);
			if(!rd_tendencia_prom.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_tendencia_prom.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_tendenciaProm+"'><font color="+color+">"+frmt.format(rd_tendencia_prom)+" %</td>"; //Tendencia - Promedio
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Tendencia - Promedio
			}
			
			if(!rd_promedio_diario.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_promedio_diario.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color+">"+frmt.format(rd_promedio_diario)+"</td>"; //Promedio Diario
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Promedio Diario
			}
			
			if(!rd_disponible.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_disponible.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color+">"+frmt.format(rd_disponible)+"</td>"; //Existencias
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Existencias
			}
			
			if(!rd_costo.toString().equals("0")){
				color  = "";
				numC = Double.parseDouble(rd_costo.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color+">"+frmt.format(rd_costo)+"</td>"; //Costo
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Costo
			}
			
			if(!rd_dias_inventario.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_dias_inventario.toString());
				if(numC < 0.0){
					color = "red";
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color+">"+frmt.format(rd_dias_inventario)+"</td>"; //Dias Inventario Promedio
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Dias Inventario Promedio
			}
			
			String c_invenTendencia = "";
			Double min_stock = Double.parseDouble(dto.getMinstock());
			
			if(!rd_dias_invent_tendencia.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_dias_invent_tendencia.toString());
				if(numC < 0.0){
					color = "red";
				}
				if(rd_dias_invent_tendencia.doubleValue() > (min_stock * 0.5) && rd_dias_invent_tendencia.doubleValue() > (dias_inventario * 1.2)){
					c_invenTendencia = rojoMedio;
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+c_invenTendencia+"'><font color="+color+" >"+frmt.format(rd_dias_invent_tendencia)+"</td>"; //Dias Inventario Tendencia
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Dias Inventario Tendencia
			}
			
			//Semaforizacion
			String s_desplazamiento = "";
			if(rd_desplazamiento.doubleValue() >= 0 && rd_desplazamiento.doubleValue() <= 0.8){
				s_desplazamiento  = verdeObscuro;
			}
			if(rd_desplazamiento.doubleValue() >= 1.5 && rd_desplazamiento.doubleValue() <= 3){
				s_desplazamiento = rojoMedio;
			}
			if(rd_desplazamiento.doubleValue() > 3 ){ // &&   rd_desplazamiento.doubleValue() <= 20
				s_desplazamiento = rojoFuerte;
			}
			System.out.println(dto.getProducto()+"  "+ desplazamiento + " "+rd_desplazamiento);
			if(!rd_desplazamiento.toString().equals("0.0")){
				color  = "";
				numC = Double.parseDouble(rd_desplazamiento.toString());
				if(numC < 0.0){
					color = "red";
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_desplazamiento+"'><font color="+color+">"+frmt.format(rd_desplazamiento)+"</td>"; //Desplazamiento
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Desplazamiento
			}
			
			html += "</tr>";
		}	
		html += "</tbody></table>";
		
		
		System.out.println("Tabla... ");
		return html;
	}
	//Tabla analisis de ventas
		public String getTableAnalisisVentas(
				String cust_id,
				String id_user,
				String id_modulo,
				String id_dashboard,
				String chart_id,
				String mes,
				String ver,
				String act) throws UnsupportedEncodingException, ParseException {
			//Configuracion del portlet o grid
			HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
			//Filtros para el portlet o grid
			String cmp_filtro = (String) hm.get("cmp_id");
			String html = "";
			boolean org_param = false;
			int meses_enc = 3;
			int meses_col = 3;
			int meses_fil = 3;
			int meses_ttl = 3;
			int meses_marca = 3;
			if(mes == null || mes == "" || mes.equals("null")){
			}else{
				meses_enc = Integer.parseInt(mes);
				meses_col = Integer.parseInt(mes);
				meses_fil = Integer.parseInt(mes);
				meses_ttl = Integer.parseInt(mes);
			}
			
			html += "<table id=tblDatos class=tablesorter border=1 >";
			html += "<thead>";
			
			//Encabezado
			html += "<tr><td colspan=2></td>";
			for (int x = 1; x <= meses_enc; meses_enc --){
				html += "<td id=Mes"+meses_enc+" class=mes>P"+meses_enc+"</td>";
			}
			/*html += "<td id=Mes4 class=mes>Mes 4</td>";
			html += "<td id=Mes3 class=mes>Mes 3</td>";
			html += "<td id=Mes2 class=mes>Mes 2</td>";
			html += "<td id=Mes1 class=mes>Mes 1</td>";*/
			html += "<td>Total</td>";
			html += "<td>Prom/P</td>";
			html += "<td>Existencias</td>";
			html += "<td>P/Inventario</td>";
			html += "<td>X Facturar</td>";
			html += "<td>Disponible</td>";
			//html += "<td>Costo</td>";
			html += "<td colspan=2>P/Inventarios</td>";
			html += "</tr>";
			
			html += "<tr>";
					
			//Nombre De las Columnas PAra La Tabla(Grid) Previcion de Compras...
			
			html += "<th></th>";
			html += "<th></th>";
			//System.out.println("Meses..."+meses_col);
			for (int y = 1; y <= meses_col; meses_col --){
				html += "<th>CAJAS</th>";
			}
			/*html += "<th>Cajas</th>";
			html += "<th>Cajas</th>";
			html += "<th>Cajas</th>";*/
			
			html += "<th>CAJAS</th>";
			html += "<th>CAJAS</th>";
			html += "<th>CAJAS</th>";
			html += "<th>PERIODO</th>";
			html += "<th>CAJAS</th>";
			html += "<th>CAJAS</th>";
			//html += "<th>$</th>";
			html += "<th>PERIODO</th>";
			html += "<th>Fecha</th>";
			html += "</tr>";
			html += "</thead>";
			html +="<tbody >";
			
			
			Calendar m1_Ini = new GregorianCalendar();
			Calendar m1_Fin = new GregorianCalendar();
			Calendar m2_Ini = new GregorianCalendar();
			Calendar m2_Fin = new GregorianCalendar();
			Calendar m3_Ini = new GregorianCalendar();
			Calendar m3_Fin = new GregorianCalendar();
			Calendar m4_Ini = new GregorianCalendar();
			Calendar m4_Fin = new GregorianCalendar();
			Calendar hoy = new GregorianCalendar();
			
			String fechaL = invent.obtieneFechaCargaDatos();
		    //System.out.println("Fecha De Carga--->"+fechaL);
		    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date fLoad = null;
			
			if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
				fLoad =  formatoF2.parse(fechaL);
				hoy.setTime(fLoad);
			}else{
				hoy.add(Calendar.DATE , -1);
			}
			
			 //Formato para fecha
		    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
			// Mes 1
		
		    //Tooltips para el rango de los meses 
		    String toltip_meses = "";
		    int rango_mes = 28;
		    Calendar fecha_hoy = new GregorianCalendar();
		    Calendar inicio = new GregorianCalendar();
		    Calendar fin =  new GregorianCalendar();
		    int diasI = 0, diasF = 0;
		    for(int w = 1; w <= meses_ttl; w ++){
		    	fecha_hoy = new GregorianCalendar();
		    	inicio = new GregorianCalendar();
		    	fin  = new GregorianCalendar();
		    	//System.out.println("Periodo Fechas Inicio: "+formatoDeFecha.format(fin.getTime()) +" -- "+formatoDeFecha.format(inicio.getTime()) );
		    	if(w != 1){
		    		inicio.setTime(fLoad);
		    		fin.setTime(fLoad);
		    		diasI =  ((rango_mes )*(w-1));
		    		diasF =  (rango_mes*w) -1;
		    		//System.out.println("Ini: "+diasI +" Fin: "+diasF);
		    		inicio.add(Calendar.DATE, -diasI);
		    		fin.add(Calendar.DATE, -diasF );
		    	}else{
		    		inicio.setTime(fLoad);
		    		fin.setTime(fLoad);
		    		//inicio.add(fecha_hoy.DATE,-1);
		    		fin.add(Calendar.DATE, -rango_mes + 1);
		    	}
		    	toltip_meses += "<div id=msgMes"+w+" style='display: none;' class=tip >"+ formatoDeFecha.format(fin.getTime()) + " - " + formatoDeFecha.format(inicio.getTime())+"</div>";;
		    	
		    }
		    
			List<InvDTO> row = invent.getDataAnalisisVentas(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, act); 
			HashMap hm_meses = invent.getDataMesesAnalisisVentas(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, act);
			HashMap ttl_marcas = invent.getDataMesesAnalisisVentasMarcas(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, act);
			for(int i = 0; i < row.size(); i ++){
				//System.out.println(row.get(i));
				//Crear total para agrupamiento por marca
				String marca_ttl ="";
				
				InvDTO dto = row.get(i);
				if(mes == null || mes == "" || mes.equals("null")){
					meses_fil = 3;
					meses_ttl = 3;
					meses_marca = 3; 
				}else{
					meses_fil = Integer.parseInt(mes);
					meses_ttl = Integer.parseInt(mes);
					meses_marca = Integer.parseInt(mes);
				}
				Calendar cal = new GregorianCalendar();
				DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
				simbolo.setDecimalSeparator('.');
				simbolo.setGroupingSeparator(',');
				DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
				
				String mes_prb = (String) hm_meses.get(dto.getKey()+"_ttl");
				Double min_stock = Double.parseDouble((String)dto.getMinstock()); 
				Double ttl_cajas = 0.0;
				//System.out.println("Null "+mes_prb);
				if(mes_prb != null){
					ttl_cajas = Double.parseDouble(mes_prb);
				}
				int perd_dias = meses_fil;// * 28;
						
				BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
				BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
				
				
				Double clc_prom_diario = ttl_cajas / perd_dias;
				BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
				BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
				Double existencia = Double.parseDouble(dto.getTtl_cajas_ext());
				BigDecimal bd_existencia = new BigDecimal(existencia);
				BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
				
				Double minStockI = clc_prom_diario * min_stock;
				Double minStockIPerc = (minStockI * 0.15) + minStockI;
				
				
				//int dias_invent = (int) Math.round(existencia / clc_prom_diario);
				Double dias_invent = existencia / clc_prom_diario;
				if(dias_invent.isInfinite() || dias_invent.isNaN()){
					dias_invent = 0.00;
				}
				BigDecimal bd_diasInvent =  new BigDecimal(0.0);
				bd_diasInvent = new BigDecimal(dias_invent);
				BigDecimal rd_diasInvent = bd_diasInvent.setScale(2,RoundingMode.HALF_UP);
				
				Double pendiente_x_fact = Double.parseDouble(dto.getPendiente_x_fact());
				BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
				BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
				Double disponible = existencia - pendiente_x_fact;
				BigDecimal bd_disponible = new BigDecimal(disponible);
				BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
				
				Double dia_inventario = disponible / clc_prom_diario;
				//Double d_dia_inventario = disponible / clc_prom_diario;
				if(dia_inventario.isInfinite() || dia_inventario.isNaN()){
					dia_inventario = 0.00;
				}
				BigDecimal bd_dia_inventario = new BigDecimal(0.0);
				bd_dia_inventario = new BigDecimal(dia_inventario);
				BigDecimal rd_dia_inventario = bd_dia_inventario.setScale(2,RoundingMode.HALF_UP);
				BigDecimal i_dia_inventario = bd_dia_inventario.setScale(0,RoundingMode.HALF_UP);
				
				Double costo = Double.parseDouble(dto.getCosto());
				Double costo_final = disponible * costo;
				int dia_invent_fin = (int) Math.round(dia_inventario - 1);
				
				BigDecimal bd_costo_final = new BigDecimal(costo_final);
				BigDecimal rd_costo_final = bd_costo_final.setScale(1, RoundingMode.HALF_UP);
				//cal.add(Calendar.DATE, dia_invent_fin);
				
				//******************************************
				String ver_class = "";
				if(ver != null && !ver.equals("null") && ver.equals("marca")){
				String marca_ini = "";
				String marca_fin = "";
				String marca = "";
				if(i == 0){
					//marca_ini = dto.getIdMarca();
					//marca_fin = row.get(i+1).getIdMarca();
					//System.out.println("Marcas: ini - "+marca_ini+" fin - "+marca_fin);
					//if(!marca_ini.equals(marca_fin)){
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						//System.out.println("Meses Fil: "+ meses_marca);
						for (int a = 1; a <= meses_marca; meses_marca --){
							double ttl_cajas_x_mes = 0.0;
							//System.out.println("cve "+dto.getIdMarca()+"_"+meses_marca);
							String ttl = (String) ttl_marcas.get(dto.getIdMarca()+"_"+meses_marca);
							//System.out.println("Total: "+ttl);
							if(ttl != null && !ttl.equals("null")){
								ttl_cajas_x_mes = Double.parseDouble(ttl);
							}
							BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
							BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_cajas_mes)+"</strong></td>"; //Mes4
						}
						Double ttl_cajas_mar = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						BigDecimal bd_ttl_cajas_mar = new BigDecimal(ttl_cajas_mar);
						BigDecimal rd_ttl_cajas_mar = bd_ttl_cajas_mar.setScale(1, RoundingMode.HALF_UP);
						
						Double clc_prom_diario_m = ttl_cajas_mar / perd_dias;
						BigDecimal prom_diario_m = new BigDecimal(clc_prom_diario_m);
						BigDecimal rd_prom_diario_m = prom_diario_m.setScale(1, RoundingMode.HALF_UP);
						

						Double ttl_existencias_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						BigDecimal bd_ttl_existencias_m = new BigDecimal(ttl_existencias_m);
						BigDecimal rd_ttl_existencias_m = bd_ttl_existencias_m.setScale(1, RoundingMode.HALF_UP);
						
						Double pendiente_x_fact_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						BigDecimal bd_pendiente_x_fact_m = new BigDecimal(pendiente_x_fact_m);
						BigDecimal rd_pendiente_x_fact_m = bd_pendiente_x_fact_m.setScale(1, RoundingMode.HALF_UP);
						
						Double costo_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_costo"));
						BigDecimal bd_costo_m = new BigDecimal(costo_m);
						BigDecimal rd_costo_m = bd_costo_m.setScale(1, RoundingMode.HALF_UP);
						
						Double dias_inventarios = 0.0;
						if(clc_prom_diario_m != 0.0){
							dias_inventarios = ttl_existencias_m / clc_prom_diario_m;
						}
						BigDecimal bd_dias_inventarios = new BigDecimal(dias_inventarios);
						BigDecimal rd_dias_inventarios  = bd_dias_inventarios.setScale(2, RoundingMode.HALF_UP);
						
						Double cajas_disponibles = ttl_existencias_m - pendiente_x_fact_m;
						BigDecimal bd_cajas_disponibles = new BigDecimal(cajas_disponibles);
						BigDecimal rd_cajas_disponibles  = bd_cajas_disponibles.setScale(1, RoundingMode.HALF_UP);
						
						//Semaforizacion
						System.out.println("Semaforizacion --> "+minStockI + " -- "+minStockI);
						if(Math.abs(disponible) >= Math.abs(minStockI) && Math.abs(disponible) <= Math.abs(minStockI)){
							System.out.println("Color Suave --> "+minStockI + " -- "+minStockI);
						}
						if(Math.abs(disponible) <= Math.abs(min_stock)){
							System.out.println("Color Fuerte --> "+minStockI + " -- "+minStockI);
						}
						Double dias_inventarios_in = 0.0;
						if(clc_prom_diario_m != 0.0){
							dias_inventarios_in = cajas_disponibles / clc_prom_diario_m;
						}
						BigDecimal bd_dias_inventarios_in = new BigDecimal(dias_inventarios_in);
						BigDecimal rd_dias_inventarios_in  = bd_dias_inventarios_in.setScale(2, RoundingMode.HALF_UP);
						//System.out.println("cont 1 = "+i);
						
						int diaM = 0;
						diaM = (int) Math.round(clc_prom_diario_m);
						
						Calendar fechaM = new GregorianCalendar();
						fechaM.setTime(fLoad);
						fechaM.add(Calendar.DATE, diaM);
						
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_cajas_mar)+"</strong></td>"; //Total por periodo de Meses
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_prom_diario_m)+"</trong></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_existencias_m)+"</trong></td>"; // existencia en inventarios
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_inventarios)+"</trong></td>"; // Dias de inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_pendiente_x_fact_m)+"</trong></td>"; // P?endiente por facturar
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_cajas_disponibles)+"</trong></td>"; //Cajas Disponmibles
						//html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_costo_m+"</trong></td>"; // Costo
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_inventarios_in)+"</trong></td>"; // Dia Inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+formatoDeFecha.format(fechaM.getTime())+"</strong></td>"; // Fecha
						html += "</tr>";
						//System.out.println("marcaaa:--- "+dto.getMarca()); 
					//}
				}else{
					
					if(i+1 < row.size()){
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i-1).getIdMarca();
						//System.out.println("Marcas: ini - "+marca_ini+" fin - "+marca_fin);
						//System.out.println("Meses Fil: "+ meses_marca);
						if(!marca_ini.equals(marca_fin)){
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getMarca()+"" +
									"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
											"</strong></td>"; //Producto
							for (int a = 1; a <= meses_marca; meses_marca --){
								double ttl_cajas_x_mes = 0.0;
								//System.out.println("cve "+dto.getIdMarca()+"_"+meses_marca);
								String ttl = (String) ttl_marcas.get(dto.getIdMarca()+"_"+meses_marca);
								//System.out.println("Total: "+ttl);
								if(ttl != null && !ttl.equals("null")){
									ttl_cajas_x_mes = Double.parseDouble(ttl);
								}
								//System.out.println("Cve: "+dto.getIdMarca()+"_"+meses_marca+" ttl_mes: "+ttl_cajas_x_mes);
								BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
								BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
								html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_cajas_mes)+"</strong></td>"; //Mes4
							}
							String ttl_mar_prb = (String) ttl_marcas.get(dto.getIdMarca()+"_ttl_mes");
							Double ttl_cajas_mar = 0.0;
							if(ttl_mar_prb != null){
								ttl_cajas_mar = Double.parseDouble(ttl_mar_prb);
							}
							BigDecimal bd_ttl_cajas_mar = new BigDecimal(ttl_cajas_mar);
							BigDecimal rd_ttl_cajas_mar = bd_ttl_cajas_mar.setScale(1, RoundingMode.HALF_UP);
							
							Double clc_prom_diario_m = ttl_cajas_mar / perd_dias;
							BigDecimal prom_diario_m = new BigDecimal(clc_prom_diario_m);
							BigDecimal rd_prom_diario_m = prom_diario_m.setScale(1, RoundingMode.HALF_UP);
							

							Double ttl_existencias_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
							BigDecimal bd_ttl_existencias_m = new BigDecimal(ttl_existencias_m);
							BigDecimal rd_ttl_existencias_m = bd_ttl_existencias_m.setScale(1, RoundingMode.HALF_UP);
							
							Double pendiente_x_fact_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
							BigDecimal bd_pendiente_x_fact_m = new BigDecimal(pendiente_x_fact_m);
							BigDecimal rd_pendiente_x_fact_m = bd_pendiente_x_fact_m.setScale(1, RoundingMode.HALF_UP);
							
							Double costo_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_costo"));
							BigDecimal bd_costo_m = new BigDecimal(costo_m);
							BigDecimal rd_costo_m = bd_costo_m.setScale(1, RoundingMode.HALF_UP);
							
							//Semaforizacion
							System.out.println("Semaforizacion --> "+minStockI + " -- "+minStockI);
							if(Math.abs(disponible) >= Math.abs(minStockI) && Math.abs(disponible) <= Math.abs(minStockI)){
								System.out.println("Color Suave --> "+minStockI + " -- "+minStockI);
							}
							if(Math.abs(disponible) <= Math.abs(min_stock)){
								System.out.println("Color Fuerte --> "+minStockI + " -- "+minStockI);
							}
							
							Double dias_inventarios = 0.0;
							//System.out.println("Dias Inv "+ttl_existencias_m +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios = ttl_existencias_m / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios = new BigDecimal(dias_inventarios);
							BigDecimal rd_dias_inventarios  = bd_dias_inventarios.setScale(2, RoundingMode.HALF_UP);
							
							//System.out.println("Cajas: Disponibles: "+ttl_existencias_m +" - "+ pendiente_x_fact_m);
							Double cajas_disponibles = ttl_existencias_m - pendiente_x_fact_m;
							BigDecimal bd_cajas_disponibles = new BigDecimal(cajas_disponibles);
							BigDecimal rd_cajas_disponibles  = bd_cajas_disponibles.setScale(1, RoundingMode.HALF_UP);
							
							Double dias_inventarios_in = 0.0;
							//System.out.println("Dias Inv In "+cajas_disponibles +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios_in = cajas_disponibles / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios_in = new BigDecimal(dias_inventarios_in);
							BigDecimal rd_dias_inventarios_in  = bd_dias_inventarios_in.setScale(2, RoundingMode.HALF_UP);
							//System.out.println("cont<size = "+i);
							
							int diaM = 0;
							diaM = (int) Math.round(clc_prom_diario_m);
							
							Calendar fechaM = new GregorianCalendar();
							fechaM.setTime(fLoad);
							fechaM.add(Calendar.DATE, diaM);
							
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_cajas_mar)+"</strong></td>"; //Total por periodo de Meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_prom_diario_m)+"</strong></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_existencias_m)+"</strong></td>"; // existencia en inventarios
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_inventarios)+"</strong></td>"; // Dias de inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_pendiente_x_fact_m)+"</strong></td>"; // P?endiente por facturar
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_cajas_disponibles)+"</strong></td>"; //Cajas Disponmibles
							//html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_costo_m+"</strong></td>"; // Costo
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_inventarios_in)+"</strong></td>"; // Dia Inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+formatoDeFecha.format(fechaM.getTime())+"</strong></td>"; // Fecha
							html += "</tr>";
							
							html += "</tr>";
							//System.out.println("marcaaa: "+dto.getMarca()); 
						
						}
					}else{
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i-1).getIdMarca();
						//System.out.println("Marcas: ini - "+marca_ini+" fin - "+marca_fin);
						if(!marca_ini.equals(marca_fin)){
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getMarca()+"" +
									"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
											"</strong></td>"; //Producto
							//System.out.println("Meses Fil: "+ meses_marca);
							for (int a = 1; a <= meses_marca; meses_marca --){
								double ttl_cajas_x_mes = 0.0;
								//System.out.println("cve "+dto.getIdMarca()+"_"+meses_marca);
								String ttl = (String) ttl_marcas.get(dto.getIdMarca()+"_"+meses_marca);
								//System.out.println("Total: "+ttl);
								if(ttl != null && !ttl.equals("null")){
									ttl_cajas_x_mes = Double.parseDouble(ttl);
								}
								BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
								BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
								html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_cajas_mes)+"</strong></td>"; //Mes4
							}
							Double ttl_cajas_mar = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
							BigDecimal bd_ttl_cajas_mar = new BigDecimal(ttl_cajas_mar);
							BigDecimal rd_ttl_cajas_mar = bd_ttl_cajas_mar.setScale(1, RoundingMode.HALF_UP);
							
							Double clc_prom_diario_m = ttl_cajas_mar / perd_dias;
							BigDecimal prom_diario_m = new BigDecimal(clc_prom_diario_m);
							BigDecimal rd_prom_diario_m = prom_diario_m.setScale(1, RoundingMode.HALF_UP);
							

							Double ttl_existencias_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
							BigDecimal bd_ttl_existencias_m = new BigDecimal(ttl_existencias_m);
							BigDecimal rd_ttl_existencias_m = bd_ttl_existencias_m.setScale(1, RoundingMode.HALF_UP);
							
							Double pendiente_x_fact_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
							BigDecimal bd_pendiente_x_fact_m = new BigDecimal(pendiente_x_fact_m);
							BigDecimal rd_pendiente_x_fact_m = bd_pendiente_x_fact_m.setScale(1, RoundingMode.HALF_UP);
							
							Double costo_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_costo"));
							BigDecimal bd_costo_m = new BigDecimal(costo_m);
							BigDecimal rd_costo_m = bd_costo_m.setScale(1, RoundingMode.HALF_UP);
							
							//Semaforizacion
							System.out.println("Semaforizacion --> "+minStockI + " -- "+minStockI);
							if(Math.abs(disponible) >= Math.abs(minStockI) && Math.abs(disponible) <= Math.abs(minStockI)){
								System.out.println("Color Suave --> "+minStockI + " -- "+minStockI);
							}
							if(Math.abs(disponible) <= Math.abs(min_stock)){
								System.out.println("Color Fuerte --> "+minStockI + " -- "+minStockI);
							}
							
							Double dias_inventarios = 0.0;
							//System.out.println("Dias Inv "+ttl_existencias_m +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios = ttl_existencias_m / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios = new BigDecimal(dias_inventarios);
							BigDecimal rd_dias_inventarios  = bd_dias_inventarios.setScale(2, RoundingMode.HALF_UP);
							
							//System.out.println("Cajas: Disponibles: "+ttl_existencias_m +" - "+ pendiente_x_fact_m);
							Double cajas_disponibles = ttl_existencias_m - pendiente_x_fact_m;
							BigDecimal bd_cajas_disponibles = new BigDecimal(cajas_disponibles);
							BigDecimal rd_cajas_disponibles  = bd_cajas_disponibles.setScale(1, RoundingMode.HALF_UP);
							
							Double dias_inventarios_in = 0.0;
							//System.out.println("Dias Inv In "+cajas_disponibles +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios_in = cajas_disponibles / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios_in = new BigDecimal(dias_inventarios_in);
							BigDecimal rd_dias_inventarios_in  = bd_dias_inventarios_in.setScale(2, RoundingMode.HALF_UP);
							//System.out.println("cont > size "+i);
							
							int diaM = 0;
							diaM = (int) Math.round(clc_prom_diario_m);
							
							Calendar fechaM = new GregorianCalendar();
							fechaM.setTime(fLoad);
							fechaM.add(Calendar.DATE, diaM);
							
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_cajas_mar)+"</strong></td>"; //Total por periodo de Meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_prom_diario_m)+"</strong></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_ttl_existencias_m)+"</strong></td>"; // existencia en inventarios
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_inventarios)+"</strong></td>"; // Dias de inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_pendiente_x_fact_m)+"</strong></td>"; // P?endiente por facturar
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_cajas_disponibles)+"</strong></td>"; //Cajas Disponmibles
							//html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_costo_m+"</strong></td>"; // Costo
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_inventarios_in)+"</strong></td>"; // Dia Inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+formatoDeFecha.format(fechaM.getTime())+"</strong></td>"; // Fecha
							html += "</tr>";
							//System.out.println("marcaaa: "+dto.getMarca()); 
						
						}
					}
				}
				//**********************************************
				ver_class = "style='display:none'";
				}
				//Link en producto, el cual abrira el detalle por producto
				String nomProducto = "";
				String ruta = "detalle_producto.jsp?id_prod="+dto.getIdProducto()+"&id_mar="+dto.getIdMarca()+"&id_chart="+chart_id;
				
				//if(chart_id.equals("8")){
					nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong> +</strong></a>";
				//}else{
					//nomProducto = "";
				//}
					
				html += "<tr class=mrc_"+dto.getIdMarca()+" "+ver_class+">";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Marca
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getProducto()+nomProducto+"</td>"; //Producto
				//Genera los periodos a mostrar
				for (int x = 1; x <= meses_fil; meses_fil --){
					String prb_ttl_x_mes = (String) hm_meses.get(dto.getKey()+"_"+meses_fil);
					Double ttl_cajas_x_mes = 0.0;
					if(prb_ttl_x_mes != null){
						ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
					}
					//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
					BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
					BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
					String cmp_ttl_cjs_mes = rd_ttl_cajas_mes.toString();
					//System.out.println("Ttl Cjs "+cmp_ttl_cjs_mes);
					if(ttl_cajas_x_mes != 0.0){
						html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(rd_ttl_cajas_mes)+"</td>"; //Mes4
					}else{
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td> "; //Mes4
					}
				}
				
				
				String colorRojo =  "#FF0000";
				String colorNaranja = "#FE9A2E";
				String colorNaranjaSuave = "#F5BCA9";
				String colorVerdeClaro  = "#A9F5A9";
				String colorNaranjaClaro = "#F5D0A9";
				
				String color = "";
				Double numC = 0.0;
				
				if(!rd_ttl_cajas.toString().equals("0.0")){
					color= "";
					numC = Double.parseDouble(rd_ttl_cajas.toString());
					if(numC < 0){
						color= "red";
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color = "+color+">"+frmt.format(rd_ttl_cajas)+"</td>"; //Total por periodo de Meses
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Total por periodo de Meses
				}
				
				if(!rd_prom_diario.toString().equals("0.0")){
					color= "";
					numC = Double.parseDouble(rd_prom_diario.toString());
					if(numC < 0){
						color= "red";
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color = "+color+">"+frmt.format(rd_prom_diario)+"</td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
				}
				
				if(!rd_existecia.toString().equals("0.0")){
					color= "";
					numC = Double.parseDouble(rd_existecia.toString());
					if(numC < 0){
						color= "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color = "+color+">"+frmt.format(rd_existecia)+"</td>"; // existencia en inventarios
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // existencia en inventarios
				}
				
				//System.out.println("Rd--> "+dias_invent);
				
				if(dias_invent != 0.00 ){
					color= "";
					numC = Double.parseDouble(rd_diasInvent.toString());
					if(numC < 0){
						color= "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color = "+color+">"+frmt.format(rd_diasInvent)+"</td>"; // Dias de inventario
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Dias de inventario
				}
				
				if(!rd_pendiente_x_fact.toString().equals("0.0")){
					color= "";
					numC = Double.parseDouble(rd_pendiente_x_fact.toString());
					if(numC < 0){
						color= "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color = "+color+">"+frmt.format(rd_pendiente_x_fact)+"</td>"; // P?endiente por facturar
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // P?endiente por facturar
				}
				
				String amarilloSuave = "#FFF0C9";
				String amarilloFuerte = "#FFDC99";
				//Semaforizacion
				String semaf = "";
				//System.out.println("Semaforizacion --> "+disponible + " -- " + min_stock + " -- "+minStockI + " -- "+minStockIPerc);
				if((disponible * 28) >=(minStockI * 1.0001) && (disponible * 28) <= (minStockI * 1.15)){
					System.out.println("Color Suave --> "+disponible + " -- " + min_stock + " -- "+minStockI + " -- "+minStockIPerc);
					semaf = amarilloSuave;
				}
				if((disponible * 28) < minStockI){
					System.out.println("Color Fuerte --> "+disponible + " -- " + min_stock + " -- "+minStockI+ " -- "+minStockIPerc);
					semaf = amarilloFuerte;
				}
				
				if(!rd_disponible.toString().equals("0.0")){
					color= "";
					numC = Double.parseDouble(rd_disponible.toString());
					if(numC < 0){
						color= "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'  bgcolor='"+semaf+"'><font color = "+color+">"+frmt.format(rd_disponible)+"</td>"; //Cajas Disponmibles
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Cajas Disponmibles
				}
				
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_costo_final+"</td>"; // Costo
				//Semaforizacion
				String s_dia = "";
				//System.out.println("Semaforizacion --> "+disponible + " -- " + min_stock + " -- "+minStockI + " -- "+minStockIPerc);
				if(dia_inventario >= (minStockI * 1.0001) && dia_inventario <= (minStockI * 1.15)){
					System.out.println("Color Suave --> "+disponible + " -- " + min_stock + " -- "+minStockI + " -- "+minStockIPerc);
					s_dia = amarilloSuave;
				}
				if(dia_inventario < minStockI){
					System.out.println("Color Fuerte --> "+disponible + " -- " + min_stock + " -- "+minStockI+ " -- "+minStockIPerc);
					s_dia = amarilloFuerte;
				}
				
				int dia_periodo = 0;
				if(dia_inventario != 0.00 ){
					color= "";
					numC = Double.parseDouble(rd_dia_inventario.toString());
					dia_periodo = Integer.parseInt(i_dia_inventario.toString());
					if(numC < 0){
						color= "red";
					}
					cal.setTime(fLoad);
					cal.add(Calendar.DATE, (dia_periodo * 28));
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor='"+s_dia+"'><font color = "+color+">"+frmt.format(rd_dia_inventario)+"</td>"; // Dia Inventario
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(cal.getTime())+"</td>"; // Fecha
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Dia Inventario
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Fecha ----><------
				}
				
				
				html += "</tr>";
				
				//Generar total para cada marca
				/*String marca_ini = "";
				String marca_fin = "";
				String marca = "";
				if(i == 0  && i+1 < row.size()){
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i+1).getIdMarca();
					//System.out.println("Marcas: ini - "+marca_ini+" fin - "+marca_fin);
					if(!marca_ini.equals(marca_fin)){
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						System.out.println("Meses Fil: "+ meses_marca);
						for (int a = 1; a <= meses_marca; meses_marca --){
							double ttl_cajas_x_mes = 0.0;
							//System.out.println("cve "+dto.getIdMarca()+"_"+meses_marca);
							String ttl = (String) ttl_marcas.get(dto.getIdMarca()+"_"+meses_marca);
							//System.out.println("Total: "+ttl);
							if(ttl != null && !ttl.equals("null")){
								ttl_cajas_x_mes = Double.parseDouble(ttl);
							}
							BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
							BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_cajas_mes+"</strong></td>"; //Mes4
						}
						Double ttl_cajas_mar = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						BigDecimal bd_ttl_cajas_mar = new BigDecimal(ttl_cajas_mar);
						BigDecimal rd_ttl_cajas_mar = bd_ttl_cajas_mar.setScale(1, RoundingMode.HALF_UP);
						
						Double clc_prom_diario_m = ttl_cajas_mar / perd_dias;
						BigDecimal prom_diario_m = new BigDecimal(clc_prom_diario_m);
						BigDecimal rd_prom_diario_m = prom_diario_m.setScale(1, RoundingMode.HALF_UP);
						

						Double ttl_existencias_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						BigDecimal bd_ttl_existencias_m = new BigDecimal(ttl_existencias_m);
						BigDecimal rd_ttl_existencias_m = bd_ttl_existencias_m.setScale(1, RoundingMode.HALF_UP);
						
						Double pendiente_x_fact_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						BigDecimal bd_pendiente_x_fact_m = new BigDecimal(pendiente_x_fact_m);
						BigDecimal rd_pendiente_x_fact_m = bd_pendiente_x_fact_m.setScale(1, RoundingMode.HALF_UP);
						
						Double costo_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_costo"));
						BigDecimal bd_costo_m = new BigDecimal(costo_m);
						BigDecimal rd_costo_m = bd_costo_m.setScale(1, RoundingMode.HALF_UP);
						
						Double dias_inventarios = 0.0;
						if(clc_prom_diario_m != 0.0){
							dias_inventarios = ttl_existencias_m / clc_prom_diario_m;
						}
						BigDecimal bd_dias_inventarios = new BigDecimal(dias_inventarios);
						BigDecimal rd_dias_inventarios  = bd_dias_inventarios.setScale(1, RoundingMode.HALF_UP);
						
						Double cajas_disponibles = ttl_existencias_m - pendiente_x_fact_m;
						BigDecimal bd_cajas_disponibles = new BigDecimal(cajas_disponibles);
						BigDecimal rd_cajas_disponibles  = bd_cajas_disponibles.setScale(1, RoundingMode.HALF_UP);
						
						Double dias_inventarios_in = 0.0;
						if(clc_prom_diario_m != 0.0){
							dias_inventarios_in = cajas_disponibles / clc_prom_diario_m;
						}
						BigDecimal bd_dias_inventarios_in = new BigDecimal(dias_inventarios_in);
						BigDecimal rd_dias_inventarios_in  = bd_dias_inventarios_in.setScale(1, RoundingMode.HALF_UP);
						System.out.println("cont 1 = "+i);
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_cajas_mar+"</strong></td>"; //Total por periodo de Meses
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_prom_diario_m+"</trong></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_existencias_m+"</trong></td>"; // existencia en inventarios
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_inventarios+"</trong></td>"; // Dias de inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_pendiente_x_fact_m+"</trong></td>"; // P?endiente por facturar
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cajas_disponibles+"</trong></td>"; //Cajas Disponmibles
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_costo_m+"</trong></td>"; // Costo
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_inventarios_in+"</trong></td>"; // Dia Inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Fecha
						html += "</tr>";
						System.out.println("marcaaa:--- "+dto.getMarca()); 
					}
				}else{
					
					if(i+1 < row.size()){
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i+1).getIdMarca();
						//System.out.println("Marcas: ini - "+marca_ini+" fin - "+marca_fin);
						//System.out.println("Meses Fil: "+ meses_marca);
						if(!marca_ini.equals(marca_fin)){
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getMarca()+"" +
									"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
											"</strong></td>"; //Producto
							for (int a = 1; a <= meses_marca; meses_marca --){
								double ttl_cajas_x_mes = 0.0;
								//System.out.println("cve "+dto.getIdMarca()+"_"+meses_marca);
								String ttl = (String) ttl_marcas.get(dto.getIdMarca()+"_"+meses_marca);
								//System.out.println("Total: "+ttl);
								if(ttl != null && !ttl.equals("null")){
									ttl_cajas_x_mes = Double.parseDouble(ttl);
								}
								//System.out.println("Cve: "+dto.getIdMarca()+"_"+meses_marca+" ttl_mes: "+ttl_cajas_x_mes);
								BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
								BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
								html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_cajas_mes+"</strong></td>"; //Mes4
							}
							String ttl_mar_prb = (String) ttl_marcas.get(dto.getIdMarca()+"_ttl_mes");
							Double ttl_cajas_mar = 0.0;
							if(ttl_mar_prb != null){
								ttl_cajas_mar = Double.parseDouble(ttl_mar_prb);
							}
							BigDecimal bd_ttl_cajas_mar = new BigDecimal(ttl_cajas_mar);
							BigDecimal rd_ttl_cajas_mar = bd_ttl_cajas_mar.setScale(1, RoundingMode.HALF_UP);
							
							Double clc_prom_diario_m = ttl_cajas_mar / perd_dias;
							BigDecimal prom_diario_m = new BigDecimal(clc_prom_diario_m);
							BigDecimal rd_prom_diario_m = prom_diario_m.setScale(1, RoundingMode.HALF_UP);
							

							Double ttl_existencias_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
							BigDecimal bd_ttl_existencias_m = new BigDecimal(ttl_existencias_m);
							BigDecimal rd_ttl_existencias_m = bd_ttl_existencias_m.setScale(1, RoundingMode.HALF_UP);
							
							Double pendiente_x_fact_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
							BigDecimal bd_pendiente_x_fact_m = new BigDecimal(pendiente_x_fact_m);
							BigDecimal rd_pendiente_x_fact_m = bd_pendiente_x_fact_m.setScale(1, RoundingMode.HALF_UP);
							
							Double costo_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_costo"));
							BigDecimal bd_costo_m = new BigDecimal(costo_m);
							BigDecimal rd_costo_m = bd_costo_m.setScale(1, RoundingMode.HALF_UP);
							
							Double dias_inventarios = 0.0;
							System.out.println("Dias Inv "+ttl_existencias_m +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios = ttl_existencias_m / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios = new BigDecimal(dias_inventarios);
							BigDecimal rd_dias_inventarios  = bd_dias_inventarios.setScale(1, RoundingMode.HALF_UP);
							
							System.out.println("Cajas: Disponibles: "+ttl_existencias_m +" - "+ pendiente_x_fact_m);
							Double cajas_disponibles = ttl_existencias_m - pendiente_x_fact_m;
							BigDecimal bd_cajas_disponibles = new BigDecimal(cajas_disponibles);
							BigDecimal rd_cajas_disponibles  = bd_cajas_disponibles.setScale(1, RoundingMode.HALF_UP);
							
							Double dias_inventarios_in = 0.0;
							System.out.println("Dias Inv In "+cajas_disponibles +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios_in = cajas_disponibles / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios_in = new BigDecimal(dias_inventarios_in);
							BigDecimal rd_dias_inventarios_in  = bd_dias_inventarios_in.setScale(1, RoundingMode.HALF_UP);
							System.out.println("cont<size = "+i);
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_cajas_mar+"</strong></td>"; //Total por periodo de Meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_prom_diario_m+"</strong></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_existencias_m+"</strong></td>"; // existencia en inventarios
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_inventarios+"</strong></td>"; // Dias de inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_pendiente_x_fact_m+"</strong></td>"; // P?endiente por facturar
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cajas_disponibles+"</strong></td>"; //Cajas Disponmibles
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_costo_m+"</strong></td>"; // Costo
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_inventarios_in+"</strong></td>"; // Dia Inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Fecha
							html += "</tr>";
							
							html += "</tr>";
							System.out.println("marcaaa: "+dto.getMarca()); 
						
						}
					}else{
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i).getIdMarca();
						//System.out.println("Marcas: ini - "+marca_ini+" fin - "+marca_fin);
						//if(!marca_ini.equals(marca_fin)){
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getMarca()+"" +
									"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
											"</strong></td>"; //Producto
							//System.out.println("Meses Fil: "+ meses_marca);
							for (int a = 1; a <= meses_marca; meses_marca --){
								double ttl_cajas_x_mes = 0.0;
								//System.out.println("cve "+dto.getIdMarca()+"_"+meses_marca);
								String ttl = (String) ttl_marcas.get(dto.getIdMarca()+"_"+meses_marca);
								//System.out.println("Total: "+ttl);
								if(ttl != null && !ttl.equals("null")){
									ttl_cajas_x_mes = Double.parseDouble(ttl);
								}
								BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
								BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
								html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_cajas_mes+"</strong></td>"; //Mes4
							}
							Double ttl_cajas_mar = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
							BigDecimal bd_ttl_cajas_mar = new BigDecimal(ttl_cajas_mar);
							BigDecimal rd_ttl_cajas_mar = bd_ttl_cajas_mar.setScale(1, RoundingMode.HALF_UP);
							
							Double clc_prom_diario_m = ttl_cajas_mar / perd_dias;
							BigDecimal prom_diario_m = new BigDecimal(clc_prom_diario_m);
							BigDecimal rd_prom_diario_m = prom_diario_m.setScale(1, RoundingMode.HALF_UP);
							

							Double ttl_existencias_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
							BigDecimal bd_ttl_existencias_m = new BigDecimal(ttl_existencias_m);
							BigDecimal rd_ttl_existencias_m = bd_ttl_existencias_m.setScale(1, RoundingMode.HALF_UP);
							
							Double pendiente_x_fact_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
							BigDecimal bd_pendiente_x_fact_m = new BigDecimal(pendiente_x_fact_m);
							BigDecimal rd_pendiente_x_fact_m = bd_pendiente_x_fact_m.setScale(1, RoundingMode.HALF_UP);
							
							Double costo_m = Double.parseDouble((String) ttl_marcas.get(dto.getIdMarca()+"_costo"));
							BigDecimal bd_costo_m = new BigDecimal(costo_m);
							BigDecimal rd_costo_m = bd_costo_m.setScale(1, RoundingMode.HALF_UP);
							
							
							Double dias_inventarios = 0.0;
							System.out.println("Dias Inv "+ttl_existencias_m +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios = ttl_existencias_m / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios = new BigDecimal(dias_inventarios);
							BigDecimal rd_dias_inventarios  = bd_dias_inventarios.setScale(1, RoundingMode.HALF_UP);
							
							System.out.println("Cajas: Disponibles: "+ttl_existencias_m +" - "+ pendiente_x_fact_m);
							Double cajas_disponibles = ttl_existencias_m - pendiente_x_fact_m;
							BigDecimal bd_cajas_disponibles = new BigDecimal(cajas_disponibles);
							BigDecimal rd_cajas_disponibles  = bd_cajas_disponibles.setScale(1, RoundingMode.HALF_UP);
							
							Double dias_inventarios_in = 0.0;
							System.out.println("Dias Inv In "+cajas_disponibles +" / "+ clc_prom_diario_m);
							if(clc_prom_diario_m != 0.0){
								dias_inventarios_in = cajas_disponibles / clc_prom_diario_m;
							}
							BigDecimal bd_dias_inventarios_in = new BigDecimal(dias_inventarios_in);
							BigDecimal rd_dias_inventarios_in  = bd_dias_inventarios_in.setScale(1, RoundingMode.HALF_UP);
							System.out.println("cont > size "+i);
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_cajas_mar+"</strong></td>"; //Total por periodo de Meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_prom_diario_m+"</strong></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ttl_existencias_m+"</strong></td>"; // existencia en inventarios
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_inventarios+"</strong></td>"; // Dias de inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_pendiente_x_fact_m+"</strong></td>"; // P?endiente por facturar
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cajas_disponibles+"</strong></td>"; //Cajas Disponmibles
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_costo_m+"</strong></td>"; // Costo
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_inventarios_in+"</strong></td>"; // Dia Inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Fecha
							html += "</tr>";
							System.out.println("marcaaa: "+dto.getMarca()); 
						
						//}
					}
				}*/
			}
			
			html += "</tbody>";
			html += "</table> "+ toltip_meses;
			
			System.out.println("Tabla... ");
			return html;
		}
	//Previcion Compras _ Prueba
		public String getTableAnalisisInvent(
				String cust_id,
				String id_user,
				String id_modulo,
				String id_dashboard,
				String chart_id,
				String mes,
				String ver,
				String act) throws UnsupportedEncodingException, ParseException {
			//Configuracion del portlet o grid
			HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
			//Filtros para el portlet o grid
			String cmp_filtro = (String) hm.get("cmp_id");
			String html = "";
			boolean org_param = false;
			int meses_enc = 4;
			int meses_col = 4;
			int meses_fil = 4;
			int meses_ttl = 4;
			int meses_marca = 4;
			
			Double in_diasP1 = 0.0;
			Double in_diasP2 = 0.0;
			Double in_diasP3 = 0.0;
			Double in_cajasP1 = 0.0;
			Double in_cajasP2 = 0.0;
			Double in_cajasP3 = 0.0;
			
			if(mes == null || mes == "" || mes.equals("null")){
			}else{
				meses_enc = Integer.parseInt(mes);
				meses_col = Integer.parseInt(mes);
				meses_fil = Integer.parseInt(mes);
				meses_ttl = Integer.parseInt(mes);
			}
			html += "<table id=tblDatos class=tablesorter border=1 >";
			html += "<thead>";
			
			//Encabezado
			html += "<tr><td colspan=2></td>";
			html += "<td >Prom/D&iacute;a</td>";
			html += "<td >Existencias</td>";
			html += "<td >D&iacute;as Inventario</td>";
			html += "<td >X Facturar Normal</td>";
			html += "<td >Disponible</td>";
			html += "<td >Costo</td>";
			html += "<td colspan=2>D&iacute;as Inventario</td>";
			html += "<td >Tiempo L&iacute;mite Para Mantener Stock</td>";
			html += "<td >Tiempo L&iacute;mite Para Levantar Pedido</td>";
			html += "</tr>";
			
			html += "<tr>";
			html += "<th></th>";
			html += "<th></th>";
			html += "<th>CAJAS</th>";
			html += "<th>CAJAS</th>";
			html += "<th>DIAS</th>";
			html += "<th>CAJAS</th>";
			html += "<th>CAJAS</th>";
			html += "<th>$</th>";
			html += "<th>DIAS</th>";
			html += "<th>FECHA</th>";
			html += "<th>DIAS</th>";
			html += "<th>DIAS</th>";
			html += "</tr>";
			html += "</thead>";
			html +="<tbody >";

			
			Calendar m1_Ini = new GregorianCalendar();
			Calendar m1_Fin = new GregorianCalendar();
			Calendar m2_Ini = new GregorianCalendar();
			Calendar m2_Fin = new GregorianCalendar();
			Calendar m3_Ini = new GregorianCalendar();
			Calendar m3_Fin = new GregorianCalendar();
			Calendar m4_Ini = new GregorianCalendar();
			Calendar m4_Fin = new GregorianCalendar();
			Calendar hoy = new GregorianCalendar();
			 //Formato para fecha
		    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
			// Mes 1
		    String fechaL = invent.obtieneFechaCargaDatos();
		    System.out.println("Fecha De Carga--->"+fechaL);
		    SimpleDateFormat formatoF1 = new SimpleDateFormat("dd/MM/yyyy");
		    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
			
			java.util.Date fLoad = null;
						    
		    //Tooltips para el rango de los meses 
		    String toltip_meses = "";
		    int rango_mes = 28;
		    		    
			//List<InvDTO> row = invent.getDataAnalisisVentas(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id);
			List<InvPrevComp> row = invent.getDataPrevComprasProdGnrl(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, act);
			HashMap hm_meses = invent.getDataMesesPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, act);
			HashMap ttl_marcas = invent.getDataMesesTtlPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, act);
			for(int i = 0; i < row.size(); i ++){
				//System.out.println(row.get(i));
				//Crear total para agrupamiento por marca
				if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
					fLoad =  formatoF2.parse(fechaL);
					hoy.setTime(fLoad);
				}else{
					hoy.add(Calendar.DATE , -1);
				}
				
				//System.out.println("Fecha De Carga--->"+formatoF1.format(fLoad.getTime())+" -- "+formatoF1.format(hoy.getTime()));
				String marca_ttl ="";
				
				InvPrevComp dto = row.get(i);
				if(mes == null || mes == "" || mes.equals("null")){
					meses_fil = 4;
					meses_ttl = 4;
					meses_marca = 4; 
				}else{
					meses_fil = Integer.parseInt(mes);
					meses_ttl = Integer.parseInt(mes);
					meses_marca = Integer.parseInt(mes);
				}
				Calendar cal = new GregorianCalendar();
				DecimalFormat formatter = new DecimalFormat("###,###,##0.000");
				String mes_prb = (String) hm_meses.get(dto.getIdProd()+"_ttl");
				
				//Fecha para determinar si tiene pedido
				Date fechaP1 = (Date) dto.getFechaP1();
				Date fechaP2 = (Date) dto.getFechaP2();
				Date fechaP3 = (Date) dto.getFechaP3();
				
				Double ttl_cajas = 0.0;
				//System.out.println("Null "+mes_prb);
				if(mes_prb != null){
					ttl_cajas = Double.parseDouble(mes_prb);
				}
				int perd_dias = meses_fil * 28;
						
				BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
				BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
				
				
				Double clc_prom_diario = ttl_cajas / perd_dias;
				BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
				BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
				Double existencia = Double.parseDouble(dto.getTtlExist());
				BigDecimal bd_existencia = new BigDecimal(existencia);
				BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
				
				Double dias_invent = (existencia / clc_prom_diario);
				Double pendiente_x_fact = Double.parseDouble(dto.getPndXFact());
				BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
				BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
				Double disponible = existencia - pendiente_x_fact;
				BigDecimal bd_disponible = new BigDecimal(disponible);
				BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
				
				Double dia_inventario =  (disponible / clc_prom_diario);
				System.out.println("F DiaInv "+ disponible +" / "+clc_prom_diario + "= "+dia_inventario);
				Double costo = Double.parseDouble(dto.getCosto());
				Double costo_final = costo * disponible; //Se modifica costo multiplicandolo por disponible
				int dia_invent_fin = (int) (Math.round(dia_inventario));
				
				BigDecimal bd_costo_final = new BigDecimal(costo_final);
				BigDecimal rd_costo_final = bd_costo_final.setScale(0, RoundingMode.HALF_UP);
				
				if(dia_inventario.isInfinite() || dia_inventario.isNaN()){
					dia_inventario = 0.0;
				}
				BigDecimal bd_dia = new BigDecimal(dia_inventario);
				BigDecimal rd_dia = bd_dia.setScale(0,RoundingMode.HALF_DOWN);
				
				//System.out.println(formatoF1.format(hoy.getTime())+" -- "+(dia_inventario));
				hoy.add(Calendar.DATE,  rd_dia.intValue());
				//System.out.println(formatoF1.format(hoy.getTime())+" -- "+rd_dia.intValue()+" -- "+formatoF1.format(cal.getTime()));
				
				Double tmpo_mtnr_stock = 0.0;
				Double min_stock = Double.parseDouble(dto.getMinStock());
				Double tiempo_prov = Double.parseDouble(dto.getTiempoProv());
				tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
				Double x_tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
				//System.out.println("----->"+tmpo_mtnr_stock);
				double diasP1 = Double.parseDouble(dto.getDiasP1());
				double diasP2 = Double.parseDouble(dto.getDiasP2());
				double diasP3 = Double.parseDouble(dto.getDiasP3());
				double cajasP1 = Double.parseDouble(dto.getCajasP1());
				double cajasP2 = Double.parseDouble(dto.getCajasP2());
				double cajasP3 = Double.parseDouble(dto.getCajasP3());
				
				Double ddiP1_prod = (cajasP1 + in_cajasP1) / clc_prom_diario;
				Double ddiP2_prod = (cajasP2 + in_cajasP2) / clc_prom_diario;
				Double ddiP3_prod = (cajasP3 + in_cajasP3) / clc_prom_diario;
				
				//System.out.println("Dias Inventario: " + dia_inventario + "Minimo Stock: "+min_stock);
				Double beOculto_prod = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + Math.abs(dia_inventario)) - min_stock));// * cajasP1) / cajasP1;
				System.out.println("Dias Inventario: " + dia_inventario + "Minimo Stock: "+min_stock+"Tiempo del provedro: "+tiempo_prov+" Oculto: "+beOculto_prod+" DDIPEdidos "+(ddiP1_prod+ddiP2_prod+ddiP3_prod));
				Double tmpo_lvtr_ped = beOculto_prod - tiempo_prov;
				Double x_tmpo_lvtr_ped = beOculto_prod - tiempo_prov;
				//(System.out.println("-------->"+tmpo_lvtr_ped);
				if(tmpo_lvtr_ped.isInfinite() || tmpo_lvtr_ped.isNaN()){
					tmpo_lvtr_ped = 0.0;
				}
				if(tmpo_mtnr_stock.isInfinite() || tmpo_mtnr_stock.isNaN()){
					tmpo_mtnr_stock = 0.0;
				}
				//Double tmpo_lvtr_ped = (ddiP1 + ddiP2 + ddiP3 + dia_inventario) - min_stock;
				//System.out.println("MantenerStock: "+tmpo_mtnr_stock);
				BigDecimal _tmpo_mtnr_stock = new BigDecimal(tmpo_mtnr_stock);
				BigDecimal rd_tmpo_mtnr_stock = _tmpo_mtnr_stock.setScale(1, RoundingMode.HALF_UP);
				BigDecimal _tmpo_lvtr_ped = new BigDecimal(tmpo_lvtr_ped);
				BigDecimal rd_tmpo_lvtr_ped = _tmpo_lvtr_ped.setScale(1, RoundingMode.HALF_UP);
				
				//Double tmpo_lvtr_ped = (diasP1 + diasP2 + diasP3 + dia_inventario) - min_stock;
				DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
				simbolo.setDecimalSeparator('.');
				simbolo.setGroupingSeparator(',');
				DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
				String color  = "";
				Double numC = 0.0;
				//DecimalFormat frmt = new DecimalFormat("###,###.##");
				
				String ver_class = "";
				if(ver != null && !ver.equals("null") && ver.equals("marca")){
				/*************************************************************************/
				//Fila con la Marca y los totales por marca
				String marca_ini = "";
				String marca_fin = "";
				String marca = "";
				if(i == 0  ){
					//marca_ini = dto.getIdMarca();
					//marca_fin = row.get(i+1).getIdMarca();
					//if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock = 0.0;
						 tiempo_limit_mantener_stock = (dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						if(tiempo_limit_mantener_stock.isInfinite() || tiempo_limit_mantener_stock.isNaN()){
							tiempo_limit_mantener_stock = 0.0;
						}
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
						Double tiempo_limit_levantar_ped = 0.0;
						tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						if(tiempo_limit_levantar_ped.isInfinite() || tiempo_limit_levantar_ped.isNaN()){
							tiempo_limit_levantar_ped = 0.0;
						}
							
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
							
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						Double dia_inventario2 = 0.0;
						if(clc_prom_diario_mrc != 0 && !clc_prom_diario_mrc.isInfinite() && !clc_prom_diario_mrc.isNaN()){
							dia_inventario2 = disponible_mrc / clc_prom_diario_mrc;
						}
						BigDecimal _diaInventario = new BigDecimal(dia_inventario2);
						BigDecimal rd_diaInventario = _diaInventario.setScale(1,RoundingMode.HALF_UP);
						
						int diaM = 0;
						diaM = (int) Math.round(dia_inventario2);
						System.out.println("DiaM ---> "+diaM);
						Calendar fecha_mrc = new GregorianCalendar();
						fecha_mrc.setTime(fLoad);
						fecha_mrc.add(Calendar.DATE, diaM);
						
						Double costo_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_costo"));
						BigDecimal bd_costo_mrc = new BigDecimal(costo_mrc);
						BigDecimal rd_costo_mrc = bd_costo_mrc.setScale(0,RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_clc_prom_diario_mrc)+"</strong></td>"; // Promedio
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_existencias_ttl_mss)+"</strong></td>"; // existencia en inventarios
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_invent_mrc)+"</strong></td>"; // Dias de inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_pend_x_fac_nrml_mrc)+"</strong></td>"; // P?endiente por facturar
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_disponible_mrc)+"</strong></td>"; //Cajas Disponmibles
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_costo_mrc)+"</strong></td>"; // Costo
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_diaInventario)+"</strong></td>"; // Dia Inventario (dias) ****
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+formatoDeFecha.format(fecha_mrc.getTime())+"</strong></td>"; // Fecha
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_tiempo_limit_mantener_stock)+"</strong></td>"; // Tiempo Limite Para Mantener Stock
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_tiempo_limit_levantar_ped)+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
						html += "</tr>";						
					//}
				}else{
					
					if(i+1 < row.size()){
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i-1).getIdMarca();
						
						if(!marca_ini.equals(marca_fin)){
							Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
							Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
							
							Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
							Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
							Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
							
							Double dias_invent_mrc = 0.0;
							dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
							if(dias_invent_mrc.isInfinite() || dias_invent_mrc.isNaN()){
								dias_invent_mrc = 0.0;
							}
							Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
							Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
							Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
							Double tiempo_limit_mantener_stock = 0.0;
							 tiempo_limit_mantener_stock = (dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
							if(tiempo_limit_mantener_stock.isInfinite() || tiempo_limit_mantener_stock.isNaN()){
								tiempo_limit_mantener_stock = 0.0;
							}
							
							///Tiempo Limite Para Levantar Pedido
							Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
							Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
							Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
							Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
							Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
							Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
							
							Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
							Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
							Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
							
							Double beOculto = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
							Double tiempo_limit_levantar_ped = 0.0;
							tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
							if(tiempo_limit_levantar_ped.isInfinite() || tiempo_limit_levantar_ped.isNaN()){
								tiempo_limit_levantar_ped = 0.0;
							}
							
							//Continuidad Existencias al Minimi P1
							Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
							Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
							Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
							if(cont_exist_min_p1.isNaN()){
								cont_exist_min_p1 = 0.0;
							}
							if(cont_exist_min_p2.isNaN()){
								cont_exist_min_p2 = 0.0;
							}
							if(cont_exist_min_p3.isNaN()){
								cont_exist_min_p3 = 0.0;
							}
							
							BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
							BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
							BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
							BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
							BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
							BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
							BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
							BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
							
							Double dia_inventario2 = 0.0;
							if(clc_prom_diario_mrc != 0 && !clc_prom_diario_mrc.isInfinite() && !clc_prom_diario_mrc.isNaN()){
								dia_inventario2 = disponible_mrc / clc_prom_diario_mrc;
							}
							BigDecimal _diaInventario = new BigDecimal(dia_inventario2);
							BigDecimal rd_diaInventario = _diaInventario.setScale(1,RoundingMode.HALF_UP);
							
							int diaM = 0;
							diaM = (int) Math.round(dia_inventario2);
							System.out.println("DiaM ---> "+diaM);
							Calendar fecha_mrc = new GregorianCalendar();
							fecha_mrc.setTime(fLoad);
							fecha_mrc.add(Calendar.DATE, diaM);
							
							Double costo_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_costo"));
							BigDecimal bd_costo_mrc = new BigDecimal(costo_mrc);
							BigDecimal rd_costo_mrc = bd_costo_mrc.setScale(0,RoundingMode.HALF_UP);
							
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
									"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
											"</strong></td>"; //Producto
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_clc_prom_diario_mrc)+"</strong></td>"; // Promedio
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_existencias_ttl_mss)+"</strong></td>"; // existencia en inventarios
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_invent_mrc)+"</strong></td>"; // Dias de inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_pend_x_fac_nrml_mrc)+"</strong></td>"; // P?endiente por facturar
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_disponible_mrc)+"</strong></td>"; //Cajas Disponmibles
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_costo_mrc)+"</strong></td>"; // Costo
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_diaInventario)+"</strong></td>"; // Dia Inventario (dias) ****
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+formatoDeFecha.format(fecha_mrc.getTime())+"</strong></td>"; // Fecha
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_tiempo_limit_mantener_stock)+"</strong></td>"; // Tiempo Limite Para Mantener Stock
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_tiempo_limit_levantar_ped)+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
							html += "</tr>";
							
						}
					}else{
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i-1).getIdMarca();
						
						if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double dias_invent_1_mrc = existencias_ttl_mss / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock = 0.0;
						 tiempo_limit_mantener_stock = (dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						if(tiempo_limit_mantener_stock.isInfinite() || tiempo_limit_mantener_stock.isNaN()){
							tiempo_limit_mantener_stock = 0.0;
						}
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
						Double tiempo_limit_levantar_ped = 0.0;
						tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						if(tiempo_limit_levantar_ped.isInfinite() || tiempo_limit_levantar_ped.isNaN()){
							tiempo_limit_levantar_ped = 0.0;
						}
						
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_1_mrc = new BigDecimal(dias_invent_1_mrc);
						BigDecimal rd_dias_invent_1_mrc = _dias_invent_1_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						Double dia_inventario2 = 0.0;
						if(clc_prom_diario_mrc != 0 && !clc_prom_diario_mrc.isInfinite() && !clc_prom_diario_mrc.isNaN()){
							dia_inventario2 = disponible_mrc / clc_prom_diario_mrc;
						}
						BigDecimal _diaInventario = new BigDecimal(dia_inventario2);
						BigDecimal rd_diaInventario = _diaInventario.setScale(1,RoundingMode.HALF_UP);
						
						int diaM = 0;
						diaM = (int) Math.round(dia_inventario2);
						System.out.println("DiaM ---> "+diaM);
						Calendar fecha_mrc = new GregorianCalendar();
						fecha_mrc.setTime(fLoad);
						fecha_mrc.add(Calendar.DATE, diaM);
						
						Double costo_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_costo"));
						BigDecimal bd_costo_mrc = new BigDecimal(costo_mrc);
						BigDecimal rd_costo_mrc = bd_costo_mrc.setScale(0,RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_clc_prom_diario_mrc)+"</strong></td>"; // Promedio
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_existencias_ttl_mss)+"</strong></td>"; // existencia en inventarios
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_dias_invent_mrc)+"</strong></td>"; // Dias de inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_pend_x_fac_nrml_mrc)+"</strong></td>"; // P?endiente por facturar
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_disponible_mrc)+"</strong></td>"; //Cajas Disponmibles
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_costo_mrc)+"</strong></td>"; // Costo
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_diaInventario)+"</strong></td>"; // Dia Inventario (dias) ****
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+formatoDeFecha.format(fecha_mrc.getTime())+"</strong></td>"; // Fecha
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_tiempo_limit_mantener_stock)+"</strong></td>"; // Tiempo Limite Para Mantener Stock
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+frmt.format(rd_tiempo_limit_levantar_ped)+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
						html += "</tr>";
						}
					}
				} 
				/**********************************************/
				ver_class = "style='display:none'";
				}
				
				//Genera link para abrir detalle del producto
				String nomProducto = "";
				String ruta = "detalle_producto.jsp?id_prod="+dto.getIdProd()+"&id_mar="+dto.getIdMarca()+"&id_chart="+chart_id;
				nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong> +</strong></a>";
				
				html += "<tr class=mrc_"+dto.getIdMarca()+" "+ver_class+"> ";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Marca
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProd()+nomProducto+"</td>"; //Producto
				//Genera los periodos a mostrar
				for (int x = 1; x <= meses_fil; meses_fil --){
					String prb_ttl_x_mes = (String) hm_meses.get(dto.getIdProd()+"_"+meses_fil);
					Double ttl_cajas_x_mes = 0.0;
					if(prb_ttl_x_mes != null){
						ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
					}
					//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
					BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
					BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
					//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_ttl_cajas_mes+"</td>"; //Mes4
				}

				
				//Semaforizacion
				String s_tmpMntnrStock = "";
				String s_tmpLvntrPed = "";
				String s_contExistP1 = "";
				String s_contExistP2 = "";
				String s_contExistP3 = "";
				String colorRojo =  "#FF0000";
				String colorNaranja = "#FE9A2E";
				String colorNaranjaSuave = "#F5BCA9";
				String colorVerdeClaro  = "#A9F5A9";
				String colorNaranjaClaro = "#F5D0A9";
				
				String amarilloSuave = "#FFF0C9";
				String amarilloFuerte = "#FFDC99";
				
				String rojoFuerte = "#FF6161";
				String rojoMedio  = "#FFB8B7";
				String rojoClaro = "#FFDAC8";
				String verdeClaro = "#F0FFC5";
				
				//System.out.println("Promedio --> "+clc_prom_diario);
				if(clc_prom_diario != 0){
					color = "";
					numC = Double.parseDouble(rd_prom_diario.toString());
					if(numC < 0.0){
						color = "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='"+color+"'>"+frmt.format(rd_prom_diario)+"</font></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
				}
				
				if(existencia != 0){
					color = "";
					numC = Double.parseDouble(rd_existecia.toString());
					if(numC < 0.0){
						color = "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='"+color+"'>"+frmt.format(rd_existecia)+"</font></td>"; // existencia en inventarios
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // --existencia en inventarios
				}
				
				//System.out.println("--------> "+dias_invent);
				if(dias_invent.isNaN() || dias_invent.isInfinite()){
					dias_invent = 0.00;
				}
				BigDecimal diasInvent = new BigDecimal( Math.abs(dias_invent));
				BigDecimal rd_diasInvent = diasInvent.setScale(1, RoundingMode.HALF_UP);
				//System.out.println("DiasInventario -- "+dias_invent);
				if(dias_invent != 0 ){
					color = "";
					//System.out.println("-----------> "+rd_diasInvent);
					numC = Double.parseDouble(rd_diasInvent.toString());
					if(numC < 0.0){
						color = "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='"+color+"'>"+frmt.format(rd_diasInvent)+"</font></td>"; // Dias de inventario
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Dias de inventario
				}
				
				
				//if(!rd_pendiente_x_fact.toString().equals("0")){
				if(pendiente_x_fact != 0.0){
					color = "";
					numC = Double.parseDouble(rd_pendiente_x_fact.toString());
					if(numC < 0.0){
						color = "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='"+color+"'>"+frmt.format(rd_pendiente_x_fact)+"</font></td>"; // P?endiente por facturar
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // P?endiente por facturar
				}
				
				
				//Semaforizacion
				Double min_stock_clc = min_stock * clc_prom_diario;
				String s_disponible = "";
				if(disponible >= (min_stock_clc * 1.0001) && disponible <= (min_stock_clc * 1.15)){
					s_disponible = amarilloSuave;
				}
				if(disponible < min_stock_clc ){
					s_disponible = amarilloFuerte;
				}
				if( disponible != 0.0){
					color = "";
					numC = Double.parseDouble(rd_disponible.toString());
					if(numC < 0.0){
						color = "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_disponible+"'><font color='"+color+"'>"+frmt.format(rd_disponible)+"</font></td>"; //Cajas Disponmibles
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Cajas Disponmibles
				}
				
				
				if(costo_final != 0.0){
					color = "";
					numC = Double.parseDouble(rd_costo_final.toString());
					if(numC < 0.0){
						color = "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='"+color+"'>"+frmt.format(rd_costo_final)+"</font></td>"; //Costo
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Costo
				}
				
				
				if(dia_inventario.isNaN() || dia_inventario.isInfinite()){
					dia_inventario = 0.00;
				}
				//Semaforizacion
				String s_diasInvent = "";
				if(dia_inventario >= (min_stock * 1.0001) && dia_inventario <= (min_stock * 1.15)){
					s_diasInvent = amarilloSuave;
				}
				if(dia_inventario < min_stock ){
					s_diasInvent = amarilloFuerte;
				}
				BigDecimal diasInventario = new BigDecimal(Math.abs(dia_inventario));
				BigDecimal rd_diasInventario = diasInventario.setScale(1, RoundingMode.HALF_UP); // Se cambia de 2  a 1 decimal
				if( dia_inventario != 0.0){
					color = "";
					numC = Double.parseDouble(rd_diasInventario.toString());
					if(numC < 0.0){
						color = "red";
					}
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_diasInvent+"'><font color='"+color+"'>"+frmt.format(rd_diasInventario)+"</font></td>"; // Dia Inventario (dias)
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(hoy.getTime())+"</td>"; // Fecha
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Dia Inventario (dias)
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(cal.getTime())+"</font></td>"; // Fecha
				}
				
				
				
				if(x_tmpo_mtnr_stock.isNaN() || x_tmpo_mtnr_stock.isInfinite()){
					x_tmpo_mtnr_stock = 0.0;
				}
				
				
				
				
				
				if(x_tmpo_mtnr_stock != 0.0){
					color = "";
					numC = Double.parseDouble(rd_tmpo_mtnr_stock.toString());
					if(numC < 0.0){
						color = "red";
					}
					
					if(tmpo_mtnr_stock < 0  && fechaP1 == null){
						s_tmpMntnrStock = rojoFuerte; // ----> Color fuerte menor a 0
						color = "";
					}
					if(tmpo_mtnr_stock >= 0 && tmpo_mtnr_stock <= 2  && fechaP1 == null){
						s_tmpMntnrStock = rojoMedio; // ----> Color medio de 0 a 2
					}
					if(tmpo_mtnr_stock > 2 && tmpo_mtnr_stock < 10  && fechaP1 == null){
						s_tmpMntnrStock = rojoClaro; // ----> Color suave de 2 a 10
					}
					if(tmpo_mtnr_stock >= 10 && fechaP1 == null){
						s_tmpMntnrStock = verdeClaro; // ----> Color verde suave de 2 a 10 y pierde un color si tiene pedido
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_tmpMntnrStock+"'><font color='"+color+"'>"+frmt.format(rd_tmpo_mtnr_stock)+"</font></td>"; // Tiempo Limite Para Levantar Stock
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Tiempo Limite Para Levantar Stock
				}
				
				//System.out.println(dto.getCodeProd()+" <---> "+tmpo_lvtr_ped);
				//System.out.println("TiempoLimite: "+tmpo_lvtr_ped);
				
				if(tmpo_lvtr_ped != 0.0){
					color = "";
					numC = Double.parseDouble(rd_tmpo_lvtr_ped.toString());
					if(numC < 0.0){
						color = "red";
					}
					
					//Semaforizacon
					if(tmpo_lvtr_ped < 0 && fechaP1 == null){
						s_tmpLvntrPed = rojoMedio; // ----> Color fuerte menor a 0
						color = "";
					}
					if(tmpo_lvtr_ped >= 0 && tmpo_lvtr_ped <= 2 && fechaP1 == null){
						s_tmpLvntrPed = rojoMedio; // ----> Color medio de 0 a 2
					}
					if(tmpo_lvtr_ped > 2 && tmpo_lvtr_ped < 10 && fechaP1 == null){
						s_tmpLvntrPed = rojoClaro; // ----> Color suave de 2 a 10
					}
					if(tmpo_lvtr_ped >= 10 && fechaP1 == null){
						s_tmpLvntrPed = verdeClaro; // ----> Color verde suave de 2 a 10 y pierde color si tiene un pedido
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_tmpLvntrPed+"'><font color='"+color+"'>"+frmt.format(rd_tmpo_lvtr_ped)+"</font></td>"; // Tiempo Limite Para Levantar Pedido
				}else{
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // ---><---Tiempo Limite Para Levantar Pedido
				}
				
				html += "</tr>";
				
				/*//Fila con la Marca y los totales por marca
				String marca_ini = "";
				String marca_fin = "";
				String marca = "";
				if(i == 0  && i+1 < row.size()){
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i+1).getIdMarca();
					if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
						Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
							
						//Continuidad Existencias al Minimi P1
						/*Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
							
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}_/*_
						
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_clc_prom_diario_mrc+"</strong></td>"; // Promedio
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_existencias_ttl_mss+"</strong></td>"; // existencia en inventarios
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_invent_mrc+"</strong></td>"; // Dias de inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_pend_x_fac_nrml_mrc+"</strong></td>"; // P?endiente por facturar
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_disponible_mrc+"</strong></td>"; //Cajas Disponmibles
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong></strong></td>"; // Dia Inventario (dias) ****
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong></strong></td>"; // Fecha
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_mantener_stock+"</strong></td>"; // Tiempo Limite Para Mantener Stock
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_levantar_ped+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
						html += "</tr>";						
					}
				}else{
					
					if(i+1 < row.size()){
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i+1).getIdMarca();
						
						if(!marca_ini.equals(marca_fin)){
							Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
							Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
							
							Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
							Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
							Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
							
							Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
							Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
							Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
							Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
							Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
							
							///Tiempo Limite Para Levantar Pedido
							Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
							Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
							Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
							Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
							Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
							Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
							
							Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
							Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
							Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
							
							Double beOculto = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
							Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
							
							//Continuidad Existencias al Minimi P1
							Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
							Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
							Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
							if(cont_exist_min_p1.isNaN()){
								cont_exist_min_p1 = 0.0;
							}
							if(cont_exist_min_p2.isNaN()){
								cont_exist_min_p2 = 0.0;
							}
							if(cont_exist_min_p3.isNaN()){
								cont_exist_min_p3 = 0.0;
							}
							
							BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
							BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
							BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
							BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
							BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
							BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
							BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
							BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
							BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
							
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
									"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
											"</strong></td>"; //Producto
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_clc_prom_diario_mrc+"</strong></td>"; // Promedio
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_existencias_ttl_mss+"</strong></td>"; // existencia en inventarios
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_invent_mrc+"</strong></td>"; // Dias de inventario
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_pend_x_fac_nrml_mrc+"</strong></td>"; // P?endiente por facturar
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_disponible_mrc+"</strong></td>"; //Cajas Disponmibles
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong></strong></td>"; // Dia Inventario (dias) ****
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong></strong></td>"; // Fecha
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_mantener_stock+"</strong></td>"; // Tiempo Limite Para Mantener Stock
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_levantar_ped+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
							html += "</tr>";
							
						}
					}else{
						marca_ini = dto.getIdMarca();
						marca_fin = row.get(i).getIdMarca();
						
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double dias_invent_1_mrc = existencias_ttl_mss / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc; //Tiempo limite para mantener Stock
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
						Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc; ///Tiempo Limite Para LEvantar Pedido
						
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_1_mrc = new BigDecimal(dias_invent_1_mrc);
						BigDecimal rd_dias_invent_1_mrc = _dias_invent_1_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_clc_prom_diario_mrc+"</strong></td>"; // Promedio
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_existencias_ttl_mss+"</strong></td>"; // existencia en inventarios
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_invent_1_mrc+"</strong></td>"; // Dias de inventario
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_pend_x_fac_nrml_mrc+"</strong></td>"; // P?endiente por facturar
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_disponible_mrc+"</strong></td>"; //Cajas Disponmibles
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_dias_invent_mrc+"</strong></td>"; // Dia Inventario (dias) ****
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong></strong></td>"; // Fecha
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_mantener_stock+"</strong></td>"; // Tiempo Limite Para Mantener Stock
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_levantar_ped+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
						html += "</tr>";						
					}
				}*/
			}
			
			html += "</tbody>";
			html += "</table> ";
			
			System.out.println("Tabla... ");
			return html;
		}
	//Tabla Previcion de compras
	public String getTablePrevisionCompras(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id
			) throws UnsupportedEncodingException {
		
		String html = "";
		boolean org_param = false;
		 
		/*HashMap body = invent.getDataAnalisisVentas(chart_id, cust_id, anioIni, anioFin, 
				id_user, id_dashboard, gpoMed, id_modulo, filtros);
		*/
				
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td colspan=2></td>";
		html += "<td >Prom/Dia</td>";
		html += "<td >Existencias</td>";
		html += "<td >Dias Inventario</td>";
		html += "<td >X Facturar Normal</td>";
		html += "<td >Disponible</td>";
		html += "<td colspan=2>Dias Inventario</td>";
		html += "<td >Tiempo Limite Para Mantener Stock</td>";
		html += "<td >Tiempo Limite Para Levantar Pedido</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<th></th>";
		html += "<th></th>";
		html += "<th>Cajas</th>";
		html += "<th>Cajas</th>";
		html += "<th>Dias</th>";
		html += "<th>Cajas</th>";
		html += "<th>Cajas</th>";
		html += "<th>Dias</th>";
		html += "<th>Fecha</th>";
		html += "<th>Dias</th>";
		html += "<th>Dias</th>";
		html += "</tr>";
		html += "</thead>";
		html +="<tbody >";
		
		List<InvDTO> row = invent.getDataAnalisisVentas(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, "1");
		for(InvDTO dto: row){

		
		html += "<tr>";
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Cve Marca
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Producto
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Total por dia: Total por periodos de meses / numero de dias por los cuatro meses
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // existencia en inventarios
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Dias de inventario
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // P?endiente por facturar
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Cajas Disponmibles
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Dia Inventario
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Fecha Inventario
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Tiempo para Mantener Stock 
		html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Tiempo Limite Para Levantar Pedido
		html += "</tr>";		
		}
		System.out.println("Tabla... ");
		return html;
	}
	
	public String getTablePrinIndicadores(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String mes) throws UnsupportedEncodingException, ParseException {
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		String html = "";
		boolean org_param = false;

		Double in_diasP1 = 0.0;
		Double in_diasP2 = 0.0;
		Double in_diasP3 = 0.0;
		Double in_cajasP1 = 0.0;
		Double in_cajasP2 = 0.0;
		Double in_cajasP3 = 0.0;
		
		int meses_enc = 4;
		int meses_col = 4;
		int meses_fil = 4;
		int meses_ttl = 4;
		int meses_marca = 4;
		if(mes == null || mes == "" || mes.equals("null")){
		}else{
			meses_enc = Integer.parseInt(mes);
			meses_col = Integer.parseInt(mes);
			meses_fil = Integer.parseInt(mes);
			meses_ttl = Integer.parseInt(mes);
		}
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td colspan=2></td>";
		html += "<td >Tiempo L&iacute;mite Para Mantener Stock</td>";
		html += "<td >Tiempo L&iacute;mite Para Levantar Pedido</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<th></th>";
		html += "<th></th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "</tr>";
		
		html += "</thead>";
		html +="<tbody >";

		
		Calendar m1_Ini = new GregorianCalendar();
		Calendar m1_Fin = new GregorianCalendar();
		Calendar m2_Ini = new GregorianCalendar();
		Calendar m2_Fin = new GregorianCalendar();
		Calendar m3_Ini = new GregorianCalendar();
		Calendar m3_Fin = new GregorianCalendar();
		Calendar m4_Ini = new GregorianCalendar();
		Calendar m4_Fin = new GregorianCalendar();
		Calendar hoy = new GregorianCalendar();
		
		String fechaL = invent.obtieneFechaCargaDatos();
	    System.out.println("Fecha De Carga--->"+fechaL);
	    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date fLoad = null;
		
		 //Formato para fecha
	    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		    
	    //Tooltips para el rango de los meses 
	    /*String toltip_meses = "";
	    int rango_mes = 28;
	    Calendar fecha_hoy = new GregorianCalendar();
	    Calendar inicio = new GregorianCalendar();
	    Calendar fin =  new GregorianCalendar();
	    for(int w = 1; w <= meses_ttl; w ++){
	    	fecha_hoy = new GregorianCalendar();
	    	inicio = new GregorianCalendar();
	    	fin  = new GregorianCalendar();
	    	//System.out.println("Periodo Fechas Inicio: "+formatoDeFecha.format(fin.getTime()) +" -- "+formatoDeFecha.format(inicio.getTime()) );
	    	if(w != 1){
	    		inicio.add(fecha_hoy.DATE, -(rango_mes*(w-1))-1);
	    		fin.add(fecha_hoy.DATE, -(rango_mes*w));
	    	}else{
	    		inicio.add(fecha_hoy.DATE,-1);
	    		fin.add(fecha_hoy.DATE, -rango_mes);
	    	}
	    	toltip_meses += "<div id=msgMes"+w+" style='display: none;' class=tip >"+ formatoDeFecha.format(fin.getTime()) + " - " + formatoDeFecha.format(inicio.getTime())+"</div>";;
	    	//System.out.println("Periodo Fechas Final: "+formatoDeFecha.format(fin.getTime()) +" -- "+formatoDeFecha.format(inicio.getTime()) );
	    }*/
	    
		List<InvPrevComp> row = invent.getDataPrevComprasProdGnrl(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, null);
		HashMap hm_meses = invent.getDataMesesPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, null);
		//HashMap ttl_marcas = invent.getDataMesesTtlPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id);
		
		for(int i = 0; i < row.size(); i ++){
			
			InvPrevComp dto = row.get(i);
			if(mes == null || mes == "" || mes.equals("null")){
				meses_fil = 4;
				meses_ttl = 4;
				meses_marca = 4; 
			}else{
				meses_fil = Integer.parseInt(mes);
				meses_ttl = Integer.parseInt(mes);
				meses_marca = Integer.parseInt(mes);
			}
			if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
				fLoad =  formatoF2.parse(fechaL);
				hoy.setTime(fLoad);
			}else{
				hoy.add(Calendar.DATE , -1);
			}
		    
			
			Calendar cal = new GregorianCalendar();
			DecimalFormat formatter = new DecimalFormat("###,###,##0.000");
			String mes_prb = (String) hm_meses.get(dto.getIdProd()+"_ttl");
			
			Double ttl_cajas = 0.0;
			//System.out.println("Null "+mes_prb);
			if(mes_prb != null){
				ttl_cajas = Double.parseDouble(mes_prb);
			}
			int perd_dias = meses_fil * 28;
					
			BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
			BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
			
			
			Double clc_prom_diario = ttl_cajas / perd_dias;
			System.out.println("Promedio <--->  "+clc_prom_diario);
			BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
			BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
			Double existencia = Double.parseDouble(dto.getTtlExist());
			System.out.println("Existencias <---> "+existencia);
			BigDecimal bd_existencia = new BigDecimal(existencia);
			BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
			
			//Double dias_invent = existencia / clc_prom_diario;
			//System.out.println("<---> "+dias_invent);
			Double pendiente_x_fact = Double.parseDouble(dto.getPndXFact());
			BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
			BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
			Double disponible = existencia - pendiente_x_fact;
			System.out.println("Disponible <---> "+disponible);
			Double dias_invent = existencia / clc_prom_diario;
			System.out.println("DiasInvet <---> "+dias_invent);
			
			BigDecimal bd_disponible = new BigDecimal(disponible);
			BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
			
			Double dia_inventario = disponible / clc_prom_diario;
			System.out.println("DiaInvent <---> "+dia_inventario);
			Double costo = Double.parseDouble(dto.getCosto());
			Double costo_final = disponible * costo;
			int dia_invent_fin = (int) Math.round(dia_inventario - 1);
			
			BigDecimal bd_costo_final = new BigDecimal(costo_final);
			BigDecimal rd_costo_final = bd_costo_final.setScale(1, RoundingMode.HALF_UP);
			cal.add(Calendar.DATE, dia_invent_fin);
			
			String nomProducto = "";
			String ruta = "detalle_producto.jsp?id_prod="+dto.getIdProd()+"&id_mar="+dto.getIdMarca()+"&id_chart="+chart_id;
			nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong> + </strong></a>";
			
			html += "<tr class=mrc_"+dto.getIdMarca()+">";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Marca
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProd()+nomProducto+"</td>"; //Producto
			//Genera los periodos a mostrar
			for (int x = 1; x <= meses_fil; meses_fil --){
				String prb_ttl_x_mes = (String) hm_meses.get(dto.getIdProd()+"_"+meses_fil);
				Double ttl_cajas_x_mes = 0.0;
				if(prb_ttl_x_mes != null){
					ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
				}
				//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
				BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
				BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_ttl_cajas_mes+"</td>"; //Mes4
			}

			Double tmpo_mtnr_stock = 0.0;
			Double min_stock = Double.parseDouble(dto.getMinStock());
			Double tiempo_prov = Double.parseDouble(dto.getTiempoProv());
			tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
			
			
			
			double diasP1 = Double.parseDouble(dto.getDiasP1());
			double diasP2 = Double.parseDouble(dto.getDiasP2());
			double diasP3 = Double.parseDouble(dto.getDiasP3());
			double cajasP1 = Double.parseDouble(dto.getCajasP1());
			double cajasP2 = Double.parseDouble(dto.getCajasP2());
			double cajasP3 = Double.parseDouble(dto.getCajasP3());
			
			Double ddiP1_prod = (cajasP1 + in_cajasP1) / clc_prom_diario;
			Double ddiP2_prod = (cajasP2 + in_cajasP2) / clc_prom_diario;
			Double ddiP3_prod = (cajasP3 + in_cajasP3) / clc_prom_diario;
			
			//Calculo #Dias De Llegada
			Date fechaP1 = (Date) dto.getFechaP1();
			Date fechaP2 = (Date) dto.getFechaP2();
			Date fechaP3 = (Date) dto.getFechaP3();
			
			//Calculo de diferencia entre fechas
			//Fecha inicial
			Calendar inicio1 =  new GregorianCalendar();
			Calendar inicio2 =  new GregorianCalendar();
			Calendar inicio3 =  new GregorianCalendar();
			
			Calendar finP1 =  new GregorianCalendar();
			Calendar finP2 =  new GregorianCalendar();
			Calendar finP3 =  new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			inicio1.setTime(fLoad);
			inicio2.setTime(fLoad);
			inicio3.setTime(fLoad);
			long ini, fin1, fin2, fin3, difP1, difP2, difP3, difDiasP1 = 0, difDiasP2 = 0, difDiasP3 = 0;
			
			
			if(fechaP1 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha1 = sdf.format(fechaP1.getTime());
				String frmInicio = sdf.format(inicio1.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha1);
				
				finP1.setTime(fFechaF);
				inicio1.setTime(fFechaI);
				
				difP1 = finP1.getTimeInMillis() - inicio1.getTimeInMillis() ;
				
				difDiasP1 = difP1/(24 * 60 * 60 * 1000);
							
			}
			//System.out.println("DiasPAraQueLlegue: "+difDiasP1);
			//System.out.println("DiasIntroducir: "+in_diasP1);
			if(fechaP2 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha2 = sdf.format(fechaP2.getTime());
				String frmInicio = sdf.format(inicio2.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha2);
				
				finP2.setTime(fFechaF);
				inicio2.setTime(fFechaI);
				
				difP2 = finP2.getTimeInMillis() - inicio2.getTimeInMillis() ;
				
				difDiasP2 = difP2/(24 * 60 * 60 * 1000);
			}
			if(fechaP3 != null){
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha3 = sdf.format(fechaP3.getTime());
				String frmInicio = sdf.format(inicio3.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha3);
				
				finP3.setTime(fFechaF);
				inicio3.setTime(fFechaI);
				
				difP3 = finP3.getTimeInMillis() - inicio3.getTimeInMillis() ;
				
				difDiasP3 = difP3/(24 * 60 * 60 * 1000);
			}
			
			//Double beOculto_prod = (ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) / min_stock;
			Double beOculto_prod = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
			Double tmpo_lvtr_ped = beOculto_prod - tiempo_prov;
			if(tmpo_lvtr_ped.isInfinite() || tmpo_lvtr_ped.isNaN()){
				tmpo_lvtr_ped = 0.0;
			}
			if(tmpo_mtnr_stock.isInfinite() || tmpo_mtnr_stock.isNaN()){
				tmpo_mtnr_stock = 0.0;
			}
			//Double tmpo_lvtr_ped = (ddiP1 + ddiP2 + ddiP3 + dia_inventario) - min_stock;
			
			BigDecimal _tmpo_mtnr_stock = new BigDecimal(tmpo_mtnr_stock);
			BigDecimal rd_tmpo_mtnr_stock = _tmpo_mtnr_stock.setScale(1, RoundingMode.HALF_UP);
			BigDecimal _tmpo_lvtr_ped = new BigDecimal(tmpo_lvtr_ped);
			BigDecimal rd_tmpo_lvtr_ped = _tmpo_lvtr_ped.setScale(1, RoundingMode.HALF_UP);
			
			Double contExistMin_1 = 0.0;
			Double contExistMin_2 = 0.0;
			Double contExistMin_3 = 0.0;
			
			Double ddiDiasP1 = 0.0;
			Double ddiDiasP2 = 0.0;
			Double ddiDiasP3 = 0.0;
			
			Double ddiPedDiasP1 = 0.0;
			Double ddiPedDiasP2 = 0.0;
			Double ddiPedDiasP3 = 0.0;
			
			ddiPedDiasP1 = (cajasP1 + in_cajasP1) / clc_prom_diario;
			
			ddiDiasP1 = ((((dia_inventario - (difDiasP1 + in_diasP1) + ddiPedDiasP1) - min_stock) / ddiPedDiasP1) * ddiPedDiasP1);
			System.out.println("DDIDia1 --> (((("+dia_inventario +"- ("+difDiasP1 +"+"+ in_diasP1+") +"+ ddiPedDiasP1+") -"+ min_stock+") / "+ddiPedDiasP1+") *"+ ddiPedDiasP1+")="+ddiDiasP1);
			contExistMin_1 = (((dia_inventario - (difDiasP1 + in_diasP1)- min_stock))/ddiDiasP1) * ddiDiasP1;
			System.out.println(" Exist1 --> ((("+dia_inventario +"- ("+difDiasP1 +"+"+ in_diasP1+"-"+ min_stock+"))/"+ddiDiasP1+") * "+ddiDiasP1+")="+ contExistMin_1 );
			if(contExistMin_1.isInfinite() || contExistMin_1.isNaN()){
				contExistMin_1 = 0.0;
			}
			//System.out.println("-----> "+contExistMin_1);
			BigDecimal _contExistMin_1 = new BigDecimal(contExistMin_1);
			BigDecimal rd_contExistMin_1 = _contExistMin_1.setScale(1, RoundingMode.HALF_UP);
			
			ddiPedDiasP2 = (cajasP2 + in_cajasP2) / clc_prom_diario;
			ddiDiasP2 = ((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP2 + in_diasP2) - min_stock) / ddiPedDiasP2) * ddiPedDiasP2);
			//System.out.println("(((("+dia_inventario +"+"+ ddiPedDiasP1 +"+"+ ddiPedDiasP2+") - ("+difDiasP2 +"+"+ in_diasP2+") - "+min_stock+") /"+ ddiPedDiasP2+") *"+ ddiPedDiasP2+")="+ddiDiasP2);
			//contExistMin_2 = ((((dia_inventario + ddiPedDiasP1) - (diasP2 + in_diasP2)- min_stock))/ddiDiasP2) * ddiDiasP2;
			contExistMin_2 = ((((dia_inventario + ddiPedDiasP1) - (difDiasP2 + in_diasP2)- min_stock)/ddiPedDiasP2) * ddiPedDiasP2);
			//System.out.println("Exist2 --> "+"(((("+dia_inventario +"+"+ ddiPedDiasP1+") - ("+difDiasP2 +"+"+ in_diasP2+")-"+ min_stock+"))/"+ddiDiasP2+") *"+ ddiDiasP2+"="+contExistMin_2);
			if(contExistMin_2.isInfinite() || contExistMin_2.isNaN()){
				contExistMin_2 = 0.0;
			}
			BigDecimal _contExistMin_2 = new BigDecimal(contExistMin_2);
			BigDecimal rd_contExistMin_2 = _contExistMin_2.setScale(1, RoundingMode.HALF_UP);
			
			ddiPedDiasP3 = (cajasP3 + in_cajasP3) / clc_prom_diario;
			ddiDiasP3 = ((((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3)) + ddiPedDiasP3) - min_stock) / ddiPedDiasP3) * ddiPedDiasP3);
			//System.out.println("(((((("+dia_inventario +"+"+ ddiPedDiasP1 +"+"+ ddiPedDiasP2+") - ("+difDiasP3 +"+"+ in_diasP3+")) +"+ ddiPedDiasP3+") -"+ min_stock+") /"+ ddiPedDiasP3+") *"+ ddiPedDiasP3+")="+ddiDiasP3);
			contExistMin_3 = ((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3) - min_stock) / ddiDiasP3) *ddiDiasP3);
			//System.out.println("Exist3 --> "+ "(((("+dia_inventario +"+"+ ddiPedDiasP1 +"+"+ ddiPedDiasP2+") - ("+difDiasP3 +"+"+ in_diasP3+") -"+ min_stock+") / "+ddiDiasP3+") *"+ddiDiasP3+")="+contExistMin_3);
			if(contExistMin_3.isInfinite() || contExistMin_3.isNaN()){
				contExistMin_3 = 0.0;
			}
			BigDecimal _contExistMin_3 = new BigDecimal(contExistMin_3);
			BigDecimal rd_contExistMin_3 = _contExistMin_3.setScale(1, RoundingMode.HALF_UP);
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
			String color = "";
			double numC = 0.0;
			
			//Semaforizacion
			String s_tmpMntnrStock = "";
			String s_tmpLvntrPed = "";
			String s_contExistP1 = "";
			String s_contExistP2 = "";
			String s_contExistP3 = "";
			String colorRojo =  "#FF0000";
			String colorNaranja = "#FE9A2E";
			String colorNaranjaSuave = "#F5BCA9";
			String colorVerdeClaro  = "#A9F5A9";
			String colorNaranjaClaro = "#F5D0A9";
			
			String rojoFuerte = "#FF6161";
			String rojoMedio  = "#FFB8B7";
			String rojoClaro = "#FFDAC8";
			String verdeClaro = "#F0FFC5";
			
			
			if(!rd_tmpo_mtnr_stock.toString().equals("0.0")){
				numC = Double.parseDouble(rd_tmpo_mtnr_stock.toString());
				if(numC < 0.0){
					color = "red";
				}
				
				if(tmpo_mtnr_stock < 0  && fechaP1 == null){
					s_tmpMntnrStock = rojoFuerte; // ----> Color fuerte menor a 0
					color = "";
				}
				if(tmpo_mtnr_stock >= 0 && tmpo_mtnr_stock <= 2  && fechaP1 == null){
					s_tmpMntnrStock = rojoMedio; // ----> Color medio de 0 a 2
				}
				if(tmpo_mtnr_stock > 2 && tmpo_mtnr_stock < 10  && fechaP1 == null){
					s_tmpMntnrStock = rojoClaro; // ----> Color suave de 2 a 10
				}
				if(tmpo_mtnr_stock >= 10 && fechaP1 == null){
					s_tmpMntnrStock = verdeClaro; // ----> Color verde suave de 2 a 10 y pierde un color si tiene pedido
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_tmpMntnrStock+"'><font color='"+color+"'>"+frmt.format(rd_tmpo_mtnr_stock)+"<font></td>"; // Tiempo Limite Para Levantar Stock
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Tiempo Limite Para Levantar Stock
			}
			
			
			if(!rd_tmpo_lvtr_ped.toString().equals("0.0")){
				color = "";
				numC = Double.parseDouble(rd_tmpo_lvtr_ped.toString());
				if(numC < 0.0){
					color = "red";
				}
				
				//Semaforizacion
				if(tmpo_lvtr_ped < 0 && fechaP1 == null){
					s_tmpLvntrPed = rojoFuerte; // ----> Color fuerte menor a 0
					color  = "";
				}
				if(tmpo_lvtr_ped >= 0 && tmpo_lvtr_ped <= 2 && fechaP1 == null){
					s_tmpLvntrPed = rojoMedio; // ----> Color medio de 0 a 2
				}
				if(tmpo_lvtr_ped > 2 && tmpo_lvtr_ped < 10 && fechaP1 == null){
					s_tmpLvntrPed = rojoClaro; // ----> Color suave de 2 a 10
				}
				if(tmpo_lvtr_ped >= 10 && fechaP1 == null){
					s_tmpLvntrPed = verdeClaro; // ----> Color verde suave de 2 a 10 y pierde color si tiene un pedido
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_tmpLvntrPed+"'><font color='"+color+"'>"+frmt.format(rd_tmpo_lvtr_ped)+"</font></td>"; // Tiempo Limite Para Levantar Pedido
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Tiempo Limite Para Levantar Pedido
			}
			
			
			
			if(!rd_contExistMin_1.toString().equals("0.0")){
				color = "";
				numC = Double.parseDouble(rd_contExistMin_1.toString());
				if(numC < 0.0){
					color = "red";
				}
				
				//Semaforizcion
				if(contExistMin_1 <= -2 ){
					s_contExistP1 = rojoFuerte;
					color = "";
				}
				if(contExistMin_1 > -2 && contExistMin_1 < 0){
					s_contExistP1 = rojoMedio;
				}
				if(contExistMin_1 >= 0 && contExistMin_1 <= 3){
					s_contExistP1 = rojoClaro; // color mas claro
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_contExistP1+"'><font color='"+color+"'>"+frmt.format(rd_contExistMin_1)+"</font></td>"; // Continuidad Existencias al Minimo
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo
			}
			
			
			if(!rd_contExistMin_2.toString().equals("0.0")){
				color = "";
				numC = Double.parseDouble(rd_contExistMin_2.toString());
				if(numC < 0.0){
					color = "red";
				}
				//Semaforizcion
				
				if(contExistMin_2 <= -2 ){
					s_contExistP2 = rojoFuerte;
					color = "";
				}
				if(contExistMin_2 > -2 && contExistMin_2 < 0){
					s_contExistP2 = rojoMedio;
				}
				if(contExistMin_2 >= 0 && contExistMin_2 <= 3){
					s_contExistP2 = rojoClaro; // color mas claro
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_contExistP2+"'><font color='"+color+"'>"+frmt.format(rd_contExistMin_2)+"</font></td>"; // Continuidad Existencias al Minimo
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo
			}
			
			
			if(!rd_contExistMin_3.toString().equals("0.0")){
				color = "";
				numC = Double.parseDouble(rd_contExistMin_3.toString());
				if(numC < 0.0){
					color = "red";
				}
				
				//Semaforizcion
				
				if(contExistMin_3 <= -2 ){
					s_contExistP3 = rojoFuerte;
					color  = "";
				}
				if(contExistMin_3 > -2 && contExistMin_3 < 0){
					s_contExistP3 = rojoMedio;
				}
				if(contExistMin_3 >= 0 && contExistMin_3 <= 3){
					s_contExistP3 = rojoClaro; // color mas claro
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor  = '"+s_contExistP3+"'><font color='"+color+"'>"+frmt.format(rd_contExistMin_3)+"</font></td>"; // Continuidad Existencias al Minimo
			}else{
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo
			}
			
			html += "</tr>";
			
			//Fila con la Marca y los totales por marca
			/*String marca_ini = "";
			String marca_fin = "";
			String marca = "";
			if(i == 0  && i+1 < row.size()){
				marca_ini = dto.getIdMarca();
				marca_fin = row.get(i+1).getIdMarca();
				if(!marca_ini.equals(marca_fin)){
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_mantener_stock+"</strong></td>"; // Tiempo Limite Para Levantar Stock
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_levantar_ped+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo
					html += "</tr>";						
				}
			}else{
				
				if(i+1 < row.size()){
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i+1).getIdMarca();
					
					if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc;
						Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
					
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
						BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
						BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
						BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_mantener_stock+"</strong></td>"; // Tiempo Limite Para Levantar Stock
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_levantar_ped+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo
						html += "</tr>";
						
					}
				}else{
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i).getIdMarca();
					
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc; //Tiempo limite para mantener Stock
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double disP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc; ///Tiempo Limite Para LEvantar Pedido
					
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
					
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_mantener_stock+"</strong></td>"; // Tiempo Limite Para Levantar Stock
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_tiempo_limit_levantar_ped+"</strong></td>"; // Tiempo Limite Para Levantar Pedido
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo
					html += "</tr>";						
				}
			}*/
			
		}
		
		html += "</tbody>";
		html += "</table> ";
		
		System.out.println("Tabla... ");
		return html;
	}
	//Analisis Compras Visto Por Dias
	public String getTableAnalisisCompras(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String mes) throws UnsupportedEncodingException, ParseException {
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		String html = "";
		boolean org_param = false;

		Double in_diasP1 = 0.0;
		Double in_diasP2 = 0.0;
		Double in_diasP3 = 0.0;
		Double in_cajasP1 = 0.0;
		Double in_cajasP2 = 0.0;
		Double in_cajasP3 = 0.0;
		
		
		int meses_enc = 4;
		int meses_col = 4;
		int meses_fil = 4;
		int meses_ttl = 4;
		int meses_marca = 4;
		if(mes == null || mes == "" || mes.equals("null")){
		}else{
			meses_enc = Integer.parseInt(mes);
			meses_col = Integer.parseInt(mes);
			meses_fil = Integer.parseInt(mes);
			meses_ttl = Integer.parseInt(mes);
		}
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td colspan=2></td>";
		html += "<td align=center colspan=5><strong>Pedido 1</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 2</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 3</strong></td>";
		html += "</tr>";
		
		html += "<tr><td colspan=2></td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > DDI Pedido</td>";
		html += "<td colspan=2>DDI al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > DDI Pedido</td>";
		html += "<td colspan=2>DDI al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > DDI Pedido</td>";
		html += "<td colspan=2>DDI al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<th></th>";
		html += "<th></th>";
		//html += "<th>DIAS</th>";
		//html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIA</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>Fecha</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIA</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>Fecha</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIA</th>";
		html += "<th>DIAS</th>";
		html += "</tr>";
		
		html += "</thead>";
		html +="<tbody >";

		
		Calendar m1_Ini = new GregorianCalendar();
		Calendar m1_Fin = new GregorianCalendar();
		Calendar m2_Ini = new GregorianCalendar();
		Calendar m2_Fin = new GregorianCalendar();
		Calendar m3_Ini = new GregorianCalendar();
		Calendar m3_Fin = new GregorianCalendar();
		Calendar m4_Ini = new GregorianCalendar();
		Calendar m4_Fin = new GregorianCalendar();
		Calendar hoy = new GregorianCalendar();
		 //Formato para fecha
	    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		    
	    //Tooltips para el rango de los meses 
	    String toltip_meses = "";
	    int rango_mes = 28;
	    Calendar fecha_hoy = new GregorianCalendar();
	    Calendar inicio = new GregorianCalendar();
	    Calendar fechaF1 =  new GregorianCalendar();
	    Calendar fechaF2 =  new GregorianCalendar();
	    Calendar fechaF3 =  new GregorianCalendar();
	    
	    String fechaL = invent.obtieneFechaCargaDatos();
	    System.out.println("Fecha De Carga--->"+fechaL);
	    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date fLoad = null;
		
	    
		List<InvPrevComp> row = invent.getDataPrevCompras(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id);
		HashMap hm_meses = invent.getDataMesesPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, null);
		//HashMap ttl_marcas = invent.getDataMesesTtlPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id);
		
		
		Calendar dias_llegada_dia_P1 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P2 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P3 =  new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P1 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P2 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P3 =  new GregorianCalendar();
	    Calendar fTip =  new GregorianCalendar();
		for(int i = 0; i < row.size(); i ++){
			
			
			fecha_hoy = new GregorianCalendar();
			dias_llegada_dia_P1 = new GregorianCalendar();
		    dias_llegada_dia_P2 = new GregorianCalendar();
		    dias_llegada_dia_P3 =  new GregorianCalendar();
		    DDI_al_llegar_dia_P1 = new GregorianCalendar();
		    DDI_al_llegar_dia_P2 = new GregorianCalendar();
		    DDI_al_llegar_dia_P3 =  new GregorianCalendar();
		    
		    if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
				fLoad =  formatoF2.parse(fechaL);
				hoy.setTime(fLoad);
			}else{
				hoy.add(Calendar.DATE , -1);
			}
		    
		    fTip =  new GregorianCalendar();
			InvPrevComp dto = row.get(i);
			if(mes == null || mes == "" || mes.equals("null")){
				meses_fil = 4;
				meses_ttl = 4;
				meses_marca = 4; 
			}else{
				meses_fil = Integer.parseInt(mes);
				meses_ttl = Integer.parseInt(mes);
				meses_marca = Integer.parseInt(mes);
			}
			Calendar cal = new GregorianCalendar();
			DecimalFormat formatter = new DecimalFormat("###,###,##0.000");
			String mes_prb = (String) hm_meses.get(dto.getIdProd()+"_ttl");
			
			Double ttl_cajas = 0.0;
			//System.out.println("Null "+mes_prb);
			if(mes_prb != null){
				ttl_cajas = Double.parseDouble(mes_prb);
			}
			int perd_dias = meses_fil * 28;
					
			BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
			BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
			
			
			Double clc_prom_diario = ttl_cajas / perd_dias;
			//System.out.println("<---> Promedio: "+clc_prom_diario);
			BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
			BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
			Double existencia = Double.parseDouble(dto.getTtlExist());
			//System.out.println("<---> Existencias: "+existencia);
			BigDecimal bd_existencia = new BigDecimal(existencia);
			BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
			Double dias_invent1 = existencia / clc_prom_diario;
			/*//Semaforizacion
			String semaf = "";
			//System.out.println("Semaforizacion --> "+disponible + " -- " + min_stock + " -- "+minStockI + " -- "+minStockIPerc);
			if(Math.abs(existencias) >= Math.abs(minStockI) && Math.abs(disponible) <= Math.abs(minStockIPerc)){
				System.out.println("Color Suave --> "+disponible + " -- " + min_stock + " -- "+minStockI + " -- "+minStockIPerc);
				semaf = "#2EFE9A";
			}
			if(disponible < minStockI){
				System.out.println("Color Fuerte --> "+disponible + " -- " + min_stock + " -- "+minStockI+ " -- "+minStockIPerc);
				semaf = "#FF4000";
			}*/
			
			//System.out.println("<---> Dias Ivent 1 "+dias_invent1);
			
			Double pendiente_x_fact = Double.parseDouble(dto.getPndXFact());
			BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
			BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
			Double disponible = existencia - pendiente_x_fact;
			//System.out.println("<---> Disponible: "+disponible);
			Double dias_invent = disponible / clc_prom_diario;
			//double dias_invent_f = disponible / clc_prom_diario;
			//System.out.println("<---> DiasInvent: " +  dias_invent);
			BigDecimal bd_disponible = new BigDecimal(disponible);
			BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
			
			Double dia_inventario = disponible / clc_prom_diario;
			Double costo = Double.parseDouble(dto.getCosto());
			Double costo_final = disponible * costo;
			int dia_invent_fin = (int) Math.round(dia_inventario - 1);
			
			BigDecimal bd_costo_final = new BigDecimal(costo_final);
			BigDecimal rd_costo_final = bd_costo_final.setScale(1, RoundingMode.HALF_UP);
			cal.add(Calendar.DATE, dia_invent_fin);
			
			//Link en producto, el cual abrira el simulador
			String nomProducto = "";
			String ruta = "ic_analisis_compras_sim_fin.jsp?id_prod="+dto.getIdProd()+"&id_mar="+dto.getIdMarca();
			
			//String nomProducto = "";
			//String ruta = "detalle_producto.jsp?id_prod="+dto.getIdProducto()+"&id_mar="+dto.getIdMarca()+"&id_chart="+chart_id;
			//nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong>+</strong></a>";
			
			if(chart_id.equals("7")){
				nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong> +</strong></a>";
			}else{
				nomProducto = "<a href='#' onclick=abrePop('"+"detalle_producto.jsp?id_prod="+dto.getIdProd()+"&id_mar="+dto.getIdMarca()+"&id_chart="+chart_id+"');><strong> +</strong></a>";
			}
			html += "<tr class=mrc_"+dto.getIdMarca()+">";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Marca
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProd()+nomProducto+"</td>"; //Producto
			//Genera los periodos a mostrar
			for (int x = 1; x <= meses_fil; meses_fil --){
				String prb_ttl_x_mes = (String) hm_meses.get(dto.getIdProd()+"_"+meses_fil);
				Double ttl_cajas_x_mes = 0.0;
				if(prb_ttl_x_mes != null){
					ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
				}
				//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
				BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
				BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_ttl_cajas_mes+"</td>"; //Mes4
			}

			Double tmpo_mtnr_stock = 0.0;
			Double min_stock = Double.parseDouble(dto.getMinStock());
			Double tiempo_prov = Double.parseDouble(dto.getTiempoProv());
			tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
			
			double diasP1 = Double.parseDouble(dto.getDiasP1());
			double diasP2 = Double.parseDouble(dto.getDiasP2());
			double diasP3 = Double.parseDouble(dto.getDiasP3());
			double cajasP1 = Double.parseDouble(dto.getCajasP1());
			double cajasP2 = Double.parseDouble(dto.getCajasP2());
			double cajasP3 = Double.parseDouble(dto.getCajasP3());
			
			Double tmpo_lvtr_ped = ((((diasP1 + diasP2 + diasP3 + dia_inventario) - min_stock ) * cajasP1 ) / cajasP1) - tiempo_prov;
			
			//Calculo #Dias De Llegada
			Date fechaP1 = (Date) dto.getFechaP1();
			Date fechaP2 = (Date) dto.getFechaP2();
			Date fechaP3 = (Date) dto.getFechaP3();
			//System.out.println("Fechas ->"+fechaP1.getTime());
			//Calculo de diferencia entre fechas
			//Fecha inicial
			Calendar inicio1 =  new GregorianCalendar();
			Calendar inicio2 =  new GregorianCalendar();
			Calendar inicio3 =  new GregorianCalendar();
			
			Calendar finP1 =  new GregorianCalendar();
			Calendar finP2 =  new GregorianCalendar();
			Calendar finP3 =  new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			inicio1.setTime(fLoad);
			inicio2.setTime(fLoad);
			inicio3.setTime(fLoad);
			//System.out.println("F1: "+sdf.format(inicio1.getTime())+"F2: "+sdf.format(inicio2.getTime())+"F3: "+sdf.format(inicio.getTime()));
			long ini, fin1, fin2, fin3, difP1, difP2, difP3, difDiasP1 = 0, difDiasP2 = 0, difDiasP3 = 0;
			
			
			if(fechaP1 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha1 = sdf.format(fechaP1.getTime());
				String frmInicio = sdf.format(inicio1.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha1);
				
				finP1.setTime(fFechaF);
				inicio1.setTime(fFechaI);
				
				difP1 = finP1.getTimeInMillis() - inicio1.getTimeInMillis() ;
				
				difDiasP1 = difP1/(24 * 60 * 60 * 1000);
							
			}
			//System.out.println("DiasPAraQueLlegue: "+difDiasP1);
			//System.out.println("DiasIntroducir: "+in_diasP1);
			if(fechaP2 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha2 = sdf.format(fechaP2.getTime());
				String frmInicio = sdf.format(inicio2.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha2);
				
				finP2.setTime(fFechaF);
				inicio2.setTime(fFechaI);
				
				difP2 = finP2.getTimeInMillis() - inicio2.getTimeInMillis() ;
				
				difDiasP2 = difP2/(24 * 60 * 60 * 1000);
			}
			if(fechaP3 != null){
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha3 = sdf.format(fechaP3.getTime());
				String frmInicio = sdf.format(inicio3.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha3);
				
				finP3.setTime(fFechaF);
				inicio3.setTime(fFechaI);
				
				difP3 = finP3.getTimeInMillis() - inicio3.getTimeInMillis() ;
				
				difDiasP3 = difP3/(24 * 60 * 60 * 1000);
			}
			
			System.out.println("Dif1: "+difDiasP1+" Dif2: "+difDiasP2+" Dif3: "+difDiasP3);
			Double contExistMin_1 = 0.0;
			Double contExistMin_2 = 0.0;
			Double contExistMin_3 = 0.0;
			
			Double ddiDiasAlLlegarP1 = 0.0;
			Double ddiDiasAlLlegarP2 = 0.0;
			Double ddiDiasAlLlegarP3 = 0.0;
			
			Double ddiPedDiasP1 = 0.0;
			Double ddiPedDiasP2 = 0.0;
			Double ddiPedDiasP3 = 0.0;
			
			ddiPedDiasP1 = (cajasP1 + in_cajasP1) / clc_prom_diario;
			
			//System.out.println("DDIPedDiasP1: "+"("+cajasP1 +"+"+ in_cajasP1+") /"+ clc_prom_diario+" = "+ddiPedDiasP1);
			ddiDiasAlLlegarP1 = ((((dias_invent - (difDiasP1 + in_diasP1) + ddiPedDiasP1) - min_stock) / ddiPedDiasP1) * ddiPedDiasP1);
			System.out.println("(((("+dias_invent +"- ("+difDiasP1 +"+"+ in_diasP1+") +"+ ddiPedDiasP1+") -"+ min_stock+") /"+ ddiPedDiasP1+") *"+ ddiPedDiasP1+")="+ddiDiasAlLlegarP1);
			contExistMin_1 = (((dias_invent - (difDiasP1 + in_diasP1)- min_stock))/ddiDiasAlLlegarP1) * ddiDiasAlLlegarP1;
			System.out.println("Exist1 --> (((("+dias_invent +"- ("+difDiasP1 +"+"+ in_diasP1+") + "+ddiPedDiasP1+") - "+min_stock+") / "+ddiPedDiasP1+") * "+ddiPedDiasP1+") =  "+ddiDiasAlLlegarP1+"="+contExistMin_1);
			//System.out.println("DDI Al Llegar P1: "+ddiDiasAlLlegarP1);
			if(ddiPedDiasP1.isInfinite() || ddiPedDiasP1.isNaN()){
				ddiPedDiasP1 = 0.0;
			}
			//System.out.println("("+cajasP1 +"+"+ in_cajasP1+") /"+ clc_prom_diario);
			
			BigDecimal _ddiPedDiasP1 = new BigDecimal(ddiPedDiasP1);
			BigDecimal rd_ddiPedDiasP1 = _ddiPedDiasP1.setScale(1, RoundingMode.HALF_UP);
			if(ddiDiasAlLlegarP1.isInfinite() || ddiDiasAlLlegarP1.isNaN()){
				ddiDiasAlLlegarP1 = 0.0;
			}
			BigDecimal _ddiDiasP1 = new BigDecimal(ddiDiasAlLlegarP1);
			BigDecimal rd_ddiDiasP1 = _ddiDiasP1.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_1.isInfinite() || contExistMin_1.isNaN()){
				contExistMin_1 = 0.0;
			}
			BigDecimal _contExistMin_1 = new BigDecimal(contExistMin_1);
			BigDecimal rd_contExistMin_1 = _contExistMin_1.setScale(1, RoundingMode.HALF_UP);
			BigDecimal rd_contExistMin_T1 = _contExistMin_1.setScale(0, RoundingMode.HALF_UP);
			
			ddiPedDiasP2 = (cajasP2 + in_cajasP2) / clc_prom_diario;
			
			ddiDiasAlLlegarP2 = ((((dias_invent + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP2 + in_diasP2) - min_stock) / ddiPedDiasP2) * ddiPedDiasP2);
			//System.out.println("(((("+dias_invent +"+"+ ddiPedDiasP1 +"+"+ ddiPedDiasP2+") - ("+difDiasP2 +"+"+ in_diasP2+") -"+ min_stock+") /"+ ddiPedDiasP2+") *"+ ddiPedDiasP2+")="+ddiDiasAlLlegarP2);
			contExistMin_2 = ((((dias_invent + ddiPedDiasP1) - (difDiasP2 + in_diasP2)- min_stock)/ddiDiasAlLlegarP2) * ddiDiasAlLlegarP2);
			//System.out.println("Exist2 --> (((("+dias_invent +"+"+ ddiPedDiasP1+") - ("+difDiasP2 +"+"+ in_diasP2+")-"+ min_stock+"))/"+ddiDiasAlLlegarP2+") *"+ ddiDiasAlLlegarP2+")="+contExistMin_2);
			if(ddiPedDiasP2.isInfinite() || ddiPedDiasP2.isNaN()){
				ddiPedDiasP2 = 0.0;
			}
			BigDecimal _ddiPedDiasP2 = new BigDecimal(ddiPedDiasP2);
			BigDecimal rd_ddiPedDiasP2 = _ddiPedDiasP2.setScale(1, RoundingMode.HALF_UP);
			if(ddiDiasAlLlegarP2.isInfinite() || ddiDiasAlLlegarP2.isNaN()){
				ddiDiasAlLlegarP2 = 0.0;
			}
			BigDecimal _ddiDiasP2 = new BigDecimal(ddiDiasAlLlegarP2);
			BigDecimal rd_ddiDiasP2 = _ddiDiasP2.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_2.isInfinite() || contExistMin_2.isNaN()){
				contExistMin_2 = 0.0;
			}
			BigDecimal _contExistMin_2 = new BigDecimal(contExistMin_2);
			BigDecimal rd_contExistMin_2 = _contExistMin_2.setScale(1, RoundingMode.HALF_UP);
			BigDecimal rd_contExistMin_T2 = _contExistMin_1.setScale(0, RoundingMode.HALF_UP);
			
			ddiPedDiasP3 = (cajasP3 + in_cajasP3) / clc_prom_diario;
			
			ddiDiasAlLlegarP3 = ((((((dias_invent + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3)) + ddiPedDiasP3) - min_stock) / ddiPedDiasP3) * ddiPedDiasP3);
			//System.out.println("(((((("+dias_invent +"+"+ ddiPedDiasP1 +"+"+ ddiPedDiasP2+") - ("+difDiasP3 +"+"+ in_diasP3+")) +"+ ddiPedDiasP3+") - "+min_stock+") /"+ ddiPedDiasP3+") *"+ ddiPedDiasP3+")="+ddiDiasAlLlegarP3);
			contExistMin_3 = ((((dias_invent + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3) - min_stock))/ ddiDiasAlLlegarP3) * ddiDiasAlLlegarP3;
			// System.out.println("Exist3 --> (((("+dias_invent +"+"+ ddiPedDiasP1 +"+"+ ddiPedDiasP2+") - ("+difDiasP3 +"+"+ in_diasP3+") - "+min_stock+"))/"+ ddiDiasAlLlegarP3+") *"+ ddiDiasAlLlegarP3+"="+contExistMin_3);
			if(ddiPedDiasP3.isInfinite() || ddiPedDiasP3.isNaN()){
				ddiPedDiasP3 = 0.0;
			}
			BigDecimal _ddiPedDiasP3 = new BigDecimal(ddiPedDiasP3);
			BigDecimal rd_ddiPedDiasP3 = _ddiPedDiasP3.setScale(1, RoundingMode.HALF_UP);
			if(ddiDiasAlLlegarP3.isInfinite() || ddiDiasAlLlegarP3.isNaN()){
				ddiDiasAlLlegarP3 = 0.0;
			}
			BigDecimal _ddiDiasP3 = new BigDecimal(ddiDiasAlLlegarP3);
			BigDecimal rd_ddiDiasP3 = _ddiDiasP3.setScale(0, RoundingMode.HALF_UP);   
			if(contExistMin_3.isInfinite() || contExistMin_3.isNaN()){
				contExistMin_3 = 0.0;
			}
			BigDecimal _contExistMin_3 = new BigDecimal(contExistMin_3);
			BigDecimal rd_contExistMin_3 = _contExistMin_3.setScale(1, RoundingMode.HALF_UP);
			BigDecimal rd_contExistMin_T3 = _contExistMin_1.setScale(0, RoundingMode.HALF_UP);
			
			int i_diasP1 = (int) difDiasP1;
			int i_diasP2 = (int) difDiasP2;
			int i_diasP3 = (int) difDiasP3;
			dias_llegada_dia_P1.add(hoy.DATE, i_diasP1);
			dias_llegada_dia_P2.add(hoy.DATE, i_diasP2);
			dias_llegada_dia_P3.add(hoy.DATE, i_diasP3);
			int i_ddiAlLlegarP1 = Integer.valueOf(rd_ddiDiasP1.intValueExact());
			//System.out.println("Dias --> (("+i_diasP1+"+"+i_ddiAlLlegarP1+") - 1)= "+((i_diasP1 + i_ddiAlLlegarP1) - 1));
			int i_ddiAlLlegarP2 = Integer.valueOf(rd_ddiDiasP2.intValueExact());
			int i_ddiAlLlegarP3 = Integer.valueOf(rd_ddiDiasP3.intValueExact());
			
			int diaFinal = (i_diasP1 + i_ddiAlLlegarP1);
			//System.out.println("DiaP1-->: "+i_diasP1 + "+" + i_ddiAlLlegarP1+ "="+diaFinal);
			//System.out.println("Dial Final Dias: "+diaFinal);
			DDI_al_llegar_dia_P1.setTime(fLoad);
			DDI_al_llegar_dia_P2.setTime(fLoad);
			DDI_al_llegar_dia_P3.setTime(fLoad);
			System.out.println("F1-->: "+sdf.format(DDI_al_llegar_dia_P1.getTime())+" Dias : "+(i_diasP1 + i_ddiAlLlegarP1));
			
			DDI_al_llegar_dia_P1.add(Calendar.DATE, (i_diasP1 + i_ddiAlLlegarP1));
			DDI_al_llegar_dia_P2.add(Calendar.DATE, (i_diasP2 + i_ddiAlLlegarP2));
			DDI_al_llegar_dia_P3.add(Calendar.DATE, (i_diasP3 + i_ddiAlLlegarP3));
			
			String frmtFechaP1 = dto.getFrmFechaP1();
			String frmtFechaP2 = dto.getFrmFechaP2();
			String frmtFechaP3 = dto.getFrmFechaP3();
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
			
			String color1 = "";
			String color2 = "";
			String color3 = "";
			String dia1 = "";
			String dia2 = "";
			String dia3 = "";
			String contE1 = "";
			String contE2 = "";
			
			String contE3 = "";
			int dias1 = 0;
			Double numC1 = 0.0;
			
			//Semaforizacion
			String s_tmpMntnrStock = "";
			String s_tmpLvntrPed = "";
			String s_contExistP1 = "";
			String s_contExistP2 = "";
			String s_contExistP3 = "";
			String s_ddiDiasAlLlegar1 = "";
			String s_ddiDiasAlLlegar2 = "";
			String s_ddiDiasAlLlegar3 = "";
			
			String colorRojo =  "#FF0000";
			String colorNaranja = "#FE9A2E";
			String colorNaranjaSuave = "#F5BCA9";
			String colorVerdeClaro  = "#A9F5A9";
			String colorNaranjaClaro = "#F5D0A9";
			
			String amarilloClaro = "#FFFFCC";
			String amarilloObscuro = "#FFFF99";
			String verdeClaro  = "#EBFFB1";
			String verdeObscuro = "#C6E6A2";
			
			String rojoFuerte = "#FF6161";
			String rojoMedio  = "#FFB8B7";
			String rojoClaro = "#FFDAC8";
			String verdeClaroCE = "#F0FFC5";
			
			Double max_stock = Double.parseDouble(dto.getMaxStock());
			
			if(frmtFechaP1 == null || frmtFechaP1 == "null"){
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al llegar (dia - fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P1
				
			}else{
				color1 = "";
				color2 = "";
				color3 = "";
				//numC = Double.parseDouble(difDiasP1.toString());
				if(difDiasP1 < 0){
					color1 = "red";
				}
				if(i_ddiAlLlegarP1 < 0){
					color2 = "red";
				}
				numC1 = Double.parseDouble(rd_contExistMin_1.toString());
				if(numC1 < 0){
					color3 = "red";
				}
				fechaF1  = new GregorianCalendar();
				fechaF1.setTime(fLoad);
				
				if(ddiDiasAlLlegarP1 < 0){
					s_ddiDiasAlLlegar1 = amarilloObscuro;
				}
				if(ddiDiasAlLlegarP1 >= 0 && ddiDiasAlLlegarP1 <= ( max_stock * 0.5)){
					s_ddiDiasAlLlegar1 = amarilloClaro;
				}
				if(ddiDiasAlLlegarP1 >= (max_stock * 0.08) && ddiDiasAlLlegarP1 <= (max_stock * 1.2)){
					s_ddiDiasAlLlegar1 = verdeClaro;
				}
				if(ddiDiasAlLlegarP1 >= (max_stock * 1.2001) && ddiDiasAlLlegarP1 <= (max_stock * 300)){
					s_ddiDiasAlLlegar1 = verdeObscuro;
				}
				
				if(contExistMin_1 <= -2 ){
					s_contExistP1 = rojoFuerte;
					color3 = "";
				}
				if(contExistMin_1 > -2 && contExistMin_1 < 0){
					s_contExistP1 = rojoMedio;
				}
				if(contExistMin_1 >= 0 && contExistMin_1 <= 3){
					s_contExistP1 = rojoClaro; // color mas claro
				}
				
				fechaF1.add(Calendar.DATE, (int)Math.round(contExistMin_1));
				System.out.println("FechaF1 "+formatoDeFecha.format(fechaF1.getTime())+"cDias: "+Math.round(contExistMin_1));
				toltip_meses += "<div id=msgtoolTipP1"+dto.getIdProd()+" style='display: none;' class=tip >"+ formatoDeFecha.format(fechaF1.getTime()) +"</div>";
	
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1+">"+frmt.format(difDiasP1)+"</font></td>"; // # Dias Llegada P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getFrmFechaP1()+"</td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor='"+s_ddiDiasAlLlegar1+"'><font color="+color2+">"+frmt.format(i_ddiAlLlegarP1)+"</font></td>"; // DDI al Llegar P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P1.getTime())+"</td>"; // DDI al llegar (dia - fecha)
				html += "<td id=toolTipP1"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+s_contExistP1+"'><font color="+color3+">"+frmt.format(rd_contExistMin_1)+"</font></td>"; // Continuidad Existencias al Minimo P1
				
			}
			
			if(frmtFechaP2 == null || frmtFechaP2 == "null"){
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al llegar (dia - fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P2
				
			}else{
				color1 = "";
				color2 = "";
				color3 = "";
				//numC = Double.parseDouble(difDiasP1.toString());
				if(difDiasP2 < 0){
					color1 = "red";
				}
				if(i_ddiAlLlegarP2 < 0){
					color2 = "red";
				}
				numC1 = Double.parseDouble(rd_contExistMin_2.toString());
				if(numC1 < 0){
					color3 = "red";
				}
				fechaF2  = new GregorianCalendar();
				fechaF2.setTime(fLoad);
				
				if(ddiDiasAlLlegarP2 < 0){
					s_ddiDiasAlLlegar2 = amarilloObscuro;
				}
				if(ddiDiasAlLlegarP2 >= 0 && ddiDiasAlLlegarP2 <= ( max_stock * 0.5)){
					s_ddiDiasAlLlegar2 = amarilloClaro;
				}
				if(ddiDiasAlLlegarP2 >= (max_stock * 0.08) && ddiDiasAlLlegarP2 <= (max_stock * 1.2)){
					s_ddiDiasAlLlegar2 = verdeClaro;
				}
				if(ddiDiasAlLlegarP2 >= (max_stock * 1.2001) && ddiDiasAlLlegarP2 <= (max_stock * 300)){
					s_ddiDiasAlLlegar2 = verdeObscuro;
				}
				
				if(contExistMin_2 <= -2 ){
					s_contExistP2 = rojoFuerte;
					color3 = "";
				}
				if(contExistMin_2 > -2 && contExistMin_2 < 0){
					s_contExistP2 = rojoMedio;
				}
				if(contExistMin_2 >= 0 && contExistMin_2 <= 3){
					s_contExistP2 = rojoClaro; // color mas claro
				}
				
				fechaF2.add(Calendar.DATE, (int)Math.round(contExistMin_2));
				System.out.println("FechaF2--> "+formatoDeFecha.format(fechaF2.getTime())+"cDias: "+Math.round(contExistMin_2));
				toltip_meses += "<div id=msgtoolTipP2"+dto.getIdProd()+" style='display: none;' class=tip >"+ formatoDeFecha.format(fechaF2.getTime()) +"</div>";
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1+">"+frmt.format(difDiasP2)+"</td>"; // # Dias Llegada P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getFrmFechaP2()+"</td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_ddiDiasAlLlegar2+"'><font color="+color2+">"+frmt.format(i_ddiAlLlegarP2)+"</td>"; // DDI al Llegar P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P2.getTime())+"</td>"; // DDI al llegar (dia - fecha)
				html += "<td id=toolTipP2"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor  = '"+s_contExistP2+"'><font color="+color3+">"+frmt.format(rd_contExistMin_2)+"</td>"; // Continuidad Existencias al Minimo P2
				
			}
			
			if(frmtFechaP3 == null || frmtFechaP3 == "null"){
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al llegar (dia - fecha))
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P3
			}else{
				color1 = "";
				color2 = "";
				color3 = "";
				//numC = Double.parseDouble(difDiasP1.toString());
				if(difDiasP3 < 0){
					color1 = "red";
				}
				if(i_ddiAlLlegarP3 < 0){
					color2= "red";
				}
				numC1 = Double.parseDouble(rd_contExistMin_3.toString());
				if(numC1 < 0){
					color3 = "red";
				}
				fechaF3  = new GregorianCalendar();
				fechaF3.setTime(fLoad);
				
				if(ddiDiasAlLlegarP3 < 0){
					s_ddiDiasAlLlegar3 = amarilloObscuro;
				}
				if(ddiDiasAlLlegarP3 >= 0 && ddiDiasAlLlegarP3 <= ( max_stock * 0.5)){
					s_ddiDiasAlLlegar3 = amarilloClaro;
				}
				if(ddiDiasAlLlegarP3 >= (max_stock * 0.08) && ddiDiasAlLlegarP3 <= (max_stock * 1.2)){
					s_ddiDiasAlLlegar3 = verdeClaro;
				}
				if(ddiDiasAlLlegarP3 >= (max_stock * 1.2001) && ddiDiasAlLlegarP3 <= (max_stock * 300)){
					s_ddiDiasAlLlegar3 = verdeObscuro;
				}
				
				if(contExistMin_3 <= -2 ){
					s_contExistP3 = rojoFuerte;
					color3 = "";
				}
				if(contExistMin_3 > -2 && contExistMin_3 < 0){
					s_contExistP3 = rojoMedio;
				}
				if(contExistMin_3 >= 0 && contExistMin_3 <= 3){
					s_contExistP3 = rojoClaro; // color mas claro
				}
				
				fechaF3.add(Calendar.DATE, (int)Math.round(contExistMin_3));
				toltip_meses += "<div id=msgtoolTipP3"+dto.getIdProd()+" style='display: none;' class=tip >"+ formatoDeFecha.format(fechaF3.getTime()) +"</div>";
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1+">"+frmt.format(difDiasP3)+"</td>"; // # Dias Llegada P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getFrmFechaP3()+"</td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor ='"+s_ddiDiasAlLlegar3+"'><font color="+color2+">"+frmt.format(i_ddiAlLlegarP3)+"</td>"; // DDI al Llegar P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P3.getTime())+"</td>"; // DDI al llegar (dia - fecha))
				html += "<td id=toolTipP3"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+s_contExistP3+"'><font color="+color3+">"+frmt.format(rd_contExistMin_3)+"</td>"; // Continuidad Existencias al Minimo P3
			}
			
			html += "</tr>"; 
			
			/*//Fila con la Marca y los totales por marca
			String marca_ini = "";
			String marca_fin = "";
			String marca = "";
			if(i == 0  && i+1 < row.size()){
				marca_ini = dto.getIdMarca();
				marca_fin = row.get(i+1).getIdMarca();
				if(!marca_ini.equals(marca_fin)){
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) - minstock_mrc) * cajasP1) / cajasP1;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double ddiP1alLlegar = (((dias_invent_mrc - (diasP1 + in_diasP1) + ddiP1) - minstock_mrc)/ddiP1)*ddiP1; 
					Double ddiP2alLlegar = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double ddiP3alLlegar = ((((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) + ddiP3) - minstock_mrc) / ddiP3) * ddiP3);
					if(ddiP1alLlegar.isInfinite() || ddiP1alLlegar.isNaN()){
						ddiP1alLlegar = 0.0;
					}
					if(ddiP2alLlegar.isInfinite() || ddiP2alLlegar.isNaN()){
						ddiP2alLlegar = 0.0;
					}
					if(ddiP3alLlegar.isInfinite() || ddiP3alLlegar.isNaN()){
						ddiP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(ddiP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(ddiP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(ddiP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}else{
				
				if(i+1 < row.size()){
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i+1).getIdMarca();
					
					if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) - minstock_mrc) * cajasP1) / cajasP1;
						Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
							
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
							
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						Double ddiP1alLlegar = (((dias_invent_mrc - (diasP1 + in_diasP1) + ddiP1) - minstock_mrc)/ddiP1)*ddiP1; 
						Double ddiP2alLlegar = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double ddiP3alLlegar = ((((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) + ddiP3) - minstock_mrc) / ddiP3) * ddiP3);
						if(ddiP1alLlegar.isInfinite() || ddiP1alLlegar.isNaN()){
							ddiP1alLlegar = 0.0;
						}
						if(ddiP2alLlegar.isInfinite() || ddiP2alLlegar.isNaN()){
							ddiP2alLlegar = 0.0;
						}
						if(ddiP3alLlegar.isInfinite() || ddiP3alLlegar.isNaN()){
							ddiP3alLlegar = 0.0;
						}
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _ddiP1alLlegar = new BigDecimal(ddiP1alLlegar);
						BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2alLlegar = new BigDecimal(ddiP2alLlegar);
						BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3alLlegar = new BigDecimal(ddiP3alLlegar);
						BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
						BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
						BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
						BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3 = new BigDecimal(ddiP3);
						BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2 = new BigDecimal(ddiP2);
						BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP1 = new BigDecimal(ddiP1);
						BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
						html += "</tr>";
						
					}
				}else{
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i).getIdMarca();
					
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double dias_invent_1_mrc = existencias_ttl_mss / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc; //Tiempo limite para mantener Stock
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) - minstock_mrc) * cajasP1) / cajasP1;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double ddiP1alLlegar = (((dias_invent_mrc - (diasP1 + in_diasP1) + ddiP1) - minstock_mrc)/ddiP1)*ddiP1; 
					Double ddiP2alLlegar = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double ddiP3alLlegar = ((((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) + ddiP3) - minstock_mrc) / ddiP3) * ddiP3);
					if(ddiP1alLlegar.isInfinite() || ddiP1alLlegar.isNaN()){
						ddiP1alLlegar = 0.0;
					}
					if(ddiP2alLlegar.isInfinite() || ddiP2alLlegar.isNaN()){
						ddiP2alLlegar = 0.0;
					}
					if(ddiP3alLlegar.isInfinite() || ddiP3alLlegar.isNaN()){
						ddiP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_1_mrc = new BigDecimal(dias_invent_1_mrc);
					BigDecimal rd_dias_invent_1_mrc = _dias_invent_1_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(ddiP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(ddiP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(ddiP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}*/
		}
		
		html += "</tbody>";
		html += "</table> "+ toltip_meses;
		
		System.out.println("Tabla... ");
		return html;
	}
	
	//Analisis Compras Visto Por Cajas
	public String getTableAnalisisComprasCajas(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String mes
			) throws UnsupportedEncodingException, ParseException {
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		String html = "";
		boolean org_param = false;

		Double in_diasP1 = 0.0;
		Double in_diasP2 = 0.0;
		Double in_diasP3 = 0.0;
		Double in_cajasP1 = 0.0;
		Double in_cajasP2 = 0.0;
		Double in_cajasP3 = 0.0;
		
		
		int meses_enc = 4;
		int meses_col = 4;
		int meses_fil = 4;
		int meses_ttl = 4;
		int meses_marca = 4;
		if(mes == null || mes == "" || mes.equals("null")){
		}else{
			meses_enc = Integer.parseInt(mes);
			meses_col = Integer.parseInt(mes);
			meses_fil = Integer.parseInt(mes);
			meses_ttl = Integer.parseInt(mes);
		}
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td colspan=2></td>";
		html += "<td align=center colspan=5><strong>Pedido 1</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 2</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 3</strong></td>";
		html += "</tr>";
		
		html += "<tr><td colspan=2></td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > Pedido</td>";
		html += "<td colspan=2>CAJAS al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > Pedido</td>";
		html += "<td colspan=2>CAJAS al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > Pedido</td>";
		html += "<td colspan=2>CAJAS al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<th></th>";
		html += "<th></th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIA</th>";
		html += "<th>CAJAS</th>";
		html += "</tr>";
		
		html += "</thead>";
		html +="<tbody >";

		
		Calendar m1_Ini = new GregorianCalendar();
		Calendar m1_Fin = new GregorianCalendar();
		Calendar m2_Ini = new GregorianCalendar();
		Calendar m2_Fin = new GregorianCalendar();
		Calendar m3_Ini = new GregorianCalendar();
		Calendar m3_Fin = new GregorianCalendar();
		Calendar m4_Ini = new GregorianCalendar();
		Calendar m4_Fin = new GregorianCalendar();
		Calendar hoy = new GregorianCalendar();
		 //Formato para fecha
	    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		    
	    //Tooltips para el rango de los meses 
	    String toltip_meses = "";
	    int rango_mes = 28;
	    Calendar fecha_hoy = new GregorianCalendar();
	    Calendar inicio = new GregorianCalendar();
	    Calendar fin =  new GregorianCalendar();
	    
	    String fechaL = invent.obtieneFechaCargaDatos();
	    System.out.println("Fecha De Carga--->"+fechaL);
	    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date fLoad = null;
		
		List<InvPrevComp> row = invent.getDataPrevCompras(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id);
		HashMap hm_meses = invent.getDataMesesPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, null);
		//HashMap ttl_marcas = invent.getDataMesesTtlPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id);
		
		Calendar dias_llegada_dia_P1 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P2 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P3 =  new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P1 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P2 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P3 =  new GregorianCalendar();
	    
		for(int i = 0; i < row.size(); i ++){
			
			fecha_hoy = new GregorianCalendar();
			dias_llegada_dia_P1 = new GregorianCalendar();
		    dias_llegada_dia_P2 = new GregorianCalendar();
		    dias_llegada_dia_P3 =  new GregorianCalendar();
		    DDI_al_llegar_dia_P1 = new GregorianCalendar();
		    DDI_al_llegar_dia_P2 = new GregorianCalendar();
		    DDI_al_llegar_dia_P3 =  new GregorianCalendar();
		    
		    if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
				fLoad =  formatoF2.parse(fechaL);
				hoy.setTime(fLoad);
			}else{
				hoy.add(Calendar.DATE , -1);
			}
		    
			InvPrevComp dto = row.get(i);
			if(mes == null || mes == "" || mes.equals("null")){
				meses_fil = 4;
				meses_ttl = 4;
				meses_marca = 4; 
			}else{
				meses_fil = Integer.parseInt(mes);
				meses_ttl = Integer.parseInt(mes);
				meses_marca = Integer.parseInt(mes);
			}
			Calendar cal = new GregorianCalendar();
			DecimalFormat formatter = new DecimalFormat("###,###,##0.000");
			String mes_prb = (String) hm_meses.get(dto.getIdProd()+"_ttl");
			
			Double ttl_cajas = 0.0;
			//System.out.println("Null "+mes_prb);
			if(mes_prb != null){
				ttl_cajas = Double.parseDouble(mes_prb);
			}
			int perd_dias = meses_fil * 28;
					
			BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
			BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
			
			
			Double clc_prom_diario = ttl_cajas / perd_dias;
			//System.out.println("<<<<---->>>>PromDiaCajas: "+ttl_cajas+" / "+perd_dias+ " = "+clc_prom_diario);
			BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
			BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
			Double existencia = Double.parseDouble(dto.getTtlExist());
			BigDecimal bd_existencia = new BigDecimal(existencia);
			BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
			
			
			Double pendiente_x_fact = Double.parseDouble(dto.getPndXFact());
			BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
			BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
			Double disponible = existencia - pendiente_x_fact;
			//System.out.println("---Disponible: "+existencia +"-"+ pendiente_x_fact+ "= "+disponible);
			Double dias_invent = existencia / clc_prom_diario;
			//double dias_invent_f = disponible / clc_prom_diario;
			//System.out.println("<---Dias Inventario: "+disponible+"/"+clc_prom_diario+" = "+dias_invent_f);
			BigDecimal bd_disponible = new BigDecimal(disponible);
			BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
			
			Double dia_inventario = disponible / clc_prom_diario;
			Double costo = Double.parseDouble(dto.getCosto());
			Double costo_final = disponible * costo;
			int dia_invent_fin = (int) Math.round(dia_inventario - 1);
			
			BigDecimal bd_costo_final = new BigDecimal(costo_final);
			BigDecimal rd_costo_final = bd_costo_final.setScale(1, RoundingMode.HALF_UP);
			cal.add(Calendar.DATE, dia_invent_fin);
			
			//Link en producto, el cual abrira el simulador
			String nomProducto = "";
			String ruta = "ic_analisis_cps_cjs_sim_fin.jsp?id_prod="+dto.getIdProd()+"&id_mar="+dto.getIdMarca();
			
			if(chart_id.equals("8")){
				nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong> +</strong></a>";
			}else{
				nomProducto = "<a href='#' onclick=abrePop('"+"detalle_producto.jsp?id_prod="+dto.getIdProd()+"&id_mar="+dto.getIdMarca()+"&id_chart="+chart_id+"');><strong>+</strong></a>";
			}
			
			html += "<tr class=mrc_"+dto.getIdMarca()+">";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Marca
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProd()+nomProducto+"</td>"; //Producto
			//Genera los periodos a mostrar
			for (int x = 1; x <= meses_fil; meses_fil --){
				String prb_ttl_x_mes = (String) hm_meses.get(dto.getIdProd()+"_"+meses_fil);
				Double ttl_cajas_x_mes = 0.0;
				if(prb_ttl_x_mes != null){
					ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
				}
				//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
				BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
				BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_ttl_cajas_mes+"</td>"; //Mes4
			}

			Double tmpo_mtnr_stock = 0.0;
			Double min_stock = Double.parseDouble(dto.getMinStock());
			Double tiempo_prov = Double.parseDouble(dto.getTiempoProv());
			tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
			
			double diasP1 = Double.parseDouble(dto.getDiasP1());
			double diasP2 = Double.parseDouble(dto.getDiasP2());
			double diasP3 = Double.parseDouble(dto.getDiasP3());
			double cajasP1 = Double.parseDouble(dto.getCajasP1());
			double cajasP2 = Double.parseDouble(dto.getCajasP2());
			double cajasP3 = Double.parseDouble(dto.getCajasP3());
			
			Double tmpo_lvtr_ped = (((diasP1 + diasP2 + diasP3 + dia_inventario) - min_stock) * cajasP1) /cajasP1;
			
			//Calculo #Dias De Llegada
			Date fechaP1 = (Date) dto.getFechaP1();
			Date fechaP2 = (Date) dto.getFechaP2();
			Date fechaP3 = (Date) dto.getFechaP3();
			
			//Calculo de diferencia entre fechas
			//Fecha inicial
			Calendar inicio1 =  new GregorianCalendar();
			Calendar inicio2 =  new GregorianCalendar();
			Calendar inicio3 =  new GregorianCalendar();
			
			Calendar finP1 =  new GregorianCalendar();
			Calendar finP2 =  new GregorianCalendar();
			Calendar finP3 =  new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			inicio1.setTime(fLoad);
			inicio2.setTime(fLoad);
			inicio3.setTime(fLoad);
			long ini, fin1, fin2, fin3, difP1, difP2, difP3, difDiasP1 = 0, difDiasP2 = 0, difDiasP3 = 0;
			
			
			if(fechaP1 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha1 = sdf.format(fechaP1.getTime());
				String frmInicio = sdf.format(inicio1.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha1);
				
				finP1.setTime(fFechaF);
				inicio1.setTime(fFechaI);
				
				difP1 = finP1.getTimeInMillis() - inicio1.getTimeInMillis() ;
				
				difDiasP1 = difP1/(24 * 60 * 60 * 1000);
							
			}
			//System.out.println("DiasPAraQueLlegue: "+difDiasP1);
			//System.out.println("DiasIntroducir: "+in_diasP1);
			if(fechaP2 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha2 = sdf.format(fechaP2.getTime());
				String frmInicio = sdf.format(inicio2.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha2);
				
				finP2.setTime(fFechaF);
				inicio2.setTime(fFechaI);
				
				difP2 = finP2.getTimeInMillis() - inicio2.getTimeInMillis() ;
				
				difDiasP2 = difP2/(24 * 60 * 60 * 1000);
			}
			if(fechaP3 != null){
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha3 = sdf.format(fechaP3.getTime());
				String frmInicio = sdf.format(inicio3.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha3);
				
				finP3.setTime(fFechaF);
				inicio3.setTime(fFechaI);
				
				difP3 = finP3.getTimeInMillis() - inicio3.getTimeInMillis() ;
				
				difDiasP3 = difP3/(24 * 60 * 60 * 1000);
			}
			
			Double contExistMin_1 = 0.0;
			Double contExistMin_2 = 0.0;
			Double contExistMin_3 = 0.0;
			
			Double cajasAlLlegarP1 = 0.0;
			Double cajasAlLlegarP2 = 0.0;
			Double cajasAlLlegarP3 = 0.0;
			
			Double ddiPedDiasP1 = 0.0;
			Double ddiPedDiasP2 = 0.0;
			Double ddiPedDiasP3 = 0.0;
			
			Double min_stock1 = clc_prom_diario * min_stock;
			//System.out.println("Min Stock = "+min_stock);
			ddiPedDiasP1 = (cajasP1 + in_cajasP1) / clc_prom_diario;
			//System.out.println("<---DDI Pedido_1: "+ddiPedDiasP1);
			cajasAlLlegarP1 = ((((disponible + cajasP1 + in_cajasP1) - ((difDiasP1 + in_diasP1) * clc_prom_diario)) - min_stock1) / ddiPedDiasP1) * ddiPedDiasP1;
			//System.out.println("<---CajasAlLlegar_1: "+cajasAlLlegarP1);
			contExistMin_1 = ((((dia_inventario - (difDiasP1 + in_diasP1)- min_stock)) * clc_prom_diario) / cajasAlLlegarP1) * cajasAlLlegarP1;
			//System.out.println("<---Existencias Al Minimo 1: "+contExistMin_1);
			//System.out.println("Cajas al llegar: (((("+disponible +"+"+ cajasP1 +"+"+ in_cajasP1+") - (("+difDiasP1 +"+"+ in_diasP1+") * "+clc_prom_diario+")) - "+min_stock1+") / "+ddiPedDiasP1+") *"+ ddiPedDiasP1+") = "+cajasAlLlegarP1);
			if(ddiPedDiasP1.isInfinite() || ddiPedDiasP1.isNaN()){
				ddiPedDiasP1 = 0.0;
			}
			BigDecimal _ddiPedDiasP1 = new BigDecimal(ddiPedDiasP1);
			BigDecimal rd_ddiPedDiasP1 = _ddiPedDiasP1.setScale(1, RoundingMode.HALF_UP);
			if(cajasAlLlegarP1.isInfinite() || cajasAlLlegarP1.isNaN()){
				cajasAlLlegarP1 = 0.0;
			}
			BigDecimal _cajasAlLlegarP1 = new BigDecimal(cajasAlLlegarP1);
			BigDecimal rd_cajasAlLlegarP1 = _cajasAlLlegarP1.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_1.isInfinite() || contExistMin_1.isNaN()){
				contExistMin_1 = 0.0;
			}
			BigDecimal _contExistMin_1 = new BigDecimal(contExistMin_1);
			BigDecimal rd_contExistMin_1 = _contExistMin_1.setScale(1, RoundingMode.HALF_UP);
			BigDecimal rd_contExistMin_1T = _contExistMin_1.setScale(0, RoundingMode.HALF_UP);
			
			ddiPedDiasP2 = (cajasP2 + in_cajasP2) / clc_prom_diario;
			//System.out.println("<<---DDI Pedido_2: "+ddiPedDiasP2);
			cajasAlLlegarP2 = ((((disponible + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((difDiasP2 + in_diasP2) * clc_prom_diario)) - min_stock1) / ddiPedDiasP2) * ddiPedDiasP2;
			//System.out.println("<<---CajasAlLlegar_2: "+cajasAlLlegarP2);
			contExistMin_2 = (((((dia_inventario + ddiPedDiasP1) - (difDiasP2 + in_diasP2) - min_stock) * clc_prom_diario) / cajasAlLlegarP2) * cajasAlLlegarP2); // / por *
			//System.out.println("Existencias Al Minimo 2: "+contExistMin_2);
			if(ddiPedDiasP2.isInfinite() || ddiPedDiasP2.isNaN()){
				ddiPedDiasP2 = 0.0;
			}
			BigDecimal _ddiPedDiasP2 = new BigDecimal(ddiPedDiasP2);
			BigDecimal rd_ddiPedDiasP2 = _ddiPedDiasP2.setScale(1, RoundingMode.HALF_UP);
			if(cajasAlLlegarP2.isInfinite() || cajasAlLlegarP2.isNaN()){
				cajasAlLlegarP2 = 0.0;
			}
			BigDecimal _cajasAlLlegarP2 = new BigDecimal(cajasAlLlegarP2);
			BigDecimal rd_cajasAlLlegarP2 = _cajasAlLlegarP2.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_2.isInfinite() || contExistMin_2.isNaN()){
				contExistMin_2 = 0.0;
			}
			BigDecimal _contExistMin_2 = new BigDecimal(contExistMin_2);
			BigDecimal rd_contExistMin_2 = _contExistMin_2.setScale(1, RoundingMode.HALF_UP);
			BigDecimal rd_contExistMin_2T = _contExistMin_2.setScale(0, RoundingMode.HALF_UP);
			
			ddiPedDiasP3 = (cajasP3 + in_cajasP3) / clc_prom_diario;
			//System.out.println("<<<---DDI Pedido_3: "+ddiPedDiasP3);
			cajasAlLlegarP3 = ((((disponible + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((difDiasP3 + in_diasP3) * clc_prom_diario)) - min_stock1) / ddiPedDiasP3) * ddiPedDiasP3;
			//System.out.println("<<<---CajasAlLlegar_3: "+cajasAlLlegarP3);
			contExistMin_3 = (((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3)- min_stock) * clc_prom_diario) / cajasAlLlegarP3) * cajasAlLlegarP3); // /--*
			//System.out.println("Existencias al minimo 3: "+contExistMin_3);
			if(ddiPedDiasP3.isInfinite() || ddiPedDiasP3.isNaN()){
				ddiPedDiasP3 = 0.0;
			}
			BigDecimal _ddiPedDiasP3 = new BigDecimal(ddiPedDiasP3);
			BigDecimal rd_ddiPedDiasP3 = _ddiPedDiasP3.setScale(1, RoundingMode.HALF_UP);
			if(cajasAlLlegarP3.isInfinite() || cajasAlLlegarP3.isNaN()){
				cajasAlLlegarP3 = 0.0;
			}
			BigDecimal _cajasAlLlegarP3 = new BigDecimal(cajasAlLlegarP3);
			BigDecimal rd_cajasAlLlegarP3 = _cajasAlLlegarP3.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_3.isInfinite() || contExistMin_3.isNaN()){
				contExistMin_3 = 0.0;
			}
			BigDecimal _contExistMin_3 = new BigDecimal(contExistMin_3);
			BigDecimal rd_contExistMin_3 = _contExistMin_3.setScale(1, RoundingMode.HALF_UP);
			BigDecimal rd_contExistMin_3T = _contExistMin_3.setScale(0, RoundingMode.HALF_UP);
			/*int i_diasP1 = (int) diasP1;
			int i_diasP2 = (int) diasP2;
			int i_diasP3 = (int) diasP3;
			dias_llegada_dia_P1.add(fecha_hoy.DATE, i_diasP1 - 1);
			dias_llegada_dia_P2.add(fecha_hoy.DATE, i_diasP2 - 1);
			dias_llegada_dia_P3.add(fecha_hoy.DATE, i_diasP3 - 1);*/
			System.out.println("Difdias1 ---> "+difDiasP1);
			int i_diasP1 = (int) difDiasP1;
			int i_diasP2 = (int) difDiasP2;
			int i_diasP3 = (int) difDiasP3;
			dias_llegada_dia_P1.setTime(fLoad);
			dias_llegada_dia_P2.setTime(fLoad);
			dias_llegada_dia_P3.setTime(fLoad);
			System.out.println("DifDiasI--> "+ i_diasP1);
			
			dias_llegada_dia_P1.add(Calendar.DATE, i_diasP1);
			dias_llegada_dia_P2.add(Calendar.DATE, i_diasP2);
			dias_llegada_dia_P3.add(Calendar.DATE, i_diasP3);
			
			Double d_cjsAlLlegarP1_entrePrm = (cajasAlLlegarP1 / clc_prom_diario);
			//System.out.println("Cajas entre Prom: "+ "("+cajasAlLlegarP1+"/"+clc_prom_diario+"= "+d_cjsAlLlegarP1_entrePrm); 
			if(d_cjsAlLlegarP1_entrePrm.isInfinite() || d_cjsAlLlegarP1_entrePrm.isNaN()){
				d_cjsAlLlegarP1_entrePrm = 0.0;
			}
			BigDecimal cjs_promP1 = new BigDecimal(d_cjsAlLlegarP1_entrePrm);
			//System.out.println("cjsAl Llegar: "+d_cjsAlLlegarP1_entrePrm+"<-->"+cajasAlLlegarP1);
			BigDecimal rd_cjs_promP1 = cjs_promP1.setScale(0, RoundingMode.HALF_UP);  
			Double d_cjsAlLlegarP2_entrePrm = (cajasAlLlegarP2 / clc_prom_diario);
			if(d_cjsAlLlegarP2_entrePrm.isInfinite() || d_cjsAlLlegarP2_entrePrm.isNaN()){
				d_cjsAlLlegarP2_entrePrm = 0.0;
			}
			BigDecimal cjs_promP2 = new BigDecimal(d_cjsAlLlegarP2_entrePrm);
			BigDecimal rd_cjs_promP2 = cjs_promP2.setScale(0, RoundingMode.HALF_UP);
			Double d_cjsAlLlegarP3_entrePrm = (cajasAlLlegarP3 / clc_prom_diario);
			if(d_cjsAlLlegarP3_entrePrm.isInfinite() || d_cjsAlLlegarP3_entrePrm.isNaN()){
				d_cjsAlLlegarP3_entrePrm = 0.0;
			}
			BigDecimal cjs_promP3 = new BigDecimal(d_cjsAlLlegarP3_entrePrm);
			BigDecimal rd_cjs_promP3 = cjs_promP3.setScale(0, RoundingMode.HALF_UP);
			
			int i_cjs_promP1 = Integer.valueOf(rd_cjs_promP1.intValueExact());
			int i_cjs_promP2 = Integer.valueOf(rd_cjs_promP2.intValueExact());
			int i_cjs_promP3 = Integer.valueOf(rd_cjs_promP3.intValueExact());
			//System.out.println("Dias --> "+i_diasP1 +"-"+ rd_cjs_promP1);
			//System.out.println("Dias --> "+i_diasP1 +"-"+ i_cjs_promP1);
			//int diaFinS = (i_diasP1 + i_cjs_promP1) / prom_diario;
			int diasFinal = ((i_diasP1 + i_cjs_promP1) - 1);
			//System.out.println("Dia Final Cajas: " + diasFinal);
			DDI_al_llegar_dia_P1.setTime(fLoad);
			DDI_al_llegar_dia_P2.setTime(fLoad);
			DDI_al_llegar_dia_P3.setTime(fLoad);
			
			System.out.println(sdf.format(DDI_al_llegar_dia_P1.getTime())+" Dias:"+i_diasP1 +"+"+ i_cjs_promP1 +"=" +(i_diasP1 + i_cjs_promP1));
			System.out.println(sdf.format(DDI_al_llegar_dia_P1.getTime())+" Dias:"+i_diasP2 +"+"+ i_cjs_promP2 +"=" +(i_diasP2 + i_cjs_promP2));
			System.out.println(sdf.format(DDI_al_llegar_dia_P1.getTime())+" Dias:"+i_diasP3 +"+"+ i_cjs_promP3 +"=" +(i_diasP3 + i_cjs_promP3));
			
			DDI_al_llegar_dia_P1.add(Calendar.DATE, (i_diasP1 + i_cjs_promP1));
			DDI_al_llegar_dia_P2.add(Calendar.DATE, (i_diasP2 + i_cjs_promP2));
			DDI_al_llegar_dia_P3.add(Calendar.DATE, (i_diasP3 + i_cjs_promP3));
			
			String frmtFechaP1 = dto.getFrmFechaP1();
			String frmtFechaP2 = dto.getFrmFechaP2();
			String frmtFechaP3 = dto.getFrmFechaP3();
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			
			DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
			String color1 = "";
			String color2 = "";
			String color3 = "";
			Double numC2 = 0.0;
			Double numC3 = 0.0;
			
			//Tooltips para el rango de los meses 
		    /*String toltip_meses = "";
		    int rango_mes = 28;
		    Calendar fecha_hoy = new GregorianCalendar();
		    Calendar inicio = new GregorianCalendar();
		    Calendar fin =  new GregorianCalendar();*/
//		    for(int w = 1; w <= meses_ttl; w ++){
//		    	fecha_hoy = new GregorianCalendar();
//		    	inicio = new GregorianCalendar();
//		    	fin  = new GregorianCalendar();
//		    	//System.out.println("Periodo Fechas Inicio: "+formatoDeFecha.format(fin.getTime()) +" -- "+formatoDeFecha.format(inicio.getTime()) );
//		    	if(w != 1){
//		    		inicio.add(fecha_hoy.DATE, -(rango_mes*(w-1))-1);
//		    		fin.add(fecha_hoy.DATE, -(rango_mes*w));
//		    	}else{
//		    		inicio.add(fecha_hoy.DATE,-1);
//		    		fin.add(fecha_hoy.DATE, -rango_mes);
//		    	}
//		    	toltip_meses += "<div id=msgMes"+w+" style='display: none;' class=tip >"+ formatoDeFecha.format(fin.getTime()) + " - " + formatoDeFecha.format(inicio.getTime())+"</div>";;
//		    	//System.out.println("Periodo Fechas Final: "+formatoDeFecha.format(fin.getTime()) +" -- "+formatoDeFecha.format(inicio.getTime()) );
//		    }
			//Semaforizacion
			String s_tmpMntnrStock = "";
			String s_tmpLvntrPed = "";
			String s_contExistP1 = "";
			String s_contExistP2 = "";
			String s_contExistP3 = "";
			String s_ddiCajasAlLlegar1 = "";
			String s_ddiCajasAlLlegar2 = "";
			String s_ddiCajasAlLlegar3 = "";
			
			String colorRojo =  "#FF0000";
			String colorNaranja = "#FE9A2E";
			String colorNaranjaSuave = "#F5BCA9";
			String colorVerdeClaro  = "#A9F5A9";
			String colorNaranjaClaro = "#F5D0A9";
			
			
			String amarilloClaro = "#FFFFCC";
			String amarilloObscuro = "#FFFF99";
			String verdeClaro  = "#EBFFB1";
			String verdeObscuro = "#C6E6A2";
			
			String rojoFuerte = "#FF6161";
			String rojoMedio  = "#FFB8B7";
			String rojoClaro = "#FFDAC8";
			String verdeClaroCE = "#F0FFC5";
			
			Double max_stock = Double.parseDouble(dto.getMaxStock());
			Double max_stock_cajs = max_stock * clc_prom_diario;
			if(frmtFechaP1 == null || frmtFechaP1 == "null"){
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar Dia
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P1
			}else{
				
				color1 = "";
				color2 = "";
				color3 = "";
				if(difDiasP1 < 0){
					color1 = "red";
				}
				numC2 = Double.parseDouble(rd_cajasAlLlegarP1.toString());
				if(numC2 < 0){
					color2= "red";
				}
				numC3 = Double.parseDouble(rd_contExistMin_1.toString());
				if(numC3 < 0){
					color3 = "red";
				}
				
				if(cajasAlLlegarP1 < 0){
					s_ddiCajasAlLlegar1 = amarilloObscuro;
				}
				if(cajasAlLlegarP1 >= 0 && cajasAlLlegarP1 <= ( max_stock_cajs * 0.5)){
					s_ddiCajasAlLlegar1 = amarilloClaro;
				}
				if(cajasAlLlegarP1 >= (max_stock_cajs * 0.08) && cajasAlLlegarP1 <= (max_stock_cajs * 1.2)){
					s_ddiCajasAlLlegar1 = verdeClaro;
				}
				if(cajasAlLlegarP1 >= (max_stock_cajs * 1.2001) && cajasAlLlegarP1 <= (max_stock_cajs * 300)){
					s_ddiCajasAlLlegar1 = verdeObscuro;
				}
				
				
				if(contExistMin_1 <= -2 ){
					s_contExistP1 = rojoFuerte;
					color3 = "";
				}
				if(contExistMin_1 > -2 && contExistMin_1 < 0){
					s_contExistP1 = rojoMedio;
				}
				if(contExistMin_1 >= 0 && contExistMin_1 <= 3){
					s_contExistP1 = rojoClaro; // color mas claro
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1+">"+frmt.format(difDiasP1)+"</td>"; // # Dias Llegada P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getFrmFechaP1()+"</td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_ddiCajasAlLlegar1+"'><font color="+color2+">"+frmt.format(rd_cajasAlLlegarP1)+"</td>"; // DDI al Llegar P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P1.getTime())+"</td>"; // DDI al Llegar Dia
				html += "<td id=toolTip"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+s_contExistP1+"'><font color="+color3+">"+frmt.format(rd_contExistMin_1)+"</td>"; // Continuidad Existencias al Minimo P1
			}
			
			if(frmtFechaP2 == null || frmtFechaP2 == "null"){
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P2
			}else{
				color1 = "";
				color2 = "";
				color3 = "";
				if(difDiasP2 < 0){
					color1 = "red";
				}
				numC2 = Double.parseDouble(rd_cajasAlLlegarP2.toString());
				if(numC2 < 0){
					color2= "red";
				}
				numC3 = Double.parseDouble(rd_contExistMin_2.toString());
				if(numC3 < 0){
					color3 = "red";
				}
				
				if(cajasAlLlegarP2 < 0){
					s_ddiCajasAlLlegar2 = amarilloObscuro;
				}
				if(cajasAlLlegarP2 >= 0 && cajasAlLlegarP2 <= ( max_stock_cajs * 0.5)){
					s_ddiCajasAlLlegar2 = amarilloClaro;
				}
				if(cajasAlLlegarP2 >= (max_stock_cajs * 0.08) && cajasAlLlegarP2 <= (max_stock_cajs * 1.2)){
					s_ddiCajasAlLlegar2 = verdeClaro;
				}
				if(cajasAlLlegarP2 >= (max_stock_cajs * 1.2001) && cajasAlLlegarP2 <= (max_stock_cajs * 300)){
					s_ddiCajasAlLlegar2 = verdeObscuro;
				}
				
				if(contExistMin_2 <= -2 ){
					s_contExistP2 = rojoFuerte;
					color3 = "";
				}
				if(contExistMin_2 > -2 && contExistMin_2 < 0){
					s_contExistP2 = rojoMedio;
				}
				if(contExistMin_2 >= 0 && contExistMin_2 <= 3){
					s_contExistP2 = rojoClaro; // color mas claro
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1+">"+frmt.format(difDiasP2)+"</td>"; // # Dias Llegada P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getFrmFechaP2()+"</td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_ddiCajasAlLlegar2+"'><font color="+color2+">"+frmt.format(rd_cajasAlLlegarP2)+"</td>"; // DDI al Llegar P2
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P2.getTime())+"</td>"; // DDI al Llegar Fecha
				html += "<td id=toolTip"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+s_contExistP2+"'><font color="+color3+">"+frmt.format(rd_contExistMin_2)+"</td>"; // Continuidad Existencias al Minimo P2
			}
			
			if(frmtFechaP3 == null || frmtFechaP3 == "null"){
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Al Llegar Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P3
			}else{
				color1 = "";
				color2 = "";
				color3 = "";
				if(difDiasP3 < 0){
					color1 = "red";
				}
				numC2 = Double.parseDouble(rd_cajasAlLlegarP3.toString());
				if(numC2 < 0){
					color2= "red";
				}
				numC3 = Double.parseDouble(rd_contExistMin_3.toString());
				if(numC3 < 0){
					color3 = "red";
				}
				
				if(cajasAlLlegarP3 < 0){
					s_ddiCajasAlLlegar3 = amarilloObscuro;
				}
				if(cajasAlLlegarP3 >= 0 && cajasAlLlegarP3 <= ( max_stock_cajs * 0.5)){
					s_ddiCajasAlLlegar3 = amarilloClaro;
				}
				if(cajasAlLlegarP3 >= (max_stock_cajs * 0.08) && cajasAlLlegarP3 <= (max_stock_cajs * 1.2)){
					s_ddiCajasAlLlegar3 = verdeClaro;
				}
				if(cajasAlLlegarP3 >= (max_stock_cajs * 1.2001) && cajasAlLlegarP3 <= (max_stock_cajs * 300)){
					s_ddiCajasAlLlegar3 = verdeObscuro;
				}
				
				if(contExistMin_3 <= -3 ){
					s_contExistP3 = rojoFuerte;
					color3 = "";
				}
				if(contExistMin_3 > -2 && contExistMin_3 < 0){
					s_contExistP3 = rojoMedio;
				}
				if(contExistMin_3 >= 0 && contExistMin_3 <= 3){
					s_contExistP3 = rojoClaro; // color mas claro
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1+">"+frmt.format(difDiasP3)+"</td>"; // # Dias Llegada P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getFrmFechaP3()+"</td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor='"+s_ddiCajasAlLlegar3+"'><font color="+color2+">"+frmt.format(rd_cajasAlLlegarP3)+"</td>"; // DDI al Llegar P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P3.getTime())+"</td>"; // DDI Al Llegar Fecha
				html += "<td id=toolTip"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+_contExistMin_3+"'><font color="+color3+">"+frmt.format(rd_contExistMin_3)+"</td>"; // Continuidad Existencias al Minimo P3
			}
			
			html += "</tr>"; 
			
			/*//Fila con la Marca y los totales por marca
			String marca_ini = "";
			String marca_fin = "";
			String marca = "";
			if(i == 0  && i+1 < row.size()){
				marca_ini = dto.getIdMarca();
				marca_fin = row.get(i+1).getIdMarca();
				if(!marca_ini.equals(marca_fin)){
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double minstock1_mrc = clc_prom_diario_mrc * minstock_mrc;
					Double maxstock1_mrc = clc_prom_diario_mrc * maxstock_mrc; 
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc)*cajasP1 ) /cajasP1 ;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = ((((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / clc_prom_diario_mrc) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = (((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / clc_prom_diario_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = (((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / clc_prom_diario_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double cjsP1alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1) - ((diasP1 + in_diasP1) * clc_prom_diario_mrc))- minstock1_mrc) / ddiP1) * ddiP1; 
					Double cjsP2alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((diasP2 + in_diasP2) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP2) * ddiP2);
					Double cjsP3alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((diasP3 + in_diasP3) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP3) * ddiP3);
					if(cjsP1alLlegar.isInfinite() || cjsP1alLlegar.isNaN()){
						cjsP1alLlegar = 0.0;
					}
					if(cjsP2alLlegar.isInfinite() || cjsP2alLlegar.isNaN()){
						cjsP2alLlegar = 0.0;
					}
					if(cjsP3alLlegar.isInfinite() || cjsP3alLlegar.isNaN()){
						cjsP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(cjsP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(cjsP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(cjsP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}else{
				
				if(i+1 < row.size()){
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i+1).getIdMarca();
					
					if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double minstock1_mrc = clc_prom_diario_mrc * minstock_mrc;
						Double maxstock1_mrc = clc_prom_diario_mrc * maxstock_mrc;
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc)*cajasP1 ) /cajasP1 ;
						Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
							
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = ((((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / clc_prom_diario_mrc) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = (((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / clc_prom_diario_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = (((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / clc_prom_diario_mrc) / ddiP3) * ddiP3;
							
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						Double cjsP1alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1) - ((diasP1 + in_diasP1) * clc_prom_diario_mrc))- minstock1_mrc) / ddiP1) * ddiP1; 
						Double cjsP2alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((diasP2 + in_diasP2) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP2) * ddiP2);
						Double cjsP3alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((diasP3 + in_diasP3) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP3) * ddiP3);
						if(cjsP1alLlegar.isInfinite() || cjsP1alLlegar.isNaN()){
							cjsP1alLlegar = 0.0;
						}
						if(cjsP2alLlegar.isInfinite() || cjsP2alLlegar.isNaN()){
							cjsP2alLlegar = 0.0;
						}
						if(cjsP3alLlegar.isInfinite() || cjsP3alLlegar.isNaN()){
							cjsP3alLlegar = 0.0;
						}
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _ddiP1alLlegar = new BigDecimal(cjsP1alLlegar);
						BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2alLlegar = new BigDecimal(cjsP2alLlegar);
						BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3alLlegar = new BigDecimal(cjsP3alLlegar);
						BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
						BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
						BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
						BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3 = new BigDecimal(ddiP3);
						BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2 = new BigDecimal(ddiP2);
						BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP1 = new BigDecimal(ddiP1);
						BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
						html += "</tr>";
						
					}
				}else{
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i).getIdMarca();
					
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double dias_invent_1_mrc = existencias_ttl_mss / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double minstock1_mrc = clc_prom_diario_mrc * minstock_mrc;
					Double maxstock1_mrc = clc_prom_diario_mrc * maxstock_mrc;
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc; //Tiempo limite para mantener Stock
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc)*cajasP1 ) /cajasP1 ;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = ((((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / clc_prom_diario_mrc) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = (((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / clc_prom_diario_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = (((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / clc_prom_diario_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double cjsP1alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1) - ((diasP1 + in_diasP1) * clc_prom_diario_mrc))- minstock1_mrc) / ddiP1) * ddiP1; 
					Double cjsP2alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((diasP2 + in_diasP2) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP2) * ddiP2);
					Double cjsP3alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((diasP3 + in_diasP3) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP3) * ddiP3);
					if(cjsP1alLlegar.isInfinite() || cjsP1alLlegar.isNaN()){
						cjsP1alLlegar = 0.0;
					}
					if(cjsP2alLlegar.isInfinite() || cjsP2alLlegar.isNaN()){
						cjsP2alLlegar = 0.0;
					}
					if(cjsP3alLlegar.isInfinite() || cjsP3alLlegar.isNaN()){
						cjsP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_1_mrc = new BigDecimal(dias_invent_1_mrc);
					BigDecimal rd_dias_invent_1_mrc = _dias_invent_1_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(cjsP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(cjsP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(cjsP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}*/
		}
		
		html += "</tbody>";
		html += "</table> "+ toltip_meses;
		
		System.out.println("Tabla... ");
		return html;
	}
	
	//Analisis compras cajas simulacion
	public String getTableAnalisisComprasCajasSim(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String mes
			) throws UnsupportedEncodingException, ParseException {
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		String html = "";
		boolean org_param = false;

		Double in_diasP1 = 0.0;
		Double in_diasP2 = 0.0;
		Double in_diasP3 = 0.0;
		Double in_cajasP1 = 0.0;
		Double in_cajasP2 = 0.0;
		Double in_cajasP3 = 0.0;
		
		
		int meses_enc = 4;
		int meses_col = 4;
		int meses_fil = 4;
		int meses_ttl = 4;
		int meses_marca = 4;
		if(mes == null || mes == "" || mes.equals("null")){
		}else{
			meses_enc = Integer.parseInt(mes);
			meses_col = Integer.parseInt(mes);
			meses_fil = Integer.parseInt(mes);
			meses_ttl = Integer.parseInt(mes);
		}
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td colspan=2></td>";
		html += "<td align=center colspan=5><strong>Pedido 1</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 2</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 3</strong></td>";
		html += "</tr>";
		
		html += "<tr><td colspan=2></td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > Pedido</td>";
		html += "<td colspan=2>CAJAS al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > Pedido</td>";
		html += "<td colspan=2>CAJAS al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > Pedido</td>";
		html += "<td colspan=2>CAJAS al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<th></th>";
		html += "<th></th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>CAJAS</th>";
		html += "<th>DIA</th>";
		html += "<th>CAJAS</th>";
		html += "</tr>";
		
		html += "</thead>";
		html +="<tbody >";

		
		Calendar m1_Ini = new GregorianCalendar();
		Calendar m1_Fin = new GregorianCalendar();
		Calendar m2_Ini = new GregorianCalendar();
		Calendar m2_Fin = new GregorianCalendar();
		Calendar m3_Ini = new GregorianCalendar();
		Calendar m3_Fin = new GregorianCalendar();
		Calendar m4_Ini = new GregorianCalendar();
		Calendar m4_Fin = new GregorianCalendar();
		Calendar hoy = new GregorianCalendar();
		 //Formato para fecha
	    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		    
	    //Tooltips para el rango de los meses 
	    String toltip_meses = "";
	    int rango_mes = 28;
	    Calendar fecha_hoy = new GregorianCalendar();
	    Calendar inicio = new GregorianCalendar();
	    Calendar fin =  new GregorianCalendar();
	    
	    String fechaL = invent.obtieneFechaCargaDatos();
	    //System.out.println("Fecha De Carga--->"+fechaL);
	    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date fLoad = null;
		
		List<InvPrevComp> row = invent.getDataPrevCompras(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id);
		HashMap hm_meses = invent.getDataMesesPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, null);
		//HashMap ttl_marcas = invent.getDataMesesTtlPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id);
		HashMap hm_prod_sim = invent.getDataProdSimulacion(cust_id, id_user, id_modulo, id_dashboard, chart_id);
		
		Calendar dias_llegada_dia_P1 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P2 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P3 =  new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P1 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P2 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P3 =  new GregorianCalendar();
	    
		for(int i = 0; i < row.size(); i ++){
			
			fecha_hoy = new GregorianCalendar();
			dias_llegada_dia_P1 = new GregorianCalendar();
		    dias_llegada_dia_P2 = new GregorianCalendar();
		    dias_llegada_dia_P3 =  new GregorianCalendar();
		    DDI_al_llegar_dia_P1 = new GregorianCalendar();
		    DDI_al_llegar_dia_P2 = new GregorianCalendar();
		    DDI_al_llegar_dia_P3 =  new GregorianCalendar();
		    
		    if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
				fLoad =  formatoF2.parse(fechaL);
				hoy.setTime(fLoad);
			}else{
				hoy.add(Calendar.DATE , -1);
			}
		    
			InvPrevComp dto = row.get(i);
			if(mes == null || mes == "" || mes.equals("null")){
				meses_fil = 4;
				meses_ttl = 4;
				meses_marca = 4; 
			}else{
				meses_fil = Integer.parseInt(mes);
				meses_ttl = Integer.parseInt(mes);
				meses_marca = Integer.parseInt(mes);
			}
			Calendar cal = new GregorianCalendar();
			DecimalFormat formatter = new DecimalFormat("###,###,##0.000");
			String mes_prb = (String) hm_meses.get(dto.getIdProd()+"_ttl");
			
			Double ttl_cajas = 0.0;
			//System.out.println("Null "+mes_prb);
			if(mes_prb != null){
				ttl_cajas = Double.parseDouble(mes_prb);
			}
			int perd_dias = meses_fil * 28;
					
			BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
			BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
			
			
			Double clc_prom_diario = ttl_cajas / perd_dias;
			BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
			BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
			Double existencia = Double.parseDouble(dto.getTtlExist());
			BigDecimal bd_existencia = new BigDecimal(existencia);
			BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
			
			
			Double pendiente_x_fact = Double.parseDouble(dto.getPndXFact());
			BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
			BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
			Double disponible = existencia - pendiente_x_fact;
			Double dias_invent = existencia / clc_prom_diario;
			//System.out.println("Disponible: "+existencia +"-"+ pendiente_x_fact+ "= "+disponible);
			BigDecimal bd_disponible = new BigDecimal(disponible);
			BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
			
			Double dia_inventario = disponible / clc_prom_diario;
			Double costo = Double.parseDouble(dto.getCosto());
			Double costo_final = disponible * costo;
			int dia_invent_fin = (int) Math.round(dia_inventario - 1);
			
			BigDecimal bd_costo_final = new BigDecimal(costo_final);
			BigDecimal rd_costo_final = bd_costo_final.setScale(1, RoundingMode.HALF_UP);
			cal.setTime(fLoad);
			cal.add(Calendar.DATE, dia_invent_fin);
			
			//Link en producto, el cual abrira el simulador
			String nomProducto = "";
			String ruta = "ic_analisis_cps_cjs_sim_fin.jsp?id_prod="+dto.getIdProd()+"&id_mar="+dto.getIdMarca();
			
			if(chart_id.equals("8")){
				nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong> +</strong></a>";
			}else{
				nomProducto = "";
			}
			
			String id_prod = dto.getIdProd();
			String prod_sim = (String) hm_prod_sim.get(id_prod+"_sim");
			String m_prod_sim = "";
			String del_sim = "";
			String tr_sim= " bgcolor=#FFFFFF";
			//System.out.println("idProd-->"+prod_sim);
			if(prod_sim != null ){
				m_prod_sim = "<font  color = red>";
				tr_sim = " bgcolor= #BDBDBD ";
				del_sim = "<a href='#' onclick=eliminaSimulacion('"+dto.getIdProd()+"');><img src='../../img/close.gif'></a>";
			}
			
			html += "<tr class=mrc_"+dto.getIdMarca()+tr_sim+" >";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Marca
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProd()+nomProducto+del_sim+"</td>"; //Producto
			//Genera los periodos a mostrar
			for (int x = 1; x <= meses_fil; meses_fil --){
				String prb_ttl_x_mes = (String) hm_meses.get(dto.getIdProd()+"_"+meses_fil);
				Double ttl_cajas_x_mes = 0.0;
				if(prb_ttl_x_mes != null){
					ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
				}
				//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
				BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
				BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_ttl_cajas_mes+"</td>"; //Mes4
			}

			Double tmpo_mtnr_stock = 0.0;
			Double min_stock = Double.parseDouble(dto.getMinStock());
			Double tiempo_prov = Double.parseDouble(dto.getTiempoProv());
			tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
			
			double diasP1 = Double.parseDouble(dto.getDiasP1());
			double diasP2 = Double.parseDouble(dto.getDiasP2());
			double diasP3 = Double.parseDouble(dto.getDiasP3());
			double cajasP1 = Double.parseDouble(dto.getCajasP1());
			double cajasP2 = Double.parseDouble(dto.getCajasP2());
			double cajasP3 = Double.parseDouble(dto.getCajasP3());
			
			Double tmpo_lvtr_ped = (((diasP1 + diasP2 + diasP3 + dia_inventario) - min_stock) * cajasP1) /cajasP1;
			
			//Calculo #Dias De Llegada
			Date fechaP1 = (Date) dto.getFechaP1();
			Date fechaP2 = (Date) dto.getFechaP2();
			Date fechaP3 = (Date) dto.getFechaP3();
			
			//Calculo de diferencia entre fechas
			//Fecha inicial
			Calendar inicio1 =  new GregorianCalendar();
			Calendar inicio2 =  new GregorianCalendar();
			Calendar inicio3 =  new GregorianCalendar();
			
			Calendar finP1 =  new GregorianCalendar();
			Calendar finP2 =  new GregorianCalendar();
			Calendar finP3 =  new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			inicio1.setTime(fLoad);
			inicio2.setTime(fLoad);
			inicio3.setTime(fLoad);
			long ini, fin1, fin2, fin3, difP1, difP2, difP3, difDiasP1 = 0, difDiasP2 = 0, difDiasP3 = 0;
			
			
			if(fechaP1 != null){
				String listFechaF [];
				String listFechaI [];
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha1 = sdf.format(fechaP1.getTime());
				String frmInicio = sdf.format(inicio1.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha1);
				
				System.out.println("FechaIni1: "+frmInicio);
				System.out.println("FechaFin1: "+frmFecha1);
				
				listFechaI = frmInicio.split("/");
				listFechaF = frmFecha1.split("/");
				System.out.println("SetFin: "+listFechaF[2] +","+ listFechaF[1]+","+listFechaF[0]);
				finP1.setTime(fFechaF);
				inicio1.setTime(fFechaI);
				
				System.out.println("Fecha---->> "+sdf.format(finP1.getTime()));
				difP1 = finP1.getTimeInMillis() - inicio1.getTimeInMillis() ;
				System.out.println(finP1.getTimeInMillis() +" - "+ inicio1.getTimeInMillis());
				System.out.println("dif: "+difP1);
				difDiasP1 = difP1/(24 * 60 * 60 * 1000);
				System.out.println(difDiasP1 + " = DifDiasP1: "+difDiasP1);
			}
			//System.out.println("DiasPAraQueLlegue: "+difDiasP1);
			//System.out.println("DiasIntroducir: "+in_diasP1);
			if(fechaP2 != null){
				
				String listFechaF [];
				String listFechaI [];
				
				String frmFecha2 = sdf.format(fechaP2.getTime());
				String frmInicio = sdf.format(inicio2.getTime());
				
				listFechaI = frmInicio.split("/");
				listFechaF = frmFecha2.split("/");
				finP2.set(Integer.parseInt(listFechaF[2]), Integer.parseInt(listFechaF[1]), Integer.parseInt(listFechaF[0]));
				inicio2.set(Integer.parseInt(listFechaI[2]), Integer.parseInt(listFechaI[1]), Integer.parseInt(listFechaI[0]));
				
				difP2 = finP2.getTimeInMillis() - inicio2.getTimeInMillis() ;
				difDiasP2 = difP2/(24 * 60 * 60 * 1000);
			}
			if(fechaP3 != null){
				String listFechaF [];
				String listFechaI [];
				
				String frmFecha3 = sdf.format(fechaP3.getTime());
				String frmInicio = sdf.format(inicio3.getTime());
				
				listFechaI = frmInicio.split("/");
				listFechaF = frmFecha3.split("/");
				finP3.set(Integer.parseInt(listFechaF[2]), Integer.parseInt(listFechaF[1]), Integer.parseInt(listFechaF[0]));
				inicio3.set(Integer.parseInt(listFechaI[2]), Integer.parseInt(listFechaI[1]), Integer.parseInt(listFechaI[0]));
				
				difP3 = finP3.getTimeInMillis() - inicio3.getTimeInMillis() ; 
				difDiasP3 = difP3/(24 * 60 * 60 * 1000);
			}
			
			Double contExistMin_1 = 0.0;
			Double contExistMin_2 = 0.0;
			Double contExistMin_3 = 0.0;
			
			Double cajasAlLlegarP1 = 0.0;
			Double cajasAlLlegarP2 = 0.0;
			Double cajasAlLlegarP3 = 0.0;
			
			Double ddiPedDiasP1 = 0.0;
			Double ddiPedDiasP2 = 0.0;
			Double ddiPedDiasP3 = 0.0;
			
			//Cajas y dias introducidos oara la smulacion
			//String id_prod = dto.getIdProd();
			String prev_diasP1 = (String) hm_prod_sim.get(id_prod+"_diasP1");
			String prev_diasP2 = (String) hm_prod_sim.get(id_prod+"_diasP2");
			String prev_diasP3 = (String) hm_prod_sim.get(id_prod+"_diasP3");
			
			String prev_cajasP1 = (String) hm_prod_sim.get(id_prod+"_cajasP1");
			String prev_cajasP2 = (String) hm_prod_sim.get(id_prod+"_cajasP2");
			String prev_cajasP3 = (String) hm_prod_sim.get(id_prod+"_cajasP3");
			
			//System.out.println("Dias P -- "+in_diasP1+" - "+diasP2+" - "+diasP3);
			
			if(prev_diasP1 == null || prev_diasP1.equals("null")){
				prev_diasP1 = "0";
			}
			if(prev_diasP2 == null || prev_diasP2.equals("null")){
				prev_diasP2 = "0";
			}
			if(prev_diasP3 == null || prev_diasP3.equals("null")){
				prev_diasP3 = "0";
			}
			
			if(prev_cajasP1 == null || prev_diasP1.equals("null")){
				prev_cajasP1 = "0";
			}
			if(prev_cajasP2 == null || prev_diasP2.equals("null")){
				prev_cajasP2 = "0";
			}
			if(prev_cajasP3 == null || prev_diasP3.equals("null")){
				prev_cajasP3 = "0";
			}
			
			in_diasP1 = Double.parseDouble(prev_diasP1);
			in_diasP2 = Double.parseDouble(prev_diasP2);
			in_diasP3 = Double.parseDouble(prev_diasP3);
			//System.out.println("...._______..... "+in_diasP1 + in_diasP1 + in_diasP1);
			difDiasP1 = difDiasP1 + in_diasP1.longValue();
			difDiasP2 = difDiasP2 + in_diasP2.longValue();
			difDiasP3 = difDiasP3 + in_diasP3.longValue();
			
			in_cajasP1 = Double.parseDouble(prev_cajasP1);
			in_cajasP2 = Double.parseDouble(prev_cajasP2);
			in_cajasP3 = Double.parseDouble(prev_cajasP3);
			
			if(in_diasP1 == null || in_diasP1.equals("null") ){
				in_diasP1 = 0.0;
			}
			if(in_diasP2 == null || in_diasP2.equals("null")){
				in_diasP2 = 0.0;
			}
			if(in_diasP3 == null || in_diasP3.equals("null")){
				in_diasP3 = 0.0;
			}
			Double min_stock1 = clc_prom_diario * min_stock;
			//System.out.println("Min Stock_1]= "+clc_prom_diario+" * "+min_stock+"= "+min_stock1);
			ddiPedDiasP1 = (cajasP1 + in_cajasP1) / clc_prom_diario;
			//System.out.println("DDI PEdido: "+"("+cajasP1+"+"+in_cajasP1+")/"+clc_prom_diario+"= "+ddiPedDiasP1);
			cajasAlLlegarP1 = ((((disponible + cajasP1 + in_cajasP1) - ((difDiasP1 + in_diasP1) * clc_prom_diario)) - min_stock1) / ddiPedDiasP1) * ddiPedDiasP1;
			contExistMin_1 = ((((dia_inventario - (difDiasP1 + in_diasP1)- min_stock)) * clc_prom_diario) / cajasAlLlegarP1) * cajasAlLlegarP1;
			//System.out.println("Cajas al llegar: (((("+disponible +"+"+ cajasP1 +"+"+ in_cajasP1+") - (("+difDiasP1 +"+"+ in_diasP1+") * "+clc_prom_diario+")) - "+min_stock1+") / "+ddiPedDiasP1+") *"+ ddiPedDiasP1+") = "+cajasAlLlegarP1);
			if(ddiPedDiasP1.isInfinite() || ddiPedDiasP1.isNaN()){
				ddiPedDiasP1 = 0.0;
			}
			BigDecimal _ddiPedDiasP1 = new BigDecimal(ddiPedDiasP1);
			BigDecimal rd_ddiPedDiasP1 = _ddiPedDiasP1.setScale(1, RoundingMode.HALF_UP);
			if(cajasAlLlegarP1.isInfinite() || cajasAlLlegarP1.isNaN()){
				cajasAlLlegarP1 = 0.0;
			}
			BigDecimal _cajasAlLlegarP1 = new BigDecimal(cajasAlLlegarP1);
			BigDecimal rd_cajasAlLlegarP1 = _cajasAlLlegarP1.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_1.isInfinite() || contExistMin_1.isNaN()){
				contExistMin_1 = 0.0;
			}
			BigDecimal _contExistMin_1 = new BigDecimal(contExistMin_1);
			BigDecimal rd_contExistMin_1 = _contExistMin_1.setScale(1, RoundingMode.HALF_UP);
			
			ddiPedDiasP2 = (cajasP2 + in_cajasP2) / clc_prom_diario;
			cajasAlLlegarP2 = ((((disponible + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((difDiasP2 + in_diasP2) * clc_prom_diario)) - min_stock1) / ddiPedDiasP2) * ddiPedDiasP2;
			contExistMin_2 = (((((dia_inventario + ddiPedDiasP1) - (diasP2 + in_diasP2) - min_stock) * clc_prom_diario) / cajasAlLlegarP2) * cajasAlLlegarP2);
			if(ddiPedDiasP2.isInfinite() || ddiPedDiasP2.isNaN()){
				ddiPedDiasP2 = 0.0;
			}
			BigDecimal _ddiPedDiasP2 = new BigDecimal(ddiPedDiasP2);
			BigDecimal rd_ddiPedDiasP2 = _ddiPedDiasP2.setScale(1, RoundingMode.HALF_UP);
			if(cajasAlLlegarP2.isInfinite() || cajasAlLlegarP2.isNaN()){
				cajasAlLlegarP2 = 0.0;
			}
			BigDecimal _cajasAlLlegarP2 = new BigDecimal(cajasAlLlegarP2);
			BigDecimal rd_cajasAlLlegarP2 = _cajasAlLlegarP2.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_2.isInfinite() || contExistMin_2.isNaN()){
				contExistMin_2 = 0.0;
			}
			BigDecimal _contExistMin_2 = new BigDecimal(contExistMin_2);
			BigDecimal rd_contExistMin_2 = _contExistMin_2.setScale(1, RoundingMode.HALF_UP);
			
			ddiPedDiasP3 = (cajasP3 + in_cajasP3) / clc_prom_diario;
			cajasAlLlegarP3 = ((((disponible + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((difDiasP3 + in_diasP3) * clc_prom_diario)) - min_stock1) / ddiPedDiasP3) * ddiPedDiasP3;
			contExistMin_3 = (((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3)- min_stock) * clc_prom_diario) / cajasAlLlegarP3) * cajasAlLlegarP3);
			if(ddiPedDiasP3.isInfinite() || ddiPedDiasP3.isNaN()){
				ddiPedDiasP3 = 0.0;
			}
			BigDecimal _ddiPedDiasP3 = new BigDecimal(ddiPedDiasP3);
			BigDecimal rd_ddiPedDiasP3 = _ddiPedDiasP3.setScale(1, RoundingMode.HALF_UP);
			if(cajasAlLlegarP3.isInfinite() || cajasAlLlegarP3.isNaN()){
				cajasAlLlegarP3 = 0.0;
			}
			BigDecimal _cajasAlLlegarP3 = new BigDecimal(cajasAlLlegarP3);
			BigDecimal rd_cajasAlLlegarP3 = _cajasAlLlegarP3.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_3.isInfinite() || contExistMin_3.isNaN()){
				contExistMin_3 = 0.0;
			}
			BigDecimal _contExistMin_3 = new BigDecimal(contExistMin_3);
			BigDecimal rd_contExistMin_3 = _contExistMin_3.setScale(1, RoundingMode.HALF_UP);
			
			/*int i_diasP1 = (int) diasP1;
			int i_diasP2 = (int) diasP2;
			int i_diasP3 = (int) diasP3;
			dias_llegada_dia_P1.add(fecha_hoy.DATE, i_diasP1 - 1);
			dias_llegada_dia_P2.add(fecha_hoy.DATE, i_diasP2 - 1);
			dias_llegada_dia_P3.add(fecha_hoy.DATE, i_diasP3 - 1);*/
			
			int i_diasP1 = (int) difDiasP1;
			int i_diasP2 = (int) difDiasP2;
			int i_diasP3 = (int) difDiasP3;
			dias_llegada_dia_P1.setTime(fLoad);
			dias_llegada_dia_P2.setTime(fLoad);
			dias_llegada_dia_P3.setTime(fLoad);
			dias_llegada_dia_P1.add(Calendar.DATE, i_diasP1);
			dias_llegada_dia_P2.add(Calendar.DATE, i_diasP2);
			dias_llegada_dia_P3.add(Calendar.DATE, i_diasP3);
			
			Double d_cjsAlLlegarP1_entrePrm = (cajasAlLlegarP1 / clc_prom_diario);
			//System.out.println("Cajas entre Prom: "+ "("+cajasAlLlegarP1+"/"+clc_prom_diario+"= "+d_cjsAlLlegarP1_entrePrm); 
			if(d_cjsAlLlegarP1_entrePrm.isInfinite() || d_cjsAlLlegarP1_entrePrm.isNaN()){
				d_cjsAlLlegarP1_entrePrm = 0.0;
			}
			BigDecimal cjs_promP1 = new BigDecimal(d_cjsAlLlegarP1_entrePrm);
			//System.out.println("cjsAl Llegar: "+d_cjsAlLlegarP1_entrePrm+"<-->"+cajasAlLlegarP1);
			BigDecimal rd_cjs_promP1 = cjs_promP1.setScale(0, RoundingMode.HALF_UP);  
			Double d_cjsAlLlegarP2_entrePrm = (cajasAlLlegarP2 / clc_prom_diario);
			if(d_cjsAlLlegarP2_entrePrm.isInfinite() || d_cjsAlLlegarP2_entrePrm.isNaN()){
				d_cjsAlLlegarP2_entrePrm = 0.0;
			}
			BigDecimal cjs_promP2 = new BigDecimal(d_cjsAlLlegarP2_entrePrm);
			BigDecimal rd_cjs_promP2 = cjs_promP2.setScale(0, RoundingMode.HALF_UP);
			Double d_cjsAlLlegarP3_entrePrm = (cajasAlLlegarP3 / clc_prom_diario);
			if(d_cjsAlLlegarP3_entrePrm.isInfinite() || d_cjsAlLlegarP3_entrePrm.isNaN()){
				d_cjsAlLlegarP3_entrePrm = 0.0;
			}
			BigDecimal cjs_promP3 = new BigDecimal(d_cjsAlLlegarP3_entrePrm);
			BigDecimal rd_cjs_promP3 = cjs_promP3.setScale(0, RoundingMode.HALF_UP);
			
			int i_cjs_promP1 = Integer.valueOf(rd_cjs_promP1.intValueExact());
			int i_cjs_promP2 = Integer.valueOf(rd_cjs_promP2.intValueExact());
			int i_cjs_promP3 = Integer.valueOf(rd_cjs_promP3.intValueExact());
			//System.out.println("Dias --> "+i_diasP1 +"-"+ rd_cjs_promP1);
			//System.out.println("Dias --> "+i_diasP1 +"-"+ i_cjs_promP1);
			//int diaFinS = (i_diasP1 + i_cjs_promP1) / prom_diario;
			int diasFinal = ((i_diasP1 + i_cjs_promP1) - 1);
			//System.out.println("Dia Final Cajas: " + diasFinal);
			DDI_al_llegar_dia_P1.setTime(fLoad);
			DDI_al_llegar_dia_P2.setTime(fLoad);
			DDI_al_llegar_dia_P3.setTime(fLoad);
			
			DDI_al_llegar_dia_P1.add(fecha_hoy.DATE, (i_diasP1 + i_cjs_promP1));
			DDI_al_llegar_dia_P2.add(fecha_hoy.DATE, (i_diasP2 + i_cjs_promP2));
			DDI_al_llegar_dia_P3.add(fecha_hoy.DATE, (i_diasP3 + i_cjs_promP3));
			
			String frmtFechaP1 = dto.getFrmFechaP1();
			String frmtFechaP2 = dto.getFrmFechaP2();
			String frmtFechaP3 = dto.getFrmFechaP3();
			

			Calendar fecha = new GregorianCalendar();
			Calendar prb = new GregorianCalendar();
			Calendar diaFin1 = new GregorianCalendar();
			Calendar diaFin2 = new GregorianCalendar();
			Calendar diaFin3 = new GregorianCalendar();
			diaFin1.setTime(fLoad);
			diaFin2.setTime(fLoad);
			diaFin3.setTime(fLoad);
			System.out.println("DifDias --> "+difDiasP1);
			System.out.println("Dif 1 --> "+formatoDeFecha.format(prb.getTime()));
			diaFin1.add(Calendar.DATE, (int) difDiasP1);
			diaFin2.add(Calendar.DATE, (int) difDiasP2);
			diaFin3.add(Calendar.DATE, (int) difDiasP3);
			System.out.println("Fecha --> "+formatoDeFecha.format(prb.getTime()));
			//System.out.println("Fecha1 ----> "+ difDiasP1 +" - "+ formatoDeFecha.format(diaFin1.getTime()));
			//System.out.println("Fecha2 ----> "+ difDiasP2 +" - "+ formatoDeFecha.format(diaFin2.getTime()));
			//System.out.println("Fecha3 ----> "+ difDiasP3 +" - "+ formatoDeFecha.format(diaFin3.getTime()));
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			
			DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);			
			
			/*html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_diasP1+"</td>"; // # Dias Llegada P1
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(dias_llegada_dia_P1.getTime())+"</td>"; // DDI Pedido Fecha
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_cajasAlLlegarP1+"</td>"; // DDI al Llegar P1
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(DDI_al_llegar_dia_P1.getTime())+"</td>"; // DDI al Llegar Dia
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_contExistMin_1+"</td>"; // Continuidad Existencias al Minimo P1
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_diasP2+"</td>"; // # Dias Llegada P2
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(dias_llegada_dia_P2.getTime())+"</td>"; // DDI Pedido Fecha
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_cajasAlLlegarP2+"</td>"; // DDI al Llegar P2
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(DDI_al_llegar_dia_P2.getTime())+"</td>"; // DDI al Llegar Fecha
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_contExistMin_2+"</td>"; // Continuidad Existencias al Minimo P2
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_diasP3+"</td>"; // # Dias Llegada P3
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(dias_llegada_dia_P3.getTime())+"</td>"; // DDI Pedido Fecha
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_cajasAlLlegarP3+"</td>"; // DDI al Llegar P3
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(DDI_al_llegar_dia_P3.getTime())+"</td>"; // DDI Al Llegar Fecha
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_contExistMin_3+"</td>"; // Continuidad Existencias al Minimo P3
			html += "</tr>"; 
			*/
			
			//Semaforizacion
			String s_tmpMntnrStock = "";
			String s_tmpLvntrPed = "";
			String s_contExistP1 = "";
			String s_contExistP2 = "";
			String s_contExistP3 = "";
			String s_ddiDiasAlLlegar1 = "";
			String s_ddiDiasAlLlegar2 = "";
			String s_ddiDiasAlLlegar3 = "";
			
			String colorRojo =  "#FF0000";
			String colorNaranja = "#FE9A2E";
			String colorNaranjaSuave = "#F5BCA9";
			String colorVerdeClaro  = "#A9F5A9";
			String colorNaranjaClaro = "#F5D0A9";
			
			String rojoFuerte = "#FF6161";
			String rojoMedio  = "#FFB8B7";
			String rojoClaro = "#FFDAC8";
			String verdeClaroCE = "#F0FFC5";

			String color1P1 = "";
			String color2P1 = "";
			String color3P1 = "";
			Double numC1 = 0.0;
			String color1P2 = "";
			String color2P2 = "";
			String color3P2 = "";
			Double numC2 = 0.0;
			String color1P3 = "";
			String color2P3 = "";
			String color3P3 = "";
			Double numC3 = 0.0;
			
			if(difDiasP1 < 0){
				color1P1 = "red";
			}
			if(cajasAlLlegarP1 < 0.0){
				color2P1 = "red";
			}
			numC1 = Double.parseDouble(rd_contExistMin_1.toString());
			//System.out.println("----> "+numC1);
			if(numC1 < 0.0){
				color3P1 = "red";
			}
			
			System.out.println("Dias--> "+difDiasP1);
			if(in_diasP1 == 0.0 && in_cajasP1 == 0.0 && frmtFechaP1 == null || frmtFechaP1 == "null" ){
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P3
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Al Llegar Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P3
			}else{
				
				if(contExistMin_1 <= -2 ){
					s_contExistP1 = rojoFuerte;
					color3P1 = "";
				}
				if(contExistMin_1 > -2 && contExistMin_1 < 0){
					s_contExistP1 = rojoMedio;
				}
				if(contExistMin_1 >= 0 && contExistMin_1 <= 3){
					s_contExistP1 = rojoClaro; // color mas claro
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1P1+">"+difDiasP1+"</td>"; // # Dias Llegada P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(diaFin1.getTime())+"</td>"; // DDI Pedido Fecha
				html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color2P1+">"+frmt.format(rd_cajasAlLlegarP1)+"</td>"; // DDI al Llegar P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P1.getTime())+"</td>"; // DDI al Llegar Dia
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_contExistP1+"'><font color="+color3P1+">"+frmt.format(rd_contExistMin_1)+"</td>"; // Continuidad Existencias al Minimo P1
			}
				if(difDiasP2 < 0){
					color1P2 = "red";
				}
				System.out.println("----> "+cajasAlLlegarP2);
				if(cajasAlLlegarP2 < 0.0){
					System.out.println("CajasAlLlegar 2 --->: "+cajasAlLlegarP2);
					color2P2 = "red";
				}
				numC2 = Double.parseDouble(rd_contExistMin_2.toString());
				
				if(numC2 < 0.0){
					color3P2 = "red";
				}	
			
				if(in_diasP2 == 0.0 && in_cajasP2 == 0.0 && frmtFechaP2 == null || frmtFechaP2 == "null" ){
					System.out.println("InDias----> "+in_diasP2);
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Pedido Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Al Llegar Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P3
				}else{
					
					if(contExistMin_2 <= -2 ){
						s_contExistP2 = rojoFuerte;
						color3P2 = "";
					}
					if(contExistMin_2 > -2 && contExistMin_2 < 0){
						s_contExistP2 = rojoMedio;
					}
					if(contExistMin_2 >= 0 && contExistMin_2 <= 3){
						s_contExistP2 = rojoClaro; // color mas claro
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1P2+">"+difDiasP2+"</td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(diaFin2.getTime())+"</td>"; // DDI Pedido Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color2P2+">"+frmt.format(rd_cajasAlLlegarP2)+"</td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P2.getTime())+"</td>"; // DDI al Llegar Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_contExistP2+"'><font color="+color3P2+">"+frmt.format(rd_contExistMin_2)+"</td>"; // Continuidad Existencias al Minimo P2
				}
				if(difDiasP3 < 0){
					color1P3 = "red";
				}
				if(cajasAlLlegarP3 < 0.0){
					color2P3 = "red";
				}
				numC3 = Double.parseDouble(rd_contExistMin_3.toString());
				//System.out.println("----> "+numC1);
				if(numC3 < 0.0){
					color3P3 = "red";
				}
				System.out.println("InDias3-----> "+in_diasP3);
				System.out.println("InCajas3-----> "+in_cajasP3);
				if(in_diasP3 == 0.0  && in_cajasP3== 0.0 && frmtFechaP3 == null || frmtFechaP3 == "null" ){
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Pedido Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI Al Llegar Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P3
				}else{
					
					if(contExistMin_3 <= -2 ){
						s_contExistP3 = rojoFuerte;
						color3P3 = "";
					}
					if(contExistMin_3 > -2 && contExistMin_3 < 0){
						s_contExistP3 = rojoMedio;
					}
					if(contExistMin_3 >= 0 && contExistMin_3 <= 3){
						s_contExistP3 = rojoClaro; // color mas claro
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color1P3+">"+difDiasP3+"</td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(diaFin3.getTime())+"</td>"; // DDI Pedido Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color="+color2P3+">"+frmt.format(rd_cajasAlLlegarP3)+"</td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P3.getTime())+"</td>"; // DDI Al Llegar Fecha
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_contExistP3+"'><font color="+color3P3+">"+frmt.format(rd_contExistMin_3)+"</td>"; // Continuidad Existencias al Minimo P3
				}
			html +="</tr>";
			/*//Fila con la Marca y los totales por marca
			String marca_ini = "";
			String marca_fin = "";
			String marca = "";
			if(i == 0  && i+1 < row.size()){
				marca_ini = dto.getIdMarca();
				marca_fin = row.get(i+1).getIdMarca();
				if(!marca_ini.equals(marca_fin)){
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double minstock1_mrc = clc_prom_diario_mrc * minstock_mrc;
					Double maxstock1_mrc = clc_prom_diario_mrc * maxstock_mrc; 
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc)*cajasP1 ) /cajasP1 ;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = ((((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / clc_prom_diario_mrc) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = (((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / clc_prom_diario_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = (((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / clc_prom_diario_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double cjsP1alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1) - ((diasP1 + in_diasP1) * clc_prom_diario_mrc))- minstock1_mrc) / ddiP1) * ddiP1; 
					Double cjsP2alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((diasP2 + in_diasP2) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP2) * ddiP2);
					Double cjsP3alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((diasP3 + in_diasP3) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP3) * ddiP3);
					if(cjsP1alLlegar.isInfinite() || cjsP1alLlegar.isNaN()){
						cjsP1alLlegar = 0.0;
					}
					if(cjsP2alLlegar.isInfinite() || cjsP2alLlegar.isNaN()){
						cjsP2alLlegar = 0.0;
					}
					if(cjsP3alLlegar.isInfinite() || cjsP3alLlegar.isNaN()){
						cjsP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(cjsP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(cjsP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(cjsP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}else{
				
				if(i+1 < row.size()){
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i+1).getIdMarca();
					
					if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double minstock1_mrc = clc_prom_diario_mrc * minstock_mrc;
						Double maxstock1_mrc = clc_prom_diario_mrc * maxstock_mrc;
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc)*cajasP1 ) /cajasP1 ;
						Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
							
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = ((((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / clc_prom_diario_mrc) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = (((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / clc_prom_diario_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = (((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / clc_prom_diario_mrc) / ddiP3) * ddiP3;
							
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						Double cjsP1alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1) - ((diasP1 + in_diasP1) * clc_prom_diario_mrc))- minstock1_mrc) / ddiP1) * ddiP1; 
						Double cjsP2alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((diasP2 + in_diasP2) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP2) * ddiP2);
						Double cjsP3alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((diasP3 + in_diasP3) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP3) * ddiP3);
						if(cjsP1alLlegar.isInfinite() || cjsP1alLlegar.isNaN()){
							cjsP1alLlegar = 0.0;
						}
						if(cjsP2alLlegar.isInfinite() || cjsP2alLlegar.isNaN()){
							cjsP2alLlegar = 0.0;
						}
						if(cjsP3alLlegar.isInfinite() || cjsP3alLlegar.isNaN()){
							cjsP3alLlegar = 0.0;
						}
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _ddiP1alLlegar = new BigDecimal(cjsP1alLlegar);
						BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2alLlegar = new BigDecimal(cjsP2alLlegar);
						BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3alLlegar = new BigDecimal(cjsP3alLlegar);
						BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
						BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
						BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
						BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3 = new BigDecimal(ddiP3);
						BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2 = new BigDecimal(ddiP2);
						BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP1 = new BigDecimal(ddiP1);
						BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
						html += "</tr>";
						
					}
				}else{
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i).getIdMarca();
					
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double dias_invent_1_mrc = existencias_ttl_mss / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double minstock1_mrc = clc_prom_diario_mrc * minstock_mrc;
					Double maxstock1_mrc = clc_prom_diario_mrc * maxstock_mrc;
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc; //Tiempo limite para mantener Stock
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) / minstock_mrc)*cajasP1 ) /cajasP1 ;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = ((((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / clc_prom_diario_mrc) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = (((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / clc_prom_diario_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = (((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / clc_prom_diario_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double cjsP1alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1) - ((diasP1 + in_diasP1) * clc_prom_diario_mrc))- minstock1_mrc) / ddiP1) * ddiP1; 
					Double cjsP2alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2) - ((diasP2 + in_diasP2) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP2) * ddiP2);
					Double cjsP3alLlegar = ((((disponible_mrc + cajasP1 + in_cajasP1 + cajasP2 + in_cajasP2 + cajasP3 + in_cajasP3) - ((diasP3 + in_diasP3) * clc_prom_diario_mrc) - minstock1_mrc) / ddiP3) * ddiP3);
					if(cjsP1alLlegar.isInfinite() || cjsP1alLlegar.isNaN()){
						cjsP1alLlegar = 0.0;
					}
					if(cjsP2alLlegar.isInfinite() || cjsP2alLlegar.isNaN()){
						cjsP2alLlegar = 0.0;
					}
					if(cjsP3alLlegar.isInfinite() || cjsP3alLlegar.isNaN()){
						cjsP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_1_mrc = new BigDecimal(dias_invent_1_mrc);
					BigDecimal rd_dias_invent_1_mrc = _dias_invent_1_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(cjsP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(cjsP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(cjsP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}*/
		}
		
		html += "</tbody>";
		html += "</table> "+ toltip_meses;
		
		System.out.println("Tabla... ");
		return html;
	}
	
	// Analisis Compras Visto Por Dias (Simulacion)
	public String getTableAnalisisComprasSim(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String mes) throws UnsupportedEncodingException, ParseException {
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		String html = "";
		boolean org_param = false;

		Double in_diasP1 = 0.0;
		Double in_diasP2 = 0.0;
		Double in_diasP3 = 0.0;
		Double in_cajasP1 = 0.0;
		Double in_cajasP2 = 0.0;
		Double in_cajasP3 = 0.0;
		
		
		int meses_enc = 4;
		int meses_col = 4;
		int meses_fil = 4;
		int meses_ttl = 4;
		int meses_marca = 4;
		if(mes == null || mes == "" || mes.equals("null")){
		}else{
			meses_enc = Integer.parseInt(mes);
			meses_col = Integer.parseInt(mes);
			meses_fil = Integer.parseInt(mes);
			meses_ttl = Integer.parseInt(mes);
		}
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td colspan=2></td>";
		html += "<td align=center colspan=5><strong>Pedido 1</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 2</strong></td>";
		html += "<td align=center colspan=5><strong>Pedido 3</strong></td>";
		html += "</tr>";
		
		html += "<tr><td colspan=2></td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > DDI Pedido</td>";
		html += "<td colspan=2>DDI al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > DDI Pedido</td>";
		html += "<td colspan=2>DDI al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "<td colspan=2># D&iacute;as Llegada</td>";
		//html += "<td > DDI Pedido</td>";
		html += "<td colspan=2>DDI al Llegar</td>";
		html += "<td >Continuidad Existencias Al M&iacute;nimo</td>";
		html += "</tr>";
		
		html += "<tr>";
		html += "<th></th>";
		html += "<th></th>";
		//html += "<th>DIAS</th>";
		//html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>FECHA</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIA</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>Fecha</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIA</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIAS</th>";
		html += "<th>Fecha</th>";
		html += "<th>DIAS</th>";
		html += "<th>DIA</th>";
		html += "<th>DIAS</th>";
		html += "</tr>";
		
		html += "</thead>";
		html +="<tbody >";

		
		Calendar m1_Ini = new GregorianCalendar();
		Calendar m1_Fin = new GregorianCalendar();
		Calendar m2_Ini = new GregorianCalendar();
		Calendar m2_Fin = new GregorianCalendar();
		Calendar m3_Ini = new GregorianCalendar();
		Calendar m3_Fin = new GregorianCalendar();
		Calendar m4_Ini = new GregorianCalendar();
		Calendar m4_Fin = new GregorianCalendar();
		Calendar hoy = new GregorianCalendar();
		 //Formato para fecha
	    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		    
	    //Tooltips para el rango de los meses 
	    String toltip_meses = "";
	    int rango_mes = 28;
	    Calendar fecha_hoy = new GregorianCalendar();
	    Calendar fechaF1 =  new GregorianCalendar();
	    Calendar fechaF2 =  new GregorianCalendar();
	    Calendar fechaF3 =  new GregorianCalendar();
	    
	    String fechaL = invent.obtieneFechaCargaDatos();
	    System.out.println("Fecha De Carga--->"+fechaL);
	    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date fLoad = null;
		
	    
		List<InvPrevComp> row = invent.getDataPrevCompras(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id);
		HashMap hm_meses = invent.getDataMesesPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id, null);
		//HashMap ttl_marcas = invent.getDataMesesTtlPrevCompras(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id);
		HashMap hm_prod_sim = invent.getDataProdSimulacion(cust_id, id_user, id_modulo, id_dashboard, chart_id);
		
		Calendar dias_llegada_dia_P1 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P2 = new GregorianCalendar();
	    Calendar dias_llegada_dia_P3 =  new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P1 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P2 = new GregorianCalendar();
	    Calendar DDI_al_llegar_dia_P3 =  new GregorianCalendar();
		for(int i = 0; i < row.size(); i ++){
			
			if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
				fLoad =  formatoF2.parse(fechaL);
				hoy.setTime(fLoad);
			}else{
				hoy.add(Calendar.DATE , -1);
			}
		    
			fecha_hoy = new GregorianCalendar();
			dias_llegada_dia_P1 = new GregorianCalendar();
		    dias_llegada_dia_P2 = new GregorianCalendar();
		    dias_llegada_dia_P3 =  new GregorianCalendar();
		    DDI_al_llegar_dia_P1 = new GregorianCalendar();
		    DDI_al_llegar_dia_P2 = new GregorianCalendar();
		    DDI_al_llegar_dia_P3 =  new GregorianCalendar();
			InvPrevComp dto = row.get(i);
			if(mes == null || mes == "" || mes.equals("null")){
				meses_fil = 4;
				meses_ttl = 4;
				meses_marca = 4; 
			}else{
				meses_fil = Integer.parseInt(mes);
				meses_ttl = Integer.parseInt(mes);
				meses_marca = Integer.parseInt(mes);
			}
			Calendar cal = new GregorianCalendar();
			DecimalFormat formatter = new DecimalFormat("###,###,##0.000");
			String mes_prb = (String) hm_meses.get(dto.getIdProd()+"_ttl");
			
			Double ttl_cajas = 0.0;
			//System.out.println("Null "+mes_prb);
			if(mes_prb != null){
				ttl_cajas = Double.parseDouble(mes_prb);
			}
			int perd_dias = meses_fil * 28;
					
			BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
			BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
			
			
			Double clc_prom_diario = ttl_cajas / perd_dias;
			System.out.println("Promedio -> "+clc_prom_diario);
			BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
			BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
			Double existencia = Double.parseDouble(dto.getTtlExist());
			System.out.println("Existencias -> "+existencia);
			BigDecimal bd_existencia = new BigDecimal(existencia);
			BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
			
			
			Double pendiente_x_fact = Double.parseDouble(dto.getPndXFact());
			BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
			BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
			Double disponible = existencia - pendiente_x_fact;
			System.out.println("Disponible -> "+disponible);
			Double dias_invent = existencia / clc_prom_diario;
			System.out.println("DianInvent1 -> "+ dias_invent);
			BigDecimal bd_disponible = new BigDecimal(disponible);
			BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
			
			Double dia_inventario = disponible / clc_prom_diario;
			System.out.println("Diainvent2 -> "+dia_inventario);
			Double costo = Double.parseDouble(dto.getCosto());
			Double costo_final = disponible * costo;
			int dia_invent_fin = (int) Math.round(dia_inventario - 1);
			
			BigDecimal bd_costo_final = new BigDecimal(costo_final);
			BigDecimal rd_costo_final = bd_costo_final.setScale(1, RoundingMode.HALF_UP);
			cal.add(Calendar.DATE, dia_invent_fin);
			
			
			//Link en producto, el cual abrira el simulador
			String nomProducto = "";
			String ruta = "../popup_simulacion.jsp";
			
			if(chart_id.equals("7")){
				nomProducto = "<a href='#' onclick=abrePop('"+ruta+"');><strong> +</strong></a>";
			}else{
				nomProducto = "";
			}
			
			String id_prod = dto.getIdProd();
			String prod_sim = (String) hm_prod_sim.get(id_prod+"_sim");
			String m_prod_sim = "";
			String del_sim ="";
			String tr_sim= " bgcolor=#FFFFFF";
			//System.out.println("idProd-->"+prod_sim);
			if(prod_sim != null ){
				m_prod_sim = "<font  color = red>";
				tr_sim = " bgcolor= #BDBDBD ";
				del_sim = "<a href='#' onclick=eliminaSimulacion('"+dto.getIdProd()+"');><img src='../../img/close.gif'></a>";
			}
			
			html += "<tr class=mrc_"+dto.getIdMarca() + tr_sim+" >";
			html += "<td align='right' valign='top'>"+dto.getCodeProd()+"</td>"; //Marca
			html += "<td align='right' valign='top'>"+dto.getDescProd()+nomProducto+" "+del_sim+"</td>"; //Producto
			//Genera los periodos a mostrar
			for (int x = 1; x <= meses_fil; meses_fil --){
				String prb_ttl_x_mes = (String) hm_meses.get(dto.getIdProd()+"_"+meses_fil);
				Double ttl_cajas_x_mes = 0.0;
				if(prb_ttl_x_mes != null){
					ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
				}
				//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
				BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
				BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_ttl_cajas_mes+"</td>"; //Mes4
			}

			Double tmpo_mtnr_stock = 0.0;
			Double min_stock = Double.parseDouble(dto.getMinStock());
			Double tiempo_prov = Double.parseDouble(dto.getTiempoProv());
			tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
			
			double diasP1 = Double.parseDouble(dto.getDiasP1());
			double diasP2 = Double.parseDouble(dto.getDiasP2());
			double diasP3 = Double.parseDouble(dto.getDiasP3());
			double cajasP1 = Double.parseDouble(dto.getCajasP1());
			double cajasP2 = Double.parseDouble(dto.getCajasP2());
			double cajasP3 = Double.parseDouble(dto.getCajasP3());
			
			Double tmpo_lvtr_ped = ((((diasP1 + diasP2 + diasP3 + dia_inventario) - min_stock ) * cajasP1 ) / cajasP1) - tiempo_prov;
			
			//Calculo #Dias De Llegada
			Date fechaP1 = (Date) dto.getFechaP1();
			Date fechaP2 = (Date) dto.getFechaP2();
			Date fechaP3 = (Date) dto.getFechaP3();
			System.out.println("Fechas ->"+fechaP1.getTime());
			//Calculo de diferencia entre fechas
			//Fecha inicial
			Calendar inicio1 =  new GregorianCalendar();
			Calendar inicio2 =  new GregorianCalendar();
			Calendar inicio3 =  new GregorianCalendar();
			
			Calendar finP1 =  new GregorianCalendar();
			Calendar finP2 =  new GregorianCalendar();
			Calendar finP3 =  new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			inicio1.setTime(fLoad);
			inicio2.setTime(fLoad);
			inicio3.setTime(fLoad);
			long ini, fin1, fin2, fin3, difP1, difP2, difP3, difDiasP1 = 0, difDiasP2 = 0, difDiasP3 = 0;
			
			
			if(fechaP1 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha1 = sdf.format(fechaP1.getTime());
				String frmInicio = sdf.format(inicio1.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha1);
				
				finP1.setTime(fFechaF);
				inicio1.setTime(fFechaI);
				
				difP1 = finP1.getTimeInMillis() - inicio1.getTimeInMillis() ;
				
				difDiasP1 = difP1/(24 * 60 * 60 * 1000);
							
			}
			//System.out.println("DiasPAraQueLlegue: "+difDiasP1);
			//System.out.println("DiasIntroducir: "+in_diasP1);
			if(fechaP2 != null){
				
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha2 = sdf.format(fechaP2.getTime());
				String frmInicio = sdf.format(inicio2.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha2);
				
				finP2.setTime(fFechaF);
				inicio2.setTime(fFechaI);
				
				difP2 = finP2.getTimeInMillis() - inicio2.getTimeInMillis() ;
				
				difDiasP2 = difP2/(24 * 60 * 60 * 1000);
			}
			if(fechaP3 != null){
				java.util.Date fFechaI = null;
				java.util.Date fFechaF = null;
				
				String frmFecha3 = sdf.format(fechaP3.getTime());
				String frmInicio = sdf.format(inicio3.getTime());
				
				fFechaI = sdf.parse(frmInicio);
				fFechaF = sdf.parse(frmFecha3);
				
				finP3.setTime(fFechaF);
				inicio3.setTime(fFechaI);
				
				difP3 = finP3.getTimeInMillis() - inicio3.getTimeInMillis() ;
				
				difDiasP3 = difP3/(24 * 60 * 60 * 1000);
			}
			
			
			
			Double contExistMin_1 = 0.0;
			Double contExistMin_2 = 0.0;
			Double contExistMin_3 = 0.0;
			
			Double ddiDiasAlLlegarP1 = 0.0;
			Double ddiDiasAlLlegarP2 = 0.0;
			Double ddiDiasAlLlegarP3 = 0.0;
			
			Double ddiPedDiasP1 = 0.0;
			Double ddiPedDiasP2 = 0.0;
			Double ddiPedDiasP3 = 0.0;
			
			//Cajas y dias introducidos oara la smulacion
			//String id_prod = dto.getIdProd();
			String prev_diasP1 = (String) hm_prod_sim.get(id_prod+"_diasP1");
			String prev_diasP2 = (String) hm_prod_sim.get(id_prod+"_diasP2");
			String prev_diasP3 = (String) hm_prod_sim.get(id_prod+"_diasP3");
			
			String prev_cajasP1 = (String) hm_prod_sim.get(id_prod+"_cajasP1");
			String prev_cajasP2 = (String) hm_prod_sim.get(id_prod+"_cajasP2");
			String prev_cajasP3 = (String) hm_prod_sim.get(id_prod+"_cajasP3");
			
			if(prev_diasP1 == null || prev_diasP1.equals("null")){
				prev_diasP1 = "0";
			}
			if(prev_diasP2 == null || prev_diasP2.equals("null")){
				prev_diasP2 = "0";
			}
			if(prev_diasP3 == null || prev_diasP3.equals("null")){
				prev_diasP3 = "0";
			}
			
			if(prev_cajasP1 == null || prev_diasP1.equals("null") || prev_cajasP1.isEmpty()){
				prev_cajasP1 = "0";
			}
			if(prev_cajasP2 == null || prev_diasP2.equals("null") || prev_cajasP2.isEmpty()){
				prev_cajasP2 = "0";
			}
			if(prev_cajasP3 == null || prev_diasP3.equals("null") || prev_cajasP3.isEmpty()){
				prev_cajasP3 = "0";
			}
			
			in_diasP1 = Double.parseDouble(prev_diasP1);
			in_diasP2 = Double.parseDouble(prev_diasP2);
			in_diasP3 = Double.parseDouble(prev_diasP3);
			
			difDiasP1 = difDiasP1 + in_diasP1.longValue();
			difDiasP2 = difDiasP2 + in_diasP2.longValue();
			difDiasP3 = difDiasP3 + in_diasP3.longValue();
			
			in_cajasP1 = Double.parseDouble(prev_cajasP1);
			in_cajasP2 = Double.parseDouble(prev_cajasP2);
			in_cajasP3 = Double.parseDouble(prev_cajasP3);
			
			if(in_diasP1 == null || in_diasP1.equals("null") ){
				in_diasP1 = 0.0;
			}
			if(in_diasP2 == null || in_diasP2.equals("null")){
				in_diasP2 = 0.0;
			}
			if(in_diasP3 == null || in_diasP3.equals("null")){
				in_diasP3 = 0.0;
			}
			
			ddiPedDiasP1 = (cajasP1 + in_cajasP1) / clc_prom_diario;
			//System.out.println("DDIPedDiasP1: "+"("+cajasP1 +"+"+ in_cajasP1+") /"+ clc_prom_diario+" = "+ddiPedDiasP1);
			ddiDiasAlLlegarP1 = ((((dia_inventario - (difDiasP1 + in_diasP1) + ddiPedDiasP1) - min_stock) / ddiPedDiasP1) * ddiPedDiasP1);
			contExistMin_1 = (((dia_inventario - (difDiasP1 + in_diasP1)- min_stock))/ddiDiasAlLlegarP1) * ddiDiasAlLlegarP1;
			if(ddiPedDiasP1.isInfinite() || ddiPedDiasP1.isNaN()){
				ddiPedDiasP1 = 0.0;
			}
			//System.out.println("("+cajasP1 +"+"+ in_cajasP1+") /"+ clc_prom_diario);
			System.out.println("(((("+dia_inventario +"- ("+difDiasP1 +"+"+ in_diasP1+") + "+ddiPedDiasP1+") - "+min_stock+") / "+ddiPedDiasP1+") * "+ddiPedDiasP1+") =  "+ddiDiasAlLlegarP1);
			BigDecimal _ddiPedDiasP1 = new BigDecimal(ddiPedDiasP1);
			BigDecimal rd_ddiPedDiasP1 = _ddiPedDiasP1.setScale(1, RoundingMode.HALF_UP);
			if(ddiDiasAlLlegarP1.isInfinite() || ddiDiasAlLlegarP1.isNaN()){
				ddiDiasAlLlegarP1 = 0.0;
			}
			BigDecimal _ddiDiasP1 = new BigDecimal(ddiDiasAlLlegarP1);
			BigDecimal rd_ddiDiasP1 = _ddiDiasP1.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_1.isInfinite() || contExistMin_1.isNaN()){
				contExistMin_1 = 0.0;
			}
			BigDecimal _contExistMin_1 = new BigDecimal(contExistMin_1);
			BigDecimal rd_contExistMin_1 = _contExistMin_1.setScale(1, RoundingMode.HALF_UP);
			
			ddiPedDiasP2 = (cajasP2 + in_cajasP2) / clc_prom_diario;
			ddiDiasAlLlegarP2 = ((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP2 + in_diasP2) - min_stock) / ddiPedDiasP2) * ddiPedDiasP2);
			contExistMin_2 = ((((dia_inventario + ddiPedDiasP1) - (difDiasP2 + in_diasP2)- min_stock))/ddiDiasAlLlegarP2) * ddiDiasAlLlegarP2;
			if(ddiPedDiasP2.isInfinite() || ddiPedDiasP2.isNaN()){
				ddiPedDiasP2 = 0.0;
			}
			BigDecimal _ddiPedDiasP2 = new BigDecimal(ddiPedDiasP2);
			BigDecimal rd_ddiPedDiasP2 = _ddiPedDiasP2.setScale(1, RoundingMode.HALF_UP);
			if(ddiDiasAlLlegarP2.isInfinite() || ddiDiasAlLlegarP2.isNaN()){
				ddiDiasAlLlegarP2 = 0.0;
			}
			BigDecimal _ddiDiasP2 = new BigDecimal(ddiDiasAlLlegarP2);
			BigDecimal rd_ddiDiasP2 = _ddiDiasP2.setScale(0, RoundingMode.HALF_UP);
			if(contExistMin_2.isInfinite() || contExistMin_2.isNaN()){
				contExistMin_2 = 0.0;
			}
			BigDecimal _contExistMin_2 = new BigDecimal(contExistMin_2);
			BigDecimal rd_contExistMin_2 = _contExistMin_2.setScale(1, RoundingMode.HALF_UP);
			
			
			ddiPedDiasP3 = (cajasP3 + in_cajasP3) / clc_prom_diario;
			ddiDiasAlLlegarP3 = ((((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3)) + ddiPedDiasP3) - min_stock) / ddiPedDiasP3) * ddiPedDiasP3);
			contExistMin_3 = ((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3) - min_stock))/ ddiDiasAlLlegarP3) * ddiDiasAlLlegarP3;
			if(ddiPedDiasP3.isInfinite() || ddiPedDiasP3.isNaN()){
				ddiPedDiasP3 = 0.0;
			}
			BigDecimal _ddiPedDiasP3 = new BigDecimal(ddiPedDiasP3);
			BigDecimal rd_ddiPedDiasP3 = _ddiPedDiasP3.setScale(1, RoundingMode.HALF_UP);
			if(ddiDiasAlLlegarP3.isInfinite() || ddiDiasAlLlegarP3.isNaN()){
				ddiDiasAlLlegarP3 = 0.0;
			}
			BigDecimal _ddiDiasP3 = new BigDecimal(ddiDiasAlLlegarP3);
			BigDecimal rd_ddiDiasP3 = _ddiDiasP3.setScale(0, RoundingMode.HALF_UP);   
			if(contExistMin_3.isInfinite() || contExistMin_3.isNaN()){
				contExistMin_3 = 0.0;
			}
			BigDecimal _contExistMin_3 = new BigDecimal(contExistMin_3);
			BigDecimal rd_contExistMin_3 = _contExistMin_3.setScale(1, RoundingMode.HALF_UP);
			
			int i_diasP1 = (int) difDiasP1;
			int i_diasP2 = (int) difDiasP2;
			int i_diasP3 = (int) difDiasP3;
			dias_llegada_dia_P1.add(fecha_hoy.DATE, i_diasP1 - 1);
			dias_llegada_dia_P2.add(fecha_hoy.DATE, i_diasP2 - 1);
			dias_llegada_dia_P3.add(fecha_hoy.DATE, i_diasP3 - 1);
			int i_ddiAlLlegarP1 = Integer.valueOf(rd_ddiDiasP1.intValueExact());
			int i_ddiAlLlegarP2 = Integer.valueOf(rd_ddiDiasP2.intValueExact());
			int i_ddiAlLlegarP3 = Integer.valueOf(rd_ddiDiasP3.intValueExact());
			int diaFinal = (i_diasP1 + i_ddiAlLlegarP1) - 1;
			
			DDI_al_llegar_dia_P1.setTime(fLoad);
			DDI_al_llegar_dia_P2.setTime(fLoad);
			DDI_al_llegar_dia_P3.setTime(fLoad);
			
			DDI_al_llegar_dia_P1.add(Calendar.DATE, (i_diasP1 + i_ddiAlLlegarP1));
			DDI_al_llegar_dia_P2.add(Calendar.DATE, (i_diasP2 + i_ddiAlLlegarP2));
			DDI_al_llegar_dia_P3.add(Calendar.DATE, (i_diasP3 + i_ddiAlLlegarP3));
			
			String frmtFechaP1 = dto.getFrmFechaP1();
			String frmtFechaP2 = dto.getFrmFechaP2();
			String frmtFechaP3 = dto.getFrmFechaP3();
			
			
			Calendar fecha = new GregorianCalendar();
			Calendar diaFin1 = new GregorianCalendar();
			Calendar diaFin2 = new GregorianCalendar();
			Calendar diaFin3 = new GregorianCalendar();
			
			diaFin1.setTime(fLoad);
			diaFin2.setTime(fLoad);
			diaFin3.setTime(fLoad);
			
			diaFin1.add(Calendar.DATE, (int) difDiasP1);
			diaFin2.add(Calendar.DATE, (int) difDiasP2);
			diaFin3.add(Calendar.DATE, (int) difDiasP3);
			
			System.out.println("Fecha1 ----> "+ difDiasP1 +" - "+ formatoDeFecha.format(diaFin1.getTime()));
			System.out.println("Fecha2 ----> "+ difDiasP2 +" - "+ formatoDeFecha.format(diaFin2.getTime()));
			System.out.println("Fecha3 ----> "+ difDiasP3 +" - "+ formatoDeFecha.format(diaFin3.getTime()));
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
			
			/*
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_diasP1+"</td>"; // # Dias Llegada P1
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(dias_llegada_dia_P1.getTime())+"</td>"; // # Dias Llegada P1 (fecha)
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_ddiAlLlegarP1+"</td>"; // DDI al Llegar P1
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(DDI_al_llegar_dia_P1.getTime())+"</td>"; // DDI al llegar (dia - fecha)
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_contExistMin_1+"</td>"; // Continuidad Existencias al Minimo P1
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_diasP2+"</td>"; // # Dias Llegada P2
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(dias_llegada_dia_P2.getTime())+"</td>"; // # Dias Llegada P1 (fecha)
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_ddiAlLlegarP2+"</td>"; // DDI al Llegar P2
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(DDI_al_llegar_dia_P2.getTime())+"</td>"; // DDI al llegar (dia - fecha)
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_contExistMin_2+"</td>"; // Continuidad Existencias al Minimo P2
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_diasP3+"</td>"; // # Dias Llegada P3
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(dias_llegada_dia_P3.getTime())+"</td>"; // # Dias Llegada P1 (fecha)
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+i_ddiAlLlegarP3+"</td>"; // DDI al Llegar P3
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+formatoDeFecha.format(DDI_al_llegar_dia_P3.getTime())+"</td>"; // DDI al llegar (dia - fecha))
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+m_prod_sim+rd_contExistMin_3+"</td>"; // Continuidad Existencias al Minimo P3
			*/
			
			
			
			String color1P1 = "";
			String color2P1 = "";
			String color3P1 = "";
			Double numC1 = 0.0;
			String color1P2 = "";
			String color2P2 = "";
			String color3P2 = "";
			Double numC2 = 0.0;
			String color1P3 = "";
			String color2P3 = "";
			String color3P3 = "";
			Double numC3 = 0.0;
			
			
			if(difDiasP1 < 0){
				color1P1 = "red";
			}
			if(i_ddiAlLlegarP1 < 0){
				color2P1 = "red";
			}
			numC1 = Double.parseDouble(rd_contExistMin_1.toString());
			System.out.println("----> "+numC1);
			if(numC1 < 0.0){
				color3P1 = "red";
			}
			fechaF1  = new GregorianCalendar();
			fechaF1.setTime(fLoad);
			
			//Semaforizacion
			String s_tmpMntnrStock = "";
			String s_tmpLvntrPed = "";
			String s_contExistP1 = "";
			String s_contExistP2 = "";
			String s_contExistP3 = "";
			String s_ddiDiasAlLlegar1 = "";
			String s_ddiDiasAlLlegar2 = "";
			String s_ddiDiasAlLlegar3 = "";
			
			String colorRojo =  "#FF0000";
			String colorNaranja = "#FE9A2E";
			String colorNaranjaSuave = "#F5BCA9";
			String colorVerdeClaro  = "#A9F5A9";
			String colorNaranjaClaro = "#F5D0A9";
			
			String amarilloClaro = "#FFFFCC";
			String amarilloObscuro = "#FFFF99";
			String verdeClaro  = "#EBFFB1";
			String verdeObscuro = "#C6E6A2";
						
			String rojoFuerte = "#FF6161";
			String rojoMedio  = "#FFB8B7";
			String rojoClaro = "#FFDAC8";
			String verdeClaroCE = "#F0FFC5";
			
			Double max_stock = Double.parseDouble(dto.getMaxStock());
			
			fechaF1.add(Calendar.DATE, (int)Math.round(contExistMin_1));
			System.out.println("FechaF1 "+formatoDeFecha.format(fechaF1.getTime())+"cDias: "+Math.round(contExistMin_1));
			toltip_meses += "<div id=msgtoolTipP1"+dto.getIdProd()+" style='display: none;' class=tip >"+ formatoDeFecha.format(fechaF1.getTime()) +"</div>";
			//if(in_diasP1 != 0){ Casilla en cero a excepcion de que sea el producto de una  operacion
			if(in_diasP1 == 0.0 && in_cajasP1 == 0.0 && frmtFechaP1 == null || frmtFechaP1 == "null" ){
				System.out.println("1---------->------------->"+in_diasP1);
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al llegar (dia - fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P1
				
			}else{
				
				if(ddiDiasAlLlegarP1 < 0){
					s_ddiDiasAlLlegar1 = amarilloObscuro;
				}
				if(ddiDiasAlLlegarP1 >= 0 && ddiDiasAlLlegarP1 <= ( max_stock * 0.5)){
					s_ddiDiasAlLlegar1 = amarilloClaro;
				}
				if(ddiDiasAlLlegarP1 >= (max_stock * 0.08) && ddiDiasAlLlegarP1 <= (max_stock * 1.2)){
					s_ddiDiasAlLlegar1 = verdeClaro;
				}
				if(ddiDiasAlLlegarP1 >= (max_stock * 1.2001) && ddiDiasAlLlegarP1 <= (max_stock * 300)){
					s_ddiDiasAlLlegar1 = verdeObscuro;
				}
				
				if(contExistMin_1 <= -2 ){
					s_contExistP1 = rojoFuerte;
					color3P1  = "";
				}
				if(contExistMin_1 > -2 && contExistMin_1 < 0){
					s_contExistP1 = rojoMedio;
				}
				if(contExistMin_1 >= 0 && contExistMin_1 <= 3){
					s_contExistP1 = rojoClaro; // color mas claro
				}
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top' ><font color="+color1P1+">"+difDiasP1+"</td>"; // # Dias Llegada P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top' >"+formatoDeFecha.format(diaFin1.getTime())+"</td>"; // # Dias Llegada P1 (fecha)
				html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_ddiDiasAlLlegar1+"'><font color="+color2P1+">"+i_ddiAlLlegarP1+"</td>"; // DDI al Llegar P1
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P1.getTime())+"</td>"; // DDI al llegar (dia - fecha)
				html += "<td id=toolTipP1"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+s_contExistP1+"'><font color="+color3P1+">"+frmt.format(rd_contExistMin_1)+"</td>"; // Continuidad Existencias al Minimo P1
			}	
				if(difDiasP2 < 0){
					color1P2 = "red";
				}
				if(i_ddiAlLlegarP2 < 0){
					color2P2 = "red";
				}
				numC2 = Double.parseDouble(rd_contExistMin_2.toString());
				//System.out.println("----> "+rd_contExistMin_2);
				if(numC2 < 0.0){
					color3P2 = "red";
				}
				fechaF2  = new GregorianCalendar();
				fechaF2.setTime(fLoad);
				
				fechaF2.add(Calendar.DATE, (int)Math.round(contExistMin_2));
				System.out.println("FechaF2 "+formatoDeFecha.format(fechaF2.getTime())+"cDias: "+Math.round(contExistMin_2));
				toltip_meses += "<div id=msgtoolTipP2"+dto.getIdProd()+" style='display: none;' class=tip >"+ formatoDeFecha.format(fechaF2.getTime()) +"</div>";
				
				if(in_diasP2 == 0.0 && in_cajasP2 == 0.0 && frmtFechaP2 == null || frmtFechaP2 == "null" ){
					System.out.println("2---------->------------->"+in_diasP2);
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1 (fecha)
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al llegar (dia - fecha)
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P1
					
				}else{
					
					if(ddiDiasAlLlegarP2 < 0){
						s_ddiDiasAlLlegar2 = amarilloObscuro;
					}
					if(ddiDiasAlLlegarP2 >= 0 && ddiDiasAlLlegarP2 <= ( max_stock * 0.5)){
						s_ddiDiasAlLlegar2 = amarilloClaro;
					}
					if(ddiDiasAlLlegarP2 >= (max_stock * 0.08) && ddiDiasAlLlegarP2 <= (max_stock * 1.2)){
						s_ddiDiasAlLlegar2 = verdeClaro;
					}
					if(ddiDiasAlLlegarP2 >= (max_stock * 1.2001) && ddiDiasAlLlegarP2 <= (max_stock * 300)){
						s_ddiDiasAlLlegar2 = verdeObscuro;
					}
					
					if(contExistMin_2 <= -2 ){
						s_contExistP2 = rojoFuerte;
						color3P2 = "";
					}
					if(contExistMin_2 > -2 && contExistMin_2 < 0){
						s_contExistP2 = rojoMedio;
					}
					if(contExistMin_2 >= 0 && contExistMin_2 <= 3){
						s_contExistP2 = rojoClaro; // color mas claro
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top' ><font color="+color1P2+">"+difDiasP2+"</td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(diaFin2.getTime())+"</td>"; // # Dias Llegada P1 (fecha)
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_ddiDiasAlLlegar2+"'><font color="+color2P2+">"+i_ddiAlLlegarP2+"</td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P2.getTime())+"</td>"; // DDI al llegar (dia - fecha)
					html += "<td id=toolTipP2"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+s_contExistP2+"'><font color="+color3P2+">"+frmt.format(numC2)+"</td>"; // Continuidad Existencias al Minimo P2
				}
				if(difDiasP3 < 0){
					color1P3 = "red";
				}
				if(i_ddiAlLlegarP3 < 0){
					color2P3 = "red";
				}
				numC3 = Double.parseDouble(rd_contExistMin_3.toString());
				System.out.println("----> "+numC3);
				if(numC3 < 0.0){
					color3P3 = "red";
				}
				fechaF3  = new GregorianCalendar();
				fechaF3.setTime(fLoad);
				
				fechaF3.add(Calendar.DATE, (int)Math.round(contExistMin_3));
				System.out.println("FechaF3 "+formatoDeFecha.format(fechaF3.getTime())+"cDias: "+Math.round(contExistMin_3));
				toltip_meses += "<div id=msgtoolTipP3"+dto.getIdProd()+" style='display: none;' class=tip >"+ formatoDeFecha.format(fechaF3.getTime()) +"</div>";
				
				if(in_diasP3 == 0.0 && in_cajasP3 == 0.0 && frmtFechaP3 == null || frmtFechaP3 == "null" ){
					System.out.println("3---------->------------->"+in_diasP3);
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // # Dias Llegada P1 (fecha)
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // DDI al llegar (dia - fecha)
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Continuidad Existencias al Minimo P1
					
				}else{
					
					if(ddiDiasAlLlegarP3 < 0){
						s_ddiDiasAlLlegar3 = amarilloObscuro;
					}
					if(ddiDiasAlLlegarP3 >= 0 && ddiDiasAlLlegarP3 <= ( max_stock * 0.5)){
						s_ddiDiasAlLlegar3 = amarilloClaro;
					}
					if(ddiDiasAlLlegarP3 >= (max_stock * 0.08) && ddiDiasAlLlegarP3 <= (max_stock * 1.2)){
						s_ddiDiasAlLlegar3 = verdeClaro;
					}
					if(ddiDiasAlLlegarP3 >= (max_stock * 1.2001) && ddiDiasAlLlegarP3 <= (max_stock * 300)){
						s_ddiDiasAlLlegar3 = verdeObscuro;
					}
					
					if(contExistMin_3 <= -2 ){
						s_contExistP1 = rojoFuerte;
						color3P3 = "";
					}
					if(contExistMin_3 > -2 && contExistMin_3 < 0){
						s_contExistP3 = rojoMedio;
					}
					if(contExistMin_3 >= 0 && contExistMin_3 <= 3){
						s_contExistP3 = rojoClaro; // color mas claro
					}
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top' ><font color="+color1P3+">"+difDiasP3+"</td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(diaFin3.getTime())+"</td>"; // # Dias Llegada P1 (fecha)
					html += "<td class=tablrWidget_headerCell align='right' valign='top' bgcolor = '"+s_ddiDiasAlLlegar3+"'><font color="+color2P3+">"+i_ddiAlLlegarP3+"</td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+formatoDeFecha.format(DDI_al_llegar_dia_P3.getTime())+"</td>"; // DDI al llegar (dia - fecha))
					html += "<td id=toolTipP3"+dto.getIdProd()+" class=toolTip align='right' valign='top' bgcolor = '"+s_contExistP3+"'><font color="+color3P3+">"+frmt.format(rd_contExistMin_3)+"</td>"; // Continuidad Existencias al Minimo P3
				}
			
			html += "</tr>"; 
			
			/*//Fila con la Marca y los totales por marca
			String marca_ini = "";
			String marca_fin = "";
			String marca = "";
			if(i == 0  && i+1 < row.size()){
				marca_ini = dto.getIdMarca();
				marca_fin = row.get(i+1).getIdMarca();
				if(!marca_ini.equals(marca_fin)){
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) - minstock_mrc) * cajasP1) / cajasP1;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double ddiP1alLlegar = (((dias_invent_mrc - (diasP1 + in_diasP1) + ddiP1) - minstock_mrc)/ddiP1)*ddiP1; 
					Double ddiP2alLlegar = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double ddiP3alLlegar = ((((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) + ddiP3) - minstock_mrc) / ddiP3) * ddiP3);
					if(ddiP1alLlegar.isInfinite() || ddiP1alLlegar.isNaN()){
						ddiP1alLlegar = 0.0;
					}
					if(ddiP2alLlegar.isInfinite() || ddiP2alLlegar.isNaN()){
						ddiP2alLlegar = 0.0;
					}
					if(ddiP3alLlegar.isInfinite() || ddiP3alLlegar.isNaN()){
						ddiP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(ddiP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(ddiP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(ddiP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}else{
				
				if(i+1 < row.size()){
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i+1).getIdMarca();
					
					if(!marca_ini.equals(marca_fin)){
						Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
						Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
						
						Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
						Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
						Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
						
						Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
						Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
						Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
						Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
						Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc;
						
						///Tiempo Limite Para Levantar Pedido
						Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
						Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
						Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
						Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
						Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
						Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
						
						Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
						Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
						Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
						
						Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) - minstock_mrc) * cajasP1) / cajasP1;
						Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
							
						//Continuidad Existencias al Minimi P1
						Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
						Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
							
						if(cont_exist_min_p1.isNaN()){
							cont_exist_min_p1 = 0.0;
						}
						if(cont_exist_min_p2.isNaN()){
							cont_exist_min_p2 = 0.0;
						}
						if(cont_exist_min_p3.isNaN()){
							cont_exist_min_p3 = 0.0;
						}
						
						Double ddiP1alLlegar = (((dias_invent_mrc - (diasP1 + in_diasP1) + ddiP1) - minstock_mrc)/ddiP1)*ddiP1; 
						Double ddiP2alLlegar = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
						Double ddiP3alLlegar = ((((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) + ddiP3) - minstock_mrc) / ddiP3) * ddiP3);
						if(ddiP1alLlegar.isInfinite() || ddiP1alLlegar.isNaN()){
							ddiP1alLlegar = 0.0;
						}
						if(ddiP2alLlegar.isInfinite() || ddiP2alLlegar.isNaN()){
							ddiP2alLlegar = 0.0;
						}
						if(ddiP3alLlegar.isInfinite() || ddiP3alLlegar.isNaN()){
							ddiP3alLlegar = 0.0;
						}
						BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
						BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
						BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
						BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
						BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
						BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
						BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
						BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
						
						BigDecimal _ddiP1alLlegar = new BigDecimal(ddiP1alLlegar);
						BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2alLlegar = new BigDecimal(ddiP2alLlegar);
						BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3alLlegar = new BigDecimal(ddiP3alLlegar);
						BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
						BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
						BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
						BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP3 = new BigDecimal(ddiP3);
						BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP2 = new BigDecimal(ddiP2);
						BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
						BigDecimal _ddiP1 = new BigDecimal(ddiP1);
						BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
						
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
								"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
										"</strong></td>"; //Producto
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
						html += "</tr>";
						
					}
				}else{
					marca_ini = dto.getIdMarca();
					marca_fin = row.get(i).getIdMarca();
					
					Double ttl_cjs_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_mes"));
					Double clc_prom_diario_mrc = ttl_cjs_mrc / perd_dias;
					
					Double existencias_ttl_mss = existencias_ttl_mss = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajas_ext"));
					Double pend_x_fac_nrml_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_pendiente_x_fact"));
					Double disponible_mrc = existencias_ttl_mss - pend_x_fac_nrml_mrc;
					
					Double dias_invent_mrc = disponible_mrc / clc_prom_diario_mrc;
					Double dias_invent_1_mrc = existencias_ttl_mss / clc_prom_diario_mrc;
					Double minstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_minstock"));
					Double maxstock_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_maxstock"));
					Double tpo_prov_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_tprov"));
					Double tiempo_limit_mantener_stock =(dias_invent_mrc - minstock_mrc)/ tpo_prov_mrc; //Tiempo limite para mantener Stock
					
					///Tiempo Limite Para Levantar Pedido
					Double cajasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp1"));
					Double cajasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp2"));
					Double cajasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_cajasp3"));
					Double diasP1_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp1"));
					Double diasP2_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp2"));
					Double diasP3_mrc = Double.parseDouble((String)ttl_marcas.get(dto.getIdMarca()+"_ttl_diasp3"));
					
					Double ddiP1 = (cajasP1_mrc + in_cajasP1) / clc_prom_diario_mrc;
					Double ddiP2 = (cajasP2_mrc + in_cajasP2) / clc_prom_diario_mrc;
					Double ddiP3 = (cajasP3_mrc + in_cajasP3) / clc_prom_diario_mrc;
					
					Double beOculto = (((ddiP1 + ddiP2 + ddiP3 + dias_invent_mrc) - minstock_mrc) * cajasP1) / cajasP1;
					Double tiempo_limit_levantar_ped = beOculto - tpo_prov_mrc;
						
					//Continuidad Existencias al Minimi P1
					Double cont_exist_min_p1 = (((dias_invent_mrc - (diasP1_mrc + in_diasP1) - minstock_mrc)) / ddiP1) * ddiP1 ; 
					Double cont_exist_min_p2 = ((((dias_invent_mrc + ddiP1) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double cont_exist_min_p3 = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) - minstock_mrc) / ddiP3) * ddiP3;
						
					if(cont_exist_min_p1.isNaN()){
						cont_exist_min_p1 = 0.0;
					}
					if(cont_exist_min_p2.isNaN()){
						cont_exist_min_p2 = 0.0;
					}
					if(cont_exist_min_p3.isNaN()){
						cont_exist_min_p3 = 0.0;
					}
					
					Double ddiP1alLlegar = (((dias_invent_mrc - (diasP1 + in_diasP1) + ddiP1) - minstock_mrc)/ddiP1)*ddiP1; 
					Double ddiP2alLlegar = ((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP2 + in_diasP2) - minstock_mrc) / ddiP2) * ddiP2);
					Double ddiP3alLlegar = ((((((dias_invent_mrc + ddiP1 + ddiP2) - (diasP3 + in_diasP3)) + ddiP3) - minstock_mrc) / ddiP3) * ddiP3);
					if(ddiP1alLlegar.isInfinite() || ddiP1alLlegar.isNaN()){
						ddiP1alLlegar = 0.0;
					}
					if(ddiP2alLlegar.isInfinite() || ddiP2alLlegar.isNaN()){
						ddiP2alLlegar = 0.0;
					}
					if(ddiP3alLlegar.isInfinite() || ddiP3alLlegar.isNaN()){
						ddiP3alLlegar = 0.0;
					}
					BigDecimal _tiempo_limit_mantener_stock = new BigDecimal(tiempo_limit_mantener_stock);
					BigDecimal rd_tiempo_limit_mantener_stock = _tiempo_limit_mantener_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tiempo_limit_levantar_ped = new BigDecimal(tiempo_limit_levantar_ped);
					BigDecimal rd_tiempo_limit_levantar_ped = _tiempo_limit_levantar_ped.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _clc_prom_diario_mrc = new BigDecimal(clc_prom_diario_mrc);
					BigDecimal rd_clc_prom_diario_mrc = _clc_prom_diario_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _existencias_ttl_mss = new BigDecimal(existencias_ttl_mss);
					BigDecimal rd_existencias_ttl_mss = _existencias_ttl_mss.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_mrc = new BigDecimal(dias_invent_mrc);
					BigDecimal rd_dias_invent_mrc = _dias_invent_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _dias_invent_1_mrc = new BigDecimal(dias_invent_1_mrc);
					BigDecimal rd_dias_invent_1_mrc = _dias_invent_1_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _pend_x_fac_nrml_mrc = new BigDecimal(pend_x_fac_nrml_mrc);
					BigDecimal rd_pend_x_fac_nrml_mrc = _pend_x_fac_nrml_mrc.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _disponible_mrc = new BigDecimal(disponible_mrc);
					BigDecimal rd_disponible_mrc = _disponible_mrc.setScale(1, RoundingMode.HALF_UP);
					
					BigDecimal _ddiP1alLlegar = new BigDecimal(ddiP1alLlegar);
					BigDecimal rd_ddiP1alLlegar = _ddiP1alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2alLlegar = new BigDecimal(ddiP2alLlegar);
					BigDecimal rd_ddiP2alLlegar = _ddiP2alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3alLlegar = new BigDecimal(ddiP3alLlegar);
					BigDecimal rd_ddiP3alLlegar = _ddiP3alLlegar.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p1 = new BigDecimal(cont_exist_min_p1);
					BigDecimal rd_cont_exist_min_p1 = _cont_exist_min_p1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p2 = new BigDecimal(cont_exist_min_p2);
					BigDecimal rd_cont_exist_min_p2 = _cont_exist_min_p2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _cont_exist_min_p3 = new BigDecimal(cont_exist_min_p3);
					BigDecimal rd_cont_exist_min_p3 = _cont_exist_min_p3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP3 = new BigDecimal(ddiP3);
					BigDecimal rd_ddiP3 = _ddiP3.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP2 = new BigDecimal(ddiP2);
					BigDecimal rd_ddiP2 = _ddiP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _ddiP1 = new BigDecimal(ddiP1);
					BigDecimal rd_ddiP1 = _ddiP1.setScale(1, RoundingMode.HALF_UP);
					
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescMarca()+"" +
							"<a href='javascript:hideRow("+dto.getIdMarca()+")'>  -</a><a href='javascript:showRow("+dto.getIdMarca()+")'> +</a>" +
									"</strong></td>"; //Producto
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP1_mrc+"</strong></td>"; // # Dias Llegada P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1+"</strong></td>"; // DDI Pedido P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP1alLlegar+"</strong></td>"; // DDI al Llegar P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p1+"</strong></td>"; // Continuidad Existencias al Minimo P1
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP2_mrc+"</strong></td>"; // # Dias Llegada P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2+"</strong></td>"; // DDI Pedido P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP2alLlegar+"</strong></td>"; // DDI al Llegar P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p2+"</strong></td>"; // Continuidad Existencias al Minimo P2
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+diasP3_mrc+"</strong></td>"; // # Dias Llegada P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3+"</strong></td>"; // DDI Pedido P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_ddiP3alLlegar+"</strong></td>"; // DDI al Llegar P3
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><strong>"+rd_cont_exist_min_p3+"</strong></td>"; // Continuidad Existencias al Minimo P3
					html += "</tr>";						
				}
			}*/
		}
		
		html += "</tbody>";
		html += "</table> "+ toltip_meses;
		
		System.out.println("Tabla... ");
		return html;
	}
	
	//Tabla Listado de Productos MPO
	public String getTableListaProdMPO(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String fechaIni,
			String fechaFin,
			String idEspacios) throws UnsupportedEncodingException, ParseException {
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		
		String html = "";
		
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td colspan=2></td>";
		html += "<td>Piezas</td>";
		html += "<td>Invent Inicial</td>";
		html += "<td>Entrada</td>";
		html += "<td>Salidas</td>";
		html += "<td>Ajuste</td>";
		html += "<td>Invent Final</td>";
		html += "<td># Productos</td>";
		html += "<td>Restan</td>";
		html += "<td>Costo</td>";
		html += "</tr>";
		
		html += "</thead>";
		html +="<tbody >";
		
		//ArrayList arrayList = new ArrayList();
		DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
		simbolo.setDecimalSeparator('.');
		simbolo.setGroupingSeparator(',');
		DecimalFormat frmt = new DecimalFormat("###,###.##",simbolo);
		
		SimpleDateFormat formatoF1 = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy/MM/dd");
		
		java.util.Date fIni = null;
		java.util.Date fFin = null;
		
		String fI = null;
		String fF = null;
		
		if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
				&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
			
			fIni =  formatoF1.parse(fechaIni);
			fFin =  formatoF1.parse(fechaFin);
			fI = formatoF2.format(fIni);
			fF = formatoF2.format(fFin);
		}
		
		Double minNumProd = 0.0;
		List<InvInsumos> row = invent.getDataInsumos(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, fI, fF, idEspacios);
		HashMap ttl_insumos = invent.getDataInsumosMarcas(cust_id, id_user, id_modulo, id_dashboard, chart_id, fI, fF, idEspacios);
		for(int i = 0; i < row.size(); i ++){
		
			InvInsumos dto = row.get(i);
			
			//Generar total para cada marca
			String marca_ini = "";
			String marca_fin = "";
			String marca = "";
			//System.out.println(dto.getIdProductoTerm()+" -- "+ttl_insumos.get(dto.getIdProductoTerm()));
			if(ttl_insumos.get(dto.getIdProductoTerm()) != null){
				minNumProd = Double.parseDouble((String)ttl_insumos.get(dto.getIdProductoTerm()));
			}
			//System.out.println("Contador: -------> "+i);
			//ArrayList arrayList = new ArrayList();----
			if(i == 0){
				
				//if(!marca_ini.equals(marca_fin)){
					html += "<tr>";
					html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+dto.getIdProductoTerm()+"</th>"; //Marca
					html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm()+"<a href=javascript:listaProdLugares('"+dto.getIdProductoTerm()+"','"+fI+"','"+fF+"')> +</a>" +
					"</strong></th>";
							/*"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
									"</strong></th>"; //Producto*/
					
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Invent Inicial
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Entradas
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Salidas
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajuste
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Invent Final
					html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+frmt.format(minNumProd)+"</th>"; // # Productos
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Costo				
				//}
			}else{
				
				if(i+1 < row.size()){
					marca_ini = dto.getIdProductoTerm();
					marca_fin = row.get(i-1).getIdProductoTerm();
					
					if(!marca_ini.equals(marca_fin)){
						html += "<tr>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+dto.getIdProductoTerm()+"</th>"; //Marca
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm()+"<a href=javascript:listaProdLugares('"+dto.getIdProductoTerm()+"','"+fI+"','"+fF+"')> +</a>" +
								"</strong></th>";
								/*"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Invent Inicial
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Entradas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Salidas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajuste
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Invent Final
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+frmt.format(minNumProd)+"</th>"; // # Productos
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Costo 
					
					}
				}else{
					marca_ini = dto.getIdProductoTerm();
					marca_fin = row.get(i-1).getIdProductoTerm();
					if(!marca_ini.equals(marca_fin)){
						html += "<tr>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+dto.getIdProductoTerm()+"</th>"; //Marca
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm()+"<a href=javascript:listaProdLugares('"+dto.getIdProductoTerm()+"','"+fI+"','"+fF+"')> +</a>" +
								"</strong></th>";
								/*"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Invent Inicial
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Entradas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Salidas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajuste
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Invent Final
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+frmt.format(minNumProd)+"</th>"; // # Productos
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Costo
					}
				}
			}
			// Filas para los subproductos que integran a un producto
			
			Double inven_ini = Double.parseDouble((String)dto.getInventInicial());
			Double numProd = 0.0;
			numProd = Double.parseDouble((String)dto.getInventFinal()) / Double.parseDouble((String)dto.getPiezas());
			Double costoFinal = 0.0;
			costoFinal = Double.parseDouble((String)dto.getInventFinal()) * Double.parseDouble((String)dto.getCosto());
			Double entradas = Double.parseDouble((String)dto.getEntradas());
			Double salidas = Double.parseDouble((String)dto.getSalidas());
			Double ajuste = Double.parseDouble((String)dto.getAjuste());
			Double invFin = Double.parseDouble((String)dto.getInventFinal());
			Double min = 0.0;
			if(ttl_insumos.get(dto.getIdProductoTerm()) != null){
				min = Double.parseDouble((String)ttl_insumos.get(dto.getIdProductoTerm()));
			}
			Double piezas = Double.parseDouble((String)dto.getPiezas());
			Double restan = ( invFin - (min * piezas));
			
			String idp = dto.getIdp();
			
			if(idp.equals("1")){
				html += "<tr class=mrc_"+dto.getItemcodeProd()+">";
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getItemcodeProd()+"</td>"; //Marca
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProducto()+"</td>"; //Producto
				
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(piezas)+"</td>"; // Piezas
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(inven_ini)+"</td>"; // Invent Inicial
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(entradas)+"</td>"; // Entradas
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(salidas)+"</td>"; // Salidas
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(ajuste)+"</td>"; // Ajuste
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(invFin)+"</td>"; // Invent Final
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(numProd)+"</td>"; // # Productos
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(restan)+"</td>"; // Restan
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(costoFinal)+"</td>"; // Costo
			}
			html += "</tr>";
			
			//arrayList.add(new Double(numProd));
			
			//---------------------------------------////
			
			
			
		}
		
		html += "</tbody>";
		html += "</table> ";
		
		System.out.println("Tabla... ");
		return html;
	}
	
	// Tabla Lista Componentes En Cada Lugar
	public String getTableListaComponentesPorLugar(
			String cust_id,
			String id_user,
			String id_modulo,
			String id_dashboard,
			String chart_id,
			String id_prod_term, 
			String fechaIni, 
			String fechaFin, 
			String idEspacios) throws UnsupportedEncodingException, ParseException {
		//Configuracion del portlet o grid
		HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
		//Filtros para el portlet o grid
		String cmp_filtro = (String) hm.get("cmp_id");
		String html = "";
		boolean org_param = false;
		int meses_enc = 3;
		int meses_col = 3;
		int meses_fil = 3;
		int meses_ttl = 3;
		int meses_marca = 3;
		
		html += "<table id=tblDatos class=tablesorter border=1 >";
		html += "<thead>";
		
		//Encabezado
		html += "<tr><td></td>";
		html += "<td></td>";
		html += "<td>Piezas</td>";
		html += "<td>Existencias</td>";
		//html += "<td>Ajuste</td>";
		html += "<td># Productos</td>";
		html += "<td>Restan</td>";
		html += "<td>Costo</td>";
		html += "</tr>";
		
		html += "</thead>";
		html +="<tbody >";
		
		DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
		simbolo.setDecimalSeparator('.');
		simbolo.setGroupingSeparator(',');
		DecimalFormat frmt = new DecimalFormat("###,###.##",simbolo);
	    
		SimpleDateFormat formatoF1 = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy/MM/dd");
		
		java.util.Date fIni = null;
		java.util.Date fFin = null;
		
		String fI = null;
		String fF = null;
		
		//if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
				//&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
			
			//fIni =  formatoF1.parse(fechaIni);
			//fFin =  formatoF1.parse(fechaFin);
			fI = fechaIni;
			fF = fechaFin;
		//}
		String espacios_not_in = "";
		List<InvInsumos> row = invent.getDataListaProdLugares(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
		//List<InvInsumos> prod_ext = invent.getDataListaProdLugaresExp(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios, espacios_not_in);
		HashMap ttl_insumos = invent.getDataListaProdLugaresMin(cust_id, id_user, id_modulo, id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
		int cont = 1;
		HashMap prod_ini =  new HashMap();
		String[] prodIni = null; 
		//Generar total para cada marca
		String marca_ini = "";
		String marca_fin = "";
		
		String almacen_ini = "";
		String almacen_fin = "";
		
		String marca = "";
		// componentes o insumos que no aparecen en el rango de fechas pero si tiene exstencias
		String espacio = "";
		String espacio_xt = ""; 
		int contador = 0;
		int cont_int = 0;
		
		for(int i = 0; i < row.size(); i ++){
		
			InvInsumos dto = row.get(i);
			
			
						
			//ArrayList arrayList = new ArrayList();----
			if(i == 0){
				
					prod_ini.put(cont, dto.getIdAlmacen());
					html += "<tr>";
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
					html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen()+"<a href=javascript:listaEntradasLugares('"+dto.getIdAlmacen()+"','"+dto.getItemProductoTerm()+"','"+fI+"','"+fF+"')> +</a>";
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
					//html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
					html += "</tr>";
					
					//Productos Terminados
					Double n_prd = 0.0;
					String prd = (String)ttl_insumos.get(dto.getIdAlmacen());
					if(n_prd != null){
						n_prd =Double.parseDouble(prd);
					}
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getItemProductoTerm()+"</th>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
					//html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+frmt.format(n_prd)+"</th>"; // #Productos
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
					html += "</tr>";
					
					espacio = (String) dto.getIdAlmacen();
					// recorres datos extras y compara el id del almacen donde se encuentre agregar el componente o insumo
//					if(fI !=  null && fF != null && !fI.equals("null") && !fF.equals("null")){
//						System.out.println("fI---<<<>>>---fF "+fI+ " "+fF);
//						for(int a = 0; a < prod_ext.size(); a ++){
//							InvInsumos prod = prod_ext.get(a);
//							espacio_xt = (String) prod.getIdAlmacen();
//							//System.out.println("Cmp --< >--- "+espacio+" -- "+espacio_xt);
//							if(espacio_xt.equals(espacio)){
//								//System.out.println("Contador interno ---> "+cont_int);
//								//System.out.println("Cmp --< >--- "+espacio+" -- "+espacio_xt);
//								String ajuste = "";
//								String ruta = "ajustes.jsp?id_prod="+prod.getIdProducto()+"&id_prod_term="+prod.getIdProductoTerm()+"&itemcode_prod_term="+prod.getItemProductoTerm()+"&id_espacio="+prod.getIdAlmacen()
//										+"&itemcode="+prod.getItemcodeProd()+"&tipo_prod="+prod.getTipo_prod();
//								ajuste = "<a href='#' onclick=abrePop('"+ruta+"');><strong> + </strong></a>";
//								
//								html += "<tr class=mrc_"+prod.getItemcodeProd()+">";
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+prod.getItemcodeProd()+"</td>"; //Marca
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+prod.getDescProducto()+" "+ajuste+"</td>"; //Producto
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Piezas
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Existencias
//								//html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Ajustes
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // #Productos
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Restan
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Costo
//								html += "</tr>";
//							}
//							//cont_int ++;
//						}
//					}	
					cont++;
			}else{
				
				
				
				if(i+1 < row.size() ){
					marca_ini = dto.getIdProductoTerm();
					marca_fin = row.get(i-1).getIdProductoTerm();
					
					almacen_ini = dto.getIdAlmacen();
					almacen_fin = row.get(i-1).getIdAlmacen();
					
					if(!almacen_ini.equals(almacen_fin)){
						html += "<tr>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen()+"<a href=javascript:listaEntradasLugares('"+dto.getIdAlmacen()+"','"+dto.getItemProductoTerm()+"','"+fI+"','"+fF+"')> +</a>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						//html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
						html += "</tr>";
						
						//Productos Terminados
						Double n_prd = 0.0;
						String prd = (String)ttl_insumos.get(dto.getIdAlmacen());
						if(n_prd != null){
							n_prd =Double.parseDouble(prd);
						}
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getItemProductoTerm()+"</th>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						//html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+frmt.format(n_prd)+"</th>"; // #Productos
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
						html += "</tr>";
						
						espacio = (String) dto.getIdAlmacen();
//						// recorres datos extras y compara el id del almacen donde se encuentre agregar el componente o insumo
//						if(fI !=  null && fF != null && !fI.equals("null") && !fF.equals("null")){
//							for(int a = 0; a < prod_ext.size(); a ++){
//								InvInsumos prod = prod_ext.get(a);
//								espacio_xt = (String) prod.getIdAlmacen();
//								//System.out.println("Cmp --< >--- "+espacio+" -- "+espacio_xt);
//								if(espacio_xt.equals(espacio)){
//									//System.out.println("Contador interno ---> "+cont_int);
//									//System.out.println("Cmp --< >--- "+espacio+" -- "+espacio_xt);
//									String ajuste2 = "";
//									String ruta2 = "ajustes.jsp?id_prod="+prod.getIdProducto()+"&id_prod_term="+prod.getIdProductoTerm()+"&itemcode_prod_term="+prod.getItemProductoTerm()+"&id_espacio="+prod.getIdAlmacen()
//											+"&itemcode="+prod.getItemcodeProd()+"&tipo_prod="+prod.getTipo_prod();
//									ajuste2 = "<a href='#' onclick=abrePop('"+ruta2+"');><strong> + </strong></a>";
//									
//									html += "<tr class=mrc_"+prod.getItemcodeProd()+">";
//									html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+prod.getItemcodeProd()+"</td>"; //Marca
//									html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+prod.getDescProducto()+" "+ajuste2+"</td>"; //Producto
//									html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Piezas
//									html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Existencias
//									//html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Ajustes
//									html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // #Productos
//									html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Restan
//									html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Costo
//									html += "</tr>";
//								}
//								cont_int ++;
//							}
//						}
						//prodIni[cont] = dto.getIdAlmacen();
						prod_ini.put(cont, dto.getIdAlmacen());
						cont++;
						
					}
				}else{
					//if(i == row.size()){
					marca_ini = dto.getIdProductoTerm();
					marca_fin = row.get(i).getIdProductoTerm();
					
					almacen_ini = dto.getIdAlmacen();
					almacen_fin = row.get(i-1).getIdAlmacen();
					
					if(!almacen_ini.equals(almacen_fin)){
					html += "<tr>";
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
					html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen()+"<a href=javascript:listaEntradasLugares('"+dto.getIdAlmacen()+"','"+dto.getIdProductoTerm()+"','"+fI+"','"+fF+"')> +</a>";/*
							"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
									"</strong></th>"; //Producto*/
					
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
					//html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
					html += "</tr>";
					
					//Productos Terminados
					Double n_prd = 0.0;
					String prd = (String)ttl_insumos.get(dto.getIdAlmacen());
					if(n_prd != null){
						n_prd =Double.parseDouble(prd);
					}
					html += "<tr>";
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getItemProductoTerm()+"</th>"; //Marca
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
							"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
									"</strong></th>"; //Producto*/
					
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
					//html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+frmt.format(n_prd)+"</th>"; // #Productos
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
					html += "</tr>";
					
					espacio = (String) dto.getIdAlmacen();
					// recorres datos extras y compara el id del almacen donde se encuentre agregar el componente o insumo
//					if(fI !=  null && fF != null && !fI.equals("null") && !fF.equals("null")){
//						for(int a = 0; a < prod_ext.size(); a ++){
//							InvInsumos prod = prod_ext.get(a);
//							espacio_xt = (String) prod.getIdAlmacen();
//							//System.out.println("Cmp --< >--- "+espacio+" -- "+espacio_xt);
//							if(espacio_xt.equals(espacio)){
//								//System.out.println("Contador interno ---> "+cont_int);
//								//System.out.println("Cmp --< >--- "+espacio+" -- "+espacio_xt);
//								String ajuste3 = "";
//								String ruta3 = "ajustes.jsp?id_prod="+prod.getIdProducto()+"&id_prod_term="+prod.getIdProductoTerm()+"&itemcode_prod_term="+prod.getItemProductoTerm()+"&id_espacio="+prod.getIdAlmacen()
//										+"&itemcode="+prod.getItemcodeProd()+"&tipo_prod="+prod.getTipo_prod();
//								ajuste3 = "<a href='#' onclick=abrePop('"+ruta3+"');><strong> + </strong></a>";
//								
//								html += "<tr class=mrc_"+prod.getItemcodeProd()+">";
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+prod.getItemcodeProd()+"</td>"; //Marca
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+prod.getDescProducto()+" "+ajuste3+"</td>"; //Producto
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Piezas
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Existencias
//								//html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Ajustes
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // #Productos
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Restan
//								html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Costo
//								html += "</tr>";
//							}
//							cont_int ++;
//						}
//					}
					prod_ini.put(cont, dto.getIdAlmacen());
					cont++;
				}}
			}
			
			
			
			
			// Filas para los subproductos que integran a un producto
			
			Double numProd = 0.0;
			//numProd = Double.parseDouble((String)dto.getInventFinal()) / Double.parseDouble((String)dto.getPiezas());
			Double piezas = Double.parseDouble((String)dto.getPiezas());
			Double existencias = Double.parseDouble((String)dto.getExistencias());
			Double costoFinal = 0.0;
			costoFinal = Double.parseDouble((String)dto.getCosto());
			Double ajusteCant = Double.parseDouble((String)dto.getAjuste());
			//Double invFin = Double.parseDouble((String)dto.getInventFinal());
			String m = (String)ttl_insumos.get(dto.getIdAlmacen());
			Double min = 0.0;
			if(m != null && !m.equals("null")){
				min = Double.parseDouble(m);
			}
			//Double piezas = Double.parseDouble((String)dto.getPiezas());
//			Double invFin = Double.parseDouble((String)dto.getInventFinal());
//			Double min = 0.0;
//			if(ttl_insumos.get(dto.getIdProductoTerm()) != null){
//				min = Double.parseDouble((String)ttl_insumos.get(dto.getIdProductoTerm()));
//			}
//			Double piezas = Double.parseDouble((String)dto.getPiezas());
//			Double restan = ( invFin - (min * piezas));
//			
			
			Double restan = ( existencias - (min * piezas));
			if(piezas != null  && piezas != null){
				numProd = ((existencias) / piezas);
			}
			String ajuste = "";
			String ruta = "ajustes.jsp?id_prod="+dto.getIdProducto()+"&id_prod_term="+dto.getIdProductoTerm()+"&itemcode_prod_term="+dto.getItemProductoTerm()+"&id_espacio="+dto.getIdAlmacen()
					+"&itemcode="+dto.getItemcodeProd()+"&tipo_prod="+dto.getTipo_prod();
			ajuste = "<a href='#' onclick=abrePop('"+ruta+"'); id='aj_modal'><strong> + </strong></a>";
			
			
			
			html += "<tr class=mrc_"+dto.getItemcodeProd()+">";
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getItemcodeProd()+"<strong><a href=javascript:listaEntradasLugaresInsumComp('"+dto.getIdAlmacen()+"','"+dto.getItemcodeProd()+"','"+fI+"','"+fF+"')> +</td>"; //Marca
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProducto()+" "+ajuste+"</td>"; //Producto
			
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(piezas)+"</td>"; // Piezas
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(existencias)+"</td>"; // Existencias
			//html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Ajustes
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(numProd)+"</td>"; // #Productos
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(restan)+"</td>"; // Restan
			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(costoFinal)+"</td>"; // Costo
			
			
			html += "</tr>";
			
		}
		/*Recorrer primero el hash map que trae los espacios ya pintados para despues recorrer los datos de
		 * los espacios que deberan venir en ceros, donde espacios en cero sea != de espacios ya pintados
		 * se procedera a recorrer datos en cero para asi formar los espacios faltantes
		 * */
//		String espacio_ini = "";
//		String espacio_fin = "";
//		Iterator it = prod_ini.entrySet().iterator();
//		Iterator it2 = prod_ini.entrySet().iterator();
//		Iterator it3 = prod_ini.entrySet().iterator();
//		
//		int c = 0;
//		while (it.hasNext()) {
//			Map.Entry e = (Map.Entry)it.next();
//			espacio_fin = (String) e.getValue();
//			System.out.println(e.getKey() + " " + espacio_fin);
//			if(c == 0){
//				espacios_not_in = (String) e.getValue();
//			}else{
//				espacios_not_in += "," + (String) e.getValue();
//			}
//			c++;
//		}
//		System.out.println("NOT IN "+espacios_not_in);
//		List<InvInsumos> prodExt = null;
//		
//		if(fI != null && fF != null){
//		//Solo se mostrara estos datos en caso de haber seleccioando un rango de dias
//		prodExt = invent.getDataListaProdLugaresExp(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios,espacios_not_in);
//		for(int p = 0; p < prodExt.size(); p++){
//			InvInsumos dt = prodExt.get(p);
//			System.out.println(" Espacios ----> " + dt.getIdAlmacen() );
//			//ArrayList arrayList = new ArrayList();----
//			if(p == 0){
//				
//					html += "<tr>";
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dt.getDescAlmacen()+"<a href=javascript:listaEntradasLugares('"+dt.getIdAlmacen()+"','"+dt.getItemProductoTerm()+"','"+fI+"','"+fF+"')> +</a>";
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//					//html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
//					html += "</tr>";
//					
//					//Productos Terminados
//					
//					html += "<tr>";
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dt.getItemProductoTerm()+"</td>"; //Marca
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dt.getDescProductoTerm()+"</td>";
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//					//html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
//					html += "</tr>";
//					
//			}else{
//				
//				
//				System.out.println(" Espacios --2--> " + dt.getIdAlmacen() );
//				if(p+1 <= prodExt.size() ){
//					//marca_ini = dt.getIdProductoTerm();
//					//marca_fin = row.get(p-1).getIdProductoTerm();
//					
//					almacen_ini = dt.getIdAlmacen();
//					almacen_fin = prodExt.get(p-1).getIdAlmacen();
//					System.out.println(" Espacios --3--> " + dt.getIdAlmacen() );
//					if(!almacen_ini.equals(almacen_fin)){
//						html += "<tr>";
//						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
//						html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dt.getDescAlmacen()+"<a href=javascript:listaEntradasLugares('"+dt.getIdAlmacen()+"','"+dt.getItemProductoTerm()+"','"+fI+"','"+fF+"')> +</a>";
//						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//						//html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
//						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
//						html += "</tr>";
//						
//						//Productos Terminados
//						html += "<tr>";
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dt.getItemProductoTerm()+"</th>"; //Marca
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dt.getDescProductoTerm();/*+"" +
//								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
//										"</strong></th>"; //Producto*/
//						
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//						//html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
//						html += "</tr>";
//						
//					
//			
//				}else{
//					//if(i == row.size()){
//					//marca_ini = dt.getIdProductoTerm();
//					//marca_fin = prodExt.get(p).getIdProductoTerm();
//					
//					almacen_ini = dt.getIdAlmacen();
//					almacen_fin = prodExt.get(p-1).getIdAlmacen();
//					
//					if(!almacen_ini.equals(almacen_fin)){
//					html += "<tr>";
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dt.getDescAlmacen()+"<a href=javascript:listaEntradasLugares('"+dt.getIdAlmacen()+"','"+dt.getIdProductoTerm()+"','"+fI+"','"+fF+"')> +</a>";/* +
//							"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
//									"</strong></th>"; //Producto*/
//					
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//					//html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
//					html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
//					html += "</tr>";
//					
//					//Productos Terminados
//					html += "<tr>";
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dt.getItemProductoTerm()+"</th>"; //Marca
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dt.getDescProductoTerm();/*+"" +
//							"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
//									"</strong></th>"; //Producto*/
//					
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//					//html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Restan
//					html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>";//<font color='#B40404'>"+ttl_insumos.get(dto.getIdProductoTerm())+"</th>"; // # Productos
//					html += "</tr>";
//					
//				}
//			}
//		}
//			}
//			String ajuste4 = "";
//			String ruta4 = "ajustes.jsp?id_prod="+dt.getIdProducto()+"&id_prod_term="+dt.getIdProductoTerm()+"&itemcode_prod_term="+dt.getItemProductoTerm()+"&id_espacio="+dt.getIdAlmacen()
//					+"&itemcode="+dt.getItemcodeProd()+"&tipo_prod="+dt.getTipo_prod();
//			ajuste4 = "<a href='#' onclick=abrePop('"+ruta4+"');><strong> + </strong></a>";
//			html += "<tr class=mrc_"+dt.getItemcodeProd()+">";
//			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dt.getItemcodeProd()+"</td>"; //Marca
//			html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dt.getDescProducto()+" "+ajuste4+"</td>"; //Producto
//			
//			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Piezas
//			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Existencias
//			//html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Ajustes
//			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // #Productos
//			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Restan
//			html += "<td class=tablrWidget_headerCell align='right' valign='top'></td>"; // Costo
//			
//			
//			html += "</tr>";
//		}}
		html += "</tbody>";
		html += "</table> ";
		
		System.out.println("Tabla... ");
		return html;
	}
	
	// Tabla Lista Entradas En Cada Lugar
		public String getTableListaEntradasPorLugar(
				String cust_id,
				String id_user,
				String id_modulo,
				String id_dashboard,
				String chart_id,
				String id_prod_term, 
				String fechaIni, 
				String fechaFin, 
				String idEspacios) throws UnsupportedEncodingException, ParseException {
			//Configuracion del portlet o grid
			HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
			//Filtros para el portlet o grid
			String cmp_filtro = (String) hm.get("cmp_id");
			String html = "";
			boolean org_param = false;
			int meses_enc = 3;
			int meses_col = 3;
			int meses_fil = 3;
			int meses_ttl = 3;
			int meses_marca = 3;
			
			html += "<table id=tblDatos class=tablesorter border=1 >";
			html += "<thead>";
			
			//Encabezado
			/*html += "<tr><td></td>";
			html += "<td></td>";
			html += "<td>Cantidad</td>";
			html += "<td>#Entradas</td>";
			html += "<td>Fechas</td>";
			html += "<td>Piezas</td>";
			html += "</tr>";
			*/
			html += "</thead>";
			html +="<tbody >";
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.##",simbolo);

			SimpleDateFormat formatoF1 = new SimpleDateFormat("dd/MM/yy");
			SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy/MM/dd");
			
			java.util.Date fIni = null;
			java.util.Date fFin = null;
			
			String fI = null;
			String fF = null;
			
			/*if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
					&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
				
				fIni =  formatoF1.parse(fechaIni);
				fFin =  formatoF1.parse(fechaFin);*/
				fI = fechaIni;
				fF = fechaFin;
			//}
			
			DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM);
			
			List<InvInsumos> row = invent.getDataListaEntradasLugares(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			//HashMap ttl_insumos = invent.getDataListaProdLugaresMin(cust_id, id_user, id_modulo, id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			for(int i = 0; i < row.size(); i ++){
			
				InvInsumos dto = row.get(i);
				
				//Generar total para cada marca
				String marca_ini = "";
				String marca_fin = "";
				
				String almacen_ini = "";
				String almacen_fin = "";
				
				String marca = "";
				
				
				//ArrayList arrayList = new ArrayList();----
				if(i == 0){
					//marca_ini = dto.getIdProductoTerm();
					//marca_fin = row.get(i+1).getIdProductoTerm();
					
					//almacen_ini = dto.getIdAlmacen();
					//almacen_fin = row.get(i+1).getIdAlmacen();
					//if(!marca_ini.equals(marca_fin)){
						html += "<tr>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>Cantidad</th>"; // Cantidad
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>#Entradas</th>"; // #Entradas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>Fecha</th>"; // Fecha
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>Piezas</th>"; // Piezas
						html += "</tr>";
						
						//Productos Terminados
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getIdProductoTerm()+"</th>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
						
						html += "</tr>";
				}else{
					
					if(i+1 < row.size() ){
						marca_ini = dto.getIdProductoTerm();
						marca_fin = row.get(i-1).getIdProductoTerm();
						
						almacen_ini = dto.getIdAlmacen();
						almacen_fin = row.get(i-1).getIdAlmacen();
						
						if(!almacen_ini.equals(almacen_fin)){
							html += "<tr>";
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
							html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen();/*+"" +
									"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
											"</strong></th>"; //Producto*/
							
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
							html += "</tr>";
							
							//Productos Terminados
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getIdProductoTerm()+"</th>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
									"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
											"</strong></th>"; //Producto*/
							
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
							html += "</tr>";
						
						}
					}else{
						//if(i == row.size()){
						marca_ini = dto.getIdProductoTerm();
						marca_fin = row.get(i).getIdProductoTerm();
						
						almacen_ini = dto.getIdAlmacen();
						almacen_fin = row.get(i-1).getIdAlmacen();
						
						if(!almacen_ini.equals(almacen_fin)){
						html += "<tr>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
						html += "</tr>";
						
						//Productos Terminados
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getIdProductoTerm()+"</th>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
						html += "</tr>";
					}}
				}
				// Filas para los subproductos que integran a un producto
				
				html += "<tr class=mrc_"+dto.getItemcodeProd()+">";
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getIdProducto()+"</td>"; //Marca
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProducto()+"</td>"; //Producto
				
				String cant = (String) dto.getCantidad();
				Double cantidad = 0.0;
				if(cant != null){
					cantidad = Double.parseDouble(cant);
				}
				
				if(i == 0){
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getIdProducto()+"</td>"; //Marca
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getDescProducto()+"</td>"; //Producto
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getEntradas()+"</td>"; // Existencias
				}else{
					String prod_ini = dto.getIdProducto();
					String prod_fin = row.get(i-1).getIdProducto();
					if(i+1 < row.size() ){
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getIdProducto()+"</td>"; //Marca
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getDescProducto()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getEntradas()+"</td>"; // Existencias
						}
					}else{
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getIdProducto()+"</td>"; //Marca
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getDescProducto()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getEntradas()+"</td>"; // Existencias
						}
					}
				}
				
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat frt = new SimpleDateFormat("dd-MMM-yy");
				java.util.Date fechaajuste = null;
				String fec = (String) dto.getFecha();
				fechaajuste = formateador.parse(fec);
				String fechaaj = frt.format(fechaajuste);
				String pzas = (String) dto.getPiezas();
				Double piezas = 0.0;
				if(pzas != null && !pzas.equals("null")){
					piezas = Double.parseDouble(pzas);
				}
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCantidad()+"</td>"; // Piezas
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getEntradas()+"</td>"; // Existencias
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fechaaj+"</td>"; // Ajustes
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(piezas)+"</td>"; // #Productos
				html += "</tr>";
				
				
				//---------------------------------------////
				
				
				
			}
			
			html += "</tbody>";
			html += "</table> ";
			
			System.out.println("Tabla... ");
			return html;
		}
	//Enlista las salidas por lugar
		public String getTableListaSalidasPorLugar(
				String cust_id,
				String id_user,
				String id_modulo,
				String id_dashboard,
				String chart_id,
				String id_prod_term, 
				String fechaIni, 
				String fechaFin, 
				String idEspacios) throws UnsupportedEncodingException, ParseException {
			//Configuracion del portlet o grid
			HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
			//Filtros para el portlet o grid
			String cmp_filtro = (String) hm.get("cmp_id");
			String html = "";
			boolean org_param = false;
			int meses_enc = 3;
			int meses_col = 3;
			int meses_fil = 3;
			int meses_ttl = 3;
			int meses_marca = 3;
			
			html += "<table id=tblDatos class=tablesorter border=1 >";
			html += "<thead>";
			
			//Encabezado
			/*html += "<tr><td></td>";
			html += "<td></td>";
			html += "<td>Cantidad</td>";
			html += "<td>#Entradas</td>";
			html += "<td>Fechas</td>";
			html += "<td>Piezas</td>";
			html += "</tr>";
			*/
			html += "</thead>";
			html +="<tbody >";
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.##",simbolo);
		    
			SimpleDateFormat formatoF1 = new SimpleDateFormat("dd/MM/yy");
			SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy/MM/dd");
			
			java.util.Date fIni = null;
			java.util.Date fFin = null;
			
			String fI = null;
			String fF = null;
			
			/*if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
					&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
				
				fIni =  formatoF1.parse(fechaIni);
				fFin =  formatoF1.parse(fechaFin);*/
				fI = fechaIni;
				fF = fechaFin;
			//}
			
			List<InvInsumos> row = invent.getDataListaSalidasLugares(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			//HashMap ttl_insumos = invent.getDataListaProdLugaresMin(cust_id, id_user, id_modulo, id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			for(int i = 0; i < row.size(); i ++){
			
				InvInsumos dto = row.get(i);
				
				//Generar total para cada marca
				String marca_ini = "";
				String marca_fin = "";
				
				String almacen_ini = "";
				String almacen_fin = "";
				
				String marca = "";
				
				
				//ArrayList arrayList = new ArrayList();----
				if(i == 0){
					//marca_ini = dto.getIdProductoTerm();
					//marca_fin = row.get(i+1).getIdProductoTerm();
					
					//almacen_ini = dto.getIdAlmacen();
					//almacen_fin = row.get(i+1).getIdAlmacen();
					//if(!marca_ini.equals(marca_fin)){
						html += "<tr>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>Cantidad</th>"; // Cantidad
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>#Salidas</th>"; // #Entradas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>Fecha</th>"; // Fecha
						html += "<th class=tablrWidget_headerCell align='right' valign='top'>Piezas</th>"; // Piezas
						html += "</tr>";
						
						//Productos Terminados
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getIdProductoTerm()+"</th>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
						
						html += "</tr>";
				}else{
					
					if(i+1 < row.size() ){
						marca_ini = dto.getIdProductoTerm();
						marca_fin = row.get(i-1).getIdProductoTerm();
						
						almacen_ini = dto.getIdAlmacen();
						almacen_fin = row.get(i-1).getIdAlmacen();
						
						if(!almacen_ini.equals(almacen_fin)){
							html += "<tr>";
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
							html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen();/*+"" +
									"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
											"</strong></th>"; //Producto*/
							
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
							html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
							html += "</tr>";
							
							//Productos Terminados
							html += "<tr>";
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getIdProductoTerm()+"</th>"; //Marca
							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
									"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
											"</strong></th>"; //Producto*/
							
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
							html += "</tr>";
						
						}
					}else{
						//if(i == row.size()){
						marca_ini = dto.getIdProductoTerm();
						marca_fin = row.get(i).getIdProductoTerm();
						
						almacen_ini = dto.getIdAlmacen();
						almacen_fin = row.get(i-1).getIdAlmacen();
						
						if(!almacen_ini.equals(almacen_fin)){
						html += "<tr>";
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; //Marca
						html += "<th class=tablrWidget_headerCell align='right' valign='top'><strong>"+dto.getDescAlmacen();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<th class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
						html += "</tr>";
						
						//Productos Terminados
						html += "<tr>";
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getIdProductoTerm()+"</th>"; //Marca
						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescProductoTerm();/*+"" +
								"<a href='javascript:hideRow("+dto.getDescProductoTerm()+")'>  -</a><a href='javascript:showRow("+dto.getIdProductoTerm()+")'> +</a>" +
										"</strong></th>"; //Producto*/
						
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
						html += "</tr>";
					}}
				}
				// Filas para los subproductos que integran a un producto
				
				html += "<tr class=mrc_"+dto.getItemcodeProd()+">";
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getIdProducto()+"</td>"; //Marca
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getDescProducto()+"</td>"; //Producto
				String cant = (String) dto.getCantidad();
				Double cantidad = 0.0;
				if(cant != null){
					cantidad = Double.parseDouble(cant);
				}
				if(i == 0){
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getIdProducto()+"</td>"; //Marca
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getDescProducto()+"</td>"; //Producto
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getSalidas()+"</td>"; // Existencias
				}else{
					String prod_ini = dto.getIdProducto();
					String prod_fin = row.get(i-1).getIdProducto();
					if(i+1 < row.size() ){
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getIdProducto()+"</td>"; //Marca
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getDescProducto()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getSalidas()+"</td>"; // Existencias
						}
					}else{
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getIdProducto()+"</td>"; //Marca
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getDescProducto()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getSalidas()+"</td>"; // Existencias
						}
					}
				}
				
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat frt = new SimpleDateFormat("dd-MMM-yy");
				java.util.Date fechaajuste = null;
				String fec = (String) dto.getFecha();
				fechaajuste = formateador.parse(fec);
				String fechaaj = frt.format(fechaajuste);
				String pzas = (String) dto.getPiezas();
				Double piezas = 0.0;
				if(pzas != null && !pzas.equals("null")){
					piezas = Double.parseDouble(pzas);
				}
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getCantidad()+"</td>"; // Piezas
				//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+dto.getSalidas()+"</td>"; // Existencias
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fechaaj+"</td>"; // Ajustes
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(piezas)+"</td>"; // #Productos
				html += "</tr>";
				
				
				//---------------------------------------////
				
				
				
			}
			
			html += "</tbody>";
			html += "</table> ";
			
			System.out.println("Tabla... ");
			return html;
		}
		
		public String getTableListaEntradasInsComp(
				String cust_id,
				String id_user,
				String id_modulo,
				String id_dashboard,
				String chart_id,
				String id_prod_term, 
				String fechaIni, 
				String fechaFin, 
				String idEspacios) throws UnsupportedEncodingException, ParseException {
			//Configuracion del portlet o grid
			HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
			//Filtros para el portlet o grid
			String cmp_filtro = (String) hm.get("cmp_id");
			String html = "";
			boolean org_param = false;
			int meses_enc = 3;
			int meses_col = 3;
			int meses_fil = 3;
			int meses_ttl = 3;
			int meses_marca = 3;
			
			html += "<table id=tblDatos class=tablesorter border=1 >";
			html += "<thead>";
			
			//Encabezado
			/*html += "<tr><td></td>";
			html += "<td></td>";
			html += "<td>Cantidad</td>";
			html += "<td>#Entradas</td>";
			html += "<td>Fechas</td>";
			html += "<td>Piezas</td>";
			html += "</tr>";
			*/
			String nombre = invent.getNombreInsComp(id_prod_term);
			html += "</thead>";
			html +="<tbody >";
			
			html += "<tr>";
			html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+nombre+"</th>";
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>Cantidad</th>"; // Cantidad
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>#Entradas</th>"; // #Entradas
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>Fecha</th>"; // Fecha
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>Piezas</th>"; // Piezas
			html += "</tr>";
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.##",simbolo);

			SimpleDateFormat formatoF1 = new SimpleDateFormat("dd/MM/yy");
			SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy/MM/dd");
			
			java.util.Date fIni = null;
			java.util.Date fFin = null;
			
			String fI = null;
			String fF = null;
			
			fI = fechaIni;
			fF = fechaFin;
			
			DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM);
			
			List<InvInsumos> row = invent.getDataListaEntradasLugaresInsComp(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			//HashMap ttl_insumos = invent.getDataListaProdLugaresMin(cust_id, id_user, id_modulo, id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			for(int i = 0; i < row.size(); i ++){
			
				InvInsumos dto = row.get(i);
				
				//Generar total para cada marca
				String marca_ini = "";
				String marca_fin = "";
				
				String almacen_ini = "";
				String almacen_fin = "";
				
				String marca = "";
				
				
				//ArrayList arrayList = new ArrayList();----
//				if(i == 0){
//					
//						html += "<tr>";
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescAlmacen()+"</th>";
//						
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//						
//						html += "</tr>";
//				}else{
//					
//					if(i+1 < row.size() ){
//						marca_ini = dto.getIdProductoTerm();
//						marca_fin = row.get(i-1).getIdProductoTerm();
//						
//						almacen_ini = dto.getIdAlmacen();
//						almacen_fin = row.get(i-1).getIdAlmacen();
//						
//						if(!almacen_ini.equals(almacen_fin)){
//							
//							//Lugares
//							html += "<tr>";
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescAlmacen()+"</th>";
//							
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//							html += "</tr>";
//						
//						}
//					}else{
//						//if(i == row.size()){
//						marca_ini = dto.getIdProductoTerm();
//						marca_fin = row.get(i).getIdProductoTerm();
//						
//						almacen_ini = dto.getIdAlmacen();
//						almacen_fin = row.get(i-1).getIdAlmacen();
//						
//						if(!almacen_ini.equals(almacen_fin)){
//							//Lugares
//							html += "<tr>";
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescAlmacen()+"</th>";
//							
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//						html += "</tr>";
//					}}
//				}
				// Filas para los subproductos que integran a un producto
								
				String cant = (String) dto.getCantidad();
				Double cantidad = 0.0;
				if(cant != null){
					cantidad = Double.parseDouble(cant);
				}
				
				if(i == 0){
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getDescAlmacen()+"</td>"; //Producto
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getEntradas()+"</td>"; // Existencias
				}else{
					String prod_ini = dto.getIdAlmacen();
					String prod_fin = row.get(i-1).getIdAlmacen();
					if(i+1 < row.size() ){
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getDescAlmacen()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getEntradas()+"</td>"; // Existencias
						}
					}else{
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getDescAlmacen()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getEntradas()+">"+dto.getEntradas()+"</td>"; // Existencias
						}
					}
				}
				
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat frt = new SimpleDateFormat("dd-MMM-yy");
				java.util.Date fechaajuste = null;
				String fec = (String) dto.getFecha();
				fechaajuste = formateador.parse(fec);
				String fechaaj = frt.format(fechaajuste);
				String pzas = (String) dto.getPiezas();
				Double piezas = 0.0;
				if(pzas != null && !pzas.equals("null")){
					piezas = Double.parseDouble(pzas);
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fechaaj+"</td>"; // Ajustes
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(piezas)+"</td>"; // #Productos
				html += "</tr>";
				
			}
			
			html += "</tbody>";
			html += "</table> ";
			
			System.out.println("Tabla... ");
			return html;
		}
	//Enlista las salidas por lugar
		public String getTableListaSalidasInsComp(
				String cust_id,
				String id_user,
				String id_modulo,
				String id_dashboard,
				String chart_id,
				String id_prod_term, 
				String fechaIni, 
				String fechaFin, 
				String idEspacios) throws UnsupportedEncodingException, ParseException {
			//Configuracion del portlet o grid
			HashMap hm = pptos.getDataChartConfig(chart_id, cust_id,id_modulo);
			//Filtros para el portlet o grid
			String cmp_filtro = (String) hm.get("cmp_id");
			String html = "";
			boolean org_param = false;
			int meses_enc = 3;
			int meses_col = 3;
			int meses_fil = 3;
			int meses_ttl = 3;
			int meses_marca = 3;
			
			html += "<table id=tblDatos class=tablesorter border=1 >";
			html += "<thead>";
			
			//Encabezado
			/*html += "<tr><td></td>";
			html += "<td></td>";
			html += "<td>Cantidad</td>";
			html += "<td>#Entradas</td>";
			html += "<td>Fechas</td>";
			html += "<td>Piezas</td>";
			html += "</tr>";
			*/
			String nombre = invent.getNombreInsComp(id_prod_term);
			html += "</thead>";
			html +="<tbody >";
			
			html += "<tr>";
			html += "<th class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'>"+nombre+"</th>";
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>Cantidad</th>"; // Cantidad
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>#Salidas</th>"; // #Salidas
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>Fecha</th>"; // Fecha
			html += "<th class=tablrWidget_headerCell align='right' valign='top'>Piezas</th>"; // Piezas
			html += "</tr>";
			
			DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
			simbolo.setDecimalSeparator('.');
			simbolo.setGroupingSeparator(',');
			DecimalFormat frmt = new DecimalFormat("###,###.##",simbolo);

			SimpleDateFormat formatoF1 = new SimpleDateFormat("dd/MM/yy");
			SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy/MM/dd");
			
			java.util.Date fIni = null;
			java.util.Date fFin = null;
			
			String fI = null;
			String fF = null;
			
			fI = fechaIni;
			fF = fechaFin;
			
			DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM);
			
			List<InvInsumos> row = invent.getDataListaSalidasLugaresInsComp(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			//HashMap ttl_insumos = invent.getDataListaProdLugaresMin(cust_id, id_user, id_modulo, id_dashboard, chart_id, id_prod_term, fI, fF, idEspacios);
			for(int i = 0; i < row.size(); i ++){
			
				InvInsumos dto = row.get(i);
				
				//Generar total para cada marca
				String marca_ini = "";
				String marca_fin = "";
				
				String almacen_ini = "";
				String almacen_fin = "";
				
				String marca = "";
				
				
				//ArrayList arrayList = new ArrayList();----
//				if(i == 0){
//					
//						html += "<tr>";
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescAlmacen()+"</th>";
//						
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//						html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//						
//						html += "</tr>";
//				}else{
//					
//					if(i+1 < row.size() ){
//						marca_ini = dto.getIdProductoTerm();
//						marca_fin = row.get(i-1).getIdProductoTerm();
//						
//						almacen_ini = dto.getIdAlmacen();
//						almacen_fin = row.get(i-1).getIdAlmacen();
//						
//						if(!almacen_ini.equals(almacen_fin)){
//							
//							//Lugares
//							html += "<tr>";
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescAlmacen()+"</th>";
//							
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//							html += "</tr>";
//						
//						}
//					}else{
//						//if(i == row.size()){
//						marca_ini = dto.getIdProductoTerm();
//						marca_fin = row.get(i).getIdProductoTerm();
//						
//						almacen_ini = dto.getIdAlmacen();
//						almacen_fin = row.get(i-1).getIdAlmacen();
//						
//						if(!almacen_ini.equals(almacen_fin)){
//							//Lugares
//							html += "<tr>";
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'><font color='#B40404'><strong>"+dto.getDescAlmacen()+"</th>";
//							
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Piezas
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Existencias
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // Ajustes
//							html += "<td class=tablrWidget_headerCell align='right' valign='top'></th>"; // #Productos
//						html += "</tr>";
//					}}
//				}
				// Filas para los subproductos que integran a un producto
								
				String cant = (String) dto.getCantidad();
				Double cantidad = 0.0;
				if(cant != null){
					cantidad = Double.parseDouble(cant);
				}
				
				if(i == 0){
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getDescAlmacen()+"</td>"; //Producto
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
					html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getSalidas()+"</td>"; // Existencias
				}else{
					String prod_ini = dto.getIdAlmacen();
					String prod_fin = row.get(i-1).getIdAlmacen();
					if(i+1 < row.size() ){
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getDescAlmacen()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getSalidas()+"</td>"; // Existencias
						}
					}else{
						
						if(!prod_ini.equals(prod_fin)){
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getDescAlmacen()+"</td>"; //Producto
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+frmt.format(cantidad)+"</td>"; // Piezas
							html += "<td class=tablrWidget_headerCell valign='middle' rowspan="+dto.getSalidas()+">"+dto.getSalidas()+"</td>"; // Existencias
						}
					}
				}
				
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat frt = new SimpleDateFormat("dd-MMM-yy");
				java.util.Date fechaajuste = null;
				String fec = (String) dto.getFecha();
				fechaajuste = formateador.parse(fec);
				String fechaaj = frt.format(fechaajuste);
				String pzas = (String) dto.getPiezas();
				Double piezas = 0.0;
				if(pzas != null && !pzas.equals("null")){
					piezas = Double.parseDouble(pzas);
				}
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+fechaaj+"</td>"; // Ajustes
				html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+frmt.format(piezas)+"</td>"; // #Productos
				html += "</tr>";
				
			}
			
			html += "</tbody>";
			html += "</table> ";
			
			System.out.println("Tabla... ");
			return html;
		}
	//Metodo para obtener los valores por Producto
	public HashMap getDatosPorProd(String id_prod, int numMes){
		HashMap ctacve = new HashMap();
		String limite = "";
		String sql = "SELECT " +
				"producto, " +
				" ttl_cajas_mes1," +
				" ttl_cajas_mes2," +
				" ttl_cajas_mes3," +
				" ttl_cajas_mes4," +
				" ttl_cajas_mes5," +
				" ttl_cajas_mes6," +
				" ttl_cajas_mes7," +
				" ttl_cajas_mes8," +
				" ttl_cajas_mes9," +
				" ttl_cajas_mes10," +
				" ttl_cajas_mes11," +
				" ttl_cajas_mes12 " +
				"diasp1, " +
				"cajasp1, " +
				"diasp2, " +
				"cajasp2, " +
				"diasp3, " +
				"cajasp3 " +
				"FROM " +
				"prev_compras" +
				" WHERE " +
				"id_producto='"+id_prod+"'";
		System.out.println("Limite CtaCve: "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		
		
		while(rs.next()){
			String diasP1 = rs.getString("diasp1");
			String diasP2 = rs.getString("diasp2");
			String diasP3 = rs.getString("diasp3");
			String cajasP1 = rs.getString("cajasp1");
			String cajasP2 = rs.getString("cajasp2");
			String cajasP3 = rs.getString("cajasp3");
			
			if(diasP1 == null){
				diasP1 = "0";
			}
			if(diasP2 == null){
				diasP2 = "0";
			}
			if(diasP3 == null){
				diasP3 = "0";
			}
			if(cajasP1 == null){
				cajasP1 = "0";
			}
			if(cajasP2 == null){
				cajasP2 = "0";
			}
			if(cajasP3 == null){
				cajasP3 = "0";
			}
			Double total = 0.0;
			Double promedio = 0.0;

			String ttl_mes = "0"; 
			for(int xMes = 1; xMes <= numMes; xMes++ ){
				ttl_mes = rs.getString("ttl_cajas_mes"+xMes);
				if(ttl_mes == null || ttl_mes.isEmpty() || ttl_mes == "null"){
					ttl_mes = "0";
				}
				total += Double.parseDouble(ttl_mes);
			}
			promedio = total / (28 * numMes);
			if(promedio.isNaN() || promedio.isInfinite()){
				promedio = 0.0;
			}
			System.out.println("Promedio: "+ promedio);
			ctacve.put("producto", rs.getString("producto"));
			ctacve.put("diasP1", diasP1);
			ctacve.put("diasP2", diasP2);
			ctacve.put("diasP3", diasP3);
			ctacve.put("cajasP1", cajasP1);
			ctacve.put("cajasP2", cajasP2);
			ctacve.put("cajasP3", cajasP3);
			ctacve.put("promedio", promedio);
		}
		return ctacve;
	}
	
	//Obtiene datos simulados
	public HashMap getDatosPorProdSim(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_prod){
		HashMap ctacve = new HashMap();
		String limite = "";
		String sql = "SELECT  diasp1, " +
				"cajasp1, " +
				"diasp2, " +
				"cajasp2, " +
				"diasp3, " +
				"cajasp3 " +
				"FROM invent_simulacion " +
				"WHERE id_producto='"+id_prod+"' " +
				"AND id_customer='"+id_customer+"' " +
				"AND id_user='"+id_user+"' " +
				"AND id_modulo='"+id_modulo+"' " +
				"AND id_dashboard='"+id_dashboard+"'";// AND id_portlet='"+id_portlet+"'";
		System.out.println("Productos Simuladps: "+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		
		
		while(rs.next()){
			String diasP1 = rs.getString("diasp1");
			String diasP2 = rs.getString("diasp2");
			String diasP3 = rs.getString("diasp3");
			String cajasP1 = rs.getString("cajasp1");
			String cajasP2 = rs.getString("cajasp2");
			String cajasP3 = rs.getString("cajasp3");
			
			if(diasP1 == null){
				diasP1 = "0";
			}
			if(diasP2 == null){
				diasP2 = "0";
			}
			if(diasP3 == null){
				diasP3 = "0";
			}
			if(cajasP1 == null){
				cajasP1 = "0";
			}
			if(cajasP2 == null){
				cajasP2 = "0";
			}
			if(cajasP3 == null){
				cajasP3 = "0";
			}
			
			//ctacve.put("producto", rs.getString("producto"));
			ctacve.put("diasP1", diasP1);
			ctacve.put("diasP2", diasP2);
			ctacve.put("diasP3", diasP3);
			ctacve.put("cajasP1", cajasP1);
			ctacve.put("cajasP2", cajasP2);
			ctacve.put("cajasP3", cajasP3);
			
		}
		return ctacve;
	}
	
	//Detalle Por Producto
	//Tabla analisis de ventas
	public String getDetalleProd(
					String cust_id,
					String id_user,
					String id_modulo,
					String id_dashboard,
					String chart_id,
					String id_producto,
					String mes) throws UnsupportedEncodingException, ParseException {
				
				int meses_fil = 4;
				
				if(mes == null || mes == "" || mes.equals("null")){
				}else{
					meses_fil = Integer.parseInt(mes);
				}
				
				HashMap detalle_producto = new HashMap();
			    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
				
			    Double in_diasP1 = 0.0;
				Double in_diasP2 = 0.0;
				Double in_diasP3 = 0.0;
				Double in_cajasP1 = 0.0;
				Double in_cajasP2 = 0.0;
				Double in_cajasP3 = 0.0;
				String tbl = "";
				
				String fechaL = invent.obtieneFechaCargaDatos();
			    System.out.println("Fecha De Carga--->"+fechaL);
			    SimpleDateFormat formatoF2 = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date fLoad = null;
				
				List<InvPrevComp> row = invent.getDataAnalisisVentasDetalleProd(cust_id,  id_user,  id_modulo,  id_dashboard, chart_id, id_producto);
				HashMap hm_meses = invent.getDataMesesAnalisisVentasDetalleProd(cust_id, id_user, id_modulo, id_dashboard, meses_fil, chart_id, id_producto);
				//HashMap ttl_marcas = invent.getDataMesesAnalisisVentasMarcas(cust_id, id_user, id_modulo, id_dashboard, meses_ttl, chart_id);
				
				Calendar dias_llegada_dia_P1 = new GregorianCalendar();
			    Calendar dias_llegada_dia_P2 = new GregorianCalendar();
			    Calendar dias_llegada_dia_P3 =  new GregorianCalendar();
			    Calendar DDI_al_llegar_dia_P1 = new GregorianCalendar();
			    Calendar DDI_al_llegar_dia_P2 = new GregorianCalendar();
			    Calendar DDI_al_llegar_dia_P3 =  new GregorianCalendar();
			    Calendar fecha_hoy = new GregorianCalendar();
			    
				for(int i = 0; i < row.size(); i ++){
					String marca_ttl ="";
					
					if(fechaL != null && !fechaL.equals("null") && !fechaL.isEmpty()){
						fLoad =  formatoF2.parse(fechaL);
						fecha_hoy.setTime(fLoad);
					}else{
						fecha_hoy.add(Calendar.DATE , -1);
					}
					
					InvPrevComp dto = row.get(i);
					if(mes == null || mes == "" || mes.equals("null")){
						meses_fil = 4;
					}else{
						meses_fil = Integer.parseInt(mes);
					}
					Calendar cal = new GregorianCalendar();
					DecimalFormat formatter = new DecimalFormat("###,###,##0.000");
					String mes_prb = (String) hm_meses.get(dto.getIdProd()+"_ttl");
					
					Double ttl_cajas = 0.0;
					//System.out.println("Null "+mes_prb);
					if(mes_prb != null){
						ttl_cajas = Double.parseDouble(mes_prb);
					}
					int perd_dias = meses_fil * 28;
							
					BigDecimal bd_ttl_cajas = new BigDecimal(ttl_cajas);
					BigDecimal rd_ttl_cajas = bd_ttl_cajas.setScale(1, RoundingMode.HALF_UP);
					
					
					Double clc_prom_diario = ttl_cajas / perd_dias;
					System.out.println("Promedio --> "+clc_prom_diario);
					BigDecimal prom_diario = new BigDecimal(clc_prom_diario);
					BigDecimal rd_prom_diario = prom_diario.setScale(1, RoundingMode.HALF_UP);
					Double existencia = Double.parseDouble(dto.getTtlExist());
					System.out.println("Existencias --> "+existencia);
					BigDecimal bd_existencia = new BigDecimal(existencia);
					BigDecimal rd_existecia = bd_existencia.setScale(1, RoundingMode.HALF_UP);
					
					Double dias_invent = existencia / clc_prom_diario;
					System.out.println("Diasinvent: "+dias_invent);
					Double pendiente_x_fact = Double.parseDouble(dto.getPndXFact());
					BigDecimal bd_pendiente_x_fact = new BigDecimal(pendiente_x_fact);
					BigDecimal rd_pendiente_x_fact = bd_pendiente_x_fact.setScale(1, RoundingMode.HALF_UP); 
					Double disponible = existencia - pendiente_x_fact;
					BigDecimal bd_disponible = new BigDecimal(disponible);
					BigDecimal rd_disponible = bd_disponible.setScale(1,RoundingMode.HALF_UP);
					
					Double dia_inventario = disponible / clc_prom_diario;
					System.out.println("Dias Invet "+dia_inventario);
					Double costo = Double.parseDouble(dto.getCosto());
					Double costo_final = disponible * costo;
					int dia_invent_fin = (int) (Math.round(dia_inventario) - 1);
					
					
					//System.out.println("Costo: -------- " +disponible+" * "+ costo);
					BigDecimal bd_costo_final = new BigDecimal(costo_final);
					BigDecimal rd_costo_final = bd_costo_final.setScale(0, RoundingMode.HALF_UP);
					cal.add(Calendar.DATE, dia_invent_fin);
					
					
					//Genera los periodos a mostrar
					for (int x = 1; x <= meses_fil; meses_fil --){
						String prb_ttl_x_mes = (String) hm_meses.get(dto.getIdProd()+"_"+meses_fil);
						Double ttl_cajas_x_mes = 0.0;
						if(prb_ttl_x_mes != null){
							ttl_cajas_x_mes =Double.parseDouble(prb_ttl_x_mes);
						}
						//System.out.println("Cve: "+dto.getKey()+" Mes_"+meses_fil+" ttl_mes: "+ttl_cajas_x_mes);
						BigDecimal ttl_cajas_mes = new BigDecimal(ttl_cajas_x_mes);
						BigDecimal rd_ttl_cajas_mes = ttl_cajas_mes.setScale(1, RoundingMode.HALF_UP);
						//html += "<td class=tablrWidget_headerCell align='right' valign='top'>"+rd_ttl_cajas_mes+"</td>"; //Mes4
					}

					Double tmpo_mtnr_stock = 0.0;
					Double min_stock = Double.parseDouble(dto.getMinStock());
					Double tiempo_prov = Double.parseDouble(dto.getTiempoProv());
					tmpo_mtnr_stock = (dia_inventario - min_stock) - tiempo_prov;
					
					
					
					double diasP1 = Double.parseDouble(dto.getDiasP1());
					double diasP2 = Double.parseDouble(dto.getDiasP2());
					double diasP3 = Double.parseDouble(dto.getDiasP3());
					double cajasP1 = Double.parseDouble(dto.getCajasP1());
					double cajasP2 = Double.parseDouble(dto.getCajasP2());
					double cajasP3 = Double.parseDouble(dto.getCajasP3());
					
					Double ddiP1_prod = (cajasP1 + in_cajasP1) / clc_prom_diario;
					Double ddiP2_prod = (cajasP2 + in_cajasP2) / clc_prom_diario;
					Double ddiP3_prod = (cajasP3 + in_cajasP3) / clc_prom_diario;
					
					//Double beOculto_prod = (ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) / min_stock;
					Double beOculto_prod = (((ddiP1_prod + ddiP2_prod + ddiP3_prod + dia_inventario) - min_stock) * cajasP1) / cajasP1;
					Double tmpo_lvtr_ped = beOculto_prod - tiempo_prov;
					if(tmpo_lvtr_ped.isInfinite() || tmpo_lvtr_ped.isNaN()){
						tmpo_lvtr_ped = 0.0;
					}
					if(tmpo_mtnr_stock.isInfinite() || tmpo_mtnr_stock.isNaN()){
						tmpo_mtnr_stock = 0.0;
					}
					//Double tmpo_lvtr_ped = (ddiP1 + ddiP2 + ddiP3 + dia_inventario) - min_stock;
					
					//Calculo #Dias De Llegada
					Date fechaP1 = (Date) dto.getFechaP1();
					Date fechaP2 = (Date) dto.getFechaP2();
					Date fechaP3 = (Date) dto.getFechaP3();
					
					Calendar inicio1 =  new GregorianCalendar();
					Calendar inicio2 =  new GregorianCalendar();
					Calendar inicio3 =  new GregorianCalendar();
					
					Calendar finP1 =  new GregorianCalendar();
					Calendar finP2 =  new GregorianCalendar();
					Calendar finP3 =  new GregorianCalendar();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					
					inicio1.setTime(fLoad);
					inicio2.setTime(fLoad);
					inicio3.setTime(fLoad);
					long ini = 0, fin1 = 0, fin2 = 0, fin3 = 0, difP1 = 0, difP2 = 0, difP3 = 0, difDiasP1 = 0, difDiasP2 = 0, difDiasP3 = 0;
					//ini = inicioDif.getTimeInMillis();
					int milsDia = (24 * 60 * 60 * 1000);
					if(fechaP1 != null){
						
						java.util.Date fFechaI = null;
						java.util.Date fFechaF = null;
						
						String frmFecha1 = sdf.format(fechaP1.getTime());
						String frmInicio = sdf.format(inicio1.getTime());
						
						fFechaI = sdf.parse(frmInicio);
						fFechaF = sdf.parse(frmFecha1);
						
						finP1.setTime(fFechaF);
						inicio1.setTime(fFechaI);
						
						difP1 = finP1.getTimeInMillis() - inicio1.getTimeInMillis() ;
						
						difDiasP1 = difP1/(24 * 60 * 60 * 1000);
									
					}
					//System.out.println("DiasPAraQueLlegue: "+difDiasP1);
					//System.out.println("DiasIntroducir: "+in_diasP1);
					if(fechaP2 != null){
						
						java.util.Date fFechaI = null;
						java.util.Date fFechaF = null;
						
						String frmFecha2 = sdf.format(fechaP2.getTime());
						String frmInicio = sdf.format(inicio2.getTime());
						
						fFechaI = sdf.parse(frmInicio);
						fFechaF = sdf.parse(frmFecha2);
						
						finP2.setTime(fFechaF);
						inicio2.setTime(fFechaI);
						
						difP2 = finP2.getTimeInMillis() - inicio2.getTimeInMillis() ;
						
						difDiasP2 = difP2/(24 * 60 * 60 * 1000);
					}
					if(fechaP3 != null){
						java.util.Date fFechaI = null;
						java.util.Date fFechaF = null;
						
						String frmFecha3 = sdf.format(fechaP3.getTime());
						String frmInicio = sdf.format(inicio3.getTime());
						
						fFechaI = sdf.parse(frmInicio);
						fFechaF = sdf.parse(frmFecha3);
						
						finP3.setTime(fFechaF);
						inicio3.setTime(fFechaI);
						
						difP3 = finP3.getTimeInMillis() - inicio3.getTimeInMillis() ;
						
						difDiasP3 = difP3/(24 * 60 * 60 * 1000);
					}
					//------------------------------
					BigDecimal _tmpo_mtnr_stock = new BigDecimal(tmpo_mtnr_stock);
					BigDecimal rd_tmpo_mtnr_stock = _tmpo_mtnr_stock.setScale(1, RoundingMode.HALF_UP);
					BigDecimal _tmpo_lvtr_ped = new BigDecimal(tmpo_lvtr_ped);
					BigDecimal rd_tmpo_lvtr_ped = _tmpo_lvtr_ped.setScale(1, RoundingMode.HALF_UP);

					String nom_producto = (String) dto.getDescProd();
					
					Double contExistMin_1 = 0.0;
					Double contExistMin_2 = 0.0;
					Double contExistMin_3 = 0.0;
					
					Double ddiDiasAlLlegarP1 = 0.0;
					Double ddiDiasAlLlegarP2 = 0.0;
					Double ddiDiasAlLlegarP3 = 0.0;
					
					Double ddiPedDiasP1 = 0.0;
					Double ddiPedDiasP2 = 0.0;
					Double ddiPedDiasP3 = 0.0;
					
					Double cajasAlLlegarP1 = 0.0;
					Double cajasAlLlegarP2 = 0.0;
					Double cajasAlLlegarP3 = 0.0;
					
					Double min_stock1 = clc_prom_diario * min_stock;
					
					ddiPedDiasP1 = (cajasP1 + in_cajasP1) / clc_prom_diario;
					ddiDiasAlLlegarP1 = ((((dias_invent - (diasP1 + in_diasP1) + ddiPedDiasP1) - min_stock) / ddiPedDiasP1) * ddiPedDiasP1);
					//contExistMin_1 = (((dias_invent - (diasP1 + in_diasP1)- min_stock))/ddiDiasAlLlegarP1) * ddiDiasAlLlegarP1;
					
					//ddiPedDiasP1 = (cajasP1 + in_cajasP1) / clc_prom_diario;
					cajasAlLlegarP1 = ((((disponible + cajasP1 + in_cajasP1) - ((difDiasP1 + in_diasP1) * clc_prom_diario)) - min_stock1) / ddiPedDiasP1) * ddiPedDiasP1;
					///contExistMin_1 = ((((dias_invent_f - (difDiasP1 + in_diasP1)- min_stock)) * clc_prom_diario) / cajasAlLlegarP1) * cajasAlLlegarP1;
					contExistMin_1 = (((dia_inventario - (difDiasP1 + in_diasP1)- min_stock))/ddiDiasAlLlegarP1) * ddiDiasAlLlegarP1;
					System.out.println("Detalle 1)----> ((("+dia_inventario +"- ("+difDiasP1 +"+"+ in_diasP1+")-"+ min_stock+"))/"+ddiDiasAlLlegarP1+") * "+ddiDiasAlLlegarP1+"="+contExistMin_1);
					if(ddiPedDiasP1.isInfinite() || ddiPedDiasP1.isNaN()){
						ddiPedDiasP1 = 0.0;
					}
					BigDecimal _ddiPedDiasP1 = new BigDecimal(ddiPedDiasP1);
					BigDecimal rd_ddiPedDiasP1 = _ddiPedDiasP1.setScale(1, RoundingMode.HALF_UP);
					if(ddiDiasAlLlegarP1.isInfinite() || ddiDiasAlLlegarP1.isNaN()){
						ddiDiasAlLlegarP1 = 0.0;
					}
					BigDecimal _ddiDiasP1 = new BigDecimal(ddiDiasAlLlegarP1);
					BigDecimal rd_ddiDiasP1 = _ddiDiasP1.setScale(0, RoundingMode.HALF_UP);
					if(contExistMin_1.isInfinite() || contExistMin_1.isNaN()){
						contExistMin_1 = 0.0;
					}
					//System.out.println("Detalle 2)----> "+contExistMin_1);
					BigDecimal _contExistMin_1 = new BigDecimal(contExistMin_1);
					BigDecimal rd_contExistMin_1 = _contExistMin_1.setScale(1, RoundingMode.HALF_UP);
					
					ddiPedDiasP2 = (cajasP2 + in_cajasP2) / clc_prom_diario;
					ddiDiasAlLlegarP2 = ((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP2 + in_diasP2) - min_stock) / ddiPedDiasP2) * ddiPedDiasP2);
					contExistMin_2 = ((((dia_inventario + ddiPedDiasP1) - (difDiasP2 + in_diasP2)- min_stock))/ddiDiasAlLlegarP2) * ddiDiasAlLlegarP2;
					if(ddiPedDiasP2.isInfinite() || ddiPedDiasP2.isNaN()){
						ddiPedDiasP2 = 0.0;
					}
					BigDecimal _ddiPedDiasP2 = new BigDecimal(ddiPedDiasP2);
					BigDecimal rd_ddiPedDiasP2 = _ddiPedDiasP2.setScale(1, RoundingMode.HALF_UP);
					if(ddiDiasAlLlegarP2.isInfinite() || ddiDiasAlLlegarP2.isNaN()){
						ddiDiasAlLlegarP2 = 0.0;
					}
					BigDecimal _ddiDiasP2 = new BigDecimal(ddiDiasAlLlegarP2);
					BigDecimal rd_ddiDiasP2 = _ddiDiasP2.setScale(0, RoundingMode.HALF_UP);
					if(contExistMin_2.isInfinite() || contExistMin_2.isNaN()){
						contExistMin_2 = 0.0;
					}
					BigDecimal _contExistMin_2 = new BigDecimal(contExistMin_2);
					BigDecimal rd_contExistMin_2 = _contExistMin_2.setScale(1, RoundingMode.HALF_UP);
					
					
					ddiPedDiasP3 = (cajasP3 + in_cajasP3) / clc_prom_diario;
					ddiDiasAlLlegarP3 = ((((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3)) + ddiPedDiasP3) - min_stock) / ddiPedDiasP3) * ddiPedDiasP3);
					contExistMin_3 = ((((dia_inventario + ddiPedDiasP1 + ddiPedDiasP2) - (difDiasP3 + in_diasP3) - min_stock))/ ddiDiasAlLlegarP3) * ddiDiasAlLlegarP3;
					if(ddiPedDiasP3.isInfinite() || ddiPedDiasP3.isNaN()){
						ddiPedDiasP3 = 0.0;
					}
					BigDecimal _ddiPedDiasP3 = new BigDecimal(ddiPedDiasP3);
					BigDecimal rd_ddiPedDiasP3 = _ddiPedDiasP3.setScale(1, RoundingMode.HALF_UP);
					if(ddiDiasAlLlegarP3.isInfinite() || ddiDiasAlLlegarP3.isNaN()){
						ddiDiasAlLlegarP3 = 0.0;
					}
					BigDecimal _ddiDiasP3 = new BigDecimal(ddiDiasAlLlegarP3);
					BigDecimal rd_ddiDiasP3 = _ddiDiasP3.setScale(0, RoundingMode.HALF_UP);   
					if(contExistMin_3.isInfinite() || contExistMin_3.isNaN()){
						contExistMin_3 = 0.0;
					}
					BigDecimal _contExistMin_3 = new BigDecimal(contExistMin_3);
					BigDecimal rd_contExistMin_3 = _contExistMin_3.setScale(1, RoundingMode.HALF_UP);
					
					int i_diasP1 = (int) diasP1;
					int i_diasP2 = (int) diasP2;
					int i_diasP3 = (int) diasP3;
					dias_llegada_dia_P1.add(fecha_hoy.DATE, i_diasP1);
					dias_llegada_dia_P2.add(fecha_hoy.DATE, i_diasP2);
					dias_llegada_dia_P3.add(fecha_hoy.DATE, i_diasP3);
					int i_ddiAlLlegarP1 = Integer.valueOf(rd_ddiDiasP1.intValueExact());
					int i_ddiAlLlegarP2 = Integer.valueOf(rd_ddiDiasP2.intValueExact());
					int i_ddiAlLlegarP3 = Integer.valueOf(rd_ddiDiasP3.intValueExact());
					DDI_al_llegar_dia_P1.add(fecha_hoy.DATE, (i_diasP1 + i_ddiAlLlegarP1));
					DDI_al_llegar_dia_P2.add(fecha_hoy.DATE, (i_diasP2 + i_ddiAlLlegarP2));
					DDI_al_llegar_dia_P3.add(fecha_hoy.DATE, (i_diasP1 + i_ddiAlLlegarP3));
					
					BigDecimal bd_cajasP1 = new BigDecimal(cajasP1);
					BigDecimal rd_cajasP1 = bd_cajasP1.setScale(1, RoundingMode.HALF_UP);
					BigDecimal bd_cajasP2 = new BigDecimal(cajasP2);
					BigDecimal rd_cajasP2 = bd_cajasP2.setScale(1, RoundingMode.HALF_UP);
					BigDecimal bd_cajasP3 = new BigDecimal(cajasP3);
					BigDecimal rd_cajasP3 = bd_cajasP3.setScale(1, RoundingMode.HALF_UP);
					
					
					DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
					simbolo.setDecimalSeparator('.');
					simbolo.setGroupingSeparator(',');
					DecimalFormat frmt = new DecimalFormat("###,###.####",simbolo);
					if(dia_inventario.isNaN() || dia_inventario.isInfinite()){
						dia_inventario = 0.00;
					}
					BigDecimal diasInvent = new BigDecimal( Math.abs(dia_inventario));
					BigDecimal rd_diasInvent = diasInvent.setScale(1, RoundingMode.HALF_UP);
					//System.out.println("DiasInventario -- "+dias_invent);
					
					//System.out.println("<<< "+dto.getFrmFechaP1()+"__"+fechaP2+"__"+fechaP3);
					detalle_producto.put("nom_producto", nom_producto);
					detalle_producto.put("prom_dia", frmt.format(rd_prom_diario));
					detalle_producto.put("existencias", frmt.format(rd_existecia));
					detalle_producto.put("dias_invent", String.valueOf(rd_diasInvent));
					detalle_producto.put("fecha_invent", formatoDeFecha.format(cal.getTime()));
					detalle_producto.put("costo", frmt.format(rd_costo_final));
					detalle_producto.put("limite_stock", frmt.format(rd_tmpo_mtnr_stock));
					detalle_producto.put("limite_pedido", frmt.format(rd_tmpo_lvtr_ped));
					
					detalle_producto.put("diasLlegada_diasP1", difDiasP1);
					detalle_producto.put("diasLlegada_fechaP1", dto.getFrmFechaP1());
					detalle_producto.put("compra_cajasP1", frmt.format(rd_cajasP1));
					System.out.println("Rd_Detalle ---> " + rd_contExistMin_1);
					detalle_producto.put("cont_exist_minP1", frmt.format(rd_contExistMin_1));
					
					detalle_producto.put("diasLlegada_diasP2", difDiasP2);
					detalle_producto.put("diasLlegada_fechaP2", dto.getFrmFechaP2());
					detalle_producto.put("compra_cajasP2", frmt.format(rd_cajasP2));
					detalle_producto.put("cont_exist_minP2", frmt.format(rd_contExistMin_2));
					
					detalle_producto.put("diasLlegada_diasP3", difDiasP3);
					detalle_producto.put("diasLlegada_fechaP3", dto.getFrmFechaP3());
					detalle_producto.put("compra_cajasP3", frmt.format(rd_cajasP3));
					detalle_producto.put("cont_exist_minP3", frmt.format(rd_contExistMin_3));
					
					
					/*Regresar Tabla Detalle de Producto*/
					String colProm = "";
					String colExist = "";
					String colDiasInv = "";
					String colCosto = "";
					String colMtnrStock = "";
					String colLvtrPed = "";
					if(rd_prom_diario.compareTo(BigDecimal.ZERO) < 0.0){
						colProm = "red";
					}
					if(rd_tmpo_lvtr_ped.compareTo(BigDecimal.ZERO) < 0.0){
						colLvtrPed = "red";
					}
					if(rd_existecia.compareTo(BigDecimal.ZERO) < 0.0){
						colExist = "red";
					}
					if(rd_diasInvent.compareTo(BigDecimal.ZERO) < 0.0){
						colDiasInv = "red";
					}
					if(rd_costo_final.compareTo(BigDecimal.ZERO) < 0.0){
						colCosto = "red";
					}
					if(rd_tmpo_mtnr_stock.compareTo(BigDecimal.ZERO) < 0.0){
						System.out.println("---------------> ");
						colMtnrStock = "red";
					}
					tbl += "<div id='box'>" +
							"<h3>Producto: "+nom_producto+"</h3>"+
							"<table class=tablesorter border=1 >"+
							"<thead>" +
							"<tr>" +
							"<th align='center'>Prom/Dia</th>" +
							"<th align='center'>Existencias</th>" +
							"<th colspan='2' align='center'>Dias Inventario</th>" +
							"<th align='center'>Costo</th>" +
							"<th align='center'>Limite Stock</th>" +
							"<th align='center'>Limite Pedido</th>" +
							"</tr>" +
							"</thead>" +
							"<tr>" +
							"<th align='right'> <font color="+colProm+">"+frmt.format(rd_prom_diario)+"</font></th>" +
							"<th align='right'> <font color="+colExist+">"+frmt.format(rd_existecia)+"</font></th>" +
							"<th align='right'> <font color="+colDiasInv+">"+rd_diasInvent+"</font></th>"+
							"<th align='center'> "+formatoDeFecha.format(cal.getTime())+"</th>" +
							"<th align='right'><font color="+colCosto+">"+frmt.format(rd_costo_final)+"</font></th>"+
							"<th align='right'><font color='"+colMtnrStock+"' >"+frmt.format(rd_tmpo_mtnr_stock)+"</font></th>"+
							"<th align='right'><font color="+colLvtrPed+">"+frmt.format(rd_tmpo_lvtr_ped)+"</font></th>"+
							"</tr>" +
							"</table>";
							


						tbl += "<table class=tablesorter border=1 >"+
								" <thead>" +
								"<tr>";
						if(dto.getFrmFechaP1() != null){ 
							tbl += "<th colspan='4' align='center'>Pedido 1</th>";
						}
						if(dto.getFrmFechaP2() != null){
							tbl += "<th colspan='4' align='center'>Pedido 2</th>";
						}
						if(dto.getFrmFechaP3() != null){
							tbl += "<th colspan='4' align='center'>Pedido 3</th>";
						}
						tbl += "</tr>" +
								"<tr>";
						
						if(dto.getFrmFechaP1() != null){ 
							tbl +="<th colspan='2' align='center'>#Dias Para Que Lllegue</th>"+
							"<th align='center'>Compra/Cajas</th>"+
							"<th align='center'>Continuidad Existencia Al Minimo</th>";
						}
						if(dto.getFrmFechaP2() != null){
							tbl += "<th colspan='2' align=''enter'>#Dias Para Que Lllegue</th>"+
							"<th align='center'>Compra/Cajas</th>"+
							"<th align='center'>Continuidad Existencia Al Minimo</th>";
						}
						if(dto.getFrmFechaP3() != null){
							tbl += "<th colspan='2' align='center'>#Dias Para Que Lllegue</th>"+
							"<th align='center'>Compra/Cajas</th>"+
							"<th align='center'>Continuidad Existencia Al Minimo</th>";
						} 
						tbl += "</tr>" +
								"</thead>" +
								"<tr>";
				String color1 = "";
				String color2 = "";
				String color3 = "";
						if(dto.getFrmFechaP1() != null){
							color1 = "";
							color2 = "";
							color3 = "";
							if(difDiasP1 < 0){
								color1 = "red";
							}
							if(rd_cajasP1.compareTo(BigDecimal.ZERO) < 0.0){
								color2 = "red";
							}
							if(rd_contExistMin_1.compareTo(BigDecimal.ZERO) < 0.0){
								color3 = "red";
							}
							tbl += "<th align='right'><font color="+color1+">"+difDiasP1+"</font></th>" +
									"<th align='center'>"+dto.getFrmFechaP1()+"</th>"+
									"<th align='right'><font color="+color2+">"+frmt.format(rd_cajasP1)+"</font></th>" +
									"<th align='right'><font color="+color3+">"+frmt.format(rd_contExistMin_1)+"</font></th>";
						}
						if(dto.getFrmFechaP2() != null){
							color1 = "";
							color2 = "";
							color3 = "";
							if(difDiasP2 < 0){
								color1 = "red";
							}
							if(rd_cajasP2.compareTo(BigDecimal.ZERO) < 0.0){
								color2 = "red";
							}
							if(rd_contExistMin_2.compareTo(BigDecimal.ZERO) < 0.0){
								color3 = "red";
							}
							tbl += "<th align='right'><font color="+color1+">"+difDiasP2+"</font></th>" +
									"<th align='center'>"+dto.getFrmFechaP2()+"</th>"+
									"<th align='right'><font color="+color2+">"+frmt.format(rd_cajasP2)+"</font></th>" +
									"<th align='right'><font color="+color3+">"+frmt.format(rd_contExistMin_2)+"</font></th>";
						}
						if(dto.getFrmFechaP3() != null){
							color1 = "";
							color2 = "";
							color3 = "";
							if(difDiasP3 < 0){
								color1 = "red";
							}
							if(rd_cajasP3.compareTo(BigDecimal.ZERO) < 0.0){
								color2 = "red";
							}
							if(rd_contExistMin_3.compareTo(BigDecimal.ZERO) < 0.0){
								color3 = "red";
							}
							tbl += "<th align='right'><font color="+color1+">"+difDiasP3+"</font></th>" +
									"<th align='center'>"+dto.getFrmFechaP3()+"</th>"+
									"<th align='right'><font color="+color2+">"+frmt.format(rd_cajasP3)+"</font></th>" +
									"<th align='right'><font color="+color3+">"+frmt.format(rd_contExistMin_3)+"</font></th>";
						}
						tbl += "</tr>" +
								"</table>" +
								"</div>";
				}
				System.out.println("tabla -----> "+tbl);
				return tbl;
			}
			
	public String drawMenu(String cli_id, String id_modulo, String id_user, String id_dashboard, String id_chart, String id_menu, String cmp_id){
		String menu = "";
		System.out.println("1) Portlet: "+ id_chart+" Menu: "+id_menu);
		String filtroIn = datosFiltro(cli_id, id_modulo, id_user, id_dashboard, id_chart, cmp_id);
		
		String sql = invent.getMenu(cli_id, id_modulo, id_user,  id_dashboard, id_menu, filtroIn, cmp_id, id_chart);
		System.out.println("sql-drawMenu: "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			if(rs.getString("id") != null){
				menu += "<option value="+rs.getString("id")+">"+rs.getString("nom")+"</option>";
			}
		}
		
		return menu;
	}
	
	public String drawMenuFiltro(String cli_id, String id_user, String id_modulo, String id_dashboard, String id_chart, String id_menu, String cmp_id){
		
		String menu = "";
		String fact = "";
		String dim_sop = "";
		String cmp_desc = "";
		String id = "";
		String idfk = "";
		String elemento = "";
		String orden = "";
		String select = "";
		
		HashMap<String,String> hm = new HashMap<String,String>();
		System.out.println("2) Portlet: "+ id_chart+" Menu: "+id_menu);
		String filtroIn = datosFiltro(cli_id, id_user, id_modulo, id_dashboard, id_chart, cmp_id);
		hm = invent.getMenuFiltro(id_user, id_dashboard, id_menu, fact, dim_sop, cmp_desc, id, idfk, elemento, orden, filtroIn);
		
		for (Map.Entry<String, String> el : hm.entrySet()) {
		     //System.out.println(el.getKey() + " _ " + el.getValue());
		     menu += "<option value="+el.getKey()+">"+el.getValue()+"</option>";
		}
		
		//System.out.println(fact + "-" + dim_sop + "-"+cmp_desc+"-"+id+"-"+idfk+"-"+elemento+"-"+orden);
		return menu;
	}
	public String datosFiltro(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String cmp_id){
		String datosFiltrados = "";
		String sql = "SELECT value FROM ppto_filtros_portlets WHERE id_customer='"+id_customer+"' AND id_modulo='"+id_modulo+"' AND id_user='"+id_user+
				"' AND id_dashboard='"+id_dashboard+"' AND parametro='"+cmp_id+"' AND id_portlet='"+id_portlet+"'";
		System.out.println("Selct datosFiltr0: "+sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while(rs.next()){
			if(rs.isFirst()){
				datosFiltrados += "'"+rs.getString("value")+"'";
			}else{
				datosFiltrados += ", '"+rs.getString("value")+"'";
			}
		}
		System.out.println("Datos_Filtrados: "+datosFiltrados);
		return datosFiltrados;
	}
	public void insertMenuFiltro(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String elementos, String cmp_id){
		int result;
		String sql = "DELETE FROM ppto_filtros_portlets WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"'" +
				" AND id_dashboard='"+id_dashboard+"' AND parametro='"+cmp_id+"'  AND id_portlet='"+id_portlet+"'";
		//String sqlCmpID = "SELECT chartcfg_cmp_id FROM chartcfg WHERE chart_id='"+id_portlet+"'";
		//SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sqlCmpID);
		String nom_cmpid = cmp_id;
		//while(rs.next()){
			//nom_cmpid = rs.getString("chartcfg_cmp_id");
		//}
		try {
			jdbcTemplateAdmin.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		System.out.println("insertaMEnuFiltro "+ id_portlet);
		if(!elementos.isEmpty()){
		String[] arrayFiltros = elementos.split(",");
		System.out.println("length "+arrayFiltros.length);
		for(int x = 0; x < arrayFiltros.length; x++){
			if(arrayFiltros[x] != null || arrayFiltros[x] != "" ){
				System.out.println("Filtros - " + id_user+ "--"+ id_dashboard +"--"+ id_portlet+"--"+arrayFiltros[x]+"--"+nom_cmpid);
				try {
					jdbcTemplateAdmin.update("INSERT INTO ppto_filtros_portlets (id_customer,id_user, id_modulo, id_dashboard, id_portlet, parametro, value, nombre) VALUES(?,?,?,?,?,?,?,?)", 
					        new Object[] {id_customer, id_user, id_modulo, id_dashboard, id_portlet, nom_cmpid, arrayFiltros[x], "---"});
					result=1;
				} catch (DataAccessException e) {
					e.printStackTrace();
					result=0;
				}
			}
		}
		}
		
	}
	public void insertaDatosSim(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, 
			String id_producto, String diasP1, String diasP2, String diasP3, String cajasP1, String cajasP2, String cajasP3){
		int result;
		String sql = "DELETE FROM invent_simulacion WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"'" +
				" AND id_dashboard='"+id_dashboard+"' AND id_producto='"+id_producto+"'";//  AND id_portlet='"+id_portlet+"'";
		
		try {
			jdbcTemplateAdmin.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		System.out.println("insertaDatosSim "+ id_portlet);
		
		try {
			jdbcTemplateAdmin.update("INSERT INTO invent_simulacion (id_customer,id_user, id_modulo, id_dashboard, id_producto, diasP1, cajasP1, diasP2, cajasP2, diasP3, cajasP3) VALUES(?,?,?,?,?,?,?,?,?,?,?)", 
			        new Object[] {id_customer, id_user, id_modulo, id_dashboard, id_producto, diasP1, cajasP1, diasP2, cajasP2, diasP3, cajasP3});
			result=1;
		} catch (DataAccessException e) {
			e.printStackTrace();
			result=0;
		}
	}
	
	public void insertaDatosAjuste(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, 
			String id_espacio, String id_prod_term, String itemcode_prod_term, String itemcode, String id_ins_comp, String tipo_prod, 
			String fecha, String fecha_ajuste, String cantidad, String num_doc, String comentario){
		int result;
		/*String sql = "DELETE FROM invent_simulacion WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"'" +
				" AND id_dashboard='"+id_dashboard+"' AND id_producto='"+id_producto+"'";//  AND id_portlet='"+id_portlet+"'";
		
		try {
			jdbcTemplateAdmin.update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		System.out.println("insertaDatosSim "+ id_portlet);*/
		
		
		try {
			jdbcTemplate.update("INSERT INTO mov_ajustes (id_customer,id_user, id_modulo, id_portlet, idalmacen, id_prod_term, itemcode, id_ins_comp,itemcodeic, fecha, cantidad, num_doc, comentario, fechaaj, tipo_prod) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 
			        new Object[] {id_customer, id_user, id_modulo, id_portlet, id_espacio, id_prod_term, itemcode_prod_term, id_ins_comp, itemcode, fecha, cantidad, num_doc, comentario, fecha_ajuste, tipo_prod});
			result=1;
		} catch (DataAccessException e) {
			e.printStackTrace();
			result=0;
		}
	}
	
	public void eliminaDatosSim(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, 
			String id_producto, String diasP1, String diasP2, String diasP3, String cajasP1, String cajasP2, String cajasP3){
		int result;
		String sql = "DELETE FROM invent_simulacion WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"'" +
				" AND id_dashboard='"+id_dashboard+"' AND id_producto='"+id_producto+"'";//  AND id_portlet='"+id_portlet+"'";
		
		try {
			jdbcTemplateAdmin.update(sql);
			System.out.println("Datos De simulacion Eliminados.......");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	//Genera combobox con espacios
	public String getFiltroEspacios(){
		
		String menu = "";
		
		HashMap<String,String> hm = new HashMap<String,String>();
		
		hm = invent.getEspacios();
		menu += "<select id = 'espacios' multiple = 'multiple' size = '3' style='padding: auto 0;'>";
		 menu += "<option value=0>---- Todos ----</option>";
		for (Map.Entry<String, String> el : hm.entrySet()) {
		     menu += "<option value="+el.getKey()+">"+el.getKey()+" - "+el.getValue()+"</option>";
		}
		menu += "</select>";
		return menu;
	}
	
}
