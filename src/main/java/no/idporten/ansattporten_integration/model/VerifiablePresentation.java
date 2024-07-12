package no.idporten.ansattporten_integration.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

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


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Claims {

        private Map<String, Object> claims = new HashMap<>();

        @JsonAnyGetter
        public Map<String, Object> getClaims() {
            return claims;
        }

        @JsonAnySetter
        public void setClaim(String name, Object value) {
            this.claims.put(name, value);
        }
    }
}


