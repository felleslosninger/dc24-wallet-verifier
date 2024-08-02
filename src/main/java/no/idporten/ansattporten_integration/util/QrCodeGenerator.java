package no.idporten.ansattporten_integration.util;

import java.io.IOException;

public class QrCodeGenerator {
    
    public static String makeQR(String request_uri) throws IOException {

        String prefix_url = "eudi-openid4vp://verifier-backend.eudiw.dev?client_id=verifier-backend.eudiw.dev&request_uri=";
        
        String encoded_url = UrlEncoder.encode(prefix_url + request_uri);

        return "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encoded_url;
    }

}
