package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception that inherits from <code>RuntimeException</code>
 * @author Ante
 *
 */
public class ScriptLexerException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an ScriptLexerException with no detail message.
	 * 
	 */
	public ScriptLexerException() {
	}

	/**
	 * Constructs an ScriptLexerException with specified message.
	 * 
	 */
	public ScriptLexerException(String message) {
		super(message);
	}


}
