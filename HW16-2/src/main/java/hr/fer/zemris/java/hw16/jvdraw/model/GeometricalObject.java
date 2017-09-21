package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Graphics;

/**
 * Represents a generic geometrical object. 
 * @author Ante
 *
 */
public abstract class GeometricalObject  {

	/**
	 * Name of object. 
	 */
	private String name;

	/**
	 * Constructor.
	 * @param name name of object
	 */
	public GeometricalObject(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Returns the bounds of this object (bounding box).
	 * @return bounds of this object
	 */
	public abstract Rectangle getBounds();

	/**
	 * Draws current object with specified arguments. 
	 * @param g graphics to use
	 * @param offset offset to set
	 */
	public abstract void draw(Graphics g, Rectangle offset);

	/**
	 * Calls update frame for this object and uses selected values.
	 */
	public abstract void update();

	/**
	 * Gets the description of this object. 
	 * @return description of object
	 */
	public abstract String getDescription();

	/**
	 * Writes its bounding box to given argument. 
	 * @param box referenec to bounding box. 
	 */
	public abstract void findBoundingBox(Rectangle box);
}
