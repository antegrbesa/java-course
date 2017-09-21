package hr.fer.zemris.java.hw16.factories;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.interfaces.GeometricalObjectFactory;
import hr.fer.zemris.java.hw16.jvdraw.model.Circle;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;

/**
 * Class that instantiates instances of {@link Circle} class.
 * @author Ante
 *
 */
public class CircleFactory implements GeometricalObjectFactory {

	@Override
	public GeometricalObject createObject(Point first, Point second, Color fg,
			Color bg, int id) {

		double radius = Math.pow((first.getX() - second.getX()), 2) + Math.pow((first.getY() - second.getY()), 2);
		radius = Math.sqrt(radius);
		return new Circle("Circle " + id, first, radius, fg);
}
}
