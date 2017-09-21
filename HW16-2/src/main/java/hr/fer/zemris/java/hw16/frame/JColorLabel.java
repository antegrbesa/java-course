package hr.fer.zemris.java.hw16.frame;

import java.awt.Color;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw16.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.interfaces.IColorProvider;

/**
 * Extends {@link JLabel}. It is used to write information about currently used colors in 
 * {@link JVDraw} class. 
 * @author Ante
 *
 */
public class JColorLabel extends JLabel implements ColorChangeListener {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 4257284718640714751L;

	/**
	 * Background color. 
	 */
	private Color bgColor;

	/**
	 * Foreground color. 
	 */
	private Color fgColor;

	/**
	 * Provider of foreground color. 
	 */
	IColorProvider fgProvider;

	/**
	 * Provider of background color. 
	 */
	IColorProvider bgProvider;

	/**
	 * Constructor.
	 * @param fgProvider provider of foreground color
	 * @param bgProvider provider of background color 
	 */
	public JColorLabel(IColorProvider fgProvider, IColorProvider bgProvider) {
		this.fgProvider = fgProvider;
		this.bgProvider = bgProvider;
		this.fgColor = fgProvider.getCurrentColor();
		this.bgColor = bgProvider.getCurrentColor();
		this.setText();
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		if (source == fgProvider && fgColor == oldColor) {
			fgColor = newColor;
		}
		if (source == bgProvider && bgColor == oldColor) {
			bgColor = newColor;
		}
		setText();
	}

	/**
	 * Sets the label text. 
	 */
	public void setText() {
		String text = String.format("Foreground color: (%d, %d, %d), background color (%d, %d,%d)",
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
		super.setText(text);
	}
}
