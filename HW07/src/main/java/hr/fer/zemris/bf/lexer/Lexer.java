package hr.fer.zemris.bf.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A lexer of a boolean expression. A boolean expression consists of constants, variables, 
 * operators and brackets. This lexer produces instances of {@link Token} class (tokens).
 * <ul>
 * <li> Constants are either {@code true (1)}  or {@code false (0)}
 * <li> Variable can be any text that starts with a letter and contains only 
 * letters or digits
 * <li> Currently supported operators are: <i>and = {@code '*'}, or = {@code '+'},
 *  xor = {@code ':+:'}, not = {@code '!'}</i>.
 *  </ul>
 *  
 *  Generates a {@link Token} of type {@code EOF} if it reaches end of text. 
 *  
 * @author Ante
 *
 */
public class Lexer {
	
	/**List of supported identifiers*/
	private static List<String> identifiers;
	
	/**Map of supported operators*/
	private static Map<String, String> operators;
	
	static {
		identifiers = new ArrayList<>();
		identifiers.add("and");
		identifiers.add("xor");
		identifiers.add("or");
		identifiers.add("not");
	}
	
	static {
		operators = new HashMap<>();
		operators.put("*", "and");
		operators.put("+", "or");
		operators.put("!", "not");
		operators.put(":+:", "xor");
	}
	
	/** Input text*/
	private char[] data;
	
	/**Current token*/
	private Token token;
	
	/**Index of the first unprocessed character*/
	private int currentIndex;
	
	/**
	 * Constructs a lexer for specified text. 
	 * @param text input text
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new LexerException("Illegal argument for input text");
		}
		
		data = text.toCharArray();
		currentIndex = 0;
	}
	

	/**
	 * Generates and returns the next token in text. 
	 * @return next token
	 * @throws LexerException if an error occurred during generation
	 * of new token
	 */
	public Token nextToken() {
		getNextToken();
		return token;
	}

	/**
	 * Returns currently stored token.
	 * @return currently stored token
	 */
	public Token getToken() {
		if (token == null) {
			getNextToken();
		}
		
		return token;
	}
	
	/**
	 * Generates new token from input text. 
	 */
	private void getNextToken() {
		if (token != null && token.getTokenType() == TokenType.EOF) {
			throw new LexerException("End of file");
		}
		
		skipBlanks();
		
		if (currentIndex >= data.length) {
			token = new Token(null, TokenType.EOF);
			return;
		}
		
		if (Character.isLetter(data[currentIndex])) {
			getIdentifier();
		} else if (Character.isDigit(data[currentIndex])) {
			getNumeric();
		} else {
			getOther();
		}
	}
	
	
	/**
	 * Generates next token from input text that isn't an identifier
	 * or a constant. 
	 * @throws LexerException if an invalid sequence was detected 
	 */
	private void getOther() {
		if (data[currentIndex] == '(') {
			token = new Token('(', TokenType.OPEN_BRACKET);
			currentIndex++;
		} else if (data[currentIndex] == ')') {
			token = new Token(')', TokenType.CLOSED_BRACKET);
			currentIndex++;
		} else if (operators.containsKey(String.valueOf(data[currentIndex]))) {
			token = new Token(operators.get(String.valueOf(data[currentIndex]))
					, TokenType.OPERATOR);
			currentIndex++;
		} else {
			try {	//check if it's xor operator
				if (data[currentIndex] == ':' && data[currentIndex+1] == '+'
						&& data[currentIndex+2] == ':') {
					token = new Token("xor", TokenType.OPERATOR);
					currentIndex += 3;
				} else {
					throw new LexerException("Unsupported sequence beginning with "+data[currentIndex]);
				}
			} catch (IndexOutOfBoundsException e) {
				throw new LexerException("Invalid character, was "+data[currentIndex]);
			}
		}
		
	}

	/**
	 * Generates next token that is of type constant.
	 */
	private void getNumeric() {
		int start = currentIndex++;
		while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			currentIndex++;
		}
		
		String number = new String(data, start, currentIndex-start);
		
		if (number.equals("1")) {
			token = new Token(true, TokenType.CONSTANT);
		} else if (number.equals("0")) {
			token = new Token(false, TokenType.CONSTANT);
		} else {
			throw new LexerException("Unexpected number: "+number);
		}
	}

	/**
	 * Generates next token that is of type identifier or constant 
	 * given as text. 
	 */
	private void getIdentifier() {
		int start = currentIndex++;
		while (currentIndex < data.length && (Character.isLetterOrDigit(data[currentIndex])
				|| data[currentIndex] == '_')) {
			currentIndex++;
		}
		
		String word = new String(data, start, currentIndex-start);
		checkIdentifier(word);
	}

	/**
	 * Checks the token type of given argument. 
	 * @param word text to check 
	 */
	private void checkIdentifier(String word) {
		String lower = word.toLowerCase();
		if (identifiers.contains(lower)) {
			token = new Token(lower, TokenType.OPERATOR);
		} else if (lower.equals("true")) {
			token = new Token(true, TokenType.CONSTANT);
		} else if (lower.equals("false")) {
			token = new Token(false, TokenType.CONSTANT);
		} else {
			token = new Token(word.toUpperCase(), TokenType.VARIABLE);
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
