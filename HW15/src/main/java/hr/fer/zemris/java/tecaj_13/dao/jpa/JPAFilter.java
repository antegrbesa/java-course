package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;


/**
 * This class represents an implementation of a {@link Filter} class. It intercepts every request and applies
 * a filter using {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} method.  
 * @author Ante
 *
 */
@WebFilter("/servleti/*")
public class JPAFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			JPAEMProvider.close();
		}
	}

	@Override
	public void destroy() {
	}
	
}