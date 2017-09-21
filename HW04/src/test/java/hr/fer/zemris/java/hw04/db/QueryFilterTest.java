package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.hw04.parser.QueryParser;

public class QueryFilterTest {

	StudentRecord[] records;
	
	@Before
	public void setUp() throws Exception {
		records = new StudentRecord[] {
				new StudentRecord("0000000001", "Marin", "Akšamović", 2),
				new StudentRecord("0000000015", "Kristijan", "Glavinić Pecotić", 4),
				new StudentRecord("0000000005", "Marina", "Aramović", 5),
				new StudentRecord("0000000014", "Kristina", "Glavinović", 3),
				new StudentRecord("0000000004", "Marija", "Aram", 5),
				new StudentRecord("0000000017", "Josip", "Glavin", 4)
		};
	}

	@Test
	public void test() {
		String text = "query jmbag>\"0000000003\" and lastName LIKE \"Ara*\"";
		QueryParser parser = new QueryParser(text);
		
		QueryFilter filter = new QueryFilter(parser.getQuery());
		assertTrue(filter.accepts(records[2]));
		assertTrue(filter.accepts(records[4]));
		assertFalse(filter.accepts(records[1]));
		assertFalse(filter.accepts(records[5]));
	}
	
	@Test
	public void testDirect() {
		String text = "query jmbag=\"0000000001\"";
		QueryParser parser = new QueryParser(text);
		
		QueryFilter filter = new QueryFilter(parser.getQuery());
		assertTrue(filter.accepts(records[0]));
		assertFalse(filter.accepts(records[1]));
		assertFalse(filter.accepts(records[5]));
	}


}
