package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.notificaciones.BandejaNotificaciones;
import co.edu.unbosque.shopflow.notificaciones.NotificadorEmail;

class NotificadorEmailTest {

	@Test
	void conUnContactoSinArrobaSeLanzaIllegalArgumentException() {
		BandejaNotificaciones bandeja = new BandejaNotificaciones();
		NotificadorEmail notificador = new NotificadorEmail(bandeja);

		assertThrows(IllegalArgumentException.class,
				() -> notificador.notificarPagoAprobado("ana.perez.correo.com", "PED-1", BigDecimal.TEN));
		assertTrue(bandeja.todas().isEmpty());
	}

	@Test
	void conUnCorreoValidoSeRegistraLaNotificacionPorElCanalEmail() {
		BandejaNotificaciones bandeja = new BandejaNotificaciones();
		NotificadorEmail notificador = new NotificadorEmail(bandeja);

		notificador.notificarPagoAprobado("ana.perez@correo.com", "PED-1", new BigDecimal("145.66"));

		assertEquals(1, bandeja.todas().size());
		assertEquals("EMAIL", bandeja.todas().get(0).canal());
		assertTrue(bandeja.todas().get(0).mensaje().contains("PED-1"));
		assertTrue(bandeja.todas().get(0).mensaje().contains("145.66"));
	}
}
