package fullclean.config;

import fullclean.security.TenantContext;
import org.hibernate.Session;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Consumer;


@Configuration
public class HibernateFilterConfig implements HibernatePropertiesCustomizer {

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.session_customizer", (Consumer<Session>) session -> {
            if (TenantContext.isSet()) {
                session.enableFilter("tenantFilter")
                       .setParameter("currentTenantId", TenantContext.getTenantId());
            }
        });
        

        // Removido fallback DEFAULT_TENANT para evitar mascaramento de requisições sem tenant
        hibernateProperties.put("hibernate.tenant_identifier_resolver", new CurrentTenantIdentifierResolver() {
            @Override
            public String resolveCurrentTenantIdentifier() {
                return TenantContext.getTenantId();
            }

            @Override
            public boolean validateExistingCurrentSessions() {
                return true;
            }
        });
    }
}
