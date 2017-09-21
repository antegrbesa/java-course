package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This servlet generates excel table containing values from a to b and power of those number in n sheets.
 * Parameters a, b and n are given in url. 
 * @author Ante Grbeša
 *
 */
@WebServlet(name="pow", urlPatterns={"/powers"})
public class Powers extends HttpServlet {

	/**
	 * Serial UID. 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = null, b = null, n = null;
		boolean error = false;
		try {
			a =  Integer.parseInt(req.getParameter("a"));
			b = Integer.parseInt(req.getParameter("b"));
			n = Integer.parseInt(req.getParameter("n"));
		} catch (NumberFormatException ex) {
			error = true;
		}
		
		if (error || a == null || b == null || n == null) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		if (a < -100 || a > 100 || b < -100 || b > 100 || n < 1 || n > 5) {
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		createExcel(a, b, n, hwb);
		resp.setContentType("application/vnd.ms-excel");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

	/**
	 * Creates excel table with given values. 
	 * @param a start value
	 * @param b end value
	 * @param n number of sheets
	 * @param hwb base excel file
	 */
	private void createExcel(Integer a, Integer b, Integer n, HSSFWorkbook hwb) {
		for (int i = 1; i <= n; i++) {
			HSSFSheet sheet =  hwb.createSheet("sheet "+i);
			for (int j = a; j <= b; j++) {
				HSSFRow row =   sheet.createRow((short)j);
				row.createCell((short) 0).setCellValue(j);
				row.createCell((short) 1).setCellValue(Math.pow(j, i));
			}
		}
	}
}
