package co.edu.unbosque.shopflow.pedidos;

import co.edu.unbosque.shopflow.catalogo.Producto;
import co.edu.unbosque.shopflow.catalogo.ServicioCatalogo;
import co.edu.unbosque.shopflow.facturacion.Comprobante;
import co.edu.unbosque.shopflow.facturacion.ServicioFacturacion;
import co.edu.unbosque.shopflow.notificaciones.Notificador;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PedidoService {

	private final ServicioCatalogo catalogo;
	private final ServicioFacturacion servicioFacturacion;
	private final Map<String, Notificador> notificadores = new HashMap<>();

	private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();
	private final AtomicLong contador = new AtomicLong(0);

	public PedidoService(ServicioCatalogo catalogo, ServicioFacturacion servicioFacturacion,
			List<Notificador> notificadoresDisponibles) {

		this.catalogo = catalogo;
		this.servicioFacturacion = servicioFacturacion;

		for (Notificador notificador : notificadoresDisponibles) {
			String canal = notificador.canal().toUpperCase(Locale.ROOT);

			this.notificadores.put(canal, notificador);
		}
	}

	public PedidoCreadoResponse crear(PedidoRequest request) {

		if (request == null) {
			throw new IllegalArgumentException("Los datos del pedido son obligatorios");
		}

		if (request.canalNotificacion() != null) {
			notificadorPara(request.canalNotificacion());
		}

		List<ItemPedido> items = new ArrayList<>();

		if (request.items() != null) {
			for (PedidoRequest.ItemRequest item : request.items()) {

				if (item == null) {
					throw new IllegalArgumentException("Los ítems del pedido no pueden ser nulos");
				}

				if (item.sku() == null || item.sku().isBlank()) {
					throw new IllegalArgumentException("El SKU del producto es obligatorio");
				}

				if (item.cantidad() <= 0) {
					throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
				}

				Producto producto = catalogo.buscar(item.sku());

				ItemPedido itemPedido = new ItemPedido(producto.sku(), producto.nombre(), item.cantidad(),
						producto.precio());

				items.add(itemPedido);
			}
		}

		String id = "PED-" + contador.incrementAndGet();

		Pedido pedido = new Pedido.Builder(id, request.clienteId(), request.documentoCliente(), request.pais(),
				request.contacto(), items).direccionEnvio(request.direccionEnvio())
				.recogerEnTienda(request.recogerEnTienda()).cupon(request.cupon()).notaRegalo(request.notaRegalo())
				.envioExpress(request.envioExpress()).canalNotificacion(request.canalNotificacion()).build();

		Comprobante comprobante = servicioFacturacion.facturar(pedido);

		pedidos.put(pedido.getId(), pedido);

		return new PedidoCreadoResponse(pedido, comprobante);
	}

	public Pedido buscar(String id) {

		Pedido pedido = pedidos.get(id);

		if (pedido == null) {
			throw new NoSuchElementException("No existe el pedido con ID: " + id);
		}

		return pedido;
	}

	public void notificarPagoAprobado(Pedido pedido, BigDecimal totalPagado) {

		notificadorPara(pedido.getCanalNotificacion()).notificarPagoAprobado(pedido.getContacto(), pedido.getId(),
				totalPagado);
	}

	private Notificador notificadorPara(String canal) {

		String clave = canal == null ? "" : canal.trim().toUpperCase(Locale.ROOT);
		Notificador notificador = notificadores.get(clave);

		if (notificador == null) {
			throw new IllegalArgumentException("Canal de notificación no soportado: " + canal);
		}

		return notificador;
	}
}
