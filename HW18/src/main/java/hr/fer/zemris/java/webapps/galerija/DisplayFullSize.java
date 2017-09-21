package hr.fer.zemris.java.webapps.galerija;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that displays a full size image.
 * 
 * @author Ante 
 *
 */
@WebServlet(urlPatterns="/fullSizePicture")
public class DisplayFullSize extends HttpServlet {
	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = 4189476563655315018L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		Path originPath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/pictures"), name);
		BufferedImage original = null;
		original = ImageIO.read(originPath.toFile());
		if(original == null) {
			resp.sendError(500);
		}
		resp.setContentType("image/png");
		ImageIO.write(original, "png", resp.getOutputStream());
		
	}
}
