package com.alejandro.reformatec.web.controller;

import java.io.IOException;
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
import com.alejandro.reformatec.model.ProyectoCriteria;
import com.alejandro.reformatec.model.ProyectoDTO;
import com.alejandro.reformatec.service.ProyectoService;
import com.alejandro.reformatec.service.impl.ProyectoServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;



public class ProyectoServlet extends HttpServlet {

	private static Logger logger = LogManager.getLogger(ProyectoServlet.class);

	// TODO sacar de configuracion
	private static int PAGE_SIZE = 3; 
	private static int PAGE_COUNT = 5;

	private ProyectoService proyectoService = null;

	public ProyectoServlet() {
		super();
		proyectoService = new ProyectoServiceImpl();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//CommandManager.getInstance().doAction(request, response); **revisar commandManager**

		// Errors aqui NO es null, estaria por ahora vacio
		Errors errors = new Errors();
		request.setAttribute(AttributeNames.ERRORS , errors);		

		String targetView = null;
		//inicio forward a false(si sale a false, se encontraron errores...)
		boolean forward = true;

		// Recogemos primero siempre la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.SEARCH_PROYECTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = request.getContextPath()+ViewNames.HOME;
			//targetView=ViewNames.HOME;

			// Recoger los datos que enviamos desde la jsp
			String buscarStr = request.getParameter(ParameterNames.DESCRIPCION);
			String provinciaStr = request.getParameter(ParameterNames.PROVINCIA_ID);
			String especializacionStr = request.getParameter(ParameterNames.ESPECIALIZACION_ID);
			String presupuestoMaxMinimoStr = request.getParameter(ParameterNames.PRESUPUESTO_MAX_MINIMO);
			String presupuestoMaxMaximoStr = request.getParameter(ParameterNames.PRESUPUESTO_MAX_MAXIMO);
			String orderByStr = request.getParameter(ParameterNames.ORDER_BY);

			//Convertir y validar datos
			if (!StringUtils.isBlank(buscarStr)) {
				// aquí hay mucho que hacer, ver txt
				buscarStr.trim();
			}

			Integer provincia = null;
			if (!StringUtils.isBlank(provinciaStr)) {
				provincia = Validator.validaInteger(provinciaStr);
			}

			Integer especializacion = null;
			if (!StringUtils.isBlank(especializacionStr)) {
				especializacion = Validator.validaEspecializacion(especializacionStr);
			}

			Integer presupuestoMaxMinimo = null;
			if (StringUtils.isBlank(presupuestoMaxMinimoStr)) {
				presupuestoMaxMinimo = Validator.validaPresupuestoMaxMinimo(presupuestoMaxMinimoStr);
			}

			Integer presupuestoMaxMaximo = null;
			if (StringUtils.isBlank(presupuestoMaxMaximoStr)) {
				presupuestoMaxMaximo = Validator.validaPresupuestoMaxMaximo(presupuestoMaxMaximoStr,presupuestoMaxMinimoStr);
			}

			if (!StringUtils.isBlank(orderByStr)) {
				orderByStr = Validator.validaOrderByUsuario(orderByStr);
			}


			ProyectoCriteria pc = new ProyectoCriteria();


			pc.setDescripcion(buscarStr);
			pc.setIdProvincia(provincia);
			pc.setIdEspecializacion(especializacion);
			pc.setPresupuestoMaxMinimo(presupuestoMaxMinimo);
			pc.setPresupuestoMaxMaximo(presupuestoMaxMaximo);
			pc.setOrderBy(orderByStr);


			// Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					List<ProyectoDTO> proyectos = proyectoService.findByCriteria(pc);
					//Modificar Atributos//
					request.setAttribute(AttributeNames.PROYECTO, proyectos);

					//Dirigir a...
					targetView = ViewNames.PROYECTO_RESULTS;

				}catch (DataException de) {
					logger.error(de.getMessage(), de);
					errors.addCommonError(ErroresNames.ERROR_DATA);

				}catch (ServiceException se) {
					logger.error(se.getMessage(), se);
					errors.addCommonError(ErroresNames.ERROR_SERVICE);

				}catch (Exception e) {
					logger.error(e.getMessage(), e);
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}




		} else if (ActionNames.DETAIL_PROYECTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = request.getContextPath()+ViewNames.HOME;
			//targetView=ViewNames.HOME;

			// Recogemos los datos de la jsp
			String proyectoIdStr = request.getParameter(ParameterNames.PROYECTO_ID);


			//Validar y convertir datos
			Long proyectoId = null;
			if  (StringUtils.isBlank(proyectoIdStr)) {
				logger.error("Dato incorrecto"+proyectoIdStr);
				errors.addParameterError(ParameterNames.PROYECTO_ID, ErroresNames.ERROR_ID_PROYECTO_OBLIGATORIO);	
			} else {
				proyectoId = Validator.validaLong(proyectoIdStr);
			}

			logger.info("Id del proyecto: "+proyectoId);
			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					ProyectoDTO p = proyectoService.findById(proyectoId);

					request.setAttribute(AttributeNames.PROYECTO, p);

					//Dirigir a...
					targetView = ViewNames.PROYECTO_DETAIL;

				}catch (DataException de) {
					logger.error(de.getMessage(), de);
					errors.addCommonError(ErroresNames.ERROR_DATA);

				}catch (ServiceException se) {
					logger.error(se.getMessage(), se);
					errors.addCommonError(ErroresNames.ERROR_SERVICE);

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}
		} else if(ActionNames.CREATE_PROYECTO.equalsIgnoreCase(action)) {

			//Falta crear aun la vista de crear proyecto
			targetView = ViewNames.HOME;

			// Recoger los datos que enviamos desde la jsp
			String tituloStr = request.getParameter(ParameterNames.TITULO_PROYECTO);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_PROYECTO);
			String presupuestoMaxStr = request.getParameter(ParameterNames.PRESUPUESTO_MAX_PROYECTO);
			String idPoblacionStr = request.getParameter(ParameterNames.POBLACION_ID);
			String idUsuarioCreadorStr = request.getParameter(ParameterNames.ID_USUARIO_CREADOR_PROYECTO);

			ProyectoDTO proyecto = new ProyectoDTO();

			// Validar y convertir los datos
			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaString(tituloStr);

				if (tituloStr!=null) {
					proyecto.setTitulo(tituloStr);
				} else {
					logger.debug("Dato incorrecto titulo: "+tituloStr);
					errors.addParameterError(ParameterNames.TITULO_PROYECTO, ErroresNames.ERROR_TITULO_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {
				logger.debug("Dato obligatorio titulo: "+tituloStr);
				errors.addParameterError(ParameterNames.TITULO_PROYECTO, ErroresNames.ERROR_TITULO_PROYECTO_OBLIGATORIO);
			}


			// Falta validar bien
			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaString(descripcionStr);

				if (tituloStr!=null) {
					proyecto.setDescripcion(descripcionStr);
				} else {
					logger.debug("Dato incorrecto descripcion: "+descripcionStr);
					errors.addParameterError(ParameterNames.DESCRIPCION_PROYECTO, ErroresNames.ERROR_DESCRIPCION_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {
				logger.debug("Dato obligatorio descripcion: "+descripcionStr);
				errors.addParameterError(ParameterNames.DESCRIPCION_PROYECTO, ErroresNames.ERROR_DESCRIPCION_PROYECTO_OBLIGATORIO);
			}


			// Falta validar bien
			Integer presupuestoMax = null;
			if (!StringUtils.isBlank(presupuestoMaxStr)) {
				presupuestoMax = Validator.validaInteger(presupuestoMaxStr);
				proyecto.setIdPoblacion(presupuestoMax);
			} else {
				logger.debug("Dato null/blanco poblacion: "+presupuestoMaxStr);
				errors.addParameterError(ParameterNames.POBLACION_ID, ErroresNames.ERROR_POBLACION_OBLIGATORIA);
			}


			// Falta validar bien
			Integer poblacion = null;
			if (!StringUtils.isBlank(idPoblacionStr)) {
				poblacion = Validator.validaInteger(idPoblacionStr);
				proyecto.setIdPoblacion(poblacion);
			} else {
				logger.debug("Dato null/blanco poblacion: "+idPoblacionStr);
				errors.addParameterError(ParameterNames.POBLACION_ID, ErroresNames.ERROR_POBLACION_OBLIGATORIA);
			}


			Long idUsuarioCreador = null;
			if (!StringUtils.isBlank(idUsuarioCreadorStr)) {				
				idUsuarioCreador = Validator.validaLong(idUsuarioCreadorStr);

				if (idUsuarioCreador!=null) {
					proyecto.setIdUsuarioCreadorProyecto(idUsuarioCreador);					
				} else {
					logger.debug("Dato incorrecto idUsuarioCreador"+idUsuarioCreadorStr);
					errors.addParameterError(ParameterNames.TIPO_USUARIO_ID, ErroresNames.ERROR_ID_USUARIO_CREADOR_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {				
				logger.debug("Dato obligatorio idUsuarioCreador"+idUsuarioCreadorStr);
				errors.addParameterError(ParameterNames.TIPO_USUARIO_ID, ErroresNames.ERROR_ID_USUARIO_CREADOR_PROYECTO_OBLIGATORIO);
			}

			if(!errors.hasErrors()) {
				try {

					proyectoService.create(proyecto);
					logger.info("Proyecto Creado: "+proyecto);

					request.setAttribute("", proyecto);

					// Dirigir a..
					targetView =ViewNames.HOME;

				}catch (DataException de) {
					logger.error(de.getMessage(), de);
					errors.addCommonError(ErroresNames.ERROR_DATA);

				}catch (ServiceException se) {
					logger.error(se.getMessage(), se);
					errors.addCommonError(ErroresNames.ERROR_SERVICE);

				}catch (Exception e) {
					logger.error(e.getMessage(), e);
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}

		}


		logger.info(forward?"Forwarding to ":"Redirecting to ", targetView);

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