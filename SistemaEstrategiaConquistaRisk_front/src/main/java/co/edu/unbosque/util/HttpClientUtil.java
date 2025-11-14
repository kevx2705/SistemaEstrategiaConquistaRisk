package co.edu.unbosque.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utilidad para realizar peticiones HTTP usando {@link HttpClient}.
 * <p>
 * Proporciona métodos simples para enviar solicitudes GET y POST con cuerpo JSON,
 * manejando respuestas y errores comunes. Está diseñada para reutilizar un único
 * cliente HTTP configurado por defecto.
 */
public class HttpClientUtil {

    /**
     * Cliente HTTP reutilizable configurado con los valores por defecto.
     */
    private static final HttpClient client = HttpClient.newBuilder().build();

    /**
     * Envía una solicitud HTTP POST a la URL especificada.
     *
     * @param url       URL a la cual se enviará la solicitud POST.
     * @param jsonBody  Cuerpo JSON a enviar. Si es {@code null}, se enviará un POST vacío.
     * @return El cuerpo de la respuesta como {@code String} si el código de estado es 2xx.
     * @throws IOException           Si ocurre un error en la comunicación o si la respuesta no es 2xx.
     * @throws InterruptedException  Si la operación HTTP es interrumpida.
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
            throw new IOException("Error en POST: " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Realiza una solicitud HTTP GET a la URL indicada.
     *
     * @param url URL desde donde se obtendrá la información.
     * @return El cuerpo de la respuesta como {@code String}.
     * @throws IOException           Si ocurre un error durante la solicitud.
     * @throws InterruptedException  Si la operación es interrumpida.
     */
    public static String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    
	/** constructor vacio */
    public HttpClientUtil() {
		// TODO Auto-generated constructor stub
	}
}
