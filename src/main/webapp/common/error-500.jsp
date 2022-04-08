<%@include file="/common/header-error.jsp"%>
    <title>500 Internal server error</title>       
    </head>
    <body>
        <div class="errorBloque">
            <div class="errorFatal">
                <div class="errorFatalImagen">
                    <a href="<%=context%>/index.jsp"><img src="<%=context%>/images/logo/logoreformatec.png" alt="Logo Reformatec"></a>
                </div>
                <div class="errorFatalTexto">
                    <p>Vaya vaya... Parece que que ha ocurrido algun problema...</p>
                </div>
                <div class="errorFatalLink">
                    <a href="<%=context%>/index.jsp"><p>Vuelve a la pagina de inicio</p></a>
                </div>
            </div>
        </div>
        <script>
        $('[data-toggle="tooltip"]').tooltip();
        $(".preloader").fadeOut();
        </script>
	</body>
</html>      