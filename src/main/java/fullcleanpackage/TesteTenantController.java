package fullcleanpackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teste")
public class TesteTenantController {

    @Autowired
    private TesteTenantRepository repository;

    @PostMapping("/salvar")
    public TesteTenant salvar(@RequestBody TesteTenant testeTenant) {
        // O TenantIdFilter já deve ter setado o Tenant ID no contexto
        // Mas para o teste, vamos garantir que o objeto tenha o ID correto
        testeTenant.setTenantId(TenantContext.getTenantId());
        return repository.save(testeTenant);
    }

    @GetMapping("/todos")
    public List<TesteTenant> buscarTodos() {
    // Agora, usamos a query que garante o uso do filtro do Hibernate
    return repository.findAllWithTenantFilter(); // <--- Alterado
}
}
