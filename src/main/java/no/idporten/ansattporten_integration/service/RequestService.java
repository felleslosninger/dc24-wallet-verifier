package no.idporten.ansattporten_integration.service;

import no.idporten.ansattporten_integration.requests.PresentationTemplate;
import no.idporten.ansattporten_integration.util.UrlEncoder;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service class for handling presentation requests and templates.
 */
@Service
public class RequestService {

    private static final Logger log = LoggerFactory.getLogger(RequestService.class);

    private final String apiURL;

    public RequestService(@Value("${EU_API_URL}") String apiURL) {
        this.apiURL = apiURL;
    }
    
    public String sendPresentationRequest() {

        return PresentationTemplate.createPresentationTemplate(apiURL);

    }
    
}