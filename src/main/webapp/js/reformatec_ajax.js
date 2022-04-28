
/*
1.- CARGAR ESPECIALIZACIONES BUSCADOR
2.- CARGAR PROVINCIAS BUSCADOR
3.-	CARGAR POBLACIONES BUSCADOR 
4.- CARGAR ORDERBY BUSCADOR
5.- BUSCADOR DESCRIPCION PROVEEDORES
6.-	BUSCADOR DESCRIPCION TRABAJOS
7.-	BUSCADOR DESCRIPCION PROYECTOS
8.-	RECUADRO BUSCADOR (DESCRIPCION)
9.- REGISTRO CLIENTE/PROVEEDOR
10.- EDITAR PERFIL CLIENTE/PROVEEDOR
11.- AÑADIR ESPECIALIZACIONES PROVEEDOR REGISTRO/UPDATE
*/


/*
window.addEventListener('load', cargarEspecializaciones);
window.addEventListener('load', cargarProvincias);
window.addEventListener('load', cargarPoblaciones);
window.addEventListener('load', buscarProveedores);
window.addEventListener('load', buscarTrabajos);
window.addEventListener('load', initPaso1);
window.addEventListener('load', initVolverPaso1);
*/


/*************************************************************************************************************

									1.- CARGAR ESPECIALIZACIONES BUSCADOR

*************************************************************************************************************/
function cargarEspecializaciones(idEspecializacionSelect) {
	var url = "/ReformatecWeb/especializacion-service";
    $.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-especializacion",
	       success: function(response) {
		    	  
		    	  $('#especializacion-select').empty();
		    	  $('#especializacion-select').append('<option value="null" selected>Selecciona una especializacion</option>');
		    	  $('#especializacion2-select').empty();
		    	  $('#especializacion2-select').append('<option value="null" selected>Selecciona una especializacion</option>');
		    	  var data = response.data;
		    	  var data2 = response.data;
		    	  for (var i=0; i<data.length;i++){
		    		 
		    		 if (data[i].idEspecializacion==idEspecializacionSelect) {
						$('#especializacion-select').append('<option selected value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
					} else {
		    		 $('#especializacion-select').append('<option  value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
		    	 	 }
		    	  }
		    	  for (var i=0; i<data2.length;i++){
		    		 
		    		 if (data[i]==idEspecializacionSelect) {
						$('#especializacion2-select').append('<option selected value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
					} else {
		    		 $('#especializacion2-select').append('<option value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
		    	  	}
		    	  }
	       }
	     });
}







/*************************************************************************************************************

									2.- CARGAR PROVINCIAS BUSCADOR

*************************************************************************************************************/	
function cargarProvincias(idProvinciaSelect) {
	var url = "/ReformatecWeb/provincia-service";
	$.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-provincia",
	       success: function(response) {
		    	  $('#provincia-select').empty();
		    	  $('#provincia-select').append('<option value="null" selected>Selecciona una provincia</option>');
		    	  $('#provincia2-select').empty();
		    	  $('#provincia2-select').append('<option value="null" selected>Selecciona una provincia</option>');
		    	  var data = response.data;
		    	  var data2 = response.data;
		    	   for (var i=0; i<data.length;i++){
		    		 
		    		 if (data[i].idProvincia==idProvinciaSelect) {
						$('#provincia-select').append('<option selected value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
					} else {
		    		 $('#provincia-select').append('<option  value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
		    	 	 }
		    	  }
		    	  for (var i=0; i<data2.length;i++){
		    		 
		    		 if (data[i].idProvincia==idProvinciaSelect) {
						$('#provincia2-select').append('<option selected value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
					} else {
		    		 $('#provincia2-select').append('<option value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
		    	  	}
		    	  }
	       }
	 	});
}
	






/*************************************************************************************************************

								3.-	CARGAR POBLACIONES BUSCADOR 

*************************************************************************************************************/
function cargarPoblaciones() {
	$("#poblacion-select").show()
	var provinciaId = $('#provincia-select').val();
	var url = "/ReformatecWeb/poblacion-service";
	    $.ajax({                        
		       type: "GET",                 
		       url: url,                    
		       data: "action=search-poblacion&id-provincia="+provinciaId,
		       success: function(response) {
		 	    	  $('#poblacion-select').empty();
			         $('#poblacion-select').append('<option disabled selected>Selecciona una poblacion</option>');
			         var data = response.data;
			         for (var i = 0; i<data.length; i++) {
			         	$('#poblacion-select').append('<option value='+data[i].idPoblacion+'>'+data[i].nombre+'</option>');
			         }
			       }
		     });
}	









/*************************************************************************************************************

									4.- CARGAR ORDERBY BUSCADOR

*************************************************************************************************************/
function cargarOrderBy() {
	$("#search-proveedor").submit;
}	








/*************************************************************************************************************

									5.- BUSCADOR DESCRIPCION PROVEEDORES

*************************************************************************************************************/
function buscarProveedores() {
	var descripcion = $('#descripcion-proveedor').val();	
	if (descripcion.length>0) {
		var url = "/ReformatecWeb/usuario-service";
		$.ajax({
		    	type: "GET", 
		    	url: url,                    
		    	data: "action=search-usuario&buscar-descripcion="+descripcion,
		       	success: function(response) {
			    	   if (response.errorCode != null && response.errorCode!= '') {
			    		   $('#lista-results').append('<p><b>'+response.errorCode+"</b><p>");
			    	   } else {
		 	    	 	   $('#lista-results').empty();
		 	    	 	   var data = response.data;
			         	   for (i = 0; i<data.length; i++) {
			         	 	$('#lista-results').append('<li class="listaBase"><a class="enlaceBase" href="/ReformatecWeb/usuario?action=detail-usuario&id-usuario='+data[i].idUsuario+'"><b>'+data[i].nombrePerfil+'</b> en '+data[i].nombrePoblacion+'</li>');	
			           	   }
			    	   }
		       }
		     });
	} else {
    	$('#proveedores-results').empty();
	}
}






/*************************************************************************************************************

								6.-	BUSCADOR DESCRIPCION TRABAJOS

*************************************************************************************************************/
function buscarTrabajos() {
	var descripcion = $('#descripcion-trabajo').val();	
	if (descripcion.length>0) {
		var url = "/ReformatecWeb/trabajo-service";
		$.ajax({
		    	type: "GET", 
		    	url: url,                    
		    	data: "action=search-trabajo&buscar-descripcion="+descripcion,
		       	success: function(response) {
			    	   if (response.errorCode != null && response.errorCode!= '') {
			    		   $('#lista-results').append('<p><b>'+response.errorCode+"</b><p>");
			    	   } else {
		 	    	 	   $('#lista-results').empty();
		 	    	 	   var data = response.data;
			         	   for (i = 0; i<data.length; i++) {
			         	 		$('#lista-results').append('<li><a class="enlaceBase" href="/ReformatecWeb/trabajoRealizado?action=detail-trabajo&id-trabajo-realizado='+data[i].idTrabajoRealizado+'"><b>'+data[i].titulo+'</b> en '+data[i].nombrePoblacion+'</li><p class="espacioBase"></p>');
			           	   }  
			    	   }
		       }
		     });
	} else {
    	$('#proveedores-results').empty();
	}
}






/*************************************************************************************************************

								7.-	BUSCADOR DESCRIPCION PROYECTOS

*************************************************************************************************************/
function buscarProyectos() {
	var descripcion = $('#descripcion-proyecto').val();	
	if (descripcion.length>0) {
		var url = "/ReformatecWeb/proyecto-service";
		$.ajax({
		    	type: "GET", 
		    	url: url,                    
		    	data: "action=search-proyecto&buscar-descripcion="+descripcion,
		       	success: function(response) {
			    	   if (response.errorCode != null && response.errorCode!= '') {
			    		   $('#lista-results').append('<p><b>'+response.errorCode+"</b><p>");
			    	   } else {
		 	    	 	   $('#lista-results').empty();
		 	    	 	   var data = response.data;
			         	   for (i = 0; i<data.length; i++) {
			         	 		$('#lista-results').append('<li><a class="enlaceBase" href="/ReformatecWeb/proyecto?action=detail-proyecto&id-proyecto='+data[i].idProyecto+'"><b>'+data[i].titulo+'</b> en '+data[i].nombrePoblacion+'</li><p class="espacioBase"></p>');
			           	   }
			    	   }
		       }
		     });
	} else {
    	$('#proveedores-results').empty();
	}
}






/*************************************************************************************************************

									8.-	RECUADRO BUSCADOR (DESCRIPCION)

*************************************************************************************************************/
/* Si hace click en el recuadro, no lo oculta, si hace click fuera del recuadro lo oculta, si vuelve pinchar al recuadro lo vuelve a mostrar*/

var mouseflag;

function setFlag() {
    mouseflag = true;
}

function ocultaDescripcion() {
    if (!mouseflag) {
    	$('#proveedores-results').hide();
    }
}

function doProcessing(id, name) {
    mouseflag = false;
}
 
 
function muestraDescripcion() {
	$('#proveedores-results').show();
}









/*************************************************************************************************************

									9.- REGISTRO CLIENTE/PROVEEDOR

*************************************************************************************************************/
/*

Registro Cliente/Proveedor - avanzar paso 1

*/
function initPaso1()
{
	$("#paso1").click(function(){
		$(".formregistro1").hide(500);
		$(".formregistro2").show(500);
	});
}	
	


/*

Registro Cliente/Proveedor - Volver Paso 1

*/
function initVolverPaso1()
{	
	$("#volverpaso1").click(function(){
		$(".formregistro2").hide(500);
		$(".formregistro1").show(500);
	});
	
}



/*

Registro Proveedor - Avanzar paso 2

 */
function initPaso2()
{
	
	$("#paso2").click(function(){
		$(".formregistro2").hide(500);
		$(".formregistro3").show(500);
	});
}



/*

Registro Proveedor -Volver Paso 2

*/
 function initVolverPaso2()
{	
	$("#volverpaso2").click(function(){
		$(".formregistro3").hide(500);
		$(".formregistro2").show(500);
	});
}
 
 
 
 /*

Registro Proveedor - Avanzar Paso 3

 */
function initPaso3()
{
	$("#paso3").click(function(){
		$(".formregistro3").hide(500);
		$(".formregistro4").show(500);
	});
}




/*

Registro Proveedor - Volver Paso 3

*/
 function initVolverPaso3()
{
	$("#volverpaso3").click(function(){
		$(".formregistro4").hide(500);
		$(".formregistro3").show(500);
	});
}
 

/*******************************************************************************************
								CARGAR ESPECIALIZACIONES REGISTRO PROVEEDOR
*********************************************************************************************/
function cargarEspecializacionesRegistro() {
	var url = "/ReformatecWeb/especializacion-service";
    $.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-especializacion",
	       success: function(response) {
		    	  $('#especializacion-select').empty();
		    	  var data = response.data;
		    	  var data2 = response.data;
		    	  for (var i=0; i<data.length;i++){
		    		 $('#especializacion-select').append('<option value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
		    	  }
		    	  for (var i=0; i<data2.length;i++){
		    		 $('#especializacion2-select').append('<option value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
		    	  }
	       }
	     });
}






 
/*************************************************************************************************************

									10.- EDITAR PERFIL CLIENTE/PROVEEDOR

*************************************************************************************************************/ 
/*

Editar Perfil cliente/proveedor - Cargar Cambiar Contraseña 

*/
function cargarCambioPass() {
	$("#cambioDePass").hide();
	$("#caja-pass").show();
	
}



/*

Editar Perfil cliente/proveedor - Mostar confirmacion eliminar cuenta

*/
function eliminarCuenta(){
	$("#eliminar-usuario").click(function(){
		$("#eliminar-usuario").hide();
		$(".eliminar-usuario-definitivo").show();
	});
}




/*****************************************************************************
							CARGAR PROVINCIAS EDITAR PERFIL
******************************************************************************/	
function cargarProvinciasPerfil() {
	var url = "/ReformatecWeb/provincia-service";
	$("#poblacionActual").hide();
	$("#provincia-select-perfil").show();

	$.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-provincia",
	       success: function(response) {
		    	  $('#provincia-select-perfil').empty();
		    	  $('#provincia-select-perfil').append('<option disabled selected>Selecciona una provincia</option>');
		    	  $('#provincia2-select-perfil').empty();
		    	  $('#provincia2-select-perfil').append('<option disabled selected>Selecciona una provincia</option>');
		    	  var data = response.data;
		    	  for (var i=0; i<data.length;i++){
		    		 $('#provincia-select-perfil').append('<option value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
		    		  $('#provincia2-select-perfil').append('<option value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
		    	  }
	       }
	 	});
}



	
/*****************************************************************************
							CARGAR POBLACIONES EDITAR PERFIL
******************************************************************************/
function cargarPoblacionesPerfil() {
	
	$("#poblacion-select-perfil").show();
	$("#codPostalEditarPerfilLabel").show();	
	$("#direccionEditarPerfilLabel").show();
	$("#codPostalEditarPerfil").show();
	$("#direccionEditarPerfil").show();


	var provinciaId = $('#provincia-select-perfil').val();
	var url = "/ReformatecWeb/poblacion-service";
	    $.ajax({                        
		       type: "GET",                 
		       url: url,                    
		       data: "action=search-poblacion&id-provincia="+provinciaId,
		       success: function(response) {
		 	    	  $('#poblacion-select-perfil').empty();			 
					  
			         $('#poblacion-select-perfil').append('<option disabled selected>Selecciona una poblacion</option>');
			         var data = response.data;
			         for (var i = 0; i<data.length; i++) {
			         	$('#poblacion-select-perfil').append('<option value='+data[i].idPoblacion+'>'+data[i].nombre+'</option>');
			         }
			       }
		     });
}	



/*****************************************************************************
CARGAR ESPECIALIZACIONES PROVEEDORES  EDITAR PERFIL	(LAS QUE YA TIENE EL PROVEEDOR)
******************************************************************************/	
function cargarEspecializacionesUpdateSi() {
	var url = "/ReformatecWeb/especializacion-service";
    $.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-usuario-especializacion",
	       success: function(data) {
		    	  $('#especializacionesSeleccionadas').empty();		    	  
		    	  for (var i=0; i<data.length;i++){
		    		 $('#especializacionesSeleccionadas').append('<option selected value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
		    	  }
	       }
	     });
}




/*****************************************************************************
CARGAR ESPECIALIZACIONES PROVEEDORES  EDITAR PERFIL		(RESTANTES)
******************************************************************************/	
function cargarEspecializacionesUpdateNo() {
	var url = "/ReformatecWeb/especializacion-service";
    $.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-usuario-sin-especializacion",
	       success: function(data) {
		    	  $('#especializacion-select').empty();		    	  
		    	  for (var i=0; i<data.length;i++){
		    		 $('#especializacion-select').append('<option value='+data[i].idEspecializacion+'>'+data[i].nombre+'</option>');
		    	  }
	       }
	     });
}






/*************************************************************************************************************

								11.- AÑADIR ESPECIALIZACIONES PROVEEDOR REGISTRO/UPDATE

*************************************************************************************************************/ 

function comenzar(){
    usuariosASeleccionar = document.getElementById("especializacion-select");
    usuariosSeleccionados = document.getElementById("especializacionesSeleccionadas");

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