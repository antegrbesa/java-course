package hr.fer.zemris.java.custom.scripting.nodes;

/**
 *A node representing a piece of textual data.
 * @author Ante Grbesa
 *
 */
public class TextNode extends Node {
	/**Text that this class represents.*/
	private String text;

	/**
	 * Constructs a TextNode with specified text.
	 * @param text text to set
	 * @throws IllegalArgumentException if given text is null
	 */
	public TextNode(String text) {
		super();
		if(text == null) {
			throw new IllegalArgumentException("Argument is null");
		}
		this.text = text;
	}
	
	/**
	 * Returns text that this node represents.
	 * @return text of this class
	 */
	public String getText() {
		return text;
	}

}
