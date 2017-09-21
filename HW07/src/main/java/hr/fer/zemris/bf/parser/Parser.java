package hr.fer.zemris.bf.parser;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.MyUtil;
import hr.fer.zemris.bf.model.VariableNode;


/**
 * Implementation of a recursive descent parser that makes use of {@link Lexer} class
 * for production of tokens. . 
 * Parses text containing boolean expression (details given in {@link Lexer} class).
 * Final result is saved as a {@link Node} containing all other elements  as classes that 
 * inherit from {@code Node} class. 
 * 
 * @author Ante Grbesa
 *
 */
public class Parser {

	/**Lexer used for production of tokens*/
	private Lexer lexer;
	
	/**Current root node*/
	private Node root;
	
	/**Current token*/
	private Token current;
	
	/**
	 * Constructs an instance of this class and immediately tries to 
	 * parse given text. 
	 * @param expression text to parse
	 * @throws IllegalArgumentException if given text is null
	 * @throws ParserException if an error occurred during parsing
	 */
	public Parser(String expression) {
		MyUtil.checkIfNull(expression);
		
		lexer = new Lexer(expression);
		root = null;
		try {
			getOperation();
		} catch (Exception e) {
			throw new ParserException(e.getMessage());
		}
	}
	
	
	
	/**
	 * Generates a {@link BinaryOperatorNode} represented by 'or' operation. 
	 */
	private void getOperation() {
		getTerm();
		
		while (current.getTokenType() == TokenType.OPERATOR) {
			if (current.getTokenValue().equals("or")) {
				boolean hasParent = false;
				BinaryOperatorNode rootCopy = checkParent();
				hasParent = rootCopy == null ? false : true;
				
				List<Node> children = new ArrayList<>();
				if (! hasParent) {
					children.add(root);
				}
				getTerm();
				children.add(root);
				
				BinaryOperatorNode operator = new BinaryOperatorNode("or", children, (s,t) -> s || t);
				if (hasParent) {
					rootCopy.getChildren().addAll(operator.getChildren());
					root = rootCopy;
				} else {
					root = operator;
				}
				
			} else {
				break;
			}
		}
		
	}
	
	/**
	 * Checks if current root node is a parent for current token. 
	 * @return root node as a BinaryOperatorNode
	 */
	private BinaryOperatorNode checkParent() {
		boolean hasParent = false;
		BinaryOperatorNode rootCopy = null;
		
		try {
			rootCopy  = (BinaryOperatorNode) root;
			if (rootCopy.getName().equals(current.getTokenValue())) {
				hasParent = true;
			}
		} catch(Exception ignorable) {}
		
		if (hasParent) {
			return rootCopy;
		} else {
			return null;
		}
	}

	/**
	 * Generates a {@link BinaryOperatorNode} represented by 'and' or 'xor' operation.
	 */
	private void getTerm() {
		getFactor();
		
		while (current.getTokenType() == TokenType.OPERATOR) {
			if (current.getTokenValue().equals("and") || current.getTokenValue().equals("xor")) {
				boolean hasParent = false;
				BinaryOperatorNode rootCopy = checkParent();
				hasParent = rootCopy == null ? false : true;
				
				String name = (String) current.getTokenValue();
				BinaryOperatorNode operator;
				List<Node> children = new ArrayList<>();
				if (! hasParent) {
					children.add(root);
				}
				
				getTerm();
				children.add(root);
				
				if (name.equals("and")) {
					operator = new BinaryOperatorNode(name, children, (s,t)-> s && t);
				} else {
					operator = new BinaryOperatorNode(name, children, (s,t)-> s ^ t);
				}
				
				if (hasParent) {
					rootCopy.getChildren().addAll(operator.getChildren());
					root = rootCopy;
				} else {
					root = operator;
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Generates a factor (constant or variable).
	 */
	private void getFactor() {
		current = lexer.nextToken();
		
		if (current.getTokenType() == TokenType.CONSTANT) {
			getConstant();
		} else if (current.getTokenType() == TokenType.VARIABLE) {
			root = new VariableNode( (String) current.getTokenValue());
			current = lexer.nextToken();
			checkOperator();

		} else if (current.getTokenType() == TokenType.OPERATOR) {
			getOperator();
		} else if (current.getTokenType() == TokenType.OPEN_BRACKET) {
			getOperation();
			
			if (current.getTokenType() != TokenType.CLOSED_BRACKET) {
				throw new ParserException("Bracket wasn't properly closed");
			}
			current = lexer.nextToken();	//ignore closed bracket
			if (current.getTokenType() != TokenType.EOF && current.getTokenType() != TokenType.OPERATOR) {
				throw new ParserException("Unexpected token");
			}
		} else {
			throw new ParserException("Unexpected token.");
		}

	}
	
	/**
	 * Generates a constant. 
	 */
	private void getConstant() {
		boolean value = (boolean) current.getTokenValue();
		
		if (value) {
			root = new ConstantNode(true);
			current = lexer.nextToken();
		} else {
			root = new ConstantNode(false);
			current = lexer.nextToken();
		}
		
		checkOperator();
	}
	
	/**
	 * Generates a 'not' operator. 
	 */
	private void getOperator() {
		
		if (current.getTokenValue().equals("not")) {
			UnaryOperatorNode not;
			getFactor();
			not = new UnaryOperatorNode("not", root, (fact)-> !fact);
			root = not;
			if (current.getTokenType() == TokenType.OPERATOR) {
				if (current.getTokenValue().equals("not")) {
					throw new ParserException("Unexpected token.");
				}
			}
			
		} else {
			throw new ParserException("Invalid use of operator "+current.getTokenValue());
		}
	}

	/**
	 * Checks if currently stored token is a operator or a closed bracket. 
	 */
	private void checkOperator() {
		if (current.getTokenType() != TokenType.EOF && current.getTokenType() != TokenType.OPERATOR
				&& current.getTokenType() != TokenType.CLOSED_BRACKET) {
			throw new ParserException("Invalid operator, was: "+current.getTokenValue());
		}
	}

	/**
	 * Returns a {@link Node} that represents a boolean operation generated 
	 * from given text. 
	 * @return node that represents a boolean operation
	 */
	public Node getExpression() {
		return root;
	}
	
	
}
