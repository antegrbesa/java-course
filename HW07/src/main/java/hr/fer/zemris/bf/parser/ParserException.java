package hr.fer.zemris.bf.parser;

/**
 * Exception that is thrown by {@link Parser} class. 
 * @author Ante
 *
 */
public class ParserException extends RuntimeException {

	/**
	 * Serial version id. 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates exception with no message. 
	 */
	public ParserException() {
		super();
	}
	
	/**
	 * Creates exception with specified message.
	 * @param message message for exception 
	 */
	public ParserException(String message) {
		super(message);
	}

}
