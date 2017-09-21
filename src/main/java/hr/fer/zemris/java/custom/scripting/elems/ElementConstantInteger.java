package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an integer value generated from text.
 * @author Ante Grbesa
 *
 */
public class ElementConstantInteger extends Element {
	/** Value that this class represents*/
	private int value;

	/**
	 * Constructs an <code>ElementConstantInteger</code> with specified value.
	 * @param value value of this element
	 */
	public ElementConstantInteger(int value) {
		super();
		this.value = value;
	}
	
	@Override
	public String asText() {
		return Integer.toString(value);
	}
	
	/**
	 * Returns value that this class represents
	 * @return value of this class
	 */
	public int getValue() {
		return value;
	}
	

}
