package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa una carta en el juego Risk.
 * Contiene información sobre el tipo de carta, el territorio asociado
 * y su disponibilidad en el mazo.
 */
@Entity
@Table(name = "carta")
public class Carta {

    /** Identificador único de la carta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de la carta (ej: infantería, caballería, artillería, comodín). */
    private String tipo;

    /** Nombre del territorio asociado a la carta. */
    private String territorio;

    /** Estado de disponibilidad de la carta. true = está en el mazo, false = la tiene un jugador. */
    private boolean disponible;

    /**
     * Constructor por defecto de la clase Carta.
     */
    public Carta() {
    }

    /**
     * Constructor parametrizado de la clase Carta.
     *
     * @param tipo Tipo de la carta (ej: infantería, caballería, artillería, comodín).
     * @param territorio Nombre del territorio asociado a la carta.
     * @param disponible Estado de disponibilidad de la carta.
     */
    public Carta(String tipo, String territorio, boolean disponible) {
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
     * Verifica si la carta está disponible en el mazo.
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
     * Obtiene el nombre del territorio asociado a la carta.
     *
     * @return El nombre del territorio asociado.
     */
    public String getTerritorio() {
        return territorio;
    }

    /**
     * Establece el nombre del territorio asociado a la carta.
     *
     * @param territorio El nombre del territorio a asociar.
     */
    public void setTerritorio(String territorio) {
        this.territorio = territorio;
    }

    /**
     * Devuelve una representación en cadena de la carta.
     *
     * @return Una cadena que representa los atributos de la carta.
     */
    @Override
    public String toString() {
        return "Carta [id=" + id + ", tipo=" + tipo + ", territorio=" + territorio + ", disponible=" + disponible + "]";
    }
}
