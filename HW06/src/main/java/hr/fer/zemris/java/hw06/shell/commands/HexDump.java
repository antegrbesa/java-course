package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * The hexdump command expects a single argument: file name, and produces hex-output
 * @author Ante
 *
 */
public class HexDump implements ShellCommand {

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
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if (! path.toFile().exists()) {
			env.writeln("File not found");
			return ShellStatus.CONTINUE;
		}
		printHex(path, env);
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints hex value to environment. 
	 * @param path path of file
	 * @param env environment used
	 */
	private void printHex(Path path, Environment env) {
		if (path.toFile().isDirectory()) {
			env.writeln("Given argument is not a file");
			return;
		}
		
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {
			char[] buff = new char[16];
			int len;
			int n = 0;
			while ((len = br.read(buff)) > 0) {
				env.write(String.format("%08X :", (byte) n));
				for (int i = 0; i <= len-1; i++) {
					byte b = (byte) buff[i];
					env.write(new String(" "+String.format("%02X ", b)+" "));
					if (i == 8) {
						env.write("|");
					}
				}
				
				env.writeln(new String(" | "+String.valueOf(buff, 0, len)));
				n += 16;
			}
		} catch (IOException e) {
			env.writeln(e.getMessage());
		}	
	}

	@Override
	public String getCommandName() {
		return new String("hexdump");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("hexdump command expects a single argument: file name, and produces hex-output");
		
		return Collections.unmodifiableList(list);
	}

}
