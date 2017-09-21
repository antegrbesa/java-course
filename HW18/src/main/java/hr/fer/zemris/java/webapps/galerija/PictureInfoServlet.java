package hr.fer.zemris.java.webapps.galerija;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.webapps.galerija.AppInitFilter.PhotoInfo;

/**
 * Servlet that delivers photo description and tags to be dynamically written.
 * 
 * @author Ante 
 *
 */
@WebServlet(urlPatterns="/getPictureInfo")
public class PictureInfoServlet extends HttpServlet {
	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 5557106642739389297L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		
		List<PhotoInfo> photoInfo = (List<PhotoInfo>) req.getServletContext().getAttribute("photos");
		
		String[] data = new String[2];
		for (PhotoInfo info : photoInfo) {
			if(info.getName().equals(name)) {
				data[0]= info.getDescription();
				data[1] = info.getTags().toString();
			}
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(data);
		resp.getWriter().write(json);
		resp.getWriter().flush();
	}
}
