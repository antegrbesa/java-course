package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector3Test {

	@Test
	public void testNorm() {
		Vector3 vect = new Vector3(2.1, 3.4, -1.2);
		
		assertEquals(4.172529, vect.norm(), 1E-6);
	}
	
	@Test
	public void testNormalized() {
		Vector3 vect = new Vector3(1, 3, 4);
		Vector3 normalized = vect.normalized();
		
		assertEquals(0.196116, normalized.getX(), 1E-6);
		assertEquals(0.588348, normalized.getY(), 1E-6);
		assertEquals(0.784464, normalized.getZ(), 1E-6);
	}
	
	@Test
	public void testAdd() {
		Vector3 vect = new Vector3(1, 3, 4);
		Vector3 vect2 = new Vector3(2.1, 3.4, -1.2);
		Vector3 vect3 = vect.add(vect2);
		
		assertEquals(1, vect.getX(), 1E-6);
		assertEquals(2.1, vect2.getX(), 1E-6);
		assertEquals(3.1, vect3.getX(), 1E-6);
		assertEquals(6.4, vect3.getY(), 1E-6);
		assertEquals(2.8, vect3.getZ(), 1E-6);
	}
	
	@Test
	public void testSub() {
		Vector3 vect = new Vector3(1, 3, 4);
		Vector3 vect2 = new Vector3(2.1, 3.4, -1.2);
		Vector3 vect3 = vect.sub(vect2);
		
		assertEquals(1, vect.getX(), 1E-6);
		assertEquals(2.1, vect2.getX(), 1E-6);
		assertEquals(-1.1, vect3.getX(), 1E-6);
		assertEquals(-0.4, vect3.getY(), 1E-6);
		assertEquals(5.2, vect3.getZ(), 1E-6);
	}
	
	@Test
	public void testDot() {
		Vector3 vect = new Vector3(1, 3, 4);
		Vector3 vect2 = new Vector3(2.1, 3.4, -1.2);
		double vect3 = vect.dot(vect2);
		
		assertEquals(1, vect.getX(), 1E-6);
		assertEquals(2.1, vect2.getX(), 1E-6);
		assertEquals(7.5, vect3, 1E-6);
	}
	
	@Test
	public void testCross() {
		Vector3 vect = new Vector3(1, 3, 4);
		Vector3 vect2 = new Vector3(2.1, 3.4, -1.2);
		Vector3 vect3 = vect.cross(vect2);
		
		assertEquals(1, vect.getX(), 1E-6);
		assertEquals(2.1, vect2.getX(), 1E-6);
		assertEquals(-17.2, vect3.getX(), 1E-6);
		assertEquals(9.6, vect3.getY(), 1E-6);
		assertEquals(-2.9, vect3.getZ(), 1E-6);
	}
	
	@Test
	public void testScale() {
		Vector3 vect = new Vector3(1, 3, 4);
		Vector3 vect2 = vect.scale(3);
		
		assertEquals(1, vect.getX(), 1E-6);
		assertEquals(3, vect2.getX(), 1E-6);
		assertEquals(9, vect2.getY(), 1E-6);
		assertEquals(12, vect2.getZ(), 1E-6);
	}

	@Test
	public void testCosAngle() {
		Vector3 vect = new Vector3(1, 3, 4);
		Vector3 vect2 = new Vector3(2.1, 3.4, -1.2);
		double angle = vect.cosAngle(vect2);
		
		assertEquals(0.352513, angle, 1E-6);	
	}
	
	
}
