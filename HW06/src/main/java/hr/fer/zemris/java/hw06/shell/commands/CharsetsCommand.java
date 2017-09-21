package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command charsets takes no arguments and lists names of supported charsets for your Java platform.
 * @author Ante
 *
 */
public class CharsetsCommand implements ShellCommand {

	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			Utility.checkForNull(env);
		} catch (IllegalArgumentException e) {
			env.writeln("An error occurred");
			return ShellStatus.CONTINUE;
		}
		Charset.availableCharsets().forEach((s, c) -> env.writeln(s));
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return new String("charsets");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("Command charsets takes no arguments and lists names ");
		list.add("of supported charsets for your Java platform");
		list.add("A single charset name is written per line.");
		
		return Collections.unmodifiableList(list);
	}

}
