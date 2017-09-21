package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Interface towards subsystem for data persistency. 
 * @author Ante Grbeša
 *
 */
public interface DAO {

	/**
	 * Gets an entry with given id. If no such entry exists, returns null.
	 * @param id key of entry
	 * @return entry or null if entry doesn't exist
	 * @throws DAOException if an exception occurs
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Gets a user with given id. If no such user exists, returns null.
	 * @param id key of user
	 * @return user or null if entry doesn't exist
	 * @throws DAOException if an exception occurs
	 */
	public BlogUser getBlogUser(Long id) throws DAOException;
	
	/**
	 * Gets a user with given nickname. If no such user exists, throws {@link IllegalArgumentException}.
	 * @param nick nickname of user
	 * @return user or throws exception if no such user exists
	 * @throws DAOException if an exception occurred during retrieval of user
	 * @throws IllegalArgumentException if given user doesn't exist
	 */
	public BlogUser getBlogUserByNick(String nick) throws DAOException;
	
	/**
	 * Adds given user to database. 
	 * @param user user to add
	 * @throws DAOException if an exception occurs
	 */
	public void addBlogUser(BlogUser user) throws DAOException;
	
	/**
	 * Returns a list containing all authors that created at least one blog.  
	 * @return list containing all authors
	 * @throws DAOException if an exception occurs
	 */
	public List<BlogUser> getAuthors() throws DAOException;
	
	/**
	 * Checks if database contains given nickname. 
	 * @param nick nickname to check
	 * @return true if nickname exists, false otherwise
	 * @throws DAOException if an exception occurs
	 */
	public boolean containsNick(String nick) throws DAOException;
	
	/**
	 * Adds given entry to database. 
	 * @param e entry to add
	 * @throws DAOException if an exception occurs
	 */
	public void addBlogEntry(BlogEntry e) throws DAOException;
}