package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.FormularForm;

/**
 * Class represents a servlet that is called when a user submits a registration form. If an error is present in form,
 * servlet redirects to registration form with error messages shown in page. 
 * Registration page is {@code Formular.jsp}.
 * @author Ante
 *
 */
@WebServlet("/servleti/save")
public class SaveUserServlet extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Message to show after an account has been created.
	 */
	private static final String SAVE_MESSAGE = "User account successfully created";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}
	
	/**
	 * Method processes a request for new user account.
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException ServletException
	 * @throws IOException IOException
	 */
	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String method = req.getParameter("method");
		if(!"Save".equals(method)) {
			resp.sendRedirect(req.getServletContext().getContextPath()+ "/servleti/main");
			return;
		}

		FormularForm f = new FormularForm();
		f.FillFromHttpRequest(req);
		f.validate();
		
		if(f.hasErrors()) {
			req.setAttribute("form", f);
			req.getRequestDispatcher("/WEB-INF/pages/Formular.jsp").forward(req, resp);
			return;
		}
		
		BlogUser r = new BlogUser();
		f.FillInUser(r);
		DAOProvider.getDAO().addBlogUser(r);
		req.getSession().setAttribute("message", SAVE_MESSAGE);
		
		resp.sendRedirect(req.getServletContext().getContextPath() + "/main");
	}
}
