<%@include file="/common/header.jsp"%>
<!-- Iniciar Sesion Start-->
			<div class="contact3_form_section">
				<div class="container">
					<div class="row">
						<div class="col">
								<!-- Contact Form -->
							<div class="contact3_form_container">
								<div class="contact_title text-center">Iniciar Sesión</div>
								<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off"  method="post" class="contact3_form text-center">
									<%@include file="/common/common-errors.jsp"%>
									<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.LOGIN%>"/>
									<input name="<%=ParameterNames.EMAIL%>" type="email" id="contact3_form_subject" class="contact3_form_subject input_field" placeholder="E-mail" data-error="Necesitamos tu E-mail..." required="required" value=" <%=ParameterUtils.print(request.getParameter(ParameterNames.EMAIL))%>"/>
									<input name="<%=ParameterNames.PASSWORD%>" type="password" id="contact3_form_subject" class="contact3_form_subject input_field" placeholder="Contraseña" required="required" data-error="Necesitamos tu Contraseña... "></imput>
									<div class="extras">
										<ul class="search3_extras clearfix">
											<li class="search3_extras_item">
												<div class="clearfix">
													<input name="<%=ParameterNames.KEEP_AUTHENTICATED%>" value="true" type="checkbox" id="recuerdame" class="search3_extras_cb">
													<label for="recuerdame">Recuérdame</label>
												</div>
											</li>
										</ul>
									</div>
									<button type="submit" id="login" class="iniciosesion_button button trans_200">Iniciar Sesión</button>
								</form>
								<div class="todaviano2">
									<a href="<%=context+ViewNames.USUARIO_FORGOT_PASSWORD%>" class="offers-link">Olvidé mi contraseña</a>
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
<!-- Iniciar Sesion End-->
<%@include file="/common/footer.jsp"%>