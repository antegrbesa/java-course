package hr.fer.zemris.java.webapps.galerija;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.webapps.galerija.AppInitFilter.PhotoInfo;

/**
 * Servlet that delivers all tags in form of JSON to frontend.
 * 
 * @author Ante 
 *
 */
@WebServlet(urlPatterns="/getTags")
public class TagServlet extends HttpServlet {
	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 6442289158776003082L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, List<PhotoInfo>> tags = (Map<String, List<PhotoInfo>>) req.getServletContext().getAttribute("tags");
		Set<String> set = tags.keySet();
		String[] tegs = new String[set.size()];
		tegs = tags.keySet().toArray(tegs);
		
		Gson gson = new Gson();
		String json = gson.toJson(tegs);
		
		resp.getWriter().write(json);
		resp.getWriter().flush();
	}
}
