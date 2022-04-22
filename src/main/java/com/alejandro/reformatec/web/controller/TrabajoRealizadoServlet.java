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
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.model.TrabajoRealizadoCriteria;
import com.alejandro.reformatec.model.TrabajoRealizadoDTO;
import com.alejandro.reformatec.model.ValoracionCriteria;
import com.alejandro.reformatec.model.ValoracionDTO;
import com.alejandro.reformatec.service.TrabajoRealizadoService;
import com.alejandro.reformatec.service.ValoracionService;
import com.alejandro.reformatec.service.impl.TrabajoRealizadoServiceImpl;
import com.alejandro.reformatec.service.impl.ValoracionServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;
import com.alejandro.reformatec.web.util.WebPagingUtils;


public class TrabajoRealizadoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(TrabajoRealizadoServlet.class);

	private static int PAGE_SIZE = 3; 
	private static int PAGE_COUNT = 5;

	private TrabajoRealizadoService trabajoRealizadoService = null;
	private ValoracionService valoracionService = null;


	public TrabajoRealizadoServlet() {
		super();   
		trabajoRealizadoService = new TrabajoRealizadoServiceImpl();	
		valoracionService = new ValoracionServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		//CommandManager.getInstance().doAction(request, response); **revisar commandManager**

		// Errors aqui NO es null, estaria por ahora vacio
		Errors errors = new Errors();
		request.setAttribute(AttributeNames.ERRORS , errors);

		String targetView = null;
		//inicio forward a false(si sale a false, se hace redirect...)
		boolean forward = true;

		// Recogemos primero siempre la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.SEARCH_TRABAJO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView=ViewNames.HOME;

			// Recoger los datos que enviamos desde la jsp
			String buscarStr = request.getParameter(ParameterNames.BUSCAR_DESCRIPCION);
			String idProvinciaStr = request.getParameter(ParameterNames.ID_PROVINCIA);
			String especializacionStr = request.getParameter(ParameterNames.ID_ESPECIALIZACION);
			String orderByStr = request.getParameter(ParameterNames.ORDER_BY);
			String idUsuarioCreadorTrabajoStr = request.getParameter(ParameterNames.ID_USUARIO_CREADOR_TRABAJO);
			
			TrabajoRealizadoCriteria trc = new TrabajoRealizadoCriteria();

			//Convertir y/o validar
			if (!StringUtils.isBlank(buscarStr)) {
				// aquí hay mucho que hacer, ver txt
				buscarStr.trim();
				trc.setDescripcion(buscarStr);
			}

			
			Integer idProvincia = null;
			if (!StringUtils.isBlank(idProvinciaStr)) {
				idProvincia = Validator.validaProvincia(idProvinciaStr);
			}
			trc.setIdProvincia(idProvincia);

			
			Integer especializacion = null;
			if (!StringUtils.isBlank(especializacionStr)) {
				especializacion = Validator.validaEspecializacion(especializacionStr);
			}
			trc.setIdEspecializacion(especializacion);			

			
			if (!StringUtils.isBlank(orderByStr)) {
				orderByStr = Validator.validaOrderByTrabajo(orderByStr);
				trc.setOrderBy(orderByStr);
			}
			
			
			Long idUsuarioCreadorTrabajo = null;
			if (!StringUtils.isBlank(idUsuarioCreadorTrabajoStr)) {
				idUsuarioCreadorTrabajo = Validator.validaLong(idUsuarioCreadorTrabajoStr);
			}
			trc.setIdProveedor(idUsuarioCreadorTrabajo);
			
			
			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {
					Integer currentPage = WebPagingUtils.getCurrentPage(request);
					Results<TrabajoRealizadoDTO> results = trabajoRealizadoService.findByCriteria(trc, (currentPage-1)*PAGE_SIZE +1, PAGE_SIZE);

					request.setAttribute(AttributeNames.TRABAJO, results);

					// Atributos para paginacion
					Integer totalPages = WebPagingUtils.getTotalPages(results.getTotal(), PAGE_SIZE);
					request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
					request.setAttribute(AttributeNames.CURRENT_PAGE, currentPage);				
					request.setAttribute(AttributeNames.PAGING_FROM, WebPagingUtils.getPageFrom(currentPage, PAGE_COUNT, totalPages));
					request.setAttribute(AttributeNames.PAGING_TO, WebPagingUtils.getPageTo(currentPage, PAGE_COUNT, totalPages));

					// Dirigir a...
					targetView= ViewNames.TRABAJO_RESULTS;

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

				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}




		} else if (ActionNames.DETAIL_TRABAJO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = request.getContextPath()+ViewNames.HOME;
			//targetView=ViewNames.HOME;

			// Recoger los datos que enviamos desde la jsp
			String idTrabajoRealizadoStr = request.getParameter(ParameterNames.ID_TRABAJO_REALIZADO);

			
			TrabajoRealizadoCriteria trc = new TrabajoRealizadoCriteria();
			ValoracionCriteria vc = new ValoracionCriteria();
			
			//Validar y convertir datos
			Long idTrabajoRealizado = null;
			if  (!StringUtils.isBlank(idTrabajoRealizadoStr)) {
				idTrabajoRealizado = Validator.validaLong(idTrabajoRealizadoStr);
				
				if (idTrabajoRealizado!=null) {
					trc.setIdTrabajoRealizado(idTrabajoRealizado);
					vc.setIdTrabajoRealizadoValorado(idTrabajoRealizado);
					
				} else {
					if (logger.isErrorEnabled()) {
						logger.error("Formato incorrecto idTrabajoRealizado: "+idTrabajoRealizadoStr);
					}
					errors.addParameterError(ParameterNames.ID_TRABAJO_REALIZADO, ErroresNames.ERROR_ID_TRABAJO_REALIZADO_FORMATO_INCORRECTO);	
				}
				
			} else {
				
				if (logger.isErrorEnabled()) {
					logger.error("Dato null/blanc idTrabajoRealizado: "+idTrabajoRealizadoStr);
				}
				errors.addParameterError(ParameterNames.ID_TRABAJO_REALIZADO, ErroresNames.ERROR_ID_TRABAJO_REALIZADO_OBLIGATORIO);				
			}

			
			
			if (logger.isTraceEnabled()) {
				logger.trace("Id del trabajo realizado: "+idTrabajoRealizado);
			}

			
			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {
					
					Results<TrabajoRealizadoDTO> trabajo = trabajoRealizadoService.findByCriteria(trc, 1, 1);

					request.setAttribute(AttributeNames.TRABAJO, trabajo);


					trabajoRealizadoService.visualizaTrabajo(idTrabajoRealizado);

					
					Results<ValoracionDTO> valoraciones = valoracionService.findByCriteria(vc, 1, 1);					

					request.setAttribute(AttributeNames.VALORACION, valoraciones);

					// Dirigir a...
					targetView =ViewNames.TRABAJO_DETAIL;

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