package hr.fer.zemris.java.hw16.factories;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.interfaces.GeometricalObjectFactory;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.Line;


/**
 * Class that instantiates instances of {@link Line} class.
 * @author Ante
 *
 */
public class LineFactory implements GeometricalObjectFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeometricalObject createObject(Point first, Point second, Color fg,
			Color bg, int id) {

		return new Line("Line " + id, first, second, fg);
}
}
