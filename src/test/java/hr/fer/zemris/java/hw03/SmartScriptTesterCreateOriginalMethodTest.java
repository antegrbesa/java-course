package hr.fer.zemris.java.hw03;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTesterCreateOriginalMethodTest {

	DocumentNode document;
	DocumentNode document2;
	
	@Before
	public void setUp() {
		String filepath = "examples\\doc2.txt";
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
		  System.exit(-1);
		} catch(Exception e) {
			e.printStackTrace(System.err);
		  System.out.println("If this line ever executes, you have failed this class!");
		  System.exit(-1);
		}
		document = parser.getDocumentNode();
		String original = SmartScriptTester.createOriginalDocumentBody(document);
		
		SmartScriptParser parser2 = new SmartScriptParser(original);
		document2 = parser2.getDocumentNode();
	}
	
	@Test
	public void testCreateOriginalDocumentBody() {
		assertEquals(document.numberOfChildren(), document2.numberOfChildren());
		assertEquals(document.getChild(1).numberOfChildren(), document2.getChild(1).numberOfChildren());
		assertEquals(document.getChild(3).numberOfChildren(), document2.getChild(3).numberOfChildren());
	}

}
