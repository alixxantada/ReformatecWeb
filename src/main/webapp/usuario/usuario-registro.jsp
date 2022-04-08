<%@include file="/common/header.jsp"%>
			<div class="contact3_form_section">
				<div class="container">
					<div class="row">
						<div class="col">
								<!-- Contact Form -->
							<div class="contact3_form_container">
								<div class="contact_title text-center">Registro</div>
								<div class="todaviano3">
									<a href="<%=context+ViewNames.USUARIO_REGISTRO_CLIENTE%>" class="registrosesion_button button">Soy Cliente</a>
									<a href="<%=context+ViewNames.USUARIO_REGISTRO_PROVEEDOR%>" class="registrosesion_button button">Soy Proveedor</a>
								</div>
								<div class="todaviano">
									<span class="text-claro">¿Ya tienes cuenta? </span>
									<a href="<%=context+ViewNames.USUARIO_LOGIN%>" class="offers-link">Iniciar Sesión</a>
								</div>	
							</div>
						</div>
					</div>
				</div>
			</div>
<%@include file="/common/footer.jsp"%>