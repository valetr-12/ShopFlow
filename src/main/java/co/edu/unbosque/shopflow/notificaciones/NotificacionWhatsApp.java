package co.edu.unbosque.shopflow.notificaciones;

public class NotificacionWhatsApp implements Notificacion {

	private final BandejaNotificaciones bandeja;

	public NotificacionWhatsApp(BandejaNotificaciones bandeja) {
		this.bandeja = bandeja;
	}

	@Override
	public void enviar(String destino, String mensaje) {
		if (!tieneIndicativo(destino)) {
			throw new IllegalArgumentException("El número de WhatsApp debe tener indicativo, por ejemplo +573001234567");
		}
		bandeja.registrar("WHATSAPP", destino, "✅ " + mensaje);
	}

	private boolean tieneIndicativo(String numero) {
		if (numero == null || !numero.startsWith("+") || numero.length() < 9 || numero.length() > 16) {
			return false;
		}
		for (int i = 1; i < numero.length(); i++) {
			if (!Character.isDigit(numero.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
