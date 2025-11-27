package com.mitica.fullclean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tenantId = TenantContext.getTenantId();

        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            org.hibernate.Filter filter = session.enableFilter("tenantFilter");
            filter.setParameter("tenantId", tenantId);
            filter.validate();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // A limpeza do TenantContext já é feita no TenantIdFilter, mas o filtro do Hibernate deve ser desabilitado
            if (tenantId != null) {
                Session session = entityManager.unwrap(Session.class);
                session.disableFilter("tenantFilter");
            }
        }
    }
}
