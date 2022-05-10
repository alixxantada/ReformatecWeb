<%@include file="/common/header.jsp"%>
<%@include file="/common/menu-usuario.jsp"%>
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
												if (usuario.getIdTipoUsuario()==TipoUsuario.USUARIO_PROVEEDOR){
													if (usuario.getDescripcion()!=null){
											%>
											<label>Tu Descripcion:</label>
											<p><%=usuario.getDescripcion()%></p>
											<%
													} else {
											%>
											<p>Vaya, todavía no tienes descripcion! A nuestros clientes les gustaria saber mas de ti!</p>
											<%
													}
												}
											%>
										</div>
										<div class="infoPerfil">
										<div class="col-lg-6 form-group">
												<label for="hue-demo">Nombre De Perfil: </label>
												<p><%=ParameterUtils.print(usuario.getNombrePerfil())%></p>
											</div>
											<div class="col-lg-6 form-group perfilinput">
												<label for="position-bottom-left">Dirección: </label>
												<p><%=ParameterUtils.print(usuario.getNombreCalle())%></p>
											</div>
											<div class="col-lg-6 form-group perfilinput">
												<label for="position-top-right">Población: </label>
												<p><%=ParameterUtils.print(usuario.getNombrePoblacion())%> ( <%=ParameterUtils.print(usuario.getNombreProvincia())%> )</p>
											</div>
											<div class="col-lg-6 form-group perfilinput">
												<label for="position-top-right">Código Postal: </label>
												<p><%=ParameterUtils.print(usuario.getCodigoPostal())%></p>
											</div>
											<div class="col-lg-6 form-group perfilinput">
												<label for="position-top-right">Teléfono: </label>
												<p><%=ParameterUtils.print(usuario.getTelefono1())%></p>
											</div>										
											<%
												if (usuario.getTelefono2()!=null){												
												
											%>
											<div class="form-group perfilinput">
												<label for="position-top-right">Teléfono opcional: </label>
												<p><%=ParameterUtils.print(usuario.getTelefono2())%></p>
											</div>
											<%
												}
											
												if (usuario.getIdTipoUsuario()==TipoUsuario.USUARIO_PROVEEDOR) {
													
								
											%>
											<div class="form-group">
												<label for="position-top-right">Cif: </label>
												<p><%=ParameterUtils.print(usuario.getCif())%></p>
											</div>
											<div class="form-group">
												<label for="position-top-right">Direccion Web: </label>
												<%
														if (usuario.getDireccionWeb()!=null){
													
												%>
												
												<p><%=ParameterUtils.print(usuario.getDireccionWeb())%></p>
												<%
														} else {														
													
												%>
												<p>Sin definir</p>
												<%
														}
												%>
											</div>
					
											<div class="form-group">
												<label for="position-top-right">Servicio 24: </label>
													<%
														if (usuario.getServicio24()==true){
													
													%>
														<p>Disponible</p>
												
													<%
														} else if (usuario.getServicio24()==false) {														
													
													%>
														<p>No dispone</p>
											</div>											
											<%
														}
											%>
											<div class="proveedor_perfil_especializaciones">
											<%									
												if (usuario.getEspecializaciones()!=null){
													%><p>Especializaciones</p><%
													for(Especializacion e : usuario.getEspecializaciones()){
														%><p class="nombre-especializacion"><%=e.getNombre()%></p><%
													}											
												} else {									
											%>
											<p>Todavia no has asignado ninguna especializacion</p>
											<%
												}
											%>
											</div>
											<%
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