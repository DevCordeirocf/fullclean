package fullclean.config;

import fullclean.security.TenantIdFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TenantIdFilter tenantIdFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        // Adiciona o TenantIdFilter antes do filtro de autenticação padrão
        http.addFilterBefore(tenantIdFilter, UsernamePasswordAuthenticationFilter.class);
        // Habilita HTTP Basic (dev) e regras de autorização
        http.httpBasic(org.springframework.security.config.Customizer.withDefaults());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**", "/health", "/static/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/teste-tenant/**").permitAll()
                .anyRequest().authenticated()
        );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        var user = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
