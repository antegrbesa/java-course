package hr.fer.zemris.java.hw04.db;

/**
 * Filter interface that filters {@link StudentRecord} records using 
 * {@link #accepts(StudentRecord)} method.
 * @author Ante Grbesa
 *
 */
public interface IFilter {

	/**
	 * Checks if specified {@link StudentRecord} is acceptable for filtering.
	 * @param record to filter
	 * @return true if it's acceptable, false otherwise
	 */
	public boolean accepts(StudentRecord record);
	
}
