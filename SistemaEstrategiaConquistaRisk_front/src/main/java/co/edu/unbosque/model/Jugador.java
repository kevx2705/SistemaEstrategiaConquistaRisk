package co.edu.unbosque.model;

/**
 * Clase que representa a un jugador en el sistema.
 * Extiende la clase {@link Usuario} para heredar sus atributos y comportamientos básicos.
 */
public class Jugador extends Usuario {

    /**
     * Índice del jugador, utilizado para identificar su posición o turno en el juego.
     */
    private int index;

    /**
     * Constructor vacío de la clase Jugador.
     */
    public Jugador() {
    }

    /**
     * Constructor con parámetros que inicializa un jugador con los atributos heredados de {@link Usuario}.
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

    /**
     * Genera un código hash para el jugador.
     * Utiliza el método hashCode de la clase padre {@link Usuario}.
     *
     * @return Código hash del jugador.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Compara este jugador con otro objeto para determinar si son iguales.
     * Utiliza el método equals de la clase padre {@link Usuario}.
     *
     * @param obj Objeto a comparar.
     * @return {@code true} si los objetos son iguales, {@code false} en caso contrario.
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
     * Devuelve una representación en cadena del jugador.
     * Extiende la representación de la clase padre {@link Usuario} añadiendo información específica del jugador.
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
}
