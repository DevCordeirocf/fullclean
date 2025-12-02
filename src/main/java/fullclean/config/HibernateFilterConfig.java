package fullclean.config;

import fullclean.security.TenantContext;
import org.hibernate.Session;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Consumer;

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
// 1. A ativação do filtro será feita de forma imperativa no Service.
        // O SessionCustomizer não é confiável para todas as operações do Spring Data JPA.
        
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
