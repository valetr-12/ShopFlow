package co.edu.unbosque.shopflow.facturacion.peru;

import co.edu.unbosque.shopflow.facturacion.CalculadoraImpuestos;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraImpuestosPeru implements CalculadoraImpuestos {

	private static final BigDecimal PORCENTAJE = new BigDecimal("18");

	@Override
	public BigDecimal porcentaje() {
		return PORCENTAJE;
	}

	@Override
	public BigDecimal calcular(BigDecimal baseImponible) {
		return baseImponible.multiply(PORCENTAJE).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}
}
