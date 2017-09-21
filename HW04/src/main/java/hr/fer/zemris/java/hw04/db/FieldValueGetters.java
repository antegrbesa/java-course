package hr.fer.zemris.java.hw04.db;

/**
 * Class offers three implementations of {@link IFieldValueGetter}. These are:
 * <ul>
 * <li> First name field value getter
 * <li> Last name field value getter
 * <li> JMBAG field value getter
 * </ul>
 * 
 * @author Ante Grbesa
 *
 */
public class FieldValueGetters {
	
	/**First name field value getter*/
	public static final IFieldValueGetter FIRST_NAME;
	
	/**Last name field value getter*/
	public static final IFieldValueGetter LAST_NAME;
	
	/**JMBAG field value getter*/
	public static final IFieldValueGetter JMBAG;
	
	static {
		FIRST_NAME = record -> record.getFirstName();
		LAST_NAME = record -> record.getLastName();	
		JMBAG = record -> record.getJmbag();	
	}
}
