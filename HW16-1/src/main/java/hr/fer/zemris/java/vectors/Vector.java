package hr.fer.zemris.java.vectors;

/**
 * Modes a n-dimensional vector. 
 * @author Ante
 *
 */
public class Vector { 

	/**
	 * Length of vector.
	 */
    private final int n; 

    /**
     * Array of components.
     */
    private double[] data;      

    /**
     * Creates vector with specified size.
     * @param n size to set
     */
    public Vector(int n) {
        this.n = n;
        this.data = new double[n];
    }

    /**
     * Creates a vector from an array.
     * @param data array to use 
     */
    public Vector(double[] data) {
        n = data.length;

        // defensive copy 
        this.data = new double[n];
        for (int i = 0; i < n; i++)
            this.data[i] = data[i];
    }

	/**
	 * Calculates cosine of angle between this vector and specified one.
	 * @param other other vector
	 * @return cosine of angle
	 */
	public double cosAngle(Vector other) {
		checkForNull(other);
		
		double numerator = dot(other);
		double denominator = norm() * other.norm();
		if (denominator == 0) {
			return Double.NaN;
		}
		
		return numerator/denominator;
	}
   
	/**
	 * Calculates norm of this vector
	 * @return norm of this vector
	 */
	public double norm() {
		double pow = 0;
		for (int i = 0; i < n; i++) {
			pow += Math.pow(data[i], 2);
		}

		return Math.sqrt(pow);
	}
	
	/**
	 * Checks argument for null
	 * @param other arg to check
	 */
	private void checkForNull(Vector other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument was null");
		}
	}
	
	/**
	 * Returns length of this vector.
	 * @return length of vector
	 */
    public int length() {
        return n;
    }
    
    /**
     * Sets the specified value at given index.
     * @param i index
     * @param value value to set
     */
    public void setValueAt(int i, double value) {
    	if (i < 0 || i > n) {
    		throw new IllegalArgumentException("Index out of bounds");
    	}
    	data[i] = value;
    }

    /**
     * Returns the inner product of this Vector a and b
     * @param other other vector
     * @return inner product
     */
    public double dot(Vector other) {
        if (this.length() != other.length())
            throw new IllegalArgumentException("Dimensions differ, was "+this.n+","+other.n);
        double sum = 0.0;
        for (int i = 0; i < n; i++)
            sum = sum + (this.data[i] * other.data[i]);
        return sum;
    }


    /**
     * Returns the corresponding coordinate.
     * @param i index
     * @return corresponding coordinate
     */
    public double valueAt(int i) {
        return data[i];
    }
    
    /**
     * Increments value at given index.
     * @param i index
     */
    public void incrementValueAt(int i) {
    	if (i < 0 || i > n) {
    		throw new IllegalArgumentException("Invalid index");
    	}
    	data[i]++;	
    }
    

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('(');
        for (int i = 0; i < n; i++) {
            s.append(data[i]);
            if (i < n-1) s.append(", ");
        }
        s.append(')');
        return s.toString();
    }

}
