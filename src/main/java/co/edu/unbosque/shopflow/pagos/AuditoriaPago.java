package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;

public class AuditoriaPago extends PasarelaDecorator {

    private final String proveedor;
    private final RegistroAuditoria registro;

    public AuditoriaPago(PasarelaDePago envuelta, String proveedor, RegistroAuditoria registro) {
        super(envuelta);
        this.proveedor = proveedor;
        this.registro = registro;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        long inicio = System.nanoTime();
        try {
            ResultadoPago resultado = super.cobrar(montoUsd, referencia);
            String estado = resultado.aprobado() ? "APROBADO" : "RECHAZADO: " + resultado.mensaje();
            registrar(referencia, montoUsd, estado, inicio);
            return resultado;
        } catch (PasarelaNoDisponibleException e) {
            registrar(referencia, montoUsd, "NO DISPONIBLE", inicio);
            throw e;  
        }
    }

    private void registrar(String referencia, BigDecimal monto, String estado, long inicio) {
        long duracionMs = (System.nanoTime() - inicio) / 1_000_000;
        registro.registrar(proveedor, referencia, monto, estado, duracionMs);
    }
}