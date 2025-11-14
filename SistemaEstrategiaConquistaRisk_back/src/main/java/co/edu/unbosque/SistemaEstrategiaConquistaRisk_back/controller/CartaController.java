package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.CartaDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.CartaService;

/**
 * Controlador REST para la gestión de cartas en el juego Risk.
 * <p>
 * Permite inicializar el mazo, robar y devolver cartas,
 * además de resetear todas las cartas disponibles.
 * </p>
 */
@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping(path = {"/carta"})
public class CartaController {

    @Autowired
    private CartaService cartaService;

    /**
     * Inicializa el mazo de cartas disponibles y lo baraja.
     *
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     */
    @PostMapping("/inicializar")
    public ResponseEntity<String> inicializarMazo() {
        cartaService.inicializarMazo();
        return ResponseEntity.status(HttpStatus.OK)
                .body("Mazo inicializado y barajado correctamente");
    }

    /**
     * Imprime el estado actual del mazo en la consola.
     * <p>
     * <b>Nota:</b> Este endpoint es para depuración y no retorna información sensible.
     * </p>
     *
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     */
    @GetMapping("/debug/mazo")
    public ResponseEntity<String> debugMazo() {
        cartaService.imprimirEstadoMazo();
        return ResponseEntity.ok("Mazo impreso en consola");
    }

    /**
     * Roba la carta superior del mazo (si hay disponibles).
     *
     * @return {@code ResponseEntity<CartaDTO>} con la carta robada.
     *         Retorna {@code 200 OK} con la carta si hay disponibles,
     *         o {@code 204 NO_CONTENT} si el mazo está vacío.
     */
    @GetMapping("/robar")
    public ResponseEntity<CartaDTO> robarCarta() {
        CartaDTO carta = cartaService.robarCarta();
        return carta != null
                ? ResponseEntity.ok(carta)
                : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Devuelve una carta al mazo y la remezcla.
     *
     * @param carta Identificador de la carta a devolver.
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     */
    @PostMapping("/devolver")
    public ResponseEntity<String> devolverCarta(@RequestBody Long carta) {
        cartaService.devolverCarta(carta);
        return ResponseEntity.ok("Carta devuelta al mazo y remezclada correctamente");
    }

    /**
     * Devuelve la cantidad actual de cartas disponibles en el mazo.
     *
     * @return {@code ResponseEntity<Integer>} con el número total de cartas disponibles.
     */
    @GetMapping("/tamano")
    public ResponseEntity<Integer> tamañoMazo() {
        int tamaño = cartaService.tamañoMazo();
        return ResponseEntity.ok(tamaño);
    }

    /**
     * Cuenta la cantidad total de cartas que han sido robadas hasta el momento.
     *
     * @return {@code ResponseEntity<Integer>} con la cantidad de cartas robadas.
     */
    @GetMapping("/robadas")
    public ResponseEntity<Integer> contarCartasRobadas() {
        int cantidad = cartaService.contarCartasRobadas();
        return ResponseEntity.ok(cantidad);
    }

    /**
     * Restaura todas las cartas como disponibles y reconstruye el mazo.
     * <p>
     * Este método reinicia el estado de todas las cartas y reconstruye el mazo
     * con todas las cartas disponibles, útil para reiniciar una partida.
     * </p>
     *
     * @return {@code ResponseEntity<String>} con un mensaje de confirmación.
     */
    @PutMapping("/reset")
    public ResponseEntity<String> resetCartas() {
        cartaService.resetCartas();
        return ResponseEntity.ok("Todas las cartas fueron reiniciadas y el mazo reconstruido");
    }
}
