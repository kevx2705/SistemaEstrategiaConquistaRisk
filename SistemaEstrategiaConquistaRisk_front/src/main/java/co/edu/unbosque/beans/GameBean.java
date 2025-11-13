package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.estructures.DNode;
import co.edu.unbosque.estructures.MyDequeList;

@Named("gameBean")
@SessionScoped
public class GameBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numJugadores;
    private MyDequeList<Jugador> jugadores;
    private Jugador jugadorActual;

    @PostConstruct
    public void init() {
        numJugadores = 2; 
        generarJugadores();
        jugadorActual = jugadores.getHead() != null ? jugadores.getHead().getInfo() : null;
    }

    public void generarJugadores() {
        jugadores = new MyDequeList<>();
        for (int i = 1; i <= numJugadores; i++) {
            Jugador jugador = new Jugador();
            jugador.setNombre("Jugador " + i);
            jugadores.insertLast(jugador);
        }
        jugadorActual = jugadores.getHead() != null ? jugadores.getHead().getInfo() : null;
    }

    public void siguienteTurno() {
        if (jugadores.size() > 0) {
            Jugador primero = jugadores.removeFirst();
            jugadores.insertLast(primero);
            jugadorActual = jugadores.getHead().getInfo();
        }
    }

    public String iniciarPartida() {
        DNode<Jugador> nodo = jugadores.getHead();
        while (nodo != null) {
            Jugador j = nodo.getInfo();
            if (j.getNombre() == null || j.getNombre().trim().isEmpty()) {
                return ""; 
            }
            nodo = nodo.getNext();
        }
        return "tablero.xhtml?faces-redirect=true";
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public void setNumJugadores(int numJugadores) {
        this.numJugadores = numJugadores;
        generarJugadores();
    }

    public MyDequeList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(MyDequeList<Jugador> jugadores) {
        this.jugadores = jugadores;
        jugadorActual = jugadores.getHead() != null ? jugadores.getHead().getInfo() : null;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }
}
