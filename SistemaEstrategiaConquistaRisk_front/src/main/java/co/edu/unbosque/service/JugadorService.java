package co.edu.unbosque.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.estrucutres.MyLinkedList;

public class JugadorService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    // ---------------- POST JSON ----------------
    public static String doPostJson(String json, String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR_INTERRUPTED";
        } catch (IOException e) {
            return "ERROR_IO: " + e.getMessage();
        }

        return response.statusCode() + "\n" + response.body();
    }

    // ---------------- GET ALL ----------------
    public static MyLinkedList<Jugador> doGetAll(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new MyLinkedList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new MyLinkedList<>();
        }

        if (response == null || response.body() == null) {
            return new MyLinkedList<>();
        }

        String body = response.body();
        Gson gson = new Gson();

        MyLinkedList<Jugador> usuarios = new MyLinkedList<>();
        try {
            // Deserializa la lista de JugadorDTO del backend
            Type listType = new TypeToken<MyLinkedList<Jugador>>() {}.getType();
            usuarios = gson.fromJson(body, listType);

            // Si Gson falla por tipo incorrecto, prueba a transformar manualmente
            if (usuarios == null) usuarios = new MyLinkedList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new MyLinkedList<>();
        }

        return usuarios;
    }

    // ---------------- DELETE ----------------
    public static String doDelete(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR_INTERRUPTED";
        } catch (IOException e) {
            return "ERROR_IO: " + e.getMessage();
        }

        return response.statusCode() + "\n" + response.body();
    }
}
