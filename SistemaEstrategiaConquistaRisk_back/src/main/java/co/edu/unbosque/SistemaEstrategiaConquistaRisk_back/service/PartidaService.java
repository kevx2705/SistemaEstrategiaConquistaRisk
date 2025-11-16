package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.LinkedTreeMap;

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
import java.io.ByteArrayOutputStream;

/**
 * Servicio que gestiona la lógica de las partidas del juego Risk. Proporciona
 * funcionalidades para crear, inicializar, retomar y finalizar partidas, así
 * como gestionar las fases del juego, los movimientos de tropas y el canje de
 * cartas.
 */
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

	/**
	 * Constructor de la clase {@code PartidaService}. Inicializa la lista de
	 * movimientos para la fase 3.
	 */
	public PartidaService() {
		movimientosFase3 = new MyLinkedList<>();
	}

	/**
	 * Crea una nueva partida con un anfitrión y otros jugadores.
	 *
	 * @param anfitrionId  Identificador del anfitrión.
	 * @param otrosNombres Nombres de los otros jugadores.
	 * @return {@code Partida} La partida creada.
	 * @throws RuntimeException Si el anfitrión no existe o ya está en una partida.
	 */
	public Partida crearPartida(Long anfitrionId, String[] otrosNombres) {
		Jugador anfitrion = jugadorRepository.findById(anfitrionId)
				.orElseThrow(() -> new RuntimeException("No existe el jugador anfitrión"));

		if (anfitrion.isActivo()) {
			throw new RuntimeException("El anfitrión ya está participando en otra partida.");
		}

		anfitrion.setColor("ROJO");
		jugadorRepository.save(anfitrion);
		jugadorService.activarJugador(anfitrion.getId());

		Partida partida = new Partida();
		partida.setIniciada(true);
		partida.setFinalizada(false);
		partida.setFechaInicio(LocalDateTime.now());
		partida.setCodigoHash(UUID.randomUUID().toString());
		partida.setJugadorActualId(anfitrionId);

		MyLinkedList<JugadorDTO> jugadoresDTO = new MyLinkedList<>();
		JugadorDTO dtoAnfitrion = modelMapper.map(anfitrion, JugadorDTO.class);
		dtoAnfitrion.setTropasDisponibles(0);
		dtoAnfitrion.setTerritoriosControlados(0);
		dtoAnfitrion.setCartas(new MyLinkedList<>());
		jugadoresDTO.addLast(dtoAnfitrion);

		String[] colores = { "ROJO", "AZUL", "VERDE", "AMARILLO", "NEGRO", "BLANCO" };
		if (otrosNombres.length + 1 > colores.length) {
			throw new RuntimeException("Hay más jugadores que colores disponibles.");
		}

		int colorIndex = 1;
		for (String nombre : otrosNombres) {
			if (nombre == null || nombre.trim().isEmpty())
				continue;
			Jugador nuevo = jugadorService.crearJugadorTemporal(nombre, colores[colorIndex++]);
			JugadorDTO dto = modelMapper.map(nuevo, JugadorDTO.class);
			dto.setTropasDisponibles(0);
			dto.setTerritoriosControlados(0);
			dto.setCartas(new MyLinkedList<>());
			jugadoresDTO.addLast(dto);
		}

		String jsonJugadores = JsonUtil.toJson(jugadoresDTO);
		partida.setJugadoresOrdenTurnoJSON(jsonJugadores);

		String mazoJSON = cartaService.inicializarMazoParaPartida();
		partida.setMazoCartasJSON(mazoJSON);

		return partidaRepository.save(partida);
	}

	/**
	 * Crea un DTO de partida a partir de una partida creada.
	 *
	 * @param anfitrionId  Identificador del anfitrión.
	 * @param otrosNombres Nombres de los otros jugadores.
	 * @return {@code PartidaDTO} El DTO de la partida creada.
	 */
	public PartidaDTO crearPartidaDTO(Long anfitrionId, String[] otrosNombres) {
		Partida partida = crearPartida(anfitrionId, otrosNombres);
		PartidaDTO dto = modelMapper.map(partida, PartidaDTO.class);
		return dto;
	}
	
	/**
	 * Obtiene la lista de jugadores de una partida específica, en formato DTO.
	 *
	 * @param partidaId Identificador de la partida.
	 * @return MyLinkedList<JugadorDTO> con la información de los jugadores.
	 * @throws RuntimeException Si la partida no existe o no tiene jugadores.
	 */
	public MyLinkedList<JugadorDTO> obtenerJugadoresPorPartida(Long partidaId) {
	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
	    if (partida.getJugadoresOrdenTurnoJSON() == null || partida.getJugadoresOrdenTurnoJSON().isEmpty()) {
	        throw new RuntimeException("La partida no tiene jugadores definidos");
	    }
	    Type tipoLista = new TypeToken<MyLinkedList<JugadorDTO>>() {}.getType();
	    MyLinkedList<JugadorDTO> jugadoresDTO = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), tipoLista);
	    MyLinkedList<JugadorDTO> respuesta = new MyLinkedList<>();
	    Node<JugadorDTO> current = jugadoresDTO.getFirst();
	    while (current != null) {
	        JugadorDTO jDto = current.getInfo();
	        JugadorDTO jRespuesta = new JugadorDTO();
	        jRespuesta.setId(jDto.getId());
	        jRespuesta.setNombre(jDto.getNombre());
	        jRespuesta.setColor(jDto.getColor());
	        jRespuesta.setTropasDisponibles(jDto.getTropasDisponibles());
	        jRespuesta.setTerritoriosControlados(jDto.getTerritoriosControlados());
	        respuesta.addLast(jRespuesta);
	        current = current.getNext();
	    }
	    return respuesta;
	}


	/**
	 * Inicializa el juego con tropas iniciales y prepara los territorios.
	 *
	 * @param partidaId Identificador de la partida.
	 */
	@Transactional
	public void inicializarJuego(Long partidaId) {

	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("No existe la partida"));

	    MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);
	    int numJugadores = ordenJugadores.size();

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

	    MyLinkedList<TerritorioDTO> territorios = cargarTerritorios(partida);
	    for (int i = 0; i < territorios.size(); i++) {
	        TerritorioDTO t = territorios.getPos(i).getInfo();
	        t.setTropas(0);
	        t.setIdJugador(0L);
	    }
	    partida.setTerritoriosJSON(gson.toJson(territorios));

	    MyLinkedList<JugadorDTO> jugadores = JsonUtil.fromJson(
	            partida.getJugadoresOrdenTurnoJSON(),
	            JugadorDTO.class
	    );

	    for (int i = 0; i < jugadores.size(); i++) {
	        JugadorDTO dto = jugadores.getPos(i).getInfo();
	        dto.setTropasDisponibles(tropasIniciales);
	        dto.setTerritoriosControlados(0); 
	        dto.setCartas(new MyLinkedList<>()); 
	    }

	    partida.setJugadoresOrdenTurnoJSON(JsonUtil.toJson(jugadores));

	    partida.setJugadorActualId(ordenJugadores.getPos(0).getInfo());

	    partidaRepository.save(partida);
	}


	/**
	 * Permite a un jugador reclamar un territorio durante la fase de
	 * inicialización.
	 *
	 * @param partidaId    Identificador de la partida.
	 * @param jugadorId    Identificador del jugador.
	 * @param territorioId Identificador del territorio a reclamar.
	 * @throws RuntimeException Si no es el turno del jugador, el territorio no
	 *                          existe o ya está asignado.
	 */
	@Transactional
	public void reclamarTerritorio(Long partidaId, Long jugadorId, Long territorioId) {

	    System.out.println("=== Intentando reclamar territorio ===");
	    System.out.println("Partida ID: " + partidaId);
	    System.out.println("Jugador ID: " + jugadorId);
	    System.out.println("Territorio ID: " + territorioId);

	    // Buscar partida
	    Partida partida = partidaRepository.findById(partidaId)
	            .orElseThrow(() -> new RuntimeException("❌ No existe la partida con ID: " + partidaId));

	    System.out.println("✔ Partida encontrada. Jugador actual: " + partida.getJugadorActualId());

	    // Verificar turno
	    if (!partida.getJugadorActualId().equals(jugadorId)) {
	        throw new RuntimeException("❌ No es el turno del jugador con ID: " + jugadorId);
	    }

	    // Cargar orden de jugadores
	    MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);

	    // Cargar territorios
	    MyLinkedList<TerritorioDTO> territorios = cargarTerritorios(partida);

	    System.out.println("✔ Territorios cargados: " + territorios.size());

	    TerritorioDTO territorioElegido = null;

	    // Buscar territorio
	    for (int i = 0; i < territorios.size(); i++) {
	        TerritorioDTO t = territorios.getPos(i).getInfo();
	        if (t.getId().equals(territorioId)) {
	            territorioElegido = t;
	            break;
	        }
	    }
	    
	    // Territorio no existe
	    if (territorioElegido == null) {
	        throw new RuntimeException("❌ Territorio con ID " + territorioId + " no encontrado.");
	    }

	    System.out.println("✔ Territorio encontrado. Jugador asignado actual: " + territorioElegido.getIdJugador());

	    // Ya reclamado
	    if (!territorioElegido.getIdJugador().equals(0L)) {
	        throw new RuntimeException("❌ El territorio ya está asignado a un jugador.");
	    }

	    // Reclamar territorio
	    territorioElegido.setIdJugador(jugadorId);
	    territorioElegido.setTropas(1);

	    System.out.println("✔ Territorio reclamado exitosamente.");

	    // Actualizar jugador
	    jugadorService.quitarTropas(jugadorId, 1);
	    jugadorService.agregarTerritorio(jugadorId);

	    System.out.println("✔ Tropas actualizadas y territorio agregado al jugador.");

	    // Cambiar turno
	    Long siguiente = obtenerSiguienteJugador(ordenJugadores, jugadorId);
	    partida.setJugadorActualId(siguiente);

	    System.out.println("✔ Siguiente jugador: " + siguiente);

	    // Guardar cambios
	    partida.setTerritoriosJSON(gson.toJson(territorios));
	    partidaRepository.save(partida);

	    System.out.println("✔ Cambios guardados en la partida.");
	    System.out.println("=== Reclamo de territorio finalizado ===");
	}


	/**
	 * Inicia la fase de colocación de tropas iniciales.
	 *
	 * @param partidaId Identificador de la partida.
	 */
	@Transactional
	public void iniciarFaseColocacionTropasInicial(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));
		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);
		partidaRepository.save(partida);
	}

	/**
	 * Inicia la fase de refuerzo de tropas.
	 *
	 * @param partidaId Identificador de la partida.
	 */
	@Transactional
	public void iniciarFaseRefuerzo(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));
		MyLinkedList<Long> ordenJugadores = cargarOrdenJugadores(partida);
		for (int i = 0; i < ordenJugadores.size(); i++) {
			Long idJugador = ordenJugadores.getPos(i).getInfo();
			verificarCanjeCartas(partidaId, idJugador);
			Jugador jugador = jugadorService.obtenerJugadorPorId(idJugador);
			int refuerzos = jugador.getTerritoriosControlados() / 3;
			if (refuerzos < 3)
				refuerzos = 3;
			refuerzos += calcularBonusContinentes(idJugador);
			jugadorService.agregarTropas(idJugador, refuerzos);
		}
		partidaRepository.save(partida);
	}

	/**
	 * Coloca tropas en un territorio específico.
	 *
	 * @param partida          Partida en la que se colocan las tropas.
	 * @param jugadorId        Identificador del jugador.
	 * @param nombreTerritorio Nombre del territorio.
	 * @param cantidad         Cantidad de tropas a colocar.
	 * @throws RuntimeException Si la cantidad es inválida, el territorio no existe
	 *                          o no pertenece al jugador.
	 */
	public void colocarTropa(Partida partida, Long jugadorId, String nombreTerritorio, int cantidad) {
		if (cantidad <= 0) {
			throw new RuntimeException("La cantidad de tropas debe ser mayor que 0.");
		}
		Type listType = new TypeToken<MyLinkedList<TerritorioDTO>>() {
		}.getType();
		if (partida.getTerritoriosJSON() == null || partida.getTerritoriosJSON().isBlank()) {
			throw new RuntimeException("No existen territorios cargados en la partida.");
		}
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(), listType);
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
		if (!territorioObjetivo.getIdJugador().equals(jugadorId)) {
			throw new RuntimeException("No puedes colocar tropas en un territorio que no te pertenece.");
		}
		int tropasActuales = territorioObjetivo.getTropas();
		territorioObjetivo.setTropas(tropasActuales + cantidad);
		String nuevosTerritoriosJSON = gson.toJson(territorios, listType);
		partida.setTerritoriosJSON(nuevosTerritoriosJSON);
		partidaRepository.save(partida);
	}

	/**
	 * Realiza un ataque entre territorios.
	 *
	 * @param partidaId            Identificador de la partida.
	 * @param atacanteId           Identificador del jugador atacante.
	 * @param territorioAtacanteId Identificador del territorio atacante.
	 * @param territorioDefensorId Identificador del territorio defensor.
	 * @return {@code ResultadoAtaqueDTO} Resultado del ataque.
	 */
	public ResultadoAtaqueDTO atacar(Long partidaId, Long atacanteId, Long territorioAtacanteId,
			Long territorioDefensorId) {
		ResultadoAtaqueDTO resultado = ataqueService.atacar(partidaId, atacanteId, territorioAtacanteId,
				territorioDefensorId);
		return resultado;
	}

	/**
	 * Finaliza el turno de un jugador y pasa al siguiente.
	 *
	 * @param partidaId Identificador de la partida.
	 * @throws RuntimeException Si la partida no existe o no hay jugadores.
	 */
	@Transactional
	public void finalizarTurno(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("Partida no existe"));
		Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType();
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), jugadoresType);
		if (jugadores == null || jugadores.size() == 0) {
			throw new RuntimeException("No hay jugadores en la partida");
		}
		Node<JugadorDTO> nodoJugadorActual = getNodoPorId(jugadores, partida.getJugadorActualId());
		if (nodoJugadorActual == null) {
			throw new RuntimeException("Jugador actual no encontrado en la lista");
		}
		JugadorDTO jugadorActual = nodoJugadorActual.getInfo();
		if (partida.getTerritoriosJSON() == null || partida.getTerritoriosJSON().isBlank()) {
			throw new RuntimeException("La partida no tiene territorios cargados.");
		}
		Type territoriosType = new TypeToken<MyLinkedList<TerritorioDTO>>() {
		}.getType();
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(), territoriosType);
		int territoriosActuales = 0;
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getIdJugador() != null && t.getIdJugador().equals(jugadorActual.getId())) {
				territoriosActuales++;
			}
		}
		if (territoriosActuales > jugadorActual.getTerritoriosControlados()) {
			CartaDTO nuevaCartaDTO = cartaService.robarCarta();
			if (nuevaCartaDTO != null) {
				if (jugadorActual.getCartas() == null) {
					jugadorActual.setCartas(new MyLinkedList<>());
				}
				jugadorActual.getCartas().add(nuevaCartaDTO);
			}
		}
		jugadorActual.setTerritoriosControlados(territoriosActuales);
		Node<JugadorDTO> nodoSiguiente = nodoJugadorActual.getNext();
		if (nodoSiguiente == null) {
			nodoSiguiente = jugadores.getFirst();
		}
		partida.setJugadorActualId(nodoSiguiente.getInfo().getId());
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores, jugadoresType));
		partidaRepository.save(partida);
	}

	/**
	 * Mueve tropas entre territorios en la fase 3.
	 *
	 * @param partidaId         Identificador de la partida.
	 * @param jugadorId         Identificador del jugador.
	 * @param territorioOrigen  Nombre del territorio de origen.
	 * @param territorioDestino Nombre del territorio de destino.
	 * @param cantidad          Cantidad de tropas a mover.
	 * @throws RuntimeException Si no es el turno del jugador, los territorios no
	 *                          existen o no están conectados.
	 */
	@Transactional
	public void moverTropasFase3(Long partidaId, Long jugadorId, String territorioOrigen, String territorioDestino,
			int cantidad) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));
		if (!partida.getJugadorActualId().equals(jugadorId)) {
			throw new RuntimeException("No es tu turno");
		}
		MyLinkedList<TerritorioDTO> territorios = cargarTerritorios(partida);
		TerritorioDTO origen = buscarTerritorio(territorios, territorioOrigen);
		TerritorioDTO destino = buscarTerritorio(territorios, territorioDestino);
		if (origen == null || destino == null) {
			throw new RuntimeException("Territorio de origen o destino no encontrado");
		}
		if (!origen.getIdJugador().equals(jugadorId) || !destino.getIdJugador().equals(jugadorId)) {
			throw new RuntimeException("Solo puedes mover tropas entre tus territorios");
		}
		if (cantidad <= 0 || cantidad >= origen.getTropas()) {
			throw new RuntimeException("Debes dejar al menos 1 tropa en el territorio de origen");
		}
		if (!mapaTerritorio.existeCamino(origen.getId(), destino.getId(), jugadorId)) {
			throw new RuntimeException("Los territorios no están conectados a través de tus territorios");
		}
		if (yaMovioTropasFase3(jugadorId, partidaId)) {
			throw new RuntimeException("Solo puedes mover tropas una vez por turno");
		}
		origen.setTropas(origen.getTropas() - cantidad);
		destino.setTropas(destino.getTropas() + cantidad);
		partida.setTerritoriosJSON(gson.toJson(territorios));
		partidaRepository.save(partida);
		marcarMovimientoFase3(jugadorId, partidaId);
	}

	/**
	 * Busca un territorio por su nombre.
	 *
	 * @param territorios Lista de territorios.
	 * @param nombre      Nombre del territorio.
	 * @return {@code TerritorioDTO} El territorio encontrado, o null si no existe.
	 */
	private TerritorioDTO buscarTerritorio(MyLinkedList<TerritorioDTO> territorios, String nombre) {
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getNombre().equalsIgnoreCase(nombre)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Canjea cartas por tropas.
	 *
	 * @param partidaId Identificador de la partida.
	 * @param jugadorId Identificador del jugador.
	 * @param idsCartas Lista de identificadores de las cartas a canjear.
	 * @return int Cantidad de tropas otorgadas por el canje.
	 * @throws RuntimeException Si la partida no existe, el jugador no está en la
	 *                          partida o la combinación es inválida.
	 */
	@Transactional
	public int canjearCartas(Long partidaId, Long jugadorId, MyLinkedList<Long> idsCartas) {
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
		for (int i = 0; i < jugador.getCartas().size(); i++) {
			CartaDTO dto = jugador.getCartas().getPos(i).getInfo();
			Carta c = modelMapper.map(dto, Carta.class);
			mano.addLast(c);
		}
		if (!cartaService.combinacionValida(idsCartas, mano))
			throw new RuntimeException("Combinación de cartas inválida");
		int bonus = cartaService.canjearCartas(idsCartas, mano);
		MyLinkedList<CartaDTO> nuevaMano = new MyLinkedList<>();
		Node<Carta> nodo = mano.getFirst();
		while (nodo != null) {
			nuevaMano.addLast(modelMapper.map(nodo.getInfo(), CartaDTO.class));
			nodo = nodo.getNext();
		}
		jugador.setCartas(nuevaMano);
		Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType();
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores, jugadoresType));
		partidaRepository.save(partida);
		return bonus;
	}

	/**
	 * Verifica si un jugador puede canjear cartas.
	 *
	 * @param partidaId Identificador de la partida.
	 * @param jugadorId Identificador del jugador.
	 * @throws RuntimeException Si el jugador tiene 5 o más cartas y no ha canjeado.
	 */
	@Transactional
	public void verificarCanjeCartas(Long partidaId, Long jugadorId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));
		MyLinkedList<Long> listaIds = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<Long>>() {
				}.getType());
		MyLinkedList<JugadorDTO> jugadores = cargarJugadoresDesdeIds(listaIds);
		Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
		if (nodoJugador == null) {
			throw new RuntimeException("Jugador no encontrado en la partida");
		}
		JugadorDTO jugador = nodoJugador.getInfo();
		int numCartas = jugador.getCartas() != null ? jugador.getCartas().size() : 0;
		if (numCartas >= 5) {
			throw new RuntimeException("Tienes 5 o más cartas, debes canjear antes de continuar");
		}
		guardarJugadores(partida, jugadores);
		partidaRepository.save(partida);
	}

	/**
	 * Obtiene el nodo de un jugador en una lista enlazada por su identificador.
	 *
	 * @param lista Lista enlazada de jugadores.
	 * @param id    Identificador del jugador.
	 * @return {@code Node<JugadorDTO>} Nodo del jugador, o null si no se encuentra.
	 */
	private Node<JugadorDTO> getNodoPorId(MyLinkedList<JugadorDTO> lista, Long id) {
		Node<JugadorDTO> current = lista.getFirst();
		while (current != null) {
			if (current.getInfo().getId().equals(id))
				return current;
			current = current.getNext();
		}
		return null;
	}

	/**
	 * Selecciona un territorio libre para un jugador.
	 *
	 * @param idJugador   Identificador del jugador.
	 * @param territorios Lista de territorios.
	 * @return {@code TerritorioDTO} Territorio libre encontrado, o null si no hay
	 *         territorios libres.
	 */
	private TerritorioDTO seleccionarTerritorioJugador(Long idJugador, MyLinkedList<TerritorioDTO> territorios) {
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getIdJugador() == 0L) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Carga el orden de jugadores de una partida.
	 *
	 * @param partida Partida de la que cargar el orden de jugadores.
	 * @return {@code MyLinkedList<Long>} Lista enlazada con los identificadores de
	 *         los jugadores.
	 * @throws RuntimeException Si la partida no tiene orden de jugadores definido.
	 */
	private MyLinkedList<Long> cargarOrdenJugadores(Partida partida) {
		if (partida.getJugadoresOrdenTurnoJSON() == null || partida.getJugadoresOrdenTurnoJSON().isEmpty()) {
			throw new RuntimeException("La partida no tiene orden de jugadores definido");
		}
		Type typeJugadores = TypeToken.getParameterized(MyLinkedList.class, JugadorDTO.class).getType();
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), typeJugadores);
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
	 * Calcula el bonus de tropas por continentes controlados.
	 *
	 * @param jugadorId Identificador del jugador.
	 * @return int Bonus de tropas por continentes controlados.
	 */
	private int calcularBonusContinentes(Long jugadorId) {
		int bonus = 0;
		MyLinkedList<ContinenteDTO> continentes = continenteService.obtenerTodos();
		for (int i = 0; i < continentes.size(); i++) {
			ContinenteDTO cont = continentes.getPos(i).getInfo();
			boolean controla = territorioService.jugadorControlaContinente(jugadorId, cont.getId());
			if (controla) {
				bonus += continenteService.getBonusPorContinente(cont.getNombre());
			}
		}
		return bonus;
	}

	/**
	 * Obtiene el siguiente jugador en el orden de jugadores.
	 *
	 * @param orden         Lista enlazada con el orden de jugadores.
	 * @param jugadorActual Identificador del jugador actual.
	 * @return {@code Long} Identificador del siguiente jugador.
	 * @throws RuntimeException Si el jugador no se encuentra en el orden.
	 */
	private Long obtenerSiguienteJugador(MyLinkedList<Long> orden, Long jugadorActual) {
		for (int i = 0; i < orden.size(); i++) {
			if (orden.getPos(i).getInfo().equals(jugadorActual)) {
				int sig = (i + 1) % orden.size();
				return orden.getPos(sig).getInfo();
			}
		}
		throw new RuntimeException("Jugador no encontrado en el orden");
	}

	/**
	 * Guarda la lista de jugadores en la partida.
	 *
	 * @param partida   Partida en la que guardar los jugadores.
	 * @param jugadores Lista enlazada de jugadores.
	 */
	private void guardarJugadores(Partida partida, MyLinkedList<JugadorDTO> jugadores) {
		String json = gson.toJson(jugadores, new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType());
		partida.setJugadoresOrdenTurnoJSON(json);
		partidaRepository.save(partida);
	}

	/**
	 * Carga los jugadores de una partida a partir de sus identificadores.
	 *
	 * @param ids Lista enlazada de identificadores de jugadores.
	 * @return {@code MyLinkedList<JugadorDTO>} Lista enlazada de jugadores en
	 *         formato DTO.
	 */
	private MyLinkedList<JugadorDTO> cargarJugadoresDesdeIds(MyLinkedList<Long> ids) {
		MyLinkedList<JugadorDTO> jugadores = new MyLinkedList<>();
		for (int i = 0; i < ids.size(); i++) {
			Long id = ids.getPos(i).getInfo();
			Jugador entidad = jugadorRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Jugador con id " + id + " no existe"));
			JugadorDTO dto = modelMapper.map(entidad, JugadorDTO.class);
			if (dto.getCartas() == null) {
				dto.setCartas(new MyLinkedList<>());
			}
			jugadores.addLast(dto);
		}
		return jugadores;
	}

	/**
	 * Elimina un jugador de una partida.
	 *
	 * @param partidaId Identificador de la partida.
	 * @param jugadorId Identificador del jugador a eliminar.
	 * @throws RuntimeException Si la partida o el jugador no existen.
	 */
	@Transactional
	public void eliminarJugador(Long partidaId, Long jugadorId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("Partida no existe"));
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<JugadorDTO>>() {
				}.getType());
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(),
				new TypeToken<MyLinkedList<TerritorioDTO>>() {
				}.getType());
		Node<JugadorDTO> nodoJugador = getNodoPorId(jugadores, jugadorId);
		if (nodoJugador == null) {
			throw new RuntimeException("Jugador no existe en la partida");
		}
		JugadorDTO jugador = nodoJugador.getInfo();
		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.getPos(i).getInfo();
			if (t.getIdJugador() != null && t.getIdJugador().equals(jugadorId)) {
				t.setIdJugador(null);
				t.setTropas(0);
			}
		}
		if (jugador.getCartas() != null) {
			jugador.setCartas(new MyLinkedList<>());
		}
		eliminarNodo(jugadores, nodoJugador);
		if (partida.getJugadorActualId() != null && partida.getJugadorActualId().equals(jugadorId)) {
			if (jugadores.size() > 0) {
				partida.setJugadorActualId(jugadores.getPos(0).getInfo().getId());
			} else {
				partida.setJugadorActualId(null);
			}
		}
		partida.setJugadoresOrdenTurnoJSON(gson.toJson(jugadores));
		partida.setTerritoriosJSON(gson.toJson(territorios));
		partidaRepository.save(partida);
	}

	/**
	 * Elimina un nodo de una lista enlazada.
	 *
	 * @param lista Lista enlazada.
	 * @param nodo  Nodo a eliminar.
	 */
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

	/**
	 * Carga los territorios de una partida.
	 *
	 * @param partida Partida de la que cargar los territorios.
	 * @return {@code MyLinkedList<TerritorioDTO>} Lista enlazada de territorios en
	 *         formato DTO.
	 */
	private MyLinkedList<TerritorioDTO> cargarTerritorios(Partida partida) {
		if (partida.getTerritoriosJSON() != null && !partida.getTerritoriosJSON().isBlank()) {
			return gson.fromJson(partida.getTerritoriosJSON(), new TypeToken<MyLinkedList<TerritorioDTO>>() {
			}.getType());
		}
		MyLinkedList<TerritorioDTO> territoriosBase = territorioService.obtenerTodos();
		partida.setTerritoriosJSON(gson.toJson(territoriosBase));
		partidaRepository.save(partida);
		return territoriosBase;
	}

	/**
	 * Verifica si un jugador ya movió tropas en la fase 3.
	 *
	 * @param jugadorId Identificador del jugador.
	 * @param partidaId Identificador de la partida.
	 * @return boolean Verdadero si el jugador ya movió tropas, falso en caso
	 *         contrario.
	 */
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

	/**
	 * Marca que un jugador ha movido tropas en la fase 3.
	 *
	 * @param jugadorId Identificador del jugador.
	 * @param partidaId Identificador de la partida.
	 */
	public void marcarMovimientoFase3(Long jugadorId, Long partidaId) {
		String key = jugadorId + "-" + partidaId;
		movimientosFase3.addLast(key);
	}

	/**
	 * Reinicia los movimientos de la fase 3 para una partida.
	 *
	 * @param partidaId Identificador de la partida.
	 */
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

	/**
	 * Verifica si la partida ha finalizado y determina al ganador.
	 *
	 * @param partida Partida a verificar.
	 * @return {@code Long} Identificador del ganador, o null si la partida no ha
	 *         finalizado.
	 */
	public Long verificarFinPartida(Partida partida) {
		MyLinkedList<TerritorioDTO> territorios = cargarTerritorios(partida);
		if (territorios.isEmpty())
			return null;
		Long jugadorGanadorId = territorios.getFirst().getInfo().getIdJugador();
		Node<TerritorioDTO> nodo = territorios.getFirst();
		while (nodo != null) {
			if (!nodo.getInfo().getIdJugador().equals(jugadorGanadorId)) {
				return null;
			}
			nodo = nodo.getNext();
		}
		partida.setGanadorId(jugadorGanadorId);
		partida.setFinalizada(true);
		partida.setFechaFin(LocalDateTime.now());
		partidaRepository.save(partida);
		try {
			MyLinkedList<Jugador> jugadores = obtenerJugadoresPartida(partida);
			for (int i = 0; i < jugadores.size(); i++) {
				Jugador j = jugadores.getPos(i).getInfo();
				if (j.getCorreo() != null && !j.getCorreo().isEmpty()) {
					emailService.enviarCorreoFinalPartida(j.getCorreo(), partida, jugadores);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jugadorGanadorId;
	}

	/**
	 * Retoma una partida a partir de su código hash.
	 *
	 * @param codigoHash Código hash de la partida.
	 * @return {@code PartidaDTO} DTO de la partida retomada.
	 * @throws RuntimeException Si no existe la partida con el código dado.
	 */
	@Transactional
	public PartidaDTO retomarPartida(String codigoHash) {
		Partida partida = partidaRepository.findByCodigoHash(codigoHash)
				.orElseThrow(() -> new RuntimeException("No existe la partida con el código dado"));
		Type jugadoresType = new TypeToken<MyLinkedList<JugadorDTO>>() {
		}.getType();
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), jugadoresType);
		Type territoriosType = new TypeToken<MyLinkedList<TerritorioDTO>>() {
		}.getType();
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(), territoriosType);
		MyLinkedList<CartaDTO> mazo = new MyLinkedList<>();
		if (partida.getMazoCartasJSON() != null && !partida.getMazoCartasJSON().isBlank()) {
			Type mazoType = new TypeToken<MyLinkedList<CartaDTO>>() {
			}.getType();
			mazo = gson.fromJson(partida.getMazoCartasJSON(), mazoType);
		}
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

	/**
	 * Retorna la entidad de una partida por su identificador.
	 *
	 * @param id Identificador de la partida.
	 * @return {@code Partida} La partida encontrada.
	 * @throws RuntimeException Si no existe la partida con el identificador dado.
	 */
	public Partida retornarPartidaEntidad(Long id) {
		return partidaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("No existe la partida con id: " + id));
	}

	/**
	 * Obtiene todas las partidas registradas.
	 *
	 * @return {@code MyLinkedList<Partida>} Lista enlazada de todas las partidas.
	 */
	public MyLinkedList<Partida> verTodasLasPartidas() {
		MyLinkedList<Partida> resultado = new MyLinkedList<>();
		for (Partida p : partidaRepository.findAll()) {
			resultado.add(p);
		}
		return resultado;
	}

	/**
	 * Obtiene una partida por su identificador.
	 *
	 * @param id Identificador de la partida.
	 * @return {@code Partida} La partida encontrada, o null si no existe.
	 */
	public Partida obtenerPartidaPorId(int id) {
		return partidaRepository.findById((long) id).orElse(null);
	}

	/**
	 * Finaliza una partida de manera forzada.
	 *
	 * @param partidaId Identificador de la partida.
	 * @return {@code PartidaDTO} DTO de la partida finalizada.
	 * @throws RuntimeException Si no existe la partida con el identificador dado.
	 */
	@Transactional
	public PartidaDTO finalizarPartidaForzada(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida con ID: " + partidaId));
		partida.setFinalizada(true);
		partida.setFechaFin(LocalDateTime.now());
		partidaRepository.save(partida);
		return modelMapper.map(partida, PartidaDTO.class);
	}

	/**
	 * Obtiene los jugadores de una partida.
	 *
	 * @param partida Partida de la que obtener los jugadores.
	 * @return {@code MyLinkedList<Jugador>} Lista enlazada de jugadores de la
	 *         partida.
	 */
	public MyLinkedList<Jugador> obtenerJugadoresPartida(Partida partida) {
		MyLinkedList<Jugador> jugadores = new MyLinkedList<>();
		if (partida.getJugadoresOrdenTurnoJSON() == null || partida.getJugadoresOrdenTurnoJSON().isEmpty()) {
			return jugadores;
		}
		Type tipoLista = new TypeToken<MyLinkedList<LinkedTreeMap>>() {
		}.getType();
		MyLinkedList<LinkedTreeMap> temp = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(), tipoLista);
		for (int i = 0; i < temp.size(); i++) {
			LinkedTreeMap map = temp.getPos(i).getInfo();
			Jugador j = new Jugador();
			j.setId(((Double) map.get("id")).longValue());
			j.setNombre((String) map.get("nombre"));
			j.setCorreo((String) map.getOrDefault("correo", ""));
			j.setColor((String) map.get("color"));
			j.setTropasDisponibles(((Double) map.getOrDefault("tropasDisponibles", 0.0)).intValue());
			j.setTerritoriosControlados(((Double) map.getOrDefault("territoriosControlados", 0.0)).intValue());
			j.setActivo((Boolean) map.getOrDefault("activo", true));
			jugadores.add(j);
		}
		return jugadores;
	}

	/**
	 * Genera un archivo ZIP con los resultados de una partida finalizada.
	 *
	 * @param partidaId Identificador de la partida.
	 * @return byte[] Arreglo de bytes del archivo ZIP generado.
	 * @throws Exception Si ocurre un error al generar el ZIP.
	 */
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

	/**
	 * Obtiene la partida actual en curso.
	 *
	 * @return {@code Long} Identificador de la partida actual, o null si no hay
	 *         ninguna.
	 */
	public Long obtenerPartidaActual() {
		Partida partida = partidaRepository.findFirstByIniciadaTrueAndFinalizadaFalse();
		if (partida == null) {
			return null;
		}
		return partida.getId();
	}

	/**
	 * Obtiene el jugador actual de una partida.
	 *
	 * @param partidaId Identificador de la partida.
	 * @return {@code Long} Identificador del jugador actual.
	 * @throws RuntimeException Si no existe la partida.
	 */
	public Long obtenerJugadorActual(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida"));
		return partida.getJugadorActualId();
	}

	/**
	 * Obtiene el identificador del anfitrión de una partida.
	 *
	 * @param partidaId Identificador de la partida.
	 * @return {@code Long} Identificador del anfitrión.
	 * @throws RuntimeException Si la partida no existe o la lista de jugadores está
	 *                          vacía.
	 */
	public Long obtenerIdAnfitrion(Long partidaId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("La partida no existe"));
		MyLinkedList<JugadorDTO> jugadores = gson.fromJson(partida.getJugadoresOrdenTurnoJSON(),
				new TypeToken<MyLinkedList<JugadorDTO>>() {
				}.getType());
		if (jugadores == null || jugadores.getFirst() == null)
			throw new RuntimeException("La lista de jugadores está vacía.");
		Node<JugadorDTO> nodoAnfitrion = jugadores.getFirst();
		return nodoAnfitrion.getInfo().getId();
	}

	public MyLinkedList<TerritorioDTO> obtenerTerritoriosPorJugador(Long partidaId, Long jugadorId) {
		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("No existe la partida con ese ID."));

		Type listType = TypeToken.getParameterized(MyLinkedList.class, TerritorioDTO.class).getType();
		MyLinkedList<TerritorioDTO> todosTerritorios = gson.fromJson(partida.getTerritoriosJSON(), listType);
		MyLinkedList<TerritorioDTO> territoriosJugador = new MyLinkedList<>();

		for (int i = 0; i < todosTerritorios.size(); i++) {
			TerritorioDTO t = todosTerritorios.get(i);
			if (t.getIdJugador() != null && t.getIdJugador().equals(jugadorId)) {
				territoriosJugador.add(t);
			}
		}

		return territoriosJugador;
	}

	@Transactional(readOnly = true)
	public MyLinkedList<TerritorioDTO> obtenerTodosLosTerritoriosDisponibles(Long partidaId) {

		Partida partida = partidaRepository.findById(partidaId)
				.orElseThrow(() -> new RuntimeException("La partida no existe"));

		Type listType = new TypeToken<MyLinkedList<TerritorioDTO>>() {
		}.getType();
		MyLinkedList<TerritorioDTO> territorios = gson.fromJson(partida.getTerritoriosJSON(), listType);

		MyLinkedList<TerritorioDTO> disponibles = new MyLinkedList<>();

		for (int i = 0; i < territorios.size(); i++) {
			TerritorioDTO t = territorios.get(i);
			if (t.getIdJugador() == 0L) {
				disponibles.add(t);
			}
		}

		return disponibles;
	}
	

}
