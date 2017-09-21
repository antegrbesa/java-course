package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 *  Implementation of a {@link IWebWorker}. It writes all parameters that were given in request. 
 * @author Ante Grbeša
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/plain");
		StringBuilder sb = new StringBuilder();
		context.getParameterNames().forEach((p)-> { 
			sb.append(p);
			sb.append("=");
			sb.append(context.getParameter(p));
			sb.append("\n");
		});

		context.write(sb.toString());
	}

}
