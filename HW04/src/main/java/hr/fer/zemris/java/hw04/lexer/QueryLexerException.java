package hr.fer.zemris.java.hw04.lexer;

/**
 * Exception for {@link QueryLexer} class.
 * @author Ante
 *
 */
public class QueryLexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an Query lexer exception with specified message.
	 * @param message message to set 
	 */
	public QueryLexerException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a query lexer exception with no message. 
	 */
	public QueryLexerException() {
		super();
	}

}
