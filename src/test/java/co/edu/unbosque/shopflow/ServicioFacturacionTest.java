package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.facturacion.ServicioFacturacion;
import co.edu.unbosque.shopflow.facturacion.colombia.FabricaColombia;
import co.edu.unbosque.shopflow.facturacion.mexico.FabricaMexico;
import co.edu.unbosque.shopflow.facturacion.peru.FabricaPeru;
import co.edu.unbosque.shopflow.pedidos.ItemPedido;
import co.edu.unbosque.shopflow.pedidos.Pedido;

class ServicioFacturacionTest {

	@Test
	void pedidoDeMexicoConCedulaColombianaDebeSerRechazado() {
		ServicioFacturacion servicio = new ServicioFacturacion(
				List.of(new FabricaColombia(), new FabricaMexico(), new FabricaPeru()));

		ItemPedido item = new ItemPedido("SKU-1", "Producto de prueba", 1, new BigDecimal("100.00"));

		Pedido pedido = new Pedido.Builder("PED-3", "CLI-1", "12345678", "MX", "cliente@correo.com", List.of(item))
				.direccionEnvio("Calle de prueba 123").envioExpress(true).build();

		assertThrows(IllegalArgumentException.class, () -> servicio.facturar(pedido));
	}
}
