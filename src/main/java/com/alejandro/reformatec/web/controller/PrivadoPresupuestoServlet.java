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
import com.alejandro.reformatec.model.LineaPresupuestoDTO;
import com.alejandro.reformatec.model.PresupuestoCriteria;
import com.alejandro.reformatec.model.PresupuestoDTO;
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.service.PresupuestoService;
import com.alejandro.reformatec.service.impl.PresupuestoServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ConfigNames;
import com.alejandro.reformatec.web.util.ControllerNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;
import com.alejandro.reformatec.web.util.WebPagingUtils;


public class PrivadoPresupuestoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(PrivadoPresupuestoServlet.class);	

	private static final String CFGM_PFX = ConfigNames.PFX;
	private static final String PAGE_SIZE_DETAIL = CFGM_PFX + ConfigNames.PAGE_SIZE_DETAIL;
	private static final String PAGE_SIZE_SEARCH = CFGM_PFX +  ConfigNames.PAGE_SIZE_SEARCH;
	private static final String PAGE_COUNT = CFGM_PFX + ConfigNames.PAGE_COUNT;
	private ConfigurationManager cfgM = ConfigurationManager.getInstance();	
	
	private PresupuestoService presupuestoService = null;

	public PrivadoPresupuestoServlet() {
		super();
		presupuestoService = new PresupuestoServiceImpl();
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

		if (ActionNames.SEARCH_PRESUPUESTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String idPresupuestoStr = request.getParameter(ParameterNames.ID_PRESUPUESTO);
			String idProyectoStr = request.getParameter(ParameterNames.ID_PROYECTO);
			String idProveedorStr = request.getParameter(ParameterNames.ID_USUARIO);
			String idTipoEstadoPresupuestoStr = request.getParameter(ParameterNames.ID_STATUS_PRESUPUESTO);


			PresupuestoCriteria pc = new PresupuestoCriteria();

			Long idPresupuesto = null;
			if (!StringUtils.isBlank(idPresupuestoStr)) {
				idPresupuesto = Validator.validaLong(idPresupuestoStr);
			}
			pc.setIdPresupuesto(idPresupuesto);			

			
			Long idProyecto = null;
			Long idProveedor = null;
			Integer idTipoEstadoPresupuesto = null;			
			if (idPresupuesto==null) {
					
				if (!StringUtils.isBlank(idProyectoStr)) {
					idProyecto = Validator.validaLong(idProyectoStr);
				}
				pc.setIdProyecto(idProyecto);


				if(idProyecto==null) {
					
					if (!StringUtils.isBlank(idProveedorStr)) {
						idProveedor = Validator.validaLong(idProveedorStr);
					}
					pc.setIdProveedor(idProveedor);					
				}
				
				
				if (!StringUtils.isBlank(idTipoEstadoPresupuestoStr)) {
					idTipoEstadoPresupuesto = Validator.validaStatusPresupuesto(idTipoEstadoPresupuestoStr);
				}
				pc.setIdTipoEstadoPresupuesto(idTipoEstadoPresupuesto);
			}
	
			
			if (logger.isTraceEnabled()) {
				logger.trace(pc);
			}

			
			// Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {
					Integer currentPage = WebPagingUtils.getCurrentPage(request);
					Results<PresupuestoDTO> results = presupuestoService.findByCriteria(pc, (currentPage-1)*Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)) +1, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));

					request.setAttribute(AttributeNames.PRESUPUESTO, results);

					// Atributos para paginacion
					Integer totalPages = WebPagingUtils.getTotalPages(results.getTotal(), Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));
					request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
					request.setAttribute(AttributeNames.CURRENT_PAGE, currentPage);					
					request.setAttribute(AttributeNames.PAGING_FROM, WebPagingUtils.getPageFrom(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));
					request.setAttribute(AttributeNames.PAGING_TO, WebPagingUtils.getPageTo(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));

					//Dirigir a...
					targetView = ViewNames.PRESUPUESTO_RESULTS;
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


		} else if (ActionNames.DETAIL_PRESUPUESTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String idPresupuestoStr = request.getParameter(ParameterNames.ID_PRESUPUESTO);

			PresupuestoCriteria pc = new PresupuestoCriteria();			

			//Validar y convertir datos
			Long idPresupuesto = null;
			if  (!StringUtils.isBlank(idPresupuestoStr)) {
				idPresupuesto = Validator.validaLong(idPresupuestoStr);				
				
				if (idPresupuesto!=null) {
					pc.setIdPresupuesto(idPresupuesto);
					
				} else {
					if (logger.isErrorEnabled()) {
						logger.error("Formato incorrecto idPresupuesto: "+idPresupuestoStr);
					}
					errors.addParameterError(ParameterNames.ID_PRESUPUESTO, ErroresNames.ERROR_ID_PRESUPUESTO_FORMATO_INCORRECTO);	
				}
				
			} else {
				if (logger.isErrorEnabled()) {
					logger.error("Dato obligatorio idPresupuesto: "+idPresupuestoStr);
				}
				errors.addParameterError(ParameterNames.ID_PRESUPUESTO, ErroresNames.ERROR_ID_PRESUPUESTO_OBLIGATORIO);	
			}


			if (logger.isInfoEnabled()) {
				logger.info("Id del presupuesto: "+idPresupuesto);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					Results<PresupuestoDTO> results = presupuestoService.findByCriteria(pc, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)));

					request.setAttribute(AttributeNames.PRESUPUESTO, results);

					//Dirigir a...
					targetView = ViewNames.PRESUPUESTO_DETAIL;
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



		} else if (ActionNames.CREATE_PRESUPUESTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp para presupuesto
			String tituloStr = request.getParameter(ParameterNames.TITULO_PRESUPUESTO);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_PRESUPUESTO);
			String idProyectoStr = request.getParameter(ParameterNames.ID_PROYECTO);
			String idUsuarioCreadorPresupuestoStr = request.getParameter(ParameterNames.ID_USUARIO_CREADOR_PRESUPUESTO);

			List<LineaPresupuestoDTO> lineas = new ArrayList<LineaPresupuestoDTO>();			
			PresupuestoDTO presupuesto = new PresupuestoDTO();
			
			
			// Validar y convertir los datos
			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaTitulo(tituloStr);

				if (tituloStr!=null) {
					presupuesto.setTitulo(tituloStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto titulo: "+tituloStr);
					}
					errors.addParameterError(ParameterNames.TITULO_PRESUPUESTO, ErroresNames.ERROR_TITULO_PRESUPUESTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio titulo: "+tituloStr);
				}
				errors.addParameterError(ParameterNames.TITULO_PRESUPUESTO, ErroresNames.ERROR_TITULO_PRESUPUESTO_OBLIGATORIO);
			}


			
			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaDescripcion(descripcionStr);

				if (descripcionStr!=null) {
					presupuesto.setDescripcion(descripcionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto descripcion: "+descripcionStr);
					}
					errors.addParameterError(ParameterNames.DESCRIPCION_PRESUPUESTO, ErroresNames.ERROR_DESCRIPCION_PRESUPUESTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio descripcion: "+descripcionStr);
				}
				errors.addParameterError(ParameterNames.DESCRIPCION_PRESUPUESTO, ErroresNames.ERROR_DESCRIPCION_PRESUPUESTO_OBLIGATORIO);
			}
			
			
			
			Long idProyecto = null;
			if (!StringUtils.isBlank(idProyectoStr)) {				
				idProyecto = Validator.validaLong(idProyectoStr);

				if (idProyecto!=null) {
					presupuesto.setIdProyecto(idProyecto);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idProyecto: "+idProyectoStr);
					}
					errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio idProyecto: "+idProyectoStr);
				}
				errors.addParameterError(ParameterNames.ID_PROYECTO, ErroresNames.ERROR_ID_PROYECTO_OBLIGATORIO);
			}			


			
			Long idUsuarioCreadorPresupuesto = null;
			if (!StringUtils.isBlank(idUsuarioCreadorPresupuestoStr)) {		
				idUsuarioCreadorPresupuesto = Validator.validaLong(idUsuarioCreadorPresupuestoStr);

				if (idUsuarioCreadorPresupuesto!=null) {
					presupuesto.setIdUsuarioCreadorPresupuesto(idUsuarioCreadorPresupuesto);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idUsuarioCreadorPresupuesto: "+idUsuarioCreadorPresupuestoStr);
					}
					errors.addParameterError(ParameterNames.ID_USUARIO_CREADOR_PRESUPUESTO, ErroresNames.ERROR_ID_USUARIO_CREADOR_PRESUPUESTO_FORMATO_INCORRECTO);
				}

			} else {				
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio idUsuarioCreadorPresupuesto: "+idUsuarioCreadorPresupuestoStr);
				}
				errors.addParameterError(ParameterNames.ID_USUARIO_CREADOR_PRESUPUESTO, ErroresNames.ERROR_ID_USUARIO_CREADOR_PRESUPUESTO_OBLIGATORIO);
			}

			
			
			// Recoger los datos que enviamos desde la jsp para linea presupuesto
			String totalLineasStr = request.getParameter(ParameterNames.CANTIDAD_LINEAS_PRESUPUESTO);
			
			Integer totalLineas = null;
			if (!StringUtils.isBlank(totalLineasStr)) {
				totalLineas = Validator.validaInteger(totalLineasStr);
				if (totalLineas!=null) {
					
					for (int i = 0; i<totalLineas; i++) {
						String descripcionLineasStr = request.getParameter(ParameterNames.DESCRIPCION_LINEA_PRESUPUESTO+(i+1));
						String importeLineasStr = request.getParameter(ParameterNames.IMPORTE_LINEA_PRESUPUESTO+(i+1));
						
						LineaPresupuestoDTO lineaPresupuesto = new LineaPresupuestoDTO();
						
						
						//Validar Datos de linea presupuesto						
						if (!StringUtils.isBlank(descripcionLineasStr)) {
							descripcionLineasStr = Validator.validaDescripcion(descripcionLineasStr);
							
							if (descripcionLineasStr!=null) {						
								lineaPresupuesto.setDescripcion(descripcionLineasStr);
								
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("Formato invalido descripcionLinea: "+descripcionLineasStr);
								}
								errors.addParameterError(ParameterNames.DESCRIPCION_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_DESCRIPCION_LINEA_PRESUPUESTO_FORMATO_INCORRECTO+(i+1));
							}
							
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Dato obligatorio descripcionLinea: "+descripcionLineasStr);
							}
							errors.addParameterError(ParameterNames.DESCRIPCION_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_DESCRIPCION_LINEA_PRESUPUESTO_OBLIGATORIO+(i+1));
							
						}
						
						
						if (!StringUtils.isBlank(importeLineasStr)) {
							Double importeDouble = Validator.validaImporteLinea(importeLineasStr);
							
							if (importeDouble!=null) {
								lineaPresupuesto.setImporte(importeDouble);
								lineas.add(lineaPresupuesto);								
								
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("Formato invalido importeLinea: "+importeLineasStr);
								}
								errors.addParameterError(ParameterNames.IMPORTE_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_IMPORTE_LINEA_PRESUPUESTO_FORMATO_INCORRECTO+(i+1));
							}
							
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Dato obligatorio importeLinea: "+importeLineasStr);
							}
							errors.addParameterError(ParameterNames.IMPORTE_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_IMPORTE_LINEA_PRESUPUESTO_OBLIGATORIO+(i+1));
							
						}
					}
					presupuesto.setLineas(lineas);
					
	
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("El formato de la cantidad de lineas no es correcto: "+totalLineasStr);
					}
					// Le estoy poniendo el mismo error que si no hubiera nada, porque no deberian estar fozando para que pase esto
					errors.addParameterError(ParameterNames.CANTIDAD_LINEAS_PRESUPUESTO, ErroresNames.ERROR_LINEA_PRESUPUESTO_OBLIGATORIA);
				}
				
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("El presupuesto debe contener almenos 1 linea de presupuesto: "+totalLineasStr);
				}
				errors.addParameterError(ParameterNames.CANTIDAD_LINEAS_PRESUPUESTO, ErroresNames.ERROR_LINEA_PRESUPUESTO_OBLIGATORIA);
			}
			
			

			if (logger.isTraceEnabled()) {
				logger.trace("Presupuesto: "+presupuesto);
				logger.trace("Lineas presupuesto: "+lineas);
			}
			
			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					presupuestoService.create(presupuesto, lineas);
					if (logger.isInfoEnabled()) {
						logger.info("Presupuesto Creado: "+presupuesto);
					}

					request.setAttribute(AttributeNames.PRESUPUESTO, presupuesto);

					// Dirigir a..
					targetView =ViewNames.USUARIO_PERFIL;
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
			
			

		} else if (ActionNames.UPDATE_PRESUPUESTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String idPresupuestoStr = request.getParameter(ParameterNames.ID_PRESUPUESTO);
			String tituloStr = request.getParameter(ParameterNames.TITULO_PRESUPUESTO);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_PRESUPUESTO);
			String idProyectoStr = request.getParameter(ParameterNames.ID_PROYECTO);
			String idUsuarioCreadorPresupuestoStr = request.getParameter(ParameterNames.ID_USUARIO_CREADOR_PRESUPUESTO);

			List<LineaPresupuestoDTO> lineas = new ArrayList<LineaPresupuestoDTO>();			
			PresupuestoDTO presupuesto = new PresupuestoDTO();
			
			
			// Validar y convertir los datos
			Long idPresupuesto = null;
			if (!StringUtils.isBlank(idPresupuestoStr)) {				
				idPresupuesto = Validator.validaLong(idPresupuestoStr);

				if (idPresupuesto!=null) {
					presupuesto.setIdPresupuesto(idPresupuesto);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idPresupuesto: "+idPresupuestoStr);
					}
					errors.addParameterError(ParameterNames.ID_PRESUPUESTO, ErroresNames.ERROR_ID_PRESUPUESTO_FORMATO_INCORRECTO);
				}

			} else {				
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio idPresupuesto: "+idPresupuestoStr);
				}
				errors.addParameterError(ParameterNames.ID_PRESUPUESTO, ErroresNames.ERROR_ID_PRESUPUESTO_OBLIGATORIO);
			}


			
			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = Validator.validaTitulo(tituloStr);

				if (tituloStr!=null) {
					presupuesto.setTitulo(tituloStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto titulo: "+tituloStr);
					}
					errors.addParameterError(ParameterNames.TITULO_PRESUPUESTO, ErroresNames.ERROR_TITULO_PRESUPUESTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio titulo: "+tituloStr);
				}
				errors.addParameterError(ParameterNames.TITULO_PRESUPUESTO, ErroresNames.ERROR_TITULO_PRESUPUESTO_OBLIGATORIO);
			}


			
			if (!StringUtils.isBlank(descripcionStr)) {
				descripcionStr = Validator.validaDescripcion(descripcionStr);

				if (descripcionStr!=null) {
					presupuesto.setDescripcion(descripcionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto descripcion: "+descripcionStr);
					}
					errors.addParameterError(ParameterNames.DESCRIPCION_PRESUPUESTO, ErroresNames.ERROR_DESCRIPCION_PRESUPUESTO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio descripcion: "+descripcionStr);
				}
				errors.addParameterError(ParameterNames.DESCRIPCION_PRESUPUESTO, ErroresNames.ERROR_DESCRIPCION_PRESUPUESTO_OBLIGATORIO);
			}


			
			Long idProyecto = null;
			if (!StringUtils.isBlank(idProyectoStr)) {				
				idProyecto = Validator.validaLong(idProyectoStr);

				if (idProyecto!=null) {
					presupuesto.setIdProyecto(idProyecto);					
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


			
			Long idUsuarioCreadorPresupuesto = null;
			if (!StringUtils.isBlank(idUsuarioCreadorPresupuestoStr)) {				
				idUsuarioCreadorPresupuesto = Validator.validaLong(idUsuarioCreadorPresupuestoStr);

				if (idUsuarioCreadorPresupuesto!=null) {
					presupuesto.setIdUsuarioCreadorPresupuesto(idUsuarioCreadorPresupuesto);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Formato incorrecto idCreadorPresupuesto: "+idUsuarioCreadorPresupuestoStr);
					}
					errors.addParameterError(ParameterNames.ID_PRESUPUESTO, ErroresNames.ERROR_ID_PRESUPUESTO_FORMATO_INCORRECTO);
				}

			} else {				
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio idCreadorPresupuesto: "+idUsuarioCreadorPresupuestoStr);
				}
				errors.addParameterError(ParameterNames.ID_PRESUPUESTO, ErroresNames.ERROR_ID_PRESUPUESTO_OBLIGATORIO);
			}

			
			// Recoger los datos que enviamos desde la jsp para linea presupuesto
			String totalLineasStr = request.getParameter(ParameterNames.CANTIDAD_LINEAS_PRESUPUESTO);
			
			Integer totalLineas = null;
			if (!StringUtils.isBlank(totalLineasStr)) {
				totalLineas = Validator.validaInteger(totalLineasStr);
				if (totalLineas!=null) {
					
					for (int i = 0; i<totalLineas; i++) {
						String descripcionLineasStr = request.getParameter(ParameterNames.DESCRIPCION_LINEA_PRESUPUESTO+(i+1));
						String importeLineasStr = request.getParameter(ParameterNames.IMPORTE_LINEA_PRESUPUESTO+(i+1));
						
						LineaPresupuestoDTO lineaPresupuesto = new LineaPresupuestoDTO();
						
						
						//Validar Datos de linea presupuesto						
						if (!StringUtils.isBlank(descripcionLineasStr)) {
							descripcionLineasStr = Validator.validaDescripcion(descripcionLineasStr);
							
							if (descripcionLineasStr!=null) {						
								lineaPresupuesto.setDescripcion(descripcionLineasStr);
								
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("Formato invalido descripcionLinea: "+descripcionLineasStr);
								}
								errors.addParameterError(ParameterNames.DESCRIPCION_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_DESCRIPCION_LINEA_PRESUPUESTO_FORMATO_INCORRECTO+(i+1));
							}
							
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Dato obligatorio descripcionLinea: "+descripcionLineasStr);
							}
							errors.addParameterError(ParameterNames.DESCRIPCION_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_DESCRIPCION_LINEA_PRESUPUESTO_OBLIGATORIO+(i+1));
							
						}
						
						
						if (!StringUtils.isBlank(importeLineasStr)) {
							Double importeDouble = Validator.validaImporteLinea(importeLineasStr);
							
							if (importeDouble!=null) {
								lineaPresupuesto.setImporte(importeDouble);
								lineas.add(lineaPresupuesto);								
								
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("Formato invalido importeLinea: "+importeLineasStr);
								}
								errors.addParameterError(ParameterNames.IMPORTE_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_IMPORTE_LINEA_PRESUPUESTO_FORMATO_INCORRECTO+(i+1));
							}
							
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Dato obligatorio importeLinea: "+importeLineasStr);
							}
							errors.addParameterError(ParameterNames.IMPORTE_LINEA_PRESUPUESTO+(i+1), ErroresNames.ERROR_IMPORTE_LINEA_PRESUPUESTO_OBLIGATORIO+(i+1));
							
						}
					}
					presupuesto.setLineas(lineas);
					
	
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("El formato de la cantidad de lineas no es correcto: "+totalLineasStr);
					}
					// Le estoy poniendo el mismo error que si no hubiera nada, porque no deberian estar fozando para que pase esto
					errors.addParameterError(ParameterNames.CANTIDAD_LINEAS_PRESUPUESTO, ErroresNames.ERROR_LINEA_PRESUPUESTO_OBLIGATORIA);
				}
				
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("El presupuesto debe contener almenos 1 linea de presupuesto: "+totalLineasStr);
				}
				errors.addParameterError(ParameterNames.CANTIDAD_LINEAS_PRESUPUESTO, ErroresNames.ERROR_LINEA_PRESUPUESTO_OBLIGATORIA);
			}

			
			
			if (logger.isTraceEnabled()) {
				logger.trace("Presupuesto: "+presupuesto);
				logger.trace("Lineas presupuesto: "+lineas);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					presupuestoService.update(presupuesto, lineas);
					
					if (logger.isInfoEnabled()) {
						logger.info("Presupuesto actualizado: "+presupuesto);
					}

					request.setAttribute(AttributeNames.PRESUPUESTO, presupuesto);

					// Dirigir a..
					targetView =ViewNames.USUARIO_PERFIL;
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



		} else if (ActionNames.UPDATE_STATUS_PRESUPUESTO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String idPresupuestoStr = request.getParameter(ParameterNames.ID_PRESUPUESTO);
			String idEstadoStr = request.getParameter(ParameterNames.ID_STATUS_PRESUPUESTO);

			
			// Validar y convertir los datos
			Long idPresupuesto = null;			
			if(!StringUtils.isBlank(idPresupuestoStr)) {
				idPresupuesto = Validator.validaLong(idPresupuestoStr);
			} 


			Integer idEstado = null;
			if(!StringUtils.isBlank(idEstadoStr)) {
				idEstado = Validator.validaStatusPresupuesto(idEstadoStr);
			}


			if (idPresupuesto==null || idEstado==null) {
				if (logger.isDebugEnabled()) {
					if (idPresupuesto==null) {
						logger.debug("Datos incorrecto idPresupuesto: "+idPresupuestoStr);
					} else {
						logger.debug("Datos incorrecto idEstado: "+idEstadoStr);
					}
				}
				errors.addParameterError(ParameterNames.ID_PRESUPUESTO, ErroresNames.ERROR_UPDATE_STATUS_PRESUPUESTO_INVALIDO);
			}

			
			if (logger.isTraceEnabled()) {
				logger.trace("idPresupuesto: "+idPresupuesto+", idEstado: "+idEstado);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					presupuestoService.updateStatus(idPresupuesto, idEstado);
					if (logger.isInfoEnabled()) {
						logger.info("Presupuesto actualizado con id: "+idPresupuesto);
					}

					// Dirigir a..
					targetView =ViewNames.USUARIO_MIS_PRESUPUESTOS;
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