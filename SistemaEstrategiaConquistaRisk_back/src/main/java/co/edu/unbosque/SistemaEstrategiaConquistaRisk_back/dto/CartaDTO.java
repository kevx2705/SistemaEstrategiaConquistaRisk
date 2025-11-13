package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;

/**
 * Clase DTO que representa una carta en el juego Risk.
 * Contiene información sobre el tipo de carta, el territorio asociado
 * y su disponibilidad para ser utilizada.
 */
public class CartaDTO {

    /** Identificador único de la carta. */
    private Long id;

    /** Tipo de la carta (ej: infantería, caballería, artillería). */
    private String tipo;

    /** Territorio asociado a la carta. */
    private String territorio;

    /** Estado de disponibilidad de la carta. */
    private boolean disponible;

    /**
     * Constructor por defecto de la clase CartaDTO.
     */
    public CartaDTO() {
        // Constructor vacío
    }

    /**
     * Constructor parametrizado de la clase CartaDTO.
     *
     * @param tipo Tipo de la carta.
     * @param territorio Territorio asociado a la carta.
     * @param disponible Estado de disponibilidad de la carta.
     */
    public CartaDTO(String tipo, String territorio, boolean disponible) {
        super();
        this.tipo = tipo;
        this.territorio = territorio;
        this.disponible = disponible;
    }

    /**
     * Obtiene el identificador único de la carta.
     *
     * @return El identificador único de la carta.
     */
    public Long getId() {
        return id;
    }

    /**
     * Verifica si la carta está disponible.
     *
     * @return true si la carta está disponible, false en caso contrario.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Establece la disponibilidad de la carta.
     *
     * @param disponible Estado de disponibilidad a establecer.
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Establece el identificador único de la carta.
     *
     * @param id El identificador único a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el tipo de la carta.
     *
     * @return El tipo de la carta.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la carta.
     *
     * @param tipo El tipo de la carta a establecer.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el territorio asociado a la carta.
     *
     * @return El territorio asociado a la carta.
     */
    public String getTerritorio() {
        return territorio;
    }

    /**
     * Establece el territorio asociado a la carta.
     *
     * @param territorio El territorio a asociar con la carta.
     */
    public void setTerritorio(String territorio) {
        this.territorio = territorio;
    }

    /**
     * Genera un código hash para la carta basado en sus atributos.
     *
     * @return El código hash generado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, territorio, tipo);
    }

    /**
     * Compara esta carta con otro objeto para determinar si son iguales.
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
        CartaDTO other = (CartaDTO) obj;
        return Objects.equals(id, other.id) && Objects.equals(territorio, other.territorio)
                && Objects.equals(tipo, other.tipo);
    }

    /**
     * Devuelve una representación en cadena de la carta.
     *
     * @return Una cadena que representa los atributos de la carta.
     */
    @Override
    public String toString() {
        return "CartaDTO [id=" + id + ", tipo=" + tipo + ", territorio=" + territorio + ", disponible=" + disponible
                + "]";
    }
}
