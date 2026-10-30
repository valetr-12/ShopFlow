package co.edu.unbosque.shopflow.pagos;

import co.edu.unbosque.shopflow.externos.quickpay.QuickPayRequest;
import co.edu.unbosque.shopflow.externos.quickpay.QuickPayResult;
import co.edu.unbosque.shopflow.externos.quickpay.QuickPaySdk;

import java.math.BigDecimal;


public class QuickPayAdapter implements PasarelaDePago {

    private static final String MONEDA = "USD";

    private final QuickPaySdk sdk;

    public QuickPayAdapter(QuickPaySdk sdk) {
        this.sdk = sdk;
    }

    @Override
    public ResultadoPago cobrar(BigDecimal montoUsd, String referencia) {
        QuickPayResult r = sdk.pay(new QuickPayRequest(referencia, montoUsd.doubleValue(), MONEDA));

        return switch (r.status()) {
            case OK -> ResultadoPago.aprobado(r.transactionId());
            case REJECTED -> ResultadoPago.rechazado("Pago rechazado por QuickPay: " + r.detail());
            case ERROR -> ResultadoPago.rechazado("Error en QuickPay: " + r.detail());
        };
    }
}