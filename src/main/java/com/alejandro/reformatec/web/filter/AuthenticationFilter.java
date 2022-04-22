package com.alejandro.reformatec.web.filter;

import java.io.IOException;

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

import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.SessionManager;
import com.alejandro.reformatec.web.util.ViewNames;


public class AuthenticationFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LogManager.getLogger(AuthenticationFilter.class);

	public AuthenticationFilter() {
		super();

	}

	// chain --> llama al siguiente filtro
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		UsuarioDTO usuario = (UsuarioDTO) SessionManager.get(httpRequest, AttributeNames.USUARIO);
		if(usuario==null) {
			/*String url = request.getCharacterEncoding()
					if(!url.contains("/privado/")) {

					}*/
			httpRequest.getRequestDispatcher(ViewNames.USUARIO_LOGIN).forward(httpRequest, httpResponse);
		}else {
			// chain.dofilter es para que pase al siguiente filtro en caso de que lo haya, el propio tomcat tiene un filtro que sería el último a ejecutar
			chain.doFilter(request, response);
		}
	}


	public void init(FilterConfig fConfig) throws ServletException {

	} 


	public void destroy() {

	}

}