package fullclean.service;

import fullclean.entity.TesteTenant;
import fullclean.repository.TesteTenantRepository;
import fullclean.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TesteTenantService - Lógica de negócio para a entidade TesteTenant.
 */
@Service
public class TesteTenantService {

    @Autowired
    private TesteTenantRepository repository;

    /**
     * Salva uma nova joia, injetando o tenantId automaticamente.
     * @param joia A entidade TesteTenant a ser salva.
     * @return A entidade salva.
     */
    @Transactional
    public TesteTenant save(TesteTenant joia) {
        // Garante que o tenantId seja injetado no objeto antes de salvar
        // Isso é necessário para a primeira inserção no banco de dados
        joia.setTenantId(TenantContext.getTenantId());
        return repository.save(joia);
    }

    /**
     * Busca todas as joias.
     * O isolamento é garantido pelo Hibernate Filter.
     * @return Lista de joias pertencentes APENAS ao tenant da requisição.
     */
    @Transactional(readOnly = true)
    public List<TesteTenant> findAll() {
        return repository.findAll();
    }
}
