package com.alejandro.reformatec.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.InvalidUserOrPasswordException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
import com.alejandro.reformatec.web.controller.Errors;

public class LoginAction extends Action {

	private static Logger logger = LogManager.getLogger(LoginAction.class);

	private UsuarioService usuarioService = null;

	public LoginAction() {
		super(ActionNames.LOGIN);
		usuarioService = new UsuarioServiceImpl();
	}

	public final String doIt(HttpServletRequest request, HttpServletResponse response) {

		// Errors aqui NO es null, estaria por ahora vacio
		Errors errors = new Errors();
		request.setAttribute(AttributeNames.ERRORS , errors);

		String targetView = null;
		//inicio forward a false(si sale a false, se encontraron errores...)
		boolean forward = true;

		// Recoger los datos que enviamos desde la jsp
		String emailStr = request.getParameter(ParameterNames.EMAIL);
		String passwordStr = request.getParameter(ParameterNames.PASSWORD);
		//TODO falta validar esto de la cookie.. 
		String keepAuthenticated = request.getParameter(ParameterNames.KEEP_AUTHENTICATED);

		// Convertir y validar datos
		if (StringUtils.isBlank(emailStr)) {
			logger.debug("Dato null/blanco email: "+emailStr);
			errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_OBLIGATORIO);
		} else {
			emailStr=emailStr.trim();

			if (!Validator.VALIDA_EMAIL.isValid(emailStr)) {
				logger.debug("Dato incorrecto email: "+emailStr);
				errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_FORMATO_INCORRECTO);
			}
		}


		if (StringUtils.isBlank(passwordStr)) {
			logger.debug("Dato null/blanco password: "+passwordStr);
			errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
		} else {
			passwordStr=passwordStr.trim();

			if (!Validator.validaPassword(passwordStr)) {
				logger.debug("Dato incorrecto password2");
				errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
			}
		}			



		logger.trace("Email: "+emailStr);

		//Acceder a la capa de negocio(si no hay errores)
		if(!errors.hasErrors()) {
			try {

				UsuarioDTO u = usuarioService.login(emailStr, passwordStr);

				if (logger.isInfoEnabled()) {
					logger.info("User "+emailStr+" authenticated sucessfully.");
				}
				SessionManager.set(request, AttributeNames.USUARIO, u);

				if (keepAuthenticated!=null) {
					//TODO Falta verificar la ip...
					CookieManager.setValue(response, AttributeNames.USUARIO, emailStr, 30*24*60*60); // Agujero!
				} else {
					CookieManager.setValue(response, AttributeNames.USUARIO, emailStr, 0); // Agujero!
				}

				// Dirigir a...
				targetView = ViewNames.HOME;
				forward = false;

			}catch (InvalidUserOrPasswordException iope) {
				logger.error("InvalidUserOrPasswordException: "+iope.getMessage(), iope);
				errors.addCommonError(ErroresNames.ERROR_USUARIO_PASSWORD_INVALID);

			}catch (DataException de) {
				logger.error("DataException: "+de.getMessage(), de);
				errors.addCommonError(ErroresNames.ERROR_DATA);

			}catch (ServiceException se) {
				logger.error("ServiceException: "+se.getMessage(), se);
				errors.addCommonError(ErroresNames.ERROR_SERVICE);

			}catch (Exception e) {
				logger.error("Exception: "+e.getMessage(), e);
				errors.addCommonError(ErroresNames.ERROR_E);
			}
		}
		logger.info(forward?"Forwarding to ":"Redirecting to ", targetView);
		// Aquí se aplica la dirección a la que dirigimos al usuario despues de cada acción
		if (forward) {
			return targetView;
		} else {
			return (request.getContextPath()+targetView);
		}
	}	
}