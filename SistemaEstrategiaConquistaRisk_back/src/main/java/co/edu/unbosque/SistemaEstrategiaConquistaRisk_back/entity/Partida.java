package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "partida")
public class Partida {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String nombre;

	private LocalDateTime fechaInicio;

	private LocalDateTime fechaFin;

	private String estado; // En curso, Finalizada, etc.

	private int turnoActual; // índice del jugador actual

	public Partida() {
		this.fechaInicio = LocalDateTime.now();
		this.estado = "EN_CURSO";
		this.turnoActual = 0;
	}

	public Partida(String nombre) {
		this();
		this.nombre = nombre;
	}

	// Getters y setters
	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getTurnoActual() {
		return turnoActual;
	}

	public void setTurnoActual(int turnoActual) {
		this.turnoActual = turnoActual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(estado, fechaFin, fechaInicio, id, nombre, turnoActual);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partida other = (Partida) obj;
		return Objects.equals(estado, other.estado) && Objects.equals(fechaFin, other.fechaFin)
				&& Objects.equals(fechaInicio, other.fechaInicio) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && turnoActual == other.turnoActual;
	}

	@Override
	public String toString() {
		return "Partida [id=" + id + ", nombre=" + nombre + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
				+ ", estado=" + estado + ", turnoActual=" + turnoActual + "]";
	}

}
