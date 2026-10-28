package co.edu.unbosque.shopflow.facturacion.colombia;

import co.edu.unbosque.shopflow.facturacion.CalculadoraImpuestos;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraImpuestosColombia implements CalculadoraImpuestos {

	private static final BigDecimal PORCENTAJE = new BigDecimal("19");

	@Override
	public BigDecimal porcentaje() {
		return PORCENTAJE;
	}

	@Override
	public BigDecimal calcular(BigDecimal baseImponible) {
		return baseImponible.multiply(PORCENTAJE).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}
}
