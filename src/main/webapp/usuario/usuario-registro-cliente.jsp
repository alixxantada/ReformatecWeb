<%@include file="/common/header.jsp"%>
<!-- Registro Cliente Start -->
			<div class="contact2_form_section">
				<div class="container">
					<div class="row">
						<div class="col">
								<!-- Contact Form -->
							<div class="contact3_form_container">
								<div class="contact_title text-center">Registro Cliente</div>
								<div class="todaviano">
									<span class="text-claro">¿Ya tienes cuenta? </span>
									<a href="<%=context+ViewNames.USUARIO_LOGIN%>" class="offers-link">Iniciar Sesión</a>
								</div> 
								<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off" method="post" id="registro-cliente" class="contact2_form text-center">
								<%@include file="/common/common-errors.jsp"%>
								<div class="formregistro1">								
									<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.SINGUP%>"/>
									<input type="hidden" name="<%=ParameterNames.ID_TIPO_USUARIO%>" value="<%=TipoUsuario.USUARIO_CLIENTE%>"/>
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.NOMBRE_PERFIL);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.NOMBRE_PERFIL%>" size="80" id="usuario-nombreperfil" class="contact2_form_subject input_field" 
									placeholder="Nombre de Perfil*" data-error="Necesitamos tu nombre de perfil..." required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.NOMBRE_PERFIL))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.EMAIL);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="email" name="<%=ParameterNames.EMAIL%>" id="usuario-email" autocomplete="off" size="256" class="contact2_form_subject input_field" 
									placeholder="E-mail*" data-error="Necesitamos tu E-mail... " required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.EMAIL))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.EMAIL_2);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="email" name="<%=ParameterNames.EMAIL_2%>" id="usuario-email-2" size="256"  class="contact2_form_subject input_field" 
									placeholder="Confirma tu E-mail*" data-error="Necesitamos confirmar tu E-mail... " required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.EMAIL_2))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.PASSWORD);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="password" name="<%=ParameterNames.PASSWORD%>" id="usuario-contraseña" size="20" class="contact2_form_subject input_field" 
									placeholder="Contraseña*" required="required" data-error="Necesitamos tu contraseña...">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.PASSWORD_2);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="password" name="<%=ParameterNames.PASSWORD_2%>" id="usuario-contraseña-2" size="20" class="contact2_form_subject input_field" 
									placeholder="Repetir Contraseña*" required="required" data-error="Necesitamos confirmar tu contraseña...">
									<button type="button" id="paso1" class="form_submit_button button trans_200">Continuar</button>
								</div>
								<div class="formregistro2">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.DNI);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.DNI%>" maxlength="10" size="10" id="usuario-cif" class="contact2_form_subject input_field" 
									placeholder="D.N.I*" data-error="Necesitamos tu DNI..." required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.DNI))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.NOMBRE);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.NOMBRE%>" size="80" id="usuario-nombre" class="contact2_form_subject input_field" 
									placeholder="Nombre*" data-error="Necesitamos tu Nombre..." required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.NOMBRE))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.APELLIDO_1);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.APELLIDO_1%>" size="80" id="usuario-apellido1" class="contact2_form_name input_field" 
									placeholder="Primer Apellido*" data-error="Necesitamos tu primer apellido..." required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.APELLIDO_1))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.APELLIDO_2);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.APELLIDO_2%>" size="80" id="usuario-apellido2" class="contact2_form_email input_field" 
									placeholder="Segundo Apellido*" data-error="Necesitamos tu segundo apellido..." required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.APELLIDO_2))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.TELEFONO_1);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.TELEFONO_1%>" size="40"id="usuario-telefono1" class="contact2_form_name input_field" 
									placeholder="Teléfono 1*" data-error="Necesitamos tu teléfono 1..." required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.TELEFONO_1))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.TELEFONO_2);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.TELEFONO_2%>" size="40" id="usuario-telefono2" class="contact2_form_email input_field" 
									placeholder="Teléfono 2" data-error="Necesitamos tu teléfono 2..." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.TELEFONO_2))%>">
				                   <%
			                  		parameterError = errors.getParameterError(ParameterNames.DIRECCION);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<input type="text" name="<%=ParameterNames.DIRECCION%>" size="80" id="usuario-direccion" class="contact2_form_subject input_field" 
									placeholder="Dirección*" data-error="Necesitamos tu Dirección.. " required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.DIRECCION))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.COD_POSTAL);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>				                  	
									<input type="text" name="<%=ParameterNames.COD_POSTAL%>" size="10" id="usuario-codigo-postal" class="contact2_form_name2 input_field"  
									placeholder="Código Postal*" data-error="Necesitamos tu Código Postal.. " required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.COD_POSTAL))%>">
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.ID_PROVINCIA);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<select name="<%=ParameterNames.ID_PROVINCIA%>" id="provincia-select" class="dropdown2_item_select search2_input"  
									required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.ID_PROVINCIA))%>"
									onchange="cargarPoblaciones()">									
									</select>
									<%
			                  		parameterError = errors.getParameterError(ParameterNames.ID_POBLACION);
			                  		if (parameterError!=null) {
				                  	%>
				                  		<p class="error_parametro"><i><%=parameterError%></i></p>
				                  	<%
				                  		}
				                  	%>
									<select name="<%=ParameterNames.ID_POBLACION%>" id="poblacion-select" class="dropdown2_item_select search2_input" 
									required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.ID_POBLACION))%>">
										<option disabled selected>Selecciona una población</option>
									</select>
									<button type="button" id="volverpaso1" class="form_submit_button button trans_200">Volver</button>									
									<button type="submit" id="crear-usuario" class="form_submit_button button trans_200">Registrarse</button>
								</div>
								</form>								
							</div>
						</div>
					</div>
				</div>
			</div>
			<script>
				$(document).ready(cargarProvincias());
				$(document).ready(cargarEspecializaciones());
				$(document).ready(initPaso1());
				$(document).ready(initVolverPaso1());
			</script>
<!-- Registro Cliente End -->
<%@include file="/common/footer.jsp"%>