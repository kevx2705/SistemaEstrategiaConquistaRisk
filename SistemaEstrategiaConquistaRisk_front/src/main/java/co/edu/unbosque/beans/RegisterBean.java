package co.edu.unbosque.beans;

import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.service.JugadorService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Bean administrado para el registro de jugadores.
 * Gestiona la creación de usuarios tipo jugador, valida campos,
 * encripta datos sensibles y comunica con el servicio REST del backend.
 */
@Named(value = "registerbean")
@RequestScoped
public class RegisterBean implements Serializable {

    /**
     * Versión serial para la serialización de la clase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nombre completo del jugador a registrar.
     */
    private String fullName;

    /**
     * Correo electrónico del jugador a registrar.
     */
    private String email;

    /**
     * Contraseña del jugador a registrar.
     */
    private String password;

    /**
     * Confirmación de la contraseña del jugador.
     */
    private String confirmPassword;

    /**
     * Lista de jugadores registrados en el sistema.
     */
    private MyLinkedList<Jugador> listaJugadores = new MyLinkedList<>();

	/** constructor vacio */
    public RegisterBean() {
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Crea un nuevo jugador en el sistema.
     * Valida que las contraseñas coincidan y envía la solicitud al backend.
     */
    public void crearJugador() {
        if (!password.equals(confirmPassword)) {
            showStickyLogin("406", "Las contraseñas no coinciden");
            return;
        }
        try {
            String nombreEnc = URLEncoder.encode(fullName, StandardCharsets.UTF_8.toString());
            String correoEnc = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
            String passEnc = URLEncoder.encode(password, StandardCharsets.UTF_8.toString());
            String url = "http://localhost:8081/jugadores/crear" + "?nombre=" + nombreEnc + "&correo=" + correoEnc
                    + "&contrasena=" + passEnc;
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

    /**
     * Limpia los campos del formulario de registro.
     */
    private void limpiarCampos() {
        fullName = "";
        email = "";
        password = "";
        confirmPassword = "";
    }

    /**
     * Muestra un mensaje emergente al usuario según el código de respuesta.
     * @param code Código de respuesta que determina el tipo de mensaje.
     * @param content Contenido del mensaje a mostrar.
     */
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

    /**
     * Obtiene el nombre completo del jugador.
     * @return Nombre completo del jugador.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Establece el nombre completo del jugador.
     * @param fullName Nombre completo del jugador.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Obtiene el correo electrónico del jugador.
     * @return Correo electrónico del jugador.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del jugador.
     * @param email Correo electrónico del jugador.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del jugador.
     * @return Contraseña del jugador.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del jugador.
     * @param password Contraseña del jugador.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene la confirmación de la contraseña del jugador.
     * @return Confirmación de la contraseña del jugador.
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Establece la confirmación de la contraseña del jugador.
     * @param confirmPassword Confirmación de la contraseña del jugador.
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Obtiene la lista de jugadores registrados.
     * @return Lista de jugadores.
     */
    public MyLinkedList<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    /**
     * Establece la lista de jugadores registrados.
     * @param listaJugadores Lista de jugadores a establecer.
     */
    public void setListaJugadores(MyLinkedList<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }

    /**
     * Obtiene el identificador de versión serial de la clase.
     * @return Identificador de versión serial.
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
