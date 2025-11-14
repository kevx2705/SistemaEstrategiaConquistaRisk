package co.edu.unbosque.beans;

import java.io.Serializable;
import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.UsuarioActual;
import co.edu.unbosque.service.JugadorService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Named(value = "loginbean")
@SessionScoped
public class LogInBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String user;
	private String password;
	private Jugador sesionIniciada;
	private MyLinkedList<Jugador> listUsers = new MyLinkedList<>();

	public String iniciarSesion() {
	    if (user == null || user.trim().isEmpty() || password == null || password.trim().isEmpty()) {
	        showStickyLogin("400", "Usuario y contraseña son obligatorios");
	        return null;
	    }
	    try {
	        String backendUrl = "http://localhost:8081/jugadores/login?correo=" + URLEncoder.encode(user, "UTF-8")
	                + "&contrasena=" + URLEncoder.encode(password, "UTF-8");
	        URL url = new URL(backendUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setDoOutput(true);
	        int responseCode = connection.getResponseCode();
	        BufferedReader reader = new BufferedReader(
	                new InputStreamReader((responseCode >= 200 && responseCode < 400) ? connection.getInputStream()
	                        : connection.getErrorStream()));
	        StringBuilder response = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            response.append(line);
	        }
	        reader.close();
	        String mensaje = response.toString();
	        switch (responseCode) {
	        case 200:
	            // La respuesta es solo el ID del jugador (como String)
	            Long idJugador = Long.parseLong(mensaje.trim());
	            showStickyLogin("200", "Sesión iniciada exitosamente");
	            this.sesionIniciada = new Jugador();
	            this.sesionIniciada.setCorreo(user);
	            this.sesionIniciada.setId(idJugador); // Asignar el ID obtenido del backend
	            UsuarioActual.setUsuarioActual(this.sesionIniciada);
	            System.out.println("ID del usuario guardado: " + idJugador); // Depuración
	            return "/grupo.xhtml?faces-redirect=true";
	        case 400:
	            showStickyLogin("400", "Correo y contraseña son obligatorios");
	            break;
	        case 401:
	            showStickyLogin("401", "Correo o contraseña incorrectos");
	            break;
	        case 406:
	            showStickyLogin("406", "Correo inválido. Use gmail, hotmail, outlook o unbosque.edu.co");
	            break;
	        default:
	            showStickyLogin("500", "Error interno del servidor");
	            break;
	        }
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        showStickyLogin("500", "Error al procesar la respuesta del servidor");
	    } catch (Exception e) {
	        e.printStackTrace();
	        showStickyLogin("500", "Error interno del servidor");
	    }
	    return null;
	}


	private void showStickyLogin(String code, String content) {
		FacesMessage.Severity severity;
		String title;

		switch (code) {
		case "200":
			severity = FacesMessage.SEVERITY_INFO;
			title = "Éxito";
			break;
		case "400":
			severity = FacesMessage.SEVERITY_WARN;
			title = "Advertencia";
			break;
		case "401":
			severity = FacesMessage.SEVERITY_ERROR;
			title = "Error";
			break;
		case "406":
			severity = FacesMessage.SEVERITY_ERROR;
			title = "Error";
			break;
		default:
			severity = FacesMessage.SEVERITY_ERROR;
			title = "Error Crítico";
			break;
		}
		FacesContext.getCurrentInstance().addMessage("sticky-key", new FacesMessage(severity, title, content));
	}

	@PostConstruct
	public void cargarUsuarios() {
		MyLinkedList<Jugador> jugadores = JugadorService.doGetAll("http://localhost:8081/jugadores/listar");
		if (jugadores != null) {
			for (int i = 0; i < jugadores.size(); i++) {
				listUsers.addLast(jugadores.get(i));
			}
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getSesionIniciada() {
		return UsuarioActual.getUsuarioActual() != null;
	}

	public String cerrarSesion() {
		UsuarioActual.setUsuarioActual(null);
		return "index.xhtml?faces-redirect=true";
	}

	public boolean getAdminIniciada() {
		return UsuarioActual.getUsuarioActual() instanceof Jugador;
	}
}
