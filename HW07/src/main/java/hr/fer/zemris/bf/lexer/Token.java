package hr.fer.zemris.bf.lexer;


/**
 * Represents a single token in {@link Lexer} class. All types that a token can be are
 * listed in {@link TokenType}.
 * 
 * @author Ante Grbesa
 *
 */
public class Token {

	/**Value of this token*/
	private Object tokenValue;
	
	/**Type of this token*/
	private TokenType tokenType;

	/**
	 * 
	 ** Constructs a single token with specified type and value
	 * @param tokenType type of this token 
	 * @param tokenValue value of this token
	 */
	public Token(Object tokenValue, TokenType tokenType) {
		super();
		this.tokenValue = tokenValue;
		this.tokenType = tokenType;
	}
	
	/**
	 * Returns the type of this token. 
	 * @return type of this token
	 */
	public TokenType getTokenType() {
		return tokenType;
	}
	
	/**
	 * Returns the value of this token.
	 * @return value of this token
	 */
	public Object getTokenValue() {
		return tokenValue;
	}
	
	@Override
	public String toString() {
		return tokenValue.toString();
	}	
}
