package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents a simple lexical analyzer. This analyzer has two modes with different sets of rules. First mode is 
 * a {@link LexerState.BASIC}. Rules for <code>BASIC</code> mode are as follows: 
 * 
 * <ul>
 * <li> A text is a sequence of words, numbers and symbols.
 * <li> Word is a sequence of one or more characters for which the method {@link #nCharacter.isLetter()} from {@link Character}
 * class returns true
 * <li> Number is a sequence of one or more digits that are in range of <code>Long</code>
 * and  for which the method{@link #nCharacter.isDigit()} from {@link Character} class returns true.
 * <li> Symbol is a single character that is left after all the words, blanks and numbers are removed
 * <li>Blanks are ignored.  
 * <li>A {@link Token} of type {@link TokenType.EOF} is generated as a last token in process, after
 * all other tokens were processed.
 * <li>If a \ character is before a digit, that digit is interpreted as a letter (so this digit can become a part of a word).
 * <li>If a \ character is written twice, it will represent a single \ symbol which is interpreted as a word.
 * <li>Everything else after \ that wasn't already defined is an invalid sequence.
 * </ul>
 * 
 * Second mode is {@link LexerState.EXTENDED} mode. Rules for this mode are as follows: 
 * 
 * <ul>
 * <li> Tokens are differentiated only by a blank space between them.
 * <li> There is no differentiation between letters, symbols or digits. 
 * </ul>
 * 
 * The two modes are triggered by # (hash) symbol. The user of this class must manually trigger different modes by
 * recognizing a special token (# symbol). The ocurrence of # token means that the user must change the mode using {@link #setState(LexerState)}
 * method. Initially, the lexer is in BASIC mode.
 * 
 * 
 * Entry point for this lexer is a text (or whatever is processed) and the output is a series of {@link Token}s.
 * This lexer is "lazy" which means that the extraction of the token is done only when it is explicitly called for using 
 * {@link #nextToken()} method. 
 *  
 * 
 * @author Ante Grbesa
 *
 */
public class Lexer {
	
	/** Input text*/
	private char[] data;
	
	/**Current token*/
	private Token token;
	
	/**Index of the first unprocessed character*/
	private int currentIndex;
	
	/**Current mode*/
	private LexerState state;
	
	/**
	 * Constructor which gets the text for processing as the argument. 
	 * @param text text for processing
	 */
	public Lexer(String text) {
		if(text == null) {
			throw new IllegalArgumentException("Input text is null.");
		}
		data = text.toCharArray();
		currentIndex = 0;
		state = LexerState.BASIC;
		
	}
	
	/**
	 *Sets the state of this lexer. 
	 * @param state state to set 
	 */
	public void setState(LexerState state) {
		if(state == null) {
			throw new IllegalArgumentException("Lexer state is null");
		}
		this.state = state;
	}

	/**
	 * Generates and returns the next token.
	 * @return next token in the series
	 * @throws LexerException if there are no more tokens left
	 * , if an invalid sequence was detected or if the token was a
	 * number out of range. 
	 */
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
	 *  @throws LexerException if there are no more tokens left
	 * , if an invalid sequence was detected or if the token was a
	 * number out of range. 
	 */
	private void getNextToken() {
		if(token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("End of text, no tokens available");
		}
		
		skipBlanks();		
		
		if(currentIndex >= data.length) {	//end of input
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		if(state == LexerState.EXTENDED) {	//lexer is in extended mode
			int start = currentIndex;
			if(data[start] == '#') {	//special "trigger" symbol for lexer
				token = new Token(TokenType.SYMBOL, '#');
				currentIndex++;
				return;
			}
			
			while(currentIndex < data.length && data[currentIndex] != ' ' && data[currentIndex] != '#') {
				currentIndex++;
			}
			String word = new String(data, start, currentIndex-start);
			token = new Token(TokenType.WORD, word);
			return;
		}	
		
		if(Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {	//token starts with a letter or with '\'
			int start = currentIndex;
			while(currentIndex < data.length && (Character.isLetter(data[currentIndex]) ||
					data[currentIndex] == '\\')) {
				
				if(data[currentIndex]=='\\') {	
					currentIndex++;
					try {
						if(data[currentIndex] == '\\' || Character.isDigit(data[currentIndex])) {	//check for a valid sequence	
							data[currentIndex-1] = ' ';	//replace first instance of \ with empty space										
							currentIndex++;	
						} else {
							throw new LexerException("Invalid escape sequence, was: "+
									data[currentIndex-1]+data[currentIndex]);
						}
					} catch(IndexOutOfBoundsException e) {	
						throw new LexerException("Invalid escape sequence, was: '\'");
					}
				} else
					currentIndex++;	//character was a letter, move index to next character
			}
			String word = new String(data, start, currentIndex-start);	//create new String with empty spaces instead of '\'
			word = word.replaceAll("\\s+", "");	//replace all empty spaces
			token = new Token(TokenType.WORD, word);
			return;
		}
		
		if(Character.isDigit(data[currentIndex])) {		//token is a number
			int start = currentIndex++;
			while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				currentIndex++;
			}
			int end = currentIndex;
			String word = new String(data, start, end-start);
			
			long number;
			try {
				number = Long.parseLong(word);
			} catch(NumberFormatException e) {
				throw new LexerException("Number is too big (max. is "+ Long.MAX_VALUE +", was "+word);
			}
			
			token = new Token(TokenType.NUMBER, number);
			return;
		}
			
		//token is a symbol
		token = new Token(TokenType.SYMBOL, data[currentIndex]);
		currentIndex++;		
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
}
