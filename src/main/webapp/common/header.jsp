<%@ page import="com.alejandro.reformatec.web.util.*,com.alejandro.reformatec.web.controller.*,java.util.*,java.text.*,com.alejandro.reformatec.model.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
		String context = request.getContextPath();
%>
<!DOCTYPE html> 
<html lang="en">
<head>
<title>Reformatec</title>
<meta charset="ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="Header">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="keywords" content="Reformatec">
<meta name="author" content="Alejandro Calvo">
<meta name="copyrigth" content="Propietario del copyright">

<link rel="stylesheet" type="text/css" href="<%=context%>/styles/bootstrap4/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/plugins/colorbox/colorbox.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/plugins/OwlCarousel2-2.2.1/owl.carousel.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/plugins/OwlCarousel2-2.2.1/owl.theme.default.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/plugins/OwlCarousel2-2.2.1/animate.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/styles/common_styles.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/styles/reformatec_styles.css">
<link rel="stylesheet" type="text/css" href="<%=context%>/styles/reformatec_responsive.css">

<script src="<%=context%>/js/jquery-3.2.1.min.js"></script>
<script src="<%=context%>/styles/bootstrap4/popper.js"></script>
<script src="<%=context%>/styles/bootstrap4/bootstrap.min.js"></script>
<script src="<%=context%>/plugins/OwlCarousel2-2.2.1/owl.carousel.js"></script>
<script src="<%=context%>/plugins/colorbox/jquery.colorbox-min.js"></script>
<script src="<%=context%>/plugins/Isotope/isotope.pkgd.min.js"></script>
<script src="<%=context%>/js/reformatec_customized.js"></script> 
<script src="<%=context%>/js/reformatec_ajax.js"></script>


<link rel="shortcut icon" type="image/x-icon" href="<%=context%>/images/iconito.png">
<link rel="icon" type="image/x-icon" href="<%=context%>/images/iconito.png">
</head>
<body>
	<div class="super_container">
		<!-- Header Start-->
		<header class="header">
			<!-- Top Bar -->
			<div class="top_bar">
				<div class="container">
					<div class="row">
						<div class="col d-flex flex-row">
							<div class="siguenos">También en:</div>
							<div class="social">
								<ul class="social_list">
									<li class="social_list_item"><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
									<li class="social_list_item"><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
									<li class="social_list_item"><a href="#"><i class="fa fa-linkedin" aria-hidden="true"></i></a></li>
								</ul>
							</div>
							<%
							UsuarioDTO usuario = (UsuarioDTO) SessionManager.get(request, AttributeNames.USUARIO);
							if (usuario == null) {
							%>
							<div class="user_box ml-auto">
								<div class="user_box_login user_box_link">
									<a href="<%=context+ViewNames.USUARIO_LOGIN%>">Iniciar Sesión</a>
								</div>
								<div class="user_box_register user_box_link">
									<a href="<%=context+ViewNames.USUARIO_REGISTRO%>">Regístrate</a>
								</div>
							</div>
							<%
							} else {
							%>
							<div class="user_box ml-auto">
								<div class="user_box_login user_box_link">
									<a href="<%=context+ControllerNames.PRIVADO_USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.LOGOUT%>">Cerrar Sesión</a>
								</div>
								<div class="user_box_register user_box_link">
									<a href="<%=context+ViewNames.USUARIO_PERFIL%>">Mi Cuenta</a>
								</div>
							</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
			</div>
			<!-- Main Navigation -->
			<nav class="main_nav">
				<div class="container">
					<div class="row">
						<div
							class="col main_nav_col d-flex flex-row align-items-center justify-content-start">
							<div class="logo_container">
								<div class="logo">
									<a href="<%=context+ViewNames.HOME%>"><img src="<%=context%>/images/iconito.png" alt="Logo Reformatec">Reformatec</a>
								</div>
							</div>
							<div class="main_nav_container ml-auto">
								<ul class="main_nav_list">
									<li class="main_nav_item"><a href="<%=context+ViewNames.HOME%>">Inicio</a></li>
									<li class="main_nav_item"><a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_USUARIO%>">Proveedores</a></li>
									<li class="main_nav_item"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_TRABAJO%>">Trabajos</a></li>
									<li class="main_nav_item"><a href="<%=context+ViewNames.ABOUT%>">Sobre Nosotros</a></li>
									<li class="main_nav_item"><a href="<%=context+ViewNames.CONTACT%>">Contacto</a></li>
								</ul>
							</div>							
							<div class="hamburger ml-auto">
								<i class="fa fa-bars trans_200"></i>
							</div>
						</div>
					</div>
				</div>
			</nav>
		</header>
		<div class="menu trans_500">
			<div class="menu_content d-flex flex-column align-items-center justify-content-center text-center">
				<div class="menu_close_container">
					<div class="menu_close"></div>
				</div>
				<div class="logo_movil_sesion">
					<a href="<%=context+ViewNames.HOME%>"><img src="<%=context%>/images/logo/logoreformatecrecort.png" alt="Logo Reformatec"></a>
				</div>
				<ul>
					<li class="main_nav_item2"><a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_USUARIO%>">Proveedores</a></li>
					<li class="main_nav_item2"><a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_TRABAJO%>">Trabajos</a></li>
					<li class="main_nav_item2"><a href="<%=context+ViewNames.ABOUT%>">Sobre Nosotros</a></li>
					<li class="main_nav_item2"><a href="<%=context+ViewNames.CONTACT%>">Contacto</a></li>
				</ul>
			</div>
		</div>
		<div class="home">
			<div class="home_slider_container">
				<div class="owl-carousel owl-theme home_slider">
					<!-- Slider Item -->
					<div class="owl-item home_slider_item">
						<div class="home_slider_background" style="background-image: url(<%=context%>/images/home_slider1.jpg)"></div>
						<div class="home_slider_content text-center">
							<div class="home_slider_content_inner" data-animation-in="flipInX" data-animation-out="animate-out fadeOut">
								<h1>¿Qué necesitas?</h1>
								<div class="button home_slider_button">
									<div class="button_bcg"></div>
									<a href="<%=context+ControllerNames.USUARIO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_USUARIO%>">Búsqueda avanzada<span></span><span></span><span></span>
									</a>
								</div>
							</div>
						</div>
					</div>
					<!-- Slider Item -->
					<div class="owl-item home_slider_item">
						<div class="home_slider_background" style="background-image: url(<%=context%>/images/home_slider2.jpg)"></div>
						<div class="home_slider_content text-center">
							<div class="home_slider_content_inner" data-animation-in="flipInX" data-animation-out="animate-out fadeOut">
								<h1>Inspírate</h1>
								<div class="button home_slider_button">
									<div class="button_bcg"></div>
									<a href="<%=context+ControllerNames.TRABAJO_REALIZADO%>?<%=ParameterNames.ACTION%>=<%=ActionNames.SEARCH_TRABAJO%>">Descubrir trabajos<span></span><span></span><span></span>
									</a>
								</div>
							</div>
						</div>
					</div>
					<!-- Slider Item -->
					<div class="owl-item home_slider_item">
						<div class="home_slider_background" style="background-image: url(<%=context%>/images/home_slider3.jpg)"></div>
						<div class="home_slider_content text-center">
							<div class="home_slider_content_inner" data-animation-in="flipInX" data-animation-out="animate-out fadeOut">
								<h1>Reformatec</h1>
								<div class="button home_slider_button">
									<div class="button_bcg"></div>
									<a href="<%=context+ViewNames.ABOUT%>">Más informacion<span></span><span></span><span></span> </a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Home Slider Nav - Prev -->
				<div class="home_slider_nav home_slider_prev">
					<svg version="1.1" id="Layer_2" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" width="28px" height="33px" viewBox="0 0 28 33" enable-background="new 0 0 28 33" xml:space="preserve">
							<defs>
								<linearGradient id='home_grad_prev'>
									<stop offset='0%' stop-color='#28395a' />
									<stop offset='100%' stop-color='#966A08' />
								</linearGradient>
							</defs>
							<path class="nav_path" fill="#F3F6F9"
							d="M19,0H9C4.029,0,0,4.029,0,9v15c0,4.971,4.029,9,9,9h10c4.97,0,9-4.029,9-9V9C28,4.029,23.97,0,19,0z
							M26,23.091C26,27.459,22.545,31,18.285,31H9.714C5.454,31,2,27.459,2,23.091V9.909C2,5.541,5.454,2,9.714,2h8.571
							C22.545,2,26,5.541,26,9.909V23.091z" />
							<polygon class="nav_arrow" fill="#F3F6F9"
							points="15.044,22.222 16.377,20.888 12.374,16.885 16.377,12.882 15.044,11.55 9.708,16.885 11.04,18.219 
							11.042,18.219 " />
						</svg>
				</div>
				<!-- Home Slider Nav - Next -->
				<div class="home_slider_nav home_slider_next">
					<svg version="1.1" id="Layer_3" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" 	width="28px" height="33px" viewBox="0 0 28 33" enable-background="new 0 0 28 33" xml:space="preserve">
							<defs>
								<linearGradient id='home_grad_next'>
									<stop offset='0%' stop-color='#28395a' />
									<stop offset='100%' stop-color='#966A08' />
								</linearGradient>
							</defs>
						<path class="nav_path" fill="#F3F6F9"
							d="M19,0H9C4.029,0,0,4.029,0,9v15c0,4.971,4.029,9,9,9h10c4.97,0,9-4.029,9-9V9C28,4.029,23.97,0,19,0z
						M26,23.091C26,27.459,22.545,31,18.285,31H9.714C5.454,31,2,27.459,2,23.091V9.909C2,5.541,5.454,2,9.714,2h8.571
						C22.545,2,26,5.541,26,9.909V23.091z" />
						<polygon class="nav_arrow" fill="#F3F6F9"
							points="13.044,11.551 11.71,12.885 15.714,16.888 11.71,20.891 13.044,22.224 18.379,16.888 17.048,15.554 
						17.046,15.554 " />
						</svg>
				</div>
			</div>
		</div>
		<!-- Header End-->