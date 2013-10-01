package br.com.luxu.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	@SuppressWarnings("rawtypes")
	private static final ThreadLocal threadlocal = new ThreadLocal();
	private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	
	@SuppressWarnings("unchecked")
	public static Session getSession(){
		Session session = (Session) threadlocal.get();		
		if (session == null){
			session = sessionFactory.openSession();
			threadlocal.set(session);
		}
		return session;
	}
	
	public void begin(){
		getSession().beginTransaction();
	}
	
	public void rollback(){
		getSession().getTransaction().rollback();
	}
	
	public void close(){
		getSession().close();
	}
	
	public static void shutdown(){
		getSessionFactory().close();
	}
	
	// passamos ele para o Filter
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
}
