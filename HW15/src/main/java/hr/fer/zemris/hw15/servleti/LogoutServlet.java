package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class represents a servlet that is called once a logout request is called. 
 * @author Ante
 *
 */
@WebServlet(urlPatterns = {"/servleti/logout"})
public class LogoutServlet extends HttpServlet{

	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();	
		resp.sendRedirect(req.getServletContext().getContextPath()+ "/servleti/main");
	}
}
