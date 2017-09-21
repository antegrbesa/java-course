package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 *  Implementation of a {@link IWebWorker}.  It will produce an PNG image with 
 * dimensions 200x200 and with a single filled circle. 
 * @author Ante Grbeša
 *
 */
public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bim.createGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, bim.getWidth(), bim.getHeight());	
		g.setColor(Color.YELLOW);
		g.fillOval(0, 0, bim.getWidth(), bim.getHeight());
		g.setColor(Color.GREEN);
		
		g.dispose();
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(bim, "png", output);
		output.flush();
		context.setContentLength((long) output.size()); 
		context.setMimeType("image/png");
		context.write(output.toByteArray());
		
		
	}

	
}
