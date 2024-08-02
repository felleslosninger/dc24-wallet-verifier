package no.idporten.ansattporten_integration.util;

import java.util.UUID;

public class NonceGenerator {
    public static String generateNonce() {
        return UUID.randomUUID().toString();
    }
}