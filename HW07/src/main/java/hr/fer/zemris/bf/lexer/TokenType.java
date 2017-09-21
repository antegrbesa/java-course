package hr.fer.zemris.bf.lexer;

/**
 * Represents all the types a {@link Token} can be.
 * @author Ante Grbesa
 *
 */
public enum TokenType {
	/**End of file*/
	EOF,
	
	/**Variable type */
	VARIABLE,
	
	/**Constant type*/
	CONSTANT,
	
	/**Operator type*/
	OPERATOR,
	
	/**Open bracket*/
	OPEN_BRACKET,
	
	/**Closed bracket*/ 
	CLOSED_BRACKET;
}
