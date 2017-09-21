package hr.fer.zemris.java.hw16.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.interfaces.IColorProvider;

/**
 * Class used for color changing in {@link JVDraw} class.
 * @author Ante
 *
 */
public class JColorArea extends JComponent implements IColorProvider {


	/**
	 * UID
	 */
	private static final long serialVersionUID = -9168612548686012306L;

	/**
	 * Currently selected color.
	 */
	private Color selectedColor;

	/**
	 * Default dimension. 
	 */
	private static final int DEFAULT_DIMENSION = 15;
	
	/**
	 * List of listeners.
	 */
	private List<ColorChangeListener> listeners;

	/**
	 * Component dimension. 
	 */
	private Dimension dimension = new Dimension(DEFAULT_DIMENSION,DEFAULT_DIMENSION);

	/**
	 * Constructor.
	 * @param initial initial color. 
	 */
	public JColorArea(Color initial) {
		selectedColor = initial;
		listeners = new ArrayList<>();

		//on click, open JColorChooser
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(JColorArea.this, "Choose color", Color.BLUE);
				if (newColor != null) {
					Color oldColor = selectedColor;
					selectedColor = newColor;
					repaint();
					fire(oldColor, newColor);
				}
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return dimension;
	}

	
	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	/**
	 * Method that adds a listener.
	 * @param l listener to add
	 * @throws IllegalArgumentException if listener is null
	 */
	public void addColorChangeListener(ColorChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException("Listener null reference.");
		}
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Removes given listener if it exists. 
	 * @param l listener to remove
	 */
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

	/**
	 * Notifies listener of a change in used color. 
	 * @param oldColor old color
	 * @param newColor new color
	 */
	public void fire(Color oldColor, Color newColor) {
		for (ColorChangeListener l : listeners) {
			l.newColorSelected(this, oldColor, newColor);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		g.fillRect(getInsets().left, getInsets().top, dimension.width, dimension.height);
	}
}
