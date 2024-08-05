package no.idporten.ansattporten_integration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.idporten.ansattporten_integration.model.Credential;
import no.idporten.ansattporten_integration.model.WalletResponse;
import no.idporten.ansattporten_integration.service.RequestService;
import no.idporten.ansattporten_integration.util.DecodeCbor;
import no.idporten.ansattporten_integration.util.QrCodeGenerator;

import no.idporten.ansattporten_integration.util.SendRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private final RequestService requestService;

    // Temporary solution to store claims and verified status; consider implementing WebSession for better handling
    private static Map<String, String> presClaims = new HashMap<>();

    public String cborString;

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
    public String presentation(HttpServletRequest request, Model model) throws IOException {
        model.addAttribute("credentials_map", presClaims);

        return "presentation-view";
    }

    /**
     * Checks the verification status.
     *
     * @return the verification status as a boolean
     */
    @PostMapping("/cbor")
    @ResponseBody
    public ResponseEntity<WalletResponse> recieveCborFromJS(Model model, @RequestBody WalletResponse walletResponse) {
        try {
            JSONObject jsonObject = new JSONObject(walletResponse);
            presClaims = DecodeCbor.decode(jsonObject.getString("vpToken"));

            return new ResponseEntity<>(walletResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(walletResponse, HttpStatus.BAD_REQUEST);
        }
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
}