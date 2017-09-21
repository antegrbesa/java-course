package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Represents a single for-loop construct.
 * A for-loop can contain 3 or 4 elements.
 *  First it must have one variable and after that two or three Elements
 *  of type variable, number or string. 
 * @author Ante Grbesa
 *
 */
public class ForLoopNode extends Node {
	/**Variable in for loop*/
	private ElementVariable variable;
	
	/**Starting expression*/
	private Element startExpression;
	
	/**End expression*/
	private Element endExpression;
	
	/**Step expression*/
	private Element stepExpression;
	
	/**
	 * Constructs a ForLoopNode with specified arguments. Step Expression argument
	 * can be null, others cannot.
	 * @param variable name of a variable
	 * @param startExpression starting expression for this loop
	 * @param endExpression end expression for this loop
	 * @param stepExpression Step expression for this loop
	 * @throws IllegalArgumentException if any of given arguments (except stepExpression) was null
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		super();
		if(variable == null || startExpression == null || endExpression == null) {
			throw new IllegalArgumentException("Arguments are null");
		}
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Returns variable name for this for loop.
	 * @return variable name
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Returns start expression for this for loop.
	 * @return start expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Returns end expression for this for loop.
	 * @return end expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Returns step expression for this for loop.
	 * @return step expression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{$FOR ");
		sb.append(getVariable().asText() + " ");
		sb.append(getStartExpression().asText() + " ");
		sb.append(getEndExpression().asText() + " ");
		Element end =  getStepExpression();
		if(end != null) {
			sb.append(end.asText() + " ");
		}
		sb.append("$} ");
		for (int i = 0; i < numberOfChildren(); i++) {
			sb.append(getChild(i).toString());
		}
		sb.append(" {$END$}");
		
		return sb.toString();
	}
	
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
}
