package fullclean.controller;

import fullclean.entity.TesteTenant;
import fullclean.service.TesteTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TesteTenantController - Controller REST para testar o isolamento multi-tenant.
 */
@RestController
@RequestMapping("/api/teste-tenant")
public class TesteTenantController {

    @Autowired
    private TesteTenantService service;

    /**
     * Endpoint para salvar uma nova joia.
     * O tenantId é injetado automaticamente pelo Service.
     * @param joia Dados da joia a ser salva.
     * @return A joia salva com o ID.
     */
    @PostMapping
    public ResponseEntity<TesteTenant> create(@RequestBody TesteTenant joia) {
        TesteTenant savedJoia = service.save(joia);
        return new ResponseEntity<>(savedJoia, HttpStatus.CREATED);
    }

    /**
     * Endpoint para buscar todas as joias.
     * CRÍTICO: Este endpoint PROVA o isolamento.
     * Ele só retornará os itens que pertencem ao 'X-Tenant-ID' enviado no header.
     * @return Lista de joias do tenant atual.
     */
    @GetMapping
    public ResponseEntity<List<TesteTenant>> findAll() {
        List<TesteTenant> joias = service.findAll();
        return ResponseEntity.ok(joias);
    }
}
