package no.idporten.ansattporten_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import java.util.concurrent.*;

// @Configuration
// @EnableSpringWebSession
// public class SessionConfig {
    
//     @Bean
//     public ReactiveSessionRepository reactiveSessionRepository() {
//         return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
//     }
// }