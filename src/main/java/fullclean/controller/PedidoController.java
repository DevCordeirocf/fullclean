package fullclean.controller;

import fullclean.domain.model.Pedido;
import fullclean.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody List<PedidoService.DadosItemPedido> itens) {
        try {
            Pedido pedido = service.criarPedido(itens);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao processar pedido: " + e.getMessage());
        }
    }
}