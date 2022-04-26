<!-- Perfil Menu Start-->
			<div class="container">
				<div class="menuusuario">
					<div><a href="<%=context+ViewNames.USUARIO_MIS_PROVEEDORES%>">Mis Proveedores</a></div>
					<div><a href="<%=context+ViewNames.USUARIO_MIS_PROYECTOS%>">Mis Proyectos</a></div>
					<%
					
					if (usuario.getIdTipoUsuario()==TipoUsuario.USUARIO_PROVEEDOR) {
					
					%>
					<div><a href="<%=context+ViewNames.USUARIO_MIS_TRABAJOS%>">Mis Trabajos</a></div>
					<div><a href="<%=context+ViewNames.USUARIO_MIS_PRESUPUESTOS%>">Mis Presupuestos</a></div>
					
					<% 
					}
					%>
				</div>
<!-- Perfil Menu End-->