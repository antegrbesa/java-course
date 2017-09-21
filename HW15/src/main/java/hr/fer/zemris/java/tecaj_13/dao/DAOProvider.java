package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/** 
 * A singleton class that returns a database access object which is used for 
 * database manipulations. 
 * @author Ante 
 *
 */
public class DAOProvider {

	/**Single instance to return*/
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Retrieves data access object.
	 * 
	 * @return object which encapsulates access to data-persistency layer.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}