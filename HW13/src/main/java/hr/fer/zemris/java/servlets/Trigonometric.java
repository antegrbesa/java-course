package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generates a table containing values of trigonometric functions sin and cosine for all integer angles 
 * (in degrees) in a range determined by URL parameters a and b.
 * @author Ante Grbeša
 *
 */
@WebServlet(name="trig", urlPatterns={"/trigonometric"})
public class Trigonometric extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = null;
		Integer b = null;
		
		try { 
			a = Integer.valueOf(req.getParameter("a"));
			b =  Integer.valueOf(req.getParameter("b"));
		} catch (ClassCastException | NumberFormatException ignorable) {}
		
		if (a == null) {
			a = 0;
		}
		if (b == null) {
			b = 360;
		}
		
		if (a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		} else if (b > a + 720) {
			b = a + 720;
		}
		
		List<TrigPair> results = new ArrayList<>();
		for (int i = a; i <= b; i++) {
			results.add(new TrigPair(i));
		}
		
		req.setAttribute("results", results);
		
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	/**
	 * Represents a container for trigonometric values (sin and cos) of  given angle.
	 * @author Ante Grbeša
	 *
	 */
	public static class TrigPair { 
		/**Angle to use*/
		Integer angle;
		
		/**Sin value of angle*/
		Double sin;
		
		/**Cos value of angle*/
		Double cos;

		/**
		 * Constructs an instance of this class.
		 * @param angle angle to use
		 */
		public TrigPair(Integer angle) {
			super();
			this.angle = angle;
			sin = Math.sin(angle.doubleValue());
			cos = Math.cos(angle.doubleValue());
		}
		
		/**
		 * Gets the angle.
		 * @return angle
		 */
		public Integer getAngle() {
			return angle;
		}
		
		/**
		 * Gets the sin value.
		 * @return sin value
		 */
		public Double getSin() {
			return sin;
		}
		
		/**
		 * Gets the cos value.
		 * @return cos value
		 */
		public Double getCos() {
			return cos;
		}
		
	}
}
