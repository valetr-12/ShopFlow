package co.edu.unbosque.shopflow.pedidos;

import java.math.BigDecimal;

/** Respuesta de un pago aprobado. */
public record PagoResponse(
        String pedidoId,
        String estado,
        String proveedor,
        BigDecimal montoPagado,
        String autorizacion) { 
	
}