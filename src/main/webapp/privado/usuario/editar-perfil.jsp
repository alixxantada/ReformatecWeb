<%@include file="/common/header.jsp"%>
<%@include file="/common/menu-usuario.jsp"%>

					<!-- Editar Perfil Start-->
				<div class="page-wrapper">                
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12">
								<div class="card">
									<div class="card-body">
										<div class="col-md-12">											
											<div class="form-group">
												<label for="hue-demo"><%=usuario.getNombrePerfil()%></label>
												<%@include file="/common/common-errors.jsp"%>
											</div>
											<div class="custom-file">
												<input type="file" class="custom-file-input" id="validatedCustomFile" required>
												<label class="custom-file-label" for="validatedCustomFile">Personalizar Foto Perfil</label>
												<div class="invalid-feedback">Archivo inválido</div>
											</div>
										</div>
										<form action="<%=context+ControllerNames.PRIVADO_USUARIO%>" autocomplete="off" method="post"  class="contact2_form">										
										<div class="col-sm-12">
											<div class="fotoperfil">
												<img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Perfil">												
											</div>																				
											<div class="descripcionUsuario">	
												<%
												if(usuario.getIdTipoUsuario()==TipoUsuario.USUARIO_PROVEEDOR){
												
												%>
												<div class="col-md-12">
													<div class="form-group">
														<%
								                  		parameterError = errors.getParameterError(ParameterNames.DESCRIPCION_USUARIO);
								                  		if (parameterError!=null) {
									                  	%>
									                  		<p class="error_parametro"><i><%=parameterError%></i></p>
									                  	<%
									                  		}
									                  	%>														
														<textarea class="descripcion-registro-proveedor" name="<%=ParameterNames.DESCRIPCION_USUARIO%>"
														 rows="10" cols="40"><%=ParameterUtils.print(usuario.getDescripcion())%></textarea>		
													</div>
												</div>
												<%
												}
												%>
											</div>
										</div>										
										<div class="col-sm-12">																																	
											<div class="form-group">													
												<label for="hue-demo">Dirección actual: </label>										
												<div id="poblacionActual">												
													<label for="position-top-right"> <%=ParameterUtils.print(usuario.getNombreCalle())%>, <%=ParameterUtils.print(usuario.getNombrePoblacion())%> <%=ParameterUtils.print(usuario.getCodigoPostal())%> ( <%=ParameterUtils.print(usuario.getNombreProvincia())%> )</label>
												<button type="button" class="btn btn-success" onclick="cargarProvinciasPerfil()">Cambiar Direccion</button>
												</div>
											</div>
												<%
						                  		parameterError = errors.getParameterError(ParameterNames.ID_PROVINCIA);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>
							                  	<%
						                  		parameterError = errors.getParameterError(ParameterNames.ID_POBLACION);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>
							                  	<%
						                  		parameterError = errors.getParameterError(ParameterNames.DIRECCION);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>
							                  	<%
						                  		parameterError = errors.getParameterError(ParameterNames.COD_POSTAL);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>
											<div class="form-group">													
												
												<select name="<%=ParameterNames.ID_PROVINCIA%>" id="provincia-select-perfil" class="dropdown2_item_select search2_input"  
												required onchange="cargarPoblacionesPerfil()">
												<option value="<%=usuario.getIdProvincia()%>" selected><%=usuario.getNombreProvincia()%></option>								
												</select>
											</div>
											<div class="form-group">
																									
												<select name="<%=ParameterNames.ID_POBLACION%>" id="poblacion-select-perfil" class="dropdown2_item_select search2_input" required>
												<option value="<%=usuario.getIdPoblacion()%>" selected><%=usuario.getNombrePoblacion()%></option>									
												</select>
											</div>	
											<div class="form-group">
												<label for="position-top-right" id="direccionEditarPerfilLabel">Direccion: </label>
														
												<input type="text" name="<%=ParameterNames.DIRECCION%>" id="direccionEditarPerfil" class="form-control demo" data-position="bottom left" placeholder="Introduce Calle, Numero, Piso" value="<%=ParameterUtils.print(usuario.getNombreCalle())%>" autocomplete="off">
											</div>
											<div class="form-group" >
												<label for="position-top-right" id="codPostalEditarPerfilLabel">Codigo Postal: </label>
												
												<input type="text" name="<%=ParameterNames.COD_POSTAL%>" id="codPostalEditarPerfil" class="form-control demo" data-position="top right" placeholder="Introduce Cod Postal" value="<%=ParameterUtils.print(usuario.getCodigoPostal())%>" autocomplete="off">
											</div>
											<div class="form-group">
												<label for="position-top-right">Teléfono 1</label>
												<%
						                  		parameterError = errors.getParameterError(ParameterNames.TELEFONO_1);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>	
												<input type="text" name="<%=ParameterNames.TELEFONO_1%>" id="telefono1" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getTelefono1())%>" autocomplete="off">
											</div>
											<div class="form-group">
												<%
						                  		parameterError = errors.getParameterError(ParameterNames.TELEFONO_2);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>	
												<label for="position-top-right">Teléfono 2</label>
												<input type="text" name="<%=ParameterNames.TELEFONO_2%>" id="telefono2" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getTelefono2())%>" autocomplete="off">
											</div>
											<%
											
											if (usuario.getIdTipoUsuario()==TipoUsuario.USUARIO_PROVEEDOR){
											
											%>
											<div class="form-group">
												<label for="position-top-right" >Cif </label>
												<%
						                  		parameterError = errors.getParameterError(ParameterNames.CIF);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>	
												<input type="text" name="<%=ParameterNames.CIF%>" class="form-control demo" data-position="top right" placeholder="Puedes indicar el CIF de tu empresa" value="<%=ParameterUtils.print(usuario.getCif())%>" autocomplete="off">
											</div>
											<div class="form-group">
												<label for="position-top-right" >Dirección Web </label>
												<%
						                  		parameterError = errors.getParameterError(ParameterNames.DIRECCION_WEB);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>
												<input type="text" name="<%=ParameterNames.DIRECCION_WEB%>" class="form-control demo" data-position="top right" placeholder="Puedes indicar la direccion WEB de tu empresa" value="<%=ParameterUtils.print(usuario.getDireccionWeb())%>" autocomplete="off">
											</div>
											<div class="clearfix" id="cajaCheckPerfil">
												<%
						                  		parameterError = errors.getParameterError(ParameterNames.SERVICIO_24);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>														
												<input <% if (usuario.getServicio24()==true) { %> checked <% } %> type="checkbox" name="<%=ParameterNames.SERVICIO_24%>" value="true" id="checkServicioPerfil" class="search2_extras_cb">
												<label id="servicio24Perfil" for="servicio24">Servicio24H</label>									
											</div>
											
												<%												
						                  		parameterError = errors.getParameterError(ParameterNames.ID_ESPECIALIZACION);
						                  		if (parameterError!=null) {
							                  	%>
							                  		<p class="error_parametro"><i><%=parameterError%></i></p>
							                  	<%
							                  		}
							                  	%>
							                  	<div class="especializaciones-edit">
													<div class="especializacion-proveedor" id="especializaciones-proveedor">
													
														<div>
					                                        <select class="select-registro-especializacion" id="especializacion-select" multiple>
																
					                                        </select>
					                                    </div>
					                                    <div>
					                                        <select class="select-registro-especializacion-seleccionadas" id="especializacionesSeleccionadas" name="<%=ParameterNames.ID_ESPECIALIZACION%>" multiple>
					                                        </select>	
					                                    </div> 
													</div>
												</div>
											<%
											}
											
					                  		parameterError = errors.getParameterError(ParameterNames.PASSWORD_ACTUAL);
					                  		if (parameterError!=null) {
						                  	%>
						                  		<p class="error_parametro"><i><%=parameterError%></i></p>
						                  	<%
						                  		}
						                  
					                  		parameterError = errors.getParameterError(ParameterNames.PASSWORD);
					                  		if (parameterError!=null) {
						                  	%>
						                  		<p class="error_parametro"><i><%=parameterError%></i></p>
						                  	<%
						                  		}
						                  
					                  		parameterError = errors.getParameterError(ParameterNames.PASSWORD_2);
					                  		if (parameterError!=null) {
						                  	%>
						                  		<p class="error_parametro"><i><%=parameterError%></i></p>
						                  	<%
						                  		}
						                  	%>
											<div class="cambi-pass">
												<button type="button" class="btn btn-success" id="cambioDePass" onclick="cargarCambioPass()">Cambiar Contraseña</button>
											</div>
											<div id="caja-pass">
												<div class="form-group">
													<label for="position-top-right">Actual Contraseña</label>													
													<input type="password" name="<%=ParameterNames.PASSWORD_ACTUAL%>" id="passoriginal" class="form-control demo" data-position="top right" autocomplete="off">
												</div>											
												<div class="form-group">
													<label for="position-top-right">Nueva Contraseña</label>													
													<input type="password" name="<%=ParameterNames.PASSWORD%>" id="pass" class="form-control demo" data-position="top right" autocomplete="off">
												</div>
												<div class="form-group">
													<label for="position-top-right">Repetir Nueva Contraseña</label>													
													<input type="password" name="<%=ParameterNames.PASSWORD_2%>" id="pass2" class="form-control demo" data-position="top right" autocomplete="off">
												</div>
											</div>
											<input type="hidden" name="<%=ParameterNames.PROVEEDOR_VERIFICADO%>" value="<%=usuario.getProveedorVerificado()%>"/>
											<input type="hidden" name="<%=ParameterNames.ID_USUARIO%>" value="<%=usuario.getIdUsuario()%>"/>
											<input type="hidden" name="<%=ParameterNames.DNI%>" value="<%=usuario.getNif()%>"/>
											<input type="hidden" name="<%=ParameterNames.ID_TIPO_USUARIO%>" value="<%=usuario.getIdTipoUsuario()%>"/>
											<input type="hidden" name="<%=ParameterNames.NOMBRE_PERFIL%>" value="<%=usuario.getNombrePerfil()%>"/>
											<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.UPDATE_USUARIO%>"/>
											<div class="botones-editar">
												<button type="submit" class="btn btn-success">Guardar</button>
												<button type="button" class="btn btn-success" id="cancelar-edit" ><a href="<%=context+ViewNames.USUARIO_PERFIL%>">Cancelar</a></button>
												<div class="caja-eliminar-usuario">
													<button type="button" id="eliminar-usuario" class="btn btn-danger">Eliminar Cuenta</a></button>
												</div>
												<div class="eliminar-usuario-definitivo" id="eliminar-usuario-def">
													<p>Esta seguro de que desea eliminar la cuenta?</p>
													<button type="button" class="btn btn-danger"><a href="<%=context+ControllerNames.PRIVADO_USUARIO%>?<%=ParameterNames.ACTION%>=
													<%=ActionNames.UPDATE_STATUS_USUARIO%>&<%=ParameterNames.ID_USUARIO%>=<%=usuario.getIdUsuario()%>&<%=ParameterNames.ID_STATUS_CUENTA%>=3"> Eliminar Cuenta</a></button>
												</div>									
											</div>
											</form>
										</div>											
										<div class="border-top">																				
										</div>
									</div>
									
								</div>
							</div>
						</div>
					</div>      
				</div>
			</div>
			<% if (usuario.getIdTipoUsuario()==TipoUsuario.USUARIO_PROVEEDOR) { %>
			<script>
				window.addEventListener("load",comenzar,false);	
				$(document).ready(cargarEspecializacionesUpdateSi());
				$(document).ready(cargarEspecializacionesUpdateNo());
				$(document).ready(comenzar());
				$(document).ready(pasar());
				$(document).ready(regresar());
			</script>
			<%
			}
			%>
			<script>				
				$(document).ready(eliminarCuenta());			
			</script>
<!-- Editar Perfil End-->
<%@include file="/common/footer.jsp"%>