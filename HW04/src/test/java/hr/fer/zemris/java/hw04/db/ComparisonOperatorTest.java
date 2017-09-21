package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComparisonOperatorTest {

	@Test
	public void testLess() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
	}
	
	@Test
	public void testLessOrEqual() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ivana", "Ivana"));
	}
	
	@Test
	public void testGreater() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
	}
	
	@Test
	public void testGreaterOrEquals() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ivana", "Ivana"));
	}
	
	@Test
	public void testEquals() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("Ivana", "Ivana"));
	}
	
	@Test
	public void testNotEquals() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		assertTrue(oper.satisfied("Jasna", "Ana"));
		assertFalse(oper.satisfied("Ivana", "Ivana"));
	}
	
	@Test
	public void testLike() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		assertTrue(oper.satisfied("AAAA", "AA*AA"));
		assertFalse(oper.satisfied("Jasna", "Ana"));
		assertTrue(oper.satisfied("AAAA", "*A"));
		assertFalse(oper.satisfied("AAA", "AA*AA"));
		assertTrue(oper.satisfied("ASDFGA", "ASDFGA*"));
		assertTrue(oper.satisfied("ASDFGA", "ASD*A"));
		assertTrue(oper.satisfied("ASDFGA", "*GA"));
		assertTrue(oper.satisfied("ASDFGA", "ASD*A"));		
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testLikeException() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		assertTrue(oper.satisfied("AAAA", "AA*A*A"));
	}
	
	

}
