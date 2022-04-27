
/*
 
1. Cargar Especializaciones
2. Cargar Provincias
3. Cargar Poblaciones
4. Buscar Proveedores
5. Buscar Trabajos
6. Buscar Proyectos
7. Paso1 (Formulario Registro)
8. Volver Paso1 (Formulario Registro)
9. Cargar Provincias Editar Perfil
10. Cargar Poblaciones Editar Perfil
11. Cargar Nueva Direccion y nuevo codigo postal Editar Perfil
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


/*

1. Cargar Especializaciones

*/
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


/*

1. Cargar EspecializacionesRegistroProveedor

*/
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

/*

2. Cargar Provincias

*/	
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
	





/*

2. Cargar Provincias Editar Perfil

*/	
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




	
/*

3. Cargar Poblaciones Editar Perfil

*/
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




/**

Cargar Cambiar Contraseña
 */
function cargarCambioPass() {
	$("#cambioDePass").hide();
	$("#caja-pass").show();
	
}





/*
3. Cargar Poblaciones

*/
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




/*
4. Buscar Proveedores

*/
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
			    		   $('#proveedores-results').append('<p><b>'+response.errorCode+"</b><p>");
			    	   } else {
		 	    	 	   $('#proveedores-results').empty();
		 	    	 	   var data = response.data;
			         	   for (i = 0; i<data.length; i++) {
			         	 		$('#proveedores-results').append('<p><b>'+data[i].nombrePerfil+'</b> de '+data[i].nombrePoblacion+'</p>');
			           	   }
			    	   }
		       }
		     });
	} else {
    	$('#proveedores-results').empty();
	}
}

/*
$('#descripcion-proveedor').blur(function(){
	alert("A");
	$('#descripcion-proveedor').val("");
});

*/

/* 

5. Buscar Trabajos


*/


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
			    		   $('#proveedores-results').append('<p><b>'+response.errorCode+"</b><p>");
			    	   } else {
		 	    	 	   $('#proveedores-results').empty();
		 	    	 	   var data = response.data;
			         	   for (i = 0; i<data.length; i++) {
			         	 		$('#proveedores-results').append('<p><b>'+data[i].titulo+'</b> en '+data[i].nombrePoblacion+'</p>');
			           	   }
			    	   }
		       }
		     });
	} else {
    	$('#proveedores-results').empty();
	}
}




/*

6. Buscar Proyectos

*/
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
			    		   $('#proveedores-results').append('<p><b>'+response.errorCode+"</b><p>");
			    	   } else {
		 	    	 	   $('#proveedores-results').empty();
		 	    	 	   var data = response.data;
			         	   for (i = 0; i<data.length; i++) {
			         	 		$('#proveedores-results').append('<p><b>'+data[i].titulo+'</b> de '+data[i].nombrePoblacion+'</p>');
			           	   }
			    	   }
		       }
		     });
	} else {
    	$('#proveedores-results').empty();
	}
}





/*

7. Paso 1 (Formulario Registro)

*/
function initPaso1()
{
	$("#paso1").click(function(){
		$(".formregistro1").hide(500);
		$(".formregistro2").show(500);
	});
}	
	



/*

8. Volver Paso 1 (Formulario Registro)

*/
function initVolverPaso1()
{	
	$("#volverpaso1").click(function(){
		$(".formregistro2").hide(500);
		$(".formregistro1").show(500);
	});
	
}




/*

Paso 2 (Formulario Registro Proveedor)

 */
function initPaso2()
{
	
	$("#paso2").click(function(){
		$(".formregistro2").hide(500);
		$(".formregistro3").show(500);
	});
}





/*

Volver Paso 2 (Formulario Registro Proveedor)

*/
 function initVolverPaso2()
{	
	$("#volverpaso2").click(function(){
		$(".formregistro3").hide(500);
		$(".formregistro2").show(500);
	});
}
 
 
 
 
 /*

Paso 3 (Formulario Registro Proveedor)

 */
function initPaso3()
{
	$("#paso3").click(function(){
		$(".formregistro3").hide(500);
		$(".formregistro4").show(500);
	});
}





/*

Volver Paso 3 (Formulario Registro Proveedor)

*/
 function initVolverPaso3()
{
	$("#volverpaso3").click(function(){
		$(".formregistro4").hide(500);
		$(".formregistro3").show(500);
	});
}
 

 
 



/*

Editar Perfil mostar eliminar cuenta

*/

function eliminarCuenta(){
	$("#eliminar-usuario").click(function(){
		$("#eliminar-usuario").hide();
		$(".eliminar-usuario-definitivo").show();
	});
}


/*

Añadir especializaciones

*/
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





/**

Cargar Index

 */
function cargarUsuariosIndex(){
	
	
	
}