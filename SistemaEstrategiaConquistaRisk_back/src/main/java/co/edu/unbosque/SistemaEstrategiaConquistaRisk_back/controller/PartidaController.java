package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.*;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.PartidaService;

@RestController
@RequestMapping("/partida")
public class PartidaController {

	@Autowired
	private PartidaService partidaService;

	// ------------------ CREAR PARTIDA ------------------
	@PostMapping("/crear")
	public ResponseEntity<PartidaDTO> crearPartida(@RequestParam Long anfitrionId, @RequestBody String[] otrosNombres) {
		return ResponseEntity.ok(partidaService.crearPartidaDTO(anfitrionId, otrosNombres)); // puedes crear un método
																								// toDTO en Partida
	}

	// ------------------ INICIALIZAR JUEGO ------------------
	@PostMapping("/{id}/inicializar")
	public ResponseEntity<Void> inicializarJuego(@PathVariable Long id) {
		partidaService.inicializarJuego(id);
		return ResponseEntity.ok().build();
	}

	// ------------------ RECLAMAR TERRITORIO ------------------
	@PostMapping("/{id}/reclamar")
	public ResponseEntity<Void> reclamarTerritorio(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestParam Long territorioId) {
		partidaService.reclamarTerritorio(id, jugadorId, territorioId);
		return ResponseEntity.ok().build();
	}

	// ------------------ INICIAR FASE DE REFUERZO ------------------
	@PostMapping("/{id}/fase/refuerzo")
	public ResponseEntity<Void> iniciarFaseRefuerzo(@PathVariable Long id) {
		partidaService.iniciarFaseRefuerzo(id);
		return ResponseEntity.ok().build();
	}

	// ------------------ COLOCAR TROPAS ------------------
	@PostMapping("/{id}/colocar-tropa")
	public ResponseEntity<Void> colocarTropa(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestParam String territorio, @RequestParam int cantidad) {
		partidaService.colocarTropa(partidaService.retornarPartidaEntidad(id), // método auxiliar para obtener Partida
				jugadorId, territorio, cantidad);
		return ResponseEntity.ok().build();
	}

	// ------------------ ATAQUE ------------------
	@PostMapping("/{id}/atacar")
	public ResponseEntity<ResultadoAtaqueDTO> atacar(@PathVariable Long id, @RequestParam Long atacanteId,
			@RequestParam Long territorioAtacanteId, @RequestParam Long territorioDefensorId,
			@RequestParam int dadosAtacante, @RequestParam int dadosDefensor) {
		return ResponseEntity.ok(partidaService.atacar(id, atacanteId, territorioAtacanteId, territorioDefensorId,
				dadosAtacante, dadosDefensor));
	}

	// ------------------ FINALIZAR TURNO ------------------
	@PostMapping("/{id}/finalizar-turno")
	public ResponseEntity<Void> finalizarTurno(@PathVariable Long id) {
		partidaService.finalizarTurno(id);
		return ResponseEntity.ok().build();
	}

	// ------------------ MOVER TROPAS FASE 3 ------------------
	@PostMapping("/{id}/mover-tropas")
	public ResponseEntity<Void> moverTropas(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestParam String origen, @RequestParam String destino, @RequestParam int cantidad) {
		partidaService.moverTropasFase3(id, jugadorId, origen, destino, cantidad);
		return ResponseEntity.ok().build();
	}

	// ------------------ CANJEAR CARTAS ------------------
	@PostMapping("/{id}/canjear-cartas")
	public ResponseEntity<Integer> canjearCartas(@PathVariable Long id, @RequestParam Long jugadorId,
			@RequestBody MyLinkedList<Long> idsCartas) {
		return ResponseEntity.ok(partidaService.canjearCartas(id, jugadorId, idsCartas));
	}

	// ------------------ ELIMINAR JUGADOR ------------------
	@DeleteMapping("/{id}/jugador/{jugadorId}")
	public ResponseEntity<Void> eliminarJugador(@PathVariable Long id, @PathVariable Long jugadorId) {
		partidaService.eliminarJugador(id, jugadorId);
		return ResponseEntity.ok().build();
	}

	// ------------------ RETOMAR PARTIDA ------------------
	@GetMapping("/retomar/{codigo}")
	public ResponseEntity<PartidaDTO> retomarPartida(@PathVariable String codigo) {
		return ResponseEntity.ok(partidaService.retomarPartida(codigo));
	}

	// ------------------ VERIFICAR FIN DE PARTIDA ------------------
	@GetMapping("/{id}/fin")
	public ResponseEntity<Long> verificarFinPartida(@PathVariable Long id) {
		Long ganadorId = partidaService.verificarFinPartida(partidaService.retornarPartidaEntidad(id));
		return ResponseEntity.ok(ganadorId);
	}

	@GetMapping("/listar")
	public ResponseEntity<?> listarPartidas() {
		try {
			return ResponseEntity.ok(partidaService.verTodasLasPartidas());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al listar partidas: " + e.getMessage());
		}
	}

}
