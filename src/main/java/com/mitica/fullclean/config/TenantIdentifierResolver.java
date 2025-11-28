package com.mitica.fullclean.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import com.mitica.fullclean.tenant.TenantContext;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        // 4.1. Retorna o ID do Tenant do ThreadLocal
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            return tenantId;
        }
        // Retorna um valor padrão ou lança exceção se necessário
        // Para o teste, vamos retornar um valor que não deve existir
        return "UNKNOWN_TENANT";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
