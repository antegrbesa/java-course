package hr.fer.zemris.java.custom.scripting.parser;


import hr.fer.zemris.java.custom.scripting.lexer.ScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.elems.*;

/**
 * Implementation of a parser that makes use of a {@link ScriptLexer}.
 * Parses text with specifications given in {@link ScriptLexer} class.
 * The final result is saved as a tree-like {@link DocumentNode}.
 * 
 * @author Ante Grbesa
 */
public class SmartScriptParser {
	/**Lexer that generates tokens from text*/
	private ScriptLexer lexer;
	
	/**A stack collection that represents parsed text*/
	private ObjectStack stack;
	
	/**A top node that represents whole text*/
	private DocumentNode document;
	
	/**Current top node in stack*/
	private Node current;
	
	/**
	 * Constructs an instance of this class and immediately tries to 
	 * parse given text. 
	 * @param text text to parse
	 * @throws IllegalArgumentException if given text is null
	 * @throws SmartScriptParserException if an error occurred during parsing
	 */
	public SmartScriptParser(String text) {
		if(text == null) {
			throw new IllegalArgumentException("Input text was null.");
		}
		stack = new ObjectStack();
		lexer = new ScriptLexer(text);
		document = new DocumentNode();
		
		current = document;
		stack.push(current);
		try {	
			parse();
		} catch(ScriptLexerException e) {
			throw new SmartScriptParserException(e.getMessage());
		}
	}
	
	/**
	 * A helper method that tries to parse given text. 
	 * Result is stored in stack collection. 
	 * @throws SmartScriptParserException if an error occurred during parsing
	 */
	private void parse() {
		Token token;
		while((token = lexer.nextToken()).getType() != TokenType.EOF) {
			TokenType type = token.getType();
			
			if(type == TokenType.TEXT) {
				current.addChildNode(new TextNode((String) token.getValue()));
				continue;
			}
			
			
			if(type == TokenType.START_TAG) {	//process the whole tag
				token = lexer.nextToken();
				type = token.getType();
				
				if(type == TokenType.ECHO) {	//tag is an echo tag
					ArrayIndexedCollection col = new ArrayIndexedCollection(6);	//random value for collection size,
					try {														//but less than default constructor size
					while((token = lexer.nextToken()).getType() != TokenType.END_TAG) {
						Element el = getElement(token, true);
						col.add(el);
					}
					} catch(ScriptLexerException e) {
						throw new SmartScriptParserException("Invalid input in tag after "+
								token.getType() + " " + token.getValue());
					}
					
					int n = col.size();
					Element[] elements = new Element[n];
					for(int i = 0; i < n; i++) {
						elements[i] = (Element) col.get(i);
					}
					
					current.addChildNode(new EchoNode(elements));
					continue;
				}
				
				if(type == TokenType.VARIABLE) {	//tag is a for loop
					String name = (String) token.getValue();
					
					if(name.toUpperCase().equals("FOR")) {	//process for tag
						token = lexer.nextToken();
						if(token.getType() != TokenType.VARIABLE) {
							throw new SmartScriptParserException("Invalid variable name in 'FOR'");
						}
						ElementVariable var = new ElementVariable((String) token.getValue());
						
						int i = 0;
						Element[] elements = new Element[3];
						while(i < 4) {	//if index is 3, there are too many arguments in for
							token = lexer.nextToken();
							if(token.getType() == TokenType.END_TAG) {
								if(i <= 1) {	//early finish, too few arguments in for
									throw new SmartScriptParserException("Too few arguments in for, had "+(i+1));
								}
								break;
							}
							if(i == 3) {	//too many arguments in for
								throw new SmartScriptParserException("Too much arguments in for, was "+(i+1));
							}
							
							elements[i] = getElement(token, false);
							i++;
						}
						
						ForLoopNode forNode = new ForLoopNode(var, elements[0], elements[1], elements[2]);
						current.addChildNode(forNode);
						stack.push(forNode);
						current = forNode;
						continue;
					}
					
					if(name.toUpperCase().equals("END")) {	//tag is an end tag
						try {
							stack.pop();
							current = (Node) stack.peek();
							continue;
						} catch(EmptyStackException e) {
							throw new SmartScriptParserException("Text contains more end tags than non-empty tags"
									+ " or end tag wasn't properly closed");
						}
					}
					
					throw new SmartScriptParserException("Invalid tag name, was "+ (String) token.getValue());
				}
				
			}
		}
		
		if(stack.size() != 1) {	//check if parsing was successful
			throw new SmartScriptParserException("For tag not properly closed");
		}
	}
	
	/**
	 * Generate an element from given token. If this method was called for
	 * while processing for tag, some types are marked as illegal (e.g. function) 
	 * with echo flag.
	 * @param token token where value is stored
	 * @param echo flag for caller identification (can be called for by
	 * echo tag or for tag)
	 * @return generated Element from given token
	 * @throws SmartScriptParserException if token isn't a legal element
	 */
	private Element getElement(Token token, boolean echo) {
		if(token == null) {
			throw new SmartScriptParserException("Input contained null");
		}
		TokenType type = token.getType();
		
		if(type == TokenType.VARIABLE) {
			return new ElementVariable((String) token.getValue());
		}
		if(type == TokenType.FUNCTION && echo) {
			return new ElementFunction((String) token.getValue());
		}
		if(type == TokenType.STRING) {
			return new ElementString((String) token.getValue());
		}
		if(type == TokenType.OPERATOR && echo) {
			return new ElementOperator((String) token.getValue());
		}
		if(type == TokenType.CONSTANT_INTEGER) {
			return new ElementConstantInteger((int) token.getValue());
		}
		if(type == TokenType.CONSTANT_DOUBLE) {
			return new ElementConstantDouble((double) token.getValue());
		}
		
		//all valid inputs were checked, what remains is invalid 
		throw new SmartScriptParserException("Invalid input in tag, was " +token.getType());
		
	}
	
	/**
	 * Returns a DocumentNode that represents parsed text. 
	 * @return DocumentNode representation of parsed text
	 * @throws SmartScriptParserException if given text was not properly written
	 */
	public DocumentNode getDocumentNode() {
		if(stack.size() != 1) {
			throw new SmartScriptParserException("Input was invalid (non-empty tag was not closed)");
		}
		
		return (DocumentNode) stack.peek();
	}
	

}
