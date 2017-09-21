package hr.fer.zemris.java.hw04.db;

/**
 * Implementation of {@link IComparisonOperator}. Operators that are implemented are: 
 * <ul>
 * <li>LESS
 * <li>LESS OR EQUALS
 * <li>GREATER
 * <li>GREATER OR EQUALS
 * <li>EQUALS
 * <li>NOT EQUALS
 * <li>LIKE
 * </ul>
 * 
 * Like operator is a special operator which uses a wildcard (*) character. Wildcard represents zero or 
 * more characters that a string can contain. String can contain only one wildcard operator. Example: 
 * String<code> "AAGGAA"</code> is equal to <code>"AA*AA"</code>, but not equal to <code>"AAA*AA"</code>
 * 
 * @author Ante Grbesa
 *
 */
public class ComparisonOperators {

	/**Less operator*/
	public static final IComparisonOperator LESS;
	
	/**Less or equals operator*/
	public static final IComparisonOperator LESS_OR_EQUALS;
	
	/**Greater operator*/
	public static final IComparisonOperator GREATER;
	
	/**Greater or equal to operator*/
	public static final IComparisonOperator GREATER_OR_EQUALS;
	
	/**Equal to operator*/
	public static final IComparisonOperator EQUALS;
	
	/**Not equal to operator*/
	public static final IComparisonOperator NOT_EQUALS;
	
	/**Like operator*/
	public static final IComparisonOperator LIKE;
	
	/**Wildcard symbol*/
	private static final char WILDCARD = '*';
	
	static {
		LESS = (value1, value2) -> value1.compareTo(value2) < 0;
		
		LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
		
		GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
		
		GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
		
		EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;
		
		NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0;
		
		LIKE = new IComparisonOperator() {
			
			@Override
			public boolean satisfied(String value1, String value2) {
				char[] first = value1.toCharArray();
				char[] second = value2.toCharArray();
				
				int i = 0, j = 0;
				boolean wildcard = false;
				boolean returnValue = false;
				while(i < first.length && j < second.length) {
					if(first[i] == second[j]) {
						i++;
						j++;
						returnValue = true;
						continue;
					} else if(second[j] == WILDCARD) {
						if(! wildcard) {	//check if it's a first occurrence of wildcard
							wildcard = true;
						} else {
							throw new IllegalArgumentException
								("More than one wildcard present.");
						}
						j++;
						if(j >= second.length) {
							returnValue = true;	//wildcard is at last position
							break;
						}
						while(i < first.length) {
							returnValue = false;
							if(first[i] == second[j]) {
								returnValue = true;
								i++;
								j++;
								break;
							}
							i++;
						}
					}
					else {
						returnValue = false;	//difference is present in strings
						break;
					}
				}
				
				if(j < second.length && second[j] != WILDCARD) {
					returnValue = false;	//more members in second string than in first one
				}							//(excluding the case when the last letter is wildcard)
				
				return returnValue;
			}
		};
	}
}
