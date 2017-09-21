package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.dao.sql.SQLDAO;

/** 
 * A singleton class that returns a database access object which is used for 
 * database manipulations. 
 * @author Ante 
 *
 */
public class DAOProvider {

	/**Single instance to return*/
	private static DAO dao = new SQLDAO();

	/**
	 * Retrieves data access object.
	 * 
	 * @return object which encapsulates access to data-persistency layer.
	 */
	public static DAO getDao() {
		return dao;
	}

}