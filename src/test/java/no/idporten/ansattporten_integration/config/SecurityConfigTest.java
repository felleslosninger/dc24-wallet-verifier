package no.idporten.ansattporten_integration.config;

import no.idporten.ansattporten_integration.VerifierApplication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = VerifierApplication.class)
@AutoConfigureWebTestClient
class SecurityConfigTest {

    private AutoCloseable closeable;

    // Initialize mocks before each test
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // Close mocks after each test
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

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
