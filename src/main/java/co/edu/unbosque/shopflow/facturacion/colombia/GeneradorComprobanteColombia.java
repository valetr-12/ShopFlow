package co.edu.unbosque.shopflow.facturacion.colombia;

import co.edu.unbosque.shopflow.facturacion.Comprobante;
import co.edu.unbosque.shopflow.facturacion.GeneradorComprobante;
import co.edu.unbosque.shopflow.pedidos.Pedido;

import java.math.BigDecimal;
import java.util.UUID;

public class GeneradorComprobanteColombia implements GeneradorComprobante {

	@Override
	public Comprobante generar(Pedido pedido, BigDecimal baseImponible, BigDecimal valorIva, BigDecimal total) {

		return new Comprobante("DIAN-" + UUID.randomUUID(), pedido.getId(), "CO", "Factura electrónica (DIAN)", "DIAN",
				pedido.getDocumentoCliente(), baseImponible, new BigDecimal("19"), valorIva, total);
	}
}
