package hr.fer.zemris.java.webapps.galerija;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener that loads all initial data required for application like tags and photo information.
 * 
 * @author Ante
 *
 */
@WebListener
public class AppInitFilter implements ServletContextListener {

	/** Key under which available photo tags are stored in servlet's context. */
	public static final String PHOTO_TAGS_KEY = "tags";
	/**
	 * Key under which information about available photos is stored in servlet's
	 * context.
	 */
	public static final String PHOTO_INFO_KEY = "photos";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Path path = Paths.get(sce.getServletContext().getRealPath("/WEB-INF/opisnik.txt"));

		List<String> lines = null;
		try {
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Exception occurred during photo info retrieval.");
		}

		List<PhotoInfo> photoInfo = parsePhotoInformation(lines);
		sce.getServletContext().setAttribute(PHOTO_INFO_KEY, photoInfo);

		Map<String, List<PhotoInfo>> tags = new HashMap<>();
		for (PhotoInfo photo : photoInfo) {
			for (String tag : photo.getTags()) {

				if (tags.containsKey(tag)) {
					tags.get(tag).add(photo);
				} else {
					List<PhotoInfo> photos = new ArrayList<>();
					photos.add(photo);
					tags.put(tag, photos);
				}

			}
		}

		sce.getServletContext().setAttribute(PHOTO_TAGS_KEY, tags);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}

	/**
	 * Parses document lines retrieved from file with information about photos.
	 * Information about one photo needs to consist of three lines in following
	 * order and meaning:
	 * <ul>
	 * <li>full photo name with extension</li>
	 * <li>photo description</li>
	 * <li>tags associated with photos</li>
	 * </ul>
	 *
	 * @param lines document lines retrieved from file with information about
	 * photos 
	 * @return retrieved information as collection of {@link PhotoInfo} objects
	 */
	private List<PhotoInfo> parsePhotoInformation(List<String> lines) {
		List<PhotoInfo> result = new ArrayList<>();

		for (int i = 0, j = lines.size(); i + 2 < j; i += 3) {

			String photoName = lines.get(i);
			String photoDescription = lines.get(i + 1);
			String[] photoTagsUnedited = lines.get(i + 2).split(",");
			List<String> photoTags = new ArrayList<>();

			for (String tag : photoTagsUnedited) {
				photoTags.add(tag.trim());
			}

			result.add(new PhotoInfo(photoName, photoDescription, photoTags));
		}

		return result;
	}

	/**
	 * Encapsulates information that gallery has about one photograph.
	 *
	 * @author Ante 
	 */
	public static class PhotoInfo {

		/** Photograph's name. */
		private String name;
		
		/** Photograph's description. */
		private String description;
		
		/** Tags associated with photograph. */
		private List<String> tags;

		/**
		 * Constructor receives all necessary photo information.
		 *
		 * @param name photograph's name
		 * @param description photograph's description
		 * @param tags tag tags associated with photograph
		 */
		public PhotoInfo(String name, String description, List<String> tags) {
			this.name = name;
			this.description = description;
			this.tags = tags;
		}

		/**
		 * Photograph's name.
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Photograph's description.
		 * 
		 * @return description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Tags associated with photograph.
		 * 
		 * @return photograph tags
		 */
		public List<String> getTags() {
			return Collections.unmodifiableList(tags);
		}
	}
}
