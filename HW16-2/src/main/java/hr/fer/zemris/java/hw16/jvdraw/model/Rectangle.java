package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Represents a rectangle. 
 * @author Ante
 *
 */
public class Rectangle {

	/**
	 * X value.
	 */
	protected int x;
	
	/**
	 * Y value.
	 */
	protected int y;
	
	/**
	 * Width
	 */
	protected int width;
	
	/**
	 * Height.
	 */
	protected int height;

	/**
	 * Constructs an instance of this class using specified arguments. 
	 * @param x x to set
	 * @param y y to set
	 * @param width width to set
	 * @param height height to set
	 */
	public Rectangle(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
