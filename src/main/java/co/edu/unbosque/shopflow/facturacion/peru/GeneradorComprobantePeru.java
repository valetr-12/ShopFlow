package co.edu.unbosque.shopflow.facturacion.peru;

import co.edu.unbosque.shopflow.facturacion.Comprobante;
import co.edu.unbosque.shopflow.facturacion.GeneradorComprobante;
import co.edu.unbosque.shopflow.pedidos.Pedido;

import java.math.BigDecimal;
import java.util.UUID;

public class GeneradorComprobantePeru implements GeneradorComprobante {

	@Override
	public Comprobante generar(Pedido pedido, BigDecimal baseImponible, BigDecimal valorIva, BigDecimal total) {

		return new Comprobante("SUNAT-" + UUID.randomUUID(), pedido.getId(), "PE", "Factura electrónica", "SUNAT",
				pedido.getDocumentoCliente(), baseImponible, new BigDecimal("18"), valorIva, total);
	}
}
