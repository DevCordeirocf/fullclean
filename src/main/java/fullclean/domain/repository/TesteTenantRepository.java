package fullclean.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fullclean.domain.model.TesteTenant;

@Repository
public interface TesteTenantRepository extends JpaRepository<TesteTenant, Long> {

    List<TesteTenant> findAllByTenantId(String tenantId);
}
