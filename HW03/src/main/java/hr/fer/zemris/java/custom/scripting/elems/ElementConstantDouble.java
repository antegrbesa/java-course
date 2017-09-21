package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents a double value generated from text. 
 * @author Ante Grbesa
 *
 */
public class ElementConstantDouble extends Element {
	
	/** Value that this class represents*/
	private double value;

	/**
	 * Constructs an <code>ElementConstantDouble</code> with specified value.
	 * @param value value of this element
	 */
	public ElementConstantDouble(double value) {
		super();
		this.value = value;
	}
	
	@Override
	public String asText() {
		return Double.toString(value);
	}
	
	/**
	 * Returns value that this class represents
	 * @return value of this class
	 */
	public double getValue() {
		return value;
	}

}
