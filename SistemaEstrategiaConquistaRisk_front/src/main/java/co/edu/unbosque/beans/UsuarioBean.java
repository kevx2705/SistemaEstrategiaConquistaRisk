package co.edu.unbosque.beans;
package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("usuariosBean")
@ViewScoped
/**
 * Bean de vista para la administración de usuarios del sistema. Consolida las
 * listas de administradores, profesores y estudiantes obtenidas de los
 * servicios REST y permite eliminar usuarios individuales.
 */
public class UsuarioBean implements Serializable {

	private ArrayList<Admin> admins;

	/** Usuario actualmente seleccionado (por ejemplo para mostrar detalles). */
	private Usuario usuarioSeleccionado;

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
		usuarios = new ArrayList<>();
		admins = AdministradorService.doGetAll("http://localhost:8081/admin/getall");
		profesores = ProfesorService.doGetAll("http://localhost:8081/profesor/getall");
		estudiantes = EstudianteService.doGetAll("http://localhost:8081/estudiante/getall");
		usuarios.addAll(admins);
		usuarios.addAll(profesores);
		usuarios.addAll(estudiantes);
	}

	/**
	 * @return Lista consolidada de usuarios.
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	/**
	 * @return Usuario actualmente seleccionado.
	 */
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	/**
	 * @param usuarioSeleccionado Usuario a marcar como seleccionado.
	 */
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	/**
	 * Devuelve una descripción legible del tipo de usuario.
	 * 
	 * @param u Usuario a evaluar.
	 * @return Tipo legible (Administrador/Profesor/Estudiante/Usuario).
	 */
	public String getTipoUsuario(Usuario u) {
		if (u instanceof Admin)
			return "Administrador";
		if (u instanceof Profesor)
			return "Profesor";
		if (u instanceof Estudiante)
			return "Estudiante";
		return "Usuario";
	}

	/**
	 * Elimina un usuario invocando el servicio REST correspondiente según su
	 * tipo. Tras la operación recarga la lista consolidada y muestra mensajes al
	 * usuario.
	 * 
	 * @param u Usuario a eliminar.
	 */
	public void eliminarUsuario(Usuario u) {
		usuarios.remove(u);
		String respuesta = "";
		switch (getTipoUsuario(u)) {
		case "Administrador":
			respuesta = ProblemaService.doDelete("http://localhost:8081/admin/eliminar?id=" + u.getId());
			break;
		case "Profesor":
			respuesta = ProblemaService.doDelete("http://localhost:8081/profesor/eliminar?id=" + u.getId());
			break;
		case "Estudiante":
			respuesta = ProblemaService.doDelete("http://localhost:8081/estudiante/eliminar?id=" + u.getId());
			break;
		default:
			respuesta = "400 Error";
			break;
		}
		String[] data = respuesta.split("\n");
		if (data[0].equals("204")) {
			showStickyLogin(data[0], "usuario eliminado");
			cargarUsuarios();
			return;
		} else {
			showStickyLogin(data[0], "no se ha podido eliminar el usuario.");
			cargarUsuarios();
			return;
		}
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

	/**
	 * Selecciona un usuario para ver sus detalles.
	 * 
	 * @param u Usuario a seleccionar.
	 */
	public void seleccionarUsuario(Usuario u) {
		this.usuarioSeleccionado = u;
	}

	/**
	 * @return Lista de administradores.
	 */
	public ArrayList<Admin> getAdmins() {
		return admins;
	}

	/**
	 * @param admins Lista de administradores.
	 */
	public void setAdmins(ArrayList<Admin> admins) {
		this.admins = admins;
	}

	/**
	 * @return Lista de profesores.
	 */
	public ArrayList<Profesor> getProfesores() {
		return profesores;
	}

	/**
	 * @param profesores Lista de profesores.
	 */
	public void setProfesores(ArrayList<Profesor> profesores) {
		this.profesores = profesores;
	}

	/**
	 * @return Lista de estudiantes.
	 */
	public ArrayList<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	/**
	 * @param estudiantes Lista de estudiantes.
	 */
	public void setEstudiantes(ArrayList<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	/**
	 * @param usuarios Lista consolidada de usuarios.
	 */
	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	
}