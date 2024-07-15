package no.idporten.ansattporten_integration.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.idporten.ansattporten_integration.util.SendRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PresentationTemplate {
    static String requestURL = "/v2/credentials/web-semantic/presentations/templates";

    static LocalDateTime currentDateTime = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static String timestamp = currentDateTime.format(formatter);
    static String uniqueTemplateName = "Ansattporten-Selective-Presentation-" + timestamp;

    public static String CreatePresentationTemplate(String issuerDID, String tenantURL, String domain, String token) {
        try {
            Map<String, Object> trustedIssuer = new HashMap<>();
            trustedIssuer.put("issuer", issuerDID);
            trustedIssuer.put("required", true);

            List<Map<String, Object>> trustedIssuerList = new ArrayList<>();
            trustedIssuerList.add(trustedIssuer);

            Map<String, Object> credentialQuery = new HashMap<>();
            credentialQuery.put("required", true);
            credentialQuery.put("reason", "Please provide your certificate for Ansattporten");
            credentialQuery.put("trustedIssuer", trustedIssuerList);

            // --------- All these go into the 'frame' hashmap, which goes into the  credentialQuery just above
            List<String> type = new ArrayList<>();
            //type.add("VerifiableCredential");
            type.add("AnsattportenCredential"); // Only accepts ansattporten credentials

            List<String> context = new ArrayList<>();
            //context.add("https://schema.org");
            context.add("https://www.w3.org/2018/credentials/v1");
            context.add("https://w3id.org/vc-revocation-list-2020/v1");
            context.add("https://mattr.global/contexts/vc-extensions/v2");

            Map<String, Object> credentialSubject = new HashMap<>();
            credentialSubject.put("@explicit", true);
            credentialSubject.put("sub", new HashMap<>()); // Change these later
            credentialSubject.put("pid", new HashMap<>()); // Change these later

            Map<String, Object> frame = new HashMap<>();
            frame.put("type", type);
            frame.put("@context", context);
            frame.put("credentialSubject", credentialSubject);
            credentialQuery.put("frame", frame);
            // -------------------------------------------------------------------------------------------------

            List<Map<String, Object>> credentialQueryList = new ArrayList<>();
            credentialQueryList.add(credentialQuery);

            Map<String, Object> queries = new HashMap<>();
            queries.put("credentialQuery", credentialQueryList);
            queries.put("type", "QueryByFrame");

            List<Map<String, Object>> queryList = new ArrayList<>();
            queryList.add(queries);

            Map<String, Object> map = new HashMap<>();
            map.put("domain", domain);
            map.put("name", uniqueTemplateName);
            map.put("query", queryList);

            // Convert to json and send the request
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(map);

            SendRequest sendRequest = new SendRequest();
            String jsonResponse = sendRequest.sendRequest(jsonString, tenantURL + requestURL, token);

            JSONObject jsonObject = new JSONObject(jsonResponse); // Convert to json to extract access_token
            return(jsonObject.getString("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}