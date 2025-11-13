package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.EmailService;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.PartidaService;

/**
 * Controlador REST para el manejo de envío de correos electrónicos relacionados con partidas.
 * Proporciona endpoints para enviar correos al finalizar una partida.
 */
@RestController
@RequestMapping("/correo")
public class EmailController {

    /** Servicio para el envío de correos electrónicos. */
    @Autowired
    private EmailService mailService;

    /** Servicio para la gestión de partidas. */
    @Autowired
    private PartidaService partidaService;

    /**
     * Envía un correo electrónico con los resultados finales de una partida.
     *
     * @param partidaId El identificador único de la partida.
     * @param email La dirección de correo electrónico del destinatario.
     * @return ResponseEntity con un mensaje de éxito o error.
     *         Si la partida no existe o no ha finalizado, retorna un error 400.
     *         Si ocurre un error durante el envío, retorna un error 500.
     */
    @PostMapping("/enviar-final/{partidaId}")
    public ResponseEntity<?> enviarCorreoFinal(@PathVariable int partidaId, @RequestParam String email) {
        try {
            Partida partida = partidaService.obtenerPartidaPorId(partidaId);
            if (partida == null) {
                return ResponseEntity.badRequest().body("La partida con ID " + partidaId + " no existe.");
            }
            if (!Boolean.TRUE.equals(partida.isFinalizada())) {
                return ResponseEntity.badRequest().body("La partida aún no ha finalizado.");
            }
            MyLinkedList<Jugador> jugadores = partidaService.obtenerJugadoresPartida(partida);
            mailService.enviarCorreoFinalPartida(email, partida, jugadores);
            return ResponseEntity.ok("Correo enviado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error enviando correo: " + e.getMessage());
        }
    }
}
