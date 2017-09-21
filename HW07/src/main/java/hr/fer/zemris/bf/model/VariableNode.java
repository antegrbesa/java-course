package hr.fer.zemris.bf.model;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Represents a variable in {@link Parser} class. 
 * @author Ante
 *
 */
public class VariableNode implements Node {
	
	/**Name of variable*/
	private String name;
	
	/**
	 * Creates an instance of this class with specified name. 
	 * @param name name to set
	 */
	public VariableNode(String name) {
		super();
		MyUtil.checkIfNull(name);
		this.name = name;
	}

	/**
	 * Returns variable name represented by this class. 
	 * @return variable name
	 */
	public String getName() {
		return name;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

}
