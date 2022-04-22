package com.alejandro.reformatec.web.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.ProyectoCriteria;
import com.alejandro.reformatec.model.ProyectoDTO;
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.service.ProyectoService;
import com.alejandro.reformatec.service.impl.ProyectoServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.MimeTypeNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.google.gson.Gson;


@WebServlet("/proyecto-service")
public class ProyectoWebServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	private static Logger logger = LogManager.getLogger(ProyectoWebServiceServlet.class);

	private ProyectoService proyectoService = null;
	private Gson gson = null;

	public ProyectoWebServiceServlet() {
		super();
		proyectoService = new ProyectoServiceImpl();
		gson = new Gson();
	}

	// TODO sacar de configuracion
	private static int PAGE_SIZE = 8; 
	private static int START_INDEX = 1;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String methodStr = request.getParameter(ParameterNames.ACTION);

		WebServiceResponse wsResponse = new WebServiceResponse();

		// TODO rest style>
		if (ActionNames.SEARCH_PROYECTO.equals(methodStr)) {

			String descripcionStr = request.getParameter(ParameterNames.BUSCAR_DESCRIPCION);

			ProyectoCriteria criteria = new ProyectoCriteria();
			
			//Validar datos
			if (!StringUtils.isBlank(descripcionStr)) {
				// aquí hay mucho que hacer, ver txt
				descripcionStr.trim();
				criteria.setDescripcion(descripcionStr);
			}
			
			
			if (logger.isTraceEnabled()) {
				logger.trace("Busqueda usuario: "+criteria);
			}
			
			
			try {

				Results<ProyectoDTO> proyectos = proyectoService.findByCriteria(criteria, START_INDEX, PAGE_SIZE);
				wsResponse.setData(proyectos);				

			} catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de);
				}

			} catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se);
				}
			}

			String json = gson.toJson(wsResponse);

			// si no sale texto/html hay que indicar el tipo de contenido (MIMETYPE)
			response.setContentType(MimeTypeNames.JSON);
			response.setCharacterEncoding("ISO-8859-1");

			ServletOutputStream sos = response.getOutputStream();												
			sos.write(json.getBytes());

			// se indica el final del json y que envie sus datos con flush
			sos.flush();
		}
	}




	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}