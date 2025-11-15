package co.edu.unbosque.model;

/**
 * Clase que representa un territorio en el juego.
 * Cada territorio tiene un identificador único, un nombre, un identificador del jugador que lo controla
 * y la cantidad de tropas desplegadas en él.
 */
public class Territorio {

    /**
     * Identificador único del territorio.
     */
    private Long id;

    /**
     * Nombre del territorio.
     */
    private String nombre;

    /**
     * Identificador del jugador que controla el territorio.
     */
    private Long idJugador;
    private Long idContinente;

    /**
     * Número de tropas desplegadas en el territorio.
     */
    private int tropas;

    /**
     * Obtiene el identificador único del territorio.
     *
     * @return Identificador único del territorio.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del territorio.
     *
     * @param id Identificador único a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del territorio.
     *
     * @return Nombre del territorio.
     */
    public String getNombre() {
        return nombre;
    }
	/** constructor vacio */
    public Territorio() {
		// TODO Auto-generated constructor stub
	}
    /**
     * Establece el nombre del territorio.
     *
     * @param nombre Nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el identificador del jugador que controla el territorio.
     *
     * @return Identificador del jugador que controla el territorio.
     */
    public Long getIdJugador() {
        return idJugador;
    }

    /**
     * Establece el identificador del jugador que controla el territorio.
     *
     * @param idJugador Identificador del jugador a establecer.
     */
    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    /**
     * Obtiene el número de tropas desplegadas en el territorio.
     *
     * @return Número de tropas en el territorio.
     */
    public int getTropas() {
        return tropas;
    }

    /**
     * Establece el número de tropas desplegadas en el territorio.
     *
     * @param tropas Número de tropas a establecer.
     */
    public void setTropas(int tropas) {
        this.tropas = tropas;
    }

	public Long getIdContinente() {
		return idContinente;
	}

	public void setIdContinente(Long idContinente) {
		this.idContinente = idContinente;
	}
    
}