package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.facturacion.ServicioFacturacion;
import co.edu.unbosque.shopflow.facturacion.colombia.FabricaColombia;
import co.edu.unbosque.shopflow.notificaciones.BandejaNotificaciones;
import co.edu.unbosque.shopflow.notificaciones.NotificadorEmail;
import co.edu.unbosque.shopflow.notificaciones.NotificadorSms;
import co.edu.unbosque.shopflow.pedidos.PedidoCreadoResponse;
import co.edu.unbosque.shopflow.pedidos.PedidoRequest;
import co.edu.unbosque.shopflow.pedidos.PedidoService;

class PedidoServiceNotificacionesTest {

	private BandejaNotificaciones bandeja;
	private PedidoService servicio;

	@BeforeEach
	void preparar() {
		bandeja = new BandejaNotificaciones();
		servicio = new PedidoService(new CatalogoFalso(), new ServicioFacturacion(List.of(new FabricaColombia())),
				List.of(new NotificadorEmail(bandeja), new NotificadorSms(bandeja)));
	}

	private PedidoRequest pedido(String contacto, String canal) {
		return new PedidoRequest("CLI-001", "1020304050", "CO", contacto,
				List.of(new PedidoRequest.ItemRequest("SKU-001", 1)), "Calle 134 # 9-51, Bogotá", false, null, null,
				true, canal);
	}

	@Test
	void crearUnPedidoConUnCanalNoSoportadoLanzaIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> servicio.crear(pedido("ana@correo.com", "FAX")));
	}

	@Test
	void unPagoAprobadoSeNotificaPorElCanalEmailDelPedido() {
		PedidoCreadoResponse creado = servicio.crear(pedido("ana@correo.com", "EMAIL"));

		servicio.notificarPagoAprobado(creado.pedido(), creado.comprobante().total());

		assertEquals(1, bandeja.todas().size());
		assertEquals("EMAIL", bandeja.todas().get(0).canal());
	}

	@Test
	void unPagoAprobadoSeNotificaPorElCanalSmsDelPedido() {
		PedidoCreadoResponse creado = servicio.crear(pedido("3001234567", "SMS"));

		servicio.notificarPagoAprobado(creado.pedido(), creado.comprobante().total());

		assertEquals(1, bandeja.todas().size());
		assertEquals("SMS", bandeja.todas().get(0).canal());
	}

	@Test
	void sinCanalEnLaPeticionSeUsaEmailPorDefecto() {
		PedidoCreadoResponse creado = servicio.crear(pedido("ana@correo.com", null));

		servicio.notificarPagoAprobado(creado.pedido(), creado.comprobante().total());

		assertEquals("EMAIL", bandeja.todas().get(0).canal());
	}
}
