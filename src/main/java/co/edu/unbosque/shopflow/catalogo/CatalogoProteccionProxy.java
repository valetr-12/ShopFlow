package co.edu.unbosque.shopflow.catalogo;

import java.math.BigDecimal;
import java.util.List;

import co.edu.unbosque.shopflow.comun.AccesoDenegadoException;

public class CatalogoProteccionProxy implements ServicioCatalogo {

	private final ServicioCatalogo real;
	private final String rol;

	public CatalogoProteccionProxy(ServicioCatalogo real, String rol) {
		this.real = real;
		this.rol = rol;
	}

	@Override
	public Producto buscar(String sku) {
		return real.buscar(sku);
	}

	@Override
	public List<Producto> listar() {
		return real.listar();
	}

	@Override
	public Producto actualizarPrecio(String sku, BigDecimal nuevoPrecio) {
		if (rol == null || !rol.equalsIgnoreCase("ADMIN")) {
			throw new AccesoDenegadoException("Solo un usuario con rol ADMIN puede cambiar precios");
		}
		return real.actualizarPrecio(sku, nuevoPrecio);
	}
}
