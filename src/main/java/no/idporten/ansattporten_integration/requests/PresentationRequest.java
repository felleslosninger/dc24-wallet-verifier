package no.idporten.ansattporten_integration.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.idporten.ansattporten_integration.util.SendRequest;
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

    // Private constructor to hide the public one.
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
    public static String createPresentationRequest(String verifierDID, String tenantURL, String templateID, String token){
        try{
            // Create a map to hold the request parameters
            Map<String, Object> map = new HashMap<>();
            map.put("challenge", "GW8FGrP6j4Frl37yQZIM6a");
            map.put("did", verifierDID);
            map.put("templateId", templateID);
            map.put("callbackUrl", "https://dc24-wallet-verifier.fly.dev/callback");
            // map.put("callbackUrl", "https://simplewebapp-llq9.onrender.com/callback");

            // Convert the map to a JSON String
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(map);

            // Send the request and get the response
            SendRequest sendRequest = new SendRequest();
            String jsonResponse = sendRequest.sendRequest(jsonString, tenantURL + requestURL, token);

            // Convert the response to a JSON object to extract the didcommURI
            JSONObject jsonObject = new JSONObject(jsonResponse); // Convert to json to extract access_token
            return(jsonObject.getString("didcommUri"));
        } catch (Exception e) {
            log.error("Error creating presentation request", e);
            return "";
        }
    }
}
