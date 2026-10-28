package co.edu.unbosque.shopflow.facturacion.mexico;

import co.edu.unbosque.shopflow.facturacion.ValidadorIdentificacion;

public class ValidadorIdentificacionMexico implements ValidadorIdentificacion {

	@Override
	public boolean validar(String identificacion) {
		if (identificacion == null) {
			return false;
		}

		String rfc = identificacion.trim().toUpperCase();

		return rfc.matches("[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}");
	}
}
