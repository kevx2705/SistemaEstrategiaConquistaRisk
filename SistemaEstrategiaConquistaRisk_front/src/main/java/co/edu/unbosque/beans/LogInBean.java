package co.edu.unbosque.beans;

import java.io.Serializable;

import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.model.Usuario;
import co.edu.unbosque.model.UsuarioActual;
import co.edu.unbosque.service.JugadorService;
import co.edu.unbosque.estrucutres.MyLinkedList;
import co.edu.unbosque.estrucutres.Node;
import co.edu.unbosque.util.AESUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

/**
 * Bean de sesión encargado del proceso de autenticación de usuarios. Usa
 * MyLinkedList en lugar de ArrayList.
 */
@Named(value = "loginbean")
@SessionScoped
public class LogInBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user;
	private String password;
	private Usuario sesionIniciada;
	private MyLinkedList<Jugador> listUsers = new MyLinkedList<>();

	public LogInBean() {
		cargarUsuarios();
	}

	public void iniciarSesion() {
	    boolean encontrado = false;
	    System.out.println(listUsers.toString());
	    for (int i = 0; i < listUsers.size(); i++) {
	        Node<Jugador> nodo = listUsers.getPos(i);
	        if (nodo == null)
	            continue;

	        Jugador usuario = nodo.getInfo();

	        try {
	            String nombreEncriptado = usuario.getNombre();
	            String contrasenaEncriptada = usuario.getContrasena();

	            if (nombreEncriptado == null || contrasenaEncriptada == null)
	            	
	                continue;
	            // Intentar desencriptar
	            System.out.println("qweqweqw");
	            String usuarioN = AESUtil.decrypt(nombreEncriptado);
	            String contrasenaN = AESUtil.decrypt(contrasenaEncriptada);

	            if (usuarioN == null) usuarioN = nombreEncriptado;
	            if (contrasenaN == null) contrasenaN = contrasenaEncriptada;

	            if (usuarioN != null && contrasenaN != null
	                    && usuarioN.trim().equalsIgnoreCase(user.trim())
	                    && contrasenaN.trim().equals(password.trim())) {

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

	public void cargarUsuarios() {
		MyLinkedList<Jugador> jugadores = JugadorService.doGetAll("http://localhost:8081/jugadores/listar");
		for (int i = 0; i < jugadores.size(); i++) {
			Node<Jugador> nodo = jugadores.getPos(i);
			if (nodo != null) {
				listUsers.addLast(nodo.getInfo());
			}
		}
	}

	public void showStickyLogin(String code, String content) {
		FacesMessage.Severity severity;
		String title;

		switch (code) {
		case "202":
			severity = FacesMessage.SEVERITY_INFO;
			title = "Hecho";
			break;
		case "401":
			severity = FacesMessage.SEVERITY_WARN;
			title = "Error";
			break;
		default:
			severity = FacesMessage.SEVERITY_ERROR;
			title = "Error Critico";
			content = "Error al crear, comuníquese con el administrador";
		}

		FacesContext.getCurrentInstance().addMessage("sticky-key", new FacesMessage(severity, title, content));
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

	public MyLinkedList<Jugador> getListUsers() {
		return listUsers;
	}

	public void setListUsers(MyLinkedList<Jugador> listUsers) {
		this.listUsers = listUsers;
	}

	public boolean getSesionIniciada() {
		return UsuarioActual.getUsuarioActual() != null;
	}

	public void setSesionIniciada(Jugador sesionIniciada) {
		this.sesionIniciada = sesionIniciada;
	}

	public String cerrarSesion() {
		UsuarioActual.setUsuarioActual(null);
		return "index.xhtml?faces-redirect=true";
	}

	/**
	 * @return {@code true} si el usuario autenticado es un administrador.
	 */
	public boolean getAdminIniciada() {
		return UsuarioActual.getUsuarioActual() instanceof Jugador;

	}

}
