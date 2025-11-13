package co.edu.unbosque.util;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtil {

    private static final HttpClient client = HttpClient.newBuilder().build();

    public static String post(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json");

        if (jsonBody != null) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
        } else {
            requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new IOException("Error en POST: " + response.statusCode() + " - " + response.body());
        }
    }

    public static String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
