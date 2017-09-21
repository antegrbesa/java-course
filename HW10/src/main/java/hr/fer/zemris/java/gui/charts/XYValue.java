package hr.fer.zemris.java.gui.charts;

/**
 * Models a pair with two values. 
 * @author Ante Grbesa
 *
 */
public class XYValue {

	/**x value*/
	private int x;
	/**y value*/
	private int y;

	/**
	 * Constructs this value with specified arguments.
	 * @param x x to set
	 * @param y y to set
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the x
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y
	 * @return y
	 */
	public int getY() {
		return y;
	}
}
