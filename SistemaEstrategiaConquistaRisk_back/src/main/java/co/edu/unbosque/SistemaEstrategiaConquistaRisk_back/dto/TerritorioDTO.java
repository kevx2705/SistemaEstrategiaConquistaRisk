package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;

/**
 * Clase DTO que representa un territorio en el juego Risk.
 * Contiene información sobre el nombre del territorio, la cantidad de tropas,
 * el continente al que pertenece y el jugador que lo controla.
 */
public class TerritorioDTO {

    /** Identificador único del territorio. */
    private Long id;

    /** Nombre del territorio. */
    private String nombre;

    /** Cantidad de tropas ubicadas en el territorio. */
    private int tropas;

    /** Identificador del continente al que pertenece el territorio. */
    private Long idContinente;

    /** Identificador del jugador que controla el territorio. */
    private Long idJugador;

    /**
     * Constructor por defecto de la clase TerritorioDTO.
     */
    public TerritorioDTO() {
        // Constructor vacío
    }

    /**
     * Constructor parametrizado de la clase TerritorioDTO.
     *
     * @param nombre       Nombre del territorio.
     * @param tropas       Cantidad de tropas en el territorio.
     * @param idContinente Identificador del continente al que pertenece el territorio.
     * @param idJugador    Identificador del jugador que controla el territorio.
     */
    public TerritorioDTO(String nombre, int tropas, Long idContinente, Long idJugador) {
        super();
        this.nombre = nombre;
        this.tropas = tropas;
        this.idContinente = idContinente;
        this.idJugador = idJugador;
    }

    /**
     * Obtiene el identificador único del territorio.
     *
     * @return El identificador único del territorio.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del territorio.
     *
     * @param id El identificador único a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del territorio.
     *
     * @return El nombre del territorio.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del territorio.
     *
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la cantidad de tropas en el territorio.
     *
     * @return La cantidad de tropas en el territorio.
     */
    public int getTropas() {
        return tropas;
    }

    /**
     * Establece la cantidad de tropas en el territorio.
     *
     * @param tropas La cantidad de tropas a establecer.
     */
    public void setTropas(int tropas) {
        this.tropas = tropas;
    }

    /**
     * Obtiene el identificador del continente al que pertenece el territorio.
     *
     * @return El identificador del continente.
     */
    public Long getIdContinente() {
        return idContinente;
    }

    /**
     * Establece el identificador del continente al que pertenece el territorio.
     *
     * @param idContinente El identificador del continente a establecer.
     */
    public void setIdContinente(Long idContinente) {
        this.idContinente = idContinente;
    }

    /**
     * Obtiene el identificador del jugador que controla el territorio.
     *
     * @return El identificador del jugador.
     */
    public Long getIdJugador() {
        return idJugador;
    }

    /**
     * Establece el identificador del jugador que controla el territorio.
     *
     * @param idJugador El identificador del jugador a establecer.
     */
    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    /**
     * Genera un código hash para el territorio basado en sus atributos.
     *
     * @return El código hash generado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, idContinente, idJugador, nombre, tropas);
    }

    /**
     * Compara este territorio con otro objeto para determinar si son iguales.
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
        TerritorioDTO other = (TerritorioDTO) obj;
        return Objects.equals(id, other.id) && Objects.equals(idContinente, other.idContinente)
                && Objects.equals(idJugador, other.idJugador) && Objects.equals(nombre, other.nombre)
                && tropas == other.tropas;
    }

    /**
     * Devuelve una representación en cadena del territorio.
     *
     * @return Una cadena que representa los atributos del territorio.
     */
    @Override
    public String toString() {
        return "TerritorioDTO [id=" + id + ", nombre=" + nombre + ", tropas=" + tropas + ", idContinente="
                + idContinente + ", idJugador=" + idJugador + "]";
    }
}
