package hr.fer.zemris.bf.lexer;

/**
 * Exception for class {@link Lexer}.
 * @author Ante
 *
 */
public class LexerException extends RuntimeException {

	/**
	 * Serial version. 
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
	 * @param text message for exception
	 */
	public LexerException(String text) {
		super(text);
	}
}
