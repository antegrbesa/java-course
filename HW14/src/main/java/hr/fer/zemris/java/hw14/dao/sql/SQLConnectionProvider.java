package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;

/**
 * This class stores connections towards database in {@link ThreadLocal} object.
 * 
 */
public class SQLConnectionProvider {

	/**
	 * Stored connections
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Sets the connection for current thread (or remove entry from map
	 *  if argument is {@code null}).
	 * 
	 * @param con connection towards database
	 */
	public static void setConnection(Connection con) {
		if (con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Gets the connection which the current thread is allowed to use.
	 * 
	 * @return connection towards database.
	 */
	public static Connection getConnection() {
		return connections.get();
	}

}