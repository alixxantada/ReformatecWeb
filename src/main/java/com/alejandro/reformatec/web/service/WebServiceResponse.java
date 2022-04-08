package com.alejandro.reformatec.web.service;


public class WebServiceResponse {
	
	private Object data = null;	
	private String errorCode = null;

	
	public WebServiceResponse() {
		
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
	
