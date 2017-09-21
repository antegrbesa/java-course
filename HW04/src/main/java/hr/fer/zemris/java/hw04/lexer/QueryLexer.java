package hr.fer.zemris.java.hw04.lexer;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.db.Token;
import hr.fer.zemris.java.hw04.db.TokenType;
import hr.fer.zemris.java.hw04.parser.QueryParser;

/**
 * A simple lexer for {@link QueryParser} class. Generates tokens from specified text. Token types 
 * are described in {@link TokenType} enum.  This lexer is "lazy" which means that the extraction of the 
 * token is done only when it is explicitly called for using  {@link #nextToken()} method.
 * 
 * @author Ante Grbesa
 *
 */
public class QueryLexer {

	/** Input text*/
	private char[] data;
	
	/**Current token*/
	private Token token;
	
	/**Index of the first unprocessed character*/
	private int currentIndex;
	
	/**List of legal identifiers*/
	private static List<String> identifiers;
	
	static {
		identifiers = new ArrayList<>();
		identifiers.add("AND");
		identifiers.add("LIKE");
		identifiers.add("JMBAG");
		identifiers.add("FIRSTNAME");
		identifiers.add("LASTNAME");
		identifiers.add("QUERY");
	}
	
	/**
	 * Constructs an instance of this lexer and specifies input text.
	 * @param text input text
	 */
	public QueryLexer(String text) {
		if(text == null) {
			throw new IllegalArgumentException("Input text is null.");
		}
		data = text.toCharArray();
		currentIndex = 0;
	}
	
	/**
	 * Returns currently stored token.
	 * @return currently stored token 
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Generates and returns the next token in text. 
	 * @return next token
	 * @throws QueryLexerException if an error occurred during generation
	 * of new token
	 */
	public Token nextToken() {
		getNextToken();
		return token;
	}
	
	/**
	 * Generates next token and stores it as current token.
	 */
	private void getNextToken() {
		if(token != null && token.getType() == TokenType.EOF) {
			throw new QueryLexerException("End of file");
		}
		
		skipBlanks();
		
		if(currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		if(Character.isLetter(data[currentIndex])) {
			getIdentifier();
		} else if(data[currentIndex] == '\"') {
			getString();
		} else if(!getOperator()) {
			throw new QueryLexerException("Invalid input after " + token.getValue());
		}
		
	}
	
	/**
	 * Generates an identifier token.
	 */
	private void getIdentifier() {
		int start = currentIndex;
		while(currentIndex < data.length && Character.isLetter(data[currentIndex])) {
			currentIndex++;
		}
		
		String word = new String(data, start, currentIndex-start);
		if(word.equals("LIKE")) {
			token = new Token(TokenType.OPERATOR, word);
			return;
		}
		checkIdentifier(word);
		
		
		token = new Token(TokenType.IDENTIFIER, word);
	}
	
	/**
	 * Checks if given string is legal identifier.
	 * @param word string to check
	 * @throws QueryLexerException if given string is illegal
	 */
	private void checkIdentifier(String word) {
		String check = word.toUpperCase();
		boolean legal = false;
		for(String tmp : identifiers) {
			if(tmp.equals(check)) {
				legal = true;
				break;
			}
		}
		
		if(! legal) {
			throw new QueryLexerException("Invalid identifier, was "+word);
		}
	}
	
	/**
	 * Generates a string token.
	 * @throws QueryLexerException if string is not properly closed
	 */
	private void getString() {
		int start = ++currentIndex;
		while(data[currentIndex] != '\"') {
			if(currentIndex == data.length - 1) {
				throw new QueryLexerException("String not properly closed");
			}
			currentIndex++;
		}
		
		String word = new String(data, start, currentIndex-start);
		word.replace("\"", "");
		token = new Token(TokenType.STRING, word);
		currentIndex++;
	}
	
	/**
	 * Generates an operator token.
	 * @return true if current element is operator, false otherwise
	 */
	private boolean getOperator() {
		char c = data[currentIndex];
		
		if(c == '=') {
			token = new Token(TokenType.OPERATOR, "=");
			currentIndex++;
			return true;
		} else if(c == '!') {
			try {
				if(data[++currentIndex] != '=') {
					throw new QueryLexerException("Invalid NOT_EQUALS operator");
				}
			} catch(IndexOutOfBoundsException e) {
				throw new QueryLexerException("Invalid NOT_EQUALS operator");
			}
			token = new Token(TokenType.OPERATOR, "!=");
			currentIndex++;
			return true;
		} else if(c == '<' || c == '>') {
			String oper = "" + data[currentIndex];
			try {
				if(data[currentIndex +1] == '=') {
					oper += "=";
					currentIndex++;
				}
			} catch(IndexOutOfBoundsException e) {
				throw new QueryLexerException("Query cannot end with "+ data[currentIndex]);
			}
			
			token = new Token(TokenType.OPERATOR, oper);
			currentIndex++;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Skips all whitespaces in text. 
	 */
	private void skipBlanks() {
		while(currentIndex < data.length) {
			char c = data[currentIndex];
			if(Character.isWhitespace(c)) {
				currentIndex++;
				continue;
			}
			break;
		}
	}
}
