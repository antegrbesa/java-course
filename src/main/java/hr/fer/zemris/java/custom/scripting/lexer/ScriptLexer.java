package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.hw03.prob1.LexerException;

/**
 * A lexer of a text that consists of tags (bounded by 
 * {$ and $}) and rest of the text. Each tag has it's name,
 * The name of {$ FOR... $} tag is  FOR, and the name of {$= ... $} tag is =. 
 * Tag names are case-insensitive. A one or more spaces (tabs, enters or spaces) 
 * can be included before tag name.
 * 
 *  = -tag is an empty tag – it has  no content so it does not need closing tag. 
 * FOR-tag, however, is not an empty tag. It has content and an accompanying END-tag 
 * must be present to close it.	
 * 
 * Please note that FOR tag can have three or four parameters, : first it must have one 
 * variable and after that two or three elements of type variable, number or string.
 * Entry point for this lexer is a text (or whatever is processed) and the output is a series of {@link Token}s.
 * This lexer is "lazy" which means that the extraction of the token is done only when it is explicitly called for using 
 * {@link #nextToken()} method.
 * @author Ante Grbesa
 *
 */
public class ScriptLexer {

	/** Input text*/
	private char[] data;
	
	/**Current token*/
	private Token token;
	
	/**Index of the first unprocessed character*/
	private int currentIndex;
	
	/**Current mode*/
	private ScriptLexerState state;
	
	/**
	 * Constructor which gets the text for processing as the argument. 
	 * @param text text for processing
	 */
	public ScriptLexer(String text) {
		if(text == null) {
			throw new IllegalArgumentException("Input text is null.");
		}
		data = text.toCharArray();
		currentIndex = 0;
		state = ScriptLexerState.TEXT;
		
	}
	
	/**
	 *Sets the state of this lexer. 
	 * @param state state to set 
	 */
	public void setState(ScriptLexerState state) {
		if(state == null) {
			throw new IllegalArgumentException("Lexer state is null");
		}
		this.state = state;
	}
	
	public Token nextToken() {
		getNextToken();
		return token;
	}
	
	/**
	 * Returns the current token.
	 * @return current token.
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Generates the next token out of given text.
	 *  @throws LexerException if an error occurs during tokenization 
	 */
	private void getNextToken() {
		if(token != null && token.getType() == TokenType.EOF) {
			throw new ScriptLexerException("End of text, no tokens available");
		}
		
		if(state == ScriptLexerState.TAG) {
			skipBlanks();
		}
		
		if(currentIndex >= data.length) {	//end of input
			if(state == ScriptLexerState.TAG) {
				throw new ScriptLexerException("Tag wasn't properly closed");	//text cannot end in tag mode
			}
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		//start tag
		if(data[currentIndex] == '{') {
			token = new Token(TokenType.START_TAG, "{$");
			currentIndex += 2;	//move index past $ symbol
			changeState();
			return;
		}
		
		
		if(data[currentIndex] == '$') {	//check if it's at tag end
			try {
				if(data[currentIndex+1] == '}') {
					token = new Token(TokenType.END_TAG, "$}");
					currentIndex += 2;
					changeState();
					return;
				}
			} catch(IndexOutOfBoundsException e) {
				//do nothing, symbol is a part of a text
			}
		}
		
		if(state == ScriptLexerState.TEXT) {
			int start = currentIndex;	//starting index of text
			boolean escape = false;	//escape flag
			while(currentIndex < data.length) {
				if(data[currentIndex] == '\\') {
					try {
						if(data[currentIndex+1] == '{' || data[currentIndex+1] == '\\') {
							currentIndex += 2;	//move index past escape symbol
							continue;
						}
						else {
							throw new ScriptLexerException("Invalid escape sequence, was \\"+data[currentIndex+1]);	//invalid escape sequence present in text
						}
					} catch(IndexOutOfBoundsException e) {
						throw new ScriptLexerException("Invalid escape sequence, was \\");
					}
				}
				if(data[currentIndex] == '{') {	//check for a start of a tag
					if(escape) {
						//escape sequence was present before $ symbol, process it as a text
						currentIndex++;
						escape=false;
						continue;
					} else {
						break;	//start of a tag, break processing of text
					}
				}
				
				currentIndex++;
			}
			
			String text = new String(data, start, currentIndex - start);
			if(text.endsWith("$")) {
				text = text.substring(0, text.length() -2);	//remove parenthesis from text
			}
			token = new Token(TokenType.TEXT, text);
			return;
		}
		
		//if a state isn't text, then it must be tag state
		//check if it's a = symbol
		if(data[currentIndex] == '=') {
			token = new Token(TokenType.ECHO, '=');
			currentIndex++;
			return;
		}
		
		//check if it's a variable
		if(Character.isLetter(data[currentIndex])) {
			int start = currentIndex++;
			while(currentIndex < data.length && (Character.isLetterOrDigit(data[currentIndex]) ||
				  data[currentIndex] == '_')) {
				currentIndex++;
			}
			
			String word = new String(data, start, currentIndex-start);
			token = new Token(TokenType.VARIABLE, word);
			return;
		}
		
		//check if it's a constant
		if(Character.isDigit(data[currentIndex])) {
			getNumber();
			return;
		}
		
		//check if it's a negative constant
		if(data[currentIndex] == '-') {
			try {
				if(Character.isDigit(data[currentIndex + 1])) {
					getNumber();
					return;
				}
			} catch(IndexOutOfBoundsException e) {
				throw new ScriptLexerException("A tag cannot end with '-"); //index out of bounds, tag was not
			}																//properly closed
		}
		
		//check if it's a function
		if(data[currentIndex] == '@') {
			int start = currentIndex;
			currentIndex++;
			try {
				if(! Character.isLetter(data[currentIndex])) {	//check for first letter in function name
					throw new SmartScriptParserException("Function name cannot start with "+ data[currentIndex]);
				}
			} catch (IndexOutOfBoundsException e) {
				throw new ScriptLexerException("Tag was not properly closed");
			}
				
			while(currentIndex < data.length && (Character.isLetterOrDigit(data[currentIndex]) ||
				  data[currentIndex] == '_')) {
				currentIndex++;
			}
			
			String word = new String(data, start, currentIndex-start);
			word = word.replace("@", "");
			token = new Token(TokenType.FUNCTION, word);
			
			return;
		}
		
		//check if it's an operator
		if(checkForOperator(data[currentIndex])) {
			currentIndex++;
			return;
		}
		
		//check if it's a string
		if(data[currentIndex] == '\\' || data[currentIndex] == '\"') {
			int start = currentIndex++;
			while(currentIndex < data.length) {
				if(data[currentIndex] == '\"' && data[currentIndex-1] != '\\') {	//check if it's end of string
					break;
				}
				if(data[currentIndex] == '\\') {
					try {
						if(data[currentIndex +1] != 'n' && data[currentIndex +1] != 't' && 
								data[currentIndex +1] != 'r') {	//check for an invalid escape sequence in string
							throw new ScriptLexerException("Invalid escape sequence in string");
						}
					} catch(IndexOutOfBoundsException e) {
							throw new ScriptLexerException("Tag was not properly closed");	//if end of text, tag was not properly closed
						}
				}
				currentIndex++;
			}
			
			currentIndex++;	//move past the last " symbol
			String word = new String(data, start, currentIndex-start);
			word = word.replace("\\", "");
			token = new Token(TokenType.STRING, word);
			
			return;
		}
		
		//everything valid was checked for, everything that remains is invalid 
		throw new ScriptLexerException("Input letter after "+token.getValue()+" is not valid.");
	}
	
	/**
	 * Method extracts a number (integer or floating point number).
	 */
	private void getNumber() {
		int start = currentIndex++;
		boolean doubleFlag = false;;
		while(currentIndex < data.length && (Character.isDigit(data[currentIndex]) 
				|| data[currentIndex] == '.' || data[currentIndex] == ',')) {
			
			if(data[currentIndex] ==  '.' || data[currentIndex] == ',') {
				if(doubleFlag) {	
					throw new ScriptLexerException("Invalid number, cannot have more than one . or ,");
				} else {
					doubleFlag = true;
				}
			}
			currentIndex++;
		}
		
		String word = new String(data, start, currentIndex-start);
		if(doubleFlag) {
			try {
				double numb = Double.parseDouble(word);
				token = new Token(TokenType.CONSTANT_DOUBLE, numb);
			} catch(NumberFormatException e) {
				throw new ScriptLexerException("Invalid floating point number decimal mark.");
			}
		} else {
			int numb = Integer.parseInt(word);
			token = new Token(TokenType.CONSTANT_INTEGER, numb);
		}
		
		return;
	}
	
	/**
	 * Extracts a mathematical operator if present.
	 * @param c character to check 
	 * @return true if given character is an operator, false otherwise
	 */
	private boolean checkForOperator(char c) {
		boolean result = false;
		
		switch (c) {	// '-' operator is missing because it was already checked for
		case '+':
			result = true;
			break;
		case '*':
			result =  true;
			break;
		case '/':
			result =  true;
			break;
		case '%':
			result =  true;
			break;
		case '^':
			result =  true;
			break;
		default:
			result = false;
			break;
		}

		if(result) {
			token = new Token(TokenType.OPERATOR, Character.toString(c));
		}
		
		return result;
		
		
	}
	
	/**
	 * Moves the current index to skip all the blanks, new lines or tabulators.
	 */
	private void skipBlanks() {
		while(currentIndex < data.length) {
			char c = data[currentIndex];
			if(c == '\n' || c== '\t' || c==' ' || c=='\r') {
				currentIndex++;
				continue;
			}
			break;
		}
	}
	
	/**
	 * Switches state of this lexer.
	 */
	private void changeState() {
		if(state == ScriptLexerState.TEXT) {
			setState(ScriptLexerState.TAG);
		} else {
			setState(ScriptLexerState.TEXT);
		}
	}

}
