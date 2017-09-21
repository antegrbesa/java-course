package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents a single token in {@link Lexer} class. A token
 * can be:  
 * <ul>
 * <li>WORD
 * <li>NUMBER
 * <li>SYMBOL
 * <li>EOF (end of file)
 * <ul>
 * (listed in {@link TokenType}).
 * 
 * @author Ante Grbesa
 *
 */
public class Token {
	
	/**Type of this token*/
	private TokenType type;
	
	/**Value of this token*/
	private Object value;

	/**
	 * Constructs a single token with specified type and value
	 * @param type type of this token 
	 * @param value value of this token
	 */
	public Token(TokenType type, Object value) {
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
	public TokenType getType() {
		return type;
	}
	
	
	
	

}
