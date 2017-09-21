package hr.fer.zemris.bf.model;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Interface that represents a generic node in a tree generated
 * by {@link Parser}. 
 * @author Ante Grbesa
 *
 */
public interface Node {
	
	/**
	 * Calls {@code visit} method from {@link NodeVisitor}
	 * on this class. 
	 * @param visitor visitor to call
	 */
	void accept(NodeVisitor visitor);
}
