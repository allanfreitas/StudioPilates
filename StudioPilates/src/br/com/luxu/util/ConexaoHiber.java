/**
 * 
 */
package br.com.luxu.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Luxu
 *
 */
public class ConexaoHiber {
	private static final SessionFactory sessionFactory;
	private static final ThreadLocal<Session> threadLocal = 
		new ThreadLocal<Session>();
	/* observe o que temos de importante 
	 * nesse bloco static, o arquivo
	 * que possui os dados de conexao com 
	 * o banco de dados hibernateconfig.xml 
	 */
	
	static{
		try{
			sessionFactory = new  Configuration().configure("br/com/luxu/util/hibernate.cfg.xml")
			.buildSessionFactory();
		}catch (Throwable e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	//retorna uma sessao de comunicacao com o BD
	public static Session getInstance(){
		Session session = (Session) threadLocal.get();
		session = sessionFactory.openSession();
		threadLocal.set(session);
		return session;
	}
}
