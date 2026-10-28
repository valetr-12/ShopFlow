package co.edu.unbosque.shopflow.facturacion;

import co.edu.unbosque.shopflow.pedidos.Pedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ServicioFacturacion {

	private final Map<String, FabricaFacturacion> fabricas = new HashMap<>();

	public ServicioFacturacion(List<FabricaFacturacion> fabricasDisponibles) {
		for (FabricaFacturacion fabrica : fabricasDisponibles) {
			String codigo = fabrica.pais().toUpperCase(Locale.ROOT);
			this.fabricas.put(codigo, fabrica);
		}
	}

	public Comprobante facturar(Pedido pedido) {

		String pais = pedido.getPais().toUpperCase(Locale.ROOT);
		FabricaFacturacion fabrica = fabricas.get(pais);

		if (fabrica == null) {
			throw new IllegalArgumentException("No existe una fábrica de facturación para el país: " + pais);
		}

		CalculadoraImpuestos calculadora = fabrica.crearCalculadora();

		ValidadorIdentificacion validador = fabrica.crearValidador();

		GeneradorComprobante generador = fabrica.crearGenerador();

		if (!validador.validar(pedido.getDocumentoCliente())) {
			throw new IllegalArgumentException("El documento de identificación no es válido para " + pais);
		}

		// Se calcula el IVA sobre el subtotal después del descuento.
		BigDecimal baseImponible = pedido.totalSinImpuestos();

		BigDecimal valorIva = calculadora.calcular(baseImponible);

		BigDecimal total = baseImponible.add(valorIva);

		return generador.generar(pedido, baseImponible, valorIva, total);
	}
}