package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper class which offers methods for parsing string argument to Path.
 * @author Ante
 *
 */
public class DocumentPathParser {
	/**Current index*/
	private int index;
	/**
	 * Constuctor for this class
	 * @param index current index
	 */
	public DocumentPathParser(int index) {
		this.index = index;
	}
	
	/**
	 * Getter for index
	 * @return index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Parses a whole input token from index.
	 * @param text text to parse
	 * @return argument as Path
	 */
	public  Path parseInputToken(String text) {
		if (text.length() == 0) {
			throw new IllegalArgumentException("Input was empty");
		}
		
		char[] data = text.toCharArray();
		
		if (index >= data.length-1) {
			return null;
		}
		
		index = skipBlanks(data, index);
		
		
		
		Path path = null;
		if (data[index] == '\"') {
			path = parseStringToPath( data);
		} else {
			path = parseToPath(data);
		}
		
		return path;
	}
	
	/**
	 * Parses given array to Path.
	 * @param data data to parse
	 * @return path form of given array 
	 */
	private  Path parseToPath(char[] data) {	
		int start = index;
		while(index < data.length) {
			if(Character.isWhitespace(data[index])) {
				break;
			}
			index++;
		}
		
		String word = new String(data, start, index-start);
		return Paths.get(word);
	}

	/**
	 * Moves the current index to skip all the blanks, new lines or tabulators.
	 * @return moved index
	 * @param data data to parse
	 * @param currentIndex index
	 */
	private  int skipBlanks(char[] data, int currentIndex) {
		while(currentIndex < data.length) {
			char c = data[currentIndex];
			if(Character.isWhitespace(c)) {
				currentIndex++;
				continue;
			}
			break;
		}
		
		return currentIndex;
	}
	
	/**
	 * Parses given array (represents string) to path. 
	 * @param data data to parse 
	 * @return parsed Path
	 */
	private  Path parseStringToPath( char[] data) {
		int start = index;
		if (data[index] == '\"') {
			index++;
			
			while(index < data.length) {
				if(data[index] == '\"' && data[index-1] != '\\') {
					break;
				}
				
				index++;
			}
			
			if (index == start+1 || index == data.length) {
				throw new IllegalArgumentException("Input not closed properly");
			}
			
			String word = new String(data, 1, index-start-1).replace("\\\\", "\\");
			return Paths.get(word);
		} else {
			throw new IllegalArgumentException("Invalid start of input, was "+data[0]);
		}
		
	}
}
