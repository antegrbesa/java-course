package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.*;


import org.junit.Test;





public class ScriptLexerTest {


	@Test
	public void testText() {
		String text = "This is sample text. {$=$}";
		ScriptLexer lex = new ScriptLexer(text);
		String s = (String) lex.nextToken().getValue();
		
		assertEquals("This is sample text. ", s);
		TokenType t = lex.nextToken().getType();
		assertEquals(TokenType.START_TAG, t);
		assertEquals(TokenType.ECHO, lex.nextToken().getType());
		assertEquals(TokenType.END_TAG, lex.nextToken().getType());
		TokenType t2 = lex.nextToken().getType();
		assertEquals(TokenType.EOF, t2);
		
	}
	
	
	@Test
	public void testEscape() {
		String text = "This is sample text. \\{$ bla bla {$";
		ScriptLexer lex = new ScriptLexer(text);
		String s = (String) lex.nextToken().getValue();
		
		assertEquals("This is sample text. \\{$ bla bla ", s);
		TokenType t = lex.nextToken().getType();
		assertEquals(TokenType.START_TAG, t);
	}
	
	@Test
	public void testTagInput() {
		String text = "This is sample text. \n {$ FOR i 1 10 1 $} \n This is {$ ";
		ScriptLexer lex = new ScriptLexer(text);
		String s = (String) lex.nextToken().getValue();
		
		assertEquals("This is sample text. \n ", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("{$", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("FOR", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("i", s);
		int n = (int) lex.nextToken().getValue();
		assertEquals(1, n);
		n = (int) lex.nextToken().getValue();
		assertEquals(10, n);
		n = (int) lex.nextToken().getValue();
		assertEquals(1, n);
		s = (String) lex.nextToken().getValue();
		assertEquals("$}", s);
		assertTrue(lex.getToken().getType() == TokenType.END_TAG);
		s = (String) lex.nextToken().getValue();
		assertEquals(" \n This is ",s);
	}
	
	
	@Test
	public void testTag() {
		String text = "{$= i i * @sin \"0.000\" @decfmt $}";
		
		Token correctData[] = {
				new Token(TokenType.START_TAG, "{$"),
				new Token(TokenType.ECHO, '='),
				new Token(TokenType.VARIABLE, "i"),
				new Token(TokenType.VARIABLE, "i"),
				new Token(TokenType.OPERATOR, "*"),
				new Token(TokenType.FUNCTION, "sin"),
				new Token(TokenType.STRING, "\"0.000\""),
				new Token(TokenType.FUNCTION, "decfmt"),
				new Token(TokenType.END_TAG, "$}"),
				new Token(TokenType.EOF, null)
			};
		
		ScriptLexer lexer = new ScriptLexer(text);
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testDoubleInTag() {
		String text = "This is sample text. \n {$ FOR i 1.12223 10.221 1 $} \n This is \\{$ ";
		ScriptLexer lex = new ScriptLexer(text);
		String s = (String) lex.nextToken().getValue();
		
		assertEquals("This is sample text. \n ", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("{$", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("FOR", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("i", s);
		double n = (double) lex.nextToken().getValue();
		assertEquals(1.12223, n, 1E-6);
		n = (double) lex.nextToken().getValue();
		assertEquals(10.221, n, 1E-6);
		int m = (int) lex.nextToken().getValue();
		assertEquals(1, m);
		Token next = lex.nextToken();
		assertEquals(TokenType.END_TAG, next.getType());
		s = (String) lex.nextToken().getValue();
		assertEquals(" \n This is \\{$ ", s);
	}
	
	@Test
	public void testNegativeConst() {
		String text =  "{$ FOR i -12 10 1 $} ";
		ScriptLexer lex = new ScriptLexer(text);

		
		String s = (String) lex.nextToken().getValue();
		assertEquals("{$", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("FOR", s);
		s = (String) lex.nextToken().getValue();
		assertEquals("i", s);
		int m = (int) lex.nextToken().getValue();
		assertEquals(-12, m);
	}
	
	
	
	// Helper method for checking if lexer generates the same stream of tokens
		// as the given stream.
		private void checkTokenStream(ScriptLexer lexer, Token[] correctData) {
			int counter = 0;
			for(Token expected : correctData) {
				Token actual = lexer.nextToken();
				String msg = "Checking token "+counter + ":";
				assertEquals(msg, expected.getType(), actual.getType());
				assertEquals(msg, expected.getValue(), actual.getValue());
				counter++;
			}
		}

}
