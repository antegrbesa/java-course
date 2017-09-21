package hr.fer.zemris.java.hw04.db;

/**
 * Interface that defines a strategy for comparing string elements. 
 * @author Ante Grbesa
 *
 */
public interface IComparisonOperator {
	
	/**
	 * Compares the two given string elements.
	 * @param value1 value to compare to
	 * @param value2 value to compare with
	 * @return true if second value satisfies comparison to the first element,
	 *  false otherwise
	 */
	public boolean satisfied(String value1, String value2);
	
}
