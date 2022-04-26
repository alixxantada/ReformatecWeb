<%@include file="/common/header.jsp"%>
<!-- Forgot Password Start-->
			<div class="contact3_form_section">
				<div class="container">
					<div class="row">
						<div class="col">
								<!-- Contact Form -->
							<div class="contact3_form_container">
								<div class="contact_title text-center">Recupera Tu Cuenta</div>
								<%@include file="/common/common-errors.jsp"%>
								<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off" method="post" id="contact3_form" class="contact3_form text-center">
								<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.FORGOT%>"/>
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.EMAIL);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="email" name="<%=ParameterNames.EMAIL%>" id="contact3_form_subject" class="contact3_form_subject input_field" placeholder="Introduce tu E-mail" required="required" data-error="Necesitamos tu E-mail...">	
									<button type="submit" id="recuperar-cuenta" class="iniciosesion_button button trans_200">Recuperar Cuenta</button>
								</form>
								<div class="todaviano">
									<a href="<%=context+ViewNames.USUARIO_LOGIN%>" class="offers-link">Iniciar Sesión</a>
								</div>
								<div class="todaviano">
									<span class="text-claro">¿Todavía no tienes cuenta? </span>
									<a href="<%=context+ViewNames.USUARIO_REGISTRO%>" class="offers-link">Registrarse ahora</a>
								</div>	
							</div>
						</div>
					</div>
				</div>
			</div>
<!-- Forgot Password End-->
<%@include file="/common/footer.jsp"%>