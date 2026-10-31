package co.edu.unbosque.shopflow.pagos;

import java.util.Locale;
import java.util.Map;

public class PasarelasDisponibles {

    private final Map<String, PasarelaDePago> porProveedor;

    public PasarelasDisponibles(Map<String, PasarelaDePago> porProveedor) {
        this.porProveedor = Map.copyOf(porProveedor);
    }

    public PasarelaDePago obtener(String proveedor) {
        if (proveedor == null || proveedor.isBlank()) {
            throw new IllegalArgumentException("El proveedor de pago es obligatorio");
        }
        PasarelaDePago pasarela = porProveedor.get(proveedor.trim().toUpperCase(Locale.ROOT));
        if (pasarela == null) {
            throw new IllegalArgumentException("Proveedor de pago no soportado: " + proveedor
                    + ". Opciones: " + porProveedor.keySet());
        }
        return pasarela;
    }
}