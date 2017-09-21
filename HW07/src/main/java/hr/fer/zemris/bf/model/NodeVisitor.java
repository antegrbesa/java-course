package hr.fer.zemris.bf.model;

/**
 * Interface that represents a <i>listener</i> for all nodes
 * derived from {@link Node} class. 
 * 
 * @author Ante Grbesa
 *
 */
public interface NodeVisitor {
	
	/**
	 * A method that is called once a {@link ConstantNode} is visited.
	 * @param node node that is visited
	 */
	void visit(ConstantNode node);
	
	/**
	 * A method that is called once a {@link VariableNode} is visited.
	 * @param node node that is visited
	 */
	void visit(VariableNode node);
	
	/**
	 * A method that is called once a {@link UnaryOperatorNode} is visited.
	 * @param node node that is visited
	 */
	void visit(UnaryOperatorNode node);
	
	/**
	 * A method that is called once a {@link BinaryOperatorNode} is visited.
	 * @param node node that is visited
	 */
	void visit(BinaryOperatorNode node);
}
