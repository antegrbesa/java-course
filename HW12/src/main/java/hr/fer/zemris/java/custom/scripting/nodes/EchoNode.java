package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;

/**
 * Represents a command which generates some textual output dynamically.
 * @author Ante Grbesa
 *
 */
public class EchoNode extends Node {
	
	/**Collection for storing elements*/
	private Element[] elements;

	/**
	 * Creates an instance of EchoNode with specified elements.
	 * @param elements elements to set
	 * @throws IllegalArgumentException if elements argument is null
	 */
	public EchoNode(Element[] elements) {
		super();
		if(elements == null) {
			throw new IllegalArgumentException("Argument is null");
		}
		this.elements = elements;
	}
	
	/**
	 * Returns an array of elements from this class.
	 * @return array of elements from this EchoNode
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$= ");
		for(Element e : elements) {
			if(e instanceof ElementFunction) {
				sb.append("@");
			}
			sb.append(e.asText() + " ");
		}
		sb.append("$}");
		
		return sb.toString();
	}
	
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
	
}
