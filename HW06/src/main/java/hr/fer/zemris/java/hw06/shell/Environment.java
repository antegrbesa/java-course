package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * This is an abstraction which will be passed to each defined {@link ShellCommand}.
 * @author Ante
 *
 */
public interface Environment {
	
	/**
	 * Read line from input.
	 * @return line that was read
	 * @throws ShellIOException if stream was closed
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Writes to desired output.
	 * @param text text to write
	 * @throws ShellIOException if stream was closed
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Writes a line to desired output.
	 * @param text text text to write
	 * @throws ShellIOException if stream was closed
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Returns sorted map that contains {@link ShellCommand} references.
	 * @return  map that contains {@link ShellCommand} references.
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns multiline symbol currenty used. 
	 * @return
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets multiline symbol currenty used. 
	 * @param symbol
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 *   Returns prompt symbol currenty used. 
	 * @return
	 */
	Character getPromptSymbol();
	
	 /**
	  *  Sets multiline symbol currenty used. 
	  * @param symbol
	  */
	void setPromptSymbol(Character symbol);
	
	/**
	 *  Returns morelines symbol currenty used. 
	 * @return
	 */
	Character getMorelinesSymbol();
	
	void setMorelinesSymbol(Character symbol);
}
