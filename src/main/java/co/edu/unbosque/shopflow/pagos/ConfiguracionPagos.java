package co.edu.unbosque.shopflow.pagos;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionPagos {

    @Value("${shopflow.legacypay.probabilidad-timeout:0.3}")
    private double probabilidadTimeout;

    @Value("${shopflow.pagos.reintentos:true}")
    private boolean reintentos;

    @Value("${shopflow.pagos.max-intentos:3}")
    private int maxIntentos;

    @Value("${shopflow.pagos.auditoria:true}")
    private boolean auditoria;
    
    @Value("${shopflow.pagos.metricas:true}")
    private boolean metricas;

    @Bean
    public PasarelasDisponibles pasarelasDisponibles(RegistroAuditoria registro) {
        Map<String, PasarelaDePago> pasarelas = new LinkedHashMap<>();
        pasarelas.put("LEGACYPAY", armar("LEGACYPAY", new LegacyPayAdapter(probabilidadTimeout), registro));
        pasarelas.put("QUICKPAY", armar("QUICKPAY", new QuickPayAdapter(), registro));
        return new PasarelasDisponibles(pasarelas);
    }


    private PasarelaDePago armar(String proveedor, PasarelaDePago adapter, RegistroAuditoria registro) {
        PasarelaDePago pasarela = adapter;
        if (reintentos) {
            pasarela = new ReintentoPago(pasarela, maxIntentos);        
        }
        if (auditoria) {
            pasarela = new AuditoriaPago(pasarela, proveedor, registro);  
        }
        if (metricas) {
            pasarela = new MetricasPago(pasarela, proveedor);            
        }
        return pasarela;
    }
}