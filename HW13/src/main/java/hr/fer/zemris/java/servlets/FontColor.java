package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet is used by funny.jsp page for generating random font color. 
 * @author Ante Grbeša
 *
 */
@WebServlet(name="fny", urlPatterns={"/funny"})
public class FontColor extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> colors = new ArrayList<>();
		
		colors.add("red");
		colors.add("blue");
		colors.add("black");
		colors.add("yellow");
		colors.add("grey");
		colors.add("purple");
		colors.add("green");
		
		Random rand = new Random();
		String color = colors.get(rand.nextInt(colors.size()));
		
		req.getSession().setAttribute("color", color);
		req.getRequestDispatcher("/WEB-INF/stories/funny.jsp").forward(req, resp);
	}
}
