package hr.fer.zemris.java.app;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hr.fer.zemris.java.vectors.Vector;

/**
 * Simple console program (models simple shell) that simulates a search engine over collection of documents. 
 * It expects a single command-line argument, path to files for searching.
 * It has three commands:
 * <ol>
 * <li>{@code query} command expects at least one key words to use for document comparing. It returns a top 10 list of documents sorted by
 * similarity. </li>
 * <li>{@code results} prints latest results (if available) </li>
 * <li>{@code list x} prints content from document which is a result at index that must be given alongside this command</li>
 * <li>{@code exit} terminates application </li>
 * </ol>
 * @author Ante
 *
 */
public class Query {

	/**
	 * Path to file containing stop words.
	 */
	private static final String STOPWORDS_PATH = "./src/main/resources/hrvatski_stoprijeci.txt";
	
	/**
	 * Result files. 
	 */
	private static Map<Path, Double> resultFiles = new LinkedHashMap<>();
	
	/**
	 * idf vectors.
	 */
	private static Map<String, Double> idfVectors = new HashMap<>();
	
	/**
	 * Pattern used for parsing. 
	 */
	private static Pattern p = Pattern.compile("\\s+|\\d+|\\W+", Pattern.UNICODE_CHARACTER_CLASS);
	
	/**
	 * Method runs once the program has started.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid cmd arguments, terminating");
			return;
		}
		
		Map<String, Integer[]> vocabulary = new HashMap<>();
		Map<Path, Vector> freqVectors = new HashMap<>();
		
		Path path = Paths.get(args[0]);
		Visitor visitor = new Visitor(vocabulary, freqVectors, STOPWORDS_PATH);
		try {
			//create vocabulary
			Files.walkFileTree(path,visitor);	
			//create frequency vectors
			visitor.vocabularyAcquired = true;
			Files.walkFileTree(path, visitor);
		} catch (IOException e) {
			System.out.println("An error occurred while reading files.");
			return;
		}	
		processFreqVectors(visitor.vocabulary, visitor.freqVectors, visitor.numOfDocs);
		System.out.println("Velicina rjecnika je: " + vocabulary.size());

		Scanner sc = new Scanner(System.in);
		runShell(sc, freqVectors, vocabulary);
		
		System.out.println("Application is closing.");
		sc.close();
		
	}

	/**
	 * Runs the shell.
	 * @param sc scanner
	 * @param freqVectors frequency vectors map
	 * @param vocabulary vocabulary map
	 */
	private static void runShell(Scanner sc, Map<Path, Vector> freqVectors, Map<String, Integer[]> vocabulary) {
		while (true) {
			System.out.print("Enter command > ");
			String command = sc.nextLine().trim();
			
			if (command.startsWith("query")) {
				executeQuery(vocabulary, freqVectors, command);
				continue;
			} else if (command.startsWith("exit")) {
				break;
			} 
			
			if (resultFiles.isEmpty()) {	//rest of commands are invalid if result map is empty
				System.out.println("Invalid command");
				continue;
			}
			
			if (command.equals("results")) {
				printResults(resultFiles);
				continue;
			} else if (command.startsWith("type")) {
				String[] arguments = command.split("\\s+");
				if (arguments.length != 2) {
					System.out.println("Invalid command");
					continue;
				}
				int index;
				try {
					index = Integer.parseInt(arguments[1]);
				} catch (NumberFormatException e) {
					System.out.println("Invalid argument for command type");
					continue;
				}
				if (index < 0 || index > resultFiles.size()) {
					System.out.println("Invalid index");
					continue;
				} 
	
				@SuppressWarnings("unchecked")
				Map.Entry<Path, Double> entry =
						(Map.Entry<Path, Double>) resultFiles.entrySet().toArray()[index];
				System.out.println("Dokument: "+entry.getKey().toString());
				System.out.println();
				try {
					Files.readAllLines(entry.getKey()).forEach((l) -> { 
						System.out.println(l);
					});
				} catch (IOException e) {
					System.out.println("Error occurred while reading file.");
					break;
				}
			} else {
				System.out.println("Invalid command");
				continue;
			}

			
		}
		
	}

	/**
	 * Executes query command.
	 * @param vocabulary vocabulary to use
	 * @param freqVectors frequency vectors to use
	 * @param command command
	 */
	private static void executeQuery(Map<String, Integer[]> vocabulary, Map<Path, Vector> freqVectors, String command) {
		String[] inputs = p.split(command);
		
		Map<String, Double> validInput = new HashMap<>();
		for (int i = 0; i < inputs.length; i++) {
			if (i==0) continue;	//skip query
			if (vocabulary.containsKey(inputs[i].toLowerCase())) {
				String lowWord = inputs[i].toLowerCase();
				if (validInput.containsKey(lowWord)) {
					validInput.put(lowWord, validInput.get(lowWord)+1);
				} else {
					validInput.put(lowWord, 1.0);
				}
			}
		}
		
		System.out.print("Query is: [");
		StringBuilder sb = new StringBuilder();
		validInput.entrySet().forEach((e) -> { 
			sb.append(e.getKey());
			sb.append(", ");
		});
		if (sb.length() > 1) {
			sb.setLength(sb.length()-2);
		}
		System.out.println(sb + "]");
		
		calculateSimilarity(validInput, vocabulary, freqVectors);
		if (resultFiles.isEmpty()) {
			System.out.println("No results");
			return;
		}
		
		printResults(resultFiles);
	}

	/**
	 * Prints results to console.
	 * @param resultFiles2 results to print
	 */
	private static void printResults(Map<Path, Double> resultFiles2) {
		System.out.println();
		System.out.println("Najboljih 10 rezultata: ");
		int i = 0;
		for (Map.Entry<Path, Double> e : resultFiles.entrySet()) {
			if (i==10) break;
			if (e.getValue() < 1E-9) break;
			System.out.printf("[%d] (%.4f) %s %n", i, e.getValue(), e.getKey().toString());
			i++;
		}
		
	}

	/**
	 * Calculate similarity between given query and documents.
	 * @param validInput input document
	 * @param vocabulary vocabulary
	 * @param freqVectors frequency vectors
	 */
	private static void calculateSimilarity(Map<String, Double> validInput, Map<String, Integer[]> vocabulary,
			Map<Path, Vector> freqVectors) {
		if (validInput.isEmpty()) {
			System.out.println("Input contained no valid keywords");
			return;
		}
		resultFiles = new LinkedHashMap<>();
		Vector vector = new Vector(vocabulary.size());
		for (Map.Entry<String, Double> entry : validInput.entrySet()) {
			int index = vocabulary.get(entry.getKey())[0];
			double result = entry.getValue() * idfVectors.get(entry.getKey());
			vector.setValueAt(index, result);
		}
		
		for (Map.Entry<Path, Vector> e : freqVectors.entrySet()) {
			double result = vector.cosAngle(e.getValue());
			resultFiles.put(e.getKey(), result);
		}
		
		resultFiles = sortByValue(resultFiles);
	}

	/**
	 * Processes frequency vectors (calculates values).
	 * @param vocabulary vocabulary
	 * @param freqVectors freqVectors
	 * @param numOfDocs numOfDocs
	 */
	private static void processFreqVectors(Map<String, Integer[]> vocabulary
			, Map<Path, Vector> freqVectors, int numOfDocs) {
		for (Map.Entry<Path, Vector> entry : freqVectors.entrySet()) {
			Vector vector = entry.getValue();
			
			for (Map.Entry<String, Integer[]> word : vocabulary.entrySet()) {
				int index = word.getValue()[0];
				double nOfDocsWithWord = word.getValue()[1];
				double freqOfWordInDoc = vector.valueAt(index);
				double idf = Math.log(numOfDocs/nOfDocsWithWord);
				double value = freqOfWordInDoc * idf;
				vector.setValueAt(index, value);
				idfVectors.put(word.getKey(), idf);
			}
		}
		
	}

	/**
	 * Sorts a map by its value.
	 * 
	 * @param map  map to sort
	 * @return sorted map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	/**
	 * Implementation of a {@link FileVisitor}. Visits all documents and builds vocabulary
	 * and frequency vectors maps.
	 * @author Ante
	 *
	 */
	private static class Visitor implements FileVisitor<Path> {

		/**
		 * Vocabulary map. Keys are words, values are arrays(first value in array is index
		 * of words, second is number of documents where this word is present).
		 */
		private Map<String, Integer[]> vocabulary;
		
		/**
		 * Frequency vectors map. Keys are paths to files, values are vectors
		 * containig frequency of each word.
		 */
		private Map<Path, Vector> freqVectors;
		
		/**
		 * List containing all stop words.
		 */
		private List<String> stopWords;
		
		/**
		 * Total number of documents.
		 */
		private int numOfDocs = 0;
		
		/**
		 * Vocabulary counter (for indexes)
		 */
		private int vocCounter = 0;
		
		/**
		 * Flag denoting if vocabulary was already acquired.
		 */
		boolean vocabularyAcquired = false;
		
		/**
		 * Constructor.
		 * @param vocabulary vocabulary to set
		 * @param freqVectors freqVectors to set
		 * @param stopWordsFile path to file containign stop words
		 */
		public Visitor(Map<String, Integer[]> vocabulary,  Map<Path, Vector> freqVectors, String stopWordsFile) {
			this.vocabulary = vocabulary;
			this.freqVectors = freqVectors;
			try {
				stopWords = Files.readAllLines(Paths.get(stopWordsFile));
			} catch (IOException e) {
				throw new IllegalArgumentException("Given path was invalid");
			}
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			if (! Files.isReadable(file)) {
				return FileVisitResult.CONTINUE;
			}
						
			if (vocabularyAcquired) {
				createFreqVector(file);
			} else { 
				numOfDocs++;
				addToVocabulary(file);
			}

			
			return FileVisitResult.CONTINUE;
		}
		
		/**
		 * Fills frequency vectors map.
		 * @param file path to current file 
		 */
		private void createFreqVector(Path file) {
			Vector vector = new Vector(vocabulary.size());
			List<String> lines;
			try {
				lines = Files.readAllLines(file);
			} catch (IOException e) {
				return;
			}
			
			Set<String> words = new HashSet<>();
			//create frequency vector of words in single document -> tf(w,d)
			for (String line : lines) {
				if (line.isEmpty()) continue;
				String[] values = p.split(line);
				for (String value : values) { 
					if (value.isEmpty()) continue;
					String lowWord = value.toLowerCase().trim();
					
					Integer[] arr = vocabulary.get(lowWord);
					if (arr == null) continue;
					vector.incrementValueAt(arr[0]);
					if (words.add(lowWord)) {
						 arr[1] += 1;	//first time occurrence of this word in document
						vocabulary.put(lowWord, arr);	//increment number of documents for this word
					}
				}
			}
			
			freqVectors.put(file, vector);		
		}

		/**
		 * Fills vocabulary. 
		 * @param file current file 
		 */
		private void addToVocabulary(Path file) {
			List<String> lines;
			try {
				lines = Files.readAllLines(file);
			} catch (IOException e) {
				return;
			}
			
			for (String s : lines) {
				if (s.isEmpty()) continue;
				String[] values = p.split(s);
				for (String value : values) {
					if (value.isEmpty()) continue;
					String valueLow = value.toLowerCase().trim();			
					if (!stopWords.contains(valueLow) && !vocabulary.containsKey(valueLow)) {			
							vocabulary.put(valueLow, new Integer[] {vocCounter, 0});
							vocCounter++;
					}
				}
			}
			
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
	}
}
