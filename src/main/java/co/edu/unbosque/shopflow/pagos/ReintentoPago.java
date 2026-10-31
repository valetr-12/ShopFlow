package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;

public class ReintentoPago extends PasarelaDecorator {

    private final int maxIntentos;

    public ReintentoPago(PasarelaDePago envuelta, int maxIntentos) {
        super(envuelta);
        if (maxIntentos < 1) {
            throw new IllegalArgumentException("max-intentos debe ser al menos 1");
        }
        this.maxIntentos = maxIntentos;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        PasarelaNoDisponibleException ultimoError = null;
        for (int intento = 1; intento <= maxIntentos; intento++) {
            try {
                return super.cobrar(montoUsd, referencia);
            } catch (PasarelaNoDisponibleException e) {
                ultimoError = e;
            }
        }
        throw ultimoError;  
    }
}