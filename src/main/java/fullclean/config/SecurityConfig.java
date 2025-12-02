package fullclean.config;

import fullclean.security.TenantIdFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig - Configuração básica do Spring Security.
 * 
 * CRÍTICO: Garante que o TenantIdFilter seja executado antes de qualquer
 * regra de autorização do Spring Security.
 */

\n@Configuration\n@EnableWebSecurity
public class SecurityConfig {\n\n    @Autowired\n    private TenantIdFilter tenantIdFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF para facilitar testes com Postman/cURL
            .csrf(AbstractHttpConfigurer::disable)
            // Adiciona o TenantIdFilter antes do filtro de autenticação padrão

            .addFilterBefore(tenantIdFilter, UsernamePasswordAuthenticationFilter.class)\n            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso a todos os endpoints para fins de teste
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}
