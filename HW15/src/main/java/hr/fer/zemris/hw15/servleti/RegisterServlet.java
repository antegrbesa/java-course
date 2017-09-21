package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.FormularForm;

/**
 * Class represents a servlet that prepares data for registration form for {@code Formular.jsp} page. 
 * @author Ante
 *
 */
@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet{

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogUser user = new BlogUser();
		FormularForm form = new FormularForm();
		
		form.FillFromBlogUser(user);
		
		req.setAttribute("form", form);
		
		req.getRequestDispatcher("/WEB-INF/pages/Formular.jsp").forward(req, resp);
	}
}
