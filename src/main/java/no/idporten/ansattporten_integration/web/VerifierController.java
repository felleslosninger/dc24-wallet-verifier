package no.idporten.ansattporten_integration.web;

import no.idporten.ansattporten_integration.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;

import no.idporten.ansattporten_integration.model.VerifiablePresentation;

import java.util.Map;

import java.io.IOException;

/**
 * Controller for handling verification-related requests.
 */
@Controller
public class VerifierController {

    private static final Logger logger = LoggerFactory.getLogger(VerifierController.class);

    private static final String CHALLENGE_ID = "challengeId";
    private static final String HOLDER = "holder";
    private static final String PRESENTATION_RECEIVED = "presentationReceived";

    private boolean hasReceivedVP = false;
    private final RequestService requestService;

    // Temporary solution to store claims and verified status; consider implementing WebSession for better handling
    private Map<String, String> presClaims;
    private boolean presVerified;

    /**
     * Constructs a new VerifierController with the specified RequestService.
     *
     * @param requestService the service to handle presentation requests
     */
    public VerifierController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Handles GET requests to the root URL ("/").
     *
     * @param session the current web session
     * @return the name of the view to render
     */
    @GetMapping("/")
    public String index(WebSession session) {
        logger.info("Session ID: {}", session.getId());
        return "index";
    }

    /**
     * Handles GET requests to "/presentation-view".
     *
     * @param model   the model to hold attributes for the view
     * @param session the current web session
     * @return the name of the view to render
     */
    @GetMapping("/presentation-view")
    public String presentation(Model model, WebSession session) {

        // logger.info("challengeId: " + session.getAttribute("challengeId"));
        // logger.info("claims: " + session.getAttribute("claims"));
        // logger.info("verified: " + session.getAttribute("verified"));
        // logger.info("holder: " + session.getAttribute("holder"));

        model.addAttribute(CHALLENGE_ID, session.getAttribute(CHALLENGE_ID));
        model.addAttribute("claims", presClaims);
        model.addAttribute("verified", presVerified);
        model.addAttribute(HOLDER, session.getAttribute(HOLDER));
        return "presentation-view";
    }

    /**
     * Handles GET requests to "/verifiaction-status".
     *
     * @param session the current web session
     * @return a boolean indicating whether the verification presentation has been received
     */
    @GetMapping("/verification-status")
    public boolean checkVerificationStatus(WebSession session) {
        return hasReceivedVP;
    }

    /**
     * Handles GET requests to "/qr-code" and triggers the creation of a presentation.
     *
     * @return the name of the view to render
     * @throws IOException if an I/O error occurs
     */
    @GetMapping("/qr-code")
    public String qrCode() throws IOException {
        requestService.createPresentation();
        return "qr-code";
    }

    /**
     * Handles POST requests to "/callback" to receive a verifiable presentation.
     *
     * @param session the current web session
     * @param model the model to hold attributes for the view
     * @param verifiablePresentation the verifiable presentation received in the request body
     * @return a ResponseEntity indicating the result of the request
     */
    @PostMapping("/callback")
    public ResponseEntity<String> receivePresentation(WebSession session, Model model, @RequestBody VerifiablePresentation verifiablePresentation) {
        try{
            logger.info("Received presentation callback");
            //logger.info("Session ID: " + session.getId());
            String responseData = "Hello from verifier";
            
            session.getAttributes().put(CHALLENGE_ID, verifiablePresentation.getChallengeId());
            session.getAttributes().put("claims", verifiablePresentation.getClaims().getClaimDetails());
            session.getAttributes().put("verified", verifiablePresentation.getVerified());
            session.getAttributes().put(HOLDER, verifiablePresentation.getHolder());

            presClaims = verifiablePresentation.getClaims().getClaimDetails();
            presVerified = verifiablePresentation.getVerified(); 

            logger.info("presClaims: {}", presClaims);
            logger.info("presVerified: {}", presVerified);

            // Indicate that the presentation was received successfully
            session.getAttributes().put(PRESENTATION_RECEIVED, true);
            hasReceivedVP = true;

            Object presentationReceived = session.getAttribute(PRESENTATION_RECEIVED);
            if (presentationReceived != null) {
                logger.info("presentationReceived: {}", presentationReceived);
            } else {
                logger.warn("presentationReceived attribute is null");
            }

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            logger.error("Error occurred while receiving presentation", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

