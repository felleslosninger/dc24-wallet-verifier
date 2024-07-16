package no.idporten.ansattporten_integration.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.idporten.ansattporten_integration.util.SendRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PresentationRequest {
    static String requestURL = "/v2/credentials/web-semantic/presentations/requests";

    public static String CreatePresentationRequest(String verifierDID, String tenantURL, String templateID, String token){
        try{
            Map<String, Object> map = new HashMap<>();
            map.put("challenge", "GW8FGrP6j4Frl37yQZIM6a");
            map.put("did", verifierDID);
            map.put("templateId", templateID);
            map.put("callbackUrl", "https://dc24-wallet-verifier.fly.dev/callback");
            // map.put("callbackUrl", "https://simplewebapp-llq9.onrender.com/callback");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(map);

            SendRequest sendRequest = new SendRequest();
            String jsonResponse = sendRequest.sendRequest(jsonString, tenantURL + requestURL, token);

            JSONObject jsonObject = new JSONObject(jsonResponse); // Convert to json to extract access_token
            return(jsonObject.getString("didcommUri"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
