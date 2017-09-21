package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the background color for web application. 
 * @author Ante
 *
 */
@WebServlet(name="stclr", urlPatterns={"/setcolor"})
public class SetColor  extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = req.getParameter("color");
		if (color == null) {
			color = "white";
		}
		
		req.getSession().setAttribute("pickedBgCol", color);	
		
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
		
		
	}

}
