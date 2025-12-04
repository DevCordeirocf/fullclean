package fullclean.service;

import fullclean.entity.TesteTenant;
import fullclean.repository.TesteTenantRepository;
import fullclean.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;


@Service
public class TesteTenantService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TesteTenantRepository repository;


    @Transactional
    public TesteTenant save(TesteTenant joia) {

        joia.setTenantId(TenantContext.getTenantId());
        return repository.save(joia);
    }

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
