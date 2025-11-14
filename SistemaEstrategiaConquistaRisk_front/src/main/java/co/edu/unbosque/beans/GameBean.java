package co.edu.unbosque.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.estructures.DNode;
import co.edu.unbosque.estructures.MyDequeList;
import co.edu.unbosque.estructures.MyLinkedList;

/**
 * Bean principal que controla la lógica del juego tipo Risk.
 * Maneja jugadores, turnos, territorios y acciones como ataque,
 * movimiento, refuerzo y asignación de tropas.
 *
 * Este bean se mantiene vivo durante toda la sesión del usuario
 * debido a la anotación {@link SessionScoped}.
 */
@Named("gameBean")
@SessionScoped
public class GameBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Número total de jugadores */
    private int numJugadores;

    /** Lista doblemente enlazada que maneja el orden de los jugadores */
    private MyDequeList<Jugador> jugadores;

    /** Jugador cuyo turno está activo */
    private Jugador jugadorActual;

    /** Territorio desde donde se ataca */
    private String fromTerritory;

    /** Territorio que recibe el ataque */
    private String toTerritory;

    /** Número de tropas usadas en el ataque */
    private int tropasParaAtacar;

    /** Territorio desde donde se moverán tropas */
    private String fromMoveTerritory;

    /** Territorio hacia donde se desplazarán tropas */
    private String toMoveTerritory;

    /** Número de tropas que se moverán */
    private int tropasAMover;

    /** Territorio seleccionado para refuerzo */
    private String territorioAReforzar;

    /** Cantidad de tropas a reforzar */
    private int tropasAReforzar;

    /** Lista de símbolos de dados del atacante */
    private MyLinkedList<String> attackerDice;

    /** Lista de símbolos de dados del defensor */
    private MyLinkedList<String> defenderDice;

    /** Territorios aliados del jugador */
    private MyLinkedList<String> territoriosAliados;

    /** Territorios enemigos */
    private MyLinkedList<String> territoriosEnemigos;

    /** Territorios conectados entre sí */
    private MyLinkedList<String> territoriosConectados;

    /** Lista de nombres de jugadores */
    private MyLinkedList<String> players;

    /** Índice del jugador actual dentro de {@link #players} */
    private int currentPlayerIndex;

    /** Nombre del jugador en turno */
    private String currentPlayer;

    /** Número de territorios del jugador */
    private int territories;

    /** Número total de tropas disponibles del jugador */
    private int troops;

    /** constructor vacio */
    public GameBean() {
		// TODO Auto-generated constructor stub
	}
    
    /**
     * Inicializador ejecutado al cargar el bean.
     * Configura jugadores iniciales, estado del turno,
     * listas de territorios y dados.
     */
    @PostConstruct
    public void init() {

        numJugadores = 2;
        generarJugadores();

        jugadorActual = jugadores.getHead() != null ? jugadores.getHead().getInfo() : null;

        currentPlayerIndex = 0;
        currentPlayer = jugadorActual != null ? jugadorActual.getNombre() : "Jugador 1";
        territories = 5;
        troops = 10;

        territoriosAliados = new MyLinkedList<>();
        territoriosAliados.add("Alaska");
        territoriosAliados.add("Alberta");
        territoriosAliados.add("Ontario");

        territoriosEnemigos = new MyLinkedList<>();
        territoriosEnemigos.add("Brasil");
        territoriosEnemigos.add("Perú");
        territoriosEnemigos.add("Argentina");

        territoriosConectados = new MyLinkedList<>();
        territoriosConectados.add("Alberta");
        territoriosConectados.add("Ontario");

        attackerDice = new MyLinkedList<>();
        defenderDice = new MyLinkedList<>();

        players = new MyLinkedList<>();
        for (int i = 1; i <= numJugadores; i++) {
            players.add("Jugador " + i);
        }
    }

    /**
     * Genera la lista inicial de jugadores según {@link #numJugadores}.
     * También configura el primer jugador como el jugador actual.
     */
    public void generarJugadores() {
        jugadores = new MyDequeList<>();
        for (int i = 1; i <= numJugadores; i++) {
            Jugador jugador = new Jugador();
            jugador.setNombre("Jugador " + i);
            jugadores.insertLast(jugador);
        }
        jugadorActual = jugadores.getHead() != null ? jugadores.getHead().getInfo() : null;
    }

    /**
     * Cambia el turno al siguiente jugador utilizando la estructura
     * circular de {@link MyDequeList}.
     */
    public void siguienteTurno() {
        if (jugadores.size() > 0) {
            Jugador primero = jugadores.removeFirst();
            jugadores.insertLast(primero);
            jugadorActual = jugadores.getHead().getInfo();
            currentPlayer = jugadorActual.getNombre();
            currentPlayerIndex = (currentPlayerIndex + 1) % numJugadores;
        }
    }

    /**
     * Valida los nombres de los jugadores e inicia la partida,
     * redirigiendo al tablero.
     *
     * @return Nombre del archivo XHTML al que se redirige.
     */
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

    /**
     * Simula un ataque generando valores fijos para los dados
     * de atacante y defensor.
     */
    public void confirmarAtaque() {
        attackerDice = new MyLinkedList<>();
        attackerDice.add("⚀");
        attackerDice.add("⚂");
        attackerDice.add("⚅");

        defenderDice = new MyLinkedList<>();
        defenderDice.add("⚄");
        defenderDice.add("⚂");
    }

    /**
     * Imprime información de movimiento de tropas para depuración.
     */
    public void confirmarMovimiento() {
        System.out.println("Moviendo tropas de " + fromMoveTerritory + " a " + toMoveTerritory + ": " + tropasAMover);
    }

    /**
     * Imprime en consola información del refuerzo realizado.
     */
    public void confirmarRefuerzo() {
        System.out.println("Reforzando " + territorioAReforzar + " con " + tropasAReforzar + " tropas.");
    }

    /**
     * Simula una tirada de dados generando resultados predeterminados.
     */
    public void rollDice() {
        attackerDice = new MyLinkedList<>();
        attackerDice.add("⚅");
        attackerDice.add("⚄");
        attackerDice.add("⚂");

        defenderDice = new MyLinkedList<>();
        defenderDice.add("⚃");
        defenderDice.add("⚁");
    }

    /**
     * Avanza al siguiente turno en el orden de jugadores.
     */
    public void nextTurn() {
        siguienteTurno();
    }

    /**
     * Obtiene el número total de jugadores configurados.
     * @return número total de jugadores
     */
    public int getNumJugadores() {
        return numJugadores;
    }

    /**
     * Establece el número de jugadores y regenera la lista interna.
     * @param numJugadores cantidad de jugadores
     */
    public void setNumJugadores(int numJugadores) {
        this.numJugadores = numJugadores;
        generarJugadores();
    }

    /**
     * Retorna la lista de jugadores almacenados en la estructura MyDequeList.
     * @return lista de jugadores
     */
    public MyDequeList<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Devuelve el jugador cuyo turno está activo actualmente.
     * @return jugador actual
     */
    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    /**
     * Obtiene el territorio origen del ataque.
     * @return territorio origen del ataque
     */
    public String getFromTerritory() {
        return fromTerritory;
    }

    /**
     * Establece el territorio desde donde se realizará el ataque.
     * @param fromTerritory territorio atacante
     */
    public void setFromTerritory(String fromTerritory) {
        this.fromTerritory = fromTerritory;
    }

    /**
     * Obtiene el territorio objetivo del ataque.
     * @return territorio objetivo del ataque
     */
    public String getToTerritory() {
        return toTerritory;
    }

    /**
     * Define el territorio al que se dirige el ataque.
     * @param toTerritory territorio que será atacado
     */
    public void setToTerritory(String toTerritory) {
        this.toTerritory = toTerritory;
    }

    /**
     * Devuelve la cantidad de tropas asignadas al ataque.
     * @return tropas usadas para atacar
     */
    public int getTropasParaAtacar() {
        return tropasParaAtacar;
    }

    /**
     * Establece la cantidad de tropas a usar en el ataque.
     * @param tropasParaAtacar tropas asignadas al ataque
     */
    public void setTropasParaAtacar(int tropasParaAtacar) {
        this.tropasParaAtacar = tropasParaAtacar;
    }

    /**
     * Obtiene el territorio desde donde se moverán tropas.
     * @return territorio origen del movimiento
     */
    public String getFromMoveTerritory() {
        return fromMoveTerritory;
    }

    /**
     * Define el territorio origen del movimiento de tropas.
     * @param fromMoveTerritory territorio origen
     */
    public void setFromMoveTerritory(String fromMoveTerritory) {
        this.fromMoveTerritory = fromMoveTerritory;
    }

    /**
     * Devuelve el territorio destino del movimiento.
     * @return territorio destino del movimiento
     */
    public String getToMoveTerritory() {
        return toMoveTerritory;
    }

    /**
     * Establece el territorio al que se moverán tropas.
     * @param toMoveTerritory territorio destino
     */
    public void setToMoveTerritory(String toMoveTerritory) {
        this.toMoveTerritory = toMoveTerritory;
    }

    /**
     * Obtiene la cantidad de tropas a mover entre territorios.
     * @return cantidad de tropas a mover
     */
    public int getTropasAMover() {
        return tropasAMover;
    }

    /**
     * Define la cantidad de tropas que serán movidas.
     * @param tropasAMover número de tropas a desplazar
     */
    public void setTropasAMover(int tropasAMover) {
        this.tropasAMover = tropasAMover;
    }

    /**
     * Obtiene el territorio que será reforzado.
     * @return territorio a reforzar
     */
    public String getTerritorioAReforzar() {
        return territorioAReforzar;
    }

    /**
     * Establece el territorio que recibirá refuerzos.
     * @param territorioAReforzar territorio destino de refuerzos
     */
    public void setTerritorioAReforzar(String territorioAReforzar) {
        this.territorioAReforzar = territorioAReforzar;
    }

    /**
     * Obtiene la cantidad de tropas asignadas como refuerzo.
     * @return número de tropas a reforzar
     */
    public int getTropasAReforzar() {
        return tropasAReforzar;
    }

    /**
     * Define la cantidad de tropas que se agregarán al refuerzo.
     * @param tropasAReforzar tropas asignadas al refuerzo
     */
    public void setTropasAReforzar(int tropasAReforzar) {
        this.tropasAReforzar = tropasAReforzar;
    }

    /**
     * Devuelve la lista de dados correspondientes al atacante.
     * @return lista de dados del atacante
     */
    public MyLinkedList<String> getAttackerDice() {
        return attackerDice;
    }

    /**
     * Devuelve la lista de dados correspondientes al defensor.
     * @return lista de dados del defensor
     */
    public MyLinkedList<String> getDefenderDice() {
        return defenderDice;
    }

    /**
     * Retorna la lista de territorios controlados por el jugador.
     * @return territorios aliados
     */
    public MyLinkedList<String> getTerritoriosAliados() {
        return territoriosAliados;
    }

    /**
     * Devuelve los territorios enemigos visibles en la interfaz.
     * @return territorios enemigos
     */
    public MyLinkedList<String> getTerritoriosEnemigos() {
        return territoriosEnemigos;
    }

    /**
     * Obtiene los territorios conectados válidos para movimiento.
     * @return territorios conectados
     */
    public MyLinkedList<String> getTerritoriosConectados() {
        return territoriosConectados;
    }

    /**
     * Devuelve una lista de nombres de jugadores.
     * @return lista de jugadores por nombre
     */
    public MyLinkedList<String> getPlayers() {
        return players;
    }

    /**
     * Obtiene el índice del jugador actualmente en turno.
     * @return índice del jugador actual
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Devuelve el nombre del jugador cuyo turno está activo.
     * @return nombre del jugador actual
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Obtiene la cantidad de territorios que controla el jugador actual.
     * @return número de territorios controlados
     */
    public int getTerritories() {
        return territories;
    }

    /**
     * Devuelve el número de tropas disponibles para el jugador.
     * @return cantidad de tropas disponibles
     */
    public int getTroops() {
        return troops;
    }

    /**
     * Retorna un valor provisional de territorios por jugador.
     * @param player jugador consultado
     * @return territorios asignados
     */
    public int getTerritoriosPorJugador(String player) {
        return 5;
    }

    /**
     * Retorna un valor provisional de tropas por jugador.
     * @param player jugador consultado
     * @return cantidad de tropas
     */
    public int getTropasPorJugador(String player) {
        return 10;
    }


}
