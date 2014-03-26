package com.auribox.html;

import java.util.ArrayList;

public class LimpiaHTML {
	
	private String cadenaHTML;

    public LimpiaHTML(String cadenaHTML) {
        this.cadenaHTML = cadenaHTML;
        
    }
    public LimpiaHTML() {
        
        
    }
    public String getCadenaHTML() {
        return cadenaHTML;
    }

    public String limpiaTablaHTML() {
        ArrayList<String> tags = new ArrayList<String>();
        String cadenaSinFormato="";
        
        
        while (cadenaHTML.indexOf(">") != -1) {
            String tag = cadenaHTML.substring(0, cadenaHTML.indexOf(">") + 1);
            cadenaHTML = cadenaHTML.substring(tag.length(), cadenaHTML.length());
            tags.add(tag);
        }
        for (int j = 0; j < tags.size(); j++) {
            String tagAux = tags.get(j);
            //Verifica cada tag y le quita todos los atributos de la etiqueta html (solo estruvtura de un table)
            if (tagAux.startsWith("<table")) {
                tags.set(j, "<table>");
            } else if (tagAux.startsWith("<tr")) {
                tags.set(j, "<tr>");
            } else if (tagAux.startsWith("<th")) {
                tags.set(j, "<th>");
            } else if (tagAux.startsWith("<td")) {
                tags.set(j, "<td>");
            }else if(!tagAux.endsWith("</table>")||!tagAux.endsWith("</tr>")||!tagAux.endsWith("</th>")||!tagAux.endsWith("</td>")){            
                String cadenaSinHTML=tagAux.replaceAll("\\<[^>]*>", "");
                tags.set(j, cadenaSinHTML);
            }

        }
        for(String cadenaAux:tags){            
            cadenaSinFormato+=cadenaAux;
        }
        
        return cadenaSinFormato;
    }

    public String remueveAtributoDisplay(String cadena){    	
			ArrayList<String> tags = new ArrayList<String>();
			String cadenaSinFormato = "";
			
			while (cadena.indexOf(">") != -1) {
			String tag = cadena.substring(0, cadena.indexOf(">") + 1);
			cadena = cadena.substring(tag.length(), cadena.length());
			tags.add(tag);
			}
			for (int i = 0; i < tags.size(); i++) {
			String aux = tags.get(i);
			if (aux.startsWith("<table")) {
				tags.set(i, "<table>");
			} else if (aux.startsWith("<tr")) {
				tags.set(i, "<tr>");
			} else if (aux.startsWith("<th")) {
				tags.set(i, "<th>");
			}	
			
			if (aux.contains("style=\"display:none\"")) {
				tags.remove(i);
				if(tags.get(i).contains("</td>"));{
					tags.remove(i);
				}
			}
			
			//if(!aux.endsWith("</table>")||!aux.endsWith("</tr>")||!aux.endsWith("</th>")||!aux.endsWith("</td>")
			//		||!aux.startsWith("<table>")||!aux.startsWith("<tr>")||!aux.startsWith("<th>")||!aux.startsWith("<td>")){            
			//    String cadenaSinHTML=aux.replaceAll("\\<[^>]*>", "");
			//    tags.set(i, cadenaSinHTML);
			//}
			
			}
			
			for (String html : tags) {
			cadenaSinFormato += (html+"\n");
			}
			System.out.println(cadenaSinFormato);
			
			
			return cadenaSinFormato;
    }
}
