package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Exception that inherits from <code>RuntimeException</code>
 * @author Ante
 *
 */
public class SmartScriptLexerException extends RuntimeException {

	
	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an ScriptLexerException with no detail message.
	 * 
	 */
	public SmartScriptLexerException() {
	}

	/**
	 * Constructs an ScriptLexerException with specified message.
	 * @param message message to set
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}


}
