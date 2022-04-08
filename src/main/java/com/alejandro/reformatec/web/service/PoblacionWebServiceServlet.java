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
import com.alejandro.reformatec.model.PoblacionDTO;
import com.alejandro.reformatec.service.PoblacionService;
import com.alejandro.reformatec.service.impl.PoblacionServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.MimeTypeNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.google.gson.Gson;

@WebServlet("/poblacion-service")
public class PoblacionWebServiceServlet extends HttpServlet {

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

		// TODO rest style>
		if (ActionNames.SEARCH_POBLACION.equals(methodStr)) {

			String provinciaIdStr = request.getParameter(ParameterNames.PROVINCIA_ID);

			try {

				Integer provinciaId = Integer.valueOf(provinciaIdStr);

				List<PoblacionDTO> poblaciones = poblacionService.findByProvincia(provinciaId);

				wsResponse.setData(poblaciones);

			} catch (DataException de) {
				logger.error("provinciaId: "+provinciaIdStr, de);

			} catch (ServiceException se) {
				logger.error("provinciaId: "+provinciaIdStr, se);
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}