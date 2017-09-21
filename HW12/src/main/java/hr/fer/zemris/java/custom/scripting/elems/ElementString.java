package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents a string generated from text. 
 * @author Ante Grbesa
 *
 */
public class ElementString extends Element {
	/** Value that this class represents*/
	private String value;

	/**
	 * Constructs an <code>ElementString</code> with specified value.
	 * @param value value of the string  for this element
	 */
	public ElementString(String value) {
		super();
		this.value = value;
	}
	
	@Override
	public String asText() {
		return value;
	}
	/**
	 * Returns string that this class represents
	 * @return string that this class represents
	 */
	public String getValue() {
		return value;
	}
}
