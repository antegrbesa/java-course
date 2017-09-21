package hr.fer.zemris.java.hw14;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollModel;

/**
 * A servlet that generates polls for {@code index.jsp} and forwards request to it. 
 * @author Ante
 *
 */
@WebServlet(name = "pollsRndr", urlPatterns = { "/servleti/index.html" })
public class PollsServlet extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<PollModel> polls = DAOProvider.getDao().getPolls();
		req.setAttribute("polls", polls);

		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
