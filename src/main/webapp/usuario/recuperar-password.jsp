<%@include file="/common/header.jsp"%>
<!-- Recuperar Cuenta Start-->
			<div class="contact3_form_section">
				<div class="container">
					<div class="row">
						<div class="col">
								<!-- Contact Form -->
							<div class="contact3_form_container">
								<div class="contact_title text-center">Recupera Tu Cuenta</div>
								<%@include file="/common/common-errors.jsp"%>
								<%
								// lio de usuarios usuarios es la lista de usuarios resultados
								Results<UsuarioDTO> results = (Results<UsuarioDTO>) request.getAttribute(AttributeNames.USUARIO);
								List<UsuarioDTO> usuarios = results.getData();
								
								for (UsuarioDTO u : usuarios) {
								%>
								<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off" method="post" class="contact3_form text-center">								
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.PASSWORD);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="password" name="<%=ParameterNames.PASSWORD%>" class="contact3_form_subject input_field" required="required" placeholder="Introduce tu nueva contraseña" >
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.PASSWORD_2);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
				                  	<input type="hidden" name="<%=ParameterNames.COD_REGISTRO%>" value="<%=request.getAttribute(AttributeNames.CODE)%>">									
									<input type="password" name="<%=ParameterNames.PASSWORD_2%>" class="contact3_form_subject input_field" required="required" placeholder="Repite la contraseña">	
									<input type="hidden" name="<%=ParameterNames.ID_USUARIO%>" value="<%=u.getIdUsuario()%>">
									<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.UPDATE_PASSWORD%>">	
									<button type="submit" id="recuperar-cuenta" class="iniciosesion_button button trans_200">Recuperar Cuenta</button>
								</form>
								<%
									}
								%>
							</div>
						</div>
					</div>
				</div>
			</div>
<!-- Recuperar Cuenta End-->

<%@include file="/common/footer.jsp"%>