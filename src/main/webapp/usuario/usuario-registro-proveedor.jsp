<%@include file="/common/header.jsp"%>
<!-- Registro Proveedor Start -->
			<div class="contact2_form_section">
				<div class="container">
					<div class="row">
						<div class="col">
								<!-- Contact Form -->
							<div class="contact3_form_container">
								<div class="contact_title text-center">Registro Proveedor</div>
								<div class="todaviano">
									<span class="text-claro">¿Ya tienes cuenta? </span>
									<a href="<%=context%>/usuario/usuario-login.jsp" class="offers-link">Iniciar Sesión</a>
								</div>	
								<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off" method="post" id="contact2_form" class="contact2_form text-center">
								<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.SINGUP%>"/>
								<input type="hidden" name="<%=ParameterNames.TIPO_USUARIO_ID%>" value="<%=2%>"/>
									<input type="text" name="<%=ParameterNames.NOMBRE_PERFIL%>" id="usuario-nombreperfil" class="contact2_form_subject input_field" placeholder="Nombre de Perfil*" required="required" data-error="Necesitamos tu nombre de perfil..." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.NOMBRE_PERFIL))%>">
									<input type="email" name="<%=ParameterNames.EMAIL%>" id="usuario-email" class="contact2_form_subject input_field" placeholder="E-mail*" required="required" data-error="Necesitamos tu E-mail... " value="<%=ParameterUtils.print(request.getParameter(ParameterNames.EMAIL))%>">
									<input type="email" name="<%=ParameterNames.EMAIL%>" id="usuario-email2" class="contact2_form_subject input_field" placeholder="Confirma tu E-mail*" required="required" data-error="Necesitamos confirmar tu E-mail... " value="<%=ParameterUtils.print(request.getParameter(ParameterNames.EMAIL_2))%>">
									<input type="password" name="<%=ParameterNames.PASSWORD%>" id="usuario-contraseña" class="contact2_form_subject input_field" placeholder="Contraseña*" required="required" data-error="Necesitamos tu contraseña...">
									<input type="password" name="<%=ParameterNames.PASSWORD%>" id="usuario-contraseña2" class="contact2_form_subject input_field" placeholder="Repetir Contraseña*" required="required" data-error="Necesitamos confirmar tu contraseña...">
									<input type="text" name="<%=ParameterNames.DNI%>" id="usuario-cif" class="contact2_form_name2 input_field" placeholder="D.N.I*" required="required" data-error="Necesitamos tu DNI..." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.DNI))%>">
									<input type="text" name="<%=ParameterNames.NOMBRE%>" id="usuario-nombre" class="contact2_form_name2 input_field" placeholder="Nombre*" required="required" data-error="Necesitamos tu Nombre..." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.NOMBRE))%>">
									<input type="text" name="<%=ParameterNames.APELLIDO_1%>" id="usuario-apellido1" class="contact2_form_name input_field" placeholder="Primer Apellido*" required="required" data-error="Necesitamos tu primer apellido..." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.APELLIDO_1))%>">
									<input type="text" name="<%=ParameterNames.APELLIDO_2%>" id="usuario-apellido2" class="contact2_form_email input_field" placeholder="Segundo Apellido*" required="required" data-error="Necesitamos tu segundo apellido..." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.APELLIDO_2))%>">
									<input type="text" name="<%=ParameterNames.TELEFONO_1%>" id="usuario-telefono1" class="contact2_form_name input_field" placeholder="Teléfono 1*" required="required" data-error="Necesitamos tu teléfono 1..."  value="<%=ParameterUtils.print(request.getParameter(ParameterNames.TELEFONO_1))%>">
									<input type="text" name="<%=ParameterNames.TELEFONO_2%>" id="usuario-telefono2" class="contact2_form_email input_field" placeholder="Teléfono 2" data-error="Necesitamos tu teléfono 2..." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.TELEFONO_2))%>">
									<input type="text" name="<%=ParameterNames.DIRECCION%>" id="usuario-direccion" class="contact2_form_subject input_field" placeholder="Dirección*" required="required" data-error="Necesitamos tu Dirección.. " value="<%=ParameterUtils.print(request.getParameter(ParameterNames.DIRECCION))%>">
									<input type="text" name="<%=ParameterNames.COD_POSTAL%>" id="usuario-codigo-postal" class="contact2_form_name2 input_field"  required="required" placeholder="Código Postal*" data-error="Necesitamos tu Código Postal.. " value="<%=ParameterUtils.print(request.getParameter(ParameterNames.COD_POSTAL))%>">
									<select name="<%=ParameterNames.PROVINCIA_ID%>" id="provincia-usuario" class="dropdown2_item_select search2_input" required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.PROVINCIA_ID))%>">
										<option disabled selected>Selecciona una provincia</option>
										<option value="1">A Coruña</option>
										<option value="2">Lugo</option>
										<option value="3">Ourense</option>
										<option value="4">Pontevedra</option>
									</select>
									<select name="<%=ParameterNames.POBLACION_ID%>" id="poblacion-usuario" class="dropdown2_item_select search2_input" required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.POBLACION_ID))%>">
										<option disabled selected>Selecciona una población</option>
										<option value="1">Chantada</option>
										<option value="2">Escairón</option>
										<option value="3">Ribadavia</option>
										<option value="4">Carballiño</option>
										<option value="5">Nigrán</option>
										<option value="5">Baiona</option>
										<option value="5">Cedeira</option>
										<option value="5">Ferrol</option>
									</select>
									<input type="text" name="<%=ParameterNames.CIF%>" id="usuario-cif" class="contact2_form_name2 input_field" placeholder="C.I.F." value="<%=ParameterUtils.print(request.getParameter(ParameterNames.CIF))%>">
									<input type="text" name="<%=ParameterNames.DIRECCION_WEB%>" id="usuario-direccion-web" class="contact2_form_subject input_field" placeholder="Dirección Web" value="<%=ParameterUtils.print(request.getParameter(ParameterNames.DIRECCION_WEB))%>">
									<div class="extras">
										<ul class="search4_extras clearfix">
											<li class="search4_extras_item">
												<div class="clearfix">
													<input type="checkbox" name="<%=ParameterNames.SERVICIO_24%>" id="servicio24" class="search2_extras_cb" value="<%=ParameterUtils.print(request.getParameter(ParameterNames.SERVICIO_24))%>">
													<label for="servicio24">Servicio24H</label>
												</div>
											</li>
											<li class="search4_extras_item">
												<div class="clearfix">
													<input type="checkbox" name="<%=ParameterNames.EXPERTO_NEGOCIO%>" id="experto-negocio" class="search2_extras_cb" value="<%=ParameterUtils.print(request.getParameter(ParameterNames.EXPERTO_NEGOCIO))%>">
													<label for="experto-negocio">ExpertoNegocio</label>
												</div>
											</li>
										</ul>
									</div>
									<button type="submit" id="crear-usuario" class="form_submit_button button trans_200">Registrarse</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
<!-- Registro Proveedor End -->
<%@include file="/common/footer.jsp"%>