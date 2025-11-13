package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa un territorio en el juego Risk.
 * Contiene información sobre el nombre del territorio, la cantidad de tropas,
 * el continente al que pertenece y el jugador que lo controla.
 */
@Entity
@Table(name = "territorio")
public class Territorio {

    /** Identificador único del territorio. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del territorio. */
    private String nombre;

    /** Cantidad de tropas ubicadas en el territorio. */
    private int tropas;

    /** Identificador del continente al que pertenece el territorio. */
    private Long idContinente;

    /** Identificador del jugador que controla el territorio. Por defecto, 0L indica que no está controlado por ningún jugador. */
    private Long idJugador = 0L;

    /**
     * Constructor por defecto de la clase Territorio.
     */
    public Territorio() {
        // Constructor vacío
    }

    /**
     * Constructor parametrizado de la clase Territorio.
     *
     * @param nombre       Nombre del territorio.
     * @param idContinente Identificador del continente al que pertenece el territorio.
     * @param tropas       Cantidad de tropas en el territorio.
     */
    public Territorio(String nombre, Long idContinente, int tropas) {
        this.nombre = nombre;
        this.idContinente = idContinente;
        this.tropas = tropas;
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
     * @return El identificador del jugador que controla el territorio.
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
        Territorio other = (Territorio) obj;
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
        return "Territorio [id=" + id + ", nombre=" + nombre + ", tropas=" + tropas + ", idContinente="
                + idContinente + ", idJugador=" + idJugador + "]";
    }
}
