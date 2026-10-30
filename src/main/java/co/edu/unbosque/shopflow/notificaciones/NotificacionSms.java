package co.edu.unbosque.shopflow.notificaciones;

public class NotificacionSms implements Notificacion {

	private final BandejaNotificaciones bandeja;

	public NotificacionSms(BandejaNotificaciones bandeja) {
		this.bandeja = bandeja;
	}

	@Override
	public void enviar(String destino, String mensaje) {
		if (destino == null || destino.isBlank()) {
			throw new IllegalArgumentException("El contacto para SMS es obligatorio");
		}
		if (mensaje.length() > 160) {
			mensaje = mensaje.substring(0, 160);
		}
		bandeja.registrar("SMS", destino, mensaje);
	}
}
