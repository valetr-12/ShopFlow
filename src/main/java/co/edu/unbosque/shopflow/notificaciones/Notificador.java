package co.edu.unbosque.shopflow.notificaciones;

import java.math.BigDecimal;

public abstract class Notificador {

	public final void notificarPagoAprobado(String destino, String pedidoId, BigDecimal totalPagado) {
		String mensaje = "Tu pago del pedido " + pedidoId + " por USD " + totalPagado + " fue aprobado.";
		Notificacion notificacion = crearNotificacion();
		notificacion.enviar(destino, mensaje);
	}

	protected abstract Notificacion crearNotificacion();

	public abstract String canal();
}
