<%@include file="/common/header.jsp"%>
<!-- Buscador Proveedor Start -->
			<div class="listing">
					<!-- Search -->
				<div class="search2">
					<div class="search2_inner">
							<!-- Search Contents -->				
						<div class="container fill_height no-padding">
							<div class="row fill_height no-margin">
								<div class="col fill_height no-padding">
										<!-- Search Tabs -->
									<div class="search3_tabs_container">
										<div class="search3_tabs d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-center">
											<div class="search3_tab active d-flex flex-row align-items-center justify-content-lg-center justify-content-center"><img src="<%=context%>/images/profesionalmenu.png" alt=""><span>Proveedores</span></div>
										</div>		
									</div>
											<!-- search2 Panel -->
									<div class="search3_panel active">
										<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off" id="search-proveedor" class="search3_panel_content d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-start">
										<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.SEARCH_USUARIO%>"/>
										<div  class="caja-descripcion" id="caja-focus-descripcion" onmousedown="setFlag()" onmouseup="doProcessing()">
											<div class="search3_item">
												<div>Descripción</div>
												<input type="text" name="<%=ParameterNames.BUSCAR_DESCRIPCION%>" id="descripcion-proveedor" placeholder="¿Qué estás buscando?" class="destination search3_input"
												onkeyup="buscarProveedores()"  onclick="muestraDescripcion()" onblur="ocultaDescripcion()"/>
											</div>													
											<div id="proveedores-results" class="cuadro-proveedores-results" >
												<ul id="lista-results">
												</ul>
											</div>
										</div>
										<div class="search3_item">
											<div>Especializacion</div>
											<select name="<%=(ParameterNames.ID_ESPECIALIZACION)%>" id="especializacion-select" class="dropdown_item_select search3_input">
													<option disabled selected>Selecciona una especialización</option>
											</select>
										</div>
										<div class="search3_item">
											<div>Provincia</div>
											<select name="<%=(ParameterNames.ID_PROVINCIA)%>" id="provincia-select" class="dropdown_item_select search3_input">
													<option disabled selected>Selecciona una provincia</option>
											</select>
										</div>
										<div class="extras">
											<ul class="search3_extras clearfix">
												<li class="search3_extras_item">
													<div class="clearfix">
														<input type="checkbox" name="<%=ParameterNames.SERVICIO_24%>" value="true" id="servicio24" class="search3_extras_cb">
														<label for="servicio24">Servicio24H</label>
													</div>
												</li>
												<li class="search3_extras_item2">
													<div class="clearfix">
														<input type="checkbox" name="<%=ParameterNames.PROVEEDOR_VERIFICADO%>" value="true" id="experto-negocio" class="search3_extras_cb">
														<label for="experto-negocio">Proveedor Verificado</label>
													</div>
												</li>
											</ul>
										</div>											
										<button class="button search2_button">Buscar<span></span><span></span><span></span></button>												
									</div>
										<!-- Search Panel End -->									
								</div>
							</div>
						</div>	
					</div>	
				</div>
				<%
					Results<UsuarioDTO> results = (Results<UsuarioDTO>) request.getAttribute(AttributeNames.USUARIO);
					List<UsuarioDTO> usuarios = results.getData();
					//Aclaracion usuarios: u = proveedor
					for (UsuarioDTO u : usuarios) {					
				%>
					<!-- Detalle Proveedor Start -->
				<div class="container">
				<%
					int valoracionMediaEntera = (int) Math.ceil(u.getValoracionMedia());
				 %>
					<div class="row">
						<div class="col-lg-12">
							<div class="caja-resultado-detail">
								<div class="caja-pre-detail-proveedor">									
									<div id="caja-corazon-<%=u.getIdUsuario()%>" class="caja-corazon">
									<% 
									if (usuario != null) {
										Set<Long> idsFavoritos = (Set<Long>) SessionManager.get(request, AttributeNames.FAVORITOS);
										if (!idsFavoritos.contains(u.getIdUsuario()) && u.getIdUsuario()!=usuario.getIdUsuario()) {
									%>
										<img onclick="anhadirFavorito(<%=u.getIdUsuario()%>)" src="<%=context%>/images/heart.png" alt="Icono Corazon Vacio" id="anhadir-favorito-<%=u.getIdUsuario()%>">
									<%
										} else if (idsFavoritos.contains(u.getIdUsuario()) && u.getIdUsuario()!=usuario.getIdUsuario()) {
									%>										
										<img onclick="deleteFavorito(<%=u.getIdUsuario()%>)" src="<%=context%>/images/heart2.png" alt="Icono Corazon Lleno" id="delete-favorito-<%=u.getIdUsuario()%>">									
									<%
										}
									}
									%>
									</div>
									<div class="nombre-perfil-detail">
										<h1><%=u.getNombrePerfil()%></h1>
									</div>
									<div class="reformatec-detail-button"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_TRABAJO%>&<%=ParameterNames.ID_USUARIO_CREADOR_TRABAJO%>=<%=u.getIdUsuario()%>">Ver Trabajos<span></span><span></span><span></span></a></div>
									<div class="valoraciones-detail-usuario">
										<div class="proveedor_detail_visu">
										<%
											if (u.getNumeroVisualizaciones()==0){
										%>												
											Sin visualizaciones											
										<%
											}else if (u.getNumeroVisualizaciones()==1){
										%>												
											<%=u.getNumeroVisualizaciones()%> visualización											
										<%
											}else if (u.getNumeroVisualizaciones()>=2){
										%>												
											<%=u.getNumeroVisualizaciones() %> visualizaciones												
										<%
											}
										%>
										</div>
										<%
										if (usuario != null) {
											if(u.getValoracionMedia()!=null) {
										%>											
											<div class="media-detail-proveedor"><%=u.getValoracionMedia() %></div>					
										<%	
											}
										}
										%>											
									</div>
								</div>
								<%
								if (usuario != null) {
								%>
								<div class="proveedor_detail_estrellas">
									<div class="rating_r rating_r_<%=valoracionMediaEntera%>" data-rating="<%=valoracionMediaEntera%>">
										<i></i>
										<i></i>
										<i></i>
										<i></i>
										<i></i>
									</div>
								</div>
								<%	
									}
								%>	
								<div class="row">
									<!-- Foto Perfil -->
									<div class="resultado_foto_detail">
										<div class="offers2_image_background" style="background-image:url(<%=context+ConstantWebUtil.WEB_USER_PATH+u.getIdUsuario()%>/perfil.jpg)" alt="Foto portada proveedor"></div>
									</div>
									<!-- Datos proveedor -->
									<div class="proveedor_caja_detail">
										<div class="col-lg-7 proveedor_detail_datos">
											<div class="proveedor_detail_provi"><p><%=u.getNombrePoblacion()%>, (<%=u.getNombreProvincia()%>)</p></div>
											<div class="proveedor_detail_calle"><p><%=u.getNombreCalle()%>, CP: (<%=u.getCodigoPostal()%>)</p></div>
											<div class="proveedor_detail_tel"><p><a href="tel:<%=u.getTelefono1()%>"><%=u.getTelefono1()%></a>
											<% 
											if (u.getTelefono2()!=null) {
											%>
												<a href="tel:<%=u.getTelefono2()%>"><%=u.getTelefono2()%></a>
											<%
											}
											%>
											</p></div>
											<%
											if (u.getDireccionWeb()!=null) {	
											%>									
											<div class="proveedor_detail_web"><p><a href="https://<%=u.getDireccionWeb()%>"><%=u.getDireccionWeb()%></a></p></div>
											<%
											}
											%>								
										</div>
										<!--  Especializaciones Proveedor -->
										<div class="col-lg-5 proveedor_detail_especializaciones">
										<%									
										if (u.getEspecializaciones()!=null){
											%><h2 class="titulo-especializaciones-detail">Especializaciones: </h2><%
											for(Especializacion e : u.getEspecializaciones()){
												%><p class="nombre-especializacion"><%=e.getNombre()%></p><%
											}											
										}									
										%>
										</div>                                    
	                                    <div class="col-lg-12 offers2_icons">
											<ul class="offers2_icons_list">
												<%if (u.getServicio24()==true){ %><li class="offers2_icons_item"><img src="<%=context%>/images/24h.png" alt=""></li><% } %>
												<%if (u.getProveedorVerificado()==true){ %><li class="offers2_icons_item"><img src="<%=context%>/images/ruler.png" alt=""></li><% } %>
											</ul>
										</div>
									</div>
										<!-- Descripcion Proveedor -->
									<div class="descripcion-proveedor-detail">
										<p><%=u.getDescripcion()%></p>
									</div>
								</div>
								<%	
								//Aclaracion usuarios: usuario = usuario en sesion(Logueado)
								if (usuario != null) {
							
									Results<ValoracionDTO> resultsValoraciones = (Results<ValoracionDTO>) request.getAttribute(AttributeNames.VALORACION);
									List<ValoracionDTO> valoraciones = resultsValoraciones.getData();
									
									for (ValoracionDTO v : valoraciones) {
								%>
									<!-- Valoraciones Del Proveedor -->
								<div class="reviews">
									<div class="reviews_title"></div>
									<div class="reviews_container">
											<!-- Valoracion -->
										<div class="review">
											<div class="row">
												<div class="col-lg-1">
													<div class="review_image">
														<img src="<%=context%>/images/valoraciondetalle.jpg" alt="Detalle Valoración">
													</div>
												</div>
												<div class="col-lg-11">
													<div class="review_content">
														<div class="review_title_container">
															<div class="review_title"><%=v.getTitulo() %></div>
															<div class="review_rating"><%=v.getNotaValoracion() %></div>
														</div>
														<div class="review_text">
															<p><%=v.getDescripcion()%></p>
														</div>
														<div class="review_name"><%=v.getNombrePerfilUsuarioValora()%></div>
														<%
															DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, (Locale) SessionManager.get(request, AttributeNames.LOCALE));
														%>
														<div class="review_date"><%=df.format(v.getFechaHoraCreacion())%></div>
													</div>
												</div>
											</div>
										</div>							
									</div>
								</div>									
								<%
										}
								%>
								<div class="caja_paginacion">
									<ul class="paginacion">
									<!--  Paginador -->
									<%
										Integer currentPage = (Integer) request.getAttribute(AttributeNames.CURRENT_PAGE);
									
										Integer pagingFrom = (Integer) request.getAttribute(AttributeNames.PAGING_FROM);
										Integer pagingTo = (Integer) request.getAttribute(AttributeNames.PAGING_TO);
										
										Integer totalPages = (Integer) request.getAttribute(AttributeNames.TOTAL_PAGES);
										
										Map<String,String[]> parameters = new HashMap<String, String[]>(request.getParameterMap());								
										parameters.remove(ParameterNames.PAGE); // para que no arrastre el valor anterior
										
										// Ya viene terminada en &
										String baseURL = ParameterUtils.getURLPaginacion(request.getContextPath()+ControllerNames.USUARIO, parameters);
		
										
										// Primera
										if (currentPage>1) {
											%> 
											<li><a href="<%=baseURL%>">Primera</a></li>
											<%
										}
		
										
										// Anterior
										if (currentPage>1) {
											%> 
											<li><a href="<%=baseURL+ParameterNames.PAGE+"="+(currentPage-1)%>">Anterior</a></li>
											<%
										}
										
										// Paginas antes de la actual
										for (int i = pagingFrom; i<currentPage; i++) {
												%> 
												<li>&nbsp;<a href="<%=baseURL+ParameterNames.PAGE+"="+i%>"><%=i%></a>&nbsp;</li>
												<% 
										}	
										
										// La actual
										%>&nbsp;<span class="paginacion_activa"><%=currentPage%></span>&nbsp;<%
										
										// Despues de la actual
										for (int i = currentPage+1; i<=pagingTo; i++) {
												%> 
												<li>&nbsp;<a href="<%=baseURL+ParameterNames.PAGE+"="+i%>"><%=i%></a>&nbsp;</li>
												<% 
										}
										
										// Siguiente
										if (currentPage<totalPages) {
											%>
												<li><a href="<%=baseURL+ParameterNames.PAGE+"="+(currentPage+1)%>">Siguiente</a></li>
											<%
										}
										
										
										// Última
										if (currentPage<totalPages) {
											%>
												<li><a href="<%=baseURL+ParameterNames.PAGE+"="+(totalPages)%>">Última</a></li>
											<%
									}
										
										%>
									</ul>
								</div>
								<%
									}
								%>	
							</div>
						</div>					
					</div>								
				</div>
				<%
					}		
				%>	
			</div>
			<script>
				$(document).ready(cargarProvincias());
				$(document).ready(cargarEspecializaciones());
			</script>
			<!-- Detalle Proveedor End -->
<%@include file="/common/footer.jsp"%>