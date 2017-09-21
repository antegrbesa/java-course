package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;


import org.junit.Test;

public class ValueWrapperTest {


	@Test
	public void testAdd1() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		assertEquals(0, (int) v1.getValue());
		assertEquals(null, v2.getValue());
	}
	
	@Test
	public void testAdd2() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue()); 
		assertEquals(13.0, (double) v3.getValue(), 1E-6);
		assertEquals(1, v4.getValue());
	}
	
	@Test
	public void testAdd3() {
		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.add(v6.getValue()); 
		assertEquals(13, v5.getValue());
		assertEquals(1, v6.getValue());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAdd4() {
		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		v7.add(v8.getValue());
	}
	
	
	@Test
	public void testSubtract() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.subtract(v4.getValue()); 
		assertEquals(11.0, (double) v3.getValue(), 1E-6);
		assertEquals(1, v4.getValue());
	}
	
	@Test
	public void testSubtractInt() {
		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.subtract(v6.getValue()); 
		assertEquals(11, v5.getValue());
		assertEquals(1, v6.getValue());
	}
	
	@Test
	public void testMultiply() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(2));
		v3.multiply(v4.getValue()); 
		assertEquals(24.0, (double) v3.getValue(), 1E-6);
		assertEquals(2, v4.getValue());
	}
	
	@Test
	public void testMultiplyNull() {
		ValueWrapper v3 = new ValueWrapper(null);
		ValueWrapper v4 = new ValueWrapper(null);
		v3.multiply(v4.getValue()); 
		assertEquals(0, (int) v3.getValue());
		assertEquals(null, v4.getValue());
	}
	
	@Test
	public void testDivide() {
		ValueWrapper v3 = new ValueWrapper("1.1E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(2));
		v3.divide(v4.getValue()); 
		assertEquals(5.5, (double) v3.getValue(), 1E-6);
		assertEquals(2, v4.getValue());
	}
	
	@Test(expected = ArithmeticException.class)
	public void testDivideWithBothNull() {
		ValueWrapper v3 = new ValueWrapper(2);
		ValueWrapper v4 = new ValueWrapper(null);
		v3.divide(v4.getValue());
	}
	
	@Test
	public void testDivideWithFirsNull() {
		ValueWrapper v3 = new ValueWrapper(null);
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(2));
		v3.divide(v4.getValue()); 
		assertEquals(0, v3.getValue());
	}
	
	@Test
	public void testNumCompare() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		assertEquals(1, v3.numCompare(v4.getValue()));
		assertEquals(1, v4.getValue());
		assertEquals("1.2E1", v3.getValue());
		assertEquals(-1, v4.numCompare(v3.getValue()));
	}
	
	@Test
	public void testNumCompare2() {
		ValueWrapper v3 = new ValueWrapper("1.22E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(13));
		assertEquals(-1, v3.numCompare(v4.getValue()));
		assertEquals(13, v4.getValue());
		assertEquals("1.22E1", v3.getValue());
		assertEquals(1, v4.numCompare(v3.getValue()));
	}
	
	@Test
	public void testNumCompare3() {
		ValueWrapper v3 = new ValueWrapper("1.22E1");
		ValueWrapper v4 = new ValueWrapper("1.23E1");
		assertEquals(-1, v3.numCompare(v4.getValue()));
		assertEquals("1.23E1", v4.getValue());
		assertEquals("1.22E1", v3.getValue());
		assertEquals(1, v4.numCompare(v3.getValue()));
	}
	
	@Test
	public void testNumCompareWithNull() {
		ValueWrapper v3 = new ValueWrapper(null);
		ValueWrapper v4 = new ValueWrapper(null);
		assertEquals(0, v3.numCompare(v4.getValue()));
		assertEquals(null, v4.getValue());
		assertEquals(null, v3.getValue());
		assertEquals(0, v4.numCompare(v3.getValue()));
	}

}
