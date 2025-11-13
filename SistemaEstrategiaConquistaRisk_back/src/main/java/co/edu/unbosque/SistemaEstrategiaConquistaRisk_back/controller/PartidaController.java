package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.*;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.PartidaService;

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
	public ResponseEntity<PartidaDTO> crearPartida(@RequestParam Long anfitrionId, @RequestBody String[] otrosNombres) {
		return ResponseEntity.ok(partidaService.crearPartidaDTO(anfitrionId, otrosNombres));
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
	public ResponseEntity<Void> reclamarTerritorio(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestParam Long territorioId) {
		partidaService.reclamarTerritorio(id, jugadorId, territorioId);
		return ResponseEntity.ok().build();
	}

	/**
	 * Inicia la fase de refuerzo en una partida.
	 *
	 * @param id Identificador de la partida.
	 * @return ResponseEntity sin contenido si la operación fue exitosa.
	 */
	@PostMapping("/{id}/fase/refuerzo")
	public ResponseEntity<Void> iniciarFaseRefuerzo(@PathVariable Long id) {
		partidaService.iniciarFaseRefuerzo(id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Coloca tropas en un territorio específico de una partida.
	 *
	 * @param id         Identificador de la partida.
	 * @param jugadorId  Identificador del jugador que coloca las tropas.
	 * @param territorio Nombre del territorio donde se colocarán las tropas.
	 * @param cantidad   Cantidad de tropas a colocar.
	 * @return ResponseEntity sin contenido si la operación fue exitosa.
	 */
	@PostMapping("/{id}/colocar-tropa")
	public ResponseEntity<Void> colocarTropa(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestParam String territorio, @RequestParam int cantidad) {
		partidaService.colocarTropa(partidaService.retornarPartidaEntidad(id), jugadorId, territorio, cantidad);
		return ResponseEntity.ok().build();
	}

	/**
	 * Realiza un ataque entre territorios en una partida.
	 *
	 * @param id                   Identificador de la partida.
	 * @param atacanteId           Identificador del jugador atacante.
	 * @param territorioAtacanteId Identificador del territorio atacante.
	 * @param territorioDefensorId Identificador del territorio defensor.
	 * @param dadosAtacante        Número de dados que usará el atacante.
	 * @param dadosDefensor        Número de dados que usará el defensor.
	 * @return ResponseEntity con el resultado del ataque en formato
	 *         ResultadoAtaqueDTO.
	 */
	@PostMapping("/{id}/atacar")
	public ResponseEntity<ResultadoAtaqueDTO> atacar(@PathVariable Long id, @RequestParam Long atacanteId,
			@RequestParam Long territorioAtacanteId, @RequestParam Long territorioDefensorId,
			@RequestParam int dadosAtacante, @RequestParam int dadosDefensor) {
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
			@RequestParam String origen, @RequestParam String destino, @RequestParam int cantidad) {
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
	public ResponseEntity<byte[]> descargarZipFinal(@PathVariable int partidaId) {
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

}
