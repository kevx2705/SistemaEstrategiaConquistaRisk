package co.edu.unbosque.model;

import co.edu.unbosque.estructures.MyLinkedList;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

/**
 * Clase que representa a un jugador en el sistema. Extiende la clase
 * {@link Usuario} para heredar sus atributos y comportamientos básicos.
 */
public class Jugador extends Usuario {

	/**
	 * Índice del jugador, utilizado para identificar su posición o turno en el
	 * juego.
	 */
	private int index;

	/**
	 * /** Color asignado al jugador en el juego.
	 */
	private String color;

	/** Cantidad de tropas disponibles para el jugador. */
	private int tropasDisponibles;

	/** Cantidad de territorios controlados por el jugador. */
	private int territoriosControlados;

	/** Estado de actividad del jugador. */
	private boolean activo;

	/** Lista de cartas que posee el jugador. */
	private MyLinkedList<Carta> cartas = new MyLinkedList<>();

	public Jugador() {
	}

	/**
	 * Constructor con parámetros que inicializa un jugador con los atributos
	 * heredados de {@link Usuario}.
	 *
	 * @param nombre     Nombre del jugador.
	 * @param correo     Correo electrónico del jugador.
	 * @param edad       Edad del jugador.
	 * @param contrasena Contraseña del jugador.
	 */
	public Jugador(String nombre, String correo, int edad, String contrasena) {
		super(nombre, correo, edad, contrasena);
		// TODO Auto-generated constructor stub
	}

	public Jugador(int index, String color, int tropasDisponibles, int territoriosControlados, boolean activo,
			MyLinkedList<Carta> cartas) {
		super();
		this.index = index;
		this.color = color;
		this.tropasDisponibles = tropasDisponibles;
		this.territoriosControlados = territoriosControlados;
		this.activo = activo;
		this.cartas = cartas;
	}

	/**
	 * Genera un código hash para el jugador. Utiliza el método hashCode de la clase
	 * padre {@link Usuario}.
	 *
	 * @return Código hash del jugador.
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Compara este jugador con otro objeto para determinar si son iguales. Utiliza
	 * el método equals de la clase padre {@link Usuario}.
	 *
	 * @param obj Objeto a comparar.
	 * @return {@code true} si los objetos son iguales, {@code false} en caso
	 *         contrario.
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
	 * Devuelve una representación en cadena del jugador. Extiende la representación
	 * de la clase padre {@link Usuario} añadiendo información específica del
	 * jugador.
	 *
	 * @return Cadena que representa al jugador.
	 */
	@Override
	public String toString() {
		return super.toString() + "Jugador";
	}

	/**
	 * Obtiene el índice del jugador.
	 *
	 * @return Índice del jugador.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Establece el índice del jugador.
	 *
	 * @param index Índice a establecer.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getTropasDisponibles() {
		return tropasDisponibles;
	}

	public void setTropasDisponibles(int tropasDisponibles) {
		this.tropasDisponibles = tropasDisponibles;
	}

	public int getTerritoriosControlados() {
		return territoriosControlados;
	}

	public void setTerritoriosControlados(int territoriosControlados) {
		this.territoriosControlados = territoriosControlados;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public MyLinkedList<Carta> getCartas() {
		return cartas;
	}

	public void setCartas(MyLinkedList<Carta> cartas) {
		this.cartas = cartas;
	}

}
