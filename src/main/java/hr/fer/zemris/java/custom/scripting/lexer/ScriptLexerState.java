package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents a state of a {@link ScriptLexer}
 * @author Ante
 *
 */
public enum ScriptLexerState {
	
	/**Represents a state where ordinary text is processed*/
	TEXT,
	
	/**Represents a state where a tag is processed*/
	TAG;

}
