package co.edu.unbosque.shopflow.pedidos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping
	public ResponseEntity<PedidoCreadoResponse> crear(@RequestBody PedidoRequest request) {

		PedidoCreadoResponse respuesta = pedidoService.crear(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> buscar(@PathVariable String id) {

		Pedido pedido = pedidoService.buscar(id);

		return ResponseEntity.ok(pedido);
	}
}
