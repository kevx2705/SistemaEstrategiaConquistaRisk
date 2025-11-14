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

import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.model.Jugador;

/**
 * Servicio para gestionar operaciones HTTP relacionadas con la entidad Jugador.
 * <p>
 * Esta clase proporciona métodos utilitarios para realizar solicitudes HTTP
 * utilizando la API HttpClient de Java 11, incluyendo:
 * <ul>
 *   <li>Enviar solicitudes POST con contenido JSON.</li>
 *   <li>Realizar peticiones GET para obtener listas de jugadores.</li>
 *   <li>Ejecutar solicitudes DELETE.</li>
 * </ul>
 * La clase usa Gson para deserializar respuestas JSON y MyLinkedList como
 * contenedor de resultados.
 */
public class JugadorService {

	/** constructor vacio */
	public JugadorService() {
		// TODO Auto-generated constructor stub
	}
	
    /** Cliente HTTP reutilizable configurado para HTTP/1.1 y timeout de 5 segundos. */
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /**
     * Envía una solicitud HTTP POST con un cuerpo en formato JSON.
     *
     * @param json JSON a enviar en el cuerpo de la solicitud.
     * @param url  URL objetivo de la petición.
     * @return Un String que contiene el código de estado y el cuerpo de la respuesta,
     *         o un mensaje de error si la operación falla.
     */
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

    /**
     * Realiza una solicitud HTTP GET para obtener una lista de objetos Jugador.
     *
     * @param url URL desde donde se obtendrá la lista de jugadores.
     * @return Un objeto MyLinkedList&lt;Jugador&gt; con los jugadores obtenidos.
     *         Si ocurre un error o la respuesta es inválida, retorna una lista vacía.
     */
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
            Type listType = new TypeToken<MyLinkedList<Jugador>>() {}.getType();
            usuarios = gson.fromJson(body, listType);
            if (usuarios == null) usuarios = new MyLinkedList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new MyLinkedList<>();
        }

        return usuarios;
    }

    /**
     * Envía una solicitud HTTP DELETE a la URL especificada.
     *
     * @param url URL del recurso que se desea eliminar.
     * @return Un String con el código de estado y la respuesta del servidor,
     *         o un mensaje de error si ocurre un fallo.
     */
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
