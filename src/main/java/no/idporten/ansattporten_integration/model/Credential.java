package no.idporten.ansattporten_integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
public class Credential {
    /**
     * Represents a document containing claims.
     */
    @Getter
    @Setter
    public static class Document {
        @JsonProperty("docType")
        private String docType;

        @JsonProperty("issuerSigned")
        private IssuerSigned issuerSigned;
    }

    /**
     * Represents the signed information from the issuer.
     */
    @Getter
    @Setter
    public static class IssuerSigned {
        @JsonProperty("nameSpaces")
        private Map<String, List<Element>> nameSpaces;
    }

    /**
     * Represents an element within the issuer-signed information.
     */
    @Getter
    @Setter
    public static class Element {
        @JsonProperty("random")
        private String random;

        @JsonProperty("digestID")
        private int digestID;

        @JsonProperty("elementValue")
        private Object elementValue;

        @JsonProperty("elementIdentifier")
        private String elementIdentifier;
    }

    /**
     * Represents the CBOR response containing documents.
     */
    @Getter
    @Setter
    public static class CborResponse {
        @JsonProperty("version")
        private String version;

        @JsonProperty("documents")
        private List<Document> documents;
    }
}