<%@include file="/common/header.jsp"%>
<!-- Sección Formulario Contacto Start -->
			<div class="contact2_form_section">
				<div class="container">
					<div class="row">
						<div class="col">
								<!-- Contact Form -->
							<div class="contact2_form_container">
								<div class="contact_title text-center">Contacto</div>
								<%@include file="/common/common-errors.jsp"%>
								<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off" method="post" class="contact2_form text-center">
									<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.CONTACT%>"/>
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.NOMBRE);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.NOMBRE%>" id="contact2_form_name" class="contact2_form_name input_field" placeholder="Nombre" required="required" data-error="Necesitamos tu nombre...">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.EMAIL);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="email" name="<%=ParameterNames.EMAIL%>" id="contact2_form_email" class="contact2_form_email input_field" placeholder="E-mail" required="required" data-error="Necesitamos tu E-mail... ">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.ASUNTO);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.ASUNTO%>" id="contact2_form_subject" class="contact2_form_subject input_field" placeholder="Asunto" required="required" data-error="Necesitamos un asunto...">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.MENSAJE);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<textarea id="contact2_form_message" name="<%=ParameterNames.MENSAJE%>" class="text_field contact2_form_message" name="message" rows="4" placeholder="Mensage" required="required" data-error="Vaya.. ¿Qué nos querías comentar?"></textarea>
									<button type="submit" id="form_submit_button" class="form_submit_button button trans_200">Enviar Mensaje<span></span><span></span><span></span></button>
								</form>
							</div>

						</div>
					</div>
				</div>
			</div>
				<!-- Sección Formulario Contacto End -->
				<!-- Sección 2 Start -->
			<div class="about">
				<div class="container">
					<div class="row">
						<div class="col-lg-5">							
								<!-- About - Image -->
							<div class="about_image">
								<img src="<%=context%>/images/man.png" alt="">
							</div>
						</div>
						<div class="col-lg-4">							
								<!-- About - Content -->
							<div class="about_content">
								<div class="logo_container about_logo">
									<div class="logo2"><a href="<%=context%>/index.jsp"><img src="<%=context%>/images/logo/logoreformatec.png" alt="Logo Reformatec"></a></div>
								</div>
								<p class="about_text">Si ocurriese cualquier error, póngase en contacto con nosotros.</p>
								
							</div>
						</div>
						<div class="col-lg-3">							
								<!-- About Info -->
							<div class="about_info">
								<ul class="contact_info_list">
									<li class="contact_info_item d-flex flex-row">
										<div><div class="contact_info_icon"><img src="<%=context%>/images/placeholder.svg" alt=""></div></div>
										<div class="contact_info_text">Avda/ Monforte S/N 27500 Chantada (Lugo)</div>
									</li>
									<li class="contact_info_item d-flex flex-row">
										<div><div class="contact_info_icon"><img src="<%=context%>/images/phone-call.svg" alt=""></div></div>
										<div class="contact_info_text"><a href="tel:0034670884461">+34 670 884 461</a></div>
									</li>
									<li class="contact_info_item d-flex flex-row">
										<div><div class="contact_info_icon"><img src="<%=context%>/images/message.svg" alt=""></div></div>
										<div class="contact_info_text"><a href="mailto:pruebafp@gmail.com?Subject=Hello" target="_top">pruebafp@gmail.com</a></div>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
<!-- Sección 2 End -->
<%@include file="/common/footer.jsp"%>