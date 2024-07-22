package no.idporten.ansattporten_integration.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.MatcherSecurityWebFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.context.ReactorContextWebFilter;
import org.springframework.security.web.server.context.SecurityContextServerWebExchangeWebFilter;
import org.springframework.security.web.server.header.HttpHeaderWriterWebFilter;
import org.springframework.security.web.server.savedrequest.ServerRequestCacheWebFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.WebFilter;
import reactor.test.StepVerifier;

@ContextConfiguration(classes = {SecurityConfig.class})
@ExtendWith(SpringExtension.class)
class SecurityConfigDiffblueTest {
    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void testSpringSecurityFilterChain() throws AssertionError {
        // Arrange and Act
        SecurityWebFilterChain actualSpringSecurityFilterChainResult = securityConfig
                .springSecurityFilterChain(ServerHttpSecurity.http());

        // Assert
        assertInstanceOf(MatcherSecurityWebFilterChain.class, actualSpringSecurityFilterChainResult);
        StepVerifier.FirstStep<WebFilter> createResult = StepVerifier
                .create(actualSpringSecurityFilterChainResult.getWebFilters());
        StepVerifier.Step<WebFilter> assertNextResult = createResult.assertNext(w -> {
        });
        StepVerifier.Step<WebFilter> assertNextResult2 = assertNextResult.assertNext(w2 -> {
            assertInstanceOf(HttpHeaderWriterWebFilter.class, w2);
        });
        StepVerifier.Step<WebFilter> assertNextResult3 = assertNextResult2.assertNext(w3 -> {
            assertInstanceOf(ReactorContextWebFilter.class, w3);
        });
        StepVerifier.Step<WebFilter> assertNextResult4 = assertNextResult3.assertNext(w4 -> {
            assertInstanceOf(SecurityContextServerWebExchangeWebFilter.class, w4);
        });
        StepVerifier.Step<WebFilter> assertNextResult5 = assertNextResult4.assertNext(w5 -> {
            assertInstanceOf(ServerRequestCacheWebFilter.class, w5);
        });
        assertNextResult5.assertNext(w6 -> {
            assertInstanceOf(LogoutWebFilter.class, w6);
        }).expectComplete().verify();
    }

    /**
     * Method under test:
     * {@link SecurityConfig#springSecurityFilterChain(ServerHttpSecurity)}
     */
    @Test
    void testSpringSecurityFilterChain2() throws AssertionError {
        // Arrange
        ServerHttpSecurity http = ServerHttpSecurity.http();
        http.addFilterAt(mock(WebFilter.class), SecurityWebFiltersOrder.FIRST);

        // Act
        SecurityWebFilterChain actualSpringSecurityFilterChainResult = securityConfig.springSecurityFilterChain(http);

        // Assert
        assertInstanceOf(MatcherSecurityWebFilterChain.class, actualSpringSecurityFilterChainResult);
        StepVerifier.FirstStep<WebFilter> createResult = StepVerifier
                .create(actualSpringSecurityFilterChainResult.getWebFilters());
        StepVerifier.Step<WebFilter> assertNextResult = createResult.assertNext(w -> {
        });
        StepVerifier.Step<WebFilter> assertNextResult2 = assertNextResult.assertNext(w2 -> {
            assertInstanceOf(HttpHeaderWriterWebFilter.class, w2);
        });
        StepVerifier.Step<WebFilter> assertNextResult3 = assertNextResult2.assertNext(w3 -> {
            assertInstanceOf(ReactorContextWebFilter.class, w3);
        });
        StepVerifier.Step<WebFilter> assertNextResult4 = assertNextResult3.assertNext(w4 -> {
            assertInstanceOf(SecurityContextServerWebExchangeWebFilter.class, w4);
        });
        StepVerifier.Step<WebFilter> assertNextResult5 = assertNextResult4.assertNext(w5 -> {
            assertInstanceOf(ServerRequestCacheWebFilter.class, w5);
        });
        assertNextResult5.assertNext(w6 -> {
            assertInstanceOf(LogoutWebFilter.class, w6);
        }).expectComplete().verify();
    }
}
