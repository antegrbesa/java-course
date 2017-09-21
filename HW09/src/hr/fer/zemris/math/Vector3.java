package hr.fer.zemris.math;

/**
 * Models an immutable 3D vector. 
 * @author Ante
 *
 */
public class Vector3 {
	/**x coordinate*/
	private double x;
	/**Y coordinate*/
	private double y;
	/**z coordinate*/
	private double z;

	/**
	 * Constructs a vector with specified coordinates. 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Calculates norm of this vector
	 * @return norm of this vector
	 */
	public double norm() {
		double xyzPow = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
		
		return Math.sqrt(xyzPow);
	}
	
	/**
	 * Returns a normalized version of this vector
	 * @return normalized version of this vector
	 */
	public Vector3 normalized() {
		double length = norm();
		if (length == 0) {
			return new Vector3(0, 0, 0);
		}
		
		return new Vector3(x/length, y/length, z/length);
	}
	
	/**
	 * Adds this vector to the specified one and returns the resulting 
	 * vector
	 * @param other other vector for adding
	 * @return resulting vector
	 */
	public Vector3 add(Vector3 other) {
		checkForNull(other);
		return new Vector3(x+other.x, y+other.y, z+other.z);
	}
	
	/**
	 * Subtracts this vector with the specified one and returns the resulting 
	 * vector
	 * @param other other vector for subtraction
	 * @return resulting vector
	 */
	public Vector3 sub(Vector3 other) {
		checkForNull(other);
		return new Vector3(x-other.x, y-other.y, z-other.z);
	}
	
	/**
	 * Scalar product of this vector with specified argument
	 * @param other other vector for product
	 * @return  Scalar product
	 */
	public double dot(Vector3 other) {
		checkForNull(other);
		return x*other.x + y*other.y + z*other.z;
	}
	
	/**
	 * Vector product of this vector with specified one
	 * @param other other vector for product
	 * @return  vector  product
	 */
	public Vector3 cross(Vector3 other) {
		checkForNull(other);
		
		double newX = y*other.z - z*other.y;
		double newY = z*other.x - x*other.z;
		double newZ = x*other.y - y*other.x;
		
		return new Vector3(newX, newY, newZ);
	}
	
	/**
	 * Scaling with specified argument.
	 * @param s scale argument
	 * @return scaled vector
	 */
	public Vector3 scale(double s) {
		return new Vector3(x*s, y*s, z*s);
	}
	
	/**
	 * Calculates cosine of angle between this vector and specified one
	 * @param other other vector
	 * @return cosine of angle
	 */
	public double cosAngle(Vector3 other) {
		checkForNull(other);
		
		double numerator = dot(other);
		double denominator = norm() * other.norm();
		if (denominator == 0) {
			return Double.NaN;
		}
		
		return numerator/denominator;
	}
	
	/**
	 * Gets x.
	 * @return x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Gets y
	 * @return y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Gets z
	 * @return z
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Returns array representation of this vector
	 * @return array representation of this vector
	 */
	public double[] toArray() {
		return new double[] {x,y,z};
	}
	
	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}
	
	/**
	 * Checks argument for null
	 * @param other arg to check
	 */
	private void checkForNull(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument was null");
		}
	}
	
}
