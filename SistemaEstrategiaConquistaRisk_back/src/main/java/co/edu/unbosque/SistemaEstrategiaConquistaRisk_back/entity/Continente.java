package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa un continente en el juego Risk.
 * Contiene información sobre el nombre del continente,
 * los refuerzos otorgados por controlarlo y la cantidad total de territorios que lo componen.
 */
@Entity
@Table(name = "continente")
public class Continente {

    /** Identificador único del continente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del continente. */
    private String nombre;

    /** Cantidad de refuerzos otorgados por controlar el continente. */
    private int refuerzoPorControl;

    /** Cantidad total de territorios que componen el continente. */
    private int territoriosTotales;

    /**
     * Constructor por defecto de la clase Continente.
     */
    public Continente() {
    }

    /**
     * Constructor parametrizado de la clase Continente.
     *
     * @param nombre              Nombre del continente.
     * @param refuerzoPorControl  Refuerzos otorgados por controlar el continente.
     * @param territoriosTotales  Total de territorios que componen el continente.
     */
    public Continente(String nombre, int refuerzoPorControl, int territoriosTotales) {
        this.nombre = nombre;
        this.refuerzoPorControl = refuerzoPorControl;
        this.territoriosTotales = territoriosTotales;
    }

    /**
     * Obtiene el identificador único del continente.
     *
     * @return El identificador único del continente.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtiene el nombre del continente.
     *
     * @return El nombre del continente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del continente.
     *
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la cantidad de refuerzos otorgados por controlar el continente.
     *
     * @return La cantidad de refuerzos por control.
     */
    public int getRefuerzoPorControl() {
        return refuerzoPorControl;
    }

    /**
     * Establece la cantidad de refuerzos otorgados por controlar el continente.
     *
     * @param refuerzoPorControl La cantidad de refuerzos a establecer.
     */
    public void setRefuerzoPorControl(int refuerzoPorControl) {
        this.refuerzoPorControl = refuerzoPorControl;
    }

    /**
     * Obtiene la cantidad total de territorios que componen el continente.
     *
     * @return La cantidad total de territorios.
     */
    public int getTerritoriosTotales() {
        return territoriosTotales;
    }

    /**
     * Establece la cantidad total de territorios que componen el continente.
     *
     * @param territoriosTotales La cantidad total de territorios a establecer.
     */
    public void setTerritoriosTotales(int territoriosTotales) {
        this.territoriosTotales = territoriosTotales;
    }

    /**
     * Genera un código hash para el continente basado en sus atributos.
     *
     * @return El código hash generado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, refuerzoPorControl, territoriosTotales);
    }

    /**
     * Compara este continente con otro objeto para determinar si son iguales.
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
        Continente other = (Continente) obj;
        return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
                && refuerzoPorControl == other.refuerzoPorControl && territoriosTotales == other.territoriosTotales;
    }

    /**
     * Devuelve una representación en cadena del continente.
     *
     * @return Una cadena que representa los atributos del continente.
     */
    @Override
    public String toString() {
        return "Continente [id=" + id + ", nombre=" + nombre + ", refuerzoPorControl=" + refuerzoPorControl
                + ", territoriosTotales=" + territoriosTotales + "]";
    }
}
