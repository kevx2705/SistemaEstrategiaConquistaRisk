package co.edu.unbosque.model;

public class Territorio {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private Long idJugador;
	private int tropas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getIdJugador() {
		return idJugador;
	}

	public void setIdJugador(Long idJugador) {
		this.idJugador = idJugador;
	}

	public int getTropas() {
		return tropas;
	}

	public void setTropas(int tropas) {
		this.tropas = tropas;
	}
}
