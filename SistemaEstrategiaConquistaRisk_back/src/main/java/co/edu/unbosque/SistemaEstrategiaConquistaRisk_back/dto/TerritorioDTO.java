package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;

public class TerritorioDTO {
	
	private Long id;

	private String nombre;

	private int tropas;
	private Long idContinente; // referencia numérica, sin relación JPA
	private Long idJugador; // referencia del propietario
	
	public TerritorioDTO() {
		// TODO Auto-generated constructor stub
	}

	public TerritorioDTO(String nombre, int tropas, Long idContinente, Long idJugador) {
		super();
		this.nombre = nombre;
		this.tropas = tropas;
		this.idContinente = idContinente;
		this.idJugador = idJugador;
	}

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

	public int getTropas() {
		return tropas;
	}

	public void setTropas(int tropas) {
		this.tropas = tropas;
	}

	public Long getIdContinente() {
		return idContinente;
	}

	public void setIdContinente(Long idContinente) {
		this.idContinente = idContinente;
	}

	public Long getIdJugador() {
		return idJugador;
	}

	public void setIdJugador(Long idJugador) {
		this.idJugador = idJugador;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idContinente, idJugador, nombre, tropas);
	}

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

	@Override
	public String toString() {
		return "TerritorioDTO [id=" + id + ", nombre=" + nombre + ", tropas=" + tropas + ", idContinente="
				+ idContinente + ", idJugador=" + idJugador + "]";
	}
	
	
}
