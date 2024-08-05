package no.idporten.ansattporten_integration.requests;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.idporten.ansattporten_integration.model.RequestData;
import no.idporten.ansattporten_integration.util.NonceGenerator;
import no.idporten.ansattporten_integration.util.SendRequest;

public class PresentationTemplate {

    private static final Logger log = LoggerFactory.getLogger(PresentationTemplate.class);

    static String requestURL = "/ui/presentations";

    static LocalDateTime currentDateTime = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static String timestamp = currentDateTime.format(formatter);
    static String uniqueTemplateId = "Ansattporten-Selective-Presentation-" + timestamp;

    public static String[] createPresentationTemplate(String apiURL) {
        try {
            
            RequestData requestData = new RequestData();

            requestData.setType("vp_token");
            requestData.setNonce(NonceGenerator.generateNonce());
            
            RequestData.PresentationDefinition presentationDefinition = new RequestData.PresentationDefinition();
            presentationDefinition.setId(uniqueTemplateId);
            
            RequestData.InputDescriptor inputDescriptor = new RequestData.InputDescriptor();
            inputDescriptor.setId("eu.europa.ec.eudi.pid.1");
            inputDescriptor.setName("EUDI PID");
            inputDescriptor.setPurpose("We need to verify your identity");
            
            RequestData.Format format = new RequestData.Format();
            format.setMso_mdoc(new HashMap<>() {{
                put("alg", Arrays.asList("ES256", "ES384", "ES512"));
            }});
            inputDescriptor.setFormat(format);
        
            RequestData.Constraints constraints = new RequestData.Constraints();
            constraints.setFields(Arrays.asList(
                    createField("$['eu.europa.ec.eudi.pid.1']['family_name']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['given_name']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['birth_date']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['age_over_18']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['age_in_years']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['age_birth_year']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['family_name_birth']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['given_name_birth']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['birth_place']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['birth_country']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['birth_state']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['birth_city']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['resident_address']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['resident_country']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['resident_state']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['resident_city']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['resident_postal_code']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['resident_street']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['resident_house_number']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['gender']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['nationality']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['issuance_date']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['expiry_date']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['issuing_authority']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['document_number']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['administrative_number']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['issuing_country']"),
                    createField("$['eu.europa.ec.eudi.pid.1']['issuing_jurisdiction']")
            ));
            inputDescriptor.setConstraints(constraints);
        
            presentationDefinition.setInput_descriptors(Arrays.asList(inputDescriptor));
            requestData.setPresentation_definition(presentationDefinition);
       

            // Convert to json and send the request
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(requestData);

            SendRequest sendRequest = new SendRequest();
            String jsonResponse = sendRequest.sendPostRequest(jsonString, apiURL + requestURL);

            // Convert the response to a JSON object to extract the request_uri
            JSONObject jsonObject = new JSONObject(jsonResponse); // Convert to json to extract access_token
            log.info("Response JSON: {}", jsonObject);

            if (jsonObject.has("request_uri")) {
                String[] strings = {jsonObject.getString("request_uri"), jsonObject.getString("presentation_id")};
                return strings;

            } else {
                log.error("Key 'request_uri' not found in the response JSON");
                return new String[0];
            }
        } catch (Exception e) {
            log.error("An error occurred while creating a new presentation template", e);
            return new String[0];
        }
    }

    private static RequestData.Field createField(String path) {
        RequestData.Field field = new RequestData.Field();
        field.setPath(Arrays.asList(path));
        field.setIntent_to_retain(false);
        return field;
    }
}
