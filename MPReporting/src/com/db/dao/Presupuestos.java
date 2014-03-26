package com.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.auribox.reporting.tools.Tools;
import com.db.DataSource;

public class Presupuestos {
	
	JdbcTemplate jdbcTemplate;
	JdbcTemplate jdbcTemplateAdmin;
	
	public Presupuestos(){
		DataSource ds = new DataSource();
		jdbcTemplate = ds.getDataSource();
		jdbcTemplateAdmin = ds.getDataSourceAdmin();
	}
	
	public List<PresupuestosDTO> getDataAllPresupuestos(){
		List<PresupuestosDTO> rows = new ArrayList<PresupuestosDTO>();		
		String sql = "SELECT * FROM fact_presupuestos LIMIT 50";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while( rs.next() ){
			PresupuestosDTO dto = new PresupuestosDTO();
			dto.setDepartamento(rs.getString("departamento"));
			dto.setCore(rs.getString("core"));
			dto.setImporte_pres(rs.getString("importe_pres"));
			rows.add(dto);
		}
		return rows;
	}
	
	// Tools 
		@SuppressWarnings("unchecked")
		public HashMap getDataChartConfig(String chart_id, String cust_id, String modulo_id){
			
			HashMap hm = new HashMap();
			
			String sql="SELECT * FROM ppto_chartcfg WHERE chart_id = '"+chart_id+"' AND clientes_id = '"+cust_id+"' AND modulo_id='"+modulo_id+"'";
			//System.out.println("Config"+sql);
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			while( rs.next() ){
				
				hm.put("tbl_fact", rs.getString("chartcfg_tbl_fact"));
				hm.put("tbl_dim", rs.getString("chartcfg_tbl_dim"));
				hm.put("cmp_desc", rs.getString("chartcfg_cmp_desc"));
				hm.put("cmp_id", rs.getString("chartcfg_cmp_id"));
				hm.put("cmp_idfk", rs.getString("chartcfg_cmp_idfk"));
				//hm.put("cmp_id_dim", rs.getString("chartcfg_cmp_id_dim"));
				
				hm.put("medida1", rs.getString("chartcfg_medida1"));
				hm.put("medida2", rs.getString("chartcfg_medida2"));

				hm.put("medida3", rs.getString("chartcfg_medida3"));
				hm.put("medida4", rs.getString("chartcfg_medida4"));
				
				//----
				hm.put("medida5", rs.getString("chartcfg_medida5"));
				hm.put("medida6", rs.getString("chartcfg_medida6"));
				hm.put("medida7", rs.getString("chartcfg_medida7"));
				hm.put("medida8", rs.getString("chartcfg_medida8"));
				/*hm.put("medida9", rs.getString("chartcfg_medida9"));
				hm.put("medida10", rs.getString("chartcfg_medida10"));
				hm.put("medida11", rs.getString("chartcfg_medida11"));
				hm.put("medida12", rs.getString("chartcfg_medida12"));
				hm.put("medida13", rs.getString("chartcfg_medida13"));*/
				//--
				hm.put("elemento", rs.getString("chartcfg_elemento"));
				
				//Agregados
				hm.put("cmp_orden", rs.getString("chartcfg_cmp_orden"));
				hm.put("tipo_orden", rs.getString("chartcfg_tipo_orden"));
				////System.out.println(rs.getString("chartcfg_cmp_orden"));
				//-----
				hm.put("caption", rs.getString("chartcfg_caption"));
				hm.put("xAxisName", rs.getString("chartcfg_xAxisName"));
				hm.put("yAxisName", rs.getString("chartcfg_yAxisName"));
				hm.put("numberPrefix", rs.getString("chartcfg_numberPrefix"));
				hm.put("showValues", rs.getString("chartcfg_showValues"));

			}
			////System.out.println("config");
			return hm;
		}
		
		public String getSQLHeader(String chart_id, String cust_id, String paramI, String paramF, 
				String tipo_orden, String orden, String id_user, String id_dashboard, String id_modulo, boolean filtros){
			Tools tools = new Tools();
			boolean sts_fact = tools.getStatusFact(id_user, id_modulo, id_dashboard);
			boolean sts_po = tools.getStatusPptoOrg(id_user, id_modulo, id_dashboard);
			boolean sts_pm = tools.getStatusPptoMod(id_user, id_modulo, id_dashboard);
			boolean gpo = tools.getStatusGpo(id_user, id_modulo, id_dashboard);
			HashMap hm = getDataChartConfig(chart_id, cust_id, id_modulo);
			HashMap parametros = getDataParameters(id_user, id_dashboard, id_modulo);
			String tbl_fact=(String)hm.get("tbl_fact");
			String tbl_dim=(String)hm.get("tbl_dim");
			String cmp_desc=(String)hm.get("cmp_desc");
			String cmp_id=(String)hm.get("cmp_id");
			String cmp_idfk=(String)hm.get("cmp_idfk");
			//String medida=(String)hm.get(cmp_medida);
			String medida2=(String)hm.get("medida2");
			String elemento=(String)hm.get("elemento");
			String xAxisName = (String) hm.get("xAxisName");
			//String cmp_id_dim = (String) hm.get("cmp_id_dim");
			//String anio = (String)prmt.get("anio");
			//HashMap md = getDataMedida(id_user, id_modulo, id_dashboard);
			//String nomMed = (String)md.get("medida_1");
			//System.out.println("Medida: "+nomMed);
			String medida = "";
			////System.out.println("NomMed "+ nomMed);
			//if(nomMed == null){
				medida=(String)hm.get("medida3");
				////System.out.println("---"+medida);
			//}else{
				//medida = (String)hm.get(nomMed);
			//}
			String filtro = "";
			String fil_cmb = "";
			String filtrosFinal = "";
			String cmpsFiltros = "";
			/*Obtiene filtros p0or portlet*/
			
			if(chart_id.equals("5") || chart_id.equals("9")){
			int num = 0;
			String cmp_fil = "";
			
			//HashMap filtroMenu = obtieneFiltros(id_user, id_dashboard, chart_id);
			String slqlFiltros = "SELECT parametro FROM ppto_filtros_portlets WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"'AND id_modulo='"+id_modulo+"' AND id_portlet!= '1'";
			String filtroSql = "";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs.next()){
				cmp_fil = rs.getString("parametro");
				//System.out.println("cmp filtro "+cmp_fil);
				if(rs.isFirst()){
					if(!cmp_id.equals(cmp_fil)){
						
						filtrosFinal += "WHERE "+tbl_fact+"."+cmp_fil+" IN(";
					}
				}else{
					if(!cmp_id.equals(cmp_fil)){
						
						filtrosFinal += "AND "+tbl_fact+"."+cmp_fil+" IN(";
					}
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"' AND id_portlet!='1'";
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				while(rf.next()){
					if(!rf.isLast()){
						if(!cmp_id.equals(cmp_fil) ){
							filtrosFinal +="'"+ rf.getString("value")+"', ";
						}
					}else{
						if(!cmp_id.equals(cmp_fil)){
							filtrosFinal +="'"+ rf.getString("value")+"') ";
						}
					}
					
				}
			}
			cmpsFiltros = getCmpFiltros(id_user, id_modulo, id_dashboard, chart_id);
			}
			//System.out.println("cmpos filtros: "+cmpsFiltros);
			//System.out.println("filtros final: "+filtrosFinal);
	
			int cont = 0;
			Iterator it = parametros.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
				
				if(cont == 0){
					if(!cmp_id.equals(e.getKey())){
						filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					}
					
				}
				else{
					if(!cmp_id.equals(e.getKey())){
						if(filtro==""){
							filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}else{
							filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}
					}
				}
				cont++;
			}
			if(filtro == ""){
				filtro += tbl_fact+"."+elemento+" >= '"+paramI+"' " +
						"AND "+tbl_fact+"."+elemento+" <= '"+paramF+"' " ;
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
			filtro = "WHERE " + filtro + fil_cmb;
			System.out.println("Filtro H: "+filtro);
							
			String sql = "";/*Cambio INNER JOIN por LEFT JOIN*/
			sql = "SELECT " +
			tbl_dim+"."+cmp_desc+" dimension, " + //Cambiar descripcion por dimension
			tbl_dim+"."+cmp_idfk+" AS id, " +
			"ROUND(SUM("+tbl_fact+"."+medida+"),0) AS medida " +
			//"ROUND(SUM("+tbl_fact+"."+medida2+"),0) AS medida2 " +
			"FROM "+tbl_dim+
			" LEFT JOIN (SELECT "+cmp_id+"," +medida+
			" "+cmpsFiltros + //agregado
			" FROM "+tbl_fact+ 
			" "+ filtro+" ) "+tbl_fact +
			" ON ( "+tbl_fact+"."+cmp_id+" = "+tbl_dim+"."+cmp_idfk +") "+
			filtrosFinal + //Agregado
			" GROUP BY " + tbl_dim+"."+cmp_desc +
			" ORDER BY "+orden+" "+tipo_orden;//tbl_dim+"."+orden
			System.out.println("SQL Header: "+xAxisName +" "+ sql);
			return sql;
		}
		
		//SQL que genera los datos para el popup general
		public String getSQLHeaderGnrl(String chart_id, String cust_id, String paramI, String paramF, 
				String tipo_orden, String orden, String id_user, String id_dashboard, String id_modulo, boolean filtros){
			
			HashMap hm = getDataChartConfig(chart_id, cust_id, id_modulo);
			Tools tools = new Tools();
			boolean sts_fact = tools.getStatusFact(id_user, id_modulo, id_dashboard);
			boolean sts_po = tools.getStatusPptoOrg(id_user, id_modulo, id_dashboard);
			boolean sts_pm = tools.getStatusPptoMod(id_user, id_modulo, id_dashboard);
			boolean gpo = tools.getStatusGpo(id_user, id_modulo, id_dashboard);
			HashMap parametros = getDataParametersGnrl(id_user, id_dashboard, id_modulo);
			String tbl_fact=(String)hm.get("tbl_fact");
			String tbl_dim=(String)hm.get("tbl_dim");
			String cmp_desc=(String)hm.get("cmp_desc");
			String cmp_id=(String)hm.get("cmp_id");
			String cmp_idfk=(String)hm.get("cmp_idfk");
			//String medida=(String)hm.get(cmp_medida);
			String medida2=(String)hm.get("medida2");
			String elemento=(String)hm.get("elemento");
			String xAxisName = (String) hm.get("xAxisName");
			//String cmp_id_dim = (String) hm.get("cmp_id_dim");
			//String anio = (String)prmt.get("anio");
			//HashMap md = getDataMedida(id_user, id_modulo, id_dashboard);
			//String nomMed = (String)md.get("medida_1");
			//System.out.println("Medida: "+nomMed);
			String medida = "";
			////System.out.println("NomMed "+ nomMed);
			//if(nomMed == null){
				medida=(String)hm.get("medida3");
				////System.out.println("---"+medida);
			//}else{
				//medida = (String)hm.get(nomMed);
			//}
			String filtro = "";
			String fil_cmb = "";
			String filtrosFinal = "";
			String cmpsFiltros = "";
			/*Obtiene filtros p0or portlet*/
			
			if(chart_id.equals("5") || chart_id.equals("9")){
			int num = 0;
			String cmp_fil = ""; 
			
			//HashMap filtroMenu = obtieneFiltros(id_user, id_dashboard, chart_id);
			String slqlFiltros = "SELECT parametro FROM ppto_filtros_portlets_gnrl WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"'AND id_modulo='"+id_modulo+"' AND id_portlet!= '1'";
			String filtroSql = "";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs.next()){
				cmp_fil = rs.getString("parametro");
				System.out.println("cmp filtro "+cmp_fil);
				if(rs.isFirst()){
					if(!cmp_id.equals(cmp_fil)){
						
						filtrosFinal += "WHERE "+tbl_fact+"."+cmp_fil+" IN(";
					}
					if(cmp_id.equals("id_sop_ctaclave")){
						
						filtrosFinal += "WHERE "+tbl_fact+"."+cmp_fil+" IN(";
					}
				}else{
					if(!cmp_id.equals(cmp_fil)){
						
						filtrosFinal += "AND "+tbl_fact+"."+cmp_fil+" IN(";
					}
					if(cmp_id.equals("id_sop_ctaclave")){
						
						filtrosFinal += "AND "+tbl_fact+"."+cmp_fil+" IN(";
					}
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets_gnrl WHERE parametro='"+cmp_fil+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"' AND id_portlet!='1'";
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				while(rf.next()){
					if(!rf.isLast()){
						if(!cmp_id.equals(cmp_fil) ){
							filtrosFinal +="'"+ rf.getString("value")+"', ";
						}
						if(cmp_id.equals("id_sop_ctaclave") ){
							filtrosFinal +="'"+ rf.getString("value")+"', ";
						}
					}else{
						if(!cmp_id.equals(cmp_fil)){
							filtrosFinal +="'"+ rf.getString("value")+"') ";
						}
						if(cmp_id.equals("id_sop_ctaclave")){
							filtrosFinal +="'"+ rf.getString("value")+"') ";
						}
					}
					
				}
			}
			cmpsFiltros = getCmpFiltros(id_user, id_modulo, id_dashboard, chart_id);
			}
			//System.out.println("cmpos filtros: "+cmpsFiltros);
			//System.out.println("filtros final: "+filtrosFinal);
	
			int cont = 0;
			Iterator it = parametros.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
				
				if(cont == 0){
					if(!cmp_id.equals(e.getKey())){
						filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					}
					
				}
				else{
					if(!cmp_id.equals(e.getKey())){
						if(filtro==""){
							filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}else{
							filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}
					}
				}
				cont++;
			}
			if(filtro == ""){
				filtro += tbl_fact+"."+elemento+" >= '"+paramI+"' " +
						"AND "+tbl_fact+"."+elemento+" <= '"+paramF+"' " ;
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
			filtro = "WHERE " + filtro + fil_cmb;
			System.out.println("Filtro H: "+filtro);
			
			//filtro = "WHERE " + filtro;
			System.out.println("Filtro: "+filtro);
							
			String sql = "";/*Cambio INNER JOIN por LEFT JOIN*/
			sql = "SELECT " +
			tbl_dim+"."+cmp_desc+" dimension, " + //Cambiar descripcion por dimension
			tbl_dim+"."+cmp_idfk+" AS id, " +
			"ROUND(SUM("+tbl_fact+"."+medida+"),0) AS medida " +
			//"ROUND(SUM("+tbl_fact+"."+medida2+"),0) AS medida2 " +
			"FROM "+tbl_dim+
			" LEFT JOIN (SELECT "+cmp_id+"," +medida+
			" "+cmpsFiltros + //agregado
			" FROM "+tbl_fact+ 
			" "+ filtro+" ) "+tbl_fact +
			" ON ( "+tbl_fact+"."+cmp_id+" = "+tbl_dim+"."+cmp_idfk +") "+
			filtrosFinal + //Agregado
			" GROUP BY " + tbl_dim+"."+cmp_desc +
			" ORDER BY "+orden+" "+tipo_orden;//tbl_dim+"."+orden
			System.out.println("SQL Header: "+xAxisName +" "+ sql);
			return sql;
		}
		public HashMap getDataParameters(String id_user, String id_dashboard, String id_modulo) {
			HashMap<String, String> hm = new HashMap<String, String>();
			String sqlParam = "SELECT * FROM ppto_parametros WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"'";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sqlParam);
			System.out.println("getDataParameters: "+sqlParam);
			while( rs.next() ){
				System.out.println(rs.getString("parametro")+"--"+ rs.getString("value"));
				hm.put(rs.getString("parametro"), rs.getString("value"));
			}
			return hm;
		}
		public HashMap getDataParametersGnrl(String id_user, String id_dashboard, String id_modulo) {
			HashMap<String, String> hm = new HashMap<String, String>();
			String sqlParam = "SELECT * FROM ppto_parametros_gnrl WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"'";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sqlParam);
			System.out.println("getDataParameters: "+sqlParam);
			while( rs.next() ){
				//System.out.println(rs.getString("parametro")+"--"+ rs.getString("value"));
				hm.put(rs.getString("parametro"), rs.getString("value"));
			}
			return hm;
		}
		public HashMap getDataMedida(String id_user, String id_modulo,String id_dashboard ) {
			////System.out.println("Medida");
			//System.out.println("User "+ id_user+" Dashboard "+ id_dashboard);
			HashMap<String, String> hm = new HashMap<String, String>();
			String sqlParamMed = "SELECT * FROM medidas WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo ='"+id_modulo+"'";
			////System.out.println(sqlParamMed);
			SqlRowSet rsMed = jdbcTemplateAdmin.queryForRowSet(sqlParamMed);
			while( rsMed.next() ){
				////System.out.println(rsMed.getString("medida") +","+ rsMed.getString("medidaValor"));
				hm.put(rsMed.getString("medida"), rsMed.getString("medidaValor"));
			}
			
			return hm;
		}
		public String getSQLBody(String chart_id, String cust_id, String paramI, String paramF, String id_user, String id_dashboard, String gpoMed, 
				String id_modulo, boolean filtros){
			Tools tools = new Tools();
			boolean sts_fact = tools.getStatusFact(id_user, id_modulo, id_dashboard);
			boolean sts_po = tools.getStatusPptoOrg(id_user, id_modulo, id_dashboard);
			boolean sts_pm = tools.getStatusPptoMod(id_user, id_modulo, id_dashboard);
			boolean gpo = tools.getStatusGpo(id_user, id_modulo, id_dashboard);
			//boolean cmb = tools.getStatusCmb(id_user, id_modulo, id_dashboard);
			HashMap hm = getDataChartConfig(chart_id, cust_id, id_modulo);
			HashMap parametros = getDataParameters(id_user, id_dashboard, id_modulo);
			String tbl_fact=(String)hm.get("tbl_fact");
			String cmp_id=(String)hm.get("cmp_id");
			String cmp_idfk=(String)hm.get("cmp_idfk");
			//String medida=(String)hm.get(cmp_medida);
			String elemento=(String)hm.get("elemento");
			String tipo_orden = (String) hm.get("tipo_orden");
			String tbl_dim = (String) hm.get("tbl_dim");
			String xAxisName = (String) hm.get("xAxisName");
			//String cmp_id_dim = (String) hm.get("cmp_id_dim");
			//HashMap md = getDataMedida(id_user, id_dashboard);
			//String nomMed = (String)md.get("medida_1");
			//String nomMed_2 = (String) md.get("medida_2");
			String medida = "";
			String medida_2 = "";
			String medida_3 = "";
			String cmp__medida_2 = "";
			String cmp_medida_3 = "";
			String cmp_med = "";
			String cmp_med_3 = "";
			String medidas = " ";
			String gpoCmpMed = "";
			int contGpo = 1;
			int contOrd = 0;
			String cmpOrd = "";
			String cmp_mu = "";
			////System.out.println("NomMed "+ nomMed);
			if(gpoMed == "" || gpoMed == null){
				HashMap grupo_med = getGrupoMed(id_user, id_modulo, id_dashboard, "2");
				Iterator iter = grupo_med.entrySet().iterator();
				Map.Entry e;
				
				while (iter.hasNext()) {
					e = (Map.Entry)iter.next();
					cmp_mu = (String) e.getValue();
					medidas += (String) hm.get(cmp_mu)+",";
					if(chart_id.equals("1") || chart_id.equals("2") || chart_id.equals("3")){
						if(!(cmp_mu.equals("medida7")) && !(cmp_mu.equals("medida8"))){
							gpoCmpMed +=", ROUND(SUM("+tbl_fact+"."+(String) hm.get(cmp_mu)+"),0) AS "+cmp_mu;
							if(contOrd == 0){
								cmpOrd = (String) e.getValue();
							}
						}
					}else{
						gpoCmpMed +=", ROUND(SUM("+tbl_fact+"."+(String) hm.get(cmp_mu)+"),0) AS "+cmp_mu;
						if(contOrd == 0){
							cmpOrd = (String) e.getValue();
						}
					}
					if(chart_id.equals("1") || chart_id.equals("2") || chart_id.equals("3")){
						if(!(cmp_mu.equals("medida7")) && !(cmp_mu.equals("medida8"))){
							contOrd ++;
							contGpo ++;
						}
					}else{
						contOrd ++;
						contGpo ++;
					}
					
				}
			}else{
				HashMap grupo_med = getGrupoMed(id_user, id_modulo, id_dashboard, gpoMed);
				Iterator iter = grupo_med.entrySet().iterator();
				Map.Entry e;
				String campo = "";
				String med = "";
				while (iter.hasNext()) {
					e = (Map.Entry)iter.next();
					campo = (String)e.getValue();
					
					medidas += (String) hm.get(campo)+",";
					med = (String) hm.get((String)campo);
					gpoCmpMed +=", ROUND(SUM("+tbl_fact+"."+med+"),0) AS "+campo;
					if(contOrd == 0){
						cmpOrd = campo;
					}
					contOrd ++;
					contGpo ++;
				}
			}
				medida_2 = "importe_pre";//(String)hm.get(nomMed_2);
				cmp__medida_2 = ", ROUND(SUM("+tbl_fact+"."+medida_2+"),0) AS medida_2 ";
				cmp_med =","+medida_2;
				
				medida_3 = "importe_prem";//(String)hm.get(nomMed_2);
				cmp_medida_3 = ", ROUND(SUM("+tbl_fact+"."+medida_3+"),0) AS medida_3 ";
				cmp_med_3 =","+medida_3;

			String filtro = "";
			String fil_cmb = "";
			String cmpsFiltros = "";
			String filtrosFinal = "";
			if(chart_id.equals("5") || chart_id.equals("9") && filtros){
			int num = 0;
			String cmp_fil = "";
			
			//HashMap filtroMenu = obtieneFiltros(id_user, id_dashboard, chart_id);
			String slqlFiltros = "SELECT parametro FROM ppto_filtros_portlets WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"'AND id_modulo='"+id_modulo+"' AND id_portlet!= '1'";
			String filtroSql = "";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs.next()){
				cmp_fil = rs.getString("parametro");
				if(rs.isFirst()){
					
					if(!cmp_fil.equals(cmp_idfk) && !cmp_fil.equals(cmp_id)){
						filtrosFinal += "WHERE "+tbl_fact+"."+cmp_fil+" IN(";
					}
				}else{
					if(!cmp_idfk.equals(cmp_fil) && !cmp_id.equals(cmp_fil)){
						filtrosFinal += "AND "+tbl_fact+"."+cmp_fil+" IN(";
					}
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets WHERE parametro='"+cmp_fil+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"' AND id_portlet!='1'";
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				while(rf.next()){
					if(!rf.isLast()){
						if(!cmp_id.equals(cmp_fil)){
							filtrosFinal +="'"+ rf.getString("value")+"', ";
						}
					}else{
						if(!cmp_id.equals(cmp_fil)){
							filtrosFinal +="'"+ rf.getString("value")+"') ";
						}
					}
					
				}
			}
			cmpsFiltros = getCmpFiltros(id_user, id_modulo, id_dashboard, chart_id);
			}
			int cont = 0;
			Iterator it = parametros.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
				if(cont == 0){
					if(!cmp_id.equals(e.getKey())){
						filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
					}
				}
				else{
					if( !cmp_id.equals(e.getKey())){
						if(filtro==""){
							filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}else{
							filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}
					}
					
					
				}
				cont++;
			}
			if(filtro == ""){
				filtro += tbl_fact+"."+elemento+" >= '"+paramI+"' ";
			}
			
			// Obtner rango de meses 
			String rango_meses = obtieneRangoMes(cust_id, id_user, id_modulo, id_dashboard, chart_id);
			if(filtro != null && filtro != "" && rango_meses !=  null && rango_meses != ""){
				filtro += "AND "+rango_meses;
			}
			String cmp_mes = "";
			//Comprueba combinacion para dejar pasar valores
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
			filtro = "WHERE " + filtro + fil_cmb;
			
			//System.out.println("Filtro B: "+filtro);
			String sql = "";
			sql = "SELECT " +
			tbl_dim+"."+cmp_idfk+" AS id, " +
			tbl_fact+".anio elemento " +
			gpoCmpMed+
			" FROM " +tbl_dim+"" +
			" LEFT JOIN "+ 
			" (SELECT "+cmp_id+","+medidas+elemento+ cmpsFiltros +//",mu "+// cmp_med + cmp_med_3+ //Agregados cmpsFiltros y cmp_med (medida_2)
			" FROM "+ tbl_fact+ 
			" "+filtro+") "+tbl_fact+
			" ON "+tbl_fact+"."+cmp_id+"= "+tbl_dim+"."+cmp_idfk +
			" " + filtrosFinal + //Agregado
			" GROUP BY " +
			tbl_fact+"."+elemento+"," +
			" id " +
			" ORDER BY "+cmpOrd+" ASC";
			 System.out.println("SQL Body: "+xAxisName +" "+sql);
			return sql;
		}
		
		//SQL Body obtinee datos para el popup general
		public String getSQLBodyGnrl(String chart_id, String cust_id, String paramI, String paramF, String id_user, String id_dashboard, String gpoMed, 
				String id_modulo, boolean filtros){
			
			Tools tools = new Tools();
			boolean sts_fact = tools.getStatusFact(id_user, id_modulo, id_dashboard);
			boolean sts_po = tools.getStatusPptoOrg(id_user, id_modulo, id_dashboard);
			boolean sts_pm = tools.getStatusPptoMod(id_user, id_modulo, id_dashboard);
			boolean gpo = tools.getStatusGpo(id_user, id_modulo, id_dashboard);
			String fil_cmb = "";
			HashMap hm = getDataChartConfig(chart_id, cust_id, id_modulo);
			HashMap parametros = getDataParametersGnrl(id_user, id_dashboard, id_modulo);
			String tbl_fact=(String)hm.get("tbl_fact");
			String cmp_id=(String)hm.get("cmp_id");
			String cmp_idfk=(String)hm.get("cmp_idfk");
			//String medida=(String)hm.get(cmp_medida);
			String elemento=(String)hm.get("elemento");
			String tipo_orden = (String) hm.get("tipo_orden");
			String tbl_dim = (String) hm.get("tbl_dim");
			String xAxisName = (String) hm.get("xAxisName");
			//String cmp_id_dim = (String) hm.get("cmp_id_dim");
			//HashMap md = getDataMedida(id_user, id_dashboard);
			//String nomMed = (String)md.get("medida_1");
			//String nomMed_2 = (String) md.get("medida_2");
			String medida = "";
			String medida_2 = "";
			String medida_3 = "";
			String cmp__medida_2 = "";
			String cmp_medida_3 = "";
			String cmp_med = "";
			String cmp_med_3 = "";
			String medidas = " ";
			String gpoCmpMed = "";
			int contGpo = 1;
			int contOrd = 0;
			String cmpOrd = "";
			String cmp_mu = "";
			////System.out.println("NomMed "+ nomMed);
			if(gpoMed == "" || gpoMed == null){
				HashMap grupo_med = getGrupoMed(id_user, id_modulo, id_dashboard, "2");
				Iterator iter = grupo_med.entrySet().iterator();
				Map.Entry e;
				
				while (iter.hasNext()) {
					e = (Map.Entry)iter.next();
					cmp_mu = (String) e.getValue();
					medidas += (String) hm.get(cmp_mu)+",";
					if(chart_id.equals("1") || chart_id.equals("2") || chart_id.equals("3")){
						if(!(cmp_mu.equals("medida7")) && !(cmp_mu.equals("medida8"))){
							gpoCmpMed +=", ROUND(SUM("+tbl_fact+"."+(String) hm.get(cmp_mu)+"),0) AS "+cmp_mu;
							if(contOrd == 0){
								cmpOrd = (String) e.getValue();
							}
						}
					}else{
						gpoCmpMed +=", ROUND(SUM("+tbl_fact+"."+(String) hm.get(cmp_mu)+"),0) AS "+cmp_mu;
						if(contOrd == 0){
							cmpOrd = (String) e.getValue();
						}
					}
					if(chart_id.equals("1") || chart_id.equals("2") || chart_id.equals("3")){
						if(!(cmp_mu.equals("medida7")) && !(cmp_mu.equals("medida8"))){
							contOrd ++;
							contGpo ++;
						}
					}else{
						contOrd ++;
						contGpo ++;
					}
					
				}
			}else{
				HashMap grupo_med = getGrupoMed(id_user, id_modulo, id_dashboard, gpoMed);
				Iterator iter = grupo_med.entrySet().iterator();
				Map.Entry e;
				String campo = "";
				String med = "";
				while (iter.hasNext()) {
					e = (Map.Entry)iter.next();
					campo = (String)e.getValue();
					
					medidas += (String) hm.get(campo)+",";
					med = (String) hm.get((String)campo);
					gpoCmpMed +=", ROUND(SUM("+tbl_fact+"."+med+"),0) AS "+campo;
					if(contOrd == 0){
						cmpOrd = campo;
					}
					contOrd ++;
					contGpo ++;
				}
			}
				medida_2 = "importe_pre";//(String)hm.get(nomMed_2);
				cmp__medida_2 = ", ROUND(SUM("+tbl_fact+"."+medida_2+"),0) AS medida_2 ";
				cmp_med =","+medida_2;
				
				medida_3 = "importe_prem";//(String)hm.get(nomMed_2);
				cmp_medida_3 = ", ROUND(SUM("+tbl_fact+"."+medida_3+"),0) AS medida_3 ";
				cmp_med_3 =","+medida_3;

			String filtro = "";
			String cmpsFiltros = "";
			String filtrosFinal = "";
			if(chart_id.equals("5") || chart_id.equals("9") && filtros){
			int num = 0;
			String cmp_fil = "";
			
			//HashMap filtroMenu = obtieneFiltros(id_user, id_dashboard, chart_id);
			String slqlFiltros = "SELECT parametro FROM ppto_filtros_portlets_gnrl WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"'AND id_modulo='"+id_modulo+"' AND id_portlet!= '1'";
			String filtroSql = "";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(slqlFiltros);
			while(rs.next()){
				cmp_fil = rs.getString("parametro");
				if(rs.isFirst()){
					
					if(!cmp_fil.equals(cmp_idfk) && !cmp_fil.equals(cmp_id)){
						filtrosFinal += "WHERE "+tbl_fact+"."+cmp_fil+" IN(";
					}
					if(cmp_fil.equals("id_sop_ctaclave") && cmp_fil.equals("id_sop_ctaclave")){
						filtrosFinal += "WHERE "+tbl_fact+".id_sop_ctaclave IN(";
					}
				}else{
					if(!cmp_idfk.equals(cmp_fil) && !cmp_id.equals(cmp_fil)){
						filtrosFinal += "AND "+tbl_fact+"."+cmp_fil+" IN(";
					}
					if(cmp_idfk.equals("id_sop_ctaclave") && cmp_id.equals("id_sop_ctaclave")){
						filtrosFinal += "AND "+tbl_fact+".id_sop_ctaclave IN(";
					}
				}
				
				filtroSql = "SELECT value FROM ppto_filtros_portlets_gnrl WHERE parametro='"+cmp_fil+"' AND id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_modulo='"+id_modulo+"' AND id_portlet!='1'";
				SqlRowSet rf = jdbcTemplateAdmin.queryForRowSet(filtroSql);
				while(rf.next()){
					if(!rf.isLast()){
						if(!cmp_id.equals(cmp_fil)){
							filtrosFinal +="'"+ rf.getString("value")+"', ";
						}
						if(cmp_id.equals("id_sop_ctaclave")){
							filtrosFinal +="'"+ rf.getString("value")+"', ";
						}
					}else{
						if(!cmp_id.equals(cmp_fil)){
							filtrosFinal +="'"+ rf.getString("value")+"') ";
						}
						if(cmp_id.equals("id_sop_ctaclave")){
							filtrosFinal +="'"+ rf.getString("value")+"') ";
						}
					}
					
				}
			}
			cmpsFiltros = getCmpFiltros(id_user, id_modulo, id_dashboard, chart_id);
			}
			int cont = 0;
			Iterator it = parametros.entrySet().iterator();
			//System.out.println("Filtros antes paametros "+filtro);
			while(it.hasNext()){
				Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
				if(cont == 0){
					//System.out.println("-------------" + cmp_id+"------"+e.getValue());
					if(cmp_id.equals("id_sop_ctaclave")){
						filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						//System.out.println("IFIF "+filtro);
					}else{
						if(!cmp_id.equals(e.getKey())){
							filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
							//System.out.println("IFELSE "+filtro);
						}
					}
					//System.out.println("IFFF "+filtro);
				}
				else{
					if( !cmp_id.equals(e.getKey())){
						if(filtro==""){
							filtro += tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}else{
							filtro += "AND "+tbl_fact+"."+e.getKey()+"='"+e.getValue()+"' ";
						}
					}
					//System.out.println("ELSEEE "+filtro);
				}
				cont++;
			}
			if(filtro == ""){
				filtro += tbl_fact+"."+elemento+" >= '"+paramI+"' ";
			}
			
			// Obtner rango de meses 
			String rango_meses = obtieneRangoMes(cust_id, id_user, id_modulo, id_dashboard, chart_id);
			if(filtro != null && filtro != "" && rango_meses !=  null && rango_meses != ""){
				filtro += "AND "+rango_meses;
			}
			String cmp_mes = "";
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
			filtro = "WHERE " + filtro + fil_cmb;
			
			//filtro = "WHERE " + filtro;
			String sql = "";
			sql = "SELECT " +
			tbl_dim+"."+cmp_idfk+" AS id, " +
			tbl_fact+".anio elemento " +
			gpoCmpMed+
			" FROM " +tbl_dim+"" +
			" LEFT JOIN "+ 
			" (SELECT "+cmp_id+","+medidas+elemento+ cmpsFiltros +//",mu "+// cmp_med + cmp_med_3+ //Agregados cmpsFiltros y cmp_med (medida_2)
			" FROM "+ tbl_fact+ 
			" "+filtro+") "+tbl_fact+
			" ON "+tbl_fact+"."+cmp_id+"= "+tbl_dim+"."+cmp_idfk +
			" " + filtrosFinal + //Agregado
			" GROUP BY " +
			tbl_fact+"."+elemento+"," +
			" id " +
			" ORDER BY "+cmpOrd+" ASC";
			 System.out.println("SQL Body: "+xAxisName +" "+sql);
			return sql;
		}
		
		public String getDimName(String chart_id, String cust_id, String id, String id_modulo){

			HashMap hm = getDataChartConfig(chart_id, cust_id, id_modulo);

			String tbl_dim=(String)hm.get("tbl_dim");
			String cmp_desc=(String)hm.get("cmp_desc");
			String cmp_id=(String)hm.get("cmp_idfk");

			String name="";
			String nv_name = "";
			String sql = "SELECT "+cmp_desc+" FROM "+tbl_dim+" WHERE "+cmp_id+" = '"+id+"'";
			////System.out.println();
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				name = rs.getString(cmp_desc);
				nv_name = name.replace("&", "%26");
			}
			return nv_name;
		}
		public String getDimNameTable(String chart_id, String cust_id, String id, String id_modulo){

			HashMap hm = getDataChartConfig(chart_id, cust_id, id_modulo);

			String tbl_dim=(String)hm.get("tbl_dim");
			String cmp_desc=(String)hm.get("cmp_desc");
			String cmp_id=(String)hm.get("cmp_idfk");

			String name="";
			String nv_name = "";
			String sql = "SELECT "+cmp_desc+" FROM "+tbl_dim+" WHERE "+cmp_id+" = '"+id+"'";
			////System.out.println();
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while( rs.next() ){
				nv_name = rs.getString(cmp_desc);
				//nv_name = name.replace("&", "%26");
			}
			return nv_name;
		}
		
		@SuppressWarnings("unchecked")
		public HashMap getDataHead(String chart_id, String cust_id, String paramI, String paramF, String tipo_orden, 
				String orden, String id_user, String id_dashboard, String id_modulo, boolean filtros){
			HashMap<Integer, String> hm = new HashMap<Integer, String>();
			String sqlHead = "";
			SqlRowSet rs = null;
			System.out.println("Año ------------------------------> "+paramI + "   "+paramF);
			if(filtros){
				sqlHead = getSQLHeader(chart_id, cust_id, paramI, paramF, tipo_orden, orden, id_user, id_dashboard, id_modulo, filtros);				
			}else{
				sqlHead = getSQLHeaderGnrl(chart_id, cust_id, paramI, paramF, tipo_orden, orden, id_user, id_dashboard, id_modulo, filtros);				
			}
			rs = jdbcTemplate.queryForRowSet(sqlHead);
			int counter=0;
			//System.out.println("Header: ");
			while( rs.next() ){
				//System.out.println("ID: "+rs.getString("id"));
				hm.put(counter, rs.getString("id"));
				counter++;
			}
			return hm;
		}
		
		@SuppressWarnings("unchecked")
		public HashMap getDataBody(String chart_id, String cust_id, String paramI, String paramF, String id_user, String id_dashboard, String gpoMed,
				String id_modulo, boolean filtros){
			HashMap<String, String> hm = new HashMap<String, String>();
			String sqlBody = "";
			if(filtros){
				sqlBody = getSQLBody(chart_id, cust_id, paramI, paramF, id_user, id_dashboard, gpoMed, id_modulo, filtros);
			}else{
				sqlBody = getSQLBodyGnrl(chart_id, cust_id, paramI, paramF, id_user, id_dashboard, gpoMed, id_modulo, filtros);
			}
			
			
			HashMap medidas = getGrupoMed(id_user, id_modulo, id_dashboard, gpoMed);
			Iterator it = medidas.entrySet().iterator();
			Map.Entry e;
			String valor = "";
			while (it.hasNext()) {
				e = (Map.Entry)it.next();
				valor = (String) e.getValue();
				//System.out.println("Valoe: "+ valor);
				if(chart_id.equals("1") || chart_id.equals("2") || chart_id.equals("3")){
					if(!valor.equals("medida7") && !valor.equals("medida8")){
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlBody);
						while( rs.next() ){
							//System.out.println(valor+"_"+rs.getString("elemento")+"_"+rs.getString("id")+","+ rs.getString(valor));
							hm.put(valor+"_"+rs.getString("elemento")+"_"+rs.getString("id"), rs.getString(valor));
						}
					}
				}else{
					SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlBody);
					while( rs.next() ){
						//System.out.println(valor+"_"+rs.getString("elemento")+"_"+rs.getString("id")+","+ rs.getString(valor));
						hm.put(valor+"_"+rs.getString("elemento")+"_"+rs.getString("id"), rs.getString(valor));
					}
				}
			}
			return hm;
		}
/*
		public String obtieneAnioInicial(){
			HashMap hm = getDataChartConfig("1", "1");

			String tbl = (String)hm.get("tbl_fact");
			String elemento = (String)hm.get("elemento");
			String anioInicial = "";
			String sql = "SELECT MIN("+elemento+") anioInicial FROM "+tbl;
			////System.out.println("Ini "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while(rs.next()){
				anioInicial = rs.getString("anioInicial");
			}
			////System.out.println("INicail " + anioInicial);
			return anioInicial;
		}
		
		public String obtieneAnioFinal(){
			HashMap hm = getDataChartConfig("1", "1");

			String tbl = (String)hm.get("tbl_fact");
			String elemento = (String)hm.get("elemento");
			String anioFinal = "";
			String sql = "SELECT MAX("+elemento+") anioFinal FROM "+tbl;
			////System.out.println("Max "+sql);
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			while(rs.next()){
				anioFinal = rs.getString("anioFinal");
			}
			////System.out.println("Final "+anioFinal);
			return anioFinal;
		}
		*/
		public HashMap obtieneFiltros(String id_user, String id_dashboard, String id_portlet){
			HashMap filtros = new HashMap();
			int cont = 0;
			String sql = "SELECT nom_cmp_id cmp_id, id_filtro id FROM filtros_portlets " +
					"WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"'";
			//System.out.println("Sqlp-Fil-Menu "+sql);
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			while(rs.next()){
				////System.out.println(rs.getString("cmp_id")+"_"+cont+"--"+ rs.getString("id"));
				filtros.put(rs.getString("cmp_id")+"_"+cont, rs.getString("id"));
				cont ++;
			}
			
			return filtros;
		}
		public boolean verificaMedida_2(String id_user, String id_dashboard){
			boolean med_2 = false;
			String medida;
			String sql = "SELECT medida FROM medidas WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"'";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			while(rs.next()){
				medida =  rs.getString("medida");
				if(medida.equals("medida_2")){
					med_2 = true;
				}
			}
			return med_2;
		}
		public String getCmpFiltros(String id_user, String id_modulo, String id_dashboard, String id_portlet){
			String cmpsFiltros = "";
			String sql = "SELECT parametro FROM ppto_filtros_portlets " +
					"WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' AND id_portlet  " +
							"NOT IN('"+id_portlet+"') AND id_portlet!='1' AND id_modulo='"+id_modulo+"'";
			System.out.println("SqlCmpFil "+sql);
			SqlRowSet rs =  jdbcTemplateAdmin.queryForRowSet(sql);
			while (rs.next()){
				cmpsFiltros += ", "+rs.getString("parametro");
			}
			////System.out.println("Campos_Filtros: "+cmpsFiltros);
			return cmpsFiltros;
		}
		public String getCmpFiltrosGlob(String id_user, String id_dashboard, String id_modulo){
			String cmpsFiltros = "";
			String sql = "SELECT nom_cmp_id FROM filtros_portlets " +
					"WHERE id_user='"+id_user+"' AND id_dashboard='"+id_dashboard+"' " +
					"GROUP by nom_cmp_id";
			SqlRowSet rs =  jdbcTemplateAdmin.queryForRowSet(sql);
			while (rs.next()){
				cmpsFiltros += ", "+rs.getString("nom_cmp_id");
			}
			////System.out.println("Campos_Filtros: "+cmpsFiltros);
			return cmpsFiltros;
		}
		public HashMap getNomMed(String id_user, String id_dashboard, String gpo_med){
			HashMap nomMed = new HashMap();
			if(gpo_med == "" || gpo_med == null){
				gpo_med = "cantidad";
			}
			//String sql = "SELECT nom_medida FROM nom_medidas WHERE num_medida = '"+num_medida+"'";
			String sql = "SELECT val_cmp, desc_cmb FROM ppto_cmps_med, ppto_medidas "+
					" WHERE ppto_cmps_med.id_gpo=ppto_medidas.id_gpo AND ppto_cmps_med.id_cmb = ppto_medidas.id_cmb";
//"SELECT num_med,nom_medida FROM ppto_nom_medidas where grupo_medida='"+gpo_med+"'";
			//System.out.println("Nom_Medidas: "+sql);
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			int cont_med = 1;
			while(rs.next()){
				nomMed.put(rs.getString("val_cmp"), rs.getString("desc_cmb"));
				cont_med ++;
				//System.out.println("num med "+ cont_med);
			}
			return nomMed;
		}
		public HashMap getNomMedTable(String id_user, String id_dashboard, String gpo_med){
			HashMap nomMed = new HashMap();
			if(gpo_med == "" || gpo_med == null){
				gpo_med = "cantidad";
			}
			//String sql = "SELECT nom_medida FROM nom_medidas WHERE num_medida = '"+num_medida+"'";
			String sql = "SELECT val_cmp, desc_cmb_table FROM ppto_cmps_med, ppto_medidas "+
					" WHERE ppto_cmps_med.id_gpo=ppto_medidas.id_gpo AND ppto_cmps_med.id_cmb = ppto_medidas.id_cmb"; 
					
					//"SELECT num_med,nom_medida_table FROM ppto_nom_medidas where grupo_medida='"+gpo_med+"'";
			//System.out.println("Nom_Medidas: "+sql);
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			int cont_med = 1;
			while(rs.next()){
				nomMed.put(rs.getString("val_cmp"), rs.getString("desc_cmb_table"));
				cont_med ++;
				//System.out.println("num med "+ cont_med);
			}
			return nomMed;
		}
		public HashMap getGrupoMed(String id_user, String id_modulo, String id_dashboard, String nomMed){
			HashMap  medidas = new HashMap();
			String sql = "SELECT val_cmp FROM ppto_cmps_med, ppto_medidas "+
					" WHERE ppto_cmps_med.id_gpo=ppto_medidas.id_gpo AND ppto_cmps_med.id_cmb = ppto_medidas.id_cmb AND ppto_medidas.id_user='"+id_user+"' " +
							"AND id_modulo='"+id_modulo+"' AND id_dashboard='"+id_dashboard+"' order by ppto_medidas.id_cmb asc "; 
					//"SELECT val_cmp FROM ppto_cmps_med WHERE id_gpo='"+nomMed+"'";
			System.out.println("sql_Gpo_med: "+sql);
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			int cont = 0;
			while(rs.next()){
				medidas.put(cont, rs.getString("val_cmp"));
				cont ++;
			}
			return medidas;
		}
		public HashMap getMedMU(String nomMed){
			HashMap  medidas = new HashMap();
			String sql = "SELECT num_med FROM ppto_nom_medidas WHERE grupo_medida='mu'";
			//System.out.println("sql_Gpo_med: "+sql);
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			int cont = 0;
			while(rs.next()){
				//System.out.println("Num Med: "+rs.getString("num_med"));
				medidas.put(cont, rs.getString("num_med"));
				cont ++;
			}
			return medidas;
		}
		
		public String obtieneRangoMes(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_chart){
			
			String rango_mes = "";
			HashMap hm = getDataChartConfig(id_chart, id_customer, id_modulo);
			String tbl_fact=(String)hm.get("tbl_fact");
			String sql = "SELECT parametro, value FROM ppto_filtros_portlets WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"'"+
					" AND id_modulo='"+id_modulo+"' AND id_portlet='1' ORDER BY value ASC ";
			System.out.println("Sql Rango MEs___-------___---------_____________: "+" "+id_chart+" "+sql);
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			while(rs.next()){
				if(rs.isFirst()){
					rango_mes += tbl_fact+"."+rs.getString("parametro")+" >= "+rs.getString("value");
				}
				if(rs.isLast()){
					rango_mes += " AND "+tbl_fact+"."+rs.getString("parametro")+" <= "+rs.getString("value");
				}
			}
			//System.out.println("Rango Messsssssss-------"+ id_chart+" "+rango_mes);
			return rango_mes;
		}
		
		public String obtieneCmpMes(String id_customer, String id_user, String id_modulo, String id_dashboard, String id_chart){
			String cmp_mes = "";
			HashMap hm = getDataChartConfig(id_chart, id_customer, id_modulo);
			String tbl_fact=(String)hm.get("tbl_fact");
			String sql = "SELECT parametro, value FROM pptos_filtros_portlets WHERE id_customer='"+id_customer+"' AND id_user='"+id_user+"'"+
					" id_modulo='"+id_modulo+"' AND id_dashboard='1'";
			SqlRowSet rs = jdbcTemplateAdmin.queryForRowSet(sql);
			if(rs.next()){
				cmp_mes += ","+rs.getString("parametro");				
			}
			return cmp_mes;
		}
}
