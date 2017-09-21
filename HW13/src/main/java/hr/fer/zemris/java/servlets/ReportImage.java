package hr.fer.zemris.java.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * Generates an image with OS usage data represented in a pie chart. 
 * OS usage data obtained from: https://www.statista.com/statistics/218089/global-market-share-of-windows-7/.
 * @author Ante Grbeša
 *
 */
@WebServlet(name="rpimage", urlPatterns={"/reportImage"})
public class ReportImage extends HttpServlet {

	/**
	 * Serial UID. 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		
		PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, "OS usage");
        
        BufferedImage img = chart.createBufferedImage(500, 250);
        ImageIO.write(img, "png", resp.getOutputStream());
	}

	/**
	 * Creates chart with given data and title. 
	 * @param dataset data to use
	 * @param title title 
	 * @return created chart
	 */
	protected static JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(
	            title,                  
	            dataset,               
	            true,                   
	            true,
	            false
	        );

	        PiePlot3D plot = (PiePlot3D) chart.getPlot();
	        plot.setStartAngle(290);
	        plot.setDirection(Rotation.CLOCKWISE);
	        plot.setForegroundAlpha(0.5f);
	        return chart;
	}

	/**
	 * Creates data set for a chart. 
	 * @return created data set.
	 */
	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 1.53);
		result.setValue("MacOSX", 11.6);
		result.setValue("Windows", 84.14);
		result.setValue("Other", 1.93);
		result.setValue("Chrome OS", 0.83);
		
		
		return result;
	}
	
}
