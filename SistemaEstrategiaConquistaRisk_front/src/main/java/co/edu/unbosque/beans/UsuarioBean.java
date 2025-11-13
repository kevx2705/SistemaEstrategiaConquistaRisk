package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

import co.edu.unbosque.estructures.MyLinkedList;
import co.edu.unbosque.estructures.Node;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.service.JugadorService;

@Named("usuariosBean")
@ViewScoped
/**
 * Bean de vista para la administración de usuarios del sistema. Consolida las
 * listas de administradores, profesores y estudiantes obtenidas de los
 * servicios REST y permite eliminar usuarios individuales.
 */
public class UsuarioBean implements Serializable {

	private MyLinkedList<Jugador> jugadores;

	/** Usuario actualmente seleccionado (por ejemplo para mostrar detalles). */
	private Jugador usuarioSeleccionado;
	private Node<Jugador> first;

	/**
	 * Constructor que dispara la carga inicial de usuarios.
	 */
	public UsuarioBean() {
		cargarUsuarios();
	}

	/**
	 * Carga las listas de usuarios desde los servicios REST y construye una lista
	 * consolidada. Marcado también como {@link PostConstruct} para garantizar su
	 * ejecución tras la construcción del bean.
	 */
	@PostConstruct
	public void cargarUsuarios() {
		jugadores = new MyLinkedList<>();
		jugadores = JugadorService.doGetAll("http://localhost:8081/jugadores/listar");
	}

	/**
	 * Muestra mensajes de estado según el resultado de operaciones con usuarios.
	 * 
	 * @param code    Código de estado.
	 * @param content Mensaje adicional.
	 */
	public void showStickyLogin(String code, String content) {
		if (code.equals("201")) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Hecho", ", se ha creado el usuario"));
		} else if (code.equals("406")) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", content));
		} else if (code.equals("204")) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Hecho", ", se ha eliminado el usuario"));
		} else {
			FacesContext.getCurrentInstance().addMessage("sticky-key", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error Critico", "Error,comuniquese con el administrador"));
		}
	}
	// ==============================
	// MÉTODOS PERSONALIZADOS
	// ==============================

	/**
	 * Busca un jugador por nombre y contraseña.
	 * 
	 * @param Name   nombre del jugador
	 * @param password contraseña del jugador
	 * @return el jugador encontrado o null si no existe
	 */
	public Jugador findByNameAndPassword(String Name, String password) {
		return findByNameAndPasswordRec(first, Name, password);
	}

	private Jugador findByNameAndPasswordRec(Node<Jugador> current, String nombre, String password) {
		if (current == null)
			return null;

		Jugador jugador = current.getInfo();
		if (jugador.getCorreo() != null && jugador.getNombre() != null && jugador.getNombre().equalsIgnoreCase(nombre)
				&& jugador.getContrasena().equals(password)) {
			return jugador;
		}

		return findByNameAndPasswordRec(current.getNext(), nombre, password);
	}
	
	/**
	 * Busca un jugador por su correo (sin distinguir mayúsculas/minúsculas).
	 * 
	 * @param correo correo del jugador
	 * @return jugador encontrado o null si no existe
	 */
	public Jugador findByCorreo(String correo) {
		return findByCorreoRec(first, correo);
	}

	private Jugador findByCorreoRec(Node<Jugador> current, String correo) {
		if (current == null)
			return null;

		Jugador jugador = current.getInfo();
		if (jugador.getCorreo() != null && jugador.getCorreo().equalsIgnoreCase(correo)) {
			return jugador;
		}

		return findByCorreoRec(current.getNext(), correo);
	}

	public MyLinkedList<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(MyLinkedList<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public Jugador getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Jugador usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

}