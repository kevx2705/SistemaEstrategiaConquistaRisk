package co.edu.unbosque.beans;

import co.edu.unbosque.service.JugadorService;
import co.edu.unbosque.util.AESUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Bean administrado para el registro de jugadores. Gestiona la creación de
 * usuarios tipo jugador, valida campos, encripta datos sensibles y comunica con
 * el servicio REST del backend.
 */
@Named(value = "registerbean")
@RequestScoped
public class RegisterBean implements Serializable{

	private String fullName;
	private String email;
	private String password;
	private String confirmPassword;

	public void crearJugador() {
		if (!password.equals(confirmPassword)) {
			showStickyLogin("406", "Las contraseñas no coinciden");
			return;
		}

		try {
			String nombreEnc = URLEncoder.encode(AESUtil.encrypt(fullName), StandardCharsets.UTF_8);
			String correoEnc = URLEncoder.encode(AESUtil.encrypt(email), StandardCharsets.UTF_8);
			String contrasenaEnc = URLEncoder.encode(AESUtil.encrypt(password), StandardCharsets.UTF_8);

			String url = "http://localhost:8081/jugadores/crear" + "?nombre=" + nombreEnc + "&correo=" + correoEnc
					+ "&contrasena=" + contrasenaEnc;
			String respuesta = JugadorService.doPostJson("", url);
			System.out.println("Respuesta del servidor: " + respuesta);

			String[] data = respuesta.split("\n", 2);
			String codigo = data[0].trim();
			String mensaje = data.length > 1 ? data[1].trim() : "";

			showStickyLogin(codigo, mensaje);

			if (codigo.equals("201")) {
				limpiarCampos();
			}

		} catch (Exception e) {
			e.printStackTrace();
			showStickyLogin("500", "Error al procesar el registro");
		}
	}

	private void limpiarCampos() {
		fullName = "";
		email = "";
		password = "";
		confirmPassword = "";
	}

	private void showStickyLogin(String code, String content) {
		FacesContext context = FacesContext.getCurrentInstance();

		switch (code) {
		case "201":
			context.addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario creado exitosamente "));
			break;
		case "406":
			context.addMessage("sticky-key", new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", content));
			break;
		case "500":
			context.addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error del servidor", "Ocurrió un problema interno"));
			break;
		default:
			context.addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error desconocido", content));
			break;
		}
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
