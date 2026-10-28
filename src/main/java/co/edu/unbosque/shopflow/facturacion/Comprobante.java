package co.edu.unbosque.shopflow.facturacion;

import java.math.BigDecimal;

public record Comprobante(
        String numero,
        String pedidoId,
        String pais,
        String tipo,
        String autoridad,
        String identificacionCliente,
        BigDecimal baseImponible,
        BigDecimal porcentajeIva,
        BigDecimal valorIva,
        BigDecimal total) {
}
