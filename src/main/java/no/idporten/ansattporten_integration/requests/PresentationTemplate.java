package no.idporten.ansattporten_integration.requests;

import no.idporten.ansattporten_integration.util.SendRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service class for creating and handling presentation templates.
 */
@Service
@Slf4j
public class PresentationTemplate {
    static String requestURL = "/v2/credentials/web-semantic/presentations/templates";

    /**
     * Private constructor to hide the public one.
     */
    private PresentationTemplate () {
    }

    /**
     * Creates a presentation template with the given parameters.
     *
     * @param issuerDID the decentralized identifier (DID) of the issuer
     * @param tenantURL the tenant's base URL
     * @param domain the domain associated with the presentation template
     * @param token the authentication token
     * @return the ID of the created presentation template, or an empty String if an error occurs
     */
    public static String createPresentationTemplate(String issuerDID, String tenantURL, String domain, String token) {
        try {
            // Create a map for the trusted issuer details
            Map<String, Object> trustedIssuer = new HashMap<>();
            trustedIssuer.put("issuer", issuerDID);
            trustedIssuer.put("required", true);

            // Create a list of trusted issuers
            List<Map<String, Object>> trustedIssuerList = new ArrayList<>();
            trustedIssuerList.add(trustedIssuer);

            // Create a map for the credential query details
            Map<String, Object> credentialQuery = new HashMap<>();
            credentialQuery.put("required", true);
            credentialQuery.put("reason", "Please provide your certificate for Ansattporten");
            credentialQuery.put("trustedIssuer", trustedIssuerList);

            // --------- All these go into the 'frame' hashmap, which goes into the  credentialQuery just above
            List<String> type = new ArrayList<>();
            type.add("AnsattportenCredential"); // Only accepts ansattporten credentials

            List<String> context = new ArrayList<>();
            context.add("https://www.w3.org/2018/credentials/v1");
            context.add("https://w3id.org/vc-revocation-list-2020/v1");
            context.add("https://mattr.global/contexts/vc-extensions/v2");

            Map<String, Object> credentialSubject = new HashMap<>();
            credentialSubject.put("@explicit", true);
            //credentialSubject.put("sub", new HashMap<>()); // Change these later
            credentialSubject.put("pid", new HashMap<>()); // Change these later

            Map<String, Object> frame = new HashMap<>();
            frame.put("type", type);
            frame.put("@context", context);
            frame.put("credentialSubject", credentialSubject);
            credentialQuery.put("frame", frame);
            // -------------------------------------------------------------------------------------------------

            // Create a list of credential queries
            List<Map<String, Object>> credentialQueryList = new ArrayList<>();
            credentialQueryList.add(credentialQuery);

            // Create the query map
            Map<String, Object> queries = new HashMap<>();
            queries.put("credentialQuery", credentialQueryList);
            queries.put("type", "QueryByFrame");

            // Create a list of queries
            List<Map<String, Object>> queryList = new ArrayList<>();
            queryList.add(queries);

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timestamp = currentDateTime.format(formatter);
            String uniqueTemplateName = "Ansattporten-Selective-Presentation-" + timestamp;


            // Create the final request map
            Map<String, Object> map = new HashMap<>();
            map.put("domain", domain);
            map.put("name", uniqueTemplateName);
            map.put("query", queryList);

            // Convert to json and send the request
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(map);

            SendRequest sendRequest = new SendRequest();
            String jsonResponse = sendRequest.sendRequest(jsonString, tenantURL + requestURL, token);

            // Convert the response to a JSON object to extract the template ID
            JSONObject jsonObject = new JSONObject(jsonResponse); // Convert to json to extract access_token
            log.info("Response JSON: {}", jsonObject);

            // Check if the key "id" exists
            if (jsonObject.has("id")) {
                return jsonObject.getString("id");
            } else {
                log.error("Key 'id' not found in the response JSON");
                return "";
            }
        } catch (Exception e) {
            log.error("An error occurred while creating a new presentation template", e);
            return "";
        }
    }
}
