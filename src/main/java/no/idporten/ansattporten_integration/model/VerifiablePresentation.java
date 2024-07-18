package no.idporten.ansattporten_integration.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a verifiable presentation containing claims and metadata
 * about the verification status and holder information.
 */
public class VerifiablePresentation {

    @JsonProperty("challengeId")
    private String challengeId;

    @JsonProperty("claims")
    private Claims claims;

    @JsonProperty("verified")
    private boolean verified;

    @JsonProperty("holder")
    private String holder;

    // Getters and Setters

    //SETTERS
    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }


    //GETTERS
    public String getChallengeId() {
        return challengeId;
    }

    public Claims getClaims() {
        return claims;
    }

    public boolean getVerified() {
        return verified;
    }

    public String getHolder() {
        return holder;
    }


    /**
     * Represents the claims associated with a verifiable presentation.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Claims {

        private Map<String, String> claims = new HashMap<>();

        /**
         * Gets all claims as a map.
         *
         * @return a map of claims where the key is the claim name and the value is the claim value
         */
        @JsonAnyGetter
        public Map<String, String> getClaims() {
            return claims;
        }

        /**
         * Sets a claim with the specified name and value.
         *
         * @param name the name of the claim
         * @param value the value of the claim
         */
        @JsonAnySetter
        public void setClaim(String name, String value) {
            this.claims.put(name, value);
        }
    }
}


