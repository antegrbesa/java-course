package hr.fer.zemris.java.hw04.db;

/**
 * This interface (implementation of it) is responsible for obtaining a requested field 
 * value from given  {@link StudentRecord}.
 * @author Ante Grbesa
 *
 */
public interface IFieldValueGetter {
	/**
	 * Returns requested field value from specified record.
	 * @param record record from which the value is taken
	 * @return the requested value
	 */
	public String get(StudentRecord record);
	
}
