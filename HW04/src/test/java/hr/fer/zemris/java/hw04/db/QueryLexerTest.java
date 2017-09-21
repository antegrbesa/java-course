package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.hw04.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.lexer.QueryLexerException;

public class QueryLexerTest {

	@Test
	public void testQueryLexer() {
		String text = "firstName>\"A\" and lastName LIKE \"B*ć\"";
		QueryLexer lex = new QueryLexer(text);
		
		assertEquals("firstName", lex.nextToken().getValue());
		assertEquals(">", lex.nextToken().getValue());
		assertEquals("A", lex.nextToken().getValue());
		assertEquals("and", lex.nextToken().getValue());
		assertEquals("lastName", lex.nextToken().getValue());
		assertEquals("LIKE", lex.nextToken().getValue());
		assertEquals("B*ć", lex.nextToken().getValue());
	}

	@Test
	public void testQueryLexer2() {
		String text = "\"A\" and firstName=\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"";
		QueryLexer lex = new QueryLexer(text);
		
		assertEquals("A", lex.nextToken().getValue());
		assertEquals("and", lex.nextToken().getValue());
		assertEquals("firstName", lex.nextToken().getValue());
		assertEquals("=", lex.nextToken().getValue());
		assertEquals("C", lex.nextToken().getValue());
		assertEquals("and", lex.nextToken().getValue());
		assertEquals("lastName", lex.nextToken().getValue());
		assertEquals("LIKE", lex.nextToken().getValue());
		assertEquals("B*ć", lex.nextToken().getValue());
		assertEquals("and", lex.nextToken().getValue());
		assertEquals("jmbag", lex.nextToken().getValue());
		assertEquals(">", lex.nextToken().getValue());
		assertEquals("0000000002", lex.nextToken().getValue());
	}

	@Test(expected = QueryLexerException.class)
	public void testInvalidNotEquals() {
		String text = "firstName!\"A\" and lastName LIKE \"B*ć\"";
		QueryLexer lex = new QueryLexer(text);
		lex.nextToken();
		lex.nextToken();
	}
	
	@Test(expected = QueryLexerException.class)
	public void testInvalidIdentifier() {
		String text = "firstName!=\"A\" hgh lastName LIKE \"B*ć\"";
		QueryLexer lex = new QueryLexer(text);
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
	}
	
	@Test(expected = QueryLexerException.class)
	public void testEOF() {
		String text = "firstName!=\"A\"";
		QueryLexer lex = new QueryLexer(text);
		lex.nextToken();
		lex.nextToken();
		lex.nextToken();
		assertEquals(TokenType.EOF, lex.nextToken().getType());
		lex.nextToken();
	}	
}
