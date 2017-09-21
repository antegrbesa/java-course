package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A demo class for {@link BarChartComponent}. Expects one command line argument: name of a source file with
 * data that is structured as follows: 
 * <pre> x axis text  
 * y axis text
 * values like x,y 
 * y start value 
 * y end value
 * y difference 
 * </pre>
 * @author Ante Grbesa
 *
 */
public class BarChartDemo extends JFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Main method.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid argument");
			System.exit(1);
		}
		Path path = Paths.get(args[0]);
		
		BarChart chart = getChartData(path);
		
		SwingUtilities.invokeLater(() -> { 
			new BarChartDemo(chart, path.toString()).setVisible(true);
		});
	}
	
	/**
	 * Returns {@link BarChart} generated from specified file path.
	 * @param path file path
	 * @return generated BarChart
	 */
	private static BarChart getChartData(Path path) {
		String xText = "";
		String yText = "";
		List<XYValue> values = new ArrayList<>();
		int minY = 0;
		int maxY = 0;
		int spacing = 0;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(new FileInputStream(path.toFile())),"UTF-8"))) {
			xText = br.readLine();
			yText = br.readLine();
			
			String[] inputs = br.readLine().split(" ");
			for (String s : inputs) {
				String[] xy = s.split(",");
				int x, y;
				x = Integer.parseInt(xy[0]);
				y = Integer.parseInt(xy[1]);
				
				values.add(new XYValue(x, y));
			}
			minY = Integer.parseInt(br.readLine());
			maxY = Integer.parseInt(br.readLine());
			spacing = Integer.parseInt(br.readLine());
			
		} catch (IOException | NumberFormatException e) {
			System.out.println("Invalid file.");
			System.exit(1);
		}
		
		return new BarChart(values, xText, yText, minY, maxY, spacing);
	}

	/**
	 * Constructor for this class. Also initializes GUI.
	 * @param chart chart data
	 * @param fileName file name
	 */
	public BarChartDemo(BarChart chart, String fileName) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		setLocation(20, 20);
		setSize(800, 600);
		setTitle("Naslov");
		
		initGUI(chart, fileName);
	}

	/**
	 * initializes GUI.
	 * @param chart chart data
	 * @param fileName file name
	 */
	private void initGUI(BarChart chart, String fileName) {
		Container cp = getContentPane();
	
		BarChartComponent comp = new BarChartComponent(chart, fileName);
		comp.setBackground(Color.WHITE);		
		cp.add(comp);
		
	}

}
