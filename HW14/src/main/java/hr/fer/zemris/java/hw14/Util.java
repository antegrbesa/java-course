package hr.fer.zemris.java.hw14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Utility class that offers some useful methods for this application.
 * @author Ante
 *
 */
public class Util {

	/**Code for exception which is thrown when user tries to create existing table.*/ 
	protected static final String EXISTS_EXCEPTION_CODE = "X0Y32";

	/**
	 * Compares code of given exception with {@link Util#EXISTS_EXCEPTION_CODE}.
	 * If the codes are equal, table already exists and method returns true.
	 * @param e exception 
	 * @return true if a table already exists, false otherwise
	 */
	public static boolean tableAlreadyExists(SQLException e) {
		if (e.getSQLState().equals(EXISTS_EXCEPTION_CODE)) {
			return true;
		}

		return false;
	}

	/**
	 * Returns a map containing data generated from given documents.
	 * 
	 * @param fileNameRes
	 *            path of result text
	 * @param fileNameDef
	 *            path of definition text
	 * @return generated map
	 * @throws IOException
	 *             if an error occurred while reading file
	 */
	protected static Map<String, Integer> getResultingMap(String fileNameRes, String fileNameDef) throws IOException {
		List<String> linesRes = Files.readAllLines(Paths.get(fileNameRes));
		Map<String, Integer> votes = new TreeMap<>(Collections.reverseOrder());
		for (String line : linesRes) {
			String[] split = line.split("\\t");
			Integer value = Integer.parseInt(split[1]);
			votes.put(split[0], value);
		}
		votes = sortByValue(votes);

		List<String> linesDef = Files.readAllLines(Paths.get(fileNameDef));
		Map<String, String> values = new TreeMap<>();
		for (String line : linesDef) {
			String[] split = line.split("\\t");
			values.put(split[0], split[1]);
		}

		Map<String, Integer> resultingMap = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> e : votes.entrySet()) {
			String key = e.getKey();
			resultingMap.put(values.get(key), e.getValue());
		}

		return resultingMap;
	}

	/**
	 * Gets the values from given document. Document must be formatted as
	 * follows: {@code id name link}.
	 * 
	 * @param fileName
	 *            name of file to use
	 * @param i
	 *            first argument to extract
	 * @param j
	 *            second argument to extract
	 * @return generated map
	 * @throws IOException
	 *             if an exception occurred
	 */
	protected static Map<String, String> getValues(String fileName, int i, int j) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));

		Map<String, String> values = new TreeMap<>();
		for (String line : lines) {
			String[] split = line.split("\\t");
			values.put(split[i], split[j]);
		}

		return values;
	}

	/**
	 * Sorts a map by it's value.
	 * 
	 * @param map
	 *            map to sort
	 * @return sorted map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
}
