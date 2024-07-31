package no.idporten.ansattporten_integration.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a verifiable presentation containing claims and metadata
 * about the verification status and holder information.
 */
@Setter
public class VerifiablePresentation {

    //GETTERS
    //SETTERS
    @Getter
    @JsonProperty("challengeId")
    private String challengeId;

    @Getter
    @JsonProperty("claims")
    private Claims claims;

    @JsonProperty("verified")
    private boolean verified;

    @Getter
    @JsonProperty("holder")
    private String holder;

    // Getter
    public boolean getVerified() {
        return verified;
    }

    /**
     * Represents the claims associated with a verifiable presentation.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Claims {
        private final Map<String, String> claimDetails = new HashMap<>();

        /**
         * Gets all claims as a map.
         *
         * @return a map of claims where the key is the claim name and the value is the claim value
         */
        @JsonAnyGetter
        public Map<String, String> getClaimDetails() {
            return claimDetails;
        }

        /**
         * Sets a claim with the specified name and value.
         *
         * @param name the name of the claim
         * @param value the value of the claim
         */
        @JsonAnySetter
        public void setClaim(String name, String value) {
            this.claimDetails.put(name, value);
        }
    }
}