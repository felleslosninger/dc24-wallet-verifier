package no.idporten.ansattporten_integration.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility class for sending HTTP requests.
 */
public class SendRequest {

    private static final Logger log = LoggerFactory.getLogger(SendRequest.class);

    // Create a single instance of HttpCLient to reuse it
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Sends a POST request with the specified JSON String, URL, and authorization token.
     *
     * @param jsonString the JSON String to send in the request body
     * @param url the URL to which the request is sent
     * @param token the authorization token for the request
     * @return the response message as a String, or an empty String if an error occurs
     */
    public String sendRequest(String jsonString, String url, String token) {
        String responseMsg = "";

        try {
            // Build the HTTP request with the specified JSON body, URL, and headers
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .build();

            // Create an HTTP client and send the request

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Get the response body
            responseMsg = response.body();
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred while sending HTTP request", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }

        return responseMsg; // Returns empty string if an error occurs
    }
}