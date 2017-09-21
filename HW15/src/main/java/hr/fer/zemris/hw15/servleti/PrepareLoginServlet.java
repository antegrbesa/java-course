package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.model.LoginForm;

/**
 * Class represents a servlet that prepares data for login form for {@code Login.jsp} page.
 * @author Ante
 *
 */
@WebServlet("/servleti/prepare")
public class PrepareLoginServlet extends HttpServlet{

	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoginForm form = new LoginForm();
		req.setAttribute("form", form);	
		req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
	}
}
