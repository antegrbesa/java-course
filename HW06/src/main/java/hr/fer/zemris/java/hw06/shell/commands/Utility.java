package hr.fer.zemris.java.hw06.shell.commands;

/**
 * Helper class. 
 * @author Ante
 *
 */
public class Utility {
/**
 * Checks if given arguments are null. 
 * @param objects objects to check for 
 */
	public static void checkForNull(Object...objects) {
		for(Object o : objects) {
			if(o == null) {
				throw new IllegalArgumentException("Given argument was null");
			}
		}
	}
}
