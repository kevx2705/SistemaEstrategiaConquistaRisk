package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;

/**
 * Clase DTO que representa a un jugador en el juego Risk.
 * Contiene información sobre el nombre, correo, contraseña, color,
 * tropas disponibles, territorios controlados, estado de actividad
 * y las cartas que posee el jugador.
 */
public class JugadorDTO {

    /** Identificador único del jugador. */
    private Long id;

    /** Nombre del jugador. */
    private String nombre;

    /** Correo electrónico del jugador. */
    private String correo;

    /** Contraseña del jugador. */
    private String contrasena;

    /** Color asignado al jugador en el juego. */
    private String color;

    /** Cantidad de tropas disponibles para el jugador. */
    private int tropasDisponibles;

    /** Cantidad de territorios controlados por el jugador. */
    private int territoriosControlados;

    /** Estado de actividad del jugador. */
    private boolean activo;

    /** Lista de cartas que posee el jugador. */
    private MyLinkedList<CartaDTO> cartas = new MyLinkedList<>();

    /**
     * Constructor por defecto de la clase JugadorDTO.
     */
    public JugadorDTO() {
        // Constructor vacío
    }

    /**
     * Constructor parametrizado de la clase JugadorDTO.
     *
     * @param nombre                Nombre del jugador.
     * @param correo                Correo electrónico del jugador.
     * @param contrasena            Contraseña del jugador.
     * @param color                 Color asignado al jugador.
     * @param tropasDisponibles     Tropas disponibles para el jugador.
     * @param territoriosControlados Territorios controlados por el jugador.
     * @param activo                Estado de actividad del jugador.
     * @param cartas                Lista de cartas que posee el jugador.
     */
    public JugadorDTO(String nombre, String correo, String contrasena, String color, int tropasDisponibles,
            int territoriosControlados, boolean activo, MyLinkedList<CartaDTO> cartas) {
        super();
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.color = color;
        this.tropasDisponibles = tropasDisponibles;
        this.territoriosControlados = territoriosControlados;
        this.activo = activo;
        this.cartas = cartas;
    }

    /**
     * Obtiene el correo electrónico del jugador.
     *
     * @return El correo electrónico del jugador.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Obtiene la contraseña del jugador.
     *
     * @return La contraseña del jugador.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del jugador.
     *
     * @param contrasena La contraseña a establecer.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Establece el correo electrónico del jugador.
     *
     * @param correo El correo electrónico a establecer.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la lista de cartas que posee el jugador.
     *
     * @return La lista de cartas del jugador.
     */
    public MyLinkedList<CartaDTO> getCartas() {
        return cartas;
    }

    /**
     * Establece la lista de cartas del jugador.
     *
     * @param cartas La lista de cartas a establecer.
     */
    public void setCartas(MyLinkedList<CartaDTO> cartas) {
        this.cartas = cartas;
    }

    /**
     * Obtiene el identificador único del jugador.
     *
     * @return El identificador único del jugador.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del jugador.
     *
     * @param id El identificador único a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return El nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el color asignado al jugador.
     *
     * @return El color del jugador.
     */
    public String getColor() {
        return color;
    }

    /**
     * Establece el color del jugador.
     *
     * @param color El color a establecer.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Obtiene la cantidad de tropas disponibles para el jugador.
     *
     * @return La cantidad de tropas disponibles.
     */
    public int getTropasDisponibles() {
        return tropasDisponibles;
    }

    /**
     * Establece la cantidad de tropas disponibles para el jugador.
     *
     * @param tropasDisponibles La cantidad de tropas a establecer.
     */
    public void setTropasDisponibles(int tropasDisponibles) {
        this.tropasDisponibles = tropasDisponibles;
    }

    /**
     * Obtiene la cantidad de territorios controlados por el jugador.
     *
     * @return La cantidad de territorios controlados.
     */
    public int getTerritoriosControlados() {
        return territoriosControlados;
    }

    /**
     * Establece la cantidad de territorios controlados por el jugador.
     *
     * @param territoriosControlados La cantidad de territorios a establecer.
     */
    public void setTerritoriosControlados(int territoriosControlados) {
        this.territoriosControlados = territoriosControlados;
    }

    /**
     * Verifica si el jugador está activo.
     *
     * @return true si el jugador está activo, false en caso contrario.
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad del jugador.
     *
     * @param activo El estado de actividad a establecer.
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Genera un código hash para el jugador basado en sus atributos.
     *
     * @return El código hash generado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(activo, color, id, nombre, territoriosControlados, tropasDisponibles);
    }

    /**
     * Compara este jugador con otro objeto para determinar si son iguales.
     *
     * @param obj El objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JugadorDTO other = (JugadorDTO) obj;
        return activo == other.activo && Objects.equals(color, other.color) && Objects.equals(id, other.id)
                && Objects.equals(nombre, other.nombre) && territoriosControlados == other.territoriosControlados
                && tropasDisponibles == other.tropasDisponibles;
    }

    /**
     * Devuelve una representación en cadena del jugador.
     *
     * @return Una cadena que representa los atributos del jugador.
     */
    @Override
    public String toString() {
        return "JugadorDTO [id=" + id + ", nombre=" + nombre + ", color=" + color + ", tropasDisponibles="
                + tropasDisponibles + ", territoriosControlados=" + territoriosControlados + ", activo=" + activo + "]";
    }
}
