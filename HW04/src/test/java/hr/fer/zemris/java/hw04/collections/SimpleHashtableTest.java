package hr.fer.zemris.java.hw04.collections;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleHashtableTest {

	SimpleHashtable<String, Integer> table;
	
	@Before
	public void setUp() throws Exception {
		table = new SimpleHashtable<>(2);
		
		table.put("Ivana", 2);
		table.put("Ante", 2);
		table.put("Jasna", 2);
		table.put("Kristina", 5);
		table.put("Ivana", 5);
		
		
	}

	@Test
	public void testFirstSuperiorPower() {
		assertEquals(32, SimpleHashtable.getFirstSuperiorPower(30));
		assertEquals(64, SimpleHashtable.getFirstSuperiorPower(60));
	}
	
	@Test
	public void testPut() {
        assertEquals(4,table.size()); 
	}

	@Test
	public void testGet() {
		assertEquals(5, (int) table.get("Ivana"));
		assertEquals(5, (int) table.get("Kristina"));
	}

	@Test
	public void testContainsKey() {
		assertTrue(table.containsKey("Ivana"));
		assertFalse(table.containsKey("Marija"));
	}

	@Test
	public void testContainsValue() {
		assertTrue(table.containsValue(5));
		assertFalse(table.containsValue(1));
	}

	@Test
	public void testRemove() {
		table.remove("Ivana");
		assertFalse(table.containsKey("Ivana"));
	}
	
	
	@Test(expected = NoSuchElementException.class)
	public void TestIterator() {
		SimpleHashtable<String, Integer> table1 = new SimpleHashtable<>(2);
		
		table1.put("Ivana", 2);
		table1.put("Ante", 2);
		table1.put("Jasna", 2);
		table1.put("Kristina", 5);
		table1.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table.iterator();
		
		assertEquals("Ante", it.next().getKey());
		assertEquals("Ivana", it.next().getKey());
		assertEquals("Jasna", it.next().getKey());
		assertEquals("Kristina", it.next().getKey());
		it.next();
	}
	
	@Test
	public void testClear() {
		table.clear();
		assertEquals(0, table.size());
		assertNull(table.get("Ivana"));
	}
	
	@Test
	public void TestRemoveIterator() {
		SimpleHashtable<String, Integer> table1 = new SimpleHashtable<>(2);
		
		table1.put("Ivana", 2);
		table1.put("Ante", 2);
		table1.put("Jasna", 2);
		table1.put("Kristina", 5);
		table1.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table1.iterator();
		
		while(it.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = it.next();
			it.remove();
		}
		
		assertEquals(0, table1.size());
	}
	
	@Test(expected = ConcurrentModificationException.class)
	public void testModificationIter() {
		SimpleHashtable<String, Integer> table1 = new SimpleHashtable<>(2);
		
		table1.put("Ivana", 2);
		table1.put("Ante", 2);
		table1.put("Jasna", 2);
		table1.put("Kristina", 5);
		table1.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table1.iterator();
		
		while(it.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = it.next();
			if(pair.getKey().equals("Ivana")) {
				table1.remove("Ivana");
			}
		}
		
	}
	
	@Test(expected = IllegalStateException.class)
	public void testIterRemoveException() {
		SimpleHashtable<String, Integer> table1 = new SimpleHashtable<>(2);
		
		table1.put("Ivana", 2);
		table1.put("Ante", 2);
		table1.put("Jasna", 2);
		table1.put("Kristina", 5);
		table1.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table1.iterator();
		
		while(it.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = it.next();
			if(pair.getKey().equals("Ivana")) {
				it.remove();
				it.remove();
			}
		}
		
	}
	

}
