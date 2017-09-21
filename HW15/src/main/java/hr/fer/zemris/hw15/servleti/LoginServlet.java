package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.LoginForm;

/**
 * Class represents a servlet that is called once a login request is called. 
 * @author Ante
 *
 */
@WebServlet("/servleti/login")
public class LoginServlet extends HttpServlet{

	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Message to show after successful login.
	 */
	private final static String LOGIN_MESSAGE = "Login successful";
	
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
	 * Processes  given login form from given request. 
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException if HttpServletResponse is thrown
	 * @throws IOException if  IOException is thrown
	 */
	private void process(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String method = req.getParameter("method");
		if(!"Login".equals(method)) {
			resp.sendRedirect(req.getServletContext().getContextPath()+ "/servleti/main");
			return;
		}
		
		LoginForm f = new LoginForm();
		f.fillFromHttpRequest(req);
		f.validate();
		
		if(f.hasErrors()) {
			req.setAttribute("form", f);
			req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
			return;
		}
		
		BlogUser currentUser = DAOProvider.getDAO().getBlogUserByNick(f.getNick());
		
		req.getSession().setAttribute("user_id", currentUser.getId());
		req.getSession().setAttribute("user_fn", currentUser.getFirstName());
		req.getSession().setAttribute("user_ln", currentUser.getLastName());
		req.getSession().setAttribute("user_nick", currentUser.getNick());
		req.getSession().setAttribute("message", LOGIN_MESSAGE);
		
		resp.sendRedirect(req.getServletContext().getContextPath() + "/main");
		
	}
	
}
