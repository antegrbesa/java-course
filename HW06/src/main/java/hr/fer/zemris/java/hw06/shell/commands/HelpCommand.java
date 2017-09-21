package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Help command. If started with no arguments, it must lists names of all supported commands.
 *	If started with single argument, it  prints name and the description of selected command
 * @author Ante
 *
 */
public class HelpCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Map<String, ShellCommand> commands = env.commands();
		arguments = arguments.trim();
		
		if (arguments == null || arguments.isEmpty()) {
			commands.forEach((k, v) -> System.out.println(k));
		} else {
			ShellCommand command = commands.get(arguments);
			if (command == null) {
				env.writeln("Command does not exist");
			} else {
				env.writeln(command.getCommandName());
				command.getCommandDescription().forEach(s -> env.writeln(s));
			}
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return new String("help");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("Prints all commands if no arguments present, otherwise prints");
		list.add(" name and description of selected command");
		
		return Collections.unmodifiableList(list);
	}

}
