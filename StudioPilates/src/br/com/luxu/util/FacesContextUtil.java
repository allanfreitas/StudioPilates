package br.com.luxu.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public class FacesContextUtil {
	
	private static final String HIBERNATE_SESSION = "hibernate_session";
	
	public static void setRequestSession(Session session){
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(HIBERNATE_SESSION, session);
	}
	
	public static Session getRequestSession(){
		return (Session) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(HIBERNATE_SESSION);
	}

	public static void setMensagemErro(String mensagem) {
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Atenção", mensagem);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}

	public static void setMensagemSucesso(String mensagem) {
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Concluído", mensagem);
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}

	public static Object getSessionAttribute(String nomeAtributo) {
		return ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getAttribute(nomeAtributo);
	}

	// redirecionador de páginas passadas pela string outcome
	public static void setNavegacao(String outcome) {
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(),null,outcome);
	}
}
