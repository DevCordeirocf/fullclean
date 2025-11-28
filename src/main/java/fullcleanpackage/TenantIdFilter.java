package fullcleanpackage;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TenantIdFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String tenantId = httpRequest.getHeader(TENANT_HEADER);

        if (tenantId == null || tenantId.trim().isEmpty()) {
            // 3.2. Se o ID for inválido, retorne um Erro 403.
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("Tenant ID não fornecido. Acesso negado.");
            return;
        }

        try {
            // 3.2. Se for válido, use TenantContext.setTenantId(id).
            TenantContext.setTenantId(tenantId);
            chain.doFilter(request, response);
        } finally {
            // 3.3. Limpeza OBRIGATÓRIA: Certifique-se de que o método TenantContext.clear() seja chamado.
            TenantContext.clear();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização, se necessário
    }

    @Override
    public void destroy() {
        // Limpeza, se necessário
    }
}
