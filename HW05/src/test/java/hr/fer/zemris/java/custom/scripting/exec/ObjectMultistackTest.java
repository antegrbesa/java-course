package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class ObjectMultistackTest {

	ObjectMultistack multistack;
	
	@Before
	public void setUp() {
		multistack = new ObjectMultistack();
	}

	@Test
	public void testPushAndPeek() {
		ValueWrapper year = new ValueWrapper(2000);
		multistack.push("year", year);
		ValueWrapper price = new ValueWrapper(200.51);
		multistack.push("price", price);
		assertEquals(2000, multistack.peek("year").getValue());
		assertEquals(200.51, multistack.peek("price").getValue());
		multistack.push("year", new ValueWrapper(1900));
		assertEquals(1900, multistack.peek("year").getValue());
		multistack.peek("year").setValue((Integer) multistack.peek("year").getValue()+50);
		assertEquals(1950, multistack.peek("year").getValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPushNull() {
		multistack.push("year", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPushKeyNull() {
		multistack.push(null, new ValueWrapper(21));
	}

	@Test
	public void testPop() {
		ValueWrapper year = new ValueWrapper(2000);
		multistack.push("year", year);
		ValueWrapper price = new ValueWrapper(200.51);
		multistack.push("price", price);
		multistack.push("year", new ValueWrapper(1900));
		
		multistack.pop("year");
		assertEquals(2000, multistack.peek("year").getValue());
		
		multistack.push("year", new ValueWrapper(1900));
		multistack.push("year", new ValueWrapper(1800));
		multistack.push("year", new ValueWrapper(1700));
		
		assertEquals(1700, multistack.peek("year").getValue());
		
		multistack.pop("year");
		multistack.pop("year");
		
		assertEquals(1900, multistack.peek("year").getValue());
	}

	@Test
	public void testIsEmpty() {
		ObjectMultistack multistack = new ObjectMultistack();
		assertTrue(multistack.isEmpty("year"));
		
		multistack.push("year", new ValueWrapper(1900));
		multistack.push("year", new ValueWrapper(1800));
		multistack.push("year", new ValueWrapper(1700));
		
		assertFalse(multistack.isEmpty("year"));
		multistack.pop("year");
		multistack.pop("year");
		multistack.pop("year");
		assertTrue(multistack.isEmpty("year"));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testEmptyStacl() {
		ObjectMultistack multistack = new ObjectMultistack();
		assertTrue(multistack.isEmpty("year"));
		
		multistack.push("year", new ValueWrapper(1900));
		multistack.push("year", new ValueWrapper(1800));
		multistack.push("year", new ValueWrapper(1700));
	
		multistack.pop("year");
		multistack.pop("year");
		multistack.pop("year");
		multistack.pop("year");
		
	}

	@Test
	public void testPopNull() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		assertEquals(null, multistack.peek("year"));
		assertEquals(null, multistack.pop("year"));
	}
	
	@Test
	public void testValueWrapper() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("year", new ValueWrapper(1940));
		multistack.peek("year").add(5.0);
		assertEquals(1945.0, multistack.peek("year").getValue());
		ValueWrapper price = new ValueWrapper(200.51);
		multistack.push("year", price);
		multistack.peek("year").add("5");
		assertEquals(205.51, multistack.peek("year").getValue());
		multistack.push("year", new ValueWrapper(1900));
		assertEquals(1900, multistack.peek("year").getValue());
	}

}
