package com.alejandro.reformatec.web.util;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import com.alejandro.reformatec.model.TipoUsuario;

public class Validator {

	public static final EmailValidator VALIDA_EMAIL = EmailValidator.getInstance();

	private static final String PASSWORD_REGEX = "[[a-z]+[A-Z]+[0-9]]{6,20}";

	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);	


	private static final String DNI_REGEX = "[0-9]{8}+[a-zA-Z]{1}";

	private static final Pattern DNI_PATTERN = Pattern.compile(DNI_REGEX);


	private static final String DIRECCION_WEB_REGEX = "/^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$/";

	private static final Pattern DIRECCION_WEB_PATTERN = Pattern.compile(DIRECCION_WEB_REGEX);


	private static final String PHONE_REGEX = "^(0034|\\+34)?(\\d\\d\\d)-? ?(\\d\\d)-? ?(\\d)-? ?(\\d)-? ?(\\d\\d)$";

	private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);



	public static final boolean validaPassword(String password) {
		Matcher m = PASSWORD_PATTERN.matcher(password);
		return m.matches();
	}



	public static final boolean validaDni(String dniStr) {
		Matcher m = DNI_PATTERN.matcher(dniStr);
		return m.matches();
	}



	public static final boolean validaDireccionWeb(String direccionWebStr) {
		Matcher m = DIRECCION_WEB_PATTERN.matcher(direccionWebStr);
		return m.matches();
	}



	public static final boolean validaTelefono(String telefonoStr){
		Matcher m = PHONE_PATTERN.matcher(telefonoStr);
		return m.matches();
	}



	/**
	 * Convierte un string a un integer.
	 * @param s
	 * @return El valor integer o null si no cumple el formato de número integer.
	 */
	
	public static final Integer validaInteger(String integerStr) {
		
		Integer i = null;
		
		if (!StringUtils.isBlank(integerStr)) {		
			
			integerStr = integerStr.trim();
			
			try {
				i = Integer.parseInt(integerStr);
				
			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}
		return i;
	}
	



	/**
	 * Convierte un string a un double.
	 * @param s
	 * @return El valor double o null si no cumple el formato de número double.
	 */
	public static Double validaDouble(String doubleStr) {

		Double d = null;
		
		if (!StringUtils.isBlank(doubleStr)) {
		
			doubleStr = doubleStr.trim();
			
			try {
				d = Double.parseDouble(doubleStr);
	
			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}
		return d;
	}




	/**
	 * Convierte un string a un boolean.
	 * @param s
	 * @return El valor boolean o null si no cumple el formato de un boolean.
	 */
	public static Boolean validaBoolean(String booleanStr) {

		Boolean b = null;

		if (!StringUtils.isBlank(booleanStr)) {
			booleanStr = booleanStr.trim();

			try{
				b = Boolean.parseBoolean(booleanStr);

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}
		return b;
	}


	
	public static String validaString(String stringStr) {
		
		String s = null;
		
		if (!StringUtils.isBlank(stringStr)) {
			stringStr = stringStr.trim();
			//TODO por ahora solo le valido la capacidad del varchar de bbdd, validar %65,.?¿!  tambien?
			if(stringStr.length()<0 || stringStr.length()>80) {
				s=null;
			}else {
				s = stringStr;
			}
		}
		return s;
	}
	
	
	
	public static String validaTitulo(String tituloStr) {
			
			String s = null;
			
			if (!StringUtils.isBlank(tituloStr)) {
				tituloStr = tituloStr.trim();
				// Los titulos en bbdd son de max 40
				if (tituloStr.length()<=0 || tituloStr.length()>40) {
					s=null;
				}else {
					s = tituloStr;
				}
			}
			return s;
		}
	
	
	
	public static String validaDescripcion(String descripcionStr) {
		
		String s = null;
		
		if (!StringUtils.isBlank(descripcionStr)) {
			descripcionStr = descripcionStr.trim();
			// Las descripciones en bbdd son de max 256
			if (descripcionStr.length()<=0 || descripcionStr.length()>256) {
				s=null;
			}else {
				s = descripcionStr;
			}
		}
		return s;
	}
	
	
	public static String validaNombreOApellido(String nombreOApellidoStr) {
			
			String s = null;
			
			if (!StringUtils.isBlank(nombreOApellidoStr)) {
				nombreOApellidoStr = nombreOApellidoStr.trim();
				// El nombre/nombrePerfil/apellido1/apellido2 son de max 80
				if (nombreOApellidoStr.length()<=0 || nombreOApellidoStr.length()>80) {
					s=null;
				}else {
					s = nombreOApellidoStr;
				}
			}
			return s;
		}
	
	
	/**
	 * Convierte un string a un Long.
	 * @param s
	 * @return El valor long o null si no cumple el formato de un long.
	 */	
	public static Long validaLong(String longStr) {

		Long l = null;

		if (!StringUtils.isBlank(longStr)) {

			longStr = longStr.trim();

			try{
				l = Long.parseLong(longStr);

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}
		return l;
	}




	/**
	 * Valida Id tipo De Usuario
	 * @param s
	 * @return El valor string o null si no cumple el formato del String.
	 */	
	public static Integer validaIdTipoUsuario (String idTipoUsuarioStr) {

		Integer iTipoUsuario = null;

		if (!StringUtils.isBlank(idTipoUsuarioStr)) {

			idTipoUsuarioStr = idTipoUsuarioStr.trim();

			try{

				iTipoUsuario = Integer.parseInt(idTipoUsuarioStr);
				if (iTipoUsuario!=TipoUsuario.USUARIO_CLIENTE && iTipoUsuario!=TipoUsuario.USUARIO_PROVEEDOR) {

					iTipoUsuario = null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return iTipoUsuario;
	}


	public static Integer validaStatusCuenta (String statusCuentaStr) {
		
		Integer i = null;
		
		if (!StringUtils.isBlank(statusCuentaStr)) {

			statusCuentaStr = statusCuentaStr.trim();

			try{
				i = Integer.parseInt(statusCuentaStr);
				//Hay 3 estados
				if (i<=0 || i>3) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;	
	}

	
	public static Integer validaStatusProyecto (String statusProyectoStr) {
		
		Integer i = null;
		
		if (!StringUtils.isBlank(statusProyectoStr)) {

			statusProyectoStr = statusProyectoStr.trim();

			try{
				i = Integer.parseInt(statusProyectoStr);
				//Hay 3 estados
				if (i<=0 || i>3) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;	
	}

	public static Integer validaPoblacion (String idPoblacionStr) {
			
			Integer i = null;
			
			if (!StringUtils.isBlank(idPoblacionStr)) {
	
				idPoblacionStr = idPoblacionStr.trim();
	
				try{
					i = Integer.parseInt(idPoblacionStr);
					//Hay 8 poblaciones
					if (i<=0 || i>8) {
	
						i=null;
					}
	
				} catch (NumberFormatException nfe) {
					// No hace falta tracear
				}
			}		
			return i;	
		}
	
	public static Integer validaProvincia (String idProvinciaStr) {
		
		Integer i = null;
		
		if (!StringUtils.isBlank(idProvinciaStr)) {

			idProvinciaStr = idProvinciaStr.trim();

			try{
				i = Integer.parseInt(idProvinciaStr);
				//Hay 4 provincias
				if (i<=0 || i>4) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;	
	}
	
	
	public static Integer validaNotaValoracion (String notaValoracionStr) {
			
			Integer i = null;
			
			if (!StringUtils.isBlank(notaValoracionStr)) {
	
				notaValoracionStr = notaValoracionStr.trim();
	
				try{
					i = Integer.parseInt(notaValoracionStr);
					//Se valora de 0 a 5.
					if (i<0 || i>5) {
	
						i=null;
					}
	
				} catch (NumberFormatException nfe) {
					// No hace falta tracear
				}
			}		
			return i;	
		}
	
	
	
	public static Integer validaStatusTrabajo (String statusTrabajoStr) {

		Integer i = null;
		
		if (!StringUtils.isBlank(statusTrabajoStr)) {

			statusTrabajoStr = statusTrabajoStr.trim();

			try{
				i = Integer.parseInt(statusTrabajoStr);
				//Hay 2 estados
				if (i<=0 || i>2) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;		
	}
	
	public static Integer validaStatusPresupuesto (String statusPresupuestoStr) {
	
		Integer i = null;
		
		if (!StringUtils.isBlank(statusPresupuestoStr)) {

			statusPresupuestoStr = statusPresupuestoStr.trim();

			try{
				i = Integer.parseInt(statusPresupuestoStr);
				//Hay 4 estados
				if (i<=0 || i>4) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;		
	}
	
	public static Integer validaStatusValoracion (String statusValoracionStr) {
		
		Integer i = null;
		
		if (!StringUtils.isBlank(statusValoracionStr)) {

			statusValoracionStr = statusValoracionStr.trim();

			try{
				i = Integer.parseInt(statusValoracionStr);
				//Hay 3 estados
				if (i<=0 || i>3) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;
	}

	public static Integer validaEspecializacion (String especializacionStr) {

		Integer i = null;

		if (!StringUtils.isBlank(especializacionStr)) {

			especializacionStr = especializacionStr.trim();

			try{
				i = Integer.parseInt(especializacionStr);
				//Hay 12 especializaciones
				if (i<=0 || i>12) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;
	}

	
	
	public static Integer validaPresupuestoMax (String presupuestoMaxStr) {

		Integer i = null;

		if (!StringUtils.isBlank(presupuestoMaxStr)) {

			presupuestoMaxStr = presupuestoMaxStr.trim();

			try{
				i = Integer.parseInt(presupuestoMaxStr);
				//Hay 12 especializaciones
				if (i<=0) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;
	}
	
	
	public static Double validaImporteLinea (String importeLineaStr) {

		Double i = null;

		if (!StringUtils.isBlank(importeLineaStr)) {

			importeLineaStr = importeLineaStr.trim();

			try{
				i = Double.parseDouble(importeLineaStr);

				if (i<0) {

					i=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}		
		return i;
	}


	public static Integer validaPresupuestoMaxMinimo (String presupuestoMaxMinimoStr) {

		Integer iMinimo = null;

		if (!StringUtils.isBlank(presupuestoMaxMinimoStr)) {

			presupuestoMaxMinimoStr = presupuestoMaxMinimoStr.trim();

			try{
				iMinimo = Integer.parseInt(presupuestoMaxMinimoStr);

				if (iMinimo>0) {

					iMinimo=null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}
		}
		return iMinimo;
	}


	//TODO No sé si esto es complicarse la vida ...
	public static Integer validaPresupuestoMaxMaximo (String presupuestoMaxMaximoStr, String presupuestoMaxMinimoStr) {

		Integer iMaximo = null;
		Integer iMinimo = null;

		if (StringUtils.isBlank(presupuestoMaxMinimoStr)) {
			iMinimo = 0;

		}else {
			presupuestoMaxMinimoStr = presupuestoMaxMinimoStr.trim();
		}

		if (!StringUtils.isBlank(presupuestoMaxMaximoStr)) {

			presupuestoMaxMaximoStr = presupuestoMaxMaximoStr.trim();

			try{
				iMaximo = Integer.parseInt(presupuestoMaxMaximoStr);
				iMinimo = Integer.parseInt(presupuestoMaxMinimoStr);

				if (iMaximo<0||iMaximo<iMinimo) {

					iMaximo = null;
				}

			} catch (NumberFormatException nfe) {
				// No hace falta tracear
			}			
		}		
		return iMaximo;
	}



	public static String validaOrderByTrabajo(String orderByStr) {

		if (!StringUtils.isBlank(orderByStr)) {			
			orderByStr=orderByStr.trim();

			if ((!orderByStr.equalsIgnoreCase("FC")) && (!orderByStr.equalsIgnoreCase("NV")) && (!orderByStr.equalsIgnoreCase("VAL"))) {
				orderByStr="VAL";
			}

		} else {
			orderByStr="VAL";
		}
		return orderByStr;
	}




	public static String validaOrderByUsuario(String orderByStr) {

		if (!StringUtils.isBlank(orderByStr)) {
			orderByStr=orderByStr.trim();

			if ((!orderByStr.equalsIgnoreCase("NV")) && (!orderByStr.equalsIgnoreCase("VAL"))) {
				orderByStr="VAL";
			}
			
		} else {
			orderByStr="VAL";
		}
		return orderByStr;
	}



	public static Date parseDate(String s) {

		Date v = null;

		try {
			v = DateUtil.INPUT_DATE_FORMAT.parse(s);

		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return v;		
	}	
}