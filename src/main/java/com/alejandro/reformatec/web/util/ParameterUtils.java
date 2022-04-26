package com.alejandro.reformatec.web.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

public class ParameterUtils {

	public static final String getValue(HttpServletRequest request, String parameterName) {
		return URLDecoder.decode(request.getParameter(parameterName));
	}


	public static final String getURL(String uri, Map<String, String[]> parameters) {
		StringBuilder sb = new StringBuilder(uri);
		if (parameters.size()>0) {
			sb.append("?");
		}
		String[] pvalues = null;
		for (String pname: parameters.keySet()) {
			pvalues = parameters.get(pname);
			for (String pvalue: pvalues) {
				sb.append(URLEncoder.encode(pname)).append("=").append(URLEncoder.encode(pvalue)).append("&");
			}
		}
		return sb.toString();
	}

	public static final String print(String parameterValue) {
		if (StringUtils.isEmpty(parameterValue)) {
			return Strings.EMPTY;
		} else {
			return parameterValue.trim();
		}
	}
	

	public static final String print(Long parameterValue) {
		if (parameterValue ==null) {
			return Strings.EMPTY;
		} else {
			return Long.toString(parameterValue);
		}
	}
	
	public static final String print(Double parameterValue) {
		if (parameterValue ==null) {
			return Strings.EMPTY;
		} else {
			return Double.toString(parameterValue);
		}
	}
	
	
}
