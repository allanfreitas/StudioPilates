package br.com.luxu.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import org.apache.commons.lang3.StringUtils;

import com.mysql.jdbc.Messages;

public class WebAppUtil {
    /*
     * Transforma mensagens recebidas da camada de serviço em mensagens para o
     * usuário.
     */
    private static PhaseEvent event;
    private final static FacesContext facesContext = event.getFacesContext();
    private static UIComponent component = null;

    static public void showValidationErrors(Exception exception, String parentId, 
	    String componentPrefixReplaceFrom, String componentPrefixReplaceTo) {
	ValidationException vd = null;
	Throwable ex = exception;
	
	while (ex != null) {
	    if (ex instanceof ValidationException) {
		vd = (ValidationException) ex;
		break;
	    }
	    ex = ex.getCause();
	}
	if (vd == null) {
	    if (exception instanceof RuntimeException)
		throw (RuntimeException) exception;
	    else
		throw new RuntimeException(exception);
	}

	for (ErrorInfo error : vd.getFieldErrors()) {

	    String fieldName = error.getFieldName();
	    if (fieldName == null)
		addWarningMessage(Messages.getString(error.getMessage(),error.getArgs()));
	    // se tiver que fazer replace de prefixo
	    else if (componentPrefixReplaceFrom != null && fieldName.startsWith(componentPrefixReplaceFrom))
		fieldName = componentPrefixReplaceTo + fieldName.substring(componentPrefixReplaceFrom.length());
	    if (StringUtils.isEmpty(parentId)) {
		fieldName = StringUtils.lowerCase(fieldName);
		component = facesContext.getViewRoot().findComponent(fieldName);
		if (component == null) // tenta prefixando com "form:" pois pode
				       // estar num subform
		    component = facesContext.getViewRoot().findComponent("form:" + fieldName);
	    } else {
		fieldName = parentId + ":" + StringUtils.lowerCase(fieldName);
		component = facesContext.getViewRoot().findComponent(fieldName);
	    }
	    if (component == null)// não encontrou o componente (just in case)
		addWarningMessage(Messages.getString(error.getMessage(),error.getArgs()));
	    else
		facesContext.addMessage(component.getClientId(facesContext),
			new FacesMessage(FacesMessage.SEVERITY_ERROR,Messages.getString(error.getMessage(),error.getArgs()), null));
	}
    }
    
    static public void addWarningMessage(final String message) 
    { 
        addMessage(javax.faces.application.FacesMessage.SEVERITY_WARN, message); 
    } 
    
    static public void addWarningMessageFromProperties(	final String messageKey) 
    { 
        addMessage(javax.faces.application.FacesMessage.SEVERITY_WARN, Messages.getString(messageKey)); 
    }
    
    static public void addMessage(final javax.faces.application.FacesMessage.Severity severity,	final String message) 
    { 
        addMessage(null, severity, message); 
    } 

    static public void addMessage(final String clientId,final javax.faces.application.FacesMessage.Severity severity,final String message) 
    { 
        final FacesContext facesContext = event.getFacesContext(); 
        facesContext.addMessage(clientId, new FacesMessage(severity, message, null)); 
    } 


}
