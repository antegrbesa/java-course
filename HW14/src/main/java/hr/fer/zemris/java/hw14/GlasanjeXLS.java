package hr.fer.zemris.java.hw14;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.hw14.model.DataModel;

/**
 * Generates an excel table (.xls) using voting data for {@code glasanjeRez.jsp}
 * page.
 * 
 * @author Ante Grbeša
 *
 */
@WebServlet(name = "glasxls", urlPatterns = { "servleti/glasanje-xls" })
public class GlasanjeXLS extends HttpServlet {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4279820507557267417L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		List<DataModel> data = (List<DataModel>) req.getSession().getAttribute("data");

		HSSFWorkbook hwb = new HSSFWorkbook();
		createExcel(data, hwb);
		resp.setContentType("application/vnd.ms-excel");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

	/**
	 * Creates excel sheet.
	 * 
	 * @param data
	 *            map with data to use
	 * @param hwb
	 *            main excel document
	 */
	private void createExcel(List<DataModel> data, HSSFWorkbook hwb) {
		HSSFSheet sheet = hwb.createSheet("Results");
		int i = 0;
		for (DataModel model : data) {
			HSSFRow row = sheet.createRow((short) i);
			row.createCell((short) 0).setCellValue(model.getOptionTitle());
			row.createCell((short) 1).setCellValue(model.getVotesCount());
			i++;
		}

	}
}
