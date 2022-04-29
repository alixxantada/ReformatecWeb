package com.alejandro.reformatec.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
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
import com.alejandro.reformatec.model.Results;
import com.alejandro.reformatec.model.UsuarioCriteria;
import com.alejandro.reformatec.model.UsuarioDTO;
import com.alejandro.reformatec.service.UsuarioService;
import com.alejandro.reformatec.service.impl.UsuarioServiceImpl;
import com.alejandro.reformatec.web.util.ActionNames;
import com.alejandro.reformatec.web.util.AttributeNames;
import com.alejandro.reformatec.web.util.ConfigNames;
import com.alejandro.reformatec.web.util.ControllerNames;
import com.alejandro.reformatec.web.util.ErroresNames;
import com.alejandro.reformatec.web.util.MimeTypeNames;
import com.alejandro.reformatec.web.util.ParameterNames;
import com.alejandro.reformatec.web.util.ParameterUtils;
import com.alejandro.reformatec.web.util.SessionManager;
import com.alejandro.reformatec.web.util.Validator;
import com.google.gson.Gson;


@WebServlet("/usuario-service")
public class UsuarioWebServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(UsuarioWebServiceServlet.class);

	private UsuarioService usuarioService = null;
	private Gson gson = null;


	private static final String CFGM_PFX = ConfigNames.PFX_WEB;
	private static final String PAGE_SIZE_WEB = CFGM_PFX +  ConfigNames.PAGE_SIZE_WEB;
	private static final String START_INDEX = CFGM_PFX + ConfigNames.START_INDEX_WEB;
	private ConfigurationManager cfgM = ConfigurationManager.getInstance();


	public UsuarioWebServiceServlet() {
		super();
		usuarioService = new UsuarioServiceImpl();
		gson = new Gson();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String actionStr = request.getParameter(ParameterNames.ACTION);

		WebServiceResponse wsResponse = new WebServiceResponse();



		if (ActionNames.SEARCH_USUARIO.equals(actionStr)) {

			String descripcionStr = request.getParameter(ParameterNames.BUSCAR_DESCRIPCION);

			UsuarioCriteria criteria = new UsuarioCriteria();

			//Validar datos
			if (!StringUtils.isBlank(descripcionStr)) {
				// aquí hay mucho que hacer, ver txt
				descripcionStr.trim();
				criteria.setDescripcion(descripcionStr);
			}


			if (logger.isTraceEnabled()) {
				logger.trace("Busqueda usuario: "+criteria);
			}

			try {

				Results<UsuarioDTO> results= usuarioService.findByCriteria(criteria, Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, START_INDEX)) , Integer.valueOf(cfgM.getParameter(ConstantConfigUtil.WEB_REFORMATEC_WEB_PROPERTIES, PAGE_SIZE_WEB)));				

				wsResponse.setData(results.getData());


			} catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de);
				}
				wsResponse.setErrorCode(ErroresNames.ERROR_DATA);

			} catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se);
				}
				wsResponse.setErrorCode(ErroresNames.ERROR_SERVICE);
			}

			String json = gson.toJson(wsResponse);

			// si no sale texto/html hay que indicar el tipo de contenido (MIMETYPE)
			response.setContentType(MimeTypeNames.JSON);
			response.setCharacterEncoding("ISO-8859-1");

			ServletOutputStream sos = response.getOutputStream();
			sos.write(json.getBytes());

			// se indica el final del json y que envie sus datos con flush
			sos.flush();
		} else if (ActionNames.ANHADIR_FAVORITO.equalsIgnoreCase(actionStr)) {


			// Recoger los datos que enviamos desde la jsp
			String idProveedorStr = request.getParameter(ParameterNames.ID_PROVEEDOR_FAVORITO);
			UsuarioDTO user = (UsuarioDTO) SessionManager.get(request, AttributeNames.USUARIO);
			Long idUsuario = user.getIdUsuario();


			//Validar y convertir datos
			Long idProveedor = null;
			if(!StringUtils.isBlank(idProveedorStr)) {
				idProveedor = Validator.validaLong(idProveedorStr);

				if(idProveedor==null) {					
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idProveedor: "+idProveedorStr);
					}					
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idUsuario: "+idProveedorStr);
				}
			}



			if(StringUtils.isBlank(idUsuario.toString())) {

				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idUsuario: "+idUsuario);
				}
			}



			//Acceder a la capa de negocio
			try {


				usuarioService.anhadirFavorito(idUsuario, idProveedor);

				Set<Long> usuariosIds = usuarioService.findFavorito(user.getIdUsuario());

				SessionManager.set(request, AttributeNames.FAVORITOS, usuariosIds);


				String json = gson.toJson("OK");

				// si no sale texto/html hay que indicar el tipo de contenido (MIMETYPE)
				response.setContentType(MimeTypeNames.JSON);
				response.setCharacterEncoding("ISO-8859-1");

				ServletOutputStream sos = response.getOutputStream();
				sos.write(json.getBytes());

				// se indica el final del json y que envie sus datos con flush
				sos.flush();

			}catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de.getMessage(), de);
				}

			}catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se.getMessage(), se);
				}

			}catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
			}


		} else if (ActionNames.ELIMINAR_FAVORITO.equalsIgnoreCase(actionStr)) {

			// Recoger los datos que enviamos desde la jsp
			String idProveedorStr = request.getParameter(ParameterNames.ID_PROVEEDOR_FAVORITO);
			UsuarioDTO user = (UsuarioDTO) SessionManager.get(request, AttributeNames.USUARIO);
			Long idUsuario = user.getIdUsuario();



			//Validar y convertir datos
			Long idProveedor = null;
			if(!StringUtils.isBlank(idProveedorStr)) {
				idProveedor = Validator.validaLong(idProveedorStr);

				if(idProveedor==null) {					
					if (logger.isDebugEnabled()) {
						logger.debug("Dato incorrecto idProveedor: "+idProveedorStr);
					}					
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idUsuario: "+idProveedorStr);
				}
			}



			if(StringUtils.isBlank(idUsuario.toString())) {

				if (logger.isDebugEnabled()) {
					logger.debug("Dato null/blanco idUsuario: "+idUsuario);
				}
			}



			//Acceder a la capa de negocio
			try {

				usuarioService.deleteFavorito(idUsuario, idProveedor);

				Set<Long> usuariosIds = usuarioService.findFavorito(user.getIdUsuario());

				SessionManager.set(request, AttributeNames.FAVORITOS, usuariosIds);


				String json = gson.toJson("OK");

				// si no sale texto/html hay que indicar el tipo de contenido (MIMETYPE)
				response.setContentType(MimeTypeNames.JSON);
				response.setCharacterEncoding("ISO-8859-1");

				ServletOutputStream sos = response.getOutputStream();
				sos.write(json.getBytes());

				// se indica el final del json y que envie sus datos con flush
				sos.flush();


			}catch (DataException de) {
				if (logger.isErrorEnabled()) {
					logger.error(de.getMessage(), de);
				}

			}catch (ServiceException se) {
				if (logger.isErrorEnabled()) {
					logger.error(se.getMessage(), se);
				}

			}catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
			}



		}
		// + acciones a continuacion...
	}    


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}