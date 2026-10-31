package co.edu.unbosque.shopflow.catalogo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CatalogoCacheProxy implements ServicioCatalogo {

	private final ServicioCatalogo real;
	private final Map<String, Producto> cache = new ConcurrentHashMap<>();
	private boolean listaCargada = false;

	public CatalogoCacheProxy(ServicioCatalogo real) {
		this.real = real;
	}

	@Override
	public Producto buscar(String sku) {
		Producto producto = cache.get(sku);
		if (producto == null) {
			producto = real.buscar(sku);
			cache.put(sku, producto);
		}
		return producto;
	}

	@Override
	public List<Producto> listar() {
		if (!listaCargada) {
			for (Producto producto : real.listar()) {
				cache.put(producto.sku(), producto);
			}
			listaCargada = true;
		}
		return new ArrayList<>(cache.values());
	}

	@Override
	public Producto actualizarPrecio(String sku, BigDecimal nuevoPrecio) {
		Producto actualizado = real.actualizarPrecio(sku, nuevoPrecio);
		cache.put(sku, actualizado);
		return actualizado;
	}
}
