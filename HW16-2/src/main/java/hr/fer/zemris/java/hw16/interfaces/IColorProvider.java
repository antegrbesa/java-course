package hr.fer.zemris.java.hw16.interfaces;

import java.awt.Color;

import hr.fer.zemris.java.hw16.frame.JVDraw;

/**
 * Interface that is implemented by all subjects that store currently selected colors
 * in {@link JVDraw} class.
 * @author Ante
 *
 */
public interface IColorProvider {

	/**
	 * Gets the current color.
	 * @return current color.
	 */
	public Color getCurrentColor();
}
