package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.estructures.DNode;
import co.edu.unbosque.estructures.MyDequeList;

@Named("gameBean")
@SessionScoped
public class GameBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int numJugadores;
	private MyDequeList<Jugador> jugadores;
	private Jugador jugadorActual;

	private String fromTerritory;
	private String toTerritory;
	private int tropasParaAtacar;

	private String fromMoveTerritory;
	private String toMoveTerritory;
	private int tropasAMover;

	private String territorioAReforzar;
	private int tropasAReforzar;

	private List<String> attackerDice;
	private List<String> defenderDice;

	private List<String> territoriosAliados;
	private List<String> territoriosEnemigos;
	private List<String> territoriosConectados;

	private List<String> players;
	private int currentPlayerIndex;

	private String currentPlayer;
	private int territories;
	private int troops;

	@PostConstruct
	public void init() {
		numJugadores = 2;
		generarJugadores();

		jugadorActual = jugadores.getHead() != null ? jugadores.getHead().getInfo() : null;

		currentPlayerIndex = 0;
		currentPlayer = jugadorActual != null ? jugadorActual.getNombre() : "Jugador 1";
		territories = 5;
		troops = 10;

		territoriosAliados = new ArrayList<>(Arrays.asList("Alaska", "Alberta", "Ontario"));
		territoriosEnemigos = new ArrayList<>(Arrays.asList("Brasil", "Perú", "Argentina"));
		territoriosConectados = new ArrayList<>(Arrays.asList("Alberta", "Ontario"));

		attackerDice = new ArrayList<>();
		defenderDice = new ArrayList<>();

		players = new ArrayList<>();
		for (int i = 1; i <= numJugadores; i++) {
			players.add("Jugador " + i);
		}
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
			currentPlayer = jugadorActual.getNombre();
			currentPlayerIndex = (currentPlayerIndex + 1) % numJugadores;
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

	public void confirmarAtaque() {
		System.out.println(
				"Atacando desde " + fromTerritory + " hacia " + toTerritory + " con " + tropasParaAtacar + " tropas.");
		attackerDice = Arrays.asList("⚀", "⚂", "⚅");
		defenderDice = Arrays.asList("⚄", "⚂");
	}

	public void confirmarMovimiento() {
		System.out.println("Moviendo tropas de " + fromMoveTerritory + " a " + toMoveTerritory + ": " + tropasAMover);
	}

	public void confirmarRefuerzo() {
		System.out.println("Reforzando " + territorioAReforzar + " con " + tropasAReforzar + " tropas.");
	}

	public void rollDice() {
		attackerDice = Arrays.asList("⚅", "⚄", "⚂");
		defenderDice = Arrays.asList("⚃", "⚁");
	}

	public void nextTurn() {
		siguienteTurno();
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
	}

	public Jugador getJugadorActual() {
		return jugadorActual;
	}

	public String getFromTerritory() {
		return fromTerritory;
	}

	public void setFromTerritory(String fromTerritory) {
		this.fromTerritory = fromTerritory;
	}

	public String getToTerritory() {
		return toTerritory;
	}

	public void setToTerritory(String toTerritory) {
		this.toTerritory = toTerritory;
	}

	public int getTropasParaAtacar() {
		return tropasParaAtacar;
	}

	public void setTropasParaAtacar(int tropasParaAtacar) {
		this.tropasParaAtacar = tropasParaAtacar;
	}

	public String getFromMoveTerritory() {
		return fromMoveTerritory;
	}

	public void setFromMoveTerritory(String fromMoveTerritory) {
		this.fromMoveTerritory = fromMoveTerritory;
	}

	public String getToMoveTerritory() {
		return toMoveTerritory;
	}

	public void setToMoveTerritory(String toMoveTerritory) {
		this.toMoveTerritory = toMoveTerritory;
	}

	public int getTropasAMover() {
		return tropasAMover;
	}

	public void setTropasAMover(int tropasAMover) {
		this.tropasAMover = tropasAMover;
	}

	public String getTerritorioAReforzar() {
		return territorioAReforzar;
	}

	public void setTerritorioAReforzar(String territorioAReforzar) {
		this.territorioAReforzar = territorioAReforzar;
	}

	public int getTropasAReforzar() {
		return tropasAReforzar;
	}

	public void setTropasAReforzar(int tropasAReforzar) {
		this.tropasAReforzar = tropasAReforzar;
	}

	public List<String> getAttackerDice() {
		return attackerDice;
	}

	public void setAttackerDice(List<String> attackerDice) {
		this.attackerDice = attackerDice;
	}

	public List<String> getDefenderDice() {
		return defenderDice;
	}

	public void setDefenderDice(List<String> defenderDice) {
		this.defenderDice = defenderDice;
	}

	public List<String> getTerritoriosAliados() {
		return territoriosAliados;
	}

	public void setTerritoriosAliados(List<String> territoriosAliados) {
		this.territoriosAliados = territoriosAliados;
	}

	public List<String> getTerritoriosEnemigos() {
		return territoriosEnemigos;
	}

	public void setTerritoriosEnemigos(List<String> territoriosEnemigos) {
		this.territoriosEnemigos = territoriosEnemigos;
	}

	public List<String> getTerritoriosConectados() {
		return territoriosConectados;
	}

	public void setTerritoriosConectados(List<String> territoriosConectados) {
		this.territoriosConectados = territoriosConectados;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getTerritories() {
		return territories;
	}

	public void setTerritories(int territories) {
		this.territories = territories;
	}

	public int getTroops() {
		return troops;
	}

	public void setTroops(int troops) {
		this.troops = troops;
	}

	public int getTerritoriosPorJugador(String player) {
		return 5;
	}

	public int getTropasPorJugador(String player) {
		return 10;
	}
}
