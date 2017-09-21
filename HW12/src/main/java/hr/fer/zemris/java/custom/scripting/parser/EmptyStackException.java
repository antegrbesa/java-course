/**
 * 
 */
package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown by stack. 
 * @author Ante Grbesa
 *
 */
public class EmptyStackException extends RuntimeException {

	/**
	 * Serial uid. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an EmptyStackException with no detail message.
	 * 
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 *Constructs an <code>EmptyStackException</code> with the
     * specified detail message.
     *
     * @param  message the detail message.
     */
	public EmptyStackException(String message) {
		super(message);
	}

}
