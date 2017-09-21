package hr.fer.zemris.java.hw06.shell;


import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexDump;
import hr.fer.zemris.java.hw06.shell.commands.LsCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeCommand;

/**
 * A simple shell implementation. Program writes a prompt symbol and waits for the user to enter a command. The command can span
 *  across multiple lines. However, each line that is not the last line of command must end with a special symbol that is used to inform 
 *  the shell that more lines are expected. These are PROMPTSYMBOL and MORELINESSYMBOL. For each line that is part of multi-line command
 *   (except for the first one) a shell must write MULTILINESYMBOL at the beginning followed by a single whitespace.
 * @author Ante Grbesa
 *
 */
public class MyShell {
	/**Commands*/
	private static SortedMap<String, ShellCommand> commands;
	
	static {
		commands = new TreeMap<>();
		ShellCommand[] com = {
				new CharsetsCommand(),
				new CatCommand(),
				new LsCommand(),
				new TreeCommand(),
				new CopyCommand(),
				new MkdirCommand(),
				new HexDump(), 
				new SymbolCommand(),
				new ExitCommand(), 
				new HelpCommand()
		};
		for (ShellCommand c : com) {
			commands.put(c.getCommandName(), c);
		}
	}
	
	/**
	 * Runs once program has started
	 * @param args not used
	 */
	public static void main(String[] args) {
		Environment env = new EnvironmentImpl();
		
		System.out.println("Welcome to MyShell v 1.0");
		while (true) {
			env.write(env.getPromptSymbol().toString());
			String line = readLine(env);
			String commandName = getCommandName(line);
			String arguments = getArguments(line);
			ShellCommand command = commands.get(commandName);
			if (command == null) {
				env.writeln("Command "+command+" does not exist");
				continue;
			}
			
			ShellStatus status = command.executeCommand(env, arguments);
			if (status == ShellStatus.TERMINATE) {
				env.write("Goodbye");
				break;
			}
		}
	}
	
	/**
	 * Gets arguments for command. Currently only works for arguments containing 
	 * no spacing in a single argument. 
	 * @param line line to extract args from
	 * @return extracted arguments as single string
	 */
	private static String getArguments(String line) {
		line = line.trim();
		String[] elems = line.split("\\s+");
		if (elems.length == 0) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < elems.length; i++) {
			sb.append(elems[i] + " ");
		}
		
		return sb.toString();
	}

	/**
	 * Extracts command name from given line.
	 * @param line line which contains command
	 * @return extracted command
	 */
	private static String getCommandName(String line) {
		line = line.trim();
		String[] elems = line.split("\\s+");
		if (elems.length == 0) {
			return null;
		}
		return elems[0];
	}

	/**
	 * Generates a single line from one or more lines.
	 * @param env Environment used
	 * @return single line 
	 */
	private static String readLine(Environment env) {
		StringBuilder sb = new StringBuilder();
		
		String line = env.readLine();
		if (line.endsWith(env.getMorelinesSymbol().toString())) {
			while (line.endsWith(env.getMorelinesSymbol().toString())) {
				sb.append(line.substring(0, line.length()-2) +" ");
				env.write(env.getMultilineSymbol().toString());
				line = env.readLine();
			}
		} else {
			return line;
		}
		
		sb.append(line);
		return sb.toString();
	}

	/**
	 * Implementation of {@link Environment}. Uses console as input/output stream. 
	 * @author Ante
	 *
	 */
	private static class EnvironmentImpl implements Environment {

		private Scanner sc;
		
		private char multiLineSymbol;
		private char promptSymbol;
		private char moreLinesSymbol;
		
		public EnvironmentImpl() {
			sc = new Scanner(System.in);
			this.multiLineSymbol = '|';
			this.promptSymbol = '>';
			this.moreLinesSymbol = '\\';
		}
		
		@Override
		public String readLine() throws ShellIOException {
			return sc.nextLine();
		}

		@Override
		public void write(String text) throws ShellIOException {
			System.out.print(text);
			
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			System.out.println(text);
			
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		@Override
		public Character getMultilineSymbol() {
			return multiLineSymbol;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			this.multiLineSymbol = symbol;
			
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			this.promptSymbol = symbol;
			
		}

		@Override
		public Character getMorelinesSymbol() {
			return moreLinesSymbol;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			this.moreLinesSymbol = symbol;
			
		}
		
	}

}
