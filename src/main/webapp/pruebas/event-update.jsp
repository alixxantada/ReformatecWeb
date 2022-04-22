<%@include file="/common/header.jsp"%>


	<%

	EventoDTO evento = (EventoDTO) request.getAttribute(AttributeNames.EVENT);
	List<UsuarioDTO> usuarios = (List<UsuarioDTO>) request.getAttribute(AttributeNames.USERS);
	List<UsuarioEventoPuntuaDTO> puntuaciones = (List<UsuarioEventoPuntuaDTO>) request.getAttribute(AttributeNames.PUNTUACIONES);
	
	
	%>



<script>

		function buscarEstadosEventoAjax() {
		    var url = "<%=CONTEXT%>/solicitud-service";
		        $.ajax({
		           type: "GET",
		           url: url,
		       data: "action=event-all-states",
		       success: function(data) {
		    	$('#estado-evento').empty();
		        for (i = 0; i<data.length; i++) {
		        	$('#estado-evento').append('<option value="'+data[i].id+'">'+data[i].nombre+'</option>');
		        }
		      }
		    });
		        
		}



		function buscarSeguidoresNoAceptadoEventoAyax() {
		    var url = "/RavegramWeb/usuario-service";
		        $.ajax({
		           type: "GET",
		           url: url,
		       data: "action=<%=ActionNames.USER_FOLLOWER_NOT_ACCEPT%>&id="+<%=request.getParameter(ParameterNames.ID)%>,
		       success: function(data) {
		    	$('#usuarios').empty();
		        for (i = 0; i<data.length; i++) {
		        	$('#usuarios').append('<option value="'+data[i].id+'">'+data[i].userName+'</option>');
		        }
		      }
		    });
		        
		}
		
		
		function buscarAsistentesEventoAyax() {
		    var url = "/RavegramWeb/usuario-service";
		        $.ajax({
		           type: "GET",
		           url: url,
		       data: "action=<%=ActionNames.USER_SEARCH_ASSISTANTS%>&id="+<%=request.getParameter(ParameterNames.ID)%>,
		       success: function(data) {
		    	$('#usuariosSeleccionados').empty();
		        for (i = 0; i<data.length; i++) {
		        	$('#usuariosSeleccionados').append('<option value="'+data[i].id+'">'+data[i].userName+'</option>');
		        }
		      }
		    });
		        
		}
		
		
		function buscarSeguidoresNoAceptadoEventoAyax() {
		    var url = "/RavegramWeb/usuario-service";
		        $.ajax({
		           type: "GET",
		           url: url,
		       data: "action=user-follower-not-accept&id="+<%=request.getParameter(ParameterNames.ID)%>,
		       success: function(data) {
		    	$('#usuarios').empty();
		        for (i = 0; i<data.length; i++) {
		        	$('#usuarios').append('<option value="'+data[i].id+'">'+data[i].userName+'</option>');
		        }
		      }
		    });
		        
		}
		


		function initMostrarAñadir(){
		    $('#añadir-usuarios-popup').click(function(){
		    	if ($('.añadir-usuarios').is(':hidden'))
		    		   $('.añadir-usuarios').show();
		    			
		    		else
		    		   $('.añadir-usuarios').hide();
		    	
		    });
		}
		
		

		window.addEventListener("load",comenzar,false);
		
		
		function comenzar(){
			usuariosASeleccionar = document.getElementById("usuarios");
			usuariosSeleccionados = document.getElementById("usuariosSeleccionados");
			
			usuariosASeleccionar.addEventListener("click",pasar,false);
			usuariosSeleccionados.addEventListener("click",regresar,false);
			
		
		}
		
	
		
		function pasar(){
			let seleccionadas = usuariosASeleccionar.selectedOptions;
			let destino = usuariosSeleccionados;
			if(seleccionadas.length>0){	
				while(seleccionadas.length>0){
					destino.add(seleccionadas[0]);				
				}	
			  }
			}
		
		
		
		function regresar(){
			let seleccionadas = usuariosSeleccionados.selectedOptions;
			let destino = usuariosASeleccionar;
			if(seleccionadas.length>0){
				while(seleccionadas.length>0){
					destino.add(seleccionadas[0]);				
				}	
			}
		}

		

	      function buscarTipoTematicaAjax() {
	            var url = '/RavegramWeb/tipo-tematica-service';
	            var selectedId = <%=evento.getIdTipoTematica()%>;
	                $.ajax({
	                   type: "GET",
	                   url: url,
	               data: "action=tipo-tematica",
	               success: function(data) {
	                for (i = 0; i<data.length; i++) {
	                	if(data[i].id == selectedId){
	                		$('#tipo-tematica-id').append('<option value="'+data[i].id+'" selected>'+data[i].nombre+'</option>');	
	                	}else{
	                		$('#tipo-tematica-id').append('<option value="'+data[i].id+'">'+data[i].nombre+'</option>');
	                	}
	                	
	                }
	              }
	            });
	        }
	      
	      
	      function buscarTipoMusicaAjax() {
	          var url = '/RavegramWeb/tipo-musica-service';
	          var selectedId = <%=evento.getIdTipoMusica()%>;
	              $.ajax({
	                 type: "GET",
	                 url: url,
	             data: "action=tipo-musica",
	             success: function(data) {
	              for (i = 0; i<data.length; i++) {
	            	  if(data[i].id == selectedId){
	            		  $('#tipo-musica-id').append('<option value="'+data[i].id+'" selected>'+data[i].nombre+'</option>');
	            	  }else{
	            		 $('#tipo-musica-id').append('<option value="'+data[i].id+'">'+data[i].nombre+'</option>');  
	            	  }
	              }
	            }
	          });
	      }
	      
	      function buscarTipoEstablecimientoAjax() {
	          var url = '/RavegramWeb/tipo-establecimiento-service';
	          var selectedId = <%=evento.getIdTipoEstablecimiento()%>;
	              $.ajax({
	                 type: "GET",
	                 url: url,
	             data: "action=tipo-establecimiento",
	             success: function(data) {
	              for (i = 0; i<data.length; i++) {
	            	  if(data[i].id == selectedId){
	            		  $('#tipo-establecimiento-id').append('<option value="'+data[i].id+'" selected>'+data[i].nombre+'</option>');
	            	  }else{
	            		  $('#tipo-establecimiento-id').append('<option value="'+data[i].id+'">'+data[i].nombre+'</option>');  
	            	  }
	              }
	            }
	          });
	      }



</script>







 <form action="<%=CONTEXT%>/private/evento" method="post">
	<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.EVENT_UPDATE%>"/>
	<input type="hidden" name="<%=ParameterNames.ID%>" value="<%=evento.getId()%>"/>
	            
     <section class="main-detail">
      <div class="wrapper">
        <div class="left-col">
          <div class="post">
            <div class="info">
              <div class="user">
                <div class="profile-pic">
                  <img src="<%=CONTEXT%>/css/images/profile.jpg" alt="" />
                </div>
                <a href="<%=CONTEXT%>/usuario?action=<%=ActionNames.USER_DETAIL%>&<%=ParameterNames.ID%>=<%=evento.getIdUsuario()%>"><p class="username"><%=evento.getNombreUsuarioCreador() %></p></a>
              </div>
             
            </div>
            <img src="<%=CONTEXT%>/css/images/1.jpg" class="post-image" />
            <div class="tabspost">
                    <div class="post-filtros">
                      <div class="tabs2">
          
                       
                           <!--  Informacion -->
                            <input type="radio" name="tabs2" id="tabone2" checked="checked">
                            <label for="tabone2">Informacion</label>
                            <div class="tab">
                              <div>

                                  <i class="icon fas fa-calendar">
                                    <input type="date" class="form-control" name="<%=ParameterNames.FECHA_HORA%>" value="<%=ParametersUtil.print(evento.getFechaHora())%>"/>
                                  </i>

								 <!-- TIPO TEMATICA -->
                                  <i class="icon fas fa-calendar">
                                    <select id="tipo-tematica-id">
                                      
                                    </select>
                                  </i>

								<!-- TIPO ESTABLECIMIENTO -->
                                  <i class="icon fas fa-hotel">
                                    <select id="tipo-establecimiento-id">

                                    </select>
                                  </i>
                                  
                                
								<!-- TIPO MUSICA -->
                                  <i class="icon fas fa-music">
                                    <select id="tipo-musica-id">
                                      
                                    </select>
                                  </i>

                                  <i class="icon fas fa-unlock"> 
                                    <select>
                                   
                                      <option>public privado</option>
                                    </select>
                                  </i>
                                 
                                  <i class="icon fas fa-music">
                                    <input type="text" class="form-control" name="<%=ParameterNames.CALLE%>" value="<%=ParametersUtil.print(evento.getCalle())%>" placeholder="calle"/>
                                  </i>
                                  <i class="icon fas fa-music">
                                    <%=evento.getNumAsistentes() %>
                                  </i>
                                  <i class="icon fas fa-location-arrow">
                                    ubicacion
                                  </i>
                                </div>
                                <div>
                                  <textarea name="<%=ParameterNames.DESCRIPCION%>" placeholder="Descripcion" class="form-control" cols="30" rows="5"><%=ParametersUtil.print(evento.getDescripcion())%></textarea>
                                </div>
                            </div>
              
          
                         <!--  Asistentes -->
                        <input type="radio" name="tabs2" id="tabtwo2">
                        <label for="tabtwo2">Asistentes</label>
                        <div class="tab">
                            <div class="asistente-tab">
                            
                            <!-- AÑADIR  ASISTENTES-->
                           
                               <div class="asistente-tab-dentro" id="añadir-usuarios-popup">
                                    <div class="asistente-pic">
                                      <img src="<%=CONTEXT%>/css/images/add-user.png" alt="" />
                                    </div>
                                    <div class="aisitente-name">
                                      <span>AÑADIR</span>
                                    </div>
                               </div>
                               
                               <!-- POP UP OCULTO PARA AÑADIR USUARIOS -->
                               
                               <div class="añadir-usuarios">
									<div>
										<select class="select" id="usuarios" multiple>
											
										</select>
									</div>
									<div>
										<select class="select" id="usuariosSeleccionados" name="<%=ParameterNames.IDS_ASISTENTES%>" multiple>
										</select>
									</div>
									<input type="submit" value="UPDATE"  name="update_profile_btn" id="update_profile_btn" class="update-profile-btn" />
									
								</div>
                           
                            
                            
                            <!--ASISTENTES-->
                               <% for (UsuarioDTO u : usuarios) { %>
                                <div class="asistente-tab-dentro">
                                  <div class="asistente-pic">
                                    <img src="<%=CONTEXT%>/css/images/profile.jpg" alt="" />
                                  </div>
                                  <div class="aisitente-name">
                                    <span><%=u.getUserName()%></span>
                                    <span class="eliminar-assitente-x">X</span>
                                  </div>
                                </div>
                                
								<%} %>
                        

                            </div>
                        </div>
                      </form>
                         <!--  Valoraciones -->
                        <input type="radio" name="tabs2" id="tabthree2">
                        <label for="tabthree2">Valoraciones</label>
                        <div class="tab">
                         <% for (UsuarioEventoPuntuaDTO valoracion : puntuaciones) { %>
                          <div class="post-content">
                          <div>
                              <span style="font-size: 30px; margin-left: -800px;"><%=valoracion.getValoracion()%></span>
                              <span style="font-size: 28px;">
                               <%=valoracion.getComentario() %>
                              </span>
                            </div>
                          </div>
                          <%} %>
                          
                          <div class="comment-wrapper">
                      			<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=ActionNames.PUNTUACION_CREATE%>"/>
                      			<input type="hidden" name="<%=ParameterNames.ID%>" value="<%=evento.getId()%>"/>
	                      
                          </div>
                        </div>
                      </div>
                         <div class="mb-3">
	            			  <input type="submit" value="UPDATE"  name="update_profile_btn" id="update_profile_btn" class="update-profile-btn" />
	            		</div>
	           	
              </div>
            </div>
          </div>
        </div>
      </div>        
    </section>
  
    
    
    

<script >$(document).ready(initMostrarAñadir());</script>
<script >$(document).ready(buscarAsistentesEventoAyax());</script>
<script >$(document).ready(buscarSeguidoresNoAceptadoEventoAyax());</script>
 
 
<script >$(document).ready(buscarTipoTematicaAjax());</script>
<script >$(document).ready(buscarTipoMusicaAjax());</script>
<script >$(document).ready(buscarTipoEstablecimientoAjax());</script>



<%@include file="/common/footer.jsp"%>
