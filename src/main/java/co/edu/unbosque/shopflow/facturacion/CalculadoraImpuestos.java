package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

public interface CalculadoraImpuestos {

    BigDecimal porcentaje();

    BigDecimal calcular(BigDecimal baseImponible);
}
