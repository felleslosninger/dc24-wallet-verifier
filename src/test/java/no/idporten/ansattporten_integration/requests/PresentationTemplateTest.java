package no.idporten.ansattporten_integration.requests;

import no.idporten.ansattporten_integration.util.SendRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PresentationTemplateTest {

    @Mock
    private SendRequest sendRequest;

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

    // Test for handling invalid parameters
    @Test
    void createPresentationTemplate_InvalidParameters() {
        when(sendRequest.sendRequest(anyString(), anyString(), anyString())).thenThrow(new IllegalArgumentException("Invalid parameters"));

        String result = PresentationTemplate.createPresentationTemplate("", "https://tenantURL", "domain", "token");

        assertEquals("", result);
    }

    // Test for handling request failure
    @Test
    void createPresentationTemplate_RequestFailure() {
        when(sendRequest.sendRequest(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("Request failed"));

        String result = PresentationTemplate.createPresentationTemplate("issuerDID", "https://tenantURL", "domain", "token");

        assertEquals("", result);
    }
}