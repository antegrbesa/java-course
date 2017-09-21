package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an operator generated from text. 
 * @author Ante Grbesa
 *
 */
public class ElementOperator extends Element {
	/** Operator that this class represents*/
	private String operator;

	/**
	 * Constructs an <code>ElementOperator</code> with specified value.
	 * @param operator operator of this element
	 */
	public ElementOperator(String operator) {
		super();
		this.operator = operator;
	}
	
	/**
	 * Returns operator that this class represents
	 * @return operator representation
	 */
	public String getOperator() {
		return operator;
	}
	
	@Override
	public String asText() {
		return operator;
	}

}
