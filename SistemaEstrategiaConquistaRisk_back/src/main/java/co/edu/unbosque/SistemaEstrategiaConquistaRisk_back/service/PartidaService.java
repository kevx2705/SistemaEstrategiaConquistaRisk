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

	private final Gson gson = new Gson();

	public PartidaService() {
		// TODO Auto-generated constructor stub
	}

	public Partida crearPartida(Long anfitrionId, String[] otrosNombres) {
		// -------------------------------
		// 0. Validar anfitrión
		// -------------------------------
		Jugador anfitrion = jugadorRepository.findById(anfitrionId)
				.orElseThrow(() -> new RuntimeException("No existe el jugador anfitrión"));

		if (anfitrion.isActivo()) {
			throw new RuntimeException("El anfitrión ya está participando en otra partida.");
		}

		// Asignar color y persistir
		anfitrion.setColor("ROJO");
		jugadorRepository.save(anfitrion); // guarda el color
		jugadorService.activarJugador(anfitrion.getId()); // marca activo

		// -------------------------------
		// 1. Crear la partida
		// -------------------------------
		Partida partida = new Partida();
		partida.setIniciada(true);
		partida.setFinalizada(false);
		partida.setFechaInicio(LocalDateTime.now());
		partida.setCodigoHash(UUID.randomUUID().toString());

		// -------------------------------
		// 2. Preparar lista de jugadores DTO
		// -------------------------------
		MyLinkedList<JugadorDTO> jugadoresDTO = new MyLinkedList<>();

		// Crear DTO del anfitrión con estado inicial
		JugadorDTO dtoAnfitrion = modelMapper.map(anfitrion, JugadorDTO.class);
		dtoAnfitrion.setTropasDisponibles(0);
		dtoAnfitrion.setTerritoriosControlados(0);
		dtoAnfitrion.setCartas(new MyLinkedList<>());
		jugadoresDTO.addLast(dtoAnfitrion);

		// -------------------------------
		// 3. Colores disponibles
		// -------------------------------
		String[] colores = { "ROJO", "AZUL", "VERDE", "AMARILLO", "NEGRO", "BLANCO" };
		if (otrosNombres.length + 1 > colores.length) {
			throw new RuntimeException("Hay más jugadores que colores disponibles.");
		}
		int colorIndex = 1; // ROJO ya usado por anfitrión

		// -------------------------------
		// 4. Crear jugadores temporales
		// -------------------------------
		for (String nombre : otrosNombres) {
			if (nombre == null || nombre.trim().isEmpty())
				continue;

			Jugador existente = jugadorRepository.findByNombre(nombre);
			if (existente != null && existente.isActivo()) {
				throw new RuntimeException("El jugador '" + nombre + "' ya está en otra partida.");
			}

			// Crear jugador temporal y persistir
			Jugador nuevo = jugadorService.crearJugadorTemporal(nombre, colores[colorIndex++]);

			// Mapear a DTO y asignar estado inicial
			JugadorDTO dto = modelMapper.map(nuevo, JugadorDTO.class);
			dto.setTropasDisponibles(0);
			dto.setTerritoriosControlados(0);
			dto.setCartas(new MyLinkedList<>());
			jugadoresDTO.addLast(dto);
		}

		// -------------------------------
		// 5. Asignar jugador actual
		// -------------------------------
		partida.setJugadorActualId(jugadoresDTO.getFirst().getInfo().getId());

		// -------------------------------
		// 6. Serializar lista de jugadores
		// -------------------------------
		String jsonJugadores = JsonUtil.toJson(jugadoresDTO);
		partida.setJugadoresOrdenTurnoJSON(jsonJugadores);

		// -------------------------------
		// 7. Inicializar mazo y descarte
		// -------------------------------
		// Aquí depende de tu implementación de cartas:
		// Puedes crear un método en cartaService que devuelva el JSON del mazo inicial
		String mazoJSON = cartaService.inicializarMazoParaPartida();
		partida.setMazoCartasJSON(mazoJSON);

		// -------------------------------
		// 8. Guardar partida
		// -------------------------------
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

	    // ✅ 2️⃣ Inicializar territorios usando SOLO el JSON de la partida
	    MyLinkedList<TerritorioDTO> territorios =
	            gson.fromJson(partida.getTerritoriosJSON(),
	                    new TypeToken<MyLinkedList<TerritorioDTO>>() {}.getType());

	    for (int i = 0; i < territorios.size(); i++) {
	        TerritorioDTO t = territorios.getPos(i).getInfo();
	        t.setTropas(0);
	        t.setIdJugador(0L); // sin dueño
	    }

	    // ✅ Guardar territorios actualizados
	    partida.setTerritoriosJSON(gson.toJson(territorios));

	    // 3️⃣ El turno inicial será el primer jugador de la lista
	    partida.setJugadorActualId(ordenJugadores.getPos(0).getInfo());

	    // 4️⃣ Guardar cambios en la partida
	    partidaRepository.save(partida);
	}


	@Transactional
	public void reclamarTerritorio(Long partidaId, Long jugadorId, Long territorioId) {

	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("No existe la partida"));

	    // ✅ Validar turno
	    if (!partida.getJugadorActualId().equals(jugadorId)) {
	        throw new RuntimeException("No es el turno de este jugador");
	    }

	    // ✅ Cargar orden de jugadores
	    MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);

	    // ✅ Cargar territorios de la partida (NO de la BD)
	    MyLinkedList<TerritorioDTO> territorios = cargarTerritorios(partida);

	    // ✅ Buscar territorio elegido
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

	    // ✅ Verificar que esté libre
	    if (territorioElegido.getIdJugador() != 0L) {
	        throw new RuntimeException("Territorio ya asignado");
	    }

	    // ✅ Asignar dueño Y 1 tropa dentro del JSON de la partida
	    territorioElegido.setIdJugador(jugadorId);
	    territorioElegido.setTropas(1);

	    // ✅ Quitar tropa del jugador
	    jugadorService.quitarTropas(jugadorId, 1);

	    // ✅ Sumar territorio al jugador
	    jugadorService.agregarTerritorio(jugadorId);

	    // ✅ Cambiar turno al siguiente jugador
	    Long siguiente = obtenerSiguienteJugador(ordenJugadores, jugadorId);
	    partida.setJugadorActualId(siguiente);

	    // ✅ Guardar territorios actualizados en la partida
	    partida.setTerritoriosJSON(gson.toJson(territorios));

	    // ✅ Guardar partida
	    partidaRepository.save(partida);
	}


	@Transactional
	public void iniciarFaseColocacionTropasInicial(Long partidaId) {
	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("No existe la partida"));

	    // Cargar el orden de turnos
	    MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);

	    // --- CAMBIO IMPORTANTE ---
	    // Aquí NO se colocan tropas ni se hace ningún recorrido con while.
	    // Solo dejamos la partida lista para que el FRONT coloque las tropas
	    // llamando a un endpoint como colocarTropa(...)

	    // Guardar estado actual
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

	    // 3️⃣ DAR REFUERZOS A CADA JUGADOR (pero SIN colocarlos aquí)
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

	    // ✅ 4️⃣ IMPORTANTE:
	    // Se ELIMINA el ciclo que esperaba la colocación manual.
	    // AHORA el FRONT debe llamar a un endpoint como:
	    //  POST /partida/{id}/colocar-tropa
	    // cada vez que el jugador ponga UNA tropa en un territorio.
	    //
	    // El backend NO debe esperar ni colocar tropas aquí.

	    // 5️⃣ Guardar cambios finales
	    partidaRepository.save(partida);
	}
	/**
	 * Coloca tropas en un territorio que pertenece al jugador.
	 * Actualiza el JSON de territorios dentro de la Partida.
	 */
	public void colocarTropa(Partida partida, Long jugadorId, String nombreTerritorio, int cantidad) {

	    if (cantidad <= 0) {
	        throw new RuntimeException("La cantidad de tropas debe ser mayor que 0.");
	    }

	    // --- 1. Cargar territorios desde JSON ---
	    Type listType = new TypeToken<MyLinkedList<TerritorioDTO>>() {}.getType();

	    if (partida.getTerritoriosJSON() == null || partida.getTerritoriosJSON().isBlank()) {
	        throw new RuntimeException("No existen territorios cargados en la partida.");
	    }

	    MyLinkedList<TerritorioDTO> territorios =
	            gson.fromJson(partida.getTerritoriosJSON(), listType);

	    // --- 2. Buscar territorio ---
	    TerritorioDTO territorioObjetivo = null;

	    for (int i = 0; i < territorios.size(); i++) {
	        TerritorioDTO t = territorios.getPos(i).getInfo();
	        if (t.getNombre().equalsIgnoreCase(nombreTerritorio)) {
	            territorioObjetivo = t;
	            break;
	        }
	    }

	    if (territorioObjetivo == null) {
	        throw new RuntimeException("El territorio '" + nombreTerritorio + "' no existe.");
	    }

	    // --- 3. Validar propietario ---
	    if (!territorioObjetivo.getIdJugador().equals(jugadorId)) {
	        throw new RuntimeException("No puedes colocar tropas en un territorio que no te pertenece.");
	    }

	    // --- 4. Actualizar tropas ---
	    int tropasActuales = territorioObjetivo.getTropas();
	    territorioObjetivo.setTropas(tropasActuales + cantidad);

	    // --- 5. Guardar cambios en JSON ---
	    String nuevosTerritoriosJSON = gson.toJson(territorios, listType);
	    partida.setTerritoriosJSON(nuevosTerritoriosJSON);

	    // --- 6. 😡 NECESARIO: Guardar en BD ---
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

		// No necesitamos guardar flags ni modificar la partida aquí.
		// El cálculo de si el jugador conquistó se hará en finalizarTurno().

		return resultado;
	}

	public void finalizarTurno(Long partidaId) {
	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("Partida no existe"));

	    // --- 1️⃣ Reconstruir lista de jugadores ---
	    Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {}.getType();
	    MyLinkedList<JugadorDTO> jugadores =
	            gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), jugadoresType);

	    Node<JugadorDTO> nodoJugadorActual = getNodoPorId(jugadores, partida.getJugadorActualId());
	    JugadorDTO jugadorActual = nodoJugadorActual.getInfo();

	    // --- 2️⃣ Cargar territorios DESDE LA PARTIDA ---
	    if (partida.getTerritoriosJSON() == null || partida.getTerritoriosJSON().isBlank()) {
	        throw new RuntimeException("La partida no tiene territorios cargados.");
	    }

	    Type territoriosType = new TypeToken<MyLinkedList<TerritorioDTO>>() {}.getType();
	    MyLinkedList<TerritorioDTO> territorios =
	            gson.fromJson(partida.getTerritoriosJSON(), territoriosType);

	    // --- 3️⃣ Contar territorios controlados ---
	    int territoriosActuales = 0;
	    for (int i = 0; i < territorios.size(); i++) {
	        TerritorioDTO t = territorios.getPos(i).getInfo();
	        if (t.getIdJugador().equals(jugadorActual.getId())) {
	            territoriosActuales++;
	        }
	    }

	    // --- 4️⃣ Verificar conquista ---
	    if (territoriosActuales > jugadorActual.getTerritoriosControlados()) {

	        CartaDTO nuevaCartaDTO = cartaService.robarCarta();
	        if (nuevaCartaDTO != null) {

	            if (jugadorActual.getCartas() == null) {
	                jugadorActual.setCartas(new MyLinkedList<>());
	            }

	            // ✅ Guardar CartaDTO, NO Carta entidad
	            jugadorActual.getCartas().add(nuevaCartaDTO);
	        }
	    }

	    // --- 5️⃣ Actualizar contador ---
	    jugadorActual.setTerritoriosControlados(territoriosActuales);

	    // --- 6️⃣ Pasar turno ---
	    Node<JugadorDTO> nodoSiguiente = nodoJugadorActual.getNext();
	    if (nodoSiguiente == null) nodoSiguiente = jugadores.getFirst();

	    partida.setJugadorActualId(nodoSiguiente.getInfo().getId());

	    // --- 7️⃣ Guardar JSON actualizado ---
	    partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores, jugadoresType));

	    partidaRepository.save(partida);
	}

	@Transactional
	public int canjearCartas(Long partidaId, Long jugadorId, MyLinkedList<Long> idsCartas) {

	    // 1️⃣ Cargar la partida
	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("Partida no existe"));

	    // 2️⃣ Reconstruir la lista de IDs de jugadores desde JSON
	    MyLinkedList<Long> listaIds = gson.fromJson(
	            partida.getJugadoresOrdenTurnoJSON(),
	            new TypeToken<MyLinkedList<Long>>(){}.getType()
	    );

	    // 3️⃣ Reconstruir los JugadorDTO usando sus IDs
	    MyLinkedList<JugadorDTO> jugadores = cargarJugadoresDesdeIds(listaIds);

	    // 4️⃣ Obtener el nodo del jugador que quiere canjear
	    Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
	    if (nodoJugador == null) {
	        throw new RuntimeException("Jugador no encontrado en la partida");
	    }

	    JugadorDTO jugador = nodoJugador.getInfo();

	    // 5️⃣ Validación: mínimo 3 cartas en la mano
	    if (jugador.getCartas() == null || jugador.getCartas().size() < 3) {
	        throw new RuntimeException("No tienes suficientes cartas para canjear");
	    }

	    // 6️⃣ Trabajar con la mano como MyLinkedList<CartaDTO>
	    MyLinkedList<CartaDTO> mano = jugador.getCartas();

	    // 7️⃣ Validar que todas las cartas enviadas existen en la mano
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

	    // 8️⃣ Aquí puedes hacer la lógica de canje:
	    //      por ejemplo, remover las cartas usadas y devolver el bonus correspondiente
	    for (int i = 0; i < idsCartas.size(); i++) {
	        Long idCarta = idsCartas.getPos(i).getInfo();
	        for (int j = 0; j < mano.size(); j++) {
	            if (mano.getPos(j).getInfo().getId().equals(idCarta)) {
	                mano.delete(mano.getPos(j).getInfo());
	                break;
	            }
	        }
	    }

	    // 9️⃣ Guardar la mano actualizada en el jugador
	    jugador.setCartas(mano);

	    // 1️⃣0️⃣ Guardar la lista de jugadores actualizada en el JSON de la partida
	    Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {}.getType();
	    partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores, jugadoresType));

	    partidaRepository.save(partida);

	    // 1️⃣1️⃣ Retornar el bonus por canje, aquí un ejemplo fijo
	    return 5; // reemplaza por la lógica de cálculo de tropas según tu juego
	}



	@Transactional
	public void verificarCanjeCartas(Long partidaId, Long jugadorId) {

	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("No existe la partida"));

	    // Cargar IDs desde el JSON
	    MyLinkedList<Long> listaIds = gson.fromJson(
	            partida.getJugadoresOrdenTurnoJSON(),
	            new TypeToken<MyLinkedList<Long>>(){}.getType()
	    );

	    // Reconstruir jugadores
	    MyLinkedList<JugadorDTO> jugadores = cargarJugadoresDesdeIds(listaIds);

	    Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
	    if (nodoJugador == null) {
	        throw new RuntimeException("Jugador no encontrado en la partida");
	    }

	    JugadorDTO jugador = nodoJugador.getInfo();

	    int numCartas = jugador.getCartas() != null ? jugador.getCartas().size() : 0;

	    if (numCartas >= 5) {
	        // OBLIGATORIO
	        throw new RuntimeException("Tienes 5 o más cartas, debes canjear antes de continuar");
	    }

	    // Si tiene 3 o 4 → canje opcional, no se obliga

	    // Guardar cambios
	    guardarJugadores(partida, jugadores);
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

		// Deserializar correctamente a MyLinkedList<JugadorDTO>
		Type typeJugadores = TypeToken.getParameterized(MyLinkedList.class, JugadorDTO.class).getType();
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), typeJugadores);

		// Convertir JugadorDTO -> Long (solo los IDs) usando la API de nodos
		MyLinkedList<Long> ordenIds = new MyLinkedList<>();

		Node<JugadorDTO> current = jugadores.getFirst();
		while (current != null) {
			JugadorDTO j = current.getInfo();
			if (j == null || j.getId() == null) {
				throw new RuntimeException("Jugador en el orden con ID nulo o inválido");
			}
			ordenIds.addLast(j.getId());
			current = current.getNext();
		}

		return ordenIds;
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

			// Verificar si el jugador controla TODO el continente (usa TerritorioService)
			boolean controla = territorioService.jugadorControlaContinente(jugadorId, cont.getId());

			if (controla) {
				// Obtener bonus definido (tu ContinenteService ya tiene getBonusPorContinente)
				bonus += continenteService.getBonusPorContinente(cont.getNombre());
			}
		}

		return bonus;
	}

	private Long obtenerSiguienteJugador(MyLinkedList<Long> orden, Long jugadorActual) {
		for (int i = 0; i < orden.size(); i++) {
			if (orden.getPos(i).getInfo().equals(jugadorActual)) {
				int sig = (i + 1) % orden.size();
				return orden.getPos(sig).getInfo();
			}
		}
		throw new RuntimeException("Jugador no encontrado en el orden");
	}
	private void guardarJugadores(Partida partida, MyLinkedList<JugadorDTO> jugadores) {
	    String json = gson.toJson(jugadores, new TypeToken<MyLinkedList<JugadorDTO>>() {}.getType());
	    partida.setJugadoresOrdenTurnoJSON(json);
	    partidaRepository.save(partida);
	}
	private MyLinkedList<JugadorDTO> cargarJugadoresDesdeIds(MyLinkedList<Long> ids) {

	    MyLinkedList<JugadorDTO> jugadores = new MyLinkedList<>();

	    for (int i = 0; i < ids.size(); i++) {

	        Long id = ids.getPos(i).getInfo();

	        Jugador entidad = jugadorRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Jugador con id " + id + " no existe"));

	        JugadorDTO dto = modelMapper.map(entidad, JugadorDTO.class);

	        // Asegurar lista de cartas
	        if (dto.getCartas() == null) {
	            dto.setCartas(new MyLinkedList<>());
	        }

	        jugadores.addLast(dto);
	    }

	    return jugadores;
	}
	@Transactional
	public void eliminarJugador(Long partidaId, Long jugadorId) {

	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("Partida no existe"));

	    // Cargar jugadores
	    MyLinkedList<JugadorDTO> jugadores = gson.fromJson(
	            partida.getJugadoresOrdenTurnoJSON(),
	            new TypeToken<MyLinkedList<JugadorDTO>>(){}.getType()
	    );

	    // Cargar territorios
	    MyLinkedList<TerritorioDTO> territorios = gson.fromJson(
	            partida.getTerritoriosJSON(),
	            new TypeToken<MyLinkedList<TerritorioDTO>>(){}.getType()
	    );

	    // Buscar jugador en lista
	    Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
	    if (nodoJugador == null) {
	        throw new RuntimeException("Jugador no existe en la partida");
	    }
	    JugadorDTO jugador = nodoJugador.getInfo();

	    // 1️⃣ Liberar territorios
	    for (int i = 0; i < territorios.size(); i++) {
	        TerritorioDTO t = territorios.getPos(i).getInfo();
	        if (t.getIdJugador() != null && t.getIdJugador().equals(jugadorId)) {
	            t.setIdJugador(null);
	            t.setTropas(0);
	        }
	    }

	    // 2️⃣ Limpiar la mano del jugador (opcional si quieres devolver las cartas al service)
	    if (jugador.getCartas() != null) {
	        jugador.setCartas(new MyLinkedList<>()); 
	    }

	    // 3️⃣ Eliminar jugador del orden de turno
	    eliminarNodo(jugadores, nodoJugador);

	    // 4️⃣ Ajustar jugador actual
	    if (partida.getJugadorActualId() != null 
	            && partida.getJugadorActualId().equals(jugadorId)) {

	        if (jugadores.size() > 0) {
	            partida.setJugadorActualId(jugadores.getPos(0).getInfo().getId());
	        } else {
	            partida.setJugadorActualId(null);
	        }
	    }

	    // 5️⃣ Guardar cambios
	    partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores));
	    partida.setTerritoriosJSON(gson.toJson(territorios));

	    partidaRepository.save(partida);
	}

	private void eliminarNodo(MyLinkedList<JugadorDTO> lista, Node<JugadorDTO> nodo) {
	    if (lista == null || nodo == null) return;

	    if (lista.getFirst() == nodo) {
	        lista.setFirst(lista.getFirst().getNext());
	        return;
	    }

	    Node<JugadorDTO> actual = lista.getFirst();
	    while (actual != null && actual.getNext() != nodo) {
	        actual = actual.getNext();
	    }

	    if (actual != null) {
	        actual.setNext(nodo.getNext());
	    }
	}
	private MyLinkedList<TerritorioDTO> cargarTerritorios(Partida partida) {

	    // Si hay JSON guardado, reconstruirlo
	    if (partida.getTerritoriosJSON() != null && !partida.getTerritoriosJSON().isBlank()) {
	        return gson.fromJson(
	                partida.getTerritoriosJSON(),
	                new TypeToken<MyLinkedList<TerritorioDTO>>() {}.getType()
	        );
	    }

	    // Si no hay JSON, significa que es la primera vez → cargar territorios base
	    MyLinkedList<TerritorioDTO> territoriosBase = territorioService.obtenerTodos();

	    // Guardarlos en partida para futuras cargas
	    partida.setTerritoriosJSON(gson.toJson(territoriosBase));
	    partidaRepository.save(partida);

	    return territoriosBase;
	}



}
