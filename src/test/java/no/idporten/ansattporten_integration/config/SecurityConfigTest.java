package no.idporten.ansattporten_integration.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import no.idporten.ansattporten_integration.VerifierApplication;

@SpringBootTest(classes = VerifierApplication.class)
@AutoConfigureWebTestClient
class SecurityConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void contextLoads() {
        // This test ensures that the application context loads successfully.
    }

    @Test
    void whenNoCredentials_thenStatusOk() {
        // This test checks that accessing the root URL without credentials returns a status OK.
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }
}
