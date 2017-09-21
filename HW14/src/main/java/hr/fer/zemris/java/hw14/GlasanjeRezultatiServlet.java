package hr.fer.zemris.java.hw14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.DataModel;

/**
 * Servlet that generates voting data for {@code glasanjeRez.jsp} page and
 * redirects to it.
 * 
 * @author Ante Grbeša
 *
 */
@WebServlet(name = "glasrez", urlPatterns = { "servleti/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 2685996724289435950L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long pollID = (Long) req.getSession().getAttribute("pollID");
		if (pollID == null) {
			return;
		}

		List<DataModel> data = DAOProvider.getDao().getPollData(pollID);

		req.setAttribute("data", data);
		req.getSession().setAttribute("data", data);

		long maxVotes = 0;
		for (DataModel m : data) {
			maxVotes = Math.max(m.getVotesCount(), maxVotes);
		}
		List<DataModel> links = new ArrayList<>();
		for (DataModel m : data) {
			if (m.getVotesCount() == maxVotes) {
				links.add(m);
			}
		}

		req.setAttribute("links", links);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
