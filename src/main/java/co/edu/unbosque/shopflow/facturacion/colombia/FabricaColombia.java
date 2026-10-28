package co.edu.unbosque.shopflow.facturacion.colombia;

import org.springframework.stereotype.Component;

import co.edu.unbosque.shopflow.*;
import co.edu.unbosque.shopflow.facturacion.CalculadoraImpuestos;
import co.edu.unbosque.shopflow.facturacion.FabricaFacturacion;
import co.edu.unbosque.shopflow.facturacion.GeneradorComprobante;
import co.edu.unbosque.shopflow.facturacion.ValidadorIdentificacion;

@Component
public class FabricaColombia implements FabricaFacturacion {

	@Override
	public String pais() {
		return "CO";
	}

	@Override
	public CalculadoraImpuestos crearCalculadora() {
		return new CalculadoraImpuestosColombia();
	}

	@Override
	public ValidadorIdentificacion crearValidador() {
		return new ValidadorIdentificacionColombia();
	}

	@Override
	public GeneradorComprobante crearGenerador() {
		return new GeneradorComprobanteColombia();
	}
}
