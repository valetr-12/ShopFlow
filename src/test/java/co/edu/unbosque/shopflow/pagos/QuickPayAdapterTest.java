package co.edu.unbosque.shopflow.pagos;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import co.edu.unbosque.shopflow.externos.quickpay.QuickPaySdk;

class QuickPayAdapterTest {

    private final PasarelaDePago pasarela = new QuickPayAdapter(new QuickPaySdk());

    @Test
    void apruebaUnCobroNormal() {
        ResultadoPago r = pasarela.cobrar(new BigDecimal("3451.00"), "PED-1");
        assertTrue(r.aprobado());
        assertTrue(r.autorizacion().startsWith("QP-"));
    }

    @Test
    void rechazaSiSuperaCincoMilDolares() {
        ResultadoPago r = pasarela.cobrar(new BigDecimal("6000.00"), "PED-2");
        assertFalse(r.aprobado());
    }
}