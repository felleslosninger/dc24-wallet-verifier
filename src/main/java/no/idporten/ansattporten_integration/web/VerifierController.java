package no.idporten.ansattporten_integration.web;

import no.idporten.ansattporten_integration.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import no.idporten.ansattporten_integration.model.VerifiablePresentation;


import java.util.Map;

import java.io.IOException;

@Controller
public class VerifierController {

    private static final Logger logger = LoggerFactory.getLogger(VerifierController.class);

    boolean hasReceivedVP = false;
    private final RequestService requestService;

    // Temporary solution to store claims and verified status; consider implementing WebSession for better handling
    public Map<String,String> presClaims;
    public boolean presVerified;

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
    public String index(HttpServletRequest request){
        logger.info("Session ID in index: " + request.getSession().getId());
        return "index";
    }

    /**
     * Handles GET requests to "/presentation-view".
     *
     * @param model the model to hold attributes for the view
     * @param session the current web session
     * @return the name of the view to render
     */
    @GetMapping("/presentation-view")
    public String presentation(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        logger.info("Session ID in /presentation-view: " + session.getId());
        logger.info("Session data for claims in /presentation-view:{}", session.getAttribute("claims"));
        model.addAttribute("claims", session.getAttribute("claims"));
        model.addAttribute("verified", presVerified);
        return "presentation-view";
    }

    /**
     * Handles GET requests to "/verifiaction-status".
     *
     * @param session the current web session
     * @return a boolean indicating whether the verification presentation has been received
     */
    @GetMapping("/verification-status")
    @ResponseBody
    public boolean checkVerificationStatus() {
        Boolean verified = hasReceivedVP;
        return verified;
    }

    /**
     * Handles GET requests to "/qr-code" and triggers the creation of a presentation.
     *
     * @return the name of the view to render
     * @throws IOException if an I/O error occurs
     */
    @GetMapping("/qr-code")
    public String qrCode(Model model) throws IOException {
        model.addAttribute("qrCode", requestService.getQR());
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
    public ResponseEntity<?> receivePresentation(HttpServletRequest request, Model model, @RequestBody VerifiablePresentation verifiablePresentation) {
        try{
            logger.info("Received presentation callback");
            String responseData = "Hello from verifier";

            presClaims = verifiablePresentation.getClaims().getClaimDetails();
            presVerified = verifiablePresentation.getVerified(); 

            logger.info("presClaims: " + presClaims);
            logger.info("presVerified: " + presVerified);

            HttpSession session = request.getSession();

            session.setAttribute("claims", presClaims);
            logger.info("Session ID in /callback: " + request.getSession().getId());
            logger.info("Session data for claims in /callback:" + session.getAttribute("claims"));
            // Indicate that the presentation was received successfully
            // session.getAttributes().put("presentationReceived", true);
            hasReceivedVP = true;
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
