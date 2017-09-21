package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * The copy command expects two arguments: source file name and destination file name (i.e. paths and
names). If destination file exists, user is asked  is it allowed to overwrite it. Works only with files. 
 * @author Ante
 *
 */
public class CopyCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			Utility.checkForNull(env, arguments);
		} catch (IllegalArgumentException e) {
			env.writeln("An error occurred");
			return ShellStatus.CONTINUE;
		}
		

		DocumentPathParser parser = new DocumentPathParser(0);
		Path source;
		Path destination;
		try {
		source = parser.parseInputToken(arguments);
		DocumentPathParser parser2 = new DocumentPathParser(parser.getIndex()+1);
		destination = parser2.parseInputToken(arguments);
		} catch(IllegalArgumentException e) {
			env.write(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if (! source.toFile().exists() || source.toFile().isDirectory()) {
			env.writeln("Specified source file does not exist or is directory");
			return ShellStatus.CONTINUE;
		}
		
		if (destination.toFile().exists()) {
			env.writeln("Destination file exists. OK to overwrite it? (type yes or no)");
			String input = env.readLine();
			if(input.toLowerCase().equals("no")) {
				return ShellStatus.CONTINUE;
			} else if (! input.toLowerCase().equals("yes")) {
				env.writeln("Invalid answer");
				return ShellStatus.CONTINUE;
			}
		}
		
		copyFile(source.toFile(), destination.toFile(), env);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Copies specified file to a new one. 
	 * @param source source file
	 * @param destination destination file
	 * @param env Environment used
	 */
	private void copyFile(File source, File destination, Environment env) {
		if (destination.isDirectory()) {
			destination = new File(destination, source.getName());
		}
		
		try (	FileInputStream in = new FileInputStream(source);
				FileOutputStream out = new FileOutputStream(destination)
			) {
			byte[] buff = new byte[2048];
			int len;
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
			
			env.writeln("File was succesfully copied");
		} catch (IOException e) {
			env.writeln("An error occured during the copying of file " + source);
			env.writeln(e.getMessage());
		}
	}

	@Override
	public String getCommandName() {
		return new String("copy");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("The copy command expects two arguments: source file name and destination file name");
		list.add("This command works only with files.");
		list.add("If the second argument is directory, it is assumed that user wants");
		list.add("to copy the original file into that directory using the original file name.");
		
		return Collections.unmodifiableList(list);
	}

	
	
}
