package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.LinkedTreeMap;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.CartaDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.ContinenteDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.PartidaDTO;
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
	private EmailService emailService;

	@Autowired
	private AtaqueService ataqueService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MapaTerritorioService mapaTerritorio;

	@Autowired
	private PDFService pdfService;

	private final Gson gson = new Gson();

	private MyLinkedList<String> movimientosFase3;

	public PartidaService() {
		movimientosFase3 = new MyLinkedList<>();
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
		partida.setJugadorActualId(anfitrionId);

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

	public PartidaDTO crearPartidaDTO(Long anfitrionId, String[] otrosNombres) {
		Partida partida = crearPartida(anfitrionId, otrosNombres);

		// Convertir entidad a DTO automáticamente
		PartidaDTO dto = modelMapper.map(partida, PartidaDTO.class);

		return dto;
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
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(),
				new TypeToken<MyLinkedList<TerritorioDTO>>() {
				}.getType());

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
		// POST /partida/{id}/colocar-tropa
		// cada vez que el jugador ponga UNA tropa en un territorio.
		//
		// El backend NO debe esperar ni colocar tropas aquí.

		// 5️⃣ Guardar cambios finales
		partidaRepository.save(partida);
	}

	/**
	 * Coloca tropas en un territorio que pertenece al jugador. Actualiza el JSON de
	 * territorios dentro de la Partida.
	 */
	public void colocarTropa(Partida partida, Long jugadorId, String nombreTerritorio, int cantidad) {

		if (cantidad <= 0) {
			throw new RuntimeException("La cantidad de tropas debe ser mayor que 0.");
		}

		// --- 1. Cargar territorios desde JSON ---
		Type listType = new TypeToken<MyLinkedList<TerritorioDTO>>() {
		}.getType();

		if (partida.getTerritoriosJSON() == null || partida.getTerritoriosJSON().isBlank()) {
			throw new RuntimeException("No existen territorios cargados en la partida.");
		}

		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(), listType);

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

	@Transactional
	public void finalizarTurno(Long partidaId) {
		// 1️⃣ Cargar la partida
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("Partida no existe"));

		// 2️⃣ Reconstruir lista de jugadores desde JSON
		Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType();
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), jugadoresType);

		if (jugadores == null || jugadores.size() == 0) {
			throw new RuntimeException("No hay jugadores en la partida");
		}

		// 3️⃣ Obtener nodo del jugador actual
		Node<JugadorDTO> nodoJugadorActual = getNodoPorId(jugadores, partida.getJugadorActualId());
		if (nodoJugadorActual == null) {
			throw new RuntimeException("Jugador actual no encontrado en la lista");
		}

		JugadorDTO jugadorActual = nodoJugadorActual.getInfo();

		// 4️⃣ Cargar territorios desde JSON
		if (partida.getTerritoriosJSON() == null || partida.getTerritoriosJSON().isBlank()) {
			throw new RuntimeException("La partida no tiene territorios cargados.");
		}

		Type territoriosType = new TypeToken<MyLinkedList<TerritorioDTO>>() {
		}.getType();
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(), territoriosType);

		// 5️⃣ Contar territorios controlados por el jugador actual
		int territoriosActuales = 0;
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getIdJugador() != null && t.getIdJugador().equals(jugadorActual.getId())) {
				territoriosActuales++;
			}
		}

		// 6️⃣ Verificar si conquistó al menos un territorio y asignar carta si aplica
		if (territoriosActuales > jugadorActual.getTerritoriosControlados()) {
			CartaDTO nuevaCartaDTO = cartaService.robarCarta();
			if (nuevaCartaDTO != null) {
				if (jugadorActual.getCartas() == null) {
					jugadorActual.setCartas(new MyLinkedList<>());
				}
				jugadorActual.getCartas().add(nuevaCartaDTO);
			}
		}

		// 7️⃣ Actualizar contador de territorios controlados
		jugadorActual.setTerritoriosControlados(territoriosActuales);

		// 8️⃣ Pasar turno al siguiente jugador
		Node<JugadorDTO> nodoSiguiente = nodoJugadorActual.getNext();
		if (nodoSiguiente == null) {
			nodoSiguiente = jugadores.getFirst();
		}

		partida.setJugadorActualId(nodoSiguiente.getInfo().getId());

		// 9️⃣ Guardar JSON actualizado
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores, jugadoresType));

		// 🔟 Guardar cambios en BD
		partidaRepository.save(partida);
	}

	// ***************** FASE 3 ********************
	@Transactional
	public void moverTropasFase3(Long partidaId, Long jugadorId, String territorioOrigen, String territorioDestino,
			int cantidad) {
		// 1️⃣ Cargar partida
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		// 2️⃣ Verificar que sea el turno del jugador
		if (!partida.getJugadorActualId().equals(jugadorId)) {
			throw new RuntimeException("No es tu turno");
		}

		// 3️⃣ Cargar territorios desde JSON
		MyLinkedList<TerritorioDTO> territorios = cargarTerritorios(partida);

		TerritorioDTO origen = buscarTerritorio(territorios, territorioOrigen);
		TerritorioDTO destino = buscarTerritorio(territorios, territorioDestino);

		if (origen == null || destino == null) {
			throw new RuntimeException("Territorio de origen o destino no encontrado");
		}

		// 4️⃣ Validar propietario
		if (!origen.getIdJugador().equals(jugadorId) || !destino.getIdJugador().equals(jugadorId)) {
			throw new RuntimeException("Solo puedes mover tropas entre tus territorios");
		}

		// 5️⃣ Validar cantidad mínima
		if (cantidad <= 0 || cantidad >= origen.getTropas()) {
			throw new RuntimeException("Debes dejar al menos 1 tropa en el territorio de origen");
		}

		// 6️⃣ Validar que los territorios estén conectados mediante territorios propios
		if (!mapaTerritorio.existeCamino(origen.getId(), destino.getId(), jugadorId)) {
			throw new RuntimeException("Los territorios no están conectados a través de tus territorios");
		}

		// 7️⃣ Verificar si ya hizo un movimiento este turno
		if (yaMovioTropasFase3(jugadorId, partidaId)) {
			throw new RuntimeException("Solo puedes mover tropas una vez por turno");
		}

		// 8️⃣ Realizar movimiento
		origen.setTropas(origen.getTropas() - cantidad);
		destino.setTropas(destino.getTropas() + cantidad);

		// 9️⃣ Guardar cambios
		partida.setTerritoriosJSON(gson.toJson(territorios));
		partidaRepository.save(partida);

		// 1️⃣0️⃣ Marcar que el jugador ya realizó su movimiento de fase 3
		marcarMovimientoFase3(jugadorId, partidaId);
	}

	// Método auxiliar para buscar territorio por nombre
	private TerritorioDTO buscarTerritorio(MyLinkedList<TerritorioDTO> territorios, String nombre) {
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getNombre().equalsIgnoreCase(nombre)) {
				return t;
			}
		}
		return null;
	}

	@Transactional
	public int canjearCartas(Long partidaId, Long jugadorId, MyLinkedList<Long> idsCartas) {
		// 1️⃣ Cargar partida y jugadores
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("Partida no existe"));

		MyLinkedList<Long> listaIds = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<Long>>() {
				}.getType());
		MyLinkedList<JugadorDTO> jugadores = cargarJugadoresDesdeIds(listaIds);

		Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
		if (nodoJugador == null)
			throw new RuntimeException("Jugador no encontrado en la partida");

		JugadorDTO jugador = nodoJugador.getInfo();
		MyLinkedList<Carta> mano = new MyLinkedList<>();
		// Convertir CartaDTO a Carta temporalmente para usar CartaService
		for (int i = 0; i < jugador.getCartas().size(); i++) {
			CartaDTO dto = jugador.getCartas().getPos(i).getInfo();
			Carta c = modelMapper.map(dto, Carta.class);
			mano.addLast(c);
		}

		// 2️⃣ Validar combinación
		if (!cartaService.combinacionValida(idsCartas, mano))
			throw new RuntimeException("Combinación de cartas inválida");

		// 3️⃣ Canjear cartas y obtener bonus
		int bonus = cartaService.canjearCartas(idsCartas, mano);

		// 4️⃣ Actualizar la mano del jugador en DTO
		MyLinkedList<CartaDTO> nuevaMano = new MyLinkedList<>();
		Node<Carta> nodo = mano.getFirst();
		while (nodo != null) {
			nuevaMano.addLast(modelMapper.map(nodo.getInfo(), CartaDTO.class));
			nodo = nodo.getNext();
		}
		jugador.setCartas(nuevaMano);

		// 5️⃣ Guardar la lista de jugadores en la partida
		Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType();
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores, jugadoresType));
		partidaRepository.save(partida);

		return bonus;
	}

	@Transactional
	public void verificarCanjeCartas(Long partidaId, Long jugadorId) {

		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));

		// Cargar IDs desde el JSON
		MyLinkedList<Long> listaIds = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<Long>>() {
				}.getType());

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
		String json = gson.toJson(jugadores, new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType());
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
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<JugadorDTO>>() {
				}.getType());

		// Cargar territorios
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(),
				new TypeToken<MyLinkedList<TerritorioDTO>>() {
				}.getType());

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

		// 2️⃣ Limpiar la mano del jugador (opcional si quieres devolver las cartas al
		// service)
		if (jugador.getCartas() != null) {
			jugador.setCartas(new MyLinkedList<>());
		}

		// 3️⃣ Eliminar jugador del orden de turno
		eliminarNodo(jugadores, nodoJugador);

		// 4️⃣ Ajustar jugador actual
		if (partida.getJugadorActualId() != null && partida.getJugadorActualId().equals(jugadorId)) {

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
		if (lista == null || nodo == null)
			return;

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
			return gson.fromJson(partida.getTerritoriosJSON(), new TypeToken<MyLinkedList<TerritorioDTO>>() {
			}.getType());
		}

		// Si no hay JSON, significa que es la primera vez → cargar territorios base
		MyLinkedList<TerritorioDTO> territoriosBase = territorioService.obtenerTodos();

		// Guardarlos en partida para futuras cargas
		partida.setTerritoriosJSON(gson.toJson(territoriosBase));
		partidaRepository.save(partida);

		return territoriosBase;
	}

	// Verifica si ya movió tropas en la fase 3 de esta partida
	public boolean yaMovioTropasFase3(Long jugadorId, Long partidaId) {
		String key = jugadorId + "-" + partidaId;
		Node<String> n = movimientosFase3.getFirst();
		while (n != null) {
			if (n.getInfo().equals(key))
				return true;
			n = n.getNext();
		}
		return false;
	}

	// Marca que ya movió tropas
	public void marcarMovimientoFase3(Long jugadorId, Long partidaId) {
		String key = jugadorId + "-" + partidaId;
		movimientosFase3.addLast(key);
	}

	// Reinicia movimientos de fase 3 para una partida (al iniciar nueva ronda)
	public void reiniciarMovimientosFase3(Long partidaId) {
		MyLinkedList<String> nuevaLista = new MyLinkedList<>();
		Node<String> n = movimientosFase3.getFirst();
		while (n != null) {
			String key = n.getInfo();
			if (!key.endsWith("-" + partidaId)) {
				nuevaLista.addLast(key);
			}
			n = n.getNext();
		}
		movimientosFase3 = nuevaLista;
	}

	public Long verificarFinPartida(Partida partida) {
		MyLinkedList<TerritorioDTO> territorios = cargarTerritorios(partida);

		if (territorios.isEmpty())
			return null;

		Long jugadorGanadorId = territorios.getFirst().getInfo().getIdJugador();

		Node<TerritorioDTO> nodo = territorios.getFirst();
		while (nodo != null) {
			if (!nodo.getInfo().getIdJugador().equals(jugadorGanadorId)) {
				return null; // aún hay territorios de otro jugador
			}
			nodo = nodo.getNext();
		}

		// Todos los territorios pertenecen a un mismo jugador
		partida.setGanadorId(jugadorGanadorId);
		partida.setFinalizada(true);
		partida.setFechaFin(LocalDateTime.now());
		partidaRepository.save(partida); // guardar cambios en la BD

		// ----------------------------
		// Generar y enviar PDF
		// ----------------------------
		try {
			MyLinkedList<Jugador> jugadores = obtenerJugadoresPartida(partida);
			for (int i = 0; i < jugadores.size(); i++) {
				Jugador j = jugadores.getPos(i).getInfo();
				if (j.getCorreo() != null && !j.getCorreo().isEmpty()) {
					// Enviar correo, pasando la lista de jugadores al EmailService
					emailService.enviarCorreoFinalPartida(j.getCorreo(), partida, jugadores);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); // manejar excepción si falla el envío, no bloquear la finalización
		}

		return jugadorGanadorId;
	}

	@Transactional
	public PartidaDTO retomarPartida(String codigoHash) {
		// 1️⃣ Buscar la partida por código
		Partida partida = partidaRepository.findByCodigoHash(codigoHash)
				.orElseThrow(() -> new RuntimeException("No existe la partida con el código dado"));

		// 2️⃣ Reconstruir lista de jugadores
		Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType();
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), jugadoresType);

		// 3️⃣ Reconstruir territorios
		Type territoriosType = new TypeToken<MyLinkedList<TerritorioDTO>>() {
		}.getType();
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(), territoriosType);

		// 4️⃣ Reconstruir mazo de cartas si existe
		MyLinkedList<CartaDTO> mazo = new MyLinkedList<>();
		if (partida.getMazoCartasJSON() != null && !partida.getMazoCartasJSON().isBlank()) {
			Type mazoType = new TypeToken<MyLinkedList<CartaDTO>>() {
			}.getType();
			mazo = gson.fromJson(partida.getMazoCartasJSON(), mazoType);
		}

		// 5️⃣ Construir DTO de partida para frontend
		PartidaDTO dto = new PartidaDTO();
		dto.setId(partida.getId());
		dto.setCodigoHash(partida.getCodigoHash());
		dto.setIniciada(partida.isIniciada());
		dto.setFinalizada(partida.isFinalizada());
		dto.setJugadorActualId(partida.getJugadorActualId());
		dto.setGanadorId(partida.getGanadorId());
		dto.setTerritoriosJSON(partida.getTerritoriosJSON());
		dto.setMazoCartasJSON(partida.getMazoCartasJSON());
		dto.setJugadoresOrdenTurnoJSON(partida.getJugadoresOrdenTurnoJSON());
		dto.setFechaInicio(partida.getFechaInicio());
		dto.setFechaFin(partida.getFechaFin());

		return dto;
	}

	public Partida retornarPartidaEntidad(Long id) {
		return partidaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("No existe la partida con id: " + id));
	}

	public MyLinkedList<Partida> verTodasLasPartidas() {

		MyLinkedList<Partida> resultado = new MyLinkedList<>();

		// findAll() retorna List<Partida>, debes convertirlo
		for (Partida p : partidaRepository.findAll()) {
			resultado.add(p);
		}

		return resultado;
	}

	public Partida obtenerPartidaPorId(int id) {
		return partidaRepository.findById((long) id).orElse(null);
	}

	// eliminar
	@Transactional
	public PartidaDTO finalizarPartidaForzada(Long partidaId) {

		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida con ID: " + partidaId));

		// ---------------------------
		// Finalización forzada
		// ---------------------------
		partida.setFinalizada(true);
		partida.setFechaFin(LocalDateTime.now());

		partidaRepository.save(partida);

		return modelMapper.map(partida, PartidaDTO.class);
	}

	public MyLinkedList<Jugador> obtenerJugadoresPartida(Partida partida) {
		MyLinkedList<Jugador> jugadores = new MyLinkedList<>();

		if (partida.getJugadoresOrdenTurnoJSON() == null || partida.getJugadoresOrdenTurnoJSON().isEmpty()) {
			return jugadores;
		}

		// Deserializar como LinkedTreeMap porque Gson no puede inferir Jugador
		// directamente
		Type tipoLista = new TypeToken<MyLinkedList<LinkedTreeMap>>() {
		}.getType();
		MyLinkedList<LinkedTreeMap> temp = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), tipoLista);

		for (int i = 0; i < temp.size(); i++) {
			LinkedTreeMap map = temp.getPos(i).getInfo();
			Jugador j = new Jugador();

			// Mapear los campos del JSON a Jugador
			j.setId(((Double) map.get("id")).longValue()); // Gson convierte números a Double
			j.setNombre((String) map.get("nombre"));
			j.setCorreo((String) map.getOrDefault("correo", ""));
			j.setColor((String) map.get("color"));
			j.setTropasDisponibles(((Double) map.getOrDefault("tropasDisponibles", 0.0)).intValue());
			j.setTerritoriosControlados(((Double) map.getOrDefault("territoriosControlados", 0.0)).intValue());
			j.setActivo((Boolean) map.getOrDefault("activo", true));
			// Si necesitas cartas u otros campos, puedes mapearlos aquí también

			jugadores.add(j);
		}

		return jugadores;
	}

	public byte[] generarZipFinalPartida(int partidaId) throws Exception {
		Partida partida = obtenerPartidaPorId(partidaId);
		if (partida == null || !Boolean.TRUE.equals(partida.isFinalizada())) {
			return null;
		}

		MyLinkedList<Jugador> jugadores = obtenerJugadoresPartida(partida);
		byte[] pdfBytes = pdfService.generarPDFPartida(partida, jugadores);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ZipOutputStream zos = new ZipOutputStream(baos)) {
			ZipEntry entry = new ZipEntry("Resultados Partida" + ".pdf");
			zos.putNextEntry(entry);
			zos.write(pdfBytes);
			zos.closeEntry();
		}

		return baos.toByteArray();
	}

	public Long obtenerPartidaActual() {
		Partida partida = partidaRepository.findFirstByIniciadaTrueAndFinalizadaFalse();

		if (partida == null) {
			return null;
		}

		return partida.getId();
	}

}
