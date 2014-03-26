package com.db;

import java.util.ResourceBundle;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSource {

	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getDataSource(){
		
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		String dataSourceFile = "datasource";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String driver = resourceBundle.getString("DRIVER");
		String url = resourceBundle.getString("URL");
		String usr = resourceBundle.getString("USER");
		String passwd = resourceBundle.getString("PASSWD");
		
		datasource.setDriverClassName(driver);
		datasource.setUrl(url);
		datasource.setUsername(usr);
		datasource.setPassword(passwd);
		
		try {
			jdbcTemplate = new JdbcTemplate(datasource);
			System.out.println("Conexion exitosa al datawarehouse");
		} catch (Exception e) {
			System.out.println("Error al conectarse:"+e.getMessage());
		}
		
		return jdbcTemplate;
	}
		
public JdbcTemplate getDataSourceAdmin(){
		
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		String dataSourceFile = "datasourceadmin";
		ResourceBundle resourceBundle = ResourceBundle.getBundle(dataSourceFile);
		
		String driver = resourceBundle.getString("DRIVER");
		String url = resourceBundle.getString("URL");
		String usr = resourceBundle.getString("USER");
		String passwd = resourceBundle.getString("PASSWD");
		
		datasource.setDriverClassName(driver);
		datasource.setUrl(url);
		datasource.setUsername(usr);
		datasource.setPassword(passwd);
		
		try {
			jdbcTemplate = new JdbcTemplate(datasource);
			System.out.println("Conexion exitosa al sistema de administración");
		} catch (Exception e) {
			System.out.println("Error al conectarse:"+e.getMessage());
		}

		return jdbcTemplate;
	}
}
