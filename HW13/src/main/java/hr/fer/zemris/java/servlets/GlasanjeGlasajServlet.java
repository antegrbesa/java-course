package hr.fer.zemris.java.servlets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet processes a vote for selected band from {@link GlasanjeServlet} {@code (glasanjeIndex.jsp)}.
 * @author Ante Grbeša
 *
 */
@WebServlet(name="glasgls", urlPatterns={"/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("WEB-INF/glasanje-rezultati.txt");
		String id = req.getParameter("id");
		if (id == null) {
			return;
		}
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		List<String> newValues = new ArrayList<>();
		for (String line : lines) {
			if (line.startsWith(id)) {
				String[] value = line.split("\\t");
				int num = Integer.parseInt(value[1].trim());
				num++;
				line = value[0]+ "	"+num;
			}
			newValues.add(line);
		}
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		for (String l : newValues) {
			try {
				bw.write(l);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bw.close();
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
	
}
