<%@include file="/common/header.jsp"%>
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
										<div  class="caja-descripcion" id="caja-focus-descripcion" onmousedown="setFlag()" onmouseup="doProcessing()">
											<div class="search3_item">
												<div>Descripción</div>
												<input type="text" name="<%=ParameterNames.BUSCAR_DESCRIPCION%>" id="descripcion-trabajo" placeholder="¿Qué estás buscando?" class="destination search3_input"
												onkeyup="buscarTrabajos()" onclick="muestraDescripcion()" onblur="ocultaDescripcion()"/>
											</div>													
											<div id="proveedores-results" class="cuadro-proveedores-results" >
												<ul id="lista-results">
												</ul>
											</div>
										</div>
										<div class="search3_item">
											<div>Especializacion</div>
											<select name="<%=ParameterNames.ID_ESPECIALIZACION%>" id="especializacion-select" class="dropdown_item_select search3_input">
												-	<option disabled selected>Selecciona una especialización</option>
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
			<%
			Results<TrabajoRealizadoDTO> results = (Results<TrabajoRealizadoDTO>) request.getAttribute(AttributeNames.TRABAJO);
			List<TrabajoRealizadoDTO> trabajos = results.getData();
			
			for (TrabajoRealizadoDTO tr : trabajos) {

			%>
				<!-- Single Listing -->
			<div class="col-lg-7 col-md-12 col-sm-12 caja_trabajo_detail">
				<div class="row">
					<div class="col-lg-12">
						<div class="single_listing">								
								<!-- Hotel Info -->
							<div class="hotel_info">
									<!-- Title -->
								<div class="hotel_title_container d-flex flex-lg-row flex-column">
									<div class="hotel_title_content">
										<h1 class="hotel_title"><%=tr.getTitulo()%><span class="username_trabajo_detail"> (<%=tr.getNombrePerfilUsuarioCreadorTrabajo()%>)</span></h1>
										<%						
										if (usuario != null) {
											if(tr.getNotaValoracion()==null) {

											} else {
																					
										%>
										<div class="proveedor_detail_estrellas">
											<div class="rating_r rating_r_<%=tr.getNotaValoracion()%> offers2_rating" data-rating="<%=tr.getNotaValoracion()%>">
												<i></i>
												<i></i>
												<i></i>
												<i></i>
												<i></i>
											</div>
										</div>								
										<%	
											}
										}
										%>
										<div class="hotel_location"><%=tr.getNombrePoblacion()%> (<%=tr.getNombreProvincia()%>)</div>
									</div>
									<div class="hotel_title_button ml-lg-auto text-lg-right">
										<div class="button book_button trans_200"><a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_USUARIO%>&<%=ParameterNames.ID_USUARIO%>=<%=tr.getIdUsuarioCreadorTrabajo()%>">Ir al Proveedor<span></span><span></span><span></span></a></div>
									</div>
								</div>
									<!-- Listing Image -->
								<div class="hotel_image">
									<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-1.jpg" alt="Foto Principal Trabajo Realizado">
								</div>								
									<!-- Hotel Gallery -->
								<div class="hotel_gallery">
									<div class="hotel_slider_container">
										<div class="owl-carousel owl-theme hotel_slider">
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-1.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-1.jpg" alt="Foto Trabajo Realizado 1">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-2.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-2.jpg" alt="Foto Trabajo Realizado 2">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-3.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-3.jpg" alt="Foto Trabajo Realizado 3">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-4.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-4.jpg" alt="Foto Trabajo Realizado 4">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-5.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-5.jpg" alt="Foto Trabajo Realizado 5">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-6.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-6.jpg" alt="Foto Trabajo Realizado 6">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-7.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-7.jpg" alt="Foto Trabajo Realizado 7">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-8.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-8.jpg" alt="Foto Trabajo Realizado 8">
												</a>
											</div>
												<!-- Hotel Gallery Slider Item -->
											<div class="owl-item">
												<a class="colorbox cboxElement" href="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-9.jpg">
													<img src="<%=context+ConstantWebUtil.WEB_TRABAJO_PATH+tr.getIdTrabajoRealizado()%>/id-9.jpg" alt="Foto Trabajo Realizado 9">
												</a>
											</div>
										</div>
											<!-- Hotel Slider Nav - Prev -->
										<div class="hotel_slider_nav hotel_slider_prev">
											<svg version="1.1" id="Layer_6" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
												width="28px" height="33px" viewBox="0 0 28 33" enable-background="new 0 0 28 33" xml:space="preserve">
												<defs>
													<linearGradient id='hotel_grad_prev'>
														<stop offset='0%' stop-color='#28395a'/>
														<stop offset='100%' stop-color='#966	A08'/>
													</linearGradient>
												</defs>
												<path class="nav_path" fill="#F3F6F9" d="M19,0H9C4.029,0,0,4.029,0,9v15c0,4.971,4.029,9,9,9h10c4.97,0,9-4.029,9-9V9C28,4.029,23.97,0,19,0z
												M26,23.091C26,27.459,22.545,31,18.285,31H9.714C5.454,31,2,27.459,2,23.091V9.909C2,5.541,5.454,2,9.714,2h8.571
												C22.545,2,26,5.541,26,9.909V23.091z"/>
												<polygon class="nav_arrow" fill="#F3F6F9" points="15.044,22.222 16.377,20.888 12.374,16.885 16.377,12.882 15.044,11.55 9.708,16.885 11.04,18.219 
												11.042,18.219 "/>
											</svg>
										</div>											
											<!-- Hotel Slider Nav - Next -->
										<div class="hotel_slider_nav hotel_slider_next">
											<svg version="1.1" id="Layer_7" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
											width="28px" height="33px" viewBox="0 0 28 33" enable-background="new 0 0 28 33" xml:space="preserve">
												<defs>
													<linearGradient id='hotel_grad_next'>
														<stop offset='0%' stop-color='#28395a'/>
														<stop offset='100%' stop-color='#966A08'/>
													</linearGradient>
												</defs>
											<path class="nav_path" fill="#F3F6F9" d="M19,0H9C4.029,0,0,4.029,0,9v15c0,4.971,4.029,9,9,9h10c4.97,0,9-4.029,9-9V9C28,4.029,23.97,0,19,0z
											M26,23.091C26,27.459,22.545,31,18.285,31H9.714C5.454,31,2,27.459,2,23.091V9.909C2,5.541,5.454,2,9.714,2h8.571
											C22.545,2,26,5.541,26,9.909V23.091z"/>
											<polygon class="nav_arrow" fill="#F3F6F9" points="13.044,11.551 11.71,12.885 15.714,16.888 11.71,20.891 13.044,22.224 18.379,16.888 17.048,15.554 
											17.046,15.554 "/>
											</svg>
										</div>
									</div>
								</div>
									<!-- Hotel Info Text -->
								<div class="hotel_info_text">
									<p><%=tr.getDescripcion()%></p>
								</div>
									<!-- Hotel Info Tags -->
							</div>			
							<%	
							//Lios de usuarios: usuario = usuario en sesion(Logueado)
								if (usuario != null) {
							
									Results<ValoracionDTO> resultsValoraciones = (Results<ValoracionDTO>) request.getAttribute(AttributeNames.VALORACION);
									List<ValoracionDTO> valoraciones = resultsValoraciones.getData();
									
									for (ValoracionDTO v : valoraciones) {	
									
							%>													
								<!-- Reviews -->
							<div class="reviews">
								<div class="reviews_title"></div>
								<div class="reviews_container">
										<!-- Review -->
									<div class="review">
										<div class="row">
											<div class="col-lg-1">
												<div class="review_image">
													<img src="<%=context%>/images/valoraciondetalle.jpg" alt="Foto Perfil Usuario Creador Valoracion">
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
	</div>
			<script>
				$(document).ready(cargarProvincias());
				$(document).ready(cargarEspecializaciones());
			</script>
<!-- Detalle Trabajo End -->
<%@include file="/common/footer.jsp"%>