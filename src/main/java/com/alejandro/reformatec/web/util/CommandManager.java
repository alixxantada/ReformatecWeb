package com.alejandro.reformatec.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandManager {

	private static Logger logger = LogManager.getLogger(CommandManager.class);
	
	private Map<String, Action> actionsByName = null;

	//private List<Action> actions = null;


	private static CommandManager instance = null;


	public static CommandManager getInstance() {
		if (instance==null) {
			instance = new CommandManager();
		}
		return instance;
	}


	
	
	
	private CommandManager() {
		actionsByName = new HashMap<String, Action>();

		// En una app web no tiene mucho sentido, ... por tamaño, por undoable ...
		// Las registro para explicar como se haria en caso de que hiciera falta
		//actions = new ArrayList<Action>();
	}

	
	
	
	public void doAction(HttpServletRequest request, HttpServletResponse response) {

		//Primero recoge la accion
		String actionName = request.getParameter(ParameterNames.ACTION);

		// Buscamos la accion en el mapa por si alguna vez se ejecutó esa accion antes ya que la creamos sino es así
		Action action = actionsByName.get(actionName);

		//Si no existe esa accion(primera vez en ejecutar el command manager)
		if (action==null) {
			try {
				//compone el nombre de la clase con el metodo de abajo(seguir mismo patrón de nombrado de las clases para que funcione ok)
				String actionClassName = getActionClassName(actionName);
				// TODO a la accion aqui que se le hace???
				action = (Action) Class.forName(actionClassName).getConstructor().newInstance();
				// crea la accion en el mapa
				actionsByName.put(actionName, action);
			} catch (Exception e) {
				//  hay que meter un common errors entiendo
				logger.error(e);
			}			
		}
		// Registro el comando(en caso de que hiciera falta)
		//actions.add(action);

		// Ejecuta el comando
		action.doAction(request, response);


	}

// Recoge el nombre de la acción y crea la ruta de la clase java teniendo todas las clases con la misma estructura de nombre...(DEBERIA ESPEDIFICAR SEARCHUSUARIO/SEARCHPROYECTO Y ASI YA QUE SE REPITEN EN LOS SERVLETS)
	private static final String getActionClassName(String actionName) {
		StringBuilder sb = new StringBuilder(CommandManager.class.getPackageName());
		sb.append(".");
		sb.append(actionName.substring(0,1).toUpperCase());
		sb.append(actionName.substring(1));
		sb.append("Action");
		logger.info("aquiiiiiiiii"+sb);
		return sb.toString();

	}

	/**
	 * For test purposes only.
	 */
	public static final void main(String args[]) {
		try {
			Action action = null;
			try {
				action = (Action) Class.forName(getActionClassName("login")).getConstructor().newInstance();
			} catch (Exception e) {			
				logger.error(e);
			}			

			System.out.println(action.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}