package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * command for setting symbols for MyShell.
 * @author Ante
 *
 */
public class SymbolCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = arguments.split("\\s+");
		
		if (args.length < 1 || args.length > 2) {
			env.writeln("Invalid number of arguments for symbol");
			return ShellStatus.CONTINUE;
		}
		
		if (setOrPrintSymbol(args, new String("PROMPT"), env
				, c -> env.setPromptSymbol(c), (e) -> e.getPromptSymbol())) {}
		else if (setOrPrintSymbol(args, new String("MORELINES"), env
				, c -> env.setMorelinesSymbol(c), (e) -> e.getMorelinesSymbol())) {} 
		else if (setOrPrintSymbol(args, new String("MULTILINE"), env
				, c -> env.setMultilineSymbol(c), (e) -> e.getMultilineSymbol())) {}
		else {
			env.writeln("Invalid value for symbol");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Sets or prints symbol depending on input.
	 * @param args  user given arguments
	 * @param name name of symbol
	 * @param env environement used
	 * @param cons consumer to perform
	 * @param funcOld function to get old symbol
	 * @return true if current input is equal to name
	 */
	private boolean setOrPrintSymbol(String[] args, String name, Environment env
			, Consumer<Character> cons, Function<Environment, Character> funcOld) {
		
		char old = funcOld.apply(env);
		if (args[0].equals(name)) {	
			if (args.length == 2) {
				if (args[1].length() == 1) {
					char c = args[1].toCharArray()[0];
					cons.accept(c);
					env.writeln(String.format("Symbol for %s changed from '%c' to '%c'", args[0], old, c));
					
				} else {
					env.writeln("Expected a single character");
				}
			} else {
				env.writeln("Symbol for "+args[0]+" is "+old);
			}
		} else {
			return false;
		}
		
		return true;
	}

	@Override
	public String getCommandName() {
		return new String("symbol");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("Gets or sets aprropriate symbol");
		
		return Collections.unmodifiableList(list);
	}

}
