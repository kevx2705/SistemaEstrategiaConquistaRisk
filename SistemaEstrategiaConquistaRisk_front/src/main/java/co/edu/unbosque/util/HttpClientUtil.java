package co.edu.unbosque.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utilidad para realizar peticiones HTTP utilizando {@link HttpClient}.
 * <p>
 * Ofrece métodos simplificados para enviar solicitudes HTTP GET y POST con
 * cuerpo JSON, manejando respuestas comunes y errores esperados. La clase
 * mantiene un único cliente HTTP reutilizable para optimizar el rendimiento.
 */
public class HttpClientUtil {

    /**
     * Cliente HTTP reutilizable configurado con valores por defecto.
     */
    private static final HttpClient client = HttpClient.newBuilder().build();

    /**
     * Envía una solicitud HTTP POST a la URL especificada.
     *
     * @param url      URL de destino.
     * @param jsonBody Cuerpo JSON a enviar. Si es {@code null}, se enviará un POST sin cuerpo.
     * @return El cuerpo de la respuesta como {@code String} si la respuesta es exitosa (código 2xx).
     * @throws IOException          Si ocurre un error en la comunicación o si la respuesta no es 2xx.
     * @throws InterruptedException Si la operación HTTP es interrumpida.
     */
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
            throw new IOException("Error:" + response.body());
        }
    }

    /**
     * Envía una solicitud HTTP GET a la URL indicada.
     *
     * @param url URL desde donde se obtendrá la información.
     * @return El cuerpo de la respuesta como {@code String}.
     * @throws IOException          Si ocurre un error durante la solicitud.
     * @throws InterruptedException Si la operación es interrumpida.
     */
    public static String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Constructor vacío. No se recomienda crear instancias de esta clase,
     * ya que sus métodos son estáticos.
     */
    public HttpClientUtil() {
        // Constructor intencionalmente vacío.
    }
    
    public static String put(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json");

        if (jsonBody != null) {
            requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
        } else {
            requestBuilder.PUT(HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new IOException(response.body());
        }
    }
    
    public static byte[] getBytes(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new IOException("Error al descargar ZIP: " + response.statusCode());
        }
    }


}