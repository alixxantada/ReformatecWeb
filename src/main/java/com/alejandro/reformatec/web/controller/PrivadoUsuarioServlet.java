package com.alejandro.reformatec.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.CookieManager;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.SessionManager;
import com.alejandro.reformatec.web.util.ViewNames;



public class PrivadoUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LogManager.getLogger(UsuarioServlet.class);

	public PrivadoUsuarioServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//CommandManager.getInstance().doAction(request, response); **revisar commandManager**

		// Errors aqui no es null, estaria por ahora vacio
		Errors errors = new Errors();
		request.setAttribute(AttributeNames.ERRORS , errors);

		String targetView = null;
		//inicio forward a false(si sale a false, se encontraron errores...)
		boolean forward = true;
		
		// Recogemos primero siempre la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.LOGOUT.equalsIgnoreCase(action)) {
			SessionManager.set(request, AttributeNames.USUARIO, null);

			// Dirigir a...
			targetView = ViewNames.HOME;
			forward = false;
		}
		
		logger.info(forward?"Forwarding to ":"Redirecting to ", targetView);
		// Aquí se aplica la dirección a la que dirigimos al usuario despues de cada acción
		if (forward) {
			request.getRequestDispatcher(targetView).forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath()+targetView);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}