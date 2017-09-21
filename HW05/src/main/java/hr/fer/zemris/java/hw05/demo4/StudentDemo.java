package hr.fer.zemris.java.hw05.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Program that prints out various combinations of {@link StudentRecord} generated from studenti.txt file 
 * and its values. Offers methods for creating various collections. 
 * @author Ante
 *
 */
public class StudentDemo {

	/**
	 * This method starts once the program has started.
	 * @param args not used
	 */
	public static void main(String[] args) {
		List<String> lines = new ArrayList<String>();
		try {
			 lines = Files.readAllLines(Paths.get("studenti.txt"));
		} catch (IOException e) {
			System.out.println("file not found");
			System.exit(1);
		}
		
		List<StudentRecord> records = getRecords(lines);
		
		System.out.println("Bodova vise od 25: " + vratiBodovaViseOd25(records));
		System.out.println();
		
		System.out.println("Bodova odlikasa: " + vratiBrojOdlikasa(records));
		System.out.println();
		
		System.out.println("Lista odlikasa: ");
		vratiListuOdlikasa(records).forEach(System.out::println);
		System.out.println();
		
		System.out.println("Lista odlikasa sortirano: ");
		vratiSortiranuListuOdlikasa(records).forEach(System.out::println);
		System.out.println();
		
		System.out.println("Popis nepolozenih: ");
		vratiPopisNepolozenih(records).forEach(System.out::println);
		System.out.println();
		
		
		
		System.out.println("Studenti po ocjenama: ");
		Map<Integer, List<StudentRecord>> map1 = razvrstajStudentePoOcjenama(records);
		for(Map.Entry<Integer, List<StudentRecord>> e : map1.entrySet()) {
			for(StudentRecord s : e.getValue()) {
				System.out.format("Ocjena: %d -> student: %s %s %s%n"
						, e.getKey(), s.getJmbag(), s.getPrezime(), s.getIme());
			}
		}
		System.out.println();
		
		System.out.println("Broj studenata po ocjenama: ");
		vratiBrojStudenataPoOcjenama(records).forEach((n, s) -> System.out.format("Ocjena %d: %d%n", n, s));
		System.out.println();
		
		System.out.println("Studenti po prolazu: ");
		Map<Boolean, List<StudentRecord>> map2 = razvrstajProlazPad(records);
		for(Map.Entry<Boolean, List<StudentRecord>> e : map2.entrySet()) {
			for(StudentRecord s : e.getValue()) {
				System.out.format("Prolaz: %b -> student %s %s %s %d%n", e.getKey()
						, s.getJmbag(), s.getPrezime(), s.getIme(), s.getOcjena());
			}
		}
		
		
		
	}
	
	/**
	 * Creates a list of Student records.
	 * @param lines lines of a text file
	 * @return list of StudentRecords
	 */
	private static List<StudentRecord> getRecords(List<String> lines) {
		List<StudentRecord> allRecords = new ArrayList<>();
		for(String line : lines) {
			String[] tokens = line.split("\\s+");
			try { 
			double bodMi = Double.parseDouble(tokens[3]);
			double bodZi =  Double.parseDouble(tokens[4]);
			double bodLab =  Double.parseDouble(tokens[5]);
			int ocjena = Integer.parseInt(tokens[6]);
			
			allRecords.add(new StudentRecord(tokens[0], tokens[1], tokens[2]
					, bodMi, bodZi, bodLab, ocjena));
			} catch(NumberFormatException e) {
				continue;
			} catch(ArrayIndexOutOfBoundsException e) {
				continue;	//line not properly formatted
			}
			
		}
		
		return allRecords;
	}
	
	/**
	 * Returns number of students with more than 25 points. 
	 * @param records student records
	 * @return number of students with more than 25 points. 
	 */
	public static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream().filter(s -> s.getBrojBodMI() + s.getBrojBodZI()
				+ s.getBrojBodLab() > 25).count();
	}
	/**
	 * Returns number of students with grade equal to 5. 
	 * @param records student records
	 * @return number of students with grade equal to 5. 
	 */
	public static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(s->s.getOcjena()==5).count();
	}
	/**
	 * Returns a collection of students with grade equal to 5
	 * @param records student records
	 * @return collection of students with grade equal to 5
	 */
	public static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(s->s.getOcjena()==5).collect(Collectors.toList());
	}
	
	/**
	 * Returns a sorted collection of students with grade equal to 5
	 * @param records student records
	 * @return collection of students with grade equal to 5
	 */
	public static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		
		odlikasi.sort(new Comparator<StudentRecord>() {
			@Override
			public int compare(StudentRecord o1, StudentRecord o2) {
				double first = o1.getBrojBodMI()+o1.getBrojBodLab()+o1.getBrojBodZI();
				double second = o2.getBrojBodMI()+o2.getBrojBodLab()+o2.getBrojBodZI();
				
				return Double.compare(second, first);
			};
		});
		
		return odlikasi;
	}
	
	/**
	 * Returns a collection of students with grade equal to 1
	 * @param records student records
	 * @return collection of students with grade equal to 1
	 */
	public static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream()
				.filter(s->s.getOcjena()==1)
				.map(s->s.getJmbag())
				.sorted((s1, s2) -> s1.compareTo(s2))
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns a map of students. Keys are grades and value is a list of students
	 * with that grade. 
	 * @param records student records
	 * @return map of students
	 */
	public static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		Map<Integer, List<StudentRecord>> map = records
				.stream()
				.collect(Collectors.groupingBy(StudentRecord::getOcjena));
		
		return map;
	}
	
	/**
	 * Returns a map of students. Keys are grades and value is number of students
	 * with that grade. 
	 * @param records student records
	 * @return map of students
	 */
	public static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		Map<Integer, Integer> map = records
				.stream()
				.collect(Collectors.toMap(StudentRecord::getOcjena, s -> 1, (s, a) -> s+a));
		
		return map;
	}
	
	/**
	 * Returns a map of students. Keys are boolean values that represent if a certain student has failed a class or not. Values
	 * are lists of students that satisfie specified condition (grade > 1). 
	 * @param records student records
	 * @return map of students
	 */
	public static  Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		Map<Boolean, List<StudentRecord>> map = records
				.stream()
				.collect(Collectors.partitioningBy(s-> s.getOcjena() > 1));
		
		return map;
	}

}
