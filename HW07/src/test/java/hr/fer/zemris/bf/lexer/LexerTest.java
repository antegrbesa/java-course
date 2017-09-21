package hr.fer.zemris.bf.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

public class LexerTest {

	@Test
	public void testBasic() {
		Lexer lexer = new Lexer("a or b");
		assertEquals("A", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
		assertEquals("or", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.OPERATOR, lexer.getToken().getTokenType());
		assertEquals("B", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
	}
	
	@Test
	public void testComplicated() {
		Lexer lexer = new Lexer("a xor b :+: c");
		assertEquals("A", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
		assertEquals("xor", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.OPERATOR, lexer.getToken().getTokenType());
		assertEquals("B", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
		assertEquals("xor", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.OPERATOR, lexer.getToken().getTokenType());
		assertEquals("C", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
	}
	
	@Test(expected = LexerException.class)
	public void testException() {
		Lexer lexer = new Lexer("a and 10");
		assertEquals("A", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
		assertEquals("and", lexer.nextToken().getTokenValue());
		assertEquals(TokenType.OPERATOR, lexer.getToken().getTokenType());
		lexer.nextToken();
	}
	
	
	
	
}


