package co.edu.unbosque.shopflow.pagos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;


@Component
public class RegistroAuditoria {

    public record EntradaAuditoria(String proveedor, String referencia, BigDecimal monto,
            String resultado, long duracionMs, LocalDateTime fecha) { }

    private final List<EntradaAuditoria> entradas = new CopyOnWriteArrayList<>();

    public void registrar(String proveedor, String referencia, BigDecimal monto,
            String resultado, long duracionMs) {
        entradas.add(new EntradaAuditoria(proveedor, referencia, monto, resultado,
                duracionMs, LocalDateTime.now()));
    }

    public List<EntradaAuditoria> todas() {
        return List.copyOf(entradas);
    }
}