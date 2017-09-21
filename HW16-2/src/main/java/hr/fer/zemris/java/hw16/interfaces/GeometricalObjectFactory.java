package hr.fer.zemris.java.hw16.interfaces;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;

/**
 * Represents a factory for instances of {@link GeometricalObject} class.
 * @author Ante
 *
 */
public interface GeometricalObjectFactory {

	/**
	 * Method that returns a {@link GeometricalObject} created with given arguments. 
	 * @param first first point
	 * @param second second point
	 * @param fg foreground color
	 * @param bg background color
	 * @param id object id
	 * @return new {@link GeometricalObject}
	 */
	public GeometricalObject createObject(Point first, Point second, Color fg, Color bg, int id);
}
