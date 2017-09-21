package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.lexer.QueryLexer;

/**
 * Represents a single token in {@link QueryLexer}. Token types are defined in
 * {@link TokenType} enum.
 * 
 * @author Ante Grbesa
 *
 */
public class Token {
	/**Token type*/
	private TokenType type;
	
	/**Value of token*/
	private String value;
	
	/**
	 * Creates a token with specified values.
	 * @param type type of token
	 * @param value value of token
	 */
	public Token(TokenType type, String value) {
		if(type == null) {
			throw new IllegalArgumentException("Token type cannot be null");
		}
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Gets the value of token.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Gets the type of token.
	 * @return the type
	 */
	public TokenType getType() {
		return type;
	}
	
	
}
