package br.com.luxu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class EmailValida implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent componente, Object objeto)
	    throws ValidatorException {
	// recebe o que foi digitado
	String emaildigitado = (String) objeto;
	// faz a validação do email usando regex
	if (emaildigitado.length() > 0){
        	Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        	Matcher m = p.matcher(emaildigitado);
        	boolean valida = m.matches();
        	if(!valida){
        	    FacesMessage mensagem = new FacesMessage();
        	    // em caso de ser inválido, o usuário recebe esta mensagem
        	    mensagem.setDetail("Email inválido!");
        	    mensagem.setSummary("E-mail inválido");
        	    mensagem.setSeverity(FacesMessage.SEVERITY_ERROR);
        	    throw new ValidatorException(mensagem);
        	}
	}
    }
}
