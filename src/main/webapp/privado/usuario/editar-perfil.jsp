<%@include file="/common/header.jsp"%>
<!-- Perfil Menu Start-->
			<div class="container">
				<div class="menuusuario">
					<div><a href="<%=context+ViewNames.USUARIO_MIS_PROVEEDORES%>">Mis Proveedores</a></div>
					<div><a href="<%=context+ViewNames.USUARIO_MIS_PROYECTOS%>">Mis Proyectos</a></div>
					<div><a href="<%=context+ViewNames.USUARIO_PERFIL%>">Mi Perfil</a></div>
				</div>
					<!-- Perfil Menu End-->
					<!-- Editar Perfil Start-->
				<div class="page-wrapper">                
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12">
								<div class="card">
									<div class="card-body">
										<div class="col-md-12">
											<div class="custom-file">
												<input type="file" class="custom-file-input" id="validatedCustomFile" required>
												<label class="custom-file-label" for="validatedCustomFile">Personalizar Foto Perfil</label>
												<div class="invalid-feedback">Archivo inválido</div>
											</div>
										</div>
										<form action="<%=context+ControllerNames.PRIVADO_USUARIO%>" autocomplete="off" method="post"  class="contact2_form">
										<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.UPDATE_USUARIO%>"/>
										<div class="col-sm-12">
											<div class="fotoperfil">
												<img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Perfil">												
											</div>																				
											<div class="descripcionUsuario">	
												<%
												if(usuario.getIdTipoUsuario()==2){
												
												%>
												<div class="col-md-12">
													<div class="form-group">
														<input type="text" name="<%=ParameterNames.BUSCAR_DESCRIPCION%>" class="form-control demo" style="z-index: 9999999" value="<%=ParameterUtils.print(usuario.getDescripcion())%>" autocomplete="off">
													</div>
												</div>
												<%
												}
												%>
											</div>
										</div>										
										<div class="col-sm-12">								
											<div class="form-group">
												<label for="hue-demo">Nombre De Perfil</label>
												<input type="text" name="<%=ParameterNames.NOMBRE_PERFIL%>" id="hue-demo" class="form-control demo" data-control="hue" value="<%=ParameterUtils.print(usuario.getNombrePerfil())%>" autocomplete="off">
											</div>																		
											<div class="form-group">
												<label for="hue-demo">Dirección actual: </label>										
												<label id="poblacionActual" for="position-top-right"> <%=ParameterUtils.print(usuario.getNombreCalle())%>, <%=ParameterUtils.print(usuario.getNombrePoblacion())%> <%=ParameterUtils.print(usuario.getCodigoPostal())%> ( <%=ParameterUtils.print(usuario.getNombreProvincia())%> )</label>
												<select name="<%=ParameterNames.ID_PROVINCIA%>" id="provincia-selectPerfil" class="dropdown2_item_select search2_input"  
												required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.ID_PROVINCIA))%>"
												onchange="cargarPoblacionesPerfil()">									
												</select>
												</div>
											<div class="form-group">
												<select name="<%=ParameterNames.ID_POBLACION%>" id="poblacion-selectPerfil" class="dropdown3_item_select search2_input" 
												required value="<%=ParameterUtils.print(request.getParameter(ParameterNames.ID_POBLACION))%>"
												onchange="cargarDireccionPerfil()">
													<option disabled selected>Selecciona una población</option>
												</select>
											</div>
											<div class="form-group">
												<input type="text" name="<%=ParameterNames.DIRECCION%>" id="direccionEditarPerfil" class="form-control demo" data-position="bottom left" placeholder="Introduce Calle, Numero, Piso" value="<%=ParameterUtils.print(usuario.getNombreCalle())%>" autocomplete="off">
											</div>
											<div class="form-group" >
												<input type="text" name="<%=ParameterNames.COD_POSTAL%>" id="codPostalEditarPerfil" class="form-control demo" data-position="top right" placeholder="Introduce Cod Postal" value="<%=ParameterUtils.print(usuario.getCodigoPostal())%>" autocomplete="off">
											</div>
											<div class="form-group">
												<label for="position-top-right">Teléfono 1</label>
												<input type="text" name="<%=ParameterNames.TELEFONO_1%>" id="position-top-right" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getTelefono1())%>" autocomplete="off">
											</div>
											<div class="form-group">
												<label for="position-top-right">Teléfono 2</label>
												<input type="text" name="<%=ParameterNames.TELEFONO_2%>" id="position-top-right" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getTelefono2())%>" autocomplete="off">
											</div>
											<input type="hidden" name="<%=ParameterNames.ID_USUARIO%>" value="<%=usuario.getIdUsuario()%>"/>
											<input type="hidden" name="<%=ParameterNames.ID_TIPO_USUARIO%>" value="<%=usuario.getIdTipoUsuario()%>"/>
											<div>
												<input type="submit" class="btn btn-success" value="Guardar"></input>
												<button class="btn btn-danger"><a href="<%=context+ViewNames.USUARIO_PERFIL%>">Cancelar</a></button>													
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
			<script>
				$(document).ready(cargarProvinciasPerfil());
			</script>
<!-- Editar Perfil End-->
<%@include file="/common/footer.jsp"%>