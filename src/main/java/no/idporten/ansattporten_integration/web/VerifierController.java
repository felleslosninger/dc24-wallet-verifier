package no.idporten.ansattporten_integration.web;

import no.idporten.ansattporten_integration.service.RequestService;
import no.idporten.ansattporten_integration.util.QrCodeGenerator;
import no.idporten.ansattporten_integration.model.VerifiablePresentation;

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

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

@Controller
public class VerifierController {
    private static final Logger logger = LoggerFactory.getLogger(VerifierController.class);
    boolean hasReceivedVP = false;
    private final RequestService requestService;

    // Temporary solution to store claims and verified status; consider implementing WebSession for better handling
    private static Map<String, String> presClaims = new HashMap<>();
    private static boolean presVerified;

    /**
     * Constructs a new VerifierController with the specified RequestService.
     *
     * @param requestService the service to handle presentation requests
     */
    public VerifierController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Handles the root ("/") endpoint and logs the session ID.
     *
     * @param request the HTTP request
     * @return the name of the view to render ("index")
     */
    @GetMapping("/")
    public String index(HttpServletRequest request){
        logger.info("Session ID in index: {}", request.getSession().getId());
        return "index";
    }

    /**
     * Handles the "/presentation-view" endpoint, logs session details, and adds attributes to the model.
     *
     * @param request the HTTP request
     * @param model the model to add attributes to
     * @return the name of the view to render ("presentation-view")
     */
    @GetMapping("/presentation-view")
    public String presentation(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        logger.info("Session ID in /presentation-view: {}", session.getId());
        logger.info("Session data for claims in /presentation-view:{}", session.getAttribute("claims"));


        //OBS: If you want to use HTTPSession, and you're sending a mock request from Postman, you need to set the sessionID in the cookies header

        //Alternatively we can use the instance variables presClaims and presVerified for testing instead of session attributes.
        model.addAttribute("claims", presClaims);
        model.addAttribute("verified", presVerified);

        return "presentation-view";
    }

    /**
     * Checks the verification status.
     *
     * @return the verification status as a boolean
     */
    @GetMapping("/verification-status")
    @ResponseBody
    public boolean checkVerificationStatus() {
        return hasReceivedVP;
    }

    /**
     * Handles GET requests to "/qr-code" and triggers the creation of a presentation.
     *
     * @return the name of the view to render
     * @throws IOException if an I/O error occurs
     */
    @GetMapping("/qr-code")
    public String qrCode(Model model) throws IOException {

        String[] responseArray = requestService.sendPresentationRequest();

        model.addAttribute("qrCode", QrCodeGenerator.makeQR(responseArray[0]));
        model.addAttribute("presentation_id", responseArray[1]);

        return "qr-code";
    }

    /**
     * Handles the callback for receiving a verifiable presentation.
     *
     * @param request the HTTP request
     * @param model the model to add attributes to
     * @param verifiablePresentation the verifiable presentation received in the request body
     * @return a ResponseEntity indicating the status of the operation
     */
    @PostMapping("/callback")
    public ResponseEntity<String> receivePresentation(HttpServletRequest request, Model model, @RequestBody VerifiablePresentation verifiablePresentation) {
        try {
            logger.info("Received presentation callback");
            String responseData = "Hello from verifier";

            presClaims = verifiablePresentation.getClaims().getClaimDetails();
            presVerified = verifiablePresentation.getVerified();

            logger.info("presClaims: {}", presClaims);
            logger.info("presVerified: {}", presVerified);

            HttpSession session = request.getSession();

            session.setAttribute("claims", presClaims);
            session.setAttribute("verified", presVerified);
            logger.info("Session ID in /callback: {}", request.getSession().getId());
            logger.info("Session data for claims in /callback: {}", session.getAttribute("claims"));

            // Indicate that the presentation was received successfully
            hasReceivedVP = true;
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}