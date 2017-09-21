package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StudentDatabaseTest {

	StudentDatabase database;
	
	@Before
	public void setUp() throws Exception {
		String data = loader("database.txt");
		String lines[] = data.split("\\r?\\n");
		database = new StudentDatabase(lines);
	}

	@Test
	public void testStudentDatabaseForJmbag() {
		StudentRecord[] records = new StudentRecord[] {
			new StudentRecord("0000000001", "Marin", "Akšamović", 2),
			new StudentRecord("0000000015", "Kristijan", "Glavinić Pecotić", 4)
		};
		
		
		assertEquals(records[0], database.forJMBAG("0000000001"));
		assertEquals(records[1], database.forJMBAG("0000000015"));
	}
	


	@Test
	public void testFilter() {
		List<StudentRecord> list1 = database.filter(rec -> false);
		List<StudentRecord> list2 = database.filter(rec -> true);
		
		assertEquals(0, list1.size());
		
		for(StudentRecord first : list2) {
			assertEquals(first, database.forJMBAG(first.getJmbag()));
		}
	}
	
	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			
			while(true) {
				int read = is.read(buffer); if(read<1) break; bos.write(buffer, 0, read);
			}
	
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
			
		} catch(IOException ex) {
			return null; 
		}
	}

}
