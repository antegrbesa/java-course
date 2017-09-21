package hr.fer.zemris.java.hw16.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.interfaces.GeometricalObjectFactory;
import hr.fer.zemris.java.hw16.interfaces.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.Rectangle;

/**
 * Represents a <i>canvas</i> that is used to draw instances of {@link GeometricalObject} class on it. 
 * It is used by {@link JVDraw} class. 
 * @author Ante
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 5889530764580555089L;

	/**
	 * Position of first mouse click.
	 */
	private Point first;

	/**
	 * Position of second mouse click (or just current location of mouse).
	 */
	private Point second;

	/**
	 * Currently drawn object. 
	 */
	private GeometricalObject current;

	/**
	 *Current drawing model 
	 */
	private DrawingModel model;

	/**
	 * Foreground color provider.
	 */
	private IColorProvider fgColor;

	/**
	 * Background color provider. 
	 */
	private IColorProvider bgColor;

	/**
	 * Id of current object. 
	 */
	private int id = 0;

	/**
	 * Canvas changed flag.
	 */
	private boolean canvasChanged = false;

	/**
	 * Command to execute. 
	 */
	private GeometricalObjectFactory command; 

	/**
	 * Constructor.
	 * @param fgColor Foreground color provider
	 * @param bgColor background color provider
	 * @param model {@link DrawingModel} reference.
	 */
	public JDrawingCanvas(IColorProvider fgColor, IColorProvider bgColor, DrawingModel model) {
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.model = model;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (command == null)  return; 
				if (first == null) {
					first = e.getPoint();
				} else {
					second = e.getPoint();
					createNewGeometricalObject(true);
				}
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (first == null) return; 
				second = e.getPoint();
				createNewGeometricalObject(false);
			}
		});
	}

	
	@Override
	protected void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON
		);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (model != null) {
			for (int i = 0; i < model.getSize(); i++) {
				model.getObject(i).draw(g, new Rectangle(0, 0, 0, 0));
			}
		}
		if (current != null) {
			current.draw(g, new Rectangle(0, 0, 0, 0));
		}
	}

	
	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		setCanvasChanged(true);
		repaint();
	}

	
	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		setCanvasChanged(true);
		repaint();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		setCanvasChanged(true);
		repaint();
	}

	/**
	 *
	 * Draws a new object on canvas. If current object is not final (second click not present), it draws object 
	 * and calls {@code repaint()} method. If it is final, it is added to model. 
	 *  @param finalObject final object flag
	 */
	public void createNewGeometricalObject(boolean finalObject) {
		int currentId = 0;
		if (finalObject) {
			currentId = id++;
		}
		current = command.createObject(
				first,
				second,
				fgColor.getCurrentColor(),
				bgColor.getCurrentColor(),
				currentId
		);
		if (finalObject) {
			model.add(current);
			resetCurrent();
		} else {
			repaint();
		}
	}

	/**
	 * Sets a new command to execute when drawing on canvas.
	 * @param c command to set
	 */
	public void setCommand(GeometricalObjectFactory c) {
		command = c;
		resetCurrent();
	}

	/**
	 * Resents current object. 
	 */
	public void resetCurrent() {
		first = null;
		second = null;
		current = null;
	}

	/**
	 * Returns true if this canvas is changed, otherwise returns false. 
	 * @return true if this canvas is changed, otherwise returns false. 
	 */
	public boolean isCanvasChanged() {
		return canvasChanged;
	}

	/**
	 * Sets the canvas changed flag.
	 * @param canvasChanged value to set
	 */
	public void setCanvasChanged(boolean canvasChanged) {
		this.canvasChanged = canvasChanged;
	}
}
