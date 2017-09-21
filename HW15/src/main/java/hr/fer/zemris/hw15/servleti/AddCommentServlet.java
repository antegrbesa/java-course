package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.CommentForm;

/**
 * Class represents a servlet that is called when user posts a comment on a blog.
 * @author Ante
 *
 */
@WebServlet("/servleti/comment")
public class AddCommentServlet extends HttpServlet {

	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Message to show after comment was added.
	 */
	private final static String LOGIN_MESSAGE = "Comment added";
	
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
	 * Processes current request to add comment.
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException if exception occurrs
	 * @throws IOException if exception occurrs
	 */
	private void process(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String method = req.getParameter("method");
		if(!"Save".equals(method)) {
			resp.sendRedirect(req.getServletContext().getContextPath()+ "/servleti/main");
			return;
		}
		
		CommentForm f = new CommentForm();
		f.fillFromHttpRequest(req);
		String nick = (String) req.getSession().getAttribute("user_nick");
		if (nick != null) {
			BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
			f.setUsersEMail(user.getEmail());
		}
		
		f.validate();
		
		if(f.hasErrors()) {
			req.setAttribute("form", f);
			req.getRequestDispatcher("/WEB-INF/pages/ShowBlog.jsp").forward(req, resp);
			return;
		}
		
		BlogComment comment = new BlogComment();
		f.fillComment(comment);	
		String eid = (String) req.getSession().getAttribute("eid");
		if (eid == null) {
			resp.sendRedirect(req.getServletContext().getContextPath()+ "/servleti/main");
		}
		
		DAOProvider.getDAO().getBlogEntry(Long.parseLong(eid)).addComment(comment);
		
		req.getSession().setAttribute("message", LOGIN_MESSAGE);
		resp.sendRedirect(req.getServletContext().getContextPath() + "/main");
		
	}
}
