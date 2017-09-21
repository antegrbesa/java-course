package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Generates an excel table (.xls) using voting data for {@code glasanjeRez.jsp} page. 
 * @author Ante Grbeša
 *
 */
@WebServlet(name="glasxls", urlPatterns={"/glasanje-xls"})
public class GlasanjeXLS extends HttpServlet{

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4279820507557267417L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileNameRes = req.getServletContext().getRealPath("WEB-INF/glasanje-rezultati.txt");
		String fileNameDef = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");
		
		Map<String, Integer> resultingMap = Util.getResultingMap(fileNameRes, fileNameDef, req);
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		createExcel(resultingMap, hwb);
		resp.setContentType("application/vnd.ms-excel");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

	/**
	 * Creates excel sheet. 
	 * @param resultingMap map with data to use
	 * @param hwb main excel document
	 */
	private void createExcel(Map<String, Integer> resultingMap, HSSFWorkbook hwb) {
		HSSFSheet sheet =  hwb.createSheet("Results");
		int i = 0;
		for (Map.Entry<String, Integer> entry : resultingMap.entrySet()) {
			HSSFRow row =   sheet.createRow((short)i);
			row.createCell((short) 0).setCellValue(entry.getKey());
			row.createCell((short) 1).setCellValue(entry.getValue());
			i++;
		}
		
	}
}
