package no.idporten.ansattporten_integration.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SendRequest {
    public String sendRequest(String jsonString, String url, String token) {
        String responseMsg = "";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            responseMsg = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return responseMsg; // Returns empty string if an error occurs
    }
}