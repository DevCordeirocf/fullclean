package fullclean.service;

import fullclean.entity.TesteTenant;
import fullclean.repository.TesteTenantRepository;
import fullclean.security.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
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
        // CORREÇÃO: Ativar o filtro do Hibernate manualmente na sessão atual
        // Isso é necessário porque o SessionCustomizer não é confiável para todas as operações do Spring Data JPA.
        Session session = entityManager.unwrap(Session.class);
        if (TenantContext.isSet()) {
            session.enableFilter("tenantFilter")
                   .setParameter("currentTenantId", TenantContext.getTenantId());
        }
        
        return repository.findAll();
    }
}
