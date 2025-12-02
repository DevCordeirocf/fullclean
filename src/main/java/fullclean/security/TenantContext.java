package fullclean.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TenantContext - Contexto de Tenant usando ThreadLocal
 * 
 * Esta classe é o CORAÇÃO do isolamento multi-tenant.
 * Ela armazena o Tenant ID da requisição atual de forma segura,
 * garantindo que cada thread (requisição) tenha seu próprio contexto isolado.
 * 
 * Funcionamento:
 * 1. O TenantIdFilter extrai o X-Tenant-ID do header HTTP
 * 2. Armazena o ID aqui via setTenantId()
 * 3. O Hibernate Filter usa getTenantId() para filtrar as queries
 * 4. O filter limpa o contexto via clear() no bloco finally
 */
public class TenantContext {
    
    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
    
    // ThreadLocal garante que cada thread tenha sua própria cópia do tenantId
    // Isso previne vazamento de dados entre requisições concorrentes
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    
    /**
     * Define o Tenant ID para a requisição atual
     * @param tenantId O identificador único do tenant
     */
    public static void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            logger.warn("Tentativa de definir tenantId nulo ou vazio");
            throw new IllegalArgumentException("Tenant ID não pode ser nulo ou vazio");
        }
        logger.debug("Definindo Tenant ID: {}", tenantId);
        CURRENT_TENANT.set(tenantId);
    }
    
    /**
     * Obtém o Tenant ID da requisição atual
     * @return O Tenant ID ou null se não estiver definido
     */
    public static String getTenantId() {
        String tenantId = CURRENT_TENANT.get();
        logger.debug("Obtendo Tenant ID: {}", tenantId);
        return tenantId;
    }
    
    /**
     * Limpa o Tenant ID da requisição atual
     * CRÍTICO: Deve ser chamado no bloco finally do filter para evitar vazamento
     */
    public static void clear() {
        String tenantId = CURRENT_TENANT.get();
        logger.debug("Limpando Tenant ID: {}", tenantId);
        CURRENT_TENANT.remove();
    }
    
    /**
     * Verifica se existe um Tenant ID definido para a requisição atual
     * @return true se o Tenant ID está definido, false caso contrário
     */
    public static boolean isSet() {
        return CURRENT_TENANT.get() != null;
    }
}
