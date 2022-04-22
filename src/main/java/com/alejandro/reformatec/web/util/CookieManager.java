package com.alejandro.reformatec.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {

	public static final String getValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies!=null) {
			for (Cookie c: cookies) {
				if (c.getName().equalsIgnoreCase(name)) {
					return c.getValue();
				}
			}
		}
		return null;
	}
	
	public static final void setValue(HttpServletResponse response, String name, String value, int ttl) {
		Cookie cookie = new Cookie(name, value); 
		cookie.setMaxAge(ttl);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
