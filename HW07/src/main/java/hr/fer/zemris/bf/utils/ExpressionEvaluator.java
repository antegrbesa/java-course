package hr.fer.zemris.bf.utils;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Implementation of a {@link NodeVisitor} interface. Calculates boolean value for given combination
 * of input variables. 
 * @author Ante
 *
 */
public class ExpressionEvaluator implements NodeVisitor {
	
	/**Values of variables*/
	private boolean[] values;
	
	/**Index positions of variables */
	private Map<String, Integer> positions;
	
	/**Collection for results*/
	private Stack<Boolean> stack = new Stack<>();
	
	/**
	 * Creates an instance of this class with specified variables,
	 * @param variables variables to set 
	 */
	public ExpressionEvaluator(List<String> variables) {
		positions = new HashMap<>();
		if (variables == null) {
			throw new IllegalArgumentException("Arguments were null");
		}
		for (String name : variables) {
			positions.put(name, variables.indexOf(name));
		}
	}
	
	/**
	 * Sets the values of currently stored variables. 
	 * @param values values to set 
	 * @throws IllegalStateException if given values contain a different number
	 * of values than there is variables
	 */
	public void setValues(boolean[] values) {
		if (values.length != positions.size()) {
			throw new IllegalStateException("Number of given variables is "
					+ "different from number of given values");
		}
		this.values = values;
		start();
	}
	
	/**
	 * Prepares this visitor for new evaluation. 
	 */
	public void start() {
		stack.clear();
	}
	
	/**
	 * Returns the result of given expression. 
	 * @return result of given expression
	 * @throws IllegalStateException if given expression
	 * was invalid 
	 */
	public boolean getResult() {
		if (stack.size() != 1) {
			throw new IllegalStateException("Expression was invalid");
		}
		
		return stack.peek();
	}
	
	@Override
	public void visit(ConstantNode node) {
		stack.push(node.getValue());

	}

	@Override
	public void visit(VariableNode node) {
		Integer n = positions.get(node.getName());
		if (n == null) {
			throw new IllegalStateException("No information given for"
					+ " variable: "+node.getName());
		}
		stack.push(values[n]);
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);
		try {
			node.getOperator().apply(stack.pop());
		} catch (EmptyStackException e) {
			throw new IllegalStateException("Expression was invalid");
		}
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		for (Node temp : node.getChildren()) {
			temp.accept(this);
		}
		try {
			for (int i = 0; i < node.getChildren().size()-1; i++) {
				boolean value = node.getOperator().apply(stack.pop(), stack.pop());
				stack.push(value);
			}
		} catch (EmptyStackException e) {
			throw new IllegalStateException("Expression was invalid");
		}
	}

}
