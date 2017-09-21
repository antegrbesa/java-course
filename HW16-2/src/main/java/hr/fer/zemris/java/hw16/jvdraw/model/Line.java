package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.frame.JColorArea;

/**
 * Models a line. Extends {@link GeometricalObject}.
 * @author Ante
 *
 */
public class Line extends GeometricalObject {

	/**
	 * First point.
	 */
	private Point first;

	/**
	 * Second point. 
	 */
	private Point second;

	/**
	 * Line color. 
	 */
	private Color color;

	/**
	 * Constructor.
	 * @param name name of object
	 * @param first first point
	 * @param second second point
	 * @param color Color of line.
	 */
	public Line(String name, Point first, Point second, Color color) {
		super(name);
		this.first = first;
		this.second = second;
		this.color = color;
	}

	/**
	 * Sets the first point
	 * @param first first point to set
	 */
	public void setFirst(Point first) {
		this.first = first;
	}

	/**
	 * Sets the second point.
	 * @param second second point
	 */
	public void setSecond(Point second) {
		this.second = second;
	}

	/**
	 * Sets the line color. 
	 * @param color color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}


	@Override
	public void draw(Graphics g, Rectangle offset) {
		g.setColor(color);
		g.drawLine(first.x - offset.x, first.y - offset.y,
				second.x - offset.x, second.y - offset.y);
	}

	@Override
	public void update() {
		JPanel panel = new JPanel();
		JTextField tfFirst = new JTextField(first.x + ", " + first.y);
		JTextField tfSecond = new JTextField(second.x + ", " + second.y);
		JColorArea newColor = new JColorArea(color);
		panel.add(new JLabel("First point (x,y):"));
		panel.add(tfFirst);
		panel.add(new JLabel("Second point (x,y):"));
		panel.add(tfSecond);
		panel.add(new JLabel("Color:"));
		panel.add(newColor);
		int status = JOptionPane.showConfirmDialog(null, panel, "Edit", JOptionPane.OK_CANCEL_OPTION);
		if (status == JOptionPane.OK_OPTION) {
			Point newFirst = null;
			Point newSecond = null;
			try {
				newFirst = new Point(
						Integer.parseInt(tfFirst.getText().split(",")[0].trim()),
						Integer.parseInt(tfFirst.getText().split(",")[1].trim())
				);
				newSecond = new Point(
						Integer.parseInt(tfSecond.getText().split(",")[0].trim()),
						Integer.parseInt(tfSecond.getText().split(",")[1].trim())
				);
			} catch (NumberFormatException | IndexOutOfBoundsException ex) {
				return;
			}
			setColor(newColor.getCurrentColor());
			setFirst(newFirst);
			setSecond(newSecond);
		}
 	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(Math.min(first.x, second.x), Math.min(first.y, second.y)
				, Math.abs(first.x - second.x), Math.abs(first.y - second.y));
		
	}

	@Override
	public String getDescription() {
		return String.format("LINE %d %d %d %d %d %d %d%n", first.x, first.y, second.x, second.y,
				color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void findBoundingBox(Rectangle box) {
		box.setX(Math.min(first.x, second.x));
		box.setWidth(Math.max(first.x, second.x));
		box.setY(Math.min(first.y, second.y));
	}
}
