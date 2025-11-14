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

/**
 * Bean de sesión que gestiona el inicio y cierre de sesión de los jugadores.
 * Realiza la autenticación con el backend y muestra mensajes de estado al usuario.
 */
@Named(value = "loginbean")
@SessionScoped
public class LogInBean implements Serializable {

    /**
     * Versión serial para la serialización de la clase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nombre de usuario o correo electrónico ingresado por el usuario.
     */
    private String user;

    /**
     * Contraseña ingresada por el usuario.
     */
    private String password;

    /**
     * Jugador cuya sesión está actualmente iniciada.
     */
    private Jugador sesionIniciada;

    /**
     * Lista de usuarios registrados en el sistema.
     */
    private MyLinkedList<Jugador> listUsers = new MyLinkedList<>();

	/** constructor vacio */
    public LogInBean() {
		// TODO Auto-generated constructor stub
	}
    /**
     * Inicia sesión con las credenciales proporcionadas.
     * Realiza una petición POST al backend para autenticar al usuario.
     * @return Ruta a la página de grupo si el inicio de sesión es exitoso, o null en caso contrario.
     */
    public String iniciarSesion() {
        if (user == null || user.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            showStickyLogin("400", "Usuario y contraseña son obligatorios");
            return null;
        }
        try {
            String backendUrl = "http://localhost:8081/jugadores/login?correo=" + URLEncoder.encode(user, "UTF-8")
                    + "&contrasena=" + URLEncoder.encode(password, "UTF-8");
            @SuppressWarnings("deprecation")
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
                Long idJugador = Long.parseLong(mensaje.trim());
                showStickyLogin("200", "Sesión iniciada exitosamente");
                this.sesionIniciada = new Jugador();
                this.sesionIniciada.setCorreo(user);
                this.sesionIniciada.setId(idJugador);
                UsuarioActual.setUsuarioActual(this.sesionIniciada);
                System.out.println("ID del usuario guardado: " + idJugador);
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

    /**
     * Muestra un mensaje emergente al usuario según el código de respuesta.
     * @param code Código de respuesta que determina el tipo de mensaje.
     * @param content Contenido del mensaje a mostrar.
     */
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

    /**
     * Carga la lista de usuarios registrados al inicializar el bean.
     */
    @PostConstruct
    public void cargarUsuarios() {
        MyLinkedList<Jugador> jugadores = JugadorService.doGetAll("http://localhost:8081/jugadores/listar");
        if (jugadores != null) {
            for (int i = 0; i < jugadores.size(); i++) {
                listUsers.addLast(jugadores.get(i));
            }
        }
    }

    /**
     * Obtiene el nombre de usuario o correo electrónico ingresado.
     * @return Nombre de usuario o correo electrónico.
     */
    public String getUser() {
        return user;
    }

    /**
     * Establece el nombre de usuario o correo electrónico.
     * @param user Nombre de usuario o correo electrónico.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Obtiene la contraseña ingresada.
     * @return Contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     * @param password Contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Verifica si hay una sesión iniciada.
     * @return true si hay una sesión iniciada, false en caso contrario.
     */
    public boolean getSesionIniciada() {
        return UsuarioActual.getUsuarioActual() != null;
    }

    /**
     * Cierra la sesión actual y redirige al inicio.
     * @return Ruta a la página de inicio.
     */
    public String cerrarSesion() {
        UsuarioActual.setUsuarioActual(null);
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * Verifica si el usuario actual es un administrador.
     * @return true si el usuario actual es un administrador, false en caso contrario.
     */
    public boolean getAdminIniciada() {
        return UsuarioActual.getUsuarioActual() instanceof Jugador;
    }
}
