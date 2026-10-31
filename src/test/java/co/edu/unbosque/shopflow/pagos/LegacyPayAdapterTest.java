package co.edu.unbosque.shopflow.pagos;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayClient;

class LegacyPayAdapterTest {

    @Test
    void apruebaUnCobroNormal() {
        PasarelaDePago pasarela = new LegacyPayAdapter(new LegacyPayClient(0.0)); 
        ResultadoPago r = pasarela.cobrar(new BigDecimal("145.66"), "PED-1");
        assertTrue(r.aprobado());
        assertTrue(r.autorizacion().startsWith("LP"));
    }

    @Test
    void rechazaPorFondosInsuficientesSiSuperaDosMilDolares() {
        PasarelaDePago pasarela = new LegacyPayAdapter(new LegacyPayClient(0.0));
        ResultadoPago r = pasarela.cobrar(new BigDecimal("2500.00"), "PED-2");
        assertFalse(r.aprobado());
        assertEquals("Fondos insuficientes", r.mensaje());
    }

    @Test
    void traduceElTimeoutDelSdkAExcepcionPropia() {
        PasarelaDePago pasarela = new LegacyPayAdapter(new LegacyPayClient(1.0)); 
        assertThrows(PasarelaNoDisponibleException.class,
                () -> pasarela.cobrar(BigDecimal.TEN, "PED-3"));
    }
}