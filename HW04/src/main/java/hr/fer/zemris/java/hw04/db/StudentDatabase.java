package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

/**
 * Represents a simple database of {@link StudentRecord}.
 * 
 * @author Ante Grbesa
 *
 */
public class StudentDatabase {
	
	/**Collection for storing student records*/
	private SimpleHashtable<String, StudentRecord> students;
	
	/**
	 * Constructs an instance of this class with the argument being 
	 * an array of strings; each string represents one row of the database file.
	 * Additionally, it creates an internal list of student records.	
	 * @param lines array of strings; each string represents one row of the database file
	 * @throws IllegalArgumentException if given argument is null
	 */
	public StudentDatabase(String[] lines) {
		if(lines == null) {
			throw new IllegalArgumentException();
		}
		
		students = new SimpleHashtable<>();
		createRecords(lines);
	}
	
	/**
	 * Creates instances of StudentRecord class from given array of strings representing
	 * one row of the text file. 
	 * @param lines array of strings; each string represents one row of the database file
	 */
	private void createRecords(String[] lines) {
		for(String tmp : lines) {
			String[] records = tmp.split("\\s+");
			if(records.length < 4 || records.length > 5) {
				continue;	//line not properly formatted
			}
			
			String jmbag = records[0];
			String lastName = records[1];
			int index = 2;
			if(records.length == 5) {
				lastName += " " + records[index++]; //last name has two parts		
			}
			String firstName = records[index++];
			short mark = 0;
			try {
				mark = Short.parseShort(records[index]);
			} catch(NumberFormatException e) {
				continue;
			}
			
			students.put(jmbag, new StudentRecord(jmbag, firstName, lastName, mark));
		}
	}
	/**
	 * Uses index to obtain requested record in O(1); if record does not exists, 
	 * the method returns null.
	 * @param jmbag jmbag value of a {@link StudentRecord} to retrieve.
	 * @return requested record, if record does not exists, 
	 * the method returns null.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return students.get(jmbag);
	}
	
	/**
	 * Accepts a reference to an object which is an instance of {@link IFilter} interface and
	 * loops through all student records in its internal list; it calls <i>accepts</i> method on given
	 * filter-object with current record; each record for which <i>accepts</i> returns true is added to
	 * temporary list and this list is then returned by the filter method.
	 * @param filter reference to an object which is an instance of {@link IFilter}
	 * @return list containing all filtered {@link StudentRecord}
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> list = new ArrayList<>();
		for(SimpleHashtable.TableEntry<String, StudentRecord> entry : students) {
			StudentRecord value = entry.getValue();
			if(filter.accepts(value)) {
				list.add(value);
			}
		}
		
		return list;
	}
}
