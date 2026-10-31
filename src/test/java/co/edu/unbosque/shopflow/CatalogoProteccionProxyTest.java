package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.catalogo.CatalogoProteccionProxy;
import co.edu.unbosque.shopflow.catalogo.Producto;
import co.edu.unbosque.shopflow.comun.AccesoDenegadoException;

class CatalogoProteccionProxyTest {

	@Test
	void unClienteNoPuedeCambiarPrecios() {
		CatalogoFalso real = new CatalogoFalso();
		CatalogoProteccionProxy proxy = new CatalogoProteccionProxy(real, "CLIENTE");

		assertThrows(AccesoDenegadoException.class, () -> proxy.actualizarPrecio("SKU-001", new BigDecimal("5.00")));
		assertEquals(0, real.llamadas);
	}

	@Test
	void sinRolNoSePuedeCambiarPrecios() {
		CatalogoFalso real = new CatalogoFalso();
		CatalogoProteccionProxy proxy = new CatalogoProteccionProxy(real, null);

		assertThrows(AccesoDenegadoException.class, () -> proxy.actualizarPrecio("SKU-001", new BigDecimal("5.00")));
		assertEquals(0, real.llamadas);
	}

	@Test
	void unAdminSiPuedeCambiarPrecios() {
		CatalogoFalso real = new CatalogoFalso();
		CatalogoProteccionProxy proxy = new CatalogoProteccionProxy(real, "ADMIN");

		Producto actualizado = proxy.actualizarPrecio("SKU-001", new BigDecimal("5.00"));

		assertEquals(new BigDecimal("5.00"), actualizado.precio());
	}

	@Test
	void cualquierRolPuedeConsultarElCatalogo() {
		CatalogoProteccionProxy proxy = new CatalogoProteccionProxy(new CatalogoFalso(), "CLIENTE");

		assertEquals("SKU-001", proxy.buscar("SKU-001").sku());
		assertEquals(2, proxy.listar().size());
	}
}
