package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.parser.Parser;

/**
 * Implementation of a {@link NodeVisitor} interface. Prints the result
 * tree from {@link Parser} class to console. 
 * @author Ante Grbesa
 *
 */
public class ExpressionTreePrinter implements NodeVisitor {
	
	/**Indentation applied*/
	private int indentation = 0;
	
	@Override
	public void visit(ConstantNode node) {
		printIndentation();
		System.out.println(node.getValue());
	}

	@Override
	public void visit(VariableNode node) {
		printIndentation();
		System.out.println(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		printIndentation();
		System.out.println(node.getName());
		indentation+=2;
		node.getChild().accept(this);
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		printIndentation();
		System.out.println(node.getName());
		indentation+=2;
		int tempIndent = indentation;
		for (Node tmp : node.getChildren()) {
			tmp.accept(this);
			indentation = tempIndent;
		}

	}
	
	/**
	 * Prints indentation whitespaces.
	 */
	private void printIndentation() {
		for (int i = 0; i < indentation; i++) {
			System.out.print(" ");
		}
	}

}
