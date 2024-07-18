package no.idporten.ansattporten_integration.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

/**
 * Configuration class for setting up security configurations for the application.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the ServerHttpSecurity instance to configure
     * @return the configured SecurityWebFilterChain
     */
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
			//Disable csrf requirement, should be enabled in production
			.csrf((csrf) -> csrf.disable());
        // Build and return the configured security filter chain
        return http.build();
    }    
}
