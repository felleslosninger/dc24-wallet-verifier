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

    public String sendPostRequest(String jsonString, String url) {
        String responseMsg = "";

        try {
            // Build the HTTP request with the specified JSON body, URL, and headers
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseMsg = response.body();
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred while sending HTTP request", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        return responseMsg; // Returns empty string if an error occurs
    }

    public String sendGetRequest(String url) {
        String responseMsg = "";

        System.out.println("REQ: ");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            System.out.println("REQ: 111111111111111");

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("REQ: 22222222222222222");
            responseMsg = response.body();
            System.out.println(response);
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred while sending HTTP request", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        return responseMsg; // Returns empty string if an error occurs
    }
}