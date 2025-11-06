package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.JsonUtil;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyDoubleLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.JugadorRepository;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.repository.PartidaRepository;

@Service
public class PartidaService {
	@Autowired
	private  PartidaRepository partidaRepository;
	@Autowired
	private  JugadorRepository jugadorRepository;
	@Autowired
	private  JugadorService jugadorService;
	@Autowired
	private  ContinenteService continenteService;
	@Autowired
	private TerritorioService territorioService;

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
	public void reclamarTerritoriosPorTurnos(Long partidaId) {

		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		// 1️⃣ Cargar orden de jugadores
		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);

		// 2️⃣ Cargar todos los territorios
		MyLinkedList<TerritorioDTO> territorios = territorioService.obtenerTodos();

		// 3️⃣ Índice para el turno del jugador
		int turnoJugador = 0;
		int totalTerritorios = territorios.size();

		// 4️⃣ Mientras haya territorios sin dueño
		boolean quedanTerritoriosLibres = true;
		while (quedanTerritoriosLibres) {

			Long idJugadorActual = ordenJugadores.getPos(turnoJugador).getInfo();

			// Selección de territorio por el jugador
			TerritorioDTO territorioElegido = seleccionarTerritorioJugador(idJugadorActual, territorios);
			if (territorioElegido == null) {
				break; // No quedan territorios libres
			}

			// Asignar territorio al jugador y reforzar con 1 tropa
			territorioService.asignarJugador(territorioElegido.getId(), idJugadorActual);
			territorioService.reforzar(territorioElegido.getId(), 1);

			// Actualizar territorios controlados usando método estático de JugadorService
			jugadorService.agregarTerritorio(idJugadorActual);

			// Pasar al siguiente jugador
			turnoJugador = (turnoJugador + 1) % ordenJugadores.size();

			// Verificar si quedan territorios libres
			quedanTerritoriosLibres = false;
			for (int i = 0; i < totalTerritorios; i++) {
				if (territorios.getPos(i).getInfo().getIdJugador() == 0L) {
					quedanTerritoriosLibres = true;
					break;
				}
			}
		}

		// 5️⃣ Asignar bonus de continentes
		MyLinkedList<ContinenteDTO> continentes = continenteService.obtenerTodos();
		for (int c = 0; c < continentes.size(); c++) {
			ContinenteDTO continente = continentes.getPos(c).getInfo();

			for (int i = 0; i < ordenJugadores.size(); i++) {
				Long idJugador = ordenJugadores.getPos(i).getInfo();
				if (territorioService.jugadorControlaContinente(idJugador, continente.getId())) {
					// Dar tropas de bonificación según el continente
					jugadorService.agregarTropas(idJugador,
							continenteService.getBonusPorContinente(continente.getNombre()));
				}
			}
		}

		// 6️⃣ Guardar cambios en la partida
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

		// 2️⃣ Cargar orden de jugadores
		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);

		// 3️⃣ Dar tropas de refuerzo inicial a cada jugador
		for (int i = 0; i < ordenJugadores.size(); i++) {
			Long idJugador = ordenJugadores.getPos(i).getInfo();
			Jugador jugador = jugadorService.obtenerJugadorPorId(idJugador);

			int refuerzos = jugador.getTerritoriosControlados() / 3;
			if (refuerzos < 3)
				refuerzos = 3; // mínimo 3 tropas
			jugadorService.agregarTropas(idJugador, refuerzos);
		}

		// 4️⃣ Ciclo de turno para que los jugadores coloquen manualmente sus tropas
		boolean quedanTropasPorColocar = true;
		int turnoJugador = 0;

		while (quedanTropasPorColocar) {
			Long idJugadorActual = ordenJugadores.getPos(turnoJugador).getInfo();
			Jugador jugador = jugadorService.obtenerJugadorPorId(idJugadorActual);

			if (jugador.getTropasDisponibles() > 0) {
				// Aquí se espera acción del jugador desde el frontend
				// Ejemplo:
				// JugadorService.colocarTropasEnTerritorio(idJugadorActual, idTerritorio,
				// cantidad);
				// Solo puede colocar tropas en territorios que controla
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

		// 5️⃣ Guardar cambios en la partida
		partidaRepository.save(partida);
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

}
