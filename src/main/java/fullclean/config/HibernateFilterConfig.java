package fullclean.config;

import fullclean.security.TenantContext;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * HibernateFilterConfig - Configuração para habilitar o filtro de tenant
 * em todas as sessões do Hibernate.
 * 
 * CORREÇÃO: Usa um HandlerInterceptor para habilitar o filtro na Session
 * obtida via EntityManager.
 */
@Configuration
public class HibernateFilterConfig implements WebMvcConfigurer {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Interceptor que habilita o filtro do Hibernate antes de cada requisição
     * e o desabilita após a requisição.
     */
    private HandlerInterceptor tenantFilterInterceptor() {
        return (request, response, handler) -> {
            if (TenantContext.isSet()) {
                // Obtém a Session nativa do Hibernate a partir do EntityManager
                Session session = entityManager.unwrap(Session.class);

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
