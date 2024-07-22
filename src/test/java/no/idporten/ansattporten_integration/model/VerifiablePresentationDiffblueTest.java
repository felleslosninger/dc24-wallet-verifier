package no.idporten.ansattporten_integration.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class VerifiablePresentationDiffblueTest {

    /**
     * Test to verify that the getClaimDetails method returns an empty map
     * when no claims have been set.
     */
    @Test
    void testClaimsGetClaimDetails() {
        // Arrange, Act and Assert
        assertTrue((new VerifiablePresentation.Claims()).getClaimDetails().isEmpty());
    }

    /**
     * Test to verify that a claim can be set and retrieved correctly.
     */
    @Test
    void testClaimsSetClaim() {
        // Arrange
        VerifiablePresentation.Claims claims = new VerifiablePresentation.Claims();

        // Act
        claims.setClaim("Name", "42");

        // Assert
        Map<String, String> claimDetails = claims.getClaimDetails();
        assertEquals(1, claimDetails.size());
        assertEquals("42", claimDetails.get("Name"));
    }
}
