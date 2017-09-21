package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Writes contents of given parsed tree ({@link DocumentNode}) generated by {@link SmartScriptParser}
 *  to console.
 * Expects a single command line argument, path to file. 
 * @author Ante Grbesa
 *
 */
public class TreeWriter {

	/**
	 * Main method.
	 * @param args command line args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("invalid args");
			System.exit(1);
		}
		
		Path path = Paths.get(args[0]);
		List<String> docBody;
		try {
			docBody = Files.readAllLines(path);
		} catch (IOException e) {
			System.out.println("Error with file");
			return;
		}
		StringBuilder sb = new StringBuilder();
		docBody.forEach((s) ->  { 
			sb.append(s);
			sb.append("\n");
		});
		SmartScriptParser parser = new SmartScriptParser(sb.toString());
		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}
	
	/**
	 * Implementation of a {@link INodeVisitor}. Prints every node visited. 
	 * @author Ante Grbesa
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		
		@Override
		public void visitTextNode(TextNode node) {
			System.out.printf(node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.printf(node.toString());
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.printf(node.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}	
	}
}