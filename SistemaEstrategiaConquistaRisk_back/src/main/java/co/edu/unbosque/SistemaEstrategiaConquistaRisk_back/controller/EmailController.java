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
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.EmailService;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.PartidaService;

@RestController
@RequestMapping("/correo")
public class EmailController {
	//CONTROLLER TEMPORALHAY Q BORRARLO DESPUES

	@Autowired
	private EmailService mailService;

	@Autowired
	private PartidaService partidaService;

	@GetMapping("/test")
	public String testCorreo() {
		mailService.enviarCorreoRegistroHTML("smunozv@unbosque.edu.co", "Natalyyyyyy");

		return "Correo enviado";
	}

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

			// Obtener jugadores de la partida
			MyLinkedList<Jugador> jugadores = partidaService.obtenerJugadoresPartida(partida);

			// Enviar correo con PDF
			mailService.enviarCorreoFinalPartida(email, partida, jugadores);

			return ResponseEntity.ok("Correo enviado correctamente.");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Error enviando correo: " + e.getMessage());
		}
	}

}
