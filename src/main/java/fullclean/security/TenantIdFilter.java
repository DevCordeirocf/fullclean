package fullclean.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * TenantIdFilter - Filtro de Servlet para extrair e validar o Tenant ID.
 * 
 * Este filtro é CRÍTICO para a segurança. Ele garante que:
 * 1. Toda requisição HTTP contenha o cabeçalho 'X-Tenant-ID'.
 * 2. O Tenant ID seja armazenado no TenantContext para uso posterior pelo Hibernate.
 * 3. O TenantContext seja limpo após o processamento da requisição.
 */
@Component
@Order(1) // Garante que este filtro seja executado primeiro
public class TenantIdFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TenantIdFilter.class);
    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. Extrair o Tenant ID do cabeçalho
        String tenantId = httpRequest.getHeader(TENANT_HEADER);

        if (tenantId == null || tenantId.trim().isEmpty()) {
            // 2. Bloquear requisições sem o Tenant ID (Erro 403 Forbidden)
            logger.warn("Requisição bloqueada: Cabeçalho '{}' ausente ou vazio.", TENANT_HEADER);
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("Acesso negado: O cabeçalho '" + TENANT_HEADER + "' é obrigatório.");
            return;
        }

        try {
            // 3. Armazenar o ID no TenantContext
            TenantContext.setTenantId(tenantId);
            logger.debug("Tenant ID '{}' configurado para a requisição.", tenantId);

            // Continua o processamento da requisição
            chain.doFilter(request, response);

        } catch (IllegalArgumentException e) {
            // Caso o setTenantId lance exceção (embora já validado acima, é uma segurança)
            logger.error("Erro ao configurar Tenant ID: {}", e.getMessage());
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().write("Erro interno ao processar Tenant ID.");
        } finally {
            // 4. Limpar o TenantContext no bloco finally para evitar vazamento de dados
            TenantContext.clear();
            logger.debug("TenantContext limpo.");
        }
    }
}
