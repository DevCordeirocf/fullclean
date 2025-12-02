package fullclean.service;

import fullclean.entity.TesteTenant;
import fullclean.repository.TesteTenantRepository;
import fullclean.security.TenantContext;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TesteTenantService - Lógica de negócio para a entidade TesteTenant.
 */
@Service
public class TesteTenantService {

    @PersistenceContext
    private EntityManager entityManager;

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
        String tenantId = TenantContext.getTenantId();
        
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID não está definido no contexto da requisição.");
        }
        
        // Usa o método do repositório com query explícita para garantir o isolamento.
        return repository.findAllByTenantId(tenantId);
    }
}
