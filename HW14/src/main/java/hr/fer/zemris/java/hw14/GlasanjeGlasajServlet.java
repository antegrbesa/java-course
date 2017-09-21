package hr.fer.zemris.java.hw14;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.DataModel;

/**
 * This servlet processes a vote for selected option from {@link GlasanjeServlet}
 * {@code (glasanjeIndex.jsp)}.
 * 
 * @author Ante Grbeša
 *
 */
@WebServlet(name = "glasgls", urlPatterns = { "/servleti/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {

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

		Long entryID = Long.parseLong(id);
		DataModel model = DAOProvider.getDao().getByID(entryID);
		model.setVotesCount(model.getVotesCount() + 1);
		DAOProvider.getDao().updateOption(model);

		req.setAttribute("pollID", model.getPollID());
		req.getSession().setAttribute("pollID", model.getPollID());
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati");
	}

}
