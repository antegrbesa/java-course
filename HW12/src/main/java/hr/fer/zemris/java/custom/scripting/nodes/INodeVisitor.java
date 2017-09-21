package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents a visitor interface for implementing <i>Visitor Design Pattern</i>
 * for {@link Node} class and it's subclasses. 
 * @author Ante
 *
 */
public interface INodeVisitor {
	
	/**
	 * This method is called once {@link TextNode} is visited. 
	 * @param node node visited
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * This method is called once {@link ForLoopNode} is visited. 
	 * @param node node visited
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * This method is called once {@link EchoNode} is visited. 
	 * @param node node visited
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * This method is called once {@link DocumentNode} is visited. 
	 * @param node node visited
	 */
	public void visitDocumentNode(DocumentNode node);
}
