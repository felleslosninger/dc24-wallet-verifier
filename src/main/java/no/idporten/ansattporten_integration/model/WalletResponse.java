package no.idporten.ansattporten_integration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WalletResponse {

    @JsonProperty("vp_token")
    private String vpToken;

    @JsonProperty("presentation_submission")
    private PresentationSubmission presentationSubmission;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PresentationSubmission {
        @JsonProperty("id")
        private String id;

        @JsonProperty("definition_id")
        private String definitionId;

        @JsonProperty("descriptor_map")
        private List<DescriptorMap> descriptorMap;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class DescriptorMap {
            @JsonProperty("id")
            private String id;

            @JsonProperty("format")
            private String format;

            @JsonProperty("path")
            private String path;
        }
    }
}