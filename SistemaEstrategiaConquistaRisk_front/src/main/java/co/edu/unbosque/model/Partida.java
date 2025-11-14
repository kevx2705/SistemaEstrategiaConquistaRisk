package co.edu.unbosque.model;

import java.time.LocalDateTime;

import co.edu.unbosque.estructures.MyLinkedList;
import jakarta.persistence.Column;

public class Partida {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String codigo;
	private Long anfitrionId;
	private MyLinkedList<Jugador> jugadores;
	private String estado;
	private String fase;
	private Long jugadorActualId;
	private int turno;
	private int totalTropasColocadas;
	private String codigoHash;
	private boolean iniciada;
	/** Estado que indica si la partida ha finalizado. */
	private boolean finalizada;

	/** Identificador del jugador ganador de la partida. */
	private Long ganadorId;

	/** Representación en JSON de los territorios de la partida. */
	@Column(columnDefinition = "LONGTEXT")
	private String territoriosJSON;

	/** Representación en JSON del mazo de cartas de la partida. */
	private String mazoCartasJSON;

	/** Representación en JSON del orden de turno de los jugadores. */
	@Column(columnDefinition = "LONGTEXT")
	private String jugadoresOrdenTurnoJSON;

	/** Fecha y hora de inicio de la partida. */
	private String fechaInicio;

	/** Fecha y hora de finalización de la partida. */
	private String fechaFin;

	// Constructor vacío (requerido por Jackson y JPA)
	public Partida() {
	}

	// Constructor con campos principales
	public Partida(Long id, String codigo, Long anfitrionId, MyLinkedList<Jugador> jugadores, String estado,
			String fase, Long jugadorActualId, int turno) {
		this.id = id;
		this.codigo = codigo;
		this.anfitrionId = anfitrionId;
		this.jugadores = jugadores;
		this.estado = estado;
		this.fase = fase;
		this.jugadorActualId = jugadorActualId;
		this.turno = turno;
	}

	public Partida(Long id, String codigo, Long anfitrionId, MyLinkedList<Jugador> jugadores, String estado,
			String fase, Long jugadorActualId, int turno, int totalTropasColocadas, String codigoHash, boolean iniciada,
			boolean finalizada, Long ganadorId, String territoriosJSON, String mazoCartasJSON,
			String jugadoresOrdenTurnoJSON, String fechaInicio, String fechaFin) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.anfitrionId = anfitrionId;
		this.jugadores = jugadores;
		this.estado = estado;
		this.fase = fase;
		this.jugadorActualId = jugadorActualId;
		this.turno = turno;
		this.totalTropasColocadas = totalTropasColocadas;
		this.codigoHash = codigoHash;
		this.iniciada = iniciada;
		this.finalizada = finalizada;
		this.ganadorId = ganadorId;
		this.territoriosJSON = territoriosJSON;
		this.mazoCartasJSON = mazoCartasJSON;
		this.jugadoresOrdenTurnoJSON = jugadoresOrdenTurnoJSON;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
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

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Long getId() {
		return id;
	}

	public boolean isIniciada() {
		return iniciada;
	}

	public void setIniciada(boolean iniciada) {
		this.iniciada = iniciada;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getCodigoHash() {
		return codigoHash;
	}

	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getAnfitrionId() {
		return anfitrionId;
	}

	public void setAnfitrionId(Long anfitrionId) {
		this.anfitrionId = anfitrionId;
	}

	public MyLinkedList<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(MyLinkedList<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public Long getJugadorActualId() {
		return jugadorActualId;
	}

	public void setJugadorActualId(Long jugadorActualId) {
		this.jugadorActualId = jugadorActualId;
	}

	public int getTurno() {
		return turno;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public int getTotalTropasColocadas() {
		return totalTropasColocadas;
	}

	public void setTotalTropasColocadas(int totalTropasColocadas) {
		this.totalTropasColocadas = totalTropasColocadas;
	}

	// ======================
	// MÉTODO TO_STRING
	// ======================
	@Override
	public String toString() {
		return "PartidaDTO{" + "id=" + id + ", codigo='" + codigo + '\'' + ", anfitrionId=" + anfitrionId + ", estado='"
				+ estado + '\'' + ", fase='" + fase + '\'' + ", turno=" + turno + ", jugadorActualId=" + jugadorActualId
				+ ", totalTropasColocadas=" + totalTropasColocadas + '}';
	}
}
