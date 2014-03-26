package com.db.dao;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.db.DataSource;

public class Inventarios {
	JdbcTemplate jdbcTemplate;
	JdbcTemplate jdbcTemplateAdmin;
	
	public Inventarios(){
		DataSource ds = new DataSource();
		jdbcTemplate = ds.getDataSource();
		jdbcTemplateAdmin = ds.getDataSourceAdmin();
	}
	
	public HashMap getDataMesesAnalisisVentas(String id_customer, String id_user, String id_modulo, String id_dashboard, int numMes, String chart_id, String activo){
		HashMap<String, String> hm = new HashMap<String, String>();
		//String sqlDatos = "";
		//sqlDatos = getSQLAnalisisVentas("2013");
		
		String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
				"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' " +
						"AND id_portlet='"+chart_id+"'GROUP BY parametro";
		System.out.println("SqlFiltros Meses: "+slqlFiltros);
		String filtroSql = "";
		String filtrosFinal = "";
		String cmp_fil = "";
		SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
		while(rs_f.next()){
			
			cmp_fil = rs_f.getString("parametro");
			if(rs_f.isFirst()){
				filtrosFinal += "WHERE "+cmp_fil+" IN(";
			}else{
				filtrosFinal += "AND "+cmp_fil+" IN(";
			}
			
			filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
					" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' " +
							"AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+chart_id+"'";
			
			SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
			while(rf.next()){
				if(!rf.isLast()){
					filtrosFinal +="'"+ rf.getString("value")+"', ";
				}else{
					filtrosFinal +="'"+ rf.getString("value")+"') ";
				}				
			}
		}
		
		String fil_activo = "";
		System.out.println("Activo ------> "+activo);
		if(activo != null && !activo.equals("null")  && !activo.equals("undefined") && !activo.isEmpty()){
			System.out.println("Filtro 1 -------------> ");
			if(filtrosFinal.isEmpty()){
				fil_activo  = " WHERE estatus='"+activo+"' ";
			}else{
				fil_activo  = " AND estatus='"+activo+"' ";
			}
		}
		System.out.println("Fil "+fil_activo);
		String sql = "SELECT" +
				" id_marca," +
				" id_producto," +
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
				" ttl_cajas_mes12" +
				" FROM" +
				" analisis_ventas "+filtrosFinal+				
				fil_activo;
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while( rs.next() ){
			Double total = 0.0;
			/*String ttl_cajas_mes1 = rs.getString("ttl_cajas_mes1");
			String ttl_cajas_mes2 = rs.getString("ttl_cajas_mes2");
			String ttl_cajas_mes3 = rs.getString("ttl_cajas_mes3");
			String ttl_cajas_mes4 = rs.getString("ttl_cajas_mes4");
			String ttl_cajas = rs.getString("ttl_cajas");
			String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
			String pendiente_x_fact = rs.getString("pendiente_x_fact");
			String costo = rs.getString("costo");
			if(ttl_cajas_mes1 == null){
				ttl_cajas_mes1 = "0";
			}
			if(ttl_cajas_mes2 == null){
				ttl_cajas_mes2 = "0";
			}
			if(ttl_cajas_mes3 == null){
				ttl_cajas_mes3 = "0";
			}
			if(ttl_cajas_mes4 == null){
				ttl_cajas_mes4 = "0";
			}
			if(ttl_cajas == null){
				ttl_cajas = "0";
			}
			if(pendiente_x_fact == null){
				pendiente_x_fact = "0";
			}
			if(ttl_cajas_ext == null){
				ttl_cajas_ext = "0";
			}
			if(costo == null){
				costo = "0";
			}*/
			String ttl_mes = "0"; 
			for(int xMes = 1; xMes <= numMes; xMes++ ){
				ttl_mes = rs.getString("ttl_cajas_mes"+xMes);
				if(ttl_mes == null || ttl_mes.isEmpty() || ttl_mes == "null"){
					ttl_mes = "0";
				}
				hm.put(rs.getString("id_marca")+"_"+rs.getString("id_producto")+"_"+xMes, ttl_mes);
				//System.out.println(rs.getString("id_marca")+"_"+rs.getString("id_producto")+"_"+xMes+","+ ttl_mes);
				total += Double.parseDouble(ttl_mes);
			}
			
			hm.put(rs.getString("id_marca")+"_"+rs.getString("id_producto")+"_ttl", String.valueOf(total));
		}
		/*SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlDatos);
		while( rs.next() ){
		hm.put(rs.getString("marca"), rs.getString("producto"));
		}*/
		
		return hm;
	}
	
	//Datos analisis ventas por meses detalle por producto
	public HashMap getDataMesesAnalisisVentasDetalleProd(String id_customer, String id_user, String id_modulo, 
			String id_dashboard, int numMes, String chart_id, String id_producto ){
		HashMap<String, String> hm = new HashMap<String, String>();
		//String sqlDatos = "";
		//sqlDatos = getSQLAnalisisVentas("2013");
		
		String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
				"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' " +
						"AND id_portlet='"+chart_id+"' GROUP BY parametro";
		System.out.println("SqlFiltros Meses: "+slqlFiltros);
		String filtroSql = "";
		String filtrosFinal = " WHERE id_producto='"+id_producto+"' ";
		String cmp_fil = "";
		SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
		while(rs_f.next()){
			
			cmp_fil = rs_f.getString("parametro");
			if(rs_f.isFirst()){
				filtrosFinal += "AND "+cmp_fil+" IN(";
			}else{
				filtrosFinal += "AND "+cmp_fil+" IN(";
			}
			
			filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
					" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' " +
							"AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+chart_id+"'";
			
			SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
			while(rf.next()){
				if(!rf.isLast()){
					filtrosFinal +="'"+ rf.getString("value")+"', ";
				}else{
					filtrosFinal +="'"+ rf.getString("value")+"') ";
				}				
			}
		}
		
		
		String sql = "SELECT" +
				" id_marca," +
				" id_producto," +
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
				" ttl_cajas_mes12" +
				" FROM" +
				" prev_compras "+filtrosFinal;
		System.out.println("SQL DetalleProducto: "+sql);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while( rs.next() ){
			Double total = 0.0;
			/*String ttl_cajas_mes1 = rs.getString("ttl_cajas_mes1");
			String ttl_cajas_mes2 = rs.getString("ttl_cajas_mes2");
			String ttl_cajas_mes3 = rs.getString("ttl_cajas_mes3");
			String ttl_cajas_mes4 = rs.getString("ttl_cajas_mes4");
			String ttl_cajas = rs.getString("ttl_cajas");
			String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
			String pendiente_x_fact = rs.getString("pendiente_x_fact");
			String costo = rs.getString("costo");
			if(ttl_cajas_mes1 == null){
				ttl_cajas_mes1 = "0";
			}
			if(ttl_cajas_mes2 == null){
				ttl_cajas_mes2 = "0";
			}
			if(ttl_cajas_mes3 == null){
				ttl_cajas_mes3 = "0";
			}
			if(ttl_cajas_mes4 == null){
				ttl_cajas_mes4 = "0";
			}
			if(ttl_cajas == null){
				ttl_cajas = "0";
			}
			if(pendiente_x_fact == null){
				pendiente_x_fact = "0";
			}
			if(ttl_cajas_ext == null){
				ttl_cajas_ext = "0";
			}
			if(costo == null){
				costo = "0";
			}*/
			String ttl_mes = "0"; 
			for(int xMes = 1; xMes <= numMes; xMes++ ){
				ttl_mes = rs.getString("ttl_cajas_mes"+xMes);
				if(ttl_mes == null || ttl_mes.isEmpty() || ttl_mes == "null"){
					ttl_mes = "0";
				}
				hm.put(rs.getString("id_marca")+"_"+rs.getString("id_producto")+"_"+xMes, ttl_mes);
				System.out.println(rs.getString("id_marca")+"_"+rs.getString("id_producto")+"_"+xMes+","+ ttl_mes);
				total += Double.parseDouble(ttl_mes);
			}
			System.out.println("DetalleProducto: "+rs.getString("id_producto")+"_ttl" +" , "+ String.valueOf(total));
			hm.put(rs.getString("id_producto")+"_ttl", String.valueOf(total));
		}
		/*SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlDatos);
		while( rs.next() ){
		hm.put(rs.getString("marca"), rs.getString("producto"));
		}*/
		
		return hm;
	}
	
	//Meses para Previcion Compras
	public HashMap getDataProdSimulacion(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet ){
		HashMap<String, String> hm = new HashMap<String, String>();
				
		String sql = "SELECT" +
				" id_producto," +
				" diasP1," +
				" cajasP1," +
				" diasP2," +
				" cajasP2," +
				" diasP3," +
				" cajasP3" +
				" FROM" +
				" invent_simulacion " +
				" WHERE id_customer='"+id_customer+"' " +
						" AND id_user='"+id_user+"' " +
								" AND id_modulo='"+id_modulo+"' " +
										" AND id_dashboard='"+id_dashboard+"' "; //+
												//" AND id_portlet='"+id_portlet+"'";
		System.out.println("SQL - Meses Simulacion " + sql);
		SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
		while( rs.next() ){
			String id_producto =  rs.getString("id_producto");
			String diasP1 = rs.getString("diasP1");
			String cajasP1 = rs.getString("cajasP1");
			String diasP2 = rs.getString("diasP2");
			String cajasP2 = rs.getString("cajasP2");
			String diasP3 = rs.getString("diasP3");
			String cajasP3 = rs.getString("cajasP3");
			
			if(diasP1 == null){
				diasP1 = "0";
			}
			if(cajasP1 == null){
				cajasP1 = "0";
			}
			if(diasP2 == null){
				diasP2 = "0";
			}
			if(cajasP2 == null){
				cajasP2 = "0";
			}
			if(diasP3 == null){
				diasP3 = "0";
			}
			if(cajasP3 == null){
				cajasP3 = "0";
			}
			//System.out.println("Dias_P1 "+diasP1);
			
			hm.put(id_producto+"_diasP1",diasP1);
			hm.put(id_producto+"_diasP2",diasP2);
			hm.put(id_producto+"_diasP3",diasP3);
			
			hm.put(id_producto+"_cajasP1",cajasP1);
			hm.put(id_producto+"_cajasP2",cajasP2);
			hm.put(id_producto+"_cajasP3",cajasP3);
			hm.put(id_producto+"_sim", "1");
		}
		
		return hm;
	}
	
	//Productos con datos de simulacion
	public HashMap getDataMesesPrevCompras(String id_customer, String id_user, String id_modulo, String id_dashboard, int numMes, String chart_id, String activo){
		HashMap<String, String> hm = new HashMap<String, String>();
		//String sqlDatos = "";
		//sqlDatos = getSQLAnalisisVentas("2013");
		
		String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
				"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' " +
						"AND id_portlet='"+chart_id+"'GROUP BY parametro";
		System.out.println("SqlFiltros Meses: "+slqlFiltros);
		String filtroSql = "";
		String filtrosFinal = "";
		String cmp_fil = "";
		SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
		while(rs_f.next()){
			
			cmp_fil = rs_f.getString("parametro");
			if(rs_f.isFirst()){
				filtrosFinal += "WHERE "+cmp_fil+" IN(";
			}else{
				filtrosFinal += "AND "+cmp_fil+" IN(";
			}
			
			filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
					" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' " +
							"AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+chart_id+"'";
			
			SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
			while(rf.next()){
				if(!rf.isLast()){
					filtrosFinal +="'"+ rf.getString("value")+"', ";
				}else{
					filtrosFinal +="'"+ rf.getString("value")+"') ";
				}				
			}
		}
		
		String fil_activo = "";
		//System.out.println("Activo: "+activo);
		if(activo != null && !activo.equals("null") && !activo.equals("undefined") && !activo.isEmpty()){
			//System.out.println("Filtro ---->");
			if(filtrosFinal.isEmpty()){
				fil_activo  = " WHERE estatus='"+activo+"' ";
			}else{
				fil_activo  = " AND estatus='"+activo+"' ";
			}
		}
		//System.out.println("Filtro_Avt "+fil_activo);
		String sql = "SELECT" +
				" id_producto," +
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
				" ttl_cajas_mes12" +
				" FROM" +
				" prev_compras "+filtrosFinal + fil_activo;
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while( rs.next() ){
			Double total = 0.0;
			/*String ttl_cajas_mes1 = rs.getString("ttl_cajas_mes1");
			String ttl_cajas_mes2 = rs.getString("ttl_cajas_mes2");
			String ttl_cajas_mes3 = rs.getString("ttl_cajas_mes3");
			String ttl_cajas_mes4 = rs.getString("ttl_cajas_mes4");
			String ttl_cajas = rs.getString("ttl_cajas");
			String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
			String pendiente_x_fact = rs.getString("pendiente_x_fact");
			String costo = rs.getString("costo");
			if(ttl_cajas_mes1 == null){
				ttl_cajas_mes1 = "0";
			}
			if(ttl_cajas_mes2 == null){
				ttl_cajas_mes2 = "0";
			}
			if(ttl_cajas_mes3 == null){
				ttl_cajas_mes3 = "0";
			}
			if(ttl_cajas_mes4 == null){
				ttl_cajas_mes4 = "0";
			}
			if(ttl_cajas == null){
				ttl_cajas = "0";
			}
			if(pendiente_x_fact == null){
				pendiente_x_fact = "0";
			}
			if(ttl_cajas_ext == null){
				ttl_cajas_ext = "0";
			}
			if(costo == null){
				costo = "0";
			}*/
			String ttl_mes = "0"; 
			for(int xMes = 1; xMes <= numMes; xMes++ ){
				ttl_mes = rs.getString("ttl_cajas_mes"+xMes);
				if(ttl_mes == null || ttl_mes.isEmpty() || ttl_mes == "null"){
					ttl_mes = "0";
				}
				hm.put(rs.getString("id_producto")+"_"+xMes, ttl_mes);
				//System.out.println(rs.getString("id_marca")+"_"+rs.getString("id_producto")+"_"+xMes+","+ ttl_mes);
				total += Double.parseDouble(ttl_mes);
			}
			
			hm.put(rs.getString("id_producto")+"_ttl", String.valueOf(total));
		}
		/*SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlDatos);
		while( rs.next() ){
		hm.put(rs.getString("marca"), rs.getString("producto"));
		}*/
		
		return hm;
	}
	
	//Metodo que obtiene los datos semanales para el grid productos que no desplazan
	public HashMap getDataSemanasProdNoDesplaza(String id_customer, String id_user, String id_modulo, String id_dashboard, int numSem, String chart_id ){
		HashMap<String, String> hm = new HashMap<String, String>();
		
		/*String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
				"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' GROUP BY parametro";
		System.out.println("SqlFiltros: "+slqlFiltros);
		String filtroSql = "";
		String filtrosFinal = "";
		String cmp_fil = "";
		SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
		while(rs_f.next()){
			
			cmp_fil = rs_f.getString("parametro");
			if(rs_f.isFirst()){
				filtrosFinal += "WHERE "+cmp_fil+" IN(";
			}else{
				filtrosFinal += "AND "+cmp_fil+" IN(";
			}
			
			filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
					" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
			
			SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
			while(rf.next()){
				if(!rf.isLast()){
					filtrosFinal +="'"+ rf.getString("value")+"', ";
				}else{
					filtrosFinal +="'"+ rf.getString("value")+"') ";
				}				
			}
		}
		*/
		
		String sql = "SELECT" +
				" id_producto," +
				" sem1," +
				" sem2," +
				" sem3," +
				" sem4," +
				" sem5," +
				" sem6," +
				" sem7," +
				" sem8," +
				" sem9," +
				" sem10," +
				" sem11," +
				" sem12," +
				" sem13," +
				" sem14," +
				" sem15," +
				" sem16," +
				" sem17," +
				" sem18" +
				" FROM" +
				" prod_no_desplaza ";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while( rs.next() ){
			Double total = 0.0;
			String ttl_semana = "0"; 
			for(int xSem = 1; xSem <= 18; xSem++ ){
				ttl_semana = rs.getString("sem"+xSem);
				if(ttl_semana == null || ttl_semana.isEmpty() || ttl_semana == "null"){
					ttl_semana = "0";
				}
				hm.put(rs.getString("id_producto")+"_"+xSem, ttl_semana);
				//System.out.println(rs.getString("id_producto")+"_"+xSem+","+ ttl_semana);
				total += Double.parseDouble(ttl_semana);
			}
			System.out.println(rs.getString("id_producto")+"_ttl"+" , "+ String.valueOf(total));
			hm.put(rs.getString("id_producto")+"_ttl", String.valueOf(total));
		}
		/*SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlDatos);
		while( rs.next() ){
		hm.put(rs.getString("marca"), rs.getString("producto"));
		}*/
		
		return hm;
	}
	//Inventarios - Analisis de Ventas
		public List<InvDTO> getDataAnalisisVentas(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String activo ){
			List<InvDTO> rows = new ArrayList<InvDTO>();		
			
			String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
					"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
							" AND id_portlet='"+id_portlet+"'    GROUP BY parametro";
			//System.out.println("SqlFiltros: "+slqlFiltros);
			String filtroSql = "";
			String filtrosFinal = "";
			String cmp_fil = "";
			SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs_f.next()){
				//num = Integer.parseInt(rs.getString("num"));
				cmp_fil = rs_f.getString("parametro");
				if(rs_f.isFirst()){
					filtrosFinal += "WHERE "+cmp_fil+" IN(";
				}else{
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
						" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
								" AND id_portlet='"+id_portlet+"' ";
				//System.out.println("filtroSql --- "+filtroSql);
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				
				while(rf.next()){
					////System.out.println(cmp_fil+"__"+rf.getString("id_filtro"));
					if(!rf.isLast()){
						filtrosFinal +="'"+ rf.getString("value")+"', ";
					}else{
						filtrosFinal +="'"+ rf.getString("value")+"') ";
					}
					
				}
			}
			String fil_activo = "";
			if(activo != null  && !activo.equals("null") && !activo.equals("undefined") && !activo.isEmpty()){
				//System.out.println("Filtro ------->>>");
				if(filtrosFinal.isEmpty()){
					fil_activo  = " WHERE estatus='"+activo+"' ";
				}else{
					fil_activo  = " AND estatus='"+activo+"' ";
				}
			}
			//System.out.println("Filtros Final..."+filtrosFinal);
			DecimalFormat formatter = new DecimalFormat("###,##0.000");
			String sql = "SELECT" +
					" fecha," +
					" id_marca," +
					" marca," +
					" itemcode,"+
					" id_producto," +
					" producto," +
					" ttl_cajas_mes1," +
					" ttl_cajas_mes2," +
					" ttl_cajas_mes3," +
					" ttl_cajas_mes4," +
					" ttl_cajas," +
					" ttl_cajas_ext," +
					" minstock,"+
					" pendiente_x_fact,"+
					" costo" +
					" FROM" +
					" analisis_ventas "+filtrosFinal +
					fil_activo +
					" ORDER BY marca ASC";
			System.out.println("Analisis Ventas: "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				InvDTO dto = new InvDTO();
				String ttl_cajas_mes1 = rs.getString("ttl_cajas_mes1");
				String ttl_cajas_mes2 = rs.getString("ttl_cajas_mes2");
				String ttl_cajas_mes3 = rs.getString("ttl_cajas_mes3");
				String ttl_cajas_mes4 = rs.getString("ttl_cajas_mes4");
				String ttl_cajas = rs.getString("ttl_cajas");
				String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
				String pendiente_x_fact = rs.getString("pendiente_x_fact");
				String costo = rs.getString("costo");
				String minstock = rs.getString("minstock");
				if(ttl_cajas_mes1 == null){
					ttl_cajas_mes1 = "0";
				}
				if(ttl_cajas_mes2 == null){
					ttl_cajas_mes2 = "0";
				}
				if(ttl_cajas_mes3 == null){
					ttl_cajas_mes3 = "0";
				}
				if(ttl_cajas_mes4 == null){
					ttl_cajas_mes4 = "0";
				}
				if(ttl_cajas == null){
					ttl_cajas = "0";
				}
				if(pendiente_x_fact == null){
					pendiente_x_fact = "0";
				}
				if(ttl_cajas_ext == null){
					ttl_cajas_ext = "0";
				}
				if(costo == null){
					costo = "0";
				}
				if(minstock == null){
					minstock = "0";
				}
				dto.setIdMarca(rs.getString("id_marca"));
				dto.setCodeProd(rs.getString("itemcode"));
				dto.setIdProducto(rs.getString("id_producto"));
				dto.setMarca(rs.getString("marca"));
				dto.setProducto(rs.getString("producto"));
				dto.setKey(rs.getString("id_marca")+"_"+rs.getString("id_producto"));
				dto.setTtl_cajas(ttl_cajas);
				dto.setTtl_cajas_mes1(ttl_cajas_mes1);
				dto.setTtl_cajas_mes2(ttl_cajas_mes2);
				dto.setTtl_cajas_mes3(ttl_cajas_mes3);
				dto.setTtl_cajas_mes4(ttl_cajas_mes4);
				dto.setTtl_cajas_ext(ttl_cajas_ext);
				dto.setPendiente_x_fact(pendiente_x_fact);
				dto.setCosto(costo);
				dto.setMinstock(minstock);
				rows.add(dto);
			}
			return rows;
		}
		
		//Datos analisis de ventas detalle producto
		public List<InvPrevComp> getDataAnalisisVentasDetalleProd(String id_customer, String id_user, String id_modulo, 
				String id_dashboard, String id_portlet, String id_producto ){
			List<InvPrevComp> rows = new ArrayList<InvPrevComp>();		
			
			String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
					"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
							" AND id_portlet='"+id_portlet+"' GROUP BY parametro";
			System.out.println("SqlFiltros: "+slqlFiltros);
			String filtroSql = "";
			String filtrosFinal = " WHERE id_producto='"+id_producto+"' ";
			String cmp_fil = "";
			SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs_f.next()){
				//num = Integer.parseInt(rs.getString("num"));
				cmp_fil = rs_f.getString("parametro");
				if(rs_f.isFirst()){
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}else{
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
						" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
								" AND id_portlet='"+id_portlet+"' ";
				System.out.println("filtroSql --- "+filtroSql);
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				
				while(rf.next()){
					////System.out.println(cmp_fil+"__"+rf.getString("id_filtro"));
					if(!rf.isLast()){
						filtrosFinal +="'"+ rf.getString("value")+"', ";
					}else{
						filtrosFinal +="'"+ rf.getString("value")+"') ";
					}
					
				}
			}
			//System.out.println("Filtros Final..."+filtrosFinal);
			DecimalFormat formatter = new DecimalFormat("###,##0.000");
			String sql = "SELECT" +
					" id_producto," +
					" itemcode," +
					" producto," +
					" id_marca,"+
					" marca,"+
					" ttl_cajas_ext," +
					" pendiente_x_fact," +
					" diasp1," +
					" fechap1," +
					" cajasp1," +
					" diasp2," +
					" fechap2," +
					" cajasp2," +
					" diasp3," +
					" fechap3," +
					" cajasp3," +
					" minstock," +
					" maxstock," +
					" tprov," +
					" idorigen,"+
					" costo"+
					" FROM" +
					" prev_compras " +
					filtrosFinal +
					" ORDER BY marca ASC";
			System.out.println("Prevision Compras: "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				InvPrevComp dto = new InvPrevComp();
				
				String id_prod = rs.getString("id_producto");
				String itemcode = rs.getString("itemcode");
				String producto = rs.getString("producto");
				String idMarca = rs.getString("id_marca");
				String descMarca = rs.getString("marca");
				String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
				String pendiente_x_fact = rs.getString("pendiente_x_fact");
				
				String diasP1 = rs.getString("diasP1");
				Date fechaP1 = rs.getDate("fechaP1");
				String cajasP1 = rs.getString("cajasP1");
				
				String diasP2 = rs.getString("diasP2");
				Date fechaP2 = rs.getDate("fechaP2");
				String cajasP2 = rs.getString("cajasP2");
				
				String diasP3 = rs.getString("diasP3");
				Date fechaP3 = rs.getDate("fechaP3");
				String cajasP3 = rs.getString("cajasP3");
				
				String minStock = rs.getString("minStock");
				String maxStock = rs.getString("maxStock");
				String tiempoProv = rs.getString("tprov");
				String origen = rs.getString("idorigen");
				String costo = rs.getString("costo");
				System.out.println("Fechas___"+fechaP1+" "+fechaP2+" "+fechaP3);
				if(pendiente_x_fact == null){
					pendiente_x_fact = "0";
				}
				if(ttl_cajas_ext == null){
					ttl_cajas_ext = "0";
				}
				if(costo == null){
					costo = "0";
				}
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
				if(tiempoProv == null){
					tiempoProv = "0";
				}
				if(minStock == null){
					minStock = "0";
				}
				//Formato de fecha
				String formatFechaP1 = null;
				String formatFechaP2 = null;
				String formatFechaP3 = null;
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				if(fechaP1 != null){
					formatFechaP1 = formato.format(fechaP1);
				}
				if(fechaP2 != null){
					formatFechaP2 = formato.format(fechaP2);
				}
				if(fechaP3 != null){
					formatFechaP3 = formato.format(fechaP3);
				}
				
				dto.setIdProd(id_prod);
				dto.setCodeProd(itemcode);
				dto.setDescProd(producto);
				dto.setIdMarca(idMarca);
				dto.setDescMarca(descMarca);
				dto.setTtlExist(ttl_cajas_ext);
				dto.setPndXFact(pendiente_x_fact);
				
				dto.setDiasP1(diasP1);
				dto.setFechaP1(fechaP1);
				dto.setFrmFechaP1(formatFechaP1);
				dto.setCajasP1(cajasP1);
				dto.setDiasP2(diasP2);
				dto.setFechaP2(fechaP2);
				dto.setFrmFechaP2(formatFechaP2);
				dto.setCajasP2(cajasP2);
				dto.setDiasP3(diasP3);
				dto.setFechaP3(fechaP3);
				dto.setFrmFechaP3(formatFechaP3);
				dto.setCajasP3(cajasP3);
				
				dto.setMinStock(minStock);
				dto.setMaxStock(maxStock);
				dto.setTiempoProv(tiempoProv);
				dto.setIdOrigen(origen);
				dto.setCosto(costo);
				rows.add(dto);
			}
			return rows;
		}
		//Obtener Datos de Previcion de Compras
		public List<InvPrevComp> getDataPrevComprasProdGnrl(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String activo){
			List<InvPrevComp> rows = new ArrayList<InvPrevComp>();		
			
			String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
					"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
							" AND id_portlet='"+id_portlet+"'    GROUP BY parametro";
			System.out.println("SqlFiltros: "+slqlFiltros);
			String filtroSql = "";
			String filtrosFinal = "";
			String cmp_fil = "";
			SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs_f.next()){
				//num = Integer.parseInt(rs.getString("num"));
				cmp_fil = rs_f.getString("parametro");
				if(rs_f.isFirst()){
					filtrosFinal += "WHERE " + cmp_fil+" IN(";
				}else{
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
						" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
								" AND id_portlet='"+id_portlet+"' ";
				//System.out.println("filtroSql --- "+filtroSql);
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				
				while(rf.next()){
					////System.out.println(cmp_fil+"__"+rf.getString("id_filtro"));
					if(!rf.isLast()){
						filtrosFinal +="'"+ rf.getString("value")+"', ";
					}else{
						filtrosFinal +="'"+ rf.getString("value")+"') ";
					}
					
				}
			}
			String fil_activo =  "";
			//System.out.println("Activo: "+activo);
			if(activo != null && !activo.equals("null") && !activo.equals("undefined") && !activo.isEmpty()){
				//System.out.println("Filtro ---->");
				if(filtrosFinal.isEmpty()){
					fil_activo  = " WHERE estatus='"+activo+"' ";
				}else{
					fil_activo  = " AND estatus='"+activo+"' ";
				}
			}
			//System.out.println("Filtro_Avt "+fil_activo);
			//System.out.println("Filtros Final..."+filtrosFinal);
			DecimalFormat formatter = new DecimalFormat("###,##0.000");
			String sql = "SELECT" +
					" id_producto," +
					" itemcode," +
					" producto," +
					" id_marca,"+
					" marca,"+
					" ttl_cajas_ext," +
					" pendiente_x_fact," +
					" diasp1," +
					" fechap1," +
					" cajasp1," +
					" diasp2," +
					" fechap2," +
					" cajasp2," +
					" diasp3," +
					" fechap3," +
					" cajasp3," +
					" minstock," +
					" maxstock," +
					" tprov," +
					" idorigen,"+
					" costo"+
					" FROM" +
					" prev_compras " +
					filtrosFinal +
					fil_activo+
					" ORDER BY marca ASC";
			System.out.println("Prevision Compras: "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				InvPrevComp dto = new InvPrevComp();
				
				String id_producto = rs.getString("id_producto");
				String itemcode = rs.getString("itemcode");
				String producto = rs.getString("producto");
				String idMarca = rs.getString("id_marca");
				String descMarca = rs.getString("marca");
				String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
				String pendiente_x_fact = rs.getString("pendiente_x_fact");
				
				String diasP1 = rs.getString("diasP1");
				Date fechaP1 = rs.getDate("fechaP1");
				String cajasP1 = rs.getString("cajasP1");
				
				String diasP2 = rs.getString("diasP2");
				Date fechaP2 = rs.getDate("fechaP2");
				String cajasP2 = rs.getString("cajasP2");
				
				String diasP3 = rs.getString("diasP3");
				Date fechaP3 = rs.getDate("fechaP3");
				String cajasP3 = rs.getString("cajasP3");
				
				String minStock = rs.getString("minStock");
				String maxStock = rs.getString("maxStock");
				String tiempoProv = rs.getString("tprov");
				String origen = rs.getString("idorigen");
				String costo = rs.getString("costo");
				
				if(pendiente_x_fact == null){
					pendiente_x_fact = "0";
				}
				if(ttl_cajas_ext == null){
					ttl_cajas_ext = "0";
				}
				if(costo == null){
					costo = "0";
				}
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
				if(tiempoProv == null){
					tiempoProv = "0";
				}
				if(minStock == null){
					minStock = "0";
				}
				dto.setIdProd(id_producto);
				dto.setCodeProd(itemcode);
				dto.setDescProd(producto);
				dto.setIdMarca(idMarca);
				dto.setDescMarca(descMarca);
				dto.setTtlExist(ttl_cajas_ext);
				dto.setPndXFact(pendiente_x_fact);
				
				dto.setDiasP1(diasP1);
				dto.setFechaP1(fechaP1);
				dto.setCajasP1(cajasP1);
				dto.setDiasP2(diasP2);
				dto.setFechaP2(fechaP2);
				dto.setCajasP2(cajasP2);
				dto.setDiasP3(diasP3);
				dto.setFechaP3(fechaP3);
				dto.setCajasP3(cajasP3);
				
				dto.setMinStock(minStock);
				dto.setMaxStock(maxStock);
				dto.setTiempoProv(tiempoProv);
				dto.setIdOrigen(origen);
				dto.setCosto(costo);
				
				rows.add(dto);
			}
			return rows;
		}
		
		//Obtener Datos de Previcion de Compras
		public List<InvPrevComp> getDataPrevCompras(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet ){
			List<InvPrevComp> rows = new ArrayList<InvPrevComp>();		
			
			String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
					"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
							" AND id_portlet='"+id_portlet+"'    GROUP BY parametro";
			System.out.println("SqlFiltros: "+slqlFiltros);
			String filtroSql = "";
			String filtrosFinal = "";
			String cmp_fil = "";
			SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs_f.next()){
				//num = Integer.parseInt(rs.getString("num"));
				cmp_fil = rs_f.getString("parametro");
				if(rs_f.isFirst()){
					filtrosFinal += "AND " + cmp_fil+" IN(";
				}else{
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
						" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'" +
								" AND id_portlet='"+id_portlet+"' ";
				System.out.println("filtroSql --- "+filtroSql);
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				
				while(rf.next()){
					////System.out.println(cmp_fil+"__"+rf.getString("id_filtro"));
					if(!rf.isLast()){
						filtrosFinal +="'"+ rf.getString("value")+"', ";
					}else{
						filtrosFinal +="'"+ rf.getString("value")+"') ";
					}
					
				}
			}
			//System.out.println("Filtros Final..."+filtrosFinal);
			DecimalFormat formatter = new DecimalFormat("###,##0.000");
			String sql = "SELECT" +
					" id_producto," +
					" itemcode," +
					" producto," +
					" id_marca,"+
					" marca,"+
					" ttl_cajas_ext," +
					" pendiente_x_fact," +
					" diasp1," +
					" fechap1," +
					" cajasp1," +
					" diasp2," +
					" fechap2," +
					" cajasp2," +
					" diasp3," +
					" fechap3," +
					" cajasp3," +
					" minstock," +
					" maxstock," +
					" tprov," +
					" idorigen,"+
					" costo"+
					" FROM" +
					" prev_compras " +
					" WHERE tipo != 0 " +
					filtrosFinal +
					" ORDER BY marca ASC";
			System.out.println("Prevision Compras: "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				InvPrevComp dto = new InvPrevComp();
				
				String id_producto = rs.getString("id_producto");
				String itemcode = rs.getString("itemcode");
				String producto = rs.getString("producto");
				String idMarca = rs.getString("id_marca");
				String descMarca = rs.getString("marca");
				String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
				String pendiente_x_fact = rs.getString("pendiente_x_fact");
				
				String diasP1 = rs.getString("diasP1");
				Date fechaP1 = rs.getDate("fechaP1");
				String cajasP1 = rs.getString("cajasP1");
				
				String diasP2 = rs.getString("diasP2");
				Date fechaP2 = rs.getDate("fechaP2");
				String cajasP2 = rs.getString("cajasP2");
				
				String diasP3 = rs.getString("diasP3");
				Date fechaP3 = rs.getDate("fechaP3");
				String cajasP3 = rs.getString("cajasP3");
			
				String minStock = rs.getString("minStock");
				String maxStock = rs.getString("maxStock");
				String tiempoProv = rs.getString("tprov");
				String origen = rs.getString("idorigen");
				String costo = rs.getString("costo");
				
				if(pendiente_x_fact == null){
					pendiente_x_fact = "0";
				}
				if(ttl_cajas_ext == null){
					ttl_cajas_ext = "0";
				}
				if(costo == null){
					costo = "0";
				}
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
				if(tiempoProv == null){
					tiempoProv = "0";
				}
				if(minStock == null){
					minStock = "0";
				}
				
				//Formato de fecha
				String formatFechaP1 = null;
				String formatFechaP2 = null;
				String formatFechaP3 = null;
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				if(fechaP1 != null){
					formatFechaP1 = formato.format(fechaP1);
				}
				if(fechaP2 != null){
					formatFechaP2 = formato.format(fechaP2);
				}
				if(fechaP3 != null){
					formatFechaP3 = formato.format(fechaP3);
				}
				
				dto.setIdProd(id_producto);
				dto.setCodeProd(itemcode);
				dto.setDescProd(producto);
				dto.setIdMarca(idMarca);
				dto.setDescMarca(descMarca);
				dto.setTtlExist(ttl_cajas_ext);
				dto.setPndXFact(pendiente_x_fact);
				
				
				dto.setDiasP1(diasP1);
				dto.setFrmFechaP1(formatFechaP1);
				dto.setFechaP1(fechaP1);
				dto.setCajasP1(cajasP1);
				dto.setDiasP2(diasP2);
				dto.setFrmFechaP2(formatFechaP2);
				dto.setFechaP2(fechaP2);
				dto.setCajasP2(cajasP2);
				dto.setDiasP3(diasP3);
				dto.setFrmFechaP3(formatFechaP3);
				dto.setFechaP3(fechaP3);
				dto.setCajasP3(cajasP3);
				
				dto.setMinStock(minStock);
				dto.setMaxStock(maxStock);
				dto.setTiempoProv(tiempoProv);
				dto.setIdOrigen(origen);
				dto.setCosto(costo);
				
				rows.add(dto);
			}
			return rows;
		}
		//Metodo para obtener los datos de productos que no desplazan
		public List<InvDTO> getDataProdNoDesplaza(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet ){
			List<InvDTO> rows = new ArrayList<InvDTO>();		
			
			String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
					"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' " +
							"AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+id_portlet+"' GROUP BY parametro";
			System.out.println("SqlFiltros NoDesplaza: "+slqlFiltros);
			String filtroSql = "";
			String filtrosFinal = "";
			String cmp_fil = "";
			SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs_f.next()){
				//num = Integer.parseInt(rs.getString("num"));
				cmp_fil = rs_f.getString("parametro");
				if(rs_f.isFirst()){
					filtrosFinal += "WHERE "+cmp_fil+" IN(";
				}else{
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
						" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' " +
								" AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+id_portlet+"'";
				//System.out.println("filtroSql --- "+filtroSql);
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				while(rf.next()){
					////System.out.println(cmp_fil+"__"+rf.getString("id_filtro"));
					if(!rf.isLast()){
						filtrosFinal +="'"+ rf.getString("value")+"', ";
					}else{
						filtrosFinal +="'"+ rf.getString("value")+"') ";
					}
					
				}
			}
			//System.out.println("Filtros Final..."+filtrosFinal);
			DecimalFormat formatter = new DecimalFormat("###,##0.000");
			String sql = "SELECT" +
					" itemcode," +
					" id_producto," +
					" producto," +
					" ttl_cajas," +
					" pendiente_x_fact,"+
					" ttl_cajas_ext," +
					" costo," +
					" minstock"+
					" FROM" +
					" prod_no_desplaza " +
					filtrosFinal+
					" ORDER BY producto ASC";
			System.out.println("ProdNoDesplaza: "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				InvDTO dto = new InvDTO();
				String ttl_cajas = rs.getString("ttl_cajas");
				String ttl_cajas_ext = rs.getString("ttl_cajas_ext");
				String costo = rs.getString("costo");
				String min_stock = rs.getString("minstock");
				
				if(ttl_cajas == null){
					ttl_cajas = "0";
				}
				if(ttl_cajas_ext == null){
					ttl_cajas_ext = "0";
				}
				if(costo == null){
					costo = "0";
				}
				if(min_stock == null){
					min_stock = "0";
				}
				String pendiente_x_fact = rs.getString("pendiente_x_fact");
				dto.setPendiente_x_fact(pendiente_x_fact);
				dto.setCodeProd(rs.getString("itemcode"));
				dto.setIdProducto(rs.getString("id_producto"));
				dto.setProducto(rs.getString("producto"));
				dto.setTtl_cajas(ttl_cajas);
				dto.setTtl_cajas_ext(ttl_cajas_ext);
				dto.setCosto(costo);
				dto.setMinstock(min_stock);
				rows.add(dto);
			}
			return rows;
		}
	//Datos Agrupados por maraca para obtener los totales
		public HashMap getDataMesesAnalisisVentasMarcas(String id_customer, String id_user, String id_modulo, String id_dashboard, int numMes, String chart_id, String activo ){
			HashMap<String, String> hm = new HashMap<String, String>();
			//String sqlDatos = "";
			//sqlDatos = getSQLAnalisisVentas("2013");
			
			String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
					"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' " +
							"AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+chart_id+"' GROUP BY parametro";
			System.out.println("SqlFiltros Marcas: "+slqlFiltros);
			String filtroSql = "";
			String filtrosFinal = "";
			String cmp_fil = "";
			SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs_f.next()){
				
				cmp_fil = rs_f.getString("parametro");
				if(rs_f.isFirst()){
					filtrosFinal += "WHERE "+cmp_fil+" IN(";
				}else{
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
						" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
				
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				while(rf.next()){
					if(!rf.isLast()){
						filtrosFinal +="'"+ rf.getString("value")+"', ";
					}else{
						filtrosFinal +="'"+ rf.getString("value")+"') ";
					}				
				}
			}
			
			String fil_activo = "";
			if(activo != null && !activo.equals("null")  && !activo.equals("undefined") && !activo.isEmpty()){
				System.out.println("Filtro 3 --------->>>");
				if(filtrosFinal.isEmpty()){
					fil_activo  = " WHERE estatus='"+activo+"' ";
				}else{
					fil_activo  = " AND estatus='"+activo+"' ";
				}
			}
			
			String sql = "SELECT" +
					" id_marca," +
					" id_producto," +
					" SUM(ttl_cajas_mes1) ttl_cajas_mes1," +
					" SUM(ttl_cajas_mes2) ttl_cajas_mes2," +
					" SUM(ttl_cajas_mes3) ttl_cajas_mes3," +
					" SUM(ttl_cajas_mes4) ttl_cajas_mes4," +
					" SUM(ttl_cajas_mes5) ttl_cajas_mes5," +
					" SUM(ttl_cajas_mes6) ttl_cajas_mes6," +
					" SUM(ttl_cajas_mes7) ttl_cajas_mes7," +
					" SUM(ttl_cajas_mes8) ttl_cajas_mes8," +
					" SUM(ttl_cajas_mes9) ttl_cajas_mes9," +
					" SUM(ttl_cajas_mes10) ttl_cajas_mes10," +
					" SUM(ttl_cajas_mes11) ttl_cajas_mes11," +
					" SUM(ttl_cajas_mes12) ttl_cajas_mes12," +
					" SUM(ttl_cajas) ttl_cajas," +
					" SUM(ttl_cajas_ext) ttl_cajas_ext," +
					" SUM(pendiente_x_fact) pendiente_x_fact,"+
					" SUM(costo) costo" +
					" FROM" +
					" analisis_ventas "+filtrosFinal +
					fil_activo + 
					" GROUP BY id_marca ";
			System.out.println("SQL - Marcas "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				Double total = 0.0;
				
				String ttl_mes = "0"; 
				for(int xMes = 1; xMes <= numMes; xMes++ ){
					ttl_mes = rs.getString("ttl_cajas_mes"+xMes);
					if(ttl_mes == null || ttl_mes.isEmpty() || ttl_mes == "null"){
						ttl_mes = "0";
					}
					hm.put(rs.getString("id_marca")+"_"+xMes, ttl_mes);
					//System.out.println(rs.getString("id_marca")+"_"+xMes+","+ ttl_mes);
					total += Double.parseDouble(ttl_mes);
				}
				String ttl_cajas_ext = "0";
				String pendiente_x_fact = "0";
				String costo = "0";
				String ttl_cajas_mes1 = "0";
				String ttl_cajas_mes2 = "0";
				String ttl_cajas_mes3 = "0";
				String ttl_cajas_mes4 = "0";
				String ttl_cajas_mes5 = "0";
				String ttl_cajas_mes6 = "0";
				String ttl_cajas_mes7 = "0";
				String ttl_cajas_mes8 = "0";
				String ttl_cajas_mes9 = "0";
				String ttl_cajas_mes10 = "0";
				String ttl_cajas_mes11 = "0";
				String ttl_cajas_mes12 = "0";
				
				ttl_cajas_ext = rs.getString("ttl_cajas_ext");
				pendiente_x_fact = rs.getString("pendiente_x_fact");
				costo = rs.getString("costo");
				ttl_cajas_mes1 = rs.getString("ttl_cajas_mes1");
				ttl_cajas_mes2 = rs.getString("ttl_cajas_mes2");
				ttl_cajas_mes3 = rs.getString("ttl_cajas_mes3");
				ttl_cajas_mes4 = rs.getString("ttl_cajas_mes4");
				ttl_cajas_mes5 = rs.getString("ttl_cajas_mes5");
				ttl_cajas_mes6 = rs.getString("ttl_cajas_mes6");
				ttl_cajas_mes7 = rs.getString("ttl_cajas_mes7");
				ttl_cajas_mes8 = rs.getString("ttl_cajas_mes8");
				ttl_cajas_mes9 = rs.getString("ttl_cajas_mes9");
				ttl_cajas_mes10 = rs.getString("ttl_cajas_mes10");
				ttl_cajas_mes11 = rs.getString("ttl_cajas_mes11");
				ttl_cajas_mes12 = rs.getString("ttl_cajas_mes12");
				
				if(ttl_cajas_ext == null || ttl_cajas_ext.isEmpty() || ttl_cajas_ext == "null"){
					ttl_cajas_ext = "0";
				}
				if(pendiente_x_fact == null || pendiente_x_fact.isEmpty() || pendiente_x_fact == "null"){
					pendiente_x_fact = "0";
				}
				if(costo == null || costo.isEmpty() || costo == "null"){
					costo = "0";
				}
				if(ttl_cajas_mes1 == null || ttl_cajas_mes1.isEmpty() || ttl_cajas_mes1 == "null"){
					ttl_cajas_mes1 = "0";
				}
				if(ttl_cajas_mes2 == null || ttl_cajas_mes2.isEmpty() || ttl_cajas_mes2 == "null"){
					ttl_cajas_mes2 = "0";
				}
				if(ttl_cajas_mes3 == null || ttl_cajas_mes3.isEmpty() || ttl_cajas_mes3 == "null"){
					ttl_cajas_mes3 = "0";
				}
				if(ttl_cajas_mes4 == null || ttl_cajas_mes4.isEmpty() || ttl_cajas_mes4 == "null"){
					ttl_cajas_mes4 = "0";
				}
				if(ttl_cajas_mes5 == null || ttl_cajas_mes5.isEmpty() || ttl_cajas_mes5 == "null"){
					ttl_cajas_mes5 = "0";
				}
				if(ttl_cajas_mes6 == null || ttl_cajas_mes6.isEmpty() || ttl_cajas_mes6 == "null"){
					ttl_cajas_mes6 = "0";
				}
				if(ttl_cajas_mes7 == null || ttl_cajas_mes7.isEmpty() || ttl_cajas_mes7 == "null"){
					ttl_cajas_mes7 = "0";
				}
				if(ttl_cajas_mes8 == null || ttl_cajas_mes8.isEmpty() || ttl_cajas_mes8 == "null"){
					ttl_cajas_mes8 = "0";
				}
				if(ttl_cajas_mes9 == null || ttl_cajas_mes9.isEmpty() || ttl_cajas_mes9 == "null"){
					ttl_cajas_mes9 = "0";
				}
				if(ttl_cajas_mes10 == null || ttl_cajas_mes10.isEmpty() || ttl_cajas_mes10 == "null"){
					ttl_cajas_mes10 = "0";
				}
				if(ttl_cajas_mes11 == null || ttl_cajas_mes11.isEmpty() || ttl_cajas_mes11 == "null"){
					ttl_cajas_mes11 = "0";
				}
				if(ttl_cajas_mes12 == null || ttl_cajas_mes12.isEmpty() || ttl_cajas_mes12 == "null"){
					ttl_cajas_mes12 = "0";
				}
				
				hm.put(rs.getString("id_marca")+"_ttl_mes", String.valueOf(total));
				hm.put(rs.getString("id_marca")+"_ttl_cajas_ext", ttl_cajas_ext);
				hm.put(rs.getString("id_marca")+"_pendiente_x_fact", pendiente_x_fact);
				hm.put(rs.getString("id_marca")+"_costo", costo);
				//System.out.println(rs.getString("id_marca")+","+ total);
			}
						
			return hm;
		}
		
		//Totales por marca, previcion compras
		public HashMap getDataMesesTtlPrevCompras(String id_customer, String id_user, String id_modulo, String id_dashboard, int numMes, String chart_id, String activo){
			HashMap<String, String> hm = new HashMap<String, String>();
			//String sqlDatos = "";
			//sqlDatos = getSQLAnalisisVentas("2013");
			
			String slqlFiltros = "SELECT COUNT(parametro) num, parametro FROM ppto_filtros_portlets WHERE " +
					"id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' " +
							"AND id_dashboard='"+id_dashboard+"' AND id_portlet='"+chart_id+"' GROUP BY parametro";
			System.out.println("SqlFiltros Marcas: "+slqlFiltros);
			String filtroSql = "";
			String filtrosFinal = "";
			String cmp_fil = "";
			SqlRowSet rs_f = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs_f.next()){
				
				cmp_fil = rs_f.getString("parametro");
				if(rs_f.isFirst()){
					filtrosFinal += "WHERE "+cmp_fil+" IN(";
				}else{
					filtrosFinal += "AND "+cmp_fil+" IN(";
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' " +
						" AND id_customer='"+id_customer+"' AND id_user='"+id_user+"' AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"'";
				
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				while(rf.next()){
					if(!rf.isLast()){
						filtrosFinal +="'"+ rf.getString("value")+"', ";
					}else{
						filtrosFinal +="'"+ rf.getString("value")+"') ";
					}				
				}
			}
			
			String fil_activo = "";
			//System.out.println("Activo: "+activo);
			if(activo != null && !activo.equals("null") && !activo.equals("undefined") && !activo.isEmpty()){
				//System.out.println("Filtro ---->");
				if(filtrosFinal.isEmpty()){
					fil_activo  = " WHERE estatus='"+activo+"' ";
				}else{
					fil_activo  = " AND estatus='"+activo+"' ";
				}
			}
			//System.out.println("Filtro_Avt "+fil_activo);
			
			String sql = "SELECT" +
					" id_producto," +
					" producto," +
					" id_marca,"+
					" SUM(ttl_cajas_mes1) ttl_cajas_mes1," +
					" SUM(ttl_cajas_mes2) ttl_cajas_mes2," +
					" SUM(ttl_cajas_mes3) ttl_cajas_mes3," +
					" SUM(ttl_cajas_mes4) ttl_cajas_mes4," +
					" SUM(ttl_cajas_mes5) ttl_cajas_mes5," +
					" SUM(ttl_cajas_mes6) ttl_cajas_mes6," +
					" SUM(ttl_cajas_mes7) ttl_cajas_mes7," +
					" SUM(ttl_cajas_mes8) ttl_cajas_mes8," +
					" SUM(ttl_cajas_mes9) ttl_cajas_mes9," +
					" SUM(ttl_cajas_mes10) ttl_cajas_mes10," +
					" SUM(ttl_cajas_mes11) ttl_cajas_mes11," +
					" SUM(ttl_cajas_mes12) ttl_cajas_mes12," +
					" SUM(ttl_cajas_ext) ttl_cajas_ext," +
					" SUM(pendiente_x_fact) pendiente_x_fact," +
					" SUM(diasp1) diasp1," +
					//" SUM(fechap1)," +
					" SUM(cajasp1) cajasp1," +
					" SUM(diasp2) diasp2," +
					//" fechap2," +
					" SUM(cajasp2) cajasp2," +
					" SUM(diasp3) diasp3," +
					//" fechap3," +
					" SUM(cajasp3) cajasp3," +
					" SUM(minstock) minstock," +
					" SUM(maxstock) maxstock," +
					" SUM(tprov) tprov," +
					//" SUM(idorigen,"+
					" SUM(costo * (ttl_cajas_ext - pendiente_x_fact)) costo"+
					" FROM" +
					" prev_compras "+filtrosFinal +
					fil_activo +
					" GROUP BY id_marca ";
			System.out.println("SQL - Marcas "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				Double total = 0.0;
				
				String ttl_mes = "0"; 
				for(int xMes = 1; xMes <= numMes; xMes++ ){
					ttl_mes = rs.getString("ttl_cajas_mes"+xMes);
					if(ttl_mes == null || ttl_mes.isEmpty() || ttl_mes == "null"){
						ttl_mes = "0";
					}
					hm.put(rs.getString("id_marca")+"_"+xMes, ttl_mes);
					//System.out.println(rs.getString("id_marca")+"_"+xMes+","+ ttl_mes);
					total += Double.parseDouble(ttl_mes);
				}
				String ttl_cajas_ext = "0";
				String pendiente_x_fact = "0";
				String costo = "0";
				String diasp1 = "0";
				String cajasp1 = "0";
				String diasp2 = "0";
				String cajasp2 = "0";
				String diasp3 = "0";
				String cajasp3 = "0";
				String minstock = "0";
				String maxstock = "0";
				String tprov = "0";
				ttl_cajas_ext = rs.getString("ttl_cajas_ext");
				pendiente_x_fact = rs.getString("pendiente_x_fact");
				costo = rs.getString("costo");
				diasp1 = rs.getString("diasp1");
				cajasp1 = rs.getString("cajasp1");
				diasp2 = rs.getString("diasp2");
				cajasp2 = rs.getString("cajasp2");
				diasp3 = rs.getString("diasp3");
				cajasp3 = rs.getString("cajasp3");
				minstock = rs.getString("minstock");
				maxstock = rs.getString("maxstock");
				tprov = rs.getString("tprov");
								
				if(ttl_cajas_ext == null || ttl_cajas_ext.isEmpty() || ttl_cajas_ext == "null"){
					ttl_cajas_ext = "0";
				}
				if(pendiente_x_fact == null || pendiente_x_fact.isEmpty() || pendiente_x_fact == "null"){
					pendiente_x_fact = "0";
				}
				if(costo == null || costo.isEmpty() || costo == "null"){
					costo = "0";
				}
				if(cajasp1 == null || cajasp1.isEmpty() || cajasp1 == "null"){
					cajasp1 = "0";
				}
				if(diasp1 == null || diasp1.isEmpty() || diasp1 == "null"){
					diasp1 = "0";
				}
				if(cajasp2 == null || cajasp2.isEmpty() || cajasp2 == "null"){
					cajasp2 = "0";
				}
				if(diasp2 == null || diasp2.isEmpty() || diasp2 == "null"){
					diasp2 = "0";
				}
				if(cajasp3 == null || cajasp3.isEmpty() || cajasp3 == "null"){
					cajasp3 = "0";
				}
				if(diasp3 == null || diasp3.isEmpty() || diasp3 == "null"){
					diasp3 = "0";
				}
				if(minstock == null || minstock.isEmpty() || minstock == "null"){
					minstock = "0";
				}
				if(maxstock == null || maxstock.isEmpty() || maxstock == "null"){
					maxstock = "0";
				}
				if(tprov == null || tprov.isEmpty() || tprov == "null"){
					tprov = "0";
				}
				
				
				hm.put(rs.getString("id_marca")+"_ttl_mes", String.valueOf(total));
				hm.put(rs.getString("id_marca")+"_ttl_cajasp1", cajasp1);
				hm.put(rs.getString("id_marca")+"_ttl_diasp1", diasp1);
				hm.put(rs.getString("id_marca")+"_ttl_cajasp2", cajasp2);
				hm.put(rs.getString("id_marca")+"_ttl_diasp2", diasp2);
				hm.put(rs.getString("id_marca")+"_ttl_cajasp3", cajasp3);
				hm.put(rs.getString("id_marca")+"_ttl_diasp3", diasp3);
				
				hm.put(rs.getString("id_marca")+"_ttl_cajas_ext", ttl_cajas_ext);
				hm.put(rs.getString("id_marca")+"_pendiente_x_fact", pendiente_x_fact);
				hm.put(rs.getString("id_marca")+"_costo", costo);
				
				hm.put(rs.getString("id_marca")+"_minstock", minstock);
				hm.put(rs.getString("id_marca")+"_maxstock", maxstock);
				hm.put(rs.getString("id_marca")+"_tprov", tprov);
				hm.put(rs.getString("id_marca")+"_costo", costo);
			}
						
			return hm;
		}
		
		
		/*public String getSQLAnalisisVentas(String anio){
		String sql ;
		sql = " SELECT sm.desc_prod_n6 marca, dp.desc_prod_n7 producto FROM fact_presupuestos fp RIGHT JOIN" +
				" soporte_marca sm ON sm.id_sop_marca = fp.id_sop_marca RIGHT JOIN" +
				" dim_productos dp ON dp.id_dim_prod = fp.id_dim_prod WHERE fp.mes='1' AND fp.anio='2013'" +
				" GROUP by fp.id_sop_marca, fp.id_dim_prod";
		sql = "SELECT dim_tiempo_fecha fecha, id_dim_prod producto, id_sop_marca marca, cantcajasfact cajas" +
				" FROM ventas GROUP by dim_tiempo_fecha, id_dim_prod, id_sop_marca";
		System.out.println("SQL Analisis Ventas "+sql);
		return sql;
	}*/
	
		// Obtiene datos para tabla insumos
		public List<InvInsumos> getDataInsumos(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet,
				String fechaIni, String fechaFin, String idEspacios) throws ParseException{
			
			List<InvInsumos> rows = new ArrayList<InvInsumos>();
			
			
			String filtro = "";
			String join = "";
			String cmp_ajuste = "";
			String inv_ini = " 0 ";
			System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
			
			
			String filtroEspacios = "";
			String filtroEspaciosJoin = "";
			String filtroEspaciosJoinM = "";
			/*String[] espaciosArray ;
			if(idEspacios != null ){
				espaciosArray = idEspacios.split(",");
				for (int i = 0; i < espaciosArray.length; i++) {
		            if(i == 0){
		            	filtroEspacios += "AND mov.idalmacen IN("+espaciosArray[i];
		            }else{
		            	filtroEspacios += ","+espaciosArray[i];
		            }
		            if(i == espaciosArray.length - 1){
		            	filtroEspacios += ") ";
		            }
		        }
			}*/
	        
			System.out.println("Filtros Array: "+filtroEspacios);
	        
			if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
				if(idEspacios.equals("0")){
					filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
					filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
				}else{
					filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
					filtroEspaciosJoin = " AND idalmacen IN("+idEspacios+") ";
					filtroEspaciosJoinM = " AND m.idalmacen IN("+idEspacios+") ";
				}
			}else{
				filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
				filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
				filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
			}
			
			if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
					&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
			
				filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
				inv_ini = " T1.invent_ini";
				cmp_ajuste = " T3.ajuste ajuste,"; 
				join = " LEFT JOIN" +
						" (SELECT ifnull(m.id_dim_prod,0) id_dim_prod,m.itemcode," +
						" ((SUM(m.pzasE)-SUM(m.pzasS))-ifnull(T2.ajuste,0)) AS invent_ini" +
						" FROM mov2 m" +
						" LEFT JOIN (SELECT a.id_ins_comp,a.itemcodeic,SUM(a.cantidad) ajuste" +
						" FROM mov_ajustes a WHERE a.fechaaj < '"+fechaIni+"' GROUP by a.id_ins_comp,a.itemcodeic) AS T2" +
						" ON T2.id_ins_comp=m.id_dim_prod AND T2.itemcodeic=m.itemcode"+
						" WHERE m.tipo=1 AND m.dim_tiempo_fecha < '"+fechaIni+"' "+ filtroEspaciosJoinM +
						" GROUP by m.id_dim_prod,m.itemcode) AS T1" +
						" ON mov.id_dim_prod = T1.id_dim_prod AND mov.itemcode=T1.itemcode "+ filtroEspacios +
						" LEFT JOIN (SELECT aj.id_ins_comp,SUM(aj.cantidad) ajuste FROM mov_ajustes aj" +
						" WHERE aj.fechaaj BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' GROUP by aj.idalmacen,aj.id_ins_comp) T3" +
						" ON T3.id_ins_comp=mov.id_dim_prod ,";
			}else{
				cmp_ajuste = " T2.ajuste ajuste,";
				join = " LEFT JOIN (SELECT id_ins_comp,SUM(a.cantidad) ajuste " +
						"FROM mov_ajustes a GROUP by idalmacen,id_ins_comp) T2 " +
						"ON T2.id_ins_comp = mov.id_dim_prod,";
			}
			
			//System.out.println("Filtros Final..."+filtrosFinal);*/
			DecimalFormat formatter = new DecimalFormat("###,##0.000");
			
			String sql = "";/*"SELECT" +
					" prod.id_prod_n0 id_prod_term," +
					" prod.desc_prod_n0 desc_prod_term," +
					" mov.id_dim_prod id_prod_int," +
					" mov.itemcode itemcode," +
					" prod.desc_prod_n0m desc_prod_int ," +
					" prod.pzas piezas," +
					" SUM(mov.pzasE) entradas," +
					" SUM(mov.pzasS) salidas," +
					" SUM(mov.pzasE)-SUM(mov.pzasS) invent_ini," +
					" mov.costo costo" +
					" FROM" +
					" mov2 mov," +
					" prod_marco prod" +
					" WHERE" +
					" mov.id_dim_prod = prod.id_dim_prodm" +
					" AND mov.itemcode = prod.id_prod_n0m" +
					filtroEspacios+
					/-" AND mov.dim_tiempo_fecha" +
					" BETWEEN '2013-02-15' AND '2013-07-03'" +-/
					filtro +
					" AND prod.idp = 1" +
					" AND mov.tipo=1"+
					" GROUP BY  prod.id_prod_n0,"+
					" prod.desc_prod_n0," +
					" mov.id_dim_prod," +
					" mov.itemcode," +
					" prod.desc_prod_n0m" +					
					" ORDER BY prod.id_prod_n0";*/
			sql= "SELECT" +
					" prod.id_prod_n0 id_prod_term," +
					" prod.desc_prod_n0 desc_prod_term," +
					" mov.id_dim_prod id_prod_int," +
					" mov.itemcode itemcode," +
					" prod.desc_prod_n0m desc_prod_int," +
					" ROUND(prod.pzas, 2) piezas,idalmacen," +
					" SUM(mov.pzasE) entradas," +
					" SUM(mov.pzasS) salidas," +
					cmp_ajuste + 
					inv_ini +" invent_ini," +
					" ((("+inv_ini+") + SUM(mov.pzasE) - SUM(mov.pzasS)) / prod.pzas) num_prod," +
					" mov.costo costo," +
					" prod.idp idp" +
					" FROM" +
					" mov2 mov" +
					join +
					" prod_marco prod" +
					" WHERE" +
					" mov.id_dim_prod = prod.id_dim_prodm" +
					" AND mov.itemcode = prod.id_prod_n0m" +
					//" AND prod.idp = 1 " +
					" AND tipo=1" +
					//" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
					//" AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'" +
					filtroEspacios+
					filtro +
					" GROUP BY" +
					" prod.id_prod_n0," +
					" prod.desc_prod_n0," +
					" mov.itemcode," +
					" prod.desc_prod_n0m" +
					" ORDER BY prod.desc_prod_n0,prod.id_prod_n0";
			System.out.println("Insumos: "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				InvInsumos dto = new InvInsumos();
				String idProductoTerm = rs.getString("id_prod_term");
				String descProductoTerm = rs.getString("desc_prod_term");
				String idProducto = rs.getString("id_prod_int");
				String itemcodeProd = rs.getString("itemcode");
				String descProducto = rs.getString("desc_prod_int");
				String piezas = rs.getString("piezas");
				String inventInicial = rs.getString("invent_ini");
				String entradas = rs.getString("entradas");
				String salidas = rs.getString("salidas");
				String inventFinal = null;//rs.getString("invent_final");
				String costo = rs.getString("costo");
				String idp = rs.getString("idp");
				String ajuste = rs.getString("ajuste");
				if(piezas == null){
					piezas = "0";
				}
				if(inventInicial == null){
					inventInicial = "0";
				}
				if(entradas == null){
					entradas = "0";
				}
				if(salidas == null){
					salidas = "0";
				}
				if(ajuste == null){
					ajuste = "0";
				}else{
					System.out.println(itemcodeProd+ " "+ajuste);
				}
				//if(inventFinal == null){
				
					inventFinal = String.valueOf(Double.parseDouble(inventInicial) + Double.parseDouble(entradas) - Double.parseDouble(salidas) + Double.parseDouble(ajuste));
				//}
				if(costo == null){
					costo = "0";
				}
				dto.setIdProductoTerm(idProductoTerm);
				dto.setDescProductoTerm(descProductoTerm);
				dto.setIdProducto(idProducto);
				dto.setDescProducto(descProducto);
				dto.setItemcodeProd(itemcodeProd);
				dto.setPiezas(piezas);
				dto.setInventInicial(inventInicial);
				dto.setEntradas(entradas);
				dto.setSalidas(salidas);
				dto.setInventFinal(inventFinal);
				dto.setCosto(costo);
				dto.setIdp(idp);
				dto.setAjuste(ajuste);
				rows.add(dto);
			}
			return rows;
		}
		
		//Obtiene datos de marcas para insumos
		public HashMap getDataInsumosMarcas(String id_customer, String id_user, String id_modulo, String id_dashboard, String chart_id,
				String fechaIni, String fechaFin, String idEspacios) throws ParseException{
			HashMap<String, String> hm = new HashMap<String, String>();
			
			String filtro = "";
			String join = ",";
			String inv_ini = " 0 ";
			System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
			
			
			String filtroEspacios = "";
			String filtroEspaciosJoin = "";
			String filtroEspaciosJoinM = "";
			/*String[] espaciosArray ;
			if(idEspacios != null ){
				espaciosArray = idEspacios.split(",");
				for (int i = 0; i < espaciosArray.length; i++) {
		            if(i == 0){
		            	filtroEspacios += "AND mov.idalmacen IN("+espaciosArray[i];
		            }else{
		            	filtroEspacios += ","+espaciosArray[i];
		            }
		            if(i == espaciosArray.length - 1){
		            	filtroEspacios += ") ";
		            }
		        }
			}*/
	        
			System.out.println("Filtros Array: "+filtroEspacios);
	        
			if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
				if(idEspacios.equals("0")){
					filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
					filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
				}else{
					filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
					filtroEspaciosJoin = " AND idalmacen IN("+idEspacios+") ";
					filtroEspaciosJoinM = " AND m.idalmacen IN("+idEspacios+") ";
				}
			}else{
				filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
				filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
				filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
			}
			String cmp_ajuste = "";
			if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
					&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
			
				filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
				inv_ini = " T1.invent_ini";
				cmp_ajuste = " T3.ajuste "; 
				join = " LEFT JOIN" +
						" (SELECT ifnull(m.id_dim_prod,0) id_dim_prod,m.itemcode," +
						" ((SUM(m.pzasE)-SUM(m.pzasS))-ifnull(T2.ajuste,0)) AS invent_ini" +
						" FROM mov2 m" +
						" LEFT JOIN (SELECT a.id_ins_comp,a.itemcodeic,SUM(a.cantidad) ajuste" +
						" FROM mov_ajustes a WHERE a.fechaaj < '"+fechaIni+"' GROUP by a.id_ins_comp,a.itemcodeic) AS T2" +
						" ON T2.id_ins_comp=m.id_dim_prod AND T2.itemcodeic=m.itemcode"+
						" WHERE m.tipo=1 AND m.dim_tiempo_fecha < '"+fechaIni+"' "+ filtroEspaciosJoinM +
						" GROUP by m.id_dim_prod,m.itemcode) AS T1" +
						" ON mov.id_dim_prod = T1.id_dim_prod AND mov.itemcode=T1.itemcode "+ filtroEspacios +
						" LEFT JOIN (SELECT aj.id_ins_comp,SUM(aj.cantidad) ajuste FROM mov_ajustes aj" +
						" WHERE aj.fechaaj BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' GROUP by aj.idalmacen,aj.id_ins_comp) T3" +
						" ON T3.id_ins_comp=mov.id_dim_prod ,";
			}else{
				cmp_ajuste = " T2.ajuste ";
				join = " LEFT JOIN (SELECT id_ins_comp,SUM(a.cantidad) ajuste " +
						"FROM mov_ajustes a GROUP by idalmacen,id_ins_comp) T2 " +
						"ON T2.id_ins_comp = mov.id_dim_prod,";
			}
			
			String sql = " SELECT" +
					" prb.id_prod_term id_prod_term," +
					" MIN(prb.num_prod) num_productos" +
					" FROM" +
					" (" +
					" SELECT" +
					" prod.id_prod_n0 id_prod_term," +
					" ( ((("+inv_ini+") + SUM(mov.pzasE) - SUM(mov.pzasS)) + SUM( ifnull("+cmp_ajuste+",0 )) ) / (ROUND(prod.pzas, 2))) num_prod" +
					" FROM" +
					" mov2 mov " +
					join +
					" prod_marco prod" +
					" WHERE" +
					" mov.id_dim_prod = prod.id_dim_prodm" +
					" AND mov.itemcode = prod.id_prod_n0m" +
					filtroEspacios+ //" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
					filtro+
					 /*" AND mov.dim_tiempo_fecha" +
					" BETWEEN '2013-02-15'" +
					" AND '2013-07-03'" +*/
					" AND prod.idp = 1" +
					" AND mov.tipo=1 "+
					" GROUP BY  prod.id_prod_n0,"+
					" prod.desc_prod_n0," +
					" mov.id_dim_prod," +
					" mov.itemcode," +
					" prod.desc_prod_n0m" +
					" ORDER BY prod.id_prod_n0) prb" +
					" GROUP by prb.id_prod_term";
			System.out.println("Insumos:-- "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				
				InvInsumos dto = new InvInsumos();
				String idProductoTerm = rs.getString("id_prod_term");
				String numProductos = rs.getString("num_productos");
				if(numProductos == null || numProductos.isEmpty() || numProductos.equals("null")){
					numProductos = "0";
				}
				hm.put(idProductoTerm, numProductos);
			}
						
			return hm;
		}
		
	//Datos para Listar Productos Por lugares
				public List<InvInsumos> getDataListaEntradasLugares(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_prod_term,
						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
					List<InvInsumos> rows = new ArrayList<InvInsumos>();		
					
					String filtro = "";
					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
					}
					
					String filtroEspacios = "";
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					}
					
					System.out.println("Lista Prod Lugares --1  "+id_prod_term);
					String filProdTerm = "";
					if(!id_prod_term.isEmpty() && id_prod_term != null){
						filProdTerm = " AND prod.id_prod_n0 = '"+id_prod_term+"'";
					}
					System.out.println(" Filtro ProdTerm "+filProdTerm);
					DecimalFormat formatter = new DecimalFormat("###,##0.000");
					String sql = "SELECT" +
							" prod.id_prod_n0 id_prod_term," +
							" prod.desc_prod_n0 desc_prod_term," +
							" prod.id_prod_n0m id_prod_int," +
							" prod.desc_prod_n0m desc_prod_int," +
							" mov.dim_tiempo_fecha fecha," +
							" mov.idalmacen id_almacen," +
							" mov.nomalmacen desc_almacen," +
							" ent.cantidad cantidad," +
							" ent.entradas entradas," +
							" mov.pzasE piezas" +
							" FROM" +
							" mov2 mov," +
							" prod_marco prod," +
							" (" +
							" SELECT" +
							"  	prod.id_prod_n0," +
							"	prod.desc_prod_n0," +
							" 	prod.id_prod_n0m," +
							" 	prod.desc_prod_n0m," +
							" 	SUM(mov.pzasE) cantidad," +
							" 	COUNT(mov.dim_tiempo_fecha) entradas" +
							" FROM" +
							" 	mov2 mov," +
							" 	prod_marco prod" +
							" WHERE" +
							"	mov.id_dim_prod = prod.id_dim_prodm" +
							"	AND mov.itemcode = prod.id_prod_n0m" +
							"	AND mov.tipo=1" +
							//"	AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							filtroEspacios +
							filtro +
							filProdTerm +
							"	AND mov.pzasE!=0" +
							//"	AND prod.id_prod_n0 = 'MPO-0605'" +
							//"	AND mov.idalmacen = '06'" +
							" GROUP by" +
							"	mov.itemcode ," +
							"	mov.nomalmacen" +
							" ORDER BY" +
							"	prod.id_prod_n0m" +
							" ) ent" +
							" WHERE" +
							" mov.id_dim_prod = prod.id_dim_prodm" +
							" AND mov.itemcode = prod.id_prod_n0m" +
							" AND prod.id_prod_n0 = ent.id_prod_n0" +
							" AND prod.id_prod_n0m = ent.id_prod_n0m" +
							" AND mov.tipo=1" +
							//" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							filtroEspacios +
							filtro +
							filProdTerm +
							" AND mov.pzasE!=0" +
							// " AND prod.id_prod_n0 = 'MPO-0605'" +
							//" AND mov.idalmacen = '06'" +
							" GROUP by" +
							" mov.itemcode ," +
							" mov.nomalmacen," +
							" mov.dim_tiempo_fecha" +
							" ORDER BY" +
							" prod.id_prod_n0m, mov.dim_tiempo_fecha ASC";
					System.out.println("Entradas: -->"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dto = new InvInsumos();
						String idProductoTerm = rs.getString("id_prod_term");
						String descProductoTerm = rs.getString("desc_prod_term");
						String idProducto = rs.getString("id_prod_int");
						//String itemcodeProd = rs.getString("itemcode");
						String descProducto = rs.getString("desc_prod_int");
						String idAlmacen = rs.getString("id_almacen");
						String descAlmacen = rs.getString("desc_almacen");
						String cantidad = rs.getString("cantidad");
						String entradas = rs.getString("entradas");
						String fecha = rs.getString("fecha");	
						String piezas = rs.getString("piezas");
						
						if(piezas == null){
							piezas = "0";
						}
						if(cantidad == null){
							cantidad = "0";
						}
						if(entradas == null){
							entradas = "0";
						}
						
						dto.setIdProductoTerm(idProductoTerm);
						dto.setDescProductoTerm(descProductoTerm);
						dto.setIdProducto(idProducto);
						dto.setDescProducto(descProducto);
						dto.setIdAlmacen(idAlmacen);
						dto.setDescAlmacen(descAlmacen);
						dto.setCantidad(cantidad);
						dto.setEntradas(entradas);
						dto.setFecha(fecha);
						dto.setPiezas(piezas);
						rows.add(dto);
					}
					return rows;
				}

				// Se enlistan datos para salidas
				public List<InvInsumos> getDataListaSalidasLugares(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_prod_term,
						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
					List<InvInsumos> rows = new ArrayList<InvInsumos>();		
					
					String filtro = "";
					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
					}
					
					String filtroEspacios = "";
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					}
					
					System.out.println("Lista Prod Lugares --1  "+id_prod_term);
					String filProdTerm = "";
					if(!id_prod_term.isEmpty() && id_prod_term != null){
						filProdTerm = " AND prod.id_prod_n0 = '"+id_prod_term+"'";
					}
					System.out.println(" Filtro ProdTerm "+filProdTerm);
					DecimalFormat formatter = new DecimalFormat("###,##0.000");
					String sql = "SELECT" +
							" prod.id_prod_n0 id_prod_term," +
							" prod.desc_prod_n0 desc_prod_term," +
							" prod.id_prod_n0m id_prod_int," +
							" prod.desc_prod_n0m desc_prod_int," +
							" mov.dim_tiempo_fecha fecha," +
							" mov.idalmacen id_almacen," +
							" mov.nomalmacen desc_almacen," +
							" sal.cantidad cantidad," +
							" sal.salidas salidas," +
							" mov.pzasS piezas" +
							" FROM" +
							" mov2 mov," +
							" prod_marco prod," +
							" (" +
							" SELECT" +
							"  	prod.id_prod_n0," +
							"	prod.desc_prod_n0," +
							" 	prod.id_prod_n0m," +
							" 	prod.desc_prod_n0m," +
							" 	SUM(mov.pzasS) cantidad," +
							" 	COUNT(mov.dim_tiempo_fecha) salidas" +
							" FROM" +
							" 	mov2 mov," +
							" 	prod_marco prod" +
							" WHERE" +
							"	mov.id_dim_prod = prod.id_dim_prodm" +
							"	AND mov.itemcode = prod.id_prod_n0m" +
							"	AND mov.tipo=1" +
							//"	AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							filtroEspacios +
							filtro +
							filProdTerm +
							"	AND mov.pzasS!=0" +
							//"	AND prod.id_prod_n0 = 'MPO-0605'" +
							//"	AND mov.idalmacen = '06'" +
							" GROUP by" +
							"	mov.itemcode ," +
							"	mov.nomalmacen" +
							" ORDER BY" +
							"	prod.id_prod_n0m" +
							" ) sal" +
							" WHERE" +
							" mov.id_dim_prod = prod.id_dim_prodm" +
							" AND mov.itemcode = prod.id_prod_n0m" +
							" AND prod.id_prod_n0 = sal.id_prod_n0" +
							" AND prod.id_prod_n0m = sal.id_prod_n0m" +
							" AND mov.tipo=1" +
							//" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							filtroEspacios +
							filtro +
							filProdTerm +
							" AND mov.pzasS!=0" +
							// " AND prod.id_prod_n0 = 'MPO-0605'" +
							//" AND mov.idalmacen = '06'" +
							" GROUP by" +
							" mov.itemcode ," +
							" mov.nomalmacen," +
							" mov.dim_tiempo_fecha" +
							" ORDER BY" +
							" prod.id_prod_n0m, mov.dim_tiempo_fecha ASC";
					System.out.println("Salidas: -->"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dto = new InvInsumos();
						String idProductoTerm = rs.getString("id_prod_term");
						String descProductoTerm = rs.getString("desc_prod_term");
						String idProducto = rs.getString("id_prod_int");
						//String itemcodeProd = rs.getString("itemcode");
						String descProducto = rs.getString("desc_prod_int");
						String idAlmacen = rs.getString("id_almacen");
						String descAlmacen = rs.getString("desc_almacen");
						String cantidad = rs.getString("cantidad");
						String salidas = rs.getString("salidas");
						String fecha = rs.getString("fecha");	
						String piezas = rs.getString("piezas");
						
						if(piezas == null){
							piezas = "0";
						}
						if(cantidad == null){
							cantidad = "0";
						}
						if(salidas == null){
							salidas = "0";
						}
						
						dto.setIdProductoTerm(idProductoTerm);
						dto.setDescProductoTerm(descProductoTerm);
						dto.setIdProducto(idProducto);
						dto.setDescProducto(descProducto);
						dto.setIdAlmacen(idAlmacen);
						dto.setDescAlmacen(descAlmacen);
						dto.setCantidad(cantidad);
						dto.setSalidas(salidas);
						dto.setFecha(fecha);
						dto.setPiezas(piezas);
						rows.add(dto);
					}
					return rows;
				}
				
				//Entradas Salidas Insumos componentes
				public List<InvInsumos> getDataListaEntradasLugaresInsComp(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_comp_insum,
						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
					List<InvInsumos> rows = new ArrayList<InvInsumos>();		
					
					String filtro = "";
					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
					}
					
					String filtroEspacios = "";
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					}
					
					System.out.println("Lista Prod Lugares --1  "+id_comp_insum);
					String filProdTerm = "";
					if(!id_comp_insum.isEmpty() && id_comp_insum != null){
						filProdTerm = " AND mov.itemcode = '"+id_comp_insum+"'";
					}
					System.out.println(" Filtro ProdTerm "+filProdTerm);
					DecimalFormat formatter = new DecimalFormat("###,##0.000");
					String sql = "SELECT" +
							" mov.itemcode itemcode_prod," +
							" mov.desc_prod_n0 desc_prod," +
							" mov.dim_tiempo_fecha fecha," +
							" mov.idalmacen id_almacen," +
							" mov.nomalmacen desc_almacen," +
							" ent.cantidad cantidad," +
							" ent.entradas entradas," +
							" mov.pzasE piezas" +
							" FROM" +
							" mov2 mov," +
							" prod_marco prod," +
							" (" +
							" SELECT" +
							" mov.itemcode," +
							" mov.idalmacen," +
							" 	SUM(mov.pzasE) cantidad," +
							" 	COUNT(mov.dim_tiempo_fecha) entradas" +
							" FROM" +
							" 	mov2 mov" +
							//" 	prod_marco prod" +
							" WHERE" +
							//"	mov.id_dim_prod = prod.id_dim_prodm" +
							//"	AND mov.itemcode = prod.id_prod_n0m" +
							" mov.tipo=1" +
							"	AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							//filtroEspacios +
							filtro +
							filProdTerm +
							"	AND mov.pzasE!=0" +
							//"	AND prod.id_prod_n0 = 'MPO-0605'" +
							//"	AND mov.idalmacen = '06'" +
							" GROUP by" +
							"	mov.itemcode ," +
							"	mov.nomalmacen" +
							" ORDER BY" +
							"	mov.dim_tiempo_fecha" +
							" ) ent" +
							" WHERE" +
							"  mov.itemcode = ent.itemcode" +
							" AND mov.idalmacen=ent.idalmacen"+
							" AND mov.tipo=1" +
							" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							//filtroEspacios +
							filtro +
							filProdTerm +
							" AND mov.pzasE!=0" +
							// " AND prod.id_prod_n0 = 'MPO-0605'" +
							//" AND mov.idalmacen = '06'" +
							" GROUP by" +
							" mov.itemcode ," +
							" mov.idalmacen," +
							" mov.dim_tiempo_fecha" +
							" ORDER BY" +
							" mov.idalmacen,mov.dim_tiempo_fecha ASC";
					System.out.println("Entradas Componentes - Insumos: -->"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dto = new InvInsumos();
						String itemcodeProd = rs.getString("itemcode_prod");
						String descProducto = rs.getString("desc_prod");
						String fecha = rs.getString("fecha");
						String idAlmacen = rs.getString("id_almacen");
						String descAlmacen = rs.getString("desc_almacen");
						String cantidad = rs.getString("cantidad");
						String entradas = rs.getString("entradas");
						String piezas = rs.getString("piezas");
						
						if(piezas == null){
							piezas = "0";
						}
						if(cantidad == null){
							cantidad = "0";
						}
						if(entradas == null){
							entradas = "0";
						}
						
						
						dto.setIdProducto(itemcodeProd);
						dto.setDescProducto(descProducto);
						dto.setIdAlmacen(idAlmacen);
						dto.setDescAlmacen(descAlmacen);
						dto.setCantidad(cantidad);
						dto.setEntradas(entradas);
						dto.setFecha(fecha);
						dto.setPiezas(piezas);
						rows.add(dto);
					}
					return rows;
				}

				// Se enlistan datos para salidas
				public List<InvInsumos> getDataListaSalidasLugaresInsComp(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_comp_insum,
						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
					List<InvInsumos> rows = new ArrayList<InvInsumos>();		
					
					String filtro = "";
					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
					}
					
					String filtroEspacios = "";
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					}
					
					System.out.println("Lista Prod Lugares --1  "+id_comp_insum);
					String filProdTerm = "";
					if(!id_comp_insum.isEmpty() && id_comp_insum != null){
						filProdTerm = " AND mov.itemcode = '"+id_comp_insum+"'";
					}
					System.out.println(" Filtro ProdTerm "+filProdTerm);
					DecimalFormat formatter = new DecimalFormat("###,##0.000");
					String sql = "SELECT" +
							" mov.itemcode itemcode_prod," +
							" mov.desc_prod_n0 desc_prod," +
							" mov.dim_tiempo_fecha fecha," +
							" mov.idalmacen id_almacen," +
							" mov.nomalmacen desc_almacen," +
							" sal.cantidad cantidad," +
							" sal.salidas salidas," +
							" mov.pzasS piezas" +
							" FROM" +
							" mov2 mov," +
							//" prod_marco prod," +
							" (" +
							" SELECT" +
							" mov.itemcode," +
							" mov.idalmacen," +
							" 	SUM(mov.pzasS) cantidad," +
							" 	COUNT(mov.dim_tiempo_fecha) salidas" +
							" FROM" +
							" 	mov2 mov" +
							//" 	prod_marco prod" +
							" WHERE" +
							//"	mov.id_dim_prod = prod.id_dim_prodm" +
							//"	AND mov.itemcode = prod.id_prod_n0m" +
							" mov.tipo=1" +
							"	AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							//filtroEspacios +
							filtro +
							filProdTerm +
							"	AND mov.pzasS!=0" +
							//"	AND prod.id_prod_n0 = 'MPO-0605'" +
							//"	AND mov.idalmacen = '06'" +
							" GROUP by" +
							"	mov.itemcode ," +
							"	mov.nomalmacen" +
							" ORDER BY" +
							"	mov.dim_tiempo_fecha" +
							" ) sal" +
							" WHERE" +
							"  mov.itemcode = sal.itemcode" +
							" AND mov.idalmacen = sal.idalmacen"+
							" AND mov.tipo=1" +
							" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)" +
							//filtroEspacios +
							filtro +
							filProdTerm +
							" AND mov.pzasS!=0" +
							// " AND prod.id_prod_n0 = 'MPO-0605'" +
							//" AND mov.idalmacen = '06'" +
							" GROUP by" +
							" mov.itemcode ," +
							" mov.idalmacen," +
							" mov.dim_tiempo_fecha" +
							" ORDER BY" +
							" mov.idalmacen,mov.dim_tiempo_fecha ASC";
					System.out.println("Salidas Componentes - Insumos: -->"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dto = new InvInsumos();
						String itemcodeProd = rs.getString("itemcode_prod");
						String descProducto = rs.getString("desc_prod");
						String fecha = rs.getString("fecha");
						String idAlmacen = rs.getString("id_almacen");
						String descAlmacen = rs.getString("desc_almacen");
						String cantidad = rs.getString("cantidad");
						String salidas = rs.getString("salidas");
						String piezas = rs.getString("piezas");
						
						if(piezas == null){
							piezas = "0";
						}
						if(cantidad == null){
							cantidad = "0";
						}
						if(salidas == null){
							salidas = "0";
						}
						
						
						dto.setIdProducto(itemcodeProd);
						dto.setDescProducto(descProducto);
						dto.setIdAlmacen(idAlmacen);
						dto.setDescAlmacen(descAlmacen);
						dto.setCantidad(cantidad);
						dto.setSalidas(salidas);
						dto.setFecha(fecha);
						dto.setPiezas(piezas);
						rows.add(dto);
					}
					return rows;
				}
				
				/// Nombre del insumo o componente
				public String getNombreInsComp(String id_comp_insum) {
					
					String sql = "SELECT  desc_prod_n0m nombre FROM prod_marco WHERE id_prod_n0m = '" +id_comp_insum+ "'";
					System.out.println("Nombre Componentes - Insumos: -->"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					String nomCompInsum = "";
					while( rs.next() ){
						nomCompInsum = rs.getString("nombre");
						
					}
					return nomCompInsum;
				}
//Se obtienen las entradas por lugar ------<>
				public List<InvInsumos> getDataListaProdLugares(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_prod_term,
						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
					List<InvInsumos> rows = new ArrayList<InvInsumos>();		
					
					String filtro = "";
					String join = "";
					String cmp_ajuste = "";
					String inv_ini = " 0 ";
					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					
					
					String filtroEspacios = "";
					String filtroEspaciosJoin = "";
					String filtroEspaciosJoinM = "";
					
					System.out.println("Filtros Array: "+filtroEspacios);
			        
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
							filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
							filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
							filtroEspaciosJoin = " AND idalmacen IN("+idEspacios+") ";
							filtroEspaciosJoinM = " AND m.idalmacen IN("+idEspacios+") ";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
						filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
						filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
					}
					
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
					System.out.println("Genera SQL-JOIN -<<<<<<<<<>>><");
						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
						inv_ini = " T1.invent_ini invent_ini,";
						cmp_ajuste = " T3.ajuster ajuste,"; 
						join = " LEFT JOIN" +
								" (SELECT ifnull(m.id_dim_prod,0) id_dim_prod,m.itemcode,m.idalmacen," +
								" ((SUM(m.pzasE)-SUM(m.pzasS))+ifnull(T2.ajuste,0)) AS invent_ini" +
								" FROM mov2 m" +
								" LEFT JOIN (SELECT a.id_ins_comp,a.itemcodeic,a.idalmacen," +
								" SUM(a.cantidad) ajuste FROM mov_ajustes a WHERE a.fechaaj < '"+fechaIni+"'" +
								" GROUP by a.id_ins_comp,a.itemcodeic) AS T2" +
								" ON T2.id_ins_comp=m.id_dim_prod AND T2.itemcodeic=m.itemcode AND T2.idalmacen=m.idalmacen" +
								" WHERE m.tipo=1 AND m.dim_tiempo_fecha < '"+fechaIni +"' "+ filtroEspaciosJoinM +
								" GROUP by m.id_dim_prod,m.itemcode,m.idalmacen) AS T1" +
								" ON mov.id_dim_prod = T1.id_dim_prod AND mov.itemcode=T1.itemcode AND " +
								" T1.idalmacen=mov.idalmacen " + filtroEspacios +
								" LEFT JOIN (SELECT aj.id_ins_comp,aj.idalmacen,SUM(aj.cantidad) ajuster" +
								" FROM mov_ajustes aj WHERE aj.fechaaj BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'" +
								" GROUP by aj.idalmacen,aj.id_ins_comp)T3" +
								" ON T3.id_ins_comp=mov.id_dim_prod AND T3.idalmacen=mov.idalmacen,";
					}else{
						inv_ini = " 0 invent_ini,";
						cmp_ajuste = " T3.ajuster ajuste,";
						join = " LEFT JOIN " +
								" (SELECT aj.id_ins_comp,aj.idalmacen,SUM(aj.cantidad) ajuster" +
								" FROM mov_ajustes aj GROUP by aj.idalmacen,aj.id_ins_comp)T3" +
								" ON T3.id_ins_comp=mov.id_dim_prod AND T3.idalmacen=mov.idalmacen,";
					}
					String filProdTerm = "";
					if(!id_prod_term.isEmpty() && id_prod_term != null){
						filProdTerm = " AND id_prod_n0 = '"+id_prod_term+"'";
					}
					//System.out.println("Filtros Final..."+filtrosFinal);*/
					DecimalFormat formatter = new DecimalFormat("###,##0.000");
					
					String sql = "SELECT" +
							" prod.id_dim_prod id_prod_term," +
							" prod.id_prod_n0 itemcode_prod_term,"+
							" prod.id_prod_n0 id_prod_term," +
							" prod.desc_prod_n0 desc_prod_term," +
							" mov.id_dim_prod id_prod_int," +
							" mov.itemcode itemcode," +
							" prod.desc_prod_n0m desc_prod_int," +
							" mov.idalmacen id_almacen,"+
							" mov.nomalmacen desc_almacen," +
							" ROUND(prod.pzas, 2) piezas, "+
							" mov.idalmacen," + 
							" SUM(mov.pzasE) entradas," +
							" SUM(mov.pzasS) salidas," +
							cmp_ajuste + 
							inv_ini +
							//" ((("+inv_ini+") + SUM(mov.pzasE) - SUM(mov.pzasS)) / prod.pzas) num_prod," +
							" mov.costo costo," +
							" prod.idp tipo_prod" +
							" FROM" +
							" mov2 mov" +
							join +
							" prod_marco prod" +
							" WHERE" +
							" mov.id_dim_prod = prod.id_dim_prodm" +
							" AND mov.itemcode = prod.id_prod_n0m" +
							// " AND prod.idp = 1" +
							" AND mov.tipo = 1" +
							filtroEspacios+
							filtro +
							filProdTerm +
							" GROUP BY" +
							" prod.id_dim_prod," +
							" prod.id_prod_n0," +
							" prod.id_dim_prodm," +
							" prod.id_prod_n0m," +
							" mov.itemcode," +
							" mov.idalmacen"+
							" ORDER BY mov.idalmacen";
					System.out.println("ListaProdlugares: --><--"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dto = new InvInsumos();
						String idProductoTerm = rs.getString("id_prod_term");
						String itemProductoTerm = rs.getString("itemcode_prod_term");
						String descProductoTerm = rs.getString("desc_prod_term");
						String idProducto = rs.getString("id_prod_int");
						String itemcodeProd = rs.getString("itemcode");
						String descProducto = rs.getString("desc_prod_int");
						String piezas = rs.getString("piezas");
						String idAlmacen = rs.getString("id_almacen");
						String descAlmacen = rs.getString("desc_almacen");
						String tipo_prod = rs.getString("tipo_prod");
						String inventInicial = rs.getString("invent_ini");
						String entradas = rs.getString("entradas");
						String salidas = rs.getString("salidas");
						String costo = rs.getString("costo");
						String ajuste = rs.getString("ajuste");
						if(entradas == null){
							entradas = "0";
						}
						if(salidas == null){
							salidas = "0";
						}
						if(ajuste == null){
							ajuste = "0";
						}
						if(inventInicial == null){
							inventInicial = "0";
						}
						if(piezas == null){
							piezas = "0";
						}
						if(costo == null){
							costo = "0";
						}
						
						String existencias = String.valueOf(Double.parseDouble(inventInicial) + Double.parseDouble(entradas) - Double.parseDouble(salidas) + Double.parseDouble(ajuste));
						dto.setIdProductoTerm(idProductoTerm);
						dto.setItemProductoTerm(itemProductoTerm);
						dto.setDescProductoTerm(descProductoTerm);
						dto.setIdProducto(idProducto);
						dto.setDescProducto(descProducto);
						dto.setItemcodeProd(itemcodeProd);
						dto.setIdAlmacen(idAlmacen);
						dto.setDescAlmacen(descAlmacen);
						dto.setPiezas(piezas);
						dto.setExistencias(existencias);
						dto.setCosto(costo);
						dto.setTipo_prod(tipo_prod);
						dto.setAjuste(ajuste);
						rows.add(dto);
					}
					return rows;
				}
		
//				public List<InvInsumos> getDataListaProdLugares(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_prod_term,
//						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
//					List<InvInsumos> rows = new ArrayList<InvInsumos>();		
//					
//					String filtro = "";
//					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
//					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
//							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
//						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
//					}
//					
//					String filtroEspacios = "";
//					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
//						if(idEspacios.equals("0")){
//							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
//						}else{
//							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
//						}
//					}else{
//						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
//					}
//					
//					System.out.println("Lista Prod Lugares --1  "+id_prod_term);
//					String filProdTerm = "";
//					if(!id_prod_term.isEmpty() && id_prod_term != null){
//						filProdTerm = " AND id_prod_n0 = '"+id_prod_term+"'";
//					}
//					System.out.println(" Filtro ProdTerm "+filProdTerm);
//					DecimalFormat formatter = new DecimalFormat("###,##0.000");
//					String sql = " SELECT" +
//							" mov.dim_tiempo_fecha fecha," +
//							" prod.id_dim_prod id_prod_term,"+
//							" prod.id_prod_n0 itemcode_prod_term," +
//							" prod.desc_prod_n0 desc_prod_term," +
//							" mov.id_dim_prod id_prod_int," +
//							" mov.itemcode itemcode," +
//							" prod.desc_prod_n0m desc_prod_int," +
//							" mov.idalmacen id_almacen," +
//							" mov.nomalmacen desc_almacen," +
//							" prod.pzas piezas," +
//							" SUM(mov.pzasE) existencias," +
//							" mov.costo costo," +
//							" prod.idp tipo_prod"+
//							" FROM" +
//							" mov2 mov," +
//							" prod_marco prod" +
//							" WHERE" +
//							" mov.id_dim_prod = prod.id_dim_prodm" +
//							" AND mov.itemcode = prod.id_prod_n0m" +
//							" AND mov.pzasE != 0 " +
//							" AND mov.tipo=2" +
//							//" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)"+
//							filtroEspacios+
//							/*" AND mov.dim_tiempo_fecha" +
//							" BETWEEN '2013-02-15' AND '2013-07-03'" +*/
//							filtro +
//							filProdTerm+
//							" GROUP by" +
//							" mov.idalmacen ," +
//							" prod.id_prod_n0m" +
//							" ORDER BY" +
//							" prod.id_prod_n0," +
//							" mov.idalmacen";
//					System.out.println("ListaProdlugares: -->"+sql);
//					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
//					while( rs.next() ){
//						
//						InvInsumos dto = new InvInsumos();
//						String idProductoTerm = rs.getString("id_prod_term");
//						String itemProductoTerm = rs.getString("itemcode_prod_term");
//						String descProductoTerm = rs.getString("desc_prod_term");
//						String idProducto = rs.getString("id_prod_int");
//						String itemcodeProd = rs.getString("itemcode");
//						String descProducto = rs.getString("desc_prod_int");
//						String piezas = rs.getString("piezas");
//						String idAlmacen = rs.getString("id_almacen");
//						String descAlmacen = rs.getString("desc_almacen");
//						String tipo_prod = rs.getString("tipo_prod");
//						//String inventFinal = null;//rs.getString("invent_final");
//						String existencias = rs.getString("existencias");
//						String costo = rs.getString("costo");
//						if(existencias == null){
//							existencias = "0";
//						}
//						if(piezas == null){
//							piezas = "0";
//						}
//						if(costo == null){
//							costo = "0";
//						}
//						dto.setIdProductoTerm(idProductoTerm);
//						dto.setItemProductoTerm(itemProductoTerm);
//						dto.setDescProductoTerm(descProductoTerm);
//						dto.setIdProducto(idProducto);
//						dto.setDescProducto(descProducto);
//						dto.setItemcodeProd(itemcodeProd);
//						dto.setIdAlmacen(idAlmacen);
//						dto.setDescAlmacen(descAlmacen);
//						dto.setPiezas(piezas);
//						dto.setExistencias(existencias);
//						dto.setCosto(costo);
//						dto.setTipo_prod(tipo_prod);
//						rows.add(dto);
//					}
//					return rows;
//				}
				
				// Componentes o insumos que tienen existencias pero no dentro del rango de fechas
				public List<InvInsumos> getDataListaProdLugaresExp(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_prod_term,
						String fechaIni, String fechaFin, String idEspacios, String idEspaciosNotIn) throws ParseException{
					List<InvInsumos> rx = new ArrayList<InvInsumos>();		
					
					String filtro = "";
					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
						filtro = " AND mov.dim_tiempo_fecha NOT BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
					}
					
					String filtroEspacios = "";
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					}
					
					String filtrosEspaciosNotIn = "";
					if(!idEspaciosNotIn.isEmpty() && idEspaciosNotIn != null){
						filtrosEspaciosNotIn = " AND mov.idalmacen Not IN("+idEspaciosNotIn+")";
					}
					System.out.println("Lista Prod Lugares --1  "+id_prod_term);
					String filProdTerm = "";
					if(!id_prod_term.isEmpty() && id_prod_term != null){
						filProdTerm = " AND id_prod_n0 = '"+id_prod_term+"'";
					}
					System.out.println(" Filtro ProdTerm "+filProdTerm);
					DecimalFormat formatter = new DecimalFormat("###,##0.000");
					String sql = " SELECT" +
							" mov.dim_tiempo_fecha fecha," +
							" prod.id_dim_prod id_prod_term,"+
							" prod.id_prod_n0 itemcode_prod_term," +
							" prod.desc_prod_n0 desc_prod_term," +
							" mov.id_dim_prod id_prod_int," +
							" mov.itemcode itemcode," +
							" prod.desc_prod_n0m desc_prod_int," +
							" mov.idalmacen id_almacen," +
							" mov.nomalmacen desc_almacen," +
							" prod.pzas piezas," +
							" SUM(mov.pzasE) existencias," +
							" mov.costo costo," +
							" prod.idp tipo_prod"+
							" FROM" +
							" mov2 mov," +
							" prod_marco prod" +
							" WHERE" +
							" mov.id_dim_prod = prod.id_dim_prodm" +
							" AND mov.itemcode = prod.id_prod_n0m" +
							" AND mov.pzasE != 0 " +
							" AND mov.tipo=2" +
							//" AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)"+
							filtroEspacios+
							filtrosEspaciosNotIn+
							/*" AND mov.dim_tiempo_fecha" +
							" BETWEEN '2013-02-15' AND '2013-07-03'" +*/
							filtro +
							filProdTerm+
							" GROUP by" +
							" mov.idalmacen ," +
							" prod.id_prod_n0m" +
							" ORDER BY" +
							" prod.id_prod_n0," +
							" mov.idalmacen";
					System.out.println("ListaProdlugaresExtras : <-->"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dtx = new InvInsumos();
						String idProductoTerm = rs.getString("id_prod_term");
						String itemProductoTerm = rs.getString("itemcode_prod_term");
						String descProductoTerm = rs.getString("desc_prod_term");
						String idProducto = rs.getString("id_prod_int");
						String itemcodeProd = rs.getString("itemcode");
						String descProducto = rs.getString("desc_prod_int");
						String piezas = rs.getString("piezas");
						String idAlmacen = rs.getString("id_almacen");
						String descAlmacen = rs.getString("desc_almacen");
						String tipo_prod = rs.getString("tipo_prod");
						//String inventFinal = null;//rs.getString("invent_final");
						String existencias = rs.getString("existencias");
						String costo = rs.getString("costo");
						if(existencias == null){
							existencias = "0";
						}
						if(piezas == null){
							piezas = "0";
						}
						if(costo == null){
							costo = "0";
						}
						dtx.setIdProductoTerm(idProductoTerm);
						dtx.setItemProductoTerm(itemProductoTerm);
						dtx.setDescProductoTerm(descProductoTerm);
						dtx.setIdProducto(idProducto);
						dtx.setDescProducto(descProducto);
						dtx.setItemcodeProd(itemcodeProd);
						dtx.setIdAlmacen(idAlmacen);
						dtx.setDescAlmacen(descAlmacen);
						dtx.setPiezas(piezas);
						dtx.setExistencias(existencias);
						dtx.setCosto(costo);
						dtx.setTipo_prod(tipo_prod);
						rx.add(dtx);
					}
					return rx;
				}
				//Obtiene datos de marcas para insumos
//				public HashMap getDataListaProdLugaresMin(String id_customer, String id_user, String id_modulo, String id_dashboard, String chart_id, String id_prod_term, 
//						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
//					HashMap<String, String> hm = new HashMap<String, String>();
//					
//					String filtro = "";
//					System.out.println("Filtros Lista Lugares --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
//					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
//							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
//						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
//					}
//					String filtroEspacios = "";
//					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
//						if(idEspacios.equals("0")){
//							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
//						}else{
//							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
//						}
//					}else{
//						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
//					}
//					System.out.println("Lista "+id_prod_term);
//					String filProdTerm = "";
//					if(!id_prod_term.isEmpty() && id_prod_term != null){
//						filProdTerm = " AND id_prod_n0 = '"+id_prod_term+"'";
//					}
//					System.out.println(" Filtro ProdTerm "+filProdTerm);
//					String sql = "SELECT" +
//							" prb.id_prod_term id_prod_term," +
//							" MIN(prb.num_prod) num_productos" +
//							" FROM" +
//							" (" +
//							" SELECT" +
//							" prod.id_prod_n0 id_prod_term," +
//							" (((SUM(mov.pzasE)-SUM(mov.pzasS)) + SUM(mov.pzasE) - SUM(mov.pzasS)) / prod.pzas) num_prod" +
//							" FROM" +
//							" mov2 mov," +
//							" prod_marco prod" +
//							" WHERE" +
//							" mov.id_dim_prod = prod.id_dim_prodm" +
//							" AND mov.itemcode = prod.id_prod_n0m" +
//							filtroEspacios+
//							/*" AND mov.dim_tiempo_fecha" +
//							" BETWEEN '2013-02-15' AND '2013-07-03'" +*/
//							filtro +
//							filProdTerm+
//							" AND prod.idp = 1" +
//							" GROUP BY mov.itemcode" +
//							" ORDER BY prod.id_prod_n0) prb" +
//							" GROUP by prb.id_prod_term";
//					System.out.println("Insumos:-- "+sql);
//					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
//					while( rs.next() ){
//						
//						InvInsumos dto = new InvInsumos();
//						String idProductoTerm = rs.getString("id_prod_term");
//						String numProductos = rs.getString("num_productos");
//						hm.put(idProductoTerm, numProductos);
//					}
//								
//					return hm;
//				}
//				
				public HashMap getDataListaProdLugaresMin(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String id_prod_term,
						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
					HashMap<String, String> hm = new HashMap<String, String>();		
					
					String filtro = "";
					String join = "";
					String cmp_ajuste = "";
					String inv_ini = " 0 ";
					System.out.println("Filtros --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					
					
					String filtroEspacios = "";
					String filtroEspaciosJoin = "";
					String filtroEspaciosJoinM = "";
					
					System.out.println("Filtros Array: "+filtroEspacios);
			        
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
							filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
							filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
							filtroEspaciosJoin = " AND idalmacen IN("+idEspacios+") ";
							filtroEspaciosJoinM = " AND m.idalmacen IN("+idEspacios+") ";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
						filtroEspaciosJoin = " AND idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
						filtroEspaciosJoinM = " AND m.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21) ";
					}
					
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
					System.out.println("Genera SQL-JOIN -<<<<<<<<<>>><");
						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
						inv_ini = " T1.invent_ini ";
						cmp_ajuste = " T3.ajuster ajuste,"; 
						join = " LEFT JOIN" +
								" (SELECT ifnull(m.id_dim_prod,0) id_dim_prod,m.itemcode,m.idalmacen," +
								" ((SUM(m.pzasE)-SUM(m.pzasS))-ifnull(T2.ajuste,0)) AS invent_ini" +
								" FROM mov2 m" +
								" LEFT JOIN (SELECT a.id_ins_comp,a.itemcodeic,a.idalmacen," +
								" SUM(a.cantidad) ajuste FROM mov_ajustes a WHERE a.fecha < '"+fechaIni+"'" +
								" GROUP by a.id_ins_comp,a.itemcodeic) AS T2" +
								" ON T2.id_ins_comp=m.id_dim_prod AND T2.itemcodeic=m.itemcode AND T2.idalmacen=m.idalmacen" +
								" WHERE m.tipo=1 AND m.dim_tiempo_fecha < '"+fechaIni +"' "+ filtroEspaciosJoinM +
								" GROUP by m.id_dim_prod,m.itemcode,m.idalmacen) AS T1" +
								" ON mov.id_dim_prod = T1.id_dim_prod AND mov.itemcode=T1.itemcode AND " +
								" T1.idalmacen=mov.idalmacen " + filtroEspacios +
								" LEFT JOIN (SELECT aj.id_ins_comp,aj.idalmacen,SUM(aj.cantidad) ajuster" +
								" FROM mov_ajustes aj WHERE aj.fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'" +
								" GROUP by aj.idalmacen,aj.id_ins_comp)T3" +
								" ON T3.id_ins_comp=mov.id_dim_prod AND T3.idalmacen=mov.idalmacen,";
					}else{
						inv_ini = " 0 ";
						cmp_ajuste = " T3.ajuster ajuste,";
						join = " LEFT JOIN " +
								" (SELECT aj.id_ins_comp,aj.idalmacen,SUM(aj.cantidad) ajuster" +
								" FROM mov_ajustes aj GROUP by aj.idalmacen,aj.id_ins_comp)T3" +
								" ON T3.id_ins_comp=mov.id_dim_prod AND T3.idalmacen=mov.idalmacen,";
					}
					String filProdTerm = "";
					if(!id_prod_term.isEmpty() && id_prod_term != null){
						filProdTerm = " AND id_prod_n0 = '"+id_prod_term+"'";
					}
					//System.out.println("Filtros Final..."+filtrosFinal);*/
					DecimalFormat formatter = new DecimalFormat("###,##0.000");
					
					String sql = "SELECT " +
							" prb.id_prod_term," +
							" prb.id_almacen," +
							" MIN(prb.num_prod) num_prod" +
							" FROM" +
							" (SELECT" +
							" prod.id_prod_n0 id_prod_term," +
							" mov.idalmacen id_almacen,"+
							" prod.pzas piezas, "+
							" mov.idalmacen," + 
							" SUM(mov.pzasE) entradas," +
							" SUM(mov.pzasS) salidas," +
							cmp_ajuste + 
							inv_ini + " invent_ini," +
							//" ((("+inv_ini+") + SUM(mov.pzasE) - SUM(mov.pzasS)) / prod.pzas) num_prod," +
							" mov.costo costo," +
							" ((("+inv_ini+") + SUM(mov.pzasE) - SUM(mov.pzasS)) / (ROUND(prod.pzas, 2))) num_prod" +
							" FROM" +
							" mov2 mov" +
							join +
							" prod_marco prod" +
							" WHERE" +
							" mov.id_dim_prod = prod.id_dim_prodm" +
							" AND mov.itemcode = prod.id_prod_n0m" +
							// " AND prod.idp = 1" +
							" AND mov.tipo = 1" +
							filtroEspacios+
							filtro +
							filProdTerm +
							" GROUP BY" +
							" prod.id_dim_prod," +
							" prod.id_prod_n0," +
							" prod.id_dim_prodm," +
							" prod.id_prod_n0m," +
							" mov.itemcode," +
							" mov.idalmacen"+
							" ORDER BY mov.idalmacen) prb" +
							" GROUP BY " +
							" prb.id_prod_term," +
							" prb.id_almacen";
					System.out.println("ListaProdlugares: --><--"+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dto = new InvInsumos();
						String idAlmacen = rs.getString("id_almacen");
						String numProductos = rs.getString("num_prod");
						if(numProductos == null || numProductos.equals("null")){
							numProductos = "0";
						}
						System.out.println(" IdAlmacen: "+idAlmacen+ " NumProd: "+numProductos);
						hm.put(idAlmacen, numProductos);
					}
					return hm;
				}
				//Otiene Entrada por Lugares 
				public HashMap getDataListaEntradasLugaresMin(String id_customer, String id_user, String id_modulo, String id_dashboard, String chart_id, String id_prod_term, 
						String fechaIni, String fechaFin, String idEspacios) throws ParseException{
					HashMap<String, String> hm = new HashMap<String, String>();
					
					String filtro = "";
					System.out.println("Filtros Lista Lugares --> "+ fechaIni + " "+ " "+ fechaFin + " " +idEspacios);
					if(fechaIni != null && !fechaIni.equals("null") && !fechaIni.isEmpty()
							&& fechaFin != null && !fechaFin.equals("null") && !fechaFin.isEmpty()){
						filtro = " AND mov.dim_tiempo_fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
					}
					String filtroEspacios = "";
					if(idEspacios != null && !idEspacios.equals("null") && !idEspacios.isEmpty()){
						if(idEspacios.equals("0")){
							filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
						}else{
							filtroEspacios = " AND mov.idalmacen IN("+idEspacios+")";
						}
					}else{
						filtroEspacios = " AND mov.idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)";
					}
					System.out.println("Lista "+id_prod_term);
					String filProdTerm = "";
					if(!id_prod_term.isEmpty() && id_prod_term != null){
						filProdTerm = " AND id_prod_n0 = '"+id_prod_term+"'";
					}
					System.out.println(" Filtro ProdTerm "+filProdTerm);
					String sql = "SELECT" +
							" prb.id_prod_term id_prod_term," +
							" MIN(prb.num_prod) num_productos" +
							" FROM" +
							" (" +
							" SELECT" +
							" prod.id_prod_n0 id_prod_term," +
							" (((SUM(mov.pzasE)-SUM(mov.pzasS)) + SUM(mov.pzasE) - SUM(mov.pzasS)) / prod.pzas) num_prod" +
							" FROM" +
							" mov2 mov," +
							" prod_marco prod" +
							" WHERE" +
							" mov.id_dim_prod = prod.id_dim_prodm" +
							" AND mov.itemcode = prod.id_prod_n0m" +
							filtroEspacios+
							/*" AND mov.dim_tiempo_fecha" +
							" BETWEEN '2013-02-15' AND '2013-07-03'" +*/
							filtro +
							filProdTerm+
							" AND prod.idp = 1" +
							" GROUP BY mov.itemcode" +
							" ORDER BY prod.id_prod_n0) prb" +
							" GROUP by prb.id_prod_term";
					System.out.println("Insumos:-- "+sql);
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
					while( rs.next() ){
						
						InvInsumos dto = new InvInsumos();
						String idProductoTerm = rs.getString("id_prod_term");
						String numProductos = rs.getString("num_productos");
						hm.put(idProductoTerm, numProductos);
					}
								
					return hm;
				}
				
	public String getMenu(String id_customer, String id_modulo, String id_user, String id_dashboard, String id_menu, String filtroIn, String cmp_id, String id_chart){
		HashMap <String, String> hm = new HashMap<String, String>();
		System.out.println("Chart id getMenu "+id_chart);
		String sql = "";
		if(id_menu.equals("1")){
			if(!filtroIn.isEmpty()){
				sql = "SELECT id_sop_marca id, desc_prod_n6 nom FROM soporte_marca WHERE id_sop_marca NOT IN(1,2,35,36,"+filtroIn+") ORDER BY desc_prod_n6 ASC";
			}else{
				sql = "SELECT id_sop_marca id, desc_prod_n6 nom FROM soporte_marca WHERE id_sop_marca NOT IN(1,2,35,36) ORDER BY desc_prod_n6 ASC";  
			}
		}
		if(id_menu.equals("2")){
			String filtroMarca = datosMarca(id_customer, id_modulo, id_user, id_dashboard, id_chart, "id_marca");
			if(filtroMarca.isEmpty()){
				filtroMarca = " ORDER BY desc_prod_n7 ASC";
			}else{
				filtroMarca =" AND id_sop_mar IN("+filtroMarca+") ORDER BY desc_prod_n7 ASC";
			}
			
			if(!filtroIn.isEmpty()){
				sql = "SELECT id_dim_prod id, desc_prod_n7 nom FROM dim_productos WHERE id_dim_prod NOT IN("+filtroIn+") AND id_sop_mar NOT IN(1,2,35,36) "+filtroMarca;
			}else{
				sql = "SELECT id_dim_prod id, desc_prod_n7 nom FROM dim_productos WHERE id_sop_mar NOT IN(1,2,35,36) "+filtroMarca;  
			}
		}
		System.out.println("Sql-Ord"+sql); 
		return sql;
	}
	public HashMap getMenuFiltro(String id_user, String id_dashboard, String id_chart, String fact, String dim_sop, String cmp_desc, 
			String id, String idfk, String elemento, String orden, String cmpsFiltros){
		HashMap <String, String> hm = new HashMap<String, String>();
		//String anioI = tool.obtieneAnioInicial();
		//String anioF = tool.obtieneAnioFinal();
		String menu="";
		String sql = "";
		if(!cmpsFiltros.isEmpty()){
			if(id_chart.equals("1")){
				sql = "SELECT id_sop_marca id, desc_prod_n6 nom FROM soporte_marca WHERE id_sop_marca IN ("+cmpsFiltros+")";
			}
			if(id_chart.equals("2")){
				sql = "SELECT id_dim_prod id, desc_prod_n7 nom FROM dim_productos WHERE id_dim_prod IN ("+cmpsFiltros+")";
			}
			
			System.out.println("SelectFil: "+ sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while(rs.next()){
				hm.put(rs.getString("id"), rs.getString("nom"));
			}
		}
		//System.out.println("SQL-MENU: "+sql);
		
		return hm;
	}
	public String datosMarca(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_portlet, String cmp_id){
		String datosFiltrados = "";
		String sql = "SELECT value FROM ppto_filtros_portlets WHERE id_customer='"+id_customer+"' AND id_modulo='"+id_modulo+"' AND id_user='"+id_user+
				"' AND id_dashboard='"+id_dashboard+"' AND parametro='"+cmp_id+"'  AND id_portlet='"+id_portlet+"'";
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
	
	//Datos Espacios
	public HashMap getEspacios(){
		HashMap <String, String> hm = new HashMap<String, String>();
		String sql = "SELECT" +
				" idalmacen," +
				" nomalmacen" +
				" FROM" +
				" mov2" +
				" WHERE idalmacen IN(03,06,09,10,15,16,17,18,19,20,21)"+
				" GROUP by" +
				" idalmacen"+
				" ORDER BY nomalmacen";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			hm.put(rs.getString("idalmacen"), rs.getString("nomalmacen"));
		}
		
		return hm;
	}
	
	//Obtiene Fecha De Carga De Datos
	public String obtieneFechaCargaDatos(){
		String fecha = null;
		
		String sql = "SELECT MAX(log_date) fecha FROM logs_inv";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while(rs.next()){
			fecha = rs.getString("fecha");
		}
		
		return fecha;
	}
}
