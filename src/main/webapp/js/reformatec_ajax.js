
/*
 
1. Cargar Especializaciones
2. Cargar Provincias
3. Cargar Poblaciones
4. Buscar Proveedores
5. Buscar Trabajos
6. Paso1 (Formulario Registro)
7. Volver Paso1 (Formulario Registro)
*/

/*

1. Cargar Especializaciones

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

function cargarEspecializaciones() {
	var url = "/ReformatecWeb/especializacion-service";
    $.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-especializacion",
	       success: function(response) {
		    	  $('#especializacion-select').empty();
		    	  $('#especializacion-select').append('<option disabled selected>Selecciona una especializacion</option>');
		    	  $('#especializacion2-select').empty();
		    	  $('#especializacion2-select').append('<option disabled selected>Selecciona una especializacion</option>');
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
function cargarProvincias() {
	var url = "/ReformatecWeb/provincia-service";
	$.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-provincia",
	       success: function(response) {
		    	  $('#provincia-select').empty();
		    	  $('#provincia-select').append('<option disabled selected>Selecciona una provincia</option>');
		    	  $('#provincia2-select').empty();
		    	  $('#provincia2-select').append('<option disabled selected>Selecciona una provincia</option>');
		    	  var data = response.data;
		    	  for (var i=0; i<data.length;i++){
		    		 $('#provincia-select').append('<option value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
		    		  $('#provincia2-select').append('<option value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
		    	  }
	       }
	 	});
}
	
	
/*

3. Cargar Poblaciones

*/
function cargarPoblaciones() {
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


$('#descripcion-proveedor').blur(function(){
	alert("A");
	$('#descripcion-proveedor').val("");
});



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

6. Paso 1 (Formulario Registro)

*/
function initPaso1()
{
	$("#paso1").click(function(){
		$(".formregistro1").hide(500);
		$(".formregistro2").show(500);
	});
}	
	


/*

7. Volver Paso 1 (Formulario Registro)

*/
function initVolverPaso1()
{
	$("#volverpaso1").click(function(){
		$(".formregistro2").hide(500);
		$(".formregistro1").show(500);
	});
}



/*

8. Cargar Provincias Editar Perfil

*/	
function cargarProvinciasPerfil() {
	var url = "/ReformatecWeb/provincia-service";
	$.ajax({                        
	       type: "GET",                 
	       url: url,                    
	       data: "action=search-provincia",
	       success: function(response) {
		    	  $('#provincia-selectPerfil').empty();
		    	  $('#provincia-selectPerfil').append('<option disabled selected>Actualizar Direccion</option>');
		    	  var data = response.data;
		    	  for (var i=0; i<data.length;i++){
		    		 $('#provincia-selectPerfil').append('<option value='+data[i].idProvincia+'>'+data[i].nombre+'</option>');
		    	  }
	       }
	 	});
}



/*

9. Cargar Poblaciones Editar Perfil

*/
function cargarPoblacionesPerfil() {
	var provinciaId = $('#provincia-selectPerfil').val();
	$("#poblacion-selectPerfil").show();
	$("#poblacionActual").hide();
	
	var url = "/ReformatecWeb/poblacion-service";
	    $.ajax({                        
		       type: "GET",                 
		       url: url,                    
		       data: "action=search-poblacion&id-provincia="+provinciaId,
		       success: function(response) {
		 	    	  $('#poblacion-selectPerfil').empty();
			         $('#poblacion-selectPerfil').append('<option disabled selected>Selecciona una poblacion</option>');
			         var data = response.data;
			         for (var i = 0; i<data.length; i++) {
			         	$('#poblacion-selectPerfil').append('<option value='+data[i].idPoblacion+'>'+data[i].nombre+'</option>');
			         }
			       },
			       error: function (jqXHR, textStatus, errorThrown) {
			        	alert(textStatus);
			 	   }
		     });
}	
/*

9. Cargar Nueva Direccion y nuevo codigo postal Editar Perfil

*/
function cargarDireccionPerfil() {
	$("#direccionEditarPerfil").show();
	$("#codPostalEditarPerfil").show();
}