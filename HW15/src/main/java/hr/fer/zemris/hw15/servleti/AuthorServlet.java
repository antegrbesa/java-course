package hr.fer.zemris.hw15.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogForm;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Class represents a servlet that is called everytime users request an /author action. 
 * @author Ante Grbeša
 *
 */
@WebServlet(urlPatterns="/servleti/author/*")
public class AuthorServlet extends HttpServlet{

	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Message to show after action wsa successfully peformed.
	 */
	private static final String SAVE_MESSAGE = "Blog successfully created.";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null) {
			resp.sendRedirect(req.getServletContext().getContextPath()+ "/servleti/main");
			return;
		} else if (pathInfo.lastIndexOf("/") == 0) {	//if given url is something like servleti/author/foo
			redirectToRoot(pathInfo, req, resp);
		} else if (pathInfo.endsWith("/new") && countOccurrences(pathInfo, '/') == 2) {
			redirectToManipulation(pathInfo, req, resp, "new");
		} else if (pathInfo.endsWith("/edit") && countOccurrences(pathInfo, '/') == 2) {
			redirectToManipulation(pathInfo, req, resp, "edit");
		} else if (countOccurrences(pathInfo, '/') == 2) {
			redirectToBlogPage(pathInfo, req, resp);
		} else {
			redirectError(resp, req);
		}

		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo.endsWith("/update") && countOccurrences(pathInfo, '/') == 2) {
			redirectToCreate(req, resp, pathInfo);
		}
	}
	
	/**
	 * Processess a request to add or edit a blog from a form page.
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param pathInfo path info
	 * @throws IOException -
	 * @throws ServletException -
	 */
	private void redirectToCreate(HttpServletRequest req, HttpServletResponse resp, String pathInfo) 
			throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		
		String method = req.getParameter("method");
		if(!"Save".equals(method)) {
			resp.sendRedirect(req.getServletContext().getContextPath()+ "/servleti/main");
			return;
		}
		
		BlogForm f = new BlogForm();
		try {
			f.fillFromHttpRequest(req);
		} catch (IllegalArgumentException e) {
			redirectError(resp, req);
		}
		
		f.validate();
		
		if(f.hasErrors()) {
			req.setAttribute("form", f);
			req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp);
			return;
		}
		
		BlogEntry e = new BlogEntry();
		f.fillEntry(e);
		String eid = (String) req.getSession().getAttribute("eid");
		if (eid != null && (boolean) req.getSession().getAttribute("edit")) {	//if user is editing existing blog
			e.setId(Long.parseLong(eid));
		}
		
		DAOProvider.getDAO().addBlogEntry(e);
		req.getSession().setAttribute("message", SAVE_MESSAGE);
		
		resp.sendRedirect(req.getServletContext().getContextPath() + "/main");			
	}

	/**
	 * Used to process a request to show a certain blog. 
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param pathInfo path info
	 * @throws IOException -
	 * @throws ServletException -
	 */
	private void redirectToBlogPage(String pathInfo, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String[] values = pathInfo.replaceFirst("/", "").split("/");
		if (values.length != 2) {
			redirectError(resp, req);
		}
		
		String nick = values[0];
		String id = values[1];
		Long eid;
		try {
			eid = Long.parseLong(id);
		} catch (NumberFormatException e) {
			redirectError(resp, req);
			return;
		}
		
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(eid);
		
		req.setAttribute("blogEntry", entry);
		req.getSession().setAttribute("blogEntry", entry);
		req.setAttribute("nick", nick);
		req.getSession().setAttribute("nick", nick);
		req.setAttribute("eid", id);
		req.getSession().setAttribute("eid", id);
		req.getRequestDispatcher("/WEB-INF/pages/ShowBlog.jsp").forward(req, resp); 	 
	}

	/**
	 * Used to process a request to create or edit a blog. It redirects to a form page
	 * that is later processed by {@link #redirectToCreate(HttpServletRequest, HttpServletResponse, String)} method.
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param pathInfo path info
	 * @param oper operation to perform 
	 * @throws IOException -
	 * @throws ServletException -
	 */
	private void redirectToManipulation(String pathInfo, HttpServletRequest req
			, HttpServletResponse resp, String oper) throws IOException, ServletException {
		String[] values = pathInfo.replaceFirst("/", "").split("/");
		if (values.length != 2) {
			redirectError(resp, req);
		}
		String nick = values[0];
		String currentNick = (String) req.getSession().getAttribute("user_nick");
		if (currentNick == null) {
			redirectError(resp, req);
		} else if (! nick.equals(currentNick)) {
			redirectError(resp, req);
		}
		
		req.setAttribute("nick", nick);
		
		BlogForm form = new BlogForm();
		
		if (oper.equals("new")) {
			req.getSession().setAttribute("edit", false);
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp); 	
		} else {	//else update existing blog
			String id = (String) req.getSession().getAttribute("eid");
			if (id == null) {
				redirectError(resp, req);
			}
			Long eid;
			try {
				eid = Long.parseLong(id);
			} catch (NumberFormatException e) {
				redirectError(resp, req);
				return;
			}
			
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(eid);
			if (entry == null) {
				redirectError(resp, req);
			}
			form.fillFromEntry(entry);
			
			req.setAttribute("form", form);
			req.setAttribute("eid", id);
			req.getSession().setAttribute("eid", id);
			req.getSession().setAttribute("edit", true);
			req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp); 	
		}
		
	}

	/**
	 * Redirects to an error page. 
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws IOException -
	 * @throws ServletException -
	 */
	private void redirectError(HttpServletResponse resp,  HttpServletRequest req)
			throws IOException, ServletException {
		req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp); 	
		
	}

	/**
	 * Redirects to authors' root page.
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param pathInfo path info
	 * @throws IOException -
	 * @throws ServletException -
	 */
	private void redirectToRoot(String pathInfo, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String nick = pathInfo.replaceFirst("/", "");
		
		BlogUser user;
		try {
			user = DAOProvider.getDAO().getBlogUserByNick(nick);
		} catch (IllegalArgumentException e) {
			redirectError(resp, req);
			return;
		}
		
		
		req.setAttribute("entries", user.getBlogs());
		req.setAttribute("nick", nick);
		req.getRequestDispatcher("/WEB-INF/pages/Root.jsp").forward(req, resp); 	
	}

	/**
	 * Counts occurrences of a given character in string. 
	 * @param url String to use
	 * @param target char to compare
	 * @return number of occurrences
	 */
	private static int countOccurrences(String url, char target) {
	    int count = 0;
	    for (int i=0; i < url.length(); i++)  {
	        if (url.charAt(i) == target)  {
	             count++;
	        }
	    }
	    
	    return count;
	}
}
