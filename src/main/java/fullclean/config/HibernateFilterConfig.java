package fullclean.config;

import fullclean.security.TenantContext;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * HibernateFilterConfig - Configuração para habilitar o filtro de tenant
 * em todas as sessões do Hibernate.
 */
@Configuration
public class HibernateFilterConfig implements WebMvcConfigurer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Interceptor que habilita o filtro do Hibernate antes de cada requisição
     * e o desabilita após a requisição.
     */
    private HandlerInterceptor tenantFilterInterceptor() {
        return (request, response, handler) -> {
            if (TenantContext.isSet()) {
                SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
                Session session = sessionFactory.getCurrentSession();

                // Habilita o filtro 'tenantFilter' definido na entidade TesteTenant
                session.enableFilter("tenantFilter")
                       // Define o parâmetro 'currentTenantId' com o valor do TenantContext
                       .setParameter("currentTenantId", TenantContext.getTenantId());
            }
            return true;
        };
    }

    /**
     * Registra o interceptor para ser executado em todas as requisições
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantFilterInterceptor());
    }
}
