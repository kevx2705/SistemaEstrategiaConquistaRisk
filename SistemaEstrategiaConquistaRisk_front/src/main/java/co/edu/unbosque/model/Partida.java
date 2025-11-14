package co.edu.unbosque.model;

import co.edu.unbosque.estructures.MyLinkedList;
import jakarta.persistence.Column;

/**
 * Clase que representa una partida en el sistema.
 * Contiene información sobre el estado, los jugadores, los territorios,
 * las cartas y otros detalles relevantes de una partida en curso o finalizada.
 */
public class Partida {

    /**
     * Identificador único de la partida.
     */
    private Long id;

    /**
     * Código único de la partida.
     */
    private String codigo;

    /**
     * Identificador del jugador anfitrión de la partida.
     */
    private Long anfitrionId;

    /**
     * Lista de jugadores que participan en la partida.
     */
    private MyLinkedList<Jugador> jugadores;

    /**
     * Estado actual de la partida.
     */
    private String estado;

    /**
     * Fase actual de la partida.
     */
    private String fase;

    /**
     * Identificador del jugador cuyo turno es actualmente.
     */
    private Long jugadorActualId;

    /**
     * Número del turno actual de la partida.
     */
    private int turno;

    /**
     * Total de tropas colocadas en la partida.
     */
    private int totalTropasColocadas;

    /**
     * Código hash asociado a la partida.
     */
    private String codigoHash;

    /**
     * Indica si la partida ha sido iniciada.
     */
    private boolean iniciada;

    /**
     * Estado que indica si la partida ha finalizado.
     */
    private boolean finalizada;

    /**
     * Identificador del jugador ganador de la partida.
     */
    private Long ganadorId;

    /**
     * Representación en JSON de los territorios de la partida.
     */
    @Column(columnDefinition = "LONGTEXT")
    private String territoriosJSON;

    /**
     * Representación en JSON del mazo de cartas de la partida.
     */
    private String mazoCartasJSON;

    /**
     * Representación en JSON del orden de turno de los jugadores.
     */
    @Column(columnDefinition = "LONGTEXT")
    private String jugadoresOrdenTurnoJSON;

    /**
     * Fecha y hora de inicio de la partida.
     */
    private String fechaInicio;

    /**
     * Fecha y hora de finalización de la partida.
     */
    private String fechaFin;

    /**
     * Constructor por defecto de la clase Partida.
     */
    public Partida() {
    }

    /**
     * Constructor que inicializa una partida con sus atributos básicos.
     *
     * @param id               Identificador único de la partida.
     * @param codigo           Código único de la partida.
     * @param anfitrionId      Identificador del jugador anfitrión.
     * @param jugadores        Lista de jugadores participantes.
     * @param estado           Estado actual de la partida.
     * @param fase             Fase actual de la partida.
     * @param jugadorActualId  Identificador del jugador actual.
     * @param turno            Número del turno actual.
     */
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

    /**
     * Constructor que inicializa una partida con todos sus atributos.
     *
     * @param id                       Identificador único de la partida.
     * @param codigo                   Código único de la partida.
     * @param anfitrionId              Identificador del jugador anfitrión.
     * @param jugadores                Lista de jugadores participantes.
     * @param estado                   Estado actual de la partida.
     * @param fase                     Fase actual de la partida.
     * @param jugadorActualId          Identificador del jugador actual.
     * @param turno                    Número del turno actual.
     * @param totalTropasColocadas     Total de tropas colocadas.
     * @param codigoHash              Código hash de la partida.
     * @param iniciada                 Indica si la partida ha sido iniciada.
     * @param finalizada               Indica si la partida ha finalizado.
     * @param ganadorId                Identificador del jugador ganador.
     * @param territoriosJSON          Representación en JSON de los territorios.
     * @param mazoCartasJSON           Representación en JSON del mazo de cartas.
     * @param jugadoresOrdenTurnoJSON Representación en JSON del orden de turno.
     * @param fechaInicio              Fecha y hora de inicio de la partida.
     * @param fechaFin                 Fecha y hora de finalización de la partida.
     */
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

    /**
     * Verifica si la partida ha finalizado.
     *
     * @return {@code true} si la partida ha finalizado, {@code false} en caso contrario.
     */
    public boolean isFinalizada() {
        return finalizada;
    }

    /**
     * Establece si la partida ha finalizado.
     *
     * @param finalizada Estado de finalización a establecer.
     */
    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    /**
     * Obtiene el identificador del jugador ganador de la partida.
     *
     * @return Identificador del jugador ganador.
     */
    public Long getGanadorId() {
        return ganadorId;
    }

    /**
     * Establece el identificador del jugador ganador de la partida.
     *
     * @param ganadorId Identificador del jugador ganador a establecer.
     */
    public void setGanadorId(Long ganadorId) {
        this.ganadorId = ganadorId;
    }

    /**
     * Obtiene la representación en JSON de los territorios de la partida.
     *
     * @return Representación en JSON de los territorios.
     */
    public String getTerritoriosJSON() {
        return territoriosJSON;
    }

    /**
     * Establece la representación en JSON de los territorios de la partida.
     *
     * @param territoriosJSON Representación en JSON de los territorios a establecer.
     */
    public void setTerritoriosJSON(String territoriosJSON) {
        this.territoriosJSON = territoriosJSON;
    }

    /**
     * Obtiene la representación en JSON del mazo de cartas de la partida.
     *
     * @return Representación en JSON del mazo de cartas.
     */
    public String getMazoCartasJSON() {
        return mazoCartasJSON;
    }

    /**
     * Establece la representación en JSON del mazo de cartas de la partida.
     *
     * @param mazoCartasJSON Representación en JSON del mazo de cartas a establecer.
     */
    public void setMazoCartasJSON(String mazoCartasJSON) {
        this.mazoCartasJSON = mazoCartasJSON;
    }

    /**
     * Obtiene la representación en JSON del orden de turno de los jugadores.
     *
     * @return Representación en JSON del orden de turno.
     */
    public String getJugadoresOrdenTurnoJSON() {
        return jugadoresOrdenTurnoJSON;
    }

    /**
     * Establece la representación en JSON del orden de turno de los jugadores.
     *
     * @param jugadoresOrdenTurnoJSON Representación en JSON del orden de turno a establecer.
     */
    public void setJugadoresOrdenTurnoJSON(String jugadoresOrdenTurnoJSON) {
        this.jugadoresOrdenTurnoJSON = jugadoresOrdenTurnoJSON;
    }

    /**
     * Obtiene la fecha y hora de inicio de la partida.
     *
     * @return Fecha y hora de inicio de la partida.
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha y hora de inicio de la partida.
     *
     * @param fechaInicio Fecha y hora de inicio a establecer.
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha y hora de finalización de la partida.
     *
     * @return Fecha y hora de finalización de la partida.
     */
    public String getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha y hora de finalización de la partida.
     *
     * @param fechaFin Fecha y hora de finalización a establecer.
     */
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene el identificador único de la partida.
     *
     * @return Identificador único de la partida.
     */
    public Long getId() {
        return id;
    }

    /**
     * Verifica si la partida ha sido iniciada.
     *
     * @return {@code true} si la partida ha sido iniciada, {@code false} en caso contrario.
     */
    public boolean isIniciada() {
        return iniciada;
    }

    /**
     * Establece si la partida ha sido iniciada.
     *
     * @param iniciada Estado de inicio a establecer.
     */
    public void setIniciada(boolean iniciada) {
        this.iniciada = iniciada;
    }

    /**
     * Establece el identificador único de la partida.
     *
     * @param id Identificador único a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el código único de la partida.
     *
     * @return Código único de la partida.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el código hash de la partida.
     *
     * @return Código hash de la partida.
     */
    public String getCodigoHash() {
        return codigoHash;
    }

    /**
     * Establece el código hash de la partida.
     *
     * @param codigoHash Código hash a establecer.
     */
    public void setCodigoHash(String codigoHash) {
        this.codigoHash = codigoHash;
    }

    /**
     * Establece el código único de la partida.
     *
     * @param codigo Código único a establecer.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el identificador del jugador anfitrión de la partida.
     *
     * @return Identificador del jugador anfitrión.
     */
    public Long getAnfitrionId() {
        return anfitrionId;
    }

    /**
     * Establece el identificador del jugador anfitrión de la partida.
     *
     * @param anfitrionId Identificador del jugador anfitrión a establecer.
     */
    public void setAnfitrionId(Long anfitrionId) {
        this.anfitrionId = anfitrionId;
    }

    /**
     * Obtiene la lista de jugadores que participan en la partida.
     *
     * @return Lista de jugadores participantes.
     */
    public MyLinkedList<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Establece la lista de jugadores que participan en la partida.
     *
     * @param jugadores Lista de jugadores a establecer.
     */
    public void setJugadores(MyLinkedList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    /**
     * Obtiene el estado actual de la partida.
     *
     * @return Estado actual de la partida.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual de la partida.
     *
     * @param estado Estado a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fase actual de la partida.
     *
     * @return Fase actual de la partida.
     */
    public String getFase() {
        return fase;
    }

    /**
     * Establece la fase actual de la partida.
     *
     * @param fase Fase a establecer.
     */
    public void setFase(String fase) {
        this.fase = fase;
    }

    /**
     * Obtiene el identificador del jugador cuyo turno es actualmente.
     *
     * @return Identificador del jugador actual.
     */
    public Long getJugadorActualId() {
        return jugadorActualId;
    }

    /**
     * Establece el identificador del jugador cuyo turno es actualmente.
     *
     * @param jugadorActualId Identificador del jugador actual a establecer.
     */
    public void setJugadorActualId(Long jugadorActualId) {
        this.jugadorActualId = jugadorActualId;
    }

    /**
     * Obtiene el número del turno actual de la partida.
     *
     * @return Número del turno actual.
     */
    public int getTurno() {
        return turno;
    }

    /**
     * Establece el número del turno actual de la partida.
     *
     * @param turno Número del turno a establecer.
     */
    public void setTurno(int turno) {
        this.turno = turno;
    }

    /**
     * Obtiene el total de tropas colocadas en la partida.
     *
     * @return Total de tropas colocadas.
     */
    public int getTotalTropasColocadas() {
        return totalTropasColocadas;
    }

    /**
     * Establece el total de tropas colocadas en la partida.
     *
     * @param totalTropasColocadas Total de tropas colocadas a establecer.
     */
    public void setTotalTropasColocadas(int totalTropasColocadas) {
        this.totalTropasColocadas = totalTropasColocadas;
    }

    /**
     * Devuelve una representación en cadena de la partida.
     *
     * @return Cadena que representa los atributos principales de la partida.
     */
    @Override
    public String toString() {
        return "PartidaDTO{" + "id=" + id + ", codigo='" + codigo + '\'' + ", anfitrionId=" + anfitrionId + ", estado='"
                + estado + '\'' + ", fase='" + fase + '\'' + ", turno=" + turno + ", jugadorActualId=" + jugadorActualId
                + ", totalTropasColocadas=" + totalTropasColocadas + '}';
    }
}
