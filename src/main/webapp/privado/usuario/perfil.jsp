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
										<%
											if (usuario!=null){				
											
										%>										
										<div>                                    											
											<button type="submit" class="btn btn-info"><a href="<%=context+ViewNames.USUARIO_EDITAR_PERFIL%>">Editar Perfil</a></button>	 												
										</div>
										<div class="perfil-caja-izq">	
												<img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion">										
										</div>
										<div class="perfil-caja-der">
											<%
												if (usuario.getIdTipoUsuario()==2){
													if (usuario.getDescripcion()!=null){
											%>
											<label>Tu Descripcion:</label>
											<p><%=usuario.getDescripcion()%></p>
											<%
													} else {
											%>
											<p>Vaya, todav�a no tienes descripcion! A nuestros clientes les gustaria saber mas de ti!</p>
											<%
													}
												}
											%>
										</div>
										<div class="infoPerfil">
										<div class="form-group">
												<label for="hue-demo">Nombre De Perfil: </label>
												<p><%=ParameterUtils.print(usuario.getNombrePerfil())%></p>
											</div>
											<div class="form-group">
												<label for="position-bottom-left">Direcci�n: </label>
												<p><%=ParameterUtils.print(usuario.getNombreCalle())%></p>
											</div>
											<div class="form-group">
												<label for="position-top-right">Poblaci�n: </label>
												<p><%=ParameterUtils.print(usuario.getNombrePoblacion())%></p>
											</div>
											<div class="form-group">
												<label for="position-top-right">C�digo Postal: </label>
												<p><%=ParameterUtils.print(usuario.getCodigoPostal())%></p>
											</div>
											<div class="form-group">
												<label for="position-top-right">Tel�fono 1: </label>
												<p><%=ParameterUtils.print(usuario.getTelefono1())%></p>
											</div>										
											<%
												if (usuario.getTelefono2()!=null){												
												
											%>
											<div class="form-group">
												<label for="position-top-right">Tel�fono 2: </label>
												<p><%=ParameterUtils.print(usuario.getTelefono2())%></p>
											</div>
											<%
												}
											
												if (usuario.getIdTipoUsuario()==2) {
													
													if (usuario.getDireccionWeb()!=null){
												
											%>
											<div class="form-group">
												<label for="position-top-right">Direccion Web: </label>
												<p><%=ParameterUtils.print(usuario.getDireccionWeb())%></p>
											</div>
												<%
												}												
													
													if (usuario.getServicio24()!=null){
													
												%>
											<div class="form-group">
												<label for="position-top-right">Servicio 24: </label>
													<%
														if (usuario.getServicio24()==true){
													
													%>
														<p>Disponible</p>
												
													<%
														} else if (usuario.getServicio24()==false) {														
													
													%>
														<p>No disponible</p>
											</div>
											<%
														}
													}
												}	
											}
											%>
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
			</div>
<!-- Editar Perfil End-->
<%@include file="/common/footer.jsp"%>