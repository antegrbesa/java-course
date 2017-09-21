package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents an entire document. Parts 
 * of parsed text are added to this node as it's children.
 * @author Ante Grbesa
 *
 */
public class DocumentNode extends Node {
	
	/**
	 * Creates an instance of DocumentNode.
	 */
	public DocumentNode() {
		super();
	}
	
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}

}
