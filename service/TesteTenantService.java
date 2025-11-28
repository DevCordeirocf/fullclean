package fullclean.service;

import fullclean.model.TesteTenant;
import fullclean.repository.TesteTenantRepository;
import fullclean.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TesteTenantService {

    private final TesteTenantRepository repository;

    @Autowired
    public TesteTenantService(TesteTenantRepository repository) {
        this.repository = repository;
    }

    public TesteTenant salvar(TesteTenant testeTenant) {
        // Lógica de negócio: setar o Tenant ID antes de salvar
        testeTenant.setTenantId(TenantContext.getTenantId());
        return repository.save(testeTenant);
    }

    public List<TesteTenant> buscarTodos() {
        // Apenas delega para o repositório, que usará o filtro de Tenant ID
        return repository.findAllWithTenantFilter();
    }
}
