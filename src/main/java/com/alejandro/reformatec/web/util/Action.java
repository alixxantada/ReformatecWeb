package com.alejandro.reformatec.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Action {

	private static Logger logger = LogManager.getLogger(Action.class);
	
	private String name = null;

	public Action(String name) {
		setName(name);
	}

	public final void doAction(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("Ejecutando "+getName()+"...");
			long t0 = System.currentTimeMillis();
			
			String targetView = doIt(request, response);
			
			long t1 = System.currentTimeMillis();
			logger.info(getName()+" ejecutada en "+ (t1-t0)+"ms! ");
			
			request.getRequestDispatcher(targetView).forward(request, response);

		} catch (Exception e) {
			// TODO
			logger.error("Error"+e);
		}
	}


	protected abstract String doIt(HttpServletRequest request, HttpServletResponse response);


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}