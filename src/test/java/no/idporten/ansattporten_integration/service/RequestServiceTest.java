package no.idporten.ansattporten_integration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

class RequestServiceTest {
    RequestService requestService = new RequestService("tenantURL", "clientSecret", "clientId", "domain", "issuerDID", "verifierDID");

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

    @Test
    void testCreatePresentation() throws IOException {
        requestService.createPresentation();
    }
}