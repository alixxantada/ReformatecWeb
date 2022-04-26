<%@include file="/common/header.jsp"%>

<!-- Buscador Start-->
			<div class="search">
				<!-- Search Contents -->
				<div class="container fill_height">
					<div class="row fill_height">
						<div class="col fill_height">
							<!-- Search Tabs -->
							<div class="search_tabs_container">
								<div class="search_tabs d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-start">
									<div class="search_tab active d-flex flex-row align-items-center justify-content-lg-center justify-content-start"><img src="<%=context%>/images/profesionalmenu.png" alt=""><span>Proveedores</span></div>
									<div class="search_tab d-flex flex-row align-items-center justify-content-lg-center justify-content-start"><img src="<%=context%>/images/trabajomenu.png" alt="Imagen trabajos realizados">Trabajos</div>
								</div>		
							</div>
							<!-- Search Panel -->
							<div class="search_panel active">
								<form action="<%=context+ControllerNames.USUARIO%>" id="proveedor" autocomplete="off" class="search_panel_content d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-start">
									<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.SEARCH_USUARIO%>"/>
									<div class="search_item">
										<div>Búsqueda</div>
										<input type="text" name="<%=ParameterNames.BUSCAR_DESCRIPCION%>" id="descripcion-proveedor"class="destination search_input" placeholder="¿Qué necesitas?"
											onkeyup="buscarProveedores()"/>
									</div>
									<div class="search_item">
										<div>Especializacion</div>
										<select name="<%=ParameterNames.ID_ESPECIALIZACION%>" id="especializacion-select" class="dropdown_item_select search_input">
											<option disabled selected>Selecciona una especialización</option>
										</select>
									</div>
									<div class="search_item">
										<div>Provincia</div>
										<select name="<%=ParameterNames.ID_PROVINCIA%>" id="provincia-select" class="dropdown_item_select search_input">
											<option disabled selected>Selecciona una provincia</option>
										</select>
									</div>
									<button class="button search_button">Buscar<span></span><span></span><span></span></button>
								</form>
							</div>							
							<!-- Search Panel -->
							<div class="search_panel">
								<form action="<%=context+ControllerNames.TRABAJO_REALIZADO%>" autocomplete="off"  id="trabajo" class="search_panel_content d-flex flex-lg-row flex-column align-items-lg-center align-items-start justify-content-lg-between justify-content-start">
									<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.SEARCH_TRABAJO%>"/>
									<div class="search_item">
										<div>Descripción</div>
										<input type="text" name="<%=ParameterNames.BUSCAR_DESCRIPCION%>" id="descripcion-trabajo" class="destination search_input" placeholder="¿Qué quieres ver?"
										onkeyup="buscarTrabajos()"/>
									</div>
									<div class="search_item">
										<div>Especializacion</div>
										<select name="<%=ParameterNames.ID_ESPECIALIZACION%>" id="especializacion2-select" class="dropdown_item_select search_input">
											<option disabled selected>Selecciona una especialización</option>
										</select>
									</div>
									<div class="search_item">
										<div>Provincia</div>
										<select name="<%=ParameterNames.ID_PROVINCIA%>" id="provincia2-select" class="dropdown_item_select search_input">
											<option disabled selected>Selecciona una provincia</option>
										</select>
									</div>
									<button class="button search_button">Buscar<span></span><span></span><span></span></button>
								</form>
							</div>
							<div id="proveedores-results" class="cuadro-proveedores-results">
							</div>
						</div>
					</div>
				</div>		
			</div>
				<!-- Buscador End-->
				<!-- Proveedores Top Start -->
			<%
			//lio de usuarios, este indica si tiene sesion iniciada.
			if (usuario != null) {
			
			%>
				
			<%@include file="/common/proveedores-top.jsp"%>
			
			<%
			}
			%>
				<!-- Proveedores Top End -->			
				<!-- Valoraciones Trabajos Start -->
			<div class="testimonials">
				<div class="test_border"></div>
				<div class="container">
					<div class="row">
						<div class="col text-center">
							<h2 class="section_title">Regístrate y descubre lo que opinan nuestros clientes</h2>
						</div>
					</div>
					<div class="row">
						<div class="col">
						
						
								<!-- Valoracion Slider -->
							<div class="test_slider_container">
								<div class="owl-carousel owl-theme test_slider">
										<!-- Valoracion Item -->
									<div class="owl-item">
										<div class="test_item">
											<div class="test_image"><img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion"></div>
											<div class="test_content_container">
												<div class="test_content">
													<div class="test_item_info">
														<div class="test_name">Nombre Perfil</div>
														<div class="test_date">May 24, 2017</div>
													</div>
													<div class="test_quote_title">" Titulo Valoración "</div>
													<p class="test_quote_text">Descripcion de una valoracion ramdom con un maximo caracteres...</p>
												</div>
											</div>
										</div>
									</div>
										<!-- Valoracion Item -->
									<div class="owl-item">
										<div class="test_item">
											<div class="test_image"><img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion"></div>
											<div class="test_content_container">
												<div class="test_content">
													<div class="test_item_info">
														<div class="test_name">Nombre Perfil</div>
														<div class="test_date">May 24, 2017</div>
													</div>
													<div class="test_quote_title">" Titulo Valoración "</div>
													<p class="test_quote_text">Descripcion de una valoracion ramdom con un maximo caracteres...</p>
												</div>
											</div>
										</div>
									</div>
										<!-- Valoracion Item -->
									<div class="owl-item">
										<div class="test_item">
											<div class="test_image"><img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion"></div>
											<div class="test_content_container">
												<div class="test_content">
													<div class="test_item_info">
														<div class="test_name">Nombre Perfil</div>
														<div class="test_date">May 24, 2017</div>
													</div>
													<div class="test_quote_title">" Título Valoración "</div>
													<p class="test_quote_text">Descripcion de una valoracion ramdom con un maximo caracteres...</p>
												</div>
											</div>
										</div>
									</div>
										<!-- Valoracion Item -->
									<div class="owl-item">
										<div class="test_item">
											<div class="test_image"><img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion"></div>
											<div class="test_content_container">
												<div class="test_content">
													<div class="test_item_info">
														<div class="test_name">Nombre Perfil</div>
														<div class="test_date">May 24, 2017</div>
													</div>
													<div class="test_quote_title">" Título Valoración "</div>
													<p class="test_quote_text">Descripcion de una valoracion ramdom con un maximo caracteres...</p>
												</div>
											</div>
										</div>
									</div>
										<!-- Valoracion Item -->
									<div class="owl-item">
										<div class="test_item">
											<div class="test_image"><img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion"></div>
											<div class="test_content_container">
												<div class="test_content">
													<div class="test_item_info">
														<div class="test_name">Nombre Perfil</div>
														<div class="test_date">May 24, 2017</div>
													</div>
													<div class="test_quote_title">" Título Valoración "</div>
													<p class="test_quote_text">Descripcion de una valoracion ramdom con un maximo caracteres...</p>
												</div>
											</div>
										</div>
									</div>
										<!-- Valoracion Item -->
									<div class="owl-item">
										<div class="test_item">
											<div class="test_image"><img src="<%=context%>/images/valoracionindex.jpg" alt="Imagen Peril Usuario creador Valoracion"></div>
											<div class="test_content_container">
												<div class="test_content">
													<div class="test_item_info">
														<div class="test_name">Nombre Perfil</div>
														<div class="test_date">May 24, 2017</div>
													</div>
													<div class="test_quote_title">" Título Valoración "</div>
													<p class="test_quote_text">Descripcion de una valoracion ramdom con un maximo caracteres...</p>
												</div>
											</div>
										</div>
									</div>
								</div>
									<!-- Valoracion Slider Nav - Prev -->
								<div class="test_slider_nav test_slider_prev">
									<svg version="1.1" id="Layer_6" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
										width="28px" height="33px" viewBox="0 0 28 33" enable-background="new 0 0 28 33" xml:space="preserve">
										<defs>
											<linearGradient id='test_grad_prev'>
												<stop offset='0%' stop-color='#28395a'/>
												<stop offset='100%' stop-color='#966A08'/>
											</linearGradient>
										</defs>
										<path class="nav_path" fill="#F3F6F9" d="M19,0H9C4.029,0,0,4.029,0,9v15c0,4.971,4.029,9,9,9h10c4.97,0,9-4.029,9-9V9C28,4.029,23.97,0,19,0z
										M26,23.091C26,27.459,22.545,31,18.285,31H9.714C5.454,31,2,27.459,2,23.091V9.909C2,5.541,5.454,2,9.714,2h8.571
										C22.545,2,26,5.541,26,9.909V23.091z"/>
										<polygon class="nav_arrow" fill="#F3F6F9" points="15.044,22.222 16.377,20.888 12.374,16.885 16.377,12.882 15.044,11.55 9.708,16.885 11.04,18.219 
										11.042,18.219 "/>
									</svg>
								</div>
									<!-- Valoracion Slider Nav - Next -->
								<div class="test_slider_nav test_slider_next">
									<svg version="1.1" id="Layer_7" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
									width="28px" height="33px" viewBox="0 0 28 33" enable-background="new 0 0 28 33" xml:space="preserve">
										<defs>
											<linearGradient id='test_grad_next'>
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
					</div>
				</div>
			</div>
				<!-- Valoraciones Trabajos End -->
<script>
	$(document).ready(cargarProvincias());
	$(document).ready(cargarEspecializaciones());
	//$(document).ready(cargarUsuariosIndex());
</script>
				
<%@include file="/common/footer.jsp"%>