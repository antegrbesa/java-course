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
 *  Models a filled circle. Extends {@link GeometricalObject}.
 * @author Ante
 *
 */
public class FilledCircle extends Circle {

	/**
	 * Background color.
	 */
	private Color bgColor;

	/**
	 * Constructor.
	 * @param name name of object
	 * @param center Center of circle.
	 * @param radius rRadius of circle.
	 * @param color Color of circle.
	 * @param bgColor background color
	 */
	public FilledCircle(String name, Point center, double radius, Color color,
			Color bgColor) {
		super(name, center, radius, color);
		this.bgColor = bgColor;
	}

	/**
	 * Sets the background color. 
	 * @param bgColor background color.
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	@Override
	public void draw(Graphics g, Rectangle offset) {
		g.setColor(bgColor);
		g.fillOval(center.x - (int)radius - offset.x, center.y - (int)radius - offset.y,
				(int)(2 * radius), (int)(2 * radius));
		super.draw(g, offset);
	}


	@Override
	public void update() {
		JPanel panel = new JPanel();
		JTextField tfFirst = new JTextField(center.x + ", " + center.y);
		JTextField tfRadius = new JTextField(String.valueOf(radius));
		JColorArea newColor = new JColorArea(this.color);
		JColorArea newBGColor = new JColorArea(bgColor);
		panel.add(new JLabel("Center (x,y):"));
		panel.add(tfFirst);
		panel.add(new JLabel("Radius"));
		panel.add(tfRadius);
		panel.add(new JLabel("FG Color:"));
		panel.add(newColor);
		panel.add(new JLabel("BG Color:"));
		panel.add(newBGColor);
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
			setBgColor(newBGColor.getCurrentColor());
			setCenter(newCenter);
			setRadius(newRadius);
		}
	}

	
	@Override
	public String getDescription() {
		return String.format(FileUtil.DEFAULT_LOCALE, "FCIRCLE %d %d %.1f %d %d %d %d %d %d%n", center.x, center.y, radius,
				color.getRed(), color.getGreen(), color.getBlue(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
	}
}
