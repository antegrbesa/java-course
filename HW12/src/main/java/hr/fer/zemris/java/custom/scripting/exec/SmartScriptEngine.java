package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiFunction;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This class executes the document whose parsed tree ({@link DocumentNode class} it obtains. 
 * Uses an implementation of a {@link INodeVisitor} class. 
 * @author Ante Grbeša
 *
 */
public class SmartScriptEngine {

	/**Parsed tree*/
	private DocumentNode documentNode;
	
	/**Request context used*/
	private RequestContext requestContext;
	
	/**Collection for storing wrapped elements*/
	private ObjectMultistack multiStack = new ObjectMultistack();
	
	/**
	 * Operations supported. 
	 */
	private static Map<String, BiFunction<Double, Double, Double>> operations;
	
	 static {
		operations = new HashMap<>();
		operations.put("+", (f,s) ->f+s);
		operations.put("-", (f,s) -> f-s);
		operations.put("*", (f,s) -> f*s);
		operations.put("/", (f,s) -> f/s);
	}
	
	 /**
	  * Constructs an instance of this class using specified arguments. 
	  * @param documentNode parsed tree to set
	  * @param requestContext request context to set
	  */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		super();
		if (documentNode == null || requestContext == null) {
			throw new IllegalArgumentException("One or more arguments were null");
		}
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Starts this smartscript engine. 
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

	/**
	 * Internal implementation of a {@link INodeVisitor} used while processing
	 * parsed tree. 
	 */
	private INodeVisitor visitor = new INodeVisitor() {
		
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				throw new RuntimeException("Error while writing, "+e.getMessage());
			}
		}
		
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String name = node.getVariable().getName();
			String endExpr = node.getEndExpression().asText();
			String stepExpr = node.getStepExpression() == null ? "1" : node.getStepExpression().asText();
			
			multiStack.push(name, new ValueWrapper(node.getStartExpression().asText()));
			while (multiStack.peek(name).numCompare(endExpr) <= 0) {
				for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
					node.getChild(i).accept(this);
				}
				
				multiStack.peek(name).add(stepExpr);
			}
			
			multiStack.pop(name);
		}
		
		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<>();
			
			for (Element e : node.getElements()) {
				if (pushValue(e, stack)) {
					continue;
				}
				if (e instanceof ElementVariable) {
					ElementVariable el = (ElementVariable) e;
					stack.push(multiStack.peek(el.getName()).getValue());
					continue;
				}
				if (e instanceof ElementOperator) {
					pushOperator(e, stack);
					continue;
				}
				if (e instanceof ElementFunction) {
					try {
						pushFunction(e, stack);
					} catch (Exception ex) {
						throw new RuntimeException("Invalid input.");

					}
					continue;
				}
			}
			
			Object[] array = stack.toArray();
			for (int i = 0; i < array.length; i++) {
				try {
					requestContext.write(array[i].toString());
				} catch (IOException e1) {
					throw new RuntimeException("Error while writing stack, "+e1.getMessage());
				}
			}
			
		}
		
		/**
		 * Pushes a function to given stack.
		 * @param e element processed
		 * @param stack stack 
		 */
		private void pushFunction(Element e, Stack<Object> stack) {
			ElementFunction el = (ElementFunction) e;
			String name = el.getName();
			
			doFunction(name.trim(), stack);
		}

		/**
		 * Executes a function with specified name. 
		 * @param name name of function
		 * @param stack stack
		 */
		private void doFunction(String name, Stack<Object> stack) {
			switch (name) {
			case "sin":
				double value = getDoubleValue(stack.pop());
				Double res =  Math.sin(Math.toRadians(value)); 
				stack.push(res.toString());
				break;
			case "decfmt":
				DecimalFormat f = new DecimalFormat((String) stack.pop());
				double x = getDoubleValue(stack.pop());
				stack.push(f.format(x));
				break;
			case "dup":
				Object o = stack.pop();
				stack.push(o);
				stack.push(o);
				break;
			case "swap":
				Object obj1 = stack.pop();
				Object obj2 = stack.pop();
				stack.push(obj1);
				stack.push(obj2);
				break;
			case "setMimeType":
				Object ob = stack.pop();
				requestContext.setMimeType((String) ob);
				break;
			case "paramGet":
				Object dv = stack.pop();
				Object name1 = stack.pop();
				String result = requestContext.getParameter((String) name1);
				stack.push(result == null ? dv : result);
				break;
			case "pparamGet":
				Object dv2 = stack.pop();
				Object name2 = stack.pop();
				String result2 = requestContext.getPersistentParameter((String) name2);
				stack.push(result2 == null ? dv2 : result2);
				break;
			case "pparamSet":
				Object name3 = stack.pop();
				Object value3 = stack.pop();
				requestContext.setPersistentParameter((String) name3, (String) value3);
				break;
			case "pparamDel":
				String name4 = (String) stack.pop();
				requestContext.removePersistentParameter(name4);
				break;
			case "tparamGet":
				Object dv5 = stack.pop();
				Object name5 = stack.pop();
				String result5 = requestContext.getTemporaryParameter((String) name5);
				stack.push(result5 == null ? dv5 : result5);
				break;
			case "tparamSet":
				Object name6 = stack.pop();
				Object value6 = stack.pop();
				requestContext.setTemporaryParameter((String) name6, (String) value6);
				break;
			case "tparamDel":
				String name7 = (String) stack.pop();
				requestContext.removeTemporaryParameter(name7);
				break;
			}
			
		}
		
		/**
		 * Gets a double value from given Object. Object must be either Integer, Double or 
		 * a String. 
		 * @param value value of object
		 * @return double value
		 */
		private double getDoubleValue(Object value) {
			double result;
			if (value instanceof String) {
				result = Double.parseDouble((String) value);
			} else if (value instanceof Integer) {
				int temp = (int) value;	//cast Integer to primitive int
				result = (double) temp;
			} else {
				result = (double) value;
			}
			
			return result;
		} 
		
		/**
		 * Pushes operator to given stack
		 * @param e operator
		 * @param stack stack
		 */
		private void pushOperator(Element e, Stack<Object> stack) {
			ElementOperator el = (ElementOperator) e;
			Object first = stack.pop();
			Object second = stack.pop();
			doOperation(first, second, el.getOperator(), stack);
			
		}

		/**
		 * Executes a given operation. 
		 * @param first first argument
		 * @param second second argument
		 * @param operator operator used
		 * @param stack stack used
		 */
		private void doOperation(Object first, Object second, String operator, Stack<Object> stack) {
			if (! operations.containsKey(operator)) {
				throw new IllegalArgumentException("Invalid operation, was "+operator);
			}
	
			
			Double res = operations.get(operator).apply(getDoubleValue(first), getDoubleValue(second));
			stack.push(res.toString());
		}

		/**
		 * Pushes given value to given stack.
		 * @param e value
		 * @param stack stack
		 * @return true if value pushed, false otherwise
		 */
		private boolean pushValue(Element e, Stack<Object> stack) {
			if (e instanceof ElementConstantDouble) {
				ElementConstantDouble el = (ElementConstantDouble) e;
				stack.push(el.getValue());
				return true;
			}
			if (e instanceof ElementConstantInteger) {
				ElementConstantInteger el = (ElementConstantInteger) e;
				stack.push(el.getValue());
				return true;
			}
			if (e instanceof ElementString)	{
				ElementString el = (ElementString) e;
				stack.push(el.getValue());
				return true;
			}
			
			return false;
		}
		
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(visitor);
			}
		}
	};
}
