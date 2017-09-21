package hr.fer.zemris.java.servlets;

import java.time.Instant;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This {@link ServletContextListener} class displays how long is the web application running.
 * @author Ante
 *
 */
@WebListener
public class AppInfo implements ServletContextListener {

	/**Start time of application*/
	private Long startTime;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		startTime = Instant.now().getEpochSecond();
		
		ServletContext ctx = sce.getServletContext();
		ctx.setAttribute("startTime", startTime);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//do nothing 
	}

	/**
	 * Gets the start time of application.
	 * @return start time
	 */
	public Long getStartTime() {
		return startTime;
	}
	
}
