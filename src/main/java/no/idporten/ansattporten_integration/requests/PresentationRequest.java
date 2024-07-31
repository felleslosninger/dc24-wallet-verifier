package no.idporten.ansattporten_integration.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import no.idporten.ansattporten_integration.util.SendRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for creating and handling presentation requests.
 */
@Service
@Slf4j
public class PresentationRequest {
    static String requestURL = "/v2/credentials/web-semantic/presentations/requests";

    /**
     * Private constructor to hide the public one.
     */
    private PresentationRequest () {
    }

    /**
     * Creates a presentation request with the given parameters.
     *
     * @param verifierDID the decentralized identifier (DID) of the verifier
     * @param tenantURL the tenant's base URL
     * @param templateID the ID of the template to use for the presentation request
     * @param token the authentication token
     * @return the didcommUri from the response, or an empty String if an error occurs
     */
    public static String createPresentationRequest(String verifierDID, String tenantURL, String templateID, String token) {
        try {
            // Create a map to hold the request parameters
            String jsonString = getString(verifierDID, templateID);

            // Send the request and get the response
            SendRequest sendRequest = new SendRequest();
            String jsonResponse = sendRequest.sendRequest(jsonString, tenantURL + requestURL, token);

            // Convert the response to a JSON object to extract the didcommURI
            JSONObject jsonObject = new JSONObject(jsonResponse); // Convert to json to extract access_token
            log.info("Response JSON: {}", jsonObject);

            // Check if the key "didcommUri" exists
            if (jsonObject.has("didcommUri")) {
                return jsonObject.getString("didcommUri");
            } else {
                log.error("Key 'didcommUri' not found in the response JSON");
                return "";
            }
        } catch (Exception e) {
            log.error("Error creating presentation request", e);
            return "";
        }
    }

    /**
     * Constructs a JSON String with the given verifier DID and template ID.
     *
     * @param verifierDID the verifier's decentralized identifier (DID)
     * @param templateID the template ID to be used
     * @return a JSON String representing the constructed map
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    private static String getString(String verifierDID, String templateID) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("challenge", "GW8FGrP6j4Frl37yQZIM6a");
        map.put("did", verifierDID);
        map.put("templateId", templateID);
        map.put("callbackUrl", "https://dc24-wallet-verifier-hei.fly.dev/callback");

        // Convert the map to a JSON String
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }
}