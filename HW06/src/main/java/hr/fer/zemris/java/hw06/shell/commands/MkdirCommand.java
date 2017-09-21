package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * The mkdir command takes a single argument: directory name, and creates the appropriate directory
structure.
 * @author Ante
 *
 */
public class MkdirCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			Utility.checkForNull(env, arguments);
		} catch (IllegalArgumentException e) {
			env.writeln("An error occurred");
			return ShellStatus.CONTINUE;
		}
		
		Path path; 
		try {
			DocumentPathParser parser = new DocumentPathParser(0);
			path = parser.parseInputToken(arguments);
			Files.createDirectories(path);
		} catch (IllegalArgumentException | IOException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Directories successfully created");
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return new String("mkdir");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("The mkdir command takes a single argument: directory name,");
		list.add(" and creates the appropriate directory structure");
		
		return Collections.unmodifiableList(list);
	}

}
