package co.edu.unbosque.shopflow.facturacion;

import co.edu.unbosque.shopflow.pedidos.Pedido;
import java.math.BigDecimal;

public interface GeneradorComprobante {

    Comprobante generar(
            Pedido pedido,
            BigDecimal baseImponible,
            BigDecimal valorIva,
            BigDecimal total);
}
