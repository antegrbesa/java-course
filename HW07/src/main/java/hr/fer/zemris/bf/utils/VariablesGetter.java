package hr.fer.zemris.bf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.parser.Parser;

/**
 * Implementation of a {@link NodeVisitor} interface. Creates a list that contains all
 * variables from a {@link Parser} result node. 
 * @author Ante Grbesa
 *
 */
public class VariablesGetter implements NodeVisitor {
	
	/**All variables*/
	private Set<String> variables;
	
	/**
	 * Constructor for this class.
	 */
	public VariablesGetter() {
		variables = new TreeSet<>();
	}
	
	@Override
	public void visit(ConstantNode node) {
	}

	@Override
	public void visit(VariableNode node) {
		variables.add(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);

	}

	@Override
	public void visit(BinaryOperatorNode node) {
		for (Node tmp : node.getChildren()) {
			tmp.accept(this);
		}
	}
	
	/**
	 * Returns all variables in lexicographic order as a list. 
	 * @return list containing all variables
	 */
	public List<String> getVariables() {
		return new ArrayList<>(variables);
	}

}
