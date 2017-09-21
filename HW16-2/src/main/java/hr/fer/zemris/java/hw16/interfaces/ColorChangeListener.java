package hr.fer.zemris.java.hw16.interfaces;

import java.awt.Color;

/**
 * Interface that is implemented by all listeners of {@link IColorProvider} subject.
 * @author Ante
 *
 */
public interface ColorChangeListener {

	/**
	 * Called when there is a change in background or foreground color. 
	 * @param source source subject
	 * @param oldColor  old color
	 * @param newColor new color
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}