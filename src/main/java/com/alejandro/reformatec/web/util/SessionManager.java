package com.alejandro.reformatec.web.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {


	public static final void set(HttpServletRequest request, String name, Object value) {
		getSession(request).setAttribute(name, value);
	}
	
	
	public static final Object get(HttpServletRequest request, String name) {
		return getSession(request).getAttribute(name);
	}
	
	
	public static final void remove(HttpServletRequest request, String name) {
		getSession(request).removeAttribute(name);
	}

	private static final HttpSession getSession(HttpServletRequest request) {		
		HttpSession session = request.getSession(false);
			
		
		if (session==null) {
			// TODO (Pasar a un filtro)
			
			session = request.getSession(true);
			
			
			Locale locale = new Locale ("es", "ES");
			session.setAttribute(AttributeNames.LOCALE, locale);
						
			// Inicializo y guardo cualquier otro objecto que tenga que
			// estar desde el primer momento
			 //Carrito carrito = new Carrito();
			 //session.setAttribute(AttributeNames.CARRITO, carrito);
		}
		return session;
	}
}




	// vista de login
	// controlador de usuario: accion de login
	// 		guardar en sesion el usuario
	// 		forward/redirect a la home...
	// vista superior derehca: entrar / menu
