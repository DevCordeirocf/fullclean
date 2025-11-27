package com.mitica.fullclean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/joias")
public class JoiaController {

    @Autowired
    private JoiaRepository repository;

    @PostMapping("/salvar")
    public Joia salvar(@RequestBody Joia joia) {
        // O TenantIdFilter já setou o Tenant ID no contexto
        // O TenantFilter garante que o filtro de Hibernate está ativo
        joia.setTenantId(TenantContext.getTenantId());
        return repository.save(joia);
    }

    @GetMapping("/todas")
    public List<Joia> buscarTodas() {
        // O filtro de Hibernate deve garantir que apenas as joias do tenant atual sejam retornadas
        return repository.findAll();
    }
}
