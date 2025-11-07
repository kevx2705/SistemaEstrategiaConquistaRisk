package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.CartaDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ResultadoAtaqueDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.JsonUtil;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyDoubleLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.JugadorRepository;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.PartidaRepository;

@Service
public class PartidaService {
	@Autowired
	private PartidaRepository partidaRepository;
	@Autowired
	private JugadorRepository jugadorRepository;
	@Autowired
	private JugadorService jugadorService;
	@Autowired
	private ContinenteService continenteService;
	@Autowired
	private TerritorioService territorioService;

	@Autowired
	private CartaService cartaService;

	@Autowired
	private AtaqueService ataqueService;

	@Autowired
	private ModelMapper modelMapper;

	private boolean conquistoTerritorio;

	private final Gson gson = new Gson();

	public PartidaService() {
		// TODO Auto-generated constructor stub
	}

	public Partida crearPartida(Long anfitrionId, String[] otrosNombres) {

		// ------------------------------------------
		// 0. Validar anfitrión no esté en otra partida
		// ------------------------------------------
		Jugador anfitrion = jugadorRepository.findById(anfitrionId)
				.orElseThrow(() -> new RuntimeException("No existe el jugador anfitrión"));

		if (anfitrion.isActivo()) {
			throw new RuntimeException("El anfitrión ya está participando en otra partida.");
		}

		// ---------------------------
		// 1. Crear la partida
		// ---------------------------
		Partida partida = new Partida();
		partida.setIniciada(true);
		partida.setFinalizada(false);
		partida.setFechaInicio(LocalDateTime.now());
		partida.setCodigoHash(UUID.randomUUID().toString());

		// -------------------------------------
		// 2. Crear MyLinkedList de jugadores
		// -------------------------------------
		MyLinkedList<Long> ordenJugadores = new MyLinkedList<>();

		// -------------------------------------
		// 3. Agregar anfitrión
		// -------------------------------------

		anfitrion.setColor("ROJO");
		jugadorService.activarJugador(anfitrion.getId());

		ordenJugadores.addLast(anfitrion.getId());

		// -------------------------------------
		// 4. Colores disponibles
		// -------------------------------------
		String[] colores = { "ROJO", "AZUL", "VERDE", "AMARILLO", "NEGRO", "BLANCO" };

		if (otrosNombres.length + 1 > colores.length) {
			throw new RuntimeException("Hay más jugadores que colores disponibles.");
		}

		int colorIndex = 1; // anfitrión usó el ROJO

		// -------------------------------------
		// 5. Crear jugadores temporales
		// -------------------------------------
		for (String nombre : otrosNombres) {

			// Validación importante:
			// un jugador temporal NO puede estar activo ya.
			Jugador existente = jugadorRepository.findByNombre(nombre);
			if (existente != null && existente.isActivo()) {
				throw new RuntimeException("El jugador '" + nombre + "' ya está en otra partida.");
			}

			Jugador nuevo = jugadorService.crearJugadorTemporal(nombre, colores[colorIndex++]);

			ordenJugadores.addLast(nuevo.getId());
		}

		// -------------------------------------
		// 6. Asignar el turno inicial
		// -------------------------------------
		partida.setJugadorActualId(ordenJugadores.getPos(0).getInfo());

		// -------------------------------------
		// 7. Serializar jugadoresOrdenTurnoJSON
		// -------------------------------------
		String json = JsonUtil.toJson(ordenJugadores);
		partida.setJugadoresOrdenTurnoJSON(json);

		// -------------------------------------
		// 8. Guardar partida
		// -------------------------------------
		return partidaRepository.save(partida);
	}

	@Transactional
	public void inicializarJuego(Long partidaId) {

		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);
		int numJugadores = ordenJugadores.size();

		// 1️⃣ Asignar tropas iniciales según número de jugadores
		int tropasIniciales;
		switch (numJugadores) {
		case 2 -> tropasIniciales = 40;
		case 3 -> tropasIniciales = 35;
		case 4 -> tropasIniciales = 30;
		case 5 -> tropasIniciales = 25;
		case 6 -> tropasIniciales = 20;
		default -> throw new RuntimeException("Número de jugadores no válido");
		}

		for (int i = 0; i < numJugadores; i++) {
			Long idJugador = ordenJugadores.getPos(i).getInfo();
			jugadorService.agregarTropas(idJugador, tropasIniciales);
		}

		// 2️⃣ Inicializar todos los territorios sin dueño y 0 tropas
		MyLinkedList<TerritorioDTO> territorios = territorioService.obtenerTodos();
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			territorioService.quitarTodasLasTropas(t.getId());
			territorioService.asignarJugador(t.getId(), 0L); // 0L = sin dueño
		}

		// 3️⃣ El turno inicial será el primer jugador de la lista
		partida.setJugadorActualId(ordenJugadores.getPos(0).getInfo());

		// 4️⃣ Guardar cambios en la partida
		partidaRepository.save(partida);
	}

	@Transactional
	public void reclamarTerritorio(Long partidaId, Long jugadorId, Long territorioId) {

		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		// Validar que sea el turno del jugador
		if (!partida.getJugadorActualId().equals(jugadorId)) {
			throw new RuntimeException("No es el turno de este jugador");
		}

		// Cargar lista de jugadores
		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);

		// Cargar todos los territorios
		MyLinkedList<TerritorioDTO> territorios = territorioService.obtenerTodos();

		// Buscar territorio solicitado
		TerritorioDTO territorioElegido = null;
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getId().equals(territorioId)) {
				territorioElegido = t;
				break;
			}
		}

		if (territorioElegido == null) {
			throw new RuntimeException("Territorio no encontrado");
		}

		// Verificar que esté libre
		if (territorioElegido.getIdJugador() != 0L) {
			throw new RuntimeException("Territorio ya tiene dueño");
		}

		// Asignar territorio y reforzar con 1 tropa
		territorioService.asignarJugador(territorioElegido.getId(), jugadorId);
		territorioService.reforzar(territorioElegido.getId(), 1);

		// Descontar la tropa de la reserva del jugador
		jugadorService.quitarTropas(jugadorId, 1);

		// Actualizar territorios controlados del jugador
		jugadorService.agregarTerritorio(jugadorId);

		// Pasar al siguiente jugador
		Node<Long> nodoActual = null;
		Node<Long> current = ordenJugadores.getFirst();
		while (current != null) {
			if (current.getInfo().equals(jugadorId)) {
				nodoActual = current;
				break;
			}
			current = current.getNext();
		}

		Node<Long> nodoSiguiente = nodoActual.getNext();
		if (nodoSiguiente == null) {
			nodoSiguiente = ordenJugadores.getFirst(); // ciclo circular
		}
		partida.setJugadorActualId(nodoSiguiente.getInfo());

		// -----------------------
		// Bonus de continentes
		// -----------------------
		boolean quedanTerritoriosLibres = false;
		for (int i = 0; i < territorios.size(); i++) {
			if (territorios.getPos(i).getInfo().getIdJugador() == 0L) {
				quedanTerritoriosLibres = true;
				break;
			}
		}

		if (!quedanTerritoriosLibres) {
			MyLinkedList<ContinenteDTO> continentes = continenteService.obtenerTodos();
			for (int c = 0; c < continentes.size(); c++) {
				ContinenteDTO continente = continentes.getPos(c).getInfo();
				for (int i = 0; i < ordenJugadores.size(); i++) {
					Long idJugador = ordenJugadores.getPos(i).getInfo();
					if (territorioService.jugadorControlaContinente(idJugador, continente.getId())) {
						jugadorService.agregarTropas(idJugador,
								continenteService.getBonusPorContinente(continente.getNombre()));
					}
				}
			}
		}

		// Guardar cambios en la partida
		partidaRepository.save(partida);
	}

	@Transactional
	public void iniciarFaseColocacionTropasInicial(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);
		int turnoJugador = 0;
		boolean quedanTropasPorColocar = true;

		// Mientras algún jugador tenga tropas disponibles
		while (quedanTropasPorColocar) {
			Long idJugadorActual = ordenJugadores.getPos(turnoJugador).getInfo();
			Jugador jugador = jugadorService.obtenerJugadorPorId(idJugadorActual);

			if (jugador.getTropasDisponibles() > 0) {
				// Aquí se espera la acción del jugador en frontend para colocar tropas:
				// Se llamaría a JugadorService.colocarTropasEnTerritorio(idJugadorActual,
				// idTerritorio, cantidad);
			}

			// Pasar al siguiente jugador
			turnoJugador = (turnoJugador + 1) % ordenJugadores.size();

			// Verificar si todos los jugadores ya colocaron todas sus tropas
			quedanTropasPorColocar = false;
			for (int i = 0; i < ordenJugadores.size(); i++) {
				Long idJ = ordenJugadores.getPos(i).getInfo();
				Jugador j = jugadorService.obtenerJugadorPorId(idJ);
				if (j.getTropasDisponibles() > 0) {
					quedanTropasPorColocar = true;
					break;
				}
			}
		}

		// Guardar cambios en la partida
		partidaRepository.save(partida);
	}

// *****FASE 1**********
	@Transactional
	public void iniciarFaseRefuerzo(Long partidaId) {

		// 1️⃣ Cargar partida
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		// 2️⃣ Cargar orden de jugadores desde el JSON
		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);

		// 3️⃣ DAR REFUERZOS A CADA JUGADOR
		for (int i = 0; i < ordenJugadores.size(); i++) {

			Long idJugador = ordenJugadores.getPos(i).getInfo();

			// ✅ A. Canje obligatorio de cartas si tiene 5+
			verificarCanjeCartas(partidaId, idJugador);

			Jugador jugador = jugadorService.obtenerJugadorPorId(idJugador);

			// ✅ B. Refuerzos por territorios (mínimo 3)
			int refuerzos = jugador.getTerritoriosControlados() / 3;
			if (refuerzos < 3)
				refuerzos = 3;

			// ✅ C. BONUS por continentes COMPLETOS
			refuerzos += calcularBonusContinentes(idJugador);

			// ✅ D. Asignar tropas disponibles al jugador
			jugadorService.agregarTropas(idJugador, refuerzos);
		}

		// 4️⃣ CICLO PARA COLOCAR TROPAS MANUALMENTE
		boolean quedanTropasPorColocar = true;
		int turnoJugador = 0;

		while (quedanTropasPorColocar) {

			Long idJugadorActual = ordenJugadores.getPos(turnoJugador).getInfo();
			Jugador jugador = jugadorService.obtenerJugadorPorId(idJugadorActual);

			if (jugador.getTropasDisponibles() > 0) {
				// Aquí el FRONT debe llamar a:
				// jugadorService.colocarTropas(idJugadorActual, idTerritorio, cantidad);
			}

			// Pasar al siguiente jugador
			turnoJugador = (turnoJugador + 1) % ordenJugadores.size();

			// Verificar si aún queda algún jugador con tropas por colocar
			quedanTropasPorColocar = false;
			for (int i = 0; i < ordenJugadores.size(); i++) {
				Jugador j = jugadorService.obtenerJugadorPorId(ordenJugadores.getPos(i).getInfo());
				if (j.getTropasDisponibles() > 0) {
					quedanTropasPorColocar = true;
					break;
				}
			}
		}

		// 5️⃣ Guardar cambios finales
		partidaRepository.save(partida);
	}

	// ************ FASE 2 ****************
	/**
	 * Flag temporal para saber si el jugador conquistó al menos un territorio en su
	 * turno
	 */

	/**
	 * Método principal para que un jugador ataque un territorio. Permite múltiples
	 * ataques por turno.
	 */
	public ResultadoAtaqueDTO atacar(Long partidaId, Long atacanteId, Long territorioAtacanteId,
			Long territorioDefensorId, int dadosAtacante, int dadosDefensor) {

		// Llama al ataque real
		ResultadoAtaqueDTO resultado = ataqueService.atacar(partidaId, atacanteId, territorioAtacanteId,
				territorioDefensorId, dadosAtacante, dadosDefensor);

		// Si hubo conquista, activa el flag para entregar carta al final del turno
		if (resultado.isConquista()) {
			conquistoTerritorio = true;
		}

		return resultado;
	}

	/**
	 * Finaliza el turno del jugador actual. Entrega carta si conquistó al menos un
	 * territorio y pasa al siguiente jugador.
	 */
	public void finalizarTurno(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("Partida no existe"));

		// Reconstruir lista de jugadores
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<JugadorDTO>>() {
				}.getType());

		// Obtener nodo del jugador actual
		Node<JugadorDTO> nodoJugadorActual = getNodoPorId(jugadores, partida.getJugadorActualId());
		JugadorDTO jugadorActual = nodoJugadorActual.getInfo();

		// Entregar carta si conquistó territorio
		if (conquistoTerritorio) {
			CartaDTO nuevaCartaDTO = cartaService.robarCarta();
			if (nuevaCartaDTO != null) {
				if (jugadorActual.getCartas() == null) {
					jugadorActual.setCartas(new MyLinkedList<>());
				}
				// Convertir DTO a entidad Carta antes de agregar
				Carta nuevaCarta = modelMapper.map(nuevaCartaDTO, Carta.class);
				jugadorActual.getCartas().addLast(nuevaCarta);
			}
			conquistoTerritorio = false; // reset para próximo turno
		}

		// Pasar al siguiente jugador
		Node<JugadorDTO> nodoSiguiente = nodoJugadorActual.getNext();
		if (nodoSiguiente == null)
			nodoSiguiente = jugadores.getFirst(); // ciclo circular
		partida.setJugadorActualId(nodoSiguiente.getInfo().getId());

		// Guardar jugadores actualizados
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores));
		partidaRepository.save(partida);
	}

	@Transactional
	public int canjearCartas(Long partidaId, Long jugadorId, MyLinkedList<Long> idsCartas) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("Partida no existe"));

		// Cargar lista de jugadores
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<JugadorDTO>>() {
				}.getType());

		Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
		if (nodoJugador == null) {
			throw new RuntimeException("Jugador no encontrado en la partida");
		}

		JugadorDTO jugador = nodoJugador.getInfo();

		// Validación: mínimo 3 cartas
		if (jugador.getCartas() == null || jugador.getCartas().size() < 3) {
			throw new RuntimeException("No tienes suficientes cartas para canjear");
		}

		MyLinkedList<Carta> mano = jugador.getCartas();

		// Validar que las cartas enviadas existan en la mano
		for (int i = 0; i < idsCartas.size(); i++) {
			Long idCarta = idsCartas.getPos(i).getInfo();
			boolean existe = false;
			for (int j = 0; j < mano.size(); j++) {
				if (mano.getPos(j).getInfo().getId().equals(idCarta)) {
					existe = true;
					break;
				}
			}
			if (!existe) {
				throw new RuntimeException("Carta con ID " + idCarta + " no encontrada en la mano");
			}
		}

		// Validar combinación válida (3 iguales o 3 diferentes con comodines)
		if (!cartaService.combinacionValida(idsCartas, mano)) {
			throw new RuntimeException("Combinación de cartas inválida");
		}

		// Remover cartas de la mano y devolver tropas
		int tropas = cartaService.canjearCartas(idsCartas, mano);

		// Guardar cambios en la partida
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores));
		partidaRepository.save(partida);

		return tropas;
	}

	@Transactional
	public void verificarCanjeCartas(Long partidaId, Long jugadorId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		// Reconstruir lista de jugadores
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<JugadorDTO>>() {
				}.getType());

		Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
		if (nodoJugador == null) {
			throw new RuntimeException("Jugador no encontrado en la partida");
		}

		JugadorDTO jugador = nodoJugador.getInfo();

		if (jugador.getCartas() == null || jugador.getCartas().size() < 3) {
			// No tiene suficientes cartas, nada que hacer
			return;
		}

		int numCartas = jugador.getCartas().size();

		if (numCartas >= 5) {
			// Forzar canje: el jugador DEBE canjear antes de continuar
			throw new RuntimeException("Tienes 5 o más cartas, debes canjear antes de continuar");
		}

		if (numCartas >= 3) {
			// Puede canjear si quiere, no se obliga
			// Aquí podrías enviar info al frontend para mostrar botón de canje
			// Por ejemplo: jugador.setPuedeCanjear(true);
		}

		// Guardar cambios de jugadores si se modifica algo (por ejemplo, bandera de
		// opción de canje)
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores));
		partidaRepository.save(partida);
	}

	/** Obtiene nodo de jugador por ID */
	private Node<JugadorDTO> getNodoPorId(MyLinkedList<JugadorDTO> lista, Long id) {
		Node<JugadorDTO> current = lista.getFirst();
		while (current != null) {
			if (current.getInfo().getId().equals(id))
				return current;
			current = current.getNext();
		}
		return null;
	}

	private TerritorioDTO seleccionarTerritorioJugador(Long idJugador, MyLinkedList<TerritorioDTO> territorios) {
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getIdJugador() == 0L) { // territorio libre
				return t;
			}
		}
		return null; // no quedan territorios libres
	}

	private MyLinkedList<Long> cargarOrdenJugadores(Partida partida) {
		if (partida.getJugadoresOrdenTurnoJSON() == null || partida.getJugadoresOrdenTurnoJSON().isEmpty()) {
			throw new RuntimeException("La partida no tiene orden de jugadores definido");
		}

		// Usamos Gson para deserializar a MyLinkedList<Long>
		Type type = new TypeToken<MyLinkedList<Long>>() {
		}.getType();
		return gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), type);
	}
	/**
	 * Calcula los bonus de continentes controlados por un jugador.
	 */
	private int calcularBonusContinentes(Long jugadorId) {

	    int bonus = 0;

	    // Obtener todos los continentes
	    MyLinkedList<ContinenteDTO> continentes = continenteService.obtenerTodos();

	    for (int i = 0; i < continentes.size(); i++) {

	        ContinenteDTO cont = continentes.getPos(i).getInfo();

	        // Verificar si el jugador controla TODO el continente
	        boolean controla = territorioService.jugadorControlaContinente(jugadorId, cont.getId());

	        if (controla) {
	            // Obtener bonus según nombre (tu servicio ya lo hace)
	            bonus += continenteService.getBonusPorContinente(cont.getNombre());
	        }
	    }

	    return bonus;
	}


}
