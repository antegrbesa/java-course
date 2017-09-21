package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Base class that represents a generic element in text.
 * Cannot be instantiated.
 * @author Ante Grbesa
 *
 */
public abstract class Element {

	/**
	 * Returns string representation of a value that this element represents.
	 * @return string representation of value
	 */
	public String asText() {
		return new String("");
	}

}
