package com.alejandro.reformatec.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.dao.util.ConfigurationManager;
import com.alejandro.reformatec.dao.util.ConstantConfigUtil;
import com.alejandro.reformatec.exception.CodeInvalidException;
import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.EmailPendienteValidacionException;
import com.alejandro.reformatec.exception.InvalidUserOrPasswordException;
import com.alejandro.reformatec.exception.MailException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.exception.UserAlreadyExistsException;
import com.alejandro.reformatec.exception.UserLowInTheSystemException;
import com.alejandro.reformatec.exception.UserNotFoundException;
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.model.TipoUsuario;
import com.alejandro.reformatec.model.UsuarioCriteria;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.model.ValoracionCriteria;
import com.alejandro.reformatec.model.ValoracionDTO;
import com.alejandro.reformatec.service.MailService;
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.ValoracionService;
import com.alejandro.reformatec.service.impl.MailServiceImpl;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
import com.alejandro.reformatec.service.impl.ValoracionServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ConfigNames;
import com.alejandro.reformatec.web.util.ControllerNames;
import com.alejandro.reformatec.web.util.CookieManager;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.SessionManager;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;
import com.alejandro.reformatec.web.util.WebPagingUtils;


public class UsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(UsuarioServlet.class);


	private static final String CFGM_PFX = ConfigNames.PFX;
	private static final String PAGE_SIZE_DETAIL = CFGM_PFX + ConfigNames.PAGE_SIZE_DETAIL;
	private static final String PAGE_SIZE_SEARCH = CFGM_PFX +  ConfigNames.PAGE_SIZE_SEARCH;
	private static final String PAGE_SIZE_PROVEEDORES_TOP = CFGM_PFX +  ConfigNames.PAGE_SIZE_PROVEEDORES_TOP;
	private static final String PAGE_SIZE_VALORACION = CFGM_PFX +  ConfigNames.PAGE_SIZE_VALORACION;
	private static final String PAGE_COUNT = CFGM_PFX + ConfigNames.PAGE_COUNT;
	private static final String START_INDEX = CFGM_PFX + ConfigNames.START_INDEX;
	private static final String MAIL = CFGM_PFX + ConfigNames.MAIL;
	private ConfigurationManager cfgM = ConfigurationManager.getInstance();	

	private UsuarioService usuarioService = null;
	private ValoracionService valoracionService = null;
	private MailService mailService = null;


	public UsuarioServlet() {
		super();
		usuarioService = new UsuarioServiceImpl();
		valoracionService = new ValoracionServiceImpl();
		mailService = new MailServiceImpl();
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



		// Recogemos primero la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.SEARCH_USUARIO.equalsIgnoreCase(action)) {

			//Direcci?n de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String buscarStr = request.getParameter(ParameterNames.BUSCAR_DESCRIPCION);
			String idProvinciaStr = request.getParameter(ParameterNames.ID_PROVINCIA);
			String especializacionStr = request.getParameter(ParameterNames.ID_ESPECIALIZACION);
			String servicio24Str = request.getParameter(ParameterNames.SERVICIO_24);
			String proveedorVerificadoStr = request.getParameter(ParameterNames.PROVEEDOR_VERIFICADO);
			String orderByStr = request.getParameter(ParameterNames.ORDER_BY);


			UsuarioCriteria uc = new UsuarioCriteria();

			//Convertir y validar datos
			if (!StringUtils.isBlank(buscarStr)) {
				// aqu? hay mucho que hacer, ver txt
				buscarStr.trim();
				uc.setDescripcion(buscarStr);
			}

			Integer idProvincia = null;
			if (!StringUtils.isBlank(idProvinciaStr)) {
				idProvincia = Validator.validaProvincia(idProvinciaStr);
			}
			uc.setIdProvincia(idProvincia);


			Integer especializacion = null;
			if (!StringUtils.isBlank(especializacionStr)) {
				especializacion = Validator.validaEspecializacion(especializacionStr);
			}
			uc.setIdEspecializacion(especializacion);


			Boolean servicio24 = null;
			if (!StringUtils.isBlank(servicio24Str)) {
				servicio24 = Validator.validaBoolean(servicio24Str);
			}
			uc.setServicio24(servicio24);


			Boolean proveedorVerificado = null;
			if (!StringUtils.isBlank(proveedorVerificadoStr)) {
				proveedorVerificado = Validator.validaBoolean(proveedorVerificadoStr);
			}
			uc.setProveedorVerificado(proveedorVerificado);


			if (!StringUtils.isBlank(orderByStr)) {
				orderByStr = Validator.validaOrderByUsuario(orderByStr);
				uc.setOrderBy(orderByStr);
			}


			if (logger.isTraceEnabled()) {
				logger.trace("Busqueda usuario: "+uc);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					Integer currentPage = WebPagingUtils.getCurrentPage(request);

					Results<UsuarioDTO> results = usuarioService.findByCriteria(uc, (currentPage-1)*Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)) +1, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));

					request.setAttribute(AttributeNames.USUARIO, results);

					// Atributos para paginacion
					Integer totalPages = WebPagingUtils.getTotalPages(results.getTotal(), Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));
					request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
					request.setAttribute(AttributeNames.CURRENT_PAGE, currentPage);
					request.setAttribute(AttributeNames.PAGING_FROM, WebPagingUtils.getPageFrom(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));
					request.setAttribute(AttributeNames.PAGING_TO, WebPagingUtils.getPageTo(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));


					// Dirigir a...
					targetView =ViewNames.USUARIO_RESULTS;
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




		} else if (ActionNames.DETAIL_USUARIO.equalsIgnoreCase(action)) {

			//Direcci?n de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			// Recoger los datos que enviamos desde la jsp
			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);

			UsuarioCriteria uc = new UsuarioCriteria();
			ValoracionCriteria vc = new ValoracionCriteria();	

			//Validar y convertir datos
			Long idUsuario = null;
			if  (!StringUtils.isBlank(idUsuarioStr)) {
				idUsuario = Validator.validaLong(idUsuarioStr);

				if (idUsuario!=null) {
					uc.setIdUsuario(idUsuario);
					vc.setIdProveedorValorado(idUsuario);

				} else {
					if (logger.isErrorEnabled()) {
						logger.error("Formato incorrecto idUsuario: "+idUsuarioStr);
					}
					errors.addParameterError(ParameterNames.ID_USUARIO, ErroresNames.ERROR_ID_USUARIO_FORMATO_INCORRECTO);	
				}

			} else {
				if (logger.isErrorEnabled()) {
					logger.error("Dato null/blanco idUsuario: "+idUsuarioStr);
				}
				errors.addParameterError(ParameterNames.ID_USUARIO, ErroresNames.ERROR_ID_USUARIO_OBLIGATORIO);	
			}


			if (logger.isTraceEnabled()) {
				logger.trace("Id del usuario: "+idUsuario);
			}



			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					Integer currentPage = WebPagingUtils.getCurrentPage(request);

					Results<UsuarioDTO> usuario = usuarioService.findByCriteria(uc, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, START_INDEX)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)));
					request.setAttribute(AttributeNames.USUARIO, usuario);


					usuarioService.visualizaUsuario(idUsuario);


					Results<ValoracionDTO> valoraciones = valoracionService.findByCriteria(vc, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, START_INDEX)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_VALORACION)));

					request.setAttribute(AttributeNames.VALORACION, valoraciones);


					// Atributos para paginacion
					Integer totalPages = WebPagingUtils.getTotalPages(valoraciones.getTotal(), Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_VALORACION)));
					request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
					request.setAttribute(AttributeNames.CURRENT_PAGE, currentPage);
					request.setAttribute(AttributeNames.PAGING_FROM, WebPagingUtils.getPageFrom(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));
					request.setAttribute(AttributeNames.PAGING_TO, WebPagingUtils.getPageTo(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));




					// Dirigir a...
					targetView =ViewNames.USUARIO_DETAIL;
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



		}else if (ActionNames.LOGIN.equalsIgnoreCase(action)) {

			//Direcci?n de la vista predefinida(en caso de error)
			targetView = ViewNames.USUARIO_LOGIN;
			forward = true;

			// Recoger los datos que enviamos desde la jsp
			String emailStr = request.getParameter(ParameterNames.EMAIL);
			String passwordStr = request.getParameter(ParameterNames.PASSWORD);
			String keepAuthenticatedStr = request.getParameter(ParameterNames.KEEP_AUTHENTICATED);


			// Convertir y validar datos
			if (StringUtils.isBlank(emailStr)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco email: "+emailStr);
				}
				errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_OBLIGATORIO);
			} else {
				emailStr=emailStr.trim();

				if (!Validator.VALIDA_EMAIL.isValid(emailStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto email: "+emailStr);
					}
					errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_FORMATO_INCORRECTO);
				}
			}



			if (StringUtils.isBlank(passwordStr)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco password:");
				}
				errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
			} else {
				passwordStr=passwordStr.trim();
				if (!Validator.validaPassword(passwordStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto password:");
					}
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
				}			
			}			



			Boolean keepAuthenticated = null;		
			if (!StringUtils.isBlank(keepAuthenticatedStr)) {
				if (Validator.validaBoolean(keepAuthenticatedStr)) {
					keepAuthenticated = Validator.validaBoolean(keepAuthenticatedStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto keepAuthenticatedStr: "+keepAuthenticatedStr);
					}
				}
			}



			if (logger.isTraceEnabled()) {
				logger.trace("Email: "+emailStr+", KeepAuthenticated: "+keepAuthenticated);
			}



			//Acceder a la capa de negocio(si no hay errores)
			try {

				UsuarioCriteria uc = new UsuarioCriteria();
				uc.setEmailActivo(emailStr);
				UsuarioDTO user = usuarioService.findByEmail(uc);

				Set<Long> usuariosIds = usuarioService.findFavorito(user.getIdUsuario());

				SessionManager.set(request, AttributeNames.FAVORITOS, usuariosIds);


				UsuarioDTO u = usuarioService.login(emailStr, passwordStr);

				if (logger.isInfoEnabled()) {
					logger.info("User "+emailStr+" authenticated sucessfully.");
				}

				SessionManager.set(request, AttributeNames.USUARIO, u);


				if (keepAuthenticated!=null) {						
					//TODO Falta verificar la ip...
					CookieManager.setValue(response, AttributeNames.USUARIO, emailStr, 30*24*60*60); // Agujero! Comprobar ip
				} else {
					CookieManager.setValue(response, AttributeNames.USUARIO, emailStr, 0); // Agujero! Comprobar ip
				}

				// Dirigir a...
				targetView = ControllerNames.USUARIO;
				forward = false;


			}catch (EmailPendienteValidacionException epve) {
				if (logger.isErrorEnabled()) {
					logger.error("EmailPendienteValidacionException: "+epve.getMessage(), epve);
				}
				errors.addCommonError(ErroresNames.ERROR_EMAIL_SIN_VALIDAR);				

			}catch (UserLowInTheSystemException ulise) {
				if (logger.isErrorEnabled()) {
					logger.error("UserLowInTheSystemException: "+ulise.getMessage(), ulise);
				}
				errors.addCommonError(ErroresNames.ERROR_USER_LOW_IN_SYSTEM);

			}catch (InvalidUserOrPasswordException iope) {
				if (logger.isErrorEnabled()) {
					logger.error("InvalidUserOrPasswordException: "+iope.getMessage(), iope);
				}
				errors.addCommonError(ErroresNames.ERROR_USUARIO_PASSWORD_INVALID);

			}catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error("DataException: "+de.getMessage(), de);
				}
				errors.addCommonError(ErroresNames.ERROR_DATA);

			}catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error("ServiceException: "+se.getMessage(), se);
				}
				errors.addCommonError(ErroresNames.ERROR_SERVICE);

			}catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("Exception: "+e.getMessage(), e);
				}
				errors.addCommonError(ErroresNames.ERROR_E);
			}



		} else if (ActionNames.SINGUP.equalsIgnoreCase(action)){

			//Direcci?n de la vista predefinida(en caso de error)
			targetView = ViewNames.USUARIO_REGISTRO;
			forward=true;


			// Recoger los datos que enviamos desde la jsp
			String idTipoUsuarioStr = request.getParameter(ParameterNames.ID_TIPO_USUARIO);
			String userNameStr = request.getParameter(ParameterNames.NOMBRE_PERFIL);
			String emailStr = request.getParameter(ParameterNames.EMAIL);
			String email2Str = request.getParameter(ParameterNames.EMAIL_2);
			String passwordStr = request.getParameter(ParameterNames.PASSWORD);
			String password2Str = request.getParameter(ParameterNames.PASSWORD_2);
			String dniStr = request.getParameter(ParameterNames.DNI);
			String nombreStr = request.getParameter(ParameterNames.NOMBRE);
			String primerApellidoStr = request.getParameter(ParameterNames.APELLIDO_1);
			String segundoApellidoStr = request.getParameter(ParameterNames.APELLIDO_2);
			String telefono1Str = request.getParameter(ParameterNames.TELEFONO_1);
			String telefono2Str = request.getParameter(ParameterNames.TELEFONO_2);
			String direccionStr = request.getParameter(ParameterNames.DIRECCION);
			String codPostalStr = request.getParameter(ParameterNames.COD_POSTAL);
			String idPoblacionStr = request.getParameter(ParameterNames.ID_POBLACION);
			String cifStr = request.getParameter(ParameterNames.CIF);
			String direccionWebStr = request.getParameter(ParameterNames.DIRECCION_WEB);
			String servicio24Str = request.getParameter(ParameterNames.SERVICIO_24);			
			String [] idsEspecializacionesStr = request.getParameterValues(ParameterNames.ID_ESPECIALIZACION);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_USUARIO);

			//TODO Falta la descripcion del usuario

			UsuarioDTO usuario = new UsuarioDTO();

			List<Integer> idsEspecializaciones = new ArrayList<Integer>();


			// Validar y Convertir Datos			
			Integer idTipoUsuario = null;
			if (!StringUtils.isBlank(idTipoUsuarioStr)) {				
				idTipoUsuario = Validator.validaIdTipoUsuario(idTipoUsuarioStr);

				if (idTipoUsuario!=null) {
					usuario.setIdTipoUsuario(idTipoUsuario);					
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idTipoUsuario"+idTipoUsuarioStr);
					}
					errors.addParameterError(ParameterNames.ID_TIPO_USUARIO, ErroresNames.ERROR_ID_TIPO_USUARIO_FORMATO_INCORRECTO);
				}

			} else {				
				if (logger.isDebugEnabled()) {
					logger.debug("Dato obligatorio idTipoUsuario"+idTipoUsuarioStr);
				}
				errors.addParameterError(ParameterNames.ID_TIPO_USUARIO, ErroresNames.ERROR_ID_TIPO_USUARIO_OBLIGATORIO);
			}


			// Dirigimos la vista en caso de error en funci?n del tipo de dato (idTipoUsuario)
			if (errors.hasErrors()) {
				targetView = ControllerNames.USUARIO;
				forward = false;
			} else {

				if (idTipoUsuario==TipoUsuario.USUARIO_PROVEEDOR) {
					targetView = ViewNames.USUARIO_REGISTRO_PROVEEDOR;
					forward =true;
				} else if (idTipoUsuario==TipoUsuario.USUARIO_CLIENTE){
					targetView = ViewNames.USUARIO_REGISTRO_CLIENTE;
					forward =true;
				} else {
					// Dirigir a...
					targetView = ControllerNames.USUARIO;
					forward = false;
				}



				if (!StringUtils.isBlank(userNameStr)) {
					userNameStr = Validator.validaNombrePerfil(userNameStr);

					if (userNameStr!=null) {
						usuario.setNombrePerfil(userNameStr);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto Nombre de perfil"+userNameStr);
						}
						errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_FORMATO_INCORRECTO);
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco Nombre de perfil: "+userNameStr);
					}
					errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_OBLIGATORIO);
				}



				if (StringUtils.isBlank(emailStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco email: "+emailStr);
					}
					errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_OBLIGATORIO);
				} else {
					emailStr=emailStr.trim();

					if (!Validator.VALIDA_EMAIL.isValid(emailStr)) {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto email: "+emailStr);
						}
						errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_FORMATO_INCORRECTO);
					}
				}



				if (StringUtils.isBlank(email2Str)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco email2: "+email2Str);
					}
					errors.addParameterError(ParameterNames.EMAIL_2, ErroresNames.ERROR_EMAIL_OBLIGATORIO);					
				} else {
					email2Str=email2Str.trim();

					if (!Validator.VALIDA_EMAIL.isValid(email2Str)) {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto email2 : "+email2Str);
						}
						errors.addParameterError(ParameterNames.EMAIL_2, ErroresNames.ERROR_EMAIL_FORMATO_INCORRECTO);
					}				
				}

				if (logger.isTraceEnabled()) {
					logger.trace("Validando iguales... email: "+emailStr+", email 2: "+email2Str);
				}

				if (emailStr.equals(email2Str)) {
					emailStr=emailStr.trim();
					usuario.setEmail(emailStr);					
				} else {
					if (logger.isTraceEnabled()) {
						logger.trace("El Email 1 "+emailStr+", y el Email 2 "+email2Str+", no son iguales");
					}
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_EMAILS_DIFERENTES);
				}



				if (StringUtils.isBlank(passwordStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco password:");
					}
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
				} else {
					passwordStr=passwordStr.trim();
					if (!Validator.validaPassword(passwordStr)) {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto password:");
						}
						errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
					}			
				}



				if (StringUtils.isBlank(password2Str)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco password2");
					}
					errors.addParameterError(ParameterNames.PASSWORD_2, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
				} else {
					password2Str=password2Str.trim();

					if (!Validator.validaPassword(password2Str)) {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto password2");
						}
						errors.addParameterError(ParameterNames.PASSWORD_2, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
					}			
				}

				if (logger.isTraceEnabled()) {
					logger.trace("Validando Password iguales...");
				}

				if (passwordStr.equals(password2Str)) {
					passwordStr=passwordStr.trim();
					usuario.setPassword(passwordStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Las passwords no son iguales");
					}
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORDS_DIFERENTES);
				}



				if (!StringUtils.isBlank(dniStr)) {
					dniStr=dniStr.trim();

					if (Validator.validaDni(dniStr)) {
						usuario.setNif(dniStr);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto dni: "+dniStr);
						}
						errors.addParameterError(ParameterNames.DNI, ErroresNames.ERROR_DNI_FORMATO_INCORRECTO);
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco dni: "+dniStr);
					}
					errors.addParameterError(ParameterNames.DNI, ErroresNames.ERROR_DNI_OBLIGATORIO);			
				}



			
				if (!StringUtils.isBlank(nombreStr)) {

					if (Validator.validaNombreOApellido(nombreStr)) {
						usuario.setNombre(nombreStr);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto nombre: "+primerApellidoStr);
						}
						errors.addParameterError(ParameterNames.NOMBRE, ErroresNames.ERROR_NOMBRE_FORMATO_INCORRECTO);
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco nombre: "+primerApellidoStr);
					}
					errors.addParameterError(ParameterNames.NOMBRE, ErroresNames.ERROR_NOMBRE_OBLIGATORIO);
				}	
				
				

				if (!StringUtils.isBlank(primerApellidoStr)) {

					if (Validator.validaNombreOApellido(primerApellidoStr)) {
						usuario.setApellido1(primerApellidoStr);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto primerApellido: "+primerApellidoStr);
						}
						errors.addParameterError(ParameterNames.APELLIDO_1, ErroresNames.ERROR_APELLIDO_1_FORMATO_INCORRECTO);
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco primerApellido: "+primerApellidoStr);
					}
					errors.addParameterError(ParameterNames.APELLIDO_1, ErroresNames.ERROR_APELLIDO_1_OBLIGATORIO);
				}	




				if (!StringUtils.isBlank(segundoApellidoStr)) {

					if (Validator.validaNombreOApellido(segundoApellidoStr)) {
						usuario.setApellido2(segundoApellidoStr);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto segundoApellido: "+segundoApellidoStr);
						}
						errors.addParameterError(ParameterNames.APELLIDO_2, ErroresNames.ERROR_APELLIDO_2_FORMATO_INCORRECTO);
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco segundoApellido: "+segundoApellidoStr);
					}
					errors.addParameterError(ParameterNames.APELLIDO_2, ErroresNames.ERROR_APELLIDO_2_OBLIGATORIO);
				}		




				if (!StringUtils.isBlank(telefono1Str)) {

					if (Validator.validaTelefono(telefono1Str)) {
						telefono1Str=telefono1Str.trim();
						usuario.setTelefono1(telefono1Str);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto telefono1: "+telefono1Str);
						}
						errors.addParameterError(ParameterNames.TELEFONO_1, ErroresNames.ERROR_TELEFONO_1_FORMATO_INCORRECTO);
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco telefono1: "+telefono1Str);
					}
					errors.addParameterError(ParameterNames.TELEFONO_1, ErroresNames.ERROR_TELEFONO_1_OBLIGATORIO);
				}			




				if (!StringUtils.isBlank(telefono2Str)) {
					telefono2Str = telefono2Str.trim();

					if (Validator.validaTelefono(telefono2Str)) {
						usuario.setTelefono2(telefono2Str);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto telefono2: "+telefono2Str);
						}
						errors.addParameterError(ParameterNames.TELEFONO_2, ErroresNames.ERROR_TELEFONO_2_FORMATO_INCORRECTO);
					}

				} else {
					telefono2Str=null;
					usuario.setTelefono2(telefono2Str);
				}




				if (!StringUtils.isBlank(direccionStr)) {
					direccionStr=direccionStr.trim();
					usuario.setNombreCalle(direccionStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco direccion: "+direccionStr);
					}
					errors.addParameterError(ParameterNames.DIRECCION, ErroresNames.ERROR_DIRECCION_OBLIGATORIO);
				}



				//!! Falta validar cod postal
				if (!StringUtils.isBlank(codPostalStr)) {
					codPostalStr=codPostalStr.trim();
					usuario.setCodigoPostal(codPostalStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco codPostal: "+codPostalStr);
					}
					errors.addParameterError(ParameterNames.COD_POSTAL, ErroresNames.ERROR_COD_POSTAL_OBLIGATORIO);
				}


				Integer idPoblacion = null;
				if (!StringUtils.isBlank(idPoblacionStr)) {
					idPoblacion = Validator.validaPoblacion(idPoblacionStr);

					if(idPoblacion!=null) {
						usuario.setIdPoblacion(idPoblacion);				
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



				if (idTipoUsuario==TipoUsuario.USUARIO_PROVEEDOR) {

					if (!StringUtils.isBlank(cifStr)) {
						cifStr = cifStr.trim();
						if (Validator.validaDni(cifStr)) {
							usuario.setCif(cifStr);	
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Dato incorrecto cif: "+cifStr);
							}
							errors.addParameterError(ParameterNames.CIF, ErroresNames.ERROR_CIF_FORMATO_INCORRECTO);
						}
					} else {
						usuario.setCif(dniStr);
					}


					if (!StringUtils.isBlank(direccionWebStr)) {
						direccionWebStr = direccionWebStr.trim();
						if (Validator.validaDireccionWeb(direccionWebStr)) {
							usuario.setDireccionWeb(direccionWebStr);
						} else {					
							if (logger.isDebugEnabled()) {
								logger.debug("Dato incorrecto Direcci?n Web: "+direccionWebStr);
							}
							errors.addParameterError(ParameterNames.DIRECCION_WEB, ErroresNames.ERROR_DIRECCION_WEB_FORMATO_INCORRECTO);
						}
					}


					
					usuario.setDescripcion(descripcionStr);


					Boolean servicio24 = false;
					if (!StringUtils.isBlank(servicio24Str)) {
						if (Validator.validaBoolean(servicio24Str)) {
							servicio24 = Validator.validaBoolean(servicio24Str);							
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Dato incorrecto servicio24"+servicio24Str);
							}
							errors.addParameterError(ParameterNames.SERVICIO_24, ErroresNames.ERROR_SERVICIO_24_FORMATO_INCORRECTO);
						}
					}
					usuario.setServicio24(servicio24);


					if (idsEspecializacionesStr!=null ) {

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

				}					


				//creo el codigo aleatorio y lo meto en bbdd
				usuario.setCodigoRegistro(RandomStringUtils.randomAlphabetic(10).toUpperCase());

				String url = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()
				+request.getContextPath()+ControllerNames.USUARIO+"?"
				+ParameterNames.ACTION+"="+ActionNames.VALIDAR_EMAIL
				+"&"+ParameterNames.EMAIL+"="+usuario.getEmail()
				+"&"+ParameterNames.COD_REGISTRO+"="+usuario.getCodigoRegistro();



				//Acceder a la capa de negocio(si no hay errores)
				if(!errors.hasErrors()) {
					try {

						usuarioService.signUp(usuario, idsEspecializaciones, url);

						request.setAttribute(AttributeNames.USUARIO, usuario);

						// Dirigir a..
						targetView =ViewNames.USUARIO_LOGIN;
						forward = true;

						
					}catch (EmailPendienteValidacionException epve) {
						if (logger.isErrorEnabled()) {
							logger.error(epve.getMessage(), epve);
						}
						errors.addCommonError(ErroresNames.ERROR_EMAIL_SIN_VALIDAR);
						
					}catch (UserLowInTheSystemException ulise) {
						if (logger.isErrorEnabled()) {
							logger.error(ulise.getMessage(), ulise);
						}
						errors.addCommonError(ErroresNames.ERROR_USER_LOW_IN_SYSTEM);

					}catch (UserAlreadyExistsException uaee) {
						if (logger.isErrorEnabled()) {
							logger.error(uaee.getMessage(), uaee);
						}
						errors.addCommonError(ErroresNames.ERROR_USER_ALREADY_EXISTS);

					}catch (MailException me) {
						if (logger.isErrorEnabled()) {
							logger.error(me.getMessage(), me);
						}
						errors.addCommonError(ErroresNames.ERROR_EMAIL);

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

		}else if (ActionNames.VALIDAR_EMAIL.equalsIgnoreCase(action)) {

			//Direcci?n de la vista predefinida(en caso de error)
			targetView=ViewNames.USUARIO_LOGIN;

			// Recoger los datos que enviamos desde la jsp
			String codRegistroStr = request.getParameter(ParameterNames.COD_REGISTRO);
			String emailStr = request.getParameter(ParameterNames.EMAIL);


			//Validar y convertir datos
			if (StringUtils.isBlank(emailStr)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco email: "+emailStr);
				}
				errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_OBLIGATORIO);
			} else {
				emailStr=emailStr.trim();
				emailStr= emailStr.toUpperCase();

				if (!Validator.VALIDA_EMAIL.isValid(emailStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto email: "+emailStr);
					}
					errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_FORMATO_INCORRECTO);
				}
			}


			if (StringUtils.isBlank(codRegistroStr)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco codRegistro: "+codRegistroStr);
				}
				errors.addParameterError(ParameterNames.COD_REGISTRO, ErroresNames.ERROR_COD_REGISTRO_OBLIGATORIO);

			} else {
				codRegistroStr = Validator.validaString(codRegistroStr);
				if (codRegistroStr==null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto codRegistro: "+codRegistroStr);
					}
					errors.addParameterError(ParameterNames.COD_REGISTRO, ErroresNames.ERROR_COD_REGISTRO_FORMATO_INCORRECTO);
				}
			}


			if (logger.isTraceEnabled()) {
				logger.trace("Email: "+emailStr+", codRegistro: "+codRegistroStr);
				//logger.trace("keepAutenticated: "+keepAutenticatedStr);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					UsuarioDTO u = usuarioService.validaEmail(emailStr, codRegistroStr);

					if (logger.isInfoEnabled()) {
						logger.info("User "+emailStr+" validation sucessfully.");
					}

					SessionManager.set(request, AttributeNames.USUARIO, u);

					// Dirigir a...
					targetView = ControllerNames.USUARIO;
					forward = false;


				}catch (UserLowInTheSystemException ulise) {
					if (logger.isErrorEnabled()) {
						logger.error("UserLowInTheSystemException: "+ulise.getMessage(), ulise);
					}
					errors.addCommonError(ErroresNames.ERROR_USER_LOW_IN_SYSTEM);

				}catch (CodeInvalidException cie) {
					if (logger.isErrorEnabled()) {
						logger.error("CodeInvalidException: "+cie.getMessage(), cie);
					}
					errors.addCommonError(ErroresNames.ERROR_CODE_SINGUP_INVALID);

				}catch (UserNotFoundException unfe) {
					if (logger.isErrorEnabled()) {
						logger.error("UserNotFoundException: "+unfe.getMessage(), unfe);
					}
					errors.addCommonError(ErroresNames.ERROR_USER_NOT_FOUND);

				}catch (DataException de) {
					if (logger.isErrorEnabled()) {
						logger.error("DataException: "+de.getMessage(), de);
					}
					errors.addCommonError(ErroresNames.ERROR_DATA);

				}catch (ServiceException se) {
					if (logger.isErrorEnabled()) {
						logger.error("ServiceException: "+se.getMessage(), se);
					}
					errors.addCommonError(ErroresNames.ERROR_SERVICE);

				}catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error("Exception: "+e.getMessage(), e);
					}
					errors.addCommonError(ErroresNames.ERROR_E);
				}
			}

		} else if (ActionNames.REACTIVAR_CUENTA.equalsIgnoreCase(action)) {

			// Vista en caso de error
			targetView = ControllerNames.USUARIO;
			forward = false;


			// Recoger los datos que enviamos desde la jsp
			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);
			String idEstadoStr = request.getParameter(ParameterNames.ID_STATUS_CUENTA);
			String codeStr = request.getParameter(ParameterNames.COD_REGISTRO);

			// Validar y convertir los datos
			Long idUsuario = null;			
			if(!StringUtils.isBlank(idUsuarioStr)) {
				idUsuario = Validator.validaLong(idUsuarioStr);
			} 


			Integer idEstado = null;
			if(!StringUtils.isBlank(idEstadoStr)) {
				idEstado = Validator.validaStatusCuenta(idEstadoStr);
			}


			if (idUsuario==null || idEstado==null || codeStr==null) {
				if (logger.isDebugEnabled()) {
					if (idUsuario==null) {
						logger.debug("Datos incorrecto idUsuario: "+idUsuarioStr);
					} else if (idEstado==null) {
						logger.debug("Datos incorrecto idEstado: "+idEstadoStr);
					} else {
						logger.debug("Datos incorrecto codRegisto: "+codeStr);
					}
				}
				errors.addParameterError(ParameterNames.ID_USUARIO, ErroresNames.ERROR_UPDATE_STATUS_CUENTA_INVALIDO);
			}


			if (logger.isTraceEnabled()) {
				logger.trace("idUsuario: "+idUsuario+", idEstado: "+idEstado);
			}



			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					String url=null;


					usuarioService.updateStatus(idUsuario, idEstado, url);

					UsuarioCriteria uc = new UsuarioCriteria();
					uc.setIdUsuario(idUsuario);

					Results<UsuarioDTO> usuario = usuarioService.findByCriteria(uc, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, START_INDEX)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)));

					boolean codeOk = false;
					for (UsuarioDTO u : usuario.getData()) {

						if (u.getCodigoRegistro().equalsIgnoreCase(codeStr)) {
							codeOk = true;							
						}

						if (codeOk==true) {	
							SessionManager.set(request, AttributeNames.USUARIO, u);

							Set<Long> usuariosIds = usuarioService.findFavorito(u.getIdUsuario());

							SessionManager.set(request, AttributeNames.FAVORITOS, usuariosIds);

							// Dirigir a..
							targetView =ControllerNames.USUARIO;
							forward = false;
						}
					}



					if (logger.isInfoEnabled()) {
						logger.info("Usuario actualizado: "+idUsuario);
					}


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

		} else if (ActionNames.FORGOT.equalsIgnoreCase(action)) {

			// Vista en caso de error
			targetView = ViewNames.USUARIO_FORGOT_PASSWORD;
			forward = true;

			String emailStr = request.getParameter(ParameterNames.EMAIL);


			//Validar Datos
			if (StringUtils.isBlank(emailStr)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco email: "+emailStr);
				}
				errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_OBLIGATORIO);
			} else {
				emailStr=emailStr.trim();

				if (!Validator.VALIDA_EMAIL.isValid(emailStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto email: "+emailStr);
					}
					errors.addParameterError(ParameterNames.EMAIL, ErroresNames.ERROR_EMAIL_FORMATO_INCORRECTO);
				}
			}


			if (logger.isTraceEnabled()) {
				logger.trace("email: "+emailStr);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					UsuarioCriteria uc = new UsuarioCriteria();
					uc.setEmailExistente(emailStr);

					UsuarioDTO user = usuarioService.findByEmail(uc);
					String code = (RandomStringUtils.randomAlphabetic(10).toUpperCase());

					usuarioService.updateCode(user.getIdUsuario(), code);

					String url= request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()
					+request.getContextPath()+ControllerNames.USUARIO+"?"
					+ParameterNames.ACTION+"="+ActionNames.RESTAURAR_PASS
					+"&"+ParameterNames.ID_USUARIO+"="+user.getIdUsuario()
					+"&"+ParameterNames.COD_REGISTRO+"="+code;


					StringBuilder msgSb = new StringBuilder("<html><body> <h1> Recuperar Cuenta Reformatec!!</h1>")
							.append("<p>Recupera tu contrase?a en el siguiente enlace: <a href='"+url+"'>Recuperar Contrase?a</a></p></body></html>");

					String msg = msgSb.toString();

					mailService.sendHTML(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, MAIL), "Recupera tu contrase?a", msg, emailStr);


					if (logger.isInfoEnabled()) {
						logger.info("Correo Enviado a: "+emailStr);
					}

					//No es un error realmente, solo notifico que revise el correo
					errors.addCommonError(ErroresNames.NOTIFICA_CORREO);
					// Dirigir a..
					targetView =ViewNames.USUARIO_LOGIN;
					forward = true;


				}catch (UserNotFoundException unfe) {
					if (logger.isErrorEnabled()) {
						logger.error(unfe.getMessage(), unfe);
					}
					errors.addCommonError(ErroresNames.ERROR_USER_NOT_FOUND);


				}catch (MailException de) {
					if (logger.isErrorEnabled()) {
						logger.error(de.getMessage(), de);
					}
					errors.addCommonError(ErroresNames.ERROR_EMAIL);

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



		} else if(ActionNames.UPDATE_PASSWORD.equalsIgnoreCase(action)) {


			// Dirigir a...
			targetView = ControllerNames.USUARIO;
			forward = false;

			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);
			String passwordStr = request.getParameter(ParameterNames.PASSWORD);
			String password2Str = request.getParameter(ParameterNames.PASSWORD_2);
			String codeStr = request.getParameter(ParameterNames.COD_REGISTRO);

			//Validar y convertir datos
			Long idUsuario = null;
			if(!StringUtils.isBlank(idUsuarioStr)) {
				idUsuario = Validator.validaLong(idUsuarioStr);

				if(idUsuario==null) {

					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idUsuario: "+idUsuarioStr);
					}
					errors.addParameterError(ParameterNames.ID_USUARIO, ErroresNames.ERROR_ID_USUARIO_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idUsuario: "+idUsuarioStr);
				}
				errors.addParameterError(ParameterNames.ID_USUARIO, ErroresNames.ERROR_ID_USUARIO_OBLIGATORIO);
			}


			if (StringUtils.isBlank(passwordStr)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco password:");
				}
				errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
			} else {
				passwordStr=passwordStr.trim();
				if (!Validator.validaPassword(passwordStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto password:");
					}
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
				}			
			}



			if (StringUtils.isBlank(password2Str)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco password2");
				}
				errors.addParameterError(ParameterNames.PASSWORD_2, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
			} else {
				password2Str=password2Str.trim();

				if (!Validator.validaPassword(password2Str)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto password2");
					}
					errors.addParameterError(ParameterNames.PASSWORD_2, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
				}			
			}

			if (logger.isTraceEnabled()) {
				logger.trace("Validando Password iguales...");
			}

			if (passwordStr.equals(password2Str)) {
				passwordStr=passwordStr.trim();
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Las passwords no son iguales");
				}
				errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORDS_DIFERENTES);
			}



			if (StringUtils.isBlank(codeStr)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco code: "+codeStr);
				}
				errors.addParameterError(ParameterNames.COD_REGISTRO, ErroresNames.ERROR_COD_REGISTRO_OBLIGATORIO);

			} else {
				codeStr = Validator.validaString(codeStr);
				if (codeStr==null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto code: "+codeStr);
					}
					errors.addParameterError(ParameterNames.COD_REGISTRO, ErroresNames.ERROR_COD_REGISTRO_FORMATO_INCORRECTO);
				}
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {


					// Por no tener un buen findby id...
					UsuarioCriteria uc = new UsuarioCriteria();
					uc.setIdUsuario(idUsuario);
					Results<UsuarioDTO> usuario = new Results<UsuarioDTO>();

					usuario = usuarioService.findByCriteria(uc, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, START_INDEX)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)));


					for (UsuarioDTO u : usuario.getData()) {
						// Compruebo en bbdd el codigo para confirmar que se le cambia la contrase?a al id del usuario correcto.
						UsuarioDTO user = usuarioService.validaEmail(u.getEmail(), codeStr);


						usuarioService.updatePassword(user.getIdUsuario(), passwordStr);

						// Si ya cambio la password, vuelvo a cambiar el codigo.
						String code = (RandomStringUtils.randomAlphabetic(10).toUpperCase());	
						usuarioService.updateCode(u.getIdUsuario(), code);

						SessionManager.set(request, AttributeNames.USUARIO, u);
					}



					if (logger.isInfoEnabled()) {
						logger.info("Usuario actualizado: "+idUsuario);
					}

					// Dirigir a...
					targetView = ViewNames.USUARIO_PERFIL;
					forward = true;



				}catch (UserNotFoundException unfe) {
					if (logger.isErrorEnabled()) {
						logger.error(unfe.getMessage(), unfe);
					}
					errors.addCommonError(ErroresNames.ERROR_USER_NOT_FOUND);

				}catch (CodeInvalidException cie) {
					if (logger.isErrorEnabled()) {
						logger.error("CodeInvalidException: "+cie.getMessage(), cie);
					}
					errors.addCommonError(ErroresNames.ERROR_CODE_SINGUP_INVALID);

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


		} else if (ActionNames.RESTAURAR_PASS.equalsIgnoreCase(action)) {


			// Dirigir a...
			targetView = ControllerNames.USUARIO;
			forward = false;

			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);
			String codeStr = request.getParameter(ParameterNames.COD_REGISTRO);

			//Validar y convertir datos
			Long idUsuario = null;			
			if(!StringUtils.isBlank(idUsuarioStr)) {
				idUsuario = Validator.validaLong(idUsuarioStr);
			} 


			if (!StringUtils.isBlank(codeStr)) {
				Validator.validaString(codeStr);
			} 


			if (idUsuario==null || codeStr==null) {
				if (logger.isDebugEnabled()) {
					if (idUsuario==null) {
						logger.debug("Datos incorrecto idUsuario: "+idUsuarioStr);
					} else {
						logger.debug("Datos incorrecto codigo: "+codeStr);
					}
				}
				errors.addParameterError(ParameterNames.ID_USUARIO, ErroresNames.ERROR_RESTAURAR_PASS_INVALID);
			}

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {


					UsuarioCriteria uc = new UsuarioCriteria();
					uc.setIdUsuario(idUsuario);

					Results<UsuarioDTO> usuarios = usuarioService.findByCriteria(uc, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)));

					boolean codeOk = false;
					for (UsuarioDTO u : usuarios.getData()) {

						if (u.getCodigoRegistro().equalsIgnoreCase(codeStr)) {
							codeOk = true;							
						}
					}

					if (codeOk==true) {						
						// Dirigir a..
						targetView =ViewNames.RECUPERAR_PASSWORD;
						forward=true;

						request.setAttribute(AttributeNames.USUARIO, usuarios);
						request.setAttribute(AttributeNames.CODE, codeStr);

					} 

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



		} else if (ActionNames.CONTACT.equalsIgnoreCase(action)) {


			// Dirigir a...
			targetView = ControllerNames.USUARIO;
			forward = false;

			String nombreStr = request.getParameter(ParameterNames.NOMBRE);
			String emailStr = request.getParameter(ParameterNames.EMAIL);
			String mensajeStr = request.getParameter(ParameterNames.MENSAJE);
			String asuntoStr = request.getParameter(ParameterNames.ASUNTO);

			//Validar y convertir



			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					String to = cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, MAIL);

					String mensajeoKStr = "Hola buenas, el usuario con email: "+emailStr+", y con nombre: "+nombreStr+" , le ha enviado el siguiente mensaje: "+mensajeStr;
					mailService.sendEmail(emailStr, asuntoStr, mensajeoKStr, to);
					logger.error(to);

					// Dirigir a...
					targetView = ControllerNames.USUARIO;
					forward = false;


				}catch (UserNotFoundException unfe) {
					if (logger.isErrorEnabled()) {
						logger.error(unfe.getMessage(), unfe);
					}
					errors.addCommonError(ErroresNames.ERROR_USER_NOT_FOUND);


				}catch (MailException de) {
					if (logger.isErrorEnabled()) {
						logger.error(de.getMessage(), de);
					}
					errors.addCommonError(ErroresNames.ERROR_EMAIL);

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



		}else{

			UsuarioCriteria criteria = new UsuarioCriteria();

			//TODO sacar a config/clase?
			criteria.setOrderBy("VAL");

			if (logger.isTraceEnabled()) {
				logger.trace("Busqueda usuarios con buena valoracion: "+criteria);
			}

			try {

				Results<UsuarioDTO> results= usuarioService.findByCriteria(criteria, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, START_INDEX)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_PROVEEDORES_TOP)));				


				request.setAttribute(AttributeNames.USUARIO, results);

				//Dirigir a..
				targetView= ViewNames.HOME;
				forward=true;

			} catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de);
				}
				errors.addCommonError(ErroresNames.ERROR_DATA);

			} catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se);
				}
				errors.addCommonError(ErroresNames.ERROR_SERVICE);
			}
		}
		// TODO Por qu? nunca pone el valor de targetview en este logger?
		if (logger.isInfoEnabled()) {
			logger.info(forward?"Forwarding to ":"Redirecting to ", targetView);
		}
		// Aqu? se aplica la direcci?n a la que dirigimos al usuario despues de cada acci?n
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