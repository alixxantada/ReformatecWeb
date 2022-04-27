<%@include file="/common/styles.jsp"%>


<!-- Proveedores Top Start -->
<div class="offers">
	<div class="container">
		<div class="row">
			<div class="col text-center">
				<h2 class="section_title">Proveedores TOP</h2>
			</div>
		</div>
		<div class="row offers_items">
			<%
			
			//lio de usuarios usuarios es la lista de usuarios resultados
			Results<UsuarioDTO> results = (Results<UsuarioDTO>) request.getAttribute(AttributeNames.USUARIO);
			List<UsuarioDTO> users = results.getData();
			
			for (UsuarioDTO u : users) {
			
			%>
			<!-- Offers Item -->
			<div class="col-lg-6 offers_col">
				<div class="offers_item">
					<div class="row">
						<div class="col-lg-6">
							<div class="offers_image_container">
								<div class="offers_image_background"
									style="background-image:url(<%=context+ConstantWebUtil.WEB_USER_PATH+u.getIdUsuario()%>/perfil.jpg)"></div>
								<div class="offer_name">
									<a href="<%=context+ViewNames.USUARIO_DETAIL%>"><%=u.getNombrePoblacion() %></a>
								</div>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="offers_price">
								<%=u.getNombrePerfil()%></br>
							</div>
							<div class="offers2_item rating_5">
								<div class="offers_content">									
									<%						
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
									<p class="offers_text"><%=u.getDescripcion()%></p>
									<div class="offers_icons">
										<ul class="offers_icons_list">
											<%if (u.getServicio24()==true){ %><li class="offers2_icons_item"><img src="<%=context%>/images/24h.png" alt=""></li><% } %>
											<%if (u.getProveedorVerificado()==true){ %><li class="offers2_icons_item"><img src="<%=context%>/images/ruler.png" alt=""></li><% } %>
										</ul>
									</div>
									<div class="offers_link">
										<a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.DETAIL_USUARIO%>&<%=ParameterNames.ID_USUARIO%>=<%=u.getIdUsuario()%>"">Ver Perfil</a>
									</div>
								</div>
							</div>							
						</div>
					</div>
				</div>
			</div>
			<%
			}
			%>
		</div>
	</div>
</div>
