package co.edu.unbosque.shopflow.notificaciones;

public class NotificacionEmail implements Notificacion {

	private final BandejaNotificaciones bandeja;

	public NotificacionEmail(BandejaNotificaciones bandeja) {
		this.bandeja = bandeja;
	}

	@Override
	public void enviar(String destino, String mensaje) {
		if (destino == null || !destino.contains("@")) {
			throw new IllegalArgumentException("El contacto para correo debe contener '@'");
		}
		bandeja.registrar("EMAIL", destino, "Asunto: Pago aprobado en ShopFlow | " + mensaje);
	}
}
