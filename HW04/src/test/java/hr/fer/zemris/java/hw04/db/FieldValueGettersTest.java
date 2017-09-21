package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldValueGettersTest {

	@Test
	public void testGetters() {
		StudentRecord record = new StudentRecord("0036412331", "Ivan", "Ivić", 5);
		assertEquals("Ivan",  FieldValueGetters.FIRST_NAME.get(record));
		assertEquals("Ivić", FieldValueGetters.LAST_NAME.get(record));
		assertEquals("0036412331", FieldValueGetters.JMBAG.get(record));
	}

}
