package no.idporten.ansattporten_integration.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;

class VerifierControllerTest {

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
    void testVerifierController() {

    }
}