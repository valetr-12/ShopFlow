package co.edu.unbosque.shopflow.pagos;

import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayClient;
import co.edu.unbosque.shopflow.externos.legacypay.LegacyPayTimeoutException;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class LegacyPayAdapter implements PasarelaDePago {

    private static final String MONEDA = "USD";
    private static final String APROBADO = "00";
    private static final String FONDOS_INSUFICIENTES = "51";

    private final LegacyPayClient cliente;   

    public LegacyPayAdapter(LegacyPayClient cliente) {
        this.cliente = cliente;
    }

    public LegacyPayAdapter(double probabilidadTimeout) {
        this(new LegacyPayClient(probabilidadTimeout));
    }
    
    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        long centavos = montoUsd.movePointRight(2)
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();

        String respuesta;
        try {
            respuesta = cliente.makeCharge(centavos, MONEDA, referencia);
        } catch (LegacyPayTimeoutException e) {
            throw new PasarelaNoDisponibleException("LegacyPay no está disponible", e);
        }

        String[] partes = respuesta.split("\\|", -1);
        String codigo = partes[0];
        String autorizacion = partes.length > 1 ? partes[1] : "";

        return switch (codigo) {
            case APROBADO -> ResultadoPago.aprobado(autorizacion);
            case FONDOS_INSUFICIENTES -> ResultadoPago.rechazado("Fondos insuficientes");
            default -> ResultadoPago.rechazado("Transacción no permitida (código " + codigo + ")");
        };
    }
}