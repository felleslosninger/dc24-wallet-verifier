package no.idporten.ansattporten_integration.web;

import no.idporten.ansattporten_integration.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.WebSession;

import no.idporten.ansattporten_integration.model.VerifiablePresentation;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
public class VerifierController {

    boolean hasReceivedVP = false;
    private final RequestService requestService;

    //Billig løsning for å lagre claims og verified status, bør se på å implementere WebSession
    String presClaims;
    boolean presVerified;

    private static final Logger logger = LoggerFactory.getLogger(VerifierController.class);

    // Needed to initialize the requestService object above
    public VerifierController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/")
    public String index(WebSession session){
        logger.info("Session ID: " + session.getId());
        return "index";
    }

    @GetMapping("/presentation-view")
    public String presentation(Model model, WebSession session) {

        logger.info("challengeId: " + session.getAttribute("challengeId"));
        logger.info("claims: " + session.getAttribute("claims"));
        logger.info("verified: " + session.getAttribute("verified"));
        logger.info("holder: " + session.getAttribute("holder"));


        model.addAttribute("challengeId", session.getAttribute("challengeId"));
        model.addAttribute("claims", presClaims);
        model.addAttribute("verified", presVerified);
        model.addAttribute("holder", session.getAttribute("holder"));
        return "presentation-view";
    }  

    @GetMapping("/verification-status")
    @ResponseBody
    public boolean checkVerificationStatus(WebSession session) {
        Boolean verified = hasReceivedVP;
        return verified;
    }

    @GetMapping("/qr-code")
    public String qrCode() throws IOException {
        requestService.createPresentation();
        return "qr-code";
    }

    @PostMapping("/callback")
    public ResponseEntity<?> receivePresentation(WebSession session, Model model, @RequestBody VerifiablePresentation verifiablePresentation) {
        try{
            logger.info("Received presentation callback");
            logger.info("Session ID: " + session.getId());
            String responseData = "Hello from verifier";
            
            session.getAttributes().put("challengeId", verifiablePresentation.getChallengeId());
            session.getAttributes().put("claims", verifiablePresentation.getClaims());
            session.getAttributes().put("verified", verifiablePresentation.getVerified());
            session.getAttributes().put("holder", verifiablePresentation.getHolder());

            presClaims = verifiablePresentation.getClaims().toString();
            presVerified = verifiablePresentation.getVerified(); 

            // Indicate that the presentation was received successfully
            session.getAttributes().put("presentationReceived", true);
            hasReceivedVP = true;
            logger.info(session.getAttribute("presentationReceived").toString());
            return ResponseEntity.ok(model);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
