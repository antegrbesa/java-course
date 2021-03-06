package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents a single token in {@link SmartScriptLexer} class. Token types 
 * are defined in {@link SmartScriptTokenType} enum. 
 * 
 * @author Ante Grbesa
 *
 */
public class SmartScriptToken {
	/**Type of this token*/
	private SmartScriptTokenType type;
	
	/**Value of this token*/
	private Object value;

	/**
	 * Constructs a single token with specified type and value
	 * @param type type of this token 
	 * @param value value of this token
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		if(type == null) {
			throw new IllegalArgumentException("Token type cannot be null");
		}
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of this token.
	 * @return value of this token
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the type of this token. 
	 * @return type of this token
	 */
	public SmartScriptTokenType getType() {
		return type;
	}
}
	
