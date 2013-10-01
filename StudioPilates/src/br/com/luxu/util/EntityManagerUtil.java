/**
 * 
 */
package br.com.luxu.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Luxu
 *
 */
public class EntityManagerUtil {
	private static EntityManagerFactory emf;
	
	public static EntityManager getEntityManager(){
		if (emf == null){
			emf = Persistence.createEntityManagerFactory("PersistenceStudio");
		}
		return emf.createEntityManager();
	}
}
