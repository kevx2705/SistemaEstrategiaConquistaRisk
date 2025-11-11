package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.TerritorioDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.TerritorioService;

@RestController
@RequestMapping("/api/territorios")
public class TerritorioController {

    @Autowired
    private TerritorioService territorioService;

    @GetMapping
    public ResponseEntity<MyLinkedList<TerritorioDTO>> listar() {
        return ResponseEntity.ok(territorioService.obtenerTodos());
    }
    // ✅ Asignar jugador a territorio
    @PostMapping("/asignar-jugador/{idTerritorio}/{idJugador}")
    public ResponseEntity<String> asignarJugador(
            @PathVariable Long idTerritorio,
            @PathVariable Long idJugador) {
        territorioService.asignarJugador(idTerritorio, idJugador);
        return new ResponseEntity<>("Jugador asignado correctamente", HttpStatus.OK);
    }

    // ✅ Reforzar territorio (añadir tropas)
    @PostMapping("/reforzar/{idTerritorio}/{cantidad}")
    public ResponseEntity<String> reforzar(
            @PathVariable Long idTerritorio,
            @PathVariable int cantidad) {
        territorioService.reforzar(idTerritorio, cantidad);
        return new ResponseEntity<>("Tropas reforzadas correctamente", HttpStatus.OK);
    }

    // ✅ Quitar tropas (por ataque)
    @PostMapping("/quitar-tropas/{idTerritorio}/{cantidad}")
    public ResponseEntity<String> quitarTropas(
            @PathVariable Long idTerritorio,
            @PathVariable int cantidad) {
        territorioService.quitarTropas(idTerritorio, cantidad);
        return new ResponseEntity<>("Tropas reducidas correctamente", HttpStatus.OK);
    }

 // ✅ Quitar todas las tropas (por id)
    @PostMapping("/quitar-todas/{idTerritorio}")
    public ResponseEntity<String> quitarTodasLasTropas(@PathVariable Long idTerritorio) {
        territorioService.quitarTodasLasTropas(idTerritorio);
        return new ResponseEntity<>("Todas las tropas fueron removidas del territorio", HttpStatus.OK);
    }
 // ✅ Reiniciar todos los territorios (poner idJugador y tropas en 0)
    @PostMapping("/reiniciar-todos")
    public ResponseEntity<String> reiniciarTodos() {
        territorioService.reiniciarTodos();
        return new ResponseEntity<>("Todos los territorios fueron reiniciados (jugadores y tropas en 0)", HttpStatus.OK);
    }


    // ✅ Verificar si un jugador controla un continente
    @GetMapping("/controla-continente/{idJugador}/{idContinente}")
    public ResponseEntity<Boolean> jugadorControlaContinente(
            @PathVariable Long idJugador,
            @PathVariable Long idContinente) {
        boolean controla = territorioService.jugadorControlaContinente(idJugador, idContinente);
        return new ResponseEntity<>(controla, HttpStatus.OK);
    }
}
