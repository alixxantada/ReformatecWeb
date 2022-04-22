package com.alejandro.reformatec.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.TrabajoRealizadoDTO;
import com.alejandro.reformatec.service.TrabajoRealizadoService;
import com.alejandro.reformatec.service.impl.TrabajoRealizadoServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;


public class PrivadoTrabajoRealizadoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(PrivadoTrabajoRealizadoServlet.class);


	private TrabajoRealizadoService trabajoRealizadoService = null;


	public PrivadoTrabajoRealizadoServlet() {
		super();   
		trabajoRealizadoService = new TrabajoRealizadoServiceImpl();
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


		if (ActionNames.CREATE_TRABAJO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.USUARIO_MIS_TRABAJOS;


			//Recoger los datos que enviamos desde la jsp			
			String idUsuarioCreadorStr = request.getParameter(ParameterNames.ID_USUARIO_CREADOR_TRABAJO);
			String tituloStr = request.getParameter(ParameterNames.TITULO_TRABAJO);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_TRABAJO);
			String idPoblacionStr = request.getParameter(ParameterNames.ID_POBLACION);			
			String idProyectoAsociadoStr =  request.getParameter(ParameterNames.ID_PROYECTO);
			String [] idsEspecializacionesStr = request.getParameterValues(ParameterNames.ID_ESPECIALIZACION);

			
			TrabajoRealizadoDTO trabajoRealizado = new TrabajoRealizadoDTO();	
			
			List<Integer> idsEspecializaciones = new ArrayList<Integer>();

			
			//Convertir y validar datos	
			Long idUsuarioCreador = null;
			if(!StringUtils.isBlank(idUsuarioCreadorStr)) {
				idUsuarioCreador = Validator.validaLong(idUsuarioCreadorStr);

				if(idUsuarioCreador!=null) {
					trabajoRealizado.setIdUsuarioCreadorTrabajo(idUsuarioCreador);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idUsuarioCreador: "+idUsuarioCreadorStr);
					}
					errors.addParameterError(ParameterNames.ID_USUARIO_CREADOR_TRABAJO, ErroresNames.ERROR_ID_USUARIO_CREADOR_TRABAJO_FORMATO_INCORRECTO);
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idUsuarioCreador: "+idUsuarioCreadorStr);
				}
				errors.addParameterError(ParameterNames.ID_USUARIO_CREADOR_TRABAJO, ErroresNames.ERROR_ID_USUARIO_CREADOR_TRABAJO_OBLIGATORIO);
			}



			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaTitulo(tituloStr);

				if (tituloStr!=null) {
					trabajoRealizado.setTitulo(tituloStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto titulo: "+tituloStr);
					}
					errors.addParameterError(ParameterNames.TITULO_TRABAJO, ErroresNames.ERROR_TITULO_TRABAJO_REALIZADO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco titulo: "+tituloStr);
				}
				errors.addParameterError(ParameterNames.TITULO_TRABAJO, ErroresNames.ERROR_TITULO_TRABAJO_REALIZADO_OBLIGATORIO);
			}



			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaDescripcion(descripcionStr);

				if (descripcionStr!=null) {
					trabajoRealizado.setDescripcion(descripcionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto descripcion: "+descripcionStr);
					}
					errors.addParameterError(ParameterNames.DESCRIPCION_TRABAJO, ErroresNames.ERROR_DESCRIPCION_TRABAJO_REALIZADO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco descripcion: "+descripcionStr);
				}
				errors.addParameterError(ParameterNames.DESCRIPCION_TRABAJO, ErroresNames.ERROR_DESCRIPCION_TRABAJO_REALIZADO_OBLIGATORIO);
			}



			Integer idPoblacion = null;
			if (!StringUtils.isBlank(idPoblacionStr)) {
				idPoblacion = Validator.validaPoblacion(idPoblacionStr);

				if(idPoblacion!=null) {
					trabajoRealizado.setIdPoblacion(idPoblacion);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato invalido idpoblacion: "+idPoblacionStr);
					}
					errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idpoblacion: "+idPoblacionStr);
				}
				errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_OBLIGATORIO);
			}


			
			Long idProyectoAsociado = null;
			if(!StringUtils.isBlank(idProyectoAsociadoStr)) {
				idProyectoAsociado = Validator.validaLong(idProyectoAsociadoStr);

				if(idProyectoAsociado==null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idProyectoAsociado: "+idProyectoAsociadoStr);
					}
					errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_ASOCIADO_FORMATO_INCORRECTO);
				}
			} 
			trabajoRealizado.setIdProyectoAsociado(idProyectoAsociado);


			
				if (idsEspecializacionesStr.length>0 ) {
				
				for (int i=0;i<idsEspecializacionesStr.length;i++) {
					if (!StringUtils.isBlank(idsEspecializacionesStr[i])) {
						Integer idEspecializacion = Validator.validaEspecializacion(idsEspecializacionesStr[i]);
						
						if(idEspecializacion!=null) {
							idsEspecializaciones.add(idEspecializacion);
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Formato invalido idEspecializacion: "+idsEspecializacionesStr[i]);
							}
							errors.addParameterError(ParameterNames.ID_ESPECIALIZACION, ErroresNames.ERROR_ID_ESPECIALIZACION_FORMATO_INCORRECTO);
						}
						
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato obligatorio idEspecializacion: "+idsEspecializacionesStr[i]);
						}
						errors.addParameterError(ParameterNames.ID_ESPECIALIZACION, ErroresNames.ERROR_ID_ESPECIALIZACION_OBLIGATORIO);
					}
				}
			}


			if (logger.isTraceEnabled()) {
				logger.trace("Trabajo Realizado: "+trabajoRealizado);
				logger.trace("Especializaciones: "+idsEspecializaciones);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					trabajoRealizadoService.create(trabajoRealizado, idsEspecializaciones);
					if (logger.isInfoEnabled()) {
						logger.info("Trabajo Creado: "+trabajoRealizado);
					}

					request.setAttribute(AttributeNames.TRABAJO, trabajoRealizado);

					// Dirigir a..
					targetView =ViewNames.USUARIO_MIS_TRABAJOS;


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



		} else if (ActionNames.UPDATE_TRABAJO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.USUARIO_MIS_TRABAJOS;


			//Recoger los datos que enviamos desde la jsp
			String idTrabajoRealizadoStr = request.getParameter(ParameterNames.ID_TRABAJO_REALIZADO);
			String tituloStr = request.getParameter(ParameterNames.TITULO_TRABAJO);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_TRABAJO);
			String idPoblacionStr = request.getParameter(ParameterNames.ID_POBLACION);			
			String idProyectoAsociadoStr =  request.getParameter(ParameterNames.ID_PROYECTO);
			String [] idsEspecializacionesStr = request.getParameterValues(ParameterNames.ID_ESPECIALIZACION);

			TrabajoRealizadoDTO trabajoRealizado = new TrabajoRealizadoDTO();	

			List<Integer> idsEspecializaciones = new ArrayList<Integer>();

			
			//Convertir y validar datos	
			Long idTrabajoRealizado = null;
			if(!StringUtils.isBlank(idTrabajoRealizadoStr)) {
				idTrabajoRealizado = Validator.validaLong(idTrabajoRealizadoStr);

				if(idTrabajoRealizado!=null) {
					trabajoRealizado.setIdTrabajoRealizado(idTrabajoRealizado);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idTrabajoRealizado: "+idTrabajoRealizadoStr);
					}
					errors.addParameterError(ParameterNames.ID_TRABAJO_REALIZADO, ErroresNames.ERROR_ID_TRABAJO_REALIZADO_FORMATO_INCORRECTO);
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idTrabajoRealizado: "+idTrabajoRealizadoStr);
				}
				errors.addParameterError(ParameterNames.ID_TRABAJO_REALIZADO, ErroresNames.ERROR_ID_TRABAJO_REALIZADO_OBLIGATORIO);
			}



			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaTitulo(tituloStr);

				if (tituloStr!=null) {
					trabajoRealizado.setTitulo(tituloStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto titulo: "+tituloStr);
					}
					errors.addParameterError(ParameterNames.TITULO_TRABAJO, ErroresNames.ERROR_TITULO_TRABAJO_REALIZADO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco titulo: "+tituloStr);
				}
				errors.addParameterError(ParameterNames.TITULO_TRABAJO, ErroresNames.ERROR_TITULO_TRABAJO_REALIZADO_OBLIGATORIO);
			}



			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaDescripcion(descripcionStr);

				if (descripcionStr!=null) {
					trabajoRealizado.setDescripcion(descripcionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto descripcion: "+descripcionStr);
					}
					errors.addParameterError(ParameterNames.DESCRIPCION_TRABAJO, ErroresNames.ERROR_DESCRIPCION_TRABAJO_REALIZADO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco descripcion: "+descripcionStr);
				}
				errors.addParameterError(ParameterNames.DESCRIPCION_TRABAJO, ErroresNames.ERROR_DESCRIPCION_TRABAJO_REALIZADO_OBLIGATORIO);
			}



			Integer idPoblacion = null;
			if (!StringUtils.isBlank(idPoblacionStr)) {
				idPoblacion = Validator.validaPoblacion(idPoblacionStr);

				if(idPoblacion!=null) {
					trabajoRealizado.setIdPoblacion(idPoblacion);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato invalido idpoblacion: "+idPoblacionStr);
					}
					errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idpoblacion: "+idPoblacionStr);
				}
				errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_OBLIGATORIO);
			}

			

			Long idProyectoAsociado = null;
			if(!StringUtils.isBlank(idProyectoAsociadoStr)) {
				idProyectoAsociado = Validator.validaLong(idProyectoAsociadoStr);

				if(idProyectoAsociado==null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idProyectoAsociado: "+idProyectoAsociadoStr);
					}
					errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_ASOCIADO_FORMATO_INCORRECTO);					
				}
			} 
			trabajoRealizado.setIdProyectoAsociado(idProyectoAsociado);


			if (idsEspecializacionesStr.length>0 ) {
				
				for (int i=0;i<idsEspecializacionesStr.length;i++) {
					if (!StringUtils.isBlank(idsEspecializacionesStr[i])) {
						Integer idEspecializacion = Validator.validaEspecializacion(idsEspecializacionesStr[i]);
						
						if(idEspecializacion!=null) {
							idsEspecializaciones.add(idEspecializacion);
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Formato invalido idEspecializacion: "+idsEspecializacionesStr[i]);
							}
							errors.addParameterError(ParameterNames.ID_ESPECIALIZACION, ErroresNames.ERROR_ID_ESPECIALIZACION_FORMATO_INCORRECTO);
						}
						
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato obligatorio idEspecializacion: "+idsEspecializacionesStr[i]);
						}
						errors.addParameterError(ParameterNames.ID_ESPECIALIZACION, ErroresNames.ERROR_ID_ESPECIALIZACION_OBLIGATORIO);
					}
				}
			}
			
			
			if (logger.isTraceEnabled()) {
				logger.trace("Trabajo Realizado: "+trabajoRealizado);
				logger.trace("Especializaciones: "+idsEspecializaciones);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					trabajoRealizadoService.update(trabajoRealizado,idsEspecializaciones);
					if (logger.isInfoEnabled()) {
						logger.info("Trabajo Actualizado: "+trabajoRealizado);
					}

					request.setAttribute(AttributeNames.TRABAJO, trabajoRealizado);

					// Dirigir a..
					targetView =ViewNames.USUARIO_MIS_TRABAJOS;


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


		} else if (ActionNames.UPDATE_STATUS_TRABAJO.equalsIgnoreCase(action)) {
			
			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.HOME;


			// Recoger los datos que enviamos desde la jsp
			String idTrabajoStr = request.getParameter(ParameterNames.ID_TRABAJO_REALIZADO);
			String idEstadoStr = request.getParameter(ParameterNames.ID_STATUS_TRABAJO);


			// Validar y convertir los datos
			Long idTrabajo = null;			
			if(!StringUtils.isBlank(idTrabajoStr)) {
				idTrabajo = Validator.validaLong(idTrabajoStr);
			} 


			Integer idEstado = null;
			if(!StringUtils.isBlank(idEstadoStr)) {
				idEstado = Validator.validaStatusTrabajo(idEstadoStr);
			}


			if (idTrabajo==null || idEstado==null) {
				if (logger.isDebugEnabled()) {
					if (idTrabajo==null) {
						logger.debug("Datos incorrecto idTrabajo: "+idTrabajoStr);
					} else {
						logger.debug("Datos incorrecto idEstado: "+idEstadoStr);
					}
				}
				errors.addParameterError(ParameterNames.ID_TRABAJO_REALIZADO, ErroresNames.ERROR_UPDATE_STATUS_TRABAJO_INVALIDO);
			}


			if (logger.isTraceEnabled()) {
				logger.trace("idTrabajo: "+idTrabajo+", idEstado: "+idEstado);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					trabajoRealizadoService.updateStatus(idTrabajo, idEstado);

					if (logger.isInfoEnabled()) {
						logger.info("Trabajo actualizado: "+idTrabajo);
					}

					// Dirigir a..
					targetView =ViewNames.USUARIO_MIS_TRABAJOS;
					

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
			
			
		} else {
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
