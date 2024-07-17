package no.idporten.ansattporten_integration.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import no.idporten.ansattporten_integration.requests.PresentationRequest;
import no.idporten.ansattporten_integration.requests.PresentationTemplate;
import no.idporten.ansattporten_integration.util.GenerateQRCode;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;


@Service
@Slf4j
public class RequestService {

    private final String tenantURL;
    private final String clientSecret;
    private final String clientId;
    private final String domain;
    private final String issuerDID;
    private final String verifierDID;

    private String mattrJwt = null;

    public RequestService(@Value("${MATTR_TENANT_URL}") String tenantURL,
                          @Value("${MATTR_CLIENT_SECRET}") String clientSecret,
                          @Value("${MATTR_CLIENT_ID}") String clientId,
                          @Value("${MATTR_DOMAIN}") String domain,
                          @Value("${MATTR_ISSUER_DID}") String issuerDID,
                          @Value("${MATTR_VERIFIER_DID}") String verifierDID) {
        this.tenantURL = tenantURL;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
        this.domain = domain;
        this.issuerDID = issuerDID;
        this.verifierDID = verifierDID;
    }

    public void createPresentation() throws IOException {
        String path = "src/main/resources/static/qrCodes/selective-disclosure.png";
        File file = new File(path);

        // Only generates a new qr code if it can't find an existing one
        if (!file.exists()) {
            String presentationTemplateId = PresentationTemplate.CreatePresentationTemplate(issuerDID, tenantURL, domain, getJwt());
            String presentationReq = PresentationRequest.CreatePresentationRequest(verifierDID, tenantURL, presentationTemplateId, getJwt());

            try {
                GenerateQRCode.GenerateQRCodeImage(presentationReq, 300, 300, path);
            } catch (WriterException | IOException e) {
                System.err.println("Error occurred while generating QR Code: " + e.getMessage());
            }
        }
    }


    private String authenticateMattr() throws IOException {
        log.info("Generating new access token");
        JsonObject json = new JsonObject();
        json.addProperty("audience", tenantURL);
        json.addProperty("grant_type", "client_credentials");
        json.addProperty("client_id", clientId);
        json.addProperty("client_secret", clientSecret);

        String postUrl = "https://auth.au01.mattr.global/oauth/token";

        log.info("Posting to: {}", postUrl);
        String responseContent = Request.post(postUrl)
                .bodyString(json.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        JsonObject responseObject = JsonParser.parseString(responseContent).getAsJsonObject();
        if (responseObject.get("access_token") == null) {
            log.error("No access token found");
            throw new IOException("No access token found");
        }
        mattrJwt = responseObject.get("access_token").getAsString();
        return mattrJwt;
    }

    public String getJwt() throws IOException {

        log.info("Getting access token");
        if(mattrJwt == null) {
            log.info("No access token found");
            return authenticateMattr();
        }
        DecodedJWT decodedJWT = JWT.decode(mattrJwt);
        if (decodedJWT.getExpiresAt() != null) {
            boolean isExpired = decodedJWT.getExpiresAt().before(new Date());
            if(isExpired) {
                log.info("Access token is expired");
                return authenticateMattr();
            } else {
                log.info("Access token is valid");
                return mattrJwt;
            }
        }
        return authenticateMattr();
    }
}