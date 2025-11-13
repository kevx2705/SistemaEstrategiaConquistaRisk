package co.edu.unbosque.model;

import java.util.Objects;


/**
 * Clase Usuario que representa a un usuario del sistema de entrenamiento
 */
public abstract class Usuario {

	/**
	 * Atributo identificador unico de la clase Usuario
	 */
	private Long id;
	/**
	 * Atributo nombre de la clase Usuario
	 */
	private String nombre;
	/**
	 * Atributo correo de la clase Usuario
	 */
	private String correo;
	/**
	 * Atributo edad de la clase Usuario
	 */
	private int edad;

	/**
	 * Atributo contrasena de la clase Usuario
	 */
	private String contrasena;

	/*
	 * Constructor vacio de la clase Usuario
	 */
	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parametros (atributos) de la clase Usuario, excepto el id
	 * 
	 * @param nombre
	 * @param correo
	 * @param edad
	 * @param contrasena
	 */
	public Usuario(String nombre, String correo, int edad, String contrasena) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.edad = edad;
		this.contrasena = contrasena;
	}

	/**
	 * Getter del atributo id
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter del atributo id
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter del atributo nombre
	 * 
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Setter del atributo nombre
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Getter del atributo correo
	 * 
	 * @return correo
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * Setter del atributo correo
	 * 
	 * @param correo
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * Getter del atributo edad
	 * 
	 * @return edad
	 */
	public int getEdad() {
		return edad;
	}

	/**
	 * Setter del atributo edad
	 * 
	 * @param edad
	 */
	public void setEdad(int edad) {
		this.edad = edad;
	}

	/**
	 * Getter del atributo contrasena
	 * 
	 * @return contrasena
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * Setter del atributo contrasena
	 * 
	 * @param contrasena
	 */
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	/**
	 * Metodo toString de la clase Usuario
	 * 
	 * @return String con la informacion del usuario
	 */
	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", correo=" + correo + ", contraseña=" + contrasena + "]";
	}

	/**
	 * Metodo hashCode de la clase Usuario
	 * 
	 * @return int con el codigo hash del usuario
	 */

	@Override
	public int hashCode() {
		return Objects.hash(contrasena, correo, edad, id, nombre);
	}

	/**
	 * Metodo equals de la clase Usuario
	 * 
	 * @param obj
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(contrasena, other.contrasena) && Objects.equals(correo, other.correo)
				&& edad == other.edad && Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre);
	}

}
