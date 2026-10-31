package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;
import java.util.Objects;


public abstract class PasarelaDecorator implements PasarelaDePago {

    protected final PasarelaDePago envuelta;

    protected PasarelaDecorator(PasarelaDePago envuelta) {
        this.envuelta = Objects.requireNonNull(envuelta, "La pasarela envuelta es obligatoria");
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        return envuelta.cobrar(montoUsd, referencia);
    }
}