package co.edu.unbosque.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import co.edu.unbosque.model.Admin;

/**
 * Servicio utilitario para consumir el backend relacionado con administradores.
 * Proporciona operaciones básicas: crear (POST JSON), listar (GET) y eliminar (DELETE).
 * Usa {@link HttpClient} de Java 11 y Gson para deserializar.
 */
public class AdministradorService {
	/** Cliente HTTP reutilizable con timeout de conexión. */
	private static final HttpClient httpClient = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(5))
			.build();

	/**
	 * Envía un JSON para crear un administrador.
	 * @param json Cuerpo JSON serializado del administrador.
	 * @return Cadena con status code y body separados por un salto de línea.
	 */
	public static String doPostJson(String json) {
		String url = "http://localhost:8081/admin/createjson";
		HttpRequest request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create(url))
				.setHeader("User-Agent", "Java 11 HttpClient Bot")
				.header("Content-Type", "application/json")
				.build();

		HttpResponse<String> response = null;
		try {
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // restablece estado de interrupción
			return "ERROR_INTERRUPTED";
		} catch (IOException e) {
			return "ERROR_IO: " + e.getMessage();
		}

		return response.statusCode() + "\n" + response.body();
	}

	/**
	 * Obtiene todos los administradores desde la URL dada.
	 * @param url Endpoint completo (ej: http://localhost:8081/admin/all).
	 * @return Lista de administradores, vacía si no hay datos o error.
	 */
	public static ArrayList<Admin> doGetAll(String url) {
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.setHeader("User-Agent", "Java 11 HttpClient Bot")
				.header("Content-Type", "application/json")
				.build();

		HttpResponse<String> response = null;
		try {
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return new ArrayList<>();
		} catch (IOException e) {
			return new ArrayList<>();
		}
		if (response == null) {
			return new ArrayList<>();
		}
		System.out.println("get status code -> " + response.statusCode());
		String body = response.body();
		Gson g = new Gson();
		Admin[] temps = g.fromJson(body, Admin[].class);
		if (temps == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(Arrays.asList(temps));
	}

	/**
	 * Elimina un administrador invocando el endpoint DELETE.
	 * @param url Endpoint DELETE completo.
	 * @return Cadena con status code y body.
	 */
	public static String doDelete(String url) {
		HttpRequest request = HttpRequest.newBuilder()
				.DELETE()
				.uri(URI.create(url))
				.setHeader("User-Agent", "Java 11 HttpClient Bot")
				.header("Content-Type", "application/json")
				.build();

		HttpResponse<String> response = null;
		try {
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return "ERROR_INTERRUPTED";
		} catch (IOException e) {
			return "ERROR_IO: " + e.getMessage();
		}

		System.out.println("delete status code -> " + response.statusCode());
		return response.statusCode() + "\n" + response.body();
	}
}
