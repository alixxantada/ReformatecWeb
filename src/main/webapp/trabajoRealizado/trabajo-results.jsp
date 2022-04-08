<%@include file="/common/header.jsp"%>
<!-- Sección Central Start-->
			<div class="col-lg-12 caja_central">
				<div class="row">						
					<div class="col-lg-3 col-md-12 col-sm-12 buscador_usuario">
							<!-- Search -->
						<div class="search2">
							<div class="search2_inner">
									<!-- Search Contents -->
								<div class="container fill_height no-padding">
									<div class="row fill_height no-margin">
										<div class="col fill_height no-padding">
												<!-- Search Tabs -->
											<div class="search3_tabs_container">
												<div class="search3_tabs d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-start">													
													<div class="search3_tab active d-flex flex-row align-items-center justify-content-lg-center justify-content-start"><img src="<%=context%>/images/trabajomenu.png" alt="icono trabajos">Trabajos</div>
												</div>		
											</div>												
												<!-- Search Panel -->
											<div class="search3_panel active">
												<form action="<%=context+ControllerNames.TRABAJO_REALIZADO%>" autocomplete="off" id="search-trabajo" class="search3_panel_content d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-start">
												<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.SEARCH_TRABAJO%>"/>
												<div class="search3_item">
													<div>Descripción</div>
													<input type="text" name="<%=ParameterNames.DESCRIPCION%>" id="descripcion-trabajo" class="destination search2_input" placeholder="¿Qué quieres ver?"
													onkeyup="buscarTrabajos()"/>
												</div>
												<div class="search3_item">
													<div>Especialización</div>
													<select name="<%=ParameterNames.ESPECIALIZACION_ID%>" id="especializacion-select"  class="dropdown_item_select search2_input">
														<option disabled selected>Selecciona una especialización</option>														
													</select>
												</div>
												<div class="search3_item">
													<div>Provincia</div>
													<select name="<%=ParameterNames.PROVINCIA_ID%>" id="provincia-select" class="dropdown_item_select search2_input">
															<option disabled selected>Selecciona una provincia</option>
													</select>
												</div>
												<button class="button search2_button">Buscar<span></span><span></span><span></span></button>
											</div>
										</div>
									</div>
								</div>	
							</div>	
						</div>
					</div>
					<div class="col-lg-8 caja_resultados_trabajos">					
							<!-- Offers Sorting -->
						<div class="col-lg-11 ordenar_por">
							<div class="offers2_sorting_container">
								<ul class="offers2_sorting">
									<li>
										<span class="sorting_text">Ordenar Por</span>
										<i class="fa fa-chevron-down"></i>
										<ul>
											<li class="sort_btn" data-isotope-option='{ "sortBy": "fecha-creacion" }'><span>Fecha Creación</span></li>
											<li class="sort_btn" data-isotope-option='{ "sortBy": "valoracion" }'><span>Valoración</span></li>
											<li class="sort_btn" data-isotope-option='{ "sortBy": "visualizaciones" }'><span>Visualizaciones</span></li>							
										</ul>
									</li>
								</ul>
							</div>
						</div>
						</form>
						<div class="col-lg-11 trabajo_resultados">
							<%
							// lio de usuarios usuarios es la lista de usuarios resultados
							Results<TrabajoRealizadoDTO> results = (Results<TrabajoRealizadoDTO>) request.getAttribute(AttributeNames.TRABAJO);
							List<TrabajoRealizadoDTO> trabajos = results.getData();
							
							for (TrabajoRealizadoDTO tr : trabajos) {
							%>
								<!-- Trabajo Result -->
							<div class="offers2_item rating_3">
							<div class="col-lg-12 valoracion_visualizacion2">
								<div class="offers2_item rating_5">	
									<div class="offers2_content">
										<%						
											if (usuario != null) {
												if(tr.getNotaMedia()==null) {
	
												} else {
											
													int valoracionMediaEntera = (int) Math.round(tr.getNotaMedia()/2);
											
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
													if (tr.getNumVisualizaciones()==0){
												%>
												<div class="offer2_reviews_content">
													<div class="offer2_reviews_subtitle">Sin visualizaciones</div>
												</div>
												<%
													}else if (tr.getNumVisualizaciones()==1){
												%>
												<div class="offer2_reviews_content">
													<div class="offer2_reviews_subtitle"><%=tr.getNumVisualizaciones() %> visualización</div>
												</div>
												<%
													}else if (tr.getNumVisualizaciones()>=2){
												%>
												<div class="offer2_reviews_content">
													<div class="offer2_reviews_subtitle"><%=tr.getNumVisualizaciones() %> visualizaciones</div>
												</div>
												<%
													}
												%>
												<%						
													if (usuario != null) {
												%>
													<div class="offer2_reviews_rating text-center"><%=tr.getNotaMedia()%></div>
												<%
													}
												%>																								
											</div>
										</div>
									</div>
								</div>
							</div>
								<div class="row">
									<div class="resultado_foto2">
										<div class="offers2_image_container">
											<div class="offers2_image_background" style="background-image:url(<%=context%>/images/proveedorportada.jpg)" alt="Foto Trabajo"></div>
											<div class="offer2_name"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_TRABAJO%>&<%=ParameterNames.TRABAJO_REALIZADO_ID%>=<%=tr.getIdTrabajoRealizado()%>">Ver Detalle</a></div>									
										</div>
									</div>
									<div class="col-lg-8 resultado_datos">
										<div class="offers2_content">
											<div class="offers2_price"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_TRABAJO%>&<%=ParameterNames.TRABAJO_REALIZADO_ID%>=<%=tr.getIdTrabajoRealizado()%>"><%=tr.getTitulo()%></a><span></span></div>
											<div class="rating_r rating_r_3 offers2_rating" data-rating="5">																					
											<p class="offers2_text"><%=tr.getDescripcion()%></p>											
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
									
									
									// Última
									if (currentPage<totalPages) {
										%>
											<li><a href="<%=baseURL+ParameterNames.PAGE+"="+(totalPages)%>">Última</a></li>
										<%
								}
									
									%>
							</ul>
						</div>
					</div>
				</div>		
			</div>
		</div>
		<script>
			$(document).ready(cargarProvincias());
			$(document).ready(cargarEspecializaciones());
		</script>
<!-- Sección Central End-->
<%@include file="/common/footer.jsp"%>