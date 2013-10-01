package br.com.luxu.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Float.class, value = "dinheiroConverter")
public class DinheiroConverter implements Converter {
 
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value != null && !value.equals("")) {
            String str = value.trim();
 
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if (Character.isDigit(ch) || ch == '.') {
                    sb.append(ch);
                }
            }
 
            return new Float(sb.toString());
        }
        return null;
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        if (value != null) {
            String prefixoMoeda = "R$ ";
            StringBuilder s = new StringBuilder(value.toString());
            if (s.substring(s.indexOf(String.valueOf('.'))).length() == 2) {
                s.append("0");
            }
            
			//s.replace(s.indexOf(String.valueOf('.')),3, ",00");
 
            for (int i = s.indexOf(String.valueOf('.')) - 3; i > 0; i -= 3) {
                s.insert(i, ',');
            }
            return prefixoMoeda + " " + s.toString();
        }
        return "";
    }
}
