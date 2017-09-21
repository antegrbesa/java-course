package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception derived from {@link RuntimeException}.
 * @author Ante
 *
 */
public class LexerException extends RuntimeException {

	/**
	 * Serial version UID. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new Lexer Exception with no specified message;
	 */
	public LexerException() {
		super();
	}

	/**
	 * Constructs a new Lexer Exception with no specified message;
	 * @param message message for exception
	 */
	public LexerException(String message) {
		super(message);
	}


}
