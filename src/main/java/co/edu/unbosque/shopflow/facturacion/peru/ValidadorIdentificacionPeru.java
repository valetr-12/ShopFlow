package co.edu.unbosque.shopflow.facturacion.peru;

import co.edu.unbosque.shopflow.facturacion.ValidadorIdentificacion;

public class ValidadorIdentificacionPeru implements ValidadorIdentificacion {

	@Override
	public boolean validar(String identificacion) {
		if (identificacion == null) {
			return false;
		}

		String documento = identificacion.trim();

		return documento.matches("\\d{8}") || documento.matches("\\d{11}");
	}
}
