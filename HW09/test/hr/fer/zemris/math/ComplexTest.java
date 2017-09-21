package hr.fer.zemris.math;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ComplexTest {

	Complex real;
	Complex imag;
	Complex both;
	Complex bothSec;
	
	@Before
	public void setUp() throws Exception {
		real = new Complex(4.12, 0);
		imag = new Complex(0, 2.25);
		both = new Complex(4.12, 2.25);
		bothSec = new Complex(2.25, 4.12);
	}

	@Test
	public void testGetReal() {
		assertEquals(4.12, both.getReal(), 1E-6);
	}

	@Test
	public void testGetImaginary() {
		assertEquals(2.25, both.getImaginary(), 1E-6);
	}

	@Test
	public void testModule() {
		assertEquals(4.69434, both.module(), 1E-5);
	}
	
	@Test
	public void testAdd() {
		Complex c = both.add(bothSec);
		
		assertEquals(6.37, c.getReal(), 1E-6);
		assertEquals(6.37, c.getImaginary(), 1E-6);
	}

	@Test
	public void testSub() {
		Complex c = both.sub(bothSec);
		
		assertEquals(1.87, c.getReal(), 1E-6);
		assertEquals(-1.87, c.getImaginary(), 1E-6);
	}

	@Test
	public void testMul() {
		Complex c = both.multiply(bothSec);
		
		assertEquals(0.0, c.getReal(), 1E-6);
		assertEquals(22.0369, c.getImaginary(), 1E-6);
	}

	@Test
	public void testDiv() {
		Complex c = both.divide(bothSec);
		
		assertEquals(0.841316, c.getReal(), 1E-6);
		assertEquals(-0.540543, c.getImaginary(), 1E-6);
	}
	
	@Test
	public void testPower() {
		Complex c = both.power(3);
		
		assertEquals(7.362028, c.getReal(), 1E-6);
		assertEquals(103.186575, c.getImaginary(), 1E-6);
	}

	@Test
	public void testRoot() {
		List<Complex> list = both.root(3);
		
		assertEquals(1.65121, list.get(0).getReal(), 1E-5);
		assertEquals(0.277697, list.get(0).getImaginary(), 1E-5);
		
		assertEquals(-1.0661, list.get(1).getReal(), 1E-5);
		assertEquals(1.29114, list.get(1).getImaginary(), 1E-5);
		
		assertEquals(-0.58511, list.get(2).getReal(), 1E-5);
		assertEquals(-1.56884, list.get(2).getImaginary(), 1E-5);
	}
	
	@Test
	public void testRootImaginaryOnly() {
		List<Complex> list = imag.root(3);
		
		assertEquals(1.13481, list.get(0).getReal(), 1E-5);
		assertEquals(0.655185, list.get(0).getImaginary(), 1E-5);
		
		assertEquals(-1.13481, list.get(1).getReal(), 1E-5);
		assertEquals(0.655185, list.get(1).getImaginary(), 1E-5);
		
	}

}
