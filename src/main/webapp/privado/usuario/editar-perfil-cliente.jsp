<%@include file="/common/header.jsp"%>
<!-- Perfil Menu Start-->
			<div class="container">
				<div class="menuusuario">
					<div><a href="<%=context+ViewNames.USUARIO_MIS_PROVEEDORES%>">Mis Proveedores</a></div>
					<div><a href="<%=context+ViewNames.USUARIO_MIS_PROYECTOS%>">Mis Proyectos</a></div>
					<div><a href="<%=context+ViewNames.USUARIO_PERFIL_CLIENTE%>">Mi Perfil</a></div>
				</div>
					<!-- Perfil Menu End-->
					<!-- Editar Perfil Start-->
				<div class="page-wrapper">                
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12">
								<div class="card">
									<div class="card-body">
										<div class="fotoperfil">
											<img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion">
										</div>
										<div class="form-group row">
											<label class="col-md-3">Cambiar Foto Perfil</label>
											<div class="col-md-9">
												<div class="custom-file">
													<input type="file" class="custom-file-input" id="validatedCustomFile" required>
													<label class="custom-file-label" for="validatedCustomFile">Subir Archivo...</label>
													<div class="invalid-feedback">Archivo inválido</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label for="hue-demo">Nombre De Perfil</label>
											<input type="text" id="hue-demo" class="form-control demo" data-control="hue" value="<%=ParameterUtils.print(usuario.getNombrePerfil())%>" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="position-bottom-left">Dirección</label>
											<input type="text" id="position-bottom-left" class="form-control demo" data-position="bottom left" value="<%=ParameterUtils.print(usuario.getNombreCalle())%>" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="position-top-right">Población</label>
											<input type="text" id="position-top-right" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getNombrePoblacion())%>" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="position-top-right">Código Postal</label>
											<input type="text" id="position-top-right" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getCodigoPostal())%>" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="position-top-right">Teléfono 1</label>
											<input type="text" id="position-top-right" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getTelefono1())%>" autocomplete="off">
										</div>
										<div class="form-group">
											<label for="position-top-right">Teléfono 2</label>
											<input type="text" id="position-top-right" class="form-control demo" data-position="top right" value="<%=ParameterUtils.print(usuario.getTelefono2())%>" autocomplete="off">
										</div>
										<div class="border-top">
											<div>
												<button type="submit" class="btn btn-success"><a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.UPDATE_USUARIO%>">Guardar</a></button>
												<button type="submit" class="btn btn-danger"><a href="<%=context+ViewNames.USUARIO_PERFIL_CLIENTE%>">Cancelar</a></button>													
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>      
				</div>
			</div>
<!-- Editar Perfil End-->
<%@include file="/common/footer.jsp"%>