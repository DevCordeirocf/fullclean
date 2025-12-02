package fullclean.repository;

import fullclean.entity.TesteTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TesteTenantRepository - Repositório JPA para a entidade TesteTenant.
 * 
 * O isolamento de dados é garantido automaticamente pelo Hibernate Filter
 * configurado na entidade e no HibernateFilterConfig.
 */
@Repository
public interface TesteTenantRepository extends JpaRepository<TesteTenant, Long> {
    // Não é necessário adicionar métodos de busca por tenantId,
    // pois o filtro do Hibernate faz isso automaticamente em TODAS as queries.
}
