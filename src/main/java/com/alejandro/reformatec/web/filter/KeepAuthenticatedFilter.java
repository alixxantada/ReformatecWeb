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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.model.UsuarioCriteria;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.CookieManager;
import com.alejandro.reformatec.web.util.SessionManager;

	
	public class KeepAuthenticatedFilter extends HttpFilter implements Filter {
		private static final long serialVersionUID = 1L;
		
		private static Logger logger = LogManager.getLogger(KeepAuthenticatedFilter.class);
		private UsuarioService usuarioService = null;
	   
	    public KeepAuthenticatedFilter() {
	        super();
	        usuarioService = new UsuarioServiceImpl();
	    }


		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			// No autenticado
			UsuarioDTO usuario = (UsuarioDTO) SessionManager.get(httpRequest, AttributeNames.USUARIO);
			
			if (usuario==null) {
			
				String email = CookieManager.getValue(httpRequest, AttributeNames.USUARIO);
				if(!StringUtils.isBlank(email)) {
					
					try {
						//TODO Esto así esta ok?
						UsuarioCriteria uc = new UsuarioCriteria();
						uc.setEmailActivo(email);						
						Results<UsuarioDTO> usuarios = usuarioService.findByCriteria(uc, 1, 1);						
						for(UsuarioDTO u : usuarios.getData()) {
							if(u.getIdUsuario()!=null) {
								usuario = u;
							}
						}
						
						SessionManager.set(httpRequest, AttributeNames.USUARIO, usuario);
						
						if (logger.isInfoEnabled()) {
							logger.info("User "+email+" authenticated form cookie");
						}
						
					} catch (DataException de) {
						logger.error("Trying to login by cookie: "+email, de);
					
					} catch (ServiceException se) {
						logger.error("Trying to login by cookie: "+email, se);
					}
				}
			}// else esta autenticado			
			chain.doFilter(request, response);
		}


		public void init(FilterConfig fConfig) throws ServletException {
		}

	}