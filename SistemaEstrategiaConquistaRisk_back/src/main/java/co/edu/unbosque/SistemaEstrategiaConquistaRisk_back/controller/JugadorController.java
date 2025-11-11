package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto.JugadorDTO;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estructures.MyLinkedList;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.CharacterException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.ExceptionCheker;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.MailException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.NumberException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.SymbolException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception.TextException;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.EmailService;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.JugadorService;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.util.AESUtil;

@RestController
@RequestMapping("/jugadores")
@CrossOrigin(origins = "*")
public class JugadorController {

	@Autowired
	private JugadorService jugadorService;

	@Autowired
	private EmailService emailService;

	@PostMapping(path = "/crear")
	public ResponseEntity<String> crearJugador(
	        @RequestParam String nombre,
	        @RequestParam String correo,
	        @RequestParam String contrasena) {
	    try {
	        String nombreDec = AESUtil.decrypt(nombre);
	        String correoDec = AESUtil.decrypt(correo);
	        String contrasenaDec = AESUtil.decrypt(contrasena);

	        ExceptionCheker.checkerText(nombreDec);      
	        ExceptionCheker.checkerMail(correoDec);       
	        ExceptionCheker.checkerPasword(contrasenaDec);

	        JugadorDTO dto = new JugadorDTO();
	        dto.setNombre(nombreDec);
	        dto.setCorreo(correoDec);
	        dto.setContraseña(contrasenaDec);

	        int status = jugadorService.create(dto);

	        if (status == 0) {
	            emailService.enviarCorreoRegistroHTML(dto.getCorreo(), dto.getNombre());

	            return ResponseEntity.status(HttpStatus.CREATED)
	                    .body("201\nUsuario creado exitosamente");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                    .body("406\nEl nombre o correo ya están en uso");
	        }

	    } catch (MailException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nCorreo inválido. Use gmail, hotmail, outlook o unbosque.edu.co");
	    } catch (TextException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nEl nombre solo debe contener letras");
	    } catch (CharacterException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nLa contraseña debe tener al menos 8 caracteres");
	    } catch (NumberException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nLa contraseña debe contener al menos un número");
	    } catch (SymbolException e) {
	        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                .body("406\nLa contraseña debe tener al menos un símbolo especial");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("500\nError interno del servidor");
	    }
	}


	@GetMapping("/listar")
	public ResponseEntity<MyLinkedList<JugadorDTO>> listarJugadores() {
		MyLinkedList<JugadorDTO> jugadores = jugadorService.getAll();
		return ResponseEntity.ok(jugadores);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		int r = jugadorService.deleteById(id);
		if (r == 0)
			return ResponseEntity.ok("Jugador eliminado");
		return ResponseEntity.status(404).body("Jugador no encontrado");
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody JugadorDTO dto) {
		int r = jugadorService.updateById(id, dto);
		if (r == 0)
			return ResponseEntity.ok("Jugador actualizado");
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
	public ResponseEntity<?> agregarTropas(@PathVariable Long id, @PathVariable int cantidad) {
		jugadorService.agregarTropas(id, cantidad);
		return ResponseEntity.ok("Tropas agregadas");
	}

	// ✅ Quitar tropas
	@PutMapping("/{id}/quitar-tropas/{cantidad}")
	public ResponseEntity<?> quitarTropas(@PathVariable Long id, @PathVariable int cantidad) {
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
	public ResponseEntity<?> quitarCarta(@PathVariable Long id, @PathVariable int index) {
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
