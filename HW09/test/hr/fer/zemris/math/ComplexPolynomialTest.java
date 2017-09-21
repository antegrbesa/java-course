package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexPolynomialTest {

	ComplexPolynomial c = new ComplexPolynomial(Complex.ONE, new Complex(5, 0), new Complex(2, 0), new Complex(7, 2));
	
	@Test
	public void testOrder() {
		assertEquals(3, c.order());
	}

	@Test
	public void testDerive() {
		ComplexPolynomial co = c.derive();
		
		System.out.println(co);
	}
	
	@Test
	public void testMultiply() {
		ComplexPolynomial c2 = new ComplexPolynomial(Complex.ONE, new Complex(2, 2));
		ComplexPolynomial res = c.multiply(c2);
		
		assertEquals(4, res.order());
		System.out.println("Mul: "+res);
	}
	
	@Test
	public void testToComplexPolynomial() {
		ComplexRootedPolynomial root = new ComplexRootedPolynomial(Complex.ONE_NEG, Complex.ONE, Complex.IM_NEG, Complex.IM);
		ComplexPolynomial res = root.toComplexPolynom();
		
		System.out.println("To Pol: "+res);
	}
	
	@Test
	public void testApply() {
		ComplexPolynomial c2 = new ComplexPolynomial(Complex.ONE,Complex.ZERO,Complex.ZERO,Complex.ZERO ,new Complex(1, 0));
		Complex c = c2.apply(new Complex(2, 2));
		
		System.out.println("Apply test: "+c);
	}
	

}
