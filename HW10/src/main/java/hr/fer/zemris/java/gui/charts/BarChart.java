package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Represents a data collection for {@link BarChartComponent} class. 
 * @author Ante Grbesa
 *
 */
public class BarChart {

	/**Values*/
	private List<XYValue> values;
	
	/**X axis text*/
	private String xText;
	/**Y axis text*/
	private String yText;
	
	/**Minimal y value*/
	private int minY;
	/**Max y value*/
	private int maxY;
	
	/**Spacing between y*/
	private int spacing;

	/**
	 * Constructs an instance of this class with specified arguments.
	 * @param values Values
	 * @param xText X axis text
	 * @param yText Y axis text
	 * @param minY Minimal y value
	 * @param maxY Max y value
	 * @param spacing Spacing between y
	 */
	public BarChart(List<XYValue> values, String xText, String yText, int minY, int maxY, int spacing) {
		super();
		this.values = values;
		this.xText = xText;
		this.yText = yText;
		this.minY = minY;
		this.spacing = spacing;
		
		if ((maxY - minY) % spacing != 0) {
			maxY = getFirstBigger(maxY);
		} else {
			this.maxY = maxY;
		}
	}

	/**
	 * Returns first bigger divisoor of given argument and spacing.
	 * @param maxY max y value
	 * @return first bigger divisor
	 */
	private int getFirstBigger(int maxY) {
		boolean search = true;
		
		while (search) {
			maxY++;
			if ((maxY - minY) % spacing == 0) {
				return maxY;
			}
		}
		
		return maxY;
	}

	/**
	 * @return the values
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * @return the xText
	 */
	public String getxText() {
		return xText;
	}

	/**
	 * @return the yText
	 */
	public String getyText() {
		return yText;
	}

	/**
	 * @return the minY
	 */
	public Integer getMinY() {
		return minY;
	}

	/**
	 * @return the maxY
	 */
	public Integer getMaxY() {
		return maxY;
	}

	/**
	 * @return the spacing
	 */
	public int getSpacing() {
		return spacing;
	}
	
	
	
	
	
	
}
