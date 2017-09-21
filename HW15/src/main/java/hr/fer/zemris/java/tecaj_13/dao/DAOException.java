package hr.fer.zemris.java.tecaj_13.dao;


/**
 * Exception thrown by {@link DAOException} and it's implementations. 
 * @author Ante Grbeša
 *
 */
public class DAOException extends RuntimeException {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an empty exception.
	 */
	public DAOException() {
	}

	/**
	 * Constructs an instance of this class with given arguments. 
	 * @param message message to set
	 * @param cause cause of exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an instance of this class with given arguments. 
	 * @param message message to set
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructs an instance of this class with given arguments. 
	 * @param cause cause of exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}