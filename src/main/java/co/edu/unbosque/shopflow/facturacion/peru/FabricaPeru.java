package co.edu.unbosque.shopflow.facturacion.peru;

import co.edu.unbosque.shopflow.facturacion.CalculadoraImpuestos;
import co.edu.unbosque.shopflow.facturacion.FabricaFacturacion;
import co.edu.unbosque.shopflow.facturacion.GeneradorComprobante;
import co.edu.unbosque.shopflow.facturacion.ValidadorIdentificacion;

import org.springframework.stereotype.Component;

@Component
public class FabricaPeru implements FabricaFacturacion {

	@Override
	public String pais() {
		return "PE";
	}

	@Override
	public CalculadoraImpuestos crearCalculadora() {
		return new CalculadoraImpuestosPeru();
	}

	@Override
	public ValidadorIdentificacion crearValidador() {
		return new ValidadorIdentificacionPeru();
	}

	@Override
	public GeneradorComprobante crearGenerador() {
		return new GeneradorComprobantePeru();
	}
}
