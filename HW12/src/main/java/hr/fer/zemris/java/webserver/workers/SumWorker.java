package hr.fer.zemris.java.webserver.workers;



import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 *  Implementation of a {@link IWebWorker}.  It accepts two parameters: a and b.
 *  If user passed something that is not integer, or if user did not send any of them, it uses the default value of  1
 * for a and 2 for b. . This worker will calculate their sum and write it. 
 * @author Ante Grbeša
 *
 */
public class SumWorker implements IWebWorker {

	/**First value*/
	private Integer aValue = 1;
	
	/**Second value*/
	private Integer bValue = 2;
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String a = context.getParameter("a");
		String b = context.getParameter("b");
		try {
			aValue = a == null ? 1 : Integer.parseInt(a);
			bValue = b == null ? 2 : Integer.parseInt(b);
		} catch (NumberFormatException e) {
			System.out.println("Error: "+e.getMessage());
		}
		
		Integer result = aValue + bValue;
		context.setTemporaryParameter("a", aValue.toString());
		context.setTemporaryParameter("b", bValue.toString());
		context.setTemporaryParameter("zbroj", result.toString());
		
		context.setMimeType("text/html");
		context.getDispatcher().dispatchRequest("/private/calc.smscr");
	}

}
