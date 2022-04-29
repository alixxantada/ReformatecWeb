package com.alejandro.reformatec.web.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.Especializacion;
import com.alejandro.reformatec.model.EspecializacionCriteria;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.service.EspecializacionService;
import com.alejandro.reformatec.service.impl.EspecializacionServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.MimeTypeNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.SessionManager;
import com.google.gson.Gson;


@WebServlet("/especializacion-service")
public class EspecializacionWebServiceServlet extends HttpServlet {       
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(EspecializacionWebServiceServlet.class);

	private EspecializacionService especializacionService = null;
	private Gson gson = null;

	public EspecializacionWebServiceServlet() {
		super();
		especializacionService = new EspecializacionServiceImpl();
		gson = new Gson();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String methodStr = request.getParameter(ParameterNames.ACTION);

		WebServiceResponse wsResponse = new WebServiceResponse();

		
		if (ActionNames.SEARCH_ESPECIALIZACION.equals(methodStr)) {

			try {

				EspecializacionCriteria criteria = new EspecializacionCriteria();
				criteria.setIdEspecializacion(null);
				criteria.setIdProyecto(null);
				criteria.setIdTrabajoRealizado(null);
				criteria.setIdUsuario(null);
				List<Especializacion> especializaciones = especializacionService.findByCriteria(criteria);
				wsResponse.setData(especializaciones);				

				
				String json = gson.toJson(wsResponse);

				// si no sale texto/html hay que indicar el tipo de contenido (MIMETYPE)
				response.setContentType(MimeTypeNames.JSON);
				response.setCharacterEncoding("ISO-8859-1");

				ServletOutputStream sos = response.getOutputStream();												
				sos.write(json.getBytes());

				// se indica el final del json y que envie sus datos con flush
				sos.flush();
				
			} catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de);
				}

			} catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se);
				}
			}

			
			
			
		} else if (ActionNames.SEARCH_USUARIO_ESPECIALIZACION.equals(methodStr)) {

			UsuarioDTO usuario = (UsuarioDTO) SessionManager.get(request, AttributeNames.USUARIO);
			EspecializacionCriteria criteria = new EspecializacionCriteria();
			criteria.setIdUsuario(usuario.getIdUsuario());

			try { 

				List<Especializacion> especializaciones = especializacionService.findByCriteria(criteria);

				String json = gson.toJson(especializaciones);

				// si no sale texto/html hay que indicar el tipo de contenido (MIMETYPE)
				response.setContentType(MimeTypeNames.JSON);
				response.setCharacterEncoding("ISO-8859-1");

				ServletOutputStream sos = response.getOutputStream();												
				sos.write(json.getBytes());

				// se indica el final del json y que envie sus datos con flush
				sos.flush();
				
				
			} catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de);
				}

			} catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se);
				}
			}
		} else if (ActionNames.SEARCH_USUARIO_SIN_ESPECIALIZACION.equals(methodStr)) {

			UsuarioDTO usuario = (UsuarioDTO) SessionManager.get(request, AttributeNames.USUARIO);
			EspecializacionCriteria criteria = new EspecializacionCriteria();
			criteria.setIdUsuarioSin(usuario.getIdUsuario());

			try { 

				List<Especializacion> especializaciones = especializacionService.findByCriteria(criteria);

				String json = gson.toJson(especializaciones);

				// si no sale texto/html hay que indicar el tipo de contenido (MIMETYPE)
				response.setContentType(MimeTypeNames.JSON);
				response.setCharacterEncoding("ISO-8859-1");

				ServletOutputStream sos = response.getOutputStream();												
				sos.write(json.getBytes());

				// se indica el final del json y que envie sus datos con flush
				sos.flush();
				
				
			} catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de);
				}

			} catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se);
				}
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}