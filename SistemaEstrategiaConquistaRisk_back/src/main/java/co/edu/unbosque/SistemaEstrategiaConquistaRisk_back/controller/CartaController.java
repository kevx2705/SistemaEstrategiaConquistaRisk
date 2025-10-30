package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @return mensaje de confirmación.
     */
    @PostMapping("/inicializar")
    public ResponseEntity<String> inicializarMazo() {
        cartaService.inicializarMazo();
        return ResponseEntity.status(HttpStatus.OK)
                .body("Mazo inicializado y barajado correctamente");
    }

    /**
     * Roba la carta superior del mazo (si hay disponibles).
     *
     * @return carta robada como {@link CartaDTO}.
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
     * @param carta carta devuelta por el jugador.
     * @return mensaje de confirmación.
     */
    @PostMapping("/devolver")
    public ResponseEntity<String> devolverCarta(@RequestBody Long carta) {
        cartaService.devolverCarta(carta);
        return ResponseEntity.ok("Carta devuelta al mazo y remezclada correctamente");
    }

    /**
     * Devuelve la cantidad actual de cartas en el mazo.
     *
     * @return número total de cartas disponibles.
     */
    @GetMapping("/tamano")
    public ResponseEntity<Integer> tamañoMazo() {
        int tamaño = cartaService.tamañoMazo();
        return ResponseEntity.ok(tamaño);
    }
    @GetMapping("/robadas")
    public ResponseEntity<Integer> contarCartasRobadas() {
        int cantidad = cartaService.contarCartasRobadas();
        return ResponseEntity.ok(cantidad);
    }

    /**
     * Restaura todas las cartas como disponibles y reconstruye el mazo.
     *
     * @return mensaje indicando que el mazo fue reseteado.
     */
    @PutMapping("/reset")
    public ResponseEntity<String> resetCartas() {
        cartaService.resetCartas();
        return ResponseEntity.ok("Todas las cartas fueron reiniciadas y el mazo reconstruido");
    }
}
