package hr.fer.zemris.bf.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;

public class ParserTest {

	@Test
	public void testOnlyValue() {
		Parser parser = new Parser("0");
		ConstantNode node =  (ConstantNode) parser.getExpression();
		assertEquals(false, node.getValue());
	}
	
	@Test
	public void testSimpleExpression() {
		Parser parser = new Parser("a xoR b");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		assertEquals("xor", node.getName());
	}
	
	@Test
	public void testComplicatedExpression1() {
		Parser parser = new Parser("a xor b or c xor d");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		assertEquals("or", node.getName());
		assertTrue(node.getChildren().size() == 2);
	}
	
	@Test
	public void testComplicatedExpression2() {
		Parser parser = new Parser("(a + b) xor (c or d)");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		assertEquals("xor", node.getName());
		assertTrue(node.getChildren().size() == 2);
	}
	
	@Test
	public void testComplicatedExpression3() {
		Parser parser = new Parser("(d or b) xor not (a or c))");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		assertEquals("xor", node.getName());
		assertTrue(node.getChildren().size() == 2);
	}
	
	@Test(expected = ParserException.class)
	public void testInvalidOperator() {
		Parser parser = new Parser("(d or b) mor not (a or c))");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		//assertEquals("xor", node.getName());
	}
	
	@Test(expected = ParserException.class)
	public void testInvalidBracket() {
		Parser parser = new Parser("(d or b) xor not (a or c))");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		//assertEquals("xor", node.getName());
	}
	
	@Test(expected = ParserException.class)
	public void testInvalidNot() {
		Parser parser = new Parser("not a not b");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		//assertEquals("xor", node.getName());
	}
	
	@Test(expected = ParserException.class)
	public void testBracketNotClosed() {
		Parser parser = new Parser("a and (b or");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		//assertEquals("xor", node.getName());
	}
	
	@Test(expected = ParserException.class)
	public void testBracketNotClosed2() {
		Parser parser = new Parser("a and (b or c");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		//assertEquals("xor", node.getName());
	}
	
	@Test(expected = ParserException.class)
	public void testLexerException() {
		Parser parser = new Parser("a and 10");
		BinaryOperatorNode node =  (BinaryOperatorNode) parser.getExpression();
		//assertEquals("xor", node.getName());
	}
	

	

}
