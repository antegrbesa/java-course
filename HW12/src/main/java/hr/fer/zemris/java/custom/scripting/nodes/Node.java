package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Base class for all graph nodes.
 * @author Ante Grbesa
 *
 */
public class Node {
	/**Collection for storing child elements of this element*/
	private ArrayIndexedCollection collection;
	
	/**Flag for first call of addChildNode() method*/
	private boolean first;
	
	/**
	 * Constructs an empty node.
	 */
	public Node() {
		first = false;
	}
	
	/**
	 * Adds given child to an internally managed collection of children.
	 * @param child child to add
	 */
	public void addChildNode(Node child) {
		if(! first) {
			collection = new ArrayIndexedCollection(4);
			first = true;
		}
		
		collection.add(child);
	}
	
	/**
	 * Returns a number of (direct) children.
	 * @return number of children
	 */
	public int numberOfChildren() {
		if (collection == null) {
			return 0;
		}
		return collection.size();
	}
	
	/**
	 * Returns selected child
	 * @param index index of a child
	 * @return child at given index
	 * @throws IndexOutOfBoundsException if index is out of range
	 */
	public Node getChild(int index) {
		return (Node) collection.get(index);
	}

	/**
	 * This method is called by an implementation of a visitor {@link INodeVisitor}.
	 * @param visitor visitor 
	 */
	public void accept(INodeVisitor visitor) {	
	}
	
}
