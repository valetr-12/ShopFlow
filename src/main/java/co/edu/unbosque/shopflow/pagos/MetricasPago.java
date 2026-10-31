package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricasPago extends PasarelaDecorator {

    private final String proveedor;
    private final AtomicInteger cobros = new AtomicInteger();
    private final AtomicInteger aprobados = new AtomicInteger();

    public MetricasPago(PasarelaDePago envuelta, String proveedor) {
        super(envuelta);
        this.proveedor = proveedor;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        boolean aprobado = false;
        try {
            ResultadoPago resultado = super.cobrar(montoUsd, referencia);
            aprobado = resultado.aprobado();
            return resultado;
        } finally {
            int total = cobros.incrementAndGet();
            int ok = aprobado ? aprobados.incrementAndGet() : aprobados.get();
            double tasa = 100.0 * ok / total;
            System.out.println("[Métricas] " + proveedor + ": " + total + " cobros, "
                    + ok + " aprobados, tasa de aprobación " + Math.round(tasa) + "%");
        }
    }
}