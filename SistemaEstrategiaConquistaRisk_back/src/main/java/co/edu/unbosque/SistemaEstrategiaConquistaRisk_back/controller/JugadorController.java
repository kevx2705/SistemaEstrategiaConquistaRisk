package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.JugadorService;

@RestController
@RequestMapping("/jugadores")
@CrossOrigin(origins = "*")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    // ==========================================================
    // ✅ CRUD BÁSICO
    // ==========================================================

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody JugadorDTO dto) {
        int r = jugadorService.create(dto);
        if (r == 0) return ResponseEntity.ok("Jugador creado");
        return ResponseEntity.badRequest().body("No se pudo crear el jugador");
    }

    @GetMapping("/all")
    public ResponseEntity<List<JugadorDTO>> getAll() {
        return ResponseEntity.ok(jugadorService.getAll());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        int r = jugadorService.deleteById(id);
        if (r == 0) return ResponseEntity.ok("Jugador eliminado");
        return ResponseEntity.status(404).body("Jugador no encontrado");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody JugadorDTO dto
    ) {
        int r = jugadorService.updateById(id, dto);
        if (r == 0) return ResponseEntity.ok("Jugador actualizado");
        return ResponseEntity.status(404).body("Jugador no encontrado");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(jugadorService.count());
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.exist(id));
    }

    // ==========================================================
    // ✅ FUNCIONES ESPECIALES DEL JUEGO
    // ==========================================================

    // ✅ Agregar tropas
    @PutMapping("/{id}/agregar-tropas/{cantidad}")
    public ResponseEntity<?> agregarTropas(
            @PathVariable Long id,
            @PathVariable int cantidad
    ) {
        jugadorService.agregarTropas(id, cantidad);
        return ResponseEntity.ok("Tropas agregadas");
    }

    // ✅ Quitar tropas
    @PutMapping("/{id}/quitar-tropas/{cantidad}")
    public ResponseEntity<?> quitarTropas(
            @PathVariable Long id,
            @PathVariable int cantidad
    ) {
        jugadorService.quitarTropas(id, cantidad);
        return ResponseEntity.ok("Tropas quitadas");
    }

    // ✅ Reset tropas disponibles
    @PutMapping("/{id}/reset-tropas")
    public ResponseEntity<?> resetTropas(@PathVariable Long id) {
        jugadorService.resetTropas(id);
        return ResponseEntity.ok("Tropas reseteadas");
    }

    // ✅ Dar carta
    @PostMapping("/{id}/dar-carta")
    public ResponseEntity<?> darCarta(@PathVariable Long id, @RequestBody Carta carta) {
        jugadorService.darCarta(id, carta);
        return ResponseEntity.ok("Carta añadida");
    }

    // ✅ Quitar carta por índice
    @DeleteMapping("/{id}/quitar-carta/{index}")
    public ResponseEntity<?> quitarCarta(
            @PathVariable Long id,
            @PathVariable int index
    ) {
        jugadorService.quitarCarta(id, index);
        return ResponseEntity.ok("Carta removida");
    }

    // ✅ Desactivar jugador
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarJugador(@PathVariable Long id) {
        jugadorService.desactivarJugador(id);
        return ResponseEntity.ok("Jugador desactivado");
    }

    // ✅ Activar jugador
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarJugador(@PathVariable Long id) {
        jugadorService.activarJugador(id);
        return ResponseEntity.ok("Jugador activado");
    }

    @PutMapping("/{id}/reset")
    public ResponseEntity<?> resetJugador(@PathVariable Long id) {
        jugadorService.resetJugadorPorId(id);
        return ResponseEntity.ok("Jugador reseteado");
    }

}
