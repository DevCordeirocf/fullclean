package com.mitica.fullclean.repository;

import com.mitica.fullclean.model.TesteTenant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteTenantRepository extends JpaRepository<TesteTenant, TesteTenant.TesteTenantId> {

    @Query("FROM TesteTenant")
    List<TesteTenant> findAllWithTenantFilter();
}
