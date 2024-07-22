package no.idporten.ansattporten_integration.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class VerifiablePresentationTest {

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
    void testGettersSetters() {
        // Create an instance of VerifiablePresentation
        VerifiablePresentation verifiablePresentation = new VerifiablePresentation();

        // Set values using setters
        verifiablePresentation.setChallengeId("challenge123");
        verifiablePresentation.setVerified(true);
        verifiablePresentation.setHolder("holder123");

        // Create an instance of claims and set a claim
        VerifiablePresentation.Claims claims = new VerifiablePresentation.Claims();
        claims.setClaim("claim1", "value1");
        verifiablePresentation.setClaims(claims);

        // Verify the values using getters
        assertEquals("challenge123", verifiablePresentation.getChallengeId());
        assertTrue(verifiablePresentation.getVerified());
        assertEquals("holder123", verifiablePresentation.getHolder());
        assertEquals("value1", verifiablePresentation.getClaims().getClaimDetails().get("claim1"));
    }

    @Test
    void testClaims() {
        // Create an instance of Claims
        VerifiablePresentation.Claims claims = new VerifiablePresentation.Claims();

        // Set a claim
        claims.setClaim("claim1", "value1");

        // Verify the claim is set correctly
        assertEquals("value1", claims.getClaimDetails().get("claim1"));

        // Set another claim
        claims.setClaim("claim2", "value2");

        // Verify the new claim is set correctly
        assertEquals("value2", claims.getClaimDetails().get("claim2"));
    }
}
