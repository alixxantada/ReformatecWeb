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
import com.alejandro.reformatec.model.Provincia;
import com.alejandro.reformatec.service.ProvinciaService;
import com.alejandro.reformatec.service.impl.ProvinciaServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.MimeTypeNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.google.gson.Gson;


@WebServlet("/provincia-service")
public class ProvinciaWebServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LogManager.getLogger(ProvinciaWebServiceServlet.class);

	private ProvinciaService provinciaService = null;
	private Gson gson = null;

	public ProvinciaWebServiceServlet() {
		super();
		provinciaService = new ProvinciaServiceImpl();
		gson = new Gson();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String methodStr = request.getParameter(ParameterNames.ACTION);

		WebServiceResponse wsResponse = new WebServiceResponse();

		// TODO rest style>
		if (ActionNames.SEARCH_PROVINCIA.equals(methodStr)) {

			try {
				
				List<Provincia> provincias = provinciaService.findByCriteria(null);
				wsResponse.setData(provincias);				

			} catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de);
				}
				wsResponse.setErrorCode(ErroresNames.ERROR_DATA);

			} catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se);
				}
				wsResponse.setErrorCode(ErroresNames.ERROR_SERVICE);
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
