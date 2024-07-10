package no.idporten.ansattporten_integration.web;

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

@Controller
public class VerifierController {

    boolean hasReceivedVP = false;
    String presClaims;
    boolean presVerified;

    private static final Logger logger = LoggerFactory.getLogger(VerifierController.class);

    @GetMapping("/")
    public String index(WebSession session){
        logger.info("Session ID: " + session.getId());
        return "index";
    }

    @GetMapping("/presentation-view")
    public String presentation(Model model, WebSession session) {
        model.addAttribute("challengeId", session.getAttribute("challengeId"));
        model.addAttribute("claims", session.getAttribute("claims"));
        model.addAttribute("verified", session.getAttribute("verified"));
        model.addAttribute("holder", session.getAttribute("holder"));
        return "presentation-view";
    }  

    @GetMapping("/verification-status")
    @ResponseBody
    public boolean checkVerificationStatus(WebSession session) {
        Boolean verified = hasReceivedVP;
        return verified;
    }

    @PostMapping("/callback")
    public ResponseEntity<?> receivePresentation(WebSession session, Model model, @RequestBody VerifiablePresentation verifiablePresentation) {
        try{
            logger.info("Received presentation callback");
            logger.info("Session ID: " + session.getId());
            String responseData = "Hello from verifier";
            
            model.addAttribute("challengeId", verifiablePresentation.getChallengeId());
            model.addAttribute("claims", verifiablePresentation.getClaims());
            model.addAttribute("verified", verifiablePresentation.getVerified());
            model.addAttribute("holder", verifiablePresentation.getHolder());

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
