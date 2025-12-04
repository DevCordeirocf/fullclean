package fullclean.repository;

import fullclean.entity.TesteTenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteTenantRepository extends JpaRepository<TesteTenant, Long> {

    List<TesteTenant> findAllByTenantId(String tenantId);
}
