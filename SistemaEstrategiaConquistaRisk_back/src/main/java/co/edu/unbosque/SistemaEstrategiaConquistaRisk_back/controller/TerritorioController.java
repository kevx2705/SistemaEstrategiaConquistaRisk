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
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.TerritorioService;

/**
 * Controlador REST para la gestión de territorios en el juego Risk.
 * <p>
 * Proporciona endpoints para listar territorios, asignar jugadores, reforzar tropas,
 * quitar tropas y verificar el control de continentes.
 * </p>
 */
@RestController
@RequestMapping("/api/territorios")
public class TerritorioController {

    /** Servicio para la gestión de territorios. */
    @Autowired
    private TerritorioService territorioService;

    /**
     * Constructor por defecto de la clase {@code TerritorioController}.
     * Inicializa una instancia del controlador para gestionar territorios.
     */
    public TerritorioController() {
        // Constructor por defecto
    }

    /**
     * Lista todos los territorios disponibles en el sistema.
     *
     * @return {@code ResponseEntity<MyLinkedList<TerritorioDTO>>} con la lista de territorios.
     *         Retorna {@code 200 OK} con la lista de territorios en formato DTO.
     */
    @GetMapping
    public ResponseEntity<MyLinkedList<TerritorioDTO>> listar() {
        return ResponseEntity.ok(territorioService.obtenerTodos());
    }

    /**
     * Asigna un jugador como propietario de un territorio.
     *
     * @param idTerritorio Identificador del territorio.
     * @param idJugador    Identificador del jugador.
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     *         Retorna {@code 200 OK} si la asignación fue exitosa.
     */
    @PostMapping("/asignar-jugador/{idTerritorio}/{idJugador}")
    public ResponseEntity<String> asignarJugador(@PathVariable Long idTerritorio, @PathVariable Long idJugador) {
        territorioService.asignarJugador(idTerritorio, idJugador);
        return new ResponseEntity<>("Jugador asignado correctamente", HttpStatus.OK);
    }

    /**
     * Reforce las tropas de un territorio.
     *
     * @param idTerritorio Identificador del territorio.
     * @param cantidad      Cantidad de tropas a añadir.
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     *         Retorna {@code 200 OK} si el refuerzo fue exitoso.
     */
    @PostMapping("/reforzar/{idTerritorio}/{cantidad}")
    public ResponseEntity<String> reforzar(@PathVariable Long idTerritorio, @PathVariable int cantidad) {
        territorioService.reforzar(idTerritorio, cantidad);
        return new ResponseEntity<>("Tropas reforzadas correctamente", HttpStatus.OK);
    }

    /**
     * Quita tropas de un territorio.
     *
     * @param idTerritorio Identificador del territorio.
     * @param cantidad      Cantidad de tropas a quitar.
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     *         Retorna {@code 200 OK} si la reducción de tropas fue exitosa.
     */
    @PostMapping("/quitar-tropas/{idTerritorio}/{cantidad}")
    public ResponseEntity<String> quitarTropas(@PathVariable Long idTerritorio, @PathVariable int cantidad) {
        territorioService.quitarTropas(idTerritorio, cantidad);
        return new ResponseEntity<>("Tropas reducidas correctamente", HttpStatus.OK);
    }

    /**
     * Quita todas las tropas de un territorio.
     *
     * @param idTerritorio Identificador del territorio.
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     *         Retorna {@code 200 OK} si todas las tropas fueron removidas.
     */
    @PostMapping("/quitar-todas/{idTerritorio}")
    public ResponseEntity<String> quitarTodasLasTropas(@PathVariable Long idTerritorio) {
        territorioService.quitarTodasLasTropas(idTerritorio);
        return new ResponseEntity<>("Todas las tropas fueron removidas del territorio", HttpStatus.OK);
    }

    /**
     * Reinicia todos los territorios, dejando sin jugador y sin tropas.
     *
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     *         Retorna {@code 200 OK} si todos los territorios fueron reiniciados.
     */
    @PostMapping("/reiniciar-todos")
    public ResponseEntity<String> reiniciarTodos() {
        territorioService.reiniciarTodos();
        return new ResponseEntity<>("Todos los territorios fueron reiniciados (jugadores y tropas en 0)",
                HttpStatus.OK);
    }

    /**
     * Verifica si un jugador controla todos los territorios de un continente.
     *
     * @param idJugador    Identificador del jugador.
     * @param idContinente Identificador del continente.
     * @return {@code ResponseEntity<Boolean>} con el resultado de la verificación.
     *         Retorna {@code true} si el jugador controla todos los territorios del continente,
     *         o {@code false} en caso contrario.
     */
    @GetMapping("/controla-continente/{idJugador}/{idContinente}")
    public ResponseEntity<Boolean> jugadorControlaContinente(@PathVariable Long idJugador,
            @PathVariable Long idContinente) {
        boolean controla = territorioService.jugadorControlaContinente(idJugador, idContinente);
        return new ResponseEntity<>(controla, HttpStatus.OK);
    }
}
