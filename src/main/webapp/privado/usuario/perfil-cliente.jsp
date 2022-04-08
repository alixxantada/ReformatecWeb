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
										<%
											if (usuario!=null){												
											
										%>
										<h4 class="card-title">Mi Perfil</h4>
										<div class="perfil-caja-izq">	
												<img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion">										
										</div>
										<div class="perfil-caja-der">
											<div class="form-group">
												<label for="hue-demo">Nombre De Perfil: </label>
												<p><%=ParameterUtils.print(usuario.getNombrePerfil())%></p>
											</div>
											<div class="form-group">
												<label for="position-bottom-left">Dirección: </label>
												<p><%=ParameterUtils.print(usuario.getNombreCalle())%></p>
											</div>
											<div class="form-group">
												<label for="position-top-right">Población: </label>
												<p><%=ParameterUtils.print(usuario.getNombrePoblacion())%></p>
											</div>
											<div class="form-group">
												<label for="position-top-right">Código Postal: </label>
												<p><%=ParameterUtils.print(usuario.getCodigoPostal())%></p>
											</div>
											<div class="form-group">
												<label for="position-top-right">Teléfono 1: </label>
												<p><%=ParameterUtils.print(usuario.getTelefono1())%></p>
											</div>										
											<%
												if (usuario.getTelefono2()!=null){												
												
											%>
											<div class="form-group">
												<label for="position-top-right">Teléfono 2: </label>
												<p><%=ParameterUtils.print(usuario.getTelefono2())%></p>
											</div>
											<%
												} 
											}
											%>
										</div>
										<div class="border-top">
											<div>                                    											
												<button type="submit" class="btn btn-info"><a href="<%=context+ViewNames.USUARIO_EDITAR_PERFIL_CLIENTE%>">Editar</a></button>	 												
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