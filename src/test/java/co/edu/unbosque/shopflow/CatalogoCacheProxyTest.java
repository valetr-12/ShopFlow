package co.edu.unbosque.shopflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.catalogo.CatalogoCacheProxy;
import co.edu.unbosque.shopflow.catalogo.Producto;

class CatalogoCacheProxyTest {

	@Test
	void dosBusquedasDelMismoSkuLlamanUnaSolaVezAlCatalogoReal() {
		CatalogoFalso real = new CatalogoFalso();
		CatalogoCacheProxy proxy = new CatalogoCacheProxy(real);

		Producto primera = proxy.buscar("SKU-001");
		Producto segunda = proxy.buscar("SKU-001");

		assertEquals(1, real.llamadas);
		assertEquals(primera, segunda);
	}

	@Test
	void unSkuInexistenteNoSeGuardaEnCache() {
		CatalogoFalso real = new CatalogoFalso();
		CatalogoCacheProxy proxy = new CatalogoCacheProxy(real);

		assertThrows(NoSuchElementException.class, () -> proxy.buscar("SKU-999"));
		assertThrows(NoSuchElementException.class, () -> proxy.buscar("SKU-999"));

		assertEquals(2, real.llamadas);
	}

	@Test
	void alCambiarUnPrecioLaCacheNoSirveElPrecioViejo() {
		CatalogoFalso real = new CatalogoFalso();
		CatalogoCacheProxy proxy = new CatalogoCacheProxy(real);

		proxy.buscar("SKU-001");
		proxy.actualizarPrecio("SKU-001", new BigDecimal("99.90"));
		Producto consultado = proxy.buscar("SKU-001");

		assertEquals(new BigDecimal("99.90"), consultado.precio());
		assertEquals(2, real.llamadas);
	}

	@Test
	void listarCargaLaCacheYLasBusquedasSiguientesNoVuelvenAlCatalogoReal() {
		CatalogoFalso real = new CatalogoFalso();
		CatalogoCacheProxy proxy = new CatalogoCacheProxy(real);

		assertEquals(2, proxy.listar().size());
		proxy.buscar("SKU-002");
		proxy.listar();

		assertEquals(1, real.llamadas);
	}
}
