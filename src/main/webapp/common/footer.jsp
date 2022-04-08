<!-- Footer Start -->
			<footer class="footer">
				<div class="container">
					<div class="row">
						<!-- Footer Column -->
						<div class="col-lg-4 footer_column">
							<div class="footer_col">
								<div class="footer_content footer_about">
										<div class="logofooter"><a href="<%=context+ViewNames.HOME%>"><img src="/ReformatecWeb/images/logo/logoreformatecjusto.png" alt="Logo Reformatec"></a></div>
									<p class="footer_about_text">Siguenos también en</p>
									<ul class="lista_footer">
										<li class="footer_social_item"><a href="#"><i class="fa fa-facebook-f"></i></a></li>
										<li class="footer_social_item"><a href="#"><i class="fa fa-twitter"></i></a></li>
										<li class="footer_social_item"><a href="#"><i class="fa fa-linkedin"></i></a></li>
									</ul>
								</div>
							</div>
						</div>
							<!-- Footer Column -->
						<div class="col-lg-4 footer_column">
							<div class="footer_col">
								<div class="footer_title">Áreas Destacadas</div>
								<div class="footer_content footer_tags">
									<ul class="footerlink">
										<li class="footerlinks"><a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_USUARIO%>">Proveedores</a></li>
										<li class="footerlinks"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_TRABAJO%>">Trabajos</a></li>
										<li class="footerlinks"><a href="<%=context+ViewNames.ABOUT%>">Sobre Nosotros</a></li>
									</ul>
								</div>
							</div>
						</div>
							<!-- Footer Column -->
						<div class="col-lg-4 footer_column">
							<div class="footer_col">
								<a href="/ReformatecWeb<%=ViewNames.CONTACT%>"><div class="footer_titulo_enlace">Contacto</div></a>
								<div class="footer_content footer_contact">
									<ul class="contact_info_list">
										<li class="contact_info_item d-flex flex-row">
											<div><div class="contact_info_icon"><img src="<%=context%>/images/placeholder.svg" alt=""></div></div>
											<div class="contact_info_text">Avda Monforte S/N 27500 Chantada(Lugo).</div>
										</li>
										<li class="contact_info_item d-flex flex-row">
											<div><div class="contact_info_icon"><img src="<%=context%>/images/phone-call.svg" alt=""></div></div>
											<div class="contact_info_text"><a href="tel:0034670884461">+34 670 884 461</a></div>
										</li>
										<li class="contact_info_item d-flex flex-row">
											<div><div class="contact_info_icon"><img src="<%=context%>/images/message.svg" alt=""></div></div>
											<div class="contact_info_text"><a href="mailto:pruebafp@gmail.com?Subject=Hello" target="_top">pruebafp@gmail.com</a></div>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</footer>
                <!-- Footer End -->
		</div>
	</body>
</html>