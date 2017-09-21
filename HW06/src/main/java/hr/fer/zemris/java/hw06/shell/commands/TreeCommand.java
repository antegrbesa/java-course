package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * The tree command expects a single argument: directory name and prints a tree
 * @author Ante
 *
 */
public class TreeCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			Utility.checkForNull(env, arguments);
		} catch (IllegalArgumentException e) {
			env.writeln("An error occurred");
			return ShellStatus.CONTINUE;
		}
		
		Path path = null;
		try {
			DocumentPathParser parser = new DocumentPathParser(0);
			path = parser.parseInputToken(arguments);
		} catch(IllegalArgumentException e) {
			env.write(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if(! path.toFile().exists()) {
			env.writeln("File not found");
			return ShellStatus.CONTINUE;
		}
		
		int indentation = 0;
		StringBuilder sb = new StringBuilder();
		try {
			printTree(path.toFile(), indentation, sb);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		env.write(sb.toString());
		
		return ShellStatus.CONTINUE;
	}

/**
 * Prints tree. 
 * @param file file to print
 * @param indentation current indentation
 * @param sb string builder
 */
	private void printTree(File file, int indentation, StringBuilder sb) {
		if (! file.isDirectory()) {
			throw new IllegalArgumentException("Given path is not a directory");
		}
		
		getIndentation(indentation, sb);
		String newLine = System.getProperty("line.separator");
		sb.append(file.getName());
		sb.append(newLine);
		
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				printTree(f, indentation + 1, sb);
			} else {
				getIndentation(indentation + 1, sb);
				sb.append(f.getName());
				sb.append(newLine);
			}
		}
	}

	/**
	 * Gets indentation. 
	 * @param indentation current indentation
	 * @param sb string builder
	 */
	private void getIndentation(int indentation, StringBuilder sb) {
		for (int i = 0; i < indentation; i++) {
			sb.append("  ");
		}
	}


	@Override
	public String getCommandName() {
		return new String("tree");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("The tree command expects a single argument: directory name and prints a tree");
		
		return Collections.unmodifiableList(list);
	}

}
