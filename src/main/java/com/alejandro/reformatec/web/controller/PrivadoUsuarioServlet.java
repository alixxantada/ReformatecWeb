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
import org.apache.logging.log4j.util.Strings;

import com.alejandro.reformatec.dao.util.ConfigurationManager;
import com.alejandro.reformatec.dao.util.ConstantConfigUtil;
import com.alejandro.reformatec.dao.util.PasswordEncryptionUtil;
import com.alejandro.reformatec.exception.DataException;
import com.alejandro.reformatec.exception.ServiceException;
import com.alejandro.reformatec.model.EstadoCuenta;
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.model.TipoUsuario;
import com.alejandro.reformatec.model.UsuarioCriteria;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
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



public class PrivadoUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(PrivadoUsuarioServlet.class);

	private UsuarioService usuarioService = null;

	private static final String CFGM_PFX = ConfigNames.PFX;
	private static final String PAGE_SIZE_DETAIL = CFGM_PFX + ConfigNames.PAGE_SIZE_DETAIL;
	private static final String PAGE_SIZE_SEARCH = CFGM_PFX +  ConfigNames.PAGE_SIZE_SEARCH;
	private static final String PAGE_COUNT = CFGM_PFX + ConfigNames.PAGE_COUNT;
	private ConfigurationManager cfgM = ConfigurationManager.getInstance();	


	public PrivadoUsuarioServlet() {
		super();
		usuarioService = new UsuarioServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//CommandManager.getInstance().doAction(request, response); **revisar commandManager**

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
			targetView = ControllerNames.USUARIO;
			forward = false;



		} else if (ActionNames.UPDATE_USUARIO.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;

			String servicio24Str=null;
			// Recoger los datos que enviamos desde la jsp
			String idTipoUsuarioStr = request.getParameter(ParameterNames.ID_TIPO_USUARIO);
			String userNameStr = request.getParameter(ParameterNames.NOMBRE_PERFIL);
			String telefono1Str = request.getParameter(ParameterNames.TELEFONO_1);
			String telefono2Str = request.getParameter(ParameterNames.TELEFONO_2);
			String direccionStr = request.getParameter(ParameterNames.DIRECCION);
			String codPostalStr = request.getParameter(ParameterNames.COD_POSTAL);
			String idPoblacionStr = request.getParameter(ParameterNames.ID_POBLACION);
			String direccionWebStr = request.getParameter(ParameterNames.DIRECCION_WEB);
			servicio24Str = request.getParameter(ParameterNames.SERVICIO_24);
			String cifStr = request.getParameter(ParameterNames.CIF);
			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);
			String proveedorVerificadoStr = request.getParameter(ParameterNames.PROVEEDOR_VERIFICADO);
			String passwordActualStr = request.getParameter(ParameterNames.PASSWORD_ACTUAL);
			String passwordStr = request.getParameter(ParameterNames.PASSWORD);
			String password2Str = request.getParameter(ParameterNames.PASSWORD_2);
			String [] idsEspecializacionesStr = request.getParameterValues(ParameterNames.ID_ESPECIALIZACION);
			String descripcionStr = request.getParameter(ParameterNames.DESCRIPCION_USUARIO);

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
				// Dirigir a...
				targetView = ControllerNames.USUARIO;
				forward = false;
			} else {				
				targetView = ViewNames.USUARIO_EDITAR_PERFIL;
				forward = true;
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



			String passwordOk = null;
			// Compruebo si me envia la password actual(Quiere cambiar la password)
			if (!StringUtils.isBlank(passwordActualStr)) {
				passwordActualStr=passwordActualStr.trim();

				if (!Validator.validaPassword(passwordActualStr)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto passwordActual");
					}
					errors.addParameterError(ParameterNames.PASSWORD_ACTUAL, ErroresNames.ERROR_PASSWORD_FORMATO_INCORRECTO);
				} else {


					UsuarioDTO user = (UsuarioDTO) SessionManager.get(request, AttributeNames.USUARIO);
					// Compruebo si la contraseña del usuario encriptada es la misma que tiene el usuario en bbdd encriptada.
					if (PasswordEncryptionUtil.checkPassword(passwordActualStr, user.getEncryptedPassword())) {


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
							passwordOk=passwordStr.trim();									
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("Las passwords no son iguales");
							}
							errors.addParameterError(ParameterNames.PASSWORD, ErroresNames.ERROR_PASSWORDS_DIFERENTES);
						}					

					} else {

						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto password:");
						}
						errors.addParameterError(ParameterNames.PASSWORD_ACTUAL, ErroresNames.ERROR_PASSWORD_ACTUAL_INCORRECTA);
					}
				}

			}
			usuario.setPassword(passwordOk);



			if (!StringUtils.isBlank(userNameStr)) {
				userNameStr = Validator.validaNombrePerfil(userNameStr);

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
					usuario.setCif(null);
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


				Boolean proveedorVerificado = false;
				if (!StringUtils.isBlank(proveedorVerificadoStr)) {
					if (Validator.validaBoolean(proveedorVerificadoStr)) {
						proveedorVerificado = Validator.validaBoolean(proveedorVerificadoStr);						
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Dato incorrecto proveedorVerificado"+proveedorVerificadoStr);
						}
						errors.addParameterError(ParameterNames.PROVEEDOR_VERIFICADO, ErroresNames.ERROR_PROVEEDOR_VERIFICADO_FORMATO_INCORRECTO);
					}
				}
				usuario.setProveedorVerificado(proveedorVerificado);


				if (idsEspecializacionesStr!=null) {

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



				//TODO como seria recomendable validar esto?
				usuario.setDescripcion(descripcionStr);


			}	


			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					usuarioService.update(usuario, idsEspecializaciones);

					// Busco al usuario actualizado para enviarlo actualizado
					UsuarioCriteria uc = new UsuarioCriteria();
					uc.setIdUsuario(usuario.getIdUsuario());					
					Results<UsuarioDTO> user = usuarioService.findByCriteria(uc, 1, 1);					
					for (UsuarioDTO u :user.getData()) {

						request.setAttribute(AttributeNames.USUARIO, u);
						SessionManager.set(request, AttributeNames.USUARIO, u);
					}

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


		} else if (ActionNames.UPDATE_STATUS_USUARIO.equalsIgnoreCase(action)) {

			// Dirigir a...
			targetView = ControllerNames.USUARIO;
			forward = false;


			// Recoger los datos que enviamos desde la jsp
			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);
			String idEstadoStr = request.getParameter(ParameterNames.ID_STATUS_CUENTA);


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

					String url=null;
					if(idEstado==EstadoCuenta.CUENTA_CANCELADA) {

						UsuarioCriteria uc = new UsuarioCriteria();
						uc.setIdUsuario(idUsuario);
						
						Results<UsuarioDTO> usuario = usuarioService.findByCriteria(uc,  Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_DETAIL)));


						for(UsuarioDTO u : usuario.getData()) {
							// Cambio el codigo por seguridad, se lo envio para que pueda reactivar SU cuenta
							String code = (RandomStringUtils.randomAlphabetic(10).toUpperCase());
							usuarioService.updateCode(u.getIdUsuario(), code);
							
							url = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort()
							+request.getContextPath()+ControllerNames.USUARIO+"?"
							+ParameterNames.ACTION+"="+ActionNames.REACTIVAR_CUENTA
							+"&"+ParameterNames.ID_USUARIO+"="+u.getIdUsuario()
							+"&"+ParameterNames.ID_STATUS_CUENTA+"="+EstadoCuenta.CUENTA_VALIDADA
							+"&"+ParameterNames.COD_REGISTRO+"="+code;	
						}

					}

					usuarioService.updateStatus(idUsuario, idEstado, url);

					CookieManager.setValue(response, AttributeNames.USUARIO, Strings.EMPTY, -1);
					SessionManager.set(request, AttributeNames.USUARIO, null);

					if (logger.isInfoEnabled()) {
						logger.info("Usuario actualizado: "+idUsuario);
					}

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

		} else if (ActionNames.MIS_FAVORITOS.equalsIgnoreCase(action)) {

			//Dirección de la vista predefinida(en caso de error)
			targetView = ControllerNames.USUARIO;
			forward = false;


			// Recoger los datos que enviamos desde la jsp
			String idUsuarioStr = request.getParameter(ParameterNames.ID_USUARIO);


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



			//Acceder a la capa de negocio(si no hay errores)
			if(!errors.hasErrors()) {
				try {

					Integer currentPage = WebPagingUtils.getCurrentPage(request);

					UsuarioCriteria uc = new UsuarioCriteria();
					uc.setIdUsuarioFavorito(idUsuario);

					Results<UsuarioDTO> results = usuarioService.findByCriteria(uc, (currentPage-1)*Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)) +1, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));

					request.setAttribute(AttributeNames.USUARIO, results);

					// Atributos para paginacion
					Integer totalPages = WebPagingUtils.getTotalPages(results.getTotal(), Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_SEARCH)));
					request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
					request.setAttribute(AttributeNames.CURRENT_PAGE, currentPage);
					request.setAttribute(AttributeNames.PAGING_FROM, WebPagingUtils.getPageFrom(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));
					request.setAttribute(AttributeNames.PAGING_TO, WebPagingUtils.getPageTo(currentPage, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_COUNT)), totalPages));


					// Dirigir a...
					targetView = ViewNames.USUARIO_MIS_PROVEEDORES;
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



		}  else {
			// Dirigir a...
			targetView = ControllerNames.USUARIO;
			forward = false;
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