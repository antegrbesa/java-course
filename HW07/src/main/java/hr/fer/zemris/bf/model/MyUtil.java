package hr.fer.zemris.bf.model;

/**
 * A custom utility helper class that offers a single method. 
 * @author Ante Grbesa
 *
 */
public class MyUtil {
	
	/**
	 * Checks if any of given arguments are null.
	 * @param objects arguments to check for
	 * @throws IllegalArgumentException if any of given arguments
	 * is null
	 */
	public static void checkIfNull(Object...objects) {
		for (Object o : objects) {
			if (o == null) {
				throw new IllegalArgumentException("Argument was null");
			}
		}
	}
	
}
