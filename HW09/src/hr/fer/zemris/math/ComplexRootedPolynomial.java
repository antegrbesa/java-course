package hr.fer.zemris.math;

/**
 * Models a polynom {@code f(z)} where {@code f(z) = (z-z1) * (z-z2) * ...}
 * @author Ante
 *
 */
public class ComplexRootedPolynomial {
	
	/**Roots of this polynom*/
	private Complex[] roots;

	/**
	 * Constructs this polynom with specified roots
	 * @param factors factors to set
	 */
	public ComplexRootedPolynomial(Complex ...roots) {
		super();
		if (roots == null) {
			throw new IllegalArgumentException("Argument was null");
		}
		
		this.roots = roots;
	}
	
	/**
	 * computes polynomial value at given point z
	 * @param z given point
	 * @return calculated value
	 */
	public Complex apply(Complex z) {
		boolean first = false;
		Complex result = null;
		
		for (Complex c : roots) {
			if (! first) {
				first = true;
				result = z.sub(c);
				continue;
			}
			
			result = result.multiply(z.sub(c));
		}
		
		return result;
	}
	
	/**
	 * Converts this representation to {@link ComplexPolynomial} type
	 * @return  this representation to {@link ComplexPolynomial} type
	 */
	public ComplexPolynomial toComplexPolynom() {	
		ComplexPolynomial c = null;
		for (int i = 0; i < roots.length; i++) {
			if (roots[i] == null)
				continue;
			
			if (i == 0) {
				c = new ComplexPolynomial(roots[i].negate(), Complex.ONE);
				continue;
			}

			c = c.multiply(new ComplexPolynomial(roots[i].negate(), Complex.ONE));
		}
		
		
		return c;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Complex c : roots) {
			sb.append("(");
			sb.append("z -");
			if (c != null) {
				sb.append(c.toString());
			}
			sb.append(") * ");
		}
		
		sb.replace(sb.length()-2, sb.length(), "  ");
		return sb.toString();
	}
	
	/**
	 * finds index of closest root for given complex number z that is within treshold;
	 *  if there is no such root, returns -1.
	 * @param z complex number for comparing
	 * @param threshold threshold
	 * @return index of closes root within threshold, -1 otherwise
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		int i = 0;
		for (Complex c : roots) {
			if (c == null)
				continue;
			
			if (Math.abs(c.getReal() - z.getReal()) < threshold
					&& Math.abs(c.getImaginary()- z.getImaginary()) < threshold) {
				return i+1;
			}
			
			++i;
		}
		
		return -1;
	}
	
	
	
	
}
