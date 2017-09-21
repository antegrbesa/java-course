package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Clas represents a provider class containing a {@link EntityManagerFactory} which {@link JPAEMProvider} class
 * will use for lazy creation of a connection. 
 * @author Ante
 *
 */
public class JPAEMFProvider {

	/**
	 * Entity manager Factory.
	 */
	public static EntityManagerFactory emf;
	
	/**
	 * Returns a single EntityManagerFactory instance
	 * @return single EntityManagerFactory
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Sets entity manager factory for this class.
	 * @param emf entity manager factory  to set
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}