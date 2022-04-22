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
import org.apache.logging.log4j.util.Strings;

import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.TipoUsuario;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.CookieManager;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.SessionManager;
import com.alejandro.reformatec.web.util.Validator;
import com.alejandro.reformatec.web.util.ViewNames;



public class PrivadoUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(PrivadoUsuarioServlet.class);

	private UsuarioService usuarioService = null;

	public PrivadoUsuarioServlet() {
		//TODO que significaba el super?
		super();
		usuarioService = new UsuarioServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//CommandManager.getInstance().doAction(request, response); **revisar commandManager**

		// Errors aqui no es null, estaria por ahora vacio
		Errors errors = new Errors();
		request.setAttribute(AttributeNames.ERRORS , errors);

		String targetView = null;

		boolean forward = true;

		// Recogemos primero siempre la accion
		String action = request.getParameter(ParameterNames.ACTION);

		if (logger.isInfoEnabled()) {
			logger.info("Processing action "+action);
		}

		if (ActionNames.LOGOUT.equalsIgnoreCase(action)) {
			CookieManager.setValue(response, AttributeNames.USUARIO, Strings.EMPTY, -1);
			SessionManager.set(request, AttributeNames.USUARIO, null);

			// Dirigir a...
			targetView = ViewNames.HOME;
			forward = false;

		} else if (ActionNames.UPDATE_USUARIO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			//TODO Estoy casi seguro que tal como se valida el primer dato SOBRA este targetview?
			targetView = ViewNames.USUARIO_REGISTRO;


			// Recoger los datos que enviamos desde la jsp
			String idTipoUsuarioStr = request.getParameter(ParameterNames.ID_TIPO_USUARIO);
			String userNameStr = request.getParameter(ParameterNames.NOMBRE_PERFIL);
			String telefono1Str = request.getParameter(ParameterNames.TELEFONO_1);
			String telefono2Str = request.getParameter(ParameterNames.TELEFONO_2);
			String direccionStr = request.getParameter(ParameterNames.DIRECCION);
			String codPostalStr = request.getParameter(ParameterNames.COD_POSTAL);
			String idPoblacionStr = request.getParameter(ParameterNames.ID_POBLACION);
			String direccionWebStr = request.getParameter(ParameterNames.DIRECCION_WEB);
			String servicio24Str = request.getParameter(ParameterNames.SERVICIO_24);		
			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);
			String [] idsEspecializacionesStr = request.getParameterValues(ParameterNames.ID_ESPECIALIZACION);
			
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


			// Dirigimos la vista predefinida en caso de error en función del tipo de dato (idTipoUsuario)
			if (errors.hasErrors()) {
				targetView = ViewNames.HOME;
				forward = false;
			} else {				
				targetView = ViewNames.USUARIO_PERFIL;	
				forward = false;
			}


			Long idUsuario = null;
			if(!StringUtils.isBlank(idUsuarioStr)) {
				idUsuario = Validator.validaLong(idUsuarioStr);

				if(idUsuario!=null) {
					usuario.setIdUsuario(idUsuario);
				} else {
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



			if (!StringUtils.isBlank(userNameStr)) {
				userNameStr = Validator.validaNombreOApellido(userNameStr);

				if (userNameStr!=null) {
					usuario.setNombrePerfil(userNameStr);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto Nombre de perfil: "+userNameStr);
					}
					errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_FORMATO_INCORRECTO);
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco Nombre de perfil: "+userNameStr);
				}
				errors.addParameterError(ParameterNames.NOMBRE_PERFIL, ErroresNames.ERROR_NOMBRE_PERFIL_OBLIGATORIO);
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
				logger.trace("Usuario: "+usuario);
				logger.trace("Especializaciones: "+idsEspecializaciones);
			}
			
			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					usuarioService.update(usuario, idsEspecializaciones);

					if (logger.isInfoEnabled()) {
						logger.info("Usuario actualizado: "+usuario);
					}

					request.setAttribute("", usuario);

					// Dirigir a..
					targetView =ViewNames.USUARIO_PERFIL;
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

		} else if (ActionNames.UPDATE_STATUS_USUARIO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ViewNames.HOME;


			// Recoger los datos que enviamos desde la jsp
			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);
			String idEstadoStr = request.getParameter(ParameterNames.ID_STATUS_CUENTA);


			// Validar y convertir los datos
			// Validar y convertir los datos
			Long idUsuario = null;			
			if(!StringUtils.isBlank(idUsuarioStr)) {
				idUsuario = Validator.validaLong(idUsuarioStr);
			} 


			Integer idEstado = null;
			if(!StringUtils.isBlank(idEstadoStr)) {
				idEstado = Validator.validaStatusCuenta(idEstadoStr);
			}


			if (idUsuario==null || idEstado==null) {
				if (logger.isDebugEnabled()) {
					if (idUsuario==null) {
						logger.debug("Datos incorrecto idUsuario: "+idUsuarioStr);
					} else {
						logger.debug("Datos incorrecto idEstado: "+idEstadoStr);
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

					usuarioService.updateStatus(idUsuario, idEstado);

					if (logger.isInfoEnabled()) {
						logger.info("Usuario actualizado: "+idUsuario);
					}

					// Dirigir a..
					targetView =ViewNames.HOME;
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}