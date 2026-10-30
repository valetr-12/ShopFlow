package co.edu.unbosque.shopflow.pagos;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AuditoriaPagoTest {

    @Test
    void conAuditoriaPorFueraDelReintentoHayUnaSolaEntradaPorCobro() {
        int[] llamadas = {0};
        PasarelaDePago inestable = (monto, ref) -> {
            if (++llamadas[0] < 3) throw new PasarelaNoDisponibleException("caída", null);
            return ResultadoPago.aprobado("AUT-1");
        };
        RegistroAuditoria registro = new RegistroAuditoria();

        PasarelaDePago pasarela =
                new AuditoriaPago(new ReintentoPago(inestable, 3), "LEGACYPAY", registro);
        pasarela.cobrar(BigDecimal.TEN, "PED-1");

        assertEquals(3, llamadas[0]);               
        assertEquals(1, registro.todas().size());     
        assertEquals("APROBADO", registro.todas().get(0).resultado());
    }

    @Test
    void registraAunqueLaPasarelaEsteCaida() {
        PasarelaDePago caida = (monto, ref) -> {
            throw new PasarelaNoDisponibleException("caída", null);
        };
        RegistroAuditoria registro = new RegistroAuditoria();

        assertThrows(PasarelaNoDisponibleException.class,
                () -> new AuditoriaPago(caida, "LEGACYPAY", registro).cobrar(BigDecimal.TEN, "PED-2"));
        assertEquals("NO DISPONIBLE", registro.todas().get(0).resultado());
    }
}