package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.lexer.QueryLexer;

/**
 * Enumeration of types of tokens that a text that {@link QueryLexer} processes
 * can have.
 * @author Ante Grbesa
 *
 */
public enum TokenType {
	/**Identifier type*/
	IDENTIFIER, 
	
	/**Operator type*/
	OPERATOR,
	
	/**String type*/
	STRING, 
	
	/**End of file type*/
	EOF;
	
}
