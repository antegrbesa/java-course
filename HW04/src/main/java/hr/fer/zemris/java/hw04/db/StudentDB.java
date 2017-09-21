package hr.fer.zemris.java.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw04.parser.QueryParser;
import hr.fer.zemris.java.hw04.parser.QueryParserException;

/**
 * A simple database emulator. Rules for this emulator are given in {@link QueryParser}.
 * To simplify, this is a program for reading user inputs that are then parsed with {@link QueryParser}.
 * This program reads the student data from current directory from file database.txt.
 * Results from processed querys are printed on the console. 
 * For exiting, type <i>exit</i>.
 * 
 * @author Ante Grbesa
 * @see QueryParser
 *
 */
public class StudentDB {
	
	/**
	 * This method starts once the program has started.
	 * @param args not used
	 */
	public static void main(String[] args) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(
				    Paths.get(".\\database.txt"), //works only for Windows
				    StandardCharsets.UTF_8
				);
		} catch (IOException e) {
			System.out.println("File not found");
		}
		
		String[] allLines = new String[1];
		allLines = lines.toArray(allLines);
		
		StudentDatabase database = new StudentDatabase(allLines);
		
		
		Scanner sc = new Scanner(System.in);
		System.out.print("> ");
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(line.equals("exit")) {
				System.out.println("Goodbye!");
				sc.close();
				System.exit(0);
			}
			
			List<StudentRecord> viableRecords = new ArrayList<>();
			try {
			processLine(database, line, viableRecords);
			} catch(QueryParserException e) {
				System.out.println(e.getMessage());
			}
			
			System.out.print("> ");
		}
		
		sc.close();
		
	}
	
	/**
	 * Processes a single input line. 
	 * @param database student database
	 * @param line line to process
	 * @param viableRecords list for all records left after filtering
	 */
	private static void processLine(StudentDatabase database, String line, List<StudentRecord> viableRecords) {
		QueryParser parser = new QueryParser(line);
		
		boolean direct = false;
		int maxFirstName = 0;
		int maxLastName = 0;
		int maxJmbag = 0;
		if(parser.isDirectQuery()) {
			StudentRecord r = database.forJMBAG(parser.getQueriedJMBAG());
			viableRecords.add(r);
			maxFirstName = r.getFirstName().length();
			maxLastName = r.getLastName().length();
			maxJmbag = r.getJmbag().length();
			direct = true;
		} else {
			for(StudentRecord r : database.filter(new QueryFilter(parser.getQuery()))) {
				viableRecords.add(r);
				if(r.getFirstName().length() > maxFirstName) {
					maxFirstName = r.getFirstName().length();
				}
				if(r.getLastName().length() > maxLastName) {
					maxLastName = r.getLastName().length();
				}
				if(r.getJmbag().length() > maxJmbag) {
					maxJmbag = r.getJmbag().length();
				}
			}
		}
		
		printViables(viableRecords, maxFirstName, maxLastName, maxJmbag, direct);
	}
	
	/**
	 * Prints all filtered student records onto the console.
	 * @param viableRecords collection of filtered records
	 * @param maxFirstName maximal size of student's last name
	 * @param maxLastName maximal size of student's first name
	 * @param maxJmbag maximal jmbag size of a student
	 * @param direct flag if a direct query was entered
	 */
	private static void printViables(List<StudentRecord> viableRecords,
			int maxFirstName, int maxLastName, int maxJmbag, boolean direct) {
		if(viableRecords.size() == 0) {
			System.out.println("Records selected: 0");
			return;
		}
		
		printDelimiter(maxFirstName, maxLastName, maxJmbag);
		
		for(StudentRecord r : viableRecords) {
			StringBuilder sb = new StringBuilder();
			sb.append("| ");
			appendFull(r.getJmbag(), maxJmbag, sb);
			appendFull(r.getLastName(), maxLastName, sb);
			appendFull(r.getFirstName(), maxFirstName, sb);
			sb.append(r.getFinalGrade() + " |");
			System.out.println(sb.toString());
		}
		
		printDelimiter(maxFirstName, maxLastName, maxJmbag);
		System.out.println("Records selected: "+viableRecords.size());
	}
	
	/**
	 * Creates a single column with specified String value
	 * @param value value from a student
	 * @param max maximal size for value
	 * @param sb StringBuilder for the whole row
	 */
	private static void appendFull(String value, int max, StringBuilder sb) {
		sb.append(value);
		for(int i = value.length(); i < max+1; i++) {
			sb.append(" ");
		}
		sb.append("| ");
	}
	
	/**
	 * Prints a delimiter row containing = and +.
	 * @param maxFirstName maximal size of student's first name
	 * @param maxLastName maximal size of student's last name
	 * @param maxJmbag maximal size of student's jmbag
	 */
	private static void printDelimiter(int maxFirstName, int maxLastName, int maxJmbag) {
		System.out.print("+");
		for(int i = 0; i < maxJmbag+2; i++) {
			System.out.print("=");
		}
		
		System.out.print("+");
		for(int i = 0; i < maxLastName+2; i++) {
			System.out.print("=");
		}
		
		System.out.print("+");
		for(int i = 0; i < maxFirstName+2; i++) {
			System.out.print("=");
		}
		
		System.out.print("+");
		for(int i = 0; i < 3; i++) {
			System.out.print("=");
		}
		System.out.println("+");
		
	}

}
