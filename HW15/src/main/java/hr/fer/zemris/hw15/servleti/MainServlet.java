package hr.fer.zemris.hw15.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Class represents a main page servlet that prepares data for {@code index.jsp} page.
 * @author Ante
 *
 */
@WebServlet(name = "main", urlPatterns = { "/servleti/main"})
public class MainServlet extends HttpServlet {
	
	/**
	 * Serial uid. 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> authors = DAOProvider.getDAO().getAuthors();
		String message = (String) req.getSession().getAttribute("message");
		if (message == null) {
			message = "";
		}
		
		req.setAttribute("message", message);
		req.setAttribute("authors", authors);
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
