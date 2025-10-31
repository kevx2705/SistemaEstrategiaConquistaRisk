package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "territorio")
public class Territorio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private int tropas;
	private Long idContinente; // referencia numérica, sin relación JPA
	private Long idJugador = 0L; // referencia del propietario

	public Territorio() {
		// TODO Auto-generated constructor stub
	}

	public Territorio(String nombre, Long idContinente, int tropas) {
		this.nombre = nombre;
		this.idContinente = idContinente;
		this.tropas = tropas;
	}

	public String getNombre() {
		return nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		Territorio other = (Territorio) obj;
		return Objects.equals(id, other.id) && Objects.equals(idContinente, other.idContinente)
				&& Objects.equals(idJugador, other.idJugador) && Objects.equals(nombre, other.nombre)
				&& tropas == other.tropas;
	}

	@Override
	public String toString() {
		return "Territorio [id=" + id + ", nombre=" + nombre + ", tropas=" + tropas + ", idContinente=" + idContinente
				+ ", idJugador=" + idJugador + "]";
	}

}
