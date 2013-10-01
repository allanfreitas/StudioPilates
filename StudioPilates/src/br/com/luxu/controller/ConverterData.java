package br.com.luxu.controller;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.joda.time.DateTime;

@FacesConverter(value="converterData")
public class ConverterData implements Converter {

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
	
	return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object data) {
	DateTime dateTime = (DateTime) data;
	
	if(dateTime.getMonthOfYear() == 1 ||
       	   dateTime.getMonthOfYear() == 2 ||
           dateTime.getMonthOfYear() == 3 ||
           dateTime.getMonthOfYear() == 4 ||
           dateTime.getMonthOfYear() == 5 ||
           dateTime.getMonthOfYear() == 6 ||
           dateTime.getMonthOfYear() == 7 ||
           dateTime.getMonthOfYear() == 8 ||
           dateTime.getMonthOfYear() == 9){
	   return dateTime.getDayOfMonth() + "/"+ "0" + dateTime.getMonthOfYear() + "/"+ dateTime.getYear();
	}
	return dateTime.getDayOfMonth() + "/"+ dateTime.getMonthOfYear() + "/"+ dateTime.getYear();
    }

}
