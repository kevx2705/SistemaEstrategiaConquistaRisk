package co.edu.unbosque.model;


/**
 * Clase Admin que representa a un administrador del sistema de entrenamiento
 * Hereda de la clase Usuario
 */

public class Admin extends Usuario {

	/**
	 * Constructor vacio de la clase Admin
	 */
	public Admin() {
	}

	/**
	 * Constructor con parametros (atributos) heredados de Usuario
	 * 
	 * @param nombre
	 * @param correo
	 * @param edad
	 * @param contrasena
	 */
	public Admin(String nombre, String correo, int edad, String contrasena) {
		super(nombre, correo, edad, contrasena);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Metodo hashCode de la clase Admin
	 * 
	 * @return int con el hashCode del estudiante
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Metodo equals de la clase Admin
	 * 
	 * @param obj Objeto a comparar
	 * @return boolean que indica si son iguales o no
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	/**
	 * Metodo toString de la clase Admin
	 * 
	 * @return String con la informacion del administrador
	 */
	@Override
	public String toString() {
		return super.toString() + "Admin";
	}

}
