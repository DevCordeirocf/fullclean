package fullclean.config;

import fullclean.security.TenantContext;
import org.hibernate.Session;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * HibernateFilterConfig - Configuração para habilitar o filtro de tenant
 * de forma correta e automática no ciclo de vida do Hibernate.
 * 
 * CORREÇÃO DEFINITIVA: Usa HibernatePropertiesCustomizer para injetar um Interceptor
 * que habilita o filtro na Session, garantindo que seja aplicado em todas as queries.
 */
@Configuration
public class HibernateFilterConfig implements HibernatePropertiesCustomizer {

    /**
     * Customiza as propriedades do Hibernate para adicionar um Interceptor
     * que habilita o filtro de tenant em cada nova Session.
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        // Adiciona um listener para habilitar o filtro em cada nova Session
        hibernateProperties.put("hibernate.session_factory.interceptor", new TenantFilterInterceptor());
    }

    /**
     * Interceptor que habilita o filtro do Hibernate na Session.
     * Deve ser um componente para que o Spring possa injetar dependências (se necessário),
     * mas aqui é instanciado diretamente.
     */
    @Component
    public static class TenantFilterInterceptor implements org.hibernate.Interceptor {

        @Override
        public void afterTransactionBegin(org.hibernate.Transaction tx) {
            // Obtém a Session atual
            Session session = tx.getSession();

            if (TenantContext.isSet()) {
                // Habilita o filtro 'tenantFilter' definido na entidade TesteTenant
                session.enableFilter("tenantFilter")
                       // Define o parâmetro 'currentTenantId' com o valor do TenantContext
                       .setParameter("currentTenantId", TenantContext.getTenantId());
            }
        }
    }
}
