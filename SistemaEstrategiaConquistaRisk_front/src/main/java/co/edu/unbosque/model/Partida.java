package co.edu.unbosque.model;

import co.edu.unbosque.estructures.MyLinkedList;

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

    // Constructor vacío (requerido por Jackson y JPA)
    public Partida() {}

    // Constructor con campos principales
    public Partida(Long id, String codigo, Long anfitrionId, MyLinkedList<Jugador> jugadores, 
                      String estado, String fase, Long jugadorActualId, int turno) {
        this.id = id;
        this.codigo = codigo;
        this.anfitrionId = anfitrionId;
        this.jugadores = jugadores;
        this.estado = estado;
        this.fase = fase;
        this.jugadorActualId = jugadorActualId;
        this.turno = turno;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
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
        return "PartidaDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", anfitrionId=" + anfitrionId +
                ", estado='" + estado + '\'' +
                ", fase='" + fase + '\'' +
                ", turno=" + turno +
                ", jugadorActualId=" + jugadorActualId +
                ", totalTropasColocadas=" + totalTropasColocadas +
                '}';
    }
}
