package hr.fer.zemris.java.hw04.parser;

/**
 * Exception thrown by {@link QueryParser}.
 * 
 * @author Ante Grbesa
 *
 */
public class QueryParserException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an instance of this class without any message.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Creates an instance of this class with specified message.
	 * @param message message to set
	 */
	public QueryParserException(String message) {
		super(message);
	}

}
