package co.edu.unbosque.model;

/**
 * Clase que representa una carta en el juego.
 * Cada carta tiene un identificador único, un nombre, un tipo y un estado de disponibilidad.
 */
public class Carta {

    /**
     * Identificador único de la carta.
     */
    private Long id;

    /**
     * Nombre de la carta.
     */
    private String nombre;

    /**
     * Tipo de la carta.
     */
    private String tipo;

    /**
     * Estado de disponibilidad de la carta.
     */
    private boolean disponible;

    /**
     * Constructor por defecto de la clase Carta.
     */
    public Carta() {}

    /**
     * Constructor que inicializa una carta con sus atributos básicos.
     *
     * @param id         Identificador único de la carta.
     * @param nombre     Nombre de la carta.
     * @param tipo       Tipo de la carta.
     * @param disponible Estado de disponibilidad de la carta.
     */
    public Carta(Long id, String nombre, String tipo, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.disponible = disponible;
    }

    /**
     * Obtiene el identificador único de la carta.
     *
     * @return Identificador único de la carta.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la carta.
     *
     * @param id Identificador único a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la carta.
     *
     * @return Nombre de la carta.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la carta.
     *
     * @param nombre Nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el tipo de la carta.
     *
     * @return Tipo de la carta.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la carta.
     *
     * @param tipo Tipo a establecer.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Verifica si la carta está disponible.
     *
     * @return {@code true} si la carta está disponible, {@code false} en caso contrario.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Establece el estado de disponibilidad de la carta.
     *
     * @param disponible Estado de disponibilidad a establecer.
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
