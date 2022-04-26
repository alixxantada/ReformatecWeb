package com.alejandro.reformatec.web.service;

import java.io.IOException;

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
import com.alejandro.reformatec.service.ContadorService;
import com.alejandro.reformatec.service.impl.ContadorServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;


@WebServlet("/contador-service")
public class ContadorServiceWebServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(ContadorServiceWebServlet.class);

	private ContadorService contadorService = null;
	//private Gson gson = null;

	public ContadorServiceWebServlet() {
		super();
		contadorService = new ContadorServiceImpl();
		//gson = new Gson();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String actionStr = request.getParameter(ParameterNames.ACTION);

		WebServiceResponse wsResponse = new WebServiceResponse();

		if (ActionNames.CONTADOR_TRABAJO_REALIZADO.equals(actionStr)) {

		} else if (ActionNames.CONTADOR_CLIENTE.equals(actionStr)) {

			try {

				Integer clientes = contadorService.cliente();
				wsResponse.setData(clientes);

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

			response.setCharacterEncoding("ISO-8859-1");

			ServletOutputStream sos = response.getOutputStream();

			//sos.write(wsResponse.get);

		} else if (ActionNames.CONTADOR_PROVEEDOR.equals(actionStr)) {



		} else if (ActionNames.CONTADOR_PROYECTO_ACTIVO.equals(actionStr)) {



		} else if (ActionNames.CONTADOR_PROYECTO_FINALIZADO.equals(actionStr)) {



		} // mas acciones
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
