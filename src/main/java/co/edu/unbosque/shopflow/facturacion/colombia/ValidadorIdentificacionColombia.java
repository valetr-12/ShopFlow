package co.edu.unbosque.shopflow.facturacion.colombia;

import co.edu.unbosque.shopflow.facturacion.ValidadorIdentificacion;

public class ValidadorIdentificacionColombia implements ValidadorIdentificacion {

	@Override
	public boolean validar(String identificacion) {
		return identificacion != null && identificacion.matches("\\d{6,10}");
	}
}
