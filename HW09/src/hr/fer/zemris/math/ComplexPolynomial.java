package hr.fer.zemris.math;

/**
 * Models a polynom {@code f(z)} where {@code f(z) = zn * z^n + zn-1 * z^(n-1)...}
 * @author Ante
 *
 */
public class ComplexPolynomial {

	/**Factors of this polynom*/
	private Complex[] factors;

	/**
	 * Constructs this polynom with specified factors
	 * @param factors factors to set
	 */
	public ComplexPolynomial(Complex ...factors) {
		super();
		if (factors == null) {
			throw new IllegalArgumentException("Argument was null");
		}
		
		this.factors = factors;
	}
	
	/**
	 * Returns the order of this polynom
	 * @return order of this polynom
	 */
	public short order() {
		short order = (short) (factors.length-1);
		
		return order;
	}
	
	/**
	 * Multiplies this polynom with specified one.
	 * @param p polynom to multiply with
	 * @return new mutliplied polynom
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		if (p == null)
			throw new IllegalArgumentException("Argument was null");
		
		int max = order()+p.order();
		
		int diff = factors.length-p.factors.length;
		Complex[] values1 = diff > 0 ? factors : p.factors;
		Complex[] values2 = diff > 0 ? p.factors : factors;
		
		Complex[] result = new Complex[max+1];
		
		for (int i = 0; i < values1.length; i++) {
			for (int j = 0; j < values2.length; j++) {
				if (result[i+j] == null) {
					result[i+j] = values1[i].multiply(values2[j]);
					continue;
				}
				
				result[i+j] = result[i+j].add(values1[i].multiply(values2[j]));
			}
		}
		
		return new ComplexPolynomial(result);
	}
	
	/**
	 *computes first derivative of this polynomial and returns resulting polynom
	 * @return resulting polynom
	 */
	public ComplexPolynomial derive() {
		Complex[] result = new Complex[factors.length-1];
		for (int i = 1; i < factors.length; i++) {
			result[i-1] = new Complex(factors[i].getReal() * i, factors[i].getImaginary() * i);
		}
		
		return new ComplexPolynomial(result);
	}
	
	/**
	 * computes polynomial value at given point z
	 * @param z given point
	 * @return calculated value
	 */
	public Complex apply(Complex z) {
		if (z == null) {
			throw new IllegalArgumentException("Argument was null");
		}
		
		Complex res  = new Complex();
		for (int i = 0; i < factors.length; i++) {
			Complex result = factors[i].multiply(z.power(i));
			res = res.add(result);
		}
		
		return res;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < factors.length; i++) {
			sb.append("(");
			if (i == 0) {
				sb.append(factors[i]);
				sb.append(")");
				sb.append(" + ");
				continue;
			}
			
			sb.append(factors[i]);
			sb.append(")");
			sb.append("*z^");
			sb.append(i);
			sb.append(" + ");
		}
		sb.replace(sb.length()-2, sb.length(), "  ");
		
		return sb.toString();
	}
	
	
	
}
