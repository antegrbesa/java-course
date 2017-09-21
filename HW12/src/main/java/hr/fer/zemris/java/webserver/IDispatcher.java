package hr.fer.zemris.java.webserver;

/**
 * Interface for a dispatcher for {@link SmartHttpServer}.
 * @author Ante Grbeša
 *
 */
public interface IDispatcher {

	/**
	 * This method is called once a dispatch is requested. 
	 *  It analyzes given path and acts accordingly. 
	 * @param urlPath path
	 * @throws Exception thrown if an exception occurred
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
