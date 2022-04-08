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
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.model.TrabajoRealizadoCriteria;
import com.alejandro.reformatec.model.TrabajoRealizadoDTO;
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
		//inicio forward a false(si sale a false, se encontraron errores...)
		boolean forward = true;
				
		// Recogemos primero siempre la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.SEARCH_TRABAJO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView=ViewNames.HOME;
			
			// Recoger los datos que enviamos desde la jsp
			String buscarStr = request.getParameter(ParameterNames.DESCRIPCION);
			String provinciaStr = request.getParameter(ParameterNames.PROVINCIA_ID);
			String especializacionStr = request.getParameter(ParameterNames.ESPECIALIZACION_ID);
			String orderByStr = request.getParameter(ParameterNames.ORDER_BY);

			TrabajoRealizadoCriteria trc = new TrabajoRealizadoCriteria();
			
			//Convertir y/o validar
			if (!StringUtils.isBlank(buscarStr)) {
				// aquí hay mucho que hacer, ver txt
				buscarStr.trim();
				trc.setDescripcion(buscarStr);
			}
			
			Integer provincia = null;
			if (!StringUtils.isBlank(provinciaStr)) {
				provincia = Validator.validaInteger(provinciaStr);
			}
			trc.setIdProvincia(provincia);
			
			Integer especializacion = null;
			if (!StringUtils.isBlank(especializacionStr)) {
				especializacion = Validator.validaEspecializacion(especializacionStr);
			}
			trc.setIdEspecializacion(especializacion);			
			
			if (!StringUtils.isBlank(orderByStr)) {
				orderByStr = Validator.validaOrderByUsuario(orderByStr);
				trc.setOrderBy(orderByStr);
			}
			
					
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




		} else if (ActionNames.DETAIL_TRABAJO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = request.getContextPath()+ViewNames.HOME;
			//targetView=ViewNames.HOME;
			
			// Recoger los datos que enviamos desde la jsp
			String trabajoRealizadoIdStr = request.getParameter(ParameterNames.TRABAJO_REALIZADO_ID);

			//Validar y convertir datos
			Long trabajoRealizadoId = null;
			if  (StringUtils.isBlank(trabajoRealizadoIdStr)) {
				logger.error("Dato incorrecto"+trabajoRealizadoIdStr);
				errors.addParameterError(ParameterNames.USUARIO_ID, ErroresNames.ERROR_ID_TRABAJO_REALIZADO_OBLIGATORIO);	
			} else {
				trabajoRealizadoId = Validator.validaLong(trabajoRealizadoIdStr);
			}
			
			logger.trace("Id del trabajo realizado: "+trabajoRealizadoId);
			
			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {
	
					TrabajoRealizadoDTO tr = trabajoRealizadoService.findById(trabajoRealizadoId);
	
					List<ValoracionDTO> valoraciones = valoracionService.findByIdTrabajoRealizadoValorado(trabajoRealizadoId);
					
					
					request.setAttribute(AttributeNames.VALORACION, valoraciones);
					
					request.setAttribute(AttributeNames.TRABAJO, tr);
	
					// Dirigir a...
					targetView =ViewNames.TRABAJO_DETAIL;
	
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