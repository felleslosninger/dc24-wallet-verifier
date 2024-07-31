package no.idporten.ansattporten_integration.service;

import no.idporten.ansattporten_integration.requests.PresentationRequest;
import no.idporten.ansattporten_integration.requests.PresentationTemplate;
import no.idporten.ansattporten_integration.util.GenerateQRCode;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.WriterException;

import lombok.extern.slf4j.Slf4j;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Service class for handling presentation requests and templates.
 */
@Service
@Slf4j
public class RequestService {
    private static final String NO_ACCESS_TOKEN_FOUND = "No access token found";

    private final String tenantURL;
    private final String clientSecret;
    private final String clientId;
    private final String domain;
    private final String issuerDID;
    private final String verifierDID;

    private String mattrJwt = null;

    /**
     * Constructs a new RequestService with the specified parameters.
     *
     * @param tenantURL the tenant's base URL
     * @param clientSecret the client secret for authentication
     * @param clientId the client ID for authentication
     * @param domain the domain associated with the presentation template
     * @param issuerDID the decentralized identifier (DID) of the issuer
     * @param verifierDID the decentralized identifier (DID) of the verifier
     */
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

    /**
     * Creates a presentation and generates a QR code if it does not already exist.
     *
     * @throws IOException if an I/O error occurs
     */
    public void createPresentation() throws IOException {
        String path = "src/main/resources/static/qrCodes/selective-disclosure.png";
        File file = new File(path);

        // Only generates a new qr code if it can't find an existing one
        if (!file.exists()) {
            String presentationTemplateId = PresentationTemplate.createPresentationTemplate(issuerDID, tenantURL, domain, getJwt());
            String presentationReq = PresentationRequest.createPresentationRequest(verifierDID, tenantURL, presentationTemplateId, getJwt());

            try {
                GenerateQRCode.generateQRCodeImage(presentationReq, 300, 300, path);
            } catch (WriterException | IOException e) {
                log.error("Error occurred while generating QR Code: {}", e.getMessage());
            }
        }
    }

    public String getQR() throws IOException {
        String presentationTemplateId = PresentationTemplate.createPresentationTemplate(issuerDID, tenantURL, domain, getJwt());
        String presentationReq = PresentationRequest.createPresentationRequest(verifierDID, tenantURL, presentationTemplateId, getJwt());
    
        return "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + presentationReq;
    }

    /**
     * Authenticates with MATTR and retrieves a new JWT access token.
     *
     * @return the JWT access token
     * @throws IOException if an I/O error occurs
     */
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
            log.error(NO_ACCESS_TOKEN_FOUND);
            throw new IOException(NO_ACCESS_TOKEN_FOUND);
        }
        mattrJwt = responseObject.get("access_token").getAsString();
        return mattrJwt;
    }

    /**
     * Retrieves the current JWT access token, refreshing it if necessary.
     *
     * @return the JWT access token
     * @throws IOException if an I/O error occurs
     */
    public String getJwt() throws IOException {

        log.info("Getting access token");
        if(mattrJwt == null) {
            log.info(NO_ACCESS_TOKEN_FOUND);
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