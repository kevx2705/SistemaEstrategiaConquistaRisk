package co.edu.unbosque.model.persistence;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase DTO que representa una partida en el juego Risk. Contiene información
 * sobre el estado de la partida, el código hash único, el ganador, el jugador
 * actual, los territorios, las cartas, el orden de turno de los jugadores, y
 * las fechas de inicio y fin de la partida.
 */
public class PartidaDTO {

	/** Identificador único de la partida. */
	private Long id;

	/** Código hash único que identifica la partida. */
	private String codigoHash;

	/** Estado que indica si la partida ha sido iniciada. */
	private boolean iniciada;

	/** Estado que indica si la partida ha finalizado. */
	private boolean finalizada;

	/** Identificador del jugador ganador de la partida. */
	private Long ganadorId;

	/** Identificador del jugador cuyo turno actual es. */
	private Long jugadorActualId;

	/** Representación en JSON de los territorios de la partida. */
	private String territoriosJSON;

	/** Representación en JSON del mazo de cartas de la partida. */
	private String mazoCartasJSON;

	/** Representación en JSON del orden de turno de los jugadores. */
	private String jugadoresOrdenTurnoJSON;

	/** Fecha y hora de inicio de la partida. */
	private LocalDateTime fechaInicio;

	/** Fecha y hora de finalización de la partida. */
	private LocalDateTime fechaFin;

	/**
	 * Constructor por defecto de la clase PartidaDTO.
	 */
	public PartidaDTO() {
		// Constructor vacío
	}

	/**
	 * Constructor parametrizado de la clase PartidaDTO.
	 *
	 * @param codigoHash              Código hash único de la partida.
	 * @param iniciada                Estado que indica si la partida ha sido
	 *                                iniciada.
	 * @param finalizada              Estado que indica si la partida ha finalizado.
	 * @param ganadorId               Identificador del jugador ganador.
	 * @param jugadorActualId         Identificador del jugador actual.
	 * @param territoriosJSON         Representación en JSON de los territorios.
	 * @param mazoCartasJSON          Representación en JSON del mazo de cartas.
	 * @param jugadoresOrdenTurnoJSON Representación en JSON del orden de turno de
	 *                                los jugadores.
	 * @param fechaInicio             Fecha y hora de inicio de la partida.
	 * @param fechaFin                Fecha y hora de finalización de la partida.
	 */
	public PartidaDTO(String codigoHash, boolean iniciada, boolean finalizada, Long ganadorId, Long jugadorActualId,
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

	/**
	 * Obtiene el identificador único de la partida.
	 *
	 * @return El identificador único de la partida.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Establece el identificador único de la partida.
	 *
	 * @param id El identificador único a establecer.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtiene el código hash único de la partida.
	 *
	 * @return El código hash único de la partida.
	 */
	public String getCodigoHash() {
		return codigoHash;
	}

	/**
	 * Establece el código hash único de la partida.
	 *
	 * @param codigoHash El código hash único a establecer.
	 */
	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}

	/**
	 * Verifica si la partida ha sido iniciada.
	 *
	 * @return true si la partida ha sido iniciada, false en caso contrario.
	 */
	public boolean isIniciada() {
		return iniciada;
	}

	/**
	 * Establece el estado de inicio de la partida.
	 *
	 * @param iniciada El estado de inicio a establecer.
	 */
	public void setIniciada(boolean iniciada) {
		this.iniciada = iniciada;
	}

	/**
	 * Verifica si la partida ha finalizado.
	 *
	 * @return true si la partida ha finalizado, false en caso contrario.
	 */
	public boolean isFinalizada() {
		return finalizada;
	}

	/**
	 * Establece el estado de finalización de la partida.
	 *
	 * @param finalizada El estado de finalización a establecer.
	 */
	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}

	/**
	 * Obtiene el identificador del jugador ganador.
	 *
	 * @return El identificador del jugador ganador.
	 */
	public Long getGanadorId() {
		return ganadorId;
	}

	/**
	 * Establece el identificador del jugador ganador.
	 *
	 * @param ganadorId El identificador del jugador ganador a establecer.
	 */
	public void setGanadorId(Long ganadorId) {
		this.ganadorId = ganadorId;
	}

	/**
	 * Obtiene el identificador del jugador actual.
	 *
	 * @return El identificador del jugador actual.
	 */
	public Long getJugadorActualId() {
		return jugadorActualId;
	}

	/**
	 * Establece el identificador del jugador actual.
	 *
	 * @param jugadorActualId El identificador del jugador actual a establecer.
	 */
	public void setJugadorActualId(Long jugadorActualId) {
		this.jugadorActualId = jugadorActualId;
	}

	/**
	 * Obtiene la representación en JSON de los territorios de la partida.
	 *
	 * @return La representación en JSON de los territorios.
	 */
	public String getTerritoriosJSON() {
		return territoriosJSON;
	}

	/**
	 * Establece la representación en JSON de los territorios de la partida.
	 *
	 * @param territoriosJSON La representación en JSON a establecer.
	 */
	public void setTerritoriosJSON(String territoriosJSON) {
		this.territoriosJSON = territoriosJSON;
	}

	/**
	 * Obtiene la representación en JSON del mazo de cartas de la partida.
	 *
	 * @return La representación en JSON del mazo de cartas.
	 */
	public String getMazoCartasJSON() {
		return mazoCartasJSON;
	}

	/**
	 * Establece la representación en JSON del mazo de cartas de la partida.
	 *
	 * @param mazoCartasJSON La representación en JSON a establecer.
	 */
	public void setMazoCartasJSON(String mazoCartasJSON) {
		this.mazoCartasJSON = mazoCartasJSON;
	}

	/**
	 * Obtiene la representación en JSON del orden de turno de los jugadores.
	 *
	 * @return La representación en JSON del orden de turno de los jugadores.
	 */
	public String getJugadoresOrdenTurnoJSON() {
		return jugadoresOrdenTurnoJSON;
	}

	/**
	 * Establece la representación en JSON del orden de turno de los jugadores.
	 *
	 * @param jugadoresOrdenTurnoJSON La representación en JSON a establecer.
	 */
	public void setJugadoresOrdenTurnoJSON(String jugadoresOrdenTurnoJSON) {
		this.jugadoresOrdenTurnoJSON = jugadoresOrdenTurnoJSON;
	}

	/**
	 * Obtiene la fecha y hora de inicio de la partida.
	 *
	 * @return La fecha y hora de inicio de la partida.
	 */
	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * Establece la fecha y hora de inicio de la partida.
	 *
	 * @param fechaInicio La fecha y hora de inicio a establecer.
	 */
	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Obtiene la fecha y hora de finalización de la partida.
	 *
	 * @return La fecha y hora de finalización de la partida.
	 */
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	/**
	 * Establece la fecha y hora de finalización de la partida.
	 *
	 * @param fechaFin La fecha y hora de finalización a establecer.
	 */
	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * Devuelve una representación en cadena de la partida.
	 *
	 * @return Una cadena que representa los atributos de la partida.
	 */
	@Override
	public String toString() {
		return "PartidaDTO [id=" + id + ", codigoHash=" + codigoHash + ", iniciada=" + iniciada + ", finalizada="
				+ finalizada + ", ganadorId=" + ganadorId + ", jugadorActualId=" + jugadorActualId
				+ ", territoriosJSON=" + territoriosJSON + ", mazoCartasJSON=" + mazoCartasJSON
				+ ", jugadoresOrdenTurnoJSON=" + jugadoresOrdenTurnoJSON + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + "]";
	}

	/**
	 * Genera un código hash para la partida basado en sus atributos.
	 *
	 * @return El código hash generado.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(codigoHash, fechaFin, fechaInicio, finalizada, ganadorId, id, iniciada, jugadorActualId,
				jugadoresOrdenTurnoJSON, mazoCartasJSON, territoriosJSON);
	}

	/**
	 * Compara esta partida con otro objeto para determinar si son iguales.
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
		PartidaDTO other = (PartidaDTO) obj;
		return Objects.equals(codigoHash, other.codigoHash) && Objects.equals(fechaFin, other.fechaFin)
				&& Objects.equals(fechaInicio, other.fechaInicio) && finalizada == other.finalizada
				&& Objects.equals(ganadorId, other.ganadorId) && Objects.equals(id, other.id)
				&& iniciada == other.iniciada && Objects.equals(jugadorActualId, other.jugadorActualId)
				&& Objects.equals(jugadoresOrdenTurnoJSON, other.jugadoresOrdenTurnoJSON)
				&& Objects.equals(mazoCartasJSON, other.mazoCartasJSON)
				&& Objects.equals(territoriosJSON, other.territoriosJSON);
	}
}
