package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that offers options for voting. Generates bands for voting for {@code glasanjeIndex.jsp} page.
 * @author Ante Grbeša
 *
 */
@WebServlet(name="glas", urlPatterns={"/glasanje"})
public class GlasanjeServlet extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");
		Map<String, String> values = Util.getValues(fileName, 0, 1);
	
		req.setAttribute("values", values);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
	
}
