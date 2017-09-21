package hr.fer.zemris.java.hw16.frame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.Circle;
import hr.fer.zemris.java.hw16.jvdraw.model.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.Line;
import hr.fer.zemris.java.hw16.jvdraw.model.Rectangle;

/**
 * Utility class that offers methods for file manipulation (open and save a file, export image)
 * for class {@link JVDraw}.
 * @author Ante
 *
 */
public class FileUtil {

	/**
	 * Locale used for writing files.
	 */
	public static final Locale DEFAULT_LOCALE = Locale.US;
	
	/**
	 * Number format.
	 */
	private static NumberFormat nf = NumberFormat.getInstance(DEFAULT_LOCALE);
	
	/**
	 * Loads  model from a file.
	 * @param model {@link DrawingModel} reference
	 */
	public static void openFile(DrawingModel model) {
		JFileChooser fc = new JFileChooser("./");
		int status = fc.showOpenDialog(null);
		if (status == JFileChooser.OPEN_DIALOG) {
			File selected = fc.getSelectedFile();
			if (!Files.isRegularFile(selected.toPath())) {
				return;
			}
			readModelFromFile(selected, model);
		}
	}

	/**
	 * Gets the location of selected file to save.
	 * @return location of file to save
	 */
	public static File getSaveFile() {
		JFileChooser fc = new JFileChooser("./");
		fc.setFileFilter(new FileNameExtensionFilter("JVD (*.jvd)", ".jvd"));
		int status = fc.showSaveDialog(null);
		if (status == JFileChooser.OPEN_DIALOG) {
			return setExtension(fc.getSelectedFile(), ".jvd") ;
		}
		return null;
	}

	/**
	 * Reads content from file and fills {@link DrawingModel} with its content.
	 * @param f source file
	 * @param model model to fill
	 */
	private static void readModelFromFile(File f, DrawingModel model) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(
					f.toPath(),
					StandardCharsets.UTF_8
			);
		} catch (IOException e) {
			return;
		}
		model.clear();
		if (lines != null) {
			int id = 0;
			for (String line : lines) {
				String[] parts = line.split("\\s+");
				GeometricalObject object = null;
				try {
					if (parts[0].equals("LINE")) {
						object = new Line(
								"Line " + id++,
								new Point(Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim())),
								new Point(Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[4].trim())),
								new Color(Integer.parseInt(parts[5].trim()), Integer.parseInt(parts[6].trim()), Integer.parseInt(parts[7].trim()))
						);

					} else if (parts[0].equals("CIRCLE")) {
						object = new Circle(
								"Circle " + id++,
								new Point(Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim())),
								nf.parse(parts[3].trim()).doubleValue(),
								new Color(Integer.parseInt(parts[4].trim()), Integer.parseInt(parts[5].trim()), Integer.parseInt(parts[6].trim()))
						);
					} else if (parts[0].equals("FCIRCLE")) {
						object = new FilledCircle(
								"Circle " + id++,
								new Point(Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim())),
								nf.parse(parts[3].trim()).doubleValue(),
								new Color(Integer.parseInt(parts[4].trim()), Integer.parseInt(parts[5].trim()), Integer.parseInt(parts[6].trim())),
								new Color(Integer.parseInt(parts[7].trim()), Integer.parseInt(parts[8].trim()), Integer.parseInt(parts[9].trim()))
						);
					}
				} catch (NumberFormatException | ParseException ignorable) {
				}
				if (object == null) continue; 
				model.add(object);
			}
		}
	}

	/**
	 * Method that saves given model to given file.
	 * @param f file to save model at
	 * @param model model to save
	 */
	public static void saveModelToFile(File f, DrawingModel model) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(
							new BufferedOutputStream(
									new FileOutputStream(f)),
							StandardCharsets.UTF_8)
					);
		} catch (FileNotFoundException ignorable) {
		}
		if (writer == null) { return; }
		for (int i = 0; i < model.getSize(); i++) {
			String description = model.getObject(i).getDescription();
			try {
				writer.write(description);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error occurred while saving image", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		try { 
			writer.close();
		} catch (IOException ignorable) {}
		
		JOptionPane.showMessageDialog(null, "Image successfully created!", "Success",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Exports given model to user-defined image. 
	 * @param model model to export 
	 * @param box reference to {@link Rectangle} object.
	 */
	public static void exportModel(DrawingModel model, Rectangle box) {
		/**
		 * Treba pronaći xmin, xmax, ymin, ymax.
		 */
		for (int i = 0; i < model.getSize(); i++) {
			model.getObject(i).findBoundingBox(box);
		}

		JFileChooser fc = new JFileChooser("./");

		FileNameExtensionFilter png = new FileNameExtensionFilter("PNG (*.png)", ".png");
		FileNameExtensionFilter gif = new FileNameExtensionFilter("GIF (*.gif)", ".gif");
		FileNameExtensionFilter jpg = new FileNameExtensionFilter("JPG (*.jpg)", ".jpg");

		fc.addChoosableFileFilter(png);
		fc.addChoosableFileFilter(gif);
		fc.addChoosableFileFilter(jpg);
		fc.setFileFilter(png);
		int status = fc.showSaveDialog(null);

		if (status != JFileChooser.OPEN_DIALOG) return;
		File f = fc.getSelectedFile();

		Rectangle boundingBox = model.getBoundingBox();
		BufferedImage image = null;
		try {
			image = new BufferedImage(boundingBox.getWidth(), boundingBox.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, boundingBox.getWidth(), boundingBox.getHeight());

			for (int i = 0, n = model.getSize(); i < n; i++) {
				model.getObject(i).draw(g, boundingBox);
			}
			g.dispose();
		} catch (Exception e) {
			return;
		}
		try {
			if (fc.getFileFilter().equals(png)) {
				f = setExtension(f, ".png");
				ImageIO.write(image, "png", f);
			} else if (fc.getFileFilter().equals(gif)) {
				f = setExtension(f, ".gif");
				ImageIO.write(image, "gif", f);
			} else if (fc.getFileFilter().equals(jpg)) {
				f = setExtension(f, ".jpg");
				ImageIO.write(image, "jpg", f);
			}
		} catch (Exception ignorable) {
			JOptionPane.showMessageDialog(null, "Error occurred while saving image", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		JOptionPane.showMessageDialog(null, "Image successfully created!", "Success",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Sets the extension to given file.
	 * @param f fle 
	 * @param extension extension to set
	 * @return file with specified extension
	 */
	private static File setExtension(File f, String extension) {
		if (f.getAbsoluteFile().toString().endsWith(extension) || 
				f.getAbsoluteFile().toString().endsWith(extension.toUpperCase())) {
			return f;
		}
		return new File(f.getAbsolutePath() + extension);
}
}
