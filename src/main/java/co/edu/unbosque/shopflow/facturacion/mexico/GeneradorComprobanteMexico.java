package co.edu.unbosque.shopflow.facturacion.mexico;

import co.edu.unbosque.shopflow.facturacion.Comprobante;
import co.edu.unbosque.shopflow.facturacion.GeneradorComprobante;
import co.edu.unbosque.shopflow.pedidos.Pedido;

import java.math.BigDecimal;
import java.util.UUID;

public class GeneradorComprobanteMexico implements GeneradorComprobante {

	@Override
	public Comprobante generar(Pedido pedido, BigDecimal baseImponible, BigDecimal valorIva, BigDecimal total) {

		return new Comprobante("CFDI-" + UUID.randomUUID(), pedido.getId(), "MX", "CFDI (SAT)",
				"SAT", pedido.getDocumentoCliente(), baseImponible, new BigDecimal("16"), valorIva, total);
	}
}