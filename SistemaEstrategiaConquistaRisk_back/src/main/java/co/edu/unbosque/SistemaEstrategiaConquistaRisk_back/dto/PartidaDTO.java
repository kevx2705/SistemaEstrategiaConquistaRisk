package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class PartidaDTO {

	private Long id;
	private String nombre;

	private LocalDateTime fechaInicio;

	private LocalDateTime fechaFin;

	private String estado; // En curso, Finalizada, etc.

	private int turnoActual; // índice del jugador actual

	public PartidaDTO() {
		// TODO Auto-generated constructor stub
	}

	public PartidaDTO(String nombre, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado,
			int turnoActual) {
		super();
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
		this.turnoActual = turnoActual;
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
		PartidaDTO other = (PartidaDTO) obj;
		return Objects.equals(estado, other.estado) && Objects.equals(fechaFin, other.fechaFin)
				&& Objects.equals(fechaInicio, other.fechaInicio) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && turnoActual == other.turnoActual;
	}

	@Override
	public String toString() {
		return "PartidaDTO [id=" + id + ", nombre=" + nombre + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
				+ ", estado=" + estado + ", turnoActual=" + turnoActual + "]";
	}

}
