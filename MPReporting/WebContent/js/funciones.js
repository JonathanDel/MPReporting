function cadenaTabla() {
	var tabla = document.getElementById("tblDatos");
	var trs = tabla.getElementsByTagName("tr");
	var tablaHTML = "<table border=1>";

	// var num = 0;
	var th = tabla.getElementsByTagName("thead")[0];
	var trHead = th.getElementsByTagName("tr");

	/*
	 * if (nombre == "analisisInventario") { tablaHTML += "<tr><td></td><td></td><td>Prom/Dia</td><td>Existencias</td><td>Dias
	 * Inventario</td><td>X Facturar Normal</td><td>Disponible</td><td>Costo</td><td>Dias
	 * Inventario</td><td>Tiempo Limite Para Mantener Stock</td><td>Tiempo
	 * Limite Para Levantar Pedido</td></tr>" + "<tr><td></td><td></td><td>CAJAS</td><td>CAJAS</td><td>CAJAS</td><td>CAJAS</td><td>CAJAS</td><td>CAJAS</td><td>Periodo</td><td>CAJAS</td><td>CAJAS</td><td>Periodo</td><td>Fecha</td></tr>";
	 * num++; } else if (nombre == "analisisVentas") { tablaHTML += "<tr><td></td><td></td><td>P3</td><td>P2</td><td>P1</td><td>Total</td><td>Prom/P</td><td>Existencias</td><td>P/Inventario</td><td>X
	 * Facturar</td><td>Disponible</td><td>P/Inventarios</td><td></td></tr>" + "<tr><td></td><td></td><td>CAJAS</td><td>CAJAS</td><td>DIAS</td><td>CAJAS</td><td>CAJAS</td><td>$</td><td>DIA</td><td>FECHA</td><td>DIAS</td><td>DIAS</td></tr>";
	 * num++ }
	 */

	for ( var n = 0; n < trHead.length; n++) {
		var hijosTr = trHead[n].childNodes;
		tablaHTML += "<tr>";
		for ( var m = 0; m < hijosTr.length; m++) {
			if (hijosTr[m] != undefined) {

				if (hijosTr[m].firstChild == undefined) {
					// console.log(hijosTr[m].textContent);
					var colspan = hijosTr[m].getAttribute("colspan");
					if (colspan > 1) {
						for ( var col = 1; col <= parseInt(colspan); col++) {
							if (col == parseInt(colspan)) {
								tablaHTML += "<td>" + hijosTr[m].textContent
										+ "</td>";
							} else {
								tablaHTML += "<td></td>";
							}
						}
					} else {
						tablaHTML += "<td>" + hijosTr[m].textContent + "</td>";
					}

				} else {
					// console.log(hijosTr[m].firstChild.textContent);
					var colspan = hijosTr[m].getAttribute("colspan");
					if (colspan > 1) {
						for ( var col = 1; col <= parseInt(colspan); col++) {
							if (col == parseInt(colspan)) {
								tablaHTML += "<td>"
										+ hijosTr[m].firstChild.textContent
										+ "</td>";
							} else {
								tablaHTML += "<td></td>";
							}
						}
					} else {
						tablaHTML += "<td>" + hijosTr[m].firstChild.textContent
								+ "</td>";
					}

				}

			}
		}
		tablaHTML += "</tr>";
	}

	for ( var i = trHead.length; i < trs.length; i++) {
		if (trs[i].getAttribute("style") != "display:none") {
			// console.log(trs[i]);
			tablaHTML += "<tr>";
			var tds = trs[i].getElementsByTagName("td");
			// console.log("valor de tds");
			for ( var j = 0; j < tds.length; j++) {
				if (tds[j] != undefined) {
					tablaHTML += "<td>";
					if (tds[j].childNodes.length>1 && tds[j].childNodes[2] != undefined) {
						tablaHTML += tds[j].childNodes[2].textContent;
						console.log(tds[j].childNodes[2]);
					} else if (tds[j].firstChild == undefined) {
						// console.log(tds[j].textContent);

						tablaHTML += tds[j].textContent;
					} else {
						// console.log(tds[j].firstChild.textContent);
						tablaHTML += tds[j].firstChild.textContent;
					}
					tablaHTML += "</td>";
				}
			}
			tablaHTML += "</tr>";
			// num++;

			// console.log(tablaHTML);

		}

	}
	tablaHTML += "</table>";

	// location.href = "excel.jsp?tabla=" + tablaHTML;
	document.getElementById("tabla").setAttribute("value", tablaHTML);

	console.log(tablaHTML);
	console.log(tablaHTML.length);
	return true;

}