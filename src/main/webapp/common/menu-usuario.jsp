<!-- Perfil Menu Start-->
			<div class="container">
				<div class="menuusuario">
					<div><a href="<%=context+ControllerNames.PRIVADO_USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.MIS_FAVORITOS%>&<%=ParameterNames.ID_USUARIO%>=<%=usuario.getIdUsuario()%>">Mis Favoritos</a></div>
					<div><a href="<%=context+ControllerNames.PRIVADO_PROYECTO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.MIS_PROYECTOS%>&<%=ParameterNames.ID_USUARIO%>=<%=usuario.getIdUsuario()%>">Mis Proyectos</a></div>
					<%
					
					if (usuario.getIdTipoUsuario()==TipoUsuario.USUARIO_PROVEEDOR) {
					
					%>
					<div><a href="<%=context+ControllerNames.PRIVADO_TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.MIS_TRABAJOS%>&<%=ParameterNames.ID_USUARIO%>=<%=usuario.getIdUsuario()%>">Mis Trabajos</a></div>
					<div><a href="<%=context+ControllerNames.PRIVADO_PRESUPUESTO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.MIS_TRABAJOS%>&<%=ParameterNames.ID_USUARIO%>=<%=usuario.getIdUsuario()%>">Mis Presupuestos</a></div>
					<% 
					}
					%>
				</div>
<!-- Perfil Menu End-->