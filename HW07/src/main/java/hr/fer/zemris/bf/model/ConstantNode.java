package hr.fer.zemris.bf.model;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Represents a constant in {@link Parser} class. 
 * @author Ante
 *
 */
public class ConstantNode implements Node {
	
	/**Constant value*/
	private boolean value;
	
	/**
	 * Creates an instance of this class with specified value.
	 * @param value value to set
	 */
	public ConstantNode(boolean value) {
		MyUtil.checkIfNull(value);
		this.value = value;
	}
	
	/**
	 * Returns value that is represented by this class. 
	 * @return value currently stored
	 */
	public boolean getValue() {
		return value;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

}
