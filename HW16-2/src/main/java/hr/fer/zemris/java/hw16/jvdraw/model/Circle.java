package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.frame.FileUtil;
import hr.fer.zemris.java.hw16.frame.JColorArea;

/**
 * Models a circle. Extends {@link GeometricalObject}.
 * @author Ante
 *
 */
public class Circle extends GeometricalObject {

	/**
	 * Center of circle.
	 */
	protected Point center;
	
	/**
	 * Radius of circle.
	 */
	protected double radius;
	
	/**
	 * Color of circle.
	 */
	protected Color color;

	
	/**
	 * Constructor.
	 * @param name name of object
	 * @param center Center of circle.
	 * @param radius rRadius of circle.
	 * @param color Color of circle.
	 */
	public Circle(String name, Point center, double radius, Color color) {
		super(name);
		this.center = center;
		this.radius = radius;
		this.color = color;
	}

	/**
	 * Sets the circle center.
	 * @param center center of circle to set
	 */
	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * Sets the circle radius.
	 * @param radius radius to set
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Sets the circle color.
	 * @param color color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	
	@Override
	public void draw(Graphics g, Rectangle offset) {
		g.setColor(color);
		int r = new Double(radius).intValue();
		g.drawOval(center.x - r - offset.x, center.y - r - offset.y,
				2*r, 2*r);
	}

	@Override
	public void update() {
		JPanel panel = new JPanel();
		JTextField tfFirst = new JTextField(center.x + ", " + center.y);
		JTextField tfRadius = new JTextField(String.valueOf(radius));
		JColorArea newColor = new JColorArea(color);
		panel.add(new JLabel("Center (x,y):"));
		panel.add(tfFirst);
		panel.add(new JLabel("Radius"));
		panel.add(tfRadius);
		panel.add(new JLabel("Color:"));
		panel.add(newColor);
		int status = JOptionPane.showConfirmDialog(null, panel, "Edit", JOptionPane.OK_CANCEL_OPTION);
		if (status == JOptionPane.OK_OPTION) {
			Point newCenter = null;
			double newRadius = 0;
			try {
				newCenter = new Point(
						Integer.parseInt(tfFirst.getText().split(",")[0].trim()),
						Integer.parseInt(tfFirst.getText().split(",")[1].trim())
				);
				newRadius = Double.parseDouble(tfRadius.getText().trim());
			} catch (NumberFormatException | IndexOutOfBoundsException ex) {
				return;
			}
			setColor(newColor.getCurrentColor());
			setCenter(newCenter);
			setRadius(newRadius);
		}
	}


    @Override
    public Rectangle getBounds() {
    	Double d = 2*radius;
    	Integer r = new Double(radius).intValue();
        return new Rectangle(center.x -r, center.y -r, d.intValue(), d.intValue());
    }
	
	
	@Override
	public String getDescription() {
		return String.format(FileUtil.DEFAULT_LOCALE,"CIRCLE %d %d %.1f %d %d %d%n", center.x, center.y, radius,
				color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void findBoundingBox(Rectangle rec) {
		rec.setX(center.x - (int)radius);
		rec.setWidth(center.x + (int)radius);
		rec.setY(center.y - (int)radius);
		rec.setHeight(center.y + (int)radius);
	}
}
