package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "partida")
public class Partida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String codigoHash;

	private boolean iniciada;
	private boolean finalizada;

	private Long ganadorId;
	private Long jugadorActualId;

	@Column(columnDefinition = "LONGTEXT")
	private String territoriosJSON;

	@Column(columnDefinition = "LONGTEXT")
	private String mazoCartasJSON;

	@Column(columnDefinition = "LONGTEXT")
	private String jugadoresOrdenTurnoJSON;

	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;

	public Partida() {
	}

	public Partida(String codigoHash, boolean iniciada, boolean finalizada, Long ganadorId, Long jugadorActualId,
			String territoriosJSON, String mazoCartasJSON, String jugadoresOrdenTurnoJSON, LocalDateTime fechaInicio,
			LocalDateTime fechaFin) {
		super();
		this.codigoHash = codigoHash;
		this.iniciada = iniciada;
		this.finalizada = finalizada;
		this.ganadorId = ganadorId;
		this.jugadorActualId = jugadorActualId;
		this.territoriosJSON = territoriosJSON;
		this.mazoCartasJSON = mazoCartasJSON;
		this.jugadoresOrdenTurnoJSON = jugadoresOrdenTurnoJSON;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoHash() {
		return codigoHash;
	}

	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}

	public boolean isIniciada() {
		return iniciada;
	}

	public void setIniciada(boolean iniciada) {
		this.iniciada = iniciada;
	}

	public boolean isFinalizada() {
		return finalizada;
	}

	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}

	public Long getGanadorId() {
		return ganadorId;
	}

	public void setGanadorId(Long ganadorId) {
		this.ganadorId = ganadorId;
	}

	public Long getJugadorActualId() {
		return jugadorActualId;
	}

	public void setJugadorActualId(Long jugadorActualId) {
		this.jugadorActualId = jugadorActualId;
	}

	public String getTerritoriosJSON() {
		return territoriosJSON;
	}

	public void setTerritoriosJSON(String territoriosJSON) {
		this.territoriosJSON = territoriosJSON;
	}

	public String getMazoCartasJSON() {
		return mazoCartasJSON;
	}

	public void setMazoCartasJSON(String mazoCartasJSON) {
		this.mazoCartasJSON = mazoCartasJSON;
	}

	public String getJugadoresOrdenTurnoJSON() {
		return jugadoresOrdenTurnoJSON;
	}

	public void setJugadoresOrdenTurnoJSON(String jugadoresOrdenTurnoJSON) {
		this.jugadoresOrdenTurnoJSON = jugadoresOrdenTurnoJSON;
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

	@Override
	public String toString() {
		return "Partida [id=" + id + ", codigoHash=" + codigoHash + ", iniciada=" + iniciada + ", finalizada="
				+ finalizada + ", ganadorId=" + ganadorId + ", jugadorActualId=" + jugadorActualId
				+ ", territoriosJSON=" + territoriosJSON + ", mazoCartasJSON=" + mazoCartasJSON
				+ ", jugadoresOrdenTurnoJSON=" + jugadoresOrdenTurnoJSON + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoHash, fechaFin, fechaInicio, finalizada, ganadorId, id, iniciada, jugadorActualId,
				jugadoresOrdenTurnoJSON, mazoCartasJSON, territoriosJSON);
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
		return Objects.equals(codigoHash, other.codigoHash) && Objects.equals(fechaFin, other.fechaFin)
				&& Objects.equals(fechaInicio, other.fechaInicio) && finalizada == other.finalizada
				&& Objects.equals(ganadorId, other.ganadorId) && Objects.equals(id, other.id)
				&& iniciada == other.iniciada && Objects.equals(jugadorActualId, other.jugadorActualId)
				&& Objects.equals(jugadoresOrdenTurnoJSON, other.jugadoresOrdenTurnoJSON)
				&& Objects.equals(mazoCartasJSON, other.mazoCartasJSON)
				&& Objects.equals(territoriosJSON, other.territoriosJSON);
	}

}
