package no.idporten.ansattporten_integration.model;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestData {
    private String type;
    private PresentationDefinition presentation_definition;
    private String nonce;

    @Getter
    @Setter
    public static class PresentationDefinition {
        private String id;
        private List<InputDescriptor> input_descriptors;
    }

    @Getter
    @Setter
    public static class InputDescriptor {
        private String id;
        private String name;
        private String purpose;
        private Format format;
        private Constraints constraints;
    }

    @Getter
    @Setter
    public static class Format {
        private Map<String, List<String>> mso_mdoc;
    }

    @Getter
    @Setter
    public static class Constraints {
        private List<Field> fields;
    }

    @Getter
    @Setter
    public static class Field {
        private List<String> path;
        private boolean intent_to_retain;
    }
}
