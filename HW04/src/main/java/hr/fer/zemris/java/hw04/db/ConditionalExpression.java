package hr.fer.zemris.java.hw04.db;

/**
 * Represents a conditional expression that contains a {@link IFieldValueGetter} getter,
 * {@link IComparisonOperator} operator and a String literal
 * 
 * @author Ante Grbesa
 *
 */
public class ConditionalExpression {
	/**Getter for field value*/
	private IFieldValueGetter fieldGetter;
	
	/**String literal*/
	private String stringLiteral;
	
	/**Operator for comparing elements*/
	private IComparisonOperator comparisonOperator;

	/**
	 * Constructs {@link ConditionalExpression} with specified values.
	 * @param getter getter to set
	 * @param literal String literal to set
	 * @param operator operator to set
	 * @throws IllegalArgumentException if any of given arguments are null
	 */
	public ConditionalExpression(IFieldValueGetter getter, String literal, IComparisonOperator operator) {
		if(getter == null || literal == null || operator == null) {
			throw new IllegalArgumentException();
		}
		
		this.fieldGetter = getter;
		this.stringLiteral = literal;
		this.comparisonOperator = operator;
	}

	/**
	 * Returns the field getter.
	 * @return the getter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Returns the string literal.
	 * @return the literal
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Returns the comparison operator.
	 * @return the operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
	
	/**
	 * Sets the string literal
	 * @param stringLiteral literal to set
	 */
	public void setStringLiteral(String stringLiteral) {
		this.stringLiteral = stringLiteral;
	}
	
}
