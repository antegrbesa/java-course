package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements support for working with complex numbers
 * <code>a+bi</code>. 
 * 
 * @author Ante Grbesa
 *
 */
public class Complex {
	
	/** Real part of a complex number*/
	private double real;
	
	/** Imaginary part of a complex number */
	private double imag;
	
	public static final Complex ZERO = new Complex(0,0);
	
	public static final Complex ONE = new Complex(1,0);
	
	public static final Complex IM = new Complex(0,1);
	
	public static final Complex ONE_NEG = new Complex(-1,0);
	
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * Constructs complex number with default (zero) real and imaginary 
	 * parts. 
	 */
	public Complex() {
		this(0, 0);
	}
	
	/**
	 * Constructs complex number with specified real and imaginary 
	 * parts. 
	 * @param real real part of this complex number
	 * @param imag imaginary part of this complex number
	 */
	public Complex(double real, double imag) {
		super();
		this.real = real;
		this.imag = imag;
	}
	
	/**
	 * Gets real part.
	 * @return real part
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Gets the imaginary part
	 * @return imaginary part
	 */
	public double getImaginary() {
		return imag;
	}
	
	
	/**
	 * Calculates the module of this ComplexNumber.
	 * @return calculated module of this ComplexNumber
	 */
	public double module() {
		return Math.hypot(real, imag);
	}
	
	/**
	 * Multiplies this complex number with the specified one.
	 * @param c   {@link Complex} which will be the multiplier
	 * @return  calculated result of this multiplication operation 
	 * as a  {@link Complex}.
	 */
	public Complex multiply(Complex c) {
		double real = this.real*c.real - this.imag*c.imag;
		double imag = this.real*c.imag + this.imag*c.real;
		
		return new Complex(real, imag);
	}
	
	/**
	 * Divides this complex number with the specified one. 
	 * @param c {@link Complex} which will be the denominator.
	 * @return  calculated result of this division operation 
	 * as a  {@link Complex}.
	 */
	public Complex divide(Complex c) {
		double squared = c.real*c.real + c.imag*c.imag;
		Complex conj = new Complex(c.real, -c.imag);
		
		return this.multiply(conj).multiply(new Complex(1/squared, 0.0));
	}
	
	/**
	 * Adds this ComplexNumber with the specified Complex Number.
	 * @param c {@link Complex} to add
	 * @return calculated result of this operation as a  {@link Complex}. 
	 */
	public Complex add(Complex c) {
		return new Complex
				(this.real+c.real,this.imag+c.imag);
	}
	
	/**
	 * Subtracts this complex number with the specified one.
	 * @param c  {@link Complex} which will be subtracter
	 * @return calculated result of this division operation as a  {@link Complex}.
	 */
	public Complex sub(Complex c) {
		return new Complex
				(this.real-c.real, this.imag-c.imag);
	}
	
	/**
	 * Negates this complex number
	 * @return negated this
	 */
	public Complex negate() {
		return new Complex(-real, -imag);
	}
	
	/**
	 * Returns the value of this complex number raised to the power of the 
	 * first argument.
	 * @param n the exponent 
	 * @return  the value of this complex number raised to the power of the 
	 * first argument as a {@link Complex}.
	 * @throws IllegalArgumentException if given argument is < 0
	 */
	public Complex power(int n) {
		if(n<0) {
			throw new IllegalArgumentException();
		}
		
		double angle = this.getAngle() * n;
		double magnitude = Math.pow(this.module(), n);
		
		double real = Math.cos(angle) * magnitude;
		double imag = Math.sin(angle) * magnitude;
		
		return new Complex(real, imag);
	}
	
	/**
	 * Calculates the angle of this ComplexNumber.
	 * @return calculated angle of this ComplexNumber in range
	 * from 0 to 2 Pi.
	 */
	private double getAngle() {
		double angle = Math.atan2(imag, real);
		
		return angle;
	}
	
	/**
	 * Returns the n-th roots of this complex number as a list. 
	 * @param n number of root 
	 * @return the n-th roots of this complex number as a list of {@link Complex}.
	 * @throws IllegalArgumentException if specified argument is <= 0
	 */
	public List<Complex> root(int n) {
		if(n <= 0) {
			throw new IllegalArgumentException();
		}
				
		double magnitude = Math.pow(this.module(), 1/(double)n);
		double firstAngle = Math.atan(this.imag/this.real) / n;
		double secondAngle = (2 * Math.PI) / n;
		
		List<Complex> list = new ArrayList<>(n);
		for(int i = 0; i < n; i++) {
			double angleSum = firstAngle + i*secondAngle;
			
			double real = magnitude * Math.cos(angleSum);
			double imag = magnitude * Math.sin(angleSum);
			list.add(new Complex(real, imag));
		}
		
		return list;
	}
	
	/**
	 * Returns the string representation of this ComplexNumber.
	 * @return string representation of this ComplexNumber.
	 */
	@Override
	public String toString() {
		return String.format("%.6f%s%.6fi", real, (this.imag < 0 ? "" : "+"), imag);
	}
}
