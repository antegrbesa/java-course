package hr.fer.zemris.java.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Generates an image with pie chart using voting results for {@code glasanjeRez.jsp} page. 
 * @author Ante
 *
 */
@WebServlet(name="glasgraf", urlPatterns={"/glasanje-grafika"})
public class GlasanjeGrafika extends HttpServlet {

	
	/**
	 * Serial UId.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileNameRes = req.getServletContext().getRealPath("WEB-INF/glasanje-rezultati.txt");
		String fileNameDef = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");
		
		Map<String, Integer> resultingMap = Util.getResultingMap(fileNameRes, fileNameDef, req);
		
		resp.setContentType("image/png");
		
		PieDataset dataset = createDataset(resultingMap);
        JFreeChart chart = ReportImage.createChart(dataset, "Band voting results");
        
        BufferedImage img = chart.createBufferedImage(500, 250);
        ImageIO.write(img, "png", resp.getOutputStream());
	}
	
	/**
	 * Creates data set using voting results. 
	 * @param resultingMap map with information to use
	 * @return generated data
	 */
	private PieDataset createDataset(Map<String, Integer> resultingMap) {
		DefaultPieDataset result = new DefaultPieDataset();
		for (Map.Entry<String, Integer> entry : resultingMap.entrySet()) {
			result.setValue(entry.getKey(), entry.getValue());
		}
		
		
		return result;
	}
}
