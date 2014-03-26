package com.db.bo;

import java.util.List;

import com.db.dao.Presupuestos;
import com.db.dao.PresupuestosDTO;

public class Prueba {
	
	public static void main(String[] args) {
		Presupuestos ppto = new Presupuestos();
		List<PresupuestosDTO> row = ppto.getDataAllPresupuestos();
		for(PresupuestosDTO dto: row){
			System.out.println(dto.getCore()+" "+dto.getDepartamento()+" "+dto.getImporte_pres());
		}
	}

}
