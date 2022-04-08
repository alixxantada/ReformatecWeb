package com.alejandro.reformatec.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.InvalidUserOrPasswordException;
import com.alejandro.reformatec.exception.MailException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.exception.UserAlreadyExistsException;
import com.alejandro.reformatec.model.ProveedorCriteria;
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.model.TipoUsuario;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.model.ValoracionDTO;
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.ValoracionService;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
import com.alejandro.reformatec.service.impl.ValoracionServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.CookieManager;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.SessionManager;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;
import com.alejandro.reformatec.web.util.WebPagingUtils;


public class UsuarioServlet extends HttpServlet {

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
			String buscarStr = request.getParameter(ParameterNames.DESCRIPCION);
			String provinciaStr = request.getParameter(ParameterNames.PROVINCIA_ID);
			String especializacionStr = request.getParameter(ParameterNames.ESPECIALIZACION_ID);
			String expertoNegocioStr = request.getParameter(ParameterNames.EXPERTO_NEGOCIO);
			String servicio24Str = request.getParameter(ParameterNames.SERVICIO_24);
			String proveedorVerificadoStr = request.getParameter(ParameterNames.PROVEEDOR_VERIFICADO);
			String orderByStr = request.getParameter(ParameterNames.ORDER_BY);

	

			
			ProveedorCriteria pc = new ProveedorCriteria();
			
			//Convertir y validar datos
			if (!StringUtils.isBlank(buscarStr)) {
				// aquí hay mucho que hacer, ver txt
				buscarStr.trim();
				pc.setDescripcion(buscarStr);
			}

			Integer provincia = null;
			if (!StringUtils.isBlank(provinciaStr)) {
				provincia = Validator.validaInteger(provinciaStr);
			}
			pc.setIdProvincia(provincia);
			
			
			Integer especializacion = null;
			if (!StringUtils.isBlank(especializacionStr)) {
				especializacion = Validator.validaEspecializacion(especializacionStr);
			}
			pc.setIdEspecializacion(especializacion);
			
			
			Boolean expertoNegocio = null;
			if (!StringUtils.isBlank(expertoNegocioStr)) {
				expertoNegocio = Validator.validaBoolean(expertoNegocioStr);
			}
			pc.setExpertoNegocio(expertoNegocio);

			
			Boolean servicio24 = null;
			if (!StringUtils.isBlank(servicio24Str)) {
				servicio24 = Validator.validaBoolean(servicio24Str);
			}
			pc.setServicio24(servicio24);

			
			Boolean proveedorVerificado = null;
			if (!StringUtils.isBlank(proveedorVerificadoStr)) {
				proveedorVerificado = Validator.validaBoolean(proveedorVerificadoStr);
			}
			pc.setProveedorVerificado(proveedorVerificado);

			
			if (!StringUtils.isBlank(orderByStr)) {
				orderByStr = Validator.validaOrderByUsuario(orderByStr);
				pc.setOrderBy(orderByStr);
			}


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {
					Integer currentPage = WebPagingUtils.getCurrentPage(request);
					Results<UsuarioDTO> results = usuarioService.findByCriteria(pc, (currentPage-1)*PAGE_SIZE +1, PAGE_SIZE);
					
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




		} else if (ActionNames.DETAIL_USUARIO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.HOME;

			// Recoger los datos que enviamos desde la jsp
			String usuarioIdStr = request.getParameter(ParameterNames.USUARIO_ID);

			//Validar y convertir datos
			Long usuarioId = null;
			if  (StringUtils.isBlank(usuarioIdStr)) {
				logger.error("Dato null/blanco id Usuario: "+usuarioIdStr);
				errors.addParameterError(ParameterNames.USUARIO_ID, ErroresNames.ERROR_ID_USUARIO_OBLIGATORIO);				
			} else {
				usuarioId = Validator.validaLong(usuarioIdStr);
			}

			logger.trace("Id del usuario: "+usuarioId);

			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					UsuarioDTO u = usuarioService.findById(usuarioId);

					usuarioService.visualiza(usuarioId);

					List<ValoracionDTO> valoraciones = valoracionService.findByIdProveedorValorado(usuarioId);

					request.setAttribute(AttributeNames.VALORACION, valoraciones);

					request.setAttribute(AttributeNames.USUARIO, u);

					// Dirigir a...
					targetView =ViewNames.USUARIO_DETAIL;

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
				logger.debug("Dato null/blanco email: "+emailStr);
			} else {
				emailStr=emailStr.trim();
				
				if (!Validator.VALIDA_EMAIL.isValid(emailStr)) {
					logger.debug("Dato incorrecto email: "+emailStr);
				}
			}


			if (StringUtils.isBlank(passwordStr)) {
				logger.debug("Dato null/blanco password");
			} else {
				passwordStr=passwordStr.trim();
				
				if (!Validator.validaPassword(passwordStr)) {
					logger.debug("Dato incorrecto password");
				}
			}			

			Boolean keepAuthenticated = null;		
			if (!StringUtils.isBlank(keepAuthenticatedStr)) {
				if (Validator.validaBoolean(keepAuthenticatedStr)) {
					keepAuthenticated = Validator.validaBoolean(keepAuthenticatedStr);
				} else {
					logger.debug("Dato incorrecto keepAuthenticatedStr: "+keepAuthenticatedStr);
				}
			}
			
			logger.trace("Email: "+emailStr+", KeepAuthenticated: "+keepAuthenticated);

			//Acceder a la capa de negocio(si no hay errores)
			
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
			


		} else if (ActionNames.SINGUP.equalsIgnoreCase(action)){

			//Dirección de la vista predefinida(en caso de error)
			//TODO Estoy casi seguro que tal como se valida el primer dato SOBRA este targetview?
			targetView = ViewNames.USUARIO_REGISTRO;


			// Recoger los datos que enviamos desde la jsp
			String idTipoUsuarioStr = request.getParameter(ParameterNames.TIPO_USUARIO_ID);
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
			String poblacionStr = request.getParameter(ParameterNames.POBLACION_ID);
			String provinciaStr = request.getParameter(ParameterNames.PROVINCIA_ID);
			String cifStr = request.getParameter(ParameterNames.CIF);
			String direccionWebStr = request.getParameter(ParameterNames.DIRECCION_WEB);
			String servicio24Str = request.getParameter(ParameterNames.SERVICIO_24);
			String expertoNegocioStr = request.getParameter(ParameterNames.EXPERTO_NEGOCIO);			

			UsuarioDTO usuario = new UsuarioDTO();


			// Validar y Convertir Datos
			Integer idTipoUsuario = null;
			if (!StringUtils.isBlank(idTipoUsuarioStr)) {				
				idTipoUsuario = Validator.validaIdTipoUsuario(idTipoUsuarioStr);
				
				if (idTipoUsuario!=null) {
					usuario.setIdTipoUsuario(idTipoUsuario);					
				} else {
					logger.debug("Dato incorrecto idTipoUsuario"+idTipoUsuarioStr);
					errors.addParameterError(ParameterNames.TIPO_USUARIO_ID, ErroresNames.ERROR_ID_TIPO_USUARIO_FORMATO_INCORRECTO);
				}
				
			} else {				
				logger.debug("Dato obligatorio idTipoUsuario"+idTipoUsuarioStr);
				errors.addParameterError(ParameterNames.TIPO_USUARIO_ID, ErroresNames.ERROR_ID_TIPO_USUARIO_OBLIGATORIO);
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
					userNameStr = Validator.validaString(userNameStr);
					
					if (userNameStr!=null) {
						//TODO Lo seteo en el formato q sea o lo modifico antes de setear?
						usuario.setNombrePerfil(userNameStr);
					} else {
						logger.debug("Dato incorrecto Nombre de perfil"+userNameStr);
						errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_FORMATO_INCORRECTO);
					}
					
				} else {
					logger.debug("Dato null/blanco Nombre de perfil: "+userNameStr);
					errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_OBLIGATORIO);
				}

				
				//TODO Me hago una clase que valide 2 iguales?
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


				
				if (StringUtils.isBlank(email2Str)) {
					logger.debug("Dato null/blanco email2: "+email2Str);
					errors.addParameterError(ParameterNames.EMAIL_2, ErroresNames.ERROR_EMAIL_OBLIGATORIO);					
				} else {
					email2Str=email2Str.trim();
					
					if (!Validator.VALIDA_EMAIL.isValid(email2Str)) {
						logger.debug("Dato incorrecto email2 : "+email2Str);
						errors.addParameterError(ParameterNames.EMAIL_2, ErroresNames.ERROR_EMAIL_FORMATO_INCORRECTO);
					}				
				}

				

				logger.trace("Validando iguales... email: "+emailStr+", email 2: "+email2Str);
				if (emailStr.equals(email2Str)) {
					emailStr=emailStr.trim();
					usuario.setEmail(emailStr);					
				} else {
					logger.trace("El Email 1 "+emailStr+", y el Email 2 "+email2Str+", no son iguales");
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_EMAILS_DIFERENTES);
				}



				if (StringUtils.isBlank(passwordStr)) {
					logger.debug("Dato null/blanco password:");
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
				} else {
					passwordStr=passwordStr.trim();
					if (!Validator.validaPassword(passwordStr)) {
						logger.debug("Dato incorrecto password:");
						errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
					}			
				}

				

				if (StringUtils.isBlank(password2Str)) {
					logger.debug("Dato null/blanco password2");
					errors.addParameterError(ParameterNames.PASSWORD_2, ErroresNames.ERROR_PASSWORD_OBLIGATORIA);
				} else {
					password2Str=password2Str.trim();
					
					if (!Validator.validaPassword(password2Str)) {
						logger.debug("Dato incorrecto password2");
						errors.addParameterError(ParameterNames.PASSWORD_2, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
					}			
				}

				

				logger.trace("Validando Password iguales...");
				if (passwordStr.equals(password2Str)) {
					passwordStr=passwordStr.trim();
					usuario.setPassword(passwordStr);
				} else {
					logger.debug("Las passwords no son iguales");
					errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORDS_DIFERENTES);
				}


				
				if (!StringUtils.isBlank(dniStr)) {
					dniStr=dniStr.trim();
					
					if (Validator.validaDni(dniStr)) {
						usuario.setNif(dniStr);
					} else {
						logger.debug("Dato incorrecto dni: "+dniStr);
						errors.addParameterError(ParameterNames.DNI, ErroresNames.ERROR_DNI_FORMATO_INCORRECTO);
					}
					
				} else {
					logger.debug("Dato null/blanco dni: "+dniStr);
					errors.addParameterError(ParameterNames.DNI, ErroresNames.ERROR_DNI_OBLIGATORIO);			
				}

				
				
				if (!StringUtils.isBlank(nombreStr)) {
					nombreStr = Validator.validaString(nombreStr);
					
					if (nombreStr!=null) {
						usuario.setNombre(nombreStr);
					} else {
						logger.debug("Dato incorrecto Nombre: "+nombreStr);
						errors.addParameterError(ParameterNames.NOMBRE, ErroresNames.ERROR_NOMBRE_FORMATO_INCORRECTO);
					}
					
				} else {
					logger.debug("Dato null/blanco nombre: "+nombreStr);
					errors.addParameterError(ParameterNames.NOMBRE, ErroresNames.ERROR_NOMBRE_OBLIGATORIO);
				}

				
				
				
				if (!StringUtils.isBlank(primerApellidoStr)) {
					primerApellidoStr = Validator.validaString(primerApellidoStr);
					
					if (primerApellidoStr!=null) {
						usuario.setApellido1(primerApellidoStr);
					} else {
						logger.debug("Dato incorrecto primerApellido: "+primerApellidoStr);
						errors.addParameterError(ParameterNames.APELLIDO_1, ErroresNames.ERROR_APELLIDO_1_FORMATO_INCORRECTO);
					}
					
				} else {				
					logger.debug("Dato obligatorio primerApellido: "+primerApellidoStr);
					errors.addParameterError(ParameterNames.APELLIDO_1, ErroresNames.ERROR_APELLIDO_1_OBLIGATORIO);
				}

				
				
				
				if (!StringUtils.isBlank(segundoApellidoStr)) {
					segundoApellidoStr = Validator.validaString(segundoApellidoStr);
					
					if (primerApellidoStr!=null) {
						usuario.setApellido2(segundoApellidoStr);
					} else {
						logger.debug("Dato incorrecto segundoApellido: "+segundoApellidoStr);
						errors.addParameterError(ParameterNames.APELLIDO_2, ErroresNames.ERROR_APELLIDO_2_FORMATO_INCORRECTO);
					}
					
				} else {
					logger.debug("Dato null/blanco segundoApellido: "+segundoApellidoStr);
					errors.addParameterError(ParameterNames.APELLIDO_2, ErroresNames.ERROR_APELLIDO_2_OBLIGATORIO);
				}		

				
				

				if (!StringUtils.isBlank(telefono1Str)) {
					
					if (Validator.validaTelefono(telefono1Str)) {
						telefono1Str=telefono1Str.trim();
						usuario.setTelefono1(telefono1Str);
					} else {
						logger.debug("Dato incorrecto telefono1: "+telefono1Str);
						errors.addParameterError(ParameterNames.TELEFONO_1, ErroresNames.ERROR_TELEFONO_1_FORMATO_INCORRECTO);
					}
					
				} else {
					logger.debug("Dato null/blanco telefono1: "+telefono1Str);
					errors.addParameterError(ParameterNames.TELEFONO_1, ErroresNames.ERROR_TELEFONO_1_OBLIGATORIO);
				}			

				
				

				if (!StringUtils.isBlank(telefono2Str)) {
					telefono2Str = telefono2Str.trim();
					
					if (Validator.validaTelefono(telefono2Str)) {
						usuario.setTelefono2(telefono2Str);
					} else {
						logger.debug("Dato incorrecto telefono2: "+telefono2Str);
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
					logger.debug("Dato null/blanco direccion: "+direccionStr);
					errors.addParameterError(ParameterNames.DIRECCION, ErroresNames.ERROR_DIRECCION_OBLIGATORIO);
				}

				
				
				//!! Falta validar cod postal
				if (!StringUtils.isBlank(codPostalStr)) {
					codPostalStr=codPostalStr.trim();
					usuario.setCodigoPostal(codPostalStr);
				} else {
					logger.debug("Dato null/blanco codPostal: "+codPostalStr);
					errors.addParameterError(ParameterNames.COD_POSTAL, ErroresNames.ERROR_COD_POSTAL_OBLIGATORIO);
				}


				Integer poblacion = null;
				if (!StringUtils.isBlank(poblacionStr)) {
					poblacion = Validator.validaInteger(poblacionStr);
					usuario.setIdPoblacion(poblacion);
				} else {
					logger.debug("Dato null/blanco poblacion: "+poblacionStr);
					errors.addParameterError(ParameterNames.POBLACION_ID, ErroresNames.ERROR_POBLACION_OBLIGATORIA);
				}


				Integer provincia = null;
				if (!StringUtils.isBlank(provinciaStr)) {
					provincia = Validator.validaInteger(provinciaStr);
					usuario.setIdProvincia(provincia);
				} else {
					logger.debug("Dato null/blanco provincia: "+provinciaStr);
					errors.addParameterError(ParameterNames.PROVINCIA_ID, ErroresNames.ERROR_PROVINCIA_OBLIGATORIA);
				}

				if (idTipoUsuario==TipoUsuario.USUARIO_PROVEEDOR) {

					if (!StringUtils.isBlank(cifStr)) {
						cifStr = cifStr.trim();
						if (Validator.validaDni(cifStr)) {
							usuario.setCif(cifStr);	
						} else {
							logger.debug("Dato incorrecto cif: "+cifStr);
							errors.addParameterError(ParameterNames.CIF, ErroresNames.ERROR_CIF_FORMATO_INCORRECTO);
						}
					}


					if (!StringUtils.isBlank(direccionWebStr)) {
						direccionWebStr = direccionWebStr.trim();
						if (Validator.validaDireccionWeb(direccionWebStr)) { 
							usuario.setDireccionWeb(direccionWebStr);
						} else {					
							logger.debug("Dato incorrecto Dirección Web: "+direccionWebStr);
							errors.addParameterError(ParameterNames.DIRECCION_WEB, ErroresNames.ERROR_DIRECCION_WEB_FORMATO_INCORRECTO);
						}
					}


					Boolean servicio24 = null;
					if (!StringUtils.isBlank(servicio24Str)) {
						if (Validator.validaBoolean(servicio24Str)) {
							servicio24 = Validator.validaBoolean(servicio24Str);
							usuario.setServicio24(servicio24);
						} else {
							logger.debug("Dato incorrecto servicio24"+servicio24Str);
							errors.addParameterError(ParameterNames.SERVICIO_24, ErroresNames.ERROR_SERVICIO_24_FORMATO_INCORRECTO);
						}
					}


					Boolean expertoNegocio = null;		
					if (!StringUtils.isBlank(expertoNegocioStr)) {				
						if (Validator.validaBoolean(expertoNegocioStr)) {
							expertoNegocio = Validator.validaBoolean(expertoNegocioStr);
							usuario.setExpertoNegocio(expertoNegocio);
						} else {
							logger.debug("Dato incorrecto expertoNegocio"+expertoNegocioStr);
							errors.addParameterError(ParameterNames.EXPERTO_NEGOCIO, ErroresNames.ERROR_EXPERTO_NEGOCIO_FORMATO_INCORRECTO);
						}
					}
				}					

				logger.trace(usuario);

				//Acceder a la capa de negocio(si no hay errores)
				if(!errors.hasErrors()) {
					try {

						usuarioService.signUp(usuario);
						logger.info("Usuario Creado: "+usuario);

						request.setAttribute("", usuario);

						// Dirigir a..
						targetView =ViewNames.USUARIO_LOGIN;
						forward = false;

					}catch (UserAlreadyExistsException uaee) {
						logger.error(uaee.getMessage(), uaee);
						errors.addCommonError(ErroresNames.ERROR_USER_ALREADY_EXISTS);

					}catch (MailException me) {
						logger.error(me.getMessage(), me);
						errors.addCommonError(ErroresNames.ERROR_EMAIL);

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
		}else if (ActionNames.UPDATE_USUARIO.equalsIgnoreCase(action)){
			
			//Dirección de la vista predefinida(en caso de error)
			//TODO Estoy casi seguro que tal como se valida el primer dato SOBRA este targetview?
			targetView = ViewNames.USUARIO_REGISTRO;


			// Recoger los datos que enviamos desde la jsp
			String idTipoUsuarioStr = request.getParameter(ParameterNames.TIPO_USUARIO_ID);
			String userNameStr = request.getParameter(ParameterNames.NOMBRE_PERFIL);
			String telefono1Str = request.getParameter(ParameterNames.TELEFONO_1);
			String telefono2Str = request.getParameter(ParameterNames.TELEFONO_2);
			String direccionStr = request.getParameter(ParameterNames.DIRECCION);
			String codPostalStr = request.getParameter(ParameterNames.COD_POSTAL);
			String poblacionStr = request.getParameter(ParameterNames.POBLACION_ID);
			String provinciaStr = request.getParameter(ParameterNames.PROVINCIA_ID);
			String cifStr = request.getParameter(ParameterNames.CIF);
			String direccionWebStr = request.getParameter(ParameterNames.DIRECCION_WEB);
			String servicio24Str = request.getParameter(ParameterNames.SERVICIO_24);
			String expertoNegocioStr = request.getParameter(ParameterNames.EXPERTO_NEGOCIO);			

			UsuarioDTO usuario = new UsuarioDTO();
			
			// Validar y Convertir Datos
			Integer idTipoUsuario = null;
			if (!StringUtils.isBlank(idTipoUsuarioStr)) {				
				idTipoUsuario = Validator.validaIdTipoUsuario(idTipoUsuarioStr);
				
				if (idTipoUsuario!=null) {
					usuario.setIdTipoUsuario(idTipoUsuario);					
				} else {
					logger.debug("Dato incorrecto idTipoUsuario"+idTipoUsuarioStr);
					errors.addParameterError(ParameterNames.TIPO_USUARIO_ID, ErroresNames.ERROR_ID_TIPO_USUARIO_FORMATO_INCORRECTO);
				}
				
			} else {			
				logger.debug("Dato obligatorio idTipoUsuario"+idTipoUsuarioStr);
				errors.addParameterError(ParameterNames.TIPO_USUARIO_ID, ErroresNames.ERROR_ID_TIPO_USUARIO_OBLIGATORIO);
			}

			
			// Dirigimos la vista en caso de error en función del tipo de dato (idTipoUsuario)
			if (errors.hasErrors()) {
				targetView = ViewNames.HOME;
				forward = false;
			} else {				
				targetView = ViewNames.USUARIO_PERFIL_CLIENTE;	
				forward = false;
			}

				
			if (!StringUtils.isBlank(userNameStr)) {
				userNameStr = Validator.validaString(userNameStr);
				
				if (userNameStr!=null) {
					//TODO Lo seteo en el formato q sea o lo modifico antes de setear?
					usuario.setNombrePerfil(userNameStr);
				} else {
					logger.debug("Dato incorrecto Nombre de perfil"+userNameStr);
					errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_FORMATO_INCORRECTO);
				}
					
			} else {
				logger.debug("Dato null/blanco Nombre de perfil: "+userNameStr);
				errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_OBLIGATORIO);
			}				
			

			if (!StringUtils.isBlank(telefono1Str)) {
				
				if (Validator.validaTelefono(telefono1Str)) {
					telefono1Str=telefono1Str.trim();
					usuario.setTelefono1(telefono1Str);
				} else {
					logger.debug("Dato incorrecto telefono1: "+telefono1Str);
					errors.addParameterError(ParameterNames.TELEFONO_1, ErroresNames.ERROR_TELEFONO_1_FORMATO_INCORRECTO);
				}
					
			} else {
				logger.debug("Dato null/blanco telefono1: "+telefono1Str);
				errors.addParameterError(ParameterNames.TELEFONO_1, ErroresNames.ERROR_TELEFONO_1_OBLIGATORIO);
			}			

								

			if (!StringUtils.isBlank(telefono2Str)) {
				telefono2Str = telefono2Str.trim();
				
				if (Validator.validaTelefono(telefono2Str)) {
					usuario.setTelefono2(telefono2Str);
				} else {
					logger.debug("Dato incorrecto telefono2: "+telefono2Str);
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
				logger.debug("Dato null/blanco direccion: "+direccionStr);
				errors.addParameterError(ParameterNames.DIRECCION, ErroresNames.ERROR_DIRECCION_OBLIGATORIO);
			}

			
			
			//!! Falta validar cod postal
			if (!StringUtils.isBlank(codPostalStr)) {
				codPostalStr=codPostalStr.trim();
				usuario.setCodigoPostal(codPostalStr);
			} else {
				logger.debug("Dato null/blanco codPostal: "+codPostalStr);
				errors.addParameterError(ParameterNames.COD_POSTAL, ErroresNames.ERROR_COD_POSTAL_OBLIGATORIO);
			}


			Integer poblacion = null;
			if (!StringUtils.isBlank(poblacionStr)) {
				poblacion = Validator.validaInteger(poblacionStr);
				usuario.setIdPoblacion(poblacion);
			} else {
				logger.debug("Dato null/blanco poblacion: "+poblacionStr);
				errors.addParameterError(ParameterNames.POBLACION_ID, ErroresNames.ERROR_POBLACION_OBLIGATORIA);
			}


			Integer provincia = null;
			if (!StringUtils.isBlank(provinciaStr)) {
				provincia = Validator.validaInteger(provinciaStr);
				usuario.setIdProvincia(provincia);
			} else {
				logger.debug("Dato null/blanco provincia: "+provinciaStr);
				errors.addParameterError(ParameterNames.PROVINCIA_ID, ErroresNames.ERROR_PROVINCIA_OBLIGATORIA);
			}

			if (idTipoUsuario==TipoUsuario.USUARIO_PROVEEDOR) {

				
				if (!StringUtils.isBlank(direccionWebStr)) {
					direccionWebStr = direccionWebStr.trim();
					if (Validator.validaDireccionWeb(direccionWebStr)) { 
						usuario.setDireccionWeb(direccionWebStr);
					} else {					
						logger.debug("Dato incorrecto Dirección Web: "+direccionWebStr);
						errors.addParameterError(ParameterNames.DIRECCION_WEB, ErroresNames.ERROR_DIRECCION_WEB_FORMATO_INCORRECTO);
					}
				}


				Boolean servicio24 = null;
				if (!StringUtils.isBlank(servicio24Str)) {
					if (Validator.validaBoolean(servicio24Str)) {
						servicio24 = Validator.validaBoolean(servicio24Str);
						usuario.setServicio24(servicio24);
					} else {
						logger.debug("Dato incorrecto servicio24"+servicio24Str);
						errors.addParameterError(ParameterNames.SERVICIO_24, ErroresNames.ERROR_SERVICIO_24_FORMATO_INCORRECTO);
					}
				}


				Boolean expertoNegocio = null;		
				if (!StringUtils.isBlank(expertoNegocioStr)) {				
					if (Validator.validaBoolean(expertoNegocioStr)) {
						expertoNegocio = Validator.validaBoolean(expertoNegocioStr);
						usuario.setExpertoNegocio(expertoNegocio);
					} else {
						logger.debug("Dato incorrecto expertoNegocio"+expertoNegocioStr);
						errors.addParameterError(ParameterNames.EXPERTO_NEGOCIO, ErroresNames.ERROR_EXPERTO_NEGOCIO_FORMATO_INCORRECTO);
					}
				}
			}					

			logger.trace(usuario);
			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					usuarioService.update(usuario);
					logger.info("Usuario actualizado: "+usuario);

					request.setAttribute("", usuario);

					// Dirigir a..
					targetView =ViewNames.USUARIO_PERFIL_CLIENTE;
					forward = false;


				}catch (MailException me) {
					logger.error(me.getMessage(), me);
					errors.addCommonError(ErroresNames.ERROR_EMAIL);

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
			
		}else {
			//SACAR UN ERROR?
			targetView = ViewNames.HOME;
		}
		// TODO Por qué nunca pone el valor de targetview en este logger?
		logger.info(forward?"Forwarding to ":"Redirecting to ", targetView);
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