package hr.fer.zemris.java.tecaj_13.dao.jpa;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * Class stores connections towards database in a {@link ThreadLocal} object and offers 
 * methods to create and close an instance of {@link EntityManager} class.
 * 
 */
public class JPAEMProvider {


	/**
	 * Stored connections
	 */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Returns an instance of {@link EntityManager} class for current thread. 
	 * @return instance of {@link EntityManager}
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if(ldata==null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Closes currenty used {@link EntityManager} and commits changes. 
	 * @throws DAOException if an exception occurred while closing entity manager
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if(ldata==null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
	/**
	 * Encapsulates instance of {@link EntityManager} class.
	 * @author Ante
	 *
	 */
	private static class LocalData {
		/**Instance to encapsulate*/
		EntityManager em;
	}
	
}