package hr.fer.zemris.java.webserver;

/**
 * An interface towards any object that can process current request: it gets {@link RequestContext}
 * as parameter and it is expected to create a content for client.
 * @author Ante Grbeša
 *
 */
public interface IWebWorker {

	/**
	 * Processes current request. 
	 * @param context request context 
	 * @throws Exception if an exception occurred
	 */
	public void processRequest(RequestContext context) throws Exception;
}
