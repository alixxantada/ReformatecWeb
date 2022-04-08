<%@ page import="com.alejandro.reformatec.web.util.*,com.alejandro.reformatec.web.controller.*,java.util.*,java.text.*,com.alejandro.reformatec.model.*" %>
<%
 	Errors errors = (Errors) request.getAttribute(AttributeNames.ERRORS);
 	if (errors == null) {
 		errors = new Errors(); // Primera renderizacion
 	}
 
 	// Variable comun para los errors por parametro
 	String parameterError = null;
              	
 	List<String> commonErrors = errors.getCommonErrors();
 	if (commonErrors.size()>0) {
 %>
 	<div class="errores_formulario text-center">
 		<ul>
	 		<%
	 			for (String error: commonErrors) {
	 				%><li><%=error %></li>
	 			<%
	        	}
	        %>
        </ul>
    </div>
              
	<%
    }
	%>