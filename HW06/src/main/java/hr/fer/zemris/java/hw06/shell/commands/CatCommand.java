package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command cat takes one or two arguments. The first argument
 *  is path to some file and is mandatory. The second argument is charset name that is
 *   used to interpret chars from bytes. If not provided, a default platform charset is used
 * @author Ante Grbesa
 *
 */
public class CatCommand implements ShellCommand {
	/**Buffer size*/
	private static final int DEFAULT_BUFFER_SIZE = 4096;
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			Utility.checkForNull(env, arguments);
		} catch (IllegalArgumentException e) {
			env.writeln("An error occurred");
			return ShellStatus.CONTINUE;
		}
		
		String[] input = arguments.split("\\s+");
		if(input.length > 2 || input.length < 1) {
			env.writeln("Invalid number of arguments");
			return ShellStatus.CONTINUE;
		}
		DocumentPathParser parser = new DocumentPathParser(0);
		Path path;
		try {
		 path = parser.parseInputToken(arguments);
		} catch(IllegalArgumentException e) {
			env.write(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		if(! path.toFile().exists()) {
			env.writeln("File not found");
			return ShellStatus.CONTINUE;
		}
		
		DocumentPathParser parser2 = new DocumentPathParser(parser.getIndex()+1);
		Path secondArg = parser2.parseInputToken(arguments);
		String charset = secondArg == null ? Charset.defaultCharset().toString() : secondArg.toString();
		try (BufferedReader br = new BufferedReader(new InputStreamReader
					(new BufferedInputStream(new FileInputStream(path.toFile())), charset)))
				{
			while(true) {
				char[] buffer = new char[DEFAULT_BUFFER_SIZE];
				int read = br.read(buffer);
				if(read < 1) {
					break;
				}
				
				env.writeln(new String(buffer));
			}
		} catch (IllegalArgumentException | IOException e) {
			env.writeln("Invalid file or charset");
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return new String("cat");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("Command cat takes one or two arguments. The first argument is ");
		list.add("path to some file and is mandatory. The second argument is charset name ");
		list.add("that is  used to interpret chars from bytes. If not provided, ");
		list.add("a default platform charset is used");
		list.add("This command opens given file and writes its content to console.");
		
		return Collections.unmodifiableList(list);
	}

}
