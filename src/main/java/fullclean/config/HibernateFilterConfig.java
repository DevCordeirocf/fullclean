package fullclean.config;

import fullclean.security.TenantContext;
import org.hibernate.Session;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * HibernateFilterConfig - Configuração para habilitar o filtro de tenant
 * de forma correta e automática no ciclo de vida do Hibernate.
 * 
 * CORREÇÃO DEFINITIVA 2.0: Usa HibernatePropertiesCustomizer para injetar
 * um SessionCustomizer que habilita o filtro na Session.
 */
@Configuration
public class HibernateFilterConfig implements HibernatePropertiesCustomizer {

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        // 1. Adiciona o SessionCustomizer para habilitar o filtro
        hibernateProperties.put("hibernate.session_factory.session_customizer", (session) -> {
            if (TenantContext.isSet()) {
                session.enableFilter("tenantFilter")
                       .setParameter("currentTenantId", TenantContext.getTenantId());
            }
        });
        
        // 2. Adiciona o CurrentTenantIdentifierResolver (embora não seja o padrão Multi-Tenant)
        // Isso garante que o Hibernate saiba que estamos em um contexto multi-tenant
        hibernateProperties.put("hibernate.tenant_identifier_resolver", new CurrentTenantIdentifierResolver() {
            @Override
            public String resolveCurrentTenantIdentifier() {
                String tenantId = TenantContext.getTenantId();
                return tenantId != null ? tenantId : "DEFAULT_TENANT"; // Retorna um valor padrão se não estiver definido
            }

            @Override
            public boolean validateExistingCurrentSessions() {
                return true;
            }
        });
    }
}
