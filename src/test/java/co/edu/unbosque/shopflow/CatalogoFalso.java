package co.edu.unbosque.shopflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import co.edu.unbosque.shopflow.catalogo.Producto;
import co.edu.unbosque.shopflow.catalogo.ServicioCatalogo;

public class CatalogoFalso implements ServicioCatalogo {

	public Map<String, Producto> productos = new HashMap<>();
	public int llamadas = 0;

	public CatalogoFalso() {
		productos.put("SKU-001", new Producto("SKU-001", "Teclado", new BigDecimal("85.00")));
		productos.put("SKU-002", new Producto("SKU-002", "Mouse", new BigDecimal("25.50")));
	}

	@Override
	public Producto buscar(String sku) {
		llamadas++;
		Producto producto = productos.get(sku);
		if (producto == null) {
			throw new NoSuchElementException("No existe el producto " + sku);
		}
		return producto;
	}

	@Override
	public List<Producto> listar() {
		llamadas++;
		return new ArrayList<>(productos.values());
	}

	@Override
	public Producto actualizarPrecio(String sku, BigDecimal nuevoPrecio) {
		llamadas++;
		Producto actualizado = new Producto(sku, productos.get(sku).nombre(), nuevoPrecio);
		productos.put(sku, actualizado);
		return actualizado;
	}
}
