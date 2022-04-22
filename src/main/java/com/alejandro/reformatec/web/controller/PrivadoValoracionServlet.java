package com.alejandro.reformatec.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.ValoracionDTO;
import com.alejandro.reformatec.service.ValoracionService;
import com.alejandro.reformatec.service.impl.ValoracionServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;


public class PrivadoValoracionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(PrivadoValoracionServlet.class);

	ValoracionService valoracionService = null;

	public PrivadoValoracionServlet() {
		super();
		valoracionService = new ValoracionServiceImpl();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//CommandManager.getInstance().doAction(request, response); **revisar commandManager**

		// Errors aqui NO es null, estaria por ahora vacio
		Errors errors = new Errors();
		request.setAttribute(AttributeNames.ERRORS , errors);

		String targetView = null;
		//inicio forward a false(si sale a false, se hace redirect...)
		boolean forward = true;

		// Recogemos primero siempre la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.CREATE_VALORACION.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.HOME;


			// Recoger los datos que enviamos desde la jsp
			String tituloStr = request.getParameter(ParameterNames.TITULO_VALORACION);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_VALORACION);
			String notaValoracionStr = request.getParameter(ParameterNames.NOTA_VALORACION);
			String idUsuarioValoraStr = request.getParameter(ParameterNames.ID_USUARIO_VALORA);
			String idProveedorValoradoStr = request.getParameter(ParameterNames.ID_PROVEEDOR_VALORADO);
			String idTrabajoRealizadoValoradoStr = request.getParameter(ParameterNames.ID_TRABAJO_REALIZADO_VALORADO);
			
			
			ValoracionDTO valoracion = new ValoracionDTO();
			
			
			//Convertir y validar datos
			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaTitulo(tituloStr);

				if (tituloStr!=null) {
					valoracion.setTitulo(tituloStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto titulo: "+tituloStr);
					}
					errors.addParameterError(ParameterNames.TITULO_VALORACION, ErroresNames.ERROR_TITULO_VALORACION_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco titulo: "+tituloStr);
				}
				errors.addParameterError(ParameterNames.TITULO_VALORACION, ErroresNames.ERROR_TITULO_VALORACION_OBLIGATORIO);
			}
			
			
			
			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaDescripcion(descripcionStr);

				if (descripcionStr!=null) {
					valoracion.setDescripcion(descripcionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto descripcion: "+descripcionStr);
					}
					errors.addParameterError(ParameterNames.DESCRIPCION_VALORACION, ErroresNames.ERROR_DESCRIPCION_VALORACION_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco descripcion: "+descripcionStr);
				}
				errors.addParameterError(ParameterNames.DESCRIPCION_VALORACION, ErroresNames.ERROR_DESCRIPCION_VALORACION_OBLIGATORIO);
			}
			
			
			
			Integer notaValoracion = null;
			if (!StringUtils.isBlank(notaValoracionStr)) {
				notaValoracion = Validator.validaNotaValoracion(notaValoracionStr);

				if(notaValoracion!=null) {
					valoracion.setNotaValoracion(notaValoracion);			
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato invalido nota valoracion: "+notaValoracionStr);
					}
					errors.addParameterError(ParameterNames.NOTA_VALORACION, ErroresNames.ERROR_NOTA_VALORACION_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco nota valoracion: "+notaValoracionStr);
				}
				errors.addParameterError(ParameterNames.NOTA_VALORACION, ErroresNames.ERROR_NOTA_VALORACION_OBLIGATORIO);
			}
			
			
			
			Long idUsuarioValora = null;
			if(!StringUtils.isBlank(idUsuarioValoraStr)) {
				idUsuarioValora = Validator.validaLong(idUsuarioValoraStr);

				if(idUsuarioValora!=null) {
					valoracion.setIdUsuarioValora(idUsuarioValora);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idUsuarioValora: "+idUsuarioValoraStr);
					}
					errors.addParameterError(ParameterNames.ID_USUARIO_VALORA, ErroresNames.ERROR_ID_USUARIO_VALORA_FORMATO_INCORRECTO);
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idUsuarioValora: "+idUsuarioValoraStr);
				}
				errors.addParameterError(ParameterNames.ID_USUARIO_VALORA, ErroresNames.ERROR_ID_USUARIO_VALORA_OBLIGATORIO);
			}
			
			
			
			Long idProveedorValorado = null;
			if(!StringUtils.isBlank(idProveedorValoradoStr)) {
				idProveedorValorado = Validator.validaLong(idProveedorValoradoStr);

				if(idProveedorValorado!=null) {
					valoracion.setIdProveedorValorado(idProveedorValorado);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idProveedorValorado: "+idProveedorValoradoStr);
					}
					errors.addParameterError(ParameterNames.ID_PROVEEDOR_VALORADO, ErroresNames.ERROR_ID_PROVEEDOR_VALORADO_FORMATO_INCORRECTO);
				}
			}
			
			
			Long idTrabajoRealizadoValorado = null;			
			if (idProveedorValorado==null) {
				
				if(!StringUtils.isBlank(idTrabajoRealizadoValoradoStr)) {
					idTrabajoRealizadoValorado = Validator.validaLong(idTrabajoRealizadoValoradoStr);

					if(idTrabajoRealizadoValorado!=null) {
						valoracion.setIdTrabajoRealizadoValorado(idTrabajoRealizadoValorado);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto idTrabajoRealizadoValorado: "+idTrabajoRealizadoValoradoStr);
						}
						errors.addParameterError(ParameterNames.ID_TRABAJO_REALIZADO_VALORADO, ErroresNames.ERROR_ID_TRABAJO_REALIZADO_VALORADO_FORMATO_INCORRECTO);
					}
				}
			}
			 
			
			
			if (idTrabajoRealizadoValorado==null && idProveedorValorado==null) {
				//TODO Aqui le paso el titulo por no pasar otra cosa? que dato paso? creo un metodo que añada los 2 parametros? // Tengo una constrain para esto tambien.
				errors.addParameterError(ParameterNames.TITULO_VALORACION, ErroresNames.ERROR_ID_VALORADO_OBLIGATORIO);
			}
			
			
			if (logger.isTraceEnabled()) {
				logger.trace(valoracion);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					valoracionService.create(valoracion);
					if (logger.isInfoEnabled()) {
						logger.info("Valoracion Creada: "+valoracion);
					}

					request.setAttribute(AttributeNames.VALORACION, valoracion);

					// Dirigir a..
					targetView =ViewNames.USUARIO_MIS_TRABAJOS;
					forward = false;


				}catch (DataException de) {
					if (logger.isErrorEnabled()) {
						logger.error(de.getMessage(), de);
					}
					errors.addCommonError(ErroresNames.ERROR_DATA);

				}catch (ServiceException se) {
					if (logger.isErrorEnabled()) {
						logger.error(se.getMessage(), se);
					}
					errors.addCommonError(ErroresNames.ERROR_SERVICE);

				}catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}
			
			
			
		} else if (ActionNames.UPDATE_STATUS_VALORACION.equalsIgnoreCase(action)) {
			
			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.HOME;


			// Recoger los datos que enviamos desde la jsp
			String idValoracionStr = request.getParameter(ParameterNames.ID_VALORACION);
			String idEstadoStr = request.getParameter(ParameterNames.ID_STATUS_VALORACION);


			// Validar y convertir los datos
			// Validar y convertir los datos
			Long idValoracion = null;			
			if(!StringUtils.isBlank(idValoracionStr)) {
				idValoracion = Validator.validaLong(idValoracionStr);
			} 


			Integer idEstado = null;
			if(!StringUtils.isBlank(idEstadoStr)) {
				idEstado = Validator.validaStatusValoracion(idEstadoStr);
			}


			if (idValoracion==null || idEstado==null) {
				if (logger.isDebugEnabled()) {
					if (idValoracion==null) {
						logger.debug("Datos incorrecto idValoracion: "+idValoracionStr);
					} else {
						logger.debug("Datos incorrecto idEstado: "+idEstadoStr);
					}
				}
				errors.addParameterError(ParameterNames.ID_VALORACION, ErroresNames.ERROR_UPDATE_STATUS_VALORACION_INVALIDO);
			}


			if (logger.isTraceEnabled()) {
				logger.trace("idValoracion: "+idValoracion+", idEstado: "+idEstado);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					valoracionService.updateStatus(idValoracion, idEstado);

					if (logger.isInfoEnabled()) {
						logger.info("Trabajo actualizado: "+idValoracion);
					}

					// Dirigir a..
					//TODO aqui en funcion de si borra una valoracion de un trabajo o de un proveedor...
					targetView =ViewNames.HOME;
					

				}catch (DataException de) {
					if (logger.isErrorEnabled()) {
						logger.error(de.getMessage(), de);
					}
					errors.addCommonError(ErroresNames.ERROR_DATA);

				}catch (ServiceException se) {
					if (logger.isErrorEnabled()) {
						logger.error(se.getMessage(), se);
					}
					errors.addCommonError(ErroresNames.ERROR_SERVICE);

				}catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}
			
			
			
		}else {
			//SACAR UN ERROR?
			targetView = ViewNames.HOME;
		}
		
		
		if (logger.isInfoEnabled()) {
			logger.info(forward?"Forwarding to ":"Redirecting to ", targetView);
		}
		// Aquí se aplica la dirección a la que dirigimos al usuario despues de cada acción
		if (forward) {
			request.getRequestDispatcher(targetView).forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath()+targetView);
		}
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}