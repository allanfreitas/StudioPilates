package br.com.luxu.util;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

@SuppressWarnings("serial")
public class GuestPreferences implements Serializable {

        private String theme = "aristo"; //default

        public String getTheme() {
                Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                if(params.containsKey("theme")) {
                        theme = params.get("theme");
                }
                
                return theme;
        }

        public void setTheme(String theme) {
                this.theme = theme;
        }
}