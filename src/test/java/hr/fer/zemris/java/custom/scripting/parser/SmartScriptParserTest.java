package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.lang.model.util.Elements;

import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.lexer.ScriptLexer;
import hr.fer.zemris.java.custom.scripting.nodes.*;

public class SmartScriptParserTest {

	@Test
	public void testSmartScriptParserBasicExample() {
		String filepath = "src\\test\\resources\\doc1.txt";
		String docBody = "";
		try {
			docBody = new String(
			        Files.readAllBytes(Paths.get(filepath)), 
			        StandardCharsets.UTF_8
					);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		SmartScriptParser parser = null;
		try {
		  parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
		  System.out.println("Unable to parse document!" + e.getMessage());
		}
		DocumentNode document = parser.getDocumentNode();
		assertEquals(6, document.numberOfChildren());
		assertTrue(document.getChild(0) instanceof TextNode);
		assertTrue(document.getChild(1) instanceof ForLoopNode);
		assertTrue(document.getChild(2) instanceof TextNode);
		assertTrue(document.getChild(3) instanceof ForLoopNode);
		assertTrue(document.getChild(4) instanceof TextNode);
		assertTrue(document.getChild(5) instanceof EchoNode);
		
		//testing for loop elements
		ForLoopNode node = (ForLoopNode) document.getChild(1);
		ElementConstantInteger start = (ElementConstantInteger) node.getStartExpression();
		assertEquals(1, start.getValue());
		assertEquals("i", node.getVariable().asText());
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testInvalidEscapeSeqInString() {
		String doc = "{$= i i * @sin  \"\\s0.000\" @decfmt $}";
		SmartScriptParser parser = new SmartScriptParser(doc);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testInvalidEscapeSeqInText() {
		String text = "This is invalid \\s";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testForWithoutEnd() {
		String text = "{$ FOR i 1 10 1 $}\n" +
				"This is {$= i $}-th time this";
		
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testInvalidTagSign() {
		String text = "{& FOR i 1 10 1 $}\n" +
				"This is {$= i $}-th time this";
		
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testEndWithoutFor() {
		String text = "{$END$}";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testTooManyEnds() {
		String text = "{$ FOR i 1 10 1 $}\n" +
				"This is {$= i $}-th time this" +
				"{$END$} {$END$}";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testInvalidVariableNameInFor() {
		String text = "{$ FOR _i 1 10 1 $}\n" +
				"This is {$= i $}-th time this" +
				"{$END$}";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testForTooManyArguments() {
		String text = "{$ FOR i 1 10 1 1 $}\n" +
				"This is {$= i $}-th time this" +
				"{$END$}}";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testForTooFewArguments() {
		String text = "{$ FOR i 1  $}\n" +
				"This is {$= i $}-th time this" +
				"{$END$}}";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testForWithFunctionException() {
		String text = "{$ FOR i 1 @sin  $}\n" +
				"This is {$= i $}-th time this" +
				"{$END$}}";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test
	public void testOnlyEcho() {
		String text = "{$= i i * @sin \"0.000\" @decfmt $}";
		SmartScriptParser parser = new SmartScriptParser(text);
		
		EchoNode node = (EchoNode) parser.getDocumentNode().getChild(0);
		Element[] el = node.getElements();
		assertEquals("i", el[0].asText());
		assertEquals("*", el[2].asText());
		assertEquals("sin", el[3].asText());
		assertEquals("\"0.000\"", el[4].asText());
		assertEquals("decfmt", el[5].asText());
		
	}
	
	@Test
	public void testWithoutBracketsTag() {
		String text =  "$ FOR i -12 10 1 $} ";
		SmartScriptParser parser = new SmartScriptParser(text);

		TextNode node = (TextNode) parser.getDocumentNode().getChild(0);
		String s = node.getText();
		assertEquals("$ FOR i -12 10 1 $} ", s);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testForWithoutBracket() {
		String text =  "{$ FOR i -12 10 1 $";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testEchoWithoutFinalBrackett() {
		String text =  "{$= i -12 10 1 $";
		SmartScriptParser parser = new SmartScriptParser(text);
	}
	
	
	
	

}
