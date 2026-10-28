package co.edu.unbosque.shopflow.pedidos;

import java.math.BigDecimal;

import java.util.List;

public final class Pedido {

	private final String id;
	private final String clienteId;
	private final String documentoCliente;
	private final String pais;
	private final String contacto;
	private final List<ItemPedido> items;
	private final String direccionEnvio;
	private final boolean recogerEnTienda;
	private final String cupon;
	private final String notaRegalo;
	private final boolean envioExpress;
	private final String canalNotificacion;
	private final BigDecimal subtotal;
	private final BigDecimal descuento;

	private Pedido(Builder builder) {
		this.id = builder.id;
		this.clienteId = builder.clienteId;
		this.documentoCliente = builder.documentoCliente;
		this.pais = builder.pais;
		this.contacto = builder.contacto;
		this.items = List.copyOf(builder.items);
		this.direccionEnvio = builder.direccionEnvio;
		this.recogerEnTienda = builder.recogerEnTienda;
		this.cupon = builder.cupon;
		this.notaRegalo = builder.notaRegalo;
		this.envioExpress = builder.envioExpress;
		this.canalNotificacion = builder.canalNotificacion;
		this.subtotal = calcularSubtotal();
		this.descuento = calcularDescuento();
	}

	private BigDecimal calcularSubtotal() {
		return items.stream().map(ItemPedido::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal calcularDescuento() {
		if ("BOSQUE10".equalsIgnoreCase(cupon)) {
			return subtotal.multiply(new BigDecimal("0.10"));
		}

		return BigDecimal.ZERO;
	}

	public BigDecimal totalSinImpuestos() {
		return subtotal.subtract(descuento);
	}

	public static class Builder {

		private final String id;
		private final String clienteId;
		private final String documentoCliente;
		private final String pais;
		private final String contacto;
		private final List<ItemPedido> items;

		private String direccionEnvio;
		private boolean recogerEnTienda = false;
		private String cupon;
		private String notaRegalo;
		private boolean envioExpress = false;
		private String canalNotificacion = "EMAIL";

		public Builder(String id, String clienteId, String documentoCliente, String pais, String contacto,
				List<ItemPedido> items) {

			this.id = id;
			this.clienteId = clienteId;
			this.documentoCliente = documentoCliente;
			this.pais = pais;
			this.contacto = contacto;
			this.items = items == null ? null : List.copyOf(items);
		}

		public Builder direccionEnvio(String direccionEnvio) {
			this.direccionEnvio = direccionEnvio;
			return this;
		}

		public Builder recogerEnTienda(Boolean recogerEnTienda) {
			this.recogerEnTienda = Boolean.TRUE.equals(recogerEnTienda);
			return this;
		}

		public Builder cupon(String cupon) {
			this.cupon = cupon;
			return this;
		}

		public Builder notaRegalo(String notaRegalo) {
			this.notaRegalo = notaRegalo;
			return this;
		}

		public Builder envioExpress(Boolean envioExpress) {
			this.envioExpress = Boolean.TRUE.equals(envioExpress);
			return this;
		}

		public Builder canalNotificacion(String canalNotificacion) {
			if (canalNotificacion != null) {
				this.canalNotificacion = canalNotificacion;
			}
			return this;
		}

		public Pedido build() {
			validar();
			return new Pedido(this);
		}

		private void validar() {
			validarTexto(id, "El ID del pedido es obligatorio");
			validarTexto(clienteId, "El cliente es obligatorio");
			validarTexto(documentoCliente, "El documento del cliente es obligatorio");
			validarTexto(pais, "El país es obligatorio");
			validarTexto(contacto, "El contacto es obligatorio");

			if (items == null || items.isEmpty()) {
				throw new IllegalArgumentException("El pedido debe tener al menos un ítem");
			}

			if (!recogerEnTienda && (direccionEnvio == null || direccionEnvio.isBlank())) {
				throw new IllegalArgumentException("La dirección de envío es obligatoria para pedidos a domicilio");
			}

			if (recogerEnTienda && envioExpress) {
				throw new IllegalArgumentException("Un pedido para recoger en tienda no puede tener envío express");
			}
			
			if (!recogerEnTienda && !envioExpress) {
				throw new IllegalArgumentException("Debe escoger un método de entrega");
			}

			if (notaRegalo != null && notaRegalo.length() > 140) {
				throw new IllegalArgumentException("La nota de regalo no puede superar los 140 caracteres");
			}

			if (canalNotificacion == null || canalNotificacion.isBlank()) {
				throw new IllegalArgumentException("El canal de notificación es obligatorio");
			}

			if (!List.of("CO", "MX", "PE").contains(pais.toUpperCase())) {
				throw new IllegalArgumentException("El país no es válido");
			}
		}

		private void validarTexto(String valor, String mensaje) {
			if (valor == null || valor.isBlank()) {
				throw new IllegalArgumentException(mensaje);
			}
		}
	}

	public String getId() {
		return id;
	}

	public String getClienteId() {
		return clienteId;
	}

	public String getDocumentoCliente() {
		return documentoCliente;
	}

	public String getPais() {
		return pais;
	}

	public String getContacto() {
		return contacto;
	}

	public List<ItemPedido> getItems() {
		return items;
	}

	public String getDireccionEnvio() {
		return direccionEnvio;
	}

	public boolean isRecogerEnTienda() {
		return recogerEnTienda;
	}

	public String getCupon() {
		return cupon;
	}

	public String getNotaRegalo() {
		return notaRegalo;
	}

	public boolean isEnvioExpress() {
		return envioExpress;
	}

	public String getCanalNotificacion() {
		return canalNotificacion;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public BigDecimal getDescuento() {
		return descuento;
	}
}