package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;

/**
 * Clase DTO que representa un continente en el juego Risk.
 * Contiene información sobre el nombre del continente, el refuerzo otorgado por controlarlo
 * y la cantidad total de territorios que lo componen.
 */
public class ContinenteDTO {

    /** Identificador único del continente. */
    private Long id;

    /** Nombre del continente. */
    private String nombre;

    /** Cantidad de refuerzos otorgados por controlar el continente. */
    private int refuerzoPorControl;

    /** Cantidad total de territorios que componen el continente. */
    private int territoriosTotales;

    /**
     * Constructor por defecto de la clase ContinenteDTO.
     */
    public ContinenteDTO() {
        // Constructor vacío
    }

    /**
     * Constructor parametrizado de la clase ContinenteDTO.
     *
     * @param nombre              Nombre del continente.
     * @param refuerzoPorControl  Refuerzos otorgados por controlar el continente.
     * @param territoriosTotales  Total de territorios que componen el continente.
     */
    public ContinenteDTO(String nombre, int refuerzoPorControl, int territoriosTotales) {
        super();
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
     * Establece el identificador único del continente.
     *
     * @param id El identificador único a establecer.
     */
    public void setId(Long id) {
        this.id = id;
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
        ContinenteDTO other = (ContinenteDTO) obj;
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
        return "ContinenteDTO [id=" + id + ", nombre=" + nombre + ", refuerzoPorControl=" + refuerzoPorControl
                + ", territoriosTotales=" + territoriosTotales + "]";
    }
}
