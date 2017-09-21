package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Interface for a shell command used in {@link MyShell}. 
 * @author Ante
 *
 */
public interface ShellCommand {
	
	/**
	 * Executes this command. 
	 * @param env Environment used in program
	 * @param arguments command arguments
	 * @return {@link ShellStatus} 
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns name of this command.
	 * @return command name
	 */
	String getCommandName();
	
	/**
	 * Returns list containing command's description.
	 * @return
	 */
	List<String> getCommandDescription();
}
