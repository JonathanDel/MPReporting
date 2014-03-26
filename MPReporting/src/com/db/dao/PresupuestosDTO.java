package com.db.dao;

public class PresupuestosDTO {

	private String departamento;
	private String core;
	private String importe_pres;
	
	public String getImporte_pres() {
		return importe_pres;
	}
	public void setImporte_pres(String importePres) {
		importe_pres = importePres;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
	
	
}
