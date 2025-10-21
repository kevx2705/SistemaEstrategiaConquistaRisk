package co.edu.unbosque.beans;

import java.io.Serializable;
import java.util.ArrayList;

import co.edu.unbosque.model.Admin;
import co.edu.unbosque.model.Usuario;
import co.edu.unbosque.model.UsuarioActual;
import co.edu.unbosque.service.AdministradorService;
import co.edu.unbosque.util.AESUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

/**
 * Bean de sesión encargado del proceso de autenticación de usuarios.
 * Responsabilidades: Cargar todos los usuarios disponibles desde los diferentes
 * servicios (Admin, Estudiante, Profesor). Validar credenciales ingresadas
 * contra la lista cargada (desencriptando datos). Mantener en sesión el usuario
 * autenticado a través de {@link UsuarioActual}. Proveer utilidades para cerrar
 * sesión y verificar el tipo de usuario autenticado.
 */
@Named(value = "loginbean")
@SessionScoped
public class LogInBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Nombre de usuario digitado. */
	private String user;
	/** Contraseña digitada (en texto plano para validación). */
	private String password;
	/** Referencia opcional (no usada directamente) al usuario en sesión. */
	private Usuario sesionIniciada;
	/** Lista consolidada de usuarios obtenidos de los servicios REST. */
	ArrayList<Usuario> listUsers = new ArrayList<>();

	/**
	 * Constructor que dispara la carga de usuarios desde los servicios.
	 */
	public LogInBean() {
		cargarUsuarios();
	}

	/**
	 * Intenta autenticar al usuario comparando las credenciales ingresadas con la
	 * lista de usuarios obtenida de los servicios. Las credenciales almacenadas
	 * están cifradas y se descifran temporalmente para la validación.
	 */
	public void iniciarSesion() {
		boolean encontrado = false;

		for (Usuario usuario : listUsers) {
			try {
				String usuarioN = AESUtil.decrypt(usuario.getNombre()).trim();
				String contrasenaN = AESUtil.decrypt(usuario.getContrasena()).trim();

				if (usuarioN.equalsIgnoreCase(user.trim()) && contrasenaN.equals(password.trim())) {
					showStickyLogin("202", "Sesión iniciada exitosamente");

					UsuarioActual.setUsuarioActual(usuario);

					password = "";

					encontrado = true;
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!encontrado) {
			showStickyLogin("401", "Credenciales inválidas");
			password = "";
		}
	}

	/**
	 * Carga todos los usuarios de los diferentes servicios y los unifica en una
	 * sola lista para facilitar la validación de credenciales.
	 */
	public void cargarUsuarios() {
		listUsers.addAll(AdministradorService.doGetAll("http://localhost:8081/admin/getall"));
	}

	/**
	 * Muestra mensajes de resultado de la operación de autenticación.
	 * 
	 * @param code    Código de resultado (200 éxito, 401 credenciales inválidas).
	 * @param content Mensaje adicional.
	 */
	public void showStickyLogin(String code, String content) {
		if (code.equals("202")) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Hecho", content));
		} else if (code.equals("401")) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", content));
		} else {
			FacesContext.getCurrentInstance().addMessage("sticky-key", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error Critico", "Error al crear,comuniquese con el administrador"));
		}
	}

	/**
	 * @return Nombre de usuario ingresado.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user Nombre de usuario digitado.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return Contraseña ingresada.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password Contraseña ingresada.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Lista consolidada de usuarios.
	 */
	public ArrayList<Usuario> getListUsers() {
		return listUsers;
	}

	/**
	 * @param listUsers Lista de usuarios a establecer (usualmente solo para tests).
	 */
	public void setListUsers(ArrayList<Usuario> listUsers) {
		this.listUsers = listUsers;
	}

	/**
	 * @return {@code true} si existe un usuario autenticado.
	 */
	public boolean getSesionIniciada() {
		return UsuarioActual.getUsuarioActual() != null;
	}

	/**
	 * @param sesionIniciada Usuario de sesión (no usado directamente; mantenido por
	 *                       compatibilidad).
	 */
	public void setSesionIniciada(Usuario sesionIniciada) {
		this.sesionIniciada = sesionIniciada;
	}

	/**
	 * Cierra la sesión eliminando el usuario actual y redirige a la página de
	 * inicio.
	 * 
	 * @return Navegación a index con redirect.
	 */
	public String cerrarSesion() {
		UsuarioActual.setUsuarioActual(null);
		return "index.xhtml?faces-redirect=true";
	}

	/**
	 * @return {@code true} si el usuario autenticado es un administrador.
	 */
	public boolean getAdminIniciada() {
		return UsuarioActual.getUsuarioActual() instanceof Admin;
	}

}
