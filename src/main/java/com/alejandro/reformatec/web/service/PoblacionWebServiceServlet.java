package com.alejandro.reformatec.web.service;

import java.io.IOException;
import java.util.List;

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
import com.alejandro.reformatec.model.PoblacionCriteria;
import com.alejandro.reformatec.model.PoblacionDTO;
import com.alejandro.reformatec.service.PoblacionService;
import com.alejandro.reformatec.service.impl.PoblacionServiceImpl;
import com.alejandro.reformatec.web.controller.Errors;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.MimeTypeNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.Validator;
import com.google.gson.Gson;

@WebServlet("/poblacion-service")
public class PoblacionWebServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(PoblacionWebServiceServlet.class);

	private PoblacionService poblacionService = null;
	private Gson gson = null; 

	public PoblacionWebServiceServlet() {
		super();
		poblacionService = new PoblacionServiceImpl();
		gson = new Gson();
	}

	//Request peticion / response respuesta
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String methodStr = request.getParameter(ParameterNames.ACTION);

		WebServiceResponse wsResponse = new WebServiceResponse();

		// Errors aqui NO es null, estaria por ahora vacio
		Errors errors = new Errors();
		request.setAttribute(AttributeNames.ERRORS , errors);

		// TODO rest style>
		if (ActionNames.SEARCH_POBLACION.equals(methodStr)) {

			String idProvinciaStr = request.getParameter(ParameterNames.ID_PROVINCIA);

			PoblacionCriteria pc = new PoblacionCriteria();


			//Validar y convertir datos
			Integer idProvincia = null;
			if (!StringUtils.isBlank(idProvinciaStr)) {
				idProvincia = Validator.validaProvincia(idProvinciaStr);

				if(idProvincia!=null) {
					pc.setIdProvincia(idProvincia);				
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idprovincia: "+idProvinciaStr);
					}
					errors.addParameterError(ParameterNames.ID_PROVINCIA, ErroresNames.ERROR_ID_PROVINCIA_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idprovincia: "+idProvinciaStr);
				}
				errors.addParameterError(ParameterNames.ID_PROVINCIA, ErroresNames.ERROR_ID_PROVINCIA_OBLIGATORIO);
			}

			
			if(!errors.hasErrors()) {

				try {

					List<PoblacionDTO> poblaciones = poblacionService.findByCriteria(pc);

					wsResponse.setData(poblaciones);

				} catch (DataException de) {
					if (logger.isErrorEnabled()) {
						logger.error("provinciaId: "+idProvinciaStr, de);
					}

				} catch (ServiceException se) {
					if (logger.isErrorEnabled()) {
						logger.error("provinciaId: "+idProvinciaStr, se);
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
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}