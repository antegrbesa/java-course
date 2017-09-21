package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents a variable generated from text. 
 * @author Ante Grbesa
 */
public class ElementVariable extends Element {

	/** Name of a variable that this class represents*/
	private String name;

	/**
	 * Constructs an <code>ElementVariable</code> with specified value.
	 * @param name name of the variable to set
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	/**
	 * Returns name of a variable that this class represents.
	 * @return variable name
	 */
	public String getName() {
		return name;
	}
	
	
}
