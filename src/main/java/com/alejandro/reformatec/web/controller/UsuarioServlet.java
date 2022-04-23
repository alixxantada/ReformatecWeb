package com.alejandro.reformatec.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.ValoracionService;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
import com.alejandro.reformatec.service.impl.ValoracionServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
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

	// TODO sacar de configuracion
	private static int PAGE_SIZE = 3; 
	private static int PAGE_COUNT = 5;


	private UsuarioService usuarioService = null;
	private ValoracionService valoracionService = null;


	public UsuarioServlet() {
		super();
		usuarioService = new UsuarioServiceImpl();
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

		// Recogemos primero la accion
		String action = request.getParameter(ParameterNames.ACTION);


		if (ActionNames.SEARCH_USUARIO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView=ViewNames.HOME;

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
				// aquí hay mucho que hacer, ver txt
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
					Results<UsuarioDTO> results = usuarioService.findByCriteria(uc, (currentPage-1)*PAGE_SIZE +1, PAGE_SIZE);

					request.setAttribute(AttributeNames.USUARIO, results);


					// Atributos para paginacion
					Integer totalPages = WebPagingUtils.getTotalPages(results.getTotal(), PAGE_SIZE);
					request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
					request.setAttribute(AttributeNames.CURRENT_PAGE, currentPage);					
					request.setAttribute(AttributeNames.PAGING_FROM, WebPagingUtils.getPageFrom(currentPage, PAGE_COUNT, totalPages));
					request.setAttribute(AttributeNames.PAGING_TO, WebPagingUtils.getPageTo(currentPage, PAGE_COUNT, totalPages));


					// Dirigir a...
					targetView =ViewNames.USUARIO_RESULTS;

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

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.HOME;

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

					Results<UsuarioDTO> usuario = usuarioService.findByCriteria(uc, 1, 1);
					request.setAttribute(AttributeNames.USUARIO, usuario);


					usuarioService.visualizaUsuario(idUsuario);


					Integer currentPage = WebPagingUtils.getCurrentPage(request);

					Results<ValoracionDTO> valoraciones = valoracionService.findByCriteria(vc, (currentPage-1)*PAGE_SIZE +1, PAGE_SIZE);

					request.setAttribute(AttributeNames.VALORACION, valoraciones);


					// Dirigir a...
					targetView =ViewNames.USUARIO_DETAIL;

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

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.USUARIO_LOGIN;
			//targetView = ViewNames.USUARIO_LOGIN;

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
				targetView = ViewNames.HOME;

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

			//Dirección de la vista predefinida(en caso de error)
			//TODO Estoy casi seguro que tal como se valida el primer dato SOBRA este targetview?
			targetView = ViewNames.USUARIO_REGISTRO;


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


			// Dirigimos la vista en caso de error en función del tipo de dato (idTipoUsuario)
			if (errors.hasErrors()) {
				targetView = ViewNames.HOME;
				forward = false;
			} else {

				if (idTipoUsuario==TipoUsuario.USUARIO_PROVEEDOR) {
					targetView = ViewNames.USUARIO_REGISTRO_PROVEEDOR;					
				} else if (idTipoUsuario==TipoUsuario.USUARIO_CLIENTE){
					targetView = ViewNames.USUARIO_REGISTRO_CLIENTE;					
				} else {
					targetView = ViewNames.HOME;
					//Si forward es false hace redirect
					forward = false;
				}



				if (!StringUtils.isBlank(userNameStr)) {
					userNameStr = Validator.validaNombreOApellido(userNameStr);

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
					nombreStr = Validator.validaNombreOApellido(nombreStr);

					if (nombreStr!=null) {
						usuario.setNombre(nombreStr);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto Nombre: "+nombreStr);
						}
						errors.addParameterError(ParameterNames.NOMBRE, ErroresNames.ERROR_NOMBRE_FORMATO_INCORRECTO);
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato null/blanco nombre: "+nombreStr);
					}
					errors.addParameterError(ParameterNames.NOMBRE, ErroresNames.ERROR_NOMBRE_OBLIGATORIO);
				}




				if (!StringUtils.isBlank(primerApellidoStr)) {
					primerApellidoStr = Validator.validaNombreOApellido(primerApellidoStr);

					if (primerApellidoStr!=null) {
						usuario.setApellido1(primerApellidoStr);
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto primerApellido: "+primerApellidoStr);
						}
						errors.addParameterError(ParameterNames.APELLIDO_1, ErroresNames.ERROR_APELLIDO_1_FORMATO_INCORRECTO);
					}

				} else {				
					if (logger.isDebugEnabled()) {
						logger.debug("Dato obligatorio primerApellido: "+primerApellidoStr);
					}
					errors.addParameterError(ParameterNames.APELLIDO_1, ErroresNames.ERROR_APELLIDO_1_OBLIGATORIO);
				}




				if (!StringUtils.isBlank(segundoApellidoStr)) {
					segundoApellidoStr = Validator.validaNombreOApellido(segundoApellidoStr);

					if (primerApellidoStr!=null) {
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



				//TODO realmente como valido callejero/portal piso? :S un REGEX PATTERN? Sin tener los parametros separados...
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
					}


					if (!StringUtils.isBlank(direccionWebStr)) {
						direccionWebStr = direccionWebStr.trim();
						if (Validator.validaDireccionWeb(direccionWebStr)) { 
							usuario.setDireccionWeb(direccionWebStr);
						} else {					
							if (logger.isDebugEnabled()) {
								logger.debug("Dato incorrecto Dirección Web: "+direccionWebStr);
							}
							errors.addParameterError(ParameterNames.DIRECCION_WEB, ErroresNames.ERROR_DIRECCION_WEB_FORMATO_INCORRECTO);
						}
					}


					Boolean servicio24 = null;
					if (!StringUtils.isBlank(servicio24Str)) {
						if (Validator.validaBoolean(servicio24Str)) {
							servicio24 = Validator.validaBoolean(servicio24Str);
							usuario.setServicio24(servicio24);
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Dato incorrecto servicio24"+servicio24Str);
							}
							errors.addParameterError(ParameterNames.SERVICIO_24, ErroresNames.ERROR_SERVICIO_24_FORMATO_INCORRECTO);
						}
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

				}					


				//creo el codigo aleatorio y lo meto en bbdd
				usuario.setCodigoRegistro(RandomStringUtils.randomAlphabetic(10).toUpperCase());

				String url = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()
				+request.getContextPath()+ControllerNames.USUARIO+"?"
				+ParameterNames.ACTION+"="+ActionNames.VALIDAR_EMAIL
				+"&"+ParameterNames.EMAIL+"="+usuario.getEmail()
				+"&"+ParameterNames.COD_REGISTRO+"="+usuario.getCodigoRegistro();

				if (logger.isTraceEnabled()) {
					logger.trace("url: "+url);
					logger.trace("Usuario: "+usuario);
					logger.trace("Especializaciones: "+idsEspecializaciones);
				}

				//Acceder a la capa de negocio(si no hay errores)
				if(!errors.hasErrors()) {
					try {

						usuarioService.signUp(usuario, idsEspecializaciones, url);
						if (logger.isInfoEnabled()) {
							logger.info("Usuario Creado: "+usuario);
						}

						request.setAttribute("", usuario);

						// Dirigir a..
						targetView =ViewNames.USUARIO_LOGIN;
						forward = false;



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

			//Dirección de la vista predefinida(en caso de error)
			targetView=ViewNames.HOME;

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
					targetView = ViewNames.HOME;

					
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


		}else{
			//SACAR UN ERROR?
			targetView = ViewNames.HOME;
		}
		// TODO Por qué nunca pone el valor de targetview en este logger?
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}