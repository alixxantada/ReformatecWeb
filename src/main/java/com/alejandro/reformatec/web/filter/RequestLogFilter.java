package com.alejandro.reformatec.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet Filter implementation class RequestLogFilter
 */

public class RequestLogFilter extends HttpFilter implements Filter {

	private static Logger logger = LogManager.getLogger(RequestLogFilter.class); 

	public RequestLogFilter() {
		super();
	}



	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;		
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		logger.debug("Request from "+httpRequest.getRemoteAddr()+"("+httpRequest.getRemoteHost()+"): "+httpRequest.getRequestURL());

		Map<String, String[]> params = httpRequest.getParameterMap();
		for (String pname: params.keySet()) {
			logger.debug("parameter: "+pname+": "+Arrays.toString(params.get(pname)));
		}



		// pass the request along the filter chain
		chain.doFilter(request, response);
	}


	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void destroy() {
	}

}
