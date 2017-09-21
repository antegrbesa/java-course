package hr.fer.zemris.java.hw14;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.DataModel;
import hr.fer.zemris.java.hw14.model.PollModel;

/**
 * Servlet that offers options for voting. Generates a poll for voting for
 * {@code glasanjeIndex.jsp} page.
 * 
 * @author Ante Grbeša
 *
 */
@WebServlet(name = "glasaj", urlPatterns = { "/servleti/glasanje" })
public class GlasanjeServlet extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		if (id == null) {
			return;
		}

		Long pollID = Long.parseLong(id);
		List<DataModel> data = DAOProvider.getDao().getPollData(pollID);

		List<PollModel> polls = DAOProvider.getDao().getPolls();
		PollModel poll = null;
		for (PollModel p : polls) {
			if (p.getId() == pollID) {
				poll = p;
				break;
			}
		}
		if (poll == null) {
			return;
		}

		req.setAttribute("poll", poll);
		req.setAttribute("data", data);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}

}
