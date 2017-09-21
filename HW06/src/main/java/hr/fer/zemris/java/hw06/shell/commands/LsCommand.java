package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command ls takes a single argument – directory – and writes a directory listing.
 * @author Ante
 *
 */
public class LsCommand  implements ShellCommand{

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			Utility.checkForNull(env, arguments);
		} catch (IllegalArgumentException e) {
			env.writeln("An error occurred");
			return ShellStatus.CONTINUE;
		}
		
		Path path = null;
		try {
			DocumentPathParser parser = new DocumentPathParser(0);
			path = parser.parseInputToken(arguments);
		} catch(IllegalArgumentException e) {
			env.write(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		File[] files =  path.toFile().listFiles();
		if (files == null) {
			env.writeln("Given argument is not a directory");
		}
		
		for (File f : files) {
			StringBuilder sb = new StringBuilder();
			
			updateString((s)->f.isDirectory(), "d", f, sb);
			updateString((s)->f.canRead(), "r", f, sb);
			updateString((s)->f.canWrite(), "w", f, sb);
			updateString((s)->f.canExecute(), "x", f, sb);
			
			String formattedDateTime = "";
			try {
				formattedDateTime = getCreationDate(path);
			} catch (IOException e) {
				env.writeln(e.getMessage());
			}
			
			env.writeln(String.format("%s %10d %s %s", sb.toString(), f.length()
					, formattedDateTime, f.getName()));
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Creates String for given path representing creation date. 
	 * @param path to file
	 * @return string creation date of file
	 * @throws IOException if an io error occured
	 */
	private static String getCreationDate(Path path) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(
		path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
		);
		BasicFileAttributes attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		
		return formattedDateTime;
	}
	
	/**
	 * Updates values of columns. 
	 * @param predicate operation to perform
	 * @param column name of column
	 * @param f file
	 * @param sb string builder
	 */
	private static void updateString(Predicate<File> predicate, String column
			, File f, StringBuilder sb) {
		if (predicate.test(f)) {
			sb.append(column);
		} else {
			sb.append("-");
		}
	}

	@Override
	public String getCommandName() {
		return new String("ls");
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> list = new ArrayList<>();
		list.add("Command ls takes a single argument – directory – ");
		list.add("and writes a directory listing. ");
		list.add("The output consists of 4 columns. First column indicates");
		list.add("if current object is directory (d), readable (r), writable (w)");
		list.add("and executable (x). Second column contains object size in bytes.");
		list.add("Follows file creation date/time and finally file name. ");
		
		return Collections.unmodifiableList(list);
	}

}
