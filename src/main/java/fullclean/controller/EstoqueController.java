package fullclean.controller;

import fullclean.domain.model.Estoque;
import fullclean.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Estoque> criar(@RequestParam String produtoId, @RequestParam Integer qtd) {
        return ResponseEntity.ok(service.inicializarEstoque(produtoId, qtd));
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<Estoque> consultar(@PathVariable String produtoId) {
        return ResponseEntity.ok(service.consultarEstoque(produtoId));
    }
}