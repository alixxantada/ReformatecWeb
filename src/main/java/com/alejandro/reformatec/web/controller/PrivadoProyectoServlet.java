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

import com.alejandro.reformatec.dao.util.ConfigurationManager;
import com.alejandro.reformatec.dao.util.ConstantConfigUtil;
import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.ProyectoCriteria;
import com.alejandro.reformatec.model.ProyectoDTO;
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.service.ProyectoService;
import com.alejandro.reformatec.service.impl.ProyectoServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ConfigNames;
import com.alejandro.reformatec.web.util.ControllerNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;
import com.alejandro.reformatec.web.util.WebPagingUtils;



public class PrivadoProyectoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(PrivadoProyectoServlet.class);

	private static final String CFGM_PFX = ConfigNames.PFX;
	private static final String PAGE_SIZE_DETAIL = CFGM_PFX + ConfigNames.PAGE_SIZE_DETAIL;
	private static final String PAGE_SIZE_SEARCH = CFGM_PFX +  ConfigNames.PAGE_SIZE_SEARCH;
	private static final String PAGE_COUNT = CFGM_PFX + ConfigNames.PAGE_COUNT;
	private ConfigurationManager cfgM = ConfigurationManager.getInstance();	

	private ProyectoService proyectoService = null;

	public PrivadoProyectoServlet() {
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
		//inicio forward a false(si sale a false, se hace redirect)
		boolean forward = true;

		// Recogemos primero siempre la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.SEARCH_PROYECTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String buscarStr = request.getParameter(ParameterNames.BUSCAR_DESCRIPCION);
			String provinciaStr = request.getParameter(ParameterNames.ID_PROVINCIA);
			String especializacionStr = request.getParameter(ParameterNames.ID_ESPECIALIZACION);
			String presupuestoMaxMinimoStr = request.getParameter(ParameterNames.PRESUPUESTO_MAX_MINIMO);
			String presupuestoMaxMaximoStr = request.getParameter(ParameterNames.PRESUPUESTO_MAX_MAXIMO);
			String orderByStr = request.getParameter(ParameterNames.ORDER_BY);
			String idUsuarioCreadorProyectoStr = request.getParameter(ParameterNames.ID_USUARIO_CREADOR_PROYECTO);


			ProyectoCriteria pc = new ProyectoCriteria();


			//Convertir y validar datos
			if (!StringUtils.isBlank(buscarStr)) {
				// aquí hay mucho que hacer, ver txt
				buscarStr.trim();
				pc.setDescripcion(buscarStr);
			}


			Integer provincia = null;
			if (!StringUtils.isBlank(provinciaStr)) {
				provincia = Validator.validaProvincia(provinciaStr);
			}
			pc.setIdProvincia(provincia);


			Integer especializacion = null;
			if (!StringUtils.isBlank(especializacionStr)) {
				especializacion = Validator.validaEspecializacion(especializacionStr);
			}
			pc.setIdEspecializacion(especializacion);


			Integer presupuestoMaxMinimo = null;
			if (StringUtils.isBlank(presupuestoMaxMinimoStr)) {
				presupuestoMaxMinimo = Validator.validaPresupuestoMaxMinimo(presupuestoMaxMinimoStr);
			}
			pc.setPresupuestoMaxMinimo(presupuestoMaxMinimo);


			Integer presupuestoMaxMaximo = null;
			if (StringUtils.isBlank(presupuestoMaxMaximoStr)) {
				presupuestoMaxMaximo = Validator.validaPresupuestoMaxMaximo(presupuestoMaxMaximoStr,presupuestoMaxMinimoStr);
			}
			pc.setPresupuestoMaxMaximo(presupuestoMaxMaximo);


			if (!StringUtils.isBlank(orderByStr)) {
				orderByStr = Validator.validaOrderByTrabajo(orderByStr);
				pc.setOrderBy(orderByStr);
			}


			Long idUsuarioCreadorProyecto = null;
			if (!StringUtils.isBlank(idUsuarioCreadorProyectoStr)) {
				idUsuarioCreadorProyecto = Validator.validaLong(idUsuarioCreadorProyectoStr);
			}
			pc.setIdUsuarioCreador(idUsuarioCreadorProyecto);


			if (logger.isTraceEnabled()) {
				logger.trace("Busqueda proyecto: "+pc);
			}

			// Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {
					Integer currentPage = WebPagingUtils.getCurrentPage(request);
					Results<ProyectoDTO> results = proyectoService.findByCriteria(pc, (currentPage-1)*Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)) +1, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));

					request.setAttribute(AttributeNames.PROYECTO, results);

					// Atributos para paginacion
					Integer totalPages = WebPagingUtils.getTotalPages(results.getTotal(), Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));
					request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
					request.setAttribute(AttributeNames.CURRENT_PAGE, currentPage);					
					request.setAttribute(AttributeNames.PAGING_FROM, WebPagingUtils.getPageFrom(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));
					request.setAttribute(AttributeNames.PAGING_TO, WebPagingUtils.getPageTo(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));


					//Dirigir a...
					targetView = ViewNames.PROYECTO_RESULTS;
					forward = true;
					
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
					logger.error(e.getMessage(), e);
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}




		} else if (ActionNames.DETAIL_PROYECTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recogemos los datos de la jsp
			String idProyectoStr = request.getParameter(ParameterNames.ID_PROYECTO);

			ProyectoCriteria pc = new ProyectoCriteria();


			//Validar y convertir datos
			Long idProyecto = null;
			if  (!StringUtils.isBlank(idProyectoStr)) {
				idProyecto = Validator.validaLong(idProyectoStr);

				if (idProyecto!=null) {
					pc.setIdProyecto(idProyecto);

				} else {
					if (logger.isErrorEnabled()) {
						logger.error("Formato incorrecto idProyecto: "+idProyectoStr);
					}
					errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_FORMATO_INCORRECTO);	
				}

			} else {

				if (logger.isErrorEnabled()) {
					logger.error("Dato null/blanco idProyecto: "+idProyectoStr);
				}
				errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_OBLIGATORIO);	
			}


			if (logger.isInfoEnabled()) {
				logger.info("Id del proyecto: "+idProyecto);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {
					
					Results<ProyectoDTO> results = proyectoService.findByCriteria(pc, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)));

					request.setAttribute(AttributeNames.PROYECTO, results);

					//Dirigir a...
					targetView = ViewNames.PROYECTO_DETAIL;
					forward = true;
					
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


		} else if(ActionNames.CREATE_PROYECTO.equalsIgnoreCase(action)) {

			// Vista en caso de error
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String tituloStr = request.getParameter(ParameterNames.TITULO_PROYECTO);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_PROYECTO);
			String presupuestoMaxStr = request.getParameter(ParameterNames.PRESUPUESTO_MAX_PROYECTO);
			String idPoblacionStr = request.getParameter(ParameterNames.ID_POBLACION);
			String idUsuarioCreadorStr = request.getParameter(ParameterNames.ID_USUARIO_CREADOR_PROYECTO);
			String [] idsEspecializacionesStr = request.getParameterValues(ParameterNames.ID_ESPECIALIZACION);


			ProyectoDTO proyecto = new ProyectoDTO();

			List<Integer> idsEspecializaciones = new ArrayList<Integer>();



			// Validar y convertir los datos
			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaTitulo(tituloStr);

				if (tituloStr!=null) {
					proyecto.setTitulo(tituloStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto titulo: "+tituloStr);
					}
					errors.addParameterError(ParameterNames.TITULO_PROYECTO, ErroresNames.ERROR_TITULO_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio titulo: "+tituloStr);
				}
				errors.addParameterError(ParameterNames.TITULO_PROYECTO, ErroresNames.ERROR_TITULO_PROYECTO_OBLIGATORIO);
			}



			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaDescripcion(descripcionStr);

				if (descripcionStr!=null) {
					proyecto.setDescripcion(descripcionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto descripcion: "+descripcionStr);
					}
					errors.addParameterError(ParameterNames.DESCRIPCION_PROYECTO, ErroresNames.ERROR_DESCRIPCION_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio descripcion: "+descripcionStr);
				}
				errors.addParameterError(ParameterNames.DESCRIPCION_PROYECTO, ErroresNames.ERROR_DESCRIPCION_PROYECTO_OBLIGATORIO);
			}



			Integer presupuestoMax = null;
			if (!StringUtils.isBlank(presupuestoMaxStr)) {
				presupuestoMax = Validator.validaPresupuestoMax(presupuestoMaxStr);

				if (presupuestoMax!=null) {
					proyecto.setPresupuestoMax(presupuestoMax);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato invalido presupuestoMax: "+presupuestoMaxStr);
					}
					errors.addParameterError(ParameterNames.PRESUPUESTO_MAX_PROYECTO, ErroresNames.ERROR_PRESUPUESTO_MAX_PROYECTO_FORMATO_INCORRECTO);
				}				

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco presupuestoMax: "+presupuestoMaxStr);
				}
				errors.addParameterError(ParameterNames.PRESUPUESTO_MAX_PROYECTO, ErroresNames.ERROR_PRESUPUESTO_MAX_PROYECTO_OBLIGATORIO);
			}



			Integer idPoblacion = null;
			if (!StringUtils.isBlank(idPoblacionStr)) {
				idPoblacion = Validator.validaPoblacion(idPoblacionStr);

				if(idPoblacion!=null) {
					proyecto.setIdPoblacion(idPoblacion);				
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idpoblacion: "+idPoblacionStr);
					}
					errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idpoblacion: "+idPoblacionStr);
				}
				errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_OBLIGATORIO);
			}



			Long idUsuarioCreador = null;
			if (!StringUtils.isBlank(idUsuarioCreadorStr)) {				
				idUsuarioCreador = Validator.validaLong(idUsuarioCreadorStr);

				if (idUsuarioCreador!=null) {
					proyecto.setIdUsuarioCreadorProyecto(idUsuarioCreador);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idUsuarioCreador: "+idUsuarioCreadorStr);
					}
					errors.addParameterError(ParameterNames.ID_USUARIO_CREADOR_PROYECTO, ErroresNames.ERROR_ID_USUARIO_CREADOR_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {				
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio idUsuarioCreador: "+idUsuarioCreadorStr);
				}
				errors.addParameterError(ParameterNames.ID_USUARIO_CREADOR_PROYECTO, ErroresNames.ERROR_ID_USUARIO_CREADOR_PROYECTO_OBLIGATORIO);
			}




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
				logger.trace("Proyecto: "+proyecto);
				logger.trace("Especializaciones: "+idsEspecializaciones);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					proyectoService.create(proyecto,idsEspecializaciones);
					
					if (logger.isInfoEnabled()) {
						logger.info("Proyecto Creado: "+proyecto);
					}

					// Dirigir a..
					targetView =ViewNames.USUARIO_MIS_PROYECTOS;
					forward = true;
					
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

		} else if (ActionNames.UPDATE_PROYECTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.USUARIO_MIS_PROYECTOS;
			// Recoger los datos que enviamos desde la jsp
			String idProyectoStr = request.getParameter(ParameterNames.ID_PROYECTO);
			String tituloStr = request.getParameter(ParameterNames.TITULO_PROYECTO);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_PROYECTO);
			String presupuestoMaxStr = request.getParameter(ParameterNames.PRESUPUESTO_MAX_PROYECTO);
			String idPoblacionStr = request.getParameter(ParameterNames.ID_POBLACION);			
			String [] idsEspecializacionesStr = request.getParameterValues(ParameterNames.ID_ESPECIALIZACION);

			ProyectoDTO proyecto = new ProyectoDTO();

			List<Integer> idsEspecializaciones = new ArrayList<Integer>();


			// Validar y convertir los datos
			Long idProyecto = null;
			if (!StringUtils.isBlank(idProyectoStr)) {				
				idProyecto = Validator.validaLong(idProyectoStr);

				if (idProyecto!=null) {
					proyecto.setIdProyecto(idProyecto);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idProyecto: "+idProyectoStr);
					}
					errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {				
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio idProyecto: "+idProyectoStr);
				}
				errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_OBLIGATORIO);
			}


			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaTitulo(tituloStr);

				if (tituloStr!=null) {
					proyecto.setTitulo(tituloStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto titulo: "+tituloStr);
					}
					errors.addParameterError(ParameterNames.TITULO_PROYECTO, ErroresNames.ERROR_TITULO_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio titulo: "+tituloStr);
				}
				errors.addParameterError(ParameterNames.TITULO_PROYECTO, ErroresNames.ERROR_TITULO_PROYECTO_OBLIGATORIO);
			}



			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaDescripcion(descripcionStr);

				if (descripcionStr!=null) {
					proyecto.setDescripcion(descripcionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto descripcion: "+descripcionStr);
					}
					errors.addParameterError(ParameterNames.DESCRIPCION_PROYECTO, ErroresNames.ERROR_DESCRIPCION_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio descripcion: "+descripcionStr);
				}
				errors.addParameterError(ParameterNames.DESCRIPCION_PROYECTO, ErroresNames.ERROR_DESCRIPCION_PROYECTO_OBLIGATORIO);
			}



			Integer presupuestoMax = null;
			if (!StringUtils.isBlank(presupuestoMaxStr)) {
				presupuestoMax = Validator.validaPresupuestoMax(presupuestoMaxStr);

				if (presupuestoMax!=null) {
					proyecto.setPresupuestoMax(presupuestoMax);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato invalido presupuestoMax: "+presupuestoMaxStr);
					}
					errors.addParameterError(ParameterNames.PRESUPUESTO_MAX_PROYECTO, ErroresNames.ERROR_PRESUPUESTO_MAX_PROYECTO_FORMATO_INCORRECTO);
				}				

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco presupuestoMax: "+presupuestoMaxStr);
				}
				errors.addParameterError(ParameterNames.PRESUPUESTO_MAX_PROYECTO, ErroresNames.ERROR_PRESUPUESTO_MAX_PROYECTO_OBLIGATORIO);
			}



			Integer idPoblacion = null;
			if (!StringUtils.isBlank(idPoblacionStr)) {
				idPoblacion = Validator.validaPoblacion(idPoblacionStr);

				if(idPoblacion!=null) {
					proyecto.setIdPoblacion(idPoblacion);				
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idpoblacion: "+idPoblacionStr);
					}
					errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idpoblacion: "+idPoblacionStr);
				}
				errors.addParameterError(ParameterNames.ID_POBLACION, ErroresNames.ERROR_ID_POBLACION_OBLIGATORIO);
			}


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
				logger.trace("Proyecto: "+proyecto);
				logger.trace("Especializaciones: "+idsEspecializaciones);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					
					proyectoService.update(proyecto, idsEspecializaciones);
					
					if (logger.isInfoEnabled()) {
						logger.info("Proyecto actualizado: "+proyecto);
					}


					request.setAttribute(AttributeNames.PROYECTO, proyecto);

					// Dirigir a...
					targetView = ControllerNames.USUARIO;
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

		} else if (ActionNames.UPDATE_STATUS_PROYECTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			// Dirigir a...
			targetView = ControllerNames.USUARIO;
			forward = false;


			// Recoger los datos que enviamos desde la jsp
			String idProyectoStr = request.getParameter(ParameterNames.ID_PROYECTO);
			String idEstadoStr = request.getParameter(ParameterNames.ID_STATUS_PROYECTO);

			//TODO el de presupuesto es distinto
			// Validar y convertir los datos
			Long idProyecto = null;			
			if(!StringUtils.isBlank(idProyectoStr)) {
				idProyecto = Validator.validaLong(idProyectoStr);
			} 


			Integer idEstado = null;
			if(!StringUtils.isBlank(idEstadoStr)) {
				idEstado = Validator.validaStatusProyecto(idEstadoStr);
			}


			if (idProyecto==null || idEstado==null) {
				if (logger.isDebugEnabled()) {
					if (idProyecto==null) {
						logger.debug("Datos incorrecto idProyecto: "+idProyectoStr);
					} else {
						logger.debug("Datos incorrecto idEstado: "+idEstadoStr);
					}
				}
				errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_UPDATE_STATUS_PROYECTO_INVALIDO);
			}



			if (logger.isTraceEnabled()) {
				logger.trace("idProyecto: "+idProyecto+", idEstado: "+idEstado);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					proyectoService.updateStatus(idProyecto, idEstado);

					if (logger.isInfoEnabled()) {
						logger.info("Proyecto actualizado: "+idProyecto);
					}

					// Dirigir a..
					targetView =ViewNames.USUARIO_MIS_PROYECTOS;
					forward = true;
					

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
			// Dirigir a...
			targetView = ControllerNames.USUARIO;
			forward = false;
		}

		if (logger.isInfoEnabled()) {
			logger.info(forward?"Forwarding to ":"Redirecting to ", targetView);
		}

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