package co.edu.unbosque.shopflow.pedidos;

import co.edu.unbosque.shopflow.facturacion.Comprobante;

public record PedidoCreadoResponse(
        Pedido pedido,
        Comprobante comprobante) {
}