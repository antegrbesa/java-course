package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration of types of tokens that a text that {@link ScriptLexer} processes
 * can have.
 * @author Ante Grbesa
 *
 */
public enum TokenType {
	
	/**Text part*/
	TEXT, 
	
	/**Variable */
	VARIABLE,
	
	/**An integer constant*/
	CONSTANT_INTEGER,
	
	/**A double constant*/
	CONSTANT_DOUBLE,
	
	/**A string inside of a tag*/
	STRING, 
	
	/**Function*/
	FUNCTION,
	
	/**Mathematical operator*/
	OPERATOR,
	
	/**Start of an echo tag*/	
	ECHO,
	
	/**Start of a  tag*/
	START_TAG,
	
	/**End of a  tag*/
	END_TAG,
	
	/**Notifies that no more tokens are left*/
	EOF;

}
