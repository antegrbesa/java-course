package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Inherits from {@link RuntimeException}.
 * @author Ante Grbesa
 *
 */
public class SmartScriptParserException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new SmartScriptParser exception with no message. 
	 */
	public SmartScriptParserException() {
	}
	
	/**
	 * Constructs a new SmartScriptParser exception with the specified detail message
	 * @param message message for exception
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}


}
