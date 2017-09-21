package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class represents a servlet that redirects request to main servlet located at ../servleti/main.
 * @author Ante
 *
 */
@WebServlet(name = "index", urlPatterns = { "/index.jsp", "/"})
public class Index extends HttpServlet {

	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("servleti/main");
	}
}
