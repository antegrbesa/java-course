package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents a function name generated from text. 
 * @author Ante
 *
 */
public class ElementFunction extends Element {
	/** Value that this class represents*/
	private String name;

	/**
	 * Constructs an <code>ElementFunction</code> with specified value.
	 * @param name function name  
	 */
	public ElementFunction(String name) {
		super();
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	/**
	 * Returns name of a function that this class represents
	 * @return function name representation
	 */
	public String getName() {
		return name;
	}
}
