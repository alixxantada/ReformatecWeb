<%@include file="/common/header.jsp"%>
<%@include file="/common/menu-usuario.jsp"%>
<!--  Mis proveedores favoritos Start -->

	<div class="col-lg-11 usuario_resultados">
	<h1 class="mis-favoritos"> Mis Favoritos </h1>
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
		
								int valoracionMediaEntera = (int) Math.round(u.getValoracionMedia());
						
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
								<div class="offer2_reviews_subtitle"><%=u.getNumeroVisualizaciones() %> visualización</div>
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
							<div class="offer2_reviews_subtitle"> 1 valoración</div>
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
			<a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_USUARIO%>&<%=ParameterNames.ID_USUARIO%>=<%=u.getIdUsuario()%>"><%=u.getNombrePerfil() %></a><span></span>
			<%
			Set<Long> idsFavoritos = (Set<Long>) SessionManager.get(request, AttributeNames.FAVORITOS);
			if(!idsFavoritos.contains(u.getIdUsuario())){

			%>
			<a href="<%=context+ControllerNames.PRIVADO_USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.ANHADIR_FAVORITO%>&<%=ParameterNames.ID_PROVEEDOR_FAVORITO%>=<%=u.getIdUsuario()%>"><img src="<%=context%>/images/heart.png" alt="Icono Corazon Vacio"></a>
			<%
			} else {
			%>										
			<a href="<%=context+ControllerNames.PRIVADO_USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.ELIMINAR_FAVORITO%>&<%=ParameterNames.ID_PROVEEDOR_FAVORITO%>=<%=u.getIdUsuario()%>"><img src="<%=context%>/images/heart2.png" alt="Icono Corazon Lleno"></a>									
			<%
			}			
			%>
		</div>
		<div class="row">
			<div class="resultado_foto">
				<div class="offers2_image_container">
					<div class="offers2_image_background" style="background-image:url(<%=context+ConstantWebUtil.WEB_USER_PATH+u.getIdUsuario()%>/perfil.jpg)" alt="Foto portada proveedor"></div>
					<div class="offer_name"><a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_USUARIO%>&<%=ParameterNames.ID_USUARIO%>=<%=u.getIdUsuario()%>"><%=u.getNombrePoblacion()%></a></div>
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
				String baseURL = ParameterUtils.getURLPaginacion(request.getContextPath()+ControllerNames.PRIVADO_USUARIO, parameters);

				
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



<!--  Mis proveedores favoritos End -->
<%@include file="/common/footer.jsp"%>