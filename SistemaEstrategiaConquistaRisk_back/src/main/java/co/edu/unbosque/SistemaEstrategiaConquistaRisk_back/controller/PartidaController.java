package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.reflect.TypeToken;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.*;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.Node;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.PartidaService;
import jakarta.persistence.EntityNotFoundException;

/**
 * Controlador REST para la gestión de partidas en el juego Risk. Proporciona
 * endpoints para crear, inicializar, reclamar territorios, atacar, mover
 * tropas, canjear cartas, eliminar jugadores, retomar partidas, verificar el
 * fin de una partida, listar partidas y descargar resultados finales.
 */
@RestController
@RequestMapping("/partida")
public class PartidaController {

	/**
	 * Servicio para la gestión de partidas.
	 */
	@Autowired
	private PartidaService partidaService;

	/**
	 * Finaliza una partida de manera forzada.
	 * <p>
	 * <strong>Nota:</strong> Este método es temporal y debe ser eliminado en
	 * producción.
	 * </p>
	 *
	 * @param id Identificador de la partida a finalizar.
	 * @return ResponseEntity con la partida finalizada en formato DTO.
	 */
	@PutMapping("/partida/{id}/finalizar-forzada")
	public ResponseEntity<PartidaDTO> finalizarForzada(@PathVariable Long id) {
		return ResponseEntity.ok(partidaService.finalizarPartidaForzada(id));
	}

	/**
	 * Crea una nueva partida con el anfitrión y los nombres de los otros jugadores.
	 *
	 * @param anfitrionId  Identificador del jugador anfitrión.
	 * @param otrosNombres Arreglo de nombres de los otros jugadores.
	 * @return ResponseEntity con la partida creada en formato DTO.
	 */
	@PostMapping("/crear")
	public ResponseEntity<?> crearPartida(@RequestParam Long anfitrionId, @RequestBody String[] otrosNombres) {

		try {
			PartidaDTO partida = partidaService.crearPartidaDTO(anfitrionId, otrosNombres);
			return ResponseEntity.ok(partida);

		} catch (EntityNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(crearError(ex.getMessage()));

		} catch (IllegalStateException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(crearError(ex.getMessage()));

		} catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(crearError(ex.getMessage()));

		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(crearError("Ocurrió un error inesperado"));
		}
	}

	private Map<String, String> crearError(String mensaje) {
		Map<String, String> error = new HashMap<String, String>();
		error.put("error", mensaje);
		return error;
	}

	/**
	 * Inicializa el juego para una partida específica.
	 *
	 * @param id Identificador de la partida a inicializar.
	 * @return ResponseEntity sin contenido si la operación fue exitosa.
	 */
	@PostMapping("/{id}/inicializar")
	public ResponseEntity<Void> inicializarJuego(@PathVariable Long id) {
		partidaService.inicializarJuego(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Permite a un jugador reclamar un territorio en una partida.
	 *
	 * @param id           Identificador de la partida.
	 * @param jugadorId    Identificador del jugador que reclama el territorio.
	 * @param territorioId Identificador del territorio a reclamar.
	 * @return ResponseEntity sin contenido si la operación fue exitosa.
	 */
	@PostMapping("/{id}/reclamar")
	public ResponseEntity<?> reclamarTerritorio(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestParam Long territorioId) {

		System.out.println("=== Controller: Reclamar Territorio ===");
		System.out.println("Partida ID: " + id + ", Jugador ID: " + jugadorId + ", Territorio ID: " + territorioId);

		try {

			partidaService.reclamarTerritorio(id, jugadorId, territorioId);

			return ResponseEntity.ok().body(Map.of("mensaje", "Territorio reclamado correctamente", "jugadorId",
					jugadorId, "territorioId", territorioId));

		} catch (RuntimeException e) {

			String mensaje = e.getMessage();

			System.out.println("Error en reclamarTerritorio: " + mensaje);

			if (mensaje.contains("No existe la partida")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", mensaje));
			}

			if (mensaje.contains("Territorio no encontrado")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", mensaje));
			}

			if (mensaje.contains("No es el turno")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", mensaje, "turnoActual", "No coincide con jugador " + jugadorId));
			}

			if (mensaje.contains("Territorio ya asignado")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", mensaje));
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error inesperado: " + mensaje));
		}
	}

	/**
	 * Obtiene la lista de jugadores de una partida específica.
	 *
	 * @param partidaId Identificador de la partida.
	 * @return ResponseEntity con la lista enlazada de jugadores en formato DTO.
	 */
	@GetMapping("/{partidaId}/jugadores")
	public ResponseEntity<List<JugadorDTO>> obtenerJugadoresPartida(@PathVariable Long partidaId) {
		MyLinkedList<JugadorDTO> jugadoresLinkedList = partidaService.obtenerJugadoresPorPartida(partidaId);

		List<JugadorDTO> jugadoresList = new ArrayList<>();
		Node<JugadorDTO> current = jugadoresLinkedList.getFirst();
		while (current != null) {
			jugadoresList.add(current.getInfo());
			current = current.getNext();
		}

		return ResponseEntity.ok(jugadoresList);
	}

	@PostMapping("/{partidaId}/fase-refuerzo/iniciar")
	public ResponseEntity<?> iniciarFaseRefuerzo(@PathVariable Long partidaId) {
		try {
			Partida partidaActualizada = partidaService.iniciarFaseRefuerzo(partidaId);
			return ResponseEntity.ok(partidaActualizada); // devolvemos la partida actualizada
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Coloca tropas en un territorio específico.
	 */
	@PostMapping("/fase-refuerzo/colocar-tropas")
	public ResponseEntity<?> colocarTropas(@RequestParam Long partidaId, @RequestParam Long jugadorId,
			@RequestParam String nombreTerritorio, @RequestParam int cantidad) {

		try {
			// Obtener la partida desde el service
			Partida partida = partidaService.obtenerPartidaPorId(partidaId);
			if (partida == null) {
				return ResponseEntity.badRequest().body("No existe la partida");
			}

			// Coloca las tropas
			partidaService.colocarTropa(partida, jugadorId, nombreTerritorio, cantidad);

			// Devuelve la partida actualizada
			partida = partidaService.obtenerPartidaPorId(partidaId);
			return ResponseEntity.ok(partida);

		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Realiza un ataque entre territorios en una partida.
	 *
	 * @param id                   Identificador de la partida.
	 * @param atacanteId           Identificador del jugador atacante.
	 * @param territorioAtacanteId Identificador del territorio atacante.
	 * @param territorioDefensorId Identificador del territorio defensor.
	 * @return ResponseEntity con el resultado del ataque en formato
	 *         ResultadoAtaqueDTO.
	 */
	@PostMapping("/{id}/atacar")
	public ResponseEntity<ResultadoAtaqueDTO> atacar(@PathVariable Long id, @RequestParam Long atacanteId,
			@RequestParam Long territorioAtacanteId, @RequestParam Long territorioDefensorId) {
		return ResponseEntity.ok(partidaService.atacar(id, atacanteId, territorioAtacanteId, territorioDefensorId));
	}

	/**
	 * Finaliza el turno actual en una partida.
	 *
	 * @param id Identificador de la partida.
	 * @return ResponseEntity sin contenido si la operación fue exitosa.
	 */
	@PostMapping("/{id}/finalizar-turno")
	public ResponseEntity<Void> finalizarTurno(@PathVariable Long id) {
		partidaService.finalizarTurno(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Mueve tropas entre territorios en la fase 3 de una partida.
	 *
	 * @param id        Identificador de la partida.
	 * @param jugadorId Identificador del jugador que mueve las tropas.
	 * @param origen    Nombre del territorio de origen.
	 * @param destino   Nombre del territorio de destino.
	 * @param cantidad  Cantidad de tropas a mover.
	 * @return ResponseEntity sin contenido si la operación fue exitosa.
	 */
	@PostMapping("/{id}/mover-tropas")
	public ResponseEntity<Void> moverTropas(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestParam Long origen, @RequestParam Long destino, @RequestParam int cantidad) {

		partidaService.moverTropasFase3(id, jugadorId, origen, destino, cantidad);
		return ResponseEntity.ok().build();
	}

	/**
	 * Canjea cartas por tropas adicionales en una partida.
	 *
	 * @param id        Identificador de la partida.
	 * @param jugadorId Identificador del jugador que canjea las cartas.
	 * @param idsCartas Lista de identificadores de las cartas a canjear.
	 * @return ResponseEntity con la cantidad de tropas obtenidas por el canje.
	 */
	@PostMapping("/{id}/canjear-cartas")
	public ResponseEntity<Integer> canjearCartas(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestBody MyLinkedList<Long> idsCartas) {
		return ResponseEntity.ok(partidaService.canjearCartas(id, jugadorId, idsCartas));
	}

	/**
	 * Elimina un jugador de una partida.
	 *
	 * @param id        Identificador de la partida.
	 * @param jugadorId Identificador del jugador a eliminar.
	 * @return ResponseEntity sin contenido si la operación fue exitosa.
	 */
	@DeleteMapping("/{id}/jugador/{jugadorId}")
	public ResponseEntity<Void> eliminarJugador(@PathVariable Long id, @PathVariable Long jugadorId) {
		partidaService.eliminarJugador(id, jugadorId);
		return ResponseEntity.ok().build();
	}

	/**
	 * Retoma una partida usando un código único.
	 *
	 * @param codigo Código único de la partida a retomar.
	 * @return ResponseEntity con la partida retomada en formato DTO.
	 */
	@GetMapping("/retomar/{codigo}")
	public ResponseEntity<PartidaDTO> retomarPartida(@PathVariable String codigo) {
		return ResponseEntity.ok(partidaService.retomarPartida(codigo));
	}

	/**
	 * Verifica si una partida ha finalizado y devuelve el identificador del
	 * ganador.
	 *
	 * @param id Identificador de la partida.
	 * @return ResponseEntity con el identificador del ganador, o null si la partida
	 *         no ha finalizado.
	 */
	@GetMapping("/{id}/fin")
	public ResponseEntity<Long> verificarFinPartida(@PathVariable Long id) {
		Long ganadorId = partidaService.verificarFinPartida(partidaService.retornarPartidaEntidad(id));
		return ResponseEntity.ok(ganadorId);
	}

	/**
	 * Lista todas las partidas disponibles.
	 *
	 * @return ResponseEntity con la lista de partidas, o un mensaje de error si
	 *         ocurre una excepción.
	 */
	@GetMapping("/listar")
	public ResponseEntity<?> listarPartidas() {
		try {
			return ResponseEntity.ok(partidaService.verTodasLasPartidas());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al listar partidas: " + e.getMessage());
		}
	}

	/**
	 * Descarga un archivo ZIP con los resultados finales de una partida.
	 *
	 * @param partidaId Identificador de la partida.
	 * @return ResponseEntity con el archivo ZIP adjunto, o un mensaje de error si
	 *         ocurre una excepción.
	 */
	@GetMapping("/descargar-final/{partidaId}")
	public ResponseEntity<byte[]> descargarZipFinal(@PathVariable Long partidaId) {
		try {
			byte[] zipBytes = partidaService.generarZipFinalPartida(partidaId);
			if (zipBytes == null) {
				return ResponseEntity.badRequest().body(null);
			}
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=\"Resultados_" + partidaId + ".zip\"")
					.header("Content-Type", "application/zip").body(zipBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/actual")
	public ResponseEntity<Long> obtenerPartidaActual() {
		Long id = partidaService.obtenerPartidaActual();

		if (id == null) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(id);
	}

	@GetMapping("/{partidaId}/jugador-actual")
	public ResponseEntity<Long> obtenerJugadorActual(@PathVariable Long partidaId) {
		return ResponseEntity.ok(partidaService.obtenerJugadorActual(partidaId));
	}

	@GetMapping("/{partidaId}/anfitrion")
	public ResponseEntity<Long> obtenerAnfitrion(@PathVariable Long partidaId) {
		Long anfitrionId = partidaService.obtenerIdAnfitrion(partidaId);
		return ResponseEntity.ok(anfitrionId);
	}

	@GetMapping("/partidas/{partidaId}/territorios/jugador/{jugadorId}")
	public ResponseEntity<MyLinkedList<TerritorioDTO>> getTerritoriosJugador(@PathVariable Long partidaId,
			@PathVariable Long jugadorId) {

		MyLinkedList<TerritorioDTO> territorios = partidaService.obtenerTerritoriosPorJugador(partidaId, jugadorId);

		return ResponseEntity.ok(territorios);
	}

	@GetMapping("/{id}/territorios/disponibles")
	public ResponseEntity<List<TerritorioDTO>> obtenerDisponibles(@PathVariable Long id) {

		MyLinkedList<TerritorioDTO> lista = partidaService.obtenerTodosLosTerritoriosDisponibles(id);

		List<TerritorioDTO> listaNormal = new ArrayList<>();

		Node<TerritorioDTO> current = lista.getFirst();
		while (current != null) {
			listaNormal.add(current.getInfo());
			current = current.getNext();
		}

		return ResponseEntity.ok(listaNormal);
	}

	@GetMapping("/{partidaId}/territorios/{territorioId}")
	public ResponseEntity<TerritorioDTO> obtenerTerritorioPorId(@PathVariable Long partidaId,
			@PathVariable Long territorioId) {

		try {
			TerritorioDTO territorio = partidaService.obtenerTerritorioPorId(partidaId, territorioId);
			return ResponseEntity.ok(territorio);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/{partidaId}/jugadores/{jugadorId}")
	public ResponseEntity<JugadorDTO> getJugadorDePartida(@PathVariable Long partidaId, @PathVariable Long jugadorId) {
		try {
			JugadorDTO jugador = partidaService.obtenerJugadorDePartida(partidaId, jugadorId);
			return ResponseEntity.ok(jugador);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/{partidaId}/jugador-con-correo")
	public ResponseEntity<String> obtenerCorreoJugadorConCorreo(@PathVariable Long partidaId) {
		try {
			// Llama al servicio que devuelve el JugadorDTO con correo
			JugadorDTO jugadorConCorreo = partidaService.obtenerJugadorConCorreo(partidaId);

			if (jugadorConCorreo.getCorreo() == null || jugadorConCorreo.getCorreo().isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			return ResponseEntity.ok(jugadorConCorreo.getCorreo());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/{partidaId}/jugadores/{jugadorId}/territorios")
	public ResponseEntity<List<TerritorioDTO>> getTerritoriosDeJugador(@PathVariable Long partidaId,
			@PathVariable Long jugadorId) {

		MyLinkedList<TerritorioDTO> territoriosLinked = partidaService.obtenerTerritoriosDeJugador(partidaId,
				jugadorId);
		List<TerritorioDTO> territoriosList = new ArrayList<>();

		for (int i = 0; i < territoriosLinked.size(); i++) {
			territoriosList.add(territoriosLinked.getPos(i).getInfo());
		}

		return ResponseEntity.ok(territoriosList);
	}

	@GetMapping("/{partidaId}/jugadores/{jugadorId}/territorios-adyacentes-aliados")
	public ResponseEntity<List<TerritorioDTO>> obtenerTerritoriosAdyacentesAliados(@PathVariable Long partidaId,
			@PathVariable Long jugadorId, @RequestParam Long territorioOrigenId) {

		MyLinkedList<TerritorioDTO> lista = partidaService.obtenerTerritoriosAdyacentesAliados(partidaId, jugadorId,
				territorioOrigenId);

		java.util.List<TerritorioDTO> listaNormal = new java.util.ArrayList<>();
		Node<TerritorioDTO> current = lista.getFirst();
		while (current != null) {
			listaNormal.add(current.getInfo());
			current = current.getNext();
		}

		return ResponseEntity.ok(listaNormal);
	}

	@GetMapping("/{partidaId}/jugadores/{jugadorId}/territorios-adyacentes-atacables")
	public ResponseEntity<List<TerritorioDTO>> obtenerTerritoriosAdyacentesAtacables(@PathVariable Long partidaId,
			@PathVariable Long jugadorId, @RequestParam Long territorioOrigenId) {

		MyLinkedList<TerritorioDTO> lista = partidaService.obtenerTerritoriosAdyacentesAtacables(partidaId, jugadorId,
				territorioOrigenId);

		List<TerritorioDTO> listaNormal = new ArrayList<>();
		Node<TerritorioDTO> current = lista.getFirst();
		while (current != null) {
			listaNormal.add(current.getInfo());
			current = current.getNext();
		}

		return ResponseEntity.ok(listaNormal);
	}

}