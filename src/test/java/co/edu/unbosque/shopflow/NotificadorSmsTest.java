package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.notificaciones.BandejaNotificaciones;
import co.edu.unbosque.shopflow.notificaciones.NotificacionSms;
import co.edu.unbosque.shopflow.notificaciones.NotificadorSms;

class NotificadorSmsTest {

	@Test
	void elSmsSeRecortaA160Caracteres() {
		BandejaNotificaciones bandeja = new BandejaNotificaciones();

		new NotificacionSms(bandeja).enviar("3001234567", "x".repeat(300));

		assertEquals(160, bandeja.todas().get(0).mensaje().length());
	}

	@Test
	void elNotificadorSmsRegistraLaNotificacionPorElCanalSms() {
		BandejaNotificaciones bandeja = new BandejaNotificaciones();

		new NotificadorSms(bandeja).notificarPagoAprobado("3001234567", "PED-2", new BigDecimal("50"));

		assertEquals("SMS", bandeja.todas().get(0).canal());
		assertEquals("3001234567", bandeja.todas().get(0).destino());
	}
}
