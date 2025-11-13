package co.edu.unbosque.model;


/**
 * Clase Jugador que representa a un Jugadoristrador del sistema de entrenamiento
 * Hereda de la clase Usuario
 */

public class Jugador extends Usuario {

	 private int index;
	
	/**
	 * Constructor vacio de la clase Jugador
	 */
	public Jugador() {
	}

	/**
	 * Constructor con parametros (atributos) heredados de Usuario
	 * 
	 * @param nombre
	 * @param correo
	 * @param edad
	 * @param contrasena
	 */
	public Jugador(String nombre, String correo, int edad, String contrasena) {
		super(nombre, correo, edad, contrasena);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Metodo hashCode de la clase Jugador
	 * 
	 * @return int con el hashCode del estudiante
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Metodo equals de la clase Jugador
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
	 * Metodo toString de la clase Jugador
	 * 
	 * @return String con la informacion del Jugadoristrador
	 */
	@Override
	public String toString() {
		return super.toString() + "Jugador";
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	

}
