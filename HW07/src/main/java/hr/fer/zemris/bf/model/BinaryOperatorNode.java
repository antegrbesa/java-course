package hr.fer.zemris.bf.model;

import java.util.List;
import java.util.function.BinaryOperator;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Represents a binary operation in {@link Parser} class. 
 * @author Ante Grbesa
 *
 */
public class BinaryOperatorNode implements Node {
	
	/**Name of operation*/
	private String name;
	
	/**Members of operation*/
	private List<Node> children;
	
	/**
	 * Operator to apply to child. 
	 */
	private BinaryOperator<Boolean> operator;
	
	/**
	 * Creates an instance of this class with specified arguments. 
	 * @param name name of operation used
	 * @param children children nodes 
	 * @param operator operation to perform on chilld
	 * @throws IllegalArgumentException if any of given arguments are null 
	 */
	public BinaryOperatorNode(String name, List<Node> children, BinaryOperator<Boolean> operator) {
		super();
		MyUtil.checkIfNull(name, children, operator);
		this.name = name;
		this.children = children;
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
	 * Gets children of this operation.
	 * @return child of this operation
	 */
	public List<Node> getChildren() {
		return children;
	}
	
	/**
	 * Returns operator for this operation.
	 * @return operator for this operation
	 */
	public BinaryOperator<Boolean> getOperator() {
		return operator;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);

	}

}
