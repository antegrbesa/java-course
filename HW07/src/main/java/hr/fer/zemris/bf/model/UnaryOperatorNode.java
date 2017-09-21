package hr.fer.zemris.bf.model;

import java.util.function.UnaryOperator;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Represents a unary operation in {@link Parser} class. 
 * @author Ante Grbesa
 *
 */
public class UnaryOperatorNode implements Node {

	/**Name of operation*/
	private String name;
	
	/**A single member of operation*/
	private Node child;
	
	/**
	 * Operator to apply to child. 
	 */
	private UnaryOperator<Boolean> operator;

	/**
	 * Creates an instance of this class with specified arguments. 
	 * @param name name of operation used
	 * @param child child node
	 * @param operator operation to perform on chilld
	 * @throws IllegalArgumentException if any of given arguments are null 
	 */
	public UnaryOperatorNode(String name, Node child, UnaryOperator<Boolean> operator) {
		super();
		MyUtil.checkIfNull(name, child, operator);
		this.name = name;
		this.child = child;
		this.operator = operator;
	}
	
	/**
	 * Gets the name of operation.
	 * @return name of operation
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets child of this operation.
	 * @return child of this operation
	 */
	public Node getChild() {
		return child;
	}
	
	/**
	 * Returns operator for this operation.
	 * @return operator for this operation
	 */
	public UnaryOperator<Boolean> getOperator() {
		return operator;
	}
	
	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

}
