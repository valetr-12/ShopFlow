package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.facturacion.ServicioFacturacion;
import co.edu.unbosque.shopflow.facturacion.colombia.FabricaColombia;
import co.edu.unbosque.shopflow.notificaciones.BandejaNotificaciones;
import co.edu.unbosque.shopflow.notificaciones.NotificadorEmail;
import co.edu.unbosque.shopflow.notificaciones.NotificadorSms;
import co.edu.unbosque.shopflow.notificaciones.NotificadorWhatsApp;
import co.edu.unbosque.shopflow.pedidos.PedidoCreadoResponse;
import co.edu.unbosque.shopflow.pedidos.PedidoRequest;
import co.edu.unbosque.shopflow.pedidos.PedidoService;

class NotificadorWhatsAppTest {

	@Test
	void unNumeroSinIndicativoSeRechaza() {
		BandejaNotificaciones bandeja = new BandejaNotificaciones();
		NotificadorWhatsApp notificador = new NotificadorWhatsApp(bandeja);

		assertThrows(IllegalArgumentException.class,
				() -> notificador.notificarPagoAprobado("3001234567", "PED-1", BigDecimal.TEN));
		assertThrows(IllegalArgumentException.class,
				() -> notificador.notificarPagoAprobado("ana@correo.com", "PED-1", BigDecimal.TEN));
		assertTrue(bandeja.todas().isEmpty());
	}

	@Test
	void conIndicativoSeAnteponeElCheckAlMensaje() {
		BandejaNotificaciones bandeja = new BandejaNotificaciones();

		new NotificadorWhatsApp(bandeja).notificarPagoAprobado("+573001234567", "PED-1", new BigDecimal("145.66"));

		assertEquals("WHATSAPP", bandeja.todas().get(0).canal());
		assertTrue(bandeja.todas().get(0).mensaje().startsWith("✅ "));
	}

	@Test
	void unPedidoConCanalWhatsAppSeNotificaSinModificarPedidoService() {
		BandejaNotificaciones bandeja = new BandejaNotificaciones();
		PedidoService servicio = new PedidoService(new CatalogoFalso(),
				new ServicioFacturacion(List.of(new FabricaColombia())), List.of(new NotificadorEmail(bandeja),
						new NotificadorSms(bandeja), new NotificadorWhatsApp(bandeja)));

		PedidoCreadoResponse creado = servicio.crear(new PedidoRequest("CLI-001", "1020304050", "CO", "+573001234567",
				List.of(new PedidoRequest.ItemRequest("SKU-001", 1)), "Calle 134 # 9-51, Bogotá", false, null, null,
				true, "WHATSAPP"));
		servicio.notificarPagoAprobado(creado.pedido(), creado.comprobante().total());

		assertEquals("WHATSAPP", bandeja.todas().get(0).canal());
	}
}
