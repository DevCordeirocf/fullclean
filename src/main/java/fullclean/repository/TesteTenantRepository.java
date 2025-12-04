package fullclean.repository;

import fullclean.entity.TesteTenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteTenantRepository extends JpaRepository<TesteTenant, Long> {

    @Query("SELECT t FROM TesteTenant t WHERE t.tenantId = :tenantId")
    List<TesteTenant> findAllByTenantId(@Param("tenantId") String tenantId);
}
