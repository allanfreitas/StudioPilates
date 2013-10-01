package br.com.luxu.controller;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.hibernate.Session;

import br.com.luxu.util.FacesContextUtil;
import br.com.luxu.util.HibernateUtil;

public class ListenerFasesJSF implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void beforePhase(PhaseEvent fase) {
//		System.out.println("Antes Fase: " + fase.getPhaseId());
	
		if (fase.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
			// recupera da sessão o MB loginbean
			LoginBean loginBean = (LoginBean) FacesContextUtil.getSessionAttribute("loginBean");
			if(loginBean == null || !loginBean.getAutenticado()){
				FacesContextUtil.setNavegacao("login");
			}
		}
		
		if (fase.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			FacesContextUtil.setRequestSession(session);
		}

	}

	@Override
	public void afterPhase(PhaseEvent fase) {
		
//		System.out.println("Depois Fase: " + fase.getPhaseId());
		
		if (fase.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
			Session session = FacesContextUtil.getRequestSession();
			try {
				session.getTransaction().commit();
				
			} catch (Exception e)
			{
				System.out.println("Erro no commit da transacao");
				System.out.println(e.getMessage());
				
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			} finally {
				session.close();
			}
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
