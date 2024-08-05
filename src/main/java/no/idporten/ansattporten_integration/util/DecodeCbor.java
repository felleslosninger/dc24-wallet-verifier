package no.idporten.ansattporten_integration.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class DecodeCbor {
    public static Map<String, String> decode(String cbor) throws IOException {
        byte[] cborBytes = Base64.getUrlDecoder().decode(cbor);
        CBORFactory cborFactory = new CBORFactory();
        ObjectMapper cborMapper = new ObjectMapper(cborFactory);
        JsonNode jsonNode = cborMapper.readTree(cborBytes);

        Map<String, String> map = new HashMap<>();

        JsonNode nameSpacesNode = jsonNode.path("documents").get(0).path("issuerSigned").path("nameSpaces").path("eu.europa.ec.eudi.pid.1");
        for(int i = 0; i < nameSpacesNode.size(); i++){
            String claim = nameSpacesNode.get(i).asText();
            byte[] claimAsBytes = Base64.getDecoder().decode(claim);
            JsonNode jsonClaim = cborMapper.readTree(claimAsBytes);

            String elementValue = jsonClaim.get("elementValue").asText();
            String elementIdentifier = jsonClaim.get("elementIdentifier").asText();

            map.put(elementIdentifier, elementValue);
        }
        return map;
    }
}
