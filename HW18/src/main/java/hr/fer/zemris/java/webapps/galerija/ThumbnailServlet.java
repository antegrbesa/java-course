package hr.fer.zemris.java.webapps.galerija;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.webapps.galerija.AppInitFilter.PhotoInfo;

/**
 * Servlet that creates thumbnails associated with tag and returns names of pictures that thumbnails have been made of.
 * 
 * @author Ante 
 *
 */
@WebServlet(urlPatterns = "/createThumbnail")
public class ThumbnailServlet extends HttpServlet {
	
	/**
	 * Serial uid.
	 */
	private static final long serialVersionUID = -6243312984608769965L;
	
	/**
	 * Thumbnail width.
	 */
	private static final int THUMBNAIL_WIDTH = 150;
	
	/**
	 * Thumbnail height. 
	 */
	private static final int THUMBNAIL_HEIGHT = 150;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String tag = req.getParameter("tag");
		Map<String, List<PhotoInfo>> tags = (Map<String, List<PhotoInfo>>) req.getServletContext().getAttribute("tags");

		List<PhotoInfo> photoInfos = tags.get(tag);

		for (PhotoInfo info : photoInfos) {

			Path p = Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails"));
			if (!Files.exists(p)) {
				Files.createDirectory(p);
			}
			p = Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails"), "thumbnail-"+info.getName());
			if (!Files.exists(p)) {
				createThumbnail(req, info.getName());
			}
		}

		String[] infos = new String[photoInfos.size()];
		int i = 0;
		for (PhotoInfo info : photoInfos) {
			infos[i++] = info.getName();
		}
		Gson gson = new Gson();
		String text = gson.toJson(infos);
		
		resp.getWriter().write(text);
		resp.getWriter().flush();
	}

	/**
	 * Method that creates a thumbnail of a picture.
	 * 
	 * @param req request
	 * @param pictureName picture name
	 * @throws IOException -
	 */
	private void createThumbnail(HttpServletRequest req, String pictureName) throws IOException {
		Path originPath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/pictures"), pictureName);
		Path destinationPath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails"), "thumbnail-"+pictureName);

		BufferedImage original = null;
		try {
			original = ImageIO.read(originPath.toFile());
		} catch (Exception e) {
			return;
		}

		Image scaledVersion = original.getScaledInstance(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH);
		BufferedImage thumbnail = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		Graphics2D tg = thumbnail.createGraphics();
		tg.drawImage(scaledVersion, 0, 0, null);
		tg.dispose();

		ImageIO.write(thumbnail, "png", destinationPath.toFile());
	}
}
