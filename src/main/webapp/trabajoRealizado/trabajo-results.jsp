<%@include file="/common/header.jsp"%>
<!-- Secci?n Central Start-->
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
												<div class="search3_item" onmousedown="setFlag()" onmouseup="doProcessing()">
													<div>Descripci?n</div>
													<input type="text" name="<%=ParameterNames.BUSCAR_DESCRIPCION%>" id="descripcion-trabajo" placeholder="?Qu? est?s buscando?" class="destination search3_input"
													onkeyup="buscarTrabajos()" onclick="muestraDescripcion()" onblur="ocultaDescripcion()"/>
												</div>													
												<div id="proveedores-results" class="cuadro-proveedores-results" onmousedown="setFlag()" onmouseup="doProcessing()">
													<ul id="lista-results">
													</ul>
												</div>
												<div class="search3_item">
													<div>Especializacion</div>
													<select name="<%=ParameterNames.ID_ESPECIALIZACION%>" id="especializacion-select" class="dropdown_item_select search3_input">
														<option disabled selected>Selecciona una especializaci?n</option>
													</select>
												</div>
												<div class="search3_item">
													<div>Provincia</div>
													<select name="<%=ParameterNames.ID_PROVINCIA%>" id="provincia-select" class="dropdown_item_select search3_input">
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
						<div class="ordenar_por">
							<div class="offers2_sorting_container">
								<ul class="offers2_sorting" >
									<select name="<%=ParameterNames.ORDER_BY%>" id="orderby-select" class="dropdown_item_select search3_input"
									onchange="this.form.submit()">
										<option disabled selected>Ordenar Por</option>
										<option value ="NV">Numero de visualizaciones</option>
										<option value ="FC">Fecha de Creacion</option>
										<option value ="VAL">Valoracion Media</option>
										<i class="fa fa-chevron-down"></i>
									</select>
								</ul>
							</div>
						</div>
						<input type="hidden" name="<%=ParameterNames.ID_USUARIO_CREADOR_TRABAJO%>" value="<%=request.getParameter(ParameterNames.ID_USUARIO_CREADOR_TRABAJO)%>"/>
						</form>
						<%
						Results<TrabajoRealizadoDTO> results = (Results<TrabajoRealizadoDTO>) request.getAttribute(AttributeNames.TRABAJO);
						List<TrabajoRealizadoDTO> trabajos = results.getData();
						if (trabajos.size()>0) {
						%>
						<div class="col-lg-11 trabajo_resultados">							
							<%
							for (TrabajoRealizadoDTO tr : trabajos) {
							%>
								<!-- Trabajo Result -->
							<div class="offers2_item rating_3">
							<div class="col-lg-12 valoracion_visualizacion2">
								<div class="offers2_item rating_5">	
									<div class="offers2_content">
										<%						
											if (usuario != null) {
												if(tr.getNotaValoracion()!=null) {
																						
												%>
											<div class="rating_r rating_r_<%=tr.getNotaValoracion()%> offers2_rating" data-rating="<%=tr.getNotaValoracion()%>">
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
													<div class="offer2_reviews_subtitle"><%=tr.getNumVisualizaciones() %> visualizaci?n</div>
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
														if(tr.getNotaValoracion()!=null) {
												%>
												<div class="offer2_reviews_rating text-center"><%=tr.getNotaValoracion()%></div>
												<%
														} else {
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
								<div class="row">
									<div class="resultado_foto2">
										<div class="offers2_image_container">
											<a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_TRABAJO%>&<%=ParameterNames.ID_TRABAJO_REALIZADO%>=<%=tr.getIdTrabajoRealizado()%>">
											<div class="offers2_image_background" style="background-image:url(<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-1.jpg)" alt="Foto Principal Trabajo"></div></a>
											<div class="offer2_name"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_TRABAJO%>&<%=ParameterNames.ID_TRABAJO_REALIZADO%>=<%=tr.getIdTrabajoRealizado()%>">Ver Detalle</a></div>									
										</div>
									</div>
									<div class="col-lg-8 resultado_datos">
										<div class="offers2_content">
											<div class="offers2_price"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_TRABAJO%>&<%=ParameterNames.ID_TRABAJO_REALIZADO%>=<%=tr.getIdTrabajoRealizado()%>"><%=tr.getTitulo()%></a><span></span></div>
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
									String baseURL = ParameterUtils.getURLPaginacion(request.getContextPath()+ControllerNames.TRABAJO_REALIZADO, parameters);
	
									
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
									
									
									// ?ltima
									if (currentPage<totalPages) {
										%>
											<li><a href="<%=baseURL+ParameterNames.PAGE+"="+(totalPages)%>">?ltima</a></li>
										<%
								}
									
									%>
							</ul>
						</div>
					</div>
					<% 
					} else {					
					%>
					<div class="caja-sin-resultados">
						<p class="sin-resultados">No se han encontrado resultados</p>
					</div>
					<%
						}
					%>
				</div>		
			</div>
		</div>
		<script>
		
		
			$(document).ready(cargarProvincias(<%=request.getParameter(ParameterNames.ID_PROVINCIA)%>));
			$(document).ready(cargarEspecializaciones(<%=request.getParameter(ParameterNames.ID_ESPECIALIZACION)%>));
		</script>
<!-- Secci?n Central End-->
<%@include file="/common/footer.jsp"%>