package hr.fer.zemris.java.hw14;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

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

import hr.fer.zemris.java.hw14.model.DataModel;

/**
 * Generates an image with pie chart using voting results for
 * {@code glasanjeRez.jsp} page.
 * 
 * @author Ante
 *
 */
@WebServlet(name = "glasgraf", urlPatterns = { "servleti/glasanje-grafika" })
public class GlasanjeGrafika extends HttpServlet {

	/**
	 * Serial UId.
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<DataModel> data = (List<DataModel>) req.getSession().getAttribute("data");

		resp.setContentType("image/png");

		PieDataset dataset = createDataset(data);
		JFreeChart chart = createChart(dataset, "Results of poll:");

		BufferedImage img = chart.createBufferedImage(500, 250);
		ImageIO.write(img, "png", resp.getOutputStream());
	}

	/**
	 * Creates data set using voting results.
	 * 
	 * @param data
	 *            map with information to use
	 * @return generated data
	 */
	private PieDataset createDataset(List<DataModel> data) {
		DefaultPieDataset result = new DefaultPieDataset();
		for (DataModel model : data) {
			result.setValue(model.getOptionTitle(), model.getVotesCount());
		}

		return result;
	}

	/**
	 * Creates chart with given data and title.
	 * 
	 * @param dataset
	 *            data to use
	 * @param title
	 *            title
	 * @return created chart
	 */
	private static JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}
}
