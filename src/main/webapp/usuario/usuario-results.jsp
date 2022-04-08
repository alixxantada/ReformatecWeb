<%@include file="/common/header.jsp"%>
<!-- Secci�n Central Start-->
			<div class="col-lg-12 caja_central">
				<div class="row">						
					<div class="col-lg-3 col-md-12 col-sm-12 buscador_usuario">
							<!-- search2 -->
						<div class="search2">
							<div class="search2_inner">
									<!-- search2 Contents -->
								<div class="container fill_height no-padding">
									<div class="row fill_height no-margin">
										<div class="col fill_height no-padding">
												<!-- search2 Tabs -->
											<div class="search3_tabs_container">
												<div class="search3_tabs d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-center">
													<div class="search3_tab active d-flex flex-row align-items-center justify-content-lg-center justify-content-center"><img src="<%=context%>/images/profesionalmenu.png" alt=""><span>Proveedores</span></div>
												</div>		
											</div>
												<!-- search2 Panel -->
											<div class="search3_panel active">
												<form action="<%=context+ControllerNames.USUARIO%>" autocomplete="off" id="search-proveedor" class="search3_panel_content d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-start">
												<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.SEARCH_USUARIO%>"/>
												<div class="search3_item">
													<div>Descripci�n</div>
													<input type="text" name="<%=ParameterNames.DESCRIPCION%>" id="descripcion-proveedor" placeholder="�Qu� est�s buscando?" class="destination search3_input"
													onkeyup="buscarProveedores()"/>
												</div>
												<div id="proveedores-results" class="cuadro-proveedores-results">
												</div>
												<div class="search3_item">
													<div>Especializacion</div>
													<select name="<%=ParameterNames.ESPECIALIZACION_ID%>" id="especializacion-select" class="dropdown_item_select search3_input">
														-	<option disabled selected>Selecciona una especializaci�n</option>
													</select>
												</div>
												<div class="search3_item">
													<div>Provincia</div>
													<select name="<%=ParameterNames.PROVINCIA_ID%>" id="provincia-select" class="dropdown_item_select search3_input">
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
										</div>
									</div>
								</div>	
							</div>	
						</div>
					</div>
					<div class="col-lg-9 caja_resultados_usuarios">
							<!-- offers2 Sorting -->
						<div class="col-lg-11 ordenar_por">
							<div class="offers2_sorting_container">
								<ul class="offers2_sorting">
									<li>
										<span class="sorting_text">Ordenar Por</span>
										<i class="fa fa-chevron-down"></i>
										<ul>
											<li class="sort_btn" data-isotope-option='{ "sortBy": "valoracion" }'><span>Valoraciones</span></li>
											<li class="sort_btn" data-isotope-option='{ "sortBy": "visualizaciones" }'><span>Visualizaciones</span></li>
										</ul>
									</li>
								</ul>
							</div>
						</div>
						</form>
						<div class="col-lg-11 usuario_resultados">

							<%
							// lio de usuarios usuarios es la lista de usuarios resultados
							Results<UsuarioDTO> results = (Results<UsuarioDTO>) request.getAttribute(AttributeNames.USUARIO);
							List<UsuarioDTO> usuarios = results.getData();
							
							for (UsuarioDTO u : usuarios) {
							%>
								<!-- Proveedor Result -->
							<div class="col-lg-12 valoracion_visualizacion">
								<div class="offers2_item rating_5">	
									<div class="offers2_content">
										<%						
											if (usuario != null) {
												if(u.getValoracionMedia()==null) {
	
												} else {
											
													int valoracionMediaEntera = (int) Math.round(u.getValoracionMedia()/2);
											
												%>
											<div class="rating_r rating_r_<%=valoracionMediaEntera%> offers2_rating" data-rating="<%=valoracionMediaEntera%>">
												<i></i>
												<i></i>
												<i></i>
												<i></i>
												<i></i>
											</div>
											<%	
											}
										}
										%>												
										<div class="offer2_reviews">
											<div class="offer2_reviews_content">
												<%
													if (u.getNumeroVisualizaciones()==0){
												%>
												<div class="offer2_reviews_content">
													<div class="offer2_reviews_subtitle">Sin visualizaciones</div>
												</div>
												<%
													}else if (u.getNumeroVisualizaciones()==1){
												%>
												<div class="offer2_reviews_content">
													<div class="offer2_reviews_subtitle"><%=u.getNumeroVisualizaciones() %> visualizaci�n</div>
												</div>
												<%
													}else if (u.getNumeroVisualizaciones()>=2){
												%>
												<div class="offer2_reviews_content">
													<div class="offer2_reviews_subtitle"><%=u.getNumeroVisualizaciones() %> visualizaciones</div>
												</div>
												<%
													}
												%>
												<%						
													if (usuario != null) {
														if (u.getNumeroValoraciones()>=2) {
												%>
													<div class="offer2_reviews_content">
													<div class="offer2_reviews_subtitle"><%=u.getNumeroValoraciones() %>  valoraciones</div>
													</div>
													<div class="offer2_reviews_rating text-center"><%=u.getValoracionMedia() %></div>
													<%
													}else if (u.getNumeroValoraciones()==1) {
												%>
												<div class="offer2_reviews_content">
												<div class="offer2_reviews_subtitle"> 1 valoraci�n</div>
												</div>
												<div class="offer2_reviews_rating text-center"><%=u.getValoracionMedia() %></div>																																					
												<%
													}else if (u.getNumeroValoraciones()==0) {
												%>
												<div class="offer2_reviews_content">
												<div class="offer2_reviews_subtitle"> Sin valoraciones</div>
												</div>
												<div class="offer2_reviews_rating text-center">-</div>	
												<%
														}
													}
												%>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-lg-12 nombre_perfil_resultados">
								<a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_USUARIO%>&<%=ParameterNames.USUARIO_ID%>=<%=u.getIdUsuario()%>"><%=u.getNombrePerfil() %></a><span></span>
							</div>
							<div class="row">
								<div class="resultado_foto">
									<div class="offers2_image_container">
										<div class="offers2_image_background" style="background-image:url(<%=context%>/images/proveedorportada.jpg)" alt="Foto portada proveedor"></div>
										<div class="offer_name"><a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_USUARIO%>&<%=ParameterNames.USUARIO_ID%>=<%=u.getIdUsuario()%>"><%=u.getNombrePoblacion()%></a></div>
									</div>
								</div>
								<div class=" col-lg-8 resultado_datos">									
									<div class="col-lg-12 caja_detalles">													
										<div class="col-lg-12 caja_descripcion">													
										<p class="offers2_text"><%=u.getDescripcion()%></p>
										</div>
										<div class="col-lg-12 caja_datos">
										</div>
									</div>									
									<div class="col-lg-12 offers2_icons">
										<ul class="offers2_icons_list">
											<%if (u.getServicio24()==true){ %><li class="offers2_icons_item"><img src="<%=context%>/images/24h.png" alt=""></li><% } %>
											<%if (u.getProveedorVerificado()==true){ %><li class="offers2_icons_item"><img src="<%=context%>/images/ruler.png" alt=""></li><% } %>
										</ul>
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
									String baseURL = ParameterUtils.getURL(request.getContextPath()+"/"+ControllerNames.USUARIO, parameters);
	
									
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
									
									
									// �ltima
									if (currentPage<totalPages) {
										%>
											<li><a href="<%=baseURL+ParameterNames.PAGE+"="+(totalPages)%>">�ltima</a></li>
										<%
								}
									
									%>
							</ul>
						</div>				
					</div>
				</div>
			</div>
			<script>
				$(document).ready(cargarProvincias());
				$(document).ready(cargarEspecializaciones());
			</script>
<!-- Secci�n Central End-->
<%@include file="/common/footer.jsp"%>