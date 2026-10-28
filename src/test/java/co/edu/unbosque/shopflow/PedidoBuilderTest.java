package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.pedidos.ItemPedido;
import co.edu.unbosque.shopflow.pedidos.Pedido;

class PedidoBuilderTest {

	@Test
	void buildSinItemsDebeLanzarExcepcion() {
		Pedido.Builder builder = new Pedido.Builder("PED-1", "CLI-1", "12345678", "CO", "cliente@correo.com",
				List.of());

		assertThrows(IllegalArgumentException.class, builder::build);
	}

	@Test
	void crearPedidoADomicilioSinDireccionDebeLanzarExcepcion() {
		ItemPedido item = new ItemPedido("SKU-1", "Producto de prueba", 1, new BigDecimal("10000"));

		assertThrows(IllegalArgumentException.class, () -> {
			new Pedido.Builder("PED-1", "CLI-1", "12345678", "CO", "cliente@correo.com", List.of(item))
					.direccionEnvio("").recogerEnTienda(false).envioExpress(true).build();
		});
	}
}
