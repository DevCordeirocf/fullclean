package fullclean.repository;

import fullclean.entity.TesteTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * TesteTenantRepository - Repositório JPA para a entidade TesteTenant.
 * 
 * O isolamento de dados é garantido automaticamente pelo Hibernate Filter
// configurado na entidade e no HibernateFilterConfig.
// O método findAllByTenantId é usado para garantir o isolamento,
// pois o filtro dinâmico do Hibernate pode falhar em algumas operações do Spring Data JPA.
 */
@Repository
public interface TesteTenantRepository extends JpaRepository<TesteTenant, Long> {

    @Query("SELECT t FROM TesteTenant t WHERE t.tenantId = :tenantId")
    List<TesteTenant> findAllByTenantId(@Param("tenantId") String tenantId);
    // Não é necessário adicionar métodos de busca por tenantId,
    // pois o filtro do Hibernate faz isso automaticamente em TODAS as queries.
}
