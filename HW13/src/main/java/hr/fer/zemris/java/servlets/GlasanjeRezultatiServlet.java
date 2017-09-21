package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that generates voting data for {@code glasanjeRez.jsp} page and redirects to it.  
 * @author Ante Grbeša
 *
 */
@WebServlet(name="glasrez", urlPatterns={"/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 2685996724289435950L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileNameRes = req.getServletContext().getRealPath("WEB-INF/glasanje-rezultati.txt");
		String fileNameDef = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");
		
		Map<String, Integer> resultingMap = Util.getResultingMap(fileNameRes, fileNameDef, req);
		req.setAttribute("votes", resultingMap);
		
		Map<String, String> links = Util.getValues(fileNameDef, 1, 2);
		
		List<String> lowLinks = new ArrayList<>();
		Integer max = null;
		for (Map.Entry<String, Integer> entry  : resultingMap.entrySet()) {
			if (max == null) {
				max = entry.getValue();
				continue;
			}
			
			if (max > entry.getValue()) {
				lowLinks.add(entry.getKey());
			}
		}
		
		lowLinks.forEach((l) -> { 
			links.remove(l);
		});
		
		req.setAttribute("links", links);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}	
}
