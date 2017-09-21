package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JComponent;

/**
 * Represents a bar chart component. 
 * @author Ante Grbesa
 *
 */
public class BarChartComponent extends JComponent {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**Chart data*/
	private BarChart chart;
	
	/**Padding to add*/
	private static final int PADDING = 8;
	
	/**Top coordinate*/
	private int top;
	
	/**Bottom coordinate*/
	private int  bottom;
	
	/**Left coordinate*/
	private int  left;
	
	/**Right coordinate*/
	private int right;
	
	/**Source file name*/
	private String fileName;

	/**
	 * Constructor for this class.
	 * @param chart chart data to set
	 * @param fileName file name to set
	 */
	public BarChartComponent(BarChart chart, String fileName) {
		super();
		this.chart = chart;
		this.fileName = fileName;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		FontMetrics fm = getFontMetrics(getFont());
		left = x + 2*PADDING +  fm.getHeight() + PADDING + fm.stringWidth(chart.getMaxY().toString()) + PADDING;
		bottom = height - (fm.getHeight() + PADDING + fm.getHeight() + 2*PADDING);
		right = width - PADDING;
		top = y + 3 * PADDING + fm.getHeight();
	}
	
	
	@Override
	public void paint(Graphics g) {	
		if (isOpaque()) {
			g.setColor(getBackground());
		}
		FontMetrics fm = getFontMetrics(getFont());
		
		g.setColor(getForeground());
		g.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 18)); 
		
		
		
	    //draw horizontal lines
	    int rowCount = chart.getMaxY() / chart.getSpacing();
	    int rowHeight = (bottom - top - PADDING) / rowCount;
	    for (int i = 0; i <= rowCount; i++) {
	    	int letterWidth = fm.stringWidth(new Integer(i*chart.getSpacing()).toString());
	    	g.drawLine(left - PADDING, bottom - (i*rowHeight), right - PADDING, bottom - (i * rowHeight));
	    	g.drawString(new Integer(i*chart.getSpacing()).toString(), left - letterWidth - (PADDING * 2)
	    			, bottom - (i*rowHeight) + (fm.getHeight()/2));
	    }
	    
	    //draw vertical lines
	    List<XYValue> values = chart.getValues();
	    int colCount = values.size();
	    int colWidth = (right - left - PADDING) / colCount;
	    for (int i = 0; i <= colCount; i++) {
	    	int letterWidth = fm.stringWidth(new Integer(i).toString());
	    	g.drawLine(left + (i*colWidth), bottom + PADDING, left + (i*colWidth), top + PADDING);
	    	g.drawString(new Integer(i+1).toString(), left + ((colWidth-letterWidth) / 2) + i*(colWidth)
	    			, bottom + PADDING + fm.getHeight());
	    	
	    	if (i == colCount) break;
	    	XYValue value = values.get(i);
	    	g.setColor(Color.getHSBColor(48f, 0.78f, 1.0f));
	    	g.fillRect(left + ((value.getX()-1)*colWidth) + 1, bottom - ((value.getY()/2)*rowHeight)+1
	    			, colWidth, rowHeight*(value.getY()/chart.getSpacing()));
	    	g.setColor(Color.black);
	    }
	    
	    // bottom text
	    g.drawString(chart.getxText(), (right - left)/2  - (fm.stringWidth(chart.getxText())/2)
	    		, bottom + PADDING + fm.getHeight() + PADDING + fm.getHeight());
	    
	    g.drawString(fileName, (right - left)/2  - (fm.stringWidth(fileName)/2) , top - fm.getHeight() - PADDING);
	    
	    //left text
	    Graphics2D g2d = (Graphics2D) g;
	    AffineTransform at = AffineTransform.getQuadrantRotateInstance(3);
	    g2d.setTransform(at);
	    g2d.drawString(chart.getyText(), -((bottom - top)/2 + (fm.stringWidth(chart.getyText())/2))
	    		, left - PADDING -  fm.stringWidth(chart.getMaxY().toString()) - 2*PADDING);
	    
	    //upper text
	    g.drawString(fileName, (right - left)/2  - (fm.stringWidth(fileName)/2) , top + fm.getHeight() + PADDING);
	}
	
}
